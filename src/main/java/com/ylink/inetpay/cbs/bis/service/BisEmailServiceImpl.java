package com.ylink.inetpay.cbs.bis.service;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Date;
import java.util.List;
import java.util.Map;
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
import com.ylink.eu.util.tools.FreeMakerUtil;
import com.ylink.inetpay.cbs.bis.cache.BisEmailTemplateDtoCache;
import com.ylink.inetpay.cbs.bis.dao.BisCheckEmailDtoMapper;
import com.ylink.inetpay.cbs.bis.dao.BisEmailDtoMapper;
import com.ylink.inetpay.cbs.common.CbsConstants;
import com.ylink.inetpay.cbs.ucs.sec.dao.UcsSecUserDtoMapper;
import com.ylink.inetpay.cbs.ucs.sec.service.UcsSecUserServiceImpl;
import com.ylink.inetpay.cbs.util.CbsExceptionLogDtoUtils;
import com.ylink.inetpay.cbs.util.HttpSendUtil;
import com.ylink.inetpay.cbs.util.PropertyUtil;
import com.ylink.inetpay.common.core.constant.EBisEmailChectStatus;
import com.ylink.inetpay.common.core.constant.EBisEmailTemplateCode;
import com.ylink.inetpay.common.core.constant.EBisExceptionLogNlevel;
import com.ylink.inetpay.common.core.constant.EBisExceptionLogType;
import com.ylink.inetpay.common.core.constant.EBisSendStatus;
import com.ylink.inetpay.common.core.constant.EBisSmsSystem;
import com.ylink.inetpay.common.core.constant.ENormalDisabledStatus;
import com.ylink.inetpay.common.core.constant.EUcsSecUserStatus;
import com.ylink.inetpay.common.project.access.app.EmailManageAppService;
import com.ylink.inetpay.common.project.access.constant.EmailRespCode;
import com.ylink.inetpay.common.project.access.dto.EmailSendDto;
import com.ylink.inetpay.common.project.access.exception.AccessCheckedException;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisCheckEmailDto;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisEmailDto;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisEmailTemplateDto;
import com.ylink.inetpay.common.project.cbs.dto.ucs.sec.UcsSecUserDto;
import com.ylink.inetpay.common.project.cbs.exception.ECbsErrorCode;
import com.ylink.inetpay.common.project.portal.enums.ErrorMsgEnum;
import com.ylink.inetpay.common.project.portal.vo.UserCheckVO;
import com.ylink.inetpay.common.project.portal.vo.sms.EmailReqVO;
import com.ylink.inetpay.common.project.portal.vo.sms.SendRespVO;

import freemarker.template.Template;
import freemarker.template.TemplateException;
@Service("bisEmailService")
@Transactional(value=CbsConstants.TX_MANAGER_BIS)
public class BisEmailServiceImpl implements BisEmailService {
	@Autowired
	private BisEmailDtoMapper bisEmailDtoMapper;
	@Autowired
	private BisEmailTemplateDtoCache bisEmailTemplateDtoCache;

	@Autowired
	private BisCheckEmailDtoMapper bisCheckEmailDtoMapper;
	@Autowired
	private EmailManageAppService emailManageAppService;
	@Autowired
	private UcsSecUserDtoMapper ucsSecUserDtoMapper;
	@Autowired
	private BisSysParamService bisSysParamService;
	@Autowired
	private BisExceptionLogService bisExceptionLogService;
	@Autowired
	private TaskExecutor taskExecutor;
	
	private static Logger _log = LoggerFactory.getLogger(UcsSecUserServiceImpl.class);
	@Override
	public PageData<BisEmailDto> findListPage(PageData<BisEmailDto> pageDate,
			BisEmailDto bisEmailDto){
		PageHelper.startPage(pageDate.getPageNumber(),pageDate.getPageSize());
		List<BisEmailDto> findListPage=bisEmailDtoMapper.list(bisEmailDto);
		Page<BisEmailDto> page =(Page<BisEmailDto>) findListPage;
		pageDate.setTotal(page.getTotal());
		pageDate.setRows(findListPage);
		return pageDate;
	}

	@Override
	public BisEmailDto details(String id){
		return bisEmailDtoMapper.selectByPrimaryKey(id);
	}

	@Override
	public void flushEmail(){
		//暂时没有此功能
			
	}

