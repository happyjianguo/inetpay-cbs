package com.ylink.inetpay.cbs.mrs.App;

import com.ylink.inetpay.common.core.constant.*;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;
import org.ylinkpay.framework.core.util.UuidUtil;

import com.ylink.inetpay.cbs.act.service.ActaccountDateService;
import com.ylink.inetpay.cbs.mrs.service.MrsBankBusiDtoService;
import com.ylink.inetpay.cbs.mrs.service.MrsWithdrawAuditService;
import com.ylink.inetpay.common.core.dto.ResultCodeDto;
import com.ylink.inetpay.common.core.util.AmountUtil;
import com.ylink.inetpay.common.project.cbs.app.MrsWithdrawAuditAppService;
import com.ylink.inetpay.common.project.cbs.constant.mrs.AduitBusiType;
import com.ylink.inetpay.common.project.cbs.constant.mrs.BankAduitType;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsBankBusiDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsPortalReviewAduitDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsWithdrawAduitDto;
import com.ylink.inetpay.common.project.pay.app.PayWithdrawAppService;
import com.ylink.inetpay.common.project.pay.app.RiskControlAppService;
import com.ylink.inetpay.common.project.pay.dto.PayWithdrawDto;
import com.ylink.inetpay.common.project.pay.pojo.RiskParams;
import com.ylink.inetpay.common.project.pay.pojo.WithdrawApplyPojo;
import com.ylink.inetpay.common.project.portal.vo.RespCheckVO;
import com.ylink.inetpay.common.project.portal.vo.bank.AduitVo;
import com.ylink.inetpay.common.project.portal.vo.bank.WithdrawVo;

@Service("mrsWithdrawAuditAppService")
public class MrsWithdrawAuditAppServiceImpl implements MrsWithdrawAuditAppService{
	
	/**
	 * 日志
	 */
	private static Logger _log = LoggerFactory.getLogger(MrsWithdrawAuditAppServiceImpl.class);
	@Autowired
	MrsWithdrawAuditService mrsWithdrawAuditService;
	@Autowired
	PayWithdrawAppService payWithdrawAppService;
	@Autowired
	MrsBankBusiDtoService mrsBankBusiDtoService;
	@Autowired
	RiskControlAppService riskControlAppService;
	@Autowired
	ActaccountDateService actaccountDateService;
	
