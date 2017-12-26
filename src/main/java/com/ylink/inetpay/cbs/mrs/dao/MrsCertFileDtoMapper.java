package com.ylink.inetpay.cbs.mrs.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsCertFileDto;

@MybatisMapper("mrsCertFileDtoMapper")
public interface MrsCertFileDtoMapper {
	int insert(MrsCertFileDto record);

	int insertSelective(MrsCertFileDto record);

	void batchInsert(@Param("batchList") List<MrsCertFileDto> list);

	int deleteByCustId(@Param("custId") String custId);
	
	int deleteById(String id);

	List<MrsCertFileDto> queryCertFile(MrsCertFileDto record);
	/**
	 * 
	 *方法描述：
	 * 创建人：ydx
	 * 创建时间：2017年2月20日 下午11:42:15
	 * @param custId
	 * @return
	 */
	List<MrsCertFileDto> findByCustId(String custId);
	
	MrsCertFileDto selectByPrimaryKey(String id);
}