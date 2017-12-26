package com.ylink.inetpay.cbs.cls.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

import com.ylink.inetpay.common.core.constant.CLSReviewStatus;
import com.ylink.inetpay.common.core.dto.ReporHeadDto;
import com.ylink.inetpay.common.project.clear.dto.ClsPayBillVo;
import com.ylink.inetpay.common.project.clear.dto.ClsShareDetail;
import com.ylink.inetpay.common.project.clear.dto.ClsShareReport;

/**
 * @类名称： ClsShareReportDao
 * @类描述： 
 * @创建人： 1603254
 * @创建时间： 2016-5-25 上午10:29:07
 *
 * @修改人： 1603254
 * @操作时间： 2016-5-25 上午10:29:07
 * @操作原因： 
 * 
 */
@MybatisMapper("clsShareReportDao")
public interface ClsShareReportDao {

	/**
	 * @方法描述:  (这里用一句话描述这个方法的作用)
	 * @作者： 1603254
	 * @日期： 2016-5-25-上午10:29:04
	 * @return 
	 * @返回类型： List<ClsShareReport>
	*/
	public List<ClsShareReport> querALlShareOrg();
	
	/**
	 * @方法描述:  (这里用一句话描述这个方法的作用)
	 * @作者： 1603254
	 * @日期： 2016-5-25-上午10:29:03
	 * @return 
	 * @返回类型： List<ClsShareReport>
	*/
	public List<ClsShareReport> queryClsShareReport(ClsShareReport report);
	
	/**
	 * @方法描述:  根据id查找
	 * @作者： 1603254
	 * @日期： 2016-5-25-下午4:19:38
	 * @param id
	 * @return 
	 * @返回类型： ClsChannelBillVo
	*/
	public ClsShareReport queryById(String id);
	
	/**
	 * @方法描述:  根据id查询
	 * @作者： 1603254
	 * @日期： 2016-5-25-上午10:51:28
	 * @param id
	 * @return 
	 * @返回类型： ClsShareReport
	*/
	public ClsShareReport findById(String id);
	
	
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
	 * 获取重新结算审核列表
	 * @param queryParam
	 * @return
	 */
	List<ClsShareReport> findShareRecordAudit(ClsShareReport queryParam);
	
	/**
	 * 判断申诉人和审核人是否相同
	 * @param keyId
	 * @param currentUserLoginName
	 * @param wait
	 * @return
	 */
	public String isEqual(@Param("id")String keyId, @Param("applicant")String currentUserLoginName,
			@Param("auditStatus")CLSReviewStatus wait);
	
}
