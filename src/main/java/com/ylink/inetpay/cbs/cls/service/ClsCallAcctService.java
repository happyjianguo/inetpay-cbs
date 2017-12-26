package com.ylink.inetpay.cbs.cls.service;

import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.common.project.cbs.exception.CbsCheckedException;
import com.ylink.inetpay.common.project.clear.dto.ClsCallAcct;
/**
 * 备付金调拨服务类
 * @author lyg
 *
 */
public interface ClsCallAcctService {
	/**
	 * 审核列表
	 * @param clsCallAcct
	 * @param pageData
	 * @return
	 * @throws CbsCheckedException
	 */
	public PageData<ClsCallAcct> pageAuditPageList(ClsCallAcct clsCallAcct,PageData<ClsCallAcct> pageData);
	/**
	 * 审核历史列表
	 * @param clsCallAcct
	 * @param pageData
	 * @return
	 * @throws CbsCheckedException
	 */
	public PageData<ClsCallAcct> pageAuditResultPageList(ClsCallAcct clsCallAcct,PageData<ClsCallAcct> pageData);
	/**
	 * 支付列表
	 * @param clsCallAcct
	 * @param pageData
	 * @return
	 * @throws CbsCheckedException
	 */
	public PageData<ClsCallAcct> pagePayPageList(ClsCallAcct clsCallAcct,PageData<ClsCallAcct> pageData);
	/**
	 * 详情
	 * @param id
	 * @return
	 * @throws CbsCheckedException
	 */
	public ClsCallAcct details(String id);
	/**
	 * 审核接口
	 * @param id
	 * @param eauditResult
	 * @throws CbsCheckedException
	 */
	public void auditPass(ClsCallAcct acctDto);
	/**
	 * 重新支付
	 * @param id
	 */
	public void againPay(String id);
	
	/**
	 * 查询某一账务日期的资金调度是否成功
	 * @param callDay
	 * @return true：表示调度成功，false：表示调度失败
	 */
	public boolean callAcctSuccessful(String callDay);
}
