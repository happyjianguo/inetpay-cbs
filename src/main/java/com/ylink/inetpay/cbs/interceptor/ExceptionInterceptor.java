package com.ylink.inetpay.cbs.interceptor;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.ylink.inetpay.common.core.util.ExceptionProcUtil;
import com.ylink.inetpay.common.project.cbs.exception.CbsCheckedException;
import com.ylink.inetpay.common.project.cbs.exception.CbsUncheckedException;
import com.ylink.inetpay.common.project.cbs.exception.ECbsErrorCode;

@Service("exceptionInterceptor")
public class ExceptionInterceptor implements MethodInterceptor {

	public Object invoke(MethodInvocation methodInvocation) throws Throwable {
		try {
			Object obj = methodInvocation.proceed();
			return obj;
		} catch (Exception e) {
			Logger logger = LoggerFactory.getLogger(methodInvocation.getMethod().getDeclaringClass());
			logger.error("系统报错，报错原因：{}", ExceptionProcUtil.getExceptionDesc(e));

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
