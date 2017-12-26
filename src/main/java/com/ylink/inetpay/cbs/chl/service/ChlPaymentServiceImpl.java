package com.ylink.inetpay.cbs.chl.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.ylink.inetpay.cbs.chl.dao.ChlOrderMapper;
import com.ylink.inetpay.common.project.channel.dto.TbChlPayOrderDto;
@Service("chlPaymentService")
public class ChlPaymentServiceImpl implements ChlPaymentService {

	@Autowired
	private ChlOrderMapper chlOrderMapper;
	
	/**查看详情**/
	@Override
	public TbChlPayOrderDto details(String id) {
		return chlOrderMapper.selectByPrimaryKey(id);
	}

	/**分页查询渠道订单信息 **/
	@Override
	public PageData<TbChlPayOrderDto> findListPage(PageData<TbChlPayOrderDto> pageDate, TbChlPayOrderDto payOrderDto){
		PageHelper.startPage(pageDate.getPageNumber(),pageDate.getPageSize());
		List<TbChlPayOrderDto> findListPage=chlOrderMapper.findListPage(payOrderDto);
		Page<TbChlPayOrderDto> page=(Page<TbChlPayOrderDto>) findListPage;
		pageDate.setTotal(page.getTotal());
		pageDate.setRows(findListPage);
		return pageDate;
	}
	

}
