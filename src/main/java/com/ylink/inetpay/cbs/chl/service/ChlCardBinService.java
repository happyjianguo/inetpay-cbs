package com.ylink.inetpay.cbs.chl.service;

import java.util.List;

import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.common.project.cbs.exception.CbsCheckedException;
import com.ylink.inetpay.common.project.channel.dto.TbChlBank;
import com.ylink.inetpay.common.project.channel.dto.TbChlCardBin;

/**
 * 
 * 银行卡Bin服务类
 * 
 * @author haha
 *
 */
public interface ChlCardBinService {
	/**
	 * 卡BIN列表（分页查询）
	 * 
	 * @param pageDate
	 * @param tbChlBankDto
	 * @return
	 * @throws CbsCheckedException
	 */
	public PageData<TbChlCardBin> findListPage(PageData<TbChlCardBin> pageDate,
			TbChlCardBin tbChlCardBin);

	public void deleteCardBin(String id); 
	
	public void updateCardBin(TbChlCardBin tbChlCardBin);
	
	public void addCardBin(TbChlCardBin tbChlCardBin) ;

	
	/**
	 * 卡BIN详情
	 * @param tbChlBankDto
	 * @throws CbsCheckedException
	 */
	public TbChlCardBin datailCardBin(String id);
	
	/**
	 * 根据卡不标识获取卡信息
	 * @param CardSign
	 * @return
	 */
    public List<TbChlCardBin> getCardBinByCardSign(String CardSign);
    
	
}
