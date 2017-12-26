package com.ylink.inetpay.cbs.bis.service;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.ylinkpay.framework.core.model.PageData;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.gson.Gson;
import com.shie.constant.SHIEConfigConstant;
import com.ylink.eu.util.tools.DateUtil;
import com.ylink.inetpay.cbs.bis.cache.BisSmsTemplateDtoCache;
import com.ylink.inetpay.cbs.bis.dao.BisSmsDtoMapper;
import com.ylink.inetpay.cbs.common.CbsConstants;
import com.ylink.inetpay.cbs.ucs.sec.dao.UcsSecUserDtoMapper;
import com.ylink.inetpay.cbs.util.CbsExceptionLogDtoUtils;
import com.ylink.inetpay.cbs.util.HttpSendUtil;
import com.ylink.inetpay.common.core.constant.EBisExceptionLogNlevel;
import com.ylink.inetpay.common.core.constant.EBisExceptionLogType;
import com.ylink.inetpay.common.core.constant.EBisSendStatus;
import com.ylink.inetpay.common.core.constant.EBisSmsSystem;
import com.ylink.inetpay.common.core.constant.EBisTemplateCode;
import com.ylink.inetpay.common.core.constant.ENormalDisabledStatus;
import com.ylink.inetpay.common.core.constant.EUcsSecUserStatus;
import com.ylink.inetpay.common.core.constant.EmessType;
import com.ylink.inetpay.common.core.constant.EsendChannel;
import com.ylink.inetpay.common.core.constant.EsendType;
import com.ylink.inetpay.common.project.access.app.SmsManageAppService;
import com.ylink.inetpay.common.project.access.constant.SmsRespCode;
import com.ylink.inetpay.common.project.access.dto.SmsSendDto;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisSmsDto;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisSmsTemplateDto;
import com.ylink.inetpay.common.project.cbs.dto.ucs.sec.UcsSecUserDto;
import com.ylink.inetpay.common.project.portal.enums.ErrorMsgEnum;
import com.ylink.inetpay.common.project.portal.vo.UserCheckVO;
import com.ylink.inetpay.common.project.portal.vo.sms.SendRespVO;
import com.ylink.inetpay.common.project.portal.vo.sms.SmsReqVO;

@Service("bisSmsService")
public class BisSmsServiceImpl implements BisSmsService {
	@Autowired
	private BisSmsDtoMapper bisSmsDtoMapper;
	@Autowired
	private BisSmsTemplateDtoCache bisSmsTemplateDtoCache;
	@Autowired
	private BisSysParamService bisSysParamService;
	@Autowired
	private UcsSecUserDtoMapper ucsSecUserDtoMapper;
	@Autowired
	private SmsManageAppService smsManageAppService;
	@Autowired
	private TaskExecutor taskExecutor;

	@Autowired
	private BisExceptionLogService bisExceptionLogService;
	
	private static Logger _log = LoggerFactory.getLogger(BisSmsServiceImpl.class);

	@Override
	public PageData<BisSmsDto> findListPage(PageData<BisSmsDto> pageDate,
			BisSmsDto bisSmsDto) {
		PageHelper.startPage(pageDate.getPageNumber(), pageDate.getPageSize());
		List<BisSmsDto> findListPage = bisSmsDtoMapper.list(bisSmsDto);
		Page<BisSmsDto> page = (Page<BisSmsDto>) findListPage;
		pageDate.setTotal(page.getTotal());
		pageDate.setRows(findListPage);
		return pageDate;
	}

	@Override
	public BisSmsDto details(String id) {
		return bisSmsDtoMapper.selectByPrimaryKey(id);
	}

