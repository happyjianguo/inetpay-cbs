package com.ylink.inetpay.cbs.bis.rest;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.ylink.inetpay.cbs.bis.service.BisAreaCityService;
import com.ylink.inetpay.cbs.chl.service.CbsChlBankCodeService;
import com.ylink.inetpay.cbs.chl.service.ChlBankService;
import com.ylink.inetpay.common.core.constant.EMsgCode;
import com.ylink.inetpay.common.core.util.ExceptionProcUtil;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisAreaCityDto;
import com.ylink.inetpay.common.project.channel.dto.TbChlBank;
import com.ylink.inetpay.common.project.channel.dto.TbChlBankCode;
import com.ylink.inetpay.common.project.portal.vo.BankCodeInfoVO;
import com.ylink.inetpay.common.project.portal.vo.BankCodeResultVO;
import com.ylink.inetpay.common.project.portal.vo.BankCodeVO;
import com.ylink.inetpay.common.project.portal.vo.BankResultVO;
import com.ylink.inetpay.common.project.portal.vo.BankVO;

@Service("bisBankCodeRestService")
public class BisBankCodeRestServiceImpl implements BisBankCodeRestService {
	@Autowired
	private CbsChlBankCodeService cbsChlBankCodeService;
	@Autowired
	private ChlBankService chlBankService;
	@Autowired
	private BisAreaCityService bisAreaCityService;
	private static Logger _log = LoggerFactory.getLogger(BisBankCodeRestServiceImpl.class);

    //市
    private static final  String city="市";
    //区
    private static final  String city1="区";

	/***
	 * 央行联行号查询
	 * 
	 * @param params
	 * @return
	 */
	@Override
	public BankCodeResultVO getBankCode(String params) {
		_log.info("--------进入方法：getBankCode:" + params);
		BankCodeResultVO bankCodeResultVO = new BankCodeResultVO();
		BankCodeVO bankCodeInVO = new BankCodeVO();
		try {
			bankCodeInVO = convertBankCodeInVO(params);
		} catch (Exception e) {
			_log.error("解析json 参数报错：" + ExceptionProcUtil.getExceptionDesc(e));
			bankCodeResultVO.setMsgCode(EMsgCode.UN_LEGAL_PARAM.getValue());
			bankCodeResultVO.setMsgInfo(EMsgCode.UN_LEGAL_PARAM.getDisplayName());
			return bankCodeResultVO;
		}
		// check入参
		String checks = check(bankCodeInVO);
		if (StringUtils.isNotBlank(checks)) {
			_log.error("params:{},输入 的参数有错", new Object[] { params });
			bankCodeResultVO.setMsgCode(EMsgCode.UN_LEGAL_PARAM.getValue());
			bankCodeResultVO.setMsgInfo(checks);
			return bankCodeResultVO;
		}
        Set<String> cityCodes=new HashSet<>();
        BisAreaCityDto bisAreaCityDto = bisAreaCityService.getByCode(bankCodeInVO.getCity());
        if(bisAreaCityDto == null || StringUtils.isBlank(bisAreaCityDto.getName())){
            _log.error("params:{},输入 的参数有错", new Object[] { params });
            bankCodeResultVO.setMsgCode(EMsgCode.UN_LEGAL_PARAM.getValue());
            bankCodeResultVO.setMsgInfo("城市代码异常！");
            return bankCodeResultVO;
        }
        //查询出选择城市下面的所有数据
        List<BisAreaCityDto> cityList=bisAreaCityService.getCityList(bisAreaCityDto.getCode());
        if(cityList==null){
            cityList=new ArrayList<>();
        }
        cityList.add(bisAreaCityDto);
        for (BisAreaCityDto areaCityDto : cityList) {
            cityCodes.add(areaCityDto.getAlias());
        }
        //将list 转换为set
      //  cityCodes.addAll(cityList.stream().map(BisAreaCityDto::getAlias).collect(Collectors.toList()));

        // 判断
        TbChlBankCode bankCode  = convertTbChlBankCode(bankCodeInVO,cityCodes);
		List<TbChlBankCode> bankCodeList = cbsChlBankCodeService.getList(bankCode);
		if (bankCodeList == null || bankCodeList.size() < 1) {
			bankCodeResultVO.setMsgCode(EMsgCode.SUCCESS.getValue());
			bankCodeResultVO.setMsgInfo("查询成功，数据为空！");
			return bankCodeResultVO;
		}
		bankCodeResultVO = convertBankCodeResultVO(bankCodeList);
		bankCodeResultVO.setMsgCode(EMsgCode.SUCCESS.getValue());
		bankCodeResultVO.setMsgInfo(EMsgCode.SUCCESS.getDisplayName());
		return bankCodeResultVO;

	}

