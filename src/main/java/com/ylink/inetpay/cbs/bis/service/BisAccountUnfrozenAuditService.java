package com.ylink.inetpay.cbs.bis.service;

import java.util.Date;
import java.util.List;

import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.common.project.cbs.dto.bis.BisAccountUnfrozenAuditDto;

public interface BisAccountUnfrozenAuditService {
	/**
	 * 获取所有复核通过，待处理，解冻日期小于等于当前账务日期的记录
	 * @param parse
	 * @return
	 */
	List<BisAccountUnfrozenAuditDto> listUnFrozenByEndTime(Date parse);
	/**
	 * 根据id获取解冻订单详情
	 * @param id
	 * @return
	 */
	BisAccountUnfrozenAuditDto findUnfrozenViewById(String id);
	/**
	 * 解冻订单明细列表
	 * @param pageData
	 * @param queryParam
	 * @return
	 */
	PageData<BisAccountUnfrozenAuditDto> listUnFrozenList(PageData<BisAccountUnfrozenAuditDto> pageData,
			BisAccountUnfrozenAuditDto queryParam);
	/**
	 * 获取到了截至日期，但是处于待复核的解冻记录，改为到期自动解冻
	 * @return
	 */
	List<BisAccountUnfrozenAuditDto> runOutWaitAuditByEndTime(Date actDate);

}
