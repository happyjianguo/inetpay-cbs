package com.ylink.inetpay.cbs.pay.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.ylink.inetpay.cbs.pay.dao.PayAllDtoMapper;
import com.ylink.inetpay.common.project.pay.dto.PayAllDto;
@Service("payAllService")
public class PayAllServiceImpl implements PayAllService {
	@Autowired
	private PayAllDtoMapper payAllDtoMapper;
	@Override
	public PageData<PayAllDto> queryDate(
			PageData<PayAllDto> pageData, PayAllDto queryparam) {
		PageHelper.startPage(pageData.getPageNumber(),pageData.getPageSize());
		List<PayAllDto> list=payAllDtoMapper.queryAllData(queryparam);
		Page<PayAllDto> page=(Page<PayAllDto>) list;
		pageData.setTotal(page.getTotal());
		pageData.setRows(list);
		return pageData;
	}
}
