package com.ylink.inetpay.cbs.mrs.service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.ylinkpay.framework.core.model.PageData;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.ylink.inetpay.cbs.common.CbsConstants;
import com.ylink.inetpay.cbs.mrs.dao.MrsOperationLogDtoMapper;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsOperationLogDto;
@Service("mrsOperationLogService")
@Transactional(value=CbsConstants.TX_MANAGER_MRS)
public class MrsOperationLogServiceImpl implements MrsOperationLogService {
	@Autowired
	private MrsOperationLogDtoMapper mrsOperationLogDtoMapper;
	@Override
	public PageData<MrsOperationLogDto> pageList(
			PageData<MrsOperationLogDto> pageDate,
			MrsOperationLogDto mrsOperationLogDto) {
		PageHelper.startPage(pageDate.getPageNumber(),pageDate.getPageSize());
		List<MrsOperationLogDto> list=mrsOperationLogDtoMapper.list(mrsOperationLogDto);
		Page<MrsOperationLogDto> page=(Page<MrsOperationLogDto>) list;
		pageDate.setTotal(page.getTotal());
		pageDate.setRows(list);
		return pageDate;
	}

	@Override
	public void add(MrsOperationLogDto mrsOperationLogDto) {
		mrsOperationLogDto.setId(UUID.randomUUID().toString());
		mrsOperationLogDto.setCreateTime(new Date());
		mrsOperationLogDtoMapper.insert(mrsOperationLogDto);
	}
}
