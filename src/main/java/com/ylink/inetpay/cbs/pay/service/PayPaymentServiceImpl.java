package com.ylink.inetpay.cbs.pay.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.ylinkpay.framework.core.model.PageData;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.ylink.inetpay.cbs.bis.service.BisAuditRuleService;
import com.ylink.inetpay.cbs.bis.service.BisAuditService;
import com.ylink.inetpay.cbs.common.CbsConstants;
import com.ylink.inetpay.cbs.mrs.dao.MrsDataAuditChangeDtoMapper;
import com.ylink.inetpay.cbs.pay.dao.PayDataAuditDtoMapper;
import com.ylink.inetpay.cbs.pay.dao.PayPaymentDtoMapper;
import com.ylink.inetpay.cbs.pay.dao.PayPaymentEleDtoMapper;
import com.ylink.inetpay.common.core.constant.BISAuditStatus;
import com.ylink.inetpay.common.core.constant.BISAuditType;
import com.ylink.inetpay.common.core.constant.EAuditStatus;
import com.ylink.inetpay.common.core.constant.EBusiType;
import com.ylink.inetpay.common.core.constant.EChlChannelCode;
import com.ylink.inetpay.common.core.constant.EIsNeedAuditEnum;
import com.ylink.inetpay.common.core.constant.ERefundSource;
import com.ylink.inetpay.common.core.constant.EResultCode;
import com.ylink.inetpay.common.core.dto.ReporHeadDto;
import com.ylink.inetpay.common.core.dto.ResultCode;
import com.ylink.inetpay.common.core.dto.ResultCodeDto;
import com.ylink.inetpay.common.core.util.ExceptionProcUtil;
import com.ylink.inetpay.common.core.util.JsonUtil;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisAuditDto;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisAuditRule;
import com.ylink.inetpay.common.project.cbs.dto.pay.PayDataAuditDto;
import com.ylink.inetpay.common.project.cbs.dto.pay.PayMentContent;
import com.ylink.inetpay.common.project.cbs.dto.ucs.sec.UcsSecUserDto;
import com.ylink.inetpay.common.project.cbs.exception.CbsUncheckedException;
import com.ylink.inetpay.common.project.cbs.vo.bis.ResponseMessage;
import com.ylink.inetpay.common.project.channel.app.RefundAppService;
import com.ylink.inetpay.common.project.channel.dto.request.RefundPojo;
import com.ylink.inetpay.common.project.channel.dto.response.RefundRespPojo;
import com.ylink.inetpay.common.project.channel.exception.ChannelCheckedException;
import com.ylink.inetpay.common.project.pay.app.PayRefundAppService;
import com.ylink.inetpay.common.project.pay.dto.PayBookDto;
import com.ylink.inetpay.common.project.pay.dto.PayPaymentDto;
import com.ylink.inetpay.common.project.pay.dto.PayRefundDto;
import com.ylink.inetpay.common.project.pay.rest.refund.RefundBizContent;

@Service("payPaymentService")
public class PayPaymentServiceImpl implements PayPaymentService {
	@Autowired
	PayPaymentDtoMapper payPaymentDtoMapper;
	@Autowired
	private PayPaymentEleDtoMapper paymentEleDtoMapper;
	@Autowired
	private PayDataAuditDtoMapper payDataAuditDtoMapper;
	@Autowired
	private MrsDataAuditChangeDtoMapper mrsDataAuditChangeDtoMapper;
	/*@Autowired
	private PayRefundDtoMapper payRefundDtoMapper;*/
	/*@Autowired
	private CbsActBillAppService cbsActBillAppService;
	@Autowired
	private PayNoticeDtoMapper payNoticeDtoMapper;*/
	/*@Autowired
	private PayBookDtoMapper payBookDtoMapper;*/
	/*@Autowired
	private ActAccountDtoMapper actAccountDtoMapper;*/
	@Autowired 
	private  PayRefundAppService payRefundAppService;
	@Autowired
	private PaySequenceService paySequenceService;
	@Autowired
	private RefundAppService refundAppService;
	@Autowired
	private PayBookService payBookService;
	@Autowired
	private BisAuditService bisAuditService;
	@Autowired
	private BisAuditRuleService bisAuditRuleService;
	private static Logger _log = LoggerFactory.getLogger(PayPaymentServiceImpl.class);
	
