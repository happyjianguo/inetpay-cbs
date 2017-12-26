package com.ylink.inetpay.cbs.act.service;

import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.common.core.dto.ReporHeadDto;
import com.ylink.inetpay.common.project.account.dto.ActBillDto;

public interface ActBillService {
	/**
	 * 根据参数查询所有记账分录数据
	 * 
	 * @param pageDate
	 * @param ActBillDto
	 * @return
	 */
	PageData<ActBillDto> queryAllData(PageData<ActBillDto> pageDate,
			ActBillDto actBillDto);
	/**
	 * 根据参数查询所有记账分录数据
	 * 
	 * @param pageDate
	 * @param ActBillDto
	 * @param subAcctType（00基础类资金，01资管类资金）
	 * @return
	 */
	PageData<ActBillDto> queryAllDataByPortal(PageData<ActBillDto> pageDate,
			ActBillDto actBillDto,String subAcctType);
	/**
	 * 根据记账分录编号查询
	 * 
	 * @param billId
	 * @return
	 */
	ActBillDto selectByBillId(String billId);
	/**
     * 统计总笔数与总金额
     * @param drCrFlag
     * @return
     */
    ReporHeadDto reportSumData(ActBillDto actBillDto);
    /**
     * 根据custId查询余额
     * @param custId
     * @return
     */
    ActBillDto selectByCustId(ActBillDto actBillDto);
}
