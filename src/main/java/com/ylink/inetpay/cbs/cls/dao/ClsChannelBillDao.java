package com.ylink.inetpay.cbs.cls.dao;

import java.util.List;

import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

import com.ylink.inetpay.common.core.dto.ReporHeadDto;
import com.ylink.inetpay.common.project.clear.dto.ClsChannelBill;
import com.ylink.inetpay.common.project.clear.dto.ClsChannelBillVo;

/**
 * 
 * 类说明：
 * 实现ClsChannelBill 的增删查改
 * 
 * <p>
 * 详细描述：
 *   
 * @author 1603254
 *   
 * CreateDate:  2016-4-29
 */
@MybatisMapper("clsChannelBillDao")
public interface ClsChannelBillDao {

	
	/**
	 * @方法描述:  明细（所有审核状态的都查出来）
	 * @作者： 1603254
	 * @日期： 2016-5-6-下午3:18:23
	 * @param batchId
	 * @return 
	 * @返回类型： List<ClsExceptionOrder>
	 */
	public List<ClsChannelBillVo> queryChannel(ClsChannelBillVo channel);
	
	/**
	 * @方法描述:   审核列表查询
	 * @作者： 1603254
	 * @日期： 2016-5-6-下午3:18:23
	 * @param batchId
	 * @return 
	 * @返回类型： List<ClsExceptionOrder>
	*/
	public List<ClsChannelBillVo> queryChannelReview(ClsChannelBillVo channel);
	
	/**
	 * @方法描述:  审核结果列表查询
	 * @作者： 1603254
	 * @日期： 2016-5-6-下午3:18:23
	 * @param batchId
	 * @return 
	 * @返回类型： List<ClsExceptionOrder>
	 */
	public List<ClsChannelBillVo> queryChannelReviewResult(ClsChannelBillVo channel);
	
	/**
	 * @方法描述:   调账列表查询
	 * @作者： 1603254
	 * @日期： 2016-5-6-下午3:18:23
	 * @param batchId
	 * @return 
	 * @返回类型： List<ClsExceptionOrder>
	 */
	public List<ClsChannelBillVo> queryChannelAdjust(ClsChannelBillVo channel);

	/**
	 * @方法描述:  查询汇总数据
	 * @作者： 1603254
	 * @日期： 2016-5-26-上午10:23:44
	 * @param channel
	 * @return 
	 * @返回类型： ReporHeadDto
	*/
	public  ReporHeadDto queryChannelSummary(ClsChannelBillVo channel);
	
	/**
	 * @方法描述:  根据记录审核表id查找
	 * @作者： 1603254
	 * @日期： 2016-5-25-下午4:19:38
	 * @param id
	 * @return 
	 * @返回类型： ClsChannelBillVo
	*/
	public ClsChannelBillVo queryById(String id);
	/**
	 * @方法描述:  查询汇总数据
	 * @作者： yuqingjun
	 * @日期： 2016-12-26-上午10:23:44
	 * @param channel
	 * @return 
	*/
	public List<ClsChannelBill> queryBankFeeDetail(ClsChannelBill report);
}