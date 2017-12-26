package com.ylink.inetpay.cbs.chl.service;

import java.util.List;

import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.common.project.channel.dto.TbChlBindBankCard;
/**
 * 用户银行卡信息服务类
 * @author haha
 *
 */
public interface ChlBindBankCardService {

	
	
	/**
	 * 根据用户银行信息查找
	 * @param bankProp
	 * @return
	 */
	public List<TbChlBindBankCard> findByChlBankProp(TbChlBindBankCard userBankDto);
	
	public TbChlBindBankCard findById(String id) ;

	public PageData<TbChlBindBankCard> pageData(PageData<TbChlBindBankCard> pageData, TbChlBindBankCard searchDto);

	/**
	 * 关联信息查询-包括银行名称
	 * @param searchDto
	 * @return
	 */
	public List<TbChlBindBankCard> listBankCard(TbChlBindBankCard searchDto);
	

}
