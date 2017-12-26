package com.ylink.inetpay.cbs.bis.service;

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
import com.ylink.eu.util.tools.DateUtil;
import com.ylink.inetpay.cbs.bis.dao.BisAuditRuleMapper;
import com.ylink.inetpay.cbs.bis.dao.BisEcternalPaymentsMapper;
import com.ylink.inetpay.cbs.bis.dao.BisTransferHandleDao;
import com.ylink.inetpay.cbs.chl.dao.TbChlBankCodeMapper;
import com.ylink.inetpay.cbs.chl.service.CbsChlBankCodeService;
import com.ylink.inetpay.cbs.chl.service.ChlBankAccountService;
import com.ylink.inetpay.cbs.common.CbsConstants;
import com.ylink.inetpay.cbs.ucs.sec.service.UcsSecUserServiceImpl;
import com.ylink.inetpay.cbs.util.CbsExceptionLogDtoUtils;
import com.ylink.inetpay.common.core.constant.BISAuditStatus;
import com.ylink.inetpay.common.core.constant.BISAuditType;
import com.ylink.inetpay.common.core.constant.EAllocateType;
import com.ylink.inetpay.common.core.constant.EAppleType;
import com.ylink.inetpay.common.core.constant.EBisExceptionLogNlevel;
import com.ylink.inetpay.common.core.constant.EBisExceptionLogType;
import com.ylink.inetpay.common.core.constant.ECurrenoy;
import com.ylink.inetpay.common.core.constant.EIsNeedAuditEnum;
import com.ylink.inetpay.common.core.constant.EPayAdjustType;
import com.ylink.inetpay.common.core.constant.EResultCode;
import com.ylink.inetpay.common.core.constant.PayStatusEnum;
import com.ylink.inetpay.common.core.dto.ResultCode;
import com.ylink.inetpay.common.core.dto.ResultCodeDto;
import com.ylink.inetpay.common.core.util.ExceptionProcUtil;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisAreaCityDto;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisAuditDto;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisAuditRule;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisEcternalPayments;
import com.ylink.inetpay.common.project.cbs.dto.ucs.sec.UcsSecUserDto;
import com.ylink.inetpay.common.project.cbs.exception.CbsCheckedException;
import com.ylink.inetpay.common.project.cbs.exception.CbsUncheckedException;
import com.ylink.inetpay.common.project.channel.dto.TbChlBankAccount;
import com.ylink.inetpay.common.project.channel.dto.TbChlBankCode;
import com.ylink.inetpay.common.project.pay.app.PayOutPayAppService;
import com.ylink.inetpay.common.project.pay.dto.PayAmtAllocateDto;
import com.ylink.inetpay.common.project.pay.exception.PayUncheckedException;
import com.ylink.inetpay.common.project.pay.pojo.PtOutPayPojo;

