package com.ylink.inetpay.cbs.mrs.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.ylink.inetpay.cbs.mrs.dao.MrsAduitPersonDtoMapper;
import com.ylink.inetpay.cbs.mrs.dao.MrsAduitPersonHisDtoMapper;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsAduitPersonDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsAduitPersonHisDto;

@Service("mrsAduitPersonHisService")
public class MrsAduitPersonHisServiceImpl implements MrsAduitPersonHisService{
    
	@Autowired
	private MrsAduitPersonHisDtoMapper mrsAduitPersonHisDtoMapper;
	@Autowired
	private MrsAduitPersonDtoMapper mrsAduitPersonDtoMapper;
	
	@Override
	public MrsAduitPersonHisDto selectByPrimaryKey(String id) {
		return mrsAduitPersonHisDtoMapper.selectByPrimaryKey(id);
	}

	@Override
	public PageData<MrsAduitPersonHisDto> findListHisPage(PageData<MrsAduitPersonHisDto> pageData,
			MrsAduitPersonHisDto hisDto) {
		PageHelper.startPage(pageData.getPageNumber(), pageData.getPageSize());
		List<MrsAduitPersonHisDto> list = mrsAduitPersonHisDtoMapper.findListHisPage(hisDto);
		Page<MrsAduitPersonHisDto> page = (Page <MrsAduitPersonHisDto>) list;
		pageData.setTotal(page.getTotal());
		pageData.setRows(list);
		return pageData;
	}

	@Override
	public PageData<MrsAduitPersonDto> findListPerPage(PageData<MrsAduitPersonDto> pageData, MrsAduitPersonDto perDto) {
		PageHelper.startPage(pageData.getPageNumber(), pageData.getPageSize());
		List<MrsAduitPersonDto> list = mrsAduitPersonDtoMapper.findListPerPage(perDto);
		Page<MrsAduitPersonDto> page = (Page <MrsAduitPersonDto>) list;
		pageData.setTotal(page.getTotal());
		pageData.setRows(list);
		return pageData;
	}
	
	@Override
	public List<String> getByCurrentUserId(String id) {
		List<String> ids = mrsAduitPersonDtoMapper.getByCurrentUserId(id);
		if(ids != null && ids.size() >0){
			return ids;
		}
		return null;
	}

	@Override
	public void save(MrsAduitPersonDto record) {
		mrsAduitPersonDtoMapper.insert(record);
	}

	@Override
	public void deleteByPrimaryKey(String id) {
		mrsAduitPersonDtoMapper.updateStatusByKey(id);
	}

	@Override
	public MrsAduitPersonDto selectPersonByKey(String id) {
		return mrsAduitPersonDtoMapper.selectByPrimaryKey(id);
	}

	@Override
	public void updateByPrimaryKey(MrsAduitPersonDto record) {
		mrsAduitPersonDtoMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public MrsAduitPersonDto selectByAduitUserName(String aduitUserId,String aduitId) {
		return mrsAduitPersonDtoMapper.selectByAduitUserName(aduitUserId,aduitId);
	}

	@Override
	public List<MrsAduitPersonHisDto> findListHisAll(MrsAduitPersonHisDto hisDto) {
		
		return mrsAduitPersonHisDtoMapper.findListHisAll(hisDto);
	}

	@Override
	public List<MrsAduitPersonDto> findByAduitId(String aduitId) {
		return mrsAduitPersonDtoMapper.findByAduitId(aduitId);
	}

	

	

}
