package com.ylink.inetpay.cbs.mrs.service;

import java.util.Date;
import java.util.List;

import com.ylink.inetpay.common.core.constant.*;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.ylinkpay.framework.core.model.PageData;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.ylink.inetpay.cbs.chl.service.ChlBankService;
import com.ylink.inetpay.cbs.common.CbsConstants;
import com.ylink.inetpay.cbs.mrs.dao.MrsPortalReviewAduitDtoMapper;
import com.ylink.inetpay.cbs.mrs.dao.MrsWithdrawAduitDtoMapper;
import com.ylink.inetpay.common.core.dto.ResultCodeDto;
import com.ylink.inetpay.common.core.util.DateUtils;
import com.ylink.inetpay.common.project.cbs.constant.mrs.AduitBusiType;
import com.ylink.inetpay.common.project.cbs.constant.mrs.BankAduitType;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsBankBusiDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsPortalReviewAduitDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsRechargeAduitDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsWithdrawAduitDto;
import com.ylink.inetpay.common.project.channel.dto.TbChlBank;
import com.ylink.inetpay.common.project.pay.app.PayWithdrawAppService;
import com.ylink.inetpay.common.project.pay.dto.PayWithdrawDto;
import com.ylink.inetpay.common.project.pay.pojo.WithdrawApplyPojo;
import com.ylink.inetpay.common.project.portal.enums.ErrorMsgEnum;
import com.ylink.inetpay.common.project.portal.vo.RespCheckVO;
import com.ylink.inetpay.common.project.portal.vo.bank.AduitVo;

@Service("mrsWithdrawAuditService")
public class MrsWithdrawAuditServiceimpl implements MrsWithdrawAuditService {
	private static Logger log = LoggerFactory.getLogger(MrsWithdrawAuditServiceimpl.class);
	@Autowired
	private MrsWithdrawAduitDtoMapper mrsWithdrawAduitDtoMapper;
	@Autowired
	private MrsPortalReviewAduitDtoMapper mrsPortalReviewAduitDtoMapper;
	@Autowired
	MrsBankBusiDtoService mrsBankBusiDtoService;
	@Autowired
	PayWithdrawAppService payWithdrawAppService;
	@Autowired
	ChlBankService chlBankService;

	@Override
	public PageData<MrsWithdrawAduitDto> queryAllData(PageData<MrsWithdrawAduitDto> pageDate,
			MrsWithdrawAduitDto mrsWithdrawAduitDto) {
		PageHelper.startPage(pageDate.getPageNumber(), pageDate.getPageSize());
		List<MrsWithdrawAduitDto> list = mrsWithdrawAduitDtoMapper.queryAllData(mrsWithdrawAduitDto);
		List<TbChlBank> banks = chlBankService.getBanks();
		for (int i = 0; i < list.size(); i++) {
			String bankType = list.get(i).getBankType();
			for (int j = 0; j < banks.size(); j++) {
				if (bankType.equals(banks.get(j).getBankType())) {
					MrsWithdrawAduitDto mrsWithdrawAduit = list.get(i);
					mrsWithdrawAduit.setBankNameZ(banks.get(j).getBankName());
					list.set(i,mrsWithdrawAduit);
				}
				
			}
			
		}
		Page<MrsWithdrawAduitDto> page = (Page<MrsWithdrawAduitDto>) list;
		pageDate.setTotal(page.getTotal());
		pageDate.setRows(list);
		return pageDate;
	}

	@Override
	public MrsWithdrawAduitDto findDtoById(String id) {
		return mrsWithdrawAduitDtoMapper.selectByPrimaryKey(id);
	}

