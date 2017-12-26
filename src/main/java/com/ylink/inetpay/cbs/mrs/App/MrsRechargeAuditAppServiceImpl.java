package com.ylink.inetpay.cbs.mrs.App;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.ylink.inetpay.cbs.mrs.service.MrsRechargeAuditService;
import com.ylink.inetpay.common.project.cbs.app.MrsRechargeAuditAppService;
import com.ylink.inetpay.common.project.cbs.constant.mrs.AduitBusiType;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsLoginUserDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsPortalReviewAduitDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsRechargeAduitDto;

@Service("mrsRechargeAuditAppService")
public class MrsRechargeAuditAppServiceImpl implements MrsRechargeAuditAppService {
	@Autowired
	MrsRechargeAuditService mrsRechargeAuditService;
	@Override
	public PageData<MrsRechargeAduitDto> queryAllData(PageData<MrsRechargeAduitDto> pageDate,
			MrsRechargeAduitDto mrsRechargeAduitDto) {
		return mrsRechargeAuditService.queryAllData(pageDate, mrsRechargeAduitDto);
	}
	
	@Override
	public MrsRechargeAduitDto selectByPrimaryKey(String id) {
		return mrsRechargeAuditService.selectByPrimaryKey(id);
	}

	@Override
	public void deleteByPrimaryKey(String id) {
		mrsRechargeAuditService.deleteByPrimaryKey(id);
	}

	@Override
	public void saveRechargeAudit(MrsRechargeAduitDto dto,MrsLoginUserDto mrsLoginUserDto){
		//审核表
		MrsPortalReviewAduitDto mrsPortalReviewAduitDto = new MrsPortalReviewAduitDto();
		mrsPortalReviewAduitDto.setCustId(dto.getCustId());
		mrsPortalReviewAduitDto.setBusiType(AduitBusiType.AB0.getValue());
		mrsPortalReviewAduitDto.setCreateUserName(mrsLoginUserDto.getCustomerName());
		mrsPortalReviewAduitDto.setCreateUserNo(mrsLoginUserDto.getId());
		mrsRechargeAuditService.saveRechargeAudit(dto,mrsPortalReviewAduitDto);
	}
	@Override
	public void updataRechargeAudit(MrsRechargeAduitDto dto,MrsLoginUserDto mrsLoginUserDto){
		//审核表
		MrsPortalReviewAduitDto mrsPortalReviewAduitDto = new MrsPortalReviewAduitDto();
		mrsPortalReviewAduitDto.setAduitUserName(getLoginName(mrsLoginUserDto));
		mrsPortalReviewAduitDto.setAduitUserId(mrsLoginUserDto.getId());
		mrsPortalReviewAduitDto.setAduitStatus(dto.getAduitStatus().getValue());
		mrsPortalReviewAduitDto.setAduitRemark(dto.getAduitRemark());
		mrsRechargeAuditService.updateRechargeAudit(dto,mrsPortalReviewAduitDto);
	}
	
	/**
	 * 获取登录用户
	 * @param request
	 * @return
	 */
	private String getLoginName(MrsLoginUserDto mrsLoginUserDto){
		if(StringUtils.isNotEmpty(mrsLoginUserDto.getAlias())){
			return 	mrsLoginUserDto.getAlias();
		}
		if(StringUtils.isNotEmpty(mrsLoginUserDto.getEmail())){
			return 	mrsLoginUserDto.getEmail();
		}
		if(StringUtils.isNotEmpty(mrsLoginUserDto.getMobile())){
			return 	mrsLoginUserDto.getMobile();
		}
		return "";
	}

	@Override
	public int insertSelective(MrsRechargeAduitDto dto) {
		return mrsRechargeAuditService.insertSelective(dto);
	}

	@Override
	public int updateByPrimaryKeySelective(MrsRechargeAduitDto mrsAduitInfoDto) {
		return mrsRechargeAuditService.updateByPrimaryKeySelective(mrsAduitInfoDto);
	}

}
