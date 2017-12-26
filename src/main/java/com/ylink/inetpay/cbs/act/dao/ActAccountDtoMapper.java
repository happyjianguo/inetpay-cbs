package com.ylink.inetpay.cbs.act.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

import com.ylink.inetpay.common.core.constant.EActBusiRefSubBusiType;
import com.ylink.inetpay.common.core.constant.EBookType;
import com.ylink.inetpay.common.core.dto.ReporHeadDto;
import com.ylink.inetpay.common.project.account.dto.ActAccountDto;
import com.ylink.inetpay.common.project.account.dto.ActBillDto;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisSetCashFundAccountDto;

@MybatisMapper("actAccountDtoMapper")
public interface ActAccountDtoMapper {
	int deleteByPrimaryKey(String id);

	int insert(ActAccountDto record);

	int insertSelective(ActAccountDto record);

	ActAccountDto selectByPrimaryKey(String id);

	void updateByPrimaryKeySelective(ActAccountDto record);

	int updateByPrimaryKey(ActAccountDto record);

	/**
	 * 根据参数查询所有用户的账户资金信息数据
	 * 
	 * @param ActAccountDto
	 * @return
	 */
	List<ActAccountDto> queryAllData(ActAccountDto actAccountDto);

	/**
	 * 根据账户ID查询
	 * 
	 * @param accountId
	 * @return
	 */
	ActAccountDto selectByAccountId(String accountId);
	/**
	 * 根据用户编号获取账户信息
	 * @param custId
	 * @return
	 */
	List<ActAccountDto> getUserAccounts(String custId);
	
	/**
	 * 根据一户通编号获取资金账户信息
	 * @param custId
	 * @return
	 */
	List<ActAccountDto> getActAccounts(String custId);
	/**
     * 统计总笔数与总金额
     * @param orderStatus
     * @return
     */
    ReporHeadDto reportSumData(ActAccountDto actAccountDto);
    /**
     * 分页查询账户信息
     * @param actAccountDto
     * @return
     */
	List<ActAccountDto> pageList(ActAccountDto actAccountDto);
	/**
	 * 联表获取账户以及二级科目名称
	 * @param actAccountDto
	 * @return
	 */
	List<ActAccountDto> getInnerAccount(ActAccountDto actAccountDto);
	
	List<ActAccountDto> findAcctIdByCustIdAndSubjectNo2(@Param(value = "custId") String custId,
			@Param(value = "subjectNo2") String subjectNo2);
	
	/**
	 * 与科目表关联查询
	 * @param actAccountDto
	 * @return
	 */
	List<ActAccountDto> getBookList(ActAccountDto actAccountDto);
	
	/**
	 * 获取指定业务类型的账户列表
	 * @param actAccountDto
	 * @param actBusiRefSubBusiType
	 * @return
	 */
	List<ActAccountDto> listWithActBusiRefSubBusiType(@Param("queryParam")ActAccountDto actAccountDto,
			@Param("actBusiRefSubBusiType")EActBusiRefSubBusiType actBusiRefSubBusiType,@Param("custId")String custId);
	/**
	 * 获取可以设置保证金额的账户
	 * @param queryParam
	 * @param balanceAccount
	 * @param depositAccount
	 * @return
	 */
	List<BisSetCashFundAccountDto> findPageWithSetCashFundable(@Param("dto")BisSetCashFundAccountDto queryParam,
			@Param("balanceAccount")EActBusiRefSubBusiType balanceAccount,
			@Param("cashFundAccount")EActBusiRefSubBusiType depositAccount,
			@Param("custId")String custId);

	List<ActAccountDto> findListByCustId(String custId);
	
	List<ActAccountDto> findListByAccountIds(@Param("accountIds")List<String> accountIds);
	/**
	 * 根据账户编号和三级科目获取冻结账户
	 * @param custId
	 * @param frozenableAccount
	 * @return
	 */
	ActAccountDto findFrozenAccountByCustId(@Param("custId")String custId, 
			@Param("frozenableAccount")EActBusiRefSubBusiType frozenableAccount,
			@Param("accountId")String accountId);
	/**
	 * 根据客户编号和二级科目获取账户数据
	 * @param custId
	 * @param subjectNoList
	 * @return
	 */
	List<ActAccountDto> findAccountDtoByCustIdAndSbjNo2(@Param(value = "custId") String custId,
			@Param("subjectNoList")List<String> subjectNoList);
	/**
	 * 根据客户编号获取账户数据
	 * @param custId
	 * @param subjectNoList
	 * @return
	 */
//	List<ActAccountDto> findAccountDtoByCustId(@Param(value = "custId") String custId);
	/**
	 * 根据一户通编号,一级科目，科目类别，查询资金账号信息
	 * 
	 * @param custId
	 * @param sub2
	 * @param busiType
	 * @return
	 */
	List<ActAccountDto> findByCustIdSubBusiType(@Param(value = "custId") String custId,
			@Param(value = "subAcctType") String subAcctType,
			@Param(value = "busiType") String busiType);
	/**
	 * 根据账户编号获取账户信息
	 * @param accountId
	 * @return
	 */
	ActAccountDto findListByAccountId(String accountId);
	/**
	 * 获取保证金账户根据科目编号
	 * @param queryParam
	 * @param balanceAccount
	 * @param depositAccount
	 * @param custId
	 * @param accountIds
	 * @return
	 */
	List<BisSetCashFundAccountDto> findCashFundableByAccountIds(
			@Param("balanceAccount")EActBusiRefSubBusiType balanceAccount, 
			@Param("cashFundAccount")EActBusiRefSubBusiType depositAccount,
			@Param("custId")String custId,@Param("accountIds")List<String> accountIds);
	/**
	 * 根据三级科目获取消费备付金账户
	 * @param accountIds
	 * @param balanceAccount
	 * @param custId
	 * @return
	 */
	List<ActAccountDto> listFrozenAccountByAccountIds(@Param("accountIds")List<String> accountIds, 
			@Param("balanceAccount")EActBusiRefSubBusiType balanceAccount,@Param("custId")String custId);
	/**
	 * 根据账户编号获取保交所账本
	 * @param accountIds
	 * @param bjs
	 * @return
	 */
	List<ActAccountDto> findBJSAccountMapByAccountIds(@Param("accountIds")List<String> accountIds, @Param("bjs")EBookType bjs);

	/**
	 * 根据accountIds获取对应二级科目的名称
	 * @param accountIds
	 * @return
	 */
	List<ActAccountDto> getAccountsByAccountIds(List<String> accountIds);
 

	/**
	 * 根据一户通编号账户大类业务类型查询账户信息
	 * @param custId 一户通号
	 * @param acctTypeNo 账户大类  基础类、资管类、参与人基础等
	 * @param busiType 业务类型  消费备付金等
	 * @return
	 */
	List<ActAccountDto> findByCustIdAndAcctType(@Param(value = "custId") String custId,
			@Param(value = "acctTypeNo") String acctTypeNo, @Param(value = "busiType") String busiType);
	/**
	 * 查询账号表
	 * @param custId 
	 * @return
	 */
	List<ActAccountDto> findActAccountDto(@Param("custId")String custId);
	 
	
}
