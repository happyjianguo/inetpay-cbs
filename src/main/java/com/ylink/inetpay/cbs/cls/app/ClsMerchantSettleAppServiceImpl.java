package com.ylink.inetpay.cbs.cls.app;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.cbs.cls.service.ClsMerSettleService;
import com.ylink.inetpay.cbs.cls.service.ClsRecordCheckService;
import com.ylink.inetpay.common.core.constant.CLSBusinessType;
import com.ylink.inetpay.common.core.constant.CLSReviewStatus;
import com.ylink.inetpay.common.project.cbs.app.ClsMerchantSettleAppService;
import com.ylink.inetpay.common.project.clear.app.ClearMerchantAppService;
import com.ylink.inetpay.common.project.clear.dto.CLsMerchantSettleVo;
import com.ylink.inetpay.common.project.clear.dto.ClsMerSett;
import com.ylink.inetpay.common.project.clear.dto.ClsPayBill;
import com.ylink.inetpay.common.project.clear.dto.ClsRecordCheck;

/**
 * @类名称： ClsMerchantSettleAppServiceImpl
 * @类描述： 
 * @创建人： 1603254
 * @创建时间： 2016-5-25 上午11:46:02
 *
 * @修改人： 1603254
 * @操作时间： 2016-5-25 上午11:46:02
 * @操作原因： 
 * 
 */
@Service("clsMerchantSettleAppService")
public class ClsMerchantSettleAppServiceImpl  implements ClsMerchantSettleAppService{


	@Autowired
	private ClsMerSettleService merSettleService;
	@Autowired
	private ClearMerchantAppService clearMerchantAppService;
	/**
	 * @方法描述: 查询商户结算记录
	 * @作者： 1603254
	 * @日期： 2016-5-25-上午10:14:32
	 * @param pageDate
	 * @param shareReport
	 * @return 
	 * @返回类型： List<ClsShareReport>
	*/
	public PageData<CLsMerchantSettleVo> findMerSettle(PageData<CLsMerchantSettleVo> pageDate,
			CLsMerchantSettleVo merSettle){
				return merSettleService.queryClsMerSett(pageDate, merSettle);
	}
	
	
	/**
	 * @方法描述: 申请商户结算
	 * @作者： 1603254
	 * @日期： 2016-5-25-下午12:14:15
	 * @param id
	 * @param userId
	 * @param userName
	 * @param suggestion
	 * @return 
	 * @返回类型： String
	*/
	public String applyMerSettle(String id,String loginName,String realName,String suggestion){
		if(findById(id) == null ){
			return "记录不存在,不能申请审核";
		}
		ClsRecordCheck check=new ClsRecordCheck();
		check.setId(UUID.randomUUID().toString());
		check.setApplicant(loginName);
		check.setApplicantName(realName);
		check.setBusiType(CLSBusinessType.MER_SETTLE);
		check.setReportId(id);
		check.setReviewStatus(CLSReviewStatus.WAIT);
		check.setApplySugg(suggestion);
		return clearMerchantAppService.applyArtifactAdjust(check);
	}
	
	
	/**
	 * @方法描述: 审核
	 * @作者： 1603254
	 * @日期： 2016-5-25-下午12:15:09
	 * @param id
	 * @param userId
	 * @param userName
	 * @param suggestion
	 * @return 
	 * @返回类型： String
	*/
	public String review(String id,String userId,String userName,
			String suggestion,CLSReviewStatus status){
		ClsRecordCheck check=new ClsRecordCheck();
		check.setReportId(id);
		check.setAuditer(userId);
		check.setAuditerName(userName);
		check.setCheckSugg(suggestion);
		check.setReviewStatus(status);
		check.setBusiType(CLSBusinessType.MER_SETTLE);
		return clearMerchantAppService.review(check);
	}


	@Override
	public CLsMerchantSettleVo findById(String id) {
		return merSettleService.queryById(id);
	}


	@Override
	public PageData<CLsMerchantSettleVo> findMerSettleAudit(
			PageData<CLsMerchantSettleVo> pageData,
			CLsMerchantSettleVo queryParam) {
		return merSettleService.findMerSettleAudit(pageData,queryParam);
	}


	@Override
	public String confirmSett(String id) {
		return clearMerchantAppService.confirmSett(id);
	}


	@Override
	public PageData<ClsMerSett> queryClsMerSett(PageData<ClsMerSett> pageData,
			ClsMerSett settle) {
		
		return merSettleService.queryClsMerSett(pageData, settle);
	}


	@Override
	public PageData<ClsPayBill> queryOrderByMerchantAndDate(
			PageData<ClsPayBill> pageData, ClsPayBill bill) {
		return merSettleService.queryOrderByMerchantAndDate(pageData, bill);
	}
}
