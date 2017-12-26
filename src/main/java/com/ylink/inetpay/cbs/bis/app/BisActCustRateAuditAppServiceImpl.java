package com.ylink.inetpay.cbs.bis.app;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.cbs.bis.service.BisActCustRateAuditService;
import com.ylink.inetpay.common.core.constant.BISAuditStatus;
import com.ylink.inetpay.common.core.constant.EActCustRateStatus;
import com.ylink.inetpay.common.project.cbs.app.BisActCustRateAuditAppService;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisActCustRateAuditDto;
import com.ylink.inetpay.common.project.cbs.exception.CbsCheckedException;

@Service("bisActCustRateAuditAppService")
public class BisActCustRateAuditAppServiceImpl implements BisActCustRateAuditAppService {

	@Autowired
	BisActCustRateAuditService bisActCustRateAuditService;
	
	@Override
	public BisActCustRateAuditDto findById(String id) throws CbsCheckedException {
		return bisActCustRateAuditService.findById(id);
	}

	@Override
	public PageData<BisActCustRateAuditDto> findPage(PageData<BisActCustRateAuditDto> pageData,
			BisActCustRateAuditDto queryParam) throws CbsCheckedException {
		return bisActCustRateAuditService.findPage(pageData, queryParam);
	}

	@Override
	public void save(BisActCustRateAuditDto actCustRateAudit) throws CbsCheckedException {
		bisActCustRateAuditService.save(actCustRateAudit);
	}

	@Override
	public void auditPass(String auditor, String auditorName, String id,String reason) throws CbsCheckedException {
		bisActCustRateAuditService.auditPass(auditor, auditorName, id,reason);
	}

	@Override
	public void batchAuditPass(String auditor, String auditorName, List<String> idList,String reason) throws CbsCheckedException {
		bisActCustRateAuditService.batchAuditPass(auditor, auditorName, idList,reason);
	}

	@Override
	public void auditReject(String auditor, String auditorName, String reason,String id) throws CbsCheckedException {
		bisActCustRateAuditService.auditReject(auditor, auditorName,reason, id);
	}

	@Override
	public void batchAuditReject(String auditor, String auditorName,String reason, List<String> idList) throws CbsCheckedException {
		bisActCustRateAuditService.batchAuditReject(auditor, auditorName,reason, idList);
	}

	@Override
	public void cancel(String auditor,String auditorName,String id,String reason) throws CbsCheckedException {
		bisActCustRateAuditService.cancel(auditor,auditorName,id,reason);
	}

	@Override
	public void batchCancel(String auditor,String auditorName,List<String> idList,String reason) throws CbsCheckedException {
		bisActCustRateAuditService.batchCancel(auditor,auditorName,idList,reason);
	}

	@Override
	public long countWaitAuditWithAccountId(String accountId) throws CbsCheckedException {
		return bisActCustRateAuditService.countWaitAuditWithAccountId(accountId);
	}

	@Override
	public long countWaitAuditWithBankCardNo(String bankCardNo) throws CbsCheckedException {
		return bisActCustRateAuditService.countWaitAuditWithBankCardNo(bankCardNo);
	}

	@Override
	public List<BisActCustRateAuditDto> findWaitAuditWithAccountIds(List<String> accountIds)
			throws CbsCheckedException {
		return bisActCustRateAuditService.findWaitAuditWithAccountIds(accountIds);
	}

	@Override
	public void saveOrUpateCustRate(List<String> accountIds, BigDecimal rate, Date validTime, String operator,
			String operatorName) throws CbsCheckedException {
		bisActCustRateAuditService.saveOrUpateCustRate(accountIds, rate, validTime, operator, operatorName);
	}

	@Override
	public long getAuditNum(List<String> ids, BISAuditStatus auditPass, EActCustRateStatus uneffective) {
		return bisActCustRateAuditService.getAuditNum(ids,auditPass,uneffective);
	}

	/**
	 * 根据批次号获取客户利率map
	 * @param batchNo
	 * @return
	 */
	@Override
	public Map<String, BisActCustRateAuditDto> findCustRateMapByBatchNo(String batchNo) {
		return bisActCustRateAuditService.findCustRateMapByBatchNo(batchNo);
	}

	@Override
	public List<BisActCustRateAuditDto> list(BisActCustRateAuditDto queryParam) {
		return bisActCustRateAuditService.list(queryParam);
	}

	@Override
	public Map<String,BisActCustRateAuditDto> findExistWaitAuditMap(List<String> accountIds) {
		return bisActCustRateAuditService.findExistWaitAuditMap(accountIds);
	}
}
