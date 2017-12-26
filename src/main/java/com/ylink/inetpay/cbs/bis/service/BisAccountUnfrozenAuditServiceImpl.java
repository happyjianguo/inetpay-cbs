package com.ylink.inetpay.cbs.bis.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.ylinkpay.framework.core.model.PageData;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.ylink.inetpay.cbs.bis.dao.BisAccountUnfrozenAuditDtoMapper;
import com.ylink.inetpay.cbs.common.CbsConstants;
import com.ylink.inetpay.common.core.constant.BISAuditType;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisAccountUnfrozenAuditDto;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisAuditDto;

@Service("bisAccountUnfrozenAuditService")
@Transactional(value=CbsConstants.TX_MANAGER_BIS)
public class BisAccountUnfrozenAuditServiceImpl implements BisAccountUnfrozenAuditService {
	@Autowired
	private BisAccountUnfrozenAuditDtoMapper bisAccountUnfrozenAuditDtoMapper;
	@Autowired
	private BisAuditService bisAuditService;
	
	/**
	 * 获取所有复核通过，待处理，解冻日期小于等于当前账务日期的记录
	 */
	@Override
	public List<BisAccountUnfrozenAuditDto> listUnFrozenByEndTime(Date actDate) {
		return bisAccountUnfrozenAuditDtoMapper.listUnFrozenByEndTime(actDate);
	}

	/**
	 * 获取解冻订单详情
	 * @param id
	 * @return
	 */
	@Override
	public BisAccountUnfrozenAuditDto findUnfrozenViewById(String id){
		BisAccountUnfrozenAuditDto unfrozen = bisAccountUnfrozenAuditDtoMapper.selectByPrimaryKey(id);
		//获取审核详情
		List<BisAuditDto> auditDtoList = bisAuditService.findFrozenListBybusId(id, BISAuditType.AMOUNT_UNFREEZE);
		if(unfrozen!=null){
			unfrozen.setAuditDtoList(auditDtoList);
		}
		return unfrozen;
	}
	
	/**
	 * 解冻订单明细列表
	 */
	@Override
	public PageData<BisAccountUnfrozenAuditDto> listUnFrozenList(PageData<BisAccountUnfrozenAuditDto> pageData,BisAccountUnfrozenAuditDto queryParam){
		PageHelper.startPage(pageData.getPageNumber(), pageData.getPageSize());
		List<BisAccountUnfrozenAuditDto> items = bisAccountUnfrozenAuditDtoMapper.listUnFrozen(queryParam);
		Page<BisAccountUnfrozenAuditDto> page = (Page<BisAccountUnfrozenAuditDto> )items;
		pageData.setRows(items);
		pageData.setTotal(page.getTotal());
		return pageData;
	}

	
	/**
	 * 获取所有超时还处理待复核的解冻订单
	 */
	@Override
	public List<BisAccountUnfrozenAuditDto> runOutWaitAuditByEndTime(Date actDate) {
		return bisAccountUnfrozenAuditDtoMapper.runOutWaitAuditByEndTime(actDate);
	}
}
