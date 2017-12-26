package com.ylink.inetpay.cbs.act.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.ylink.inetpay.cbs.act.dao.ActAccountDtoMapper;
import com.ylink.inetpay.cbs.bis.dao.BisSetCashfundMapper;
import com.ylink.inetpay.cbs.bis.service.BisSysParamService;
import com.ylink.inetpay.cbs.ucs.sec.service.UcsSecUserServiceImpl;
import com.ylink.inetpay.common.core.constant.BISAuditStatus;
import com.ylink.inetpay.common.core.constant.EActBusiRefSubBusiType;
import com.ylink.inetpay.common.core.constant.EBookType;
import com.ylink.inetpay.common.core.constant.EUcsSecUserStatus;
import com.ylink.inetpay.common.core.dto.ReporHeadDto;
import com.ylink.inetpay.common.project.account.app.ActBookAppService;
import com.ylink.inetpay.common.project.account.dto.ActAccountDto;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisSetCashFundAccountDto;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisSetCashfund;
import com.ylink.inetpay.common.project.cbs.exception.CbsCheckedException;
import com.ylink.inetpay.common.project.cbs.exception.ECbsErrorCode;

@Service("actAccountService")
public class ActAccountServiceImpl implements ActAccountService {
	@Autowired
	ActAccountDtoMapper actAccountDtoMapper;
	@Autowired
	private ActBookAppService actBookAppService;
	@Autowired
	private BisSetCashfundMapper bisSetCashfundMapper;
	@Autowired
	private BisSysParamService bisSysParamService;
	private static Logger _log = LoggerFactory.getLogger(UcsSecUserServiceImpl.class);

	@Override
	public PageData<ActAccountDto> queryAllData(PageData<ActAccountDto> pageDate, ActAccountDto actAccountDto) {
		PageHelper.startPage(pageDate.getPageNumber(), pageDate.getPageSize());
		List<ActAccountDto> list = actAccountDtoMapper.queryAllData(actAccountDto);
		Page<ActAccountDto> page = (Page<ActAccountDto>) list;
		pageDate.setTotal(page.getTotal());
		pageDate.setRows(list);
		return pageDate;
	}

	@Override
	public ActAccountDto selectByAccountId(String accountId) {
		return actAccountDtoMapper.selectByAccountId(accountId);
	}

	@Override
	public List<ActAccountDto> getUserAccounts(String custId) throws CbsCheckedException {
		return actAccountDtoMapper.getUserAccounts(custId);
	}

	@Override
	public ReporHeadDto reportSumData(ActAccountDto actAccountDto) {
		return actAccountDtoMapper.reportSumData(actAccountDto);
	}

	@Override
	public PageData<ActAccountDto> pageList(ActAccountDto actAccountDto, PageData<ActAccountDto> pageData)
			throws CbsCheckedException {

		PageHelper.startPage(pageData.getPageNumber(), pageData.getPageSize());
		List<ActAccountDto> pageList = actAccountDtoMapper.getInnerAccount(actAccountDto);
		Page<ActAccountDto> page = (Page<ActAccountDto>) pageList;
		pageData.setRows(pageList);
		pageData.setTotal(page.getTotal());
		return pageData;
	}

	@Override
	public void frozen(String id, EUcsSecUserStatus normal) throws CbsCheckedException {
		try {
			if (EUcsSecUserStatus.DISABLED.getValue().equals(normal.getValue())) {
				actBookAppService.lockAccount(id);
			}
			if (EUcsSecUserStatus.NORMAL.getValue().equals(normal.getValue())) {
				actBookAppService.unlockAccount(id);
			}
		} catch (Exception e) {
			String errMsg = "账户【" + id + "】冻结/解冻失败：" + e;
			_log.error(errMsg);
			throw new CbsCheckedException(ECbsErrorCode.ACCESS_ACT.getValue(),
					ECbsErrorCode.ACCESS_ACT.getDisplayName());
		}
	}

