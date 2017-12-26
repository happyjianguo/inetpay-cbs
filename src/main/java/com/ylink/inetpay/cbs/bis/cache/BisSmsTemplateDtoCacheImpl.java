/**
 * 版权所有(C) 2016 深圳市雁联计算系统有限公司
 * 创建:lyg 2016-9-2
 */

package com.ylink.inetpay.cbs.bis.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.ylink.inetpay.cbs.bis.dao.BisSmsTemplateDtoMapper;
import com.ylink.inetpay.common.core.constant.EBisTemplateCode;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisSmsTemplateDto;

/** 
 * @author lyg
 * @date 2016-9-2
 * @description：TODO
 */

@Service("bisSmsTemplateDtoCache")
public class BisSmsTemplateDtoCacheImpl implements BisSmsTemplateDtoCache {

	@Autowired
	private BisSmsTemplateDtoMapper bisSmsTemplateDtoMapper;

	@Override
	@Cacheable(value="mpay",key="'bisSmsTemplateDtoCache[businessScenario-' + #templateCode.value")
	public BisSmsTemplateDto getSmsTempla(EBisTemplateCode templateCode) {
		// TODO Auto-generated method stub
		return bisSmsTemplateDtoMapper.getSmsTempla(templateCode);
	}

	@Override
	@CachePut(value="mpay",key="'bisSmsTemplateDtoCache[businessScenario-' + #record.businessScenario.value")
	public BisSmsTemplateDto updateBySmsTempla(BisSmsTemplateDto record) {
		bisSmsTemplateDtoMapper.updateByPrimaryKey(record);
		record=bisSmsTemplateDtoMapper.getSmsTempla(record.getBusinessScenario());
		return record;
	}
	
	
}
