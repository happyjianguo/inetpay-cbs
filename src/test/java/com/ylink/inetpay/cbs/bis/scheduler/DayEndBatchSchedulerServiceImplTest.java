package com.ylink.inetpay.cbs.bis.scheduler;

import com.ylink.inetpay.cbs.bis.service.SchedulerService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ylink.inetpay.cbs.UCBaseTest;
import com.ylink.inetpay.cbs.bis.service.AbstractSchedulerService;
import com.ylink.inetpay.common.core.constant.EAutoManual;
import com.ylink.inetpay.common.project.clear.app.ClearInnerCheckAppService;

import javax.annotation.Resource;

public class DayEndBatchSchedulerServiceImplTest extends UCBaseTest {

	@Autowired
	AbstractSchedulerService dayEndBatchSchedulerService;
	@Autowired
	ClearInnerCheckAppService clearInnerCheckAppService;

    @Resource(name="dayEndSettlementSchedulerService")
    private SchedulerService schedulerService;

    @Test
	public void testExecute() throws InterruptedException {
//		dayEndBatchSchedulerService.execute();
	//	System.out.println("#################"+clearInnerCheckAppService.check("20160813",EAutoManual.AUTO));
		//Thread.sleep(10000000);
        schedulerService.execute();
	}
}
