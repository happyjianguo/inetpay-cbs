package com.ylink.inetpay.cbs.mrs.service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.ylinkpay.framework.core.model.PageData;
import org.ylinkpay.framework.core.util.DateUtils;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.ylink.inetpay.cbs.chl.dao.TbChlBankMapper;
import com.ylink.inetpay.cbs.chl.service.ChlBankService;
import com.ylink.inetpay.cbs.common.CbsConstants;
import com.ylink.inetpay.cbs.mrs.dao.MrsAccountDtoMapper;
import com.ylink.inetpay.cbs.mrs.dao.MrsBankBusiDtoMapper;
import com.ylink.inetpay.cbs.mrs.dao.MrsPortalReviewAduitDtoMapper;
import com.ylink.inetpay.cbs.mrs.dao.MrsSubAccountDtoMapper;
import com.ylink.inetpay.common.core.constant.BusinessCodeEnum;
import com.ylink.inetpay.common.core.constant.EActBusiRefSubBusiType;
import com.ylink.inetpay.common.core.constant.ECHLBusiMode;
import com.ylink.inetpay.common.core.constant.EChlChannelCode;
import com.ylink.inetpay.common.core.constant.EChlFrontAccountCode;
import com.ylink.inetpay.common.core.constant.EChlReturnCode;
import com.ylink.inetpay.common.core.constant.ECurrenoy;
import com.ylink.inetpay.common.core.constant.ECustType;
import com.ylink.inetpay.common.core.constant.EIsCrossBank;
import com.ylink.inetpay.common.core.constant.EMrsCarinfoStatus;
import com.ylink.inetpay.common.core.constant.EWorkMode;
import com.ylink.inetpay.common.core.constant.IsDelete;
import com.ylink.inetpay.common.core.constant.MrsAccountAuthStatus;
import com.ylink.inetpay.common.core.constant.MrsAccountStatus;
import com.ylink.inetpay.common.core.constant.MrsCredentialsType;
import com.ylink.inetpay.common.core.constant.MrsCustomerType;
import com.ylink.inetpay.common.core.constant.MrsSubAccountStatus;
import com.ylink.inetpay.common.core.constant.SubAcctType;
import com.ylink.inetpay.common.core.exception.CodeUncheckException;
import com.ylink.inetpay.common.core.util.Des3;
import com.ylink.inetpay.common.core.util.WeekdayUtil;
import com.ylink.inetpay.common.project.account.dto.ActAccountDto;
import com.ylink.inetpay.common.project.cbs.app.ChlFrontendBusiAppService;
import com.ylink.inetpay.common.project.cbs.app.ChlParamAppService;
import com.ylink.inetpay.common.project.cbs.app.MrsAccountAppService;
import com.ylink.inetpay.common.project.cbs.app.MrsBankBusiDtoAppService;
import com.ylink.inetpay.common.project.cbs.app.MrsOrganAppService;
import com.ylink.inetpay.common.project.cbs.app.MrsPersonAppService;
import com.ylink.inetpay.common.project.cbs.constant.mrs.AduitBusiType;
import com.ylink.inetpay.common.project.cbs.constant.mrs.BankAddMode;
import com.ylink.inetpay.common.project.cbs.constant.mrs.BankAduitType;
import com.ylink.inetpay.common.project.cbs.constant.mrs.BankBindType;
import com.ylink.inetpay.common.project.cbs.constant.mrs.BankCardType;
import com.ylink.inetpay.common.project.cbs.constant.mrs.BankPayType;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsAccountDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsBankBusiDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsOrganDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsPersonDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsPortalReviewAduitDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsSubAccountDto;
import com.ylink.inetpay.common.project.channel.app.ChlFrontAccountAppService;
import com.ylink.inetpay.common.project.channel.app.FastPayAppService;
import com.ylink.inetpay.common.project.channel.app.SmsManageAppService;
import com.ylink.inetpay.common.project.channel.dto.TbChlBank;
import com.ylink.inetpay.common.project.channel.dto.TbChlParamDto;
import com.ylink.inetpay.common.project.channel.dto.request.BocFastSignAreementPojo;
import com.ylink.inetpay.common.project.channel.dto.request.FrontAccountPojo;
import com.ylink.inetpay.common.project.channel.dto.request.UnBindFastPayPojo;
import com.ylink.inetpay.common.project.channel.dto.response.BaseRespPojo;
import com.ylink.inetpay.common.project.channel.dto.response.FrontAccountRespPojo;
import com.ylink.inetpay.common.project.portal.enums.ErrorMsgEnum;
import com.ylink.inetpay.common.project.portal.exception.PortalCheckedException;
import com.ylink.inetpay.common.project.portal.vo.RespCheckVO;
import com.ylink.inetpay.common.project.portal.vo.UserCheckVO;
import com.ylink.inetpay.common.project.portal.vo.bank.AduitVo;
import com.ylink.inetpay.common.project.portal.vo.bank.BankAddVo;
import com.ylink.inetpay.common.project.portal.vo.bank.BankCardRequestVo;
import com.ylink.inetpay.common.project.portal.vo.customer.BankBusiReqVO;

@Service("mrsBankBusiDtoService")
public class MrsBankBusiDtoServiceImpl implements MrsBankBusiDtoService{
	private static Logger log = LoggerFactory.getLogger(MrsBankBusiDtoServiceImpl.class);
	
	/**
	 * 工作模式
	 * WorkingDay：工作日
	 * NaturalDay：自然日
	 */
	public static final String WORK_MODE = "work_mode";
	
	/**
	 * 工作时间
	 */
	public static final String WORK_TIME = "work_time";
	
	/**
	 * 中行Type
	 */
	public static final String BOC_BANK_TYPE = "104";
	@Autowired
	private MrsBankBusiDtoMapper mrsBankBusiDtoMapper;
	
	@Autowired
	private MrsPortalReviewAduitDtoMapper mrsPortalReviewAduitDtoMapper;
	
	@Autowired
	private MrsAccountDtoMapper mrsAccountDtoMapper;
	
	@Autowired
	private MrsSubAccountDtoMapper mrsSubAccountDtoMapper;
	
	@Autowired
	private ChlFrontAccountAppService chlFrontAccountAppService;
	
	@Autowired
	private MrsAccountAppService mrsAccountAppService;
	
	@Autowired
	private ChlParamAppService chlParamAppService;
	
	@Autowired
	private MrsPersonAppService mrsPersonAppService;
	
	@Autowired
	private MrsOrganAppService  mrsOrganAppService;
	@Autowired
	private ChlBankService  chlBankService;
	@Autowired
	private TbChlBankMapper  tbChlBankMapper;
	@Autowired
	private MrsBankBusiDtoAppService mrsBankBusiDtoAppService;
	
	@Autowired
	private ChlFrontendBusiAppService chlFrontendBusiAppService;
	
	@Autowired
    private SmsManageAppService smsManageAppService;
	
	@Autowired
	private FastPayAppService fastPayAppService;

	@Override
	public List<MrsBankBusiDto> findByCustId(String custId) {
		return mrsBankBusiDtoMapper.findByCustId(custId);
	}
	
	@Override
	public List<MrsBankBusiDto> findByChlBankProp(MrsBankBusiDto userBankDto) {
		return mrsBankBusiDtoMapper.selectByParam(userBankDto);
	}
	
