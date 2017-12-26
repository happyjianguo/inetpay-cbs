package com.ylink.inetpay.cbs.cls.app;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.cbs.cls.service.ClsShareReportService;
import com.ylink.inetpay.common.core.constant.CLSBusinessType;
import com.ylink.inetpay.common.core.constant.CLSReviewStatus;
import com.ylink.inetpay.common.project.cbs.app.ClsShareSettleAppService;
import com.ylink.inetpay.common.project.clear.app.ClearShareAppService;
import com.ylink.inetpay.common.project.clear.dto.ClsRecordCheck;
import com.ylink.inetpay.common.project.clear.dto.ClsShareReport;

/**
 * @类名称： ClsShareSettleAppService
 * @类描述： 分润结算 服务类
 * @创建人： 1603254
 * @创建时间： 2016-5-25 上午10:09:52
 *
 * @修改人： 1603254
 * @操作时间： 2016-5-25 上午10:09:52
 * @操作原因： 
 * 
 */
@Service("clsShareSettleAppService")
public class ClsShareSettleAppServiceImpl implements ClsShareSettleAppService {

	@Autowired
	private ClsShareReportService shareReportService;
	@Autowired
	private ClearShareAppService clearShareAppService;

	
	/**
	 * @方法描述:  查询所有的分润机构
	 * @作者： 1603254
	 * @日期： 2016-5-25-上午10:11:08
	 * @return 
	 * @返回类型： List<ClsShareReport>
	*/
	public List<ClsShareReport> findAllShareOrg(){
		return shareReportService.findAllShare();
	}
	
	
	/**
	 * @方法描述: 查询分润结算记录
	 * @作者： 1603254
	 * @日期： 2016-5-25-上午10:14:32
	 * @param pageDate
	 * @param shareReport
	 * @return 
	 * @返回类型： List<ClsShareReport>
	*/
	public PageData<ClsShareReport> findShareRecord(PageData<ClsShareReport> pageDate,
			ClsShareReport shareReport){
		return shareReportService.queryClsShareReport(pageDate, shareReport);
	}
	
	
	/**
	 * @方法描述:  申请结算
	 * @作者： 1603254
	 * @日期： 2016-5-25-上午10:23:03
	 * @param id
	 * @return 
	 * @返回类型： String
	*/
	public String applyToSettle(String id,String loginName,
			String realName,String suggestion){
		if(findById(id) == null ){
			return "记录不存在,不能申请审核";
		}
		ClsRecordCheck check=new ClsRecordCheck();
		check.setId(UUID.randomUUID().toString());
		check.setApplicant(loginName);
		check.setApplicantName(realName);
		check.setBusiType(CLSBusinessType.SHARE);
		check.setReportId(id);
		check.setReviewStatus(CLSReviewStatus.WAIT);
		check.setApplySugg(suggestion);
		return clearShareAppService.applyArtifactAdjust(check);
	}	
	
	/**
	 * @方法描述:  审核
	 * @作者： 1603254
	 * @日期： 2016-5-25-上午10:24:07
	 * @param id
	 * @param reviewStatus
	 * @return 
	 * @返回类型： String
	*/
	public String review(String id,String userId,
			String userName,String suggestion,CLSReviewStatus reviewStatus){
		ClsRecordCheck check=new ClsRecordCheck();
		check.setReportId(id);
		check.setAuditer(userId);
		check.setAuditerName(userName);
		check.setCheckSugg(suggestion);
		check.setReviewStatus(reviewStatus);
		check.setBusiType(CLSBusinessType.SHARE);
		return  clearShareAppService.review(check);
	}


	/**
	 * @方法描述:  根据id查找
	 * @作者： 1603254
	 * @日期： 2016-5-25-下午4:19:38
	 * @param id
	 * @return 
	 * @返回类型： CLsMerchantSettleVo
	*/
	public ClsShareReport findById(String id) {
		return shareReportService.queryById(id);
	}


	@Override
	public PageData<ClsShareReport> findShareRecordAudit(
			PageData<ClsShareReport> pageData, ClsShareReport queryParam) {
		return shareReportService.findShareRecordAudit(pageData,queryParam);
	}


	@Override
	public String confirmShare(String id) {
		return clearShareAppService.confirmShare(id);
	}


	@Override
	public boolean isEqual(String keyId, String currentUserLoginName,
			CLSReviewStatus wait) {
		return shareReportService.isEqual(keyId,currentUserLoginName,wait);
	}
	 
}
