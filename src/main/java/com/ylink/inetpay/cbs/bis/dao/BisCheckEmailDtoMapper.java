package com.ylink.inetpay.cbs.bis.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

import com.ylink.inetpay.common.core.constant.EBisEmailChectStatus;
import com.ylink.inetpay.common.core.constant.EBisEmailTemplateCode;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisCheckEmailDto;
@MybatisMapper("bisCheckEmailDtoMapper")
public interface BisCheckEmailDtoMapper {
    int deleteByPrimaryKey(String id);

    int insert(BisCheckEmailDto record);

    int insertSelective(BisCheckEmailDto record);

    BisCheckEmailDto selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(BisCheckEmailDto record);
    int updateOperTypeAndCustId(BisCheckEmailDto record);
    int updateByPrimaryKey(BisCheckEmailDto record);
    /**
     * 根据验签信息，获取验证邮件。
     * @param checkMessage
     * @return
     */
	BisCheckEmailDto getCheckEmail(String checkMessage);
	
	BisCheckEmailDto getByCustIdOperTypeStatus(@Param("custId")String custId, @Param("operType")EBisEmailTemplateCode operType, 
			@Param("status")EBisEmailChectStatus status);
	
	List<BisCheckEmailDto> getByCustIdOrCheckMessageAndOperTypeAndStatus(@Param("custId")String custId, @Param("checkMessage")String checkMessage, @Param("operType")EBisEmailTemplateCode operType, @Param("status")EBisEmailChectStatus status);
	/**
	 * 根据业务类型，修改痛着类型的验证了邮件为已验证
	 * @param operType
	 * @param checkPass
	 */
	void updateEmailStatus(@Param("operType")EBisEmailTemplateCode operType,
			@Param("status")EBisEmailChectStatus checkPass,@Param("custId")String custId);
}