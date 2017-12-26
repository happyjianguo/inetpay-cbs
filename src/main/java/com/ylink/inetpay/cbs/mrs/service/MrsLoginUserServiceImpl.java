package com.ylink.inetpay.cbs.mrs.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.ylinkpay.framework.core.model.PageData;
import org.ylinkpay.framework.core.util.MD5Utils;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.shie.constant.SHIEConfigConstant;
import com.ylink.eu.util.tools.CollectionUtil;
import com.ylink.eu.util.tools.StringUtil;
import com.ylink.inetpay.cbs.bis.service.BisSysParamService;
import com.ylink.inetpay.cbs.common.CbsConstants;
import com.ylink.inetpay.cbs.mrs.dao.MrsAduitInfoDtoMapper;
import com.ylink.inetpay.cbs.mrs.dao.MrsAduitPersonHisDtoMapper;
import com.ylink.inetpay.cbs.mrs.dao.MrsLoginUserDtoMapper;
import com.ylink.inetpay.cbs.mrs.dao.MrsUserAccountDtoMapper;
import com.ylink.inetpay.cbs.mrs.dao.MrsUserPayPasswordDtoMapper;
import com.ylink.inetpay.common.core.constant.EAduitTypeEnum;
import com.ylink.inetpay.common.core.constant.LoginUserStatus;
import com.ylink.inetpay.common.core.constant.MrsCustomerType;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisSysParamDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsAduitInfoDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsAduitPersonHisDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsLoginUserDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsUserAccountDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsUserPayPasswordDto;
import com.ylink.inetpay.common.project.portal.enums.ErrorMsgEnum;
import com.ylink.inetpay.common.project.portal.exception.PortalCheckedException;
import com.ylink.inetpay.common.project.portal.vo.UserCheckVO;

@Service("mrsLoginUserService")
public class MrsLoginUserServiceImpl implements MrsLoginUserService {

	private static Logger log = LoggerFactory.getLogger(MrsLoginUserServiceImpl.class);

	@Autowired
	private MrsLoginUserDtoMapper mrsLoginUserDtoMapper;
	@Autowired
	private BisSysParamService bisSysParamService;
	@Autowired
	private  MrsAduitPersonHisDtoMapper mrsAduitPersonHisDtoMapper;
	@Autowired
	private MrsAduitInfoDtoMapper mrsAduitInfoDtoMapper;
	@Autowired
	private MrsUserPayPasswordDtoMapper mrsUserPayPasswordDtoMapper;
	@Autowired
	private MrsUserAccountDtoMapper mrsUserAccountDtoMapper;

    /**
     * 根据上锁时间和锁定时间(单位：小时)获取解锁时间
     *
     * @param lockTime
     * @param lockHour
     * @return
     */
    public static Date getUnlockTime(Date lockTime, int lockHour) {
        Calendar lockCal = Calendar.getInstance();
        lockCal.setTime(lockTime);
        lockCal.add(Calendar.HOUR, lockHour);
        return lockCal.getTime();
    }

    /**
     * 比较date和当前时间的大小 系统时间大于或等于date返回 true 系统时间小于date返回 false
     *
     * @param date
     * @return
     */
    public static boolean compareSysTime(Date date) {
        return new Date().compareTo(date) >= 0;
    }

