/**
 * 版权所有(C) 2013 深圳市雁联计算系统有限公司
 * 创建:LS 2013-8-27
 */

package com.ylink.inetpay.cbs.bis.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ylink.inetpay.cbs.bis.dao.BisSchedJobIntervalDtoMapper;
import com.ylink.inetpay.cbs.common.CbsConstants;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisSchedJobIntervalDto;

/**
 * @author LS
 * @date 2013-8-27
 * @description：实现类
 */

@Service("bisSchedJobIntervalService")
@Transactional(value=CbsConstants.TX_MANAGER_BIS)
public class BisSchedJobIntervalServiceImpl implements BisSchedJobIntervalService {

	@Autowired
	private BisSchedJobIntervalDtoMapper bisSchedJobIntervalDtoMapper;

	@Override
	public BisSchedJobIntervalDto get(String triggerName) {
		return bisSchedJobIntervalDtoMapper.selectByPrimaryKey(triggerName);
	}

	@Override
	public void save(BisSchedJobIntervalDto jobTrigger) {
		bisSchedJobIntervalDtoMapper.insert(jobTrigger);
	}

	@Override
	public void update(BisSchedJobIntervalDto jobTrigger) {
		bisSchedJobIntervalDtoMapper.updateByPrimaryKeySelective(jobTrigger);
	}

	@Override
	public void delete(String triggerName) {
		bisSchedJobIntervalDtoMapper.deleteByPrimaryKey(triggerName);
	}

	@Override
	public List<BisSchedJobIntervalDto> listAll() {
		return bisSchedJobIntervalDtoMapper.listAll();
	}

}
