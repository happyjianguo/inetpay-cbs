package com.ylink.inetpay.cbs.mrs.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.ylink.inetpay.cbs.mrs.dao.MrsDataParamDtoMapper;
import com.ylink.inetpay.common.core.constant.MrsDataParamType;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsDataParamDto;
import com.ylink.inetpay.common.project.portal.vo.customer.DataParamVO;

@Service("mrsDataParamDtoService")
public class MrsDataParamServiceImpl implements MrsDataParamService {

	@Autowired
	private MrsDataParamDtoMapper mrsDataParamDtoMapper;
	
	@Override
	public PageData<MrsDataParamDto> findDataParam(PageData<MrsDataParamDto> pageData, MrsDataParamDto queryParam) {
		PageHelper.startPage(pageData.getPageNumber(), pageData.getPageSize());
		List<MrsDataParamDto> list = mrsDataParamDtoMapper.list(queryParam);
		Page<MrsDataParamDto> page = (Page<MrsDataParamDto>) list;
		pageData.setTotal(page.getTotal());
		pageData.setRows(list);
		return pageData;
	}

	@Override
	public MrsDataParamDto findByCodeType(String code, String type) {
		return mrsDataParamDtoMapper.findByCodeType(code, type);
	}

	@Override
	public List<DataParamVO> findByType(MrsDataParamType typeEnum) {
		List<DataParamVO> list = mrsDataParamDtoMapper.findByType(typeEnum.getValue());
		return list;
	}

}