    /**校验支付密码 **/
	@Override
	public MrsLoginUserDto doPayPasswCheck(MrsLoginUserDto loginUser) {
		MrsLoginUserDto loginUserDto = new MrsLoginUserDto();
		UserCheckVO uservo = new UserCheckVO(true);
		uservo.setMsg("成功");
		loginUserDto.setUserCheckVo(uservo);
		/**
		 * 用户24小时内连续因密码错误导致失败次数超过3次，则系统自动锁定用户1小时，并提示用户1小时之后再试。
		 */
		String payPwd = MD5Utils.MD5(loginUser.getPayPwd() + SHIEConfigConstant.SALT);
		// 根据一户通号查询登录密码
		MrsUserPayPasswordDto mrsUserPayPasswordDto = mrsUserPayPasswordDtoMapper.selectByCustId(loginUser.getCustId());
		if (mrsUserPayPasswordDto == null) {
			log.error("根据一户通号查询支付密码[id=" + loginUserDto.getId() + "],没有对应的数据");
			UserCheckVO vo = new UserCheckVO(false, ErrorMsgEnum.USER_NO_PAY_PWD_ERROR);
			loginUserDto.setUserCheckVo(vo);
			return loginUserDto;
		}
		String oldPayPwd = mrsUserPayPasswordDto.getPassword();
		String lockHourStr = "";// 支付密码锁定时间(单位:小时)
		String maxFailNumStr = "";// 支付密码错误次数
		String continuedHourStr = "";// 支付密码错误次数持续时间
		// 获取系统配置的锁定时间和最大错误次数
		List<String> paramNames = new ArrayList<>();
		paramNames.add(SHIEConfigConstant.PAY_LOCK_HOUR);
		paramNames.add(SHIEConfigConstant.PAY_MAX_FAIL_NUM);
		paramNames.add(SHIEConfigConstant.PAY_PWD_CONTINUED_HOUR);
		List<BisSysParamDto> sysParamList = bisSysParamService.findByParamNames(paramNames);
		if (sysParamList != null && !sysParamList.isEmpty()) {
			for (BisSysParamDto sysParamDto : sysParamList) {
				if (SHIEConfigConstant.PAY_LOCK_HOUR.equals(sysParamDto.getKey())) {
					lockHourStr = sysParamDto.getValue();
					continue;
				}
				if (SHIEConfigConstant.PAY_MAX_FAIL_NUM.equals(sysParamDto.getKey())) {
					maxFailNumStr = sysParamDto.getValue();
					continue;
				}
				if (SHIEConfigConstant.PAY_PWD_CONTINUED_HOUR.equals(sysParamDto.getKey())) {
					continuedHourStr = sysParamDto.getValue();
					continue;
				}
			}
		}
		int lockHour = Integer.valueOf(lockHourStr);
		int maxFailNum = Integer.valueOf(maxFailNumStr);
		int continuedHour = Integer.valueOf(continuedHourStr);

		//先比较次数达到锁定次数,与时间是否在锁定时间的范围内
		Short errorNo = StringUtil.isEmpty(mrsUserPayPasswordDto.getErrorNum()+"")?0:mrsUserPayPasswordDto.getErrorNum();
		//如果次数到最大次数，再看锁定时间
		if (errorNo.equals(Short.valueOf(maxFailNumStr))) {
			Date lockTime = mrsUserPayPasswordDto.getLastErrTime(); // 获取锁定账户时间
			if (compareSysTime(getUnlockTime(lockTime, lockHour))) {
				// 解锁,清空锁定时间和次数为0
				mrsUserPayPasswordDtoMapper.clearLockTime(mrsUserPayPasswordDto.getId());
			} else {
				// 未达到解锁时间 不用解锁
				log.info("用户[id=" + loginUserDto.getId() + "]已锁定");
				UserCheckVO vo = new UserCheckVO(false);
				vo.setMsg("密码已连续错误" + maxFailNumStr + "次,将锁定" + lockHourStr + "小时");
				loginUserDto.setUserCheckVo(vo);
				return loginUserDto;
			}
		}
		// 判断密码是否相等
		if (!oldPayPwd.equals(payPwd)) {
			// 密码不相等
			log.info("用户[cusitId=" + loginUserDto.getCustId() + "]支付密码错误");
			// 判断登陆密码连续错误次数+1后判断是否大于等于3
			// 如果是则将登陆用户锁定，并将用户状态设置为锁定状态，登陆密码连续错误次数重置为3，上锁时间设置为当前系统时间
			// 如果不是则将登陆密码错误加1
			Short failNumShort =StringUtil.isEmpty(mrsUserPayPasswordDto.getErrorNum()+"")?0:mrsUserPayPasswordDto.getErrorNum();
			Date firsetPwdErrorTime = mrsUserPayPasswordDto.getLastErrTime(); // 第一次密码错误时间
			firsetPwdErrorTime = firsetPwdErrorTime == null ? new Date() : firsetPwdErrorTime;
			int failNum = failNumShort == null ? 0 : failNumShort;
			failNum++;
			if (compareSysTime(getUnlockTime(firsetPwdErrorTime, continuedHour))) {
				// 当前系统时间大（ 第一次密码错误时间失效）
				mrsUserPayPasswordDto.setErrorNum((short) 1);
				mrsUserPayPasswordDto.setLastErrTime(new Date());
				// 更新信息
				mrsUserPayPasswordDtoMapper.updateByPrimaryKeySelective(mrsUserPayPasswordDto);
				UserCheckVO vo = new UserCheckVO(false, ErrorMsgEnum.USER_PAY_PWD_ERROR); // 报错：密码错误
				loginUserDto.setUserCheckVo(vo);
				return loginUserDto;
			} else {
				 if (failNum == 1) {
					// 登陆密码错误次数加1 首次错误记录登陆错误时间
					mrsUserPayPasswordDto.setErrorNum((short) failNum);
					mrsUserPayPasswordDto.setLastErrTime(new Date());
					// 更新信息
					mrsUserPayPasswordDtoMapper.updateByPrimaryKeySelective(mrsUserPayPasswordDto);
					UserCheckVO vo = new UserCheckVO(false, ErrorMsgEnum.USER_PAY_PWD_ERROR);
					loginUserDto.setUserCheckVo(vo);
					return loginUserDto;
				} else {
					mrsUserPayPasswordDto.setErrorNum((short) failNum);
					// 更新信息
					mrsUserPayPasswordDtoMapper.updateByPrimaryKeySelective(mrsUserPayPasswordDto);
					UserCheckVO vo = new UserCheckVO(false, ErrorMsgEnum.USER_PAY_PWD_ERROR);
					loginUserDto.setUserCheckVo(vo);
					return loginUserDto;
				}
			}
		}
		//密码正常，如果有错误次数，就清除
		Short failNumShort = StringUtil.isEmpty(mrsUserPayPasswordDto.getErrorNum()+"")?0:mrsUserPayPasswordDto.getErrorNum();
		if(failNumShort < maxFailNum){
			// 解锁,清空锁定时间和次数为0
			mrsUserPayPasswordDtoMapper.clearLockTime(mrsUserPayPasswordDto.getId());
		}
		return loginUserDto;
	}

	@Override
	// @Transactional(value = CbsConstants.TX_MANAGER_MRS)
	// 单表操作不需要控制事物，且需要异常往外带出错误信息给客户
	public MrsLoginUserDto doLogin(MrsLoginUserDto loginUser) {

		/**
		 * 用户24小时内连续因密码错误导致登录失败次数超过3次，则系统自动锁定用户1小时，并提示用户1小时之后再试。
		 */
		String loginPwd = "";
		if (loginUser.isSysLogin()) {
			loginPwd = loginUser.getLoginPwd();
		} else {
			loginPwd = MD5Utils.MD5(loginUser.getLoginPwd() + SHIEConfigConstant.SALT);
		}
		List<MrsLoginUserDto> loginUserList = mrsLoginUserDtoMapper.findByLoginMsg(loginUser);
		MrsLoginUserDto loginUserDto = null;

		if (CollectionUtil.isEmpty(loginUserList)) {
			// 查询是否是门户注册的用户
			List<MrsLoginUserDto> myloginUserList = mrsLoginUserDtoMapper.findByMobileAndAlias(loginUser);
			if (CollectionUtil.isEmpty(myloginUserList)) {
				return null;
			}else{
				loginUserDto = myloginUserList.get(0);
			}
		}else{
			// 如果根据登陆信息查询出多个信息，则是一个一户通账户对应多个用户
			loginUserDto = loginUserList.get(0);
		}
		//只是个人去提示设置密码,如果登录时间为空，则设置为第一次登录
		if(MrsCustomerType.MCT_0.getValue().equals(loginUser.getCustomerType())
				&&loginUserDto.getLoginTime()==null){
			loginUserDto.setFirstLogin(true);
		}
		return doLogin(loginUserDto, loginPwd,loginUserDto.getLoginPwd());

	}

