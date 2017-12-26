package com.ylink.inetpay.cbs.bis.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.ylinkpay.framework.core.model.PageData;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.ylink.eu.util.tools.DateUtil;
import com.ylink.inetpay.cbs.act.service.ActAccountService;
import com.ylink.inetpay.cbs.act.service.ActaccountDateService;
import com.ylink.inetpay.cbs.bis.dao.BisAuditDtoMapper;
import com.ylink.inetpay.cbs.bis.dao.BisSetCashFundOperDtoMapper;
import com.ylink.inetpay.cbs.bis.dao.BisSetCashfundMapper;
import com.ylink.inetpay.cbs.bis.dao.BisTransferHandleDao;
import com.ylink.inetpay.cbs.common.CbsConstants;
import com.ylink.inetpay.cbs.util.CbsExceptionLogDtoUtils;
import com.ylink.inetpay.common.core.constant.AuditTypeEnum;
import com.ylink.inetpay.common.core.constant.BISAuditStatus;
import com.ylink.inetpay.common.core.constant.BISAuditType;
import com.ylink.inetpay.common.core.constant.EAppleType;
import com.ylink.inetpay.common.core.constant.EBisExceptionLogNlevel;
import com.ylink.inetpay.common.core.constant.EBisExceptionLogType;
import com.ylink.inetpay.common.core.constant.EPayAdjustType;
import com.ylink.inetpay.common.core.constant.EPocessStatusEnum;
import com.ylink.inetpay.common.core.constant.EResultCode;
import com.ylink.inetpay.common.core.constant.EffectiveStatusEnum;
import com.ylink.inetpay.common.core.constant.PayStatusEnum;
import com.ylink.inetpay.common.core.dto.ResultCodeDto;
import com.ylink.inetpay.common.core.util.DateUtils;
import com.ylink.inetpay.common.core.util.ExceptionProcUtil;
import com.ylink.inetpay.common.core.util.StringUtils;
import com.ylink.inetpay.common.project.account.dto.ActAccountDto;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisAuditDto;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisSetCashFundOperDto;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisSetCashfund;
import com.ylink.inetpay.common.project.cbs.dto.ucs.sec.UcsSecUserDto;
import com.ylink.inetpay.common.project.cbs.exception.CbsCheckedException;
import com.ylink.inetpay.common.project.pay.app.PayAccountAdjustAppService;
import com.ylink.inetpay.common.project.pay.dto.PayAccountAdjustDto;

@Service("bisSetCashFundService")
@Transactional(value=CbsConstants.TX_MANAGER_BIS)
public class BisSetCashFundServiceImpl implements BisSetCashFundService {
	@Autowired
	private BisSetCashfundMapper bisSetCashfundMapper;
	@Autowired
	private BisAuditDtoMapper bisAuditDtoMapper;
	@Autowired
	private ActAccountService actAccountService;
	@Autowired
	private PayAccountAdjustAppService paySystemPayAccountAdjustAppService;
	@Autowired
	private ActaccountDateService actaccountDateService;
	@Autowired 
	private BisExceptionLogService bisExceptionLogService;
	@Autowired 
	private BisTransferHandleDao bisTransferHandleDao;
	@Autowired 
	private BisSetCashFundOperDtoMapper bisSetCashFundOperDtoMapper;
	@Autowired
	private BisBatchExpService bisBatchExpService;
	private static Logger _log = LoggerFactory.getLogger(BisSetCashFundServiceImpl.class);
	@Override
	public PageData<BisSetCashfund> findCheckStatus(PageData<BisSetCashfund> pageData, BisSetCashfund queryParam) {
			PageHelper.startPage(pageData.getPageNumber(),pageData.getPageSize());
			List<BisSetCashfund> findListPage=bisSetCashfundMapper.findListPage(queryParam);
			findSubjectName(findListPage);
			Page<BisSetCashfund> page=(Page<BisSetCashfund>) findListPage;
			pageData.setTotal(page.getTotal());
			pageData.setRows(findListPage);
			return pageData;
	}
	
	/**
	 * 获取list记录对象的科目名称
	 * @param bisSetCashfunds
	 */
	public void findSubjectName(List<BisSetCashfund> bisSetCashfunds){
		if(bisSetCashfunds!=null && !bisSetCashfunds.isEmpty()){
			ArrayList<String> accountIds = new ArrayList<String>();
			for (BisSetCashfund bisSetCashfund : bisSetCashfunds) {
				bisSetCashfund.setEffectiveDate(DateUtils.stringToString(bisSetCashfund.getEffectiveDate()));
				accountIds.add(bisSetCashfund.getCashfundAccountId());
			}
			List<ActAccountDto> accounts = actAccountService.findListByAccountIds(accountIds);
			HashMap<String, String> accountMap = new HashMap<String,String>();
			for (int i = 0; i < accounts.size(); i++) {
				ActAccountDto actAccountDto = accounts.get(i);
				accountMap.put(actAccountDto.getAccountId(), actAccountDto.getSubjectNo2Name());
			}
			for (BisSetCashfund bisSetCashfund : bisSetCashfunds) {
				bisSetCashfund.setCashfundSubjectName(accountMap.get(bisSetCashfund.getCashfundAccountId()));
			}
		}
	}

