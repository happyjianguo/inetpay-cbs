package com.ylink.inetpay.cbs.chl.service;

import java.util.List;

import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.common.project.cbs.exception.CbsCheckedException;
import com.ylink.inetpay.common.project.channel.dto.TbChlParamDto;

/**
 * 渠道参数信息服务类
 * @author haha
 *
 */
public interface ChlParamService {
	/**
	 * 渠道参数列表（分页查询）
	 * @param pageDate
	 * @param chlParamDto
	 * @return
	 * @throws CbsCheckedException
	 */
	public PageData<TbChlParamDto> findListPage(PageData<TbChlParamDto> pageDate,TbChlParamDto chlParamDto);
	
	/**
	 * 根据渠道参数id获取渠道参数详情
	 * @param channelParamId
	 * @return
	 */
	public TbChlParamDto details(String channelParamId);
	
	public TbChlParamDto selectByChannelCodeAndParam(String channelCode,String paramCode);
	/**
	 * 修改渠道信息参数
	 * @param tbChlParamDto
	 */
	public void updateChannelParam(TbChlParamDto tbChlParamDto);
	
	public List<TbChlParamDto> listByChannelCodeAndParam(List<String> channelCodeList, List<String> paramCodeList);
}
