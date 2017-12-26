package com.ylink.inetpay.cbs.bis.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

import com.ylink.inetpay.common.core.constant.BISAuditStatus;
import com.ylink.inetpay.common.core.constant.PayStatusEnum;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisAccountUnfrozenAuditDto;
@MybatisMapper("bisAccountUnfrozenAuditDtoMapper")
public interface BisAccountUnfrozenAuditDtoMapper {
    int deleteByPrimaryKey(String id);

    int insert(BisAccountUnfrozenAuditDto record);

    int insertSelective(BisAccountUnfrozenAuditDto record);

    BisAccountUnfrozenAuditDto selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(BisAccountUnfrozenAuditDto record);

    int updateByPrimaryKey(BisAccountUnfrozenAuditDto record);
    /**
     * 获取解冻订单列表
     * @param queryParam
     * @return
     */
	List<BisAccountUnfrozenAuditDto> listUnFrozen(BisAccountUnfrozenAuditDto queryParam);
	/**
	 * 获取所有复核通过，待处理，解冻日期小于等于当前账务日期的记录
	 * @param actDate
	 * @return
	 */
	List<BisAccountUnfrozenAuditDto> listUnFrozenByEndTime(Date actDate);
	/**
	 * 查询未知状态的记录（处理状态未知）
	 * @return
	 */
	List<BisAccountUnfrozenAuditDto> autoQueryUnDownStatus();
	/**
	 * 获取所有超时待复核的记录
	 * @return
	 */
	List<BisAccountUnfrozenAuditDto> runOutWaitAuditByEndTime(Date actDate);
	/**
	 * 复核通过，待处理的记录数
	 * @param ids
	 * @param auditPass
	 * @param waitpay
	 * @return
	 */
	long getAuditNum(@Param("ids")List<String> ids, @Param("auditPass")BISAuditStatus auditPass, @Param("waitpay")PayStatusEnum waitpay);
	
}