package com.ylink.inetpay.cbs.bis.app;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.cbs.bis.service.BisAuditRuleService;
import com.ylink.inetpay.cbs.ucs.sec.service.UcsSecUserServiceImpl;
import com.ylink.inetpay.common.core.constant.BISAuditType;
import com.ylink.inetpay.common.project.cbs.app.BisAuditRuleAppService;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisAuditRule;

@Service("bisAuditRuleAppService")
public class BisAuditRuleAppServiceImpl implements BisAuditRuleAppService {
	@Autowired
	private BisAuditRuleService bisAuditRuleService;
	private static Logger _log = LoggerFactory.getLogger(UcsSecUserServiceImpl.class);

	@Override
	public void addBisAuditRule(BisAuditRule bisAuditRule) {
		try {
			bisAuditRuleService.addBisAuditRule(bisAuditRule);
		} catch (Exception e) {
			_log.error("新增审核规则异常："+e);
		}

	}

	@Override
	public PageData<BisAuditRule> queryBisAuditRule(BisAuditRule bisAuditRule, PageData<BisAuditRule> pageData) {
		return bisAuditRuleService.queryBisAuditRule(bisAuditRule, pageData);
	}

	@Override
	public void deleteBisAuditRule(List<String> list) {
		try {
			bisAuditRuleService.deleteBisAuditRule(list);
		} catch (Exception e) {
			_log.error("新增审核规则异常："+e);
		}

	}

	@Override
	public void updateBisAuditRule(BisAuditRule bisAuditRule) {
		try {
			bisAuditRuleService.updateBisAuditRule(bisAuditRule);
		} catch (Exception e) {
			_log.error("修改审核规则异常："+e);
		}

	}

	@Override
	public BisAuditRule details(String id) {
		return bisAuditRuleService.details(id);
	}

	@Override
	public int getNum(String value, long amount) {
		return bisAuditRuleService.getNum(value, amount);
	}

	@Override
	public List<BisAuditRule> findByAuditType(BISAuditType auditType) {
		return bisAuditRuleService.findByAuditType(auditType);
	}

	@Override
	public void delete(String id) {
		bisAuditRuleService.delete(id);
	}

	@Override
	public boolean isExistAcross(Long startRuleAmt, Long endRuleAmt, String id,BISAuditType auditType) {
		return bisAuditRuleService.isExistAcross(startRuleAmt,endRuleAmt,id,auditType);
	}

}