	@Override
	public BisSetCashfund getView(String id) {
		BisSetCashfund bisSetCashfund =bisSetCashfundMapper.findBeanDto(id);
		ActAccountDto account = actAccountService.findAccountDtoByAccountId(bisSetCashfund.getCashfundAccountId());
		bisSetCashfund.setCashfundSubjectName(account!=null?account.getSubjectNo2Name():"");
		List<BisAuditDto> auditDtos = bisAuditDtoMapper.findListBybusId(id, BISAuditType.CASH_FUND);
		if(bisSetCashfund!=null){
			bisSetCashfund.setAuditDtos(auditDtos);
		}
		return bisSetCashfund;
	}

	@Override
	public List<String> batchSetCashFund(List<String> balanceAccountIds,List<String> cashfundAccountIds, UcsSecUserDto userDto,Long setAmount,String effectiveDate,String remarks) throws CbsCheckedException {
		ArrayList<String> errorMsgs = new ArrayList<String>();
		if(cashfundAccountIds!=null && !cashfundAccountIds.isEmpty()){
			ArrayList<BisSetCashfund> setCashFunds = new ArrayList<BisSetCashfund>();
			//判断保证金账户是否已经存在待复核记录
			List<BisSetCashfund> waitAudit = bisSetCashfundMapper.isWaitAudit(cashfundAccountIds,BISAuditStatus.WAIT_AUDIT);
			if(waitAudit!=null && !waitAudit.isEmpty()){
				HashMap<String, BisSetCashfund> waitAuditCashfundMap = new HashMap<String,BisSetCashfund>();
				for (BisSetCashfund bisSetCashfund : waitAudit) {
					waitAuditCashfundMap.put(bisSetCashfund.getCashfundAccountId(), bisSetCashfund);
				}
				for (String cashfundId : cashfundAccountIds) {
					BisSetCashfund waitAuditCashfund = waitAuditCashfundMap.get(cashfundId);
					if(waitAuditCashfund!=null){
						errorMsgs.add("余额账户编号："+waitAuditCashfund.getBalanceAccountId()+";保证金账户编号："+waitAuditCashfund.getCashfundAccountId()+";已经存在待复核记录");
						continue;
					}
				}
			}
			List<ActAccountDto> cashfundAccountList = actAccountService.findListByAccountIds(cashfundAccountIds);
			if(cashfundAccountList!=null && !cashfundAccountList.isEmpty()){
				HashMap<String, ActAccountDto> cashfundAccountMap = new HashMap<String,ActAccountDto>();
				for (ActAccountDto actAccountDto : cashfundAccountList) {
					cashfundAccountMap.put(actAccountDto.getAccountId(), actAccountDto);
				}
				for (int i = 0; i < cashfundAccountIds.size(); i++) {
					String cashfundAccountId = cashfundAccountIds.get(i);
					ActAccountDto actAccountDto = cashfundAccountMap.get(cashfundAccountId);
					if(actAccountDto==null){
						errorMsgs.add("保证金账户："+cashfundAccountId+"不存在");
					}else{
						BisSetCashfund bisSeCashfund = new BisSetCashfund();
						bisSeCashfund.setId(UUID.randomUUID().toString());
						bisSeCashfund.setCashfundAccountId(cashfundAccountId);
						//bisSeCashfund.setCashfundSubjectName(cashfundSubjectName);
						bisSeCashfund.setCashfundAmount(actAccountDto.getCashAmount());
						bisSeCashfund.setSetAmount(setAmount);
						bisSeCashfund.setBalanceAccountId(balanceAccountIds.get(i));
						bisSeCashfund.setActaulSetAmount(0L);
						bisSeCashfund.setEffectiveDate(effectiveDate);
						bisSeCashfund.setEffectiveStatus(EffectiveStatusEnum.WAIT_EFFECT);
						bisSeCashfund.setAuditStatus(BISAuditStatus.WAIT_AUDIT);
						bisSeCashfund.setCreater(userDto.getLoginName());
						bisSeCashfund.setCreaterName(userDto.getRealName());
						bisSeCashfund.setCreateDatetime(new Date());
						bisSeCashfund.setRemarks(remarks);
						bisSeCashfund.setCustId(actAccountDto.getCustId());
						bisSeCashfund.setCustName(actAccountDto.getCustName());
						bisSeCashfund.setProcessStatus(EPocessStatusEnum.WAIT_COMPLATE);
						bisSeCashfund.setAppleType(EAppleType.HAND_MADE);
						setCashFunds.add(bisSeCashfund);
					}
				}
			}
			if(errorMsgs.size()==0){
				bisSetCashfundMapper.batchCashFundExp(setCashFunds);
			}
		}
		return errorMsgs;
	}