	@Override
	public void flushSms() {
		SimpleDateFormat format = new SimpleDateFormat("HHmm");
		String time = format.format(new Date());
		//修改此次需要发送的短信记录为发送中
		List<BisSmsDto> smsDtos = bisSmsDtoMapper.getSms(time,EBisSendStatus.STAY_SEND);
		// 获取短信发送的配置信息，调用前置系统的短信发送接口发送短信。
		/*String url = bisSysParamService.getValue(SystemParamConstants.SMS_URL);
		String mer = bisSysParamService.getValue(SystemParamConstants.SMS_MER);
		String pw = bisSysParamService.getValue(SystemParamConstants.SMS_PW);
		String port = bisSysParamService.getValue(SystemParamConstants.SMS_PORT);*/
		for (BisSmsDto bisSmsDto : smsDtos) {
			if(bisSmsDto.getSendChannel().equals(EsendChannel.SHOR_MESSAGE)){
				//更新发送记录为发送中
				if(updateStatus(bisSmsDto.getId())){
					SmsSendDto smsSendDto = new SmsSendDto();
					/*smsSendDto.setSmsAddress(url);
					smsSendDto.setSmsPort(port);
					smsSendDto.setSmsName(mer);
					smsSendDto.setSmsPassword(pw);*/
					smsSendDto.setContent(bisSmsDto.getSendContent());
					smsSendDto.setPhone(bisSmsDto.getMobile());
					try {
						SmsRespCode resCode=null;
						if(bisSmsDto.getMobile().split(",").length>1){
							_log.info("调用前置系统群发短信:收件人【"+smsSendDto.getPhone()+"】短信内容【"+smsSendDto.getContent()+"】");
							resCode = smsManageAppService.sendSmsGroupMsg(smsSendDto);
						}else{
							_log.info("调用前置系统单发短信:收件人【"+smsSendDto.getPhone()+"】短信内容【"+smsSendDto.getContent()+"】");
							resCode = smsManageAppService.sendSmsMsg(smsSendDto);
						}
						if(resCode.equals(SmsRespCode.Success)){
							bisSmsDto.setStatus(EBisSendStatus.SEND_SUCCESS);
						}else {
							bisSmsDto.setStatus(EBisSendStatus.SEND_FAIL);
							bisSmsDto.setFailReason(resCode.getDisplayName());
							_log.info("发送短信失败:收件人【"+smsSendDto.getPhone()+"】短信内容【"+smsSendDto.getContent()+"】原因："+resCode.getDisplayName());
						}
					}catch (Exception e) {
						bisSmsDto.setStatus(EBisSendStatus.SEND_FAIL);
						bisSmsDto.setFailReason("发送短信失败，调用短信前置系统短信发送接口异常");
						String errorMsg="发送短信失败，调用短信前置系统短信发送接口异常"+e;
						_log.error(errorMsg);
						bisExceptionLogService.saveLog(CbsExceptionLogDtoUtils.getBisExceptionLog(EBisExceptionLogNlevel.ERROR, EBisExceptionLogType.SEND_SMS, errorMsg));
					}
					bisSmsDto.setSendTime(new Date());
					bisSmsDtoMapper.updateByPrimaryKey(bisSmsDto);
				}else{
					_log.info("短信不是待发送状态，短信记录ID【"+bisSmsDto.getId()+"】短信内容【"+bisSmsDto.getSendContent()+"】");
				}
			}else if(bisSmsDto.getSendChannel().equals(EsendChannel.APP)){
				_log.info("定时发送app消息，功能未实现");
			}else if(bisSmsDto.getSendChannel().equals(EsendChannel.WECHAT)){
				_log.info("定时发送微信消息，功能未实现");
			}
		}
	}
	
	/**
	 * 判断是否修改记录，防止短信重复发送
	 * @param id
	 * @return
	 */
	private boolean updateStatus(String id){
		return bisSmsDtoMapper.updateStatus(EBisSendStatus.SEND_IN,EBisSendStatus.STAY_SEND,id)==1;
	}

