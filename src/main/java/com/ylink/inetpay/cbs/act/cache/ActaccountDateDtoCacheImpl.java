/**
 * 版权所有(C) 2016 深圳市雁联计算系统有限公司
 * 创建:lyg 2016-9-5
 */

package com.ylink.inetpay.cbs.act.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.ylink.inetpay.cbs.act.dao.ActaccountDateDtoMapper;
import com.ylink.inetpay.common.project.account.dto.ActaccountDateDto;

/** 
 * @author lyg
 * @date 2016-9-5
 * @description：TODO
 */
@Service("actaccountDateDtoCache")
public class ActaccountDateDtoCacheImpl  implements ActaccountDateDtoCache{

	@Autowired
	ActaccountDateDtoMapper actaccountDateDtoMapper;

	@Override
	@Cacheable(value="mpay",key="'actaccountDateDtoCache[id-'+#id")
	public ActaccountDateDto selectByPrimaryKey(String id) {
		
		return actaccountDateDtoMapper.selectByPrimaryKey(id);
	}
	

}
