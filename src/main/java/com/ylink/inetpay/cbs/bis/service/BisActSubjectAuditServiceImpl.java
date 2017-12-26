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
import com.ylink.inetpay.cbs.act.service.ActSubjectService;
import com.ylink.inetpay.cbs.bis.dao.BisActSubjectAuditDtoMapper;
import com.ylink.inetpay.cbs.common.CbsConstants;
import com.ylink.inetpay.common.core.constant.BISAuditStatus;
import com.ylink.inetpay.common.core.constant.BISAuditType;
import com.ylink.inetpay.common.core.constant.EOperateType;
import com.ylink.inetpay.common.core.util.ExceptionProcUtil;
import com.ylink.inetpay.common.project.account.app.ActSubjectAppService;
import com.ylink.inetpay.common.project.account.dto.ActSubjectDto;
import com.ylink.inetpay.common.project.account.exception.AccountCheckedException;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisActSubjectAuditDto;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisAuditDto;
import com.ylink.inetpay.common.project.cbs.exception.CbsUncheckedException;

@Service("bisActSubjectAuditService")
@Transactional(value=CbsConstants.TX_MANAGER_BIS)
public class BisActSubjectAuditServiceImpl implements BisActSubjectAuditService {

	private Logger _log = LoggerFactory.getLogger(BisActSubjectAuditServiceImpl.class);
	
	@Autowired
	BisActSubjectAuditDtoMapper bisActSubjectAuditDtoMapper;
	@Autowired
	ActSubjectService actSubjectService;
	@Autowired
	BisAuditService bisAuditService;
	@Autowired
	ActSubjectAppService actSubjectAppService;
	
	@Override
	public BisActSubjectAuditDto findById(String id) {
		return bisActSubjectAuditDtoMapper.selectByPrimaryKey(id);
	}

	@Override
	public PageData<BisActSubjectAuditDto> findPage(PageData<BisActSubjectAuditDto> pageData,
			BisActSubjectAuditDto queryParam) {
		PageHelper.startPage(pageData.getPageNumber(), pageData.getPageSize());
		List<BisActSubjectAuditDto> items = bisActSubjectAuditDtoMapper.list(queryParam);
		
		Page<BisActSubjectAuditDto> page = (Page<BisActSubjectAuditDto>)items;
		
		pageData.setRows(items);
		pageData.setTotal(page.getTotal());
		
		return pageData;
	}

	@Override
	public void changeActSubject(BisActSubjectAuditDto actSubjectAudit) {
		actSubjectAudit.setId(actSubjectAudit.getIdentity());
		actSubjectAudit.setCreateTime(new Date());
		if(StringUtils.isNotBlank(actSubjectAudit.getRefId())) {
			ActSubjectDto actSubjectDto = actSubjectService.findById(actSubjectAudit.getRefId());
			if(actSubjectDto == null) {
				throw new CbsUncheckedException("","获取科目失败refID="+actSubjectAudit.getRefId());
			}
			//将对象转换为json字符串
			Gson gson = new Gson();
			actSubjectAudit.setRemark(gson.toJson(actSubjectDto,ActSubjectDto.class));
		}
		bisActSubjectAuditDtoMapper.insert(actSubjectAudit);
	}

	@Override
	public long countBySubjectNo(String subjectNo) {
		return bisActSubjectAuditDtoMapper.countWaitAuditBySubjectNo(subjectNo);
	}

