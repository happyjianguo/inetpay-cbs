package com.ylink.inetpay.cbs.bis.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.ylinkpay.framework.core.model.PageData;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.StringUtil;
import com.ylink.inetpay.cbs.act.service.ActAccountService;
import com.ylink.inetpay.cbs.bis.dao.BisAccountFrozenAuditDtoMapper;
import com.ylink.inetpay.cbs.bis.dao.BisAccountFrozenOperDtoMapper;
import com.ylink.inetpay.cbs.bis.dao.BisAccountUnfrozenAuditDtoMapper;
import com.ylink.inetpay.cbs.common.CbsConstants;
import com.ylink.inetpay.cbs.util.CbsExceptionLogDtoUtils;
import com.ylink.inetpay.common.core.constant.BISAuditStatus;
import com.ylink.inetpay.common.core.constant.BISAuditType;
import com.ylink.inetpay.common.core.constant.EActInterestDealStatus;
import com.ylink.inetpay.common.core.constant.EAppleType;
import com.ylink.inetpay.common.core.constant.EBisExceptionLogNlevel;
import com.ylink.inetpay.common.core.constant.EBisExceptionLogType;
import com.ylink.inetpay.common.core.constant.EFrozenStatus;
import com.ylink.inetpay.common.core.constant.EPayAdjustType;
import com.ylink.inetpay.common.core.constant.EResultCode;
import com.ylink.inetpay.common.core.constant.EUnfreezeType;
import com.ylink.inetpay.common.core.constant.PayStatusEnum;
import com.ylink.inetpay.common.core.dto.ResultCodeDto;
import com.ylink.inetpay.common.core.util.DateUtils;
import com.ylink.inetpay.common.project.account.app.ActAccountDateAppService;
import com.ylink.inetpay.common.project.account.dto.ActAccountDto;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisAccountFrozenAuditDto;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisAccountFrozenOperDto;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisAccountUnfrozenAuditDto;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisAuditDto;
import com.ylink.inetpay.common.project.cbs.dto.ucs.sec.UcsSecUserDto;
import com.ylink.inetpay.common.project.cbs.exception.CbsCheckedException;
import com.ylink.inetpay.common.project.cbs.exception.CbsUncheckedException;
import com.ylink.inetpay.common.project.pay.app.PayAccountAdjustAppService;
import com.ylink.inetpay.common.project.pay.dto.PayAccountAdjustDto;

@Service("bisAccountFrozenAuditService")
@Transactional(value=CbsConstants.TX_MANAGER_BIS)
public class BisAccountFrozenAuditServiceImpl implements BisAccountFrozenAuditService {
	
	private static Logger _log = LoggerFactory.getLogger(BisAccountFrozenAuditServiceImpl.class);

	@Autowired
	BisAccountFrozenAuditDtoMapper bisAccountFrozenAuditDtoMapper;
	@Autowired
	BisAuditService bisAuditService;
	@Autowired
	PayAccountAdjustAppService accountAdjustAppService;
	@Autowired
	ActAccountDateAppService actAccountDateAppService;
	@Autowired
	BisExceptionLogService bisExceptionLogService;
	@Autowired
	ActAccountService actAccountService;
	@Autowired
	private BisAccountFrozenOperDtoMapper bisAccountFrozenOperDtoMapper;
	@Autowired
	private BisAccountUnfrozenAuditDtoMapper bisAccountUnfrozenAuditDtoMapper;
	@Autowired
	private BisBatchExpService bisBatchExpService;
	/**
	 * 分页获取冻结列表
	 */
	@Override
	public PageData<BisAccountFrozenAuditDto> listFrozen(PageData<BisAccountFrozenAuditDto> pageData,BisAccountFrozenAuditDto queryParam){
		PageHelper.startPage(pageData.getPageNumber(), pageData.getPageSize());
		List<BisAccountFrozenAuditDto> items = bisAccountFrozenAuditDtoMapper.listFrozen(queryParam);
		Page<BisAccountFrozenAuditDto> page = (Page<BisAccountFrozenAuditDto> )items;
		pageData.setRows(items);
		pageData.setTotal(page.getTotal());
		return pageData;
	}
	
	/**
	 * 生成冻结复核记录(备注在外面传入)
	 */
	@Override
	public void save(BisAccountFrozenAuditDto bisAccountFrozenAuditDto,UcsSecUserDto user) {
		//添加冻结账户编号
		ActAccountDto frozenAccount=actAccountService.findAccountDtoByAccountId(bisAccountFrozenAuditDto.getFrozenAccountId());
		if(frozenAccount==null){
			throw new CbsUncheckedException("","账户【"+bisAccountFrozenAuditDto.getFrozenAccountId()+"】不存在冻结备付金账户");
		}
		bisAccountFrozenAuditDtoMapper.insert(getFrozenDto(frozenAccount, bisAccountFrozenAuditDto, user));
	}
	
	/**
	 * 封装资金冻结复核记录
	 * @param frozenAccount
	 * @param bisAccountFrozenAuditDto
	 * @param user
	 */
	public BisAccountFrozenAuditDto getFrozenDto(ActAccountDto frozenAccount,BisAccountFrozenAuditDto bisAccountFrozenAuditDto,UcsSecUserDto user){
		BisAccountFrozenAuditDto newAccountFrozenDto = new BisAccountFrozenAuditDto();
		newAccountFrozenDto.setFrozenAccountId(frozenAccount.getAccountId());
		newAccountFrozenDto.setFrozenAccountName(frozenAccount.getCustName());
		newAccountFrozenDto.setCustId(frozenAccount.getCustId());
		newAccountFrozenDto.setCustName(frozenAccount.getCustName());
		newAccountFrozenDto.setSubjectName(frozenAccount.getSubjectNo2Name());
		newAccountFrozenDto.setAppleType(EAppleType.HAND_MADE);
		newAccountFrozenDto.setFrozenApplyTime(new Date());
		newAccountFrozenDto.setFrozenOperator(user.getLoginName());
		newAccountFrozenDto.setFrozenOperatorName(user.getRealName());
		newAccountFrozenDto.setFrozenStatus(EFrozenStatus.FROZEN_APPLY);
		newAccountFrozenDto.setFrozenAuditStatus(BISAuditStatus.WAIT_AUDIT);
		newAccountFrozenDto.setActualFrozenAmt(0L);
		newAccountFrozenDto.setUnfrozenAmt(0L);
		newAccountFrozenDto.setActualUnfrozenAmt(0L);
		
		newAccountFrozenDto.setAmount(bisAccountFrozenAuditDto.getAmount());
		newAccountFrozenDto.setFrozenDays(bisAccountFrozenAuditDto.getFrozenDays());
		newAccountFrozenDto.setFrozenReason(bisAccountFrozenAuditDto.getFrozenReason());
		newAccountFrozenDto.setFrozenValidTime(bisAccountFrozenAuditDto.getFrozenValidTime());
		newAccountFrozenDto.setFrozenEndTime(bisAccountFrozenAuditDto.getFrozenEndTime());
		long seq = bisAccountFrozenAuditDtoMapper.getSequence();
		//冻结编号由FROZEN+yyyyMMdd+序列号
		String id = new SimpleDateFormat("yyyyMMdd").format(new Date())+String.valueOf(seq);
		newAccountFrozenDto.setId(id);
		return newAccountFrozenDto;
	}
	
	@Override
	public void batchActFrozen(List<String> accountIds, BisAccountFrozenAuditDto actFrozenDto, UcsSecUserDto currentUser) {
		List<ActAccountDto> accountDtos = actAccountService.findListByAccountIds(accountIds);
		if(accountDtos!=null && !accountIds.isEmpty()){
			if(accountDtos.size()!=accountIds.size()){
				throw new CbsUncheckedException("","批量资金冻结记录数【"+accountIds.isEmpty()+"】和系统冻结资金账户记录数【"+accountDtos.size()+"】不一致");
			}
			ArrayList<BisAccountFrozenAuditDto> accountFrozenDtos = new ArrayList<BisAccountFrozenAuditDto>();
			for (ActAccountDto actDto : accountDtos) {
				accountFrozenDtos.add(getFrozenDto(actDto, actFrozenDto, currentUser));
			}
			bisAccountFrozenAuditDtoMapper.batchAccountFrozenExp(accountFrozenDtos);
		}else{
			throw new CbsUncheckedException("","系统中不存在此批冻结账户");
		}
	}
	
