package com.ylink.inetpay.cbs.cls.dao;

import java.util.List;

import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

import com.ylink.inetpay.common.project.clear.dto.ClsAccessCheck;

/**
 * 查询对账单，下载
 * @author pst23
 */
@MybatisMapper("clsAccessCheckMapper")
public interface ClsAccessCheckMapper {
	
	/**
	 * 查询所有数据
	 */
	List<ClsAccessCheck> queryAllData(ClsAccessCheck queryparam);
	
	/**
	 * 根据主键查询
	 */
	ClsAccessCheck selectByPrimaryKey(String id);
	
}