package com.ylink.inetpay.cbs.cls.app;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;
import com.ylink.inetpay.cbs.cls.dao.ClsExceptionOrderDao;
import com.ylink.inetpay.cbs.cls.service.ClsCheckInnerService;
import com.ylink.inetpay.cbs.cls.service.ClsRecordCheckService;
import com.ylink.inetpay.common.core.constant.CLSBusinessType;
import com.ylink.inetpay.common.core.constant.CLSReviewStatus;
import com.ylink.inetpay.common.core.dto.ReporHeadDto;
import com.ylink.inetpay.common.project.cbs.app.ClsCheckInnerAppService;
import com.ylink.inetpay.common.project.clear.dto.ClsPayBillVo;
import com.ylink.inetpay.common.project.clear.dto.ClsRecordCheck;
import com.ylink.inetpay.common.project.clear.app.ClearInnerCheckAppService;
/**
 * @类名称： ClsCheckInnerAppService
 * @类描述： 内部对账服务类
 * @创建人： 1603254
 * @创建时间： 2016-5-24 下午4:17:16
 *
 * @修改人： 1603254
 * @操作时间： 2016-5-24 下午4:17:16
 * @操作原因： 
 * 
 */
@Service("clsCheckInnerAppService")
public class ClsCheckInnerAppServiceImpl implements  ClsCheckInnerAppService{

	@Autowired
	private ClsCheckInnerService checkInnerService;
	@Autowired
	private ClsExceptionOrderDao exceptionOrderDao;
	@Autowired 
	private ClearInnerCheckAppService clearInnerCheckAppService;
	
	/**
	 * @方法描述: 查询内部对账明细(调账)
	 * @作者： 1603254
	 * @日期： 2016-5-24-下午4:17:20
	 * @param pageDate
	 * @param payBill
	 * @return 
	 * @返回类型： PageData<ClsPayBillVo>
	*/
	public PageData<ClsPayBillVo> findCheckInnerDetailAdjust(PageData<ClsPayBillVo> pageDate,
			ClsPayBillVo payBill){
		return checkInnerService.findCheckDetailAdjust(pageDate, payBill);
	}

	/**
	 * @方法描述:  查询内部对账明细(审核)
	 * @作者： 1603254
	 * @日期： 2016-5-24-下午4:17:20
	 * @param pageDate
	 * @param payBill
	 * @return 
	 * @返回类型： PageData<ClsPayBillVo>
	 */
	public PageData<ClsPayBillVo> findCheckInnerDetailReview(
			PageData<ClsPayBillVo> pageDate, ClsPayBillVo payBill) {
		return checkInnerService.findCheckDetailReview(pageDate, payBill);
	}
	

	/**
	 * @方法描述:  查询内部对账(审核结果)
	 * @作者： 1603254
	 * @日期： 2016-5-24-下午4:17:20
	 * @param pageDate
	 * @param payBill
	 * @return 
	 * @返回类型： PageData<ClsPayBillVo>
	 */
	public PageData<ClsPayBillVo> findCheckInnerDetailReviewResult(
			PageData<ClsPayBillVo> pageDate, ClsPayBillVo payBill) {
		return checkInnerService.findCheckDetailReviewResult(pageDate, payBill);
	}

	/**
	 * @方法描述:  查询内部对账明细
	 * @作者： 1603254
	 * @日期： 2016-5-24-下午4:17:20
	 * @param pageDate
	 * @param payBill
	 * @return 
	 * @返回类型： PageData<ClsPayBillVo>
	 */
	public PageData<ClsPayBillVo> findCheckInnerDetail(
			PageData<ClsPayBillVo> pageDate, ClsPayBillVo payBill) {
		return checkInnerService.findCheckDetail(pageDate, payBill);
	}
	
	/**
	 * @方法描述:  查询调账根据id
	 * @作者： 1603254
	 * @日期： 2016-5-24-下午5:39:48
	 * @param pageDate
	 * @param payBill
	 * @return 
	 * @返回类型： PageData<ClsPayBillVo>
	 */
	public ClsPayBillVo findById(String id) {
		return checkInnerService.queryById(id);
	}

	/**
	 * @方法描述:  申请审核
	 * @作者： 1603254
	 * @日期： 2016-5-25-上午10:23:03
	 * @param id
	 * @return 
	 * @返回类型： String
	*/
	public String applyToAdjust(String id, String userId, String userName,
			String suggestion) {
		if(exceptionOrderDao.queryExceptionOrderById(id) == null ){
			return "记录不存在,不能申请审核";
		}
		ClsRecordCheck check=new ClsRecordCheck();
		check.setId(UUID.randomUUID().toString());
		check.setApplicant(userId);
		check.setApplicantName(userName);
		check.setBusiType(CLSBusinessType.CHECK_ADJUST);
		check.setReportId(id);
		check.setReviewStatus(CLSReviewStatus.WAIT);
		check.setApplySugg(suggestion);
		return clearInnerCheckAppService.applyArtifactAdjust(check);	
	}
	
	public String adjustRefund(ClsPayBillVo pay) {
		return clearInnerCheckAppService.adjustRefund(pay);	
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
	public String review(String id, String userId, String userName,
			String suggestion, CLSReviewStatus reviewStatus) {
		ClsRecordCheck check=new ClsRecordCheck();
		check.setId(id);
		check.setAuditer(userId);
		check.setAuditerName(userName);
		check.setCheckSugg(suggestion);
		check.setReviewStatus(reviewStatus);
		check.setBusiType(CLSBusinessType.CHECK_ADJUST);
		return clearInnerCheckAppService.review(check);
	}

	

	/**
	 * @方法描述: 汇总数据
	 * @作者： 1603254
	 * @日期： 2016-5-26-上午9:42:41
	 * @param pageDate
	 * @param channel
	 * @return 
	 * @返回类型： ReporHeadDto
	*/
	public ReporHeadDto findCheckInnerSummary(ClsPayBillVo payBill) {
		return checkInnerService.queryPayBillSumarry(payBill);
	}

	@Override
	public ReporHeadDto getReporHead(ClsPayBillVo queryParam) {
		return checkInnerService.getReporHead(queryParam);
	}
	
	@Override
	public boolean isEqual(String keyId, String currentUserLoginName,
			CLSReviewStatus wait) {
		return checkInnerService.isEqual(keyId,currentUserLoginName,wait);
	}
}