	@Override
	public UserCheckVO sendSms(String tel, EBisSmsSystem smsSystem,List<String> params, EBisTemplateCode smsTemplateCode,EsendChannel sendChannel,EmessType messType) {
		// 获取模板
		BisSmsTemplateDto smsTempla = bisSmsTemplateDtoCache.getSmsTempla(smsTemplateCode);
		if (smsTempla == null) {
			String errorMsg="发送短信失败：【"+smsTemplateCode.getDisplayName()+"】短信模板不存在";
			_log.info(errorMsg);
			bisExceptionLogService.saveLog(CbsExceptionLogDtoUtils.getBisExceptionLog(EBisExceptionLogNlevel.ERROR, EBisExceptionLogType.SEND_SMS, errorMsg));
			return new UserCheckVO(false, ErrorMsgEnum.EMAIL_SEND_FAIL);
		}else{
			if(ENormalDisabledStatus.DISABLE.getValue().equals(smsTempla.getStatus().getValue())){
				_log.info("发送短信失败：【"+smsTemplateCode.getDisplayName()+"】短信模板已停用");
				return new UserCheckVO(false, ErrorMsgEnum.EMAIL_SEND_FAIL);
			}
		}
		String smsContent = smsTempla.getContent();
		for (String vo : params) {
			smsContent = smsContent.replaceFirst("\\{code\\}", vo);
		}
		// 创建短信发送记录
		BisSmsDto bisSmsDto = new BisSmsDto();
		bisSmsDto.setId(UUID.randomUUID().toString());
		bisSmsDto.setSystem(smsSystem);
		bisSmsDto.setSendStartTime(smsTempla.getSendStartTime());
		bisSmsDto.setSendEndTime(smsTempla.getSendEndTime());
		bisSmsDto.setCreateTime(new Date());
		bisSmsDto.setSendContent(smsContent);
		bisSmsDto.setSendChannel(EsendChannel.SHOR_MESSAGE);
		bisSmsDto.setMessType(messType);
		bisSmsDto.setSendType(EsendType.REAL_TIME);
//		bisSmsDto.setSendEr(bisSysParamService.getValue(SystemParamConstants.SMS_MER));
//		if (StringUtils.isEmpty(tel)) {
//			String phones =getPhones(smsTempla.getPhone());
//			if(phones==null){
//				String errorMsg="发送短信失败：短信接收人为空；业务类型【"+smsTemplateCode.getDisplayName()+"】";
//				_log.error(errorMsg);
//				bisExceptionLogService.saveLog(CbsExceptionLogDtoUtils.getBisExceptionLog(EBisExceptionLogNlevel.ERROR, EBisExceptionLogType.SEND_SMS, errorMsg));
//				return;
//			}
//			bisSmsDto.setMobile(phones);
//		} else {
//			bisSmsDto.setMobile(tel);
//		}	
		bisSmsDto.setMobile(tel);
		if("0000".compareTo(smsTempla.getSendStartTime())==0 && "0000".compareTo(smsTempla.getSendEndTime())==0){
			// 非定时短信
			sendSms(bisSmsDto);
		} else {
			_log.error("定时短信");
		}
		return new UserCheckVO(true);
	}

	private void sendSms(BisSmsDto bisSmsDto) {
		// 获取短信发送的配置信息，调用前置系统的短信发送接口发送短信。
		String url = bisSysParamService.getValue(SHIEConfigConstant.EMAIL_SMS_URL);
		String serviceUrl = bisSysParamService.getValue(SHIEConfigConstant.SMS_SERVICE_URL);
		taskExecutor.execute(new Runnable() {
			@Override
			public void run() {
				SmsReqVO smsReqVo = new SmsReqVO();
				smsReqVo.setEventCode(SHIEConfigConstant.EVEN_CODE);
				smsReqVo.setBizType(SHIEConfigConstant.SMS_BIZ_TYPE);
				smsReqVo.setMessage(bisSmsDto.getSendContent());
				smsReqVo.setModule(SHIEConfigConstant.SMS_EMAIL_MODULE);
				smsReqVo.setPhone(bisSmsDto.getMobile());
				smsReqVo.setSeqId(DateUtil.getNowYearToSecond$()+(Math.random()+"").substring(2,7));
				_log.info("调用短信发送接口:收件人【"+bisSmsDto.getMobile()+"】短信内容【"+bisSmsDto.getSendContent()+"】");
				SendRespVO respVo = null;
				// 调用
				String retJson = HttpSendUtil.sendPostJson(smsReqVo, url+serviceUrl);
				respVo = new Gson().fromJson(retJson, SendRespVO.class);
				_log.info("短信接口返回参数：【"+respVo+"】");
				if(SHIEConfigConstant.RET_SUCCESS.equals(respVo.getResult())){
					bisSmsDto.setStatus(EBisSendStatus.SEND_SUCCESS);
				}else {
					bisSmsDto.setStatus(EBisSendStatus.SEND_FAIL);
					bisSmsDto.setFailReason(respVo.getMsg());
					_log.error("返回为空，发送短信失败:收件人【"+bisSmsDto.getMobile()+"】短信内容【"+bisSmsDto.getSendContent()+"】");
				}
				bisSmsDtoMapper.insert(bisSmsDto);	
			}
		});
	}