	@Override
	public PageData<PayPaymentDto> queryAllData(PageData<PayPaymentDto> pageDate, PayPaymentDto PayPaymentDto) {
		PageHelper.startPage(pageDate.getPageNumber(), pageDate.getPageSize());
		List<PayPaymentDto> list = payPaymentDtoMapper.queryAllData(PayPaymentDto);
		Page<PayPaymentDto> page = (Page<PayPaymentDto>) list;
		pageDate.setTotal(page.getTotal());
		pageDate.setRows(list);
		return pageDate;
	}
	@Override
	public PageData<PayPaymentDto> queryAuditAllData(PageData<PayPaymentDto> pageDate, PayPaymentDto PayPaymentDto,String loginName) {
		PageHelper.startPage(pageDate.getPageNumber(), pageDate.getPageSize());
		List<PayPaymentDto> list = payPaymentDtoMapper.queryAuditAllData(PayPaymentDto);
		//判断当前用户是否已经复核
		if(list!=null && !list.isEmpty()){
			ArrayList<String> busiIds = new ArrayList<String>();
			for (PayPaymentDto dto : list) {
				busiIds.add(dto.getAuditId());
			}
			List<BisAuditDto> bisAuditDtos=bisAuditService.findRefundListBybusIds(busiIds, BISAuditType.BUSI_REFUND,BISAuditType.MER_REFUND,loginName);
			if(bisAuditDtos!=null && !bisAuditDtos.isEmpty()){
				HashMap<String, BisAuditDto> auditOperDtoMap = new HashMap<String,BisAuditDto>();
				for (BisAuditDto bisAuditDto : bisAuditDtos) {
					auditOperDtoMap.put(bisAuditDto.getBusId(), bisAuditDto);
				}
				for (PayPaymentDto dto : list) {
					BisAuditDto bisAuditDto = auditOperDtoMap.get(dto.getAuditId());
					if(bisAuditDto!=null){
						dto.setIsAudit(true);
					}
				}
			}
		}
		Page<PayPaymentDto> page = (Page<PayPaymentDto>) list;
		pageDate.setTotal(page.getTotal());
		pageDate.setRows(list);
		return pageDate;
	}

	@Override
	public PayPaymentDto selectByBusiId(String busiId) {
		// 获取订单的个性化要素
		PayPaymentDto selectByBusiId = payPaymentDtoMapper.selectByBusiId(busiId);
		if (selectByBusiId != null) {
			selectByBusiId.setAttachs(paymentEleDtoMapper.getAttachs(busiId));
			/*BISAuditType auditType;
			if(ERefundSource.SYSTEM==selectByBusiId.getRefundType()){
				auditType=BISAuditType.BUSI_REFUND;
			}else{
				auditType=BISAuditType.MER_REFUND;
			}
			selectByBusiId.setBisAuditDtos(bisAuditService.findListBybusId(selectByBusiId.getBusiId(),auditType));*/
		}
		return selectByBusiId;
	}

	@Override
	public ReporHeadDto reportSumData(PayPaymentDto payPaymentDto) {
		return payPaymentDtoMapper.reportSumData(payPaymentDto);
	}