	/**
	 * 银行行别查询
	 * 
	 * @param params
	 * @return
	 */
	@Override
	public BankResultVO getBankList(String params) {
		_log.info("--------进入方法：getBankCode:" + params);
		BankResultVO bankResultVO = new BankResultVO();
		BankVO bankVO = new BankVO();
		TbChlBank record = new TbChlBank();
		try {
			bankVO = convertBankVO(params);
		} catch (Exception e) {
			_log.error("解析json 参数报错：" + ExceptionProcUtil.getExceptionDesc(e));
			bankResultVO.setMsgCode(EMsgCode.UN_LEGAL_PARAM.getValue());
			bankResultVO.setMsgInfo(EMsgCode.UN_LEGAL_PARAM.getDisplayName());
			return bankResultVO;
		}
		// 判断参数
		record = convertTbChlBank(bankVO);
		List<TbChlBank> recordList = chlBankService.getList(record);
		if (recordList == null || recordList.size() < 1) {
			bankResultVO.setMsgCode(EMsgCode.SUCCESS.getValue());
			bankResultVO.setMsgInfo("查询成功，数据为空！");
			return bankResultVO;
		}
		bankResultVO = convertBankResultVO(recordList);
		bankResultVO.setMsgCode(EMsgCode.SUCCESS.getValue());
		bankResultVO.setMsgInfo(EMsgCode.SUCCESS.getDisplayName());
		return bankResultVO;
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
	 * 判断参数
	 * 
	 * @return
	 */
	public String check(BankCodeVO bankCodeInVO) {
		if (bankCodeInVO == null) {
			return "参数异常：入参为空！";
		}
		if (StringUtils.isBlank(bankCodeInVO.getBankType())) {
			return "省份代码：银行行别代码入参为空！";
		}
		if (StringUtils.isBlank(bankCodeInVO.getProvince())) {
			return "参数异常：省份代码为空！";
		}
		if (StringUtils.isBlank(bankCodeInVO.getCity())) {
			return "参数异常：城市代码入参为空！";
		}
		return null;

	}

	/**
	 * 入参转换
	 * 
	 * @param bankCodeInVO
	 * @return
	 */
    public TbChlBankCode convertTbChlBankCode(BankCodeVO bankCodeInVO,Set<String> cityCodes) {
        TbChlBankCode tbChlBankCode = new TbChlBankCode();
        tbChlBankCode.setBankType(bankCodeInVO.getBankType());
        tbChlBankCode.setCityCodes(cityCodes);
        if (StringUtils.isNotBlank(bankCodeInVO.getBranchName())) {
            tbChlBankCode.setBranchName(bankCodeInVO.getBranchName().replace("支行", ""));
        }
        return tbChlBankCode;

    }

	/**
	 * 入参转换
	 * 
	 * @param bankCodeInVO
	 * @return
	 */
	public TbChlBank convertTbChlBank(BankVO bankVO) {
		TbChlBank tbChlBank = new TbChlBank();
		if (bankVO == null) {
			return tbChlBank;
		}
		if (StringUtils.isNotBlank(bankVO.getBankType())) {
			tbChlBank.setBankType(bankVO.getBankType());
			return tbChlBank;
		}
		if (StringUtils.isBlank(bankVO.getBankType()) && StringUtils.isNotBlank(bankVO.getBankName())) {
			tbChlBank.setBankName(bankVO.getBankName().replace("银行", ""));
			return tbChlBank;
		}
		if (StringUtils.isNotBlank(bankVO.getBankType()) && StringUtils.isNotBlank(bankVO.getBankName())) {
			tbChlBank.setBankType(bankVO.getBankType());
			tbChlBank.setBankName(bankVO.getBankName().replace("银行", ""));
			return tbChlBank;
		}
		return tbChlBank;

	}

	/**
	 * @param params
	 * @return
	 * @throws Exception
	 */
	private BankCodeVO convertBankCodeInVO(String params) throws Exception {
		BankCodeVO bankCodeInVO = null;
		try {
			bankCodeInVO = getObjectBean(params, BankCodeVO.class);
			if (bankCodeInVO == null) {
				throw new Exception("转换对象为空、params对象转换失败");
			}
			return bankCodeInVO;
		} catch (Exception e) {
			_log.error("Gson转换错误：" + e.toString());
			throw new Exception("Gson转换错误：" + e.toString());
		}
	}

	/**
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	private BankVO convertBankVO(String params) throws Exception {
		BankVO bankVO = null;
		try {
			bankVO = getObjectBean(params, BankVO.class);
			if (bankVO == null) {
				throw new Exception("转换对象为空、params对象转换失败");
			}
			return bankVO;
		} catch (Exception e) {
			_log.error("Gson转换错误：" + e.toString());
			throw new Exception("Gson转换错误：" + e.toString());
		}
	}

	/**
	 * 类型转换TbChlBankCode-->BankCodeResultVO
	 * 
	 * @param bankCodeList
	 * @return
	 */
	public BankCodeResultVO convertBankCodeResultVO(List<TbChlBankCode> bankCodeList) {
		BankCodeResultVO bankCodeResultVO = new BankCodeResultVO();
		List<BankCodeInfoVO> result = new ArrayList<>();
		if (bankCodeList != null && bankCodeList.size() > 0) {
			for (TbChlBankCode tbChlBankCode : bankCodeList) {
				BankCodeInfoVO bankCodeVO = new BankCodeInfoVO();
				bankCodeVO.setBankType(tbChlBankCode.getBankType());
				bankCodeVO.setBranchName(tbChlBankCode.getBankName());
				bankCodeVO.setBranchNo(tbChlBankCode.getBankCode());
				result.add(bankCodeVO);
			}
			bankCodeResultVO.setResult(result);
		}
		return bankCodeResultVO;
	}

	/**
	 * 类型转换TbChlBank-->BankResultVO
	 * 
	 * @param bankCodeList
	 * @return
	 */
	public BankResultVO convertBankResultVO(List<TbChlBank> recordList) {
		BankResultVO bankResultVO = new BankResultVO();
		List<BankVO> result = new ArrayList<>();
		if (recordList != null && recordList.size() > 0) {
			for (TbChlBank tbChlBank : recordList) {
				BankVO bankVO = new BankVO();
				bankVO.setBankName(tbChlBank.getBankName());
				bankVO.setBankType(tbChlBank.getBankType());
				result.add(bankVO);
			}
			bankResultVO.setResult(result);
		}
		return bankResultVO;
	}


}
