package com.ylink.inetpay.cbs.bis.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.context.request.WebRequest;
import org.ylinkpay.framework.core.model.PageData;

 
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.ylink.inetpay.cbs.bis.dao.BisAccessorDtoMapper;
import com.ylink.inetpay.cbs.common.CbsConstants;
import com.ylink.inetpay.common.core.constant.EBisSendStatus;
import com.ylink.inetpay.common.core.constant.EBisSmsSystem;
import com.ylink.inetpay.common.core.constant.EsendChannel;
import com.ylink.inetpay.common.core.converter.EBisSendStatusConverter;
import com.ylink.inetpay.common.core.converter.EBisSmsSystemConverter;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisAccessorDto;
 
@Service("bisAccessorService")
@Transactional(value=CbsConstants.TX_MANAGER_BIS)
public class BisAccessorServiceImpl implements BisAccessorService {
	@Autowired
	private BisAccessorDtoMapper bisAccessorDtoMapper;

	@Override
	public PageData<BisAccessorDto> pageList(PageData<BisAccessorDto> pageData, BisAccessorDto queryParam) {
		PageHelper.startPage(pageData.getPageNumber(), pageData.getPageSize());
		List<BisAccessorDto> items = bisAccessorDtoMapper.list(queryParam);
		Page<BisAccessorDto> page = (Page<BisAccessorDto> )items;
		pageData.setRows(items);
		pageData.setTotal(page.getTotal());
		return pageData;
	}

	@Override
	public List<BisAccessorDto> list() {
		return bisAccessorDtoMapper.list(new BisAccessorDto());
	}

	@Override
	public void saveAccessor(BisAccessorDto queryParam) {
		long quence=bisAccessorDtoMapper.getAccessorQuence();
		String formatStr = "%08d";
		queryParam.setAccessCode(String.format(formatStr, quence));
		queryParam.setId(queryParam.getIdentity());
		bisAccessorDtoMapper.insert(queryParam);
		
	}

	@Override
	public BisAccessorDto details(String viewId) {
		 
		return bisAccessorDtoMapper.selectByPrimaryKey(viewId);
	}

	@Override
	public void deleteBisAccessorDto(String viewId) {
		bisAccessorDtoMapper.deleteByPrimaryKey(viewId);
	}

	@Override
	public void updateAccessorDto(BisAccessorDto accessorDto) {
		bisAccessorDtoMapper.updateByPrimaryKeySelective(accessorDto);
		
	}

	@Override
	public List<String> queryBisAccessorDtoCustId() {
		List<BisAccessorDto> dtos = bisAccessorDtoMapper.queryCustId();
		List<String> custIdList = new ArrayList<String>();
		if(dtos!=null && !dtos.isEmpty()){
			for (BisAccessorDto dto : dtos) {
				custIdList.add(dto.getCustId());
			}
		}
		return custIdList;
	}
	 
}