	@Override
	@Transactional(value = CbsConstants.TX_MANAGER_MRS)
	public RespCheckVO aduit(AduitVo vo, String name, String userId) {
		RespCheckVO respVo = new RespCheckVO(true);
		try {
			// 复核失败
			if (BankAduitType.FAIl.getValue().equals(vo.getAduitStatus())) {
				MrsWithdrawAduitDto mrsWithdrawAduitDto = mrsWithdrawAduitDtoMapper.selectByPrimaryKey(vo.getId());
				mrsWithdrawAduitDto.setAduitStatus(BankAduitType.getEnum(vo.getAduitStatus()));
				mrsWithdrawAduitDtoMapper.updateByPrimaryKeySelective(mrsWithdrawAduitDto);
				MrsPortalReviewAduitDto aduitDto = mrsPortalReviewAduitDtoMapper.selectByBusiNoAndType(vo.getId(),
						AduitBusiType.AB1.getValue());
				aduitDto.setAduitStatus(vo.getAduitStatus());
				aduitDto.setAduitRemark(vo.getAduitRemark());
				aduitDto.setAduitUserId(userId);
				aduitDto.setAduitUserName(name);
				aduitDto.setAduitTime(new Date());
				mrsPortalReviewAduitDtoMapper.updateByPrimaryKeySelective(aduitDto);
				return respVo;
			}
			// 复核成功
			else {
				MrsWithdrawAduitDto mrsWithdrawAduitDto = mrsWithdrawAduitDtoMapper.selectByPrimaryKey(vo.getId());
				mrsWithdrawAduitDto.setAduitStatus(BankAduitType.getEnum(vo.getAduitStatus()));
				
				MrsPortalReviewAduitDto aduitDto = mrsPortalReviewAduitDtoMapper.selectByBusiNoAndType(vo.getId(),
						AduitBusiType.AB1.getValue());
				aduitDto.setAduitStatus(vo.getAduitStatus());
				aduitDto.setAduitRemark(vo.getAduitRemark());
				aduitDto.setAduitUserId(userId);
				aduitDto.setAduitUserName(name);
				aduitDto.setAduitTime(new Date());
				mrsPortalReviewAduitDtoMapper.updateByPrimaryKeySelective(aduitDto);

				// 审核通过后调用提现接口
				// 根据银行表主键获取银行信息
				MrsBankBusiDto mrsBankBusiDto = mrsBankBusiDtoService.findById(mrsWithdrawAduitDto.getBankId());
				if (mrsBankBusiDto == null) {
					log.error("根据银行卡ID={}，没有找到对应数据", mrsWithdrawAduitDto.getBankId());
					throw new Exception("根据银行卡ID=" + mrsWithdrawAduitDto.getBankId() + "，没有找到对应数据");
				}
				WithdrawApplyPojo applyPojo = new WithdrawApplyPojo();
				applyPojo.setMerOrderId(mrsWithdrawAduitDto.getId());
				applyPojo.setCustId(aduitDto.getCustId());
				applyPojo.setCustName(mrsBankBusiDto.getCustName());
				applyPojo.setCurrenoy(ECurrenoy.RMB);
				applyPojo.setOrderAmt(mrsWithdrawAduitDto.getAmount());
				// 银行卡对应数据
				applyPojo.setBankType(mrsBankBusiDto.getBankType());
				if(ECustType.PERSON.equals(ECustType.getEnum(mrsBankBusiDto.getAccountType()))){
					applyPojo.setPubPriv(EPubOrPriv.PRIV);
				} else {
					applyPojo.setPubPriv(EPubOrPriv.PUB);
				}

				applyPojo.setBankName(mrsBankBusiDto.getBankName());
				applyPojo.setCustName(mrsBankBusiDto.getCustName());
				applyPojo.setActBusiType(mrsWithdrawAduitDto.getAcctType());
				applyPojo.setProvince(mrsBankBusiDto.getProvinceCode());
				applyPojo.setCity(mrsBankBusiDto.getCityCode());
				applyPojo.setCentralBk(mrsBankBusiDto.getBankCode());
				applyPojo.setPayeeBankCustName(mrsBankBusiDto.getAccName());
				applyPojo.setPayeeBankCardNo(mrsBankBusiDto.getAccNo());
				ResultCodeDto<PayWithdrawDto> resultCodeDto = payWithdrawAppService.withdrawApply(applyPojo,
						EBindCardTypeCode.CREDIT_BIND);
				
				if(resultCodeDto != null && resultCodeDto.getMsgDetail() != null){
					if(resultCodeDto.getMsgDetail().length() > 200) {
						mrsWithdrawAduitDto.setErrorRemark(resultCodeDto.getMsgDetail().substring(0, 200));
					} else {
						mrsWithdrawAduitDto.setErrorRemark(resultCodeDto.getMsgDetail());
					}
					
				}
				
				// 提现处理结果
				if (resultCodeDto != null && EResultCode.SUCCESS.equals(resultCodeDto.getResultCode())) {
					if(resultCodeDto.getObject() != null){
						mrsWithdrawAduitDto.setWithdrawId(resultCodeDto.getObject().getBusiId());
					}
					mrsWithdrawAduitDtoMapper.updateByPrimaryKeySelective(mrsWithdrawAduitDto);
					return respVo;
				} else {
					log.error("调用提现接口处理失败");
					throw new Exception("调用提现接口处理失败");
				}
				
			}
		} catch (Exception e) {
			log.error("审核提现信息处理失败", e);
			RespCheckVO resp = new RespCheckVO(false, ErrorMsgEnum.SYSTEM_ERROR);
			resp.setMsg("审核提现信息处理失败");
			return resp;
		}
	}

	@Override
	@Transactional(value = CbsConstants.TX_MANAGER_MRS)
	public RespCheckVO saveWithdrawAudit(MrsWithdrawAduitDto mrsWithdrawAduitDto,
			MrsPortalReviewAduitDto mrsPortalReviewAduitDto) {
		RespCheckVO respVo = new RespCheckVO(true);
		try {
			mrsWithdrawAduitDto.setId(getMrsAuditOrderNosVal());
			mrsWithdrawAduitDto.setCreateTime(new Date());
			mrsWithdrawAduitDtoMapper.insert(mrsWithdrawAduitDto);
			mrsPortalReviewAduitDto.setId(mrsPortalReviewAduitDto.getIdentity());
			mrsPortalReviewAduitDto.setCreateTime(new Date());
			mrsPortalReviewAduitDto.setBusiNo(mrsWithdrawAduitDto.getId());
			mrsPortalReviewAduitDtoMapper.insert(mrsPortalReviewAduitDto);
		} catch (Exception e) {
			log.error("保存提现审核数据处理失败", e);
			return new RespCheckVO(false, ErrorMsgEnum.SYSTEM_ERROR);
		}
		return respVo;
	}

	@Override
	public String getMrsAuditOrderNosVal() {
		return DateUtils.getCurrentDate()+mrsWithdrawAduitDtoMapper.getMrsAuditOrderNosVal();
	}

}
