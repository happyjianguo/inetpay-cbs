package com.ylink.inetpay.cbs.pay.service;

import org.ylinkpay.framework.core.model.PageData;
import com.ylink.inetpay.common.project.pay.dto.PayBwlistDto;
/**
 * 黑白名单服务类
 * @author haha
 *
 */
public interface PayBwlistService {
	/**
	 * 分页查询黑白名单
	 * @param pageDate
	 * @param payBwlistDto
	 * @return
	 */
	public PageData<PayBwlistDto> findListPage(PageData<PayBwlistDto> pageDate,PayBwlistDto payBwlistDto);
	/**
	 * 详情
	 * @param id
	 * @return
	 */
	public PayBwlistDto details(String id);
	/**
	 * 新增黑白名单
	 * @param payBwlistDto
	 */
	public void add(PayBwlistDto payBwlistDto);
	/**
	 * 删除黑白名单
	 * @param id
	 */
	public void delete(String id,String custId);
}
