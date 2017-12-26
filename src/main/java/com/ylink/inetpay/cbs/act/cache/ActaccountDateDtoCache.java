/**
 * 版权所有(C) 2016 深圳市雁联计算系统有限公司
 * 创建:lyg 2016-9-5
 */

package com.ylink.inetpay.cbs.act.cache;

import java.util.Date;

import com.ylink.inetpay.common.project.account.dto.ActaccountDateDto;

/** 
 * @author lyg
 * @date 2016-9-5
 * @description：TODO
 */

public interface ActaccountDateDtoCache {

	ActaccountDateDto selectByPrimaryKey(String id);
}
