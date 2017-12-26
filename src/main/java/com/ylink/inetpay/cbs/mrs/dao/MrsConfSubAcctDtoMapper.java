package com.ylink.inetpay.cbs.mrs.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsConfSubAcctDto;
@MybatisMapper("mrsConfSubAcctDtoMapper")
public interface MrsConfSubAcctDtoMapper {
    int deleteByPrimaryKey(String id);

    int insert(MrsConfSubAcctDto record);

    int insertSelective(MrsConfSubAcctDto record);

    MrsConfSubAcctDto selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(MrsConfSubAcctDto record);

    int updateByPrimaryKey(MrsConfSubAcctDto record);
    /**
     * 根据查询条件查询个人客户信息
     * @param searchDto
     * @return
     */
    List<MrsConfSubAcctDto> list(MrsConfSubAcctDto searchDto);
    /**
     * 
     *方法描述：根据关联属性查询个人客户子账户配置信息
     * 创建人：ydx
     * 创建时间：2017年2月12日 下午4:25:11
     * @param relationType
     * @return
     */
    List<MrsConfSubAcctDto> findByPersonType(@Param("relationType")String relationType,@Param("platformCode")String platformCode);
    /**
     * 
     *方法描述：根据关联属性查询机构客户子账户配置信息
     * 创建人：ydx
     * 创建时间：2017年2月12日 下午4:25:11
     * @param relationType
     * @return
     */
    List<MrsConfSubAcctDto> findByOrganType(@Param("relationType")String relationType,@Param("platformCode")String platformCode);
    /**
     * 
     *方法描述：根据关联属性查询产品客户子账户配置信息
     * 创建人：ydx
     * 创建时间：2017年2月12日 下午4:25:11
     * @param relationType
     * @return
     */
    List<MrsConfSubAcctDto> findByProductType(@Param("relationType")String relationType,@Param("platformCode")String platformCode);
    
    
    /**
     * 根据子账户名称获取子账户信息
     * @param accountName
     * @return
     */
    List<MrsConfSubAcctDto> findByAccountName(String accountName);
    
    /**
     * 
     *方法描述：根据子账号，渠道号，一户通类型，
     *主键查询子账号，渠道号，一户通类型主键不想相等的信息
     * 创建人：ydx
     * 创建时间：2017年4月1日 下午2:14:26
     * @param subAccountCode 子账户编号
     * @param platformCode 渠道
     * @param accountType 一户通类型
     * @param id 可为空
     * @return
     */
    MrsConfSubAcctDto checkMrsConfSubAcct(@Param("subAccountCode")String subAccountCode,@Param("platformCode") String platformCode,
    		@Param("accountType")String accountType, @Param("id")String id);
    
    /**
     * 
     *方法描述：
     * 创建人：ydx
     * 创建时间：2017年4月14日 下午4:29:32
     * @param relationType 关联关系
     * @param platformCode 渠道类型
     * @param accountType 一户通类型
     * @return
     */
    List<MrsConfSubAcctDto> findByPersonType3Element(@Param("relationType")String relationType,
    		@Param("platformCode")String platformCode,@Param("accountType")String accountType );
    /**
     * 
     *方法描述：
     * 创建人：ydx
     * 创建时间：2017年4月14日 下午4:29:32
     * @param relationType 关联关系
     * @param platformCode 渠道类型
     * @param accountType 一户通类型
     * @return
     */
    List<MrsConfSubAcctDto> findByOrganType3Element(@Param("relationType")String relationType,
    		@Param("platformCode")String platformCode,@Param("accountType")String accountType );
    /**
     * 
     *方法描述：
     * 创建人：ydx
     * 创建时间：2017年4月14日 下午4:29:32
     * @param relationType 关联关系
     * @param platformCode 渠道类型
     * @param accountType 一户通类型
     * @return
     */
    List<MrsConfSubAcctDto> findByProductType3Element(@Param("relationType")String relationType,
    		@Param("platformCode")String platformCode,@Param("accountType")String accountType );
   
    
    /**
     * 查询子账户配置表获取个人客户子账户必选、可选数据
     * @param relationType
     * @param platformCode
     * @param accountType
     * @return
     */
    List<MrsConfSubAcctDto> findByPersonType3Elements(@Param("relationType")String relationType,
    		@Param("platformCode")String platformCode,@Param("accountType")String accountType );
    
    /**
     * 查询子账户配置表获取机构客户子账户必选、可选数据
     * @param relationType
     * @param platformCode
     * @param accountType
     * @return
     */
    List<MrsConfSubAcctDto> findByOrganType3Elements(@Param("relationType")String relationType,
    		@Param("platformCode")String platformCode,@Param("accountType")String accountType );
    
    /**
     * 查询子账户配置表获取产品客户子账户必选、可选数据
     * @param relationType
     * @param platformCode
     * @param accountType
     * @return
     */
    List<MrsConfSubAcctDto> findByProductType3Elements(@Param("relationType")String relationType,
    		@Param("platformCode")String platformCode,@Param("accountType")String accountType );
}