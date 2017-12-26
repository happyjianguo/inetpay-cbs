package com.ylink.inetpay.cbs.ucs.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.cbs.ucs.sec.service.UcsSecOperationLogService;
import com.ylink.inetpay.cbs.ucs.sec.service.UcsSecUserService;
import com.ylink.inetpay.common.core.constant.EUcsSecOperationLogType;
import com.ylink.inetpay.common.project.cbs.app.UcsSecUserAppService;
import com.ylink.inetpay.common.project.cbs.dto.ucs.sec.UcsSecOperationLogDto;
import com.ylink.inetpay.common.project.cbs.dto.ucs.sec.UcsSecUserDto;
import com.ylink.inetpay.common.project.cbs.exception.CbsCheckedException;

@Service("ucsSecUserAppService")
public class UcsSecUserAppServiceImpl implements UcsSecUserAppService {

	@Autowired
	private UcsSecUserService ucsSecUserService;
	
	@Autowired
	private UcsSecOperationLogService ucsSecOperationLogService;
	
	@Autowired
	@Qualifier("logTaskExecutor")
	private TaskExecutor taskExecutor;

	@Override
	public UcsSecUserDto getWithRolesByLoginName(String loginName)
			throws CbsCheckedException {
		return ucsSecUserService.getWithRolesByLoginName(loginName);
	}

	@Override
	public UcsSecUserDto getByLoginName(String loginName)
			throws CbsCheckedException {
		return ucsSecUserService.getByLoginName(loginName);
	}

	@Override
	public void lockUser(String userId) throws CbsCheckedException {
		ucsSecUserService.updateLockTime(userId);
	}

	@Override
	public void logLoginSuccess(UcsSecUserDto user, String ip)
			throws CbsCheckedException {
		
		final UcsSecOperationLogDto ucsSecOperationLogDto = new UcsSecOperationLogDto();
		ucsSecOperationLogDto.setLoginName(user.getLoginName());
		ucsSecOperationLogDto.setRealName(user.getRealName());
		ucsSecOperationLogDto.setdName(user.getDepartmentName());
		ucsSecOperationLogDto.setIp(ip);
		ucsSecOperationLogDto.setOperType(EUcsSecOperationLogType.LOGIN);
		ucsSecOperationLogDto.setDescription("登录成功");
		//用户对象，将用户之前登录失败次数设置为0
		final UcsSecUserDto entity = new UcsSecUserDto();
		entity.setId(user.getId());
		entity.setLoginFailNum((short)0);
		
		// 异步记日志
		taskExecutor.execute(new Runnable() {
			@Override
			public void run() {
				ucsSecOperationLogService.insert(ucsSecOperationLogDto);
				ucsSecUserService.updateSelective(entity);
			}
		});
	}

	@Override
	public void logLoginFailure(UcsSecUserDto user,String ip, String errMsg)
			throws CbsCheckedException {
		final UcsSecOperationLogDto ucsSecOperationLogDto = new UcsSecOperationLogDto();
		ucsSecOperationLogDto.setLoginName(user.getLoginName());
		ucsSecOperationLogDto.setRealName(user.getRealName());
		ucsSecOperationLogDto.setdName(user.getDepartmentName());
		ucsSecOperationLogDto.setIp(ip);
		ucsSecOperationLogDto.setOperType(EUcsSecOperationLogType.LOGIN);
		ucsSecOperationLogDto.setDescription("登录失败");
		
		// 异步记日志
		taskExecutor.execute(new Runnable() {
			@Override
			public void run() {
				ucsSecOperationLogService.insert(ucsSecOperationLogDto);
			}
		});
	}

	@Override
	public UcsSecUserDto get(String id) throws CbsCheckedException {
		return ucsSecUserService.get(id);
	}

	@Override
	public UcsSecUserDto getWithRoles(String id) throws CbsCheckedException {
		return ucsSecUserService.getWithRoles(id);
	}

	@Override
	public PageData<UcsSecUserDto> listWidthRolesPage(
			PageData<UcsSecUserDto> pageData, UcsSecUserDto params)
			throws CbsCheckedException {
		return ucsSecUserService.listWidthRolesPage(pageData, params);
	}

	@Override
	public void saveWithRoles(UcsSecUserDto entity) throws CbsCheckedException {
		ucsSecUserService.saveWithRoles(entity);
	}

	@Override
	public void updateWithRoles(UcsSecUserDto entity)
			throws CbsCheckedException {
		ucsSecUserService.updateWithRoles(entity);
	}

	@Override
	public boolean checkLoginName(String id, String loginName)
			throws CbsCheckedException {
		return ucsSecUserService.checkLoginName(id, loginName);
	}

	@Override
	public boolean checkEmail(String id, String email)
			throws CbsCheckedException {
		return ucsSecUserService.checkEmail(id, email);
	}

	@Override
	public void updateLockTime(String id) throws CbsCheckedException {
		ucsSecUserService.updateLockTime(id);
	}

	@Override
	public void delete(String id) throws CbsCheckedException {
		ucsSecUserService.delete(id);
	}

	@Override
	public void enableLoginAcccount(String id) throws CbsCheckedException {
		ucsSecUserService.enableLoginAcccount(id);
	}

	@Override
	public void disableLoginAcccount(String id) throws CbsCheckedException {
		ucsSecUserService.disableLoginAcccount(id);
	}

	@Override
	public void changepwd(String id, String password)
			throws CbsCheckedException {
		ucsSecUserService.changepwd(id, password);
	}

	@Override
	public void resetpwd(String id, String password) throws CbsCheckedException {
		ucsSecUserService.resetpwd(id, password);
	}

	@Override
	public void updateSelective(UcsSecUserDto entity)
			throws CbsCheckedException {
		ucsSecUserService.updateSelective(entity);
	}
}
