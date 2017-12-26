package com.ylink.inetpay.cbs.act.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ylink.inetpay.common.project.account.app.ActAccountDateAppService;
import com.ylink.inetpay.common.project.account.dto.ActBillDto;
import com.ylink.inetpay.common.project.account.dto.ActaccountDateDto;

/**
 * 
 * @author yanggang
 * @2016-6-13 2016-6-13
 */
@Service("actAccountDateAppService")
public class ActAccountDateAppServiceImpl implements ActAccountDateAppService{
	@Autowired
	private ActaccountDateService actaccountDateService;
	@Override
	public String getAccountDate() {
		return actaccountDateService.getAccountDate();
	}
	@Override
	public ActaccountDateDto selectByPrimaryKey(String id) {
		// TODO Auto-generated method stub
		return actaccountDateService.selectByPrimaryKey(id);
	}
 
	
}
