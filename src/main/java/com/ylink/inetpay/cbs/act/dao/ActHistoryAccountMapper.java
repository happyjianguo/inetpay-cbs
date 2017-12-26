package com.ylink.inetpay.cbs.act.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

import com.ylink.inetpay.common.project.account.dto.ActAccountDto;
import com.ylink.inetpay.common.project.account.dto.ActBillDto;
import com.ylink.inetpay.common.project.account.dto.ActHistoryAccountDto; 

/**
 * 
 * 类说明：
 * 实现ActHistoryAccountDto 的增删查改
 * 
 * <p>
 * 详细描述：
 *   
 * @author 1603254
 *   
 * CreateDate:  2016-11-17
 */
 @MybatisMapper("actHistoryAccountDtoDao")
public interface ActHistoryAccountMapper {

	/**
	 * 方法说明： 
	 * 添加ActHistoryAccountDto
	 * @param  ActHistoryAccountDto				
	 */
	void addActHistoryAccount(ActHistoryAccountDto actHistoryAccountDto);
	

	/**
	 * 方法说明： 
	 * 查询ActHistoryAccountDto
	 * @param  ActHistoryAccountDto				
	 * @return List 	查询的结果集
	 */	
	List<ActHistoryAccountDto> queryActHistoryAccount(ActHistoryAccountDto actHistoryAccountDto);

	/**
	 * 方法说明： 
	 * 删除ActHistoryAccountDto
	 * @param  ActHistoryAccountDto的标识id				
	 */	
	void deleteActHistoryAccount(List<String> list);
	
	
	/**
	 * 方法说明： 
	 * 更新ActHistoryAccountDto
	 * @param  ActHistoryAccountDto				
	 * @return List 	查询的结果集
	 */		
	void updateActHistoryAccount(ActHistoryAccountDto actHistoryAccountDto);
	
	/**
	 * 与科目表关联查询
	 * @param actHistoryAccountDto
	 * @return
	 */
	public List<ActHistoryAccountDto> getList(ActHistoryAccountDto actHistoryAccountDto);
	
	/**
	 * 根据账户号查询
	 * @param accountId
	 * @return
	 */
	public ActHistoryAccountDto selectByAccountId(String accountId);
	/**
	 * 根据账户编号和账户日期获取历史账户详情
	 * @param id
	 * @param accountDate
	 * @return
	 */
	ActHistoryAccountDto selectByAccountIdAndAccountDate(@Param("id")String id, @Param("accountDate")String accountDate);

	/**
	 * 根据账户编号和账户日期
	 * @param actAccountDtoList
	 * @param queryParam
	 * @return
	 */
	List<ActHistoryAccountDto> getCashAmount(@Param("actAccountDtoList")List<ActAccountDto> actAccountDtoList,@Param("queryParam") ActBillDto queryParam);

	List<ActHistoryAccountDto> findAccountBySubjectNo2AndAccountDate(@Param("subjectList")List<String> subjectList,@Param("lastAccountDate") String lastAccountDate);
	
}