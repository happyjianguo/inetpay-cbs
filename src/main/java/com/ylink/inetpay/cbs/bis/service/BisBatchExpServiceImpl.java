package com.ylink.inetpay.cbs.bis.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.ylinkpay.framework.core.model.PageData;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.ylink.eu.util.tools.DateUtil;
import com.ylink.inetpay.cbs.bis.dao.BisAccountFrozenAuditDtoMapper;
import com.ylink.inetpay.cbs.bis.dao.BisActCustRateAuditDtoMapper;
import com.ylink.inetpay.cbs.bis.dao.BisBatchExpMapper;
import com.ylink.inetpay.cbs.bis.dao.BisEcternalPaymentsMapper;
import com.ylink.inetpay.cbs.bis.dao.BisSetCashfundMapper;
import com.ylink.inetpay.cbs.bis.dao.BisTransferHandleDao;
import com.ylink.inetpay.cbs.common.CbsConstants;
import com.ylink.inetpay.common.core.constant.BISAuditStatus;
import com.ylink.inetpay.common.core.constant.EBatchBusiType;
import com.ylink.inetpay.common.core.constant.EBatchHandleStatus;
import com.ylink.inetpay.common.core.constant.EPayAdjustType;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisAccountFrozenAuditDto;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisActCustRateAuditDto;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisBatchExp;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisEcternalPayments;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisSetCashfund;
import com.ylink.inetpay.common.project.cbs.exception.CbsCheckedException;
import com.ylink.inetpay.common.project.cbs.exception.CbsUncheckedException;
@Transactional(value=CbsConstants.TX_MANAGER_BIS)
@Service("bisBatchExpService")
public class BisBatchExpServiceImpl implements BisBatchExpService {
	@Autowired
	private BisBatchExpMapper bisBatchExpMapper;
	@Autowired
	private BisSetCashfundMapper bisSetCashfundMapper;
	@Autowired
	private BisAccountFrozenAuditDtoMapper bisAccountFrozenAuditDtoMapper;
	@Autowired
	private BisActCustRateAuditDtoMapper bisActCustRateAuditDtoMapper;
	@Autowired
	private BisEcternalPaymentsMapper bisEcternalPaymentsMapper;
	@Autowired
	private BisTransferHandleDao bisTransferHandleDao;
	@Autowired
	private BisActCustRateAuditService bisActCustRateAuditService;
	@Override
	public PageData<BisBatchExp> findPageList(PageData<BisBatchExp> pageData, BisBatchExp queryParam) {
		PageHelper.startPage(pageData.getPageNumber(), pageData.getPageSize());
		List<BisBatchExp> list = bisBatchExpMapper.list(queryParam);
		Page<BisBatchExp> page = (Page<BisBatchExp>) list;
		pageData.setTotal(page.getTotal());
		pageData.setRows(list);
		return pageData;
	}
	@Override
	public long batchCashFundExp(List<BisSetCashfund> batchDtos, BisBatchExp bisBatchExp) throws CbsCheckedException {
		getBisBatchExp(bisBatchExp);
		/*for (BisSetCashfund bisSetCashfund : batchDtos) {
			bisSetCashfund.setId(UUID.randomUUID().toString());
			bisSetCashfund.setEffectiveStatus(EffectiveStatusEnum.WAIT_EFFECT);
			bisSetCashfund.setAuditStatus(BISAuditStatus.WAIT_AUDIT);
			bisSetCashfund.setCreateDatetime(new Date());
			bisSetCashfund.setProcessStatus(EPocessStatusEnum.WAIT_COMPLATE);
			bisSetCashfund.setBatchNo(bisBatchExp.getBatchNo());
			bisSetCashfund.setAppleType(EAppleType.BATCH);
			bisSetCashfund.setActaulSetAmount(0l);
		}*/
		//批量保存保证金设置明细
		return bisSetCashfundMapper.batchCashFundExp(batchDtos);
	}
	@Override
	public long batchAccountFrozenExp(List<BisAccountFrozenAuditDto> batchDtos, BisBatchExp bisBatchExp)
			throws CbsCheckedException {
		getBisBatchExp(bisBatchExp);
		for (BisAccountFrozenAuditDto bisAccountFrozenAuditDto : batchDtos) {
			long seq = bisAccountFrozenAuditDtoMapper.getSequence();
			String frozenId=new SimpleDateFormat("yyyyMMdd").format(new Date())+String.valueOf(seq);
			bisAccountFrozenAuditDto.setId(frozenId);
			/*bisAccountFrozenAuditDto.setFrozenStatus(EFrozenStatus.FROZEN_APPLY);
			bisAccountFrozenAuditDto.setFrozenAuditStatus(BISAuditStatus.WAIT_AUDIT);
			bisAccountFrozenAuditDto.setAppleType(EAppleType.BATCH);
			bisAccountFrozenAuditDto.setBatchNo(bisBatchExp.getBatchNo());
			bisAccountFrozenAuditDto.setActualAmt(0l);
			bisAccountFrozenAuditDto.setActualUnfrozenAmt(0L);
			bisAccountFrozenAuditDto.setUnfrozenAmt(0L);*/
		}
		return bisAccountFrozenAuditDtoMapper.batchAccountFrozenExp(batchDtos);
	}
	@Override
	public long batchCustRateExp(List<BisActCustRateAuditDto> batchDtos, BisBatchExp bisBatchExp)
			throws CbsCheckedException {
		getBisBatchExp(bisBatchExp);
		/*for (BisActCustRateAuditDto bisActCustRateAuditDto : batchDtos) {
			bisActCustRateAuditDto.setId(UUID.randomUUID().toString());
			bisActCustRateAuditDto.setStatus(EActCustRateStatus.UNEFFECTIVE);
			bisActCustRateAuditDto.setCreateTime(new Date());
			bisActCustRateAuditDto.setRateType(ERateType.CUST_RATE);
			bisActCustRateAuditDto.setOperateType(EOperateType.NEW);
			bisActCustRateAuditDto.setAuditStatus(BISAuditStatus.WAIT_AUDIT);
			bisActCustRateAuditDto.setOperateTime(new Date());
			bisActCustRateAuditDto.setBatchNo(bisBatchExp.getBatchNo());
			bisActCustRateAuditDto.setAppleType(EAppleType.BATCH);
		}*/
		return bisActCustRateAuditDtoMapper.batchCustRateExp(batchDtos);
	}
	@Override
	public long batchEcternalPayExp(List<BisEcternalPayments> batchDtos, BisBatchExp bisBatchExp)
			throws CbsCheckedException {
		getBisBatchExp(bisBatchExp);
		for (BisEcternalPayments bisEcternalPayments : batchDtos) {
			/*bisEcternalPayments.setId(UUID.randomUUID().toString());
			bisEcternalPayments.setInnerAccountType(EPubOrPriv.PRIV);
			bisEcternalPayments.setCurrency(ECurrenoy.RMB);
			bisEcternalPayments.setnCheckNum(1l);
			bisEcternalPayments.setaCheckNum(0l);
			bisEcternalPayments.setHandleTime(new Date());
			bisEcternalPayments.setCheckStatus(BISAuditStatus.WAIT_AUDIT);
			bisEcternalPayments.setAllocateType(EAllocateType.PT_OUT_PAY);
			bisEcternalPayments.setAppleType(EAppleType.BATCH);
			bisEcternalPayments.setBatchNo(bisBatchExp.getBatchNo());*/
			bisEcternalPayments.setBatch(DateUtil.getYearMonthDay$(new Date())+EPayAdjustType.PT_OUT_PAY.getValue() + bisTransferHandleDao.getSeqenceVals());
		}
		return bisEcternalPaymentsMapper.batchEcternalPayExp(batchDtos);
	}
	
