package com.ylink.inetpay.cbs.mrs.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsLoginUserDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsUserAccountDto;
import com.ylink.inetpay.common.project.portal.exception.PortalCheckedException;
import com.ylink.inetpay.common.project.portal.vo.UserCheckVO;

public interface MrsLoginUserService {

    MrsLoginUserDto doLogin(MrsLoginUserDto loginUser);

    /**支付密码校验 **/
    MrsLoginUserDto doPayPasswCheck(MrsLoginUserDto loginUser);

    MrsLoginUserDto doOrganLogin(MrsLoginUserDto loginUser) throws PortalCheckedException;

    /**
	 * 通过用户ID查找关联的一户通信息(custId)
	 */
    List<MrsLoginUserDto> getCustIdsByLoginUserId(String id);

    /**
	 * 获取登录信息
	 * @param dto
	 * @return
	 * @throws PortalCheckedException 
	 */
    MrsLoginUserDto getLoginUser(MrsLoginUserDto dto) throws PortalCheckedException;

	/**
	 * 重置密码
	 * @param id
	 * @param loginPwd
	 */
    void updateLoginPwd(String id, String loginPwd);

    void upLoginPwd(MrsLoginUserDto dto);

    /**
	 * 根据ID获取非注销用户的登陆信息
	 * @param id
	 * @return
	 */
    MrsLoginUserDto getEffectLoginUserById(String id);

	/**
	 * 更新密码
	 * @param id
	 * @param oldPwd
	 * @param newPwd
	 * @throws PortalCheckedException 
	 */
    UserCheckVO updateLoginPwd(String id, String oldPwd, String newPwd);

	/**
	 * 更新密码
	 * @param id
	 * @param oldPwd
	 * @param newPwd
	 * @throws PortalCheckedException 
	 */
    UserCheckVO updateLoginPwdByMobile(String id, String oldPwd, String newPwd, String mobile);

    /**
	 * 
	 * @param dto
	 * @return
	 */
    PageData<MrsLoginUserDto> findPerson(PageData<MrsLoginUserDto> pageData, MrsLoginUserDto searchDto);

    void updateLoginPwdByEmail(String checkMsg, String loginPwd) throws PortalCheckedException;

    MrsLoginUserDto findByAlias(String alias);

    MrsLoginUserDto findUserDtoByLoginId(String id);

    MrsLoginUserDto updateAlias(String loginId, String alias) throws PortalCheckedException;

    MrsLoginUserDto getByCustIdLoginPwd(String custId, String loginPwd);

    MrsLoginUserDto selectByCustId(String custId);

	MrsLoginUserDto findByIdAndLoginPwd(String id, String loginPwd);

    MrsLoginUserDto isExistEmail(String email);

    MrsLoginUserDto isExistMobile(String mobile);

    /**
	 * 保存登录信息
	 * @param dto
	 * @return
	 * @throws PortalCheckedException 
	 */
    void saveLoginUser(MrsLoginUserDto dto) throws PortalCheckedException;

    int insertSelective(MrsLoginUserDto dto);
	
	/**
     * 根据ID查询用户信息表
     */
    MrsLoginUserDto selectByPrimaryKey(String id);
	
	 /**
     * 个人用户登录信息查询
     * @param dto
     * @return
     */
    List<MrsLoginUserDto> findByMobileAndAlias(MrsLoginUserDto dto);
    /**
     * 
     *方法描述：根据一户通号码查询登录用户信息
     * 创建人：ydx
     * 创建时间：2017年2月20日 下午8:56:14
     * @param custId
     * @return
     */
    List<MrsLoginUserDto> findByCustId(String custId);

	/**
	 * 分页查询
	 * @param pageData
	 * @param searchDto
	 * @return
	 */
    PageData<MrsLoginUserDto> finduser(PageData<MrsLoginUserDto> pageData, MrsLoginUserDto searchDto);

    /**
	 * 根据Id获取一户通信息
	 * @param id
	 * @return
	 */
	PageData<MrsLoginUserDto> findId(PageData<MrsLoginUserDto> pageData,MrsLoginUserDto id);

	
	/**
     * 通过一户通号码查询主用户(机构)
     * @param custId
     * @return
     */
    MrsLoginUserDto findMainByCustId(String custId);

	 /**
     * 获取用户开户审核数据
     * @param loginId
     * @return
     */
    UserCheckVO getUserAuditInfo(String loginId);

    
    /**
     * 获取用户开户审核数据
     * @param loginId
     * @return
     */
    UserCheckVO getUserAuditInfoStatus(String loginId);
    
    /**
     * MRSUSERACCOUNT
     * @param isMain
     * @return
     */
    MrsUserAccountDto isExistIsMain(String isMain);
    
    MrsLoginUserDto selectLoginUserByCustId(@Param("custId")String custId);
    
    /**
     * 根据主键进行查询
     * @param id
     * @return
     */
    MrsLoginUserDto findById(String id);
    
    /**
     * 根据电话号码
     * @param mobile
     * @return
     */
    MrsLoginUserDto findByMobile(String mobile);
    
    /**
     * 更新
     * @param dto
     */
    void updateDto(MrsLoginUserDto dto);
    /**
     * 通过一户通号码查询主,检查支付密码是否设置
     * @param custId
     * @return
     */
    boolean checkUserPayPwd(String custId);
    
    /**
     * 根据微信号获取用户信息
     * @param weChatId
     * @return
     */
    MrsLoginUserDto findLoginUserDtoByWeChatId(String weChatId);
    
    /**
     * 更新用户信息
     * @param mrsLoginUserDto
     */
    void updateLoginUserDto(MrsLoginUserDto mrsLoginUserDto);

}
