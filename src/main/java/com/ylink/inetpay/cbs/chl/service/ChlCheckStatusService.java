package com.ylink.inetpay.cbs.chl.service;

import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.common.project.channel.dto.TbChlCheckfileRecordDto;

public interface ChlCheckStatusService {

	
	/**
	 * @方法描述: 根据id查找对象
	 * @作者： 1603254
	 * @日期： 2016-5-30-上午10:00:31
	 * @param id
	 * @return 
	 * @返回类型： TbChlCheckfileRecordDto
	*/
	public TbChlCheckfileRecordDto findById(String id);
	
	
	
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
			TbChlCheckfileRecordDto check);
	
	/**
	 * @方法描述: 修改状态
	 * @作者： 1603254
	 * @日期： 2016-5-30-上午10:29:03
	 * @param check 
	 * @返回类型： void
  	*/
	public void changeStatus(TbChlCheckfileRecordDto check);
}
