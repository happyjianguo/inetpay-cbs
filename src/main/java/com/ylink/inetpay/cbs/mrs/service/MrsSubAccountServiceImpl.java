package com.ylink.inetpay.cbs.mrs.service;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ylink.inetpay.cbs.common.CbsConstants;
import com.ylink.inetpay.cbs.mrs.dao.MrsSubAccountDtoMapper;
import com.ylink.inetpay.common.core.constant.MrsSubAccountStatus;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsSubAccountDto;

@Service("mrsSubAccountService")
public class MrsSubAccountServiceImpl implements MrsSubAccountService {

	@Autowired
	private MrsSubAccountDtoMapper mrsSubAccountDtoMapper;
	
	@Override
	@Transactional(value = CbsConstants.TX_MANAGER_MRS)
	public int insertSelective(MrsSubAccountDto dto) {
		return mrsSubAccountDtoMapper.insert(dto);
	}
	
	@Override
	public List<MrsSubAccountDto> findByCustId(String custId) {
		if(StringUtils.isBlank(custId)){
			return null;
		}
		return mrsSubAccountDtoMapper.findByCustId(custId);
	}

	@Override
	public List<MrsSubAccountDto> findByCustIdAndSubAccountType(MrsSubAccountDto mrsSubAccountDto) {
		return mrsSubAccountDtoMapper.findByCustIdAndSubAccountType(mrsSubAccountDto);
	}

	@Override
	@Transactional(value = CbsConstants.TX_MANAGER_MRS)
	public int updateSelective(MrsSubAccountDto dto) {
		return mrsSubAccountDtoMapper.updateByPrimaryKey(dto);
	}

	@Override
	@Transactional(value = CbsConstants.TX_MANAGER_MRS)
	public void updateByCustId(String custId, String subAccountStatus, Date updateTime) {
		mrsSubAccountDtoMapper.updateByCustId(custId, subAccountStatus, updateTime);
	}
	@Override
	@Transactional(value = CbsConstants.TX_MANAGER_MRS)
	public void updateByCustIdAndSubType(String custId, String subAccountType,String subAccountStatus, Date updateTime) {
		mrsSubAccountDtoMapper.updateByCustIdAndSubType(custId,subAccountType, subAccountStatus, updateTime);
	}

	@Override
	public MrsSubAccountDto findByAccountNameandCustId(String accountName, String custId) {
		return mrsSubAccountDtoMapper.findByAccountNameandCustId(accountName, custId);
	}

	@Override
	public boolean checkSubAccountStatus(List<MrsSubAccountDto> list) {
		for(MrsSubAccountDto dto:list){
			if(!dto.getSubAccountStatus().equals(MrsSubAccountStatus.MSAS_0.getValue())){
				return false;
			}
		}
		return true;
	}

	@Override
	public MrsSubAccountDto findSubAccountStatusByCustIdAndType(String custId) {
		return mrsSubAccountDtoMapper.findSubAccountStatusByCustIdAndType(custId);
	}


 }
