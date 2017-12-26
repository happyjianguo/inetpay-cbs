package com.ylink.inetpay.cbs.cls.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

import com.ylink.inetpay.common.core.constant.CLSReviewStatus;
import com.ylink.inetpay.common.project.clear.dto.ClsRecordCheck;
import com.ylink.inetpay.common.project.clear.dto.ClsReviewVo;


/**
 * 
 * 类说明：
 * 实现ClsRecordCheck 的增删查改
 * 
 * <p>
 * 详细描述：
 *   
 * @author 1603254
 *   
 * CreateDate:  2016-4-29
 */
@MybatisMapper("clsRecordCheckDao")
public interface ClsRecordCheckDao {

	/**
	 * @方法描述: 插入分润结算申请记录
	 * @作者： 1603254
	 * @日期： 2016-5-25-上午10:56:03
	 * @param recordCheck
	 * @return 
	 * @返回类型： ClsRecordCheck
	*/
	public void insertShareSettleRecord(ClsRecordCheck recordCheck);
	
	/**
	 * @方法描述:  更新审核记录
	 * @作者： 1603254
	 * @日期： 2016-5-25-上午11:24:28
	 * @param recordCheck 
	 * @返回类型： void
	*/
	public void updateRecord(ClsRecordCheck recordCheck);
	
	/**
	 * @方法描述:  获取审核记录条数
	 * @作者： 1603254
	 * @日期： 2016-6-1-上午11:49:27
	 * @return 
	 * @返回类型： Integer
	*/
	public ClsReviewVo queryReviewCount();
	
	public List<ClsRecordCheck> getRecordChecks(String busId);
	
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
