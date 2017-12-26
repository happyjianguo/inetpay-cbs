package com.ylink.inetpay.cbs.cls.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.ylink.inetpay.cbs.bis.service.BisSysParamService;
import com.ylink.inetpay.cbs.cls.dao.ClsAcctCheckDao;
import com.ylink.inetpay.cbs.cls.dao.ClsCallAcctDtoMapper;
import com.ylink.inetpay.cbs.ucs.sec.service.UcsSecUserServiceImpl;
import com.ylink.inetpay.common.core.SystemParamConstants;
import com.ylink.inetpay.common.core.constant.CLSPayType;
import com.ylink.inetpay.common.core.constant.CLSReviewStatus;
import com.ylink.inetpay.common.core.constant.CLSSettleStatus;
import com.ylink.inetpay.common.core.constant.EProcessStatus;
import com.ylink.inetpay.common.core.constant.ESuccessFailDealing;
import com.ylink.inetpay.common.core.dto.SuccessFailDealingDto;
import com.ylink.inetpay.common.project.cbs.app.BisSysParamAppService;
import com.ylink.inetpay.common.project.cbs.exception.CbsUncheckedException;
import com.ylink.inetpay.common.project.cbs.exception.ECbsErrorCode;
import com.ylink.inetpay.common.project.clear.app.ClearTransferFundAppService;
import com.ylink.inetpay.common.project.clear.dto.ClsAcctCheck;
import com.ylink.inetpay.common.project.clear.dto.ClsCallAcct;

@Service("clsCallAcctService")
public class ClsCallAcctServiceImpl implements ClsCallAcctService {
	@Autowired
	private ClsCallAcctDtoMapper clsCallAcctDtoMapper;
	@Autowired
	private ClearTransferFundAppService clearTransferFundAppService; 
	@Autowired
	private ClsAcctCheckDao clsAcctCheckDao;
	@Autowired
	BisSysParamService bisSysParamService;
	private static Logger _log = LoggerFactory.getLogger(UcsSecUserServiceImpl.class);
	@Override
	public PageData<ClsCallAcct> pageAuditPageList(ClsCallAcct clsCallAcct,
			PageData<ClsCallAcct> pageData) {
		PageHelper.startPage(pageData.getPageNumber(), pageData.getPageSize());
		List<ClsCallAcct> list = clsCallAcctDtoMapper.pageAuditPageList(clsCallAcct,CLSPayType.REVIEW,CLSSettleStatus.REVIEW);
		Page<ClsCallAcct> page = (Page<ClsCallAcct>) list;
		pageData.setTotal(page.getTotal());
		pageData.setRows(list);
		return pageData;
	}

	@Override
	public PageData<ClsCallAcct> pageAuditResultPageList(
			ClsCallAcct clsCallAcct, PageData<ClsCallAcct> pageData){
		PageHelper.startPage(pageData.getPageNumber(), pageData.getPageSize());
		List<ClsCallAcct> list = clsCallAcctDtoMapper.pageAuditResultPageList(clsCallAcct,CLSPayType.REVIEW,CLSReviewStatus.PASS,CLSReviewStatus.REFUSE);
		Page<ClsCallAcct> page = (Page<ClsCallAcct>) list;
		pageData.setTotal(page.getTotal());
		pageData.setRows(list);
		return pageData;
	}

	@Override
	public PageData<ClsCallAcct> pagePayPageList(ClsCallAcct clsCallAcct,
			PageData<ClsCallAcct> pageData){
		PageHelper.startPage(pageData.getPageNumber(), pageData.getPageSize());
		//List<ClsCallAcct> list = clsCallAcctDtoMapper.pagePayPageList(clsCallAcct,CLSPayType.REVIEW,CLSSettleStatus.SETTLE);
		List<ClsCallAcct> list = clsCallAcctDtoMapper.pagePayPageList(clsCallAcct,CLSSettleStatus.SETTLE);
		Page<ClsCallAcct> page = (Page<ClsCallAcct>) list;
		pageData.setTotal(page.getTotal());
		pageData.setRows(list);
		return pageData;
	}

	@Override
	public ClsCallAcct details(String id) {
		return clsCallAcctDtoMapper.details(id);
	}

	@Override
	public void auditPass(ClsCallAcct acctDto){
		//调用清结算系调拨审核接口
		try {
			clearTransferFundAppService.review(acctDto.getId(), acctDto.getAuditer(), acctDto.getAuditerName(), acctDto.getCheckSugg(), acctDto.getReviewStatus());
		} catch (Exception e) {
			_log.error("备付金调拨审核："+ECbsErrorCode.CLEAR_SYSTEM_ERROR.getDisplayName());
			throw new CbsUncheckedException(ECbsErrorCode.CLEAR_SYSTEM_ERROR.getValue(),ECbsErrorCode.CLEAR_SYSTEM_ERROR.getDisplayName());
		}
	}

	@Override
	public void againPay(String id) {
		//调用清结算系统重新支付系统
		try{
			clearTransferFundAppService.transferById(id);
		} catch (Exception e) {
			_log.error("备付金重新支付："+ECbsErrorCode.CLEAR_SYSTEM_ERROR.getDisplayName());
			throw new CbsUncheckedException(ECbsErrorCode.CLEAR_SYSTEM_ERROR.getValue(),ECbsErrorCode.CLEAR_SYSTEM_ERROR.getDisplayName());
		}
	}

	@Override
	public boolean callAcctSuccessful(String callDay) {
		try {
			String isAllot=bisSysParamService.getValue(SystemParamConstants.IS_ALLOT);
			if("2".equals(isAllot.trim())){//无须强制依赖
				_log.info("资金调拨，返回【处理成功】,原因是资金调拨任务不是退款、转账、提现的强制依赖条件，日期："+callDay);
				return true;
			}
		} catch (Exception e) {
			_log.error("资金调拨任务，获取是否强制依赖参数异常"+e);
		}
		List<ClsCallAcct> items = clsCallAcctDtoMapper.queryClsCallAcctByDate(callDay);
		if(items == null || items.isEmpty()){
			//没有资金调拨任务时进一步判断是否做过监控任务初始化(有内部对账任务则为已初始化过)
			//1.若有初始化过则认为当天不用做资金调拨，可以做提现、退款、转账处理
			//2.若没有初始化过则认为当天还未生产资金调拨认为，不能做提现、退款、转账处理
			ClsAcctCheck check =clsAcctCheckDao.queryByCheckDay(callDay);
			if(check!=null){
				return true;
			}else{
				return false;
			}
		}
		
		for(ClsCallAcct ca : items){
			if(!(ca.getSettleStatus() == CLSSettleStatus.SETTLE && 
					ca.getDealStatus() == EProcessStatus.PROCESS_SUCCESS)) {
				return false;
			}
		}
		return true;
	}
}
