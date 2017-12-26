package com.ylink.inetpay.cbs.bis.cache;

import java.util.List;

import com.ylink.inetpay.common.project.cbs.dto.bis.BisAreaCityDto;

public interface BisAreaCityDtoCache {

	/**
	 * 根据code查询
	 * 
	 * @param toGeoId
	 * @return
	 */
	public List<BisAreaCityDto> getCityList(String code);

	/**
	 * 所有城市地区
	 * 
	 * @return
	 */
	public List<BisAreaCityDto> getCityAllList();

	/**
	 * 根据name获取
	 * 
	 * @return
	 */
	public List<BisAreaCityDto> getCityByName(String name);

	/**
	 * 根据编号和名称查询
	 * 
	 * @return
	 */
	public List<BisAreaCityDto> getCityByCodeAndName(String code, String name);

	/**
	 * 根据name获取
	 * 
	 * @param name
	 * @return
	 */
	public BisAreaCityDto getByName(String name);

	/**
	 * 根据code获取
	 * 
	 * @param code
	 * @return
	 */
	public BisAreaCityDto getByCode(String code);
}
