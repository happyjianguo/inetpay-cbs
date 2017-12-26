/**
 * 版权所有(C) 2016 深圳市雁联计算系统有限公司
 * 创建:lyg 2016-9-2
 */

package com.ylink.inetpay.cbs.bis.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.ylink.inetpay.cbs.bis.dao.BisEmailTemplateDtoMapper;
import com.ylink.inetpay.common.core.constant.EBisEmailTemplateCode;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisEmailTemplateDto;

/** 
 * @author lyg
 * @date 2016-9-2
 * @description：TODO
 */

@Service("bisEmailTemplateDtoCache")
public class BisEmailTemplateDtoCacheImpl implements BisEmailTemplateDtoCache {

	@Autowired
	private BisEmailTemplateDtoMapper bisEmailTemplateDtoMapper;
	@Override
	@CachePut(value="mpay",key="'bisEmailTemplateDtoCache[businessScenario-' + #record.businessScenario.value")
	public BisEmailTemplateDto updateByEmailTempla(BisEmailTemplateDto record) {
		// TODO Auto-generated method stub
		bisEmailTemplateDtoMapper.updateByPrimaryKey(record);
		record=bisEmailTemplateDtoMapper.getEmailTempla(record.getBusinessScenario());
		return record;
	}

	@Override
	@Cacheable(value="mpay",key="'bisEmailTemplateDtoCache[businessScenario-' + #templateCode.value")
	public BisEmailTemplateDto getEmailTempla(EBisEmailTemplateCode templateCode) {
		// TODO Auto-generated method stub
		return bisEmailTemplateDtoMapper.getEmailTempla(templateCode);
	}

}
