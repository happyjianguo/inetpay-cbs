/**
 * 版权所有(C) 2013 深圳市雁联计算系统有限公司
 * 创建:LS 2013-8-27
 */

package com.ylink.inetpay.cbs.bis.service;

import java.util.List;

import com.ylink.inetpay.common.project.cbs.dto.bis.BisSchedJobIntervalDto;


/**
 * @author LS
 * @date 2013-8-27
 * @description：触发器数据服务层
 */

public interface BisSchedJobIntervalService {

	BisSchedJobIntervalDto get(String triggerName);

	void save(BisSchedJobIntervalDto jobTrigger);

	void update(BisSchedJobIntervalDto jobTrigger);

	void delete(String triggerName);

	List<BisSchedJobIntervalDto> listAll();
}
