package com.ylink.inetpay.cbs.bis.cache;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.ylink.inetpay.cbs.bis.dao.BisAreaCityDaoMapper;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisAreaCityDto;

@Service("bisAreaCityDtoCache")
public class BisAreaCityDtoCacheImpl implements BisAreaCityDtoCache {

	@Autowired
	private BisAreaCityDaoMapper bisAreaCityDaoMapper;

	@Override
	@Cacheable(value="mpay",key="'bisAreaCityDtoCache[getCityList-key-' + #code")
	public List<BisAreaCityDto> getCityList(String code) {
		return bisAreaCityDaoMapper.getCityList(code);
	}
	@Override
	@Cacheable(value="mpay",key="'bisAreaCityDtoCache[getCityAllList-key-all'")
	public List<BisAreaCityDto> getCityAllList() {
		return bisAreaCityDaoMapper.getCityAllList();
	}
	
	@Override
	@Cacheable(value="mpay",key="'bisAreaCityDtoCache[key-' + #name")
	public BisAreaCityDto getByName(String name) {
		return bisAreaCityDaoMapper.getByName(name);
	}
	
	@Override
	@Cacheable(value="mpay",key="'bisAreaCityDtoCache[getByCode-key-' + #code")
	public BisAreaCityDto getByCode(String code) {
		return bisAreaCityDaoMapper.getByCode(code);
	}
	@Override
	@Cacheable(value="mpay",key="'bisAreaCityDtoCache[getCityByNamekey-' + #name")
	public List<BisAreaCityDto> getCityByName(String name) {
		return bisAreaCityDaoMapper.getCityByName(name);
	}
	@Override
	@Cacheable(value="mpay",key="'bisAreaCityDtoCache[getCityByCodeAndNamekey-' + #name")
	public List<BisAreaCityDto> getCityByCodeAndName(String code, String name) {
		return bisAreaCityDaoMapper.getCityByCodeAndName(code, name);
	}
	
	
}
