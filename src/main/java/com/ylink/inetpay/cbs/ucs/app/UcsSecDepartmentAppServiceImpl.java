package com.ylink.inetpay.cbs.ucs.app;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ylink.inetpay.cbs.ucs.sec.service.UcsSecDepartmentService;
import com.ylink.inetpay.common.project.cbs.app.UcsSecDepartmentAppService;
import com.ylink.inetpay.common.project.cbs.dto.ucs.sec.UcsSecDepartmentDto;
import com.ylink.inetpay.common.project.cbs.exception.CbsCheckedException;

@Service("ucsSecDepartmentAppService")
public class UcsSecDepartmentAppServiceImpl implements
		UcsSecDepartmentAppService {
	
	@Autowired
	private UcsSecDepartmentService ucsSecDepartmentService;

	@Override
	public UcsSecDepartmentDto findById(String id) throws CbsCheckedException {
		return ucsSecDepartmentService.findById(id);
	}

	@Override
	public void save(UcsSecDepartmentDto ucsSecDepartments)
			throws CbsCheckedException {
		ucsSecDepartmentService.save(ucsSecDepartments);
	}

	@Override
	public void updateSelective(UcsSecDepartmentDto ucsSecDepartment)
			throws CbsCheckedException {
		ucsSecDepartmentService.updateSelective(ucsSecDepartment);
	}

	@Override
	public List<UcsSecDepartmentDto> getDeptTree() throws CbsCheckedException {
		return ucsSecDepartmentService.getDeptTree();
	}

	@Override
	public List<UcsSecDepartmentDto> getChildDepts(String parentId)
			throws CbsCheckedException {
		return ucsSecDepartmentService.getChildDepts(parentId);
	}

	@Override
	public void delete(String id) throws CbsCheckedException {
		ucsSecDepartmentService.delete(id);
	}

}
