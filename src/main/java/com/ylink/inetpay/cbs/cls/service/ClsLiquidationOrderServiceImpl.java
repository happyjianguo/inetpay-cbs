package com.ylink.inetpay.cbs.cls.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.ylink.inetpay.cbs.cls.dao.ClsLiquidationOrderDao;
import com.ylink.inetpay.common.project.clear.dto.ClsLiquidationOrder;
@Service("clsLiquidationOrderService")
public class ClsLiquidationOrderServiceImpl implements ClsLiquidationOrderService{
	@Autowired
	private ClsLiquidationOrderDao clsLiquidationOrderDao;
	/**
	 * 查询清分任务监控借口实现类
	 */
	
	public PageData<ClsLiquidationOrder> findLiquidationOrder(PageData<ClsLiquidationOrder> pageDate,
			ClsLiquidationOrder clsLiquidationOrder) {
		PageHelper.startPage(pageDate.getPageNumber(),pageDate.getPageSize());
		List<ClsLiquidationOrder> list=clsLiquidationOrderDao.queryAll(clsLiquidationOrder);
		Page<ClsLiquidationOrder> page=(Page<ClsLiquidationOrder>) list;
		pageDate.setTotal(page.getTotal());
		pageDate.setRows(list);
		return pageDate;
	}
	/**
	 * 根据id查询详情
	 */
	public ClsLiquidationOrder findById(String id) {
		return clsLiquidationOrderDao.queryById(id);
	}
}
