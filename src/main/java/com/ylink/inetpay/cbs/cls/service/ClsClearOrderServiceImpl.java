package com.ylink.inetpay.cbs.cls.service;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.ylink.inetpay.cbs.cls.dao.ClsClearOrderDao;
import com.ylink.inetpay.cbs.cls.dao.ClsLiquidationJobDao;
import com.ylink.inetpay.common.core.dto.ReporHeadDto;
import com.ylink.inetpay.common.project.clear.dto.CLsMerchantSettleVo;
import com.ylink.inetpay.common.project.clear.dto.ClsClearOrder;
import com.ylink.inetpay.common.project.clear.dto.ClsLiquidationJob;
import com.ylink.inetpay.common.project.clear.dto.ClsMerSett;
import com.ylink.inetpay.common.project.clear.dto.ClsPayBill;
@Service("clsClearOrderService")
public class ClsClearOrderServiceImpl implements ClsClearOrderService{
	@Autowired
	private ClsClearOrderDao clsClearOrderDao;
	/**
	 * 查询清分订单
	 */
	
	public PageData<ClsClearOrder> queryClsClearOrder(PageData<ClsClearOrder> pageDate,
			ClsClearOrder clsClearOrder) {
		PageHelper.startPage(pageDate.getPageNumber(),pageDate.getPageSize());
		List<ClsClearOrder> list=clsClearOrderDao.queryClsClearOrder(clsClearOrder);
		Page<ClsClearOrder> page=(Page<ClsClearOrder>) list;
		pageDate.setTotal(page.getTotal());
		pageDate.setRows(list);
		return pageDate;
	}
	
	/**
	 * 根据id查询订单详情
	 */
	public ClsClearOrder deails(String id) {
		return clsClearOrderDao.queryById(id);
	}
}
