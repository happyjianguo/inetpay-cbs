package com.ylink.inetpay.cbs.mrs.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.ylink.inetpay.cbs.mrs.dao.MrsAccountAduitInfoDtoMapper;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsAccountAduitInfoDto;

@Service("mrsAccountAduitInfoService")
public class MrsAccountAduitInfoServiceImpl implements MrsAccountAduitInfoService{
    
	@Autowired
	private MrsAccountAduitInfoDtoMapper mrsAccountAduitInfoDtoMapper;
	
	@Override
	public MrsAccountAduitInfoDto selectByPrimaryKey(String id) {
		return mrsAccountAduitInfoDtoMapper.selectByPrimaryKey(id);
	}

	@Override
	public void updateByPrimaryKey(MrsAccountAduitInfoDto record) {
		mrsAccountAduitInfoDtoMapper.updateByPrimaryKey(record);
		
	}

	@Override
	public PageData<MrsAccountAduitInfoDto> findListPage(PageData<MrsAccountAduitInfoDto> pageData,
			MrsAccountAduitInfoDto queryParam) {
		PageHelper.startPage(pageData.getPageNumber(), pageData.getPageSize());
		List<MrsAccountAduitInfoDto> list = mrsAccountAduitInfoDtoMapper.findListPage(queryParam);
		Page<MrsAccountAduitInfoDto> page = (Page <MrsAccountAduitInfoDto>) list;
		pageData.setTotal(page.getTotal());
		pageData.setRows(list);
		return pageData;
	}

	@Override
	public PageData<MrsAccountAduitInfoDto> getByCont(PageData<MrsAccountAduitInfoDto> pageData,
			MrsAccountAduitInfoDto queryParam) {
		PageHelper.startPage(pageData.getPageNumber(), pageData.getPageSize());
		List<MrsAccountAduitInfoDto> list = mrsAccountAduitInfoDtoMapper.getByIds(queryParam);
		Page<MrsAccountAduitInfoDto> page = (Page <MrsAccountAduitInfoDto>) list;
		pageData.setTotal(page.getTotal());
		pageData.setRows(list);
		return pageData;
	}


}
