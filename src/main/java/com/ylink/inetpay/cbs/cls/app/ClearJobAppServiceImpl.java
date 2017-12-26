package com.ylink.inetpay.cbs.cls.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.cbs.cls.service.ClearJobService;
import com.ylink.inetpay.common.project.cbs.app.ClearJobAppService;
import com.ylink.inetpay.common.project.clear.dto.ClsClearJob;

/**
 * @类名称： ClsMerchantSettleAppServiceImpl
 * @类描述： 
 * @创建人： 1603254
 * @创建时间： 2016-5-25 上午11:46:02
 *
 * @修改人： 1603254
 * @操作时间： 2016-5-25 上午11:46:02
 * @操作原因： 
 * 
 */
@Service("clearJobAppService")
public class ClearJobAppServiceImpl implements ClearJobAppService{
	@Autowired
	private ClearJobService clearJobService;
	/**
	 * 查询清分任务监控
	 */
	public PageData<ClsClearJob> findClearJob(PageData<ClsClearJob> pageData,
			ClsClearJob clsClearJob) {
		return clearJobService.queryClearJob(pageData, clsClearJob);
	}
	/***
	 * 查询清分任务详情
	 */
	public ClsClearJob detail(String id){
		return clearJobService.detail(id);
	}
}
