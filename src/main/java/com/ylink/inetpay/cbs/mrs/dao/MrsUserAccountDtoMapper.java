package com.ylink.inetpay.cbs.mrs.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsUserAccountDto;

@MybatisMapper("mrsUserAccountDtoMapper")
public interface MrsUserAccountDtoMapper {
    int deleteByPrimaryKey(String id);

    int insert(MrsUserAccountDto record);

    int insertSelective(MrsUserAccountDto record);

    MrsUserAccountDto selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(MrsUserAccountDto record);

    int updateByPrimaryKey(MrsUserAccountDto record);

	List<MrsUserAccountDto> selectByCustIds(List<String> custIds);
	
	int deleteByUserIdAndAccountId(@Param("userId")String userId,@Param("accountId")String accountId);
	/**
	 * 根据账户表主键删除
	 * @param accountId
	 * @return
	 */
	int deleteByAccountId(@Param("accountId")String accountId);
	
	MrsUserAccountDto findByIsMain(@Param("accountId")String accountId);
	
	List<MrsUserAccountDto> findByIsAccountId(@Param("accountId")String accountId);
}