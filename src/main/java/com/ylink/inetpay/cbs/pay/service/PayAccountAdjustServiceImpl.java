package com.ylink.inetpay.cbs.pay.service;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.ylink.inetpay.cbs.act.dao.ActAccountDtoMapper;
import com.ylink.inetpay.cbs.pay.dao.PayAccountAdjustDtoMapper;
import com.ylink.inetpay.cbs.ucs.sec.service.UcsSecUserServiceImpl;
import com.ylink.inetpay.common.core.constant.EAccountDrCr;
import com.ylink.inetpay.common.core.constant.EAuditResult;
import com.ylink.inetpay.common.core.constant.EPayAdjustType;
import com.ylink.inetpay.common.core.constant.EResultCode;
import com.ylink.inetpay.common.core.dto.ExportDto;
import com.ylink.inetpay.common.core.dto.ResultCodeDto;
import com.ylink.inetpay.common.project.account.dto.ActAccountDto;
import com.ylink.inetpay.common.project.cbs.exception.CbsUncheckedException;
import com.ylink.inetpay.common.project.cbs.exception.ECbsErrorCode;
import com.ylink.inetpay.common.project.pay.app.PayAccountAdjustAppService;
import com.ylink.inetpay.common.project.pay.dto.PayAccountAdjustDto;
import com.ylink.inetpay.common.project.pay.exception.PayCheckedException;
@Service("payAccountAdjustService")
public class PayAccountAdjustServiceImpl implements PayAccountAdjustService {
	@Autowired
	private PayAccountAdjustDtoMapper payAccountAdjustDtoMapper;
	@Autowired
	private PayAccountAdjustAppService paySystemPayAccountAdjustAppService;
	@Autowired
	private ActAccountDtoMapper actAccountDtoMapper;
	private static Logger _log = LoggerFactory.getLogger(UcsSecUserServiceImpl.class);
	@Override
	public void addNotes(PayAccountAdjustDto payAccountAdjustDto){
		//判断借贷方向
		ActAccountDto dr = actAccountDtoMapper.selectByAccountId(payAccountAdjustDto.getDrAccountId());
		ActAccountDto cr = actAccountDtoMapper.selectByAccountId(payAccountAdjustDto.getCrAccountId());
		if(dr==null || cr==null){
			throw new CbsUncheckedException(ECbsErrorCode.ACCOUNT_USER_ERROR.getValue(), ECbsErrorCode.ACCOUNT_USER_ERROR.getDisplayName());
		}
		payAccountAdjustDto.setDrAccountName(dr.getCustName());
		payAccountAdjustDto.setCrAccountName(cr.getCustName());
		String drValue = EAccountDrCr.DR.getValue();
		String crValue = EAccountDrCr.CR.getValue();
		String drFlag = dr.getDrCrFlag().getValue();
		String crFlag = cr.getDrCrFlag().getValue();
		if(drValue.equals(drFlag) && crValue.equals(crFlag)){
			//一个借方一个贷方，DR付款
			payAccountAdjustDto.setPayerCustId(dr.getCustId());
			payAccountAdjustDto.setPayerCustName(dr.getCustName());
			payAccountAdjustDto.setPayeeCustId(cr.getCustId());
			payAccountAdjustDto.setPayeeCustName(cr.getCustName());
		}
		if(!drValue.equals(drFlag) && !crValue.equals(crFlag)){
			//一个贷方一个借方，dr付款
			payAccountAdjustDto.setPayerCustId(dr.getCustId());
			payAccountAdjustDto.setPayerCustName(dr.getCustName());
			payAccountAdjustDto.setPayeeCustId(cr.getCustId());
			payAccountAdjustDto.setPayeeCustName(cr.getCustName());
		}
		if(!drValue.equals(drFlag) && crValue.equals(crFlag)){
			//两个贷方，DR付款
			payAccountAdjustDto.setPayerCustId(dr.getCustId());
			payAccountAdjustDto.setPayerCustName(dr.getCustName());
			payAccountAdjustDto.setPayeeCustId(cr.getCustId());
			payAccountAdjustDto.setPayeeCustName(cr.getCustName());
		}
		if(drValue.equals(drFlag) && !crValue.equals(crFlag)){
			//两个借方，CR付款方
			payAccountAdjustDto.setPayerCustId(cr.getCustId());
			payAccountAdjustDto.setPayerCustName(cr.getCustName());
			payAccountAdjustDto.setPayeeCustId(dr.getCustId());
			payAccountAdjustDto.setPayeeCustName(dr.getCustName());
		}
		try {
			paySystemPayAccountAdjustAppService.save(payAccountAdjustDto);
		} catch (PayCheckedException e) {
			_log.error("新增账户调账："+ECbsErrorCode.ACCOUNT_DATE_ERROR.getDisplayName());
			throw new CbsUncheckedException(ECbsErrorCode.PAY_SYS_ERROR.getValue(), ECbsErrorCode.PAY_SYS_ERROR.getDisplayName());
		}
	}

