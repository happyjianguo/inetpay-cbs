package com.ylink.inetpay.cbs.bis.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.ylinkpay.framework.core.model.PageData;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.gson.Gson;
import com.ylink.inetpay.cbs.act.service.ActAccountService;
import com.ylink.inetpay.cbs.act.service.ActCustRateService;
import com.ylink.inetpay.cbs.bis.dao.BisActCustRateAuditDtoMapper;
import com.ylink.inetpay.cbs.common.CbsConfig;
import com.ylink.inetpay.cbs.common.CbsConstants;
import com.ylink.inetpay.common.core.constant.BISAuditStatus;
import com.ylink.inetpay.common.core.constant.BISAuditType;
import com.ylink.inetpay.common.core.constant.EActCustRateStatus;
import com.ylink.inetpay.common.core.constant.EAppleType;
import com.ylink.inetpay.common.core.constant.EAutoManual;
import com.ylink.inetpay.common.core.constant.EBisExceptionLogNlevel;
import com.ylink.inetpay.common.core.constant.EBisExceptionLogType;
import com.ylink.inetpay.common.core.constant.EBisSmsSystem;
import com.ylink.inetpay.common.core.constant.EOperateType;
import com.ylink.inetpay.common.core.constant.ERateType;
import com.ylink.inetpay.common.core.constant.ESuccessFailDealing;
import com.ylink.inetpay.common.core.dto.SuccessFailDealingDto;
import com.ylink.inetpay.common.core.util.DateUtils;
import com.ylink.inetpay.common.core.util.ExceptionProcUtil;
import com.ylink.inetpay.common.project.account.app.ActAccountDateAppService;
import com.ylink.inetpay.common.project.account.app.ActCustRateAppService;
import com.ylink.inetpay.common.project.account.dto.ActAccountDto;
import com.ylink.inetpay.common.project.account.dto.ActCustRateDto;
import com.ylink.inetpay.common.project.account.exception.AccountCheckedException;
import com.ylink.inetpay.common.project.cbs.app.BisSchedulerAppService;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisActCustRateAuditDto;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisAuditDto;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisExceptionLogDto;
import com.ylink.inetpay.common.project.cbs.exception.CbsUncheckedException;

@Service("bisActCustRateAuditService")
@Transactional(value=CbsConstants.TX_MANAGER_BIS)
public class BisActCustRateAuditServiceImpl implements BisActCustRateAuditService {

	@Autowired
	BisActCustRateAuditDtoMapper bisActCustRateAuditDtoMapper;
	@Autowired
	BisAuditService bisAuditService;
	@Autowired
	ActCustRateService actCustRateService;
	@Autowired
	ActCustRateAppService actCustRateAppService;
	@Autowired
	ActAccountService actAccountService;
	@Autowired
	ActAccountDateAppService actAccountDateAppService;
	@Autowired
	private BisExceptionLogService bisExceptionLogService;
	@Autowired
	BisSchedulerAppService bisSchedulerAppService;
	@Autowired
	private BisBatchExpService bisBatchExpService;
	private Logger _log = LoggerFactory.getLogger(BisActCustRateAuditServiceImpl.class);
	
	
	@Override
	public PageData<BisActCustRateAuditDto> findPage(PageData<BisActCustRateAuditDto> pageData, BisActCustRateAuditDto queryParam) {
		PageHelper.startPage(pageData.getPageNumber(), pageData.getPageSize());
		List<BisActCustRateAuditDto> items = bisActCustRateAuditDtoMapper.list(queryParam);
		Page<BisActCustRateAuditDto> page = (Page<BisActCustRateAuditDto>)items;
		pageData.setRows(items);
		pageData.setTotal(page.getTotal());
		return pageData;
	}

	@Override
	public void save(BisActCustRateAuditDto bisActCustRateAudit) {
		Date dt = new Date();
		bisActCustRateAudit.setId(bisActCustRateAudit.getIdentity());
		bisActCustRateAudit.setCreateTime(dt);
		bisActCustRateAudit.setUpdateTime(dt);
		if(StringUtils.isNotBlank(bisActCustRateAudit.getRefId())) {
			ActCustRateDto actCustRateDto = actCustRateService.findById(bisActCustRateAudit.getRefId());
			if(actCustRateDto == null) {
				throw new CbsUncheckedException("","获取利率失败refID="+bisActCustRateAudit.getRefId());
			}
			//将对象转换为json字符串
			Gson gson = new Gson();
			bisActCustRateAudit.setRemark(gson.toJson(actCustRateDto,ActCustRateDto.class));
		}
		bisActCustRateAudit.setAppleType(EAppleType.HAND_MADE);
		bisActCustRateAuditDtoMapper.insert(bisActCustRateAudit);
	}
	