@Service("bisEcternalPaymentsService")
@Transactional(value=CbsConstants.TX_MANAGER_BIS)
public class BisEcternalPaymentsServiceImpl implements BisEcternalPaymentsService {
	@Autowired
	private BisEcternalPaymentsMapper bisEcternalPaymentsMapper;
	@Autowired
	private ChlBankAccountService chlBankAccountService;
	@Autowired
	private BisTransferHandleDao bisTransferHandleDao;
	@Autowired
	BisAuditRuleMapper bisAuditRuleMapper;
	@Autowired
	private PayOutPayAppService payOutPayAppService;
	@Autowired
	private BisAuditService bisAuditService;
	@Autowired
	private TbChlBankCodeMapper tbChlBankCodeMapper;
	@Autowired
	private BisAreaCityService bisAreaCityService;
	@Autowired
	private CbsChlBankCodeService cbsChlBankCodeService;
	@Autowired
	private BisExceptionLogService bisExceptionLogService;
	@Autowired
	private BisBatchExpService bisBatchExpService;
	/*@Autowired
	private ChlBankService chlBankService;*/
	private static Logger _log = LoggerFactory.getLogger(UcsSecUserServiceImpl.class);
	@Override
	public void saveEcternalPayments(BisEcternalPayments bisEcternalPayments) throws CbsCheckedException {
		TbChlBankAccount tbChlBankAccount=chlBankAccountService.findByBankAccNo(bisEcternalPayments.getOuterAcct());
		if(tbChlBankAccount==null){
			throw new CbsCheckedException("cbs-0007","获取出金人信息失败");
		}
		List<TbChlBankCode> oBankCodeDto=tbChlBankCodeMapper.findBankNameByBankCode(tbChlBankAccount.getBankCode());
		if(oBankCodeDto.isEmpty()){
			throw new CbsCheckedException("cbs-0008","获取出金人银行失败");
		}
		List<TbChlBankCode> iBankCodeDto=tbChlBankCodeMapper.findBankNameByBankCode(bisEcternalPayments.getInnerAccountBankcode());
		if(iBankCodeDto.isEmpty()){
			throw new CbsCheckedException("cbs-0009","获取入金人银行失败");
		}
		bisEcternalPayments.setId(UUID.randomUUID().toString());
		bisEcternalPayments.setOuterSubject(tbChlBankAccount.getCustId());
		bisEcternalPayments.setOuterBankname(tbChlBankAccount.getBankAccName());
		bisEcternalPayments.setOuterBank(tbChlBankAccount.getBankType());
		bisEcternalPayments.setOuterAccountBankcode(tbChlBankAccount.getBankCode());
		bisEcternalPayments.setBankNameO(oBankCodeDto.get(0).getBankName());
		bisEcternalPayments.setBankNameI(iBankCodeDto.get(0).getBankName());
		bisEcternalPayments.setHandleTime(new Date());
		bisEcternalPayments.setAllocateType(EAllocateType.PT_OUT_PAY);
		//生成交易流水号
		bisEcternalPayments.setBatch(DateUtil.getYearMonthDay$(new Date())+EPayAdjustType.PT_OUT_PAY.getValue() + bisTransferHandleDao.getSeqenceVals());
		long num = getNum(bisEcternalPayments.getAmount(),BISAuditType.EXTERNAL);
		if(num==0){
			//如果复核次数为0，直接调用对外支付接口
			doPay(bisEcternalPayments);
			bisEcternalPayments.setCheckStatus(BISAuditStatus.AUDIT_PASS);
		}else{
			bisEcternalPayments.setCheckStatus(BISAuditStatus.WAIT_AUDIT);
			//支付状态为待支付
			bisEcternalPayments.setPayStatus(PayStatusEnum.WAITPAY);
		}
		bisEcternalPayments.setnCheckNum(num);
		bisEcternalPayments.setaCheckNum(0L);
		bisEcternalPayments.setAppleType(EAppleType.HAND_MADE);
		bisEcternalPaymentsMapper.insert(bisEcternalPayments);
	}
	
	/**
	 * 保存商户对外支付订单
	 * @param bisEcternalPayments
	 * @return
	 */
	@Override
	public ResultCode saveMerChantEcternalPayments(PayAmtAllocateDto payAmtAllocateDto) {
		try {
			//判断如果订单号已经存在，保存失败
			String busiId = payAmtAllocateDto.getBusiId();
			long num=bisEcternalPaymentsMapper.findBeanByBustId(busiId);
			if(num!=0){
				_log.error("保存商户对外支付复核记录失败：订单"+busiId+"复核记录已存在");
				return new ResultCode(EResultCode.SUCCESS, EIsNeedAuditEnum.NEED, "复核记录"+busiId+"已存在");
			}
			num=0;
			num = getNum(payAmtAllocateDto.getOrderAmt(),BISAuditType.MER_EXTERNAL);
			//getBisEcternalPayments(payAmtAllocateDto,num);
			if(num==0){
				//如果复核次数为0，直接调用对外支付接口
				return new ResultCode(EResultCode.SUCCESS, EIsNeedAuditEnum.NO_NEED, "复核次数为0");
			}else{
				//保存商户对外支付复核记录
				getBisEcternalPayments(payAmtAllocateDto,num);
				return new ResultCode(EResultCode.SUCCESS, EIsNeedAuditEnum.NEED, "复核次数为"+num);
			}
		} catch (Exception e) {
			_log.error("保存商户对外支付复核记录失败："+ExceptionProcUtil.getExceptionDesc(e));
			return new ResultCode(EResultCode.FAIL, EIsNeedAuditEnum.NEED, "保存商户对外支付复核记录失败");
		}
	}
	
