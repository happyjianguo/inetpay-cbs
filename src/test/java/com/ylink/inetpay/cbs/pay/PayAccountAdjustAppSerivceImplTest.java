package com.ylink.inetpay.cbs.pay;

import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.cbs.OtherBaseTest;
import com.ylink.inetpay.common.core.constant.EAuditResult;
import com.ylink.inetpay.common.project.cbs.app.PayAccountAdjustAppService;
import com.ylink.inetpay.common.project.cbs.exception.CbsCheckedException;
import com.ylink.inetpay.common.project.pay.dto.PayAccountAdjustDto;
import com.ylink.inetpay.common.project.pay.exception.PayCheckedException;

/**
 * 账户调账测试类
 * @author lyg
 *
 */
public class PayAccountAdjustAppSerivceImplTest extends OtherBaseTest{
	@Autowired
	private PayAccountAdjustAppService payAccountAdjustAppService;
	@Test
	public void addNotesTest() throws CbsCheckedException, PayCheckedException{
		PayAccountAdjustDto dto = new PayAccountAdjustDto();
		dto.setDrAccountId("20110011000000105");
		dto.setCrAccountId("20110021000000106");
		dto.setAmount(100l);
		dto.setOperReason("调账原因");
		dto.setLoginName("yanggang");
		dto.setRealName("阳刚");
		dto.setOperDate(new Date());
		payAccountAdjustAppService.addNotes(dto);
	}
	@Test
	public void auditPassTest() throws CbsCheckedException, PayCheckedException{
		PayAccountAdjustDto dto = new PayAccountAdjustDto();
		dto.setId("444");
		dto.setDrAccountId("444");
		dto.setAuditLoginName("yanggang");
		dto.setAuditRealName("阳刚");
		dto.setAuditRemark("审核备注");
		dto.setAuditDate(new Date());
		dto.setAuditResult(EAuditResult.AUDIT_PASS);
		payAccountAdjustAppService.auditPass(dto);
	}
	@Test
	public void auditPageList() throws CbsCheckedException{
		PayAccountAdjustDto dto1 = new PayAccountAdjustDto();
		//dto1.setAuditResult(EAuditResult.WAIT_AUDIT);
		PageData<PayAccountAdjustDto> auditPageList = payAccountAdjustAppService.auditPageList(dto1, new PageData<PayAccountAdjustDto>());
		for (PayAccountAdjustDto dto : auditPageList.getRows()) {
			System.out.println(dto);
		}
	}
	@Test
	public void auditDetails() throws CbsCheckedException{
		PayAccountAdjustDto auditDetails = payAccountAdjustAppService.auditDetails("444");
		System.out.println(auditDetails);
	}
}