	/**
	 * 批量冻结复核通过
	 */
	@Override
	public ResultCodeDto<BisAccountFrozenAuditDto> frozenBatchAuditPass(String auditor, String auditorName, List<String> idList,String remark) {
		if(idList == null || idList.isEmpty())return new ResultCodeDto<BisAccountFrozenAuditDto>(EResultCode.FAIL,"冻结记录为空");
		int failNum = 0;
		int unknownNum = 0;
		int successNum=0;
		for(String id : idList){
			ResultCodeDto<BisAccountFrozenAuditDto> rnt = frozenAuditPass(auditor, auditorName, id,remark);
			if(rnt.getResultCode().equals(EResultCode.FAIL)){
				failNum++;
			} else if(rnt.getResultCode().equals(EResultCode.SUCCESS)){
				successNum++;
			} else {
				unknownNum++;
			}
		}
		if(failNum > 0 || unknownNum > 0){
			String msg = "资金冻结成功"+successNum+"条，失败"+failNum+"条，状态未知"+unknownNum+"条";
			return new ResultCodeDto<>(EResultCode.FAIL,msg);
		} else {
			return new ResultCodeDto<>(EResultCode.SUCCESS);
		}
	}
	
	/**
	 * 冻结审核
	 */
	@Override
	public ResultCodeDto<BisAccountFrozenAuditDto> frozenAuditPass(String auditor, String auditorName, String id,String remark) {
		BisAccountFrozenAuditDto item = bisAccountFrozenAuditDtoMapper.selectByPrimaryKey(id);
		ResultCodeDto<BisAccountFrozenAuditDto> resultCode = new ResultCodeDto<>(EResultCode.SUCCESS);
		//当前账务日期大于等于生效日期，立即生效。
		String accountDate = actAccountDateAppService.getAccountDate();
		if(StringUtil.isEmpty(accountDate)){
			return new ResultCodeDto<>(EResultCode.FAIL,"冻结订单【"+id+"】获取账务日期为空");
		}
		Date changeToDate;
		try {
			changeToDate = getAccountDate();
		} catch (ParseException e1) {
			_log.error("获取账务日期失败：{}",e1);
			return new ResultCodeDto<>(EResultCode.FAIL,"冻结订单【"+id+"】账务日期转换失败");
		}
		if(item==null){
			return new ResultCodeDto<>(EResultCode.FAIL,"冻结订单【"+id+"】记录不存在");
		}
		item.setFrozenAuditStatus(BISAuditStatus.AUDIT_PASS);
		//如果审核的时候，到了截至日期，直接解冻
		if(changeToDate.compareTo(item.getFrozenEndTime())>=0){
			sysAutoUnFrozen(item);
			return resultCode;
		}
		//如果存在未知状态的冻结记录，不允许生成冻结记录(此处不需要校验)
		/*if(isExistUndownStatusOper(item.getId())){
			_log.info("冻结订单【"+id+"】存在未知状态的记录，不能生成冻结记录");
			return resultCode;
		}*/
		if(changeToDate.compareTo(item.getFrozenValidTime())>=0){
			_log.info("手工审核通过，执行冻结");
			String orderId = getOrderId();
			long frozenAmt=0;
			try {
				String accountId = item.getFrozenAccountId();
				long autualAmt=item.getActualFrozenAmt();
				frozenAmt = getFrozenAmt(autualAmt, accountId, item.getAmount());
				if(0==frozenAmt){
					messageNotify(autualAmt, frozenAmt,item.getId());
				}else{
					ResultCodeDto<PayAccountAdjustDto> returnCodeDto = accountAdjustAppService.frozenAccountAmount(accountId, frozenAmt, orderId);
					saveOper(returnCodeDto, orderId, frozenAmt,item);
				}
			} catch (CbsCheckedException e) {
				_log.error("资金冻结异常：{}",e);
				bisExceptionLogService.saveLog(CbsExceptionLogDtoUtils.getBisExceptionLog(EBisExceptionLogNlevel.ERROR, EBisExceptionLogType.FROZEN,e.getMessage()));
				saveOper(null, orderId, frozenAmt,item);
				return new ResultCodeDto<>(EResultCode.FAIL);
			}catch (Exception e) {
				_log.error("资金冻结异常：{}",e);
				bisExceptionLogService.saveLog(CbsExceptionLogDtoUtils.getBisExceptionLog(EBisExceptionLogNlevel.ERROR, EBisExceptionLogType.FROZEN,e.getMessage()!=null?e.getMessage():"资金冻结异常，空指针"));
				return new ResultCodeDto<>(EResultCode.FAIL);
			}
		}
		updateSelective(item);
		//保存信息到审核记录表
		saveBisAuditDto(auditor, auditorName, id,BISAuditStatus.AUDIT_PASS,BISAuditType.AMOUNT_FROZEN,remark);
		return resultCode;
	}
	
	/**
	 * 冻结批量复核拒绝
	 * @param auditor
	 * @param auditorName
	 * @param idList
	 * @param reason
	 */
	@Override
	public void frozenBatchAuditReject(String auditor, String auditorName, List<String> idList,String reason) {
		if(idList == null || idList.isEmpty())return;
		for(String id : idList){
			frozenAuditReject(auditor, auditorName, id,reason);
		}
	}
	
	/**
	 * 冻结复核拒绝
	 */
	@Override
	public void frozenAuditReject(String auditor, String auditorName, String id,String reason) {
		BisAccountFrozenAuditDto dto = bisAccountFrozenAuditDtoMapper.selectByPrimaryKey(id);
		if(dto!=null){
			if(BISAuditStatus.AUDIT_REJECT==dto.getFrozenAuditStatus()){
				throw new CbsUncheckedException("","冻结编号【"+id+"】已审核拒绝，不能重复拒绝");
			}
			dto.setFrozenAuditStatus(BISAuditStatus.AUDIT_REJECT);
			updateSelective(dto);
			//保存信息到审核记录表
			saveBisAuditDto(auditor, auditorName, id,BISAuditStatus.AUDIT_REJECT,BISAuditType.AMOUNT_FROZEN,reason);
		}else{
			throw new CbsUncheckedException("","冻结编号【"+id+"】不存在");
		}
	}
	
	/**
	 * 批量冻结撤销
	 * @param idList
	 * @param reason
	 * @param loginName
	 * @param realName
	 */
	@Override
	public void frozenBatchCancel(List<String> idList,String reason,String loginName,String realName) {
		if(idList == null || idList.isEmpty())return;
		for(String id : idList){
			frozenCancel(loginName,realName,id,reason);
		}
	}
	
	/**
	 * 冻结撤销
	 * @param auditor
	 * @param auditorName
	 * @param id
	 * @param reason
	 */
	@Override
	public void frozenCancel(String auditor, String auditorName,String id,String reason) {
		BisAccountFrozenAuditDto dto = bisAccountFrozenAuditDtoMapper.selectByPrimaryKey(id);
		if(dto!=null){
			if(BISAuditStatus.REVOKED==dto.getFrozenAuditStatus()){
				throw new CbsUncheckedException("","冻结编号【"+id+"】已撤销，不能重复撤销");
			}
			dto.setFrozenAuditStatus(BISAuditStatus.REVOKED);
			updateSelective(dto);
			//保存信息到审核记录表
			saveBisAuditDto(auditor, auditorName, id,BISAuditStatus.REVOKED,BISAuditType.AMOUNT_FROZEN,reason);
		}else{
			throw new CbsUncheckedException("","冻结编号【"+id+"】不存在");
		}
	}
	
	/**
	 * 解冻列表
	 */
	@Override
	public PageData<BisAccountFrozenAuditDto> listUnfreeze(PageData<BisAccountFrozenAuditDto> pageData,BisAccountFrozenAuditDto queryParam) {
		PageHelper.startPage(pageData.getPageNumber(), pageData.getPageSize());
		List<BisAccountFrozenAuditDto> items = bisAccountFrozenAuditDtoMapper.listUnfreezeAudit(queryParam);
		Page<BisAccountFrozenAuditDto> page = (Page<BisAccountFrozenAuditDto> )items;
		pageData.setRows(items);
		pageData.setTotal(page.getTotal());
		return pageData;
	}
	
