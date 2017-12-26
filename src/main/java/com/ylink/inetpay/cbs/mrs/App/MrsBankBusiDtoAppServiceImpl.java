package com.ylink.inetpay.cbs.mrs.App;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.cbs.mrs.service.MrsBankBusiDtoService;
import com.ylink.inetpay.common.project.cbs.app.MrsBankBusiDtoAppService;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsBankBusiDto;
import com.ylink.inetpay.common.project.portal.vo.RespCheckVO;
import com.ylink.inetpay.common.project.portal.vo.UserCheckVO;
import com.ylink.inetpay.common.project.portal.vo.bank.AduitVo;
import com.ylink.inetpay.common.project.portal.vo.bank.BankAddVo;
import com.ylink.inetpay.common.project.portal.vo.bank.BankCardRequestVo;
import com.ylink.inetpay.common.project.portal.vo.customer.BankBusiReqVO;

@Service("mrsBankBusiDtoAppService")
public class MrsBankBusiDtoAppServiceImpl implements MrsBankBusiDtoAppService{
	
	@Autowired
	private MrsBankBusiDtoService mrsBankBusiDtoService;
	
	@Override
	public List<MrsBankBusiDto> findByCustId(String custId) {
		return mrsBankBusiDtoService.findByCustId(custId);
	}

	@Override
	public List<MrsBankBusiDto> findByChlBankProp(MrsBankBusiDto userBankDto) {
		return mrsBankBusiDtoService.findByChlBankProp(userBankDto);
	}

	@Override
	public MrsBankBusiDto findByOnly(String bankType, String custName, String accNo) {
		return mrsBankBusiDtoService.findByOnly(bankType, custName, accNo);
	}

	@Override
	public List<MrsBankBusiDto> findByCustIdAndAduitStatus(String custId, String aduitStatus) {
		return mrsBankBusiDtoService.findByCustIdAndAduitStatus(custId, aduitStatus);
	}
	

	@Override
	public MrsBankBusiDto findById(String id) {
		return mrsBankBusiDtoService.findById(id);
	}

	@Override
	public RespCheckVO aduit(AduitVo vo, String name,String userId) {
		return mrsBankBusiDtoService.aduit(vo, name, userId);
	}
	
	@Override
	public RespCheckVO addBankByPortal(BankBusiReqVO bankBusiReqVO) {
		return mrsBankBusiDtoService.addBankByPortal(bankBusiReqVO);
	}

	@Override
	public RespCheckVO delete(String id) {
		return mrsBankBusiDtoService.delete(id);
		
	}
	@Override
	public RespCheckVO delcard(String accNo,String custId) {
		return mrsBankBusiDtoService.delcard(accNo,custId);
		
	}
	@Override
	public RespCheckVO updateBankCardStatus(String channelCode, String accNo,String operType) {
		return mrsBankBusiDtoService.updateBankCardStatus(channelCode, accNo, operType);
		
	}
	@Override
	public PageData<MrsBankBusiDto> listWidthRolesPage(PageData<MrsBankBusiDto> pageDate,
			MrsBankBusiDto mrsuser) {
		return mrsBankBusiDtoService.findbank(pageDate, mrsuser);
	}

	@Override
	public RespCheckVO addBankkj(BankAddVo vo, String type, String custId) {
		return mrsBankBusiDtoService.addBankkj(vo, type, custId);
	}

	@Override
	public RespCheckVO addBanksx(BankAddVo vo, String type, String custId, String name,String userId) {
		return mrsBankBusiDtoService.addBanksx(vo, type, custId, name,userId);
	}



	@Override
	public List<MrsBankBusiDto> findBanksByCustId(String custId) {
		return mrsBankBusiDtoService.findBanksByCustId(custId);
	}

	@Override
	public List<MrsBankBusiDto> findBandByCustId(String custId) {
		return mrsBankBusiDtoService.findBandByCustId(custId);
	}

	@Override
	public List<MrsBankBusiDto> findByCustIdAndAduitStatusAndSuccess(String custId, String aduitStatus) {
		return mrsBankBusiDtoService.findByCustIdAndAduitStatusAndSuccess(custId, aduitStatus);
	}



	@Override
	public RespCheckVO update(String id,String name,String userId) {
		return mrsBankBusiDtoService.update(id, name, userId);
	}



	@Override
	public MrsBankBusiDto findByAccnoAndCustidAndPaytype(String accNo, String custId, String payType) {
		return mrsBankBusiDtoService.findByAccnoAndCustidAndPaytype(accNo, custId, payType);
	}

	public UserCheckVO deleteDefault(MrsBankBusiDto mrsBankBusiDto){
		return mrsBankBusiDtoService.updateDefault(mrsBankBusiDto);
	}
	
	public MrsBankBusiDto findIsDefault(MrsBankBusiDto mrsBankBusiDto){
		return mrsBankBusiDtoService.findIsDefault(mrsBankBusiDto);
	}

	@Override
	public RespCheckVO delQuick(BankBusiReqVO bankBusiReqVO) {
		return mrsBankBusiDtoService.delQuick(bankBusiReqVO);
	}

	@Override
	public RespCheckVO bindBankCard(BankCardRequestVo bankCard) {
		return mrsBankBusiDtoService.bindBankCard(bankCard);
	}

}