	@Override
	public List<String> batchAudit(List<String> ids,UcsSecUserDto userDto, AuditTypeEnum auditType,String auditReason) throws CbsCheckedException {
		ArrayList<String> errorMsgs = new ArrayList<String>();
		String accountDate = actaccountDateService.getAccountDate();
		for (String id : ids) {
			BisSetCashfund bisCashFund = bisSetCashfundMapper.findBeanDto(id);
			if(bisCashFund==null){
				errorMsgs.add("编号："+id+"保证金设置记录不存在");
				continue;
			}
			BisAuditDto bisAuditDto = new BisAuditDto();
			if(AuditTypeEnum.AUDIT_PASS==auditType){
				/**
				 * 判断是否复核状态
				 */
				if(BISAuditStatus.WAIT_AUDIT!=bisCashFund.getAuditStatus()){
					errorMsgs.add("保证金账号："+bisCashFund.getBalanceAccountId()+",可用账号："+bisCashFund.getBalanceAccountId()+"的记录为"+bisCashFund.getAuditStatus().getDisplayName()+"状态，不能复核");
					continue;
				}
				if(accountDate.compareTo(bisCashFund.getEffectiveDate())>=0){
					bisCashFund.setEffectiveStatus(EffectiveStatusEnum.VALID);
					//获取备付金账户
					ActAccountDto balanceAccount = actAccountService.selectByAccountId(bisCashFund.getBalanceAccountId());
					if(balanceAccount==null){
						errorMsgs.add("可用账号："+bisCashFund.getBalanceAccountId()+"的可用账户不存在");
						continue;
					}
					//获取保证金账户
					ActAccountDto cashFundAccount = actAccountService.selectByAccountId(bisCashFund.getCashfundAccountId());
					if(cashFundAccount==null){
						errorMsgs.add("保证金账号："+bisCashFund.getBalanceAccountId()+"的保证金账户不存在");
						continue;
					}
					//如果保证金和设置金额一致，不做任何处理，异常日志通知
					if(cashFundAccount.getCashAmount().equals(bisCashFund.getSetAmount())){
						bisCashFund.setProcessStatus(EPocessStatusEnum.Y_COMPLATE);
						updateBatchExpNum(bisCashFund, 1L,0L);
					}else{
						//获取实际设置金额
						Long actaulSetAmount = setSetAmount(bisCashFund, balanceAccount.getCashAmount(), cashFundAccount.getCashAmount(), bisCashFund.getSetAmount());
						//第一次设置时金额不够，消息通知
						if(bisCashFund.getSetAmount()!=actaulSetAmount && bisSetCashFundOperDtoMapper.existOperNum(bisCashFund.getId())==0){
							//消息通知
							
						}
						/**
						 * 调用支付系统缴纳保证金接口，如果实际设置金额为0，不用调用接口
						 */
						if(actaulSetAmount!=null && actaulSetAmount!=0){
							/*if(bisCashFund.getActaulSetAmount()==null){
								bisCashFund.setActaulSetAmount(actaulSetAmount);
							}else{
								bisCashFund.setActaulSetAmount(actaulSetAmount+bisCashFund.getActaulSetAmount());
							}*/
							ResultCodeDto<PayAccountAdjustDto> resultDto=null;
							String orderId = null;
							try {
								orderId=DateUtil.getYearMonthDay$(new Date())+EPayAdjustType.DEPOSIT.getValue() + bisTransferHandleDao.getSeqenceVals();
								resultDto = paySystemPayAccountAdjustAppService.depositAccountAmount(bisCashFund.getCashfundAccountId(), actaulSetAmount, orderId);
								if(resultDto!=null && EResultCode.SUCCESS.equals(resultDto.getResultCode())){
									if(bisCashFund.getActaulSetAmount()==null){
										bisCashFund.setActaulSetAmount(actaulSetAmount);
									}else{
										bisCashFund.setActaulSetAmount(actaulSetAmount+bisCashFund.getActaulSetAmount());
									}
								}
								saveBisSetCashFundOperDto(orderId, bisCashFund.getId(), resultDto, actaulSetAmount);
								//如果保证金和设置金额一致，支付状态为已处理
								if(isYComplate(resultDto.getResultCode(), bisCashFund.getCashfundAccountId(), bisCashFund.getSetAmount())){
									bisCashFund.setProcessStatus(EPocessStatusEnum.Y_COMPLATE);
									updateBatchExpNum(bisCashFund, 1L,0L);
								}
							} catch (Exception e) {
								_log.error("调用保证金设置接口异常"+ExceptionProcUtil.getExceptionDesc(e));
								saveBisSetCashFundOperDto(orderId, bisCashFund.getId(), resultDto, actaulSetAmount);
							}
						}
					}
				}
				bisCashFund.setAuditStatus(BISAuditStatus.AUDIT_PASS);
				bisAuditDto.setAuditStatus(BISAuditStatus.AUDIT_PASS);
			}else if(AuditTypeEnum.AUDIT_REJECT==auditType){
				/**
				 * 判断是否复核状态
				 */
				if(BISAuditStatus.WAIT_AUDIT!=bisCashFund.getAuditStatus()){
					errorMsgs.add("保证金账号："+bisCashFund.getBalanceAccountId()+",可用账号："+bisCashFund.getBalanceAccountId()+"的记录为"+bisCashFund.getAuditStatus().getDisplayName()+"状态，不能复核");
					continue;
				}
				if(EffectiveStatusEnum.VALID==bisCashFund.getEffectiveStatus()){
					errorMsgs.add("保证金账号："+bisCashFund.getBalanceAccountId()+",可用账号："+bisCashFund.getBalanceAccountId()+"的记录为生效状态，不能复核拒绝");
					continue;
				}
				bisCashFund.setAuditStatus(BISAuditStatus.AUDIT_REJECT);
				//bisCashFund.setProcessStatus(EPocessStatusEnum.Y_COMPLATE);
				bisAuditDto.setAuditStatus(BISAuditStatus.AUDIT_REJECT);
			}else{
				/**
				 * 只有复核通过未生效，待复核的数据才可以撤销
				 */
				if(!(BISAuditStatus.AUDIT_PASS==bisCashFund.getAuditStatus() && EffectiveStatusEnum.WAIT_EFFECT==bisCashFund.getEffectiveStatus())){
					errorMsgs.add("保证金账号："+bisCashFund.getBalanceAccountId()+",可用账号："+bisCashFund.getBalanceAccountId()+"的记录.复核状态："+bisCashFund.getAuditStatus().getDisplayName()+"，生效状态："+bisCashFund.getEffectiveStatus().getDisplayName()+";只有复核通过，生效状态为未生效");
					continue;
				}
				//bisCashFund.setEffectiveStatus(EffectiveStatusEnum.WAIT_EFFECT);
				//由于复核通过未生效的数据在进入方法前已经被修改为中断，这里需要改为未完成
				bisCashFund.setProcessStatus(EPocessStatusEnum.WAIT_COMPLATE);
				bisCashFund.setAuditStatus(BISAuditStatus.REVOKED);
				bisAuditDto.setAuditStatus(BISAuditStatus.REVOKED);
			}
			//保存操作记录
			bisAuditDto.setId(UUID.randomUUID().toString());
			bisAuditDto.setBusId(bisCashFund.getId());
			bisAuditDto.setAuditType(BISAuditType.CASH_FUND);
			bisAuditDto.setAuditor(userDto.getLoginName());
			bisAuditDto.setAuditorName(userDto.getRealName());
			bisAuditDto.setReason(auditReason);
			bisAuditDto.setAuditTime(new Date());
			//保存复核记录
			bisAuditDtoMapper.insert(bisAuditDto);
			//修改保证金设置记录
			bisSetCashfundMapper.updateSetCashFund(bisCashFund);
		}
		return errorMsgs;
	}
	/**
	 * 保存保证金设置操作记录
	 * @param orderId
	 * @param busId
	 * @param resultDto
	 * @return
	 */
	public void saveBisSetCashFundOperDto(String orderId,String busId,ResultCodeDto<PayAccountAdjustDto> resultDto,long setAmt){
		BisSetCashFundOperDto bisSetCashFundOperDto = new BisSetCashFundOperDto();
		bisSetCashFundOperDto.setId(UUID.randomUUID().toString());
		bisSetCashFundOperDto.setBusId(busId);
		bisSetCashFundOperDto.setCreateTime(new Date());
		bisSetCashFundOperDto.setOrderId(orderId);
		bisSetCashFundOperDto.setSetAmt(setAmt);
		if(resultDto==null){
			bisSetCashFundOperDto.setPayStatus(PayStatusEnum.UNKNOWN);
			bisSetCashFundOperDto.setRemark("调用支付接口返回状态为空");
		}else{
			bisSetCashFundOperDto.setRemark(resultDto.getMsgDetail());
			setPayStatus(resultDto.getResultCode(), bisSetCashFundOperDto);
		}
		bisSetCashFundOperDtoMapper.insert(bisSetCashFundOperDto);
	}
	
