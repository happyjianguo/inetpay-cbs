package com.ylink.inetpay.cbs.pay.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.ylink.inetpay.cbs.pay.dao.PayTransferDtoMapper;
import com.ylink.inetpay.common.core.dto.ReporHeadDto;
import com.ylink.inetpay.common.project.pay.dto.PayTransferDto;

@Service("payTransferService")
public class PayTransferServiceImpl implements PayTransferService {
	@Autowired
	PayTransferDtoMapper payTransferDtoMapper;

	@Override
	public PageData<PayTransferDto> queryAllData(
			PageData<PayTransferDto> pageDate, PayTransferDto payTransferDto) {

		PageHelper.startPage(pageDate.getPageNumber(), pageDate.getPageSize());
		List<PayTransferDto> list = payTransferDtoMapper
				.queryAllData(payTransferDto);
		Page<PayTransferDto> page = (Page<PayTransferDto>) list;
		pageDate.setTotal(page.getTotal());
		pageDate.setRows(list);
		return pageDate;
	}

	@Override
	public PayTransferDto selectByBusiId(String busiId) {
		return payTransferDtoMapper.selectByBusiId(busiId);
	}
	@Override
	public ReporHeadDto reportSumData(PayTransferDto payTransferDto) {
		return payTransferDtoMapper.reportSumData(payTransferDto);
	}
}