	/**
	 * 根据一户通号查询(快捷专用:绑定(02)、绑卡类型(01))
	 * @param custId
	 * @return
	 */
	@Override
	public List<MrsBankBusiDto> findByBindStatusAndCustidAndPaytype(String custId) {
		return mrsBankBusiDtoMapper.findByBindStatusAndCustidAndPaytype(custId);
	}
	/**
	 * 快捷绑卡
	 */
	@Override
	@Transactional(value=CbsConstants.TX_MANAGER_MRS)
	public RespCheckVO addBankkj(BankAddVo vo, String type, String custId) {
		try {
			MrsBankBusiDto mrsBankBusiDto = getBankDto(vo, type, custId);
			MrsBankBusiDto busiDto = mrsBankBusiDtoMapper.findByAccnoAndCustidAndPaytype(vo.getAccNo(), custId, BankPayType.BP1.getValue());
			TbChlBank tbChlBank =tbChlBankMapper.findByChltypeAndbankType("03", mrsBankBusiDto.getBankType());
			mrsBankBusiDto.setBankName(tbChlBank.getBankName());//暂写
			mrsBankBusiDto.setBindStatus(BankBindType.SUCCESS.getValue());//绑定状态
			mrsBankBusiDto.setCardType(vo.getCardType());//
			mrsBankBusiDto.setPayType(BankPayType.BP1.getValue());//绑卡类型
			mrsBankBusiDto.setAduitStatus(BankAduitType.SUCCESS.getValue());//审核成功s
			mrsBankBusiDto.setCreateTime(new Date());
			//渠道编码
			mrsBankBusiDto.setChannelCode(tbChlBank.getChannlCode());
						
			//调接口
			//获取快捷绑卡认证码
			/*SpdbDynNmReqPojo fastSms = new SpdbDynNmReqPojo();
	    	fastSms.setAcctNo(vo.getAccNo());//银行卡号
	    	fastSms.setTranAmt("1");//金额
	    	fastSms.setIdNo(vo.getIdNo());//证件号
	    	fastSms.setMobileNo(vo.getPhoneNo());//手机号
	    	fastSms.setPayCardName(vo.getCustName());//客户号
	    	
	    	BaseRespPojo respPojo = smsManageAppService.SendSms(fastSms);
	    	log.info("调接口返回结果："+respPojo.getRetrunCode().getValue());
			if(StringUtils.isNotEmpty(respPojo.getReturnMsg())){
				log.info("返回信息："+respPojo.getReturnMsg());
			}
			if(StringUtils.isNotEmpty(respPojo.getReturnMsg())){
				log.info("返回信息："+respPojo.getErrorMsg());
			}
			
			DynNumRespPojo pojo = (DynNumRespPojo) respPojo;
	    	String uniqueCode = pojo.getToken();*/
			
	    	//绑卡
	    	BocFastSignAreementPojo fastSignPojo = new BocFastSignAreementPojo();
	    	fastSignPojo.setCardNo(vo.getAccNo());//银行卡号
	    	fastSignPojo.setMobileNo(vo.getPhoneNo());//手机号
	    	fastSignPojo.setUniqueCode(vo.getUniqueCode());//认证码
	    	fastSignPojo.setCustName(vo.getCustName());//客户姓名
	    	fastSignPojo.setIdentityNo(vo.getIdNo());//证件号码
	    	fastSignPojo.setSmsCode(vo.getSmscode());//短信验证码
	    	
	    	BaseRespPojo fastSignRespPojo = smsManageAppService.SignAgreement(fastSignPojo);
	    	log.info("调接口返回结果："+fastSignRespPojo.getRetrunCode().getValue());
			if(StringUtils.isNotEmpty(fastSignRespPojo.getReturnMsg())){
				log.info("返回信息："+fastSignRespPojo.getReturnMsg());
			}
			if(StringUtils.isNotEmpty(fastSignRespPojo.getReturnMsg())){
				log.info("返回信息："+fastSignRespPojo.getErrorMsg());
			}
			
			if(fastSignRespPojo.getRetrunCode().equals(EChlReturnCode.SUCCESS)){
				if(null != busiDto){
					mrsBankBusiDto.setId(busiDto.getId());
					mrsBankBusiDto.setCreateTime(busiDto.getCreateTime());
					mrsBankBusiDto.setModifyTime(new Date());
					mrsBankBusiDtoMapper.updateByPrimaryKey(mrsBankBusiDto);
				}else{
					mrsBankBusiDtoMapper.insert(mrsBankBusiDto);
				}
				
				//TODO 默认卡设置判断
				List<MrsBankBusiDto> band = mrsBankBusiDtoAppService.findBandByCustId(mrsBankBusiDto.getCustId());
				if (band.size() == 1) {
					MrsBankBusiDto bands = band.get(0);
					bands.setIsDefault(EIsCrossBank.YES.getValue());
					mrsBankBusiDtoMapper.updateByPrimaryKey(bands);
				}
			}else{
				return new RespCheckVO(false,ErrorMsgEnum.SYSTEM_ERROR);
			}
			
		} catch (Exception e) {
			log.error("快捷绑卡处理失败",e);
			log.error("失败原因：" + e.getMessage());
			return new RespCheckVO(false,ErrorMsgEnum.SYSTEM_ERROR);
		} 
		return new RespCheckVO(true);
	}

