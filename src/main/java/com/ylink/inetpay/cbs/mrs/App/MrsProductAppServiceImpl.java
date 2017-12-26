package com.ylink.inetpay.cbs.mrs.App;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.cbs.mrs.service.MrsProductService;
import com.ylink.inetpay.common.project.cbs.app.MrsProductAppService;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsAccountDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsLoginUserDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsProductDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsSubAccountDto;
import com.ylink.inetpay.common.project.cbs.vo.mrs.MrsActAccountVo;
import com.ylink.inetpay.common.project.cbs.vo.mrs.MrsProductVo;
import com.ylink.inetpay.common.project.cbs.vo.mrs.MrsRemoveAccountVo;
import com.ylink.inetpay.common.project.cbs.vo.mrs.SaveAduitProductResponseVo;

@Service("mrsProductAppService")
public class MrsProductAppServiceImpl implements MrsProductAppService{
    
	@Autowired
	private MrsProductService mrsProductService;

	
	@Override
	public PageData<MrsProductDto> findListPage(PageData<MrsProductDto> pageData, MrsProductDto proDto) {
		return mrsProductService.findListPage(pageData, proDto);
	}

	@Override
	public void updateByPrimaryKey(MrsProductDto record) {
		mrsProductService.updateByPrimaryKey(record);
	}

	@Override
	public MrsProductDto findBy3Element(String productName, String credentialsType, String credentialsNumber) {
		return mrsProductService.findBy3Element(productName, credentialsType, credentialsNumber);
	}


	@Override
	public SaveAduitProductResponseVo saveAduitProductVo(MrsProductVo productVo) {
		return mrsProductService.saveAduitProductVo(productVo);
	}

	@Override
	public MrsAccountDto findByCustId(String custId) {
		return mrsProductService.findByCustId(custId);
	}

	@Override
	public PageData<MrsSubAccountDto> findSubAccountByCustId(PageData<MrsSubAccountDto> pageData, String custId) {
		return mrsProductService.findSubAccountByCustId(pageData, custId);
	}

	@Override
	public MrsProductDto selectByPrimaryKey(String id) {
		return mrsProductService.selectByPrimaryKey(id);
	}

	@Override
	public PageData<MrsLoginUserDto> findLoginUserByCustId(PageData<MrsLoginUserDto> pageData, String custId) {
		return mrsProductService.findLoginUserByCustId(pageData, custId);
	}

	@Override
	public SaveAduitProductResponseVo updateAduitProduct(MrsProductVo mrsProductVo) {
		return mrsProductService.updateAduitProduct(mrsProductVo);
	}

	@Override
	public SaveAduitProductResponseVo doUpdateAcctStatus(MrsActAccountVo mrsActAccountVo) {
		return mrsProductService.doUpdateAcctStatus(mrsActAccountVo);
	}

	@Override
	public MrsProductDto findCustId(String custId) {
		return mrsProductService.findCustId(custId);
	}
	@Override
	public void updateBaseAndSync(MrsProductDto dto) {
		mrsProductService.updateBaseAndSync(dto);
	}

	@Override
	public void updateBaseFileAndSync(MrsProductDto dto) {
		mrsProductService.updateBaseFileAndSync(dto);
	}
	
	@Override
	public List<MrsProductDto> findBy3ElementAndNoEff(String productName, String credentialsType,
			String credentialsNumber) {
		return mrsProductService.findBy3ElementAndNoEff(productName, credentialsType, credentialsNumber);
	}

	@Override
	public List<MrsProductDto> findBy3ElementAndStatus(String productName, String credentialsType,
			String credentialsNumber, String accountStatus) {
		return mrsProductService.findBy3ElementAndStatus(productName, credentialsType, credentialsNumber, accountStatus);
	}

	@Override
	public SaveAduitProductResponseVo removeSaveAduit(MrsRemoveAccountVo removeAccountVo) {
		return mrsProductService.removeSaveAduit(removeAccountVo);
	}

}
