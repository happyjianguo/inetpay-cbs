package com.ylink.inetpay.cbs.bis.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

import com.ylink.inetpay.common.core.constant.BISAuditStatus;
import com.ylink.inetpay.common.core.constant.EActCustRateStatus;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisActCustRateAuditDto;

@MybatisMapper("bisActCustRateAuditDtoMapper")
public interface BisActCustRateAuditDtoMapper {
    int deleteByPrimaryKey(String id);

    int insert(BisActCustRateAuditDto record);

    int insertSelective(BisActCustRateAuditDto record);

    BisActCustRateAuditDto selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(BisActCustRateAuditDto record);

    int updateByPrimaryKeyWithBLOBs(BisActCustRateAuditDto record);

    int updateByPrimaryKey(BisActCustRateAuditDto record);
    
    List<BisActCustRateAuditDto> list(BisActCustRateAuditDto queryParam);
    
    long countWaitAuditWithAccountId(String accountId);
    
    long countWaitAuditWithBankCardNo(String bankCardNo);
    
    List<BisActCustRateAuditDto> findWithAccountIds(
    		@Param("auditStatus")BISAuditStatus auditStatus,@Param("accountIds")List<String> accountIds,
    		@Param("auditPass")BISAuditStatus auditPass, @Param("uneffective")EActCustRateStatus uneffective);

    long saveBatch(@Param("acraList")List<BisActCustRateAuditDto> acraList);
    
    List<BisActCustRateAuditDto> findPassAndValidTime(Date date);
    /**
     * 获取审核记录数
     * @param ids
     * @param auditPass
     * @param uneffective
     * @return
     */
	long getAuditNum(@Param("ids")List<String> ids, @Param("auditPass")BISAuditStatus auditPass, @Param("uneffective")EActCustRateStatus uneffective);
	/**
	 * 批量保存客户利率
	 * @param batchDtos
	 * @return
	 */
	long batchCustRateExp(@Param("batchDtos")List<BisActCustRateAuditDto> batchDtos);
	/**
	 * 根据批次号修改批次明细复核状态
	 * @param batchNo
	 * @param auditStatus
	 * @return
	 */
	long updateAuditStatusByBatchNo(@Param("batchNo")String batchNo, @Param("auditStatus")BISAuditStatus auditStatus);
	/**
	 * 根据批次号获取客户利率list
	 * @param batchNo
	 * @return
	 */
	List<BisActCustRateAuditDto> findCustRateListByBatchNo(String batchNo);
	/**
	 * 获取复核通过或者复核成功未生效的数据
	 * @param accountIds
	 * @return
	 */
	List<BisActCustRateAuditDto> findWaitAudit(@Param("accountIds")List<String> accountIds);
	/**
	 * 获取复核通过或者复核成功未生效的数据
	 * @param accountIds
	 * @return
	 */
	List<BisActCustRateAuditDto> findWaitAuditByBankAccNos(@Param("bankAccNos")List<String> bankAccNos);
	/**
	 * 根据id修改生效状态
	 * @param id
	 * @param uneffective
	 * @param effective
	 * @return
	 */
	long updateStatusById(@Param("id")String id, @Param("oldStatus")EActCustRateStatus oldStatus, @Param("newStatus")EActCustRateStatus newStatus);
    
}