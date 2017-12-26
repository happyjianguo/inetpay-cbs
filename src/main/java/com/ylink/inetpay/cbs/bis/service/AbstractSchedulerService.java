package com.ylink.inetpay.cbs.bis.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.ylink.inetpay.cbs.bis.pojo.Result;
import com.ylink.inetpay.cbs.common.CbsConfig;
import com.ylink.inetpay.common.core.constant.EAutoManual;
import com.ylink.inetpay.common.core.constant.EBisExceptionLogNlevel;
import com.ylink.inetpay.common.core.constant.EBisExceptionLogType;
import com.ylink.inetpay.common.core.constant.EBisSmsSystem;
import com.ylink.inetpay.common.core.constant.ESchedJobQueueStatus;
import com.ylink.inetpay.common.core.constant.ESchedPlanType;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisExceptionLogDto;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisSchedJobQueueDto;
import com.ylink.inetpay.common.project.cbs.exception.CbsUncheckedException;
import com.ylink.inetpay.common.project.cbs.exception.ECbsErrorCode;

/**
 * 抽象任务业务执行类
 * 
 * @author LS
 * 
 */
public abstract class AbstractSchedulerService implements SchedulerService {

	private static final long serialVersionUID = -9096702523848816901L;

	/**
	 * 日志
	 */
	private static final Logger log = LoggerFactory.getLogger(AbstractSchedulerService.class);

	@Autowired
	private BisSchedJobQueueService jobQueueService;
	@Autowired
	private BisExceptionLogService bisExceptionLogService;
	/**
	 * 任务类型主键
	 */
	public abstract ESchedPlanType getJobTypeKey();

	/**
	 * 业务处理
	 */
	public abstract List<Result> doBusiness(List<BisSchedJobQueueDto> items);

	/**
	 * 任务执行完成后的处理
	 * 
	 * @description
	 * @param results
	 * @author LS
	 * @date 2013-9-4
	 */
	public abstract void postJobExecute(List<Result> results);

	@Override
	public synchronized void execute() {
		try {
			List<BisSchedJobQueueDto> list = jobQueueService.list(getJobTypeKey());
			executeJobQueue(list);
		} catch (Exception e) {
			BisExceptionLogDto bisExceptionLogDto = new BisExceptionLogDto();
			bisExceptionLogDto.setSystem(EBisSmsSystem.CBS);
			bisExceptionLogDto.setType(EBisExceptionLogType.BASE_SERVICE);
			bisExceptionLogDto.setNlevel(EBisExceptionLogNlevel.ERROR);
			bisExceptionLogDto.setContent("批处理任务"+getJobTypeKey().getDisplayName() +"出错："+ e.getMessage());
			bisExceptionLogDto.setAllpath(CbsConfig.getLogFullPath());
			bisExceptionLogService.saveLog(bisExceptionLogDto);
			log.error("定时任务异常：", e);
		}
	}

	/**
	 * 执行任务队列数据
	 */
	protected Collection<Result> executeJobQueue(List<BisSchedJobQueueDto> items) {
		if (items == null || items.isEmpty()) {
			return new ArrayList<Result>();
		}
		
		List<Result> results = doBusiness(items);// 业务处理
		if (results == null || results.isEmpty())
			throw new CbsUncheckedException(ECbsErrorCode.SYS_ERROR.getValue(),"批处理返回结果为空！");
		
		List<BisSchedJobQueueDto> successQueues = new ArrayList<BisSchedJobQueueDto>();// 成功队列 或 不再执行队列
		List<BisSchedJobQueueDto> updateQueues = new ArrayList<BisSchedJobQueueDto>();// 更新队列
		
		for (Result result : results) {
			BisSchedJobQueueDto item = result.getBisSchedJobQueueDto();
			if(result.getStatus() == ESchedJobQueueStatus.FAILURE ||
					result.getStatus() == ESchedJobQueueStatus.EXCEPTION){
				item.setFailureCount(item.getFailureCount() + 1);
			}
			item.setStatus(result.getStatus());
			item.setErrMsg(result.getMessage());

			if(item.getStatus() == ESchedJobQueueStatus.SUCCESS || 
					item.getStatus() == ESchedJobQueueStatus.FAILURE || item.isDeletable()) {
				if(item.isDeletable()) {
					result.setMessage("执行失败次数超过了最大的允许执行次数");
					result.setStatus(ESchedJobQueueStatus.FAILURE);
				}
				successQueues.add(item);
			} else {
				item.setExecType(EAutoManual.AUTO);
				updateQueues.add(item);
			}
		}

		// 成功或执行达到最大调用次数，从队列中删除
		if (!successQueues.isEmpty())
			jobQueueService.delete(successQueues);

		if (!updateQueues.isEmpty())
			jobQueueService.update(updateQueues);

		if (!results.isEmpty())
			postJobExecute(results);

		return results;
	}

}