	private MrsLoginUserDto doLogin(MrsLoginUserDto loginUserDto,String fillPwd,String dbPwd){
		// 获取系统配置的锁定时间和最大错误次数
				String lockHourStr = bisSysParamService.getValue(SHIEConfigConstant.LOCK_HOUR);
				String maxFailNumStr = bisSysParamService.getValue(SHIEConfigConstant.MAX_FAIL_NUM);
				String continuedHourStr = bisSysParamService.getValue(SHIEConfigConstant.LOGIN_PWD_CONTINUED_HOUR);
				int lockHour = Integer.valueOf(lockHourStr);
				int maxFailNum = Integer.valueOf(maxFailNumStr);
				int continuedHour = Integer.valueOf(continuedHourStr);

				// 判断密码是否相等
				if (fillPwd.equals(dbPwd)) {
					// 密码相等
					// 判断用户状态是否被锁定
					if (LoginUserStatus.LOGIN_USER_STATUS_0.getValue().equals(loginUserDto.getUserStatus())) {
						// 用户状态正常
						// 更新上次登录时间为当前时间，将登陆密码连续错误次数设置为0，上锁时间设置为空
						loginUserDto.setLoginTime(new Date());
						loginUserDto.setLoginFailNum((short) 0);
						loginUserDto.setLastLoginLockTime(null);
						loginUserDto.setUserCheckVo(new UserCheckVO(true));
					} else if (LoginUserStatus.LOGIN_USER_STATUS_1.getValue().equals(loginUserDto.getUserStatus())) {
						// 用户状态不正常 判断是否需要解锁
						// 判断密码上锁时间加上系统设置的锁定时间是否大于当前时间，
						// 如果是则解锁（将登陆密码连续错误次数设置为1，上次成功登陆时间不变，上锁时间设置为空）
						Date lockTime = loginUserDto.getLastLoginLockTime(); // 获取锁定账户时间
						if (compareSysTime(getUnlockTime(lockTime, lockHour))) {
							// 解锁
							log.info("登陆用户[id=" + loginUserDto.getId() + "]解锁");
							loginUserDto.setLoginTime(new Date()); // 设置登录时间
							loginUserDto.setLastLoginLockTime(null);
							loginUserDto.setLoginFailNum((short) 0);
							loginUserDto.setUserStatus(LoginUserStatus.LOGIN_USER_STATUS_0.getValue());
							loginUserDto.setUserCheckVo(new UserCheckVO(true));
						} else {
							// 未达到解锁时间 不用解锁
							log.info("登陆用户[id=" + loginUserDto.getId() + "]已锁定");
							UserCheckVO vo = new UserCheckVO(false);
							vo.setMsg("用户密码已连续错误" + maxFailNumStr + "次,用户将锁定" + lockHourStr + "小时");
							loginUserDto.setUserCheckVo(vo);
							return loginUserDto;
						}
					} else if (LoginUserStatus.LOGIN_USER_STATUS_2.getValue().equals(loginUserDto.getUserStatus())) {
						log.info("登陆用户[id=" + loginUserDto.getId() + "]已注销");
						UserCheckVO vo = new UserCheckVO(false, ErrorMsgEnum.USER_CANCEL);
						loginUserDto.setUserCheckVo(vo);
						return loginUserDto;
					}
					// 更新信息
					mrsLoginUserDtoMapper.updateByPrimaryKey(loginUserDto);
					return loginUserDto;
				} else {
					// 密码不相等
					log.info("登陆用户[id=" + loginUserDto.getId() + "]密码错误");
					// 如果用户已经注销 直接返回用户已被注销
					// 如用用户状态正常：
					// 判断登陆密码连续错误次数+1后判断是否大于等于3
					// 如果是则将登陆用户锁定，并将用户状态设置为锁定状态，登陆密码连续错误次数重置为3，上锁时间设置为当前系统时间
					// 如果不是则将登陆密码错误加1
					// 如果用户状态为已经锁定：
					// 根据上锁时间和锁定时间（以小时为单位）获取解锁时间
					// 如果系统时间大于或等于当前解锁时间，则将用户状态设置为正常状态，登陆密码连续错误次数设置为1，上锁时间设置为空,并返回登录名或密码错误
					// 如果系统时间小于当前解锁时间，则返回登录名或密码错误
					if (LoginUserStatus.LOGIN_USER_STATUS_0.getValue().equals(loginUserDto.getUserStatus())) {
						// 账户状态正常 判断是否需要锁定账户
						Short failNumShort = loginUserDto.getLoginFailNum();
						Date firsetPwdErrorTime = loginUserDto.getLastLoginLockTime(); // 第一次密码错误时间
						firsetPwdErrorTime = firsetPwdErrorTime == null ? new Date() : firsetPwdErrorTime;
						int failNum = failNumShort == null ? 0 : failNumShort;
						failNum++;
						if (compareSysTime(getUnlockTime(firsetPwdErrorTime, continuedHour))) {
							// 当前系统时间大（ 第一次密码错误时间失效）
							loginUserDto.setLoginFailNum((short) 1);
							loginUserDto.setLastLoginLockTime(new Date());
							mrsLoginUserDtoMapper.updateByPrimaryKey(loginUserDto);
							UserCheckVO vo = new UserCheckVO(false, ErrorMsgEnum.USER_PWD_ERROR); // 报错：用户名或密码错误
							loginUserDto.setUserCheckVo(vo);
							return loginUserDto;
						} else {
							// 当前系统时间小（ 第一次密码错误时间有效）
							if (failNum >= maxFailNum) {
								log.info("登陆用户[id=" + loginUserDto.getId() + "]锁定");
								// 达到最大错误次数 登陆密码错误次数设置为最大错误次数，用户状态为锁定状态，上锁时间为当前时间
								loginUserDto.setLoginFailNum((short) maxFailNum);
								loginUserDto.setUserStatus(LoginUserStatus.LOGIN_USER_STATUS_1.getValue());
								loginUserDto.setLastLoginLockTime(new Date());
								mrsLoginUserDtoMapper.updateByPrimaryKey(loginUserDto);
								// 密码错误
								UserCheckVO vo = new UserCheckVO(false);
								String value = ErrorMsgEnum.LOCK_PWD.getValue();
								value = value.replace("{0}", maxFailNumStr).replace("{1}", lockHourStr);
								vo.setMsg(value);
								loginUserDto.setUserCheckVo(vo);
								return loginUserDto;
							} else if (failNum == 1) {
								// 登陆密码错误次数加1 首次错误记录登陆错误时间
								loginUserDto.setLoginFailNum((short) failNum);
								loginUserDto.setLastLoginLockTime(new Date());
								mrsLoginUserDtoMapper.updateByPrimaryKey(loginUserDto);
								UserCheckVO vo = new UserCheckVO(false, ErrorMsgEnum.USER_PWD_ERROR);
								loginUserDto.setUserCheckVo(vo);
								return loginUserDto;
							} else {
								loginUserDto.setLoginFailNum((short) failNum);
								mrsLoginUserDtoMapper.updateByPrimaryKey(loginUserDto);
								UserCheckVO vo = new UserCheckVO(false, ErrorMsgEnum.USER_PWD_ERROR);
								loginUserDto.setUserCheckVo(vo);
								return loginUserDto;
							}
						}
					} else if (LoginUserStatus.LOGIN_USER_STATUS_1.getValue().equals(loginUserDto.getUserStatus())) {
						// 账户锁定 判断是否需要解锁账户
						Date lockTime = loginUserDto.getLastLoginLockTime(); // 上锁时间
						if (compareSysTime(getUnlockTime(lockTime, lockHour))) {
							// 解锁
							log.info("登陆用户[id=" + loginUserDto.getId() + "]解锁");
							loginUserDto.setLoginFailNum((short) 1);
							loginUserDto.setUserStatus(LoginUserStatus.LOGIN_USER_STATUS_0.getValue());
							loginUserDto.setLastLoginLockTime(new Date());
							mrsLoginUserDtoMapper.updateByPrimaryKey(loginUserDto);
							UserCheckVO vo = new UserCheckVO(false, ErrorMsgEnum.USER_PWD_ERROR);
							loginUserDto.setUserCheckVo(vo);
							return loginUserDto;
						} else {
							log.info("登陆用户[id=" + loginUserDto.getId() + "]已经被锁定");
							UserCheckVO vo = new UserCheckVO(false);
							String value = ErrorMsgEnum.LOCK_PWD.getValue();
							value = value.replace("{0}", maxFailNumStr);
							value = value.replace("{1}", lockHourStr);
							vo.setMsg(value);
							loginUserDto.setUserCheckVo(vo);
							return loginUserDto;
						}
					} else if (LoginUserStatus.LOGIN_USER_STATUS_2.getValue().equals(loginUserDto.getUserStatus())) {
						log.info("登陆用户[id=" + loginUserDto.getId() + "]已注销");
						UserCheckVO vo = new UserCheckVO(false, ErrorMsgEnum.USER_CANCEL);
						loginUserDto.setUserCheckVo(vo);
						return loginUserDto;
					} else {
						log.info("登陆用户[id=" + loginUserDto.getId() + "]状态不存在");
						return null;
					}
				}
	}

