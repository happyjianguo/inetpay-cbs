package com.ylink.inetpay.cbs.cls.dao;

import java.util.List;
import java.util.Map;

import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

import com.ylink.inetpay.common.core.dto.ReporHeadDto;
import com.ylink.inetpay.common.project.clear.dto.ClsPayBill;
import com.ylink.inetpay.common.project.clear.dto.ClsPayBillVo;
import com.ylink.inetpay.common.project.clear.dto.ClsTradeDetailVo;
import com.ylink.inetpay.common.project.clear.dto.ClsTradeStationVo;

/**
 * @类名称： ClsPayBillDao
 * @类描述： 支付流水抽取表数据库操作
 * @创建人： 1603254
 * @创建时间： 2016-5-24 下午3:55:17
 *
 * @修改人： 1603254
 * @操作时间： 2016-5-24 下午3:55:17
 * @操作原因： 
 * 
 */
@MybatisMapper("clsPayBillDao")
public interface ClsPayBillDao {
	
	/**
	 * @方法描述:  支付流水抽取表查询（所有审核状态查询）
	 * @作者： 1603254
	 * @日期： 2016-5-24-下午3:55:24
	 * @param payBill
	 * @return 
	 * @返回类型： List<ClsPayBillVo>
	*/
	List<ClsPayBillVo> queryPayBill(ClsPayBillVo payBill);
	/**
	 * @方法描述:  支付流水抽取表查询（调账查询）
	 * @作者： 1603254
	 * @日期： 2016-5-24-下午3:55:24
	 * @param payBill
	 * @return 
	 * @返回类型： List<ClsPayBillVo>
	 */
	List<ClsPayBillVo> queryPayBillAdjust(ClsPayBillVo payBill);
	/**
	 * @方法描述:  支付流水抽取表查询（审核查询）
	 * @作者： 1603254
	 * @日期： 2016-5-24-下午3:55:24
	 * @param payBill
	 * @return 
	 * @返回类型： List<ClsPayBillVo>
	 */
	List<ClsPayBillVo> queryPayBillReview(ClsPayBillVo payBill);
	
	/**
	 * @方法描述:  支付流水抽取表查询（审核结果查询）
	 * @作者： 1603254
	 * @日期： 2016-5-24-下午3:55:24
	 * @param payBill
	 * @return 
	 * @返回类型： List<ClsPayBillVo>
	 */
	List<ClsPayBillVo> queryPayBillReviewResult(ClsPayBillVo payBill);
	
	/**
	 * @方法描述:  支付流水抽取表查询 汇总
	 * @作者： 1603254
	 * @日期： 2016-5-24-下午3:55:24
	 * @param payBill
	 * @return 
	 * @返回类型： List<ClsPayBillVo>
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
	 * @方法描述:  支付流水抽取表查询（审核结果查询）
	 * @作者： 1603254
	 * @日期： 2016-5-24-下午3:55:24
	 * @param payBill
	 * @return 
	 * @返回类型： List<ClsPayBillVo>
	 */
	public List<ClsTradeDetailVo> queryMerTradeDetail(ClsTradeDetailVo detail);
	
	public ReporHeadDto queryMerTradeDetailSummarySuccess(ClsTradeDetailVo detail);
	
	public ReporHeadDto queryMerTradeDetailSummaryAll(ClsTradeDetailVo detail);
	
	public ClsTradeStationVo queryStation(ClsTradeStationVo station);

	public List<ClsTradeStationVo>  queryActiveUser(ClsTradeStationVo vo);

	public List<ClsTradeStationVo>  queryActiveMer(ClsTradeStationVo vo);
	
	/**
	 * 获取对账列表表头
	 * @param payBill
	 * @return
	 */
	ReporHeadDto CheckInnerDetail(ClsPayBillVo payBill);
	/**
	 * 获取对账列表表头
	 * @param payBill
	 * @return
	 */
	ReporHeadDto CheckInnerDetail2(ClsPayBillVo payBill);
	
	/**
	 * 获取调账列表表头
	 * @param payBill
	 * @return
	 */
	ReporHeadDto InnerDetailAdjust(ClsPayBillVo payBill);
	/**
	 * 获取审核列表表头
	 * @param payBill
	 * @return
	 */
	ReporHeadDto InnerDetailReview(ClsPayBillVo payBill);
	/**
	 * 获取审核结果列表表头
	 * @param payBill
	 * @return
	 */
	ReporHeadDto CheckInnerDetailReviewResult(ClsPayBillVo payBill);
	
	/****
	 * 查询平账支付抽取流水
	 * @description 
	 * @param map
	 * @return  
	 * @author lyg
	 * @date 2016-9-12
	 */
	List<ClsPayBill> queryOrderByMerchantAndDate(ClsPayBill bill);
  
}