	@Override
	public void sendEmail(String email,String id, EBisSmsSystem emailSystem,
			List<String> params, EBisEmailTemplateCode emailTemplateCode,
			String  checkMessage){
		//获取邮件模板
		BisEmailTemplateDto emailTempla = bisEmailTemplateDtoCache.getEmailTempla(emailTemplateCode);
		if(emailTempla!=null){
			if(emailTempla.getStatus().getValue().equals(ENormalDisabledStatus.DISABLE.getValue())){
				_log.error("邮件发送失败：邮件模板已停用；业务类型【"+emailTemplateCode.getDisplayName()+"】");
				return;
			}
			//String isSSL=bisSysParamDtoMapper.getValue(SystemParamConstants.MEAIL_IS_SSL);
			String emailSender = bisSysParamService.getValue(SHIEConfigConstant.EMAIL_SENDER);
			//记录发送记录
			BisEmailDto bisEmailDto = new BisEmailDto();
			bisEmailDto.setId(UUID.randomUUID().toString());
			bisEmailDto.setSystem(emailSystem);
			bisEmailDto.setAddressee(email);
			bisEmailDto.setEmailTitle(emailTempla.getTitle());
			bisEmailDto.setSender(emailSender);
			bisEmailDto.setCreateTime(new Date());
			bisEmailDto.setSendTime(new Date());
			//如果邮件需要验证，记录验证信息
			
			if(!StringUtils.isBlank(checkMessage)){
				BisCheckEmailDto bisCheckEmailDto = new BisCheckEmailDto();
				bisCheckEmailDto.setId(UUID.randomUUID().toString());
				//验证信息：用户编号+时间戳    MD5加密
				bisCheckEmailDto.setCheckMessage(checkMessage);
				bisCheckEmailDto.setCustId(id);
				if(email!=null){
					bisCheckEmailDto.setEmail(email);
				}else{
					bisCheckEmailDto.setEmail(emailTempla.getAddressee());
				}
				bisCheckEmailDto.setOperType(emailTempla.getBusinessScenario());
				bisCheckEmailDto.setStatus(EBisEmailChectStatus.PENDING_CHECK);
				bisCheckEmailDto.setCreateTime(new Date());
				//使旧的失效
				bisCheckEmailDtoMapper.updateOperTypeAndCustId(bisCheckEmailDto);
				bisCheckEmailDtoMapper.insert(bisCheckEmailDto);
			}
			//拼装邮件模板内容，如果是需要验证的邮件，需要添加链接地址
			String templaContent = emailTempla.getContent();
			for (String vo : params) {
				templaContent= templaContent.replaceFirst("\\{code\\}", vo);
			}
			bisEmailDto.setEmailContent(templaContent);
			try {
				// 发送邮件
//				sendEmail(bisEmailDto);
			} catch (Exception e) {
				bisEmailDto.setSendStatus(EBisSendStatus.SEND_FAIL);
				bisEmailDto.setFailReason("邮件发送失败，调用前置系统接口异常");
				bisEmailDtoMapper.insert(bisEmailDto);
				String errMsg="邮件发送失败，调用前置系统接口异常："+e;
				_log.error(errMsg);
				bisExceptionLogService.saveLog(CbsExceptionLogDtoUtils.getBisExceptionLog(EBisExceptionLogNlevel.ERROR, EBisExceptionLogType.SEND_EMAIL, errMsg));
			} finally {
				bisEmailDtoMapper.insert(bisEmailDto);
			}
		}else{
			String errMsg="邮件发送失败：邮件模板不存在；业务类型【"+emailTemplateCode.getDisplayName()+"】";
			_log.error(errMsg);
			bisExceptionLogService.saveLog(CbsExceptionLogDtoUtils.getBisExceptionLog(EBisExceptionLogNlevel.ERROR, EBisExceptionLogType.SEND_EMAIL, errMsg));				
		}
	}