	@Override
	public void auditPass(String auditor, String auditorName, String id) {
		BisActSubjectAuditDto bisActSubjectAuditDto = bisActSubjectAuditDtoMapper.selectByPrimaryKey(id);
		if(bisActSubjectAuditDto == null)return ;
		//保存信息到审核记录表
		saveBisAuditDto(auditor, auditorName, id,BISAuditStatus.AUDIT_PASS);
		//审核通过同步科目信息
		ActSubjectDto actSubjectDto = new ActSubjectDto();
		if(bisActSubjectAuditDto.getOperateType() == EOperateType.NEW) {
			BeanUtils.copyProperties(bisActSubjectAuditDto, actSubjectDto, new String[]{"id"} );
			try {
				actSubjectAppService.save(actSubjectDto);
			} catch (Exception e) {
				_log.error("审核通过同步科目失败:{}",ExceptionProcUtil.getExceptionDesc(e));
				throw new CbsUncheckedException("","审核通过同步科目失败："+e.getMessage(),e);
			}
		} else if(bisActSubjectAuditDto.getOperateType() == EOperateType.EDIT){
			if(StringUtils.isBlank(bisActSubjectAuditDto.getRefId())) {
				throw new CbsUncheckedException("","修改时关联原科目失败refId="+bisActSubjectAuditDto.getRefId());
			}
			BeanUtils.copyProperties(bisActSubjectAuditDto, actSubjectDto, new String[]{"id"} );
			try {
				actSubjectDto.setId(bisActSubjectAuditDto.getRefId());
				actSubjectAppService.save(actSubjectDto);
			} catch (Exception e) {
				_log.error("审核通过同步科目失败:{}",ExceptionProcUtil.getExceptionDesc(e));
				throw new CbsUncheckedException("","审核通过同步科目失败："+e.getMessage(),e);
			}
		} else if(bisActSubjectAuditDto.getOperateType() == EOperateType.DELETE){
			try {
				actSubjectAppService.delete(bisActSubjectAuditDto.getRefId());
			} catch (AccountCheckedException e) {
				throw new CbsUncheckedException("","删除科目失败："+e.getMessage(),e);
			}
		}
		//更新科目审核表
		bisActSubjectAuditDto.setAuditStatus(BISAuditStatus.AUDIT_PASS);
		bisActSubjectAuditDto.setAuditor(auditor);
		bisActSubjectAuditDto.setAuditorName(auditorName);
		bisActSubjectAuditDto.setAuditTime(new Date());
		bisActSubjectAuditDtoMapper.updateByPrimaryKeySelective(bisActSubjectAuditDto);
	}

	private void saveBisAuditDto(String auditor, String auditorName, String id,
			BISAuditStatus bisAuditStatus) {
		BisAuditDto auditDto = new BisAuditDto();
		auditDto.setId(auditDto.getIdentity());
		auditDto.setAuditor(auditor);
		auditDto.setAuditorName(auditorName);
		auditDto.setAuditStatus(bisAuditStatus);
		auditDto.setAuditTime(new Date());
		auditDto.setAuditType(BISAuditType.SUBJECT);
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
	public void auditReject(String auditor, String auditorName, String id) {
		BisActSubjectAuditDto bisActSubjectAuditDto = bisActSubjectAuditDtoMapper.selectByPrimaryKey(id);
		if(bisActSubjectAuditDto == null)return ;
		//保存信息到审核记录表
		saveBisAuditDto(auditor, auditorName, id,BISAuditStatus.AUDIT_REJECT);
		//更新记账规则审核表
		bisActSubjectAuditDto.setAuditStatus(BISAuditStatus.AUDIT_REJECT);
		bisActSubjectAuditDto.setAuditor(auditor);
		bisActSubjectAuditDto.setAuditorName(auditorName);
		bisActSubjectAuditDto.setAuditTime(new Date());
		bisActSubjectAuditDtoMapper.updateByPrimaryKeySelective(bisActSubjectAuditDto);
	}

	@Override
	public void batchAuditReject(String auditor, String auditorName, List<String> idList) {
		if(idList == null || idList.isEmpty())return;
		for(String id : idList){
			auditReject(auditor, auditorName, id);
		}
	}

	@Override
	public void cancel(String id) {
		BisActSubjectAuditDto bisActSubjectAuditDto = bisActSubjectAuditDtoMapper.selectByPrimaryKey(id);
		if(bisActSubjectAuditDto == null)return ;
		//更新记账规则审核表
		bisActSubjectAuditDto.setAuditStatus(BISAuditStatus.REVOKED);
		bisActSubjectAuditDtoMapper.updateByPrimaryKeySelective(bisActSubjectAuditDto);
	}

	@Override
	public void batchCancel(List<String> idList) {
		if(idList == null || idList.isEmpty())return;
		for(String id : idList){
			cancel(id);
		}
	}

	@Override
	public long getAuditNum(List<String> ids, BISAuditStatus auditReject) {
		return bisActSubjectAuditDtoMapper.getAuditNum(ids,auditReject);
	}
}