	@Override
	public MrsLoginUserDto doOrganLogin(MrsLoginUserDto loginUser) throws PortalCheckedException {
		/**
		 * 用户24小时内连续因密码错误导致登录失败次数超过3次，则系统自动锁定用户1小时，并提示用户1小时之后再试。
		 */
		MrsLoginUserDto loginUserDto = mrsLoginUserDtoMapper.findOrganLoginByCustId(loginUser.getCustId());
		if (loginUserDto == null) {
			// 如果根据一户通账号和主用户查询不到数据，则根据昵称查询
			loginUserDto = mrsLoginUserDtoMapper.findOrganLoginByAlias(loginUser.getAlias());
		}

		if (loginUserDto == null) {
			log.info("登陆信息不存在");
			return null;
		}

		// 获取系统配置的锁定时间和最大错误次数
		String lockHourStr = bisSysParamService.getValue(SHIEConfigConstant.LOCK_HOUR);
		String maxFailNumStr = bisSysParamService.getValue(SHIEConfigConstant.MAX_FAIL_NUM);
		String continuedHourStr = bisSysParamService.getValue(SHIEConfigConstant.LOGIN_PWD_CONTINUED_HOUR);
		int lockHour = Integer.valueOf(lockHourStr);
		int maxFailNum = Integer.valueOf(maxFailNumStr);
		int continuedHour = Integer.valueOf(continuedHourStr);

		String loginPwd = MD5Utils.MD5(loginUser.getLoginPwd() + SHIEConfigConstant.SALT);
		// 判断密码是否相等
		if (loginPwd.equals(loginUserDto.getLoginPwd())) {
			// 密码相等
			// 判断用户状态是否被锁定
			if (LoginUserStatus.LOGIN_USER_STATUS_0.getValue().equals(loginUserDto.getUserStatus())) {
				// 用户状态正常
				// 更新上次登录时间为当前时间，将登陆密码连续错误次数设置为0，上锁时间设置为空
				loginUserDto.setLoginTime(new Date());
				loginUserDto.setLoginFailNum((short) 0);
				loginUserDto.setLastLoginLockTime(null);
				loginUserDto.setUserCheckVo(new UserCheckVO(true));
			} else if (LoginUserStatus.LOGIN_USER_STATUS_1.getValue().equals(loginUserDto.getUserStatus())) {
				// 用户状态不正常 判断是否需要解锁
				// 判断密码上锁时间加上系统设置的锁定时间是否大于当前时间，
				// 如果是则解锁（将登陆密码连续错误次数设置为1，上次成功登陆时间不变，上锁时间设置为空）
				Date lockTime = loginUserDto.getLastLoginLockTime(); // 获取锁定账户时间
				if (compareSysTime(getUnlockTime(lockTime, lockHour))) {
					// 解锁
					log.info("登陆用户[id=" + loginUserDto.getId() + "]解锁");
					loginUserDto.setLoginTime(new Date()); // 设置登录时间
					loginUserDto.setLastLoginLockTime(null);
					loginUserDto.setLoginFailNum((short) 0);
					loginUserDto.setUserStatus(LoginUserStatus.LOGIN_USER_STATUS_0.getValue());
				} else {
					// 未达到解锁时间 不用解锁
					log.info("登陆用户[id=" + loginUserDto.getId() + "]已锁定");
					UserCheckVO vo = new UserCheckVO(false);
					vo.setMsg("用户密码已连续错误" + maxFailNumStr + "次,用户将锁定" + lockHourStr + "小时");
					loginUserDto.setUserCheckVo(vo);
					return loginUserDto;
				}
			} else if (LoginUserStatus.LOGIN_USER_STATUS_2.getValue().equals(loginUserDto.getUserStatus())) {
				log.info("登陆用户[id=" + loginUserDto.getId() + "]已注销");
				UserCheckVO vo = new UserCheckVO(false, ErrorMsgEnum.USER_CANCEL);
				loginUserDto.setUserCheckVo(vo);
				return loginUserDto;
			}
			// 更新信息
			mrsLoginUserDtoMapper.updateByPrimaryKey(loginUserDto);
			return loginUserDto;
		} else {
			// 密码不相等
			log.info("登陆用户[id=" + loginUserDto.getId() + "]密码错误");
			// 如果用户已经注销 直接返回用户已被注销
			// 如用用户状态正常：
			// 判断登陆密码连续错误次数+1后判断是否大于等于3
			// 如果是则将登陆用户锁定，并将用户状态设置为锁定状态，登陆密码连续错误次数重置为3，上锁时间设置为当前系统时间
			// 如果不是则将登陆密码错误加1
			// 如果用户状态为已经锁定：
			// 根据上锁时间和锁定时间（以小时为单位）获取解锁时间
			// 如果系统时间大于或等于当前解锁时间，则将用户状态设置为正常状态，登陆密码连续错误次数设置为1，上锁时间设置为空,并返回登录名或密码错误
			// 如果系统时间小于当前解锁时间，则返回登录名或密码错误
			if (LoginUserStatus.LOGIN_USER_STATUS_0.getValue().equals(loginUserDto.getUserStatus())) {
				// 账户状态正常 判断是否需要锁定账户
				Short failNumShort = loginUserDto.getLoginFailNum();
				Date firsetPwdErrorTime = loginUserDto.getLastLoginLockTime(); // 第一次密码错误时间
				firsetPwdErrorTime = firsetPwdErrorTime == null ? new Date() : firsetPwdErrorTime;
				int failNum = failNumShort == null ? 0 : failNumShort;
				failNum++;
				if (compareSysTime(getUnlockTime(firsetPwdErrorTime, continuedHour))) {
					// 当前系统时间大（ 第一次密码错误时间失效）
					loginUserDto.setLoginFailNum((short) 1);
					loginUserDto.setLastLoginLockTime(new Date());
					mrsLoginUserDtoMapper.updateByPrimaryKey(loginUserDto);
					UserCheckVO vo = new UserCheckVO(false, ErrorMsgEnum.USER_PWD_ERROR); // 报错：用户名或密码错误
					loginUserDto.setUserCheckVo(vo);
					return loginUserDto;
				} else {
					// 当前系统时间小（ 第一次密码错误时间有效）
					if (failNum >= maxFailNum) {
						log.info("登陆用户[id=" + loginUserDto.getId() + "]锁定");
						// 达到最大错误次数 登陆密码错误次数设置为最大错误次数，用户状态为锁定状态，上锁时间为当前时间
						loginUserDto.setLoginFailNum((short) maxFailNum);
						loginUserDto.setUserStatus(LoginUserStatus.LOGIN_USER_STATUS_1.getValue());
						loginUserDto.setLastLoginLockTime(new Date());
						mrsLoginUserDtoMapper.updateByPrimaryKey(loginUserDto);
						// 密码错误
						UserCheckVO vo = new UserCheckVO(false);
						String value = ErrorMsgEnum.LOCK_PWD.getValue();
						value = value.replace("{0}", maxFailNumStr).replace("{1}", lockHourStr);
						vo.setMsg(value);
						loginUserDto.setUserCheckVo(vo);
						return loginUserDto;
					} else if (failNum == 1) {
						// 登陆密码错误次数加1 首次错误记录登陆错误时间
						loginUserDto.setLoginFailNum((short) failNum);
						loginUserDto.setLastLoginLockTime(new Date());
						mrsLoginUserDtoMapper.updateByPrimaryKey(loginUserDto);
						UserCheckVO vo = new UserCheckVO(false, ErrorMsgEnum.USER_PWD_ERROR);
						loginUserDto.setUserCheckVo(vo);
						return loginUserDto;
					} else {
						loginUserDto.setLoginFailNum((short) failNum);
						mrsLoginUserDtoMapper.updateByPrimaryKey(loginUserDto);
						UserCheckVO vo = new UserCheckVO(false, ErrorMsgEnum.USER_PWD_ERROR);
						loginUserDto.setUserCheckVo(vo);
						return loginUserDto;
					}
				}
			} else if (LoginUserStatus.LOGIN_USER_STATUS_1.getValue().equals(loginUserDto.getUserStatus())) {
				// 账户锁定 判断是否需要解锁账户
				Date lockTime = loginUserDto.getLastLoginLockTime(); // 上锁时间
				if (compareSysTime(getUnlockTime(lockTime, lockHour))) {
					// 解锁
					log.info("登陆用户[id=" + loginUserDto.getId() + "]解锁");
					loginUserDto.setLoginFailNum((short) 1);
					loginUserDto.setUserStatus(LoginUserStatus.LOGIN_USER_STATUS_0.getValue());
					loginUserDto.setLastLoginLockTime(new Date());
					mrsLoginUserDtoMapper.updateByPrimaryKey(loginUserDto);
					UserCheckVO vo = new UserCheckVO(false, ErrorMsgEnum.USER_PWD_ERROR);
					loginUserDto.setUserCheckVo(vo);
					return loginUserDto;
				} else {
					log.info("登陆用户[id=" + loginUserDto.getId() + "]已经被锁定");
					UserCheckVO vo = new UserCheckVO(false);
					String value = ErrorMsgEnum.LOCK_PWD.getValue();
					value = value.replace("{0}", maxFailNumStr);
					value = value.replace("{1}", lockHourStr);
					vo.setMsg(value);
					loginUserDto.setUserCheckVo(vo);
					return loginUserDto;
				}
			} else if (LoginUserStatus.LOGIN_USER_STATUS_2.getValue().equals(loginUserDto.getUserStatus())) {
				log.info("登陆用户[id=" + loginUserDto.getId() + "]已注销");
				UserCheckVO vo = new UserCheckVO(false, ErrorMsgEnum.USER_CANCEL);
				loginUserDto.setUserCheckVo(vo);
				return loginUserDto;
			} else {
				log.info("登陆用户[id=" + loginUserDto.getId() + "]状态不存在");
				return null;
			}
		}
	}

