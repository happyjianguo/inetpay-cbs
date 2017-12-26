package com.ylink.inetpay.cbs.cls.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.cbs.cls.service.ClsBankCheckService;
import com.ylink.inetpay.common.core.constant.CLSBankCheckStatus;
import com.ylink.inetpay.common.core.constant.CLSReviewStatus;
import com.ylink.inetpay.common.core.constant.EProcessStatus;
import com.ylink.inetpay.common.core.constant.ESuccessFailDealing;
import com.ylink.inetpay.common.core.dto.SuccessFailDealingDto;
import com.ylink.inetpay.common.project.cbs.app.ClsBankCheckAppService;
import com.ylink.inetpay.common.project.clear.app.ClearChannelCheckAppService;
import com.ylink.inetpay.common.project.clear.dto.ClsBankCheck;

/**
 * @类名称： ClsChannelStatusAppService
 * @类描述： 资金渠道文件监控管理
 * @创建人： 1603254
 * @创建时间： 2016-5-27 下午2:47:52
 *
 * @修改人： 1603254
 * @操作时间： 2016-5-27 下午2:47:52
 * @操作原因： 
 * 
 */
@Service("clsBankCheckAppService")
public class ClsBankCheckAppServiceImpl  implements ClsBankCheckAppService{

	@Autowired
	private ClsBankCheckService bankCheckService;
	@Autowired
	private  ClearChannelCheckAppService channelCheckAppService;
	
	private static final Logger logger=LoggerFactory.getLogger(ClsBankCheckAppServiceImpl.class);
	

	/**
	 * @方法描述:  查询资金
	 * @作者： 1603254
	 * @日期： 2016-5-27-下午2:54:25
	 * @param pageData
	 * @param check
	 * @return 
	 * @返回类型： ClsBankCheck
	 */
	public PageData<ClsBankCheck> findBankCheck(PageData<ClsBankCheck> pageData,ClsBankCheck check){
		return bankCheckService.findBankCheck(pageData, check);
	}
	
	
	/**
	 * @方法描述: 重新执行失败的记录
	 * @作者： 1603254
	 * @日期： 2016-5-27-下午2:55:45
	 * @param id
	 * @return 
	 * @返回类型： String
	*/
	public String reProcessFailRecord(String id) {
		ClsBankCheck check = bankCheckService.findById(id);
		if (check == null) {
			return "记录不存在";
		}
		if (check.getMoniStatus() == CLSBankCheckStatus.DOWNLOAD_PRASE
				&& check.getDealStatus() == EProcessStatus.PROCESS_FAIL) {
			SuccessFailDealingDto dto = channelCheckAppService.downLoadAndParseBillById(id);
			if (dto.getSfd() == ESuccessFailDealing.FAIL) {
				return "操作失败";
			}
		}
		return "操作完毕";
	}
	
	@Override
	public boolean isEqual(String keyId, String currentUserLoginName,
			CLSReviewStatus wait) {
		return bankCheckService.isEqual(keyId,currentUserLoginName,wait);
	}
}