	@Override
	@Transactional(value = CbsConstants.TX_MANAGER_MRS)
	public boolean genRefund(PayMentContent payMentContent) {
		// 生成退款订单号
		String busiId = getSequenceId(EBusiType.REFUND.getValue(), 10);
		payMentContent.setRefundId(busiId);
		// 转成json串
		String jsonStr = JsonUtil.ObjectToJson(payMentContent);
		PayDataAuditDto baseDataAuditChangeDto = new PayDataAuditDto();
		baseDataAuditChangeDto.setId(baseDataAuditChangeDto.getIdentity()); // 主键ID
		baseDataAuditChangeDto.setRefId(payMentContent.getOrderId()); // 关联ID
		baseDataAuditChangeDto.setBusiType(EBusiType.PAY.getValue()); // 审核类型
		baseDataAuditChangeDto.setChangeContent(jsonStr); // 变更内容
		baseDataAuditChangeDto.setCreateUser(payMentContent.getOperUser()); // 创建用户
		baseDataAuditChangeDto.setCreateTime(new Date()); // 创建时间
		baseDataAuditChangeDto.setRefundType(ERefundSource.SYSTEM);
		long num=0;
		num = getNum(payMentContent.getAmount(),BISAuditType.BUSI_REFUND);
		if(num!=0){
			baseDataAuditChangeDto.setAuditStatus(EAuditStatus.AUDIT_WAIT.getValue());// 复核核状态
		}else{
			RefundBizContent refundBizContent = new RefundBizContent();
			refundBizContent.setOrderId(payMentContent.getMerOrderId());
			refundBizContent.setRefundId(payMentContent.getRefundId());
			refundBizContent.setSource(ERefundSource.getEnum(payMentContent.getSource()));
			refundBizContent.setAmount(payMentContent.getAmount());
			ResultCodeDto<PayRefundDto> result = payRefundAppService.refundApply(refundBizContent,
					payMentContent.getAccessCode());
			if (result!=null && !EResultCode.SUCCESS.equals(result.getResultCode())) {
				throw new CbsUncheckedException("",result.getMsgDetail());
			}
			baseDataAuditChangeDto.setAuditStatus(EAuditStatus.AUDIT_PASS.getValue());// 复核核状态
		}
		baseDataAuditChangeDto.setMustCheckNum(num);
		baseDataAuditChangeDto.setAlreadyCheckNum(0l);
		payDataAuditDtoMapper.insert(baseDataAuditChangeDto);
		return true;
	}
	
	/**
	 * 保存复核记录
	 * @param userDto
	 * @param payDataAuditDto
	 * @param reason
	 * @param auditType
	 * @param auditStatus
	 */
	public void saveAuditOper(UcsSecUserDto userDto,PayDataAuditDto payDataAuditDto,String reason,
			BISAuditType auditType,BISAuditStatus auditStatus){
		//保存操作记录
		BisAuditDto bisAuditDto = new BisAuditDto();
		bisAuditDto.setId(UUID.randomUUID().toString());
		bisAuditDto.setBusId(payDataAuditDto.getId());
		bisAuditDto.setAuditType(auditType);
		bisAuditDto.setAuditor(userDto.getLoginName());
		bisAuditDto.setAuditorName(userDto.getRealName());
		bisAuditDto.setReason(reason);
		bisAuditDto.setAuditStatus(auditStatus);
		bisAuditDto.setAuditTime(new Date());
		//获取前台出入的备注
		bisAuditDto.setReason(reason);
		bisAuditService.insert(bisAuditDto);
	}

	public String getSequenceId(String prefix, String seqName, int length) {
		StringBuffer seq = new StringBuffer(prefix);
		seq.append(seqName);

		String value = mrsDataAuditChangeDtoMapper.getPayNoVal();

		String formatStr = "%0" + length + "d";
		seq.append(String.format(formatStr, Long.valueOf(value)));

		return seq.toString();
	}

	public String getSequenceId(String seqName, int length) {
		return paySequenceService.getSequenceId(seqName, length);
	}

