package com.ylink.inetpay.cbs.pay.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.code.lightssh.common.util.StringUtil;
import com.ylink.inetpay.cbs.chl.service.ChlBankService;
import com.ylink.inetpay.cbs.pay.dao.PayRechargeDtoMapper;
import com.ylink.inetpay.common.core.dto.ReporHeadDto;
import com.ylink.inetpay.common.project.channel.dto.TbChlBank;
import com.ylink.inetpay.common.project.pay.dto.PayRechargeDto;

@Service("payRechargeService")
public class PayRechargeServiceImpl implements PayRechargeService {
	@Autowired
	PayRechargeDtoMapper payRechargeDtoMapper;
	@Autowired
	ChlBankService chlBankService;

	@Override
	public PageData<PayRechargeDto> queryAllData(
			PageData<PayRechargeDto> pageDate, PayRechargeDto payRechargeDto) {

		PageHelper.startPage(pageDate.getPageNumber(), pageDate.getPageSize());
		List<PayRechargeDto> list = payRechargeDtoMapper.queryAllData(payRechargeDto);
		ArrayList<String> bankTypes = new ArrayList<String>();
		for (PayRechargeDto dto : list) {
			bankTypes.add(dto.getBankType());
		}
		List<TbChlBank> chlBankList = null;
		if(!bankTypes.isEmpty()){
			chlBankList=chlBankService.getBankByBankTypes(bankTypes);
		}
		if(chlBankList!=null  && !chlBankList.isEmpty()){
			HashMap<String, TbChlBank> bankTypeMap = new HashMap<String,TbChlBank>();
			for (TbChlBank bankDto : chlBankList) {
				bankTypeMap.put(bankDto.getBankType(), bankDto);
			}
			for (PayRechargeDto dto : list) {
				TbChlBank tbChlBank = bankTypeMap.get(dto.getBankType());
				if(tbChlBank!=null){
					dto.setBankNameZ(tbChlBank.getBankName());
				}else{
					dto.setBankNameZ(dto.getBankName());
				}
			}
		}
		/*List<TbChlBank> banks = chlBankService.getBanks();
		for (int i = 0; i < list.size(); i++) {
			String bankType = list.get(i).getBankType();
			for (int j = 0; j < banks.size(); j++) {
				PayRechargeDto payRecharge = list.get(i);
				if (bankType.equals(banks.get(j).getBankType())) {
					payRecharge.setBankNameZ(banks.get(j).getBankName());
					list.set(i,payRecharge);
				}
				
			}
			
		}*/
		Page<PayRechargeDto> page = (Page<PayRechargeDto>) list;
		pageDate.setTotal(page.getTotal());
		pageDate.setRows(list);
		return pageDate;
	}

	@Override
	public PayRechargeDto selectByBusiId(String busiId) {
		PayRechargeDto payRechargeDto = payRechargeDtoMapper.selectByBusiId(busiId);
		try {
			String bankType = payRechargeDto.getBankType();
			TbChlBank tbChlBank = chlBankService.getBankByBankType(bankType);
			if (tbChlBank == null) {
				payRechargeDto.setBankNameZ(payRechargeDto.getBankName());
			}else {
				payRechargeDto.setBankNameZ(tbChlBank.getBankName());
			}
		} catch (Exception e) {
			return payRechargeDto;
		}
		
		return payRechargeDto;
	}
	@Override
	public ReporHeadDto reportSumData(PayRechargeDto payRechargeDto) {
		return payRechargeDtoMapper.reportSumData(payRechargeDto);
	}
}