	@Override
	public void auditPass(PayAccountAdjustDto payAccountAdjustDto){
		ResultCodeDto<PayAccountAdjustDto> auditPass =null;
		try {
			if(EAuditResult.AUDIT_PASS.getValue().equals(payAccountAdjustDto.getAuditResult().getValue())){
				//调用审核通过接口
				auditPass = paySystemPayAccountAdjustAppService.auditPass(payAccountAdjustDto.getId(), payAccountAdjustDto.getAuditLoginName(), payAccountAdjustDto.getAuditRealName(), payAccountAdjustDto.getAuditRemark());
			}
			if(EAuditResult.AUDIT_REJECT.getValue().equals(payAccountAdjustDto.getAuditResult().getValue())){
				//调用审核拒绝接口
				auditPass = paySystemPayAccountAdjustAppService.auditReject(payAccountAdjustDto.getId(), payAccountAdjustDto.getAuditLoginName(), payAccountAdjustDto.getAuditRealName(), payAccountAdjustDto.getAuditRemark());
			}
		} catch (PayCheckedException e) {
			_log.error("账户调账审核："+ECbsErrorCode.PAY_SYS_ERROR.getDisplayName());
			throw new CbsUncheckedException(e.getCode(),ECbsErrorCode.PAY_SYS_ERROR.getDisplayName());
		} catch (Exception e) {
			_log.error("账户调账审核："+ECbsErrorCode.PAY_SYS_ERROR.getDisplayName());
			throw new CbsUncheckedException(ECbsErrorCode.PAY_SYS_ERROR.getValue(), "调用支付系统超时");
		}
		if(!EResultCode.SUCCESS.getValue().equals(auditPass.getResultCode().getValue())){
			_log.error("账户调账审核："+auditPass.getResultCode().getDisplayName());
			throw new CbsUncheckedException(auditPass.getResultCode().getValue(), auditPass.getMsgDetail());
		}
	}

	@Override
	public PageData<PayAccountAdjustDto> auditPageList(
			PayAccountAdjustDto payAccountAdjustDto,PageData<PayAccountAdjustDto> pageData)  {
		PageHelper.startPage(pageData.getPageNumber(), pageData.getPageSize());
		//查询审核列表或者审核历史列表
		List<PayAccountAdjustDto> pageList=null;
		if(payAccountAdjustDto.getAuditResult()!=null){
			pageList=payAccountAdjustDtoMapper.pageList(payAccountAdjustDto);
		}else{
			pageList=payAccountAdjustDtoMapper.pageResultList(payAccountAdjustDto,EAuditResult.AUDIT_PASS,EAuditResult.AUDIT_REJECT);
			for (PayAccountAdjustDto dto : pageList) {
				String operReason = dto.getOperReason();
				if(operReason!=null && operReason.length()>10){
					operReason=operReason.substring(0,10)+"...";
				}
				dto.setOperReason(operReason);
			}
		}
		Page<PayAccountAdjustDto> page=(Page<PayAccountAdjustDto>) pageList;
		/*//循环获取借方账户名称和贷方账户名称
		for (PayAccountAdjustDto dto : pageList) {
			ActAccountDto dr = actAccountDtoMapper.selectByAccountId(dto.getDrAccountId());//需要优化
			if(dr.getCustId().equals(dto.getPayeeCustId())){
				dto.setDrAccountName(dto.getPayeeCustName());
				dto.setCrAccountName(dto.getPayerCustName());
			}else{
				dto.setDrAccountName(dto.getPayerCustName());
				dto.setCrAccountName(dto.getPayeeCustName());
			}
		}*/
		pageData.setRows(pageList);
		pageData.setTotal(page.getTotal());
		return pageData;
	}

	@Override
	public PayAccountAdjustDto auditDetails(String id)  {
		//PayAccountAdjustDto dto = 
		/*ActAccountDto dr = actAccountDtoMapper.selectByAccountId(dto.getDrAccountId());*/
		/*if(dr.getCustId().equals(dto.getPayeeCustId())){
			dto.setDrAccountName(dto.getPayeeCustName());
			dto.setCrAccountName(dto.getPayerCustName());
		}else{
			dto.setDrAccountName(dto.getPayerCustName());
			dto.setCrAccountName(dto.getPayeeCustName());
		}*/
		return payAccountAdjustDtoMapper.selectByPrimaryKey(id);
	}
	
	@Override
	public Integer queryAdjustCountByAdjust() {
		return payAccountAdjustDtoMapper.queryAdjustCountByAdjust();
	}
	@Override
	public Integer queryAdjustCountByRecover() {
		return payAccountAdjustDtoMapper.queryAdjustCountByRecover();
	}
 
	@Override
	public ExportDto export(PayAccountAdjustDto queryParam) {
		return payAccountAdjustDtoMapper.export(queryParam,EAuditResult.AUDIT_PASS);
	}

	@Override
	public PageData<PayAccountAdjustDto> list(PageData<PayAccountAdjustDto> pageData,
			PayAccountAdjustDto queryParam) {
		PageHelper.startPage(pageData.getPageNumber(), pageData.getPageSize());
		List<PayAccountAdjustDto> pageList=payAccountAdjustDtoMapper.pageList(queryParam);
		Page<PayAccountAdjustDto> page = (Page<PayAccountAdjustDto>) pageList;
		pageData.setTotal(page.getTotal());
		pageData.setRows(pageList);
		return pageData;
	}

	@Override
	public PayAccountAdjustDto getById(String id) {
		return payAccountAdjustDtoMapper.getById(id);
	}
}