	@Override
	public boolean checkPayData(String busiId) {
		PayDataAuditDto payDataAuditDto = payDataAuditDtoMapper.selectByRefId(busiId);
		if (payDataAuditDto != null) {
			return true;
		}
		return false;
	}
	@Override
	public PayDataAuditDto selectByRefId(String refId) {
		return payDataAuditDtoMapper.selectByRefId(refId);
	}
	@Override
	public ResponseMessage auditPay(String auditType, String auditId, String reason, UcsSecUserDto userDto) {
		ResponseMessage responseMessage = new ResponseMessage();	
		ResultCodeDto<PayRefundDto> result = new ResultCodeDto<PayRefundDto>();
		// 查审核信息
		PayDataAuditDto baseDataAuditChangeDto = payDataAuditDtoMapper.selectByPrimaryKey(auditId);
		if(!EAuditStatus.AUDIT_WAIT.getValue().equals(baseDataAuditChangeDto.getAuditStatus())){
			responseMessage.setMessage("审核失败,数据已被其他管理员审核！");
		}else{
			ArrayList<String> ids = new ArrayList<String>();
			ids.add(baseDataAuditChangeDto.getId());
			List<BisAuditDto> auditDtos = bisAuditService.findRefundListBybusIds(ids, BISAuditType.BUSI_REFUND, BISAuditType.MER_REFUND, userDto.getLoginName());
			if(auditDtos!=null && auditDtos.size()>0){
				responseMessage.setMessage("您参与复核，请邀请其管理员复核！");
				responseMessage.setSuccess(false);
				return responseMessage;
			}else{
				long alreadyCheckNum=baseDataAuditChangeDto.getAlreadyCheckNum()+1;
				BISAuditStatus auditStatus = null;
				PayMentContent payMentContent = JsonUtil.JsonToObject(baseDataAuditChangeDto.getChangeContent(),PayMentContent.class);
				if(EAuditStatus.AUDIT_PASS.getValue().equals(auditType)){//通过
					if(baseDataAuditChangeDto.getMustCheckNum()==alreadyCheckNum){
						// 获取特参改变后的信息
						RefundBizContent refundBizContent = new RefundBizContent();
						refundBizContent.setOrderId(payMentContent.getMerOrderId());
						refundBizContent.setRefundId(payMentContent.getRefundId());
						refundBizContent.setSource(ERefundSource.getEnum(payMentContent.getSource()));
						refundBizContent.setAmount(payMentContent.getAmount());
						if(ERefundSource.MER==baseDataAuditChangeDto.getRefundType()){
							result = payRefundAppService.merRefundSuccess(payMentContent.getRefundId());
						}else if(ERefundSource.SYSTEM==baseDataAuditChangeDto.getRefundType()){
							result = payRefundAppService.refundApply(refundBizContent,payMentContent.getAccessCode());
						}
						if (result!=null && !EResultCode.SUCCESS.equals(result.getResultCode())) {
							responseMessage.setSuccess(false);
							responseMessage.setMessage(result.getMsgDetail());
							return responseMessage;
						}
						baseDataAuditChangeDto.setAuditStatus(EAuditStatus.AUDIT_PASS.getValue());//复核核状态【1-通过】
					}
					auditStatus=BISAuditStatus.AUDIT_PASS;
				}else if(EAuditStatus.AUDIT_FAIL.getValue().equals(auditType)){//拒绝
					if(ERefundSource.MER==baseDataAuditChangeDto.getRefundType()){
						result = payRefundAppService.merRefundSuccess(payMentContent.getRefundId());
					}
					baseDataAuditChangeDto.setAuditStatus(EAuditStatus.AUDIT_FAIL.getValue());//复核核状态【2-拒绝】
					auditStatus=BISAuditStatus.AUDIT_REJECT;
				}
				//更新审核信息
				baseDataAuditChangeDto.setAlreadyCheckNum(alreadyCheckNum);
				payDataAuditDtoMapper.updateByPrimaryKeySelective(baseDataAuditChangeDto);
				BISAuditType refundType=null;
				if(ERefundSource.SYSTEM==baseDataAuditChangeDto.getRefundType()){
					refundType=BISAuditType.BUSI_REFUND;
				}else if(ERefundSource.MER==baseDataAuditChangeDto.getRefundType()){
					refundType=BISAuditType.MER_REFUND;
				}
				saveAuditOper(userDto, baseDataAuditChangeDto,reason,refundType, auditStatus);
			}
		}
		responseMessage.setSuccess(true);
		return responseMessage;
	}
	@Override
	public PayPaymentDto selectByAuditDataId(String auditId) {
		PayPaymentDto selectByBusiId = payPaymentDtoMapper.selectByAuditDataId(auditId);
		if(selectByBusiId!=null){
			BISAuditType auditType;
			if(ERefundSource.SYSTEM==selectByBusiId.getRefundType()){
				auditType=BISAuditType.BUSI_REFUND;
			}else{
				auditType=BISAuditType.MER_REFUND;
			}
			selectByBusiId.setBisAuditDtos(bisAuditService.findListBybusId(selectByBusiId.getAuditId(),auditType));
		}
		return selectByBusiId;
	}
	@Override
	public RefundRespPojo refund(String busiId) {
		RefundRespPojo refundRespPojo = null;
		try {
			PayBookDto payBookDto = payBookService.selectByPayId(busiId);
			if (null == payBookDto) {
				System.out.println("====================支付流水表没有查到数据,根据busiId:"+busiId);
				return null;
			}
			RefundPojo refundPojo = new RefundPojo();
			EChlChannelCode channelCode = EChlChannelCode.getEChlChannelCode(payBookDto.getChannelCode()); 
			// 生成退款支付流水
			String payId = getSequenceId(EBusiType.PAY_BOOK.getValue(), 10);
			refundPojo.setChannelCode(channelCode);
			refundPojo.setPayId(payId);
			refundPojo.setOldPayId(payBookDto.getPayId());
			refundPojo.setRefundFee(payBookDto.getOrderAmt());
			//调退款申请接口
			refundRespPojo = refundAppService.refundHandler(refundPojo);
			
			/*if (EChlRefundState.REFUND_SUCESS.equals(refundRespPojo.getTradeState())) {
				
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");  
				String str = null;
				Date date = null;
				try {
					str = sdf.format(new Date());
					date = sdf.parse(refundRespPojo.getRefundDate());
				} catch (ParseException e) {
					e.printStackTrace();
				}
//	TODO			cbsActBillAppService.selectByCustId(actBillDto);
//				actAccountDtoMapper.findAcctIdByCustIdAndSubjectNo2(payBookDto.getPayerCustId(), subjectNo2);
				
				
				PayPaymentDto payPaymentDto = payPaymentDtoMapper.selectByBusiId(busiId);
				payPaymentDto.setOrderStatus(EOrderStatus.ORDER_STATUS_REFUND_SUCCESS);
				Long refundAmt = Long.parseLong(refundRespPojo.getTotalFee());
				payPaymentDto.setRefundAmt(refundAmt);
				payPaymentDto.setCompleteTime(date);
				//修改订单状态
//				payPaymentDtoMapper.updateByPrimaryKeySelective(payPaymentDto);
				// 生成退款订单号
				String busiIdt = getSequenceId(EBusiType.REFUND.getValue(), 10);
				PayRefundDto payRefundDto = new PayRefundDto();
				payRefundDto.setId(UUID.randomUUID().toString());
				payRefundDto.setAccessCode(payPaymentDto.getAccessCode());
				payRefundDto.setAccountDate(str);
				payRefundDto.setBusiId(busiIdt);
				payRefundDto.setOldBusiId(busiId);
				payRefundDto.setBusiType(EBusiType.REFUND);
				payRefundDto.setClearDate(null);
				payRefundDto.setCompleteTime(date);
				payRefundDto.setCreateTime(new Date());
				payRefundDto.setCurrenoy(payPaymentDto.getCurrenoy());
				payRefundDto.setCustId(payPaymentDto.getCustId());
				payRefundDto.setCustName(payPaymentDto.getCustName());
				payRefundDto.setDealStatus(EProcessStatus.PROCESS_SUCCESS);
				payRefundDto.setExtOrgId(payPaymentDto.getExtOrgId());
				payRefundDto.setMerCode(payPaymentDto.getMerCode());
				payRefundDto.setMerName(payPaymentDto.getMerName());
				payRefundDto.setMerOrderDate(payPaymentDto.getMerOrderDate());
				payRefundDto.setMerOrderId(payPaymentDto.getMerOrderId());
				payRefundDto.setNotifyUrl(payPaymentDto.getNotifyUrl());
				payRefundDto.setOrderAmt(payPaymentDto.getOrderAmt());
				payRefundDto.setOrderStatus(EOrderStatus.ORDER_STATUS_REFUND_SUCCESS);
				payRefundDto.setPayerCustId(payPaymentDto.getPayerCustId());
				payRefundDto.setPayerCustName(payPaymentDto.getPayerCustName());
				payRefundDto.setPayStyle(payPaymentDto.getPayStyle());
				payRefundDto.setRemark(payPaymentDto.getRemark());
				payRefundDto.setSource(ERefundSource.MER);
				payRefundDto.setTradeName(payPaymentDto.getTradeName());
				payRefundDto.setUpdateTime(new Date());
				//向退款订单表中新增数据
//				payRefundDtoMapper.insertSelective(payRefundDto);
				payBookDto.setId(UUID.randomUUID().toString());
				payBookDto.setPayId(payId);
				payBookDto.setOrderStatus(EOrderStatus.ORDER_STATUS_REFUND_SUCCESS);
				payBookDto.setCreateTime(new Date());
				payBookDto.setUpdateTime(null);
				payBookDto.setQueryTimes(0);
				payBookDto.setStartPayTime(refundRespPojo.getRefundTime());
				//支付流水表存数据
//				payBookDtoMapper.insertSelective(payBookDto);
				PayNoticeDto payNoticeDto = payNoticeDtoMapper.selectBybusiId(busiId);
				payNoticeDto.setId(UUID.randomUUID().toString());
				payNoticeDto.setBusiId(busiIdt);
				payNoticeDto.setMerOrderDate(refundRespPojo.getRefundDate());
//				payNoticeDto.setMerOrderId(UuidUtil.getUUID());
				payNoticeDto.setNoticeType(ENoticeType.REFUND_SUCCESS);
				payNoticeDto.setCreateTime(new Date());
				//通知表存数据
//				payNoticeDtoMapper.insertSelective(payNoticeDto);
			}*/
			
		} catch (ChannelCheckedException e) {
			e.printStackTrace();
		}
		return refundRespPojo;
	}
	@Override
	public ResultCode saveMerRefundAudiit(PayRefundDto payRefundDto) {
		try {
			//判断如果订单号已经存在，保存失败
			String busiId = payRefundDto.getBusiId();
			PayDataAuditDto refundAuditDto = payDataAuditDtoMapper.selectByRefId(busiId);
			if(refundAuditDto!=null){
				_log.error("保存商户退款：订单"+busiId+"已存在");
				return new ResultCode(EResultCode.SUCCESS, EIsNeedAuditEnum.NEED, "订单"+busiId+"已存在");
			}
			long num=0;
			num = getNum(payRefundDto.getOrderAmt(),BISAuditType.MER_REFUND);
			//getRefundAuditDto(payRefundDto,num);
			if(num==0){
				//如果复核次数为0，直接退款
				return new ResultCode(EResultCode.SUCCESS, EIsNeedAuditEnum.NO_NEED, "复核次数为0");
			}else{
				//保存退款复核记录
				getRefundAuditDto(payRefundDto,num);
				return new ResultCode(EResultCode.SUCCESS, EIsNeedAuditEnum.NEED, "复核次数为"+num);
			}
		} catch (Exception e) {
			_log.error("保存退款复核记录失败："+ExceptionProcUtil.getExceptionDesc(e));
			return new ResultCode(EResultCode.FAIL, EIsNeedAuditEnum.NEED, "保存退款复核记录失败");
		}
		
	}
	