	/**
	 * 设置支付状态
	 * @param resultCode
	 * @param bisEcternalPayments
	 */
	public void setPayStatus(EResultCode resultCode,BisSetCashFundOperDto bisSetCashFundOperDto){
		if(EResultCode.FAIL==resultCode){
			bisSetCashFundOperDto.setPayStatus(PayStatusEnum.FAIL);
		}else if(EResultCode.SUCCESS==resultCode){
			bisSetCashFundOperDto.setPayStatus(PayStatusEnum.SUCCESS);
		}else{
			bisSetCashFundOperDto.setPayStatus(PayStatusEnum.UNKNOWN);
		}
	}
	
	/**
	 * 判断保证金设置金额与余额是否一致
	 * @param bisSetCashFundOperDto
	 * @param bisSetCashfund
	 */
	public boolean isYComplate(EResultCode resultCode,String accountId,long setAmt){
		if(EResultCode.SUCCESS==resultCode){
			//获取保证金
			ActAccountDto cashFundAccount = actAccountService.selectByAccountId(accountId);
			if(cashFundAccount.getCashAmount().equals(setAmt)){
				return true;
			}
		}
		return false;
	}
	
	@Override
	public void flushSetCashFund() {
	String accountDate = actaccountDateService.getAccountDate();
		//获取小于或等于当前账务日期，审核通过，未完成的数据（是否生效不关心）
		List<BisSetCashfund> bisCashFunds=bisSetCashfundMapper.findListAuditPass(accountDate,BISAuditStatus.AUDIT_PASS,EPocessStatusEnum.WAIT_COMPLATE);
		_log.info("保证金设置定时任务扫描到【"+bisCashFunds.size()+"】条审核通过，未完成记录");
		for (BisSetCashfund bisSetCashfund : bisCashFunds) {
			//如果保证金和设置金额一致，支付状态为已处理
			if(isYComplate(EResultCode.SUCCESS, bisSetCashfund.getCashfundAccountId(), bisSetCashfund.getSetAmount())){
				bisSetCashfund.setProcessStatus(EPocessStatusEnum.Y_COMPLATE);
				bisSetCashfund.setEffectiveStatus(EffectiveStatusEnum.VALID);
				bisSetCashfundMapper.updateSetCashFund(bisSetCashfund);
				updateBatchExpNum(bisSetCashfund, 1L,0L);
				continue;
			}
			//判断是否存在未知的操作记录
			long num=bisSetCashFundOperDtoMapper.findByBusId(bisSetCashfund.getId(),PayStatusEnum.UNKNOWN);
			if(num!=0){
				_log.info("存在未知状态的操作记录，不予执行保证金设置");
				//bisExceptionLogService.saveLog(CbsExceptionLogDtoUtils.getBisExceptionLog(EBisExceptionLogNlevel.MESSAGE, EBisExceptionLogType.SETCASHFUND,"存在未知状态的操作记录，不予执行保证金设置"));
				continue;
			}else{
				try {
					bisSetCashfund.setEffectiveStatus(EffectiveStatusEnum.VALID);
					//获取实际设置金额
					ActAccountDto balanceAccount = actAccountService.selectByAccountId(bisSetCashfund.getBalanceAccountId());
					if(balanceAccount==null){
						String errorMsg="可用账号："+bisSetCashfund.getBalanceAccountId()+"的可用账户不存在";
						_log.error(errorMsg);
						//bisExceptionLogService.saveLog(CbsExceptionLogDtoUtils.getBisExceptionLog(EBisExceptionLogNlevel.ERROR, EBisExceptionLogType.SETCASHFUND,errorMsg));
						continue;
					}
					ActAccountDto cashFundAccount = actAccountService.selectByAccountId(bisSetCashfund.getCashfundAccountId());
					if(cashFundAccount==null){
						String errorMsg="保证金账号："+bisSetCashfund.getBalanceAccountId()+"的保证金账户不存在";
						_log.error(errorMsg);
						//bisExceptionLogService.saveLog(CbsExceptionLogDtoUtils.getBisExceptionLog(EBisExceptionLogNlevel.ERROR, EBisExceptionLogType.SETCASHFUND,errorMsg));
						continue;
					}
					//如果保证金和设置金额一致，不做任何处理，异常日志通知
					if(cashFundAccount.getCashAmount().equals(bisSetCashfund.getSetAmount())){
						bisSetCashfund.setProcessStatus(EPocessStatusEnum.Y_COMPLATE);
						updateBatchExpNum(bisSetCashfund, 1, 0);
					}else{
						Long actaulSetAmount = setSetAmount(bisSetCashfund, balanceAccount.getCashAmount(), cashFundAccount.getCashAmount(), bisSetCashfund.getSetAmount());
						//第一次设置时金额不够，消息通知
						if(bisSetCashfund.getSetAmount()!=actaulSetAmount && bisSetCashFundOperDtoMapper.existOperNum(bisSetCashfund.getId())==0){
							//消息通知
							
						}
						if(actaulSetAmount!=null && actaulSetAmount!=0){
							ResultCodeDto<PayAccountAdjustDto> resultDto=null;
							String orderId=null;
							try {
								orderId=DateUtil.getYearMonthDay$(new Date())+EPayAdjustType.DEPOSIT.getValue() + bisTransferHandleDao.getSeqenceVals();
								resultDto = paySystemPayAccountAdjustAppService.depositAccountAmount(bisSetCashfund.getCashfundAccountId(), actaulSetAmount, orderId);
								//支付明确返回成功，增加实际设置金额
								if(resultDto!=null && EResultCode.SUCCESS.equals(resultDto.getResultCode())){
									if(bisSetCashfund.getActaulSetAmount()==null){
										bisSetCashfund.setActaulSetAmount(actaulSetAmount);
									}else{
										bisSetCashfund.setActaulSetAmount(actaulSetAmount+bisSetCashfund.getActaulSetAmount());
									}
								}
								saveBisSetCashFundOperDto(orderId, bisSetCashfund.getId(), resultDto, actaulSetAmount);
								//如果保证金和设置金额一致，支付状态为已处理
								if(isYComplate(resultDto.getResultCode(), bisSetCashfund.getCashfundAccountId(), bisSetCashfund.getSetAmount())){
									bisSetCashfund.setProcessStatus(EPocessStatusEnum.Y_COMPLATE);
									updateBatchExpNum(bisSetCashfund, 1, 0);
								}
							} catch (Exception e) {
								_log.error("调用保证金设置接口异常:"+ExceptionProcUtil.getExceptionDesc(e));
								saveBisSetCashFundOperDto(orderId, bisSetCashfund.getId(), resultDto, actaulSetAmount);
								bisExceptionLogService.saveLog(CbsExceptionLogDtoUtils.getBisExceptionLog(EBisExceptionLogNlevel.ERROR, EBisExceptionLogType.SETCASHFUND,"调用保证金设置接口异常"));
							}
						}
					}				
					} catch (Exception e) {
						/**
						 * 记录异常日志
						 */
						String errorMsg=ExceptionProcUtil.getExceptionDesc(e);
						if(!StringUtils.isBlank(errorMsg) && errorMsg.length()>=1000){
							errorMsg=errorMsg.substring(0,999);
						}else{
							errorMsg="定时任务-保证金设置失败：数据为空";
						}
						_log.error(errorMsg);
						bisExceptionLogService.saveLog(CbsExceptionLogDtoUtils.getBisExceptionLog(EBisExceptionLogNlevel.ERROR, EBisExceptionLogType.SETCASHFUND,errorMsg));
					}
			}
			//修改保证金设置记录
			bisSetCashfundMapper.updateSetCashFund(bisSetCashfund);
			
		}
	}
	
