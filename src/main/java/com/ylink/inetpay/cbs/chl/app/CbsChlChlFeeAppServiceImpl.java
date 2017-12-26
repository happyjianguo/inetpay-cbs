package com.ylink.inetpay.cbs.chl.app;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.cbs.chl.service.CbsChlChlFeeService;
import com.ylink.inetpay.common.core.constant.EStatus;
import com.ylink.inetpay.common.project.cbs.exception.CbsCheckedException;
import com.ylink.inetpay.common.project.channel.app.CbsChlChlFeeAppService;
import com.ylink.inetpay.common.project.channel.dto.ChlChlFeeDto;
/** 渠道银行表**/
@Service("cbsChlChlFeeAppService")
public class CbsChlChlFeeAppServiceImpl implements CbsChlChlFeeAppService {
	@Autowired
	private CbsChlChlFeeService cbsChlChlFeeService;
	@Override
	public PageData<ChlChlFeeDto> findAll(PageData<ChlChlFeeDto> pageData, ChlChlFeeDto queryParam) {
		return cbsChlChlFeeService.findAll(pageData, queryParam);
	}

	@Override
	public int saveTemplate(ChlChlFeeDto template) throws CbsCheckedException {
		return cbsChlChlFeeService.saveTemplate(template);
	}

	@Override
	public int updateTemplate(ChlChlFeeDto tenplate) throws CbsCheckedException {
		return cbsChlChlFeeService.updateTemplate(tenplate);
	}

	@Override
	public int deleteTemplate(String id) throws CbsCheckedException {
		return cbsChlChlFeeService.deleteTemplate(id);
	}

	@Override
	public int blockUp(EStatus status, String id) throws CbsCheckedException {
		return cbsChlChlFeeService.blockUp(status, id);
	}

	@Override
	public ChlChlFeeDto findById(String id) {
		return cbsChlChlFeeService.findById(id);
	}

	@Override
	public List<ChlChlFeeDto> queryAllChannels() throws CbsCheckedException {
		return cbsChlChlFeeService.queryAllChannels();
	}
	
}
