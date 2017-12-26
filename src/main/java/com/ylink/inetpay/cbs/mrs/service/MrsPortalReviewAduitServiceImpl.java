package com.ylink.inetpay.cbs.mrs.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ylink.inetpay.cbs.mrs.dao.MrsPortalReviewAduitDtoMapper;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsPortalReviewAduitDto;

@Service("mrsPortalReviewAduitService")
public class MrsPortalReviewAduitServiceImpl implements MrsPortalReviewAduitService{
	
	@Autowired
	private MrsPortalReviewAduitDtoMapper mrsPortalReviewAduitDtoMapper;

	@Override
	public MrsPortalReviewAduitDto findByBusiNoAndType(String busiNo, String busiType) {
		return mrsPortalReviewAduitDtoMapper.selectByBusiNoAndType(busiNo, busiType);
	}

	@Override
	public MrsPortalReviewAduitDto findByBusiNoAndTypeWait(String busiNo, String busiType) {
		return mrsPortalReviewAduitDtoMapper.selectByBusiNoAndTypeWait(busiNo, busiType);
	}

}
