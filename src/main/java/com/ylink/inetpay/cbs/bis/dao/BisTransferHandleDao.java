package com.ylink.inetpay.cbs.bis.dao;

import java.util.List;

import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

import com.ylink.inetpay.common.core.constant.PayStatusEnum;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisTransferHandleDto;
/**
 * 头寸划拨经办
 * @author pst10
 *
 */
@MybatisMapper("bisTransferHandleDao")
public interface BisTransferHandleDao {
	/**
	 * 查询所有分页
	 * @param params
	 * @return
	 */
	public List<BisTransferHandleDto> getByCond(BisTransferHandleDto clsCallAcctHandle);
	
	/**
	 * 新增
	 * @param clsCallAcctHandle
	 */
	public void insert(BisTransferHandleDto clsCallAcctHandle);
	
	/**
	 * 修改
	 * @param clsCallAcctHandle
	 */
	public void update(BisTransferHandleDto clsCallAcctHandle);
	
	/**
	 * 单条详情
	 * @param id
	 * @return
	 */
	public BisTransferHandleDto details(String id);
	
	/***
	 * 修改所有
	 * @param clsCallAcctHandle
	 */
	public void updateAll(BisTransferHandleDto clsCallAcctHandle);
	
	
	/**
	 * 单条详情加锁
	 * @param id
	 * @return
	 */
	public BisTransferHandleDto getById(String id);
	
	/**
	 * 获取序列
	 * @return
	 */
	public String getSeqenceVals();
	
	/**
	 * 更改支付状态
	 * @param id
	 */
	public void updatePayStatus(BisTransferHandleDto clsCallAcctHandle);
	/**
	 * 获取所有未知状态的数据
	 * @param unknown
	 * @return
	 */
	public List<BisTransferHandleDto> getUnDownStatusDtos(PayStatusEnum unknown);
}