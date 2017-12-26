package com.ylink.inetpay.cbs.cls.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.ylink.inetpay.cbs.cls.dao.ClsAuditDtoMapper;
import com.ylink.inetpay.common.core.constant.BISAuditType;
import com.ylink.inetpay.common.project.clear.dto.ClsAuditDto;
@Service("clsAuditService")
public class ClsAuditServiceImpl implements  ClsAuditService{
	@Autowired
	private ClsAuditDtoMapper clsAuditDtoMapper;
	
	@Override
	public List<ClsAuditDto> getByCond(String busId) {
		 List<ClsAuditDto> list = clsAuditDtoMapper.getByCond(busId);
		 return list;
	}

	@Override
	public void insert(ClsAuditDto bisAuditDto) {
		clsAuditDtoMapper.insert(bisAuditDto);		
	}

	@Override
	public boolean isAudit(String id, String loginName) {
		List<ClsAuditDto> bisAuditDtos=clsAuditDtoMapper.isAudit(id, loginName);
		if(bisAuditDtos.size()>0){
			return true;
		}
		return false;
	}

	@Override
	public PageData<ClsAuditDto> findListPage(PageData<ClsAuditDto> pageData, ClsAuditDto queryParam) {
		PageHelper.startPage(pageData.getPageNumber(), pageData.getPageSize());
		List<ClsAuditDto> list = clsAuditDtoMapper.queryAllToList(queryParam);
		Page<ClsAuditDto> page = (Page<ClsAuditDto>) list;
		pageData.setTotal(page.getTotal());
		pageData.setRows(list);
		return pageData;
	}

	@Override
	public ClsAuditDto getAudit(String id) {
		return clsAuditDtoMapper.selectByPrimaryKey(id);
	}

	@Override
	public List<ClsAuditDto> findListBybusId(String id, BISAuditType auditType) {
		return clsAuditDtoMapper.findListBybusId(id,auditType);
	}
	
}