	@Override
	public void auditPass(String auditor, String auditorName, String id,String reason) {
		BisActCustRateAuditDto actCustRateAudit = bisActCustRateAuditDtoMapper.selectByPrimaryKey(id);
		if(actCustRateAudit == null)return ;
		//保存信息到审核记录表
		saveBisAuditDto(auditor, auditorName, id,BISAuditStatus.AUDIT_PASS,reason);
		//审核通过同步利率信息,如果超过有限期，则同步，否则通过定时任务同步
		Date accountDate = DateUtils.changeToDate(actAccountDateAppService.getAccountDate());
		if(accountDate.compareTo(actCustRateAudit.getValidTime()) >= 0) {
			ActCustRateDto actCustRateDto = new ActCustRateDto();
			if(actCustRateAudit.getOperateType() == EOperateType.NEW || 
					actCustRateAudit.getOperateType() == EOperateType.EDIT) {
				BeanUtils.copyProperties(actCustRateAudit, actCustRateDto, new String[]{"id"} );
				try {
					actCustRateDto.setId(actCustRateAudit.getRefId());
					actCustRateDto.setStatus(EActCustRateStatus.EFFECTIVE);
					//actCustRateDto.setId(actCustRateAudit.getRefId());
					actCustRateAppService.save(actCustRateDto);
				} catch (Exception e) {
					throw new CbsUncheckedException("","审核通过同步利率失败："+e.getMessage(),e);
				}
			} else if(actCustRateAudit.getOperateType() == EOperateType.DELETE){
				try {
					actCustRateAppService.delete(actCustRateAudit.getRefId());
				} catch (AccountCheckedException e) {
					throw new CbsUncheckedException("","删除利率失败："+e.getMessage(),e);
				}
			}
			actCustRateAudit.setStatus(EActCustRateStatus.EFFECTIVE);
			//更新导入批次的成功失败笔数
			//updateBatchExpNum(actCustRateAudit, 1, 0);
		}else{
			actCustRateAudit.setStatus(EActCustRateStatus.UNEFFECTIVE);
		}
		//更新利率审核表
		actCustRateAudit.setAuditStatus(BISAuditStatus.AUDIT_PASS);
		actCustRateAudit.setAuditor(auditor);
		actCustRateAudit.setAuditorName(auditorName);
		actCustRateAudit.setAuditTime(new Date());
		actCustRateAudit.setUpdateTime(new Date());
		
		bisActCustRateAuditDtoMapper.updateByPrimaryKeySelective(actCustRateAudit);
	}

	private void saveBisAuditDto(String auditor, String auditorName, String id,
			BISAuditStatus bisAuditStatus,String remark) {
		BisAuditDto auditDto = new BisAuditDto();
		auditDto.setId(auditDto.getIdentity());
		auditDto.setAuditor(auditor);
		auditDto.setAuditorName(auditorName);
		auditDto.setAuditStatus(bisAuditStatus);
		auditDto.setAuditTime(new Date());
		auditDto.setAuditType(BISAuditType.CUST_RATE);
		auditDto.setBusId(id);
		auditDto.setReason(remark);
		bisAuditService.insert(auditDto);
	}

	@Override
	public void auditReject(String auditor, String auditorName, String reason,String id) {
		BisActCustRateAuditDto bisActCustRateAudit = bisActCustRateAuditDtoMapper.selectByPrimaryKey(id);
		if(bisActCustRateAudit == null)return ;
		//保存信息到审核记录表
		saveBisAuditDto(auditor, auditorName, id,BISAuditStatus.AUDIT_REJECT,reason);
		//更新利率审核表
		bisActCustRateAudit.setAuditStatus(BISAuditStatus.AUDIT_REJECT);
		bisActCustRateAudit.setAuditor(auditor);
		bisActCustRateAudit.setAuditorName(auditorName);
		bisActCustRateAudit.setAuditTime(new Date());
		bisActCustRateAudit.setUpdateTime(new Date());
		bisActCustRateAudit.setRejectReason(reason);
		bisActCustRateAuditDtoMapper.updateByPrimaryKeySelective(bisActCustRateAudit);
	}

