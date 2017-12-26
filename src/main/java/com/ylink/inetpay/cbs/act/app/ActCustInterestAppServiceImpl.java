package com.ylink.inetpay.cbs.act.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.cbs.act.service.ActCustInterestService;
import com.ylink.inetpay.common.project.account.dto.ActCustInterestDto;
import com.ylink.inetpay.common.project.cbs.app.CbsActCustInterestAppService;
import com.ylink.inetpay.common.project.cbs.exception.CbsCheckedException;


@Service("cbsActCustInterestAppService")
public class ActCustInterestAppServiceImpl implements CbsActCustInterestAppService {

	@Autowired
	ActCustInterestService actCustInterestService;
	
	@Override
	public PageData<ActCustInterestDto> findPage(PageData<ActCustInterestDto> pageData, ActCustInterestDto queryParam)
			throws CbsCheckedException {
		return actCustInterestService.findPage(pageData, queryParam);
	}

	@Override
	public ActCustInterestDto findById(String id) throws CbsCheckedException {
		return actCustInterestService.findById(id);
	}

}
