package com.ylink.inetpay.cbs.cls.service;

import java.util.List;

import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.common.core.constant.BISAuditStatus;
import com.ylink.inetpay.common.core.constant.CLSReviewStatus;
import com.ylink.inetpay.common.project.cbs.dto.ucs.sec.UcsSecUserDto;
import com.ylink.inetpay.common.project.clear.dto.ClsAuditDto;
import com.ylink.inetpay.common.project.clear.dto.ClsSettOrder;

/**
 * @类名称： ClsMerSettleService
 * @类描述： 结算订单接口服务类
 * @创建人： 1603254
 * @创建时间： 2016-5-25 下午2:35:48
 *
 * @修改人： 1603254
 * @操作时间： 2016-5-25 下午2:35:48
 * @操作原因： 
 * 
 */
public interface ClsSettOrderService {

	/**
	 * @方法描述: 分页查询清分订单
	 * @作者： 1603254
	 * @日期： 2016-5-25-下午2:37:07
	 * @param pageDate
	 * @param settle
	 * @return 
	 * @返回类型： PageData<ClsClearOrder>
	*/
	public PageData<ClsSettOrder> queryClsSettOrder(PageData<ClsSettOrder> pageDate,
			ClsSettOrder clsSettOrder);
	public ClsSettOrder details(String id);
	/**
	 * 插入复核数据
	 */
	public ClsAuditDto queryCheckData(UcsSecUserDto user,String id,BISAuditStatus auditStatus);
	
	public void settleOrderBatchAudit(UcsSecUserDto currentUser, List<String> ids,
			BISAuditStatus auditPass, CLSReviewStatus pass);
}
