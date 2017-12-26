/**
 * 版权所有(C) 2016 深圳市雁联计算系统有限公司
 * 创建:lyg 2016-9-5
 */

package com.ylink.inetpay.cbs.bis.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import com.ylink.inetpay.cbs.bis.dao.BisSysParamDtoMapper;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisSysParamDto;

/** 
 * @author lyg
 * @date 2016-9-5
 * @description：TODO
 */

@Service("bisSysParamDtoCache")
public class BisSysParamDtoCacheImpl implements BisSysParamDtoCache {

	@Autowired
	BisSysParamDtoMapper bisSysParamDtoMapper;

	@Override
	@Caching(put={@CachePut(value="mpay",key="'bisSysParamDtoCache[key-' + #record.key")
	,@CachePut(value="mpay",key="'bisSysParamDtoCache[id-' + #record.id")})
	public BisSysParamDto updateById(BisSysParamDto record) {
		
		 bisSysParamDtoMapper.updateByPrimaryKey(record);
		 record=bisSysParamDtoMapper.selectByPrimaryKey(record.getId());
		 return record;
	}

	@Override
	@Cacheable(value="mpay",key="'bisSysParamDtoCache[id-' + #id")
	public BisSysParamDto selectById(String id) {
		
		return bisSysParamDtoMapper.selectByPrimaryKey(id);
	}

	@Override
	@Cacheable(value="mpay",key="'bisSysParamDtoCache[key-' + #key")
	public BisSysParamDto selectByKey(String key) {
		// TODO Auto-generated method stub
		return bisSysParamDtoMapper.selectByKey(key);
	}
	
	
	
	
}
