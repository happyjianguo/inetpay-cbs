package com.ylink.inetpay.cbs.chl.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.ylink.inetpay.cbs.chl.dao.TbChlTransferOrderMapper;
import com.ylink.inetpay.common.project.channel.dto.TbChlTransferOrder;
@Service("chlReplaceService")
public class ChlReplaceServiceImpl implements ChlReplaceService {

	@Autowired
	private TbChlTransferOrderMapper transferOrderMapper;
	
	/**根据ID获取代付订单信息**/
	@Override
	public TbChlTransferOrder details(String id) {
		return transferOrderMapper.selectByPrimaryKey(id);
	}

	/**分页查看代付订单信息**/
	@Override
	public PageData<TbChlTransferOrder> findListPage(PageData<TbChlTransferOrder> pageDate,
			TbChlTransferOrder tbDirectPayDto) {
		PageHelper.startPage(pageDate.getPageNumber(),pageDate.getPageSize());
		List<TbChlTransferOrder> findListPage=transferOrderMapper.findListPage(tbDirectPayDto);
		Page<TbChlTransferOrder> page=(Page<TbChlTransferOrder>) findListPage;
		pageDate.setTotal(page.getTotal());
		pageDate.setRows(findListPage);
		return pageDate;
	}
	

}
