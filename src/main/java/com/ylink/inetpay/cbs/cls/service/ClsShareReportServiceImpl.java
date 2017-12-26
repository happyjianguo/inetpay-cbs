package com.ylink.inetpay.cbs.cls.service;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.ylink.inetpay.cbs.cls.dao.ClsRecordCheckDao;
import com.ylink.inetpay.cbs.cls.dao.ClsShareDetailDao;
import com.ylink.inetpay.cbs.cls.dao.ClsShareReportDao;
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
@Service("clsShareReportService")
public class ClsShareReportServiceImpl implements ClsShareReportService{

	@Autowired
	private ClsShareReportDao reportDao;
	@Autowired
	private ClsShareDetailDao shareDetailDao;
	@Autowired
	private ClsRecordCheckDao clsRecordCheckDao;
	
	
	/**
	 * @方法描述:  查询所有的分润机构
	 * @作者： 1603254
	 * @日期： 2016-5-25-上午10:36:25
	 * @return 
	 * @返回类型： List<ClsShareReport>
	*/
	public List<ClsShareReport> findAllShare(){
		return reportDao.querALlShareOrg();	
	}
	

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
			ClsShareReport shareReport){
		PageHelper.startPage(pageDate.getPageNumber(),pageDate.getPageSize());
		List<ClsShareReport> list=reportDao.queryClsShareReport(shareReport);
		Page<ClsShareReport> page=(Page<ClsShareReport>) list;
		pageDate.setTotal(page.getTotal());
		pageDate.setRows(list);
		return pageDate;
	}
 
	/**
	 * @方法描述:  根据id查找
	 * @作者： 1603254
	 * @日期： 2016-5-25-下午4:19:38
	 * @param id
	 * @return 
	 * @返回类型： ClsChannelBillVo
	*/
	public ClsShareReport queryById(String id){
		ClsShareReport queryById = reportDao.queryById(id);
		queryById.setRecordChecks(clsRecordCheckDao.getRecordChecks(id));
		return queryById;
	}
	
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
			ClsShareDetail detail){
		PageHelper.startPage(pageData.getPageNumber(),pageData.getPageSize());
		List<ClsShareDetail> list=shareDetailDao.queryClsShareDetail(detail);
		Page<ClsShareDetail> page=(Page<ClsShareDetail>) list;
		pageData.setTotal(page.getTotal());
		pageData.setRows(list);
		return pageData;
	}
	
	 
	/**
	 * @方法描述:  手续费分润报表表头
	 * @作者： 1603254
	 * @日期： 2016-6-2-上午10:50:33
	 * @param report
	 * @return 
	 * @返回类型： ReporHeadDto
	*/
	public ReporHeadDto queryShareDetailSummary(ClsShareDetail detail) {
		return  shareDetailDao.queryShareDetailSummary(detail);
	}
	
	/**
	 * @方法描述:  手续费分润明细报表表头
	 * @作者： 1603254
	 * @日期： 2016-6-2-上午10:50:33
	 * @param report
	 * @return 
	 * @返回类型： ReporHeadDto
	 */
	public ReporHeadDto queryShareReportSummary(ClsShareReport report) {
		return 	reportDao.queryShareReportSummary(report);		
	}


	@Override
	public PageData<ClsShareReport> findShareRecordAudit(
			PageData<ClsShareReport> pageData, ClsShareReport queryParam) {
		PageHelper.startPage(pageData.getPageNumber(),pageData.getPageSize());
		List<ClsShareReport> list=reportDao.findShareRecordAudit(queryParam);
		Page<ClsShareReport> page=(Page<ClsShareReport>) list;
		pageData.setTotal(page.getTotal());
		pageData.setRows(list);
		return pageData;
	}


	@Override
	public boolean isEqual(String keyId, String currentUserLoginName,
			CLSReviewStatus wait) {
		if("0".equals(reportDao.isEqual(keyId,currentUserLoginName,wait))){
			return false;
		}else{
			return true;
		}
	}
}
