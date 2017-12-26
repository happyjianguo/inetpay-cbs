package com.ylink.inetpay.cbs;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.ylink.inetpay.cbs.util.HttpSendUtil;



@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath*:spring/spring-core.xml"})
@TransactionConfiguration(transactionManager="ucTxManager")
public class MrsReturnPhoneMessageCheckTest {	

	@Autowired
	protected ApplicationContext applicationContext;
	
	public static void main(String[] args) {
		String sendPostJsonStr = HttpSendUtil.sendPostJsonStr("{\"test\":\"测试调用返回接口\"}", "http://172.16.123.155:8089/cbs/return/phoneMessageCheck");
		System.out.println(sendPostJsonStr);
	}
}
