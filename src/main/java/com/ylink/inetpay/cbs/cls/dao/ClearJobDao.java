package com.ylink.inetpay.cbs.cls.dao;


import java.util.List;

import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

import com.ylink.inetpay.common.project.clear.dto.ClsClearJob;

@MybatisMapper("clearJobDao")
public interface ClearJobDao {
	/**
	 * 查询清分任务监控
	 */
	List<ClsClearJob> queryAll(ClsClearJob param);
	
	/**
	 * 查询情分详情
	 */
	ClsClearJob detail(String id);
}
