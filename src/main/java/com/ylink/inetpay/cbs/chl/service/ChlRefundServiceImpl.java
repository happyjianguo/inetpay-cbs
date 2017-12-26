package com.ylink.inetpay.cbs.chl.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.ylink.inetpay.cbs.chl.dao.ChlRefundOrderMapper;
import com.ylink.inetpay.common.project.channel.dto.TbChlRefundOrderDto;
@Service("chlRefundService")
public class ChlRefundServiceImpl implements ChlRefundService {

	@Autowired
	private ChlRefundOrderMapper refundOderMapper;
	
	/**查看退款订单明细**/
	@Override
	public TbChlRefundOrderDto details(String id) {
		return refundOderMapper.selectByPrimaryKey(id);
	}

	/**分页查询退款信息**/
	@Override
	public PageData<TbChlRefundOrderDto> findListPage(PageData<TbChlRefundOrderDto> pageDate,
			TbChlRefundOrderDto refundOrderDto) {
		PageHelper.startPage(pageDate.getPageNumber(),pageDate.getPageSize());
		List<TbChlRefundOrderDto> findListPage=refundOderMapper.findListPage(refundOrderDto);
		Page<TbChlRefundOrderDto> page=(Page<TbChlRefundOrderDto>) findListPage;
		pageDate.setTotal(page.getTotal());
		pageDate.setRows(findListPage);
		return pageDate;
	}
	
}
