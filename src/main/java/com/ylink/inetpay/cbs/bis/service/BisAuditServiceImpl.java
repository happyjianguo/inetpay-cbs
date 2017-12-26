package com.ylink.inetpay.cbs.bis.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.ylink.inetpay.cbs.bis.dao.BisAuditDtoMapper;
import com.ylink.inetpay.common.core.constant.BISAuditStatus;
import com.ylink.inetpay.common.core.constant.BISAuditType;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisAuditDto;
@Service("bisAuditService")
public class BisAuditServiceImpl implements  BisAuditService{
	@Autowired
	private BisAuditDtoMapper bisAuditDtoMapper;
	
	@Override
	public List<BisAuditDto> getByCond(String busId) {
		 List<BisAuditDto> list = bisAuditDtoMapper.getByCond(busId);
		 return list;
	}

	@Override
	public void insert(BisAuditDto bisAuditDto) {
		bisAuditDtoMapper.insert(bisAuditDto);		
	}

	@Override
	public boolean isAudit(String id, String loginName,BISAuditType auditType) {
		List<BisAuditDto> bisAuditDtos=bisAuditDtoMapper.isAudit(id, loginName,auditType);
		if(bisAuditDtos.size()>0){
			return true;
		}
		return false;
	}
	
	@Override
	public List<BisAuditDto> findBisAuditDtoByLoginName(String loginName,List<String> ids,BISAuditType auditType){
		return bisAuditDtoMapper.findBisAuditDtoByLoginName(loginName,ids,auditType);
	}

	@Override
	public PageData<BisAuditDto> findListPage(PageData<BisAuditDto> pageData, BisAuditDto queryParam) {
		PageHelper.startPage(pageData.getPageNumber(), pageData.getPageSize());
		List<BisAuditDto> list = bisAuditDtoMapper.queryAllToList(queryParam);
		Page<BisAuditDto> page = (Page<BisAuditDto>) list;
		pageData.setTotal(page.getTotal());
		pageData.setRows(list);
		return pageData;
	}

	@Override
	public BisAuditDto getAudit(String id) {
		return bisAuditDtoMapper.selectByPrimaryKey(id);
	}

	@Override
	public List<BisAuditDto> findListBybusId(String id, BISAuditType auditType) {
		return bisAuditDtoMapper.findListBybusId(id,auditType);
	}

	@Override
	public boolean isExistAucit(String id, String loginName, BISAuditType auditType) {
		List<BisAuditDto> bisAuditDtos=bisAuditDtoMapper.isAudit(id, loginName,auditType);
		if(bisAuditDtos.size()>0 && bisAuditDtos.get(0).getAuditType()==auditType){
			return true;
		}
		return false;
	}

	@Override
	public long getAucitNum(List<String> ids, String loginName, BISAuditStatus auditStatus,BISAuditType auditType) {
		return bisAuditDtoMapper.getAucitNum(ids, loginName,auditStatus,auditType);
	}

	@Override
	public List<BisAuditDto> findFrozenListBybusId(String id, BISAuditType amountFrozen) {
		return bisAuditDtoMapper.findFrozenListBybusId(id,amountFrozen);
	}

	@Override
	public List<BisAuditDto> findRefundListBybusIds(List<String> busiIds, BISAuditType busiRefund,
			BISAuditType merRefund, String loginName) {
		return bisAuditDtoMapper.findRefundListBybusIds(busiIds,busiRefund,merRefund,loginName);
	}
	
}
