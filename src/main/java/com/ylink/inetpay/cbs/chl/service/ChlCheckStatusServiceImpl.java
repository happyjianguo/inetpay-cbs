package com.ylink.inetpay.cbs.chl.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.ylink.inetpay.cbs.chl.dao.ChlCheckfileRecordMapper;
import com.ylink.inetpay.common.project.channel.dto.TbChlCheckfileRecordDto;
import com.ylink.inetpay.common.project.clear.dto.ClsBankCheck;

@Service("chlCheckStatusService")
public class ChlCheckStatusServiceImpl implements ChlCheckStatusService{

	@Autowired
	private ChlCheckfileRecordMapper checkfileRecordMapper;
	
	/**
	 * @方法描述: 根据id查找对象
	 * @作者： 1603254
	 * @日期： 2016-5-30-上午10:00:31
	 * @param id
	 * @return 
	 * @返回类型： TbChlCheckfileRecordDto
	*/
	public TbChlCheckfileRecordDto findById(String id){
		return checkfileRecordMapper.queryById(id);
	}
	
	
	/**
	 * @方法描述:  查询资金渠道对账 文件的处理状态
	 * @作者： 1603254
	 * @日期： 2016-5-27-下午2:54:25
	 * @param pageData
	 * @param check
	 * @return 
	 * @返回类型： ClsBankCheck
	*/
	public PageData<TbChlCheckfileRecordDto> findBankCheck(PageData<TbChlCheckfileRecordDto> pageData,
			TbChlCheckfileRecordDto check){
		PageHelper.startPage(pageData.getPageNumber(),pageData.getPageSize());
		List<TbChlCheckfileRecordDto> list=checkfileRecordMapper.queryChlCheckfileRecord(check);
		Page<TbChlCheckfileRecordDto> page=(Page<TbChlCheckfileRecordDto>) list;
		pageData.setTotal(page.getTotal());
		pageData.setRows(list);
		return pageData;
	}
	
	
	/**
	 * @方法描述: 修改状态
	 * @作者： 1603254
	 * @日期： 2016-5-30-上午10:29:03
	 * @param check 
	 * @返回类型： void
	*/
	public void changeStatus(TbChlCheckfileRecordDto check){
		checkfileRecordMapper.changeStatus(check);
	}
}