	/**
	 * 批量解冻
	 * @param ids
	 * @param loginName
	 * @param realName
	 * @param reason
	 * @return
	 */
	@Override
	public long batchUnFrozen(List<String> ids,String loginName,String realName,String reason) {
		List<BisAccountFrozenAuditDto> frozenDtos=bisAccountFrozenAuditDtoMapper.queryFrozenListByIds(ids);
		if(frozenDtos!=null && !frozenDtos.isEmpty() && frozenDtos.size()==ids.size()){
			for (BisAccountFrozenAuditDto frozenDto : frozenDtos) {
				Long unfrozenAmt=frozenDto.getActualFrozenAmt()-frozenDto.getUnfrozenAmt();
				if(unfrozenAmt==0){
					frozenDto.setFrozenStatus(EFrozenStatus.UNFREEZE_SUCCESS);
					bisAccountFrozenAuditDtoMapper.updateByPrimaryKeySelective(frozenDto);
					_log.info("冻结编号【"+frozenDto.getId()+"】可以解冻金额为0，无需解冻");
					continue;
				}else{
					BisAccountUnfrozenAuditDto unfrozenDto = new BisAccountUnfrozenAuditDto();
					String orderId = getOrderId();
					//生成解冻记录
					Date accountDate = null;
					try {
						accountDate = getAccountDate();
					} catch (ParseException e1) {
						_log.error("获取账务日期失败：{}",e1);
					}
					frozenDtoCopyToUnFrozenDto(frozenDto, unfrozenDto, accountDate, BISAuditStatus.WAIT_AUDIT,
							EUnfreezeType.MANUAL,reason , unfrozenAmt,orderId,loginName,realName,null);
					//修改冻结金额汇总
					bisAccountFrozenAuditDtoMapper.updateUnFrozenAmt(unfrozenAmt, frozenDto.getId(),EFrozenStatus.UNFROZEN_DOING);
				}
			}
		}else{
			throw new CbsUncheckedException("","批量解冻记录中个别记录不存在");
		}
		return frozenDtos.size();
	}
	
	/**
	 * 部分解冻
	 * @param id
	 * @param loginName
	 * @param realName
	 * @param reason
	 * @param unFrozenAmt
	 * @param unFrozenDate
	 * @return
	 */
	@Override
	public long partUnFrozen(String id,String loginName,String realName,String reason,long unFrozenAmt,Date unFrozenDate){
		BisAccountFrozenAuditDto frozenDto = bisAccountFrozenAuditDtoMapper.selectByPrimaryKey(id);
		if(frozenDto!=null){
			Long unfrozenAmt=frozenDto.getActualFrozenAmt()-frozenDto.getUnfrozenAmt();
			if(unfrozenAmt==0){
				frozenDto.setFrozenStatus(EFrozenStatus.UNFREEZE_SUCCESS);
				bisAccountFrozenAuditDtoMapper.updateByPrimaryKeySelective(frozenDto);
				_log.info("冻结编号【"+frozenDto.getId()+"】可以解冻金额为0，无需解冻");
			}else{
				//生成解冻记录
				String orderId = getOrderId();
				BisAccountUnfrozenAuditDto unfrozenDto = new BisAccountUnfrozenAuditDto();
				frozenDtoCopyToUnFrozenDto(frozenDto, unfrozenDto, unFrozenDate, BISAuditStatus.WAIT_AUDIT,
						EUnfreezeType.MANUAL,reason , unFrozenAmt,orderId,loginName,realName,null);
				//修改解冻金额汇总
				bisAccountFrozenAuditDtoMapper.updateUnFrozenAmt(unFrozenAmt, frozenDto.getId(),EFrozenStatus.UNFROZEN_DOING);
			}
		}else{
			throw new CbsUncheckedException("","冻结编号【"+id+"】记录不存在");
		}
		return 0;
	}
	
	/**
	 * 批量解冻复核通过
	 * @param auditor
	 * @param auditorName
	 * @param idList
	 * @return
	 */
	@Override
	public ResultCodeDto<BisAccountFrozenAuditDto> unfreezeBatchAuditPass(String auditor, String auditorName, List<String> idList,String reason) {
		if(idList == null || idList.isEmpty())return new ResultCodeDto<BisAccountFrozenAuditDto>(EResultCode.FAIL,"解冻记录为空");
		int failNum = 0;
		int unknownNum = 0;
		int successNum=0;
		for(String id : idList){
			ResultCodeDto<BisAccountFrozenAuditDto> rnt = unfreezeAuditPass(auditor, auditorName, id,reason);
			if(rnt.getResultCode().equals(EResultCode.FAIL)){
				failNum++;
			} else if(rnt.getResultCode().equals(EResultCode.SUCCESS)){
				successNum++;
			} else {
				unknownNum++;
			}
		}
		if(failNum > 0 || unknownNum > 0){
			String msg = "资金解冻成功"+successNum+"条，失败"+failNum+"条，状态未知"+unknownNum+"条";
			return new ResultCodeDto<>(EResultCode.FAIL,msg);
		} else {
			return new ResultCodeDto<>(EResultCode.SUCCESS);
		}
	}
	
	/**
	 * 解冻复核通过
	 * @param auditor
	 * @param auditorName
	 * @param id
	 * @param reason
	 * @return
	 */
	@Override
	public ResultCodeDto<BisAccountFrozenAuditDto> unfreezeAuditPass(String auditor, String auditorName, String id,String reason) {
		BisAccountUnfrozenAuditDto unFrozenDto = bisAccountUnfrozenAuditDtoMapper.selectByPrimaryKey(id);
		//当前账务日期大于等于生效日期，立即生效。
		String accountDate = actAccountDateAppService.getAccountDate();
		if(StringUtil.isEmpty(accountDate)){
			return new ResultCodeDto<>(EResultCode.FAIL,"冻结订单【"+id+"】账务日期获取为空");
		}
		//生成解冻记录
		Date changeToDate = null;
		try {
			changeToDate = getAccountDate();
		} catch (ParseException e1) {
			_log.error("获取账务日期失败：{}",e1);
			return new ResultCodeDto<>(EResultCode.FAIL,"冻结订单【"+id+"】账务日期转型失败");
		}
		if(unFrozenDto==null){
			return new ResultCodeDto<>(EResultCode.FAIL,"冻结订单【"+id+"】记录不存在");
		}
		if(changeToDate.compareTo(unFrozenDto.getUnfrozenValidTime())>=0){
			doUnforzen(unFrozenDto,false);
		}
		//修改解冻订单
		unFrozenDto.setUnfreezeAuditStatus(BISAuditStatus.AUDIT_PASS);
		bisAccountUnfrozenAuditDtoMapper.updateByPrimaryKeySelective(unFrozenDto);
		//保存复核记录
		saveBisAuditDto(auditor, auditorName, id,BISAuditStatus.AUDIT_PASS,BISAuditType.AMOUNT_UNFREEZE,reason);
		return new ResultCodeDto<>(EResultCode.SUCCESS);
	}
	