	@Override
	@Transactional(value=CbsConstants.TX_MANAGER_MRS)
	public RespCheckVO addBanksx(BankAddVo vo, String type, String custId, String name,String userId) {
		try {
			MrsBankBusiDto mrsBankBusiDto = getBankDto(vo, type, custId);
			MrsBankBusiDto busiDto = mrsBankBusiDtoMapper.findByAccnoAndCustidAndPaytype(vo.getAccNo(), custId, BankPayType.BP2.getValue());
			TbChlBank tbChlBank =tbChlBankMapper.findByChltypeAndbankType("09", mrsBankBusiDto.getBankType());
			mrsBankBusiDto.setBankName(tbChlBank.getBankName());
			mrsBankBusiDto.setBindStatus(BankBindType.SUCCESS.getValue());//绑定状态
			mrsBankBusiDto.setCardType(BankCardType.DEBIT.getValue());//
			mrsBankBusiDto.setPayType(BankPayType.BP2.getValue());//绑卡类型
			mrsBankBusiDto.setCreateTime(new Date());
			//渠道编码
			mrsBankBusiDto.setChannelCode(tbChlBank.getChannlCode());
			
			//机构
			if(type.equals(MrsCustomerType.MCT_1.getValue()) || type.equals(MrsCustomerType.MCT_2.getValue()) ||type.equals(MrsCustomerType.MCT_9.getValue())){
				//审核表
				MrsPortalReviewAduitDto	aduitDto =new MrsPortalReviewAduitDto();
				aduitDto.setId(UUID.randomUUID().toString());//
				aduitDto.setBusiNo(mrsBankBusiDto.getId());
				aduitDto.setBusiType(AduitBusiType.AB2.getValue());
				aduitDto.setAduitStatus(BankAduitType.WAIT.getValue());
				aduitDto.setCreateRemark("银保绑卡");
				aduitDto.setCreateTime(new Date());
				aduitDto.setCreateUserName(name);
				aduitDto.setCreateUserNo(userId);
				aduitDto.setCustId(custId);
				mrsPortalReviewAduitDtoMapper.insert(aduitDto);
				mrsBankBusiDto.setBindStatus(BankBindType.TODO.getValue());
				mrsBankBusiDto.setAduitStatus(BankAduitType.WAIT.getValue());//待审核
				if(null != busiDto){
					mrsBankBusiDto.setId(busiDto.getId());
					mrsBankBusiDto.setCreateTime(busiDto.getCreateTime());
					mrsBankBusiDto.setModifyTime(new Date());
					mrsBankBusiDtoMapper.updateByPrimaryKey(mrsBankBusiDto);
				}else{
					mrsBankBusiDtoMapper.insert(mrsBankBusiDto);
				}
				return new RespCheckVO(false,ErrorMsgEnum.SUCCESS_NEED_ADUIT);
				//个人
			}else if (type.equals(MrsCustomerType.MCT_0.getValue())){
				//调接口
				mrsBankBusiDto.setAduitStatus(BankAduitType.SUCCESS.getValue());//审核成功
				FrontAccountRespPojo respPojo = addCard(mrsBankBusiDto);
				mrsBankBusiDto.setErrorRemark(respPojo.getErrorMsg());
				log.info("调接口返回结果："+respPojo.getRetrunCode().getValue());
				if(StringUtils.isNotEmpty(respPojo.getReturnMsg())){
					log.info("返回信息："+respPojo.getReturnMsg());
				}
				if(StringUtils.isNotEmpty(respPojo.getReturnMsg())){
					log.info("返回信息："+respPojo.getErrorMsg());
				}
				
				mrsBankBusiDto.setBindStatus(BankBindType.SUCCESS.getValue());//绑定状态
				//成功
				if(EChlReturnCode.SUCCESS.equals(respPojo.getRetrunCode())){
					//线下成功
					if(ECHLBusiMode.DOWN_LINE.equals(respPojo.getBusiMode())){
						mrsBankBusiDto.setBindStatus(BankBindType.TODO.getValue());
						mrsBankBusiDto.setAddMode(BankAddMode.BA0.getValue());
						
						if(null != busiDto){
							mrsBankBusiDto.setId(busiDto.getId());
							mrsBankBusiDto.setCreateTime(busiDto.getCreateTime());
							mrsBankBusiDto.setModifyTime(new Date());
							mrsBankBusiDtoMapper.updateByPrimaryKey(mrsBankBusiDto);
						}else{
							mrsBankBusiDtoMapper.insert(mrsBankBusiDto);
						}
						
						RespCheckVO checkVo = new RespCheckVO(false,ErrorMsgEnum.ADD_OUT_LINE_REMIND);
						log.info("接收渠道接口，保交所资金账号："+respPojo.getFundAccount());
						checkVo.setMsg(mrsBankBusiDto.getAuthNo());
						realNameIng(custId);
						return checkVo;
					}else{
						//更改实名认证信息
						realNameAuth(custId);
					}
				//失败
				}else if(EChlReturnCode.FAILER.equals(respPojo.getRetrunCode())){
					mrsBankBusiDto.setBindStatus(BankBindType.TODO.getValue());
					if(null != busiDto){
						mrsBankBusiDto.setId(busiDto.getId());
						mrsBankBusiDto.setCreateTime(busiDto.getCreateTime());
						mrsBankBusiDto.setModifyTime(new Date());
						mrsBankBusiDtoMapper.updateByPrimaryKey(mrsBankBusiDto);
					}else{
						mrsBankBusiDtoMapper.insert(mrsBankBusiDto);
					}
					RespCheckVO reDto = new RespCheckVO(false, ErrorMsgEnum.ADD_CARD_FAIL);
					String reMsg = "";
					if (StringUtils.isNotEmpty(respPojo.getErrorMsg())) {
						if (respPojo.getErrorMsg().length() > 30) {
							reMsg = "[" + respPojo.getErrorMsg().subSequence(0, 30) + "]";
						} else {
							reMsg = "[" + respPojo.getErrorMsg() + "]";
						}
					}
					reDto.setMsg(ErrorMsgEnum.ADD_CARD_FAIL.getValue() + reMsg);
					return reDto;
				}
				if(null != busiDto){
					mrsBankBusiDto.setId(busiDto.getId());
					mrsBankBusiDto.setCreateTime(busiDto.getCreateTime());
					mrsBankBusiDto.setModifyTime(new Date());
					mrsBankBusiDtoMapper.updateByPrimaryKey(mrsBankBusiDto);
				}else{
					mrsBankBusiDtoMapper.insert(mrsBankBusiDto);
				}
				//TODO 默认卡设置判断
				List<MrsBankBusiDto> band = mrsBankBusiDtoAppService.findBandByCustId(mrsBankBusiDto.getCustId());
				if (band.size() == 1) {
					MrsBankBusiDto bands = band.get(0);
					bands.setIsDefault(EIsCrossBank.YES.getValue());
					mrsBankBusiDtoMapper.updateByPrimaryKey(bands);
				}
			}
			
		} catch (Exception e) {
			log.error("银保绑卡处理失败",e);
			log.error("失败原因：" + e.getMessage());
			return new RespCheckVO(false,ErrorMsgEnum.SYSTEM_ERROR);
		}
		return new RespCheckVO(true);
	}

	@Override
	public MrsBankBusiDto findByOnly(String bankType, String custName, String accNo) {
		return mrsBankBusiDtoMapper.findByOnly(bankType, custName, accNo);
	}

	@Override
	public List<MrsBankBusiDto> findByCustIdAndAduitStatus(String custId, String aduitStatus) {
		return mrsBankBusiDtoMapper.findByCustIdAndAduitStatus(custId,aduitStatus);
	}

	@Override
	public MrsBankBusiDto findById(String id) {
		MrsBankBusiDto mrsBankBusiDto = mrsBankBusiDtoMapper.selectByPrimaryKey(id);
		try {
			String bankType = mrsBankBusiDto.getBankType();
			TbChlBank tbChlBank = chlBankService.getBankByBankType(bankType);
			if (tbChlBank == null) {
				return mrsBankBusiDto;
			}else {
				mrsBankBusiDto.setBankName(tbChlBank.getBankName());
			}
		} catch (Exception e) {
			return mrsBankBusiDto;
		}
		return mrsBankBusiDto;
	}

