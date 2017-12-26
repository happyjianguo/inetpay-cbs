package com.ylink.inetpay.cbs.bis.service;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ylink.inetpay.cbs.UCBaseTest;
import com.ylink.inetpay.common.core.util.StringUtils;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisAreaCityDto;

public class BisAreaAppServiceTest extends UCBaseTest {
	@Autowired
	private BisAreaCityService bisAreaCityService;

	@Test
	public void test() {
		//一级
		List<BisAreaCityDto> cityList = bisAreaCityService.getCityList("COUNTRY_CN");
		//二三级
		List<BisAreaCityDto> cityAllList = bisAreaCityService.getCityAllList();
		StringBuffer json = new StringBuffer();
		StringBuffer jsonOneLevel = new StringBuffer();
		StringBuffer jsonTwoLevel = new StringBuffer();
		if (cityList != null && cityList.size() > 0 &&
				cityAllList != null && cityAllList.size() > 0) {
			//start
			 json.append("{86: {  'A_Z': [ ");
			 int onecitylist = cityList.size();
			for (int i = 0; i < onecitylist; i++){
				BisAreaCityDto bcd = cityList.get(i);
				if ((onecitylist - 1) == i) {
					//last line
					json.append("{code: '").append(bcd.getCode()).append("', address: '").append(bcd.getName()).append("'}],}, ");
				}else{
					json.append("{code: '").append(bcd.getCode()).append("', address: '").append(bcd.getName()).append("'},");
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
					//json.append("},");
				} else {
					if (onetoGeoId.equals(toGeoId)) {
						json.append(fromGeoId).append(": '").append(name).append("',");
					} else {
						json.append("},");// 上次一级的结束
						json.append(toGeoId).append(": {");
						json.append(fromGeoId).append(": '").append(name).append("',");
						// 最后一行
						if ((allList - 1) == i) {
							jsonOneLevel.append(jsonTwoLevel).append("}");// 上次一级的结束
						}
					}
				}
				onetoGeoId = toGeoId;
			}
			// end
			 json.append("}");
		}
	}
}