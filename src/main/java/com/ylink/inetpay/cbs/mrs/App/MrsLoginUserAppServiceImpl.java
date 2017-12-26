package com.ylink.inetpay.cbs.mrs.App;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;
import org.ylinkpay.framework.core.util.MD5Utils;

import com.shie.constant.SHIEConfigConstant;
import com.ylink.eu.util.tools.CollectionUtil;
import com.ylink.eu.util.tools.StringUtil;
import com.ylink.inetpay.cbs.mrs.service.MrsAccountService;
import com.ylink.inetpay.cbs.mrs.service.MrsLoginUserService;
import com.ylink.inetpay.cbs.mrs.service.MrsUserPayPasswordService;
import com.ylink.inetpay.common.core.constant.LoginUserStatus;
import com.ylink.inetpay.common.core.constant.MrsAccountStatus;
import com.ylink.inetpay.common.core.constant.MrsCustType;
import com.ylink.inetpay.common.core.constant.MrsCustomerType;
import com.ylink.inetpay.common.core.constant.MrsPlatformCode;
import com.ylink.inetpay.common.core.util.ExceptionProcUtil;
import com.ylink.inetpay.common.project.cbs.app.MrsLoginUserAppService;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsLoginUserDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsUserPayPasswordDto;
import com.ylink.inetpay.common.project.portal.dto.AccountMsg;
import com.ylink.inetpay.common.project.portal.enums.ErrorMsgEnum;
import com.ylink.inetpay.common.project.portal.exception.PortalCheckedException;
import com.ylink.inetpay.common.project.portal.vo.UserCheckVO;

@Service("mrsLoginUserAppService")
public class MrsLoginUserAppServiceImpl implements MrsLoginUserAppService{
	
	private static Logger log = LoggerFactory.getLogger(MrsLoginUserAppServiceImpl.class);
	@Autowired
	private MrsLoginUserService mrsLoginUserService;
	@Autowired
	private MrsAccountService mrsAccountService;
	@Autowired
	private MrsUserPayPasswordService mrsUserPayPasswordService;
	/**
	 * 支付密码验证
	 * @param dto
	 * @return
	 * @throws PortalCheckedException 
	 */
	@Override
    public MrsLoginUserDto doPayPasswCheck(MrsLoginUserDto dto) {
		log.info("支付密码校验，用户CustId{}支付密码为{}",dto.getCustId(),MD5Utils.MD5(dto.getPayPwd() + SHIEConfigConstant.SALT));
		try {
			MrsLoginUserDto loginUser = mrsLoginUserService.doPayPasswCheck(dto);
			return loginUser;
		} catch (Exception e) {
			log.error("支付密码验证失败:", e);
			UserCheckVO vo = new UserCheckVO(false, ErrorMsgEnum.USER_PAY_PWD_ERROR );
			MrsLoginUserDto retDto = new MrsLoginUserDto();
			retDto.setUserCheckVo(vo);
			return retDto;
		}
    }
	
