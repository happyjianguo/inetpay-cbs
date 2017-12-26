package com.ylink.inetpay.cbs.bis.service;

import java.util.List;

import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.common.core.constant.BISAuditType;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisAuditRule;

/**
 * 审核规则service
 * @author pst10
 *
 */
public interface BisAuditRuleService {
	/**
	 * 方法说明： 添加BisAuditRule
	 * 
	 * @param BisAuditRule
	 */
	void addBisAuditRule(BisAuditRule bisAuditRule);

	/**
	 * 方法说明： 查询BisAuditRule
	 * 
	 * @param BisAuditRule
	 * @return List 查询的结果集
	 */
	PageData<BisAuditRule> queryBisAuditRule(BisAuditRule bisAuditRule, PageData<BisAuditRule> pageData);

	/**
	 * 方法说明： 删除BisAuditRule
	 * 
	 * @param BisAuditRule的标识id
	 */
	void deleteBisAuditRule(List<String> list);

	/**
	 * 方法说明： 更新BisAuditRule
	 * 
	 * @param BisAuditRule
	 * @return List 查询的结果集
	 */
	void updateBisAuditRule(BisAuditRule bisAuditRule);
	
	/**
	 * 查询单条数据
	 * @param id
	 * @return
	 */
	public BisAuditRule details(String id);

	/**
	 * 获取需要审核次数(数据为空默认为1)
	 * @param vaule
	 * @param amount
	 * @return
	 */
	public int getNum(String value,long amount);
	/**
	 * @方法描述: 根据审核类型查询审核规则
	 * @作者： hinode
	 * @日期： 2016年11月24日-下午7:42:54
	 * @param auditType
	 * @return 
	 * @返回类型： List<BisAuditRule>
	 */
	public List<BisAuditRule> findByAuditType(BISAuditType auditType);
	
	
	/**
	 * 删除单条数据
	 * @param id
	 */
	public void delete(String id);
	/**
	 * 判断是否存在金额区间交叉
	 * @param startRuleAmt
	 * @param endRuleAmt
	 * @param id
	 * @return
	 */
	boolean isExistAcross(Long startRuleAmt, Long endRuleAmt, String id,BISAuditType auditType);
	/**
	 * 获取复核数
	 * @param bisAuditRule
	 * @return
	 */
	List<BisAuditRule> queryBisAuditRule(BisAuditRule bisAuditRule);
}