	/**
	 * 执行解冻
	 * @param unFrozenDto
	 */
	public void doUnforzen(BisAccountUnfrozenAuditDto unFrozenDto,boolean isCreate){
		//调用解冻接口
		long unfrozenAmt=unFrozenDto.getActualUnfrozenAmt();
		String frozenId = unFrozenDto.getFrozenId();
		//如果不是创建解冻订单和解冻一起的，不能重复加解冻金额，但实际解冻金额是成功后才会加的(到期自动解冻，解冻复核通过，定时任务调用解冻)
		long setAmt=unfrozenAmt;
		if(!isCreate){
			setAmt=0l;
		}
		try {
			ResultCodeDto<PayAccountAdjustDto> rtn = accountAdjustAppService.noFrozenAccountAmount(unFrozenDto.getUnfrozenAccountId(), 
					unfrozenAmt, unFrozenDto.getId());
			if(rtn!=null && rtn.getResultCode()!=null){
				EResultCode resultCodeEnum = rtn.getResultCode();
				if(EResultCode.SUCCESS==resultCodeEnum){
					BisAccountFrozenAuditDto forzenDto = bisAccountFrozenAuditDtoMapper.selectByPrimaryKey(frozenId);
					EFrozenStatus frozenStatus=EFrozenStatus.UNFROZEN_DOING;
					if(isComplate(forzenDto.getActualUnfrozenAmt(), forzenDto.getActualFrozenAmt(), unfrozenAmt)){
						frozenStatus=EFrozenStatus.UNFREEZE_SUCCESS;
					}
					//修改解冻金额和实际解冻金额
					_log.info("执行解冻，实际解冻金额【"+unfrozenAmt+"】解冻金额【"+setAmt+"】");
					bisAccountFrozenAuditDtoMapper.updateActualUnFrozenAmt(setAmt, frozenId,frozenStatus,unfrozenAmt);
					_log.info("执行结束");
					//bisAccountFrozenAuditDtoMapper.updateUnFrozenAmt(unfrozenAmt, frozenId);
					unFrozenDto.setPayStatus(PayStatusEnum.SUCCESS);
					unFrozenDto.setRemark(rtn.getMsgDetail());
					//如果是系统自动创建解冻记录的，一般是冻结没有完成的，此时如果解冻成功需要修改批次的成功笔数
					if(isCreate && EFrozenStatus.FROZEN_DOING.getValue().equals(forzenDto.getFrozenStatus().getValue())){
						updateBatchExpNum(forzenDto,1,0);
					}
				}else if(EResultCode.FAIL==resultCodeEnum){
					unFrozenDto.setPayStatus(PayStatusEnum.FAIL);
					unFrozenDto.setRemark(rtn.getMsgDetail());
					//解冻不论成功失败，只要生成解冻记录就变成解冻中
					bisAccountFrozenAuditDtoMapper.updateUnFrozenAmt(0L, frozenId,EFrozenStatus.UNFROZEN_DOING);
				}else{
					bisAccountFrozenAuditDtoMapper.updateUnFrozenAmt(setAmt, frozenId,EFrozenStatus.UNFROZEN_DOING);
					unFrozenDto.setPayStatus(PayStatusEnum.UNKNOWN);
					unFrozenDto.setRemark(rtn.getMsgDetail());
				}
			}else{
				bisAccountFrozenAuditDtoMapper.updateUnFrozenAmt(setAmt, frozenId,EFrozenStatus.UNFROZEN_DOING);
				unFrozenDto.setPayStatus(PayStatusEnum.UNKNOWN);
				unFrozenDto.setRemark(rtn.getMsgDetail());
			}
		} catch (Exception e) {
			_log.error("冻结编号【"+frozenId+"】，调用支付解冻接口异常：{}",e);
			bisAccountFrozenAuditDtoMapper.updateUnFrozenAmt(setAmt, frozenId,EFrozenStatus.UNFROZEN_DOING);
			unFrozenDto.setPayStatus(PayStatusEnum.UNKNOWN);
			unFrozenDto.setRemark("调用支付解冻接口异常");
		}
	}
	
	/**
	 * 冻结订单copy到解冻订单,并保存冻结复核记录
	 */
	public void frozenDtoCopyToUnFrozenDto(BisAccountFrozenAuditDto frozenDto,BisAccountUnfrozenAuditDto unfrozenDto,Date unfrozenValidTime,
			BISAuditStatus auditStatus,EUnfreezeType freezenType,String unfrozenReason,Long unfrozenAmt,String orderId,String loginName,String realName,
			String remark){
		unfrozenDto.setId(orderId);
		//unfrozenDto.setAccountId(frozenDto.getAccountId());
		//unfrozenDto.setAccountName(frozenDto.getAccountName());
		ActAccountDto accountFrozenDto = actAccountService.findFrozenAccountByCustId(frozenDto.getCustId(),frozenDto.getFrozenAccountId());
		if(accountFrozenDto!=null){
			unfrozenDto.setUnfrozenAccountId(accountFrozenDto.getAccountId());
		}else{
			throw new CbsUncheckedException("","解冻编号【"+frozenDto.getId()+"】资金解冻失败：客户编号【"+frozenDto.getCustId()+"】没有冻结备付金");
		}
		unfrozenDto.setUnfrozenAccountName(frozenDto.getFrozenAccountName());
		unfrozenDto.setCustId(frozenDto.getCustId());
		unfrozenDto.setCustName(frozenDto.getCustName());
		unfrozenDto.setUnfrozenValidTime(unfrozenValidTime);
		unfrozenDto.setUnfreezeAuditStatus(auditStatus);
		unfrozenDto.setUnfreezeOperator(loginName);
		unfrozenDto.setUnfreezeOperatorName(realName);
		unfrozenDto.setUnfreezeOperatorTime(new Date());
		unfrozenDto.setUnfreezeType(freezenType);
		unfrozenDto.setUnfrozenReason(unfrozenReason);
		unfrozenDto.setActualUnfrozenAmt(unfrozenAmt);
		unfrozenDto.setFrozenId(frozenDto.getId());
		unfrozenDto.setSubjectName(accountFrozenDto.getSubjectNo2Name());
		unfrozenDto.setPayStatus(PayStatusEnum.WAITPAY);
		//unfrozenDto.setRemark("冻结截至日期小于或等于当前账务日期，自动解冻");
		bisAccountUnfrozenAuditDtoMapper.insert(unfrozenDto);
	}
	
	/**
	 * 批量解冻复核拒绝
	 * @param auditor
	 * @param auditorName
	 * @param idList
	 * @param reason
	 */
	@Override
	public void unfreezeBatchAuditReject(String auditor, String auditorName, List<String> idList,String reason) {
		if(idList == null || idList.isEmpty())return;
		for(String id : idList){
			unfreezeAuditReject(auditor, auditorName, id,reason);
		}
	}
	
	/**
	 * 解冻复核拒绝
	 * @param auditor
	 * @param auditorName
	 * @param id
	 * @param reason
	 */
	@Override
	public void unfreezeAuditReject(String auditor, String auditorName, String id,String reason) {
		BisAccountUnfrozenAuditDto unfrozenDto =bisAccountUnfrozenAuditDtoMapper.selectByPrimaryKey(id);
		unfrozenDto.setUnfreezeAuditStatus(BISAuditStatus.AUDIT_REJECT);
		bisAccountUnfrozenAuditDtoMapper.updateByPrimaryKeySelective(unfrozenDto);
		//扣减冻结金额汇总
		bisAccountFrozenAuditDtoMapper.updateUnFrozenAmt(0-unfrozenDto.getActualUnfrozenAmt(), unfrozenDto.getFrozenId(), EFrozenStatus.UNFROZEN_DOING);
		//添加复核记录
		saveBisAuditDto(auditor, auditorName, id,BISAuditStatus.AUDIT_REJECT,BISAuditType.AMOUNT_UNFREEZE,reason);	
	}
	
	/**
	 * 批量解冻 撤销
	 * @param idList
	 * @param reason
	 */
	@Override
	public void unfreezeBatchCancel(List<String> idList,String reason,String loginName,String realName) {
		if(idList == null || idList.isEmpty())return;
		for(String id : idList){
			unfreezeCancel(id,reason,loginName,realName);
		}
	}

	/**
	 * 解冻撤销
	 * @param id
	 * @param reason
	 */
	@Override
	public void unfreezeCancel(String id,String reason,String loginName,String realName) {
		BisAccountUnfrozenAuditDto unfrozenDto =bisAccountUnfrozenAuditDtoMapper.selectByPrimaryKey(id);
		unfrozenDto.setUnfreezeAuditStatus(BISAuditStatus.REVOKED);
		bisAccountUnfrozenAuditDtoMapper.updateByPrimaryKeySelective(unfrozenDto);
		//扣减冻结金额汇总
		bisAccountFrozenAuditDtoMapper.updateUnFrozenAmt(0-unfrozenDto.getActualUnfrozenAmt(), unfrozenDto.getFrozenId(), EFrozenStatus.UNFROZEN_DOING);
		//添加复核记录
		saveBisAuditDto(loginName, realName, id,BISAuditStatus.REVOKED,BISAuditType.AMOUNT_UNFREEZE,reason);
	}
	/**
	 * 冻结订单详情
	 */
	@Override
	public BisAccountFrozenAuditDto findById(String id) {
		BisAccountFrozenAuditDto frozenAuditDto = bisAccountFrozenAuditDtoMapper.selectByPrimaryKey(id);
		//获取审核详情
		List<BisAuditDto> auditDtoList = bisAuditService.findFrozenListBybusId(id, BISAuditType.AMOUNT_FROZEN);
		if(frozenAuditDto!=null){
			frozenAuditDto.setAuditDtoList(auditDtoList);
		}
		return frozenAuditDto;
	}
	
