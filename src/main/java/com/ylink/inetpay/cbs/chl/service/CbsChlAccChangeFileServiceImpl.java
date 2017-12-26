package com.ylink.inetpay.cbs.chl.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.ylink.inetpay.cbs.chl.dao.TbChlAccChangeFileRecordMapper;
import com.ylink.inetpay.common.project.channel.dto.TbChlAccChangeFileRecord;

@Service("cbsChlAccChangeFileService")
public class CbsChlAccChangeFileServiceImpl implements CbsChlAccChangeFileService {
	@Autowired
	TbChlAccChangeFileRecordMapper TbChlAccChangeFileRecordMapper;

	@Override
	public PageData<TbChlAccChangeFileRecord> queryAllData(
			PageData<TbChlAccChangeFileRecord> pageDate, TbChlAccChangeFileRecord TbChlAccChangeFileRecord) {

		PageHelper.startPage(pageDate.getPageNumber(), pageDate.getPageSize());
		List<TbChlAccChangeFileRecord> list = TbChlAccChangeFileRecordMapper
				.queryAllData(TbChlAccChangeFileRecord);
		Page<TbChlAccChangeFileRecord> page = (Page<TbChlAccChangeFileRecord>) list;
		pageDate.setTotal(page.getTotal());
		pageDate.setRows(list);
		return pageDate;
	}

	@Override
	public TbChlAccChangeFileRecord selectByBusiId(String id) {
		return TbChlAccChangeFileRecordMapper.selectByPrimaryKey(id);
	}

	

}
