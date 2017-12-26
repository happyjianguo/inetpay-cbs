package com.ylink.inetpay.cbs.bis.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ylink.inetpay.cbs.bis.dao.BisSchedJobQueueDtoMapper;
import com.ylink.inetpay.cbs.common.CbsConstants;
import com.ylink.inetpay.common.core.constant.EAutoManual;
import com.ylink.inetpay.common.core.constant.ESchedJobQueueStatus;
import com.ylink.inetpay.common.core.constant.ESchedPlanType;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisSchedJobQueueDto;

@Service("bisSchedJobQueueService")
@Transactional(value=CbsConstants.TX_MANAGER_BIS)
public class BisSchedJobQueueServiceImpl implements BisSchedJobQueueService {

	private static Logger _log = LoggerFactory.getLogger(BisSchedJobQueueServiceImpl.class);

	/** 默认队列最大执行次数 */
	public static final int DEFAULT_MAX_EXECUTE_COUNT = 100000;

	@Autowired
	BisSchedJobQueueDtoMapper bisSchedJobQueueDtoMapper;
	
	@Autowired
	BisSchedPlanDetailService jobPlanDetailService;

	/**
	 * 工作任务队列大小
	 */
	@Override
	public int getJobQueueSize() {
		return getJobQueueSize(null);
	}

	/**
	 * 工作任务队列大小
	 */
	@Override
	public int getJobQueueSize(ESchedPlanType planType) {
		List<BisSchedJobQueueDto> list = list(planType);
		if (list != null) {
			return list.size();
		}
		return 0;
	}

	/**
	 * 查询工作队列
	 */
	@Override
	public List<BisSchedJobQueueDto> list(ESchedPlanType planType) {
		return bisSchedJobQueueDtoMapper.list(planType);
	}

	/**
	 * 任务入队列
	 */
	@Override
	public void jobInQueue(ESchedPlanType planType, String refId, Date invokeTime,EAutoManual execType) {
		jobInQueue(planType, new String[]{refId}, invokeTime,execType);
	}

	/**
	 * 任务入队列
	 */
	@Override
	public void jobInQueue(ESchedPlanType planType,String[] refIds, Date invokeTime,EAutoManual execType) {
		List<BisSchedJobQueueDto> queues = new ArrayList<BisSchedJobQueueDto>();
		for (String refId : refIds) {
			BisSchedJobQueueDto exists = bisSchedJobQueueDtoMapper.getByPlanTypeRefId(planType, refId);
			if (exists != null) {
				_log.warn("任务[类型={}][关联ID={}]已存在队列！", planType.getValue(), refId);
				continue;
			}
			BisSchedJobQueueDto queue = new BisSchedJobQueueDto();
			queue.setId(queue.getIdentity());
			queue.setPlanType(planType);
			queue.setRefId(refId);
			queue.setMaxSendCount(DEFAULT_MAX_EXECUTE_COUNT);
			queue.setFailureCount(0);
			queue.setStatus(ESchedJobQueueStatus.NEW);
			queue.setInvokeTime(invokeTime);
			queue.setErrMsg("");
			queue.setDescription("");
			queue.setExecType(execType);
			queue.setCreateTime(new Date());
			queues.add(queue);
			
			//将执行明细计划状态改为执行中
			jobPlanDetailService.updateJobPlanDetailStatus(refId, ESchedJobQueueStatus.PROCESSING);
		}
		if (!queues.isEmpty())
			bisSchedJobQueueDtoMapper.saveList(queues);
	}

	@Override
	public void delete(String id) {
		bisSchedJobQueueDtoMapper.deleteByPrimaryKey(id);
	}

	@Override
	public void delete(List<BisSchedJobQueueDto> queueList) {
		if (queueList != null) {
			for (BisSchedJobQueueDto queue : queueList) {
				delete(queue.getId());
			}
		}
	}

	@Override
	public void update(BisSchedJobQueueDto queue) {
		String msg = queue.getErrMsg();
		if(msg != null && msg.length() > 150) {
			msg = msg.substring(0,150);
		}
		bisSchedJobQueueDtoMapper.updateByPrimaryKeySelective(queue);
	}

	@Override
	public void update(List<BisSchedJobQueueDto> queueList) {
		if (queueList != null) {
			for (BisSchedJobQueueDto queue : queueList) {
				update(queue);
			}
		}
	}

	@Override
	public List<BisSchedJobQueueDto> listWithLock(ESchedPlanType planType) {
		return bisSchedJobQueueDtoMapper.listWithLock(planType);
	}
}