	@Override
	@Transactional(value=CbsConstants.TX_MANAGER_MRS)
	public RespCheckVO aduit(AduitVo vo, String name,String userId) {
		RespCheckVO respVo = new RespCheckVO(true);
		try {
			MrsBankBusiDto mrsBankBusiDto = mrsBankBusiDtoMapper.selectByPrimaryKey(vo.getId());
			mrsBankBusiDto.setModifyTime(new Date());
			mrsBankBusiDto.setAduitStatus(vo.getAduitStatus());
			
			MrsPortalReviewAduitDto  aduitDto = mrsPortalReviewAduitDtoMapper.selectByBusiNoAndTypeWait(vo.getId(), AduitBusiType.AB2.getValue());
			aduitDto.setAduitStatus(vo.getAduitStatus());
			aduitDto.setAduitRemark(vo.getAduitRemark());
			aduitDto.setAduitUserId(userId);
			aduitDto.setAduitUserName(name);
			aduitDto.setAduitTime(new Date());
			mrsPortalReviewAduitDtoMapper.updateByPrimaryKey(aduitDto);
			//审核拒绝
			if(BankAduitType.FAIl.getValue().equals(vo.getAduitStatus())){
				//拒绝默认未签约
				mrsBankBusiDto.setBindStatus(BankBindType.TODO.getValue());
				mrsBankBusiDtoMapper.updateByPrimaryKey(mrsBankBusiDto);
			//审核成功
			}else if(BankAduitType.SUCCESS.getValue().equals(vo.getAduitStatus())){
				//调接口
				FrontAccountRespPojo respPojo = addCard(mrsBankBusiDto);
				mrsBankBusiDto.setErrorRemark(respPojo.getErrorMsg());
				System.out.println("调接口返回结果："+respPojo.getRetrunCode().getValue());
				log.info("调接口返回结果："+respPojo.getRetrunCode().getValue());
				if(StringUtils.isNotEmpty(respPojo.getReturnMsg())){
					log.info("返回信息："+respPojo.getReturnMsg());
				}
				if(StringUtils.isNotEmpty(respPojo.getReturnMsg())){
					log.info("返回信息："+respPojo.getErrorMsg());
				}
				
				mrsBankBusiDto.setAduitStatus(vo.getAduitStatus());
				//成功
				if(EChlReturnCode.SUCCESS.equals(respPojo.getRetrunCode())){
					
					//线下成功
					if(ECHLBusiMode.DOWN_LINE.equals(respPojo.getBusiMode())){
						mrsBankBusiDto.setAddMode(BankAddMode.BA0.getValue());
						mrsBankBusiDto.setBindStatus(BankBindType.TODO.getValue());
						mrsBankBusiDtoMapper.updateByPrimaryKey(mrsBankBusiDto);
						RespCheckVO checkVo = new RespCheckVO(false,ErrorMsgEnum.ADD_OUT_LINE_REMIND);
						log.info("接收渠道接口，保交所资金账号："+respPojo.getFundAccount());
						checkVo.setMsg(mrsBankBusiDto.getAuthNo());
						realNameIng(mrsBankBusiDto.getCustId());
						return checkVo;
					}else{
						mrsBankBusiDto.setBindStatus(BankBindType.SUCCESS.getValue());
						//实名认证
						realNameAuth(mrsBankBusiDto.getCustId());
					}
				//失败
				}else if(EChlReturnCode.FAILER.equals(respPojo.getRetrunCode())){
					mrsBankBusiDto.setBindStatus(BankBindType.TODO.getValue());
					mrsBankBusiDtoMapper.updateByPrimaryKey(mrsBankBusiDto);
					RespCheckVO reDto = new RespCheckVO(false, ErrorMsgEnum.ADD_CARD_FAIL);
					String reMsg = "";
					if (StringUtils.isNotEmpty(respPojo.getErrorMsg())) {
						if (respPojo.getErrorMsg().length() > 30) {
							reMsg = "[" + respPojo.getErrorMsg().subSequence(0, 30) + "]";
						} else {
							reMsg = "[" + respPojo.getErrorMsg() + "]";
						}
					}
					reDto.setMsg(ErrorMsgEnum.ADD_CARD_FAIL.getValue() + reMsg);
					return reDto;
				}
				mrsBankBusiDtoMapper.updateByPrimaryKey(mrsBankBusiDto);
				//TODO 默认卡设置判断
				List<MrsBankBusiDto> band = mrsBankBusiDtoAppService.findBandByCustId(mrsBankBusiDto.getCustId());
				if (band.size() == 1) {
					MrsBankBusiDto bands = band.get(0);
					bands.setIsDefault(EIsCrossBank.YES.getValue());
					mrsBankBusiDtoMapper.updateByPrimaryKey(bands);
				}
			}
			
		} catch (Exception e) {
			log.error("审核银行卡信息处理失败",e);
			log.error("失败原因：" + e.getMessage());
			return new RespCheckVO(false,ErrorMsgEnum.SYSTEM_ERROR);
		}
		return respVo;
	}

	@Override
	@Transactional(value=CbsConstants.TX_MANAGER_MRS)
	public RespCheckVO addBankByPortal(BankBusiReqVO bankBusiReqVO) {
		RespCheckVO respVo = new RespCheckVO(true);
		MrsBankBusiDto mrsBankBusiDto = new MrsBankBusiDto();
		try {
			BeanUtils.copyProperties(bankBusiReqVO, mrsBankBusiDto);
			mrsBankBusiDto.setAddMode(BankAddMode.BA1.getValue());
			mrsBankBusiDto.setIsDelete(IsDelete.IS_DELETE_N.getValue());
			mrsBankBusiDto.setCreateTime(new Date());
			mrsBankBusiDto.setId(UUID.randomUUID().toString());
			mrsBankBusiDtoMapper.insert(mrsBankBusiDto);
		} catch (Exception e) {
			log.error("保存银行卡信息处理失败",e);
			log.error("失败原因：" + e.getMessage());
			return new RespCheckVO(false,ErrorMsgEnum.SYSTEM_ERROR);
		}
		return respVo;
	}

