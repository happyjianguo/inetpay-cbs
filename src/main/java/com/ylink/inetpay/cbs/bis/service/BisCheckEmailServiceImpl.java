package com.ylink.inetpay.cbs.bis.service;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shie.constant.SHIEConfigConstant;
import com.ylink.inetpay.cbs.bis.dao.BisCheckEmailDtoMapper;
import com.ylink.inetpay.cbs.common.CbsConstants;
import com.ylink.inetpay.cbs.mrs.service.MrsLoginUserService;
import com.ylink.inetpay.common.core.constant.EBisEmailChectStatus;
import com.ylink.inetpay.common.core.constant.EBisEmailTemplateCode;
import com.ylink.inetpay.common.core.util.DateUtils;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisCheckEmailDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsLoginUserDto;
import com.ylink.inetpay.common.project.cbs.exception.ECbsErrorCode;
import com.ylink.inetpay.common.project.portal.enums.ErrorMsgEnum;
import com.ylink.inetpay.common.project.portal.vo.UserCheckVO;
@Service("bisCheckEmailService")
public class BisCheckEmailServiceImpl implements BisCheckEmailService {
	
	@Autowired
	private BisCheckEmailDtoMapper bisCheckEmailDtoMapper;
	@Autowired
	private BisSysParamService bisSysParamService;
	@Autowired
	private MrsLoginUserService mrsLoginUserService;

	private static Logger _log = LoggerFactory.getLogger(BisCheckEmailServiceImpl.class);


	@Override
	public void updateEmailStatus(EBisEmailTemplateCode operType,
			EBisEmailChectStatus checkStatus, String custId) {
		bisCheckEmailDtoMapper.updateEmailStatus(operType,checkStatus,custId);
	}

	@Override
	@Transactional(value=CbsConstants.TX_MANAGER_BIS)
	public MrsLoginUserDto shieCheckEmail(String checkMessage, String id, EBisEmailTemplateCode template) {
		
		if(EBisEmailTemplateCode.SET_EMAIL.equals(template) || EBisEmailTemplateCode.UPDATE_EMAIL.equals(template)) {
			// 修改和设置用一个邮件模板
			template = EBisEmailTemplateCode.SET_EMAIL;
		}
		List<BisCheckEmailDto> checkEmailList = bisCheckEmailDtoMapper.
				getByCustIdOrCheckMessageAndOperTypeAndStatus(id, checkMessage, template, EBisEmailChectStatus.PENDING_CHECK);
		MrsLoginUserDto loginUser = null;
		if(checkEmailList == null|| checkEmailList.size()==0){
			_log.error("邮件[id = "+id+" or checkMessage = "+checkMessage+"] template = "+template+", "
					+ "status = "+EBisEmailChectStatus.PENDING_CHECK+"]不存在");
			loginUser = new MrsLoginUserDto();
			loginUser.setUserCheckVo(new UserCheckVO(false, ErrorMsgEnum.INVALID_EMAIL));
			return loginUser;
		}
		BisCheckEmailDto checkEmail = checkEmailList.get(0);
		// 两者不相等时 
		if(!(checkEmail.getCheckMessage().equals(checkMessage) && checkEmail.getCustId().equals(id))){
			checkEmail.setStatus(EBisEmailChectStatus.CHECK_REJECT);
			_log.error("邮件验证："+ECbsErrorCode.EMAIL_UNEXIST_ERROR.getDisplayName());
			bisCheckEmailDtoMapper.updateByPrimaryKey(checkEmail);
			loginUser = new MrsLoginUserDto();
			loginUser.setUserCheckVo(new UserCheckVO(false, ErrorMsgEnum.INVALID_EMAIL));
			return loginUser;
		}
		
		// 获取邮件有效时间。
		String value = bisSysParamService.getValue(SHIEConfigConstant.EMAIL_PERIOD);
		Date nextHour = DateUtils.getNextHour(checkEmail.getCreateTime(), Integer.valueOf(value));
		// 判断邮件是否超时    > 0 邮件有效期超时
		if(DateUtils.compare_date(nextHour) < 0 ){
			_log.info("邮件超时，验证失败");
			checkEmail.setStatus(EBisEmailChectStatus.CHECK_REJECT);
			bisCheckEmailDtoMapper.updateByPrimaryKey(checkEmail);
			loginUser = new MrsLoginUserDto();
			loginUser.setUserCheckVo(new UserCheckVO(false, ErrorMsgEnum.MAIL_TIMEOUT));
			return loginUser;
		}
		loginUser = mrsLoginUserService.getEffectLoginUserById(checkEmail.getCustId());
		
		if(loginUser == null){
			_log.error("邮件验证失败：用户编号【"+checkEmail.getCustId()+"】的用户不存在");
			//bisExceptionLogServiceImpl.saveLog(CbsExceptionLogDtoUtils.getBisExceptionLog(EBisExceptionLogNlevel.ERROR, EBisExceptionLogType.EMAIL_CHECK,errMsg));
			loginUser = new MrsLoginUserDto();
			loginUser.setUserCheckVo(new UserCheckVO(false, ErrorMsgEnum.USER_NULL));
			return loginUser;
		}
		loginUser.setEmail(checkEmail.getEmail());
		checkEmail.setStatus(EBisEmailChectStatus.CHECK_PASS);
		bisCheckEmailDtoMapper.updateByPrimaryKey(checkEmail);
		loginUser.setUserCheckVo(new UserCheckVO(true));
		return loginUser;
	}
}
