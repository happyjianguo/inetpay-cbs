package com.ylink.inetpay.cbs.pay.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;
import com.alibaba.dubbo.common.utils.StringUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.gson.Gson;
import com.ylink.inetpay.cbs.bis.dao.BisAreaCityDaoMapper;
import com.ylink.inetpay.cbs.chl.dao.TbChlBankMapper;
import com.ylink.inetpay.cbs.chl.service.ChlBankService;
import com.ylink.inetpay.cbs.pay.dao.PayAmtAllocateDtoMapper;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisAreaCityDto;
import com.ylink.inetpay.common.project.cbs.exception.PortalCode;
import com.ylink.inetpay.common.project.channel.dto.TbChlBank;
import com.ylink.inetpay.common.project.channel.dto.TbChlBankCode;
import com.ylink.inetpay.common.project.pay.dto.PayAmtAllocateDto;
import com.ylink.inetpay.common.project.pay.dto.PayAmtAllocateEleDto;
import com.ylink.inetpay.common.project.portal.exception.PortalCheckedException;
@Service("payAmtAllocateService")
public class PayAmtAllocateServiceImpl implements PayAmtAllocateService {
	@Autowired
	private PayAmtAllocateDtoMapper payAmtAllocateDtoMapper;
	
	@Autowired
	private BisAreaCityDaoMapper bisAreaCityDaoMapper;

	@Autowired
 
	private TbChlBankMapper tbChlBankMapper;
  
	@Autowired
	ChlBankService chlBankService;

	@Override
	public PageData<PayAmtAllocateDto> findAll(	PageData<PayAmtAllocateDto> pageData, PayAmtAllocateDto queryparam){
		
		//付款方银行类型查询条件
		 List<TbChlBank> payerBankTypeLis=null;
		 if(!StringUtils.isBlank(queryparam.getPayerBankTypeName())){
			 payerBankTypeLis=tbChlBankMapper.findPayerBankType(queryparam.getPayerBankTypeName());
		 }
		 //收款银行类型查询条件
		 List<TbChlBank> payeeBankTypeList=null;
		 if(!StringUtils.isBlank(queryparam.getPayeeBankTypeName())){
			 payeeBankTypeList=tbChlBankMapper.findPayerBankType(queryparam.getPayeeBankTypeName());
		}
			 
		PageHelper.startPage(pageData.getPageNumber(),pageData.getPageSize());
		List<PayAmtAllocateDto> list=payAmtAllocateDtoMapper.list(queryparam,payerBankTypeLis,payeeBankTypeList);
		if(list!=null && !list.isEmpty()){
			Map<String,String> bankTypeMap = new HashMap<String,String>();
			for (PayAmtAllocateDto payBill : list) {
				bankTypeMap.put(payBill.getPayeeBankType(),payBill.getPayeeBankType());
				bankTypeMap.put(payBill.getPayerBankType(),payBill.getPayerBankType());
			}
			Set<String> keySet = bankTypeMap.keySet();
			List<TbChlBank> bankTypeDtoList = tbChlBankMapper.findByBankTypeSet(keySet);
			if(bankTypeDtoList!=null && !bankTypeDtoList.isEmpty()){
				Map<String,String> bankTypeNameMap = new HashMap<String,String>(); 
				for (TbChlBank tbChlBank : bankTypeDtoList) {
					bankTypeNameMap.put(tbChlBank.getBankType(), tbChlBank.getBankName());
				}
				for (PayAmtAllocateDto payBill : list) {
					payBill.setPayeeBankTypeName(bankTypeNameMap.get(payBill.getPayeeBankType()));
					payBill.setPayerBankTypeName(bankTypeNameMap.get(payBill.getPayerBankType()));
				}
			}
		}
		Page<PayAmtAllocateDto> page=(Page<PayAmtAllocateDto>) list;
		pageData.setTotal(page.getTotal());
		pageData.setRows(list);
		return pageData;
	}
	
	public static void listToMap(List<TbChlBankCode> bankCodes,Map<String,String> bankCodeMap){
		for (TbChlBankCode dto : bankCodes) {
			bankCodeMap.put(dto.getBankCode(), dto.getBankName());
		}
	}

