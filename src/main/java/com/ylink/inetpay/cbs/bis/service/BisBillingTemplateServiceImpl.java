package com.ylink.inetpay.cbs.bis.service;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.ylinkpay.framework.core.model.PageData;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.ylink.inetpay.cbs.bis.dao.BisBillingTemplateDtoMapper;
import com.ylink.inetpay.cbs.common.CbsConstants;
import com.ylink.inetpay.common.core.constant.EBisBillingType;
import com.ylink.inetpay.common.core.constant.EBisBusinessType;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisBillingTemplateDto;
import com.ylink.inetpay.common.project.cbs.exception.CbsUncheckedException;
import com.ylink.inetpay.common.project.cbs.exception.ECbsErrorCode;
@Service("bisBillingTemplateService")
@Transactional(value=CbsConstants.TX_MANAGER_BIS)
public class BisBillingTemplateServiceImpl implements BisBillingTemplateService {
	
	private static Logger _log = LoggerFactory.getLogger(BisBillingTemplateServiceImpl.class);
	@Autowired
	private BisBillingTemplateDtoMapper bisBillingTemplateDtoMapper;
	@Override
	public PageData<BisBillingTemplateDto> findListPage(
			PageData<BisBillingTemplateDto> pageDate,
			BisBillingTemplateDto bisBillingTemplateDto){
		PageHelper.startPage(pageDate.getPageNumber(),pageDate.getPageSize());
		List<BisBillingTemplateDto> findListPage=bisBillingTemplateDtoMapper.list(bisBillingTemplateDto);
		Page<BisBillingTemplateDto> page=(Page<BisBillingTemplateDto>) findListPage;
		pageDate.setTotal(page.getTotal());
		pageDate.setRows(findListPage);
		return pageDate;
	}

	@Override
	public void update(BisBillingTemplateDto bisBillingTemplateDto){
		bisBillingTemplateDto.setUpdateTime(new Date());
		bisBillingTemplateDtoMapper.updateByPrimaryKey(bisBillingTemplateDto);
	}

	@Override
	public BisBillingTemplateDto details(String id){
		return bisBillingTemplateDtoMapper.selectByPrimaryKey(id);
	}

	@Override
	public Long compuOderFee(EBisBusinessType businessType,long orderAmt) {
		long feeAmt = 0l;
		List<BisBillingTemplateDto> billingTemplateDto = bisBillingTemplateDtoMapper.selectByTrade(businessType);
		if (billingTemplateDto == null || billingTemplateDto.isEmpty()) {	//不存在计费模板
			return feeAmt;
		}else if (billingTemplateDto.size()>1) {	//计费模板配置错误
			_log.error(businessType.getDisplayName()+"计费模板存在多条，配置错误 method:BisBillingTemplateServiceImpl.compuOderFee");
			throw new CbsUncheckedException(ECbsErrorCode.TEMP_ERROR.getValue(), ECbsErrorCode.TEMP_ERROR.getDisplayName());
		}
		BisBillingTemplateDto bisBillingDto = billingTemplateDto.get(0);
		if (bisBillingDto.getType() == EBisBillingType.BY_PEN) {	//按笔计费
			return bisBillingDto.getValue().setScale(0, BigDecimal.ROUND_HALF_UP).longValue();
		}else {	//按百分比
			BigDecimal a =BigDecimal.valueOf(orderAmt);
			BigDecimal orderFeeDeic = bisBillingDto.getValue().multiply(a);
			long orderFee = orderFeeDeic.setScale(0, BigDecimal.ROUND_HALF_UP).longValue();
			long guranttee = bisBillingDto.getGuarantee();	//保底值
			long capValue = bisBillingDto.getCapValue();	//封顶值
			if (orderFee < guranttee) {
				return guranttee;
			}else if (capValue != 0 && orderFee > capValue) {
				return capValue;
			}else {
				return orderFee;
			}
		}
	}
}
