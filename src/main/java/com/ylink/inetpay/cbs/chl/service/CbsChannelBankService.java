package com.ylink.inetpay.cbs.chl.service;

import java.util.List;

import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.common.project.cbs.exception.CbsCheckedException;
import com.ylink.inetpay.common.project.channel.dto.TbChlChannelBank;
/**
 * 银行信息服务类
 * @author haha
 *
 */
public interface CbsChannelBankService {
	/**
	 * 银行列表（分页查询）
	 * @param pageDate
	 * @param tbChlBankDto
	 * @return
	 * @throws CbsCheckedException
	 */
	public PageData<TbChlChannelBank> findListPage(PageData<TbChlChannelBank> pageDate,TbChlChannelBank tbChlBankDto);
	
	public TbChlChannelBank details(String id) ;
	
	public List<TbChlChannelBank> findListPage(TbChlChannelBank tbChlBankDto);

	public TbChlChannelBank findById(String id);
}
