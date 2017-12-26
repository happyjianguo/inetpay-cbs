package com.ylink.inetpay.cbs.act.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.cbs.act.service.ActCheckStatusService;
import com.ylink.inetpay.common.core.constant.EAutoManual;
import com.ylink.inetpay.common.core.constant.EProcessStatus;
import com.ylink.inetpay.common.project.account.app.ActFileAppService;
import com.ylink.inetpay.common.project.account.dto.ActCheckFileDto;
import com.ylink.inetpay.common.project.cbs.app.ActCheckStatusAppService;



/**
 * @类名称： ActCheckStatusAppService
 * @类描述： 记账监控任务状态服务接口类
 * @创建人： 1603254
 * @创建时间： 2016-5-27 上午10:06:01
 *
 * @修改人： 1603254
 * @操作时间： 2016-5-27 上午10:06:01
 * @操作原因： 
 * 
 */
@Service("actCheckStatusAppService")
public class ActCheckStatusAppServiceImpl implements ActCheckStatusAppService {

	private static final Logger logger=LoggerFactory.getLogger(ActCheckStatusAppServiceImpl.class);
	@Autowired
	private ActCheckStatusService checkStatusService;
	@Autowired
	private ActFileAppService actFileAppService;

	
	/**
	 * @方法描述: 查询记账对账监控状态
	 * @作者： 1603254
	 * @日期： 2016-5-27-上午10:09:01
	 * @param pageData
	 * @param check
	 * @return 
	 * @返回类型： PageData<ActCheckFileDto>
	*/
	public PageData<ActCheckFileDto> findCheckStatus(PageData<ActCheckFileDto> pageData,
			ActCheckFileDto check){
		return checkStatusService.findCheckStatus(pageData, check);
	}
	
	
	/**
	 * @方法描述: 重新执行失败的记录
	 * @作者： 1603254
	 * @日期： 2016-5-27-上午10:01:06
	 * @param id 
	 * @返回类型： void
	*/
	public String reProcessFailRecord(String id){
		ActCheckFileDto dto=checkStatusService.findById(id);
		if(dto == null){
			return "记录不存在";
		}
		if("1".equals(dto.getUploadStatus()) &&
				dto.getDealStatus() == EProcessStatus.PROCESS_FAIL){
			actFileAppService.uploadFile(dto.getAccountDay(),EAutoManual.MANUAL);
		}
		return "操作成功";
		
	}
	
}