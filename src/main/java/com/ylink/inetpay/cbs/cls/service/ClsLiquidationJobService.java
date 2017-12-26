package com.ylink.inetpay.cbs.cls.service;

import java.util.List;

import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.common.core.constant.CLSReviewStatus;
import com.ylink.inetpay.common.core.dto.ReporHeadDto;
import com.ylink.inetpay.common.project.clear.dto.CLsMerchantSettleVo;
import com.ylink.inetpay.common.project.clear.dto.ClsLiquidationJob;
import com.ylink.inetpay.common.project.clear.dto.ClsMerSett;
import com.ylink.inetpay.common.project.clear.dto.ClsPayBill;
import com.ylink.inetpay.common.project.clear.dto.ClsPayBillVo;

/**
 * @类名称： ClsMerSettleService
 * @类描述： 清算任务监控接口服务类
 * @创建人： 1603254
 * @创建时间： 2016-5-25 下午2:35:48
 *
 * @修改人： 1603254
 * @操作时间： 2016-5-25 下午2:35:48
 * @操作原因： 
 * 
 */
public interface ClsLiquidationJobService {

	/**
	 * @方法描述: 分页查询清算监控任务
	 * @作者： 1603254
	 * @日期： 2016-5-25-下午2:37:07
	 * @param pageDate
	 * @param settle
	 * @return 
	 * @返回类型： PageData<CLsMerchantSettleVo>
	*/
	public PageData<ClsLiquidationJob> queryClsLiquidationJob(PageData<ClsLiquidationJob> pageDate,
			ClsLiquidationJob liquidationJob);
	
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
			ClsMerSett settle);
	/**
	 * @方法描述:  根据id查找
	 * @作者： 1603254
	 * @日期： 2016-5-25-下午4:19:38
	 * @param id
	 * @return 
	 * @返回类型： CLsMerchantSettleVo
	*/
	public CLsMerchantSettleVo queryById(String id);
	
	/**
	 * @方法描述: 
	 * @作者： 1603254
	 * @日期： 2016-5-31-上午11:45:49
	 * @param settle
	 * @return 
	 * @返回类型： ReporHeadDto
	*/
	public ReporHeadDto queryMerSettSummary(ClsMerSett settle);
	/**
	 * 获取重新结算审核列表
	 * @param pageData
	 * @param queryParam
	 * @return
	 */
	public PageData<CLsMerchantSettleVo> findMerSettleAudit(
			PageData<CLsMerchantSettleVo> pageData,
			CLsMerchantSettleVo queryParam);

	public PageData<ClsPayBill> queryOrderByMerchantAndDate(PageData<ClsPayBill> pageData,ClsPayBill bill);
}
