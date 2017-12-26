package com.ylink.inetpay.cbs.chl.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.exception.BusinessRuntimeException;
import org.ylinkpay.framework.core.model.PageData;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.ylink.inetpay.cbs.chl.cache.TbChlChannelDtoCache;
import com.ylink.inetpay.cbs.chl.dao.TbChlChannelMapper;
import com.ylink.inetpay.cbs.ucs.sec.service.UcsSecUserServiceImpl;
import com.ylink.inetpay.common.core.constant.EChlChannelCode;
import com.ylink.inetpay.common.core.constant.EChlOrCheckFile;
import com.ylink.inetpay.common.project.cbs.exception.CbsCheckedException;
import com.ylink.inetpay.common.project.channel.app.ChlChannelAppService;
import com.ylink.inetpay.common.project.channel.dto.TbChlChannel;
import com.ylink.inetpay.common.project.channel.dto.TbChlPayStyle;
import com.ylink.inetpay.common.project.channel.exception.ChannelCheckedException;
@Service("chlChannelService")
public class ChlChannelServiceImpl implements ChlChannelService {
	@Autowired
//	private TbChlChannelMapper ChlChannelMapper;
	private TbChlChannelMapper tbChlChannelMapper;
	@Autowired
	private ChlChannelAppService chlChannelApp;
	@Autowired
	private TbChlChannelDtoCache tbChlChannelDtoCache;

	private static Logger _log = LoggerFactory.getLogger(UcsSecUserServiceImpl.class);
	@Override
	public PageData<TbChlChannel> findListPage(
			PageData<TbChlChannel> pageDate, TbChlChannel tbChlChannelDto) {
		PageHelper.startPage(pageDate.getPageNumber(),pageDate.getPageSize());
		List<TbChlChannel> findListPage=tbChlChannelMapper.findListPage(tbChlChannelDto);
		Page<TbChlChannel> page=(Page<TbChlChannel>) findListPage;
		pageDate.setTotal(page.getTotal());
		pageDate.setRows(findListPage);
		return pageDate;
	}
	
	/**
	 * 获取支付方式
	 * @return
	 * @throws CbsCheckedException
	 */
	@Override
	public List<TbChlPayStyle> getPayStyle(){
		return tbChlChannelDtoCache.getPayStyle();
	}

	@Override
	public TbChlChannel getChannelByCode(String channelCode){
		return tbChlChannelDtoCache.selectByChannelCode(EChlChannelCode.getEChlChannelCode(channelCode));
	}
	
	@Override
	public List<TbChlPayStyle> getMobileFastStyle(){
		return tbChlChannelDtoCache.getMobileFastStyle();
	}
	@Override
	public TbChlChannel details(String id) {
		return tbChlChannelMapper.selectByPrimaryKey(id);
	}
	
	@Override
	public List<TbChlChannel> getChannels() {
		return tbChlChannelDtoCache.getChannels(EChlOrCheckFile.JOIN_BANK);
	}
	
	@Override
	public void updateChannel(TbChlChannel record){
		try {
			chlChannelApp.updateByPrimaryKeySelective(record);
		} catch (ChannelCheckedException e) {
			throw new BusinessRuntimeException("更新渠道信息出错");
		}
	}

}
