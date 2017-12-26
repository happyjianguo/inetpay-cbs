package com.ylink.inetpay.cbs.cls.dao;

import java.util.List;

import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

import com.ylink.inetpay.common.project.clear.dto.ClsLiquidationOrder;
/**
 * 清算订单dao层
 * @author pst09
 *
 */
@MybatisMapper("clsLiquidationOrderDao")
public interface ClsLiquidationOrderDao {
	/**
	 * 查询全部
	 * @param param
	 * @return
	 */
	List<ClsLiquidationOrder> queryAll(ClsLiquidationOrder param);
 
	/**
	 * 根据id查询详情
	 */
	ClsLiquidationOrder queryById(String id);
}