	/**
	 * 解冻订单列表
	 */
	@Override
	public PageData<BisAccountFrozenAuditDto> listUnFrozen(PageData<BisAccountFrozenAuditDto> pageData,BisAccountFrozenAuditDto queryParam){
		PageHelper.startPage(pageData.getPageNumber(), pageData.getPageSize());
		List<BisAccountFrozenAuditDto> items = bisAccountFrozenAuditDtoMapper.listUnfreezeAudit(queryParam);
		Page<BisAccountFrozenAuditDto> page = (Page<BisAccountFrozenAuditDto> )items;
		pageData.setRows(items);
		pageData.setTotal(page.getTotal());
		return pageData;
	}
	
	/**
	 * 获取复核通过"冻结申请或者冻结中"的冻结记录
	 */
	@Override
	public List<BisAccountFrozenAuditDto> listFrozenWaitOrDing(Date parse) {
		return bisAccountFrozenAuditDtoMapper.listFrozenWaitOrDing(parse);
	}
	
	/**
	 * 执行冻结
	 */
	@Override
	public void frozenAccount(BisAccountFrozenAuditDto item) {
		//如果存在未知状态的冻结记录，不允许生成冻结记录(此处不需要校验)
		if(isExistUndownStatusOper(item.getId())){
			_log.info("冻结订单【"+item.getId()+"】存在未知状态的记录，不能生成冻结记录");
			return;
		}
		doFrozen(item);
	}
	
	/**
	 * 定时任务调用，执行解冻(所有复核通过，到了截止日期还未完成解冻的，系统自动解冻)
	 * 如果存在到了截止日期还没有冻结完成的，此处默认冻结完成，修改批次成功笔数
	 */
	@Override
	public void unFrozenAccount(BisAccountFrozenAuditDto frozenDto) {
		Long actualFrozenAmt = frozenDto.getActualFrozenAmt();
		Long unfrozenAmt = frozenDto.getUnfrozenAmt();
		if(actualFrozenAmt-unfrozenAmt!=0){
			BisAccountUnfrozenAuditDto unfrozenDto = new BisAccountUnfrozenAuditDto();
			String orderId = getOrderId();
			//生成解冻记录
			Date accountDate = null;
			try {
				accountDate = getAccountDate();
			} catch (ParseException e1) {
				_log.error("获取账务日期失败：{}",e1);
			}
			//生成解冻记录
			frozenDtoCopyToUnFrozenDto(frozenDto, unfrozenDto, accountDate, BISAuditStatus.AUTO_UNFROZEN,
					EUnfreezeType.AUTO,null , actualFrozenAmt-unfrozenAmt,orderId,null,null,"到达截止日期，系统自动生成解冻记录");
			//调用解冻接口
			doUnforzen(unfrozenDto,true);
			//修改解冻订单
			bisAccountUnfrozenAuditDtoMapper.updateByPrimaryKeySelective(unfrozenDto);
		}/*else{
			frozenDto.setFrozenStatus(EFrozenStatus.FROZEN_SUCCESS);
			bisAccountFrozenAuditDtoMapper.updateByPrimaryKeySelective(frozenDto);
		}*/
	}
	
	/**
	 * 定时任务调用，复核通过 到了解冻日期且待处理的解冻记录
	 */
	@Override
	public void doUnFrozenAccount(BisAccountUnfrozenAuditDto unfrozenDto) {
		doUnforzen(unfrozenDto,false);
		//修改解冻订单
		bisAccountUnfrozenAuditDtoMapper.updateByPrimaryKeySelective(unfrozenDto);
	}
	
	/**
	 * 获取复核通过，但是到了冻结截至日期数据
	 */
	@Override
	public List<BisAccountFrozenAuditDto> listUnFrozenByEndTime(Date actDate) {
		return bisAccountFrozenAuditDtoMapper.listUnFrozenByEndTime(actDate);
	}
	
	/**
	 * 解冻未知状态查询
	 */
	@Override
	public void autoUnFrozenQueryUnDownStatus() {
		_log.info("解冻未知状态查询开始");
		//查询所有支付状未知
		List<BisAccountUnfrozenAuditDto> unDownStatus =bisAccountUnfrozenAuditDtoMapper.autoQueryUnDownStatus();
		if(unDownStatus!=null && !unDownStatus.isEmpty()){
			_log.info("解冻未知状态查询开始：查询条数【"+unDownStatus.size()+"】");
			for (BisAccountUnfrozenAuditDto dto : unDownStatus) {
				doUnfrozenQueryUnDownStatus(dto);
			}
		}
		_log.info("解冻未知状态查询结束");
		
	}
	
	/**
	 * 执行解冻未知状态查询
	 * @param dto
	 */
	public void doUnfrozenQueryUnDownStatus(BisAccountUnfrozenAuditDto dto){
		try {
			ResultCodeDto<PayAccountAdjustDto> msgResult = accountAdjustAppService.findByOrderAndPayAdjustType(dto.getId(), EPayAdjustType.FROZEN);
			if(msgResult==null){
				bisExceptionLogService.saveLog(CbsExceptionLogDtoUtils.getBisExceptionLog(EBisExceptionLogNlevel.ERROR, EBisExceptionLogType.QUERY_UNDOWNSTATUS,"解冻自动查询状态失败:返回结果为null"));
				_log.error("解冻订单未知状态查询失败，返回结果为空");
				return ;
			}
			EResultCode rtn = msgResult.getResultCode();
			if(EResultCode.SUCCESS.equals(rtn)){
				BisAccountFrozenAuditDto frozenDto = bisAccountFrozenAuditDtoMapper.selectByPrimaryKey(dto.getFrozenId());
				dto.setPayStatus(PayStatusEnum.SUCCESS);
				EFrozenStatus frozenStatus=EFrozenStatus.UNFROZEN_DOING;
				if(isComplate(frozenDto.getActualUnfrozenAmt(),frozenDto.getActualFrozenAmt(), dto.getActualUnfrozenAmt())){
					frozenStatus=EFrozenStatus.UNFREEZE_SUCCESS;
				}
				bisAccountFrozenAuditDtoMapper.updateActualUnFrozenAmt(0l, frozenDto.getId(), frozenStatus,dto.getActualUnfrozenAmt());
				
			} else if(EResultCode.FAIL.equals(rtn)){
				dto.setPayStatus(PayStatusEnum.FAIL);
				BisAccountFrozenAuditDto frozenDto = bisAccountFrozenAuditDtoMapper.selectByPrimaryKey(dto.getFrozenId());
				bisAccountFrozenAuditDtoMapper.updateUnFrozenAmt(0-dto.getActualUnfrozenAmt(), frozenDto.getId(), EFrozenStatus.UNFROZEN_DOING);
			}
			dto.setRemark(msgResult.getMsgDetail());
			bisAccountUnfrozenAuditDtoMapper.updateByPrimaryKeySelective(dto);
		} catch (Exception e) {
			_log.error(e.getMessage(),e);
			bisExceptionLogService.saveLog(CbsExceptionLogDtoUtils.getBisExceptionLog(EBisExceptionLogNlevel.ERROR, EBisExceptionLogType.QUERY_UNDOWNSTATUS,"解冻自动查询状态失败"));
		}
	}
	
	/**
	 * 修改批量导入记录成功/失败笔数
	 * @param frozenDto
	 * @param successNum
	 * @param failNum
	 */
	@Override
	public void updateBatchExpNum(BisAccountFrozenAuditDto frozenDto, long successNum, long failNum) {
		if(StringUtils.isBlank(frozenDto.getBatchNo())){
			return ;
		}
		bisBatchExpService.updateBatchExpNum(successNum,failNum,frozenDto.getBatchNo());
	}

