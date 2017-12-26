package com.ylink.inetpay.cbs.cls.service;

import com.ylink.inetpay.common.project.clear.dto.ClsReviewVo;

/**
 * @类名称： ClsRecordCheckService
 * @类描述： 记录审核表服务接口类 
 * @创建人： 1603254
 * @创建时间： 2016-5-25 上午11:04:43
 *
 * @修改人： 1603254
 * @操作时间： 2016-5-25 上午11:04:43
 * @操作原因： 
 * 
 */
public interface ClsRecordCheckService {
 
	/**
	 * @方法描述:  获取审核记录条数
	 * @作者： 1603254
	 * @日期： 2016-6-1-上午11:49:27
	 * @return 
	 * @返回类型： Integer
	*/
	public ClsReviewVo queryReviewCount();
}
