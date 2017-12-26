package com.ylink.inetpay.cbs.cls.service;


import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.common.project.clear.dto.ClsPayBook;


/**
 * @类名称： ClsMerSettleService
 * @类描述： 清分订单接口服务类
 * @创建人： 1603254
 * @创建时间： 2016-5-25 下午2:35:48
 *
 * @修改人： 1603254
 * @操作时间： 2016-5-25 下午2:35:48
 * @操作原因： 
 * 
 */
public interface ClsPayBookService {

	/**
	 * @方法描述: 分页查询支付流水
	 * @作者： 1603254
	 * @日期： 2016-5-25-下午2:37:07
	 * @param pageDate
	 * @param settle
	 * @return 
	 * @返回类型： PageData<ClsClearOrder>
	*/
	public PageData<ClsPayBook> queryClsPayBook(PageData<ClsPayBook> pageData,
			ClsPayBook clsPayBook);
}
