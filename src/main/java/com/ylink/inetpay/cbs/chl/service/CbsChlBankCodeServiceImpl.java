package com.ylink.inetpay.cbs.chl.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.ylink.inetpay.cbs.chl.dao.TbChlBankCodeMapper;
import com.ylink.inetpay.common.project.cbs.exception.CbsCheckedException;
import com.ylink.inetpay.common.project.channel.app.ChlBankCodeAppService;
import com.ylink.inetpay.common.project.channel.dto.TbChlBankCode;
import com.ylink.inetpay.common.project.channel.exception.ChannelCheckedException;
@Service("cbsChlBankCodeService")
public class CbsChlBankCodeServiceImpl implements CbsChlBankCodeService {
	@Resource
	private ChlBankCodeAppService chlBankCodeAppService;
	@Resource
	private TbChlBankCodeMapper tbChlBankCodeMapper;
	@Override
	public void saveBacnkCode(TbChlBankCode bankCode) throws CbsCheckedException {
		try {
			chlBankCodeAppService.saveBacnkCode(bankCode);
		} catch (ChannelCheckedException e) {
			throw new CbsCheckedException(e.getCode(),"新增失败：渠道系统接口异常");
		}
	}

	@Override
	public void updateBankCodeById(TbChlBankCode bankCode) throws CbsCheckedException {
		try {
			chlBankCodeAppService.updateBankCodeById(bankCode);
		} catch (ChannelCheckedException e) {
			throw new CbsCheckedException(e.getCode(),"修改失败：渠道系统接口异常");
		}
	}

	@Override
	public void deleteBankCodeId(String id) throws CbsCheckedException {
		try {
			chlBankCodeAppService.deleteBankCodeId(id);
		} catch (ChannelCheckedException e) {
			throw new CbsCheckedException(e.getCode(),"删除失败：渠道系统接口异常");
		}
	}

	@Override
	public PageData<TbChlBankCode> queryAllData(PageData<TbChlBankCode> pageData,TbChlBankCode bankCode) throws CbsCheckedException {
		PageHelper.startPage(pageData.getPageNumber(),pageData.getPageSize());
		List<TbChlBankCode> findListPage=tbChlBankCodeMapper.findListPage(bankCode);
		Page<TbChlBankCode> page=(Page<TbChlBankCode>) findListPage;
		pageData.setTotal(page.getTotal());
		pageData.setRows(findListPage);
		return pageData;
	}

	@Override
	public TbChlBankCode getBankCodeById(String id) {
		return tbChlBankCodeMapper.selectByPrimaryKey(id);
	}
	@Override
	public List<TbChlBankCode> getDepositBankCode(String cityCode, String bankType) {
		return tbChlBankCodeMapper.getDepositBankCode(cityCode,bankType);
	}

	@Override
	public List<TbChlBankCode> getList(TbChlBankCode bankCode) {
		return tbChlBankCodeMapper.getList(bankCode);
	}

	@Override
	public List<TbChlBankCode> findBankNameByBankCode(String bankCode) {
		return tbChlBankCodeMapper.findBankNameByBankCode(bankCode);
	}

	@Override
	public List<TbChlBankCode> findBankByBankName(String bankName) {
		return tbChlBankCodeMapper.findBankByBankName(bankName);
	}

	@Override
	public Map<String, Map<String, TbChlBankCode>> findChlBankCodeMapByBankNames(List<String> queryBankNames) {
		List<TbChlBankCode> chlBankCodes=tbChlBankCodeMapper.findChlBankCodeMapByBankNames(queryBankNames);
		HashMap<String, Map<String, TbChlBankCode>> bankCodeMap = new HashMap<String,Map<String,TbChlBankCode>>();
		if(chlBankCodes!=null && !chlBankCodes.isEmpty()){
			for (TbChlBankCode tbChlBankCode : chlBankCodes) {
				String bankTypeCityCodeKey=tbChlBankCode.getBankType()+tbChlBankCode.getCityCode();
				Map<String, TbChlBankCode> map = bankCodeMap.get(bankTypeCityCodeKey);
				if(map==null){
					map=new HashMap<String, TbChlBankCode>();
					bankCodeMap.put(bankTypeCityCodeKey, map);
				}
				map.put(tbChlBankCode.getBankName(), tbChlBankCode); 
			}
		}
		return bankCodeMap;
	}

	@Override
	public Map<String, TbChlBankCode> findChlBankMapGroupByBankType() {
		List<TbChlBankCode> chlBankCodes=tbChlBankCodeMapper.findChlBankMapGroupByBankType();
		HashMap<String, TbChlBankCode> bankCodeMap = new HashMap<String,TbChlBankCode>();
		if(chlBankCodes!=null && !chlBankCodes.isEmpty()){
			for (TbChlBankCode tbChlBankCode : chlBankCodes) {
				bankCodeMap.put(tbChlBankCode.getBankType(), tbChlBankCode);
			}
		}
		return bankCodeMap;
	}	
}
