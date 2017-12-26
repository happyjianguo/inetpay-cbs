package com.ylink.inetpay.cbs.bis.service;

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
import com.ylink.inetpay.cbs.act.service.ActRuleService;
import com.ylink.inetpay.cbs.bis.dao.BisActRuleAuditDtoMapper;
import com.ylink.inetpay.cbs.common.CbsConstants;
import com.ylink.inetpay.common.core.constant.BISAuditStatus;
import com.ylink.inetpay.common.core.constant.BISAuditType;
import com.ylink.inetpay.common.core.constant.EActRuleType;
import com.ylink.inetpay.common.core.constant.EOperateType;
import com.ylink.inetpay.common.core.constant.ETradeType;
import com.ylink.inetpay.common.core.util.ExceptionProcUtil;
import com.ylink.inetpay.common.project.account.app.ActRuleAppService;
import com.ylink.inetpay.common.project.account.dto.ActRuleDto;
import com.ylink.inetpay.common.project.account.exception.AccountCheckedException;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisActRuleAuditDto;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisAuditDto;
import com.ylink.inetpay.common.project.cbs.exception.CbsUncheckedException;

@Service("bisActRuleAuditService")
@Transactional(value=CbsConstants.TX_MANAGER_BIS)
public class BisActRuleAuditServiceImpl implements BisActRuleAuditService {
	
	private Logger _log = LoggerFactory.getLogger(BisActRuleAuditServiceImpl.class);
	
	@Autowired
	BisActRuleAuditDtoMapper bisActRuleAuditDtoMapper;
	@Autowired
	BisAuditService bisAuditService;
	@Autowired
	ActRuleAppService actRuleAppService;
	@Autowired
	ActRuleService actRuleService;
	

	@Override
	public PageData<BisActRuleAuditDto> findPage(PageData<BisActRuleAuditDto> pageData, BisActRuleAuditDto queryParam) {
		PageHelper.startPage(pageData.getPageNumber(), pageData.getPageSize());
		List<BisActRuleAuditDto> items = bisActRuleAuditDtoMapper.list(queryParam);
		
		Page<BisActRuleAuditDto> page = (Page<BisActRuleAuditDto>)items;
		
		pageData.setRows(items);
		pageData.setTotal(page.getTotal());
		
		return pageData;
	}

	@Override
	public void changeActRule(BisActRuleAuditDto actRuleAudit) {
		actRuleAudit.setId(actRuleAudit.getIdentity());
		actRuleAudit.setCreateTime(new Date());
		if(StringUtils.isNotBlank(actRuleAudit.getRefId())) {
			ActRuleDto actRuleDto = actRuleService.findById(actRuleAudit.getRefId());
			if(actRuleDto == null) {
				throw new CbsUncheckedException("","获取记账规则失败refID="+actRuleAudit.getRefId());
			}
			//将对象转换为json字符串
			Gson gson = new Gson();
			actRuleAudit.setRemark(gson.toJson(actRuleDto,ActRuleDto.class));
		}
		bisActRuleAuditDtoMapper.insert(actRuleAudit);
	}
	
	@Override
	public void auditPass(String auditor, String auditorName, String id) {
		BisActRuleAuditDto actRuleAudit = bisActRuleAuditDtoMapper.selectByPrimaryKey(id);
		if(actRuleAudit == null)return ;
		//判断是否已经审核过
		if(BISAuditStatus.AUDIT_PASS==actRuleAudit.getAuditStatus()){
			throw new CbsUncheckedException("","审核失败：不能重复审核");
		}
		//保存信息到审核记录表
		saveBisAuditDto(auditor, auditorName, id,BISAuditStatus.AUDIT_PASS);
		//审核通过同步记账规则信息
		ActRuleDto actRuleDto = new ActRuleDto();
		if(actRuleAudit.getOperateType() == EOperateType.NEW) {
			BeanUtils.copyProperties(actRuleAudit, actRuleDto, new String[]{"id"} );
			try {
				actRuleAppService.save(actRuleDto);
			} catch (Exception e) {
				_log.error("审核通过同步记账规则失败:{}",ExceptionProcUtil.getExceptionDesc(e));
				throw new CbsUncheckedException("","审核通过同步记账规则失败："+e.getMessage(),e);
			}
		} else if(actRuleAudit.getOperateType() == EOperateType.EDIT){
			if(StringUtils.isBlank(actRuleAudit.getRefId())) {
				throw new CbsUncheckedException("","修改时关联原记账规则失败refId="+actRuleAudit.getRefId());
			}
			BeanUtils.copyProperties(actRuleAudit, actRuleDto, new String[]{"id"} );
			try {
				actRuleDto.setId(actRuleAudit.getRefId());
				actRuleAppService.save(actRuleDto);
			} catch (Exception e) {
				_log.error("审核通过同步记账规则失败:{}",ExceptionProcUtil.getExceptionDesc(e));
				throw new CbsUncheckedException("","审核通过同步记账规则失败："+e.getMessage(),e);
			}
		} else if(actRuleAudit.getOperateType() == EOperateType.DELETE){
			try {
				actRuleAppService.delete(actRuleAudit.getRefId());
			} catch (AccountCheckedException e) {
				throw new CbsUncheckedException("","删除记账规则失败："+e.getMessage(),e);
			}
		}
		//更新记账规则审核表
		actRuleAudit.setAuditStatus(BISAuditStatus.AUDIT_PASS);
		actRuleAudit.setAuditor(auditor);
		actRuleAudit.setAuditorName(auditorName);
		actRuleAudit.setAuditTime(new Date());
		bisActRuleAuditDtoMapper.updateByPrimaryKeySelective(actRuleAudit);
	}