	private void sendEmail(BisCheckEmailDto bisCheckEmailDto, String content) {
		String url = bisSysParamService.getValue(SHIEConfigConstant.EMAIL_SMS_URL);
		String serviceUrl = bisSysParamService.getValue(SHIEConfigConstant.EMAIL_SERVICE_URL);
		String emailSender = bisSysParamService.getValue(SHIEConfigConstant.EMAIL_SENDER);
		String fromEmail = bisSysParamService.getValue(SHIEConfigConstant.EMAIL_FROM_EMAIL);
		taskExecutor.execute(new Runnable() {
			@Override
			public void run() {
				EmailReqVO emailReqVo = new EmailReqVO();
				emailReqVo.setBizType(SHIEConfigConstant.EMAIL_BIZ_TYPE);
				emailReqVo.setContent(content);
				emailReqVo.setEventCode(SHIEConfigConstant.EVEN_CODE);
				emailReqVo.setModule(SHIEConfigConstant.SMS_EMAIL_MODULE);
				emailReqVo.setSeqId(DateUtil.getNowYearToSecond$()+(Math.random()+"").substring(2,6));
				emailReqVo.setSubject(bisCheckEmailDto.getOperType().getDisplayName());
				emailReqVo.setToMail(bisCheckEmailDto.getEmail());
				emailReqVo.setSender(emailSender);//发送人名称	
				emailReqVo.setFromEmail(fromEmail);//发送人邮箱地址	
				SendRespVO respVo = null;
				// 发送Post请求
				String retJson = HttpSendUtil.sendPostJson(emailReqVo, url+serviceUrl);
				respVo = new Gson().fromJson(retJson, SendRespVO.class);
				_log.info("邮件发送接口返回参数：["+respVo+"]");
			}
		});
	}
	@SuppressWarnings("unused")
	@Deprecated
	private void generateOld(String email, BisEmailTemplateDto emailTempla, BisEmailDto bisEmailDto)
			throws AccessCheckedException {
		EmailSendDto emailSend=new EmailSendDto();
		//获取短信发送的配置信息，调用前置系统的短信发送接口发送短信。
		/*String url = bisSysParamService.getValue(SystemParamConstants.EMAIL_URL);
		String port=bisSysParamService.getValue(SystemParamConstants.EMAIL_PORT);
		String user=bisSysParamService.getValue(SystemParamConstants.EMAIL_USER);
		String pw=bisSysParamService.getValue(SystemParamConstants.EMAIL_PW);
		emailSend.setEmailHostKey(url);
		emailSend.setEmailPortKey(port);
		emailSend.setEmailUserNameKey(user);
		emailSend.setEmailPassWordKey(pw);*/
		emailSend.setEmailContent(bisEmailDto.getEmailContent());
		emailSend.setSubject(bisEmailDto.getEmailTitle());
		//接收返回值
		EmailRespCode massEmailMsg=null;
		if(email==null){
			String emails = getEmails(emailTempla.getAddressee());
			if(emails==null){
				String errorMsg="发送邮件失败：邮件接收人为空；业务类型【"+emailTempla.getBusinessScenario().getDisplayName()+"】";
				_log.error(errorMsg);
				bisExceptionLogService.saveLog(CbsExceptionLogDtoUtils.getBisExceptionLog(EBisExceptionLogNlevel.ERROR, EBisExceptionLogType.SEND_EMAIL, errorMsg));
				return;
			}
			emailSend.setAddRessee(emails);
		}else{
			emailSend.setAddRessee(email);
		}
		if(emailSend.getAddRessee().split(",").length>1){
			_log.info("调用前置系统单发邮件:收件人【"+emailSend.getAddRessee()+"】短信内容【"+emailSend.getEmailContent()+"】");
			massEmailMsg=emailManageAppService.massEmailMsg(emailSend);
		}else{
			_log.info("调用前置系统群发发邮件:收件人【"+emailSend.getAddRessee()+"】短信内容【"+emailSend.getEmailContent()+"】");
			massEmailMsg=emailManageAppService.sendEmailMsg(emailSend);
		}
		if(EmailRespCode.Success==massEmailMsg){
			bisEmailDto.setSendStatus(EBisSendStatus.SEND_SUCCESS);
			bisEmailDtoMapper.insert(bisEmailDto);
		}else{
			bisEmailDto.setSendStatus(EBisSendStatus.SEND_FAIL);
			bisEmailDto.setFailReason(massEmailMsg.getDisplayName());
			bisEmailDtoMapper.insert(bisEmailDto);
			String errMsg="邮件发送："+ECbsErrorCode.EMAIL_SEND_ERROR.getDisplayName();
			_log.error(errMsg);
			bisExceptionLogService.saveLog(CbsExceptionLogDtoUtils.getBisExceptionLog(EBisExceptionLogNlevel.ERROR, EBisExceptionLogType.SEND_EMAIL, errMsg));
		}
	}
	