	@Override
	public MrsLoginUserDto doLogin(MrsLoginUserDto dto) {
		try {
			dto.setCustomerType(MrsCustomerType.MCT_0.getValue());
			dto.setRegisterType(MrsCustType.MRS_CUST_TYPE_01.getValue());
			MrsLoginUserDto loginUser = mrsLoginUserService.doLogin(dto);
			if(loginUser == null){
				log.info("登陆信息不存在");
				UserCheckVO vo = new UserCheckVO(false, ErrorMsgEnum.CUSTOMER_SEAARCH_FAIL);
				MrsLoginUserDto retDto = new MrsLoginUserDto();
				retDto.setUserCheckVo(vo);
				return retDto;
			}
			// 置空登陆密码和支付密码
			loginUser.setLoginPwd("");
			loginUser.setPayPwd("");
			if(!loginUser.getUserCheckVo().isCheckValue()) {
				log.info("登陆失败:"+loginUser.getUserCheckVo().getMsg());
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
				//只是被动开户的个人去提示设置密码，账户状态是未生效
				if(MrsCustomerType.MCT_0.getValue().equals(accountMsgs.get(0).getCustomerType()) 
						&&loginUser.isFirstLogin() 
						&& MrsAccountStatus.ACCOUNT_STATUS_9.getValue().equals(accountMsgs.get(0).getAccountStatus())
						&& !MrsPlatformCode.ACCOUNT.getValue().equals(accountMsgs.get(0).getPlatformCode())){
					loginUser.setFirstLogin(true);
				}else{
					loginUser.setFirstLogin(false);
				}
				//检查支付密码
				MrsUserPayPasswordDto payDto = mrsUserPayPasswordService.findByCustId(accountMsgs.get(0).getCustId());
				if(payDto!=null
						&&  StringUtil.isNEmpty(payDto.getPassword())){
					loginUser.setSetPayPwd(true);
				}
			}
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
			
			UserCheckVO vo = new UserCheckVO(true);
			loginUser.setUserCheckVo(vo);
			return loginUser;
		} catch (Exception e) {
			log.error("登陆失败:", e);
			UserCheckVO vo = new UserCheckVO(false, ErrorMsgEnum.LOGIN_FAIL );
			MrsLoginUserDto retDto = new MrsLoginUserDto();
			retDto.setUserCheckVo(vo);
			return retDto;
		}
	}
	
	@Override
	public MrsLoginUserDto doLoginForToken(MrsLoginUserDto loginUser) {
		try {
			if(loginUser == null){
				log.info("登陆信息不存在");
				UserCheckVO vo = new UserCheckVO(false, ErrorMsgEnum.CUSTOMER_SEAARCH_FAIL);
				MrsLoginUserDto retDto = new MrsLoginUserDto();
				retDto.setUserCheckVo(vo);
				return retDto;
			}
			
			// 判断用户状态
			if (LoginUserStatus.LOGIN_USER_STATUS_2.getValue().equals(loginUser.getUserStatus())) {
				UserCheckVO vo = new UserCheckVO(false, ErrorMsgEnum.USER_CANCEL);
				loginUser.setUserCheckVo(vo);
				return loginUser;
			} 
			
			// 记录登陆信息
			loginUser.setLoginTime(new Date());
			mrsLoginUserService.updateDto(loginUser);
			
			// 置空登陆密码和支付密码
			loginUser.setLoginPwd("");
			loginUser.setPayPwd("");
		
			// 根据登陆信息获取账户信息
			List<AccountMsg> accountMsgs = mrsAccountService.findPersonAccountMsgByLoginUserId(loginUser.getId());
			if(CollectionUtil.isEmpty(accountMsgs)) {
				log.info("根据登陆ID[id="+loginUser.getId()+"]没有找到账户信息");
				// 二期改造    表示客户只有客户信息没有账户信息
				loginUser.setHasAcnt(false);
			} else {
				loginUser.setHasAcnt(true);
				loginUser.setAccountMsgs(accountMsgs);
				//只是被动开户的个人去提示设置密码，账户状态是未生效
				if(MrsCustomerType.MCT_0.getValue().equals(accountMsgs.get(0).getCustomerType()) 
						&&loginUser.isFirstLogin() 
						&& MrsAccountStatus.ACCOUNT_STATUS_9.getValue().equals(accountMsgs.get(0).getAccountStatus())
						&& !MrsPlatformCode.ACCOUNT.getValue().equals(accountMsgs.get(0).getPlatformCode())){
					loginUser.setFirstLogin(true);
				}else{
					loginUser.setFirstLogin(false);
				}
				//检查支付密码
				MrsUserPayPasswordDto payDto = mrsUserPayPasswordService.findByCustId(accountMsgs.get(0).getCustId());
				if(payDto!=null
						&&  StringUtil.isNEmpty(payDto.getPassword())){
					loginUser.setSetPayPwd(true);
				}
			}
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
			
			UserCheckVO vo = new UserCheckVO(true);
			loginUser.setUserCheckVo(vo);
			return loginUser;
		} catch (Exception e) {
			log.error("登陆失败:", e);
			UserCheckVO vo = new UserCheckVO(false, ErrorMsgEnum.LOGIN_FAIL );
			MrsLoginUserDto retDto = new MrsLoginUserDto();
			retDto.setUserCheckVo(vo);
			return retDto;
		}
	}

