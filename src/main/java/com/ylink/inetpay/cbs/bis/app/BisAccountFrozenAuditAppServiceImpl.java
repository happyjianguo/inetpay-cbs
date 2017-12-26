package com.ylink.inetpay.cbs.bis.app;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.cbs.bis.service.BisAccountFrozenAuditService;
import com.ylink.inetpay.cbs.bis.service.BisAccountUnfrozenAuditService;
import com.ylink.inetpay.cbs.bis.service.BisExceptionLogService;
import com.ylink.inetpay.cbs.util.CbsExceptionLogDtoUtils;
import com.ylink.inetpay.common.core.constant.EBisExceptionLogNlevel;
import com.ylink.inetpay.common.core.constant.EBisExceptionLogType;
import com.ylink.inetpay.common.core.dto.ResultCodeDto;
import com.ylink.inetpay.common.core.util.DateUtils;
import com.ylink.inetpay.common.core.util.ExceptionProcUtil;
import com.ylink.inetpay.common.project.account.app.ActAccountDateAppService;
import com.ylink.inetpay.common.project.cbs.app.BisAccountFrozenAuditAppService;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisAccountFrozenAuditDto;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisAccountFrozenOperDto;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisAccountUnfrozenAuditDto;
import com.ylink.inetpay.common.project.cbs.dto.ucs.sec.UcsSecUserDto;
import com.ylink.inetpay.common.project.cbs.exception.CbsCheckedException;

@Service("bisAccountFrozenAuditAppService")
public class BisAccountFrozenAuditAppServiceImpl implements BisAccountFrozenAuditAppService {
	
	@Autowired
	BisAccountFrozenAuditService bisAccountFrozenAuditService; 
	@Autowired
	private ActAccountDateAppService actAccountDateAppService;
	@Autowired
	private BisExceptionLogService bisExceptionLogService;
	@Autowired
	private BisAccountUnfrozenAuditService bisAccountUnfrozenAuditService;
	@Autowired
	@Qualifier("taskExecutor")
	TaskExecutor taskExecutor;
	private Logger _log = LoggerFactory.getLogger(BisAccountFrozenAuditAppServiceImpl.class);
	@Override
	public BisAccountFrozenAuditDto findById(String id) throws CbsCheckedException {
		return bisAccountFrozenAuditService.findById(id);
	}
	
	@Override
	public PageData<BisAccountFrozenAuditDto> listFrozen(PageData<BisAccountFrozenAuditDto> pageData,
			BisAccountFrozenAuditDto queryParam) throws CbsCheckedException {
		return bisAccountFrozenAuditService.listFrozen(pageData, queryParam);
	}

	@Override
	public void save(BisAccountFrozenAuditDto bisAccountFrozenAuditDto,UcsSecUserDto user) throws CbsCheckedException {
		bisAccountFrozenAuditService.save(bisAccountFrozenAuditDto,user);
	}

	@Override
	public void updateSelective(BisAccountFrozenAuditDto bisAccountFrozenAuditDto) throws CbsCheckedException {
		bisAccountFrozenAuditService.updateSelective(bisAccountFrozenAuditDto);
	}

	@Override
	public ResultCodeDto<BisAccountFrozenAuditDto> frozenAuditPass(String auditor, String auditorName, String id,String reason) throws CbsCheckedException {
		//审核通过状态和冻结是一个事务，不需要考虑定时任务和人工同时执行的情况
		return bisAccountFrozenAuditService.frozenAuditPass(auditor, auditorName, id,reason);
	}

	@Override
	public ResultCodeDto<BisAccountFrozenAuditDto> frozenBatchAuditPass(String auditor, String auditorName, List<String> idList,String reason)
			throws CbsCheckedException {
		return bisAccountFrozenAuditService.frozenBatchAuditPass(auditor, auditorName, idList,reason);
	}

	@Override
	public void frozenAuditReject(String auditor, String auditorName, String id,String reason) throws CbsCheckedException {
		bisAccountFrozenAuditService.frozenAuditReject(auditor, auditorName, id,reason);
	}

	@Override
	public void frozenBatchAuditReject(String auditor, String auditorName, List<String> idList,String reason)
			throws CbsCheckedException {
		bisAccountFrozenAuditService.frozenBatchAuditReject(auditor, auditorName, idList,reason);
	}