    @Override
	public MrsLoginUserDto getLoginUser(MrsLoginUserDto dto) throws PortalCheckedException {
		List<MrsLoginUserDto> loginUserList = mrsLoginUserDtoMapper.findByLoginMsg(dto);
		MrsLoginUserDto loginUserDto = null;

		if (CollectionUtil.isEmpty(loginUserList)) {
			// 查询是否是门户注册的用户
			List<MrsLoginUserDto> myloginUserList = mrsLoginUserDtoMapper.findByMobileAndAlias(dto);
			if (CollectionUtil.isEmpty(myloginUserList)) {
				return null;
			}else{
				loginUserDto = myloginUserList.get(0);
			}
		}else{
			// 如果根据登陆信息查询出多个信息，则是一个一户通账户对应多个用户
			loginUserDto = loginUserList.get(0);
		}

		if (LoginUserStatus.LOGIN_USER_STATUS_2.getValue().equals(loginUserDto.getUserStatus())) {
			log.error("账户[id=" + loginUserDto.getId() + "]已注销");
			throw new PortalCheckedException(ErrorMsgEnum.USER_CANCEL.getKey());
		}
		return loginUserDto;
	}

	@Override
	public void updateLoginPwd(String id, String loginPwd) {
		MrsLoginUserDto loginUser = mrsLoginUserDtoMapper.selectByPrimaryKey(id);
		loginPwd = MD5Utils.MD5(loginPwd + SHIEConfigConstant.SALT);
		loginUser.setLoginPwd(loginPwd);
		mrsLoginUserDtoMapper.updateByPrimaryKey(loginUser);
	}

