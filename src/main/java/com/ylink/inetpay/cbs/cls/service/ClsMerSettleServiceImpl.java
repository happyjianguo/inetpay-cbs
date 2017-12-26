package com.ylink.inetpay.cbs.cls.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.ylink.inetpay.cbs.cls.dao.ClsMerSettleDao;
import com.ylink.inetpay.cbs.cls.dao.ClsPayBillDao;
import com.ylink.inetpay.cbs.cls.dao.ClsRecordCheckDao;
import com.ylink.inetpay.common.core.dto.ReporHeadDto;
import com.ylink.inetpay.common.project.clear.dto.CLsMerchantSettleVo;
import com.ylink.inetpay.common.project.clear.dto.ClsMerSett;
import com.ylink.inetpay.common.project.clear.dto.ClsPayBill;

/**
 * @类名称： ClsMerSettleServiceImpl
 * @类描述： 商户结算实现类
 * @创建人： 1603254
 * @创建时间： 2016-5-25 下午2:37:55
 *
 * @修改人： 1603254
 * @操作时间： 2016-5-25 下午2:37:55
 * @操作原因： 
 * 
 */
@Service("clsMerSettleService")
public class ClsMerSettleServiceImpl implements ClsMerSettleService {

	@Autowired
	private ClsMerSettleDao merSettleDao;
	@Autowired
	private ClsRecordCheckDao clsRecordCheckDao;
	
	@Autowired
	private ClsPayBillDao clsPayBillDao;
	/**
	 * @方法描述: 分页查询商户结算记录
	 * @作者： 1603254
	 * @日期： 2016-5-25-下午2:37:07
	 * @param pageDate
	 * @param settle
	 * @return 
	 * @返回类型： PageData<CLsMerchantSettleVo>
	*/
	public PageData<CLsMerchantSettleVo> queryClsMerSett(PageData<CLsMerchantSettleVo> pageDate,
			CLsMerchantSettleVo settle){
		PageHelper.startPage(pageDate.getPageNumber(),pageDate.getPageSize());
		List<CLsMerchantSettleVo> list=merSettleDao.queryClsMerSett(settle);
		Page<CLsMerchantSettleVo> page=(Page<CLsMerchantSettleVo>) list;
		pageDate.setTotal(page.getTotal());
		pageDate.setRows(list);
		return pageDate;
		
	}
	
	/**
	 * @方法描述: 分页查询商户结算记录
	 * @作者： 1603254
	 * @日期： 2016-5-25-下午2:37:07
	 * @param pageDate
	 * @param settle
	 * @return 
	 * @返回类型： PageData<CLsMerchantSettleVo>
	 */
	public PageData<ClsMerSett> queryClsMerSett(PageData<ClsMerSett> pageData,
			ClsMerSett settle){
		PageHelper.startPage(pageData.getPageNumber(),pageData.getPageSize());
		List<ClsMerSett> list=merSettleDao.queryMerSett(settle);
		Page<ClsMerSett> page=(Page<ClsMerSett>) list;
		pageData.setTotal(page.getTotal());
		pageData.setRows(list);
		return pageData;
	}
	
	/**
	 * @方法描述:  根据id查找
	 * @作者： 1603254
	 * @日期： 2016-5-25-下午4:19:38
	 * @param id
	 * @return 
	 * @返回类型： ClsChannelBillVo
	*/
	public CLsMerchantSettleVo queryById(String id){
		CLsMerchantSettleVo queryById = merSettleDao.queryById(id);
		queryById.setRecordChecks(clsRecordCheckDao.getRecordChecks(id));
		return queryById;
	}
	
	/**
	 * @方法描述: 
	 * @作者： 1603254
	 * @日期： 2016-5-31-上午11:45:49
	 * @param settle
	 * @return 
	 * @返回类型： ReporHeadDto
	*/
	public ReporHeadDto queryMerSettSummary(ClsMerSett settle){
		return merSettleDao.queryMerSettSummary(settle);
	}

	@Override
	public PageData<CLsMerchantSettleVo> findMerSettleAudit(
			PageData<CLsMerchantSettleVo> pageData,
			CLsMerchantSettleVo queryParam) {
		PageHelper.startPage(pageData.getPageNumber(),pageData.getPageSize());
		List<CLsMerchantSettleVo> list=merSettleDao.findMerSettleAudit(queryParam);
		Page<CLsMerchantSettleVo> page=(Page<CLsMerchantSettleVo>) list;
		pageData.setTotal(page.getTotal());
		pageData.setRows(list);
		return pageData;
	}

	@Override
	public PageData<ClsPayBill> queryOrderByMerchantAndDate(PageData<ClsPayBill> pageData,ClsPayBill bill) {
		PageHelper.startPage(pageData.getPageNumber(),pageData.getPageSize());
		List<ClsPayBill> list=clsPayBillDao.queryOrderByMerchantAndDate(bill);
		Page<ClsPayBill> page=(Page<ClsPayBill>) list;
		pageData.setTotal(page.getTotal());
		pageData.setRows(list);
		return pageData;
	}
}
