package com.ylink.inetpay.cbs.bis.app;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.cbs.bis.service.BisEmailService;
import com.ylink.inetpay.cbs.mrs.service.MrsExternalMailboxServerService;
import com.ylink.inetpay.cbs.mrs.service.MrsLoginUserService;
import com.ylink.inetpay.common.core.constant.EBisEmailTemplateCode;
import com.ylink.inetpay.common.core.constant.EBisSmsSystem;
import com.ylink.inetpay.common.project.cbs.app.BisEmailAppService;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisEmailDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsLoginUserDto;
import com.ylink.inetpay.common.project.cbs.exception.CbsCheckedException;
import com.ylink.inetpay.common.project.portal.enums.ErrorMsgEnum;
import com.ylink.inetpay.common.project.portal.vo.UserCheckVO;
import com.ylink.inetpay.common.project.portal.vo.customer.SendEmailRespVO;
@Service("bisEmailAppService")
public class BisEmailAppServiceImpl implements BisEmailAppService {
	
	private static Logger log = LoggerFactory.getLogger(BisEmailAppServiceImpl.class);
	
	@Autowired
	private BisEmailService bisEmailService;
	@Autowired
	private MrsLoginUserService mrsLoginUserService;
	@Autowired
	TaskExecutor smsTaskExecutor;
	@Autowired
	private MrsExternalMailboxServerService mrsExternalMailboxServerService;
	@Override
	public PageData<BisEmailDto> findListPage(PageData<BisEmailDto> pageDate,
			BisEmailDto bisEmailDto) throws CbsCheckedException {
		return bisEmailService.findListPage(pageDate, bisEmailDto);
	}

	@Override
	public BisEmailDto details(String id) throws CbsCheckedException {
		return bisEmailService.details(id);
	}

	@Override
	public void flushEmail() throws CbsCheckedException {
		bisEmailService.flushEmail();
	}

	@Override
	public void sendEmail(final String email,final String id,final EBisSmsSystem emailSystem,
			final List<String> params,final EBisEmailTemplateCode emailTemplateCode,
			final String checkMessage) throws CbsCheckedException {
//		bisEmailService.sendEmail(email,id, emailSystem, params, emailTemplateCode, checkMessage);
		smsTaskExecutor.execute(new Runnable() {
			@Override
			public void run() {
				bisEmailService.sendEmail(email, id, emailSystem, params, emailTemplateCode, checkMessage);
			}
		});
		// 获取客户邮箱的  邮箱登陆地址
		
	}

	@Override
	public String sendEmail(String email, String id, EBisSmsSystem emailSystem, EBisEmailTemplateCode template, 
			Map<String, Object> params) {
		try {
			UserCheckVO checkVo = bisEmailService.shieSendEmail(email, id, emailSystem, template, params,null);
			if(!checkVo.isCheckValue()){
				log.error("邮件发送失败:"+checkVo.getMsg());
				return null;
			}
			String suffix = email.substring(email.lastIndexOf("@")+1);
			return mrsExternalMailboxServerService.getServerPath(suffix);
		} catch (Exception e) {
			log.error("邮件发送失败:",e);
			return null;
		}
	}

	@Override
	public SendEmailRespVO sendEmailValiPwd(String email, String id, String loginPwd, EBisSmsSystem emailSystem, EBisEmailTemplateCode template, 
			Map<String, Object> params) {
		
		SendEmailRespVO vo = new SendEmailRespVO();
		try {
			MrsLoginUserDto loginUser = mrsLoginUserService.findByIdAndLoginPwd(id, loginPwd);
			if(loginUser == null) {
				log.error("用户名或密码[id = "+id+"]错误:");
				vo.setCheckVo(new UserCheckVO(false, ErrorMsgEnum.LOGIN_PWD_ERROR));
				return vo;
			}
			UserCheckVO checkVo = bisEmailService.shieSendEmail(email, id, emailSystem, template, params,null);
			if(!checkVo.isCheckValue()) {
				log.error("邮件发送失败:"+checkVo.getMsg());
				vo.setCheckVo(checkVo);
				return vo;
			}
			String suffix = email.substring(email.lastIndexOf("@")+1);
			vo.setCheckVo(new UserCheckVO(true));
			vo.setEmailLoginAddr(mrsExternalMailboxServerService.getServerPath(suffix));
			return vo;
		} catch (Exception e) {
			log.error("邮件发送失败:",e);
			vo.setCheckVo(new UserCheckVO(false, ErrorMsgEnum.EMAIL_SEND_FAIL));
			return vo;
		}
	}
	
}
