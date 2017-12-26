package com.ylink.inetpay.cbs.chl.service;

import java.util.List;

import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.common.project.cbs.exception.CbsCheckedException;
import com.ylink.inetpay.common.project.channel.dto.TbChlAccountChangeNotice;
import com.ylink.inetpay.common.project.channel.dto.TbChlChannelBank;
/**
 * 动帐通知服务类
 * @author haha
 *
 */
public interface ChlAccountChangeNotice {
	/**
	 * 动帐通知列表
	 * @param pageDate
	 * @param tbChlBankDto
	 * @return
	 * @throws CbsCheckedException
	 */
	public PageData<TbChlAccountChangeNotice> findAll(PageData<TbChlAccountChangeNotice> pageData,TbChlAccountChangeNotice param);
	/**
	 * 查询动帐通知详情
	 */
	public TbChlAccountChangeNotice detail(String id);
}
