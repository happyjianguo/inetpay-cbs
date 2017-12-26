package com.ylink.inetpay.cbs.pay.app;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.cbs.pay.service.PayFeeConfigService;
import com.ylink.inetpay.cbs.pay.service.PayFeeConfigServiceImpl;
import com.ylink.inetpay.common.core.constant.EPayBusiType;
import com.ylink.inetpay.common.core.constant.EPayFeeState;
import com.ylink.inetpay.common.core.util.DateUtils;
import com.ylink.inetpay.common.project.account.app.ActAccountDateAppService;
import com.ylink.inetpay.common.project.cbs.app.CbsPayFeeConfigAppService;
import com.ylink.inetpay.common.project.pay.dto.PayFeeConfigDto;

@Service("cbsPayFeeConfigAppService")
public class CbsPayFeeConfigAppServiceImpl implements CbsPayFeeConfigAppService {
	
	private static final Logger _log = LoggerFactory.getLogger(PayFeeConfigServiceImpl.class);
	@Resource(name = "taskExecutor")
	private ThreadPoolTaskExecutor taskExecutor;
	
	
	@Autowired
	private PayFeeConfigService payFeeConfigService;
	@Autowired
	private ActAccountDateAppService actAccountDateAppService;
	
	/**获取财务日期*/
	public Date getCurrentTime(){
		String accountDate = actAccountDateAppService.getAccountDate();
		Date date = null;
		try {
			  date = DateUtils.stringToDate(DateUtils.stringToString(accountDate));
		} catch (ParseException e) {
			_log.error("进入添加支付手续费模板页面获取账务日期失败:{}",e);
		}
		return date;
	}
	

	@Override
	public long save(PayFeeConfigDto record) {
		return payFeeConfigService.save(record);
	}

	@Override
	public PayFeeConfigDto findFeeConfig(String merCode, EPayBusiType busiType, String effectiveDate) {
		return payFeeConfigService.findFeeConfig(merCode,busiType,effectiveDate);
	}

	@Override
	public PageData<PayFeeConfigDto> findByCondition(PageData<PayFeeConfigDto> pageData, PayFeeConfigDto queryParam) {
		return payFeeConfigService.findByCondition(pageData,queryParam);
	}

	@Override
	public PayFeeConfigDto findById(String id) {
		
		return payFeeConfigService.findById(id);
	}

	@Override
	public long update(PayFeeConfigDto record) {
		return payFeeConfigService.update(record);
	}

	@Override
	public void payFeeConfigEffect() {
		_log.debug("开始进行手续费生效处理");
		taskExecutor.execute(new Runnable() {
			@Override
			public void run() {
				// 1.每天定时任务查询手续费配置表，查询“0-待生效”并且生效日期为当前日期的所有配置信息
				// 2.遍历查询出来的手续费配置信息，逐一更新费率状态为“1-已生效”;
				
				
				int pageSize =1000;//每次处理的数量
				while(true){
					PageData<PayFeeConfigDto> pageData = new PageData<PayFeeConfigDto>();
					pageData.setPageNumber(1);
					pageData.setPageSize(pageSize);
					//查询待生效列表
					_log.debug("开始查询待生效列表。。。");
					List<PayFeeConfigDto> list = payFeeConfigService.findByStatusPage(pageData,EPayFeeState.PAY_FEE_STATE_0,getCurrentTime());
					
					if(list==null||list.size()==0){
						_log.debug("没有待生效列表。。。");
						break;
					}
					
					for (PayFeeConfigDto payFeeConfigDto : list) {
						//更新为“1-已生效”
						payFeeConfigService.updateFeeStatus(payFeeConfigDto.getId(),EPayFeeState.PAY_FEE_STATE_1);
						_log.debug("执行手续费生效操作成功！");
						
						// 3.并且根据计费商户+费用类型将该维度且是已生效的配置项的费率状态修改为“2-已失效”。
						String merCode = payFeeConfigDto.getMerCode();
						EPayBusiType busiType =  payFeeConfigDto.getBusiType();
						String effectiveDate = payFeeConfigDto.getEffectiveDate();
						PayFeeConfigDto dto = payFeeConfigService.queryByMerCodeBusiTypeEffectiveDate(merCode,busiType,effectiveDate,EPayFeeState.PAY_FEE_STATE_1);
						_log.debug("查询手续费待失效列表："+dto);
						if(dto!=null){
							payFeeConfigService.updateFeeStatus(dto.getId(),EPayFeeState.PAY_FEE_STATE_2);
							_log.debug("修改费率状态为-已失效-成功！");
						}
						
					}
				}
				
			}
		});

	}


	@Override
	public List<PayFeeConfigDto> editAjaxcheck(String merCode, EPayBusiType busiType, String effectiveDate) {
		return payFeeConfigService.editAjaxcheck(merCode,busiType,effectiveDate);
	}
	

	@Override
	public String getPlatTradeNoSeqNo(int length, String seqName) {
		return payFeeConfigService.getPlatTradeNoSeqNo(length,seqName);
	}

	
}
