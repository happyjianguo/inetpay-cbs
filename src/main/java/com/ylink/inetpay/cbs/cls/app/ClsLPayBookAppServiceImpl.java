package com.ylink.inetpay.cbs.cls.app;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.cbs.cls.service.ClsPayBookService;
import com.ylink.inetpay.common.project.cbs.app.ClsPayBookAppService;
import com.ylink.inetpay.common.project.clear.dto.ClsPayBook;

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
@Service("clsPayBookAppService")
public class ClsLPayBookAppServiceImpl implements ClsPayBookAppService{

	@Autowired
	private ClsPayBookService clsPayBookService;
	public PageData<ClsPayBook> findClsPayBook(PageData<ClsPayBook> pageData, ClsPayBook clsPayBook) {
		return clsPayBookService.queryClsPayBook(pageData, clsPayBook);
	}
}	