	private String getEmails(String key){
		if ("all".equals(key)) {
			// 获取所有的管理员，调用群发接口
			List<UcsSecUserDto> users = ucsSecUserDtoMapper.allNomalUser(EUcsSecUserStatus.NORMAL);
			StringBuilder phone =new StringBuilder();
			for (UcsSecUserDto ucsSecUserDto : users) {
				phone.append(ucsSecUserDto.getEmail()).append(",");
			}
			return phone.deleteCharAt(phone.toString().lastIndexOf(",")).toString();
		}else if (StringUtils.isEmpty(key)) {
			return null;
		} else {
			return key;
		}
	}
	
	@Override
	public UserCheckVO sendExceptionLogEmail(String email, EBisSmsSystem emailSystem, EBisEmailTemplateCode template,
			Map<String, Object> params) {
		String ftlFilePath = PropertyUtil.getProperty(template.name());
		if(StringUtils.isEmpty(ftlFilePath)) {
			_log.error("文件地址为空!");
			return new UserCheckVO(false, ErrorMsgEnum.EMAIL_SEND_FAIL);
		}
		Template t = FreeMakerUtil.getTemplateByTempPath(ftlFilePath, "UTF-8");
		//模板信息由调用者传入
//		params.put(SHIEConfigConstant.CURRENT_ADMINISTRATOR, checkMessage);
//		params.put(SHIEConfigConstant.ERROR_MSG, address);
		StringWriter sw = new StringWriter();
		String content = processByClose(t, params, sw);
		if(StringUtils.isEmpty(content)) {
			_log.error("邮件内容生成失败");
			return new UserCheckVO(false, ErrorMsgEnum.EMAIL_SEND_FAIL);
		}
		//调用接口发送邮件
		BisCheckEmailDto bisCheckEmailDto = new BisCheckEmailDto();
		bisCheckEmailDto.setEmail(email);
		bisCheckEmailDto.setOperType(template);
		sendEmail(bisCheckEmailDto, content);
		_log.info("邮件内容:{}",content);
		return new UserCheckVO(true);
	}