	private void saveBisAuditDto(String auditor, String auditorName, String id,
			BISAuditStatus bisAuditStatus) {
		BisAuditDto auditDto = new BisAuditDto();
		auditDto.setId(auditDto.getIdentity());
		auditDto.setAuditor(auditor);
		auditDto.setAuditorName(auditorName);
		auditDto.setAuditStatus(bisAuditStatus);
		auditDto.setAuditTime(new Date());
		auditDto.setAuditType(BISAuditType.RULE_TYPE);
		auditDto.setBusId(id);
		auditDto.setReason(null);
		bisAuditService.insert(auditDto);
	}

	@Override
	public void auditReject(String auditor, String auditorName, String id) {
		BisActRuleAuditDto actRuleAudit = bisActRuleAuditDtoMapper.selectByPrimaryKey(id);
		if(actRuleAudit == null)return ;
		//判断是否已经审核过
		if(BISAuditStatus.AUDIT_REJECT==actRuleAudit.getAuditStatus()){
			throw new CbsUncheckedException("","审核失败：不能重复审核");
		}
		//保存信息到审核记录表
		saveBisAuditDto(auditor, auditorName, id,BISAuditStatus.AUDIT_REJECT);
		//更新记账规则审核表
		actRuleAudit.setAuditStatus(BISAuditStatus.AUDIT_REJECT);
		actRuleAudit.setAuditor(auditor);
		actRuleAudit.setAuditorName(auditorName);
		actRuleAudit.setAuditTime(new Date());
		bisActRuleAuditDtoMapper.updateByPrimaryKeySelective(actRuleAudit);
	}

	@Override
	public void cancel(String id) {
		BisActRuleAuditDto actRuleAudit = bisActRuleAuditDtoMapper.selectByPrimaryKey(id);
		if(actRuleAudit == null)return ;
		//更新记账规则审核表
		actRuleAudit.setAuditStatus(BISAuditStatus.REVOKED);
		bisActRuleAuditDtoMapper.updateByPrimaryKeySelective(actRuleAudit);
	}

	@Override
	public long countWaitAuditByTradeTypeAndRuleType(ETradeType tradeType, EActRuleType ruleType) {
		return bisActRuleAuditDtoMapper.countWaitAuditByTradeTypeAndRuleType(tradeType, ruleType);
	}

	@Override
	public void batchAuditPass(String auditor, String auditorName, List<String> idList) {
		if(idList == null || idList.isEmpty())return;
		for(String id : idList){
			auditPass(auditor, auditorName, id);
		}
	}

	@Override
	public void batchAuditReject(String auditor, String auditorName, List<String> idList) {
		if(idList == null || idList.isEmpty())return;
		for(String id : idList){
			auditReject(auditor, auditorName, id);
		}
	}

	@Override
	public void batchCancel(List<String> idList) {
		if(idList == null || idList.isEmpty())return;
		for(String id : idList){
			cancel(id);
		}
	}

	@Override
	public BisActRuleAuditDto findById(String id) {
		return bisActRuleAuditDtoMapper.selectByPrimaryKey(id);
	}

	@Override
	public long getAuditNum(List<String> ids, BISAuditStatus auditPass) {
		return bisActRuleAuditDtoMapper.getAuditNum(ids,auditPass);
	}

}
