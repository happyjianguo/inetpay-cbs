package com.ylink.inetpay.cbs.cls.app;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.cbs.cls.dao.ClsExceptionOrderDao;
import com.ylink.inetpay.cbs.cls.service.ClsCheckChannelService;
import com.ylink.inetpay.cbs.cls.service.ClsRecordCheckService;
import com.ylink.inetpay.common.core.constant.CLSBusinessType;
import com.ylink.inetpay.common.core.constant.CLSReviewStatus;
import com.ylink.inetpay.common.core.dto.ReporHeadDto;
import com.ylink.inetpay.common.project.cbs.app.ClsCheckChannelAppService;
import com.ylink.inetpay.common.project.clear.app.ClearChannelCheckAppService;
import com.ylink.inetpay.common.project.clear.dto.ClsChannelBillVo;
import com.ylink.inetpay.common.project.clear.dto.ClsRecordCheck;

 
/**
 * @类名称： ClsCheckChannelAppServiceImpl
 * @类描述： 
 * @创建人： 1603254
 * @创建时间： 2016-5-26 上午10:20:47
 *
 * @修改人： 1603254
 * @操作时间： 2016-5-26 上午10:20:47
 * @操作原因： 
 * 
 */
@Service("clsCheckChannelAppService")
public class ClsCheckChannelAppServiceImpl implements ClsCheckChannelAppService{

	@Autowired
	private ClsCheckChannelService channelService;
	@Autowired
	private ClsExceptionOrderDao exceptionOrderDao;
	@Autowired
	private ClearChannelCheckAppService channelCheckAppService;
	
	/**
	 * @方法描述: 申请调账
	 * @作者： 1603254
	 * @日期： 2016-5-25-下午12:14:15
	 * @param id
	 * @param userId
	 * @param userName
	 * @param suggestion
	 * @return 
	 * @返回类型： String
	*/
	public String applyAdjust(String id,String userId,String userName,String suggestion){
		if(exceptionOrderDao.queryExceptionOrderById(id) == null ){
			return "异常记录不存在,不能申请审核";
		}
		ClsRecordCheck check=new ClsRecordCheck();
		check.setId(UUID.randomUUID().toString());
		check.setApplicant(userId);
		check.setApplicantName(userName);
		check.setBusiType(CLSBusinessType.CHECK_ADJUST);
		check.setReportId(id);
		check.setReviewStatus(CLSReviewStatus.WAIT);
		check.setApplySugg(suggestion);
		return channelCheckAppService.applyArtifactAdjust(check);
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
		check.setId(id);
		check.setAuditer(userId);
		check.setAuditerName(userName);
		check.setCheckSugg(suggestion);
		check.setReviewStatus(status);
		check.setBusiType(CLSBusinessType.CHECK_ADJUST);
		return channelCheckAppService.reviewAdjustOrder(check);
	}
	
	/**
	 * @方法描述:  根据id查找
	 * @作者： 1603254
	 * @日期： 2016-5-25-下午4:19:38
	 * @param id
	 * @return 
	 * @返回类型： ClsChannelBillVo
	*/
	public ClsChannelBillVo findById(String id) {
		return channelService.queryById(id);
	}

	/**
	 * @方法描述: 查询资金渠道对账记录 (对账明细)
	 * @作者： 1603254
	 * @日期： 2016-5-25-下午3:16:42
	 * @param pageDate
	 * @param channel
	 * @return 
	 * @返回类型： PageData<ClsChannelBillVo>
	 */
	public PageData<ClsChannelBillVo> findCheckChannel(
			PageData<ClsChannelBillVo> pageDate, ClsChannelBillVo channel) {
		return channelService.findCheckChannel(pageDate, channel);
	}

	/**
	 * @方法描述: 查询资金渠道对账记录 (调账的)
	 * @作者： 1603254
	 * @日期： 2016-5-25-下午3:16:42
	 * @param pageDate
	 * @param channel
	 * @return 
	 * @返回类型： PageData<ClsChannelBillVo>
	*/
	public PageData<ClsChannelBillVo> findCheckChannelAdjust(
			PageData<ClsChannelBillVo> pageDate, ClsChannelBillVo channel) {
		return channelService.findCheckChannelAdjust(pageDate, channel);
	}

	/**
	 * @方法描述: 查询资金渠道对账记录 (审核的)
	 * @作者： 1603254
	 * @日期： 2016-5-25-下午3:16:42
	 * @param pageDate
	 * @param channel
	 * @return 
	 * @返回类型： PageData<ClsChannelBillVo>
	 */
	public PageData<ClsChannelBillVo> findCheckChannelReview(
			PageData<ClsChannelBillVo> pageDate, ClsChannelBillVo channel) {
		return channelService.findCheckChannelReview(pageDate, channel);
	}
	
	/**
	 * @方法描述: 查询资金渠道对账记录 (审核结果)
	 * @作者： 1603254
	 * @日期： 2016-5-25-下午3:16:42
	 * @param pageDate
	 * @param channel
	 * @return 
	 * @返回类型： PageData<ClsChannelBillVo>
	 */
	public PageData<ClsChannelBillVo> findCheckChannelReviewResult(
			PageData<ClsChannelBillVo> pageDate, ClsChannelBillVo channel) {
		return channelService.findCheckChannelReviewResult(pageDate, channel);
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
	public ReporHeadDto findCheckChannelSummary(ClsChannelBillVo channel) {
		return channelService.findCheckChannelSummary(channel);
	}

	@Override
	public boolean isEqual(String keyId, String currentUserLoginName,
			CLSReviewStatus wait) {
		return channelService.isEqual(keyId,currentUserLoginName,wait);
	}
}