	@Override
	public void frozenCancel(String auditor, String auditorName,String id,String reason) throws CbsCheckedException {
		bisAccountFrozenAuditService.frozenCancel(auditor,auditorName,id,reason);
	}

	@Override
	public void frozenBatchCancel(List<String> idList,String reason, String loginName, String realName) throws CbsCheckedException {
		bisAccountFrozenAuditService.frozenBatchCancel(idList,reason,loginName,realName);
	}

	@Override
	public ResultCodeDto<BisAccountFrozenAuditDto> unfreezeAuditPass(String auditor, String auditorName, String id,String reason) throws CbsCheckedException {
		return bisAccountFrozenAuditService.unfreezeAuditPass(auditor, auditorName, id,reason);
	}

	@Override
	public ResultCodeDto<BisAccountFrozenAuditDto> unfreezeBatchAuditPass(String auditor, String auditorName, List<String> idList,String reason)
			throws CbsCheckedException {
		return bisAccountFrozenAuditService.unfreezeBatchAuditPass(auditor, auditorName, idList,reason);
	}

	@Override
	public void unfreezeAuditReject(String auditor, String auditorName, String id,String reason) throws CbsCheckedException {
		bisAccountFrozenAuditService.unfreezeAuditReject(auditor, auditorName, id,reason);
	}

	@Override
	public void unfreezeBatchAuditReject(String auditor, String auditorName, List<String> idList,String reason)
			throws CbsCheckedException {
		bisAccountFrozenAuditService.unfreezeBatchAuditReject(auditor, auditorName, idList,reason);
	}

	@Override
	public void unfreezeCancel(String id,String reason,String loginName, String realName) throws CbsCheckedException {
		bisAccountFrozenAuditService.unfreezeCancel(id,reason,loginName,realName);
	}

	@Override
	public void unfreezeBatchCancel(List<String> idList,String reason, String loginName, String realName) throws CbsCheckedException {
		bisAccountFrozenAuditService.unfreezeBatchCancel(idList,reason,loginName,realName);
	}

	@Override
	public PageData<BisAccountFrozenAuditDto> listUnfreeze(PageData<BisAccountFrozenAuditDto> pageData,
			BisAccountFrozenAuditDto queryParam) throws CbsCheckedException {
		return bisAccountFrozenAuditService.listUnfreeze(pageData, queryParam);
	}

	/**
	 * 定时任务调用，资金冻结
	 */
	@Override
	public void taskFrozeAccount() throws CbsCheckedException {
		taskExecutor.execute(new Runnable() {
			@Override
			public void run() {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				try {
					//获取审核通过"冻结中，冻结申请"的记录
					Date accountDate = DateUtils.changeToDate(actAccountDateAppService.getAccountDate());
					List<BisAccountFrozenAuditDto> items = bisAccountFrozenAuditService.listFrozenWaitOrDing(sdf.parse(sdf.format(accountDate)));
					if(items != null && !items.isEmpty()){
						for(BisAccountFrozenAuditDto item : items){
							bisAccountFrozenAuditService.frozenAccount(item);
						}
					}
				} catch (ParseException e) {
					_log.error("系统报错，报错原因：{}",ExceptionProcUtil.getExceptionDesc(e));
					bisExceptionLogService.saveLog(CbsExceptionLogDtoUtils.getBisExceptionLog(EBisExceptionLogNlevel.ERROR, EBisExceptionLogType.FROZEN,"自动冻结失败"));
				}	
			}
		});
	}
	/**
	 * 定时任务调用，资金解冻
	 */
	@Override
	public void taskUnfreezeAccount() throws CbsCheckedException {
		taskExecutor.execute(new Runnable() {
			@Override
			public void run() {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				try {
					
					Date accountDate = DateUtils.changeToDate(actAccountDateAppService.getAccountDate());
					
					Date actDate = sdf.parse(sdf.format(accountDate));
					//将复合通过，截止日期小于等于当前账务日期，冻结申请并且实际冻结金额等于0的直接修改为已解冻
					bisAccountFrozenAuditService.autoUpdateUnFrozenByActDate(actDate);
					//获取审核通过"到达截止日期"的冻结记录，根据解冻金额和冻结金额生成任务并执行解冻操作
					List<BisAccountFrozenAuditDto> items = bisAccountFrozenAuditService.listUnFrozenByEndTime(actDate);
					if(items != null && !items.isEmpty()){
						for(BisAccountFrozenAuditDto item : items){
							bisAccountFrozenAuditService.unFrozenAccount(item);
						}
					}
					//获取所有复核通过，待处理，解冻日期小于等于当前账务日期的记录
					List<BisAccountUnfrozenAuditDto> unfrozenDtos = bisAccountUnfrozenAuditService.listUnFrozenByEndTime(actDate);
					if(unfrozenDtos!=null && !unfrozenDtos.isEmpty()){
						for (BisAccountUnfrozenAuditDto unfrozenDto : unfrozenDtos) {
							bisAccountFrozenAuditService.doUnFrozenAccount(unfrozenDto);
						}
					}
					//如果复核通过存在待复核的解冻记录，修改状态为"到期自动解冻".
					unfrozenDtos=bisAccountUnfrozenAuditService.runOutWaitAuditByEndTime(actDate);
					if(unfrozenDtos!=null && !unfrozenDtos.isEmpty()){
						for (BisAccountUnfrozenAuditDto dto : unfrozenDtos) {
							bisAccountFrozenAuditService.runOutWaitAuditToAutoFrozen(dto);
						}
					}
					
					
				} catch (ParseException e) {
					_log.error("系统报错，报错原因：{}",ExceptionProcUtil.getExceptionDesc(e));
					bisExceptionLogService.saveLog(CbsExceptionLogDtoUtils.getBisExceptionLog(EBisExceptionLogNlevel.ERROR, EBisExceptionLogType.UNFROZEN,"自动冻结失败"));
				}	
			}
		});
	}