	@Override
	public void cancel(String auditor, String auditorName,String id,String reason) {
		BisActCustRateAuditDto actRuleAudit = bisActCustRateAuditDtoMapper.selectByPrimaryKey(id);
		if(actRuleAudit == null)return ;
		//更新利率审核表
		actRuleAudit.setAuditStatus(BISAuditStatus.REVOKED);
		actRuleAudit.setUpdateTime(new Date());
		actRuleAudit.setCancelReason(reason);
		bisActCustRateAuditDtoMapper.updateByPrimaryKeySelective(actRuleAudit);
		//保存复核记录
		saveBisAuditDto(auditor, auditorName, id, BISAuditStatus.REVOKED, reason);
	}

	@Override
	public void batchAuditPass(String auditor, String auditorName, List<String> idList,String reason) {
		if(idList == null || idList.isEmpty())return;
		for(String id : idList){
			auditPass(auditor, auditorName, id,reason);
		}
	}

	@Override
	public void batchAuditReject(String auditor, String auditorName,String reason, List<String> idList) {
		if(idList == null || idList.isEmpty())return;
		for(String id : idList){
			auditReject(auditor, auditorName,reason, id);
		}
	}

	@Override
	public void batchCancel(String auditor,String auditorName,List<String> idList,String reason) {
		if(idList == null || idList.isEmpty())return;
		for(String id : idList){
			cancel(auditor, auditorName,id,reason);
		}
	}

	@Override
	public BisActCustRateAuditDto findById(String id) {
		return bisActCustRateAuditDtoMapper.selectByPrimaryKey(id);
	}

	@Override
	public long countWaitAuditWithAccountId(String accountId) {
		return bisActCustRateAuditDtoMapper.countWaitAuditWithAccountId(accountId);
	}

	@Override
	public long countWaitAuditWithBankCardNo(String bankCardNo) {
		return bisActCustRateAuditDtoMapper.countWaitAuditWithBankCardNo(bankCardNo);
	}

	@Override
	public List<BisActCustRateAuditDto> findWaitAuditWithAccountIds(List<String> accountIds) {
		return bisActCustRateAuditDtoMapper.findWithAccountIds(BISAuditStatus.WAIT_AUDIT, accountIds,BISAuditStatus.AUDIT_PASS,EActCustRateStatus.UNEFFECTIVE);
	}

	@Override
	public void saveOrUpateCustRate(List<String> accountIds, BigDecimal rate, Date validTime,
			String operator,String operatorName) {
		List<ActAccountDto> accounts = actAccountService.findListByAccountIds(accountIds);
		List<BisActCustRateAuditDto> items = new ArrayList<BisActCustRateAuditDto>();
		BisActCustRateAuditDto temp = null;
		for(ActAccountDto acct : accounts){
			temp = new BisActCustRateAuditDto();
			temp.setId(temp.getIdentity());
			temp.setCustId(acct.getCustId());
			temp.setCustName(acct.getCustName());
			temp.setAccountId(acct.getAccountId());
			temp.setStatus(EActCustRateStatus.UNEFFECTIVE);
			temp.setRateType(ERateType.CUST_RATE);
			temp.setRate(rate);
			temp.setAuditStatus(BISAuditStatus.WAIT_AUDIT);
			temp.setValidTime(validTime);
			temp.setAppleType(EAppleType.HAND_MADE);
			/**
			 * 如果已经设置利率则为修改
			 */
			ActCustRateDto custRateDto = actCustRateService.findByAccountId(acct.getAccountId());
			if(custRateDto==null){
				temp.setOperateType(EOperateType.NEW);
			}else{
				temp.setOperateType(EOperateType.EDIT);
			}
			temp.setOperator(operator);
			temp.setOperatorName(operatorName);
			temp.setSubjectName(acct.getSubjectNo2Name());
			Date nt = new Date();
			temp.setOperateTime(nt);
			temp.setCreateTime(nt);
			temp.setUpdateTime(nt);
			items.add(temp);
		}
		if(!items.isEmpty()){
			bisActCustRateAuditDtoMapper.saveBatch(items);
		}
	}

