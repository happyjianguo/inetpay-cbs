package com.ylink.inetpay.cbs.cls.app;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.cbs.cls.service.ClsSettOrderService;
import com.ylink.inetpay.common.core.constant.BISAuditStatus;
import com.ylink.inetpay.common.core.constant.CLSReviewStatus;
import com.ylink.inetpay.common.project.cbs.app.ClsSettOrderAppService;
import com.ylink.inetpay.common.project.cbs.dto.ucs.sec.UcsSecUserDto;
import com.ylink.inetpay.common.project.cbs.exception.CbsCheckedException;
import com.ylink.inetpay.common.project.clear.dto.ClsAuditDto;
import com.ylink.inetpay.common.project.clear.dto.ClsSettOrder;

@Service("clsSettOrderAppService")
public class ClsSettOrderAppServiceImpl implements ClsSettOrderAppService{
	
	@Autowired
	private ClsSettOrderService clsSettOrderService;

	public PageData<ClsSettOrder> findClsSettOrder(PageData<ClsSettOrder> pageData, ClsSettOrder clsSettOrder) {
		return clsSettOrderService.queryClsSettOrder(pageData, clsSettOrder);
	}

	public ClsSettOrder details(String id) {
		return clsSettOrderService.details(id);
	}

	/**查询复核数据 **/
	public ClsAuditDto queryCheckData(UcsSecUserDto currentUser,String id,BISAuditStatus auditStatus) throws CbsCheckedException{
		return clsSettOrderService.queryCheckData(currentUser, id, auditStatus);
	}

	@Override
	public void settleOrderBatchAudit(UcsSecUserDto currentUser, List<String> ids,
			BISAuditStatus auditPass, CLSReviewStatus pass) throws CbsCheckedException {
		clsSettOrderService.settleOrderBatchAudit(currentUser,ids,auditPass,pass);
	}
}
