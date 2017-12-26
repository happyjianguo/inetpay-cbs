package com.ylink.inetpay.cbs.ucs.sec.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ylink.inetpay.cbs.common.CbsConstants;
import com.ylink.inetpay.cbs.ucs.sec.dao.UcsSecDepartmentDtoMapper;
import com.ylink.inetpay.common.project.cbs.dto.ucs.sec.UcsSecDepartmentDto;
import com.ylink.inetpay.common.project.cbs.dto.ucs.sec.UcsSecUserDto;
import com.ylink.inetpay.common.project.cbs.exception.CbsUncheckedException;
import com.ylink.inetpay.common.project.cbs.exception.ECbsErrorCode;

@Transactional(value=CbsConstants.TX_MANAGER_UCS)
@Service("ucsSecDepartmentService")
public class UcsSecDepartmentServiceImpl implements UcsSecDepartmentService {

	@Autowired
	private UcsSecDepartmentDtoMapper ucsSecDepartmentDtoMapper;

	@Autowired
	private UcsSecUserService ucsSecUserService;
	
	@Override
	public UcsSecDepartmentDto findById(String id) {
		return ucsSecDepartmentDtoMapper.selectByPrimaryKey(id);
	}

	@Override
	public void updateSelective(UcsSecDepartmentDto ucsSecDepartment) {
		ucsSecDepartment.setUpdateTime(new Date());
		ucsSecDepartmentDtoMapper.updateByPrimaryKeySelective(ucsSecDepartment);
	}

	@Override
	public List<UcsSecDepartmentDto> getDeptTree() {
		List<UcsSecDepartmentDto> topDepts = ucsSecDepartmentDtoMapper.getTopDepts();
		getChildrenDepts(topDepts);
		return topDepts;
	}
	
	private List<String> getPids(List<UcsSecDepartmentDto> depts){
		if(depts != null && !depts.isEmpty()) {
			List<String> items = new ArrayList<String>();
			for(UcsSecDepartmentDto dept : depts){
				items.add(dept.getId());
			}
			return items;
		}
		return null;
	}
	
	private void getChildrenDepts(List<UcsSecDepartmentDto> depts){
		if(depts == null || depts.isEmpty())return;
		
		List<String> pids = getPids(depts);
		if(pids == null || pids.isEmpty())return ;
		
		List<UcsSecDepartmentDto> childrenDetps = ucsSecDepartmentDtoMapper.getChildrenDeptsByPids(pids);
		if(childrenDetps == null || childrenDetps.isEmpty())return;
		
		//子类分类
		Map<String,List<UcsSecDepartmentDto>> tmpMap = new HashMap<String,List<UcsSecDepartmentDto>>();
		List<UcsSecDepartmentDto> tmpList = null;
		for(UcsSecDepartmentDto child : childrenDetps){
			tmpList = tmpMap.get(child.getParentId());
			if(tmpList == null){
				tmpList = new ArrayList<UcsSecDepartmentDto>();
				tmpMap.put(child.getParentId(), tmpList);
			}
			tmpList.add(child);
		}
		//设置父节点的所有子节点
		for(UcsSecDepartmentDto p : depts){
			p.setChildren(tmpMap.get(p.getId()));
		}
		
		getChildrenDepts(childrenDetps);
	}

	@Override
	public void save(UcsSecDepartmentDto ucsSecDepartments) {
		ucsSecDepartments.setCreateTime(new Date());
		ucsSecDepartmentDtoMapper.insert(ucsSecDepartments);
	}

	@Override
	public List<UcsSecDepartmentDto> getChildDepts(String parentId) {
		return ucsSecDepartmentDtoMapper.getChildrenDepts(parentId);
	}

	@Override
	public void delete(String id) {
		List<UcsSecDepartmentDto> child = getChildDepts(id);
		if(child != null && !child.isEmpty()) {
			throw new CbsUncheckedException(ECbsErrorCode.SYS_ERROR.getValue(), "不能删除非叶子节点部门机构");
		}
		List<UcsSecUserDto>  userList = ucsSecUserService.listByDeptId(id);
		if(userList != null && !userList.isEmpty()) {
			throw new CbsUncheckedException(ECbsErrorCode.SYS_ERROR.getValue(), "有用户关联了该部门，不能删除");
		}
		ucsSecDepartmentDtoMapper.deleteByPrimaryKey(id);
	}

}
