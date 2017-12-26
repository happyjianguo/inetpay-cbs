package com.ylink.inetpay.cbs.act.app;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.cbs.act.service.ActInterestDateService;
import com.ylink.inetpay.common.project.account.app.ActInterestDateAppService;
import com.ylink.inetpay.common.project.account.dto.ActInterestDateDto;
import com.ylink.inetpay.common.project.account.exception.AccountCheckedException;
import com.ylink.inetpay.common.project.cbs.app.CbsActInterestDateAppService;
import com.ylink.inetpay.common.project.cbs.exception.CbsCheckedException;

@Service("cbsActInterestDateAppService")
public class CbsActInterestDateAppServiceImpl implements CbsActInterestDateAppService {

	@Autowired
	ActInterestDateService actInterestDateService;
	@Autowired
	ActInterestDateAppService actInterestDateAppService;

	@Override
	public PageData<ActInterestDateDto> findPage(PageData<ActInterestDateDto> pageData, ActInterestDateDto queryParam)
			throws CbsCheckedException {
		return actInterestDateService.findPage(pageData, queryParam);
	}

	@Override
	public ActInterestDateDto findById(String id) throws CbsCheckedException {
		return actInterestDateService.findById(id);
	}

	@Override
	public void saveOrUpate(ActInterestDateDto actInterestDate) throws CbsCheckedException {
		try {
			if (StringUtils.isNotBlank(actInterestDate.getId())) {
				actInterestDateAppService.updateSeletive(actInterestDate);
			} else {
				actInterestDateAppService.save(actInterestDate);
			}
		} catch (AccountCheckedException e) {
			throw new CbsCheckedException(e.getCode(), e.getMessage(),e);
		}
	}

}