	/**
	 * 授信解绑
	 */
	@Override
	@Transactional(value=CbsConstants.TX_MANAGER_MRS)
	public RespCheckVO delete(String id) {
		RespCheckVO respVo = new RespCheckVO(true);
		try {
			MrsBankBusiDto mrsBankBusiDto = mrsBankBusiDtoMapper.selectByPrimaryKey(id);
			if(BankPayType.BP2.getValue().equals(mrsBankBusiDto.getPayType())){
				//判断是否为线上
				log.info("调渠道查看该卡是否线上。。用户类型"+mrsBankBusiDto.getAccountType()+"渠道类型"+mrsBankBusiDto.getChannelCode());
				boolean isOnLine = chlFrontendBusiAppService.checkBusiMode(ECustType.getEnum(mrsBankBusiDto.getAccountType()), EChlChannelCode.getEChlChannelCode(mrsBankBusiDto.getChannelCode()), BusinessCodeEnum.CLOSE_ACCOUNT);
				log.info("渠道返回结果为："+(isOnLine?"线上":"线下"));
				if(isOnLine){
					//调接口
					FrontAccountRespPojo respPojo = removeCard(mrsBankBusiDto);
					mrsBankBusiDto.setErrorRemark(respPojo.getErrorMsg());
					log.info("调接口返回结果："+respPojo.getRetrunCode().getValue());
					if(StringUtils.isNotEmpty(respPojo.getReturnMsg())){
						log.info("返回信息："+respPojo.getReturnMsg());
					}
					if(StringUtils.isNotEmpty(respPojo.getReturnMsg())){
						log.info("返回信息："+respPojo.getErrorMsg());
					}
					
					
					mrsBankBusiDto.setModifyTime(new Date());
					mrsBankBusiDto.setIsDefault(EIsCrossBank.NO.getValue());
					//成功
					if(EChlReturnCode.SUCCESS.equals(respPojo.getRetrunCode())){
						mrsBankBusiDto.setBindStatus(BankBindType.CANCELLED.getValue());
						mrsBankBusiDtoMapper.updateByPrimaryKey(mrsBankBusiDto);
					//失败
					} else if (EChlReturnCode.FAILER.equals(respPojo.getRetrunCode())) {
						RespCheckVO reDto = new RespCheckVO(false, ErrorMsgEnum.REMOVE_CARD_FAIL);
						String reMsg = "";
						if (StringUtils.isNotEmpty(respPojo.getErrorMsg())) {
							if (respPojo.getErrorMsg().length() > 30) {
								reMsg = "[" + respPojo.getErrorMsg().subSequence(0, 30) + "]";
							} else {
								reMsg = "[" + respPojo.getErrorMsg() + "]";
							}
						}
						reDto.setMsg(ErrorMsgEnum.REMOVE_CARD_FAIL.getValue() + reMsg);
						return reDto;
					}
				}else{
					RespCheckVO vo = new RespCheckVO(false,ErrorMsgEnum.ADD_OUT_LINE_REMIND);
					vo.setMsg(mrsBankBusiDto.getAuthNo());
					return vo;
				}
			}else{
				mrsBankBusiDto.setBindStatus(BankBindType.TODO.getValue());
			}
				mrsBankBusiDtoMapper.updateByPrimaryKey(mrsBankBusiDto);
		} catch (Exception e) {
			log.error("删除银行卡信息处理失败",e);
			log.error("失败原因：" + e.getMessage());
			return new RespCheckVO(false,ErrorMsgEnum.SYSTEM_ERROR);
		}
		return respVo;
		
	}
	@Override
	@Transactional(value=CbsConstants.TX_MANAGER_MRS)
	public RespCheckVO delcard(String accNo, String custId) {
		RespCheckVO respVo = new RespCheckVO(true);
		try {
			List<MrsBankBusiDto> mrsBankBusiList = mrsBankBusiDtoMapper.findByAccnoAndCustid(accNo, custId);
			if (mrsBankBusiList != null && mrsBankBusiList.size() > 0) {
				for (MrsBankBusiDto bankDto : mrsBankBusiList) {
					if(BankBindType.SUCCESS.getValue().equals(bankDto.getBindStatus())){
						respVo.setCheckValue(false);
						respVo.setErrorMsg(ErrorMsgEnum.REMOVE_CARD_FAIL);
						respVo.setMsg("删除失败，银行卡存在已签约数据，请先解绑银行卡！");
						log.error("删除失败，存在为解绑数据，请先解绑银行卡！");
						return respVo;
					}
				}
				//直接删除
				for (MrsBankBusiDto bankDto : mrsBankBusiList) {
					mrsBankBusiDtoMapper.deleteByPrimaryKey(bankDto.getId());
				}
			} else {
				respVo.setCheckValue(false);
				respVo.setErrorMsg(ErrorMsgEnum.REMOVE_CARD_FAIL);
				respVo.setMsg("删除失败，没有找到对应的银行卡数据！");
				log.error("删除失败，没有找到对应的银行卡数据！");
				return respVo;
			}
		} catch (Exception e) {
			log.error("删除银行卡信息处理失败", e);
			log.error("失败原因：" + e.getMessage());
			return new RespCheckVO(false, ErrorMsgEnum.SYSTEM_ERROR);
		}
		return respVo;

	}
	@Override
	public RespCheckVO update(String id,String name,String userId) {
		try {
			MrsBankBusiDto mrsBankBusiDto = mrsBankBusiDtoMapper.selectByPrimaryKey(id);
			//调接口
			if(MrsCustomerType.MCT_0.getValue().equals(mrsBankBusiDto.getCustType())){
				FrontAccountRespPojo respPojo = addCard(mrsBankBusiDto);
				mrsBankBusiDto.setErrorRemark(respPojo.getErrorMsg());
				log.info("调接口返回结果："+respPojo.getRetrunCode().getValue());
				if(StringUtils.isNotEmpty(respPojo.getReturnMsg())){
					log.info("返回信息："+respPojo.getReturnMsg());
				}
				if(StringUtils.isNotEmpty(respPojo.getReturnMsg())){
					log.info("返回信息："+respPojo.getErrorMsg());
				}
				mrsBankBusiDto.setModifyTime(new Date());
				
				//成功
				if(EChlReturnCode.SUCCESS.equals(respPojo.getRetrunCode())){
					//线下成功
					if(ECHLBusiMode.DOWN_LINE.equals(respPojo.getBusiMode())){
						mrsBankBusiDto.setBindStatus(BankBindType.TODO.getValue());
						mrsBankBusiDto.setAddMode(BankAddMode.BA0.getValue());
						mrsBankBusiDtoMapper.updateByPrimaryKey(mrsBankBusiDto);
						RespCheckVO vo = new RespCheckVO(false,ErrorMsgEnum.ADD_OUT_LINE_REMIND);
						vo.setMsg(respPojo.getFundAccount());
						return vo;
					}else{
						mrsBankBusiDto.setBindStatus(BankBindType.SUCCESS.getValue());
					}
				//失败
				}else if(EChlReturnCode.FAILER.equals(respPojo.getRetrunCode())){
					mrsBankBusiDto.setBindStatus(BankBindType.TODO.getValue());
					mrsBankBusiDtoMapper.updateByPrimaryKey(mrsBankBusiDto);
					RespCheckVO reDto = new RespCheckVO(false, ErrorMsgEnum.ADD_CARD_FAIL);
					String reMsg = "";
					if (StringUtils.isNotEmpty(respPojo.getErrorMsg())) {
						if (respPojo.getErrorMsg().length() > 30) {
							reMsg = "[" + respPojo.getErrorMsg().subSequence(0, 30) + "]";
						} else {
							reMsg = "[" + respPojo.getErrorMsg() + "]";
						}
					}
					reDto.setMsg(ErrorMsgEnum.ADD_CARD_FAIL.getValue() + reMsg);
					return reDto;
				}
				mrsBankBusiDtoMapper.updateByPrimaryKey(mrsBankBusiDto);
				//TODO 默认卡设置判断
				List<MrsBankBusiDto> band = mrsBankBusiDtoAppService.findBandByCustId(mrsBankBusiDto.getCustId());
				if (band.size() == 1) {
					MrsBankBusiDto bands = band.get(0);
					bands.setIsDefault(EIsCrossBank.YES.getValue());
					mrsBankBusiDtoMapper.updateByPrimaryKey(bands);
				}
			}else {
				//审核表
				MrsPortalReviewAduitDto	aduitDto =new MrsPortalReviewAduitDto();
				aduitDto.setId(UUID.randomUUID().toString());//
				aduitDto.setBusiNo(mrsBankBusiDto.getId());
				aduitDto.setBusiType(AduitBusiType.AB2.getValue());
				aduitDto.setCreateRemark("银保绑卡");
				aduitDto.setCreateTime(new Date());
				aduitDto.setCreateUserName(name);
				aduitDto.setCreateUserNo(userId);
				aduitDto.setCustId(mrsBankBusiDto.getCustId());
				aduitDto.setAduitStatus(BankAduitType.WAIT.getValue());
				mrsPortalReviewAduitDtoMapper.insert(aduitDto);
				mrsBankBusiDto.setAduitStatus(BankAduitType.WAIT.getValue());//待审核
				mrsBankBusiDto.setBindStatus(BankBindType.TODO.getValue());
				mrsBankBusiDtoMapper.updateByPrimaryKey(mrsBankBusiDto);
				
				return new RespCheckVO(false,ErrorMsgEnum.SUCCESS_NEED_ADUIT);
				
			}
			
		} catch (Exception e) {
			log.error("重新绑定银行卡信息处理失败",e);
			return new RespCheckVO(false,ErrorMsgEnum.SYSTEM_ERROR);
		}
		return new RespCheckVO(true);
	}


	@Override
	public PageData<MrsBankBusiDto> findbank(PageData<MrsBankBusiDto> pageData, MrsBankBusiDto searchDto) {
		PageHelper.startPage(pageData.getPageNumber(), pageData.getPageSize());
		List<MrsBankBusiDto> list = mrsBankBusiDtoMapper.pageBank(searchDto);
		if (list != null && list.size() != 0) {
			List<TbChlBank> banks = chlBankService.getBanks();
			for (int i = 0; i < list.size(); i++) {
				String bankType = list.get(i).getBankType();
				for (int j = 0; j < banks.size(); j++) {
					if (bankType.equals(banks.get(j).getBankType())) {
						MrsBankBusiDto bankBusiDto = list.get(i);
						bankBusiDto.setBankName(banks.get(j).getBankName());
						list.set(i,bankBusiDto);
					}
				}
			}
		}
		Page<MrsBankBusiDto> page = (Page<MrsBankBusiDto>) list;
		pageData.setTotal(page.getTotal());
		pageData.setRows(list);
		return pageData;
	}
	
