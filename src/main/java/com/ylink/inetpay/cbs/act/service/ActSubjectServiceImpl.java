package com.ylink.inetpay.cbs.act.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.ylink.inetpay.cbs.act.dao.ActSubjectDtoMapper;
import com.ylink.inetpay.common.core.constant.EActBusiRefSubBusiType;
import com.ylink.inetpay.common.core.constant.EBookType;
import com.ylink.inetpay.common.project.account.dto.ActSubjectDto;
import com.ylink.inetpay.common.project.account.dto.ActSubjectListDto;
import com.ylink.inetpay.common.project.cbs.constant.ParentSubjectFlag;

@Service("actSubjectService")
public class ActSubjectServiceImpl implements ActSubjectService {
	@Autowired
	ActSubjectDtoMapper actSubjectDtoMapper;

	@Override
	public ActSubjectListDto queryAllDataByTree(ActSubjectDto actSubjectDto) {
		ActSubjectListDto actSubjectListDto = new ActSubjectListDto();
		// 根据参数查询视图
		List<ActSubjectDto> list = actSubjectDtoMapper
				.queryAllData(actSubjectDto);
		List<ActSubjectDto> parentList = new ArrayList<ActSubjectDto>();
		List<ActSubjectDto> subList = null;
		for (int i = 0; i < list.size(); i++) {
			ActSubjectDto parentDto = list.get(i);
			// 如果是父级，则直接保存。如果有子级则组List
			if (parentDto.getParentSubject() == null||ParentSubjectFlag.PARENT.getValue().equals(
					parentDto.getParentSubject())) {
				subList = new ArrayList<ActSubjectDto>();
				parentDto.setSubList(subList);
				parentList.add(parentDto);
			} else {
				subList.add(parentDto);
			}
		}
		actSubjectListDto.setActSubjectList(parentList);
		return actSubjectListDto;
	}

	@Override
	public ActSubjectDto findById(String id) {
		return actSubjectDtoMapper.selectByPrimaryKey(id);
	}

	@Override
	public List<ActSubjectDto> findBySubjecNo(String subjectNo) {
		return actSubjectDtoMapper.findBySubjecNo(subjectNo);
	}

	@Override
	public PageData<ActSubjectDto> listPage(PageData<ActSubjectDto> pageData, ActSubjectDto actSubjectDto) {
		PageHelper.startPage(pageData.getPageNumber(), pageData.getPageSize());
		List<ActSubjectDto> items = actSubjectDtoMapper.list(actSubjectDto);
		
		Page<ActSubjectDto> page = (Page<ActSubjectDto>)items;
		
		pageData.setRows(items);
		pageData.setTotal(page.getTotal());
		
		return pageData;
	}

	@Override
	public List<ActSubjectDto> getSubjects(String value) {
		return actSubjectDtoMapper.getSubjects(value);
	}
	@Override
	public List<ActSubjectDto> findLevelOneSubject() {
		return actSubjectDtoMapper.findLevelOneSubject();
	}

	@Override
	public List<ActSubjectDto> findByParentSubjectNo(String subjectNo) {
		return actSubjectDtoMapper.findByParentSubjectNo(subjectNo);
	}

	@Override
	public List<ActSubjectDto> findLevelOneSubject(EBookType bookType) {
		return actSubjectDtoMapper.findLevelOneSubjectByBookType(bookType);
	}
	
	@Override
	public List<ActSubjectDto> findByAcctTypeNo(String acctTypeNo) {
		return actSubjectDtoMapper.findByAcctTypeNo(acctTypeNo,EActBusiRefSubBusiType.getTypeEnum());
	}

}
