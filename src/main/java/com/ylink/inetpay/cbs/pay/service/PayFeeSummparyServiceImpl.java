package com.ylink.inetpay.cbs.pay.service;
 
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.ylink.inetpay.cbs.pay.dao.PayFeeSummaryDtoMapper;
import com.ylink.inetpay.common.project.pay.dto.PayFeeSummaryDto;

 
@Service("payFeeSummparyService")
public class PayFeeSummparyServiceImpl implements PayFeeSummparyService {
	 @Autowired
	 private PayFeeSummaryDtoMapper payFeeSummaryDtoMapper;

	 @Override
	public PageData<PayFeeSummaryDto> pageList(PageData<PayFeeSummaryDto> pageData, PayFeeSummaryDto queryParam) {
		  
		PageHelper.startPage(pageData.getPageNumber(), pageData.getPageSize());
		List<PayFeeSummaryDto> items = payFeeSummaryDtoMapper.queryList(queryParam);
		Page<PayFeeSummaryDto> page = (Page<PayFeeSummaryDto> )items;
		pageData.setRows(items);
		pageData.setTotal(page.getTotal());
		return pageData;
	}

	@Override
	public PayFeeSummaryDto detail(String id) {
		 return payFeeSummaryDtoMapper.detail(id);
	} 
}
