package com.ylink.inetpay.cbs.mrs.App;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ylink.inetpay.cbs.mrs.service.MrsSubAccountService;
import com.ylink.inetpay.common.project.cbs.app.MrsSubAccountAppService;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsSubAccountDto;

@Service("mrsSubAccountAppService")
public class MrsSubAccountAppServiceImpl implements MrsSubAccountAppService {

	@Autowired
	private MrsSubAccountService mrsSubAccountService;
	
	
	@Override
	public int insertSelective(MrsSubAccountDto dto) {
		return mrsSubAccountService.insertSelective(dto);
	}
	
	
	@Override
	public List<MrsSubAccountDto> findByCustId(String custId) {
		return mrsSubAccountService.findByCustId(custId);
	}


	@Override
	public List<MrsSubAccountDto> findByCustIdAndSubAccountType(MrsSubAccountDto mrsSubAccountDto) {
		return mrsSubAccountService.findByCustIdAndSubAccountType(mrsSubAccountDto);
	}


	@Override
	public int updateSelective(MrsSubAccountDto dto) {
		return mrsSubAccountService.updateSelective(dto);
	}


	@Override
	public MrsSubAccountDto findByAccountNameandCustId(String accountName, String custId) {
		return mrsSubAccountService.findByAccountNameandCustId(accountName, custId);
	}


	@Override
	public boolean checkSubAccountStatus(List<MrsSubAccountDto> list) {
		return mrsSubAccountService.checkSubAccountStatus(list);
	}

}