	@Override
	public List<ActAccountDto> findAcctIdByCustIdAndSubjectNo2(String custId, String subjectNo2) {
		return actAccountDtoMapper.findAcctIdByCustIdAndSubjectNo2(custId, subjectNo2);
	}

	@Override
	public PageData<ActAccountDto> getBookList(ActAccountDto actAccountDto, PageData<ActAccountDto> pageData) {
		PageHelper.startPage(pageData.getPageNumber(), pageData.getPageSize());
		List<ActAccountDto> list = actAccountDtoMapper.getBookList(actAccountDto);
		Page<ActAccountDto> page = (Page<ActAccountDto>) list;
		pageData.setTotal(page.getTotal());
		pageData.setRows(list);
		return pageData;
	}

	@Override
	public PageData<ActAccountDto> findPageWithFrozenable(PageData<ActAccountDto> pageData,
			ActAccountDto actAccountDto) {
		String custId = bisSysParamService.getValue("CUST_ID");
		PageHelper.startPage(pageData.getPageNumber(), pageData.getPageSize());
		List<ActAccountDto> list = actAccountDtoMapper.listWithActBusiRefSubBusiType(actAccountDto,
				EActBusiRefSubBusiType.BALANCE_ACCOUNT, custId);
		Page<ActAccountDto> page = (Page<ActAccountDto>) list;
		pageData.setTotal(page.getTotal());
		pageData.setRows(list);
		return pageData;
	}

	@Override
	public PageData<BisSetCashFundAccountDto> findPageWithSetCashFundable(PageData<BisSetCashFundAccountDto> pageData,
			BisSetCashFundAccountDto queryParam) {
		String custId = bisSysParamService.getValue("CUST_ID");
		PageHelper.startPage(pageData.getPageNumber(), pageData.getPageSize());
		List<BisSetCashFundAccountDto> list = actAccountDtoMapper.findPageWithSetCashFundable(queryParam,
				EActBusiRefSubBusiType.BALANCE_ACCOUNT, EActBusiRefSubBusiType.DEPOSIT_ACCOUNT, custId);
		// 判断保证金账户是否已经存在待复核记录
		if (list != null && !list.isEmpty()) {
			ArrayList<String> cashfundAccountIds = new ArrayList<String>();
			for (BisSetCashFundAccountDto bisSetCashFundAccountDto : list) {
				cashfundAccountIds.add(bisSetCashFundAccountDto.getCashfundAccountId());
			}
			if (cashfundAccountIds != null && !cashfundAccountIds.isEmpty()) {
				List<BisSetCashfund> waitAudit = bisSetCashfundMapper.isWaitAudit(cashfundAccountIds,
						BISAuditStatus.WAIT_AUDIT);
				if (waitAudit != null && !waitAudit.isEmpty()) {
					HashMap<String, BisSetCashfund> waitAuditCashfundMap = new HashMap<String, BisSetCashfund>();
					for (BisSetCashfund bisSetCashfund : waitAudit) {
						waitAuditCashfundMap.put(bisSetCashfund.getCashfundAccountId(), bisSetCashfund);
					}
					for (BisSetCashFundAccountDto bisSetCashFundAccountDto : list) {
						BisSetCashfund waitAuditCashfund = waitAuditCashfundMap
								.get(bisSetCashFundAccountDto.getCashfundAccountId());
						if (waitAuditCashfund != null) {
							bisSetCashFundAccountDto.setIsWaitAudidt(true);
						} else {
							bisSetCashFundAccountDto.setIsWaitAudidt(false);
						}
					}
				}
			}
		}
		Page<BisSetCashFundAccountDto> page = (Page<BisSetCashFundAccountDto>) list;
		pageData.setTotal(page.getTotal());
		pageData.setRows(list);
		return pageData;
	}

	@Override
	public List<ActAccountDto> findListByCustId(String custId) {
		return actAccountDtoMapper.findListByCustId(custId);
	}

	@Override
	public List<ActAccountDto> findListByAccountIds(List<String> accountIds) {
		return actAccountDtoMapper.findListByAccountIds(accountIds);
	}