	/**
	 * 银行卡实体
	 * @param vo
	 * @param type
	 * @param custId
	 * @return
	 */
	public MrsBankBusiDto getBankDto(BankAddVo vo, String type,String custId) {
		MrsBankBusiDto mrsBankBusiDto = new MrsBankBusiDto();
		mrsBankBusiDto.setAccName(vo.getCustName());//
		mrsBankBusiDto.setBankType(vo.getBankType());
		mrsBankBusiDto.setCardType(vo.getCardType());
		mrsBankBusiDto.setAccNo(vo.getAccNo());//
		mrsBankBusiDto.setAddMode(BankAddMode.BA1.getValue());//
		mrsBankBusiDto.setCustType(type);;//
		mrsBankBusiDto.setBankCode(vo.getBankCode());
		mrsBankBusiDto.setCityCode(vo.getCity());
		mrsBankBusiDto.setCreateTime(new Date());
		mrsBankBusiDto.setCustId(custId);
		mrsBankBusiDto.setCustName(vo.getCustName());
		mrsBankBusiDto.setId(UUID.randomUUID().toString());
		mrsBankBusiDto.setIdNo(vo.getIdNo());
		mrsBankBusiDto.setIdType(vo.getIdType());
//		mrsBankBusiDto.setIsDefault(vo.getIsDefault());//是否默认卡
		mrsBankBusiDto.setIsDelete(IsDelete.IS_DELETE_N.getValue());
		mrsBankBusiDto.setRegionCode(vo.getRegionCode());
		mrsBankBusiDto.setProvinceCode(vo.getProvince());
		mrsBankBusiDto.setOpenBankName(vo.getOpenBankName());
		//中行才设密码
		if(BOC_BANK_TYPE.equals(vo.getBankType())){
			mrsBankBusiDto.setBankMoblePwd(vo.getBankMoblePwd());
		}
//		String custIdOne = custId.substring(0, 1);
//		if("8".equals(custIdOne)){
//			mrsBankBusiDto.setAccountType(BankAccountType.PRIV.getValue());;
//		}else if("9".equals(custIdOne)){
//			mrsBankBusiDto.setAccountType(BankAccountType.PUB.getValue());;
//		}
		if(MrsCustomerType.MCT_0.getValue().equals(type)){
			mrsBankBusiDto.setAccountType(ECustType.PERSON.getValue());
		}else{
			mrsBankBusiDto.setAccountType(ECustType.JIGOU.getValue());
		}
		//资金账号
		String futuresAccount = "";
		ActAccountDto actAccountDto = mrsAccountAppService.findByCustIdSubBusiType(custId,
					SubAcctType.BASE.getValue(), EActBusiRefSubBusiType.BALANCE_ACCOUNT.getValue());
		if(null != actAccountDto){
			futuresAccount = actAccountDto.getAccountId();
		}else {
			throw new RuntimeException("不存在资金账号！");
		}
		
		mrsBankBusiDto.setAuthNo(futuresAccount);
		return mrsBankBusiDto;
	}
	/**
	 * 授信绑卡调接口
	 */
	public FrontAccountRespPojo addCard(MrsBankBusiDto mrsBankBusiDto){
		
		FrontAccountPojo frontAccountPojo = getFrontAccountPojo(mrsBankBusiDto);
		frontAccountPojo.setAccountCode(EChlFrontAccountCode.OPEN_ACCOUNT);
		return chlFrontAccountAppService.openAccount(frontAccountPojo);
	}
	
	/**
	 * 授信解绑调接口
	 */
	public FrontAccountRespPojo removeCard(MrsBankBusiDto mrsBankBusiDto){
		FrontAccountPojo frontAccountPojo = getFrontAccountPojo(mrsBankBusiDto);
		frontAccountPojo.setAccountCode(EChlFrontAccountCode.CANCEL_ACCOUNT);
		return chlFrontAccountAppService.cancelAccount(frontAccountPojo);
	}
	
	/**
	 * 获取渠道request实体
	 * @param mrsBankBusiDto
	 * @return
	 */
	public FrontAccountPojo getFrontAccountPojo(MrsBankBusiDto mrsBankBusiDto){
		FrontAccountPojo frontAccountPojo = new FrontAccountPojo();
		frontAccountPojo.setChannelCode(EChlChannelCode.getEChlChannelCode(mrsBankBusiDto.getChannelCode()));
		frontAccountPojo.setCustName(mrsBankBusiDto.getCustName());
		if(BOC_BANK_TYPE.equals(mrsBankBusiDto.getBankType())){
			try {
				frontAccountPojo.setPwd(Des3.decode(mrsBankBusiDto.getBankMoblePwd(), mrsBankBusiDto.getCustId()));
			} catch (Exception e) {
				log.error("处理中国银行des3解密key失败", e);
				throw new RuntimeException("处理中国银行des3解密key失败");
			}
		}
		frontAccountPojo.setAccName(mrsBankBusiDto.getAccName());
		frontAccountPojo.setAccNo(mrsBankBusiDto.getAccNo());
		frontAccountPojo.setOpenBankNo(mrsBankBusiDto.getBankCode());
		frontAccountPojo.setCurrency(ECurrenoy.RMB.getValue());
		frontAccountPojo.setCustType(mrsBankBusiDto.getAccountType());
		
		frontAccountPojo.setIdNo(mrsBankBusiDto.getIdNo());
		if(MrsCredentialsType.MCT_01.getValue().equals(mrsBankBusiDto.getIdType())){
			frontAccountPojo.setIdType("10");
		}else if(MrsCredentialsType.MCT_73.getValue().equals(mrsBankBusiDto.getIdType())){
			frontAccountPojo.setIdType("16");
		}else if(MrsCredentialsType.MCT_71.getValue().equals(mrsBankBusiDto.getIdType())){
			frontAccountPojo.setIdType("17");
		}else{
			frontAccountPojo.setIdType("20");
		}
		
		frontAccountPojo.setOpenBankName(mrsBankBusiDto.getOpenBankName());
		frontAccountPojo.setCustId(mrsBankBusiDto.getCustId());
		//资金子账户
		frontAccountPojo.setFuturesAccount(mrsBankBusiDto.getAuthNo());
		return frontAccountPojo;
	}

	@Override
	public List<MrsBankBusiDto> findBanksByCustId(String custId) {
		return mrsBankBusiDtoMapper.findBanksByCustId(custId);
	}
	
	@Override
	public List<MrsBankBusiDto> findBandByCustId(String custId) {
		return mrsBankBusiDtoMapper.findBandByCustId(custId);
	}

	@Override
	public List<MrsBankBusiDto> findByCustIdAndAduitStatusAndSuccess(String custId, String aduitStatus) {
		return mrsBankBusiDtoMapper.findByCustIdAndAduitStatusAndSuccess(custId, aduitStatus);
	}

