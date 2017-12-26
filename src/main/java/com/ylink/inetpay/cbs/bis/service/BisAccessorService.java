package com.ylink.inetpay.cbs.bis.service;

import java.util.List;

import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.common.project.cbs.dto.bis.BisAccessorDto;

/**
 * 业务设置service
 * @类名称： BisAccessorService
 * @类描述： 
 * @创建人： yc
 * @创建时间： 2017年4月18日 下午2:19:23
 * 
 *
 */
public interface BisAccessorService {
		
	public PageData<BisAccessorDto> pageList(PageData<BisAccessorDto> pageData, BisAccessorDto queryParam);
	/**
	 * 获取接入方列表
	 * @return
	 */
	public List<BisAccessorDto> list();

	public void saveAccessor(BisAccessorDto queryParam);

	public BisAccessorDto details(String viewId);

	public void deleteBisAccessorDto(String viewId);

	public void updateAccessorDto(BisAccessorDto accessorDto);

	public List<String> queryBisAccessorDtoCustId();
}