	@Override
	public ActAccountDto findFrozenAccountByCustId(String custId,String accountId) {
		return actAccountDtoMapper.findFrozenAccountByCustId(custId, EActBusiRefSubBusiType.FROZENABLE_ACCOUNT,accountId);
	}

	@Override
	public List<ActAccountDto> findAccountDtoByCustIdAndSbjNo2(String custId, List<String> subjectNoList) {
		return actAccountDtoMapper.findAccountDtoByCustIdAndSbjNo2(custId, subjectNoList);
	}

//	@Override
//	public List<ActAccountDto> findAccountDtoByCustId(String custId) {
//		return actAccountDtoMapper.findAccountDtoByCustId(custId);
//	}

	@Override
	public List<ActAccountDto> findByCustIdSubBusiType(String custId, String subAcctType, String busiType) {
		return actAccountDtoMapper.findByCustIdSubBusiType(custId, subAcctType, busiType);
	}

	@Override
	public ActAccountDto findAccountDtoByAccountId(String accountId) {
		return actAccountDtoMapper.findListByAccountId(accountId);
	}

	@Override
	public List<ActAccountDto> getActAccounts(String custId) {
		return actAccountDtoMapper.getActAccounts(custId);
	}

	@Override
	public Map<String, BisSetCashFundAccountDto> findCashFundAccountMap(List<String> accountIds) {
		_log.info("查询保证金账户记录开始");
		long queryStartTime = new Date().getTime();
		String custId = bisSysParamService.getValue("CUST_ID");
		List<BisSetCashFundAccountDto> bisSetCashFundAccountDtos = actAccountDtoMapper.findCashFundableByAccountIds(
				EActBusiRefSubBusiType.BALANCE_ACCOUNT, EActBusiRefSubBusiType.DEPOSIT_ACCOUNT, custId,
				accountIds);
		HashMap<String, BisSetCashFundAccountDto> cashfundAccountMap = new HashMap<String, BisSetCashFundAccountDto>();
		if (bisSetCashFundAccountDtos != null && !bisSetCashFundAccountDtos.isEmpty()) {
			for (BisSetCashFundAccountDto cashfundAccount : bisSetCashFundAccountDtos) {
				cashfundAccountMap.put(cashfundAccount.getCashfundAccountId(), cashfundAccount);
			}
		}
		_log.info("查询保证金账户记录结束，查询耗时【" + (new Date().getTime() - queryStartTime) + "】");
		return cashfundAccountMap;
	}

	@Override
	public Map<String, ActAccountDto> findFrozenAccountMap(List<String> accountIds) {
		String custId = bisSysParamService.getValue("CUST_ID");
		List<ActAccountDto> accountFrozenList = actAccountDtoMapper.listFrozenAccountByAccountIds(accountIds,
				EActBusiRefSubBusiType.BALANCE_ACCOUNT, custId);
		HashMap<String, ActAccountDto> accountFrozenMap = new HashMap<String, ActAccountDto>();
		if (accountFrozenList != null && !accountFrozenList.isEmpty()) {
			for (ActAccountDto frozenAccountDto : accountFrozenList) {
				accountFrozenMap.put(frozenAccountDto.getAccountId(), frozenAccountDto);
			}
		}
		return accountFrozenMap;
	}

	@Override
	public Map<String, ActAccountDto> findBJSAccountMapByAccountIds(List<String> accountIds) {
		List<ActAccountDto> actAccountList = actAccountDtoMapper.findBJSAccountMapByAccountIds(accountIds,EBookType.BJS);
		HashMap<String, ActAccountDto> actAccountMap = new HashMap<String, ActAccountDto>();
		if (actAccountList != null && !actAccountList.isEmpty()) {
			for (ActAccountDto dto : actAccountList) {
				actAccountMap.put(dto.getAccountId(), dto);
			}
		}
		return actAccountMap;
	}
	
	@Override
	public List<ActAccountDto> getAccountsByAccountIds(ArrayList<String> accountIdList) {
		return actAccountDtoMapper.getAccountsByAccountIds(accountIdList);
	}
}
