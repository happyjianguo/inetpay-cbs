package com.ylink.inetpay.cbs.mrs.dao;

import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsSubPayBusiDto;
@MybatisMapper("mrsSubPayBusiDtoMapper")
public interface MrsSubPayBusiDtoMapper {
    int deleteByPrimaryKey(String id);

    int insert(MrsSubPayBusiDto record);

    int insertSelective(MrsSubPayBusiDto record);

    MrsSubPayBusiDto selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(MrsSubPayBusiDto record);

    int updateByPrimaryKey(MrsSubPayBusiDto record);
    /**
     * 
     *方法描述：根据子账户配置主键集合查询子账户配置支持科目对象
     * 创建人：ydx
     * 创建时间：2017年2月13日 上午10:10:32
     * @param confSubAcctIds
     * @return
     */
    /*List<String> findByConfSubAcctIds(@Param("confSubAcctIds")List<String> confSubAcctIds);*/
    /**
     * 
     *方法描述：根据子账户配置主键集合查询子账户配置支持科目对象
     * 创建人：ydx
     * 创建时间：2017年2月13日 上午10:10:32
     * @param confSubAcctIds
     * @return
     */
//    List<String> findBySubAccountCodeWithOrgan(@Param("subAccountCode")String subAccountCode,
//    		@Param("relationType")String relationType);
    /**
     * 
     *方法描述：根据子账户配置主键集合查询子账户配置支持科目对象
     * 创建人：ydx
     * 创建时间：2017年2月13日 上午10:10:32
     * @param confSubAcctIds
     * @return
     */
//    List<String> findBySubAccountCodeProduct(@Param("subAccountCode")String subAccountCode,
//    		@Param("relationType")String relationType);
}