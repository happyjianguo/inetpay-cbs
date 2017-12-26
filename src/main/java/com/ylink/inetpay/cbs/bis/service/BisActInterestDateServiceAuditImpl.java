package com.ylink.inetpay.cbs.bis.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import com.ylink.inetpay.cbs.act.service.ActInterestDateService;
import com.ylink.inetpay.cbs.bis.dao.BisActInterestDateAuditDtoMapper;
import com.ylink.inetpay.cbs.common.CbsConstants;
import com.ylink.inetpay.common.core.constant.BISAuditStatus;
import com.ylink.inetpay.common.core.constant.BISAuditType;
import com.ylink.inetpay.common.core.constant.EAutoManual;
import com.ylink.inetpay.common.core.constant.ESuccessFailDealing;
import com.ylink.inetpay.common.core.dto.SuccessFailDealingDto;
import com.ylink.inetpay.common.core.util.ExceptionProcUtil;
import com.ylink.inetpay.common.project.account.app.ActAccountDateAppService;
import com.ylink.inetpay.common.project.account.app.ActCustInterestAppService;
import com.ylink.inetpay.common.project.account.app.ActInterestDateAppService;
import com.ylink.inetpay.common.project.account.dto.ActInterestDateDto;
import com.ylink.inetpay.common.project.account.exception.AccountCheckedException;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisActInterestDateAuditDto;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisAuditDto;
import com.ylink.inetpay.common.project.cbs.exception.CbsUncheckedException;

@Service("bisActInterestDateService")
@Transactional(value=CbsConstants.TX_MANAGER_BIS)
public class BisActInterestDateServiceAuditImpl implements BisActInterestDateAuditService {

	private Logger _log = LoggerFactory.getLogger(BisActInterestDateServiceAuditImpl.class);
	
	@Autowired
	BisActInterestDateAuditDtoMapper bisActInterestDateAuditDtoMapper;
	@Autowired
	ActInterestDateService actInterestDateService;
	@Autowired
	BisAuditService bisAuditService;
	@Autowired
	ActInterestDateAppService actInterestDateAppService;
	@Autowired
	ActAccountDateAppService actAccountDateAppService;
	@Autowired
	ActCustInterestAppService actCustInterestAppService;
	
	@Override
	public BisActInterestDateAuditDto findById(String id) {
		return bisActInterestDateAuditDtoMapper.selectByPrimaryKey(id);
	}

	@Override
	public PageData<BisActInterestDateAuditDto> findPage(PageData<BisActInterestDateAuditDto> pageData,
			BisActInterestDateAuditDto queryParam) {
		PageHelper.startPage(pageData.getPageNumber(), pageData.getPageSize());
		List<BisActInterestDateAuditDto> items = bisActInterestDateAuditDtoMapper.list(queryParam);
		
		Page<BisActInterestDateAuditDto> page = (Page<BisActInterestDateAuditDto>)items;
		
		pageData.setRows(items);
		pageData.setTotal(page.getTotal());
		
		return pageData;
	}

	@Override
	public void save(BisActInterestDateAuditDto obj) {
		obj.setId(obj.getIdentity());
		obj.setOperateTime(new Date());
		if(StringUtils.isNotBlank(obj.getRefId())) {
			ActInterestDateDto actInterestDateDto = actInterestDateService.findById(obj.getRefId());
			if(actInterestDateDto == null) {
				throw new CbsUncheckedException("","获取客户结息日失败refID="+obj.getRefId());
			}
			//将对象转换为json字符串
			Gson gson = new Gson();
			obj.setRemark(gson.toJson(actInterestDateDto,ActInterestDateDto.class));
		}
		bisActInterestDateAuditDtoMapper.insert(obj);
	}

