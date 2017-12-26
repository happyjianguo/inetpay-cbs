package com.ylink.inetpay.cbs.mrs.dao;

import java.util.List;

import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsWithdrawAduitDto;
@MybatisMapper("mrsWithdrawAduitDtoMapper")
public interface MrsWithdrawAduitDtoMapper {
    int deleteByPrimaryKey(String id);

    int insert(MrsWithdrawAduitDto record);

    int insertSelective(MrsWithdrawAduitDto record);

    MrsWithdrawAduitDto selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(MrsWithdrawAduitDto record);

    int updateByPrimaryKey(MrsWithdrawAduitDto record);
    /**
	 * 获取序列号-用于充值提现审核申请生成流水
	 * 
	 * @return
	 */
	String getMrsAuditOrderNosVal();
    /**
     * 根据参数查询所有提现待审核数据
     * @param mrsWithdrawAduitDto
     * @return
     */
    List<MrsWithdrawAduitDto> queryAllData(MrsWithdrawAduitDto mrsWithdrawAduitDto);
}