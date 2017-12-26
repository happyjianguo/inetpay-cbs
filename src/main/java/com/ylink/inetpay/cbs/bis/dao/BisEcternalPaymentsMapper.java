package com.ylink.inetpay.cbs.bis.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

import com.ylink.inetpay.common.core.constant.BISAuditStatus;
import com.ylink.inetpay.common.core.constant.PayStatusEnum;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisEcternalPayments;
@MybatisMapper("bisEcternalPaymentsMapper")
public interface BisEcternalPaymentsMapper {
    int insert(BisEcternalPayments record);

    int insertSelective(BisEcternalPayments record);

	void deleteEcternalPaymentsId(String id);


	List<BisEcternalPayments> list(BisEcternalPayments bisEcternalPayments);

	BisEcternalPayments selectByPrimaryKey(String id);

	void updateEcternalPaymentsById(BisEcternalPayments bisEcternalPayments);
	/**
	 * 根据订单号获取对外支付信息
	 * @param batch
	 * @return
	 */
	long findBeanByBustId(String busiId);
	/**
	 * 获取所有状态为"未知"的数据
	 * @param unknown
	 * @return
	 */
	List<BisEcternalPayments> getUnDownStatusDtos(PayStatusEnum unknown);
	/**
	 * 批量导入对外支付
	 * @param batchDtos
	 * @return
	 */
	long batchEcternalPayExp(@Param("batchDtos")List<BisEcternalPayments> batchDtos);
	/**
	 * 根据批次号修改批次明细复核状态
	 * @param batchNo
	 * @param auditStatus
	 * @return
	 */
	long updateAuditStatusByBatchNo(@Param("batchNo")String batchNo, @Param("checkStatus")BISAuditStatus auditStatus);
	/**
	 * 查询所有复核通过，待处理的记录
	 * @param unknown
	 * @param auditPass
	 * @return
	 */
	List<BisEcternalPayments> getAuditPassWaitPay(@Param("waitPay")PayStatusEnum waitPay, @Param("auditPass")BISAuditStatus auditPass);
	/**
	 * 根据批次号获取对外支付list
	 * @param batchNo
	 * @return
	 */
	List<BisEcternalPayments> findEcternalPaymentsListByBatchNo(String batchNo);
}