	@Override
	public void auditPass(String auditor, String auditorName, String id) {
		BisActInterestDateAuditDto actInterestDateAudit = bisActInterestDateAuditDtoMapper.selectByPrimaryKey(id);
		ActInterestDateDto oldActInterestDateDto = null;
		try {
			oldActInterestDateDto = actInterestDateAppService.findById(actInterestDateAudit.getRefId());
		} catch (AccountCheckedException e1) {
			_log.error("客户结息日审核通过失败：{}",e1);
			throw new CbsUncheckedException("","审核通过获取客户下一结息日记录失败");
		}
		if(actInterestDateAudit == null)return ;
		//保存信息到审核记录表
		saveBisAuditDto(auditor, auditorName, id,BISAuditStatus.AUDIT_PASS);
		//审核通过同步
		ActInterestDateDto actInterestDateDto = new ActInterestDateDto();
		BeanUtils.copyProperties(actInterestDateAudit, actInterestDateDto, new String[]{"id"} );
		//更新利率审核表
		actInterestDateAudit.setAuditStatus(BISAuditStatus.AUDIT_PASS);
		actInterestDateAudit.setAuditor(auditor);
		actInterestDateAudit.setAuditorName(auditorName);
		actInterestDateAudit.setAuditTime(new Date());
		bisActInterestDateAuditDtoMapper.updateByPrimaryKeySelective(actInterestDateAudit);
		try {
			actInterestDateDto.setId(actInterestDateAudit.getRefId());
			String accountDate = actAccountDateAppService.getAccountDate();
			/**
			 * 如果当前账务日期小于等于下一结息日，且下一结息日大于客户上一结息日
			 */
			String nextInterestDay = actInterestDateAudit.getNextInterestDay();
			String lastInterestDay = oldActInterestDateDto.getLastInterestDay();
			if(nextInterestDay.compareTo(accountDate)<=0){
				if(StringUtils.isBlank(lastInterestDay) || lastInterestDay.compareTo(nextInterestDay)<0){
					try {
						SuccessFailDealingDto msgResultStatus = actCustInterestAppService.manualSettleInterest(EAutoManual.MANUAL, nextInterestDay, actInterestDateAudit.getAccountId());
						//actCustInterestAppService.manualInterestClear(EAutoManual.MANUAL, actInterestDateAudit.getNextInterestDay(), actInterestDateAudit.getAccountId());
						if(msgResultStatus!=null && ESuccessFailDealing.FAIL==msgResultStatus.getSfd()){
							String errorString="调用账务结息失败";
							if(!StringUtils.isBlank(msgResultStatus.getMessage())){
								errorString=msgResultStatus.getMessage();
							}
							throw new AccountCheckedException("",errorString);
						}
					} catch (AccountCheckedException e) {
						throw new CbsUncheckedException("",e.getMessage());
					}catch (Exception e) {
						throw new CbsUncheckedException("","审核通过账务系统通讯异常");
					}
				}else{
					_log.info("下一结息日小于等于客户上一结息日,不允许审核通过");
					throw new CbsUncheckedException("","下一结息日小于等于客户上一结息日,不允许审核通过");
				}
			}else{
				/**
				 * 修改用户的结息日
				 */
				if(StringUtils.isNotBlank(actInterestDateAudit.getRefId())){
					actInterestDateAppService.updateSeletive(actInterestDateDto);
				} else {
					actInterestDateAppService.save(actInterestDateDto);
				}
			}	
		} catch (Exception e) {
			_log.error("审核通过同步客户计息日失败:{}",ExceptionProcUtil.getExceptionDesc(e));
			throw new CbsUncheckedException("",e.getMessage(),e);
		}
	}

	private void saveBisAuditDto(String auditor, String auditorName, String id,
			BISAuditStatus bisAuditStatus) {
		BisAuditDto auditDto = new BisAuditDto();
		auditDto.setId(auditDto.getIdentity());
		auditDto.setAuditor(auditor);
		auditDto.setAuditorName(auditorName);
		auditDto.setAuditStatus(bisAuditStatus);
		auditDto.setAuditTime(new Date());
		auditDto.setAuditType(BISAuditType.INTEREST_DAY);
		auditDto.setBusId(id);
		auditDto.setReason(null);
		bisAuditService.insert(auditDto);
	}

	@Override
	public void batchAuditPass(String auditor, String auditorName, List<String> idList) {
		if(idList == null || idList.isEmpty())return;
		for(String id : idList){
			auditPass(auditor, auditorName, id);
		}
	}

	@Override
	public void auditReject(String auditor, String auditorName, String reason, String id) {
		BisActInterestDateAuditDto actInterestDateAudit = bisActInterestDateAuditDtoMapper.selectByPrimaryKey(id);
		if(actInterestDateAudit == null)return ;
		//保存信息到审核记录表
		saveBisAuditDto(auditor, auditorName, id,BISAuditStatus.AUDIT_REJECT);
		//更新利率审核表
		actInterestDateAudit.setAuditStatus(BISAuditStatus.AUDIT_REJECT);
		actInterestDateAudit.setAuditor(auditor);
		actInterestDateAudit.setAuditorName(auditorName);
		actInterestDateAudit.setAuditTime(new Date());
		actInterestDateAudit.setRejectReason(reason);
		bisActInterestDateAuditDtoMapper.updateByPrimaryKeySelective(actInterestDateAudit);
	}

	@Override
	public void batchAuditReject(String auditor, String auditorName, String reason, List<String> idList) {
		if(idList == null || idList.isEmpty())return;
		for(String id : idList){
			auditReject(auditor, auditorName, reason, id);
		}
	}

	@Override
	public void cancel(String loginName,String realName,String id, String reason) {
		BisActInterestDateAuditDto actInterestDateAudit = bisActInterestDateAuditDtoMapper.selectByPrimaryKey(id);
		if(actInterestDateAudit == null)return ;
		//更新利率审核表
		actInterestDateAudit.setAuditStatus(BISAuditStatus.REVOKED);
		actInterestDateAudit.setCancelReason(reason);
		actInterestDateAudit.setAuditor(loginName);
		actInterestDateAudit.setAuditorName(realName);
		bisActInterestDateAuditDtoMapper.updateByPrimaryKeySelective(actInterestDateAudit);
	}

	@Override
	public void batchCancel(String loginName,String realName,List<String> idList, String reason) {
		if(idList == null || idList.isEmpty())return;
		for(String id : idList){
			cancel(loginName,realName,id,reason);
		}
	}

	@Override
	public long countWaitAuditWithAccountId(String accountId) {
		return bisActInterestDateAuditDtoMapper.countWaitAuditWithAccountId(accountId);
	}

	@Override
	public List<BisActInterestDateAuditDto> findWaitAudit(ArrayList<String> accountIds) {
		return bisActInterestDateAuditDtoMapper.findWaitAudit(accountIds);
	}

}
