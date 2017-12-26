package com.ylink.inetpay.cbs.bis.service;

import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.common.project.cbs.dto.bis.BisProductDto;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisProductTradeTypePojoDto;

/**
 * Created by pst25 on 2017/3/29.
 */
public interface BisProductService {

    BisProductDto findById(String id);
    /**
     * 分页获取产品列表
     * @param pageData
     * @param queryParam
     * @return
     */
	PageData<BisProductTradeTypePojoDto> pageList(PageData<BisProductTradeTypePojoDto> pageData,
			BisProductTradeTypePojoDto queryParam);
	/**
	 * 获取产品详情
	 * @param id
	 * @return
	 */
	BisProductTradeTypePojoDto findProductTradeById(String id,String settleCycleId);
	/**
	 * 保存产品
	 * @param bisProductDto
	 */
	void save(BisProductDto bisProductDto);
	/**
	 * 修改产品信息
	 * @param productDto
	 */
	void update(BisProductTradeTypePojoDto productDto);
	/**
	 * 根据产品code获取产品
	 * @param productCode
	 * @return
	 */
	BisProductDto findByProductCode(String productCode);
}