	public void getRefundAuditDto(PayRefundDto payRefundDto,long num){
		PayMentContent payMentContent = new PayMentContent();
		payMentContent.setOrderId(payRefundDto.getBusiId());
		payMentContent.setMerOrderId(payRefundDto.getMerOrderId());
		payMentContent.setAccessCode(payRefundDto.getAccessCode());
		payMentContent.setAmount(payRefundDto.getOrderAmt());
		payMentContent.setSource(ERefundSource.SYSTEM.getValue());
		//payMentContent.setOperUser("支付系统调用");
		
		String jsonStr = JsonUtil.ObjectToJson(payMentContent);
		PayDataAuditDto baseDataAuditChangeDto = new PayDataAuditDto();
		baseDataAuditChangeDto.setId(baseDataAuditChangeDto.getIdentity()); // 主键ID
		baseDataAuditChangeDto.setRefId(payMentContent.getOrderId()); // 关联ID
		baseDataAuditChangeDto.setBusiType(EBusiType.PAY.getValue()); // 审核类型
		baseDataAuditChangeDto.setChangeContent(jsonStr); // 变更内容
		baseDataAuditChangeDto.setAuditStatus(EAuditStatus.AUDIT_WAIT.getValue());// 复核核状态
		//baseDataAuditChangeDto.setCreateUser(payMentContent.getOperUser()); // 创建用户
		baseDataAuditChangeDto.setCreateTime(new Date()); // 创建时间
		baseDataAuditChangeDto.setMustCheckNum(num);
		baseDataAuditChangeDto.setAlreadyCheckNum(0l);
		baseDataAuditChangeDto.setRefundType(payRefundDto.getSource());
		payDataAuditDtoMapper.insert(baseDataAuditChangeDto);
	}
	
	public int getNum(long amount,BISAuditType auditType) {
		//需要复核次数
		BisAuditRule bisAuditRule = new BisAuditRule();
		bisAuditRule.setAuditType(auditType);
		List<BisAuditRule> bisAuditRuleList = bisAuditRuleService.queryBisAuditRule(bisAuditRule);
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
	public PayDataAuditDto findById(String id) {
		return payDataAuditDtoMapper.selectByPrimaryKey(id);
	}
}
