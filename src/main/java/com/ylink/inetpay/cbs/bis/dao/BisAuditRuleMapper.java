package com.ylink.inetpay.cbs.bis.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

import com.ylink.inetpay.common.core.constant.BISAuditType;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisAuditRule; 

/**
 * 
 * 类说明：
 * 实现BisAuditRule 的增删查改
 * 
 * <p>
 * 详细描述：
 *   
 * @author 1603254
 *   
 * CreateDate:  2016-11-15
 */
 @MybatisMapper("bisAuditRuleMapper")
public interface BisAuditRuleMapper {

	/**
	 * 方法说明： 
	 * 添加BisAuditRule
	 * @param  BisAuditRule				
	 */
	void addBisAuditRule(BisAuditRule bisAuditRule);
	

	/**
	 * 方法说明： 
	 * 查询BisAuditRule
	 * @param  BisAuditRule				
	 * @return List 	查询的结果集
	 */	
	List<BisAuditRule> queryBisAuditRule(BisAuditRule bisAuditRule);

	/**
	 * 方法说明： 
	 * 删除BisAuditRule
	 * @param  BisAuditRule的标识id				
	 */	
	void deleteBisAuditRule(List<String> list);
	
	
	/**
	 * 方法说明： 
	 * 更新BisAuditRule
	 * @param  BisAuditRule				
	 * @return List 	查询的结果集
	 */		
	void updateBisAuditRule(BisAuditRule bisAuditRule);
	
	/**
	 * 查询单条数据
	 * @param id
	 * @return
	 */
	public BisAuditRule details(String id);
	
	
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
	long isExistAcross(@Param("startRuleAmt")Long startRuleAmt, 
			@Param("endRuleAmt")Long endRuleAmt, 
			@Param("id")String id,
			@Param("auditType")BISAuditType auditType);
	
}