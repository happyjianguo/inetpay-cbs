/**
 * 版权所有(C) 2016 深圳市雁联计算系统有限公司
 * 创建:lyg 2016-9-5
 */

package com.ylink.inetpay.cbs.chl.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.ylink.inetpay.cbs.chl.dao.TbChlFrontendBusiMappingMapper;
import com.ylink.inetpay.common.project.channel.dto.TbChlFrontendBusiMapping;


@Service("tbChlFrontendBusiMappDtoCache")
public class TbChlFrontendBusiMappDtoCacheImpl implements TbChlFrontendBusiMappDtoCache {

	@Autowired
	TbChlFrontendBusiMappingMapper tbChlFrontendBusiMappingMapper;



	@Override
	@Cacheable(value="mpay",key="'tbChlFrontendBusiMappDtoCache[key-' + #record.custType+'-'+#record.channelCode+'-'+#record.busiCode")
	public TbChlFrontendBusiMapping selectByParam(TbChlFrontendBusiMapping record) {
		return tbChlFrontendBusiMappingMapper.selectByParam(record);
	}
	
	
	
}