	@SuppressWarnings("unused")
	@Deprecated
	private void generateOldCode(BisSmsDto bisSmsDto) {
		// 获取短信发送的配置信息，调用前置系统的短信发送接口发送短信。
		/*String url = bisSysParamService.getValue(SystemParamConstants.SMS_URL);
		String port = bisSysParamService.getValue(SystemParamConstants.SMS_PORT);
		String pw = bisSysParamService.getValue(SystemParamConstants.SMS_PW);
		SmsSendDto smsSendDto = new SmsSendDto();
		smsSendDto.setSmsAddress(url);
		smsSendDto.setSmsPort(port);
		smsSendDto.setSmsName(bisSmsDto.getSendEr());
		smsSendDto.setSmsPassword(pw);
		smsSendDto.setContent(bisSmsDto.getSendContent());
		smsSendDto.setPhone(bisSmsDto.getMobile());
		_log.debug("开始调用前置系统发短信：收件人");
		SmsRespCode resCode=null;
		if(smsSendDto.getPhone().split(",").length>1){
			_log.info("调用前置系统群发短信:收件人【"+smsSendDto.getPhone()+"】短信内容【"+smsSendDto.getContent()+"】");
			resCode = smsManageAppService.sendSmsGroupMsg(smsSendDto);
		}else{
			_log.info("调用前置系统单发短信:收件人【"+smsSendDto.getPhone()+"】短信内容【"+smsSendDto.getContent()+"】");
			resCode = smsManageAppService.sendSmsMsg(smsSendDto);
		}
		if(SmsRespCode.Success.equals(resCode)){
			bisSmsDto.setStatus(EBisSendStatus.SEND_SUCCESS);
		}else {
			bisSmsDto.setStatus(EBisSendStatus.SEND_FAIL);
			bisSmsDto.setFailReason(resCode.getDisplayName());
			_log.info("发送短信失败:收件人【"+smsSendDto.getPhone()+"】短信内容【"+smsSendDto.getContent()+"】原因："+resCode.getDisplayName());
		}*/
	}
	
	private String getPhones(String key){
		if ("all".equals(key)) {
			// 获取所有的管理员，调用群发接口
			List<UcsSecUserDto> users = ucsSecUserDtoMapper.allNomalUser(EUcsSecUserStatus.NORMAL);
			StringBuilder phone =new StringBuilder();
			for (UcsSecUserDto ucsSecUserDto : users) {
				phone.append(ucsSecUserDto.getMobile()).append(",");
			}
			return phone.deleteCharAt(phone.toString().lastIndexOf(",")).toString();
		}else if (StringUtils.isEmpty(key)) {
			return null;
		} else {
			return key;
		}
	}
		