	@Override
	@Transactional(value=CbsConstants.TX_MANAGER_MRS)
	public RespCheckVO updateBankCardStatus(String channelCode, String accNo, String operType) {
		log.info("银行卡号："+accNo+",绑定状态："+operType);
		RespCheckVO respVo = new RespCheckVO(true);
		log.info("处理银行卡状态变更：请求参数channelCode="+channelCode+";accNo="+accNo+";operType="+operType);
		try {
			if (org.apache.commons.lang.StringUtils.isEmpty(channelCode)
					|| org.apache.commons.lang.StringUtils.isEmpty(accNo)
					|| org.apache.commons.lang.StringUtils.isEmpty(operType)) {
				log.info(ErrorMsgEnum.VALUE_ERROR.getValue());
				return new RespCheckVO(false, ErrorMsgEnum.VALUE_ERROR);
			}
			String oldbindStatus;
			String newbindStatus;
			// 绑定
			if (EMrsCarinfoStatus.BINDING.getValue().equals(operType)) {
				oldbindStatus = BankBindType.TODO.getValue();
				newbindStatus = BankBindType.SUCCESS.getValue();
			}
			// 解绑
			else if (EMrsCarinfoStatus.UNBUNDLING.getValue().equals(operType)) {
				oldbindStatus = BankBindType.SUCCESS.getValue();
				newbindStatus = BankBindType.TODO.getValue();
			} else {
				log.info(ErrorMsgEnum.VALUE_ERROR.getValue());
				return new RespCheckVO(false, ErrorMsgEnum.VALUE_ERROR);
			}
			List<MrsBankBusiDto> bankList = mrsBankBusiDtoMapper.findBanksBychnlAndAccno(channelCode, accNo,
					oldbindStatus);
			if (bankList == null || bankList.size() == 0) {
				log.info(ErrorMsgEnum.VALUE_ERROR.getValue());
				return new RespCheckVO(false, ErrorMsgEnum.VALUE_ERROR);
			}
			log.info("处理银行卡状态变更：查询原银行记录数据="+bankList.size());
			MrsBankBusiDto bankDto = bankList.get(0);
			bankDto.setBindStatus(newbindStatus);
			mrsBankBusiDtoMapper.updateByPrimaryKeySelective(bankDto);
			
			//修改实名认证状态
			realNameAuth(bankDto.getCustId());
			log.info("处理银行卡状态变更完成：更新银行卡状态为="+newbindStatus);
			//TODO 默认卡设置判断
			List<MrsBankBusiDto> band = mrsBankBusiDtoAppService.findBandByCustId(bankDto.getCustId());
			if (band.size() == 1) {
				MrsBankBusiDto bands = band.get(0);
				bands.setIsDefault(EIsCrossBank.YES.getValue());
				mrsBankBusiDtoMapper.updateByPrimaryKey(bands);
			}
		} catch (Exception e) {
			log.error("渠道同步银行卡状态处理失败", e);
			return new RespCheckVO(false, ErrorMsgEnum.SYSTEM_ERROR);
		}
		log.info("操作成功!");
		return respVo;
	}
	
	/**
	 * 更新实名认证状态已认证
	 * @param custId
	 */
	public void realNameAuth(String custId){
		MrsAccountDto mrsAccountDto= mrsAccountDtoMapper.findByCustId(custId);
		if(!MrsAccountAuthStatus.MAAS_2.getValue().equals(mrsAccountDto.getAuthStatus())){
			//
			mrsAccountDto.setAuthStatus(MrsAccountAuthStatus.MAAS_2.getValue());
			mrsAccountDto.setAccountStatus(MrsAccountStatus.ACCOUNT_STATUS_0.getValue());
			mrsAccountDtoMapper.updateByPrimaryKey(mrsAccountDto);
			//更新子表
			List<MrsSubAccountDto> mrsSubAccountDtos= mrsSubAccountDtoMapper.findByCustId(custId);
			if(mrsSubAccountDtos != null && mrsSubAccountDtos.size()>0){
				for(MrsSubAccountDto mrsSubAccountDto:mrsSubAccountDtos){
					mrsSubAccountDto.setSubAccountStatus(MrsSubAccountStatus.MSAS_0.getValue());
					mrsSubAccountDtoMapper.updateByPrimaryKey(mrsSubAccountDto);
				}
			}
			//更新用户信息
			if(MrsCustomerType.MCT_0.getValue().equals(mrsAccountDto.getCustomerType())){
				MrsPersonDto mrsPersonDto = mrsPersonAppService.findByCustId(custId);
				mrsPersonAppService.updateBaseAndSync(mrsPersonDto);
			}else{
				try {
					MrsOrganDto mrsOrganDto = mrsOrganAppService.findByCustId(custId);
					mrsOrganAppService.updateBaseAndSync(mrsOrganDto);
				} catch (PortalCheckedException e) {
					log.error("获取机构用户失败！",e);
				}
			}
			
		}
	}
	
	public void realNameIng(String custId){
		MrsAccountDto mrsAccountDto= mrsAccountDtoMapper.findByCustId(custId);
		if(!MrsAccountAuthStatus.MAAS_2.getValue().equals(mrsAccountDto.getAuthStatus())&& !MrsAccountAuthStatus.MAAS_9.getValue().equals(mrsAccountDto.getAuthStatus())){
			//
			mrsAccountDto.setAuthStatus(MrsAccountAuthStatus.MAAS_4.getValue());
			mrsAccountDto.setAccountStatus(MrsAccountStatus.ACCOUNT_STATUS_9.getValue());
			mrsAccountDtoMapper.updateByPrimaryKey(mrsAccountDto);
			//更新子表
			List<MrsSubAccountDto> mrsSubAccountDtos= mrsSubAccountDtoMapper.findByCustId(custId);
			if(mrsSubAccountDtos != null && mrsSubAccountDtos.size()>0){
				for(MrsSubAccountDto mrsSubAccountDto:mrsSubAccountDtos){
					mrsSubAccountDto.setSubAccountStatus(MrsSubAccountStatus.MSAS_9.getValue());
					mrsSubAccountDtoMapper.updateByPrimaryKey(mrsSubAccountDto);
				}
			}
		}
		
	}

	@Override
	public MrsBankBusiDto findByAccnoAndCustidAndPaytype(String accNo, String custId, String payType) {
		return mrsBankBusiDtoMapper.findByAccnoAndCustidAndPaytype(accNo, custId, payType);
	}
	
	// 检查是否为工作时间
	public boolean WorkTime(String channelCode){
		try {
			// 获取工作模式
			String workMode = getChannelParam(channelCode, WORK_MODE);
			// 获取工作时间
			String workTime = getChannelParam(channelCode, WORK_TIME);
			// 如果是工作日模式，则检查当前日期是否是工作日，即周一至周五
			String info = null;
			if (EWorkMode.WORKINGDAY.getValue().equals(workMode)) {
				// 如果当前日期为非工作，则抛出错误信息
				if (!WeekdayUtil.isCurWeekDay()) {
					info = String.format("渠道[%s]只支持工作日([%s]).", channelCode, workTime);
					log.info(info);
					return false;
				}
			}else {
				
			}
			// 检查工作时间
			String[] workTimeArr = workTime.split("~");
			if (workTimeArr != null && workTimeArr.length == 2) {
				// 获取当前时间
				String time = DateUtils.getTime1(new Date()).substring(0, 5);
				if (time.compareTo(workTimeArr[0]) < 0 || time.compareTo(workTimeArr[1]) > 0) {
					info = String.format("渠道[%s]只支持工作时间(%s).", channelCode, workTime);
					log.info(info);
					return false;
				}
			} else {
				info = String.format("渠道[%s]工作时间配置错误.", channelCode);
				log.info(info);
				return false;
			}
		} catch (Exception e) {
			log.info("检查工作时间有误",e);
			return false;
		}
		return true;
	}
	/**
	 * 根据渠道编码、参数代码查询渠道参数信息表获取渠道参数值
	 * 
	 * @param channelCode
	 *            渠道编码
	 * @param paramCode
	 *            参数代码
	 * @return
	 */
	public String getChannelParam(String channelCode, String paramCode) {
		TbChlParamDto chlParam = chlParamAppService.selectByChannelCodeAndParam(channelCode, paramCode);
		if (chlParam == null || StringUtils.isBlank(chlParam.getParamValue())) {
			String error = String.format("渠道编码[%s]未配置参数[%s].", channelCode, paramCode);
			log.info(error);
			throw new CodeUncheckException("99", error);
		}
		return chlParam.getParamValue();
	}
	@Override
	public UserCheckVO updateDefault(MrsBankBusiDto mrsBankBusiDto) {
		try {
			mrsBankBusiDto.setModifyTime(new Date());
			mrsBankBusiDtoMapper.updateByPrimaryKey(mrsBankBusiDto);
			return new UserCheckVO(true);
		} catch (Exception e) {
			log.error("默认卡删除失败:", e);
			return new UserCheckVO(false,ErrorMsgEnum.SYSTEM_ERROR);
		}
	}
	@Override
	public MrsBankBusiDto findIsDefault(MrsBankBusiDto mrsBankBusiDto) {
			String custId = mrsBankBusiDto.getCustId();
			return mrsBankBusiDtoMapper.findIsDefault(custId);
	}

