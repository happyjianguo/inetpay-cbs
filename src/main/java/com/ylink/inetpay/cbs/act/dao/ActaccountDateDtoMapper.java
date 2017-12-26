package com.ylink.inetpay.cbs.act.dao;

import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

import com.ylink.inetpay.common.project.account.dto.ActaccountDateDto;

@MybatisMapper("actaccountDateDtoMapper")
public interface ActaccountDateDtoMapper {
   // int deleteByPrimaryKey(String id);

   // int insert(ActaccountDateDto record);

  //  int insertSelective(ActaccountDateDto record);

    ActaccountDateDto selectByPrimaryKey(String id);

  //  int updateByPrimaryKeySelective(ActaccountDateDto record);

 //   int updateByPrimaryKey(ActaccountDateDto record);
    /**
     * 获取账务日期
     * @return
     */
//	String getAccountDate();
}