package com.ylink.inetpay.cbs.mrs.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsConfReviewDto;

@MybatisMapper("mrsConfReviewDtoMapper")
public interface MrsConfReviewDtoMapper {
    int deleteByPrimaryKey(String id);

    int insert(MrsConfReviewDto record);

    int insertSelective(MrsConfReviewDto record);

    MrsConfReviewDto selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(MrsConfReviewDto record);

    int updateByPrimaryKey(MrsConfReviewDto record);
    
    List<MrsConfReviewDto> list(MrsConfReviewDto searchDto);
    
    MrsConfReviewDto selectById(@Param("id")String id);
    /**
	 * 
	 *方法描述：根据一户通编号，机构复核业务主键查询信息
	 * 创建人：pst10
	 * 创建时间：2017年5月10日 下午2:27:06
	 * @param custId 一户通编号
	 * @param id 主键
	 * @return
	 */
	MrsConfReviewDto selectByCustIdAndId(@Param("custId")String custId, @Param("id")String id);
	/**
	 * 
	 *方法描述：根据一户通编号查询机构复核业务，
	 *如果不存在返回默认的配置信息（配置会有条默认的配置信息）
	 * 创建人：pst10
	 * 创建时间：2017年5月10日 下午2:27:42
	 * @param custId
	 * @return
	 */
	MrsConfReviewDto selectByCustId(@Param("custId")String custId);
	/**
	 * 
	 *方法描述：查询机构复核业务数据，配置的全局复核的参数，默认是全局的
	 * 创建人：pst10
	 * 创建时间：2017年5月10日 下午3:02:42
	 * @return
	 */
	MrsConfReviewDto selectByIsGlobal();
	
}