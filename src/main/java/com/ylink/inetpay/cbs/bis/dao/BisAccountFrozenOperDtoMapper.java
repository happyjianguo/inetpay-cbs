package com.ylink.inetpay.cbs.bis.dao;

import java.util.List;

import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

import com.ylink.inetpay.common.project.cbs.dto.bis.BisAccountFrozenOperDto;
@MybatisMapper("bisAccountFrozenOperDtoMapper")
public interface BisAccountFrozenOperDtoMapper {
    int deleteByPrimaryKey(String id);

    int insert(BisAccountFrozenOperDto record);

    int insertSelective(BisAccountFrozenOperDto record);

    BisAccountFrozenOperDto selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(BisAccountFrozenOperDto record);

    int updateByPrimaryKey(BisAccountFrozenOperDto record);
    /**
     * 获取所有未知状态的冻结明细记录
     * @return
     */
	List<BisAccountFrozenOperDto> autoQueryUnDownStatus();
	/**
	 * 判断是否存在位置状态的记录
	 * @param id
	 * @return
	 */
	long isExistUndownStatusOper(String busId);
	/**
	 * 判断是否存在支付记录
	 * @param busId
	 * @return
	 */
	long isExistPayOper(String busId);
	/**
	 * 根据id获取操作记录列表
	 * @param batchNo
	 * @return
	 */
	List<BisAccountFrozenOperDto> findActFrozenOperListByBusId(String busId);
}