	/**
	 * 根据余额账户和保证金账户设置封装对象
	 * @param custName 
	 * @param custId 
	 * @param setCashFund
	 */
	public BisSetCashfund getBisSetCashFund(ActAccountDto balanceAccount,ActAccountDto cashfundAccount, Long setAmount,UcsSecUserDto userDto,String effectiveDate,String remarks, String custId, String custName){
		BisSetCashfund setCashFund = new BisSetCashfund();
		setCashFund.setId(UUID.randomUUID().toString());
		//setCashFund.setOrderId(DateUtil.getYearMonthDay$(new Date())+EPayAdjustType.DEPOSIT.getValue() + bisTransferHandleDao.getSeqenceVals());
		//setCashFund.setOrderId(DateUtil.getYearMonthDay$(new Date())+EPayAdjustType.DEPOSIT.getValue());
		setCashFund.setCashfundAccountId(cashfundAccount.getAccountId());
		setCashFund.setBalanceAccountId(balanceAccount.getAccountId());
		setCashFund.setCashfundAmount(cashfundAccount.getCashAmount());
		setCashFund.setSetAmount(setAmount);
		setCashFund.setEffectiveDate(effectiveDate);
		setCashFund.setEffectiveStatus(EffectiveStatusEnum.WAIT_EFFECT);
		setCashFund.setAuditStatus(BISAuditStatus.WAIT_AUDIT);
		setCashFund.setCreater(userDto.getLoginName());
		setCashFund.setCreaterName(userDto.getRealName());
		setCashFund.setCreateDatetime(new Date());
		setCashFund.setRemarks(remarks);
		setCashFund.setCustId(custId);
		setCashFund.setCustName(custName);
		setCashFund.setProcessStatus(EPocessStatusEnum.WAIT_COMPLATE);
		setCashFund.setAppleType(EAppleType.HAND_MADE);
		return setCashFund;
	}
	
