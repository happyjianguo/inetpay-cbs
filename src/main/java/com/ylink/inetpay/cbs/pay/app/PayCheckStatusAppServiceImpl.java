package com.ylink.inetpay.cbs.pay.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.cbs.pay.service.PayCheckStatusService;
import com.ylink.inetpay.common.core.constant.EProcessStatus;
import com.ylink.inetpay.common.core.constant.EAutoManual;
import com.ylink.inetpay.common.core.constant.ESuccessFailDealing;
import com.ylink.inetpay.common.core.dto.SuccessFailDealingDto;
import com.ylink.inetpay.common.project.cbs.app.PayCheckStatusAppService;
import com.ylink.inetpay.common.project.pay.app.PayMerchantAppService;
import com.ylink.inetpay.common.project.pay.dto.PayCheckFileDto;

/**
 * @类名称： PayCheckStatusAppService
 * @类描述： 支付系统对账监控任务执行
 * @创建人： 1603254
 * @创建时间： 2016-5-27 上午9:37:28
 *
 * @修改人： 1603254
 * @操作时间： 2016-5-27 上午9:37:28
 * @操作原因： 
 * 
 */
@Service("payCheckStatusAppService")
public class PayCheckStatusAppServiceImpl implements PayCheckStatusAppService{


	@Autowired
	private PayCheckStatusService checkStatusService;
	@Autowired
	private PayMerchantAppService merchantAppService;
	private static final Logger logger=LoggerFactory.getLogger(PayCheckStatusAppServiceImpl.class);
	
	
	/**
	 * @方法描述:  查询监控状态  
	 * @作者： 1603254
	 * @日期： 2016-5-27-上午9:47:16
	 * @param pageDate
	 * @param check
	 * @return 
	 * @返回类型： PageData<PayCheckFileDto>
	*/
	public PageData<PayCheckFileDto> finCheckStatus(PageData<PayCheckFileDto> pageData,
			PayCheckFileDto check){
		return checkStatusService.finCheckStatus(pageData, check);
	}
	
	
	/**
	 * @方法描述: 重新执行失败的记录
	 * @作者： 1603254
	 * @日期： 2016-5-27-上午10:01:06
	 * @param id 
	 * @返回类型： void
	*/
	public String reProcessFailRecord(String id){
		PayCheckFileDto dto=checkStatusService.findById(id);
		if(dto == null){
			return "记录不存在";
		}
		try {
			if(dto.getDealStatus() == EProcessStatus.PROCESS_FAIL){
				SuccessFailDealingDto result=merchantAppService.accountFeeAndUploadFile(dto.getAccountDay(),EAutoManual.MANUAL);
				if(result.getSfd() == ESuccessFailDealing.FAIL){
					return  "处理失败";
				}
			}
		} catch (Exception e) {
			logger.error("method reProcessFailRecord error ",e);
			return "处理失败，系统异常";
		}
		return  "处理成功";
	}

}
