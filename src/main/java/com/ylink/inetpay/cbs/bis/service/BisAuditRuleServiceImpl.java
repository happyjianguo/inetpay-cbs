package com.ylink.inetpay.cbs.bis.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.ylink.inetpay.cbs.bis.dao.BisAuditRuleMapper;
import com.ylink.inetpay.common.core.constant.BISAuditType;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisAuditRule;

@Service("bisAuditRuleService")
public class BisAuditRuleServiceImpl implements BisAuditRuleService {
	@Autowired
	BisAuditRuleMapper bisAuditRuleMapper;

	@Override
	public void addBisAuditRule(BisAuditRule bisAuditRule) {
		bisAuditRuleMapper.addBisAuditRule(bisAuditRule);

	}

	@Override
	public PageData<BisAuditRule> queryBisAuditRule(BisAuditRule bisAuditRule, PageData<BisAuditRule> pageData) {
		PageHelper.startPage(pageData.getPageNumber(), pageData.getPageSize());
		List<BisAuditRule> list = bisAuditRuleMapper.queryBisAuditRule(bisAuditRule);
		Page<BisAuditRule> page = (Page<BisAuditRule>) list;
		pageData.setTotal(page.getTotal());
		pageData.setRows(list);
		return pageData;
	}

	@Override
	public void deleteBisAuditRule(List<String> list) {
		bisAuditRuleMapper.deleteBisAuditRule(list);

	}

	@Override
	public void updateBisAuditRule(BisAuditRule bisAuditRule) {
		bisAuditRuleMapper.updateBisAuditRule(bisAuditRule);

	}

	@Override
	public BisAuditRule details(String id) {
		return bisAuditRuleMapper.details(id);
	}

	@Override
	public int getNum(String value, long amount) {
		BisAuditRule bisAuditRule = new BisAuditRule();
		bisAuditRule.setAuditType(BISAuditType.getEnum(value));
		List<BisAuditRule> bisAuditRuleList = bisAuditRuleMapper.queryBisAuditRule(bisAuditRule);
		if (bisAuditRuleList == null || bisAuditRuleList.size() <1) {
			return 0;
		}
		for (BisAuditRule bisAuditRulex : bisAuditRuleList) {
			if (bisAuditRulex.getStartAudit() <= amount && bisAuditRulex.getEndAudit() > amount) {
				return bisAuditRulex.getCheckNum();
			}
		}
		return 0;
	}
	
	public List<BisAuditRule> findByAuditType(BISAuditType auditType){
		BisAuditRule bisAuditRule = new BisAuditRule();
		bisAuditRule.setAuditType(auditType);
		return bisAuditRuleMapper.queryBisAuditRule(bisAuditRule);
	}

	@Override
	public void delete(String id) {
		bisAuditRuleMapper.delete(id);
	}

	@Override
	public boolean isExistAcross(Long startRuleAmt, Long endRuleAmt, String id,BISAuditType auditType) {
		//如果是修改，id不为空。需要排除id
		long ruleNum=bisAuditRuleMapper.isExistAcross(startRuleAmt,endRuleAmt,id,auditType);
		return ruleNum>0;
	}

	@Override
	public List<BisAuditRule> queryBisAuditRule(BisAuditRule bisAuditRule) {
		return bisAuditRuleMapper.queryBisAuditRule(bisAuditRule);
	}

}