	/**
	 * 将对外支付订单转换成对外支付审核记录
	 * @param payAmtDto
	 * @return
	 */
	public void getBisEcternalPayments(PayAmtAllocateDto payAmtDto,Long num){
		BisEcternalPayments bisEcternalPayments = new BisEcternalPayments();
		bisEcternalPayments.setId(UUID.randomUUID().toString());
		bisEcternalPayments.setInnerBank(payAmtDto.getPayeeBankType());
		bisEcternalPayments.setInnerAcc(payAmtDto.getPayeeBankCardNo());
		bisEcternalPayments.setInnerName(payAmtDto.getPayeeBankCustName());
		bisEcternalPayments.setInnerAccountId(payAmtDto.getPayeeAcctSub());
		bisEcternalPayments.setInnerAccountType(payAmtDto.getPubPriv());
		bisEcternalPayments.setInnerAccountCitycode(payAmtDto.getCity());
		bisEcternalPayments.setInnerAccountBankcode(payAmtDto.getCentralBk());
		bisEcternalPayments.setAmount(payAmtDto.getOrderAmt());
		bisEcternalPayments.setCurrency(ECurrenoy.RMB);
		bisEcternalPayments.setnCheckNum(num);
		bisEcternalPayments.setaCheckNum(0l);
		bisEcternalPayments.setMemo(payAmtDto.getRemark());
		List<TbChlBankCode> bankCodeDto = cbsChlBankCodeService.findBankNameByBankCode(payAmtDto.getCentralBk());
		if(bankCodeDto!=null && !bankCodeDto.isEmpty()){
			bisEcternalPayments.setBankNameI(bankCodeDto.get(0).getBankName());
		}
		//bisEcternalPayments.setBankNameO(payAmtDto.getPayeeBankTypeName());
		bisEcternalPayments.setAllocateType(EAllocateType.MER_OUT_PAY);
		bisEcternalPayments.setBustId(payAmtDto.getBusiId());
		bisEcternalPayments.setHandleTime(new Date());
		bisEcternalPayments.setProvince(payAmtDto.getProvince());
		BisAreaCityDto cityDto = bisAreaCityService.getByCode(payAmtDto.getCity());
		BisAreaCityDto provinceDto = bisAreaCityService.getByCode(payAmtDto.getProvince());
		if(cityDto!=null){
			bisEcternalPayments.setCityName(cityDto.getName());
		}
		if(provinceDto!=null){
			bisEcternalPayments.setProvinceName(provinceDto.getName());
		}
		bisEcternalPayments.setMerCode(payAmtDto.getMerCode());
		bisEcternalPayments.setMerName(payAmtDto.getMerName());
		bisEcternalPayments.setBatch(payAmtDto.getMerOrderId());
		if(num==0L){
			bisEcternalPayments.setAbstracts("无需复核，复核次数为【"+num+"】");
			bisEcternalPayments.setCheckStatus(BISAuditStatus.AUDIT_PASS);
			bisEcternalPayments.setPayStatus(PayStatusEnum.UNKNOWN);
		}else{
			bisEcternalPayments.setCheckStatus(BISAuditStatus.WAIT_AUDIT);
			bisEcternalPayments.setPayStatus(PayStatusEnum.WAITPAY);
		}
		//获取批次号 申请类型()
		String batchNo = payAmtDto.getBatchNo();
		if(!StringUtils.isBlank(batchNo)){
			bisEcternalPayments.setAppleType(EAppleType.BATCH);
			bisEcternalPayments.setBatchNo(batchNo);
		}else{
			bisEcternalPayments.setAppleType(EAppleType.HAND_MADE);
		}
		bisEcternalPaymentsMapper.insert(bisEcternalPayments);
	}
	
