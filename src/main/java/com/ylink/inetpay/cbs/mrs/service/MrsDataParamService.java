package com.ylink.inetpay.cbs.mrs.service;

import java.util.List;

import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.common.core.constant.MrsDataParamType;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsDataParamDto;
import com.ylink.inetpay.common.project.portal.vo.customer.DataParamVO;

public interface MrsDataParamService {

	public PageData<MrsDataParamDto> findDataParam(PageData<MrsDataParamDto> pageData, MrsDataParamDto queryParam);
	
	MrsDataParamDto findByCodeType(String code, String type);

	public List<DataParamVO> findByType(MrsDataParamType typeEnum);
	
}
