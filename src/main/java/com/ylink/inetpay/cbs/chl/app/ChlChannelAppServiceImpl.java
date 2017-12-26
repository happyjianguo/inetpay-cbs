package com.ylink.inetpay.cbs.chl.app;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.cbs.chl.service.ChlChannelService;
import com.ylink.inetpay.common.project.cbs.app.ChlChannelAppService;
import com.ylink.inetpay.common.project.cbs.exception.CbsCheckedException;
import com.ylink.inetpay.common.project.channel.dto.TbChlChannel;
import com.ylink.inetpay.common.project.channel.dto.TbChlPayStyle;
@Service("chlChannelAppService")
public class ChlChannelAppServiceImpl implements ChlChannelAppService {
	@Autowired
	private ChlChannelService chlChannelService;
	@Override
	public TbChlChannel details(String id) throws CbsCheckedException {
		return chlChannelService.details(id);
	}

	@Override
	public TbChlChannel getChannelByCode(String channelCode){
		return chlChannelService.getChannelByCode(channelCode);
	}
	
	@Override
	public PageData<TbChlChannel> findListPage(
			PageData<TbChlChannel> pageDate, TbChlChannel tbChlChannelDto)
			throws CbsCheckedException {
		return chlChannelService.findListPage(pageDate, tbChlChannelDto);
	}

	@Override
	public List<TbChlPayStyle> getPayStyle() throws CbsCheckedException{
		return chlChannelService.getPayStyle();
	}
	
	@Override
	public List<TbChlPayStyle> getMobileFastStyle() throws CbsCheckedException{
		return chlChannelService.getMobileFastStyle();
	}
	
	@Override
	public List<TbChlChannel> getChannels() throws CbsCheckedException {
		return chlChannelService.getChannels();
	}

	/**调用渠道进行更新 **/
	@Override
	public void updateChannel(TbChlChannel tbChlChannelDto)
			throws CbsCheckedException {
		chlChannelService.updateChannel(tbChlChannelDto);
	}

}