	public int getNum(long amount,BISAuditType auditType) {
		//需要复核次数
		BisAuditRule bisAuditRule = new BisAuditRule();
		bisAuditRule.setAuditType(auditType);
		List<BisAuditRule> bisAuditRuleList = bisAuditRuleMapper.queryBisAuditRule(bisAuditRule);
		if (bisAuditRuleList == null) {
			return 0;
		}
		for (BisAuditRule auditRuledto : bisAuditRuleList) {
			if (auditRuledto.getStartAudit() <= amount && auditRuledto.getEndAudit() > amount) {
				return auditRuledto.getCheckNum();
			}
		}
		return 0;
	}

	@Override
	public void updateEcternalPaymentsById(BisEcternalPayments bisEcternalPayments) throws CbsCheckedException {
		bisEcternalPaymentsMapper.updateEcternalPaymentsById(bisEcternalPayments);
	}

	@Override
	public void deleteEcternalPaymentsId(String id) throws CbsCheckedException {
		bisEcternalPaymentsMapper.deleteEcternalPaymentsId(id);
	}
	
	/**
	 * 查询对外支付列表
	 */
	@Override
	public PageData<BisEcternalPayments> findListPage(PageData<BisEcternalPayments> pageDate,
			BisEcternalPayments bisEcternalPayments) throws CbsCheckedException {
		BISAuditType auditType;
		if(EAllocateType.PT_OUT_PAY==bisEcternalPayments.getAllocateType()){
			auditType=BISAuditType.EXTERNAL;
		}else{
			auditType=BISAuditType.MER_EXTERNAL;
		}
		PageHelper.startPage(pageDate.getPageNumber(),pageDate.getPageSize());
		List<BisEcternalPayments> findListPage=bisEcternalPaymentsMapper.list(bisEcternalPayments);
		for (BisEcternalPayments dto : findListPage) {
			dto.setIsAudit(bisAuditService.isAudit(dto.getId(),bisEcternalPayments.getCurrentUserName(),auditType));
		}
		Page<BisEcternalPayments> page=(Page<BisEcternalPayments>) findListPage;
		pageDate.setTotal(page.getTotal());
		pageDate.setRows(findListPage);
		return pageDate;
	}

