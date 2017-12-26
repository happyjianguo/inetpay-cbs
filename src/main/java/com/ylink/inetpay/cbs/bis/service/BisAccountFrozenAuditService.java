package com.ylink.inetpay.cbs.bis.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.common.core.constant.BISAuditStatus;
import com.ylink.inetpay.common.core.constant.EActInterestDealStatus;
import com.ylink.inetpay.common.core.dto.ResultCodeDto;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisAccountFrozenAuditDto;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisAccountFrozenOperDto;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisAccountUnfrozenAuditDto;
import com.ylink.inetpay.common.project.cbs.dto.ucs.sec.UcsSecUserDto;

public interface BisAccountFrozenAuditService {
	/**
	 * 获取冻结记录详情
	 * @param id
	 * @return
	 */
	public BisAccountFrozenAuditDto findById(String id);
	
	/**
	 * 冻结列表
	 * @param pageData
	 * @param queryParam
	 * @return
	 */
	public PageData<BisAccountFrozenAuditDto> listFrozen(PageData<BisAccountFrozenAuditDto> pageData,
			BisAccountFrozenAuditDto queryParam);
	
	/**
	 * 生成冻结记录
	 * @
	 */
	public void save(BisAccountFrozenAuditDto bisAccountFrozenAuditDto,UcsSecUserDto user);
	
	/**
	 * 更新操作
	 * @param bisAccountFrozenAuditDto
	 */
	public void updateSelective(BisAccountFrozenAuditDto bisAccountFrozenAuditDto);
	
	/**
	 * 审核拒绝
	 * @param auditor
	 * @param auditorName
	 * @param id
	 */
	public ResultCodeDto<BisAccountFrozenAuditDto> frozenAuditPass(String auditor,String auditorName,String id,String remark);
	
	/**
	 * 批量审核通过
	 * @param auditor
	 * @param auditorName
	 * @param id
	 * @
	 */
	public ResultCodeDto<BisAccountFrozenAuditDto> frozenBatchAuditPass(String auditor,String auditorName,List<String> idList,String remark);
	
	/**
	 * 审核通过
	 * @param auditor
	 * @param auditorName
	 * @param id
	 */
	public void frozenAuditReject(String auditor,String auditorName,String id,String reason);
	
	/**
	 * 批量审核拒绝
	 * @param auditor
	 * @param auditorName
	 * @param id
	 * @
	 */
	public void frozenBatchAuditReject(String auditor,String auditorName,List<String> idList,String reason);
	
	/**
	 * 撤销
	 * @param id
	 * @
	 */
	public void frozenCancel(String auditor, String auditorName,String id,String reason);

	/**
	 * 审核通过
	 * @param auditor
	 * @param auditorName
	 * @param id
	 */
	public void unfreezeAuditReject(String auditor,String auditorName,String id,String reason);
	
	/**
	 * 批量审核拒绝
	 * @param auditor
	 * @param auditorName
	 * @param id
	 * @
	 */
	public void unfreezeBatchAuditReject(String auditor,String auditorName,List<String> idList,String reason);
	
