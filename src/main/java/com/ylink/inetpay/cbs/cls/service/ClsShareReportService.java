package com.ylink.inetpay.cbs.cls.service;

import java.util.List;

import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.common.core.constant.CLSReviewStatus;
import com.ylink.inetpay.common.core.dto.ReporHeadDto;
import com.ylink.inetpay.common.project.clear.dto.ClsShareDetail;
import com.ylink.inetpay.common.project.clear.dto.ClsShareReport;

/**
 * @类名称： ClsShareReportService
 * @类描述：分润结算接口服务类
 * @创建人： 1603254
 * @创建时间： 2016-5-25 上午10:35:02
 *
 * @修改人： 1603254
 * @操作时间： 2016-5-25 上午10:35:02
 * @操作原因： 
 * 
 */
public interface ClsShareReportService {

	/**
	 * @方法描述:  查询所有的分润机构
	 * @作者： 1603254
	 * @日期： 2016-5-25-上午10:36:25
	 * @return 
	 * @返回类型： List<ClsShareReport>
	*/
	public List<ClsShareReport> findAllShare();

	/**
	 * @方法描述:  根据id查找
	 * @作者： 1603254
	 * @日期： 2016-5-25-下午4:19:38
	 * @param id
	 * @return 
	 * @返回类型： CLsMerchantSettleVo
	*/
	public ClsShareReport queryById(String id);
	
	/**
	 * @方法描述:  查询分润结算记录
	 * @作者： 1603254
	 * @日期： 2016-5-25-上午10:36:36
	 * @param pageDate
	 * @param shareReport
	 * @return 
	 * @返回类型： PageData<ClsShareReport>
	*/
	public PageData<ClsShareReport> queryClsShareReport(PageData<ClsShareReport> pageDate,
			ClsShareReport shareReport);
	
	/**
	 * @方法描述:  查询分润结算记录
	 * @作者： 1603254
	 * @日期： 2016-5-25-上午10:36:36
	 * @param pageDate
	 * @param shareReport
	 * @return 
	 * @返回类型： PageData<ClsShareReport>
	 */
	public PageData<ClsShareDetail> queryClsShareDetail(PageData<ClsShareDetail> pageData,
			ClsShareDetail detail);
	
	
	/**
	 * @方法描述:  手续费分润报表表头
	 * @作者： 1603254
	 * @日期： 2016-6-2-上午10:50:33
	 * @param report
	 * @return 
	 * @返回类型： ReporHeadDto
	*/
	public ReporHeadDto  queryShareReportSummary(ClsShareReport report);
	
	
	/**
	 * @方法描述:  手续费分润明细报表表头
	 * @作者： 1603254
	 * @日期： 2016-6-2-上午10:50:33
	 * @param report
	 * @return 
	 * @返回类型： ReporHeadDto
	 */
	public ReporHeadDto  queryShareDetailSummary(ClsShareDetail detail);
	
	/**
	 * 获取重新结算审核列表
	 * @param pageData
	 * @param queryParam
	 * @return
	 */
	public PageData<ClsShareReport> findShareRecordAudit(
			PageData<ClsShareReport> pageData, ClsShareReport queryParam);
	/**
	 * 判断申诉人和审核人是否相同
	 * @param keyId
	 * @param currentUserLoginName
	 * @param wait
	 * @return
	 */
	public boolean isEqual(String keyId, String currentUserLoginName,
			CLSReviewStatus wait);
 
}

