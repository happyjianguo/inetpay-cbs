package com.ylink.inetpay.cbs.mrs.App;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.cbs.mrs.service.MrsConfReviewService;
import com.ylink.inetpay.common.core.constant.EConfReviewBusiType;
import com.ylink.inetpay.common.project.cbs.app.MrsConfReviewAppService;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsConfReviewDto;
/**
 * 
 * @author pst10
 * 类名称：MrsConfReviewAppServiceImpl
 * 类描述：机构复核业务操作服务
 * 创建时间：2017年5月10日 上午11:10:21
 */
@Service("mrsConfReviewAppService")
public class MrsConfReviewAppServiceImpl implements MrsConfReviewAppService{

	@Autowired
	MrsConfReviewService mrsConfReviewService;

	@Override
	public PageData<MrsConfReviewDto> findPage(PageData<MrsConfReviewDto> pageData, MrsConfReviewDto searchDto) {
		return mrsConfReviewService.findPage(pageData, searchDto);
	}

	@Override
	public MrsConfReviewDto selectById(String id) {
		return mrsConfReviewService.selectById(id);
	}

	@Override
	public void addOrUpdateConfReview(MrsConfReviewDto mrsConfReviewDto) {
		mrsConfReviewService.addOrUpdateConfReview(mrsConfReviewDto);
	}

	@Override
	public MrsConfReviewDto selectByCustId(String custId) {
		return mrsConfReviewService.selectByCustId(custId);
	}

	@Override
	public boolean checkByCustIdAndId(String custId, String id) {
		return mrsConfReviewService.checkByCustIdAndId(custId, id);
	}

	@Override
	public boolean checkByCustIdAndType(String custId, EConfReviewBusiType busiType) {
		return mrsConfReviewService.checkByCustIdAndType(custId,busiType);
	}
	

}
