package com.ylink.inetpay.cbs.chl.app;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.cbs.chl.service.CbsChlBankCodeService;
import com.ylink.inetpay.common.project.cbs.app.CbsChlBankCodeAppService;
import com.ylink.inetpay.common.project.cbs.exception.CbsCheckedException;
import com.ylink.inetpay.common.project.channel.dto.TbChlBankCode;
@Service("cbsChlBankCodeAppService")
public class CbsChlBankCodeAppServiceImpl implements CbsChlBankCodeAppService{
	@Autowired
	private CbsChlBankCodeService cbsChlBankCodeService;
	@Override
	public void saveBacnkCode(TbChlBankCode bankCode) throws CbsCheckedException {
		cbsChlBankCodeService.saveBacnkCode(bankCode);
	}

	@Override
	public void updateBankCodeById(TbChlBankCode bankCode) throws CbsCheckedException {
		cbsChlBankCodeService.updateBankCodeById(bankCode);
	}

	@Override
	public void deleteBankCodeId(String id) throws CbsCheckedException {
		cbsChlBankCodeService.deleteBankCodeId(id);
	}

	@Override
	public PageData<TbChlBankCode> queryAllData(PageData<TbChlBankCode> pageData,TbChlBankCode bankCode) throws CbsCheckedException {
		return cbsChlBankCodeService.queryAllData(pageData, bankCode);
	}

	@Override
	public TbChlBankCode getBankCodeById(String id) {
		return cbsChlBankCodeService.getBankCodeById(id);
	}

	@Override
	public List<TbChlBankCode> getDepositBankCode(String cityCode, String bankType) {
		return cbsChlBankCodeService.getDepositBankCode(cityCode,bankType);
	}

	@Override
	public List<TbChlBankCode> findBankNameByBankCode(String bankCode) {
		return cbsChlBankCodeService.findBankNameByBankCode(bankCode);
	}

	@Override
	public List<TbChlBankCode> findBankByBankName(String bankName) {
		return cbsChlBankCodeService.findBankByBankName(bankName);
	}

	@Override
	public Map<String, Map<String, TbChlBankCode>> findChlBankCodeMapByBankNames(List<String> queryBankNames) {
		return cbsChlBankCodeService.findChlBankCodeMapByBankNames(queryBankNames);
	}

	@Override
	public Map<String, TbChlBankCode> findChlBankMapGroupByBankType() {
		return cbsChlBankCodeService.findChlBankMapGroupByBankType();
	}
	
	
	

}