	/**
	 * 快捷解绑
	 */
	@Override
	public RespCheckVO delQuick(BankBusiReqVO bankBusiReqVO) {
		RespCheckVO respVo = new RespCheckVO(true);
		try {
			MrsBankBusiDto dto = mrsBankBusiDtoAppService.findById(bankBusiReqVO.getId());
			//校验卡号、一户通号、绑卡方式是否一致
			MrsBankBusiDto mrsBankBusiDto = mrsBankBusiDtoMapper.findByAccnoAndCustidAndPaytype(
					dto.getAccNo(), bankBusiReqVO.getCustId(), BankPayType.BP1.getValue());
			if(null == mrsBankBusiDto){
				log.error("银行卡号和一户通绑定信息不一致");
				return new RespCheckVO(false,ErrorMsgEnum.SYSTEM_ERROR);
			}
			//调接口
	    	UnBindFastPayPojo unBindFastPayPojo = new UnBindFastPayPojo();
	    	unBindFastPayPojo.setBankAccountNo(mrsBankBusiDto.getAccNo());
	    	unBindFastPayPojo.setChannelCode(EChlChannelCode.getEChlChannelCode(mrsBankBusiDto.getChannelCode()));
	    	
	    	BaseRespPojo respPojo = fastPayAppService.UnBindFastPay(unBindFastPayPojo);
	    	
	    	mrsBankBusiDto.setErrorRemark(respPojo.getErrorMsg());
			log.info("调接口返回结果："+respPojo.getRetrunCode().getValue());
			if(StringUtils.isNotEmpty(respPojo.getReturnMsg())){
				log.info("返回信息："+respPojo.getReturnMsg());
			}
			if(StringUtils.isNotEmpty(respPojo.getReturnMsg())){
				log.info("返回信息："+respPojo.getErrorMsg());
			}
			
			mrsBankBusiDto.setModifyTime(new Date());
			mrsBankBusiDto.setIsDefault(EIsCrossBank.NO.getValue());
			//成功
			if(EChlReturnCode.SUCCESS.equals(respPojo.getRetrunCode())){
				mrsBankBusiDto.setBindStatus(BankBindType.CANCELLED.getValue());
				mrsBankBusiDtoMapper.updateByPrimaryKey(mrsBankBusiDto);
			//失败
			} else if (EChlReturnCode.FAILER.equals(respPojo.getRetrunCode())) {
				RespCheckVO reDto = new RespCheckVO(false, ErrorMsgEnum.REMOVE_CARD_FAIL);
				String reMsg = "";
				if (StringUtils.isNotEmpty(respPojo.getErrorMsg())) {
					if (respPojo.getErrorMsg().length() > 30) {
						reMsg = "[" + respPojo.getErrorMsg().subSequence(0, 30) + "]";
					} else {
						reMsg = "[" + respPojo.getErrorMsg() + "]";
					}
				}
				reDto.setMsg(ErrorMsgEnum.REMOVE_CARD_FAIL.getValue() + reMsg);
				return reDto;
			}
		} catch (Exception e) {
			log.error("快捷解绑银行卡信息处理失败",e);
			log.error("失败原因：" + e.getMessage());
			return new RespCheckVO(false,ErrorMsgEnum.SYSTEM_ERROR);
		}
		
		return respVo;
	}

	@Override
	@Transactional(value=CbsConstants.TX_MANAGER_MRS)
	public RespCheckVO bindBankCard(BankCardRequestVo bankCard) {
		try {
			MrsBankBusiDto mrsBankBusiDto = new MrsBankBusiDto();
			BeanUtils.copyProperties(bankCard, mrsBankBusiDto);
			mrsBankBusiDto.setAddMode(BankAddMode.BA1.getValue());//
			mrsBankBusiDto.setCreateTime(new Date());
			mrsBankBusiDto.setId(UUID.randomUUID().toString());
//			mrsBankBusiDto.setIsDefault(vo.getIsDefault());//是否默认卡
			mrsBankBusiDto.setIsDelete(IsDelete.IS_DELETE_N.getValue());
//			//中行才设密码
//			if(BOC_BANK_TYPE.equals(vo.getBankType())){
//				mrsBankBusiDto.setBankMoblePwd(vo.getBankMoblePwd());
//			}
//			if(MrsCustomerType.MCT_0.getValue().equals(type)){
//				mrsBankBusiDto.setAccountType(ECustType.PERSON.getValue());
//			}else{
//				mrsBankBusiDto.setAccountType(ECustType.JIGOU.getValue());
//			}
			//资金账号
			String futuresAccount = "";
			ActAccountDto actAccountDto = mrsAccountAppService.findByCustIdSubBusiType(bankCard.getCustId(),
						SubAcctType.BASE.getValue(), EActBusiRefSubBusiType.BALANCE_ACCOUNT.getValue());
			if(null != actAccountDto){
				futuresAccount = actAccountDto.getAccountId();
			}else {
				throw new RuntimeException("不存在资金账号！");
			}
			mrsBankBusiDto.setAuthNo(futuresAccount);
			TbChlBank tbChlBank =tbChlBankMapper.findByChltypeAndbankType("03", mrsBankBusiDto.getBankType());
			mrsBankBusiDto.setBankName(tbChlBank.getBankName());//暂写
			mrsBankBusiDto.setBindStatus(BankBindType.SUCCESS.getValue());//绑定状态
			mrsBankBusiDto.setPayType(BankPayType.BP1.getValue());//绑卡类型
			mrsBankBusiDto.setAduitStatus(BankAduitType.SUCCESS.getValue());//审核成功s
			mrsBankBusiDto.setCreateTime(new Date());
			//渠道编码
			mrsBankBusiDto.setChannelCode(tbChlBank.getChannlCode());
	    	
			mrsBankBusiDtoMapper.insert(mrsBankBusiDto);
			
			// 默认卡设置判断
			List<MrsBankBusiDto> band = mrsBankBusiDtoAppService.findBandByCustId(mrsBankBusiDto.getCustId());
			if (band.size() == 1) {
				MrsBankBusiDto bands = band.get(0);
				bands.setIsDefault(EIsCrossBank.YES.getValue());
				mrsBankBusiDtoMapper.updateByPrimaryKey(bands);
			}
		} catch (Exception e) {
			log.error("保存银行卡处理失败",e);
			return new RespCheckVO(false,ErrorMsgEnum.SYSTEM_ERROR);
		} 
		return new RespCheckVO(true);
	}


}
