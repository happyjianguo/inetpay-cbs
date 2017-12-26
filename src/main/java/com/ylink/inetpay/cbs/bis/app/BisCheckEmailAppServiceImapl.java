package com.ylink.inetpay.cbs.bis.app;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ylink.eu.util.tools.CollectionUtil;
import com.ylink.eu.util.tools.StringUtil;
import com.ylink.inetpay.cbs.bis.service.BisCheckEmailService;
import com.ylink.inetpay.cbs.mrs.service.MrsAccountService;
import com.ylink.inetpay.cbs.mrs.service.MrsPersonService;
import com.ylink.inetpay.common.core.constant.EBisEmailChectStatus;
import com.ylink.inetpay.common.core.constant.EBisEmailTemplateCode;
import com.ylink.inetpay.common.project.cbs.app.BisCheckEmailAppService;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsLoginUserDto;
import com.ylink.inetpay.common.project.cbs.exception.CbsCheckedException;
import com.ylink.inetpay.common.project.portal.dto.AccountMsg;
import com.ylink.inetpay.common.project.portal.enums.ErrorMsgEnum;
import com.ylink.inetpay.common.project.portal.vo.UserCheckVO;
@Service("bisCheckEmailAppService")
public class BisCheckEmailAppServiceImapl implements BisCheckEmailAppService{
	
	private static Logger log = LoggerFactory.getLogger(BisCheckEmailAppServiceImapl.class);
	
	@Autowired
	private BisCheckEmailService bisCheckEmailService;
	@Autowired
	private MrsAccountService mrsAccountService;
	@Autowired
	private MrsPersonService mrsPersonService;
	
	@Override
	public void updateEmailStatus(EBisEmailTemplateCode operType,
			EBisEmailChectStatus checkStatus, String custId)
			throws CbsCheckedException {
		bisCheckEmailService.updateEmailStatus(operType,checkStatus,custId);
	}
	
	@Override
	public MrsLoginUserDto shieCheckEmail(String checkMessage, String id, EBisEmailTemplateCode template) {
		try {
			MrsLoginUserDto loginUser =  bisCheckEmailService.shieCheckEmail(checkMessage, id, template);
			if(!loginUser.getUserCheckVo().isCheckValue()) {
				log.error("邮件验证失败:"+loginUser.getUserCheckVo().getMsg());
				return loginUser;
			}
			// 根据登陆信息获取账户信息
			List<AccountMsg> accountMsgs = mrsAccountService.findPersonAccountMsgByLoginUserId(loginUser.getId());
			if(CollectionUtil.isEmpty(accountMsgs)) {
				log.info("根据登陆ID[id="+loginUser.getId()+"]没有找到账户信息");
				// 二期改造    表示客户只有客户信息没有账户信息
				loginUser.setHasAcnt(false);
			} else {
				loginUser.setCustId(accountMsgs.get(0).getCustId());
				loginUser.setHasAcnt(true);
				loginUser.setAccountMsgs(accountMsgs);
				if(StringUtil.isEmpty(loginUser.getMobile())) {
					loginUser.setBindMobile(false);
				}else {
					loginUser.setBindMobile(true);
				}
				if(StringUtil.isEmpty(loginUser.getEmail())) {
					loginUser.setBindEmail(false);
				}else {
					loginUser.setBindEmail(true);
				}
				loginUser.setCustomerName(accountMsgs.get(0).getCustomerName());
			}
			
			loginUser.setLoginPwd("");
			loginUser.setPayPwd("");
			UserCheckVO vo = new UserCheckVO(true);
			loginUser.setUserCheckVo(vo);
			return loginUser;
		} catch (Exception e) {
			log.error("验证邮件失败:", e);
			UserCheckVO vo = new UserCheckVO(false, ErrorMsgEnum.EMAIL_CHECK_FAIL);
			MrsLoginUserDto retDto = new MrsLoginUserDto();
			retDto.setUserCheckVo(vo);
			return retDto;
		}
	}

	@Override
	public MrsLoginUserDto shieCheckEmailAndSet(String checkMessage, String id, EBisEmailTemplateCode template) {
		try {
			// 验证邮件
			MrsLoginUserDto loginUser =  bisCheckEmailService.shieCheckEmail(checkMessage, id, template);
			if(!loginUser.getUserCheckVo().isCheckValue()) {
				log.error("邮件验证失败:"+loginUser.getUserCheckVo().getMsg());
				return loginUser;
			}
			loginUser = mrsPersonService.restEmailById(id, loginUser.getEmail());
			loginUser.setLoginPwd("");
			loginUser.setPayPwd("");
			if(!loginUser.getUserCheckVo().isCheckValue()) {
				log.error("修改失败:"+loginUser.getUserCheckVo().getMsg());
				return loginUser;
			}
			// 根据登陆信息获取账户信息
			List<AccountMsg> accountMsgs = mrsAccountService.findPersonAccountMsgByLoginUserId(loginUser.getId());
			if(CollectionUtil.isEmpty(accountMsgs)) {
				log.info("根据登陆ID[id="+loginUser.getId()+"]没有找到账户信息");
				// 二期改造    表示客户只有客户信息没有账户信息
				loginUser.setHasAcnt(false);
			} else {
				loginUser.setHasAcnt(true);
				loginUser.setAccountMsgs(accountMsgs);
				if(StringUtil.isEmpty(loginUser.getMobile())) {
					loginUser.setBindMobile(false);
				}else {
					loginUser.setBindMobile(true);
				}
				if(StringUtil.isEmpty(loginUser.getEmail())) {
					loginUser.setBindEmail(false);
				}else {
					loginUser.setBindEmail(true);
				}
			}
			UserCheckVO vo = new UserCheckVO(true);
			loginUser.setUserCheckVo(vo);
			return loginUser;
		} catch (Exception e) {
			log.error("验证邮件失败:", e);
			UserCheckVO vo = new UserCheckVO(false, ErrorMsgEnum.EMAIL_CHECK_FAIL);
			MrsLoginUserDto retDto = new MrsLoginUserDto();
			retDto.setUserCheckVo(vo);
			return retDto;
		}
	}
}
