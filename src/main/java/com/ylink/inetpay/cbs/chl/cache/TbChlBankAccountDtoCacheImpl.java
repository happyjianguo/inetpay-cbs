/**
 * 版权所有(C) 2016 深圳市雁联计算系统有限公司
 * 创建:lyg 2016-9-5
 */

package com.ylink.inetpay.cbs.chl.cache;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.ylink.inetpay.cbs.chl.dao.TbChlBankAccountMapper;
import com.ylink.inetpay.common.project.channel.dto.TbChlBankAccount;



@Service("tbChlBankAccountDtoCache")
public class TbChlBankAccountDtoCacheImpl implements TbChlBankAccountDtoCache {

	@Autowired
	TbChlBankAccountMapper tbChlBankAccountMapper;



	@Override
	@Cacheable(value="mpay",key="'cbsTbChlBankAccountDtoCache[key-' + #channelCode")
	public TbChlBankAccount findByChannelCode(String channelCode) {
		return tbChlBankAccountMapper.findByChannelCode(channelCode);
	}
	
	@Override
	@Cacheable(value="mpay",key="'cbsTbChlBankAccountDtoCache[key-' + #goldBankType")
	public List<TbChlBankAccount> chlBankService(String goldBankType){
		return tbChlBankAccountMapper.chlBankService(goldBankType);
	}
	
	@Override
	@Cacheable(value="mpay",key="'cbsTbChlBankAccountDtoCache[key-'+ #outerAcct")
	public TbChlBankAccount findByBankAccNo(String outerAcct) {
		return tbChlBankAccountMapper.findByBankAccNo(outerAcct);
	}
	
	
	
}
