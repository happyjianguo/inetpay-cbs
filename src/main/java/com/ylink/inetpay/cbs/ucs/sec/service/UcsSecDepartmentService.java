package com.ylink.inetpay.cbs.ucs.sec.service;

import java.util.List;

import com.ylink.inetpay.common.project.cbs.dto.ucs.sec.UcsSecDepartmentDto;

public interface UcsSecDepartmentService {

	public UcsSecDepartmentDto findById(String id);
	
	public void save(UcsSecDepartmentDto ucsSecDepartments);
	
	public void updateSelective(UcsSecDepartmentDto ucsSecDepartment);
	
	public List<UcsSecDepartmentDto> getDeptTree();
	
	public List<UcsSecDepartmentDto> getChildDepts(String parentId);
	
	public void delete(String id);
}