	@Override
	public void upLoginPwd(MrsLoginUserDto usrDto) {
		MrsLoginUserDto loginUser = mrsLoginUserDtoMapper.selectByPrimaryKey(usrDto.getId());
		loginUser.setLoginPwd(MD5Utils.MD5(usrDto.getLoginPwd() + SHIEConfigConstant.SALT));
		loginUser.setUpdateOperator(usrDto.getUpdateOperator());
		loginUser.setUpdateTime(new Date());
		short num = 0;
		loginUser.setLoginFailNum(num);
		loginUser.setLastLoginLockTime(null);
		loginUser.setUserStatus(LoginUserStatus.LOGIN_USER_STATUS_0.getValue());// 用户状态设置为有效
		mrsLoginUserDtoMapper.updateByPrimaryKey(loginUser);
	}

    @Override
	@Transactional(value = CbsConstants.TX_MANAGER_MRS)
	public UserCheckVO updateLoginPwd(String id, String oldPwd, String newPwd) {
		oldPwd = MD5Utils.MD5(oldPwd + SHIEConfigConstant.SALT); // e10adc3949ba59abbe56e057f20f883e
		MrsLoginUserDto loginUser = mrsLoginUserDtoMapper.findByIdAndLoginPwd(id, oldPwd);
		if (loginUser == null) {
			log.error("[id = " + id + "]登陆密码错误，修改失败");
			return new UserCheckVO(false, ErrorMsgEnum.LOGIN_PWD_ERROR);
		}
		newPwd = MD5Utils.MD5(newPwd + SHIEConfigConstant.SALT);
		if (oldPwd.equals(newPwd)) {
			log.error("[id = " + id + "]新密码不能和原密码相同");
			return new UserCheckVO(false, ErrorMsgEnum.PWD_EQUAL);
		}
		loginUser.setLoginPwd(newPwd);
		mrsLoginUserDtoMapper.updateByPrimaryKey(loginUser);
        return new UserCheckVO(true);
	}

