package com.ylink.inetpay.cbs.mrs.service;

import java.util.Date;
import java.util.List;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsSubAccountDto;

public interface MrsSubAccountService {
	
	int insertSelective(MrsSubAccountDto dto);
	/**
	 * 
	 *方法描述：根据CustId查询子账户信息
	 * 创建人：ydx
	 * 创建时间：2017年2月20日 下午8:42:48
	 * @param custId
	 * @return
	 */
	List<MrsSubAccountDto> findByCustId(String custId);
	/**
	 * 更新数据
	 * @param dto
	 * @return
	 */
	int updateSelective(MrsSubAccountDto dto);
	
	/**
	 * 根据请求参数中一户通账号和子账户类型和一户通状态为非“注销”状态关联查询“子账户表”和“一户通账户表”
	 * @param custId
	 * @param subAccountType
	 * @return
	 */
	List<MrsSubAccountDto> findByCustIdAndSubAccountType(MrsSubAccountDto mrsSubAccountDto);
	 /**
     * 根据一户通更新子账户状态
     * @param custId
     * @param subAccountStatus
     * @param updateTime
     * @return
     */
    void updateByCustId(String custId, String subAccountStatus, Date updateTime);
    
    /**
     * 根据一户通更新子账户状态
     * @param custId
     * @param subAccountStatus
     * @param updateTime
     * @return
     */
    void updateByCustIdAndSubType(String custId,String subAccountType, String subAccountStatus, Date updateTime);
    
    MrsSubAccountDto findByAccountNameandCustId(String accountName,String custId);
    
    boolean checkSubAccountStatus(List<MrsSubAccountDto> list);
    
    /**
     * 根据一户通账号查询子账户状态
     * (投保人子账户)
     * @param custId
     * @return
     */
    MrsSubAccountDto findSubAccountStatusByCustIdAndType(String custId);
}
