package com.ylink.inetpay.cbs.act.app;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.cbs.act.service.ActAccountService;
import com.ylink.inetpay.common.core.constant.EUcsSecUserStatus;
import com.ylink.inetpay.common.core.dto.ReporHeadDto;
import com.ylink.inetpay.common.project.account.dto.ActAccountDto;
import com.ylink.inetpay.common.project.account.exception.EAccountErrorCode;
import com.ylink.inetpay.common.project.cbs.app.CbsActAccountAppService;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisSetCashFundAccountDto;
import com.ylink.inetpay.common.project.cbs.exception.CbsCheckedException;

@Service("cbsActAccountAppService")
public class ActAccountAppServiceImpl implements CbsActAccountAppService {
	@Autowired
	ActAccountService actAccountService;

	@Override
	public PageData<ActAccountDto> queryAllData(
			PageData<ActAccountDto> pageDate, ActAccountDto actAccountDto) {
		return actAccountService.queryAllData(pageDate, actAccountDto);
	}

	@Override
	public ActAccountDto selectByAccountId(String accountId) {
		return actAccountService.selectByAccountId(accountId);
	}
	@Override
	public ReporHeadDto reportSumData(ActAccountDto actAccountDto) {
		//所有总笔数和总金额
		ReporHeadDto reporAllDto = new ReporHeadDto();
		reporAllDto = actAccountService.reportSumData(actAccountDto);
		return reporAllDto;
	}
	@Override
	public List<ActAccountDto> getUserAccounts(String custId)
			throws CbsCheckedException {
		return actAccountService.getUserAccounts(custId);
	}

	@Override
	public PageData<ActAccountDto> pageList(ActAccountDto actAccountDto,
			PageData<ActAccountDto> pageData) throws CbsCheckedException {
		return actAccountService.pageList(actAccountDto, pageData);
	}

	@Override
	public void frozen(String id, EUcsSecUserStatus normal) throws CbsCheckedException{
		actAccountService.frozen(id,normal);
	}

    /**
     * 根据一户通编号和科目查询资金账号信息
     * @param custId
     * @param sub2
     * @return
     * @throws CbsCheckedException
     */
	@Override
	public ActAccountDto findByCustIdSubject2(String custId, String  sub2) throws CbsCheckedException {
		List<ActAccountDto> items = actAccountService.findAcctIdByCustIdAndSubjectNo2(custId, sub2);
		if (items == null || items.size() > 1) {
			throw new CbsCheckedException(EAccountErrorCode.CUST_SUBJECT2_NOT_FOUND.getValue(),EAccountErrorCode.CUST_SUBJECT2_NOT_FOUND.getDisplayName());
		}
		return items.get(0);
	}



    @Override
	public PageData<ActAccountDto> getBookList(ActAccountDto actAccountDto, PageData<ActAccountDto> pageData) {
		return actAccountService.getBookList(actAccountDto, pageData);
	}

	@Override
	public PageData<ActAccountDto> findPageWithFrozenable(PageData<ActAccountDto> pageData,
			ActAccountDto actAccountDto) {
		return actAccountService.findPageWithFrozenable(pageData, actAccountDto);
	}

	@Override
	public PageData<BisSetCashFundAccountDto> findPageWithSetCashFundable(PageData<BisSetCashFundAccountDto> pageData,
			BisSetCashFundAccountDto queryParam) {
		return actAccountService.findPageWithSetCashFundable(pageData,queryParam);
	}

	@Override
	public List<ActAccountDto> findListByCustId(String custId) throws CbsCheckedException {
		return actAccountService.findListByCustId(custId);
	}

	@Override
	public List<ActAccountDto> getActAccounts(String custId) {
		return actAccountService.getActAccounts(custId);
	}

	@Override
	public Map<String, BisSetCashFundAccountDto> findCashFundAccountMap(List<String> accountIds) {
		return actAccountService.findCashFundAccountMap(accountIds);
	}
	/**
	 * 获取冻结账户|（批量资金懂事校验使用）
	 */
	@Override
	public Map<String, ActAccountDto> findFrozenAccountMap(List<String> accountIds) {
		return actAccountService.findFrozenAccountMap(accountIds);
	}

	@Override
	public Map<String, ActAccountDto> findBJSAccountMapByAccountIds(List<String> accountIds) {
		return actAccountService.findBJSAccountMapByAccountIds(accountIds);
	}

	@Override
	public List<ActAccountDto> getAccountsByAccountIds(ArrayList<String> accountIdList) {
		return actAccountService.getAccountsByAccountIds(accountIdList);
	}

}
