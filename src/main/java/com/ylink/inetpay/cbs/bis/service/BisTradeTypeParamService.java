package com.ylink.inetpay.cbs.bis.service;

import java.util.List;

import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.common.project.cbs.dto.bis.BisTradeTypeParamDto;

/**
 * 结算交易类型
 * @类名称： BisTradeTypeParamService
 * @类描述： 
 * @创建人： yc
 * @创建时间： 2017年4月18日 下午2:19:23
 * 
 *
 */
public interface BisTradeTypeParamService {

	PageData<BisTradeTypeParamDto> pageList(PageData<BisTradeTypeParamDto> pageData, BisTradeTypeParamDto queryParam);

	void insertBisTradeTypeParam(BisTradeTypeParamDto bisTradeTypeParamDto);

	BisTradeTypeParamDto detail(String viewId);

	void update(BisTradeTypeParamDto bisTradeTypeParamDto);
	/**
	 * 获取所有的交易类型
	 * @return
	 */
	List<BisTradeTypeParamDto> findAllTradeType();

 
	 
}

