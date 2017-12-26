package com.ylink.inetpay.cbs.bis.service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.ylinkpay.framework.core.model.PageData;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.ylink.inetpay.cbs.bis.cache.BisSysParamDtoCache;
import com.ylink.inetpay.cbs.bis.dao.BisSysParamDtoMapper;
import com.ylink.inetpay.cbs.common.CbsConstants;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisSysParamDto;
@Service("bisSysParamService")
@Transactional(value=CbsConstants.TX_MANAGER_BIS)
public class BisSysParamServiceImpl implements BisSysParamService {
	@Autowired
	private BisSysParamDtoMapper bisSysParamDtoMapper;
	@Autowired
	private BisSysParamDtoCache bisSysParamDtoCache;
	@Override
	public PageData<BisSysParamDto> findListPage(PageData<BisSysParamDto> pageVO,
			BisSysParamDto bisSysParamDto) {
		PageHelper.startPage(pageVO.getPageNumber(), pageVO.getPageSize());
		List<BisSysParamDto> list=bisSysParamDtoMapper.list(bisSysParamDto);
		Page<BisSysParamDto> page=(Page<BisSysParamDto>) list;
		pageVO.setTotal(page.getTotal());
		pageVO.setRows(list);
		return pageVO;
	}

	/*@Override
	public Map<String, String> mapAllCode() {
		List<BisSysParamDto> mapAllCode=bisSysParamDtoMapper.mapAllCode();
		Map<String,String> hashMap = new HashMap<>();
		for (BisSysParamDto bisSysParamDto : mapAllCode) {
			hashMap.put(bisSysParamDto.getKey(),bisSysParamDto.getValue());
		}
		return hashMap;
	}*/

	@Override
	public String getValue(String key) {
		BisSysParamDto dto=bisSysParamDtoCache.selectByKey(key);
		if(dto==null)
		{
			return null;
		}
		return dto.getValue();
	}

	@Override
	public void updateSelective(BisSysParamDto record) {
		record.setUpdateTime(new Date());
		bisSysParamDtoCache.updateById(record);
	}

	@Override
	public BisSysParamDto details(String id){
		return bisSysParamDtoMapper.selectByPrimaryKey(id);
	}

	@Override
	public List<BisSysParamDto> getLimitParams(String groupName) {
		return bisSysParamDtoMapper.getLimitParams(groupName);
	}

	@Override
	public BisSysParamDto selectById(String id) {
		// TODO Auto-generated method stub
		return bisSysParamDtoCache.selectById(id);
	}

	@Override
	public BisSysParamDto selectByKey(String key) {
		// TODO Auto-generated method stub
		return bisSysParamDtoCache.selectByKey(key);
	}

	@Override
	public List<BisSysParamDto> findByParamNames(List<String> paramNames) {
		return bisSysParamDtoMapper.findByParamNames(paramNames);
	}

	@Override
	public void saveMerChantChannelId(String merchantChannelId, String string) {
		BisSysParamDto bisSysParamDto = new BisSysParamDto();
		bisSysParamDto.setCreateTime(new Date());
		bisSysParamDto.setGroupName("用户认证组");
		bisSysParamDto.setId(UUID.randomUUID().toString());
		bisSysParamDto.setKey(merchantChannelId);
		bisSysParamDto.setRemark("系统自动生成");
		bisSysParamDto.setValue(string);
		bisSysParamDtoMapper.insert(bisSysParamDto);
	}
}
