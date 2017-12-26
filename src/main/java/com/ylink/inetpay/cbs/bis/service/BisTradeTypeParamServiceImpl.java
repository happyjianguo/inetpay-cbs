package com.ylink.inetpay.cbs.bis.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.ylinkpay.framework.core.model.PageData;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.ylink.inetpay.cbs.bis.dao.BisTradeTypeParamDtoMapper;
import com.ylink.inetpay.cbs.common.CbsConstants;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisTradeTypeParamDto;
 
@Service("bisTradeTypeParamService")
@Transactional(value=CbsConstants.TX_MANAGER_BIS)
public class BisTradeTypeParamServiceImpl implements BisTradeTypeParamService {
	 	@Autowired
	 	private BisTradeTypeParamDtoMapper bisTradeTypeParamDtoMapper;

		@Override
		public PageData<BisTradeTypeParamDto> pageList(PageData<BisTradeTypeParamDto> pageData,
				BisTradeTypeParamDto queryParam) {
			PageHelper.startPage(pageData.getPageNumber(), pageData.getPageSize());
			List<BisTradeTypeParamDto> items = bisTradeTypeParamDtoMapper.list(queryParam);
			Page<BisTradeTypeParamDto> page = (Page<BisTradeTypeParamDto> )items;
			pageData.setRows(items);
			pageData.setTotal(page.getTotal());
			return pageData;
		}

		@Override
		public void insertBisTradeTypeParam(BisTradeTypeParamDto bisTradeTypeParamDto) {
			bisTradeTypeParamDto.setId(bisTradeTypeParamDto.getIdentity());
			bisTradeTypeParamDtoMapper.insertSelective(bisTradeTypeParamDto);
	 	
		}

		@Override
		public BisTradeTypeParamDto detail(String id) {
			 return bisTradeTypeParamDtoMapper.selectByPrimaryKey(id);
		}

		@Override
		public void update(BisTradeTypeParamDto bisTradeTypeParamDto) {
			bisTradeTypeParamDtoMapper.updateByPrimaryKeySelective(bisTradeTypeParamDto);
			
		}

		@Override
		public List<BisTradeTypeParamDto> findAllTradeType() {
			return bisTradeTypeParamDtoMapper.list(new BisTradeTypeParamDto());
		}
}