	@Override
	@Transactional(value = CbsConstants.TX_MANAGER_MRS)
	public UserCheckVO updateLoginPwdByMobile(String id, String oldPwd, String newPwd,String mobile) {
		oldPwd = MD5Utils.MD5(oldPwd + SHIEConfigConstant.SALT); // e10adc3949ba59abbe56e057f20f883e
		MrsLoginUserDto loginUser = mrsLoginUserDtoMapper.findByIdAndLoginPwd(id, oldPwd);
		if (loginUser == null) {
			log.error("[id = " + id + "]登陆密码错误，修改失败");
			return new UserCheckVO(false, ErrorMsgEnum.LOGIN_PWD_ERROR);
		}
		newPwd = MD5Utils.MD5(newPwd + SHIEConfigConstant.SALT);
		if (oldPwd.equals(newPwd)) {
			log.error("[id = " + id + "]新密码不能和原密码相同");
			return new UserCheckVO(false, ErrorMsgEnum.PWD_EQUAL);
		}
		loginUser.setLoginPwd(newPwd);
		loginUser.setMobile(mobile);
		mrsLoginUserDtoMapper.updateByPrimaryKeySelective(loginUser);
        return new UserCheckVO(true);
	}

	@Override
	public MrsLoginUserDto findByIdAndLoginPwd(String id, String loginPwd) {
		loginPwd = MD5Utils.MD5(loginPwd + SHIEConfigConstant.SALT);
		return mrsLoginUserDtoMapper.findByIdAndLoginPwd(id, loginPwd);
	}

	@Override
	public MrsLoginUserDto getEffectLoginUserById(String id) {
		MrsLoginUserDto loginUser = mrsLoginUserDtoMapper.selectByPrimaryKey(id);
		if (loginUser == null) {
			return null;
		}
		if (LoginUserStatus.LOGIN_USER_STATUS_2.getValue().equals(loginUser.getId())) {
			// 已注销用户
			log.error("登陆用户[id=" + id + "]已注销");
			return null;
		}
		return loginUser;
	}

	@Override
	public PageData<MrsLoginUserDto> findPerson(PageData<MrsLoginUserDto> pageData, MrsLoginUserDto searchDto) {
		PageHelper.startPage(pageData.getPageNumber(), pageData.getPageSize());
		List<MrsLoginUserDto> list = mrsLoginUserDtoMapper.pageList(searchDto);
		Page<MrsLoginUserDto> page = (Page<MrsLoginUserDto>) list;
		pageData.setTotal(page.getTotal());
		pageData.setRows(list);
		return pageData;
	}

	@Override
	@Transactional(value = CbsConstants.TX_MANAGER_MRS)
	public void updateLoginPwdByEmail(String loginId, String loginPwd) throws PortalCheckedException {
		/*
		 * BisCheckEmailDto dto =
		 * bisCheckEmailDtoMapper.getCheckEmail(checkMsg); //
		 * BisCheckEmailDto中的custId属性保存的是TB_MRS_LOGIN_USER表的ID字段 if(dto == null
		 * || !EBisEmailChectStatus.PENDING_CHECK.equals(dto.getStatus())){
		 * log.error("邮件无效，修改失败"); throw new
		 * PortalCheckedException(ErrorMsgEnum.INVALID_EMAIL.getKey()); }
		 * dto.setStatus(EBisEmailChectStatus.CHECK_PASS);
		 * bisCheckEmailDtoMapper.updateByPrimaryKey(dto); String loginId =
		 * dto.getCustId();
		 */
		MrsLoginUserDto loginUser = mrsLoginUserDtoMapper.selectByPrimaryKey(loginId);
		loginPwd = MD5Utils.MD5(loginPwd + SHIEConfigConstant.SALT);
		loginUser.setLoginPwd(loginPwd);
		mrsLoginUserDtoMapper.updateByPrimaryKey(loginUser);
	}

	@Override
	public MrsLoginUserDto findByAlias(String alias) {
		return mrsLoginUserDtoMapper.findByAlias(alias);
	}

	@Override
	public MrsLoginUserDto updateAlias(String loginId, String alias) throws PortalCheckedException {
		int row = mrsLoginUserDtoMapper.updateAlias(loginId, alias);
		if (row != 1) {
			log.error("昵称修改失败[id = " + loginId + "]");
			throw new PortalCheckedException(ErrorMsgEnum.UPDATE_FAIL.getKey());
		}
		MrsLoginUserDto dto = mrsLoginUserDtoMapper.selectByPrimaryKey(loginId);
		if (dto == null) {
			log.error("查找失败[id = " + loginId + "]");
			throw new PortalCheckedException(ErrorMsgEnum.UPDATE_FAIL.getKey());
		}
		return dto;
	}

	@Override
	public MrsLoginUserDto getByCustIdLoginPwd(String custId, String loginPwd) {
		loginPwd = MD5Utils.MD5(loginPwd + SHIEConfigConstant.SALT);// e10adc3949ba59abbe56e057f20f883e
		return mrsLoginUserDtoMapper.getByCustIdLoginPwd(custId, loginPwd);
	}

	@Override
	public MrsLoginUserDto selectByCustId(String custId) {
		return mrsLoginUserDtoMapper.selectByCustId(custId);
	}

	@Override
	public MrsLoginUserDto isExistEmail(String email) {
		List<MrsLoginUserDto>  usrtList = mrsLoginUserDtoMapper.findByEmail(email);
		if (usrtList != null && usrtList.size()>0) {
			return usrtList.get(0);
		}else{
			return null;
		}

	}

	@Override
	public MrsLoginUserDto isExistMobile(String mobile) {
		return mrsLoginUserDtoMapper.findByMobile(mobile);
	}

	@Override
	@Transactional(value = CbsConstants.TX_MANAGER_MRS)
	public void saveLoginUser(MrsLoginUserDto dto) throws PortalCheckedException {
		dto.setCreateTime(new Date());
		dto.setId(UUID.randomUUID().toString());
		dto.setUserStatus(LoginUserStatus.LOGIN_USER_STATUS_0.getValue());
		//密码再次加密处理
		dto.setLoginPwd(MD5Utils.MD5(dto.getLoginPwd() + SHIEConfigConstant.SALT));
		//注册成功后就登录，则设置登录时间
		dto.setLoginTime(new Date());
		// 保存登陆信息
		mrsLoginUserDtoMapper.insert(dto);
	}


