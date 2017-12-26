/**
 * 版权所有(C) 2016 深圳市雁联计算系统有限公司
 * 创建:lyg 2016-9-5
 */

package com.ylink.inetpay.cbs.chl.cache;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.ylink.inetpay.cbs.chl.dao.TbChlChannelMapper;
import com.ylink.inetpay.common.core.constant.EChlChannelCode;
import com.ylink.inetpay.common.core.constant.EChlOrCheckFile;
import com.ylink.inetpay.common.core.util.ExceptionProcUtil;
import com.ylink.inetpay.common.project.channel.dto.TbChlChannel;
import com.ylink.inetpay.common.project.channel.dto.TbChlPayStyle;

@Service("tbChlChannelDtoCache")
public class TbChlChannelDtoCacheImpl implements TbChlChannelDtoCache {

	@Autowired
	TbChlChannelMapper tbChlChannelMapper;
	public static final Logger _log = LoggerFactory.getLogger(TbChlChannelDtoCacheImpl.class);



	@Override
	@Cacheable(value="mpay",key="'cbsTbChlChannelDtoCache[key-' + #channelCode.value")
	public TbChlChannel selectByChannelCode(EChlChannelCode channelCode) {
		return tbChlChannelMapper.selectByChannelCode(channelCode);
	}
	
	@Override
	@Cacheable(value="mpay",key="'cbsTbChlChannelDtoCache[key-' + #joinBank.value")
	public List<TbChlChannel> getChannels(EChlOrCheckFile joinBank){
		return tbChlChannelMapper.getChannels(joinBank);
	}
	
	
	@Override
	@Cacheable(value="mpay",key="'channelRouteDtoCache'")
	public List<TbChlPayStyle>  getPayStyle(){
		try {
			return tbChlChannelMapper.getPayStyle();
		} catch (Exception e) {
			_log.error("收银台获取渠道路由异常{}",ExceptionProcUtil.getExceptionDesc(e));
		}
		return null;
	}

	@Override
	@Cacheable(value="mpay",key="'channelRouteDtoCacheMobileFast'")
	public List<TbChlPayStyle> getMobileFastStyle(){
		try {
			return tbChlChannelMapper.getMobileFastStyle();
		} catch (Exception e) {
			_log.error("手机收银台获取渠道路由异常{}",ExceptionProcUtil.getExceptionDesc(e));
		}
		return null;
	}
	
	
	
	
	
}
