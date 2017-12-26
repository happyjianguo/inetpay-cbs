package com.ylink.inetpay.cbs.cls.dao;


import java.util.List;

import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

import com.ylink.inetpay.common.project.clear.dto.ClsLiquidationJob;

@MybatisMapper("clsLiquidationJobDao")
public interface ClsLiquidationJobDao {
	/**
	 * 查询清算任务监控
	 */
	List<ClsLiquidationJob> queryAll(ClsLiquidationJob param);
}
