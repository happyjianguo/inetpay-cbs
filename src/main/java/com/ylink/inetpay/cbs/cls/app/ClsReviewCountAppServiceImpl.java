package com.ylink.inetpay.cbs.cls.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ylink.inetpay.cbs.cls.service.ClsRecordCheckService;
import com.ylink.inetpay.cbs.pay.service.PayAccountAdjustService;
import com.ylink.inetpay.common.project.cbs.app.ClsReviewCountAppService;
import com.ylink.inetpay.common.project.clear.dto.ClsReviewVo;


/**
 * @类名称： ClsReviewCountAppService
 * @类描述： 审核记录条数查询
 * @创建人： 1603254
 * @创建时间： 2016-6-1 上午11:00:04
 *
 * @修改人： 1603254
 * @操作时间： 2016-6-1 上午11:00:04
 * @操作原因： 
 * 
 */
@Service("clsReviewCountAppService")
public class ClsReviewCountAppServiceImpl implements ClsReviewCountAppService {

	@Autowired
	private ClsRecordCheckService recordCheckService;
	@Autowired
	private PayAccountAdjustService payAccountAdjustService;
	
	/**
	 * @方法描述: 查找各个业务类型未处理的记录条数
	 * @作者： 1603254
	 * @日期： 2016-6-1-上午11:05:56
	 * @return 
	 * @返回类型： ClsReviewVo
	*/
	public ClsReviewVo findRecord(){
		ClsReviewVo record=recordCheckService.queryReviewCount();
		record.setTransferAdjustCount(payAccountAdjustService.queryAdjustCountByAdjust());
		record.setTransferRecoverCount(payAccountAdjustService.queryAdjustCountByRecover());
		return record;
	}
}