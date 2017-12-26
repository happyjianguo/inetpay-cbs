package com.ylink.inetpay.cbs.bis.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

import com.ylink.inetpay.common.core.constant.BISAuditStatus;
import com.ylink.inetpay.common.core.constant.BISAuditType;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisAuditDto;

@MybatisMapper("bisAuditDtoMapper")
public interface BisAuditDtoMapper {
    int deleteByPrimaryKey(String id);

    int insert(BisAuditDto record);

    int insertSelective(BisAuditDto record);

    BisAuditDto selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(BisAuditDto record);

    int updateByPrimaryKeyWithBLOBs(BisAuditDto record);

    int updateByPrimaryKey(BisAuditDto record);

	List<BisAuditDto> getByCond(String busId);
	/**
	 * 判断用户是否已经参与审核
	 * @param id
	 * @param loginName
	 * @return
	 */
	List<BisAuditDto> isAudit(@Param("id")String id, @Param("loginName")String loginName,@Param("auditType")BISAuditType auditType);
	/**
	 * 根据当前登录用户查找被该用户审核过的业务id
	 */
	List<BisAuditDto> findBisAuditDtoByLoginName(@Param("loginName")String loginName,@Param("ids")List<String> ids,@Param("auditType")BISAuditType auditType);
	
	/**
	 * 根据条件查询记录列表
	 * @param queryParam
	 * @return
	 */
	List<BisAuditDto> queryAllToList(BisAuditDto queryParam);
	/**
	 * 根据业务id获取审核记录
	 * @param id
	 * @return
	 */
	List<BisAuditDto> findListBybusId(@Param("id")String id, @Param("auditType")BISAuditType auditType);
	/**
	 * 获取用户审核记录数量
	 * @param ids
	 * @param loginName
	 * @param auditType
	 * @return
	 */
	long getAucitNum(@Param("ids")List<String> ids, @Param("loginName")String loginName, 
			@Param("auditStatus")BISAuditStatus auditStatus,@Param("auditType")BISAuditType auditType);
	/**
	 * 获取冻结解冻审核记录
	 * @param id
	 * @param amountFrozen
	 * @param amountUnfreeze 
	 * @return
	 */
	public List<BisAuditDto> findFrozenListBybusId(@Param("id")String id, 
			@Param("amountFrozen")BISAuditType amountFrozen);
	/**
	 * 获取用户退款复核记录
	 * @param busiIds
	 * @param busiRefund
	 * @param merRefund
	 * @param loginName
	 * @return
	 */
	List<BisAuditDto> findRefundListBybusIds(@Param("busiIds")List<String> busiIds, 
			@Param("busiRefund")BISAuditType busiRefund, 
			@Param("merRefund")BISAuditType merRefund,
			@Param("loginName")String loginName);
	/**
	 * 获取退款审核记录
	 * @param id
	 * @param refund
	 * @return
	 */
	public List<BisAuditDto> findPayDataListBybusId(@Param("id")String id, 
			@Param("refund")BISAuditType refund);
}