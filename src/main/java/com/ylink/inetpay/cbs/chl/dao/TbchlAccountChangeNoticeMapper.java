package com.ylink.inetpay.cbs.chl.dao;

import java.util.List;

import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

import com.ylink.inetpay.common.project.channel.dto.TbChlAccountChangeNotice;
import com.ylink.inetpay.common.project.channel.dto.TbChlTransferOrder;
@MybatisMapper("tbchlAccountChangeNoticeMapper")
public interface TbchlAccountChangeNoticeMapper {
	/**
	 * 查询动账通知列表
	 * @param record
	 * @return
	 */
    List<TbChlAccountChangeNotice> findAll(TbChlAccountChangeNotice param);
    
    /**
     * 查询动帐通知详情
     */
    TbChlAccountChangeNotice detail(String id);
}