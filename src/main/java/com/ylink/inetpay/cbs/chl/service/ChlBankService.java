package com.ylink.inetpay.cbs.chl.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.common.project.cbs.exception.CbsCheckedException;
import com.ylink.inetpay.common.project.channel.dto.TbChlBank;
/**
 * 银行信息服务类
 * @author haha
 *
 */
public interface ChlBankService {
	/**
	 * 银行列表（分页查询）
	 * @param pageDate
	 * @param tbChlBankDto
	 * @return
	 * @throws CbsCheckedException
	 */
	public PageData<TbChlBank> findListPage(PageData<TbChlBank> pageDate,TbChlBank tbChlBankDto);
	
	/**
	 * 修改银行卡
	 * @param tbChlBankDto
	 */
	public TbChlBank details(String id) ;
	
	public List<TbChlBank> findListPage(TbChlBank tbChlBankDto);
	/**
	 * 获取银行信息列表
	 * @return
	 */
	public List<TbChlBank> getBanks();
	
	public List<TbChlBank> getList(TbChlBank tbChlBankDto);
	
	public TbChlBank getBankByBankType(String bankType);
	
	/**
     * 获取账户表中的银行名称
     * @return
     */
    List<TbChlBank> getBankType();
    /**
     * 获取系统支持的银行列表
     * @param pageDate
     * @param tbChlBankDto
     * @return
     */
	PageData<TbChlBank> getInnerBanks(PageData<TbChlBank> pageDate, TbChlBank tbChlBankDto);
	/**
	 * 根据bankType列表获取银行列表
	 * @param bankTypes
	 * @return
	 */
	public List<TbChlBank> getBankByBankTypes(ArrayList<String> bankTypes);
	/**
	 * 获取所有渠道信息
	 * @return
	 */
	public List<TbChlBank> getChannels();
	
	/**
	 * 根据渠道类型查找银行列表
	 */
	List<TbChlBank> findListByChannelType(String channelType);
	/**
	 * 获取渠道银行信息集合
	 * @return
	 */
	public Map<String, TbChlBank> findChannelBankMap();
}
