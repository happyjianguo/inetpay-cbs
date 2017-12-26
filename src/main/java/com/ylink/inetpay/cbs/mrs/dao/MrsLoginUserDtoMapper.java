package com.ylink.inetpay.cbs.mrs.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsLoginUserDto;

@MybatisMapper("mrsLoginUserDtoMapper")
public interface MrsLoginUserDtoMapper {
	
    int deleteByPrimaryKey(String id);

    int insert(MrsLoginUserDto record);

    int insertSelective(MrsLoginUserDto record);

    MrsLoginUserDto selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(MrsLoginUserDto record);

    int updateByPrimaryKey(MrsLoginUserDto record);
    
    MrsLoginUserDto selectByCustId(@Param("custId")String custId);
    
    MrsLoginUserDto selectLoginUserByCustId(@Param("custId")String custId);
    /**
     * 个人用户登录信息查询
     * 		根据登陆信息(不包含密码)
     * @param dto
     * @return
     */
    List<MrsLoginUserDto> findByLoginMsg(MrsLoginUserDto dto);
    /**
     * 查询门户登录用户
     * @param dto
     * @return
     */
    List<MrsLoginUserDto> findByLoginUser(MrsLoginUserDto dto);
   
    
    /**
     * 机构用户登录信息查询(根据一户通账号)
     * @param custId
     * @return
     */
    MrsLoginUserDto findOrganLoginByCustId(String custId);
    
    /**
     * 机构用户登录信息查询(根据昵称)
     * @param alias
     * @return
     */
    MrsLoginUserDto findOrganLoginByAlias(String alias);
    
    /**
     * 
     * @param email
     * @return
     */
    List<MrsLoginUserDto> findByEmail(@Param("email")String email);
    
    /**
     * 
     * @param mobile
     * @return
     */
    MrsLoginUserDto findByMobile(@Param("mobile")String mobile);
    
    /**
     * 根据ID和登录密码查找登陆信息
     * @param id
     * @param loginPwd
     * @return
     */
    MrsLoginUserDto findByIdAndLoginPwd(@Param("id")String id, @Param("loginPwd")String loginPwd);
    
    /**
     * 分页查询登陆用户的信息
     * @param dto
     * @return
     */
    List<MrsLoginUserDto> pageList(MrsLoginUserDto dto);

    int updateMobile(@Param("custId") String custId, @Param("mobile") String mobile);

    int updateEmail(@Param("custId") String custId, @Param("email") String email);

    int updateEmailById(@Param("id") String id, @Param("email") String email);

	MrsLoginUserDto findByAlias(String alias);

	int updateAlias(@Param("id")String loginId, @Param("alias")String alias);

	MrsLoginUserDto getByCustIdLoginPwd(@Param("custId")String custId, @Param("loginPwd")String loginPwd);

	MrsLoginUserDto findLoginUserByCustId(String custId);
	  /**
     * 个人用户登录信息查询
     * @param dto
     * @return
     */
    List<MrsLoginUserDto> findByMobileAndAlias(MrsLoginUserDto dto);
    
    List<MrsLoginUserDto> findUserDtoByCustId(String custId);
    
    /**
     * 分页查询用户的信息
     * @param dto
     * @return
     */
    List<MrsLoginUserDto> pageUser(MrsLoginUserDto dto);
    /**
     * 
     * @param searchDto
     * @return
     */
    List<MrsLoginUserDto> list(String id);

    
    /**
     * 通过一户通号码查询主用户(机构)
     * @param custId
     * @return
     */
    MrsLoginUserDto findMainByCustId(String custId);
    
    /**
     * 根据微信号查询用户信息
     * @param weChatId
     * @return
     */
    MrsLoginUserDto findUserDtoByWeChatId(String weChatId);

    /**
     * 根据登录用户查找
     **/
    MrsLoginUserDto findUserDtoByLoginId(String loginId);
    
}