	public void getBisBatchExp(BisBatchExp bisBatchExp) {
		//获取序号
		/*bisBatchExp.setId(UUID.randomUUID().toString());
		bisBatchExp.setBatchNo(DateUtils.dateToyyMMdd(new Date())+bisBatchExpMapper.getBatchNo());
		bisBatchExp.setExpTime(new Date());
		bisBatchExp.setFailNum(0);
		bisBatchExp.setSuccessNum(0);
		bisBatchExp.setTotalNum(0);
		bisBatchExp.setTotalAmt(new BigDecimal("0"));
		bisBatchExp.setCheckStatus(BISAuditStatus.WAIT_AUDIT);
		bisBatchExp.setHandleStatus(EBatchHandleStatus.WAITPAY);*/
		bisBatchExpMapper.insert(bisBatchExp);
	}
	
	/**
	 * 通过批次号获取批次记录
	 */
	@Override
	public BisBatchExp findListByBatchNo(String batchNo) {
		return bisBatchExpMapper.selectByBatchNo(batchNo);
	}
	
	/**
	 * 根据批次号修改批次和批次明细的复核状态
	 */
	@Override
	public long batchExpAudit(String batchNo, BISAuditStatus auditStatus,EBatchBusiType busiType,String auditor,String auditorName,String checkReason) {
		//修改批次的复核状态
	    bisBatchExpMapper.updateAuditStatusByBatchNo(batchNo,auditStatus,auditor,auditorName,new Date(),checkReason);
		//修改批次明细的复核记录复核状态
		if(EBatchBusiType.BATCH_CASHFUND==busiType){
			return bisSetCashfundMapper.updateAuditStatusByBatchNo(batchNo,auditStatus);
		}else if(EBatchBusiType.BATCH_FROZEN==busiType){
			return bisAccountFrozenAuditDtoMapper.updateAuditStatusByBatchNo(batchNo,auditStatus);
		}else if(EBatchBusiType.BATCH_PAY== busiType){
			return bisEcternalPaymentsMapper.updateAuditStatusByBatchNo(batchNo,auditStatus);
		}else if(EBatchBusiType.BATCH_RATE== busiType){
			//需改客户利率生效状态
			bisActCustRateAuditService.custRateEffective(batchNo);
			return bisActCustRateAuditDtoMapper.updateAuditStatusByBatchNo(batchNo,auditStatus);
		}else{
			throw new CbsUncheckedException("","暂时不支持此类型业务");
		}
	}
	
