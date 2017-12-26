/**
 * 版权所有(C) 2016 深圳市雁联计算系统有限公司
 * 创建:lyg 2016-9-5
 */

package com.ylink.inetpay.cbs.chl.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.ylink.inetpay.cbs.chl.dao.TbChlBankMapper;
import com.ylink.inetpay.common.project.channel.dto.TbChlBank;



@Service("tbChlBankDtoCache")
public class TbChlBankDtoCacheImpl implements TbChlBankDtoCache {

	@Autowired
	TbChlBankMapper tbChlBankMapper;



	@Override
	@Cacheable(value="mpay",key="'cbsTbChlBankDtoCache[key-' + #bankType")
	public TbChlBank getBankByBankType(String bankType) {
		return tbChlBankMapper.getBankByBankType(bankType);
	}
	
}