	@Override
	public void audit(String auditType, String id, UcsSecUserDto userDto,String reason) throws CbsCheckedException {
		BisEcternalPayments bisEcternalPayments=bisEcternalPaymentsMapper.selectByPrimaryKey(id);
		BISAuditType ecternalPayType;
		if(EAllocateType.PT_OUT_PAY==bisEcternalPayments.getAllocateType()){
			ecternalPayType=BISAuditType.EXTERNAL;
		}else{
			ecternalPayType=BISAuditType.MER_EXTERNAL;
		}
		//一个用户只能复核一次
		if(bisAuditService.isAudit(id,userDto.getLoginName(),ecternalPayType)){
			throw new CbsCheckedException("cbs-0002","您已参与复核，请邀请其他复核人员复核");
		}
		//经办人和复核人不能相同
		if(!EAllocateType.MER_OUT_PAY.equals(bisEcternalPayments.getAllocateType())){
			if(bisEcternalPayments.getHandle().equals(userDto.getLoginName())){
				throw new CbsCheckedException("cbs-0003","您，不能复核自己经办的对外支付");
			}
		}
		//根据id和用户编号判断用户是否已经复核过，如果复核过不允许用户再次复核
		if(bisEcternalPayments.getCheckStatus().equals(BISAuditStatus.AUDIT_REJECT.getValue())){
			throw new CbsCheckedException("cbs-0001","对外支付数据已经复核拒绝，不能再次复核");
		}
		Long nCheckNum = bisEcternalPayments.getnCheckNum();
		Long aCheckNum = bisEcternalPayments.getaCheckNum();
		if(aCheckNum.equals(nCheckNum)){
			throw new CbsCheckedException("cbs-0005","对外支付数,已经达到复核次数不能再次复核");
		}
		BISAuditStatus auditStatus;
		if("auditPass".equals(auditType)){
			if(nCheckNum.equals(aCheckNum+1)){
				doPay(bisEcternalPayments);
			}
			auditStatus=BISAuditStatus.AUDIT_PASS;
		}else{
			//商户对外支付需要调用支付的接口(异常和未知，支付状态未知)
			EAllocateType allocateType = bisEcternalPayments.getAllocateType();
			String message="";
			if(EAllocateType.MER_OUT_PAY==allocateType){
				try {
					ResultCodeDto<PayAmtAllocateDto> resultCodeDto = payOutPayAppService.auditFailCall(bisEcternalPayments.getBustId(), reason);
					if(resultCodeDto==null){
						bisEcternalPayments.setPayStatus(PayStatusEnum.UNKNOWN);
						message="调用支付系统返回状态为空";
					}if(EResultCode.FAIL==resultCodeDto.getResultCode()){
						throw new CbsCheckedException("cbs-0003",resultCodeDto.getMsgDetail());
					}else if(EResultCode.SUCCESS==resultCodeDto.getResultCode()){
						bisEcternalPayments.setPayStatus(PayStatusEnum.SUCCESS);
						message=resultCodeDto.getMsgDetail();
					}else{
						bisEcternalPayments.setPayStatus(PayStatusEnum.UNKNOWN);
						message=resultCodeDto.getMsgDetail();
					}
				} catch (Exception e) {
					bisEcternalPayments.setPayStatus(PayStatusEnum.UNKNOWN);
					_log.error("商户对外支付调用支付接口失败："+ExceptionProcUtil.getExceptionDesc(e));
				}
			}
			bisEcternalPayments.setAbstracts(message);
			bisEcternalPayments.setCheckStatus(BISAuditStatus.AUDIT_REJECT);
			auditStatus=BISAuditStatus.AUDIT_REJECT;
		}
		bisEcternalPayments.setaCheckNum(aCheckNum+1);
		bisEcternalPaymentsMapper.updateEcternalPaymentsById(bisEcternalPayments);
		//保存审核记录
		saveAuditOper(userDto, bisEcternalPayments, reason, auditStatus);
	}
	
	/**
	 * 保存审核记录
	 * @param userDto
	 * @param bisEcternalPayments
	 * @param reason
	 * @param auditType
	 */
	public void saveAuditOper(UcsSecUserDto userDto,BisEcternalPayments bisEcternalPayments,String reason,BISAuditStatus auditStatus){
		//保存操作记录
		BisAuditDto bisAuditDto = new BisAuditDto();
		bisAuditDto.setId(UUID.randomUUID().toString());
		bisAuditDto.setBusId(bisEcternalPayments.getId());
		EAllocateType allocateType = bisEcternalPayments.getAllocateType();
		if(EAllocateType.MER_OUT_PAY==allocateType){
			bisAuditDto.setAuditType(BISAuditType.MER_EXTERNAL);
		}else{
			bisAuditDto.setAuditType(BISAuditType.EXTERNAL);
		}
		bisAuditDto.setAuditor(userDto.getLoginName());
		bisAuditDto.setAuditorName(userDto.getRealName());
		bisAuditDto.setReason(reason);
		bisAuditDto.setAuditStatus(auditStatus);
		bisAuditDto.setAuditTime(new Date());
		//获取前台出入的备注
		bisAuditDto.setReason(bisEcternalPayments.getMemo());
		bisAuditService.insert(bisAuditDto);
	}
	
