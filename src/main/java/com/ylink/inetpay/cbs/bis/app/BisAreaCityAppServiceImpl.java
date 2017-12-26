package com.ylink.inetpay.cbs.bis.app;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ylink.inetpay.cbs.bis.service.BisAreaCityService;
import com.ylink.inetpay.common.core.util.StringUtils;
import com.ylink.inetpay.common.project.cbs.app.BisAreaCityAppService;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisAreaCityDto;
import com.ylink.inetpay.common.project.channel.dto.TbChlBankCode;

@Service("bisAreaCityAppService")
public class BisAreaCityAppServiceImpl implements BisAreaCityAppService {
	@Autowired
	private BisAreaCityService bisAreaCityService;

	@Override
	public List<BisAreaCityDto> getCityList(String code) {
		return bisAreaCityService.getCityList(code);
	}

	@Override
	public BisAreaCityDto getByName(String name) {
		return bisAreaCityService.getByName(name);
	}

	@Override
	public BisAreaCityDto getByCode(String code) {
		return bisAreaCityService.getByCode(code);
	}

	@Override
	public Object getCityAllList() {
		// 一级
		List<BisAreaCityDto> cityListold = bisAreaCityService.getCityList("COUNTRY_CN");
		//排序
		String[] strCity = new String[31];
		int j = 0;
		for(BisAreaCityDto dto :cityListold){
			String s= dto.getName();
			if("重庆市".equals(s)){
				s="从庆市";
			}
			strCity[j] = s;
			j++;
		}
		 Comparator<Object> com=Collator.getInstance(java.util.Locale.CHINA);  
		 Arrays.sort(strCity,com);
		 List<BisAreaCityDto> cityList = new ArrayList<BisAreaCityDto>();
        for(String i:strCity){
        	if("从庆市".equals(i)){
				i="重庆市";
			}
        	for(BisAreaCityDto dto :cityListold){
    			if(i.equals(dto.getName())){
    				cityList.add(dto);
    			};
    		}
        }  

		// 二三级
		List<BisAreaCityDto> cityAllList = bisAreaCityService.getCityAllList();
		StringBuffer json = new StringBuffer();
		if (cityList != null && cityList.size() > 0 && cityAllList != null && cityAllList.size() > 0) {
			// start
			json.append("{86: {  'A_Z': [ ");
			int onecitylist = cityList.size();
			for (int i = 0; i < onecitylist; i++) {
				BisAreaCityDto bcd = cityList.get(i);
				if ((onecitylist - 1) == i) {
					// last line
					json.append("{code: '").append(bcd.getCode()).append("', address: '").append(bcd.getName())
							.append("'}],}, ");
				} else {
					json.append("{code: '").append(bcd.getCode()).append("', address: '").append(bcd.getName())
							.append("'},");
				}

			}
			// 整体分组
			String onetoGeoId = "";
			int allList = cityAllList.size();
			for (int i = 0; i < allList; i++) {
				BisAreaCityDto cityDto = cityAllList.get(i);
				// 整体
				String toGeoId = cityDto.getToGeoId();
				// 部分
				String fromGeoId = cityDto.getFromGeoId();
				// 名称
				String name = cityDto.getName();
				// 首次
				if (StringUtils.isBlank(onetoGeoId)) {
					json.append(toGeoId).append(": {");
					json.append(fromGeoId).append(": '").append(name).append("',");
				} 
				else {
					if (onetoGeoId.equals(toGeoId)) {
						json.append(fromGeoId).append(": '").append(name).append("',");
					} else {
						json.append("},");// 上次一级的结束
						json.append(toGeoId).append(": {");
						json.append(fromGeoId).append(": '").append(name).append("',");
					}
					// 最后一行
					if ((allList - 1) == i) {
						json.append("}");// 上次一级的结束
					}
				}
				onetoGeoId = toGeoId;
			}
			// end
			json.append("}");
		}
		return json;

	}

	@Override
	public List<TbChlBankCode> getBankCodeByPortal(String cityCode, String bankType) {
		return bisAreaCityService.getBankCodeByPortal(cityCode, bankType);
	}

	@Override
	public List<BisAreaCityDto> getCityByCodes(List<String> codes) {
		return bisAreaCityService.getCityByCodes(codes);
	}

}
