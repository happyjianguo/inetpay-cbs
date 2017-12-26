package com.ylink.inetpay.cbs.mrs.App;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.cbs.mrs.service.MrsConfSubAcctService;
import com.ylink.inetpay.common.core.constant.MrsConfSubRelationType;
import com.ylink.inetpay.common.core.constant.MrsCustomerType;
import com.ylink.inetpay.common.project.cbs.app.MrsConfSubAcctAppService;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsConfSubAcctDto;
import com.ylink.inetpay.common.project.cbs.exception.CbsUncheckedException;
/**
 * 
 * @author pst10
 * 类名称：MrsConfSubAcctAppServiceImpl
 * 类描述：子账户配置服务类
 * 创建时间：2017年2月14日 下午5:40:18
 */
@Service("mrsConfSubAcctAppService")
public class MrsConfSubAcctAppServiceImpl implements MrsConfSubAcctAppService {

	private static Logger log = LoggerFactory.getLogger(MrsConfSubAcctAppServiceImpl.class);
	@Autowired
	private MrsConfSubAcctService mrsConfSubAcctService;
	@Override
	public List<MrsConfSubAcctDto> findByUserTypeAndRationType(MrsCustomerType custType,
			MrsConfSubRelationType relationType,String platformCode) {
		return mrsConfSubAcctService.findByUserTypeAndRationType(custType, relationType,platformCode);
	}
	@Override
	public List<MrsConfSubAcctDto> findByAccountName(String accountName) {
		return mrsConfSubAcctService.findByAccountName(accountName);
	}
	@Override
	public PageData<MrsConfSubAcctDto> findPage(PageData<MrsConfSubAcctDto> page, MrsConfSubAcctDto seachDto) {
		return mrsConfSubAcctService.findPage(page,seachDto);
	}
	@Override
	public MrsConfSubAcctDto selectById(String id) {
		return mrsConfSubAcctService.selectById(id);
	}
	@Override
	public void addOrUpdateMrsConfSubAcct(MrsConfSubAcctDto confSubAcct) throws CbsUncheckedException {
		mrsConfSubAcctService.addOrUpdateMrsConfSubAcct(confSubAcct);
	}
	@Override
	public boolean checkMrsConfSubAcct(String subAccountCode, String platformCode, String accountType, String id) {
		return mrsConfSubAcctService.checkMrsConfSubAcct(subAccountCode,platformCode,accountType,id);
	}
	@Override
	public List<MrsConfSubAcctDto> findBy4Elment(MrsCustomerType userType, MrsConfSubRelationType relationType,
			String platformCode, String accountType) {
		return mrsConfSubAcctService.findBy4Elment(userType, relationType, platformCode, accountType);
	}
	@Override
	public List<MrsConfSubAcctDto> findBy4Elments(MrsCustomerType userType, MrsConfSubRelationType relationType,
			String platformCode, String accountType) {
		return mrsConfSubAcctService.findBy4Elments(userType, relationType, platformCode, accountType);
	}
	
}
