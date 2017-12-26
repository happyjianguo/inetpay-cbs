package com.ylink.inetpay.cbs.ucs.sec.service;
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
import com.ylink.inetpay.cbs.ucs.sec.dao.UcsSecOperationLogDtoMapper;
import com.ylink.inetpay.common.project.cbs.dto.ucs.sec.UcsSecOperationLogDto;
@Service("ucsSecOpertionLogService")
@Transactional(value=CbsConstants.TX_MANAGER_BIS)
public class UcsSecOperationLogServiceImpl implements UcsSecOperationLogService {
	@Autowired
	private UcsSecOperationLogDtoMapper ucsSecOperationLogDtoMapper;
	@Override
	public PageData<UcsSecOperationLogDto> list(
			PageData<UcsSecOperationLogDto> pageDate,
			UcsSecOperationLogDto ucsSecOperationLogDto){
		PageHelper.startPage(pageDate.getPageNumber(), pageDate.getPageSize());
		List<UcsSecOperationLogDto> list = ucsSecOperationLogDtoMapper.list(ucsSecOperationLogDto);
		Page<UcsSecOperationLogDto> page=(Page<UcsSecOperationLogDto>)list;
		pageDate.setTotal(page.getTotal());
		pageDate.setRows(list);
		return pageDate;
	}
	@Override
	public void insert(UcsSecOperationLogDto ucsSecOperationLogDto){
		ucsSecOperationLogDto.setCreateTime(new Date());
		ucsSecOperationLogDto.setId(UUID.randomUUID().toString());
		ucsSecOperationLogDtoMapper.insert(ucsSecOperationLogDto);
	}
}
