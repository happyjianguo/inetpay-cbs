package com.ylink.inetpay.cbs.mrs.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsPersonDto;
import com.ylink.inetpay.common.project.portal.vo.customer.PersonVO;

@MybatisMapper("mrsPersonDtoMapper")
public interface MrsPersonDtoMapper {
   
	int deleteByPrimaryKey(String id);

    int insert(MrsPersonDto record);

    int insertSelective(MrsPersonDto record);

    MrsPersonDto selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(MrsPersonDto record);

    int updateByPrimaryKey(MrsPersonDto record);
    
    List<MrsPersonDto> findBy3Element(@Param("name")String name, @Param("type")String type, 
    		@Param("number")String number);
    List<MrsPersonDto> findActPersonBy3Element(@Param("name")String name, @Param("type")String type, 
    		@Param("number")String number);
    /**
     * 根据客户号获取个人客户信息
     * @param custId
     * @return
     */
    MrsPersonDto findByCustId(@Param("custId")String custId);
    
    PersonVO findPersonVoByCustId(@Param("custId")String custId);
    
    /**
     * 根据查询条件查询个人客户信息
     * @param searchDto
     * @return
     */
    List<MrsPersonDto> list(MrsPersonDto searchDto);
    /**
     * 根据查询条件查询个人客户信息
     * @param searchDto
     * @return
     */
    List<MrsPersonDto> updateAuditList(MrsPersonDto searchDto);
    
    public int restMobile(@Param("custId")String custId, @Param("mobile")String mobile);
    
    public int restEmail(@Param("custId")String custId, @Param("email")String email);
    
    public int restEmailByLoginId(@Param("loginId")String loginId, @Param("email")String email);

	int updateFileId(@Param("custId")String custId, @Param("fileId")String fileId, @Param("updateTime")Date updateTime);
	
	int updateCustomerCodeByCustId(@Param("custId")String custId, @Param("customerCode")String customerCode, @Param("updateTime")Date updateTime);
	
	MrsPersonDto findPersondDtoByCustId(String custId);
}