	@Override
	public UserCheckVO shieSendEmail(String email, String id, EBisSmsSystem emailSystem, EBisEmailTemplateCode template, Map<String, Object> params
			,String custId) {
		String emailType = template.getValue(); 
		if(EBisEmailTemplateCode.SET_EMAIL.equals(template) || EBisEmailTemplateCode.UPDATE_EMAIL.equals(template)) {
			// 修改和设置用一个邮件模板
			template = EBisEmailTemplateCode.SET_EMAIL;
		}
		String ftlFilePath = PropertyUtil.getProperty(template.name());
		String address = bisSysParamService.getValue(SHIEConfigConstant.PORTAL_CALLBACK_ADDR);
		if(StringUtils.isEmpty(ftlFilePath)) {
			_log.error("文件地址为空!");
			return new UserCheckVO(false, ErrorMsgEnum.EMAIL_SEND_FAIL);
		}
		
		// 判断当前客户在邮件验证表中是否存在邮件类型为template且状态为待验证的邮件
		BisCheckEmailDto dto = bisCheckEmailDtoMapper.getByCustIdOperTypeStatus(id, template, EBisEmailChectStatus.PENDING_CHECK);
		if(dto != null){
			// 将dto状态设置为无效
			dto.setStatus(EBisEmailChectStatus.CHECK_REJECT);
			bisCheckEmailDtoMapper.updateByPrimaryKey(dto);
		}
		String content = "";
		String checkMessage = UUID.randomUUID().toString();
		if(EBisEmailTemplateCode.LOGIN_PASSWORD.equals(template)){
			// 登陆密码找回
			Template t = FreeMakerUtil.getTemplateByTempPath(ftlFilePath, "UTF-8");
			params.put(SHIEConfigConstant.CALLBACK_KEY, checkMessage);
			params.put(SHIEConfigConstant.PORTAL_CALLBACK_ADDR, address);
			params.put(SHIEConfigConstant.LOGIN_ID, id);
			StringWriter sw = new StringWriter();
			content = processByClose(t, params, sw);
			if(StringUtils.isEmpty(content)) {
				_log.error("邮件内容生成失败");
				return new UserCheckVO(false, ErrorMsgEnum.EMAIL_SEND_FAIL);
			}
		} else if(EBisEmailTemplateCode.REST_LOGIN_PWD.equals(template)){
			// 登陆密码找回
			Template t = FreeMakerUtil.getTemplateByTempPath(ftlFilePath, "UTF-8");
			params.put(SHIEConfigConstant.CALLBACK_KEY, checkMessage);
			params.put(SHIEConfigConstant.PORTAL_CALLBACK_ADDR, address);
			params.put(SHIEConfigConstant.LOGIN_ID, id);
			params.put(SHIEConfigConstant.CUST_ID, custId);
			StringWriter sw = new StringWriter();
			content = processByClose(t, params, sw);
			if(StringUtils.isEmpty(content)) {
				_log.error("邮件内容生成失败");
				return new UserCheckVO(false, ErrorMsgEnum.EMAIL_SEND_FAIL);
			}
		} else if(EBisEmailTemplateCode.REST_MOBILE.equals(template)){
			// 重置手机邮件
			Template t = FreeMakerUtil.getTemplateByTempPath(ftlFilePath, "UTF-8");
			params.put(SHIEConfigConstant.CALLBACK_KEY, checkMessage);
			params.put(SHIEConfigConstant.PORTAL_CALLBACK_ADDR, address);
			params.put(SHIEConfigConstant.LOGIN_ID, id);
			StringWriter sw = new StringWriter();
			content = processByClose(t, params, sw);
			if(StringUtils.isEmpty(content)) {
				_log.error("邮件内容生成失败");
				return new UserCheckVO(false, ErrorMsgEnum.EMAIL_SEND_FAIL);
			}
		}  else if(EBisEmailTemplateCode.SET_EMAIL.equals(template)  || EBisEmailTemplateCode.UPDATE_EMAIL.equals(template)){
			// 给新邮箱发送邮件（修改邮箱功能给新邮箱发送邮件、设置新邮箱时发送邮件）
			Template t = FreeMakerUtil.getTemplateByTempPath(ftlFilePath, "UTF-8");
			params.put(SHIEConfigConstant.CALLBACK_KEY, checkMessage);
			params.put(SHIEConfigConstant.PORTAL_CALLBACK_ADDR, address);
			params.put(SHIEConfigConstant.LOGIN_ID, id);
			params.put(SHIEConfigConstant.EMAIL_TYPE, emailType);
			StringWriter sw = new StringWriter();
			content = processByClose(t, params, sw);
			if(StringUtils.isEmpty(content)) {
				_log.error("邮件内容生成失败");
				return new UserCheckVO(false, ErrorMsgEnum.EMAIL_SEND_FAIL);
			}
		}  else if(EBisEmailTemplateCode.OLD_EMAIL.equals(template)){
			// 修改邮箱功能向原邮箱发送邮件 
			Template t = FreeMakerUtil.getTemplateByTempPath(ftlFilePath, "UTF-8");
			params.put(SHIEConfigConstant.CALLBACK_KEY, checkMessage);
			params.put(SHIEConfigConstant.PORTAL_CALLBACK_ADDR, address);
			params.put(SHIEConfigConstant.LOGIN_ID, id);
			StringWriter sw = new StringWriter();
			content = processByClose(t, params, sw);
			if(StringUtils.isEmpty(content)) {
				_log.error("邮件内容生成失败");
				return new UserCheckVO(false, ErrorMsgEnum.EMAIL_SEND_FAIL);
			}
		} 
		// 保存邮件验证信息
		BisCheckEmailDto bisCheckEmailDto = new BisCheckEmailDto();
		bisCheckEmailDto.setId(UUID.randomUUID().toString());
		bisCheckEmailDto.setCheckMessage(checkMessage);
		bisCheckEmailDto.setCustId(id);
		bisCheckEmailDto.setEmail(email);
		bisCheckEmailDto.setOperType(template);
		bisCheckEmailDto.setStatus(EBisEmailChectStatus.PENDING_CHECK);
		bisCheckEmailDto.setCreateTime(new Date());
		//使旧的失效
		bisCheckEmailDtoMapper.insert(bisCheckEmailDto);
		sendEmail(bisCheckEmailDto, content);
		_log.info("邮件内容:{}",content);
		return new UserCheckVO(true);
	}
	
	public static String processByClose(Template template ,Map<String ,Object> paramMap ,Writer out) {
		if(null == template || null == paramMap) 
			return "";
		try {
			template.process(paramMap , out);
			return out.toString();
		} catch (TemplateException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally{
			try {
				out.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}
	
}
