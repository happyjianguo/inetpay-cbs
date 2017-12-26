package com.ylink.inetpay.cbs.pay.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.cbs.pay.service.PayAllService;
import com.ylink.inetpay.cbs.pay.service.PayPaymentService;
import com.ylink.inetpay.common.core.constant.EOrderStatus;
import com.ylink.inetpay.common.core.dto.ReporHeadDto;
import com.ylink.inetpay.common.core.dto.ResultCode;
import com.ylink.inetpay.common.project.cbs.app.CbsPayPaymentAppService;
import com.ylink.inetpay.common.project.cbs.dto.pay.PayDataAuditDto;
import com.ylink.inetpay.common.project.cbs.dto.pay.PayMentContent;
import com.ylink.inetpay.common.project.cbs.dto.ucs.sec.UcsSecUserDto;
import com.ylink.inetpay.common.project.cbs.exception.CbsCheckedException;
import com.ylink.inetpay.common.project.cbs.vo.bis.ResponseMessage;
import com.ylink.inetpay.common.project.channel.dto.response.RefundRespPojo;
import com.ylink.inetpay.common.project.pay.dto.PayAllDto;
import com.ylink.inetpay.common.project.pay.dto.PayPaymentDto;
import com.ylink.inetpay.common.project.pay.dto.PayRefundDto;

@Service("cbsPayPaymentAppService")
public class CbsPayPaymentAppServiceImpl implements CbsPayPaymentAppService {
	
	private static Logger _loger = LoggerFactory.getLogger(CbsPayPaymentAppServiceImpl.class);
	@Autowired
	PayPaymentService payPaymentService;
	@Autowired
	PayAllService payAllService;
	
	
	@Override
	public PageData<PayPaymentDto> queryAllData(
			PageData<PayPaymentDto> pageDate, PayPaymentDto payPaymentDto) {
		return payPaymentService.queryAllData(pageDate, payPaymentDto);
	}
	@Override
	public PageData<PayPaymentDto> queryAuditAllData(
			PageData<PayPaymentDto> pageDate, PayPaymentDto payPaymentDto,String loginName) {
		return payPaymentService.queryAuditAllData(pageDate, payPaymentDto,loginName);
	}
	@Override
	public PageData<PayAllDto> queryDate(
			PageData<PayAllDto> pageDate, PayAllDto payAllDto) {
		return payAllService.queryDate(pageDate, payAllDto);
	}
	@Override
	public PayPaymentDto selectByBusiId(String busiId) {
		_loger.debug("查询订单信息:"+busiId);
		return payPaymentService.selectByBusiId(busiId);
	}
	@Override
	public ReporHeadDto reportSumData(PayPaymentDto payPaymentDto) {
		//所有总笔数和总金额
		ReporHeadDto reporAllDto = new ReporHeadDto();
		//所有成功的总笔数和总金额
		ReporHeadDto reporSuccDto = new ReporHeadDto();
		reporAllDto = payPaymentService.reportSumData(payPaymentDto);
		if(payPaymentDto.getOrderStatus()!=null){
			if(EOrderStatus.ORDER_STATUS_SUCCESS.getValue().equals(payPaymentDto.getOrderStatus().getValue())){
				reporAllDto.setSuccNum(reporAllDto.getAllNum());
				reporAllDto.setSuccAmt(reporAllDto.getAllAmt());
			}else{
				reporAllDto.setSuccNum(0l);
				reporAllDto.setSuccAmt(0l);
			}
		}else{
			payPaymentDto.setOrderStatus(EOrderStatus.ORDER_STATUS_SUCCESS);
			reporSuccDto = payPaymentService.reportSumData(payPaymentDto);
			if(reporSuccDto!=null){
				reporAllDto.setSuccNum(reporSuccDto.getAllNum());
				reporAllDto.setSuccAmt(reporSuccDto.getAllAmt());
			}
		}
		return reporAllDto;
	}
	@Override
	public boolean genRefund(PayMentContent payMentContent)throws CbsCheckedException {
		return payPaymentService.genRefund(payMentContent);

	}
	@Override
	public boolean checkPayData(String busiId) {
		return payPaymentService.checkPayData(busiId);

	}
	@Override
	public PayDataAuditDto selectByRefId(String refId) {
		return payPaymentService.selectByRefId(refId);
	}
	@Override
	public ResponseMessage auditPay(String auditType, String auditId, String renson, UcsSecUserDto userDto) {
		return payPaymentService.auditPay(auditType, auditId, renson, userDto);
	
	}
	@Override
	public PayPaymentDto selectByAuditDataId(String auditId) {
		return payPaymentService.selectByAuditDataId(auditId);
	}
	@Override
	public RefundRespPojo refund(String busiId) {
		return payPaymentService.refund(busiId);
	}
	@Override
	public ResultCode saveMerRefundAudiit(PayRefundDto payRefundDto) {
		return payPaymentService.saveMerRefundAudiit(payRefundDto);
	}
	@Override
	public PayDataAuditDto findById(String id) {
		return payPaymentService.findById(id);
	}
}
