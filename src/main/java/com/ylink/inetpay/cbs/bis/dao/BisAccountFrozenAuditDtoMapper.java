package com.ylink.inetpay.cbs.bis.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

import com.ylink.inetpay.common.core.constant.BISAuditStatus;
import com.ylink.inetpay.common.core.constant.EActInterestDealStatus;
import com.ylink.inetpay.common.core.constant.EFrozenStatus;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisAccountFrozenAuditDto;
@MybatisMapper("bisAccountFrozenAuditDtoMapper")
public interface BisAccountFrozenAuditDtoMapper {
    int deleteByPrimaryKey(String id);

    int insert(BisAccountFrozenAuditDto record);

    int insertSelective(BisAccountFrozenAuditDto record);

    BisAccountFrozenAuditDto selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(BisAccountFrozenAuditDto record);

    int updateByPrimaryKey(BisAccountFrozenAuditDto record);

	//long updateActualAmt(@Param("setAmt")long setAmt, @Param("id")String id, @Param("frozenStatus")EFrozenStatus frozenStatus);
	/**
	 * 获取冻结序号
	 * @return
	 */
	long getSequence();
	/**
	 * 
	 * @param newStatus
	 * @param oldStatus
	 * @param id
	 * @return
	 */
	long updateDealStatus(@Param("newStatus")EActInterestDealStatus newStatus, @Param("oldStatus")EActInterestDealStatus oldStatus, @Param("id")String id);
	/**
	 * 根据批次号获取批次列表
	 * @param batchNo
	 * @return
	 */
	List<BisAccountFrozenAuditDto> findListByBatchNo(String batchNo);
	/**
	 * 根据批次号修改批次明细的审核状态
	 * @param auditStatus
	 * @param batchNo
	 * @return
	 */
	long updateBatchAuditStatus(@Param("auditStatus")BISAuditStatus auditStatus, @Param("batchNo")String batchNo);
	/**
	 * 分页查询冻结复核列表
	 * @param queryParam
	 * @return
	 */
	List<BisAccountFrozenAuditDto> listFrozen(BisAccountFrozenAuditDto queryParam);
	/**
	 * 批量保存导入数据
	 * @param batchDtos
	 * @return
	 */
	long batchAccountFrozenExp(@Param("batchDtos")List<BisAccountFrozenAuditDto> batchDtos);
	/**
	 * 获取解冻列表
	 * @param queryParam
	 * @return
	 */
	List<BisAccountFrozenAuditDto> listUnfreezeAudit(BisAccountFrozenAuditDto queryParam);
	/**
	 * 根据id集合查询冻结列表
	 * @param ids
	 * @return
	 */
	List<BisAccountFrozenAuditDto> queryFrozenListByIds(@Param("ids")List<String> ids);
	/**
	 * 修改实际解冻金额 解冻金额
	 * @param setAmt
	 * @param id
	 * @return
	 */
	long updateActualUnFrozenAmt(@Param("setAmt")long setAmt, @Param("id")String id,@Param("frozenStatus")EFrozenStatus frozenStatus,@Param("actualFrozenAmt")long actualFrozenAmt);
	/**
	 * 修改解冻金额汇总
	 * @param setAmt
	 * @param id
	 * @return
	 */
	long updateUnFrozenAmt(@Param("setAmt")long setAmt, @Param("id")String id,@Param("frozenStatus")EFrozenStatus frozenStatus);
	/**
	 * 获取复核通过"冻结申请或者冻结中"的冻结记录
	 */
	List<BisAccountFrozenAuditDto> listFrozenWaitOrDing(Date actDate);
	/**
	 * 获取复核通过，但是到了冻结截至日期数据
	 */
	List<BisAccountFrozenAuditDto> listUnFrozenByEndTime(Date actDate);
	/**
	 * 获取复核通过，冻结申请的记录数
	 * @param ids
	 * @param auditPass
	 * @param frozenDoing
	 * @return
	 */
	long getAuditNum(@Param("ids")List<String> ids, @Param("auditPass")BISAuditStatus auditPass, 
			@Param("frozenApply")EFrozenStatus frozenApply);
	/**
	 * 根据批次号修改批次明细复核状态
	 * @param batchNo
	 * @param auditStatus
	 * @return
	 */
	long updateAuditStatusByBatchNo(@Param("batchNo")String batchNo, @Param("auditStatus")BISAuditStatus auditStatus);
	/**
	 * 根据批次号获取冻结list
	 */
	List<BisAccountFrozenAuditDto> findActFrozenListByBatchNo(String batchNo);
	/**
	 * 直接将复合通过，但是实际冻结金额为0并且到达冻结截止日期的记录修改为已解冻
	 * @param actDate
	 * @return
	 */
	long autoUpdateUnFrozenByActDate(@Param("actDate")Date actDate);
	/**
	 * 获取 实际冻结金额为0并且到达截至日期的记录
	 * @return
	 */
	List<BisAccountFrozenAuditDto> findListByUnFrozenByActDate(@Param("actDate")Date actDate);
	/**
	 * 修改冻结状态
	 * @param unfreezeSuccess
	 * @param id
	 * @return
	 */
	long updateFrozenStatus(@Param("frozenStatus")EFrozenStatus frozenStatus, @Param("id")String id);
}