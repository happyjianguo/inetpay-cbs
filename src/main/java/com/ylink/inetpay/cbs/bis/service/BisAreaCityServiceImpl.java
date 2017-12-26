package com.ylink.inetpay.cbs.bis.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ylink.inetpay.cbs.bis.cache.BisAreaCityDtoCache;
import com.ylink.inetpay.cbs.bis.dao.BisAreaCityDaoMapper;
import com.ylink.inetpay.cbs.chl.service.CbsChlBankCodeService;
import com.ylink.inetpay.common.core.util.StringUtils;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisAreaCityDto;
import com.ylink.inetpay.common.project.channel.dto.TbChlBankCode;

@Service("bisAreaCityService")
public class BisAreaCityServiceImpl implements BisAreaCityService {
	@Autowired
	BisAreaCityDtoCache bisAreaCityDtoCache;
	@Autowired
	private CbsChlBankCodeService cbsChlBankCodeService;
	@Autowired
	private BisAreaCityDaoMapper bisAreaCityDaoMapper;

	@Override
	public List<BisAreaCityDto> getCityList(String code) {
		return bisAreaCityDtoCache.getCityList(code);
	}

	@Override
	public BisAreaCityDto getByName(String name) {
		return bisAreaCityDtoCache.getByName(name);
	}

	@Override
	public BisAreaCityDto getByCode(String code) {
		return bisAreaCityDtoCache.getByCode(code);
	}

	@Override
	public List<BisAreaCityDto> getCityAllList() {
		return bisAreaCityDtoCache.getCityAllList();
	}

	@Override
	public List<TbChlBankCode> getBankCodeByPortal(String cityCode, String bankType) {
		if(StringUtils.isEmpty(cityCode) || StringUtils.isEmpty(bankType)){
			return null;
		}
//		String cCode = "";
		//页面传的中文名，则要根据名称去查询
//		String[] names = name.split("/");
//		if(names.length==3){
//			List<BisAreaCityDto> cityList = bisAreaCityDtoCache.getCityByName(names[2]);
//			if(cityList!=null && cityList.size()==1){
//				cCode = cityList.get(0).getAlias();
//			}else if(cityList.size()>1){
//				List<BisAreaCityDto> cityTwoList = bisAreaCityDtoCache.getCityByName(names[1]);
//				if(cityTwoList!=null && cityTwoList.size()>0){
//					List<BisAreaCityDto> cityThreeList = 
//							bisAreaCityDtoCache.getCityByCodeAndName(cityTwoList.get(0).getCode().substring(0, 4), names[2]);
//					if(cityThreeList!=null && cityThreeList.size()>0){
//						cCode = cityThreeList.get(0).getAlias();
//					}
//				}
//			}
//		} else if(names.length==2){
//			List<BisAreaCityDto> cityList = bisAreaCityDtoCache.getCityByName(names[1]);
//			if(cityList!=null && cityList.size()==1){
//				cCode = cityList.get(0).getAlias();
//			}else if(cityList.size()>1){
//				List<BisAreaCityDto> cityTwoList = bisAreaCityDtoCache.getCityByName(names[0]);
//				if(cityTwoList!=null && cityTwoList.size()>0){
//					List<BisAreaCityDto> cityThreeList = 
//							bisAreaCityDtoCache.getCityByCodeAndName(cityTwoList.get(0).getCode().substring(0, 2), names[1]);
//					if(cityThreeList!=null && cityThreeList.size()>0){
//						cCode = cityThreeList.get(0).getAlias();
//					}
//				}
//			}
//		}else{
//			return null;
//		}
		BisAreaCityDto city = bisAreaCityDtoCache.getByCode(cityCode);
		if(city==null){
			return null;
		}
		// 根据银行城市代码和银行行号获取精确银行信息
		return cbsChlBankCodeService.getDepositBankCode(city.getAlias(), bankType);
	}

	@Override
	public List<BisAreaCityDto> getCityByCodes(List<String> codes) {
		return bisAreaCityDaoMapper.getCityListByCodes(codes);
	}

}
