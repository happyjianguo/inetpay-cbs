package com.ylink.inetpay.cbs.act.service;

import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.common.core.dto.ReporHeadDto;
import com.ylink.inetpay.common.project.account.dto.ActAccountDto;
import com.ylink.inetpay.common.project.account.dto.ActBookDto;

public interface ActBookService {
	/**
	 * 根据参数查询所有记账交易流水数据
	 * 
	 * @param pageDate
	 * @param ActBookDto
	 * @return
	 */
	PageData<ActBookDto> queryAllData(PageData<ActBookDto> pageDate,
			ActBookDto actBookDto);

	/**
	 * 根据记账交易流水编号查询
	 * 
	 * @param bookId
	 * @return
	 */
	ActBookDto selectByBookId(String bookId);
	/**
	 * 根据PAYID查询
	 * 
	 * @param accountId
	 * @return
	 */
	ActBookDto selectByPayId(String payId);
	/**
     * 统计总笔数与总金额
     * @param checkStatus
     * @return
     */
    ReporHeadDto reportSumData(ActBookDto actBookDto);
}