	/**
	 * 设置保证金实际设置金额和获取保证金变动金额
	 * @param setCashFund
	 * @param balanceAmmount
	 * @param cashFundAmount
	 * @param setAmount
	 */
	public Long setSetAmount(BisSetCashfund setCashFund,Long balanceAmmount,Long cashFundAmount,Long setAmount){
		//以保证金的为主
		if(setAmount<=cashFundAmount){
			//setCashFund.setActaulSetAmount(setAmount);
			//调用接口是传入金额为setAmount-cashFundAmount
			return setAmount-cashFundAmount;
		}else{
			if(balanceAmmount>(setAmount-cashFundAmount)){
				//setCashFund.setActaulSetAmount(setAmount);
				//调用接口是传入金额为setAmount-cashFundAmount
				return setAmount-cashFundAmount;
			}else{
				//setCashFund.setActaulSetAmount(setAmount-(setAmount-cashFundAmount-balanceAmmount));
				//调用接口是传入金额为balanceAmmount
				return balanceAmmount;
			}
		}
	}

	@Override
	public long getAuditNum(List<String> ids, BISAuditStatus auditPass, EffectiveStatusEnum waitEffect) {
		return bisSetCashfundMapper.getAuditNum(ids,auditPass,waitEffect);
	}
	
	@Override
	public void stopSetCashFund(List<String> ids, EPocessStatusEnum stop) {
		List<BisSetCashfund> bisSetCashFunds=bisSetCashfundMapper.findListByIds(ids);
		List<String> accountIds = new ArrayList<String>();
		for (BisSetCashfund dto : bisSetCashFunds) {
			accountIds.add(dto.getCashfundAccountId());
		}
		//审核通过、未生效的数据也回被终止，正常情况下：如果存在次记录该账户是不可以再次设置保证金的
		_log.info("终止审核通过、未完成的保证金任务");
		bisSetCashfundMapper.stopSetCashFund(accountIds,stop,BISAuditStatus.AUDIT_PASS,EPocessStatusEnum.WAIT_COMPLATE);
	}
	
