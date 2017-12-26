package com.ylink.inetpay.cbs.cls.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.cbs.cls.service.ClsMerCheckStatusService;
import com.ylink.inetpay.common.core.constant.EAutoManual;
import com.ylink.inetpay.common.core.constant.EProcessStatus;
import com.ylink.inetpay.common.core.constant.ESuccessFailDealing;
import com.ylink.inetpay.common.core.dto.SuccessFailDealingDto;
import com.ylink.inetpay.common.project.cbs.app.ClsMerCheckStatusAppService;
import com.ylink.inetpay.common.project.clear.app.ClearMerchantAppService;
import com.ylink.inetpay.common.project.clear.dto.ClsAccessCheck;

@Service("clsMerCheckStatusAppService")
public class ClsMerCheckStatusAppServiceImpl  implements ClsMerCheckStatusAppService{

	@Autowired
	private ClsMerCheckStatusService checkStatusService;
	@Autowired
	private ClearMerchantAppService merchantAppService;
	
	/**
	 * @方法描述: 查询记账对账监控状态
	 * @作者： 1603254
	 * @日期： 2016-5-27-上午10:09:01
	 * @param pageData
	 * @param check
	 * @return 
	 * @返回类型： PageData<ActCheckFileDto>
	*/
	public PageData<ClsAccessCheck> findCheckStatus(PageData<ClsAccessCheck> pageData,
			ClsAccessCheck check){
		return  checkStatusService.queryMerCheck(pageData, check);	
	}
	
	
	/**
	 * @方法描述: 重新执行失败的记录
	 * @作者： 1603254
	 * @日期： 2016-5-27-上午10:01:06
	 * @param id 
	 * @返回类型： void
	*/
	public String reProcessFailRecord(String id){
		ClsAccessCheck check=checkStatusService.queryById(id);
		if(check == null){
			return "记录不存在";
		}
		if(check.getDealStatus() == EProcessStatus.PROCESS_FAIL){
			SuccessFailDealingDto dto=merchantAppService.uploadMerchantCheckFile(check.getFileDay(),EAutoManual.MANUAL);
			if(dto.getSfd() == ESuccessFailDealing.FAIL){
				return "操作失败";
			}
		}
		return "操作成功";
	}
	
}
