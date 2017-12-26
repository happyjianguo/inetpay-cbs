/**
 * 版权所有(C) 2016 深圳市雁联计算系统有限公司
 * 创建:lyg 2016-9-5
 */

package com.ylink.inetpay.cbs.chl.cache;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ylink.inetpay.common.project.channel.dto.TbChlBankAccount;


public interface TbChlBankAccountDtoCache {
	
	
	
	
	public TbChlBankAccount findByChannelCode(@Param("channelCode")String channelCode);
	
	
	public List<TbChlBankAccount> chlBankService(String goldBankType);
	
	public TbChlBankAccount findByBankAccNo(String outerAcct);
    
	
}
