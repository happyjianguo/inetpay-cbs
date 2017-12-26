/**
 * 版权所有(C) 2016 深圳市雁联计算系统有限公司
 * 创建:lyg 2016-9-5
 */

package com.ylink.inetpay.cbs.chl.cache;

import java.util.List;

import com.ylink.inetpay.common.core.constant.EChlChannelCode;
import com.ylink.inetpay.common.core.constant.EChlOrCheckFile;
import com.ylink.inetpay.common.project.channel.dto.TbChlChannel;
import com.ylink.inetpay.common.project.channel.dto.TbChlPayStyle;


public interface TbChlChannelDtoCache {
	
	
	public TbChlChannel selectByChannelCode(EChlChannelCode channelCode);
	
	List<TbChlChannel> getChannels(EChlOrCheckFile joinBank);
	
	/**获取收银台路由 **/
	public List<TbChlPayStyle>  getPayStyle();
	
	
	/**获取手机收银台快捷路由 **/
	public List<TbChlPayStyle> getMobileFastStyle();
	
}
