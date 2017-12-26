package com.ylink.inetpay.cbs.bis.service;

import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ylink.inetpay.cbs.UCBaseTest;
import com.ylink.inetpay.common.core.constant.EIsFinished;
import com.ylink.inetpay.common.core.constant.ESchedJobQueueStatus;
import com.ylink.inetpay.common.core.constant.ESchedPlanType;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisSchedPlanDetailDto;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisSchedPlanDto;

public class BisSchedPlanDetailServiceTest extends UCBaseTest {

	@Autowired
	BisSchedPlanDetailService bisSchedPlanDetailService;
	
	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testUpdateJobDetail() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetJobDetail() {
		fail("Not yet implemented");
	}

	@Test
	public void testUpdateJobPlanDetailStatusStringESchedJobQueueStatus() {
		fail("Not yet implemented");
	}

	@Test
	public void testUpdateJobPlanDetailStatusStringBooleanString() {
		fail("Not yet implemented");
	}

	@Test
	public void testUpdateDetailStatus() {
		fail("Not yet implemented");
	}

	@Test
	public void testListRelyOnUnsuccessful() {
		fail("Not yet implemented");
	}

	@Test
	public void testListJobPlanDetail() {
		fail("Not yet implemented");
	}

	@Test
	public void testSave() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testSaveList() {
		List<BisSchedPlanDetailDto> details = new ArrayList<BisSchedPlanDetailDto>();
		//日切
		int i = 0;
		BisSchedPlanDetailDto daycut = createBisSchedPlanDetailDto("abc",  ++i, null, 
				ESchedPlanType.DAY_CUT, "日切", "A");
		details.add(daycut);
		
		//试算平衡
		BisSchedPlanDetailDto trialBalance = createBisSchedPlanDetailDto("abc", ++i, 
				daycut.getId(), ESchedPlanType.TRIAL_BALANCE, "试算平衡","A");
		details.add(trialBalance);
		
		bisSchedPlanDetailService.saveList(details);
	}

	private BisSchedPlanDetailDto createBisSchedPlanDetailDto(String planId,
			int seq,String preDetailId,ESchedPlanType planDetailType,String description,String group){
		BisSchedPlanDetailDto planDetailDto = new BisSchedPlanDetailDto();
		planDetailDto.setId(planId + "-" + seq);
		planDetailDto.setPlanId(planId);
		planDetailDto.setPlanType(ESchedPlanType.DAY_SCHED_BATCH_DEAL);
		planDetailDto.setPlanDetailType(planDetailType);
		planDetailDto.setPreDetailId(preDetailId);
		planDetailDto.setStatus(ESchedJobQueueStatus.NEW);
		planDetailDto.setErrMsg("");
		planDetailDto.setFailureCount(0);
		planDetailDto.setFinished(EIsFinished.UNFINISHED);
		planDetailDto.setGroupName(group);
		planDetailDto.setAccountDate("20160403");
		planDetailDto.setDescription(description);
		planDetailDto.setCreateTime(new Date());
		return planDetailDto;
	}

	@Test
	public void testCountUnSuccessPlanDetails() {
		fail("Not yet implemented");
	}

	@Test
	public void testCountUnSuccessPlanDetailsByDetailIds() {
		fail("Not yet implemented");
	}

}
