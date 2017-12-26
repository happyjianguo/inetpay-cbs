package com.ylink.inetpay.cbs.chl.service;

import java.util.List;

import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.common.project.cbs.exception.CbsCheckedException;
import com.ylink.inetpay.common.project.channel.dto.TbChlChannel;
import com.ylink.inetpay.common.project.channel.dto.TbChlPayStyle;

/**
 * 渠道信息服务类
 * @author haha
 *
 */
public interface ChlChannelService {
	/**
	 * 渠道列表（分页查询）
	 * @param pageDate
	 * @param tbChlBankDto
	 * @return
	 * @throws CbsCheckedException
	 */
	public PageData<TbChlChannel> findListPage(PageData<TbChlChannel> pageDate,TbChlChannel tbChlChannelDto);
	/**
	 * 渠道详情
	 * @param id
	 * @return
	 * @throws CbsCheckedException
	 */
	public TbChlChannel details(String id);
	/**根据渠道编码获取渠道信息 **/
	public TbChlChannel getChannelByCode(String channelCode);
	
	
	/**
	 * 查询所有需要下载对账文件的渠道信息（有对账文件）
	 * @return
	 * @throws CbsCheckedException
	 */
	public List<TbChlChannel> getChannels();
	
	/**
	 * 获取支付方式
	 * @return
	 * @throws CbsCheckedException
	 */
	public List<TbChlPayStyle> getPayStyle();
	/**获取手机快捷 **/
	public List<TbChlPayStyle> getMobileFastStyle();
	
	public void updateChannel(TbChlChannel record);
	

	
}
