package com.ylink.inetpay.cbs.cls.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.cbs.cls.service.ClsAcctCheckService;
import com.ylink.inetpay.common.core.constant.EProcessStatus;
import com.ylink.inetpay.common.core.constant.EAutoManual;
import com.ylink.inetpay.common.core.constant.ESuccessFailDealing;
import com.ylink.inetpay.common.core.dto.SuccessFailDealingDto;
import com.ylink.inetpay.common.project.cbs.app.ClsAcctCheckAppService;
import com.ylink.inetpay.common.project.clear.app.ClearInnerCheckAppService;
import com.ylink.inetpay.common.project.clear.dto.ClsAcctCheck;

/**
 * @类名称： ClsAcctCheckAppServiceImpl
 * @类描述： 内部对账监控服务类
 * @创建人： 1603254
 * @创建时间： 2016-5-27 下午4:05:32
 *
 * @修改人： 1603254
 * @操作时间： 2016-5-27 下午4:05:32
 * @操作原因： 
 * 
 */
@Service("clsAcctCheckAppService")
public class ClsAcctCheckAppServiceImpl implements ClsAcctCheckAppService {

	@Autowired
	private ClsAcctCheckService acctCheckService;
	@Autowired
	private ClearInnerCheckAppService innerCheckAppService;
	
	/**
	 * @方法描述:  查询对账监控的记录
	 * @作者： 1603254
	 * @日期： 2016-5-27-下午4:06:40
	 * @param pageData
	 * @param book
	 * @return 
	 * @返回类型： PageData<ClsAcctBook>
	*/
	public PageData<ClsAcctCheck> findAcctCheck(PageData<ClsAcctCheck> pageData,
			ClsAcctCheck check){
		return acctCheckService.findAcctCheck(pageData, check);
	}
	
	
	
	/**
	 * @方法描述:  重新执行失败的记录
	 * @作者： 1603254
	 * @日期： 2016-5-27-下午4:07:19
	 * @param id
	 * @return 
	 * @返回类型： String
	*/
	public String reProceeFailRecord(String id){
		ClsAcctCheck check=acctCheckService.findById(id);
		if(check == null){
			return "记录不存在";
		}
		if(check.getDealStatus() == EProcessStatus.PROCESS_FAIL){
			SuccessFailDealingDto dto=innerCheckAppService.check(check.getCheckDay(),EAutoManual.MANUAL);
			if(dto.getSfd() == ESuccessFailDealing.FAIL){
				return "操作失败";
			}
		}
		
		return "操作成功";
	}



 
}