	/**
	 * 冻结未知状态查询
	 */
	@Override
	public void autoFrozenQueryUnDownStatus(){
		_log.info("冻结未知状态查询开始");
		//查询所有支付状未知
		List<BisAccountFrozenOperDto> unDownStatus =bisAccountFrozenOperDtoMapper.autoQueryUnDownStatus();
		if(unDownStatus!=null && !unDownStatus.isEmpty()){
			_log.info("冻结未知状态查询开始：查询条数【"+unDownStatus.size()+"】");
			for (BisAccountFrozenOperDto dto : unDownStatus) {
				doFrozenQueryUnDownStatus(dto);
			}
		}
		_log.info("冻结未知状态查询结束");
	}
	
	/**
	 * 执行冻结未知状态查询
	 * @param dto
	 */
	public void doFrozenQueryUnDownStatus(BisAccountFrozenOperDto dto){
		try {
			ResultCodeDto<PayAccountAdjustDto> msgResult = accountAdjustAppService.findByOrderAndPayAdjustType(dto.getId(), EPayAdjustType.FROZEN);
			if(msgResult==null){
				bisExceptionLogService.saveLog(CbsExceptionLogDtoUtils.getBisExceptionLog(EBisExceptionLogNlevel.ERROR, EBisExceptionLogType.QUERY_UNDOWNSTATUS,"冻结自动查询状态失败:返回结果为null"));
				_log.error("冻结订单未知状态查询失败，返回结果为空");
				return ;
			}
			EResultCode rtn = msgResult.getResultCode();
			if(EResultCode.SUCCESS.equals(rtn)){
				BisAccountFrozenAuditDto frozenDto = bisAccountFrozenAuditDtoMapper.selectByPrimaryKey(dto.getBusId());
				dto.setPayStatus(PayStatusEnum.SUCCESS);
				EFrozenStatus frozenStatus=EFrozenStatus.FROZEN_DOING;
				if(isComplate(frozenDto.getActualFrozenAmt(), frozenDto.getAmount(), dto.getSetAmt())){
					frozenStatus=EFrozenStatus.FROZEN_SUCCESS;
					//修改批量导入记录的成功笔数
					updateBatchExpNum(frozenDto,1,0);
				}
				//bisAccountFrozenAuditDtoMapper.updateActualAmt(dto.getSetAmt(),dto.getId(),frozenStatus);
				frozenDto.setFrozenStatus(frozenStatus);
				frozenDto.setActualFrozenAmt(frozenDto.getActualFrozenAmt()+dto.getSetAmt());
				bisAccountFrozenAuditDtoMapper.updateByPrimaryKey(frozenDto);
			} else if(EResultCode.FAIL.equals(rtn)){
				dto.setPayStatus(PayStatusEnum.FAIL);
			}
			dto.setRemark(msgResult.getMsgDetail());
			bisAccountFrozenOperDtoMapper.updateByPrimaryKeySelective(dto);
		} catch (Exception e) {
			_log.error(e.getMessage(),e);
			bisExceptionLogService.saveLog(CbsExceptionLogDtoUtils.getBisExceptionLog(EBisExceptionLogNlevel.ERROR, EBisExceptionLogType.QUERY_UNDOWNSTATUS,"冻结自动查询状态失败"));
		}
	}

	/**
	 * 批次导入审核通过
	 */
	@Override
	public void batchExpAudit(BisAccountFrozenAuditDto dto) {
		if(isExistUndownStatusOper(dto.getId())){
			_log.info("冻结编号【"+dto.getId()+"】存在未知状态冻结明细记录，不予冻结");
		}else{
			//当前账务日期大于等于生效日期，立即生效。
			String accountDate = actAccountDateAppService.getAccountDate();
			if(StringUtil.isEmpty(accountDate)){
				_log.error("获取账务日期失败：账务日期为空");
				bisExceptionLogService.saveLog(CbsExceptionLogDtoUtils.getBisExceptionLog(EBisExceptionLogNlevel.ERROR, EBisExceptionLogType.FROZEN,"获取账务日期失败：账务日期为空"));
			}
			//生成解冻记录
			Date actDate = null;
			try {
				actDate = getAccountDate();
			} catch (ParseException e1) {
				_log.error("获取账务日期失败：{}",e1);
			}
			//如果审核的时候，到了截至日期，直接解冻
			if(actDate.compareTo(dto.getFrozenEndTime())>=0){
				sysAutoUnFrozen(dto);
			}else if(actDate.compareTo(dto.getFrozenValidTime())<=0){
				doFrozen(dto);
			}	
		}
		updateDealStatus(EActInterestDealStatus.WAIT_DEAL, EActInterestDealStatus.DEALING, dto.getId());
	}
	
	/**
	 * 判断是否存在未知状态的冻结明细记录
	 * @param id
	 * @return
	 */
	private boolean isExistUndownStatusOper(String busId) {
		long num=bisAccountFrozenOperDtoMapper.isExistUndownStatusOper(busId);
		if(num>0){
			return true;
		}
		return false;
	}
	
	private boolean isExistPayOper(String busId) {
		long num=bisAccountFrozenOperDtoMapper.isExistPayOper(busId);
		if(num>0){
			return true;
		}
		return false;
	}

	@Override
	public long updateBatchAuditStatus(BISAuditStatus auditStatus, String batchNo) {
		return bisAccountFrozenAuditDtoMapper.updateBatchAuditStatus(auditStatus,batchNo);
	}

	@Override
	public List<BisAccountFrozenAuditDto> findListByBatchNo(String batchNo) {
		return bisAccountFrozenAuditDtoMapper.findListByBatchNo(batchNo);
	}

	@Override
	public long updateDealStatus(EActInterestDealStatus newStatus, EActInterestDealStatus oldStatus, String id) {
		return bisAccountFrozenAuditDtoMapper.updateDealStatus(newStatus,oldStatus,id);
	}
	
	@Override
	public void updateSelective(BisAccountFrozenAuditDto bisAccountFrozenAuditDto) {
		bisAccountFrozenAuditDtoMapper.updateByPrimaryKeySelective(bisAccountFrozenAuditDto);
	}
	
	/**
	 * 获取冻结金额
	 * @param actualAmt
	 * @param accountId
	 * @param setAmt
	 * @return
	 * @throws CbsCheckedException
	 */
	public long getFrozenAmt(long actualAmt,String accountId,long setFrozenAmt) throws CbsCheckedException{
		long amt = setFrozenAmt-actualAmt;
		if(amt>0){
			ActAccountDto accountDto=actAccountService.findAccountDtoByAccountId(accountId);
			if(accountDto!=null){
				long cashAmount = accountDto.getCashAmount();
				if(cashAmount-amt>=0){
					return amt;
				}else{
					return cashAmount;
				}
			}
			throw new CbsCheckedException("","账户【"+accountId+"】不存在");
		}
		return amt;		
	}
	
	/**
	 * 如果是第一次冻结且冻结金额不足时，消息通知业务人员
	 * @param autualAmt
	 * @param frozenAmt
	 */
	public void messageNotify(long autualAmt,long frozenAmt,String busId){
		if(0==autualAmt && !isExistPayOper(busId)){
			//调用消息通知接口
			
		}
	}
	
	/**
	 * 获取序列
	 * @return
	 */
	public String getOrderId(){
		long seq = bisAccountFrozenAuditDtoMapper.getSequence();
		return new SimpleDateFormat("yyyyMMdd").format(new Date())+String.valueOf(seq);
	}
	
