package com.ylink.inetpay.cbs.mrs.App;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.cbs.mrs.service.MrsConfAcctService;
import com.ylink.inetpay.common.project.account.dto.ActSubjectDto;
import com.ylink.inetpay.common.project.cbs.app.MrsConfAcctAppService;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsConfAcctDto;
import com.ylink.inetpay.common.project.cbs.exception.CbsUncheckedException;
/**
 * 
 * @author pst10
 * 类名称：MrsConfAcctAppServiceImpl
 * 类描述：子账户信息操作服务
 * 创建时间：2017年3月31日 下午7:51:59
 */
@Service("mrsConfAcctAppService")
public class MrsConfAcctAppServiceImpl implements MrsConfAcctAppService{

	@Autowired
	MrsConfAcctService mrsConfAcctService;
	@Override
	public PageData<MrsConfAcctDto> findPage(PageData<MrsConfAcctDto> page, MrsConfAcctDto seachDto) {
		
		return mrsConfAcctService.findPage(page, seachDto);
	}

	@Override
	public MrsConfAcctDto selectById(String id) {
		return mrsConfAcctService.selectById(id);
	}

	@Override
	public void addOrUpdateMrsConfAcct(MrsConfAcctDto mrsConfAcctDto) throws CbsUncheckedException{
		mrsConfAcctService.addOrUpdateMrsConfAcct(mrsConfAcctDto);
	}

	@Override
	public boolean checkMrsConfAcct(String name, String id) {
		return mrsConfAcctService.checkMrsConfAcct(name, id);
	}

	@Override
	public List<MrsConfAcctDto> findAll() {
		return mrsConfAcctService.findAll();
	}

	@Override
	public List<ActSubjectDto> findActByConfAcctId(String id) {
		return mrsConfAcctService.findActByConfAcctId(id);
	}

}
