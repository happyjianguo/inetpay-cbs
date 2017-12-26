package com.ylink.inetpay.cbs.pay.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.ylink.inetpay.cbs.chl.service.ChlBankService;
import com.ylink.inetpay.cbs.pay.dao.PayWithdrawDtoMapper;
import com.ylink.inetpay.common.core.dto.ReporHeadDto;
import com.ylink.inetpay.common.project.channel.dto.TbChlBank;
import com.ylink.inetpay.common.project.pay.dto.PayWithdrawDto;

@Service("payWithdrawService")
public class PayWithdrawServiceImpl implements PayWithdrawService {
	@Autowired
	PayWithdrawDtoMapper payWithdrawDtoMapper;
	@Autowired
	ChlBankService chlBankService;

	@Override
	public PageData<PayWithdrawDto> queryAllData(
			PageData<PayWithdrawDto> pageDate, PayWithdrawDto payWithdrawDto) {

		PageHelper.startPage(pageDate.getPageNumber(), pageDate.getPageSize());
		List<PayWithdrawDto> list = payWithdrawDtoMapper.queryAllData(payWithdrawDto);
		ArrayList<String> bankTypes = new ArrayList<String>();
		for (PayWithdrawDto dto : list) {
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
			for (PayWithdrawDto dto : list) {
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
				if (bankType.equals(banks.get(j).getBankType())) {
					PayWithdrawDto payWithdraw = list.get(i);
					payWithdraw.setBankNameZ(banks.get(j).getBankName());
					list.set(i,payWithdraw);
				}
				
			}
			
		}*/
		Page<PayWithdrawDto> page = (Page<PayWithdrawDto>) list;
		pageDate.setTotal(page.getTotal());
		pageDate.setRows(list);
		return pageDate;
	}

	@Override
	public PayWithdrawDto selectByBusiId(String busiId) {
		PayWithdrawDto payWithdrawDto =  payWithdrawDtoMapper.selectByBusiId(busiId);
		try {
			String bankType = payWithdrawDto.getBankType();
			TbChlBank tbChlBank = chlBankService.getBankByBankType(bankType);
			if (tbChlBank == null) {
				payWithdrawDto.setBankNameZ(payWithdrawDto.getBankName());
			}else {
				payWithdrawDto.setBankNameZ(tbChlBank.getBankName());
			}
		} catch (Exception e) {
			return payWithdrawDto;
		}
		return payWithdrawDto;
	}
	@Override
	public ReporHeadDto reportSumData(PayWithdrawDto payWithdrawDto) {
		return payWithdrawDtoMapper.reportSumData(payWithdrawDto);
	}
}