	@Override
	public PayAmtAllocateDto details(String id) {
		PayAmtAllocateDto payAmtAllocateDto = payAmtAllocateDtoMapper.selectByPrimaryKey(id);
		if(payAmtAllocateDto!=null ){
			List<String> bankTypes = new ArrayList<>();
			bankTypes.add(payAmtAllocateDto.getPayeeBankType());
			bankTypes.add(payAmtAllocateDto.getPayerBankType());
			//查询多个银行信息
			if(bankTypes!=null&&bankTypes.size()>0){
				List<TbChlBank> bankList=tbChlBankMapper.findByBankTypes(bankTypes);
				if(bankList !=null && bankList.size()>0){
						 for(TbChlBank bankdto : bankList){
							 if(!StringUtils.isBlank(payAmtAllocateDto.getPayeeBankType())
									 && payAmtAllocateDto.getPayeeBankType().equals(bankdto.getBankType())){
								 payAmtAllocateDto.setPayeeBankTypeName(bankdto.getBankName());
								 break;
							 }
						 }
						 for(TbChlBank bankdto : bankList){
							 if(!StringUtils.isBlank(payAmtAllocateDto.getPayerBankType())
									 && payAmtAllocateDto.getPayerBankType().equals(bankdto.getBankType())){
								 payAmtAllocateDto.setPayerBankTypeName(bankdto.getBankName());
								 break;
							 }
						 }
				}
			}
		}
		return payAmtAllocateDto;
	}

	@Override
	public List<PayAmtAllocateDto> findList(PayAmtAllocateDto payAmtAllocate) {
		return payAmtAllocateDtoMapper.listForOut(payAmtAllocate);
	}

	@Override
	public List<PayAmtAllocateDto> queryAllPayAmtAllocate(PayAmtAllocateDto queryParam) {
		//付款方银行类型
		List<TbChlBank> payerBankTypeLis=null;
		if(!StringUtils.isBlank(queryParam.getPayerBankTypeName())){
			payerBankTypeLis=tbChlBankMapper.findPayerBankType(queryParam.getPayerBankTypeName());
		}
		//收款银行类型
		List<TbChlBank> payeeBankTypeList=null;
		if(!StringUtils.isBlank(queryParam.getPayeeBankTypeName())){
			payeeBankTypeList=tbChlBankMapper.findPayerBankType(queryParam.getPayeeBankTypeName());
		}
		List<PayAmtAllocateDto> list = payAmtAllocateDtoMapper.list(queryParam, payerBankTypeLis,payeeBankTypeList);
		if(list!=null && !list.isEmpty()){
			Map<String,String> bankTypeMap = new HashMap<String,String>();
			for (PayAmtAllocateDto payBill : list) {
				bankTypeMap.put(payBill.getPayeeBankType(),payBill.getPayeeBankType());
				bankTypeMap.put(payBill.getPayerBankType(),payBill.getPayerBankType());
			}
			Set<String> keySet = bankTypeMap.keySet();
			List<TbChlBank> bankTypeDtoList = tbChlBankMapper.findByBankTypeSet(keySet);
			if(bankTypeDtoList!=null && !bankTypeDtoList.isEmpty()){
				Map<String,String> bankTypeNameMap = new HashMap<String,String>(); 
				for (TbChlBank tbChlBank : bankTypeDtoList) {
					bankTypeNameMap.put(tbChlBank.getBankType(), tbChlBank.getBankName());
				}
				for (PayAmtAllocateDto payBill : list) {
					payBill.setPayeeBankTypeName(bankTypeNameMap.get(payBill.getPayeeBankType()));
					payBill.setPayerBankTypeName(bankTypeNameMap.get(payBill.getPayerBankType()));
				}
			}
		}
		return list;
	}
	@Override
	public PageData<PayAmtAllocateDto> queryAll(PageData<PayAmtAllocateDto> pageData,
			PayAmtAllocateDto payAmtAllocateDto) {
		PageHelper.startPage(pageData.getPageNumber(), pageData.getPageSize());
		List<PayAmtAllocateDto> list = payAmtAllocateDtoMapper.queryAll(payAmtAllocateDto);
		Page<PayAmtAllocateDto> page = (Page<PayAmtAllocateDto>)list;
		pageData.setTotal(page.getTotal());
		pageData.setRows(list);
		return pageData;
	}

	@Override
	public PayAmtAllocateDto receiptDetail(String busiId) {
		PayAmtAllocateDto payAmtAllocateDto = null;
		try {
			payAmtAllocateDto = payAmtAllocateDtoMapper.selectByPrimaryKey(busiId);
		} catch (Exception e) {
			System.out.println("收款查询错误:"+e);
		}
		return payAmtAllocateDto;
	}

