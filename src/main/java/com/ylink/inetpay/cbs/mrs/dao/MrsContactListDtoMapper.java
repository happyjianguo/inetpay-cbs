package com.ylink.inetpay.cbs.mrs.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsContactListDto;
@MybatisMapper("mrsContactListDtoMapper")
public interface MrsContactListDtoMapper {
    int deleteByPrimaryKey(String id);

    int insert(MrsContactListDto record);

    int insertSelective(MrsContactListDto record);

    MrsContactListDto selectByPrimaryKey(String id);
    
    MrsContactListDto selectPage(MrsContactListDto mrsContactListDto);

    int updateByPrimaryKeySelective(MrsContactListDto record);

    int updateByPrimaryKey(MrsContactListDto record);
    
    /**
     * 分页查询
     * @param params
     * @return
     */
	List<MrsContactListDto> queryAllData(MrsContactListDto params);
	
	/**
	 * 条件查询
	 * @param mrsContactListDto
	 * @return
	 */
	MrsContactListDto selectBycustId(MrsContactListDto mrsContactListDto);
	/**
	 * 
	 *方法描述：根据一户通号码查询联系人信息
	 * 创建人：ydx
	 * 创建时间：2017年2月20日 下午9:44:42
	 * @param custId
	 * @return
	 */
	List<MrsContactListDto> findByCustId(String custId);
	
	/**
	 * 根据一户通账户、联系人姓名删除联系人信息
	 * @param custId
	 * @param name
	 */
	void deleteByNameAndCustId(@Param("custId")String custId,@Param("name")String name);
	
	/**
	 * 根据主键、一户通账号查询联系人信息
	 * @param id
	 * @param custId
	 * @return
	 */
	MrsContactListDto findByIdandCustId(@Param("id")String id,@Param("custId")String custId);
}