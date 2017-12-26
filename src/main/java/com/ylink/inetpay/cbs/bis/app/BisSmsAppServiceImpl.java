package com.ylink.inetpay.cbs.bis.app;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.cbs.bis.service.BisSmsService;
import com.ylink.inetpay.common.core.constant.EAccountType;
import com.ylink.inetpay.common.core.constant.EBisSmsSystem;
import com.ylink.inetpay.common.core.constant.EBisTemplateCode;
import com.ylink.inetpay.common.core.constant.EBusiType;
import com.ylink.inetpay.common.core.constant.EmessType;
import com.ylink.inetpay.common.core.constant.EsendChannel;
import com.ylink.inetpay.common.core.constant.EsendType;
import com.ylink.inetpay.common.project.cbs.app.BisSmsAppService;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisSmsDto;
import com.ylink.inetpay.common.project.cbs.exception.CbsCheckedException;
import com.ylink.inetpay.common.project.portal.enums.ErrorMsgEnum;
import com.ylink.inetpay.common.project.portal.vo.UserCheckVO;
@Service("bisSmsAppService")
public class BisSmsAppServiceImpl implements BisSmsAppService {
	private Logger log = LoggerFactory.getLogger(BisSmsAppServiceImpl.class);
	@Autowired
	private BisSmsService bisSmsService;
	@Autowired
	@Qualifier("smsTaskExecutor")
	TaskExecutor smsTaskExecutor;
	
	@Override
	public PageData<BisSmsDto> findListPage(PageData<BisSmsDto> pageDate,
			BisSmsDto bisSmsDto) throws CbsCheckedException {
		return bisSmsService.findListPage(pageDate, bisSmsDto);
	}

	@Override
	public BisSmsDto details(String id) throws CbsCheckedException {
		return bisSmsService.details(id);
	}

	@Override
	public UserCheckVO sendSms(final String tel, final EBisSmsSystem smsSystem,
		final List<String> params, final EBisTemplateCode smsTemplateCode,final EmessType messType)  {
		log.info("开始发送短信[tel="+tel+"]");
		try {
			UserCheckVO checkVo = bisSmsService.sendSms(tel, smsSystem, params, smsTemplateCode,EsendChannel.SHOR_MESSAGE,messType);
			return checkVo;
		} catch (Exception e) {
			log.error("短信发送失败:",e);
			return new UserCheckVO(false, ErrorMsgEnum.SMS_SEND_FAIL);
		}
		/*smsTaskExecutor.execute(new Runnable() {
			@Override
			public void run() {
				bisSmsService.sendSms(tel, smsSystem, params, smsTemplateCode,EsendChannel.SHOR_MESSAGE,messType);
			}
		});*/
	}
	
	@Override
	public void accountChangeSms(String custId, EBusiType busiType,EAccountType accountType,long transferAmt,long accountAmt,long fee) throws CbsCheckedException{
		/*MrsUserDto user=mrsUserService.selectUser(custId);
		String tel=user.getPhone();
		EBisSmsSystem smsSystem=EBisSmsSystem.ACCOUNT;
		 List<String> params =new ArrayList<>();
		 String openId=user.getOpenId();
		 EmessType messType=null;
		 EBisTemplateCode smsTemplateCode=null;
		 //用户姓名
		 params.add(user.getRealName()==null?user.getLoginName():user.getRealName());
		 //交易金额
		 params.add(String.valueOf(BigDecimalUtil.formatMoney(transferAmt-fee)));
		 
		 String content="";
		 switch (busiType) {
		case RECHARGE:
			messType=EmessType.RECHARGE_NOTIFICATION;
			smsTemplateCode=EBisTemplateCode.MSG_RECHARGE;
			content="充值成功";
			break;
		case WITHDRAW:
			messType=EmessType.MESSAGE_NOTIFICATION;
			smsTemplateCode=EBisTemplateCode.MSG_WITHDRAW;
			//手续费，只有转账提现有
			params.add(String.valueOf(BigDecimalUtil.formatMoney(fee)));
			content="提现成功";
			break;
		case TRANSFER:
			messType=EmessType.MESSAGE_NOTIFICATION;
			smsTemplateCode=EBisTemplateCode.MSG_TRANSFER;
			//手续费，只有转账提现有
			params.add(String.valueOf(BigDecimalUtil.formatMoney(fee)));
			content="转账成功";
			break;
		case PAY:
			messType=EmessType.CONSUMPTION_NOTIFICATION;
			smsTemplateCode=EBisTemplateCode.MSG_PAY;
			content="消费成功";
			break;
		case REFUND:
			messType=EmessType.MESSAGE_NOTIFICATION;
			smsTemplateCode=EBisTemplateCode.MSG_REFUND;
			content="退款成功";
			break;

		default:
			throw new  CbsCheckedException(ECbsErrorCode.SMS_SEND_ERROR.getValue(),ECbsErrorCode.SMS_SEND_ERROR.getDisplayName());
		}
		 //余额
		 params.add(String.valueOf(BigDecimalUtil.formatMoney(accountAmt)));
		 sendSms(tel, smsSystem, params, smsTemplateCode, messType);*/
	}

	@Override
	public void sendWebChatSms(final String tel,final EBisSmsSystem smsSystem,
			final String content,final EsendChannel sendChannel,final EmessType messType,final String openId,
			final String messTitle,final Long amount,final Long balance,final EsendType sendType,final String sendTime)
			throws CbsCheckedException {
		smsTaskExecutor.execute(new Runnable() {
			@Override
			public void run() {
					bisSmsService.sendWebChatSms(tel, smsSystem,content,sendChannel,messType,openId,messTitle,amount,balance,sendType,sendTime);
			}
		});
	}

	@Override
	public void flushSms() throws CbsCheckedException {
		bisSmsService.flushSms();
	}

	@Override
	public List<BisSmsDto> findList(BisSmsDto bisSmsDto) {
		
		return bisSmsService.findList( bisSmsDto);
	}
}
