package com.ylink.inetpay.cbs.bis.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.ylinkpay.framework.core.model.PageData;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.ylink.inetpay.cbs.bis.cache.BisSmsTemplateDtoCache;
import com.ylink.inetpay.cbs.bis.dao.BisSmsTemplateDtoMapper;
import com.ylink.inetpay.cbs.common.CbsConstants;
import com.ylink.inetpay.common.core.constant.EBisTemplateCode;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisSmsTemplateDto;
@Service("bisSmsTemplateService")
@Transactional(value=CbsConstants.TX_MANAGER_BIS)
public class BisSmsTemplateServiceImpl implements BisSmsTemplateService {
	@Autowired
	private BisSmsTemplateDtoMapper bisSmsTemplateDtoMapper;
	@Autowired
	private BisSmsTemplateDtoCache bisSmsTemplateDtoCache;
	@Override
	public PageData<BisSmsTemplateDto> findListPage(
			PageData<BisSmsTemplateDto> pageDate,
			BisSmsTemplateDto bisSmsTemplateDto) {
		PageHelper.startPage(pageDate.getPageNumber(),pageDate.getPageSize());
		List<BisSmsTemplateDto> findListPage=bisSmsTemplateDtoMapper.list(bisSmsTemplateDto);
		Page<BisSmsTemplateDto> page =(Page<BisSmsTemplateDto>) findListPage;
		pageDate.setTotal(page.getTotal());
		pageDate.setRows(findListPage);
		return pageDate;
	}

	@Override
	public BisSmsTemplateDto details(String id) {
		return bisSmsTemplateDtoMapper.selectByPrimaryKey(id);
	}

	@Override
	public void update(BisSmsTemplateDto bisSmsTemplateDto) {
		bisSmsTemplateDto.setUpdateTime(new Date());
		bisSmsTemplateDtoCache.updateBySmsTempla(bisSmsTemplateDto);
	}

	@Override
	public BisSmsTemplateDto getSmsTempla(EBisTemplateCode templateCode) {
		return bisSmsTemplateDtoCache.getSmsTempla(templateCode);
	}

}