	@Override
	public MrsLoginUserDto doOrganLogin(MrsLoginUserDto dto) {
		try {
			//MrsLoginUserDto loginUser = mrsLoginUserService.doOrganLogin(dto);
			dto.setCustomerType(MrsCustomerType.MCT_1.getValue());
			dto.setRegisterType(MrsCustType.MRS_CUST_TYPE_02.getValue());
			dto.setMobile(null);
			MrsLoginUserDto loginUser = mrsLoginUserService.doLogin(dto);
			if(loginUser == null){
				UserCheckVO vo = new UserCheckVO(false, ErrorMsgEnum.CUSTOMER_SEAARCH_FAIL);
				MrsLoginUserDto retDto = new MrsLoginUserDto();
				retDto.setUserCheckVo(vo);
				return retDto;
			}
			if(!loginUser.getUserCheckVo().isCheckValue()) {
				return loginUser;
			}
 			List<AccountMsg> accountMsgs = mrsAccountService.findOrganAccountMsgByLoginUserId(loginUser.getId());
			if(CollectionUtil.isEmpty(accountMsgs)) {
				log.info("根据登陆ID[id="+loginUser.getId()+"]没有找到账户信息");
				// 二期改造    表示客户只有客户信息没有账户信息
				loginUser.setHasAcnt(false);
			} else {
				loginUser.setHasAcnt(true);
				loginUser.setFirstLogin(false);
				loginUser.setAccountMsgs(accountMsgs);
			}
			UserCheckVO vo = new UserCheckVO(true);
			loginUser.setUserCheckVo(vo);
			return loginUser;
		/*} catch (PortalCheckedException e) {
			ErrorMsgEnum errorMsg = ErrorMsgEnum.getErrorMsgEnumValue(e.getMessage());
			log.error("登陆失败:"+errorMsg.getValue());
			UserCheckVO vo = new UserCheckVO(false, errorMsg);
			MrsLoginUserDto retDto = new MrsLoginUserDto();
			retDto.setUserCheckVo(vo);
			return retDto;*/
		} catch (Exception e) {
			log.error("登陆失败:", e);
			UserCheckVO vo = new UserCheckVO(false, ErrorMsgEnum.LOGIN_FAIL);
			MrsLoginUserDto retDto = new MrsLoginUserDto();
			retDto.setUserCheckVo(vo);
			return retDto;
		}
	}

