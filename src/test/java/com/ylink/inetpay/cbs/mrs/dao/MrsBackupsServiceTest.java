package com.ylink.inetpay.cbs.mrs.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ylink.inetpay.cbs.MRBaseTest;
import com.ylink.inetpay.cbs.bis.service.CbsBackupsService;

public class MrsBackupsServiceTest extends MRBaseTest{
	@Autowired
	private CbsBackupsService cbsBackupsService;
	
	@Test
	public void test(){
			SimpleDateFormat f=new SimpleDateFormat("yyyyMMdd");
			try {
				cbsBackupsService.backupsSms(f.parse("20160909"));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
}