	/**
	 * 调用平台对外支付
	 * @param bisEcternalPayments
	 */
	public void doPay(BisEcternalPayments bisEcternalPayments){
		bisEcternalPayments.setCheckStatus(BISAuditStatus.AUDIT_PASS);
		//调用对外支付接口
		PtOutPayPojo ptOutPayPojo = new PtOutPayPojo();
		ptOutPayPojo.setRemark(bisEcternalPayments.getMemo());
		ptOutPayPojo.setPayerBankType(bisEcternalPayments.getOuterBank());
		ptOutPayPojo.setPayerBankCustName(bisEcternalPayments.getOuterBankname());
		ptOutPayPojo.setPayerBankCardNo(bisEcternalPayments.getOuterAcct());
		ptOutPayPojo.setPayerCustId(bisEcternalPayments.getOuterSubject());
		ptOutPayPojo.setPayeeBankType(bisEcternalPayments.getInnerBank());
		ptOutPayPojo.setPayeeBankCustName(bisEcternalPayments.getInnerName());
		ptOutPayPojo.setPayeeBankCardNo(bisEcternalPayments.getInnerAcc());
		ptOutPayPojo.setPayeeAcctSub(bisEcternalPayments.getInnerAccountId());
		ptOutPayPojo.setCurrenoy(bisEcternalPayments.getCurrency());
		ptOutPayPojo.setPubPriv(bisEcternalPayments.getInnerAccountType());
		ptOutPayPojo.setProvince(bisEcternalPayments.getProvince());
		ptOutPayPojo.setCity(bisEcternalPayments.getInnerAccountCitycode());
		ptOutPayPojo.setCentralBk(bisEcternalPayments.getInnerAccountBankcode());
		ptOutPayPojo.setOrderAmt(bisEcternalPayments.getAmount());
		ptOutPayPojo.setCentralBk(bisEcternalPayments.getInnerAccountBankcode());
		ptOutPayPojo.setPayerCentralBk(bisEcternalPayments.getOuterAccountBankcode());
		ptOutPayPojo.setAllocateType(bisEcternalPayments.getAllocateType());
		ptOutPayPojo.setOrderId(bisEcternalPayments.getBatch());
		try {
			_log.info("调用支付平台对外支付接口");
			ResultCodeDto<PayAmtAllocateDto> resultCodeDto = payOutPayAppService.ptOutPayApply(ptOutPayPojo);
			setPayStatus(resultCodeDto,bisEcternalPayments,false);
		} catch (Exception e) {
			bisEcternalPayments.setPayStatus(PayStatusEnum.UNKNOWN);
			bisEcternalPayments.setAbstracts(e.getMessage());
			_log.error("系统报错，报错原因:{}",ExceptionProcUtil.getExceptionDesc(e));
		}
	}

	@Override
	public BisEcternalPayments selectOneBeanById(String id) {
		//需要获取复核记录展现出来
		BisEcternalPayments bisEcternalPayments = bisEcternalPaymentsMapper.selectByPrimaryKey(id);
		EAllocateType allocateType = bisEcternalPayments.getAllocateType();
		if(EAllocateType.MER_OUT_PAY==allocateType){
			bisEcternalPayments.setBisAuditDtos(bisAuditService.findListBybusId(bisEcternalPayments.getId(),BISAuditType.MER_EXTERNAL));
		}else{
			bisEcternalPayments.setBisAuditDtos(bisAuditService.findListBybusId(bisEcternalPayments.getId(),BISAuditType.EXTERNAL));
		}
		return bisEcternalPayments;
	}

	@Override
	public BisEcternalPayments getOldDto(String id) {
		//需要获取操作记录展现出来
		BisEcternalPayments bisEcternalPayments = bisEcternalPaymentsMapper.selectByPrimaryKey(id);
		return bisEcternalPayments;
	}

	@Override
	public void queryPayStatus(String id) {
		BisEcternalPayments bisEcternalPaymentsDto = bisEcternalPaymentsMapper.selectByPrimaryKey(id);
		if(bisEcternalPaymentsDto==null){
			throw new CbsUncheckedException("cbs-0010","获取支付状态失败：记录不存在");
		}
		if(PayStatusEnum.UNKNOWN!=bisEcternalPaymentsDto.getPayStatus()){
			throw new CbsUncheckedException("cbs-0011","支付状态已为终态，无需查询");
		}
		doQueryPayStatus(bisEcternalPaymentsDto);
	}
	
