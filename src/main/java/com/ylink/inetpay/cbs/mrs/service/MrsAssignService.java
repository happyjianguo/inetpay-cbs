package com.ylink.inetpay.cbs.mrs.service;

import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsAssignShieNoSubDto;
import com.ylink.inetpay.common.project.portal.vo.AssignShieNoRequestVO;

public interface MrsAssignService {
	/**
	 * 根据客户名称、证件类别、证件号码查询系统是否存在历史配号数据
	 * 
	 * @param custName客户名称
	 * @param certiType证件类别
	 * @param certiNum证件号码
	 * @return
	 */
	public String findAssignNoBy3Element(String custName, String certiType, String certiNum, String source);

	/**
	 * 客户号生成
	 * 
	 * @param custType证件类别
	 * @return
	 */
	public String generateAssignShieNo(String custType);

	/**
	 * 系统根据规则生成新的客户号，并记录客户号与客户名称、证件类别、证件号码、客户类型的关联关系
	 * 
	 * @param assignShieNoRequestVO
	 * @param assignShieNo客户号
	 */
	public void saveAssignShieNo(AssignShieNoRequestVO assignShieNoRequestVO, String assignShieNo);

	
	/**
	 * 根据主表id查询和平台
	 * @param refId
	 * @return
	 */
	public MrsAssignShieNoSubDto findByRefId(String refId,String source);
	
	/**
	 * 更新子表
	 * @param mrsAssignShieNoSubDto
	 */
	public void updateSubDto(MrsAssignShieNoSubDto mrsAssignShieNoSubDto);
	
	
}
