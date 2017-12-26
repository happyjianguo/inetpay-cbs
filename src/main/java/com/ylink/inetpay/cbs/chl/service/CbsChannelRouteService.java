package com.ylink.inetpay.cbs.chl.service;

import java.util.List;

import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.common.project.cbs.exception.CbsCheckedException;
import com.ylink.inetpay.common.project.channel.dto.TbChlChannelRoute;;
/**
 * 银行信息服务类
 * @author haha
 *
 */
public interface CbsChannelRouteService {
	/**
	 * 银行列表（分页查询）
	 * @param pageDate
	 * @param tbChlBankDto
	 * @return
	 * @throws CbsCheckedException
	 */
	public PageData<TbChlChannelRoute> findListPage(PageData<TbChlChannelRoute> pageDate,TbChlChannelRoute tbChlBankDto);
	
	public TbChlChannelRoute details(String id) ;
	
	public List<TbChlChannelRoute> findListPage(TbChlChannelRoute tbChlBankDto);
    
	public List<TbChlChannelRoute> findRouteByPayOreder(Long payOrder);
}
