package com.ylink.inetpay.cbs.cls.service;

import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.common.project.clear.dto.ClsClearJob;

/**
 * @类名称： ClsMerSettleService
 * @类描述： 清算任务监控接口服务类
 * @创建人： 1603254
 * @创建时间： 2016-5-25 下午2:35:48
 *
 * @修改人： 1603254
 * @操作时间： 2016-5-25 下午2:35:48
 * @操作原因： 
 * 
 */
public interface ClearJobService {

	/**
	 * @方法描述: 分页查询清分监控任务接口
	 * @作者： 徐浩南
	 * @日期： 2016-5-25-下午2:37:07
	 * @param pageDate
	 * @param settle
	 * @return 
	 * @返回类型： PageData<ClsClearJob>
	*/
	public PageData<ClsClearJob> queryClearJob(PageData<ClsClearJob> pageData,
			ClsClearJob clsClearJob);
	/**
	 * 查询情分任务详情
	 * @param id
	 * @return
	 */
	public ClsClearJob detail(String id);
}
