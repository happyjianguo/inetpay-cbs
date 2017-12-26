package com.ylink.inetpay.cbs.bis.service;
import java.util.List;

import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.common.core.constant.EBisSmsSystem;
import com.ylink.inetpay.common.core.constant.EBisTemplateCode;
import com.ylink.inetpay.common.core.constant.EmessType;
import com.ylink.inetpay.common.core.constant.EsendChannel;
import com.ylink.inetpay.common.core.constant.EsendType;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisSmsDto;
import com.ylink.inetpay.common.project.cbs.exception.CbsCheckedException;
import com.ylink.inetpay.common.project.portal.vo.UserCheckVO;
/**
 * 邮件发送记录服务类
 * @author haha
 *
 */
public interface BisSmsService {
	/**
	 * 获取短信发送记录列表
	 * @param pageDate
	 * @param bisSmsDto
	 * @return
	 * @throws CbsCheckedException
	 */
	public PageData<BisSmsDto> findListPage(PageData<BisSmsDto> pageDate,BisSmsDto bisSmsDto);
	
	public List<BisSmsDto> findList(BisSmsDto bisSmsDto);
	/**
	 * 获取短信发送记录详情
	 * @param id
	 * @return
	 * @throws CbsCheckedException
	 */
	public BisSmsDto details(String id);
	/**
	 * 定时刷新短信的状态，对非实时发送的短信进行发送操作。
	 * @throws CbsCheckedException
	 */
	public void flushSms();
	/**
	 * 普通短信发送
	 * @param tel
	 * @param smsSystem
	 * @param params
	 * @param smsTemplateCode
	 * @param sendChannel
	 * @param messType
	 * @param messTitle
	 * @throws CbsCheckedException
	 */
	public UserCheckVO sendSms(String tel, EBisSmsSystem smsSystem,
			List<String> params, EBisTemplateCode smsTemplateCode,
			EsendChannel sendChannel, EmessType messType);
	/**
	 * 微信/app短信发送
	 * @param tel
	 * @param smsSystem
	 * @param content
	 * @param sendChannel
	 * @param messType
	 * @param openId
	 * @param messTitle
	 * @param amount
	 * @param balance
	 * @param sendType
	 * @param sendTime
	 */
	public void sendWebChatSms(String tel, EBisSmsSystem smsSystem,
			String content, EsendChannel sendChannel, EmessType messType,
			String openId, String messTitle, Long amount, Long balance,
			EsendType sendType, String sendTime);
	
}