	/**
	 * 查询未知状态的订单状态
	 */
	@Override
	public void queryUndownStatus(){
		//获取所有未知状态记录
		List<BisSetCashFundOperDto> bisSetCashFundOperDtos=bisSetCashFundOperDtoMapper.findListUndown(PayStatusEnum.UNKNOWN);
		if(bisSetCashFundOperDtos!=null && !bisSetCashFundOperDtos.isEmpty()){
			_log.info("获取未知状态记录数【"+bisSetCashFundOperDtos.size()+"】");
			for (BisSetCashFundOperDto dto : bisSetCashFundOperDtos) {
				try {
					ResultCodeDto<PayAccountAdjustDto> resultCode = paySystemPayAccountAdjustAppService.findByOrderAndPayAdjustType(dto.getOrderId(),EPayAdjustType.DEPOSIT);
					if(resultCode!=null){
						setOperPayStatus(resultCode, dto);
					}
				} catch (Exception e) {
					_log.error("保证金设置未知状态查询失败："+ExceptionProcUtil.getExceptionDesc(e));
					bisExceptionLogService.saveLog(CbsExceptionLogDtoUtils.getBisExceptionLog(EBisExceptionLogNlevel.ERROR, EBisExceptionLogType.QUERY_UNDOWNSTATUS,"保证金设置未知状态查询失败"));
				}
				
			}
		}
	}
	
