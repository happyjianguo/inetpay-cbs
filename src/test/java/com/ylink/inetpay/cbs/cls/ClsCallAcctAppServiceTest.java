package com.ylink.inetpay.cbs.cls;

import com.ylink.inetpay.common.project.clear.app.ClearCATAppService;
import com.ylink.inetpay.common.project.clear.exception.ClearCheckedException;
import com.ylink.inetpay.common.project.clear.exception.EClearCommonResult;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ylink.inetpay.cbs.OtherBaseTest;
import com.ylink.inetpay.common.project.cbs.app.ClsCallAcctAppService;
import com.ylink.inetpay.common.project.cbs.exception.CbsCheckedException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;

public class ClsCallAcctAppServiceTest extends OtherBaseTest{

	@Autowired
	private ClsCallAcctAppService acctAppService;

    @Autowired
    @Qualifier("clearCATAppService")
    private ClearCATAppService clearCATAppService;


	@Test
	public void test(){
		try {
			acctAppService.againPay("2cd14d5b-519a-47cf-b20f-108fadfe2851");
		} catch (CbsCheckedException e) {
			e.printStackTrace();
		}
	}

    @Test
    public void test111() throws ClearCheckedException, InterruptedException {
        for (int i=0;i<10;i++ ) {
            try {
                EClearCommonResult rssult= clearCATAppService.clear();
                System.out.println("调用结束："+rssult.getDisplayName());
            } catch (ClearCheckedException e) {
                e.printStackTrace();
            }

        }

    }
}
