package com.ylink.inetpay.cbs.cls.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.ylink.inetpay.cbs.cls.dao.ClsBankFeeRepDtoMapper;
import com.ylink.inetpay.cbs.cls.dao.ClsChannelBillDao;
import com.ylink.inetpay.common.project.clear.dto.ClsBankFeeRepDto;
import com.ylink.inetpay.common.project.clear.dto.ClsChannelBill;

/**
 * @类名称： ClsBankFeeReportServiceImpl
 * 
 * @类描述：渠道手续费接口服务类 @创建人： yuqingjun @创建时间： 2016-12-25 上午10:35:02
 * 
 */
@Service("clsBankFeeReportService")
public class ClsBankFeeReportServiceImpl implements ClsBankFeeReportService {

	@Autowired
	private ClsChannelBillDao clsChannelBillDao;
	@Autowired
	private ClsBankFeeRepDtoMapper clsBankFeeRepDtoMapper;

	@Override
	public List<ClsBankFeeRepDto> queryAllBankFee(ClsBankFeeRepDto report) {
		return clsBankFeeRepDtoMapper.queryBankFee(report);
	}

	@Override
	public PageData<ClsBankFeeRepDto> queryBankFee(PageData<ClsBankFeeRepDto> pageData, ClsBankFeeRepDto report) {

		PageHelper.startPage(pageData.getPageNumber(), pageData.getPageSize());
		List<ClsBankFeeRepDto> list = clsBankFeeRepDtoMapper.queryBankFee(report);
		Page<ClsBankFeeRepDto> page = (Page<ClsBankFeeRepDto>) list;
		pageData.setTotal(page.getTotal());
		pageData.setRows(list);
		return pageData;

	}

	@Override
	public PageData<ClsChannelBill> queryBankFeeDetail(PageData<ClsChannelBill> pageData, ClsChannelBill report) {

		PageHelper.startPage(pageData.getPageNumber(), pageData.getPageSize());
		List<ClsChannelBill> list = clsChannelBillDao.queryBankFeeDetail(report);
		Page<ClsChannelBill> page = (Page<ClsChannelBill>) list;
		pageData.setTotal(page.getTotal());
		pageData.setRows(list);
		return pageData;

	}

	@Override
	public List<ClsChannelBill> queryAllBankFeeDetail(ClsChannelBill report) {
		return clsChannelBillDao.queryBankFeeDetail(report);
	}

}
