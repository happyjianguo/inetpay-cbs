package com.ylink.inetpay.cbs.cls.service;

import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.common.core.constant.CLSReviewStatus;
import com.ylink.inetpay.common.core.dto.ReporHeadDto;
import com.ylink.inetpay.common.project.cbs.exception.CbsCheckedException;
import com.ylink.inetpay.common.project.clear.dto.ClsChannelBillVo;
import com.ylink.inetpay.common.project.clear.dto.ClsPayBillVo;
import com.ylink.inetpay.common.project.clear.dto.ClsRecordCheck;

		/**
 * @类名称： ClsCheckInnerService
 * @类描述： 
 * @创建人： 1603254
 * @创建时间： 2016-5-24 下午3:30:17
 *
 * @修改人： 1603254
 * @操作时间： 2016-5-24 下午3:30:17
 * @操作原因： 
 * 
 */
public interface ClsCheckInnerService {

	/**
	 * 支付流水查询（调账列表查询）
	 * @param pageDate
	 * @param chlParamDto
	 * @return
	 * @throws CbsCheckedException
	 */
	public PageData<ClsPayBillVo> findCheckDetailAdjust(PageData<ClsPayBillVo> pageDate,ClsPayBillVo payBill);
	/**
	 * 支付流水查询（审核列表查询）
	 * @param pageDate
	 * @param chlParamDto
	 * @return
	 * @throws CbsCheckedException
	 */
	public PageData<ClsPayBillVo> findCheckDetailReview(PageData<ClsPayBillVo> pageDate,ClsPayBillVo payBill);
	
	/**
	 * 支付流水查询（审核结果列表查询）
	 * @param pageDate
	 * @param chlParamDto
	 * @return
	 * @throws CbsCheckedException
	 */
	public PageData<ClsPayBillVo> findCheckDetailReviewResult(PageData<ClsPayBillVo> pageDate,ClsPayBillVo payBill);
	/**
	 * 支付流水查询（明细查询所有审核状态）
	 * @param pageDate
	 * @param chlParamDto
	 * @return
	 * @throws CbsCheckedException
	 */
	public PageData<ClsPayBillVo> findCheckDetail(PageData<ClsPayBillVo> pageDate,ClsPayBillVo payBill);

	/**
	 * @方法描述:  汇总查询
	 * @作者： 1603254
	 * @日期： 2016-5-26-上午10:57:55
	 * @param payBill
	 * @return 
	 * @返回类型： ReporHeadDto
	*/
	ReporHeadDto queryPayBillSumarry(ClsPayBillVo payBill);
	
	/**
	 * @方法描述:  根据id查找
	 * @作者： 1603254
	 * @日期： 2016-5-25-下午4:19:38
	 * @param id
	 * @return 
	 * @返回类型： ClsChannelBillVo
	*/
	public ClsPayBillVo queryById(String id);
	/**
	 * 获取内部对账表头
	 * @param checkType
	 * @param queryParam
	 * @return
	 */
	public ReporHeadDto getReporHead(ClsPayBillVo queryParam);
	
	/**
	 * @方法描述:  
	 * @作者： 1603254
	 * @日期： 2016-9-2-上午10:31:26
	 * @param keyId
	 * @param currentUserLoginName
	 * @param wait
	 * @return 
	 * @返回类型： boolean
	*/
	public boolean isEqual(String keyId, String currentUserLoginName,
			CLSReviewStatus wait);
	
}
