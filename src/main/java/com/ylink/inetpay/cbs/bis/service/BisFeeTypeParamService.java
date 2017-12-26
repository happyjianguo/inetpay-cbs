package com.ylink.inetpay.cbs.bis.service;

import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.common.project.cbs.dto.bis.BisFeeTypeParamDto;

/**
 * 费用类型service
 * @类名称： BisAccessorService
 * @类描述： 
 * @创建人： yc
 * @创建时间： 2017年4月18日 下午2:19:23
 * 
 *
 */
public interface BisFeeTypeParamService {

	//查询费用类型
	PageData<BisFeeTypeParamDto> pageList(PageData<BisFeeTypeParamDto> pageData, BisFeeTypeParamDto queryParam);
	//保存费用类型
	void saveBisFeeTypeParam(BisFeeTypeParamDto bisFeeTypeParamDto);
	//根据id查询费用类型表
	BisFeeTypeParamDto details(String viewId);
	//费用类型修改
	void updateAccessorDto(BisFeeTypeParamDto feeTypeParamDto);

	 
		
	 
}

