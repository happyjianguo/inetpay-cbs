package com.ylink.inetpay.cbs.pay.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.ylink.inetpay.cbs.chl.dao.TbChlBankMapper;
import com.ylink.inetpay.cbs.pay.dao.PayAccChangeNotifyDtoMapper;
import com.ylink.inetpay.common.core.constant.EResultCode;
import com.ylink.inetpay.common.core.dto.ResultCodeDto;
import com.ylink.inetpay.common.project.cbs.exception.CbsUncheckedException;
import com.ylink.inetpay.common.project.channel.dto.TbChlBank;
import com.ylink.inetpay.common.project.pay.app.PayAccChangeNotifyAppService;
import com.ylink.inetpay.common.project.pay.dto.PayAccChangeNotifyDto;
@Service("payAccChangeNotifyService")
public class PayAccChangeNotifyServiceImpl implements PayAccChangeNotifyService {
	@Autowired
	private PayAccChangeNotifyDtoMapper payAccChangeNotifyDtoMapper;	
	@Autowired
	private TbChlBankMapper tbChlBankMapper;	
	@Autowired
	private PayAccChangeNotifyAppService payAccChangeNotifyAppService;
	private static Logger _log = LoggerFactory.getLogger(PayAccChangeNotifyServiceImpl.class);
	@Override
	public PageData<PayAccChangeNotifyDto> findAll(PageData<PayAccChangeNotifyDto> pageData,
			PayAccChangeNotifyDto queryparam) {		
		
		//动账通知订单
		PageHelper.startPage(pageData.getPageNumber(),pageData.getPageSize());
		List<PayAccChangeNotifyDto> list=payAccChangeNotifyDtoMapper.queryList(queryparam);
		if(list!=null && list.size()>0){
			List<String> bankTypes = new ArrayList<>();
			for(PayAccChangeNotifyDto pcndto : list){
				bankTypes.add(pcndto.getRecBankType());
				bankTypes.add(pcndto.getPayBankType());
			}
			//查询多个银行信息
			if(bankTypes!=null&&bankTypes.size()>0){
				List<TbChlBank> bankList=tbChlBankMapper.findByBankTypes(bankTypes);
				if(bankList !=null && bankList.size()>0){
					for (PayAccChangeNotifyDto notifydto : list) {
						 for(TbChlBank bankdto : bankList){
							 if(StringUtils.isNotBlank(notifydto.getRecBankType())
									 && notifydto.getRecBankType().equals(bankdto.getBankType())){
								 notifydto.setRecBankTypeName(bankdto.getBankName());
								 break;
							 }
						 }
						 for(TbChlBank bankdto : bankList){
							 if(StringUtils.isNotBlank(notifydto.getPayBankType())
									 && notifydto.getPayBankType().equals(bankdto.getBankType())){
								 notifydto.setPayBankTypeName(bankdto.getBankName());
								 break;
							 }
						 }
					}
				}
			}
		}
				
		Page<PayAccChangeNotifyDto> page=(Page<PayAccChangeNotifyDto>) list;
		pageData.setTotal(page.getTotal());
		pageData.setRows(list);		
		return pageData;
	}
	@Override
	public PayAccChangeNotifyDto details(String id) {
		PayAccChangeNotifyDto payAccChangeNotifyDto=payAccChangeNotifyDtoMapper.selectByPrimaryKey(id);
		if(payAccChangeNotifyDto!=null){

			List<String> bankTypes = new ArrayList<>();
			bankTypes.add(payAccChangeNotifyDto.getRecBankType());
			bankTypes.add(payAccChangeNotifyDto.getPayBankType());
			//查询多个银行信息
			if(bankTypes!=null&&bankTypes.size()>0){
				List<TbChlBank> bankList=tbChlBankMapper.findByBankTypes(bankTypes);
				if(bankList !=null && bankList.size()>0){
						 for(TbChlBank bankdto : bankList){
							 if(StringUtils.isNotBlank(payAccChangeNotifyDto.getRecBankType())
									 && payAccChangeNotifyDto.getRecBankType().equals(bankdto.getBankType())){
								 payAccChangeNotifyDto.setRecBankTypeName(bankdto.getBankName());
								 break;
							 }
						 }
						 for(TbChlBank bankdto : bankList){
							 if(StringUtils.isNotBlank(payAccChangeNotifyDto.getPayBankType())
									 && payAccChangeNotifyDto.getPayBankType().equals(bankdto.getBankType())){
								 payAccChangeNotifyDto.setPayBankTypeName(bankdto.getBankName());
								 break;
							 }
						 }
				}
			}
		
		}
		
		return payAccChangeNotifyDto;
	}
	@Override
	public void movingAccountMatching(String streamNo, String accChangeBusiId) {
		//未知状态，处理状态为成功的订单才可以动账匹配
		long num=payAccChangeNotifyDtoMapper.isMovingAccountMatching(accChangeBusiId);
		String errorMsg="";
		if(num>0){
			try {
				//调用退保接口
				ResultCodeDto<PayAccChangeNotifyDto> resultCodeDto = payAccChangeNotifyAppService.manualMatchAccChange(streamNo, accChangeBusiId);
				if(resultCodeDto!=null && resultCodeDto.getResultCode()!=null){
					if(EResultCode.SUCCESS!=resultCodeDto.getResultCode()){
						_log.error("调用动账匹配接口失败：{}","返回结果为空",resultCodeDto.getMsgDetail());
						errorMsg="动帐匹配失败："+resultCodeDto.getMsgDetail();
					}
				}else{
					_log.error("调用动账匹配接口失败：{}","返回结果为空");
					errorMsg="动帐匹配失败：调用支付接口返回不明确";
				}
			} catch (Exception e) {
				_log.error("调用动账匹配接口失败：{}",e);
				throw new CbsUncheckedException("","动帐匹配失败：调用支付接口失败");
			}
			if(!StringUtils.isBlank(errorMsg)){
				throw new CbsUncheckedException("",errorMsg);
			}
		}else{
			throw new CbsUncheckedException("","不能动账匹配");
		}
		
	}
	@Override
	public PayAccChangeNotifyDto findPayAccChangeNotifyDtoById(String id) {
		 
		return payAccChangeNotifyDtoMapper.findPayAccChangeNotifyDtoById(id);
	}
}
