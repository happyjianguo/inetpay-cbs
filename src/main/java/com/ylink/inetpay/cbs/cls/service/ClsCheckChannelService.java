package com.ylink.inetpay.cbs.cls.service;

import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.cbs.cls.dao.ClsChannelBillDao;
import com.ylink.inetpay.common.core.constant.CLSReviewStatus;
import com.ylink.inetpay.common.core.dto.ReporHeadDto;
import com.ylink.inetpay.common.project.clear.dto.ClsChannelBillVo;

/**
 * @类名称： ClsCheckChannelService
 * @类描述： 资金渠道接口类
 * @创建人： 1603254
 * @创建时间： 2016-5-25 下午4:46:17
 *
 * @修改人： 1603254
 * @操作时间： 2016-5-25 下午4:46:17
 * @操作原因： 
 * 
 */
public interface ClsCheckChannelService {

	/**
	 * @方法描述:  查找资金渠道记录
	 * @作者： 1603254
	 * @日期： 2016-5-25-下午4:52:25
	 * @param pageDate
	 * @param channel
	 * @return 
	 * @返回类型： PageData<ClsChannelBillVo>
	 */
	public PageData<ClsChannelBillVo> findCheckChannel(PageData<ClsChannelBillVo> pageDate,
			ClsChannelBillVo channel);
	
	/**
	 * @方法描述:  查找资金渠道记录(审核)
	 * @作者： 1603254
	 * @日期： 2016-5-25-下午4:52:25
	 * @param pageDate
	 * @param channel
	 * @return 
	 * @返回类型： PageData<ClsChannelBillVo>
	*/
	public PageData<ClsChannelBillVo> findCheckChannelReview(PageData<ClsChannelBillVo> pageDate,
			ClsChannelBillVo channel);
	/**
	 * @方法描述:  查找资金渠道记录(审核结果)
	 * @作者： 1603254
	 * @日期： 2016-5-25-下午4:52:25
	 * @param pageDate
	 * @param channel
	 * @return 
	 * @返回类型： PageData<ClsChannelBillVo>
	 */
	public PageData<ClsChannelBillVo> findCheckChannelReviewResult(PageData<ClsChannelBillVo> pageDate,
			ClsChannelBillVo channel);
	/**
	 * @方法描述:  查找资金渠道记录(调账)
	 * @作者： 1603254
	 * @日期： 2016-5-25-下午4:52:25
	 * @param pageDate
	 * @param channel
	 * @return 
	 * @返回类型： PageData<ClsChannelBillVo>
	 */
	public PageData<ClsChannelBillVo> findCheckChannelAdjust(PageData<ClsChannelBillVo> pageDate,
			ClsChannelBillVo channel);
	
	
 
	/**
	 * @方法描述:  根据id查找
	 * @作者： 1603254
	 * @日期： 2016-5-25-下午4:19:38
	 * @param id
	 * @return 
	 * @返回类型： ClsChannelBillVo
	*/
	public ClsChannelBillVo queryById(String id);
	
	
	/**
	 * @方法描述:  查找资金渠道记录 汇总消息
	 * @作者： 1603254
	 * @日期： 2016-5-25-下午4:52:25
	 * @param pageDate
	 * @param channel
	 * @return 
	 * @返回类型： PageData<ClsChannelBillVo>
	*/
	public ReporHeadDto findCheckChannelSummary(ClsChannelBillVo channel);
	
	/**
	 * @方法描述:  (这里用一句话描述这个方法的作用)
	 * @作者： 1603254
	 * @日期： 2016-9-2-上午10:32:36
	 * @param keyId
	 * @param currentUserLoginName
	 * @param wait
	 * @return 
	 * @返回类型： boolean
	*/
	public boolean isEqual(String keyId, String currentUserLoginName,
			CLSReviewStatus wait);
}
