package com.ylink.inetpay.cbs.pay.app;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;
import com.ylink.inetpay.cbs.pay.service.PayAccountAdjustService;
import com.ylink.inetpay.common.core.dto.ExportDto;
import com.ylink.inetpay.common.project.cbs.app.PayAccountAdjustAppService;
import com.ylink.inetpay.common.project.cbs.exception.CbsCheckedException;
import com.ylink.inetpay.common.project.pay.dto.PayAccountAdjustDto;
@Service("payAccountAdjustAppService")
public class PayAccountAdjustAppSerivceImpl implements
		PayAccountAdjustAppService {
	@Autowired
	private PayAccountAdjustService payAccountAdjustService;
	@Override
	public void addNotes(PayAccountAdjustDto payAccountAdjustDto)
			throws CbsCheckedException{
		payAccountAdjustService.addNotes(payAccountAdjustDto);
	}

	@Override
	public void auditPass(PayAccountAdjustDto payAccountAdjustDto)
			throws CbsCheckedException{
		payAccountAdjustService.auditPass(payAccountAdjustDto);
	}

	@Override
	public PageData<PayAccountAdjustDto> auditPageList(
			PayAccountAdjustDto payAccountAdjustDto,PageData<PayAccountAdjustDto> pageData)throws CbsCheckedException {
		return payAccountAdjustService.auditPageList(payAccountAdjustDto, pageData);
	}

	@Override
	public PayAccountAdjustDto auditDetails(String id) throws CbsCheckedException {
		return payAccountAdjustService.auditDetails(id);
	}

	@Override
	public ExportDto export(PayAccountAdjustDto queryParam) throws CbsCheckedException{
		return payAccountAdjustService.export(queryParam);
	}

	@Override
	public PageData<PayAccountAdjustDto> list(
			PageData<PayAccountAdjustDto> pageData,
			PayAccountAdjustDto queryParam) {
		return payAccountAdjustService.list(pageData,queryParam);
	}

	@Override
	public PayAccountAdjustDto getById(String id) {
		return payAccountAdjustService.getById(id);
	}

}
