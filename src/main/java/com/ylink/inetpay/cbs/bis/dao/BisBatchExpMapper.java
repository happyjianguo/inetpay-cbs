package com.ylink.inetpay.cbs.bis.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

import com.ylink.inetpay.common.core.constant.BISAuditStatus;
import com.ylink.inetpay.common.core.constant.EBatchBusiType;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisBatchExp;
@MybatisMapper("bisBatchExpMapper")
public interface BisBatchExpMapper {
    int deleteByPrimaryKey(String id);

    int insert(BisBatchExp record);

    int insertSelective(BisBatchExp record);

    BisBatchExp selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(BisBatchExp record);

    int updateByPrimaryKey(BisBatchExp record);
    
    List<BisBatchExp> list(BisBatchExp record);
    /**
     * 获取序列
     * @return
     */
	String getBatchNo();
	/**
	 * 修改成功笔数/失败笔数，如果成功笔数等于总笔数状态改为全部成功，如果失败笔数等于总笔数状态改为全部失败。
	 * @param successNum
	 * @param failNum
	 */
	long updateBatchExpNum(@Param("successNum")long successNum, @Param("failNum")long failNum,@Param("batchNo")String batchNo);
	/**
	 * 根据批次号，查询批次记录
	 * @param batchNo
	 * @return
	 */
	BisBatchExp selectByBatchNo(String batchNo);
	/**
	 * 根据批次号修改批次复核状态
	 * @param batchNo
	 * @param auditStatus
	 * @return
	 */
	long updateAuditStatusByBatchNo(@Param("batchNo")String batchNo, 
			@Param("checkStatus")BISAuditStatus auditStatus,
			@Param("auditor")String auditor,@Param("auditorName")String auditorName,
			@Param("checkTime")Date checkTime,@Param("checkReason")String checkReason);
	/**
	 * 获取指定类型是否有相同文件记录
	 * @param expFileName
	 * @param busiType
	 * @return
	 */
	long isExistFile(@Param("expFileName")String expFileName, @Param("busiType")EBatchBusiType busiType);
}