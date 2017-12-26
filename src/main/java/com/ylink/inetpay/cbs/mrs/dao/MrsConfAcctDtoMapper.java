package com.ylink.inetpay.cbs.mrs.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsConfAcctDto;
@MybatisMapper("mrsConfAcctDtoMapper")
public interface MrsConfAcctDtoMapper {
    int deleteByPrimaryKey(String id);

    int insert(MrsConfAcctDto record);

    int insertSelective(MrsConfAcctDto record);

    MrsConfAcctDto selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(MrsConfAcctDto record);

    int updateByPrimaryKey(MrsConfAcctDto record);
    /**
     * 
     *方法描述：根据查询条件查询子账户信息
     * 创建人：ydx
     * 创建时间：2017年3月31日 下午7:59:15
     * @param searchDto
     * @return
     */
	List<MrsConfAcctDto> list(MrsConfAcctDto searchDto);
	/**
	 * 
	 *方法描述：根据子账户名称，主键查询子账户信息（id可为空）
	 * 创建人：ydx
	 * 创建时间：2017年3月31日 下午8:35:01
	 * @param name 子账户名称
	 * @param id 主键
	 * @return
	 */
	public MrsConfAcctDto selectByNameAndId(@Param("name")String name , @Param("id")String id);
	/**
	 * 
	 *方法描述：查询所有子账户信息
	 * 创建人：ydx
	 * 创建时间：2017年4月10日 下午3:17:38
	 * @return
	 */
	List<MrsConfAcctDto> findAll();
	/**
	 * 
	 *方法描述：根据子账户配置信息主键查询子账户所属大类
	 * 创建人：ydx
	 * 创建时间：2017年4月17日 上午11:34:25
	 * @param confSubAcctIds 子账户配置主键集合
	 * @return
	 */
	List<String> findSubAcctTypesByConfSubIds(@Param("confSubAcctIds")List<String> confSubAcctIds );
	/**
	 * 
	 *方法描述：根据一户通号码查询子账号类别（
	 *先根据一户通号查询子账户信息，根据子账户中子账号编号查询子账号类别）
	 * 创建人：ydx
	 * 创建时间：2017年4月18日 下午8:40:39
	 * @param custId
	 * @return
	 */
	List<String> findSubAcctTypeByCustId(@Param("custId")String custId);
}