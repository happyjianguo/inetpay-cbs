package com.ylink.inetpay.cbs.cls.dao;


import java.util.List;

import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

import com.ylink.inetpay.common.project.clear.dto.ClsClearJob;
import com.ylink.inetpay.common.project.clear.dto.ClsSettJob;

@MybatisMapper("clsSettJobDao")
public interface ClsSettJobDao {
	/**
	 * 查询结算任务监控
	 */
	List<ClsSettJob> queryAll(ClsSettJob param);
	/**
	 * 结算任务详情
	 * @param id
	 * @return
	 */
	ClsSettJob queryById(String id);
}