	/**
	 * 获取批次记录详情
	 */
	@Override
	public BisBatchExp batchExpView(String id) {
		return bisBatchExpMapper.selectByPrimaryKey(id);
	}
	
	@Override
	public void updateBatchExpNum(long successNum, long failNum, String batchNo) {
			BisBatchExp dto=bisBatchExpMapper.selectByBatchNo(batchNo);
			if(successNum!=0){
				dto.setSuccessNum(dto.getSuccessNum()+successNum);
			}
			if(failNum!=0){
				dto.setFailNum(dto.getFailNum()+failNum);
			}
			if(dto.getTotalNum()==dto.getSuccessNum()){
				dto.setHandleStatus(EBatchHandleStatus.ALL_SUCCESS);
			}else if(dto.getTotalNum()==dto.getFailNum()){
				dto.setHandleStatus(EBatchHandleStatus.ALL_FAIL);
			}else{
				dto.setHandleStatus(EBatchHandleStatus.PART_SUCCESS);
			}
			bisBatchExpMapper.updateByPrimaryKeySelective(dto);
	}
	@Override
	public String getBatchNo() {
		return bisBatchExpMapper.getBatchNo();
	}
	@Override
	public boolean isExistFile(String expFileName, EBatchBusiType busiType) {
		long num=bisBatchExpMapper.isExistFile(expFileName,busiType);
		return num>0;
	}
	
}