	/**
	 * 自动查询未知状态
	 */
	@Override
	public void autoQueryUnDownStatus(){
		//获取所有支付状态为未知的数据
		try {
			List<BisEcternalPayments> bisEcternalPaymentsList=bisEcternalPaymentsMapper.getUnDownStatusDtos(PayStatusEnum.UNKNOWN);
			if(bisEcternalPaymentsList!=null && !bisEcternalPaymentsList.isEmpty()){
				for (BisEcternalPayments bisEcternalPayments : bisEcternalPaymentsList) {
					doQueryPayStatus(bisEcternalPayments);
				}
			}
		} catch (Exception e) {
			_log.error("对外支付自动查询状态失败："+ExceptionProcUtil.getExceptionDesc(e));
			bisExceptionLogService.saveLog(CbsExceptionLogDtoUtils.getBisExceptionLog(EBisExceptionLogNlevel.ERROR, EBisExceptionLogType.QUERY_UNDOWNSTATUS,"对外支付自动查询状态失败"));
		}
	}
	
	/**
	 * 自动对外支付
	 */
	@Override
	public void autoEctPay(){
		try {
			List<BisEcternalPayments> dtoList = bisEcternalPaymentsMapper.getAuditPassWaitPay(PayStatusEnum.WAITPAY,BISAuditStatus.AUDIT_PASS);
			if(dtoList!=null && !dtoList.isEmpty()){
				for (BisEcternalPayments dto : dtoList) {
					doPay(dto);
					bisEcternalPaymentsMapper.updateEcternalPaymentsById(dto);
				}
			}
		} catch (Exception e) {
			_log.error("自动对外支付失败："+ExceptionProcUtil.getExceptionDesc(e));
			bisExceptionLogService.saveLog(CbsExceptionLogDtoUtils.getBisExceptionLog(EBisExceptionLogNlevel.ERROR, EBisExceptionLogType.AUTO_ECT_PAY,"自动对外支付失败"));
		}
	}
	
	/**
	 * 执行状态查询
	 * @param bisEcternalPaymentsDto
	 */
	public void doQueryPayStatus(BisEcternalPayments bisEcternalPaymentsDto){
		//如果是商户对外支付，审核状态：审核拒绝，支付状态：未知.重新调用审核拒绝接口
		EAllocateType allocateType = bisEcternalPaymentsDto.getAllocateType();
		ResultCodeDto<PayAmtAllocateDto> resultCodeDto=null;
		if(EAllocateType.MER_OUT_PAY==allocateType && BISAuditStatus.AUDIT_REJECT==bisEcternalPaymentsDto.getCheckStatus()
				&& PayStatusEnum.UNKNOWN==bisEcternalPaymentsDto.getPayStatus()){
			try {
				resultCodeDto = payOutPayAppService.auditFailCall(bisEcternalPaymentsDto.getBatch(), "商户对外支付支付状态-未知，状态查询");
				setPayStatus(resultCodeDto, bisEcternalPaymentsDto, true);
			}catch (PayUncheckedException e) {
				_log.error("商户对外支付查询处理状态失败："+ExceptionProcUtil.getExceptionDesc(e));
				throw new CbsUncheckedException("","商户对外支付状态查询失败："+e.getMessage());
			} catch (Exception e) {
				_log.error("商户对外支付查询处理状态失败："+ExceptionProcUtil.getExceptionDesc(e));
				throw new CbsUncheckedException("","商户对外支付状态查询失败：调用支付接口异常");
			}
		}else{
			try {
				resultCodeDto = payOutPayAppService.queryCashTransferAndPtOutPay(bisEcternalPaymentsDto.getBatch(),allocateType);
				setPayStatus(resultCodeDto, bisEcternalPaymentsDto,false);
			} catch (PayUncheckedException e) {
				_log.error("对外支付查询处理状态失败："+ExceptionProcUtil.getExceptionDesc(e));
				throw new CbsUncheckedException("","对外支付状态查询失败："+e.getMessage());
			}catch (Exception e) {
				_log.error("平台对外支付查询处理状态失败："+ExceptionProcUtil.getExceptionDesc(e));
				throw new CbsUncheckedException("","对外支付状态查询失败：调用支付接口异常");
			}
		}
		bisEcternalPaymentsMapper.updateEcternalPaymentsById(bisEcternalPaymentsDto);
	}
	