	/**
	 * 根据返回值保存冻结明细记录
	 * @param returnCodeDto
	 * @return
	 */
	private void saveOper(ResultCodeDto<PayAccountAdjustDto> returnCodeDto,String orderId,long setAmt,BisAccountFrozenAuditDto dto){
		EFrozenStatus frozenStatus=EFrozenStatus.FROZEN_DOING;
		PayStatusEnum payStatus;
		String remark;
		if(returnCodeDto!=null && returnCodeDto.getResultCode()!=null){
			EResultCode resultCode=returnCodeDto.getResultCode();
			if(EResultCode.SUCCESS==resultCode){
				if(isComplate(dto.getActualFrozenAmt(), dto.getAmount(), setAmt)){
					frozenStatus=EFrozenStatus.FROZEN_SUCCESS;
					//修改批量导入记录的成功笔数
					updateBatchExpNum(dto,1,0);
				}
				payStatus=PayStatusEnum.SUCCESS;
				//bisAccountFrozenAuditDtoMapper.updateActualAmt(setAmt,dto.getId(),frozenStatus);
				dto.setActualFrozenAmt(dto.getActualFrozenAmt()+setAmt);
				
			}else if(EResultCode.FAIL==resultCode){
				payStatus=PayStatusEnum.FAIL;
				dto.setFrozenStatus(EFrozenStatus.FROZEN_DOING);
				bisAccountFrozenAuditDtoMapper.updateByPrimaryKeySelective(dto);
				//bisAccountFrozenAuditDtoMapper.updateActualAmt(setAmt,dto.getId(),frozenStatus );
				//修改批量导入记录的失败笔数
				//updateBatchExpNum(dto,0,1);
			}else {
				payStatus=PayStatusEnum.UNKNOWN;
				dto.setFrozenStatus(EFrozenStatus.FROZEN_DOING);
				bisAccountFrozenAuditDtoMapper.updateByPrimaryKeySelective(dto);
				//bisAccountFrozenAuditDtoMapper.updateActualAmt(setAmt,dto.getId(),frozenStatus );
			}
			remark=returnCodeDto.getMsgDetail();
		}else{
			payStatus=PayStatusEnum.UNKNOWN;
			remark="资金冻结调用支付接口异常";
			dto.setFrozenStatus(EFrozenStatus.FROZEN_DOING);
			bisAccountFrozenAuditDtoMapper.updateByPrimaryKeySelective(dto);
			//bisAccountFrozenAuditDtoMapper.updateActualAmt(setAmt,dto.getId(),frozenStatus);
		}
		dto.setFrozenStatus(frozenStatus);
		//生成冻结操作记录
		BisAccountFrozenOperDto bisAccountFrozenOperDto = new BisAccountFrozenOperDto();
		bisAccountFrozenOperDto.setBusId(dto.getId());
		bisAccountFrozenOperDto.setCreateTime(new Date());
		bisAccountFrozenOperDto.setId(UUID.randomUUID().toString());
		bisAccountFrozenOperDto.setOrderId(orderId);
		bisAccountFrozenOperDto.setPayStatus(payStatus);
		bisAccountFrozenOperDto.setRemark(remark);
		bisAccountFrozenOperDto.setSetAmt(setAmt);
		bisAccountFrozenOperDtoMapper.insert(bisAccountFrozenOperDto);
	}
	
	/**
	 * 判断冻结/解冻是否完成
	 * @param actualAmt
	 * @param frozenAmt
	 * @param setAmt
	 * @return
	 */
	public boolean isComplate(long actualAmt,long frozenAmt,long setAmt){
		if(actualAmt+setAmt==frozenAmt){
			return true;
		}
		return false;
	}
	
	/**
	 * 审核通过后，到了解冻日期，无需调用支付系统直接解冻
	 * 
	 * @param dto
	 */
	public void sysAutoUnFrozen(BisAccountFrozenAuditDto dto){
		//到了截至日期，系统自动解冻
		long frozenAmt=dto.getActualFrozenAmt()-dto.getUnfrozenAmt();
		if(frozenAmt!=0){
			String orderId = getOrderId();
			BisAccountUnfrozenAuditDto unfrozenAuditDto = new BisAccountUnfrozenAuditDto();
			String remark="";
			try {
				ResultCodeDto<PayAccountAdjustDto> rtn = accountAdjustAppService.noFrozenAccountAmount(dto.getFrozenAccountId(), frozenAmt, orderId);
				if(rtn!=null){
					EResultCode resultCode = rtn.getResultCode();
					if(EResultCode.SUCCESS==resultCode){
						unfrozenAuditDto.setPayStatus(PayStatusEnum.SUCCESS);
						EFrozenStatus frozenStatus=EFrozenStatus.UNFROZEN_DOING;
						if(isComplate(dto.getActualUnfrozenAmt(), dto.getActualFrozenAmt(), frozenAmt)){
							frozenStatus=EFrozenStatus.UNFREEZE_SUCCESS;
						}
						//bisAccountFrozenAuditDtoMapper.updateActualUnFrozenAmt(frozenAmt, dto.getId(), frozenStatus);
						dto.setUnfrozenAmt(dto.getUnfrozenAmt()+frozenAmt);
						dto.setActualUnfrozenAmt(dto.getActualUnfrozenAmt()+frozenAmt);
						dto.setFrozenStatus(frozenStatus);
						bisAccountFrozenAuditDtoMapper.updateByPrimaryKeySelective(dto);
					}else if(EResultCode.FAIL==resultCode){
						unfrozenAuditDto.setPayStatus(PayStatusEnum.FAIL);
						dto.setFrozenStatus(EFrozenStatus.UNFROZEN_DOING);
						bisAccountFrozenAuditDtoMapper.updateByPrimaryKeySelective(dto);
					}else{
						unfrozenAuditDto.setPayStatus(PayStatusEnum.UNKNOWN);
						bisAccountFrozenAuditDtoMapper.updateUnFrozenAmt(frozenAmt, dto.getId(), EFrozenStatus.UNFROZEN_DOING);
					}
					remark=rtn.getMsgDetail();
				}else{
					remark="调用支付系统返回结果为空";
					unfrozenAuditDto.setPayStatus(PayStatusEnum.UNKNOWN);
					bisAccountFrozenAuditDtoMapper.updateUnFrozenAmt(frozenAmt, dto.getId(), EFrozenStatus.UNFROZEN_DOING);
				}
			} catch (Exception e) {
				unfrozenAuditDto.setPayStatus(PayStatusEnum.UNKNOWN);
				remark=e.getMessage();
				bisAccountFrozenAuditDtoMapper.updateUnFrozenAmt(frozenAmt, dto.getId(), EFrozenStatus.UNFROZEN_DOING);
				_log.error("到达冻结截至日期，系统自动解冻调用支付系统接口出错：{}",e);
			}
			 //生成解冻记录
			unfrozenAuditDto.setId(orderId);
			//unfrozenAuditDto.setAccountId(dto.getfrozenAccountId);
			//unfrozenAuditDto.setAccountName(dto.getAccountName());
			ActAccountDto accountFrozenDto = actAccountService.findFrozenAccountByCustId(dto.getCustId(),dto.getFrozenAccountId());
			if(accountFrozenDto!=null){
				unfrozenAuditDto.setUnfrozenAccountId(accountFrozenDto.getAccountId());
			}else{
				throw new CbsUncheckedException("","解冻编号【"+dto.getId()+"】资金解冻失败：客户编号【"+dto.getCustId()+"】没有冻结备付金");
			}
			unfrozenAuditDto.setUnfrozenAccountName(dto.getFrozenAccountName());
			unfrozenAuditDto.setCustId(dto.getCustId());
			unfrozenAuditDto.setCustName(dto.getCustName());
			unfrozenAuditDto.setSubjectName(accountFrozenDto.getSubjectNo2Name());
			Date accountDate = null;
			try {
				accountDate = getAccountDate();
			} catch (ParseException e1) {
				_log.error("获取账务日期失败：{}",e1);
			}
			unfrozenAuditDto.setUnfrozenValidTime(accountDate);
			unfrozenAuditDto.setUnfreezeAuditStatus(BISAuditStatus.AUDIT_PASS);
			unfrozenAuditDto.setUnfreezeOperatorTime(new Date());
			unfrozenAuditDto.setUnfreezeType(EUnfreezeType.AUTO);
			unfrozenAuditDto.setUnfrozenReason("冻结截至日期小于或等于当前账务日期，自动解冻");
			unfrozenAuditDto.setActualUnfrozenAmt(frozenAmt);
			unfrozenAuditDto.setFrozenId(dto.getId());
			unfrozenAuditDto.setRemark(remark);
			bisAccountUnfrozenAuditDtoMapper.insert(unfrozenAuditDto);
		}
		//dto.setRemark("冻结截至日期小于或等于当前账务日期，自动解冻");
		//dto.setUnfreezeType(EUnfreezeType.AUTO);
		//updateSelective(dto);
	}
	