	/**
	 * 设置操作记录的支付状态
	 * @param resultCode
	 * @param bisEcternalPayments
	 */
	public void setOperPayStatus(ResultCodeDto<PayAccountAdjustDto> resultCode,BisSetCashFundOperDto dto){
		if(resultCode==null){
			dto.setPayStatus(PayStatusEnum.UNKNOWN);
			dto.setRemark("调用支付系统接口返回状态为空");
		}else if(EResultCode.FAIL==resultCode.getResultCode()){
			dto.setPayStatus(PayStatusEnum.FAIL);
			dto.setRemark(resultCode.getMsgDetail());
		}else if(EResultCode.SUCCESS==resultCode.getResultCode()){
			BisSetCashfund setCashFundDto = bisSetCashfundMapper.findBeanDto(dto.getBusId());
			if(setCashFundDto!=null){
				if(isYComplate(EResultCode.SUCCESS, setCashFundDto.getCashfundAccountId(), setCashFundDto.getSetAmount())){
					setCashFundDto.setProcessStatus(EPocessStatusEnum.Y_COMPLATE);
					updateBatchExpNum(setCashFundDto, 1, 0);
				}
				//支付明确返回成功，增加实际设置金额
				if(setCashFundDto.getActaulSetAmount()==null){
					setCashFundDto.setActaulSetAmount(dto.getSetAmt());
				}else{
					setCashFundDto.setActaulSetAmount(dto.getSetAmt()+setCashFundDto.getActaulSetAmount());
				}
				bisSetCashfundMapper.updateSetCashFund(setCashFundDto);
			}
			dto.setRemark(resultCode.getMsgDetail());
			dto.setPayStatus(PayStatusEnum.SUCCESS);
		}else{
			dto.setPayStatus(PayStatusEnum.UNKNOWN);
			dto.setRemark(resultCode.getMsgDetail());
		}
		bisSetCashFundOperDtoMapper.updatePayStatus(dto);
	}

	@Override
	public List<BisSetCashFundOperDto> getOperList(String id) {
		return bisSetCashFundOperDtoMapper.getOperList(id);
	}
	
	/**
	 * 修改批量导入记录成功/失败笔数
	 * @param frozenDto
	 * @param successNum
	 * @param failNum
	 */
	private void updateBatchExpNum(BisSetCashfund dto, long successNum, long failNum) {
		if(StringUtils.isBlank(dto.getBatchNo())){
			return ;
		}
		bisBatchExpService.updateBatchExpNum(successNum,failNum,dto.getBatchNo());
	}
	
	/**
	 * 根据批次号获取保证金map
	 */
	@Override
	public Map<String, BisSetCashfund> findCashFundMapByBatchNo(String batchNo) {
		List<BisSetCashfund> setCashFundDtoList=bisSetCashfundMapper.findCashFundListByBatchNo(batchNo);
		HashMap<String, BisSetCashfund> CashFundMap = new HashMap<String,BisSetCashfund>();
		if(setCashFundDtoList!=null && !setCashFundDtoList.isEmpty()){
			for (BisSetCashfund bisSetCashfund : setCashFundDtoList) {
				BisSetCashfund dto = CashFundMap.get(bisSetCashfund.getCashfundAccountId());
				if(dto!=null){
					dto.setSetAmount(dto.getSetAmount()+bisSetCashfund.getSetAmount());
				}else{
					CashFundMap.put(bisSetCashfund.getCashfundAccountId(), bisSetCashfund);
				}
			}
		}
		return CashFundMap;
	}

	@Override
	public List<BisSetCashfund> list(BisSetCashfund queryParam) {
		List<BisSetCashfund> findListPage = bisSetCashfundMapper.findListPage(queryParam);
		findSubjectName(findListPage);
		return findListPage;
	}

	@Override
	public Map<String,BisSetCashfund> findExistWaitAuditMap(List<String> accountIds) {
		List<BisSetCashfund> waitAudit = bisSetCashfundMapper.isWaitAudit(accountIds,BISAuditStatus.WAIT_AUDIT);
		HashMap<String, BisSetCashfund> cashfundMap = new HashMap<String,BisSetCashfund>();
		if(waitAudit!=null && !waitAudit.isEmpty()){
			for (BisSetCashfund bisSetCashfund : waitAudit) {
				cashfundMap.put(bisSetCashfund.getCashfundAccountId(), bisSetCashfund);
			}
		}
		return cashfundMap;
	}

	@Override
	public long stopSetCashFundByBatchNo(String batchNo, EPocessStatusEnum stop) {
		List<BisSetCashfund> bisSetcashfundDtos = bisSetCashfundMapper.findCashFundListByBatchNo(batchNo);
		if(bisSetcashfundDtos!=null && !bisSetcashfundDtos.isEmpty()){
			ArrayList<String> accountIds = new ArrayList<String>();
			for (BisSetCashfund dto : bisSetcashfundDtos) {
				accountIds.add(dto.getCashfundAccountId());
			}
			bisSetCashfundMapper.stopSetCashFund(accountIds,stop,BISAuditStatus.AUDIT_PASS,EPocessStatusEnum.WAIT_COMPLATE);
		}
		return 0;
	}
}
