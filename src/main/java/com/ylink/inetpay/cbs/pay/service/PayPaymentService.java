package com.ylink.inetpay.cbs.pay.service;

import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.common.core.dto.ReporHeadDto;
import com.ylink.inetpay.common.core.dto.ResultCode;
import com.ylink.inetpay.common.project.cbs.dto.pay.PayDataAuditDto;
import com.ylink.inetpay.common.project.cbs.dto.pay.PayMentContent;
import com.ylink.inetpay.common.project.cbs.dto.ucs.sec.UcsSecUserDto;
import com.ylink.inetpay.common.project.cbs.vo.bis.ResponseMessage;
import com.ylink.inetpay.common.project.channel.dto.response.RefundRespPojo;
import com.ylink.inetpay.common.project.pay.dto.PayPaymentDto;
import com.ylink.inetpay.common.project.pay.dto.PayRefundDto;

public interface PayPaymentService {
	/**
	 * 查询支付订单数据
	 * 
	 * @param pageDate
	 * @param PayPaymentDto
	 * @return
	 */
	PageData<PayPaymentDto> queryAllData(PageData<PayPaymentDto> pageDate,
			PayPaymentDto payPaymentDto);
	/**
	 * 查询支付订单数据
	 * 
	 * @param pageDate
	 * @param PayPaymentDto
	 * @return
	 */
	PageData<PayPaymentDto> queryAuditAllData(PageData<PayPaymentDto> pageDate,
			PayPaymentDto payPaymentDto,String loginName);
	/**
	 * 根据平台业务订单号查询
	 * 
	 * @param busiId
	 * @return
	 */
	PayPaymentDto selectByBusiId(String busiId);
	 /**
     * 统计总笔数与总金额
     * @param orderStatus
     * @return
     */
    ReporHeadDto reportSumData(PayPaymentDto payPaymentDto);
    
    boolean genRefund(PayMentContent payMentContent);
    
    /**
     * 是否有订单审核数据
     * @param busiId
     * @return
     */
    boolean checkPayData(String busiId);
    /**
  	 * 根据平台业务订单号查询
  	 * 
  	 * @param refId
  	 * @return
  	 */
  	PayDataAuditDto selectByRefId(String refId);
  	 /**
     * 根据auditId查询
     * @param auditId
     * @return
     */
    PayPaymentDto selectByAuditDataId(String auditId);
  	/**
	 * 审核操作
	 * @param yesOrNo审核通过/拒绝
	 * @param auditId审核表
	 * @param renson原因
	 * @return
	 */
	public ResponseMessage auditPay(String auditType,String auditId,String renson, UcsSecUserDto userDto);
	/**
     * 退款
     * @param refundPojo
     * @return
     */
	RefundRespPojo refund(String busiId);
    /**
     * 保存商户退款订单
     * @param payRefundDto
     * @return
     */
	ResultCode saveMerRefundAudiit(PayRefundDto payRefundDto);
	/**
	 * 根据id获取复核记录
	 * @param id
	 * @return
	 */
	PayDataAuditDto findById(String id);

}