	/**
	 * 根据返回状态设置对外支付的支付状态
	 * @param allocateStatus
	 * @param bisEcternalPayments
	 * @param isMerOutPayAucitReject 是否是商户对外支付复核拒绝
	 */
	public void setPayStatus(ResultCodeDto<PayAmtAllocateDto> resultCodeDto,BisEcternalPayments bisEcternalPayments,boolean isMerOutPayAucitReject){
		String message="";
		if(resultCodeDto==null){
			bisEcternalPayments.setPayStatus(PayStatusEnum.UNKNOWN);
			message="调用支付系统返回状态为空";
		}else{
			EResultCode resultCode = resultCodeDto.getResultCode();
			if(EResultCode.SUCCESS==resultCode){
				bisEcternalPayments.setPayStatus(PayStatusEnum.SUCCESS);
				updateBatchExpNum(bisEcternalPayments, 1, 0);
			}else if(EResultCode.FAIL==resultCode){
				//如果是商户对外支付复核拒绝，失败需要将状态改为待处理
				if(isMerOutPayAucitReject){
					bisEcternalPayments.setCheckStatus(BISAuditStatus.WAIT_AUDIT);
				}
				updateBatchExpNum(bisEcternalPayments, 0, 1);
				bisEcternalPayments.setPayStatus(PayStatusEnum.FAIL);
			}else{
				bisEcternalPayments.setPayStatus(PayStatusEnum.UNKNOWN);
			}
			message=resultCodeDto.getMsgDetail();
		}
		bisEcternalPayments.setAbstracts(message);
	}
	
	/**
	 * 修改批量导入记录成功/失败笔数
	 * @param frozenDto
	 * @param successNum
	 * @param failNum
	 */
	private void updateBatchExpNum(BisEcternalPayments dto, long successNum, long failNum) {
		if(StringUtils.isBlank(dto.getBatchNo()) || EAllocateType.MER_OUT_PAY==dto.getAllocateType()){
			return ;
		}
		bisBatchExpService.updateBatchExpNum(successNum,failNum,dto.getBatchNo());
	}

	@Override
	public Map<String, BisEcternalPayments> findEcternalPaymentsMapByBatchNo(String batchNo) {
		List<BisEcternalPayments> ecternalPaymentsList=bisEcternalPaymentsMapper.findEcternalPaymentsListByBatchNo(batchNo);
		HashMap<String, BisEcternalPayments> ecternalPaymentsMap = new HashMap<String,BisEcternalPayments>();
		//Map<String, TbChlBank> bankDtoMap = chlBankService.findChannelBankMap();
		if(ecternalPaymentsList!=null && !ecternalPaymentsList.isEmpty()){
			for (BisEcternalPayments bisEcternalPayments : ecternalPaymentsList) {
				//获取key
				StringBuilder sb = new StringBuilder();
		    	sb.append(bisEcternalPayments.getOuterAcct())/*.append(bankDtoMap.get(bisEcternalPayments.getInnerBank()))*/.append(bisEcternalPayments.getInnerAcc())
		    	/*.append(bisEcternalPayments.getInnerName()).append(bisEcternalPayments.getInnerAccountType().getValue()).append(bisEcternalPayments.getProvinceName())
		    	.append(bisEcternalPayments.getCityName()).append(bisEcternalPayments.getInnerAccountBankcode()).append(bisEcternalPayments.getBankNameI()).append(bisEcternalPayments.getInnerAccountId())*/;
		    	String key=sb.toString();
		    	BisEcternalPayments dto = ecternalPaymentsMap.get(key);
		    	if(dto!=null){
		    		dto.setAmount(dto.getAmount()+bisEcternalPayments.getAmount());
		    	}else{
		    		ecternalPaymentsMap.put(key, bisEcternalPayments);
		    	}
			}
			
		}
		return ecternalPaymentsMap;
	}

	@Override
	public List<BisEcternalPayments> list(BisEcternalPayments queryParam) {
		return bisEcternalPaymentsMapper.list(queryParam);
	}
}
