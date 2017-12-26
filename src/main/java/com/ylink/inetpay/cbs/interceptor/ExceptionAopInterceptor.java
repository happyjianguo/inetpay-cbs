package com.ylink.inetpay.cbs.interceptor;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ylink.inetpay.common.project.cbs.exception.CbsCheckedException;
import com.ylink.inetpay.common.project.cbs.exception.CbsUncheckedException;
import com.ylink.inetpay.common.project.cbs.exception.ECbsErrorCode;

public class ExceptionAopInterceptor {

	private static Logger _log = LoggerFactory
			.getLogger(ExceptionAopInterceptor.class);

	public Object around(JoinPoint joinPoint) throws Throwable {
		try {
			return ((ProceedingJoinPoint) joinPoint).proceed();
		} catch (Throwable e) {
			_log.error(e.getMessage(), e);
			CbsCheckedException checkedEx = null;
			if (e instanceof CbsUncheckedException) {
				CbsUncheckedException ex = (CbsUncheckedException) e;
				checkedEx = new CbsCheckedException(ex.getCode(),
						ex.getMessage(), ex);
			} else if (e instanceof CbsCheckedException) {
				checkedEx = (CbsCheckedException) e;
			} else {
				checkedEx = new CbsCheckedException(
						ECbsErrorCode.SYS_ERROR.getValue(),
						ECbsErrorCode.SYS_ERROR.getDisplayName()
								+ e.getMessage(), e);
			}
			throw checkedEx;
		}
	}

}
