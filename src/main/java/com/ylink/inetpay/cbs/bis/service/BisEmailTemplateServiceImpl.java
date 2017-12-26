package com.ylink.inetpay.cbs.bis.service;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.ylinkpay.framework.core.model.PageData;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.ylink.inetpay.cbs.bis.cache.BisEmailTemplateDtoCache;
import com.ylink.inetpay.cbs.bis.dao.BisEmailTemplateDtoMapper;
import com.ylink.inetpay.cbs.common.CbsConstants;
import com.ylink.inetpay.common.core.constant.EBisEmailTemplateCode;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisEmailTemplateDto;
@Service("bisEmailTemplateService")
@Transactional(value=CbsConstants.TX_MANAGER_BIS)
public class BisEmailTemplateServiceImpl implements BisEmailTemplateService {
	@Autowired
	private BisEmailTemplateDtoMapper bisEmailTemplateDtoMapper;
	
	@Autowired
	private BisEmailTemplateDtoCache bisEmailTemplateDtoCache;
	@Override
	public PageData<BisEmailTemplateDto> findListPage(
			PageData<BisEmailTemplateDto> pageDate,
			BisEmailTemplateDto bisEmailTemplateDto) {
		PageHelper.startPage(pageDate.getPageNumber(),pageDate.getPageSize());
		List<BisEmailTemplateDto> findListPage=bisEmailTemplateDtoMapper.list(bisEmailTemplateDto);
		Page<BisEmailTemplateDto> page =(Page<BisEmailTemplateDto>) findListPage;
		pageDate.setTotal(page.getTotal());
		pageDate.setRows(findListPage);
		for (BisEmailTemplateDto dto : findListPage) {
			if((dto.getAddressee()!=null && dto.getAddressee().length()>11)){
				dto.setAddressee(dto.getAddressee().substring(0,11)+"...");
			}
			if(dto.getContent()!=null && dto.getContent().length()>20){
				dto.setContent(dto.getContent().substring(0,20)+"...");
			}
		}
		return pageDate;
	}

	@Override
	public BisEmailTemplateDto details(String id) {
		return bisEmailTemplateDtoMapper.selectByPrimaryKey(id);
	}

	@Override
	public void update(BisEmailTemplateDto bisEmailTemplateDto) {
		bisEmailTemplateDto.setUpdateTime(new Date());
		bisEmailTemplateDtoCache.updateByEmailTempla(bisEmailTemplateDto);
	}

	@Override
	public BisEmailTemplateDto getEmailTempla(EBisEmailTemplateCode templateCode) {
		return bisEmailTemplateDtoCache.getEmailTempla(templateCode);
	}

}
