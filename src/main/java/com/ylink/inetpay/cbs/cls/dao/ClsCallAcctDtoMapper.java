package com.ylink.inetpay.cbs.cls.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

import com.ylink.inetpay.common.core.constant.CLSPayType;
import com.ylink.inetpay.common.core.constant.CLSReviewStatus;
import com.ylink.inetpay.common.core.constant.CLSSettleStatus;
import com.ylink.inetpay.common.project.clear.dto.ClsCallAcct;

/**
 * 
 * 类说明：
 * 实现ClsCallAcct 的增删查改
 * 
 * <p>
 * 详细描述：
 *   
 * @author 1603254
 *   
 * CreateDate:  2016-4-29
 */
@MybatisMapper("clsCallAcctDao")
public interface ClsCallAcctDtoMapper {
	/**
	 * 方法说明： 
	 * 添加ClsCallAcct
	 * @param  ClsCallAcct				
	 */
	void addClsCallAcct(ClsCallAcct clsCallAcct);
	

	/**
	 * 方法说明： 
	 * 查询TbClsCallAcct
	 * @param  TbClsCallAcct				
	 * @return List 	查询的结果集
	 */	
	List<ClsCallAcct> queryClsCallAcctByDate(String callDay);

	
	/**
	 * @方法描述: 查询为统计的记录条数
	 * @作者： 1603254
	 * @日期： 2016-5-18-上午11:01:10
	 * @param callDay
	 * @return 
	 * @返回类型： Integer
	*/
	public  Integer queryUnAccountRecordCount(String callDay);
	
	/**
	 * @方法描述: 根据条件查询资金调拨
	 * @作者： 1603254
	 * @日期： 2016-5-20-下午2:03:14
	 * @param map
	 * @return 
	 * @返回类型： List<ClsCallAcct>
	*/
	public List<ClsCallAcct> queryByStatus(Map<String,String> map);
	
	/**
	 * 方法说明： 
	 * 删除ClsCallAcct
	 * @param  ClsCallAcct的标识id				
	 */	
	void deleteClsCallAcct(List<String> list);
	
	
	/**
	 * 方法说明： 
	 * 更新ClsCallAcct
	 * @param  ClsCallAcct				
	 * @return List 	查询的结果集
	 */		
	void updateClsCallAcct(ClsCallAcct clsCallAcct);
	
	
	/**
	 * @方法描述: 更新资金调拨状态
	 * @作者： 1603254
	 * @日期： 2016-5-19-下午5:51:02
	 * @param map 
	 * @返回类型： void
	*/
	void updateCallAcctStatus(Map<String,String> map);

	/**
	 * 查询备付金调拨审核列表(手工调账且没有审核通过记录的，如果有待审核且已结算、审核通过待审核同存 的都属于系统bug)(手工调账，且结算状态为审核的)
	 * @param clsCallAcct
	 * @return
	 */
	List<ClsCallAcct> pageAuditPageList(@Param("acct")ClsCallAcct acct,@Param("payment")CLSPayType payment,@Param("settleStatus")CLSSettleStatus settleStatus);

	/**
	 * 查询备付金审核历史列表（手工调账且审核通过或拒绝）
	 * @param clsCallAcct
	 * @return
	 */
	List<ClsCallAcct> pageAuditResultPageList(@Param("acct")ClsCallAcct acct,@Param("payment")CLSPayType payment,@Param("auditPass")CLSReviewStatus auditPass,@Param("auditReject")CLSReviewStatus auditReject);

	/**
	 * 查询备付金调拨支付列表(手工调账且审核通过)(结算状态为结算，并且手工调账)
	 * @param clsCallAcct
	 * @return
	 */
	List<ClsCallAcct> pagePayPageList(@Param("acct")ClsCallAcct acct/*,@Param("payment")CLSPayType payment*/,@Param("settleStatus")CLSSettleStatus settleStatus);
	/**
	 * 详情
	 * @param clsCallAcct
	 * @return
	 */
	ClsCallAcct details(String id);
}