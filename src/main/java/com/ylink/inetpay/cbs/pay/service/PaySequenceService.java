package com.ylink.inetpay.cbs.pay.service;


public interface PaySequenceService {

	
	/**
	 * 获取序列
	 * @param seqName 序列名称
	 * @param length 长度
	 * @return
	 */
	public String getSequenceId(String prefix, String seqName,int length);
	
	
	/**
	 * 获取序列
	 * @param seqName 序列名称
	 * @param length 长度
	 * @return
	 */
	public String getSequenceId(String seqName,int length);
}