	/**
	 * 解冻列表
	 * @param pageData	
	 * @param queryParam
	 * @return
	 */
	public PageData<BisAccountFrozenAuditDto> listUnfreeze(PageData<BisAccountFrozenAuditDto> pageData,
			BisAccountFrozenAuditDto queryParam);
	/**
	 * 获取冻结/解冻审核通过 冻结申请
	 * @param ids
	 * @param auditPass
	 * @param frozenDoing
	 * @param frozen
	 * @return
	 */
	public long getAuditNum(List<String> ids,boolean isFrozen);
	/**
	 * 冻结未知状态查询
	 */
	public void autoFrozenQueryUnDownStatus();
	/**
	 * 解冻未知状态查询
	 */
	public void autoUnFrozenQueryUnDownStatus();
	/**
	 * 根据批次号修改审核状态
	 * @param auditStatus
	 * @param batchNo
	 * @return
	 */
	public long updateBatchAuditStatus(BISAuditStatus auditStatus, String batchNo);
	/**
	 * 根据批次号获取批次明细
	 * @param batchNo
	 * @return
	 */
	public List<BisAccountFrozenAuditDto> findListByBatchNo(String batchNo);
	/**
	 * 修改冻结记录的复核状态
	 * @param dealing
	 * @param waitDeal
	 * @param id
	 * @return
	 */
	public long updateDealStatus(EActInterestDealStatus dealing, EActInterestDealStatus waitDeal, String id);
	/**
	 * 执行批量资金冻结审核
	 * @param dto
	 */
	public void batchExpAudit(BisAccountFrozenAuditDto dto);
	/**
	 * 业务人员在页面选择多个记录进行解冻，默认解冻时间当前账务日期，默认将剩余未解冻的记录全部解冻。
	 * @param ids
	 * @param loginName
	 * @param realName
	 * @param reason
	 * @return
	 */
	long batchUnFrozen(List<String> ids, String loginName, String realName, String reason);
	/**
	 * 部分解冻
	 * @param id
	 * @param loginName
	 * @param realName
	 * @param reason
	 * @param unFrozenAmt
	 * @return
	 */
	long partUnFrozen(String id, String loginName, String realName, String reason, long unFrozenAmt, Date unFrozenDate);
	/**
	 * 批量冻结撤销
	 * @param idList
	 * @param reason
	 * @param loginName
	 * @param realName
	 */
	void frozenBatchCancel(List<String> idList, String reason, String loginName, String realName);
	/**
	 * 批量解冻复核通过（非导入）
	 * @param auditor
	 * @param auditorName
	 * @param idList
	 * @param reason
	 * @return
	 */
	ResultCodeDto<BisAccountFrozenAuditDto> unfreezeBatchAuditPass(String auditor, String auditorName,
			List<String> idList, String reason);
	/**
	 * 解冻复核通过（非导入）
	 * @param auditor
	 * @param auditorName
	 * @param id
	 * @param reason
	 * @return
	 */
	ResultCodeDto<BisAccountFrozenAuditDto> unfreezeAuditPass(String auditor, String auditorName, String id,
			String reason);
	/**
	 * 批量解冻复核撤销
	 * @param idList
	 * @param reason
	 * @param loginName
	 * @param realName
	 */
	void unfreezeBatchCancel(List<String> idList, String reason, String loginName, String realName);
	/**
	 * 解冻复核撤销
	 * @param id
	 * @param reason
	 * @param loginName
	 * @param realName
	 */
	void unfreezeCancel(String id, String reason, String loginName, String realName);
	/**
	 * 获取审核通过"冻结中或者冻结申请"的记录
	 * @param parse
	 * @return
	 */
	public List<BisAccountFrozenAuditDto> listFrozenWaitOrDing(Date parse);
	/**
	 * 根据冻结订单冻结
	 * @param item
	 */
	public void frozenAccount(BisAccountFrozenAuditDto item);
	/**
	 * 解冻账户
	 * @param item
	 */
	public void unFrozenAccount(BisAccountFrozenAuditDto item);
	/**
	 * 获取复核通过到了冻结截止日期的记录（不论是否已冻结完成）
	 * @param parse
	 * @return
	 */
	public List<BisAccountFrozenAuditDto> listUnFrozenByEndTime(Date parse);

	/**
	 *  解冻订单列表
	 */
	PageData<BisAccountFrozenAuditDto> listUnFrozen(PageData<BisAccountFrozenAuditDto> pageData,
			BisAccountFrozenAuditDto queryParam);
	/**
	 * 审核通过达到冻结日期且待处理的记录，执行冻结（定时任务调用）
	 * @param unfrozenDto
	 */
	public void doUnFrozenAccount(BisAccountUnfrozenAuditDto unfrozenDto);
	/**
	 * 到截至日期 还是待处理的记录改为到期自动解冻，执行解冻操作
	 * @param dto
	 */
	public void runOutWaitAuditToAutoFrozen(BisAccountUnfrozenAuditDto dto);
	/**
	 * 查询冻结复核记录的状态
	 * @param id
	 * @return
	 */
	public ResultCodeDto<BisAccountFrozenAuditDto> redoQueryFrozenStatus(String id);
	/**
	 * 查询解冻复核记录的状态
	 * @param id
	 * @return
	 */
	public ResultCodeDto<BisAccountFrozenAuditDto> redoQueryUnfreezeStatus(String id);
	/**
	 * 根据批次号获取冻结map
	 * @param batchNo
	 * @return
	 */
	public Map<String, BisAccountFrozenAuditDto> findActFrozenMapByBatchNo(String batchNo);
	/**
	 * 根据查询条件获取资金冻结列表
	 * @param queryParam
	 * @return
	 */
	public List<BisAccountFrozenAuditDto> list(BisAccountFrozenAuditDto queryParam);
	/**
	 * 根据id实际资金冻结操作记录
	 * @param id
	 * @return
	 */
	public List<BisAccountFrozenOperDto> getOperList(String id);
	/**
	 * 批量资金冻结
	 * @param accountIds
	 * @param actDto
	 * @param currentUser
	 */
	public void batchActFrozen(List<String> accountIds, BisAccountFrozenAuditDto actDto, UcsSecUserDto currentUser);
	/**
	 * 直接将复合通过，但是实际冻结金额为0并且到达冻结截止日期的记录修改为已解冻
	 * @param actDate
	 * @return
	 */
	public long autoUpdateUnFrozenByActDate(Date actDate);
	/**
	 * 修改批次导入成功笔数
	 * @param frozenDto
	 * @param successNum
	 * @param failNum
	 */
	public void updateBatchExpNum(BisAccountFrozenAuditDto frozenDto, long successNum, long failNum);
}