	@Override
	public List<BisActCustRateAuditDto> findPassAndValidTime(Date date) {
		return bisActCustRateAuditDtoMapper.findPassAndValidTime(date);
	}

	@Override
	public void updateSelective(BisActCustRateAuditDto bisActCustRateAudit) {
		bisActCustRateAuditDtoMapper.updateByPrimaryKeySelective(bisActCustRateAudit);
	}

	@Override
	public long getAuditNum(List<String> ids, BISAuditStatus auditPass, EActCustRateStatus uneffective) {
		return bisActCustRateAuditDtoMapper.getAuditNum(ids,auditPass,uneffective);
	}
	
	/**
	 * 客户利率生效，批量定时任务掉 
	 */
	@Override
	public SuccessFailDealingDto custRateEffective(EAutoManual auto, String accountDate, final String detailId) {
		try {
			final List<BisActCustRateAuditDto> items = findPassAndValidTime(DateUtils.stringToDate(DateUtils.stringToString(actAccountDateAppService.getAccountDate())));
			if(items != null && !items.isEmpty()){
				new Thread(new Runnable(){
					@Override
					public void run() {
						ActCustRateDto actCustRateDto = null;
						for(BisActCustRateAuditDto item : items){
							actCustRateDto = new ActCustRateDto();
							BeanUtils.copyProperties(item, actCustRateDto, new String[]{"id"} );
							try {
								actCustRateDto.setId(item.getRefId());
								actCustRateDto.setStatus(EActCustRateStatus.EFFECTIVE);
								actCustRateAppService.save(actCustRateDto);
								item.setStatus(EActCustRateStatus.EFFECTIVE);
								long num=bisActCustRateAuditDtoMapper.updateStatusById(item.getId(),EActCustRateStatus.UNEFFECTIVE,EActCustRateStatus.EFFECTIVE);
								//更新导入批次的成功失败笔数
								if(num>0){
									updateBatchExpNum(item, 1, 0);
								}
							} catch (Exception e) {
								saveExceptionLog("定时执行利率生效同步失败:"+e.getMessage());
								_log.error("定时执行利率生效同步失败：客户【"+item.getCustId()+"】主键【"+item.getId()+"】" + ExceptionProcUtil.getExceptionDesc(e));
								try {
									bisSchedulerAppService.planCallBack(detailId, new SuccessFailDealingDto(ESuccessFailDealing.FAIL),"客户利率生效失败:"+e.getMessage());
								} catch (Exception e2) {
									_log.error(String.format("修改客户利率生效定时任务为失败异常:{}",e2.getMessage()));
								}
							}
						}
					}
				}).start();
				return new SuccessFailDealingDto(ESuccessFailDealing.DEALING);
			}else{
				return new SuccessFailDealingDto(ESuccessFailDealing.SUCCESS);
			}
		} catch (Exception e) {
			saveExceptionLog("定时任务同步利息失败"+e.getMessage());
			_log.error("定时执行利息生效同步失败：" + ExceptionProcUtil.getExceptionDesc(e));
			return new SuccessFailDealingDto(ESuccessFailDealing.FAIL);
		}
	}
	
	/**
	 * 批次复核通过后，需要对客户利率生效
	 */
	@Override
	public void custRateEffective(String batchNo) {
		new Thread(new Runnable(){
			@Override
			public void run() {
				try {
				Date accountDate = DateUtils.stringToDate(DateUtils.stringToString(actAccountDateAppService.getAccountDate()));
				final List<BisActCustRateAuditDto> items = findPassAndValidTime(accountDate);
					if(items != null && !items.isEmpty()){
						ActCustRateDto actCustRateDto = null;
						for(BisActCustRateAuditDto item : items){
							actCustRateDto = new ActCustRateDto();
							BeanUtils.copyProperties(item, actCustRateDto, new String[]{"id"} );
							
								actCustRateDto.setId(item.getRefId());
								actCustRateDto.setStatus(EActCustRateStatus.EFFECTIVE);
								actCustRateAppService.save(actCustRateDto);
								item.setStatus(EActCustRateStatus.EFFECTIVE);
								long num=bisActCustRateAuditDtoMapper.updateStatusById(item.getId(),EActCustRateStatus.UNEFFECTIVE,EActCustRateStatus.EFFECTIVE);
								//更新导入批次的成功失败笔数
								if(num>0){
									updateBatchExpNum(item, 1, 0);
								}
						}
					}
				} catch (Exception e) {
					saveExceptionLog("批次【"+batchNo+"】复核通过，生效客户利率失败");
					_log.error("批次【"+batchNo+"】复核通过，生效客户利率失败：{}",e);
				}
			}
		}).start();
	}
	