	@Override
	public List<PayAmtAllocateDto> listBatchNo(PayAmtAllocateDto queryParam) {
		List<PayAmtAllocateDto> list = payAmtAllocateDtoMapper.listBatchNo(queryParam);
		ArrayList<String> bankTypes = new ArrayList<String>();
		ArrayList<String> paybankTypes = new ArrayList<String>();
		PayAmtAllocateEleDto jsonToVO;
		for (PayAmtAllocateDto dto : list) {
			bankTypes.add(dto.getPayeeBankType());
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
			for (PayAmtAllocateDto dto : list) {
				TbChlBank tbChlBank = bankTypeMap.get(dto.getPayeeBankType());
				if(null != dto.getRemark() || dto.getRemark() != ""){
					try {
						jsonToVO = this.jsonToVO(dto.getRemark());
						dto.setPayAmtAllocateEleDto(jsonToVO);
					} catch (Exception e) {
						System.out.println(e);
						e.printStackTrace();
					}
				}
				if(tbChlBank!=null){
					dto.setPayeeBankTypeName(tbChlBank.getBankName());
				}else{
					dto.setPayeeBankTypeName(null);
				}
			}
			for (PayAmtAllocateDto dto : list) {
				TbChlBank tbChlBank = bankTypeMap.get(dto.getPayeeBankType());
				if(tbChlBank!=null){
					dto.setPayerBankTypeName(tbChlBank.getBankName());
				}else{
					dto.setPayerBankTypeName(null);
				}
			}
		}
		return list;
	}
	
	/**
	 * 转换处理
	 * @param params
	 * @return
	 * @throws Exception
	 */
	private PayAmtAllocateEleDto jsonToVO(String params) throws Exception{
		PayAmtAllocateEleDto requestVo = null;
		try {
			requestVo = getObjectBean(params,PayAmtAllocateEleDto.class);
			if(requestVo == null){
//				logger.error("转换对象为空、Gson对象转换失败!");
				throw new PortalCheckedException(PortalCode.CODE_9999,"JSON转换失败");
			}
			return requestVo;
		} catch (Exception e) {
//			loggger.error("Gson转换错误："+ e.toString());
			throw new PortalCheckedException(PortalCode.CODE_9999,"JSON转换失败");
		}
	}
	/**
	 * 从json获取Bean 对象
	 * @param jsonString
	 * @param cls
	 * @return
	 */
	private <T> T getObjectBean(String jsonString, Class<T> cls) {
        T t = null;
        Gson gson = new Gson();
        t = gson.fromJson(jsonString, cls);
        return t;
    }

	@Override
	public List<PayAmtAllocateDto> findPayAmtAllocateByBatchNo(PayAmtAllocateDto queryParam) {
		List<PayAmtAllocateDto> PayAmtAllocateList = payAmtAllocateDtoMapper.findPayAmtAllocateByBatchNo(queryParam);
		
		if(PayAmtAllocateList!=null && PayAmtAllocateList.size()>0){
			Map<String,String> bankTypeMap = new HashMap<String,String>();
			Map<String,String> cityMao= new HashMap<String,String>();
			for (PayAmtAllocateDto dato : PayAmtAllocateList) {
				bankTypeMap.put(dato.getPayeeBankType(),dato.getPayeeBankType());
				if(!StringUtils.isBlank(dato.getCity())){
					cityMao.put(dato.getCity(),dato.getCity());
				}
				if(!StringUtils.isBlank(dato.getProvince())){
					cityMao.put(dato.getProvince(),dato.getProvince());
				}
			}
			Set<String> keySet = bankTypeMap.keySet();
			Set<String> cityKey = cityMao.keySet();
			List<TbChlBank> bankTypeDtoList = tbChlBankMapper.findByBankTypeSet(keySet);
			List<BisAreaCityDto> bisAreaCityDotList=bisAreaCityDaoMapper.getcityName(cityKey);
			if(bankTypeDtoList!=null && !bankTypeDtoList.isEmpty()){
				Map<String,String> bankTypeNameMap = new HashMap<String,String>(); 
				for (TbChlBank tbChlBank : bankTypeDtoList) {
					bankTypeNameMap.put(tbChlBank.getBankType(), tbChlBank.getBankName());
				}
				for (PayAmtAllocateDto payBill : PayAmtAllocateList) {
					payBill.setPayeeBankTypeName(bankTypeNameMap.get(payBill.getPayeeBankType()));
					
				}
			}
			if(bisAreaCityDotList!=null&&!bisAreaCityDotList.isEmpty()){
				Map<String,String> cityNameMap = new HashMap<String,String>(); 
				for (BisAreaCityDto cityDto : bisAreaCityDotList) {
					cityNameMap.put(cityDto.getCode(),cityDto.getName());
				}
				for (PayAmtAllocateDto payDto : PayAmtAllocateList) {
					payDto.setCity(cityNameMap.get(payDto.getCity()));
					payDto.setProvince(cityNameMap.get(payDto.getProvince()));
				}
			}
		}
		
		
		return PayAmtAllocateList;
	}
 
}
