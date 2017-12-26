package com.ylink.inetpay.cbs.act.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

import com.ylink.inetpay.common.core.constant.EActBusiRefSubBusiType;
import com.ylink.inetpay.common.project.account.dto.ActBusiRefSubDto;

@MybatisMapper("actBusiRefSubDtoMapper")
public interface ActBusiRefSubDtoMapper {
    int deleteByPrimaryKey(String id);

    int insert(ActBusiRefSubDto record);

    int insertSelective(ActBusiRefSubDto record);

    ActBusiRefSubDto selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(ActBusiRefSubDto record);

    int updateByPrimaryKey(ActBusiRefSubDto record);
    /**
     * 
     *方法描述：根据账户类型编号查询科目编号
     * 创建时间：2017年4月17日 下午2:43:02
     * @param acctTypeNos
     * @return
     */
    List<String> findSub2NoByAcctTypeNos(@Param("acctTypeNos")List<String> acctTypeNos);
    /**
     * 
     *方法描述：根据账户类型编号查询
     * 创建人：ydx
     * 创建时间：2017年4月18日 下午8:14:52
     * @param acctTypeNos
     * @return
     */
    List<ActBusiRefSubDto> findByAcctTypeNos(@Param("acctTypeNos")List<String> acctTypeNos,@Param("busiTypes")List<EActBusiRefSubBusiType> busiTypes);
}