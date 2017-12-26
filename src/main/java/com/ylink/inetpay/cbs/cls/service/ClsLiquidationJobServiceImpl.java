package com.ylink.inetpay.cbs.cls.service;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.ylink.inetpay.cbs.cls.dao.ClsLiquidationJobDao;
import com.ylink.inetpay.common.core.dto.ReporHeadDto;
import com.ylink.inetpay.common.project.clear.dto.CLsMerchantSettleVo;
import com.ylink.inetpay.common.project.clear.dto.ClsLiquidationJob;
import com.ylink.inetpay.common.project.clear.dto.ClsMerSett;
import com.ylink.inetpay.common.project.clear.dto.ClsPayBill;
@Service("clsLiquidationJobService")
public class ClsLiquidationJobServiceImpl implements ClsLiquidationJobService{
	@Autowired
	private ClsLiquidationJobDao clsLiquidationJobDao;
	/**
	 * 查询清算任务监控
	 */
	
	public PageData<ClsLiquidationJob> queryClsLiquidationJob(PageData<ClsLiquidationJob> pageDate,
			ClsLiquidationJob liquidationJob) {
		PageHelper.startPage(pageDate.getPageNumber(),pageDate.getPageSize());
		List<ClsLiquidationJob> list=clsLiquidationJobDao.queryAll(liquidationJob);
		Page<ClsLiquidationJob> page=(Page<ClsLiquidationJob>) list;
		pageDate.setTotal(page.getTotal());
		pageDate.setRows(list);
		return pageDate;
	}
	@Test
	public void test(){
		PageData<ClsLiquidationJob> pageDate = new PageData<>();
		ClsLiquidationJob cls = new ClsLiquidationJob();
		PageData<ClsLiquidationJob> page = queryClsLiquidationJob(pageDate,cls);
		System.out.println(page);
	}
	@Override
	public PageData<ClsMerSett> queryClsMerSett(PageData<ClsMerSett> pageData, ClsMerSett settle) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CLsMerchantSettleVo queryById(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ReporHeadDto queryMerSettSummary(ClsMerSett settle) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PageData<CLsMerchantSettleVo> findMerSettleAudit(PageData<CLsMerchantSettleVo> pageData,
			CLsMerchantSettleVo queryParam) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PageData<ClsPayBill> queryOrderByMerchantAndDate(PageData<ClsPayBill> pageData, ClsPayBill bill) {
		// TODO Auto-generated method stub
		return null;
	}

}
