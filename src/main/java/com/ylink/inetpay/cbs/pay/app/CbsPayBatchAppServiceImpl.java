package com.ylink.inetpay.cbs.pay.app;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.cbs.pay.service.PayBatchService;
import com.ylink.inetpay.common.project.cbs.app.CbsPayBatchAppService;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsPayBatchDetailDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsPayBatchDto;
import com.ylink.inetpay.common.project.portal.vo.UserCheckVO;

@Service("cbsPayBatchAppService")
public class CbsPayBatchAppServiceImpl implements CbsPayBatchAppService {
	@Autowired
	PayBatchService payBatchService;

	@Override
	public PageData<MrsPayBatchDto> queryAllData(PageData<MrsPayBatchDto> pageDate, MrsPayBatchDto mrsPayBatchDto) {
		return payBatchService.queryAllData(pageDate, mrsPayBatchDto);
	}

	@Override
	public PageData<MrsPayBatchDto> queryAllDataAudit(PageData<MrsPayBatchDto> pageDate,
			MrsPayBatchDto mrsPayBatchDto) {
		return payBatchService.queryAllDataAudit(pageDate, mrsPayBatchDto);
	}

	@Override
	public List<MrsPayBatchDetailDto> selectByBatchNo(String batchNo) {
		return payBatchService.selectByBatchNo(batchNo);
	}

	@Override
	public MrsPayBatchDto payBatchByKeyId(String keyId) {
		return payBatchService.payBatchByKeyId(keyId);
	}

	@Override
	public UserCheckVO savePayBatchDetail(List<MrsPayBatchDetailDto> detailList, MrsPayBatchDto mrsPayBatchDto) {
		return payBatchService.savePayBatchDetail(detailList, mrsPayBatchDto);
	}

	@Override
	public void updateByPrimaryKeySelective(MrsPayBatchDto record) {
		payBatchService.updateByPrimaryKeySelective(record);

	}

}
