package com.ylink.inetpay.cbs.pay.service;


import java.text.ParseException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.ylink.inetpay.cbs.pay.dao.PayAmtAllocateBatchDtoMapper;
import com.ylink.inetpay.common.core.util.DateUtils;
import com.ylink.inetpay.common.project.pay.dto.PayAmtAllocateBatchDto;


@Service("payAmtMerchantsService")
public class PayAmtMerchantsServiceImpl implements PayAmtMerchantsService {
	 @Autowired
	 private PayAmtAllocateBatchDtoMapper payAmtAllocateBatchDtoMapper;

	@Override
	public PageData<PayAmtAllocateBatchDto> queryAllDate(PageData<PayAmtAllocateBatchDto> pageData, PayAmtAllocateBatchDto queryParam) {
		PageHelper.startPage(pageData.getPageNumber(), pageData.getPageSize());
		List<PayAmtAllocateBatchDto> list = payAmtAllocateBatchDtoMapper.listAll(queryParam);
		if(list!=null&&!list.isEmpty()){
			try {
				for (PayAmtAllocateBatchDto dto : list) {
				String merOrder=DateUtils.stringToString(dto.getMerOrderDate());
					dto.setCreateDate(DateUtils.stringToDate(merOrder));
				}
			} catch (ParseException e) {
				 
				e.printStackTrace();
			}
		}
		Page<PayAmtAllocateBatchDto> page = (Page<PayAmtAllocateBatchDto>)list;
		pageData.setTotal(page.getTotal());
		pageData.setRows(list);
		return pageData;
	}
	 
}
