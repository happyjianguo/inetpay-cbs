package com.ylink.inetpay.cbs.ucs.sec.service;

import static org.junit.Assert.fail;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ylink.inetpay.cbs.UCBaseTest;
import com.ylink.inetpay.common.project.cbs.dto.ucs.sec.UcsSecDepartmentDto;

public class UcsSecDepartmentServiceTest extends UCBaseTest {
	
	@Autowired
	UcsSecDepartmentService ucsSecDepartmentService;

	@Test
	public void testFindById() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testSave() {
		UcsSecDepartmentDto ucsSecDepartment = new UcsSecDepartmentDto();
		ucsSecDepartment.setDepartmentName("部门");
		ucsSecDepartment.setSeq(1);
		ucsSecDepartment.setRemark(ucsSecDepartment.getDepartmentName());
		ucsSecDepartmentService.save(ucsSecDepartment);
		saveChild(ucsSecDepartment);
	}

	private void saveChild(UcsSecDepartmentDto ucsSecDepartment){
		for(int i =1 ; i <=5 ; i++){
			UcsSecDepartmentDto temp = new UcsSecDepartmentDto();
			temp.setDepartmentName(ucsSecDepartment.getDepartmentName() + i);
			temp.setParentId(ucsSecDepartment.getId());
			temp.setSeq(i);
			temp.setRemark(temp.getDepartmentName());
			ucsSecDepartmentService.save(temp);
			if(temp.getDepartmentName().matches(".*[1-9]{5,}$")) {
				return ;
			}
			saveChild(temp);
		}
	}
	
	public static void main(String[] args) {
		System.out.println("部门222212156222".matches(".*[1-9]{10,}$"));
	}
	

	@Test
	public void testUpdateSelective() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetDeptTree() {
		fail("Not yet implemented");
	}

}
