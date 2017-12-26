package com.ylink.inetpay.cbs.mrs.App;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ylink.inetpay.cbs.mrs.service.MrsPortalReviewAduitService;
import com.ylink.inetpay.common.project.cbs.app.MrsPortalReviewAduitAppService;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsPortalReviewAduitDto;

@Service("mrsPortalReviewAduitAppService")
public class MrsPortalReviewAduitAppServiceImpl implements MrsPortalReviewAduitAppService{
	
	@Autowired
	private MrsPortalReviewAduitService mrsPortalReviewAduitService;

	@Override
	public MrsPortalReviewAduitDto findByBusiNoAndType(String busiNo, String busiType) {
		return mrsPortalReviewAduitService.findByBusiNoAndType(busiNo, busiType);
	}

	@Override
	public MrsPortalReviewAduitDto findByBusiNoAndTypeWait(String busiNo, String busiType) {
		return mrsPortalReviewAduitService.findByBusiNoAndTypeWait(busiNo, busiType);
	}
}