	private void saveExceptionLog(String msg){
		BisExceptionLogDto bisExceptionLogDto = new BisExceptionLogDto();
		bisExceptionLogDto.setSystem(EBisSmsSystem.CBS);
		bisExceptionLogDto.setType(EBisExceptionLogType.CUSTRATE_EFFECTIVE);
		bisExceptionLogDto.setNlevel(EBisExceptionLogNlevel.ERROR);
		bisExceptionLogDto.setContent(msg);
		bisExceptionLogDto.setAllpath(CbsConfig.getLogFullPath());
		bisExceptionLogService.saveLog(bisExceptionLogDto);
	}
	
	/**
	 * 修改批量导入记录成功/失败笔数
	 * @param frozenDto
	 * @param successNum
	 * @param failNum
	 */
	private void updateBatchExpNum(BisActCustRateAuditDto dto, long successNum, long failNum) {
		if(StringUtils.isBlank(dto.getBatchNo())){
			return ;
		}
		bisBatchExpService.updateBatchExpNum(successNum,failNum,dto.getBatchNo());
	}
	/**
	 * 根据批次号获取客户利率map
	 * @param batchNo
	 * @return
	 */
	@Override
	public Map<String, BisActCustRateAuditDto> findCustRateMapByBatchNo(String batchNo) {
		List<BisActCustRateAuditDto> actCustRateAuditDtoList=bisActCustRateAuditDtoMapper.findCustRateListByBatchNo(batchNo);
		HashMap<String, BisActCustRateAuditDto> actCustRateAuditDtoMap = new HashMap<String,BisActCustRateAuditDto>();
		if(actCustRateAuditDtoList!=null && !actCustRateAuditDtoList.isEmpty()){
			for (BisActCustRateAuditDto bisActCustRateAuditDto : actCustRateAuditDtoList) {
				BisActCustRateAuditDto dto = actCustRateAuditDtoMap.get(bisActCustRateAuditDto.getAccountId());
				if(dto!=null){
					//不可能重复
				}else{
					actCustRateAuditDtoMap.put(bisActCustRateAuditDto.getAccountId(), bisActCustRateAuditDto);
				}
			}
		}
		return actCustRateAuditDtoMap;
	}

	@Override
	public List<BisActCustRateAuditDto> list(BisActCustRateAuditDto queryParam) {
		return bisActCustRateAuditDtoMapper.list(queryParam);
	}

	@Override
	public List<BisActCustRateAuditDto> findWaitAudit(ArrayList<String> accountIds) {
		return bisActCustRateAuditDtoMapper.findWaitAudit(accountIds);
	}

	@Override
	public List<BisActCustRateAuditDto> findWaitAuditByBankAccNos(ArrayList<String> bankAccNos) {
		return bisActCustRateAuditDtoMapper.findWaitAuditByBankAccNos(bankAccNos);
	}

	@Override
	public Map<String,BisActCustRateAuditDto> findExistWaitAuditMap(List<String> accountIds) {
		List<BisActCustRateAuditDto> BisActCustRateAuditDtos=bisActCustRateAuditDtoMapper.findWaitAudit(accountIds);
		HashMap<String, BisActCustRateAuditDto> actCustRateAuditDtoMap = new HashMap<String,BisActCustRateAuditDto>();
		if(BisActCustRateAuditDtos!=null && !BisActCustRateAuditDtos.isEmpty()){
			for (BisActCustRateAuditDto bisActCustRateAuditDto : BisActCustRateAuditDtos) {
				actCustRateAuditDtoMap.put(bisActCustRateAuditDto.getAccountId(), bisActCustRateAuditDto);
			}
		}
		return actCustRateAuditDtoMap;
	}
}
