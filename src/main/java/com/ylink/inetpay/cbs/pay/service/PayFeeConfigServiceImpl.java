package com.ylink.inetpay.cbs.pay.service;

import java.util.Date;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.ylink.inetpay.cbs.pay.dao.PayFeeConfigDtoMapper;
import com.ylink.inetpay.common.core.constant.EPayBusiType;
import com.ylink.inetpay.common.core.constant.EPayFeeState;
import com.ylink.inetpay.common.core.util.DateUtils;
import com.ylink.inetpay.common.project.pay.app.PayFeeConfigAppService;
import com.ylink.inetpay.common.project.pay.dto.PayFeeConfigDto;

@Service("payFeeConfigService")
public class PayFeeConfigServiceImpl implements PayFeeConfigService {

	@Autowired
	private PayFeeConfigDtoMapper payFeeConfigDtoMapper;
	@Autowired
	private PayFeeConfigAppService payFeeConfigAppService;


	/**
	 * 新增
	 */
	@Override
	public long save(PayFeeConfigDto record) {
		// return payFeeConfigDtoMapper.insert(record);
		return payFeeConfigAppService.insert(record);
	}

	/**
	 * 查询手续费配置是否存在
	 */
	@Override
	public PayFeeConfigDto findFeeConfig(String merCode, EPayBusiType busiType, String effectiveDate) {
		return payFeeConfigDtoMapper.findFeeConfig(merCode, busiType, effectiveDate);
	}

	/**
	 * 条件查询
	 */
	@Override
	public PageData<PayFeeConfigDto> findByCondition(PageData<PayFeeConfigDto> pageData, PayFeeConfigDto queryParam) {
		PageHelper.startPage(pageData.getPageNumber(), pageData.getPageSize());
		List<PayFeeConfigDto> items = payFeeConfigDtoMapper.findByCondition(queryParam);

		Page<PayFeeConfigDto> page = (Page<PayFeeConfigDto>) items;
		pageData.setRows(items);
		pageData.setTotal(page.getTotal());
		return pageData;
	}

	/**
	 * 主键查询
	 */
	@Override
	public PayFeeConfigDto findById(String id) {
		return payFeeConfigDtoMapper.selectByPrimaryKey(id);
	}

	/**
	 * 修改
	 */
	@Override
	public long update(PayFeeConfigDto record) {
		// return payFeeConfigDtoMapper.updateByPrimaryKeySelective(record);
		return payFeeConfigAppService.updateByPrimaryKeySelective(record);
	}

	/**
	 * 查询需要生效的手续费配置列表
	 */
	@Override
	public List<PayFeeConfigDto> findByStatusPage(PageData<PayFeeConfigDto> pageData, EPayFeeState payFeeState,
			Date effectiveDate) {
		PageHelper.startPage(pageData.getPageNumber(), pageData.getPageSize());
		List<PayFeeConfigDto> list = payFeeConfigDtoMapper.findByStatusPage(payFeeState.getValue(),effectiveDate);
		return list;
	}

	/**
	 * 代付手续费生效
	 */
	@Override
	public void updateFeeStatus(String id, EPayFeeState payFeeState) {
		payFeeConfigAppService.updateFeeStatus(id,payFeeState.getValue());
	}

	@Override
	public List<PayFeeConfigDto> editAjaxcheck(String merCode, EPayBusiType busiType, String effectiveDate) {
		return payFeeConfigDtoMapper.editAjaxcheck(merCode,busiType,effectiveDate);
	}

	@Override
	public PayFeeConfigDto queryByMerCodeBusiTypeEffectiveDate(String merCode, EPayBusiType busiType,
			String effectiveDate, EPayFeeState payFeeState) {
		return payFeeConfigDtoMapper.queryByMerCodeBusiTypeEffectiveDate(merCode,busiType.getValue(),effectiveDate,payFeeState.getValue());
	}

	/**
	 * 获取时间和序列字符串
	 * @param length
	 * @param seqName
	 * @return
	 */
	@Override
	public String getPlatTradeNoSeqNo(int length,String seqName){
		String currentTime = DateUtils.getCurrentDate();
		long value =payFeeConfigDtoMapper.getPlatTradeNoSeqNo(seqName);
		long divNum=Math.round(Math.pow(10,length));
		value=value%divNum;
		String formatStr = "%0" + length + "d";
		formatStr = String.format(formatStr, value);
		return currentTime+formatStr;
	}
	
}