	/**
	 * 如果生效执行冻结
	 * @param dto
	 */
	public void doFrozen(BisAccountFrozenAuditDto dto){
		Long frozenAmt = dto.getAmount();
		Long actualFrozenAmt = dto.getActualFrozenAmt();
		if(frozenAmt!=actualFrozenAmt){
			String orderId = getOrderId();
			try {
				String accountId = dto.getFrozenAccountId();
				frozenAmt = getFrozenAmt(actualFrozenAmt, accountId, frozenAmt);
				if(frozenAmt==0){
					messageNotify(actualFrozenAmt, frozenAmt,dto.getId());
				}else{
					ResultCodeDto<PayAccountAdjustDto> returnCodeDto = accountAdjustAppService.frozenAccountAmount(accountId, frozenAmt, orderId);
					saveOper(returnCodeDto, orderId, frozenAmt,dto);
				}
			} catch (CbsCheckedException e) {
				_log.error("资金冻结异常：{}",e);
				bisExceptionLogService.saveLog(CbsExceptionLogDtoUtils.getBisExceptionLog(EBisExceptionLogNlevel.ERROR, EBisExceptionLogType.FROZEN,e.getMessage()));
			}catch (Exception e) {
				_log.error("资金冻结异常：{}",e);
				bisExceptionLogService.saveLog(CbsExceptionLogDtoUtils.getBisExceptionLog(EBisExceptionLogNlevel.ERROR, EBisExceptionLogType.FROZEN,e.getMessage()));
				saveOper(null, orderId, frozenAmt,dto);
			}
		}else{
			dto.setFrozenStatus(EFrozenStatus.FROZEN_SUCCESS);
		}
		bisAccountFrozenAuditDtoMapper.updateByPrimaryKeySelective(dto);
	}
	
	/**
	 * 保存复核记录
	 * @param auditor
	 * @param auditorName
	 * @param id
	 * @param bisAuditStatus
	 * @param auditType
	 */
	private void saveBisAuditDto(String auditor, String auditorName, String id,
			BISAuditStatus bisAuditStatus,BISAuditType auditType,String remark) {
		BisAuditDto auditDto = new BisAuditDto();
		auditDto.setId(auditDto.getIdentity());
		auditDto.setAuditor(auditor);
		auditDto.setAuditorName(auditorName);
		auditDto.setAuditStatus(bisAuditStatus);
		auditDto.setAuditTime(new Date());
		auditDto.setAuditType(auditType);
		auditDto.setBusId(id);
		auditDto.setReason(remark);
		bisAuditService.insert(auditDto);
	}
	
	/**
	 * 账务日期转换
	 * @return
	 * @throws ParseException
	 */
	public Date getAccountDate() throws ParseException{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date accountDate = DateUtils.changeToDate(actAccountDateAppService.getAccountDate());
		return sdf.parse(sdf.format(accountDate));
	}
	
	/**
	 * 到了截止日期，如果存在待复核的解冻订单，直接修改状态为"到期自动解冻",执行解冻操作
	 * @param actDate
	 */
	@Override
	public void runOutWaitAuditToAutoFrozen(BisAccountUnfrozenAuditDto dto) {
		//执行解冻
		doUnforzen(dto,false);
		dto.setUnfreezeAuditStatus(BISAuditStatus.AUTO_UNFROZEN);
		bisAccountUnfrozenAuditDtoMapper.updateByPrimaryKeySelective(dto);
	}
	/**
	 * 获取审核通过未生效的数据记录
	 * @param ids
	 * @param auditPass
	 * @param frozenDoing
	 * @return
	 */
	@Override
	public long getAuditNum(List<String> ids,boolean isFrozen) {
		if(isFrozen){
			return bisAccountFrozenAuditDtoMapper.getAuditNum(ids,BISAuditStatus.AUDIT_PASS,EFrozenStatus.FROZEN_APPLY);
		}else{
			return bisAccountUnfrozenAuditDtoMapper.getAuditNum(ids,BISAuditStatus.AUDIT_PASS,PayStatusEnum.WAITPAY);
		}
	}
	
	/**
	 * 冻结处理状态查询
	 */
	@Override
	public ResultCodeDto<BisAccountFrozenAuditDto> redoQueryFrozenStatus(String id) {
		BisAccountFrozenOperDto operDto = bisAccountFrozenOperDtoMapper.selectByPrimaryKey(id);
		if(operDto!=null){
			if(PayStatusEnum.UNKNOWN!=operDto.getPayStatus()){
				return new ResultCodeDto<BisAccountFrozenAuditDto>(EResultCode.SUCCESS,"状态已为终态");
			}
			doFrozenQueryUnDownStatus(operDto);
			return new ResultCodeDto<BisAccountFrozenAuditDto>(EResultCode.SUCCESS,"查询完成");
		}
		throw new CbsUncheckedException("","实际冻结金额明细记录不存在");
	}
	
	/**
	 * 解冻处理状态查询
	 */
	@Override
	public ResultCodeDto<BisAccountFrozenAuditDto> redoQueryUnfreezeStatus(String id) {
		BisAccountUnfrozenAuditDto unFrozenDto = bisAccountUnfrozenAuditDtoMapper.selectByPrimaryKey(id);
		if(unFrozenDto!=null){
			if(PayStatusEnum.UNKNOWN!=unFrozenDto.getPayStatus()){
				return new ResultCodeDto<BisAccountFrozenAuditDto>(EResultCode.SUCCESS,"状态已为终态");
			}
			doUnfrozenQueryUnDownStatus(unFrozenDto);
			return new ResultCodeDto<BisAccountFrozenAuditDto>(EResultCode.SUCCESS,"查询完成");
		}
		throw new CbsUncheckedException("","解冻复核记录不存在");
	}

	/**
	 * 根据批次号获取冻结map
	 */
	@Override
	public Map<String, BisAccountFrozenAuditDto> findActFrozenMapByBatchNo(String batchNo) {
		List<BisAccountFrozenAuditDto> bisAccountFrozenAuditDtoList=bisAccountFrozenAuditDtoMapper.findActFrozenListByBatchNo(batchNo);
		HashMap<String, BisAccountFrozenAuditDto> actFrozenDtoMap = new HashMap<String,BisAccountFrozenAuditDto>();
		if(bisAccountFrozenAuditDtoList!=null && !bisAccountFrozenAuditDtoList.isEmpty()){
			for (BisAccountFrozenAuditDto bisAccountFrozenAuditDto : bisAccountFrozenAuditDtoList) {
				BisAccountFrozenAuditDto dto = actFrozenDtoMap.get(bisAccountFrozenAuditDto.getFrozenAccountId());
				if(dto!=null){
					dto.setAmount(dto.getAmount()+bisAccountFrozenAuditDto.getAmount());
				}else{
					actFrozenDtoMap.put(bisAccountFrozenAuditDto.getFrozenAccountId(), bisAccountFrozenAuditDto);
				}
			}
		}
		return actFrozenDtoMap;
	}

	@Override
	public List<BisAccountFrozenAuditDto> list(BisAccountFrozenAuditDto queryParam) {
		return bisAccountFrozenAuditDtoMapper.listFrozen(queryParam);
	}

	@Override
	public List<BisAccountFrozenOperDto> getOperList(String id) {
		return bisAccountFrozenOperDtoMapper.findActFrozenOperListByBusId(id);
	}
	//将符合通过，截止日期小于等于当前账务日期，冻结申请并且实际冻结金额等于0的直接修改为已解冻
	@Override
	public long autoUpdateUnFrozenByActDate(Date actDate) {
		List<BisAccountFrozenAuditDto> actFrozenDtos=bisAccountFrozenAuditDtoMapper.findListByUnFrozenByActDate(actDate);
		if(actFrozenDtos!=null && !actFrozenDtos.isEmpty()){
			for (BisAccountFrozenAuditDto dto : actFrozenDtos) {
				updateBatchExpNum(dto, 1, 0);
				bisAccountFrozenAuditDtoMapper.updateFrozenStatus(EFrozenStatus.UNFREEZE_SUCCESS,dto.getId());
			}
			return actFrozenDtos.size();
		}
		//return bisAccountFrozenAuditDtoMapper.autoUpdateUnFrozenByActDate(actDate);
		return 0;
	}
}