	@Override
	public PageData<MrsWithdrawAduitDto> queryAllData(PageData<MrsWithdrawAduitDto> pageDate,
			MrsWithdrawAduitDto mrsWithdrawAduitDto) {
		return mrsWithdrawAuditService.queryAllData(pageDate, mrsWithdrawAduitDto);
	}
	@Override
	public MrsWithdrawAduitDto findDtoById(String id) {
		return mrsWithdrawAuditService.findDtoById(id);
	}
	@Override
	public RespCheckVO aduit(AduitVo vo, String name, String userId) {
		return mrsWithdrawAuditService.aduit(vo, name, userId);
	}
	@Override
	public ResultCodeDto<PayWithdrawDto> doWithdrawApply(WithdrawVo withdrawVo, EBindCardTypeCode bindCardTypeCode) {
		 ResultCodeDto<PayWithdrawDto> resultCode=new ResultCodeDto<>();
		//根据银行表主键获取银行信息
		if(StringUtils.isBlank(withdrawVo.getBankBusiId())){
			_log.error("提现请求参数为空！");
            resultCode.setResultCode(EResultCode.FAIL);
            resultCode.setMsgDetail("提现请求参数为空");
            return resultCode;
		}
		MrsBankBusiDto mrsBankBusiDto=mrsBankBusiDtoService.findById(withdrawVo.getBankBusiId());
		if(mrsBankBusiDto==null){
			_log.error("根据银行卡ID={}，没有找到对应数据",withdrawVo.getBankBusiId());
            resultCode.setResultCode(EResultCode.FAIL);
            resultCode.setMsgDetail("根据银行卡ID="+withdrawVo.getBankBusiId()+"，没有找到对应数据");
            return resultCode;
		}
		WithdrawApplyPojo applyPojo = new WithdrawApplyPojo();
		BeanUtils.copyProperties(withdrawVo, applyPojo);
		applyPojo.setMerOrderId(UuidUtil.getUUID());
		applyPojo.setCurrenoy(ECurrenoy.RMB);
		applyPojo.setOrderAmt(AmountUtil.yuanToFen(withdrawVo.getWithdrawAmt()));
		//银行卡对应数据
		applyPojo.setBankType(mrsBankBusiDto.getBankType());
		if(ECustType.PERSON.equals(ECustType.getEnum(mrsBankBusiDto.getAccountType()))){
			applyPojo.setPubPriv(EPubOrPriv.PRIV);
		} else {
			applyPojo.setPubPriv(EPubOrPriv.PUB);
		}
		applyPojo.setCustName(mrsBankBusiDto.getCustName());
		applyPojo.setProvince(mrsBankBusiDto.getProvinceCode());
		applyPojo.setCity(mrsBankBusiDto.getCityCode());
		applyPojo.setCentralBk(mrsBankBusiDto.getBankCode());
		applyPojo.setPayeeBankCustName(mrsBankBusiDto.getAccName());
		applyPojo.setPayeeBankCardNo(mrsBankBusiDto.getAccNo());
		return payWithdrawAppService.withdrawApply(applyPojo, bindCardTypeCode);
	}
	@Override
	public ResultCodeDto<PayWithdrawDto> saveWithdrawAudit(WithdrawVo withdrawVo, EBindCardTypeCode bindCardTypeCode) {
		MrsWithdrawAduitDto mrsWithdrawAduitDto = new MrsWithdrawAduitDto();
		MrsPortalReviewAduitDto mrsPortalReviewAduitDto = new MrsPortalReviewAduitDto();
		ResultCodeDto<PayWithdrawDto> resultCode = new ResultCodeDto<>();
		MrsBankBusiDto mrsBankBusiDto = mrsBankBusiDtoService.findById(withdrawVo.getBankBusiId());
		if (mrsBankBusiDto == null) {
			_log.error("根据银行卡ID={}，没有找到对应数据", withdrawVo.getBankBusiId());
			resultCode.setResultCode(EResultCode.FAIL);
			resultCode.setMsgDetail("根据银行卡ID=" + withdrawVo.getBankBusiId() + "，没有找到对应数据");
			return resultCode;
		}
		mrsWithdrawAduitDto.setBankId(withdrawVo.getBankBusiId());
		mrsWithdrawAduitDto.setCustId(withdrawVo.getCustId());
		mrsWithdrawAduitDto.setBankType(mrsBankBusiDto.getBankType());
		mrsWithdrawAduitDto.setBankName(mrsBankBusiDto.getBankName());
		mrsWithdrawAduitDto.setCertNo(mrsBankBusiDto.getAccNo());
		mrsWithdrawAduitDto.setAmount(AmountUtil.yuanToFen(withdrawVo.getWithdrawAmt()));
		mrsWithdrawAduitDto.setAcctType(withdrawVo.getActBusiType());
		mrsWithdrawAduitDto.setAcctNo(withdrawVo.getAcctNo());
		mrsWithdrawAduitDto.setAduitStatus(BankAduitType.WAIT);
		mrsWithdrawAduitDto.setWithdrawStatus(EOrderStatus.ORDER_STATUS_WAITING_AUDIT);
		//审核表
		mrsPortalReviewAduitDto.setCreateRemark(withdrawVo.getRemark());
		mrsPortalReviewAduitDto.setCustId(withdrawVo.getCustId());
		mrsPortalReviewAduitDto.setBusiType(AduitBusiType.AB1.getValue());
		mrsPortalReviewAduitDto.setCreateUserName(withdrawVo.getLoginUserAlias());
		mrsPortalReviewAduitDto.setCreateUserNo(withdrawVo.getLoginUserId());
		mrsPortalReviewAduitDto.setAduitStatus(BankAduitType.WAIT.getValue());
		
		RespCheckVO respCheckVO = mrsWithdrawAuditService.saveWithdrawAudit(mrsWithdrawAduitDto,mrsPortalReviewAduitDto);
		
		if (respCheckVO.isCheckValue()) {
			// 返回成功。
			return new ResultCodeDto<>(EResultCode.SUCCESS, EResultCode.SUCCESS.getDisplayName());
		} else {
			_log.error("保存提现审核数据处理失败");
			resultCode.setResultCode(EResultCode.FAIL);
			resultCode.setMsgDetail("保存提现审核数据处理失败");
			return resultCode;
		}
	}
	@Override
	public RiskParams riskControlQuery(String custId, String acctBusiNo, EPayLimitBusinessType busiType) {
		return riskControlAppService.riskControlQuery(custId, acctBusiNo, busiType, actaccountDateService.getAccountDate());
	}
}
