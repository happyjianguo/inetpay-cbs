package com.ylink.inetpay.cbs.bis.dao;

import java.util.List;
import java.util.Set;

import org.apache.ibatis.annotations.Param;
import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

import com.ylink.inetpay.common.project.cbs.dto.bis.BisAreaCityDto;

/**
 * 省市
 * @author pst10
 *
 */
@MybatisMapper("bisAreaCityDaoMapper")
public interface BisAreaCityDaoMapper {
    
	/**
	 * 根据code查询
	 * @param toGeoId
	 * @return
	 */
    public List<BisAreaCityDto> getCityList(String code);
    /**
	 * 所有城市地区
	 * @return
	 */
	public List<BisAreaCityDto> getCityAllList();
    /**
	 * 根据name获取
	 * @return
	 */
    public List<BisAreaCityDto> getCityByName(String name);
    /**
   	 * 根据编号和名称查询
   	 * @return
   	 */
       public List<BisAreaCityDto> getCityByCodeAndName(String code,String name);
    /**
     * 根据name获取
     * @param name
     * @return
     */
    public BisAreaCityDto getByName(String name);
    
    /**
     * 根据code获取
     * @param code
     * @return
     */
    public BisAreaCityDto getByCode(String code);
    /**
     * 根据城市codes获取下级城市
     * @param code
     * @return
     */
    public List<BisAreaCityDto> getCityListByCodes(@Param("codes")List<String> codes);
    
	public List<BisAreaCityDto> getcityName(@Param("cityKey")Set<String> cityKey);
}