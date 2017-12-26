package com.ylink.inetpay.cbs.bis.rest;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.ylink.inetpay.cbs.bis.service.BisAreaCityService;
import com.ylink.inetpay.common.core.constant.EMsgCode;
import com.ylink.inetpay.common.core.util.ExceptionProcUtil;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisAreaCityDto;
import com.ylink.inetpay.common.project.portal.vo.AreaCityInfoVO;
import com.ylink.inetpay.common.project.portal.vo.AreaCityResultVO;
import com.ylink.inetpay.common.project.portal.vo.AreaCityVO;

@Service("bisAreaCityRestService")
public class BisAreaCityRestServiceImpl implements BisAreaCityRestService {
	@Autowired
	private BisAreaCityService bisAreaCityService;

	private static Logger _log = LoggerFactory.getLogger(BisAreaCityRestServiceImpl.class);

	@Override
	public AreaCityResultVO getBankCode(String params) {
		_log.info("--------进入方法：getBankCode:" + params);
		AreaCityResultVO areaCityResultVO = new AreaCityResultVO();
		AreaCityVO areaCityVO = new AreaCityVO();
		try {
			areaCityVO = convertBankCodeInVO(params);
		} catch (Exception e) {
			_log.error("解析json 参数报错：{}",ExceptionProcUtil.getExceptionDesc(e));
			areaCityResultVO.setMsgCode(EMsgCode.UN_LEGAL_PARAM.getValue());
			areaCityResultVO.setMsgInfo(EMsgCode.UN_LEGAL_PARAM.getDisplayName());
			return areaCityResultVO;
		}
		String checks = check(areaCityVO);
		if(StringUtils.isNotBlank(checks)){
			_log.error("params:{},输入 的参数有错", new Object[] { params });
			areaCityResultVO.setMsgCode(EMsgCode.UN_LEGAL_PARAM.getValue());
			areaCityResultVO.setMsgInfo(checks);
			areaCityResultVO.setType(areaCityVO.getQueryType());
			return areaCityResultVO;
		}
		String code = convert(areaCityVO);
		if(StringUtils.isBlank(code)){
			_log.error("params:{},输入 的参数有错", new Object[] { params });
			areaCityResultVO.setMsgCode(EMsgCode.UN_LEGAL_PARAM.getValue());
			areaCityResultVO.setMsgInfo("传参异常！");
			areaCityResultVO.setType(areaCityVO.getQueryType());
			return areaCityResultVO;
		}
		List<BisAreaCityDto> cityList = bisAreaCityService.getCityList(code);
		if (cityList == null || cityList.size() < 1) {
			areaCityResultVO.setMsgCode(EMsgCode.SUCCESS.getValue());
			areaCityResultVO.setMsgInfo("查询成功，数据为空！");
			areaCityResultVO.setType(areaCityVO.getQueryType());
			return areaCityResultVO;
		}
		areaCityResultVO = convertBisAreaCityDto(cityList);
		areaCityResultVO.setType(areaCityVO.getQueryType());
		areaCityResultVO.setMsgCode(EMsgCode.SUCCESS.getValue());
		areaCityResultVO.setMsgInfo(EMsgCode.SUCCESS.getDisplayName());
		return areaCityResultVO;
	}

	public String check(AreaCityVO areaCityVO){
		if(areaCityVO == null){
			return "参数异常：入参为空！";
		}
		if((StringUtils.equals("2", areaCityVO.getQueryType())||"3".equals(areaCityVO.getQueryType()))
                && StringUtils.isBlank(areaCityVO.getLastCode())){
			return "参数异常：查询类型为城市，省份代码不能为空！";
		}
		return null;
		
	}
	
	/**
	 * 获取参数
	 * @param areaCityVO
	 * @return
	 */
	public String convert(AreaCityVO areaCityVO){
		if(StringUtils.equals("1", areaCityVO.getQueryType())){
			return "COUNTRY_CN";
		}
		if((StringUtils.equals("2", areaCityVO.getQueryType())||"3".equals(areaCityVO.getQueryType()) )
                && StringUtils.isNotBlank(areaCityVO.getLastCode())){
			return areaCityVO.getLastCode();
		}
		return null;
		
	}
	
	public AreaCityResultVO convertBisAreaCityDto(List<BisAreaCityDto> cityList){
		AreaCityResultVO areaCityResultVO = new AreaCityResultVO();
		List<AreaCityInfoVO> result = new ArrayList<>();
		if(cityList != null && cityList.size()>0){
			for (BisAreaCityDto bisAreaCityDto : cityList) {
				AreaCityInfoVO areaCityInfoVO = new AreaCityInfoVO();
				areaCityInfoVO.setCode(bisAreaCityDto.getCode());
				areaCityInfoVO.setName(bisAreaCityDto.getName());
				result.add(areaCityInfoVO);
			}
			areaCityResultVO.setResult(result);
		}
		return areaCityResultVO;
		
	}
	
	/**
	 * 从json获取Bean 对象
	 * 
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

	/**
	 * @param params
	 * @return
	 * @throws Exception
	 */
	private AreaCityVO convertBankCodeInVO(String params) throws Exception {
		AreaCityVO areaCityVO = null;
		try {
			areaCityVO = getObjectBean(params, AreaCityVO.class);
			if (areaCityVO == null) {
				throw new Exception("转换对象为空、params对象转换失败");
			}
			return areaCityVO;
		} catch (Exception e) {
			_log.error("Gson转换错误：" + e.toString());
			throw new Exception("Gson转换错误：" + e.toString());
		}
	}
}