	@Override
	@Transactional(value=CbsConstants.TX_MANAGER_BIS)
	public void sendWebChatSms(String tel, EBisSmsSystem smsSystem,
			String content, EsendChannel sendChannel,EmessType messType, String openId, String messTitle, Long amount,
			Long balance,EsendType sendType,String sendTime){
			// 记录发送记录
			BisSmsDto bisSmsDto = new BisSmsDto();
			bisSmsDto.setId(UUID.randomUUID().toString());
			bisSmsDto.setSystem(smsSystem);
			bisSmsDto.setMobile(tel);
			bisSmsDto.setSendContent(content);
			bisSmsDto.setSendChannel(sendChannel);
			bisSmsDto.setMessType(messType);
			bisSmsDto.setOpenId(openId);
			bisSmsDto.setAmount(amount);
			bisSmsDto.setBalance(balance);
			bisSmsDto.setCreateTime(new Date());
			bisSmsDto.setMessTitle(messTitle);
			bisSmsDto.setSendType(sendType);
			try{
				//设置发送对象
				/*List<BisServiceAccessPartyDto> serviceAccessPartys=null;
				if(EsendChannel.APP.getValue().endsWith(sendChannel.getValue())){
					serviceAccessPartys=bisServiceAccessPartyDtoCache.getSendUrl(EServiceAccessPartyType.APP);	
				}else{
					serviceAccessPartys=bisServiceAccessPartyDtoMapper.getSendUrl(EServiceAccessPartyType.WECHAT,ENormalDisabledStatus.NORMAL);	
				}
				if(serviceAccessPartys==null || serviceAccessPartys.size()!=1){
					String errMsg="微信发送失败："+ECbsErrorCode.ACCESS_PARTY_UNEXIST_ERROR.getDisplayName();
					_log.error(errMsg);
					bisExceptionLogService.saveLog(CbsExceptionLogDtoUtils.getBisExceptionLog(EBisExceptionLogNlevel.ERROR, EBisExceptionLogType.SEND_WEBCHAT, errMsg));
				}
				BisServiceAccessPartyDto serviceAccessParty=serviceAccessPartys.get(0);
				WeixinAppDto weixinAppDto = new WeixinAppDto();
				weixinAppDto.setSendUrl(serviceAccessParty.getUrl());
				weixinAppDto.setAccessCode(serviceAccessParty.getAccessPartyCode());
				weixinAppDto.setIsCheck(serviceAccessParty.getIsCheck());
				weixinAppDto.setCheckType(serviceAccessParty.getCheckType());
				weixinAppDto.setCheckKey(serviceAccessParty.getCheckKey());
				weixinAppDto.setCheckCrt(serviceAccessParty.getSafetyCode());
				weixinAppDto.setOpenId(openId);
				weixinAppDto.setMessTitle(messTitle);
				weixinAppDto.setMessContent(content);
				weixinAppDto.setAmount(amount);
				weixinAppDto.setTime(sendTime);
				weixinAppDto.setBalance(balance);
				weixinAppDto.setPropeType(sendType.getValue());
				weixinAppDto.setSendChannel(sendChannel.getValue());
				//如果是定时发送，并且当前时间大于定时时间，直接发送
				WeixinAppRespDto noticeWeixinApp=null;
				if(EsendType.TIMING.getValue().equals(sendType.getValue())){
					bisSmsDto.setSendStartTime(sendTime);
					SimpleDateFormat format = new SimpleDateFormat("HHmm");
					String time = format.format(new Date());
					if(time.compareTo(sendTime)>=0){
						//发送信息
						_log.info("调用前置系统单发短微信:收件人【"+tel+"】微信内容【"+weixinAppDto.getMessContent()+"】");
					}else{
						_log.info("定时发送微信消息，功能未实现");
					}
				}else{
					//发送信息
					_log.info("调用前置系统单发短微信:收件人【"+tel+"】微信内容【"+weixinAppDto.getMessContent()+"】");
				}
				EAccessWxReturnCode resultCode = noticeWeixinApp.getResultCode();
				if(resultCode.equals(EAccessWxReturnCode.SUCCESS)){
					bisSmsDto.setStatus(EBisSendStatus.SEND_SUCCESS);
				}else {
					bisSmsDto.setStatus(EBisSendStatus.SEND_FAIL);
					bisSmsDto.setFailReason(resultCode.getDisplayName());
					_log.info("发送微信失败:收件人【"+tel+"】短信内容【"+weixinAppDto.getMessContent()+"】原因："+resultCode.getDisplayName());
				}*/
				
				bisSmsDto.setSendTime(new Date());
				bisSmsDtoMapper.insert(bisSmsDto);
			} catch (Exception e) {
				bisSmsDto.setSendTime(new Date());
				bisSmsDto.setStatus(EBisSendStatus.SEND_FAIL);
				bisSmsDto.setFailReason("发送微信失败，调用前置系统接口出错");
				bisSmsDtoMapper.insert(bisSmsDto);
				//String errMsg="发送微信失败，调用前置系统接口出错"+e.getMessage();
				_log.error("微信发送失败："+e);
//				bisExceptionLogService.saveLog(CbsExceptionLogDtoUtils.getBisExceptionLog(EBisExceptionLogNlevel.ERROR, EBisExceptionLogType.SEND_WEBCHAT, errMsg));
		}
	}

	@Override
	public List<BisSmsDto> findList(BisSmsDto bisSmsDto) {
		List<BisSmsDto> list;
		try {
			list = bisSmsDtoMapper.list(bisSmsDto);
		} catch (Exception e) {
			list = null;
		}
		return list;
	}
}
