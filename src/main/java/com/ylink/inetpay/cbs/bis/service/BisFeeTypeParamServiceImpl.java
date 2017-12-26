package com.ylink.inetpay.cbs.bis.service;
 
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.ylinkpay.framework.core.model.PageData;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.ylink.inetpay.cbs.bis.dao.BisFeeTypeParamDtoMapper;
import com.ylink.inetpay.cbs.common.CbsConstants;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisFeeTypeParamDto;
 
 
@Service("bisFeeTypeParamService")
@Transactional(value=CbsConstants.TX_MANAGER_BIS)
public class BisFeeTypeParamServiceImpl implements BisFeeTypeParamService {
	@Autowired
	private  BisFeeTypeParamDtoMapper bisFeeTypeParamDtoMapper;
	
	@Override
	public PageData<BisFeeTypeParamDto> pageList(PageData<BisFeeTypeParamDto> pageData, BisFeeTypeParamDto queryParam) {
		PageHelper.startPage(pageData.getPageNumber(), pageData.getPageSize());
		List<BisFeeTypeParamDto> items = bisFeeTypeParamDtoMapper.list(queryParam);
		Page<BisFeeTypeParamDto> page = (Page<BisFeeTypeParamDto> )items;
		pageData.setRows(items);
		pageData.setTotal(page.getTotal());
		return pageData;
	}

	@Override
	public void saveBisFeeTypeParam(BisFeeTypeParamDto bisFeeTypeParamDto) {
		bisFeeTypeParamDto.setId(bisFeeTypeParamDto.getIdentity());
		bisFeeTypeParamDtoMapper.insert(bisFeeTypeParamDto);
		
	}

	@Override
	public BisFeeTypeParamDto details(String viewId) {
		 
		return bisFeeTypeParamDtoMapper.selectByPrimaryKey(viewId);
	}

	@Override
	public void updateAccessorDto(BisFeeTypeParamDto feeTypeParamDto) {
		  bisFeeTypeParamDtoMapper.updateByPrimaryKeySelective(feeTypeParamDto);
	}
	 
}

