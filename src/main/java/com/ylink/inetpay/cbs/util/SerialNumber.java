package com.ylink.inetpay.cbs.util;

import java.util.Random;

import org.ylinkpay.framework.core.util.UuidUtil;

import com.ylink.inetpay.common.core.util.Md5Util;
import com.ylink.inetpay.common.project.channel.constant.Constants;

/**
 * 序号生成器
 * 
 * @author pst01
 *
 */
public class SerialNumber {
	/**
	 * RedisCache缓存超时的时间，单位是秒
	 */
	public static final long CACHE_TIME_OUT = 7*86400L;
	
	public static String generate() {
		Random rand = new Random();
		int tmp = Math.abs(rand.nextInt());
		String s = "000000" + tmp;
		return s.substring(s.length() - 6);
	}

	/**
	 * 根据UUID 和用户的主键值，生成MD5加密的Token
	 * 
	 * @param userKey
	 * @return
	 */
	public static String generatePortalToken(String userKey) {
		String sysuuid = UuidUtil.getUUID();
		String portalToken = Md5Util.MD5Encoder(sysuuid + userKey, Constants.ENCODING);
		return portalToken;
	}
}
