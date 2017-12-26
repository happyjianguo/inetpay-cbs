package com.ylink.inetpay.cbs.chl.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.ylink.inetpay.cbs.chl.cache.TbChlBankDtoCache;
import com.ylink.inetpay.cbs.chl.dao.TbChlBankMapper;
import com.ylink.inetpay.cbs.ucs.sec.service.UcsSecUserServiceImpl;
import com.ylink.inetpay.common.project.channel.dto.TbChlBank;

@Service("chlBankService")
public class ChlBankServiceImpl implements ChlBankService {
	@Autowired
	private TbChlBankMapper chlBankMapper;
	@Autowired
	private TbChlBankDtoCache tbChlBankDtoCache;

	private static Logger _log = LoggerFactory.getLogger(UcsSecUserServiceImpl.class);

	@Override
	public PageData<TbChlBank> findListPage(PageData<TbChlBank> pageDate, TbChlBank tbChlBankDto) {
		PageHelper.startPage(pageDate.getPageNumber(), pageDate.getPageSize());
		List<TbChlBank> findListPage = chlBankMapper.findListPage(tbChlBankDto);
		Page<TbChlBank> page = (Page<TbChlBank>) findListPage;
		pageDate.setTotal(page.getTotal());
		pageDate.setRows(findListPage);
		return pageDate;
	}
	
	@Override
	public PageData<TbChlBank> getInnerBanks(PageData<TbChlBank> pageDate, TbChlBank tbChlBankDto) {
		PageHelper.startPage(pageDate.getPageNumber(), pageDate.getPageSize());
		List<TbChlBank> findListPage = chlBankMapper.getInnerBanks(tbChlBankDto);
		Page<TbChlBank> page = (Page<TbChlBank>) findListPage;
		pageDate.setTotal(page.getTotal());
		pageDate.setRows(findListPage);
		return pageDate;
	}

	@Override
	public TbChlBank details(String id) {
		return chlBankMapper.selectByPrimaryKey(id);
	}

	@Override
	public List<TbChlBank> findListPage(TbChlBank tbChlBankDto) {
		return chlBankMapper.findListPage(tbChlBankDto);
	}

	@Override
	public List<TbChlBank> getBanks() {
		List<TbChlBank> banks = chlBankMapper.getBanks();
		//过滤掉支付宝和微信
		List<TbChlBank> newBanks = new ArrayList<TbChlBank>();
		String bankType = null;
		for (int i = 0; i < banks.size(); i++) {
			bankType = banks.get(i).getBankType();
			if(StringUtils.isNotEmpty(bankType)&&!bankType.startsWith("00")){
				newBanks.add(banks.get(i));
			}
		}
		return newBanks;
	}
	@Override
	public List<TbChlBank> getList(TbChlBank tbChlBankDto) {
		return chlBankMapper.getList(tbChlBankDto);
	}

	@Override
	public List<TbChlBank> getBankType() {
		return chlBankMapper.getBankType();
	}

	@Override
	public TbChlBank getBankByBankType(String bankType) {
		return tbChlBankDtoCache.getBankByBankType(bankType);
	}

	@Override
	public List<TbChlBank> getBankByBankTypes(ArrayList<String> bankTypes) {
		return chlBankMapper.findByBankTypes(bankTypes);
	}

	@Override
	public List<TbChlBank> getChannels() {
		return chlBankMapper.getChannels();
	}

	@Override
	public List<TbChlBank> findListByChannelType(String channelType) {
		return chlBankMapper.findListByChannelType(channelType);
	}

	@Override
	public Map<String, TbChlBank> findChannelBankMap() {
		List<TbChlBank> bankList = chlBankMapper.getBanks();
		HashMap<String, TbChlBank> bankMap = new HashMap<String,TbChlBank>();
		if(bankList!=null && !bankList.isEmpty()){
			for (TbChlBank tbChlBank : bankList) {
				bankMap.put(tbChlBank.getBankName(), tbChlBank);
			}
		}
		return bankMap;
	}

}