	@Override
	public MrsLoginUserDto getByLoginName(MrsLoginUserDto dto) {
		try {
			MrsLoginUserDto loginUser = mrsLoginUserService.getLoginUser(dto);
			if(loginUser == null){
				UserCheckVO vo = new UserCheckVO(false, ErrorMsgEnum.CUSTOMER_SEAARCH_FAIL);
				MrsLoginUserDto retDto = new MrsLoginUserDto();
				retDto.setUserCheckVo(vo);
				return retDto;
			}
			// by yuqingjun 20170413 不启用下面代码，个人与机构都可找回密码
			/*MrsCustomerType customerType = mrsAccountService.getCustTypeByLoginId(loginUser.getId());
			if(customerType != null && !MrsCustomerType.MCT_0.equals(customerType)){
				log.error("客户[loginId = "+loginUser.getId()+"]类型不存在");
				UserCheckVO vo = new UserCheckVO(false, ErrorMsgEnum.FIND_PWD_CUSTOMER_ERROR);
				MrsLoginUserDto retDto = new MrsLoginUserDto();
				retDto.setUserCheckVo(vo);
				return retDto;
			}*/

			MrsLoginUserDto retLoginUser = new MrsLoginUserDto();
			retLoginUser.setEmail(loginUser.getEmail());
			retLoginUser.setId(loginUser.getId());
			retLoginUser.setAlias(loginUser.getAlias());
			retLoginUser.setMobile(loginUser.getMobile());
			List<AccountMsg> accountMsgs = mrsAccountService.findPersonAccountMsgByLoginUserId(loginUser.getId());
			if(accountMsgs!=null && accountMsgs.size()>0){
				AccountMsg accountMsg = accountMsgs.get(0);
				// 发送邮件或短信时 短信用户需要用到客户名称
				retLoginUser.setCustomerName(accountMsg.getCustomerName());
				retLoginUser.setCustId(accountMsg.getCustId());
				// 二期改造    表示客户只有客户信息没有账户信息
				loginUser.setHasAcnt(true);
			}else{
				List<AccountMsg> accountMsgsOrgan = mrsAccountService.findOrganAccountMsgByLoginUserId(loginUser.getId());
				if(accountMsgsOrgan!=null && accountMsgsOrgan.size()>0){
					AccountMsg accountOrgan = accountMsgsOrgan.get(0);
					// 发送邮件或短信时 短信用户需要用到客户名称
					retLoginUser.setCustomerName(accountOrgan.getCustomerName());
					retLoginUser.setCustId(accountOrgan.getCustId());
					// 二期改造    表示客户只有客户信息没有账户信息
					loginUser.setHasAcnt(true);
				}else{
					loginUser.setHasAcnt(false);
				}
			}
			UserCheckVO vo = new UserCheckVO(true);
			retLoginUser.setUserCheckVo(vo);
			return retLoginUser;
		} catch (PortalCheckedException e) {
			ErrorMsgEnum errorMsg = ErrorMsgEnum.getErrorMsgEnumValue(e.getMessage());
			log.error("获取客户信息失败:"+errorMsg.getValue());
			UserCheckVO vo = new UserCheckVO(false, errorMsg);
			MrsLoginUserDto retDto = new MrsLoginUserDto();
			retDto.setUserCheckVo(vo);
			return retDto;
		} catch (Exception e) {
			log.error("获取客户信息失败:", e);
			UserCheckVO vo = new UserCheckVO(false, ErrorMsgEnum.CUSTOMER_SEAARCH_FAIL);
			MrsLoginUserDto retDto = new MrsLoginUserDto();
			retDto.setUserCheckVo(vo);
			return retDto;
		}
	}

	@Override
	public UserCheckVO updateLoginPwd(String id, String loginPwd) {
		try {
			mrsLoginUserService.updateLoginPwd(id, loginPwd);
			return new UserCheckVO(true);
		} catch (Exception e) {
			log.error("更新密码失败:", e);
			return new UserCheckVO(false,ErrorMsgEnum.PWD_UPDATE_ERROR);
		}
	}
	
	@Override
	public UserCheckVO upLoginPwd(MrsLoginUserDto usrDto) {
		try {
			mrsLoginUserService.upLoginPwd(usrDto);
			return new UserCheckVO(true);
		} catch (Exception e) {
			log.error("更新密码失败:", e);
			return new UserCheckVO(false,ErrorMsgEnum.PWD_UPDATE_ERROR);
		}
	}
	
	@Override
	public UserCheckVO updateLoginPwdByEmail(String loginId, String loginPwd){
		try {
			mrsLoginUserService.updateLoginPwdByEmail(loginId, loginPwd);
			return new UserCheckVO(true);
		} catch (PortalCheckedException e) {
			ErrorMsgEnum errorMsg = ErrorMsgEnum.getErrorMsgEnumValue(e.getMessage());
			log.error("更新密码失败:"+errorMsg.getValue());
			return new UserCheckVO(false, errorMsg );
		} catch (Exception e) {
			log.error("更新密码失败:" + ExceptionProcUtil.getExceptionDesc(e));
			return new UserCheckVO(false, ErrorMsgEnum.PWD_UPDATE_ERROR);
		}
	}

