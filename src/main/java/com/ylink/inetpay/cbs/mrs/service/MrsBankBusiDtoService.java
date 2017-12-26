package com.ylink.inetpay.cbs.mrs.service;

import java.util.List;

import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsBankBusiDto;
import com.ylink.inetpay.common.project.portal.vo.RespCheckVO;
import com.ylink.inetpay.common.project.portal.vo.UserCheckVO;
import com.ylink.inetpay.common.project.portal.vo.bank.AduitVo;
import com.ylink.inetpay.common.project.portal.vo.bank.BankAddVo;
import com.ylink.inetpay.common.project.portal.vo.bank.BankCardRequestVo;
import com.ylink.inetpay.common.project.portal.vo.customer.BankBusiReqVO;

public interface MrsBankBusiDtoService {
	
	
	public List<MrsBankBusiDto> findByChlBankProp(MrsBankBusiDto userBankDto) ;
	
	/**
	 * 根据一户通主键查找
	 * @param custId
	 * @return
	 */
	public List<MrsBankBusiDto> findByCustId(String custId);
	/**
	 * 根据一户通号，查找授信的，状态为复核成功或无需复核，绑定状态：绑定成功
	 * @param custId
	 * @return
	 */
	public List<MrsBankBusiDto> findBanksByCustId(String custId);
	/**
	 * 根据一户通号，绑定状态：绑定成功
	 * @param custId
	 * @return
	 */
	public List<MrsBankBusiDto> findBandByCustId(String custId);
	/**
	 * 添加银行卡快捷
	 * @param vo
	 */
	public RespCheckVO addBankkj(BankAddVo vo,String type,String custId);
	/**
	 * 添加银行卡授信
	 * @param vo
	 */
	public RespCheckVO addBanksx(BankAddVo vo,String type,String custId, String name,String userId);
	/**
	 * 保存银行卡信息
	 * 
	 * @param bankCard
	 * @return
	 */
	public RespCheckVO bindBankCard(BankCardRequestVo bankCard);
	/**
	 * 添加银行卡
	 * @param vo
	 */
	public RespCheckVO addBankByPortal(BankBusiReqVO bankBusiReqVO);
	/**
	 * 根据行别、用户名、卡号加起来的唯一性找银行卡
	 * @param bankType
	 * @param custName
	 * @param accNo
	 * @return
	 */
	public MrsBankBusiDto findByOnly(String bankType,String custName,String accNo);
	
	/**
	 * 根据一户通主键和审核状态查找
	 * @param custId
	 * @return
	 */
	List<MrsBankBusiDto> findByCustIdAndAduitStatus(String custId,String aduitStatus);
	
	/**
	 * 根据一户通主键和审核状态查找且处理成功
	 * @param custId
	 * @return
	 */
	List<MrsBankBusiDto> findByCustIdAndAduitStatusAndSuccess(String custId,String aduitStatus);
	
	/**
	 * 根据表Id进行查询银行账户信息
	 * @param id
	 * @return
	 */
	public MrsBankBusiDto findById(String id);
	
	/**
	 * 审核
	 * @param vo
	 * @param name
	 */
	public RespCheckVO aduit(AduitVo vo,String name,String userId);
	
	/**
	 * 删除银行账户
	 */
	public RespCheckVO delete(String id);
	/**
	 * 删除银行账户
	 */
	public RespCheckVO delcard(String accNo,String custId);
	
	/**
	 * 修改银行账户
	 */
	public RespCheckVO update(String id,String name,String userId);
	/**
	 * 根据渠道编码和银行卡号更新银行卡状态
	 * @param channelCode 渠道编码
	 * @param accNo 银行卡号
	 * @param accNo 操作类型，("1", "绑定"),("2", "解绑")
	 * @return
	 */
	public RespCheckVO updateBankCardStatus(String channelCode, String accNo,String operType);
	
	/**
	 * 分页查询
	 * @param pageData
	 * @param searchDto
	 * @return
	 */
	public PageData<MrsBankBusiDto> findbank(PageData<MrsBankBusiDto> pageData, MrsBankBusiDto searchDto) ;
	
	/**
	 * 根据卡号、一户通号、绑卡类型查询
	 * @param accNo
	 * @param custId
	 * @param payType 枚举：BankPayType
	 * @return
	 */
	public MrsBankBusiDto findByAccnoAndCustidAndPaytype(String accNo,String custId,String payType );
	
	/**
	 * 删除默认银行卡
	 * @return 
	 */
	public UserCheckVO updateDefault(MrsBankBusiDto mrsBankBusiDto);
	
	/**
	 * 添加默认银行卡
	 * @return 
	 */
	public MrsBankBusiDto findIsDefault(MrsBankBusiDto mrsBankBusiDto);
	
	/**
	 * 快捷解绑
	 */
	public RespCheckVO delQuick(BankBusiReqVO bankBusiReqVO);
	
	/**
	 * 根据一户通号查询(快捷专用:绑定(02)、绑卡类型(01))
	 * @param custId
	 * @return
	 */
	public List<MrsBankBusiDto> findByBindStatusAndCustidAndPaytype(String custId);
}
