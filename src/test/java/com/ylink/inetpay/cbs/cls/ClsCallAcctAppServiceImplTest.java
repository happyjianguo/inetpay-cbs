package com.ylink.inetpay.cbs.cls;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.ylinkpay.framework.core.model.PageData;
import com.ylink.inetpay.cbs.OtherBaseTest;
import com.ylink.inetpay.common.project.cbs.app.ClsCallAcctAppService;
import com.ylink.inetpay.common.project.cbs.exception.CbsCheckedException;
import com.ylink.inetpay.common.project.clear.dto.ClsCallAcct;

/**
 * 备付金调账测试类
 * @author lyg
 *
 */
public class ClsCallAcctAppServiceImplTest extends OtherBaseTest {
	@Autowired
	private ClsCallAcctAppService clsCallAcctAppService;
	@Test
	public void auditPageListTest() throws CbsCheckedException{
		PageData<ClsCallAcct> pageAuditPageList = clsCallAcctAppService.pageAuditPageList(new ClsCallAcct(), new PageData<ClsCallAcct>());
		for (ClsCallAcct dto : pageAuditPageList.getRows()) {
			System.out.println(dto);
		}
	}
	@Test
	public void pageAuditResultList() throws CbsCheckedException{
		PageData<ClsCallAcct> pageAuditPageList = clsCallAcctAppService.pageAuditResultPageList(new ClsCallAcct(), new PageData<ClsCallAcct>());
		for (ClsCallAcct dto : pageAuditPageList.getRows()) {
			System.out.println(dto);
		}
	}
	@Test
	public void pagePayList() throws CbsCheckedException{
		PageData<ClsCallAcct> pageAuditPageList = clsCallAcctAppService.pagePayPageList(new ClsCallAcct(), new PageData<ClsCallAcct>());
		for (ClsCallAcct dto : pageAuditPageList.getRows()) {
			System.out.println(dto);
		}
	}
	@Test
	public void details() throws CbsCheckedException{
		ClsCallAcct details = clsCallAcctAppService.details("b493393c-a181-44e9-a612-dfcd0cc60625");
		System.out.println(details);
		
	}
}