	@Override
	public UserCheckVO updateLoginPwd(String id, String oldPwd, String newPwd) {
		try {
			return mrsLoginUserService.updateLoginPwd(id, oldPwd, newPwd);
		} catch (Exception e) {
			log.error("更新密码失败:" + ExceptionProcUtil.getExceptionDesc(e));
			return new UserCheckVO(false, ErrorMsgEnum.PWD_UPDATE_ERROR);
		}
	}
	@Override
	public UserCheckVO updateLoginPwdByMobile(String id, String oldPwd, String newPwd,String mobile) {
		try {
			return mrsLoginUserService.updateLoginPwdByMobile(id, oldPwd, newPwd,mobile);
		} catch (Exception e) {
			log.error("更新密码失败:" + ExceptionProcUtil.getExceptionDesc(e));
			return new UserCheckVO(false, ErrorMsgEnum.PWD_UPDATE_ERROR);
		}
	}

    @Override
    public MrsLoginUserDto findUserDtoByLoginId(String id) {
        return mrsLoginUserService.findUserDtoByLoginId(id);
    }

    @Override
	public UserCheckVO isUsableAlias(String alias) {
		try {
			MrsLoginUserDto loginUser = mrsLoginUserService.findByAlias(alias);
			// loginUser为空  昵称可用    loginUser不为空  昵称不可用
			if(loginUser == null){
				return new UserCheckVO(true);
			} else { 
				return new UserCheckVO(false, ErrorMsgEnum.ALIAS_EXIST);
			}
		} catch (Exception e) {
			log.error("昵称查找失败:"+ExceptionProcUtil.getExceptionDesc(e));
			return new UserCheckVO(false, ErrorMsgEnum.ALIAS_SEARCH_FAIL);
		}
	}

	@Override
	public MrsLoginUserDto updateAlias(String loginId, String alias, MrsCustomerType customerType) {
		try {
			MrsLoginUserDto loginUser = mrsLoginUserService.updateAlias(loginId, alias);
			// 根据登陆信息获取账户信息
			
			List<AccountMsg> accountMsgs = null;
			if(MrsCustomerType.MCT_0.equals(customerType)){
				accountMsgs = mrsAccountService.findPersonAccountMsgByLoginUserId(loginUser.getId());
			} else {
				accountMsgs = mrsAccountService.findOrganAccountMsgByLoginUserId(loginUser.getId());
			}
			if(CollectionUtil.isEmpty(accountMsgs)) {
				log.info("根据登陆ID[id="+loginUser.getId()+"]没有找到账户信息");
				// 二期改造    表示客户只有客户信息没有账户信息
				loginUser.setHasAcnt(false);
			} else {
				loginUser.setHasAcnt(true);
				loginUser.setAccountMsgs(accountMsgs);
			}
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
			UserCheckVO vo = new UserCheckVO(true);
			loginUser.setLoginPwd("");
			loginUser.setPayPwd("");
			loginUser.setUserCheckVo(vo);
			return loginUser;
		} catch (PortalCheckedException e) {
			ErrorMsgEnum errorMsg = ErrorMsgEnum.getErrorMsgEnumValue(e.getMessage());
			log.error("更新昵称失败:"+errorMsg.getValue());
			UserCheckVO vo = new UserCheckVO(false, errorMsg);
			MrsLoginUserDto retDto = new MrsLoginUserDto();
			retDto.setUserCheckVo(vo);
			return retDto;
		} catch (Exception e) {
			log.error("更新昵称失败:", e);
			UserCheckVO vo = new UserCheckVO(false, ErrorMsgEnum.CUSTOMER_SEAARCH_FAIL);
			MrsLoginUserDto retDto = new MrsLoginUserDto();
			retDto.setUserCheckVo(vo);
			return retDto;
		}
	}

	@Override
	public UserCheckVO isExistEmail(String email) {
		try {
			MrsLoginUserDto dto = mrsLoginUserService.isExistEmail(email);
			if(dto == null) {
				return new UserCheckVO(true);
			}
			return new UserCheckVO(false);	
		} catch (Exception e) {
			log.error("查询失败:",e);
			return new UserCheckVO(false, ErrorMsgEnum.SEARCH_FAIL);
		}
	}

