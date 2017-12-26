package com.ylink.inetpay.cbs.act.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.common.core.constant.EUcsSecUserStatus;
import com.ylink.inetpay.common.core.dto.ReporHeadDto;
import com.ylink.inetpay.common.project.account.dto.ActAccountDto;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisSetCashFundAccountDto;
import com.ylink.inetpay.common.project.cbs.exception.CbsCheckedException;

public interface ActAccountService {
	/**
	 * 根据参数查询所有用户的账户资金信息数据
	 * 
	 * @param pageDate
	 * @param ActAccountDto
	 * @return
	 */
	PageData<ActAccountDto> queryAllData(PageData<ActAccountDto> pageDate,
			ActAccountDto actAccountDto);
	/**
	 * 根据账户ID查询
	 * 
	 * @param accountId
	 * @return
	 */
	ActAccountDto selectByAccountId(String accountId);
	/**
	 * 查询用户账户（根据用户编号）
	 * @param custId
	 * @return
	 * @throws CbsCheckedException
	 */
	public List<ActAccountDto> getUserAccounts(String custId)throws CbsCheckedException;
	/**
     * 统计总笔数与总金额
     * @param orderStatus
     * @return
     */
    ReporHeadDto reportSumData(ActAccountDto actAccountDto);
    /**
     * 分页查询所有的账户信息
     * @return
     * @throws CbsCheckedException
     */
    public PageData<ActAccountDto> pageList(ActAccountDto actAccountDto,PageData<ActAccountDto> pageData)throws CbsCheckedException;
	/**
	 * 冻结/解冻账户
	 * @param id
	 * @param normal
	 */
    void frozen(String id, EUcsSecUserStatus normal)throws CbsCheckedException;
    

	public List<ActAccountDto> findAcctIdByCustIdAndSubjectNo2(String custId,
			String subjectNo2) ;
	/**
	 * 根据一户通编号,一级科目，科目类别，查询资金账号信息
	 * 
	 * @param custId
	 * @param sub2
	 * @param busiType
	 * @return
	 */
	List<ActAccountDto> findByCustIdSubBusiType(String custId, String sub2, String busiType);
	
	/**
	 * 与科目表关联查询
	 * @param actAccountDto
	 * @return
	 */
	public PageData<ActAccountDto> getBookList(ActAccountDto actAccountDto,PageData<ActAccountDto> pageData);

	public PageData<ActAccountDto> findPageWithFrozenable(PageData<ActAccountDto> pageData,
			ActAccountDto actAccountDto);
	
	public PageData<BisSetCashFundAccountDto> findPageWithSetCashFundable(PageData<BisSetCashFundAccountDto> pageData,
			BisSetCashFundAccountDto queryParam);
	
	public List<ActAccountDto> findListByCustId(String custId);
	
	public List<ActAccountDto> findListByAccountIds(List<String> accountIds);
	/**
	 * 根据账户编号获取冻结备付金
	 * @param custId
	 * @return
	 */
	ActAccountDto findFrozenAccountByCustId(String custId,String accountId);
	/**
	 * 根据客户编号和二级科目获取账户数据
	 * @param custId
	 * @param subjectNoList
	 * @return
	 */
	List<ActAccountDto> findAccountDtoByCustIdAndSbjNo2(String custId,
			List<String> subjectNoList);
	/**
	 * 根据客户编号获取账户数据
	 * @param custId
	 * @param subjectNoList
	 * @return
	 */
//	List<ActAccountDto> findAccountDtoByCustId(String custId);
	/**
	 * 根据账户编号获取账户信息
	 * @param accountId
	 * @return
	 */
	ActAccountDto findAccountDtoByAccountId(String accountId);
	
	/**
	 * 根据一户通编号查询
	 * @param custId
	 * @return
	 */
	List<ActAccountDto> getActAccounts(String custId);
	/**
	 * 获取保证金账户（批量保证金设置校验使用）
	 * @return
	 */
	Map<String, BisSetCashFundAccountDto> findCashFundAccountMap(List<String> accountIds);
	/**
	 * 获取冻结账户|（批量资金懂事校验使用）
	 * @return
	 */
	Map<String, ActAccountDto> findFrozenAccountMap(List<String> accountIds);
	/**
	 * 根据账户获取所有保交所账户集合
	 * @return
	 */
	Map<String, ActAccountDto> findBJSAccountMapByAccountIds(List<String> accountIds);
	List<ActAccountDto> getAccountsByAccountIds(ArrayList<String> accountIdList);
}
