package com.ylink.inetpay.cbs.act.app;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.cbs.act.service.ActSubjectService;
import com.ylink.inetpay.common.core.constant.EBookType;
import com.ylink.inetpay.common.project.account.dto.ActSubjectDto;
import com.ylink.inetpay.common.project.account.dto.ActSubjectListDto;
import com.ylink.inetpay.common.project.cbs.app.CbsActSubjectAppService;
import com.ylink.inetpay.common.project.cbs.exception.CbsCheckedException;

@Service("cbsActSubjectAppService")
public class ActSubjectAppServiceImpl implements CbsActSubjectAppService {
	@Autowired
	ActSubjectService actSubjectService;

	@Override
	public ActSubjectListDto queryAllDataByTree(ActSubjectDto actSubjectDto) {
		return actSubjectService.queryAllDataByTree(actSubjectDto);
	}

	@Override
	public ActSubjectDto findById(String id) throws CbsCheckedException {
		return actSubjectService.findById(id);
	}

	@Override
	public List<ActSubjectDto> findBySubjectNo(String subjectNo) throws CbsCheckedException {
		return actSubjectService.findBySubjecNo(subjectNo);
	}

	@Override
	public PageData<ActSubjectDto> listPage(PageData<ActSubjectDto> pageData, ActSubjectDto actSubjectDto)
			throws CbsCheckedException {
		return actSubjectService.listPage(pageData, actSubjectDto);
	}

	@Override
	public List<ActSubjectDto> getSubjects(String value) {
		return actSubjectService.getSubjects(value);
	}


	@Override
	public List<ActSubjectDto> findLevelOneSubject() throws CbsCheckedException {
		return actSubjectService.findLevelOneSubject();
	}

	@Override
	public List<ActSubjectDto> findByParentSubjectNo(String subjectNo) {
		return actSubjectService.findByParentSubjectNo(subjectNo);
	}

	@Override
	public List<ActSubjectDto> findLevelOneSubject(EBookType bookTypeEnum) {
		return actSubjectService.findLevelOneSubject(bookTypeEnum);
	}

	@Override
	public List<ActSubjectDto> findByAcctTypeNo(String acctTypeNo) {
		return actSubjectService.findByAcctTypeNo(acctTypeNo);
	}
}