	@Override
	public UserCheckVO isExistMobile(String mobile) {
		try {
			MrsLoginUserDto dto = mrsLoginUserService.isExistMobile(mobile);
			if(dto == null) {
				return new UserCheckVO(true);
			}
			return new UserCheckVO(false);	
		} catch (Exception e) {
			log.error("查询失败:",e);
			return new UserCheckVO(false, ErrorMsgEnum.SEARCH_FAIL);
		}
	}


	@Override
	public MrsLoginUserDto doPersonReg(MrsLoginUserDto dto) {
		try {
			// 检查昵称是否被注册
			if (StringUtils.isNotEmpty(dto.getAlias())) {
				UserCheckVO aliasCheck = isUsableAlias(dto.getAlias());
				if (!aliasCheck.isCheckValue()) {
					log.info("昵称已存在");
					dto.setUserCheckVo(aliasCheck);
					return dto;
				}
			}
			// 检查手机号是否被注册
			if (StringUtils.isNotEmpty(dto.getMobile())) {
				UserCheckVO mobileCheck = isExistMobile(dto.getMobile());
				if (!mobileCheck.isCheckValue()) {
					log.info("手机号已存在");
					dto.setUserCheckVo(mobileCheck);
					return dto;
				}
			}
			// 保存登录信息
			mrsLoginUserService.saveLoginUser(dto);
			UserCheckVO vo = new UserCheckVO(true);
			dto.setUserCheckVo(vo);
			return dto;
		} catch (Exception e) {
			log.error("注册个人用户失败:", e);
			UserCheckVO vo = new UserCheckVO(false, ErrorMsgEnum.SYSTEM_ERROR);
			MrsLoginUserDto retDto = new MrsLoginUserDto();
			retDto.setUserCheckVo(vo);
			return retDto;
		}
	}

	@Override
	public int insertSelective(MrsLoginUserDto dto) {
		return mrsLoginUserService.insertSelective(dto);
	}

	@Override
	public PageData<MrsLoginUserDto> findList(PageData<MrsLoginUserDto> pageData, MrsLoginUserDto userDto) {
		return mrsLoginUserService.findPerson(pageData, userDto);
	}

	@Override
	public MrsLoginUserDto selectByPrimaryKey(String id) {
		return mrsLoginUserService.selectByPrimaryKey(id);
	}
	@Override
	public List<MrsLoginUserDto> findByMobileAndAlias(MrsLoginUserDto dto) {
		return mrsLoginUserService.findByMobileAndAlias(dto);
	}

	@Override
	public List<MrsLoginUserDto> findByCustId(String custId) {
		return mrsLoginUserService.findByCustId(custId);
	}
	@Override
	public PageData<MrsLoginUserDto> listWidthRolesPage(PageData<MrsLoginUserDto> pageDate,
			MrsLoginUserDto mrsuser) {
			return mrsLoginUserService.finduser(pageDate, mrsuser);
		}
	
	@Override
	public PageData<MrsLoginUserDto> findId(PageData<MrsLoginUserDto> pageData,MrsLoginUserDto id) {
		log.debug("MrsPersonAppServiceImpl.findCertByCustId run....");
		return mrsLoginUserService.findId(pageData,id);
	}

	@Override
	public UserCheckVO getUserAuditInfo(String loginId) {
		return mrsLoginUserService.getUserAuditInfo(loginId);
	}
	@Override
	public UserCheckVO getUserAuditInfoStatus(String loginId) {
		return mrsLoginUserService.getUserAuditInfoStatus(loginId);
	}

	@Override
	public MrsLoginUserDto findMainByCustId(String custId) {
		return mrsLoginUserService.findMainByCustId(custId);
	}

	@Override
	public List<MrsLoginUserDto> getCustIdsByLoginUserId(String id) {
		// TODO Auto-generated method stub
		return mrsLoginUserService.getCustIdsByLoginUserId(id);
	}

	@Override
	public MrsLoginUserDto selectLoginUserByCustId(String custId) {
		return mrsLoginUserService.selectLoginUserByCustId(custId);
	}
	@Override
	public boolean checkUserPayPwd(String custId) {
		return mrsLoginUserService.checkUserPayPwd(custId);
	}
}
