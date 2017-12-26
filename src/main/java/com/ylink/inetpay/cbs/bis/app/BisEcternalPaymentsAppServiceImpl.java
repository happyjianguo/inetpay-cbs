package com.ylink.inetpay.cbs.bis.app;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.cbs.bis.service.BisEcternalPaymentsService;
import com.ylink.inetpay.common.core.dto.ResultCode;
import com.ylink.inetpay.common.project.cbs.app.BisEcternalPaymentsAppService;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisEcternalPayments;
import com.ylink.inetpay.common.project.cbs.dto.ucs.sec.UcsSecUserDto;
import com.ylink.inetpay.common.project.cbs.exception.CbsCheckedException;
import com.ylink.inetpay.common.project.pay.dto.PayAmtAllocateDto;

@Service("bisEcternalPaymentsAppService")
public class BisEcternalPaymentsAppServiceImpl implements BisEcternalPaymentsAppService {
	@Autowired
	private BisEcternalPaymentsService bisEcternalPaymentsService;
	@Override
	public void saveEcternalPayments(BisEcternalPayments bisEcternalPayments) throws CbsCheckedException {
		bisEcternalPaymentsService.saveEcternalPayments(bisEcternalPayments);
	}

	@Override
	public void updateEcternalPaymentsById(BisEcternalPayments bisEcternalPayments) throws CbsCheckedException {
		bisEcternalPaymentsService.updateEcternalPaymentsById(bisEcternalPayments);
	}

	@Override
	public void deleteEcternalPaymentsId(String id) throws CbsCheckedException {
		bisEcternalPaymentsService.deleteEcternalPaymentsId(id);;
	}

	@Override
	public PageData<BisEcternalPayments> findListPage(PageData<BisEcternalPayments> pageDate,
			BisEcternalPayments bisEcternalPayments) throws CbsCheckedException {
		return bisEcternalPaymentsService.findListPage(pageDate, bisEcternalPayments);
	}

	@Override
	public void audit(String auditType, String id,UcsSecUserDto userDto,String reason) throws CbsCheckedException {
		bisEcternalPaymentsService.audit(auditType,id,userDto,reason);
	}

	@Override
	public BisEcternalPayments selectOneBeanById(String id) throws CbsCheckedException {
		return bisEcternalPaymentsService.selectOneBeanById(id);
	}

	@Override
	public BisEcternalPayments getOldDto(String id) {
		return bisEcternalPaymentsService.getOldDto(id);
	}

	@Override
	public void queryPayStatus(String id) {
		bisEcternalPaymentsService.queryPayStatus(id);
	}

	@Override
	public ResultCode saveMerChantEcternalPayments(PayAmtAllocateDto payAmtAllocateDto) {
		return bisEcternalPaymentsService.saveMerChantEcternalPayments(payAmtAllocateDto);
	}

	@Override
	public Map<String, BisEcternalPayments> findEcternalPaymentsMapByBatchNo(String batchNo) {
		return bisEcternalPaymentsService.findEcternalPaymentsMapByBatchNo(batchNo);
	}

	@Override
	public List<BisEcternalPayments> list(BisEcternalPayments queryParam) {
		return bisEcternalPaymentsService.list(queryParam);
	}
}