	@Override
	public int insertSelective(MrsLoginUserDto dto) {
		return mrsLoginUserDtoMapper.insertSelective(dto);
	}

	@Override
	public MrsLoginUserDto selectByPrimaryKey(String id) {
		return mrsLoginUserDtoMapper.selectByPrimaryKey(id);
    }

    @Override
    public MrsLoginUserDto findUserDtoByLoginId(String id) {
        return mrsLoginUserDtoMapper.findUserDtoByLoginId(id);
    }


	@Override
	public List<MrsLoginUserDto> findByMobileAndAlias(MrsLoginUserDto dto) {
		List<MrsLoginUserDto> loginUserList = mrsLoginUserDtoMapper.findByMobileAndAlias(dto);
		if (CollectionUtil.isEmpty(loginUserList)) {
			List<MrsLoginUserDto> myloginUserList = mrsLoginUserDtoMapper.findByLoginUser(dto);
			if (CollectionUtil.isEmpty(myloginUserList)) {
				return null;
			}else{
				return myloginUserList;
			}
		}else{
			return loginUserList;
		}
	}
	
	@Override
	public List<MrsLoginUserDto> findByCustId(String custId) {
		if(StringUtils.isBlank(custId)){
			return null;
		}
		return mrsLoginUserDtoMapper.findUserDtoByCustId(custId);
	}

	@Override
	public MrsLoginUserDto findMainByCustId(String custId) {
		return mrsLoginUserDtoMapper.findMainByCustId(custId);
	}

	@Override
	public UserCheckVO getUserAuditInfo(String loginId) {
		UserCheckVO ucVo = new UserCheckVO(true);
		List<MrsAduitPersonHisDto> aduitList = mrsAduitPersonHisDtoMapper.findListHisByLoginId(loginId);
		if (aduitList != null && aduitList.size() > 0) {
			MrsAduitPersonHisDto auditDto = aduitList.get(0);
			if (EAduitTypeEnum.ADUIT_REJUST.equals(auditDto.getAduitStatus())) {
				ucVo.setMsg(auditDto.getAduitStatus().getValue());
				ucVo.setRemark(auditDto.getAduitRemark());
				return ucVo;
			}
		} else {
			ucVo.setCheckValue(false);
		}
		return ucVo;
	}

	@Override
	public UserCheckVO getUserAuditInfoStatus(String loginId) {
		UserCheckVO ucVo = new UserCheckVO(true);
		List<MrsAduitInfoDto> aduitList = mrsAduitInfoDtoMapper.findListByLoginId(loginId);
		if (aduitList != null && aduitList.size() > 0) {
			ucVo.setMsg(aduitList.get(0).getStatus().getValue());
			return ucVo;
		} else {
			ucVo.setCheckValue(false);
		}
		return ucVo;

	}
	@Override
	public PageData<MrsLoginUserDto> findId(PageData<MrsLoginUserDto> pageData,MrsLoginUserDto searchDto) {
		PageHelper.startPage(pageData.getPageNumber(), pageData.getPageSize());
		List<MrsLoginUserDto> list = mrsLoginUserDtoMapper.list(searchDto.getId());
		Page<MrsLoginUserDto> page = (Page<MrsLoginUserDto>) list;
		pageData.setTotal(page.getTotal());
		pageData.setRows(list);
		return pageData;
	}

	@Override
	public PageData<MrsLoginUserDto> finduser(PageData<MrsLoginUserDto> pageData, MrsLoginUserDto searchDto) {
		PageHelper.startPage(pageData.getPageNumber(), pageData.getPageSize());
		List<MrsLoginUserDto> list = mrsLoginUserDtoMapper.pageUser(searchDto);
		Page<MrsLoginUserDto> page = (Page<MrsLoginUserDto>) list;
		pageData.setTotal(page.getTotal());
		pageData.setRows(list);
		return pageData;
	}


	@Override
	public MrsUserAccountDto isExistIsMain(String isMain) {
		try {
			return mrsUserAccountDtoMapper.findByIsMain(isMain);
		} catch (Exception e) {
			log.error("查询失败:",e);
			return null;
		}
	}


	@Override
	public List<MrsLoginUserDto> getCustIdsByLoginUserId(String id) {
		return mrsLoginUserDtoMapper.list(id);
	}


	@Override
	public MrsLoginUserDto selectLoginUserByCustId(String custId) {
		return mrsLoginUserDtoMapper.selectLoginUserByCustId(custId);
	}


	@Override
	public MrsLoginUserDto findById(String id) {
		return mrsLoginUserDtoMapper.selectByPrimaryKey(id);
	}


	@Override
	public MrsLoginUserDto findByMobile(String mobile) {
		return mrsLoginUserDtoMapper.findByMobile(mobile);
	}
	@Override
	public void updateDto(MrsLoginUserDto dto){
		mrsLoginUserDtoMapper.updateByPrimaryKey(dto);
	}
	
	@Override
	public boolean checkUserPayPwd(String custId) {
		// 根据一户通号查询登录密码
		MrsUserPayPasswordDto mrsUserPayPasswordDto = mrsUserPayPasswordDtoMapper.selectByCustId(custId);
        return mrsUserPayPasswordDto != null;
    }


	@Override
	public MrsLoginUserDto findLoginUserDtoByWeChatId(String weChatId) {
		return mrsLoginUserDtoMapper.findUserDtoByWeChatId(weChatId);
	}


	@Override
	public void updateLoginUserDto(MrsLoginUserDto mrsLoginUserDto) {
		mrsLoginUserDtoMapper.updateByPrimaryKeySelective(mrsLoginUserDto);
	}
}
