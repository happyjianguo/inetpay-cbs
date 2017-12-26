package com.ylink.inetpay.cbs.pay.dao;

import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

@MybatisMapper("paySequenceMapper")
public interface PaySequenceMapper {
	/**
	 * 10位长度序列
	 * @return
	 */
	long getPaySeqNo(String key);
}