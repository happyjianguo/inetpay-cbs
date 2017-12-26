package com.ylink.inetpay.cbs.pay.service;

import java.util.List;

import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsPayBatchDetailDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsPayBatchDto;
import com.ylink.inetpay.common.project.portal.vo.UserCheckVO;

public interface PayBatchService {
	/**
	 * 批量支付查询接口
	 * 
	 * @param pageDate
	 * @param mrsPayBatchDto
	 * @return
	 */
	PageData<MrsPayBatchDto> queryAllData(PageData<MrsPayBatchDto> pageDate, MrsPayBatchDto mrsPayBatchDto);

	/**
	 * 批量支付查询接口
	 * 
	 * @param pageDate
	 * @param mrsPayBatchDto
	 * @return
	 */
	PageData<MrsPayBatchDto> queryAllDataAudit(PageData<MrsPayBatchDto> pageDate, MrsPayBatchDto mrsPayBatchDto);

	/**
	 * 批量支付明细接口
	 * 
	 * @param batchNo
	 * @return
	 */
	List<MrsPayBatchDetailDto> selectByBatchNo(String batchNo);

	/**
	 * 批量支付详细数据
	 * 
	 * @param batchNo
	 * @return
	 */
	MrsPayBatchDto payBatchByKeyId(String keyId);

	/**
	 * 保存批量支付明细
	 * 
	 * @param batchNo
	 * @return
	 */
	UserCheckVO savePayBatchDetail(List<MrsPayBatchDetailDto> detailList, MrsPayBatchDto mrsPayBatchDto);

	/**
	 * 更新数据
	 * 
	 * @param record
	 */

	void updateByPrimaryKeySelective(MrsPayBatchDto record);
}