	@Override
	public long getAuditNum(List<String> ids,boolean isFrozen) {
		return bisAccountFrozenAuditService.getAuditNum(ids,isFrozen);
	}
	/**
	 * 部分解冻
	 */
	@Override
	public void partUnFrozen(String id, String loginName, String realName, String reason, long unFrozenAmt,
			Date unFrozenDate) {
		bisAccountFrozenAuditService.partUnFrozen(id, loginName, realName, reason, unFrozenAmt, unFrozenDate);
	}
	/**
	 * 批量解冻
	 */
	@Override
	public void batchUnFrozen(List<String> ids, String loginName, String realName, String reason) {
		bisAccountFrozenAuditService.batchUnFrozen(ids, loginName, realName, reason);
	}
	
	/**
	 * 查询解冻复核列表
	 */
	@Override
	public PageData<BisAccountUnfrozenAuditDto> listUnFrozenList(PageData<BisAccountUnfrozenAuditDto> pageData,
			BisAccountUnfrozenAuditDto queryParam) {
		return bisAccountUnfrozenAuditService.listUnFrozenList(pageData, queryParam);
	}
	
	/**
	 * 查询解冻复核记录详情
	 */
	@Override
	public BisAccountUnfrozenAuditDto findUnfrozenViewById(String id) {
		return bisAccountUnfrozenAuditService.findUnfrozenViewById(id);
	}
	/**
	 * 查询冻结复核记录状态
	 */
	@Override
	public ResultCodeDto<BisAccountFrozenAuditDto> redoQueryFrozenStatus(String id) {
		return bisAccountFrozenAuditService.redoQueryFrozenStatus(id);
	}
	
	/**
	 * 查询解冻复核记录状态
	 */
	@Override
	public ResultCodeDto<BisAccountFrozenAuditDto> redoQueryUnfreezeStatus(String id) {
		return bisAccountFrozenAuditService.redoQueryUnfreezeStatus(id);
	}
	
	/**
	 * 根据批次号获取资金冻结map
	 */
	@Override
	public Map<String, BisAccountFrozenAuditDto> findActFrozenMapByBatchNo(String batchNo) {
		return bisAccountFrozenAuditService.findActFrozenMapByBatchNo(batchNo);
	}

	@Override
	public List<BisAccountFrozenAuditDto> list(BisAccountFrozenAuditDto queryParam) {
		return bisAccountFrozenAuditService.list(queryParam);
	}

	@Override
	public List<BisAccountFrozenOperDto> getOperList(String id) {
		return bisAccountFrozenAuditService.getOperList(id);
	}

	@Override
	public void batchActFrozen(List<String> accountIds, BisAccountFrozenAuditDto actDto, UcsSecUserDto currentUser) throws CbsCheckedException{
		bisAccountFrozenAuditService.batchActFrozen(accountIds,actDto,currentUser);
	}
}
