package com.ylink.inetpay.cbs.chl.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.cbs.act.app.ActCheckStatusAppServiceImpl;
import com.ylink.inetpay.cbs.chl.service.ChlCheckStatusService;
import com.ylink.inetpay.common.project.cbs.app.ChlCheckStatusAppService;
import com.ylink.inetpay.common.project.cbs.exception.CbsCheckedException;
import com.ylink.inetpay.common.project.channel.app.CheckFileTaskAppService;
import com.ylink.inetpay.common.project.channel.dto.TbChlCheckfileRecordDto;
import com.ylink.inetpay.common.project.channel.exception.ChannelCheckedException;



/**
 * @类名称： ChannelCheckStatusService
 * @类描述：  资金渠道对账任务监控
 * @创建人： 1603254
 * @创建时间： 2016-5-30 上午9:43:04
 *
 * @修改人： 1603254
 * @操作时间： 2016-5-30 上午9:43:04
 * @操作原因： 
 * 
 */
@Service("chlCheckStatusAppService")
public class ChlCheckStatusAppServiceImpl implements ChlCheckStatusAppService{

	@Autowired
	private ChlCheckStatusService checkStatusService;
	@Autowired
	private CheckFileTaskAppService taskAppService;
	@Autowired
	@Qualifier("smsTaskExecutor")
	private TaskExecutor taskExecutor;
	
	
	private static final Logger logger=LoggerFactory.getLogger(ActCheckStatusAppServiceImpl.class);
	
	/**
	 * @方法描述:  查询资金渠道对账 文件的处理状态
	 * @作者： 1603254
	 * @日期： 2016-5-27-下午2:54:25
	 * @param pageData
	 * @param check
	 * @return 
	 * @返回类型： ClsBankCheck
	 */
	public PageData<TbChlCheckfileRecordDto> findBankCheck(PageData<TbChlCheckfileRecordDto> pageData,
			TbChlCheckfileRecordDto check){
		return checkStatusService.findBankCheck(pageData, check);
	}
	
 
	
	
	public TbChlCheckfileRecordDto reProcessFailRecord(String id){
		return checkStatusService.findById(id);
		
	}




	@Override
	public void updateStateById(TbChlCheckfileRecordDto check) throws CbsCheckedException {
		try {
			taskAppService.updateStateById(check);
		} catch (ChannelCheckedException e) {
			logger.error("修改渠道对账文件记录信息失败:"+e.getMessage());
		}
	}




	@Override
	public void greatUploadFileTaskByDate(final String checkDate) throws CbsCheckedException {
		taskExecutor.execute(new Runnable() {
			@Override
			public void run() {
				try {
					taskAppService.greatUploadFileTaskByDate(checkDate);
				} catch (ChannelCheckedException e) {
					logger.error("手动触发处理对账文件失败:"+e.getMessage());
				}
			}
		});
	}

}
