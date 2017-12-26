package com.ylink.inetpay.cbs.pay.service;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.ylink.inetpay.cbs.pay.dao.PayBwlistDtoMapper;
import com.ylink.inetpay.cbs.ucs.sec.service.UcsSecUserServiceImpl;
import com.ylink.inetpay.common.core.util.ExceptionProcUtil;
import com.ylink.inetpay.common.project.cbs.exception.CbsUncheckedException;
import com.ylink.inetpay.common.project.cbs.exception.ECbsErrorCode;
import com.ylink.inetpay.common.project.pay.app.PayBwlistAppService;
import com.ylink.inetpay.common.project.pay.dto.PayBwlistDto;
import com.ylink.inetpay.common.project.pay.exception.PayCheckedException;
@Service("payBwlistService")
public class PayBwlistServiceImpl implements PayBwlistService {
	@Autowired
	private PayBwlistDtoMapper  payBwlistDtoMapper;
	@Autowired
	private PayBwlistAppService paySystemPayBwlistAppService;
	private static Logger _log = LoggerFactory.getLogger(UcsSecUserServiceImpl.class);
	@Override
	public PageData<PayBwlistDto> findListPage(PageData<PayBwlistDto> pageDate,
			PayBwlistDto payBwlistDto){
		PageHelper.startPage(pageDate.getPageNumber(), pageDate.getPageSize());
		List<PayBwlistDto> findListPage=payBwlistDtoMapper.list(payBwlistDto);
		Page<PayBwlistDto> page=(Page<PayBwlistDto>) findListPage;
		pageDate.setTotal(page.getTotal());
		pageDate.setRows(findListPage);
		return pageDate;
	}

	@Override
	public void add(PayBwlistDto payBwlistDto){
		//调用支付系统接口
		try {
			paySystemPayBwlistAppService.insert(payBwlistDto);
		} catch (PayCheckedException e) {
			_log.error("新增黑白名单："+e.getMessage());
			throw new CbsUncheckedException(e.getCode(),e.getMessage());
		} catch (Exception e) {
			_log.error("新增黑白名单：调用支付系统超时:{}",ExceptionProcUtil.getExceptionDesc(e));
			throw new CbsUncheckedException(ECbsErrorCode.PAY_SYS_ERROR.getValue(),"调用支付系统超时");
		}
	}

	@Override
	public void delete(String id,String custId){
		//调用支付系统接口
		try {
			paySystemPayBwlistAppService.delete(id,custId);
		} catch (PayCheckedException e) {
			_log.error("新增黑白名单："+e.getMessage());
			throw new CbsUncheckedException(e.getCode(),e.getMessage());
		} catch (Exception e) {
			_log.error("删除黑白名单：调用支付系统超时:{}",ExceptionProcUtil.getExceptionDesc(e));
			throw new CbsUncheckedException(ECbsErrorCode.PAY_SYS_ERROR.getValue(),"调用支付系统超时");
		}
	}

	@Override
	public PayBwlistDto details(String id) {
		return payBwlistDtoMapper.selectByPrimaryKey(id);
	}

}
