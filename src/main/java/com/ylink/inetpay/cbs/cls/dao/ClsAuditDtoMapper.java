package com.ylink.inetpay.cbs.cls.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

import com.ylink.inetpay.common.core.constant.BISAuditType;
import com.ylink.inetpay.common.project.clear.dto.ClsAuditDto;

@MybatisMapper("clsAuditDtoMapper")
public interface ClsAuditDtoMapper {
    int deleteByPrimaryKey(String id);

    int insert(ClsAuditDto record);

    int insertSelective(ClsAuditDto record);

    ClsAuditDto selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(ClsAuditDto record);

    int updateByPrimaryKeyWithBLOBs(ClsAuditDto record);

    int updateByPrimaryKey(ClsAuditDto record);

	List<ClsAuditDto> getByCond(String busId);
	/**
	 * 判断用户是否已经参与审核
	 * @param id
	 * @param loginName
	 * @return
	 */
	List<ClsAuditDto> isAudit(@Param("id")String id, @Param("loginName")String loginName);
	/**
	 * 根据条件查询记录列表
	 * @param queryParam
	 * @return
	 */
	List<ClsAuditDto> queryAllToList(ClsAuditDto queryParam);
	/**
	 * 根据业务id获取审核记录
	 * @param id
	 * @return
	 */
	List<ClsAuditDto> findListBybusId(@Param("id")String id, @Param("auditType")BISAuditType auditType);
}