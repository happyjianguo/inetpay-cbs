package com.ylink.inetpay.cbs.bis.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.ylinkpay.framework.core.model.PageData;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.ylink.inetpay.cbs.bis.dao.BisMessageNotificationDtoMapper;
import com.ylink.inetpay.cbs.common.CbsConstants;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisMessageNotificationDto;

@Transactional(value=CbsConstants.TX_MANAGER_BIS)
@Service("bisMsgNotificationService")
public class BisMsgNotificationServiceImpl implements BisMsgNotificationService {
	@Autowired
	private BisMessageNotificationDtoMapper bisMessageNotificationDtoMapper;
	@Override
	public PageData<BisMessageNotificationDto> findListPage(PageData<BisMessageNotificationDto> pageDate,
			BisMessageNotificationDto bisMessageNotificationDto) {
		PageHelper.startPage(pageDate.getPageNumber(), pageDate.getPageSize());
		List<BisMessageNotificationDto> findListPage = bisMessageNotificationDtoMapper.list(bisMessageNotificationDto);
		Page<BisMessageNotificationDto> page = (Page<BisMessageNotificationDto>) findListPage;
		pageDate.setTotal(page.getTotal());
		pageDate.setRows(findListPage);
		return pageDate;
	}

	@Override
	public void saveNotification(BisMessageNotificationDto bisMessageNotificationDto) {
		bisMessageNotificationDto.setCreateTime(new Date());
		bisMessageNotificationDto.setId(bisMessageNotificationDto.getIdentity());
		if(bisMessageNotificationDto.getMessageContent() != null && bisMessageNotificationDto.getMessageContent().length() > 500){
			bisMessageNotificationDto.setMessageContent(bisMessageNotificationDto.getMessageContent().substring(0, 500));
		}
		bisMessageNotificationDtoMapper.insert(bisMessageNotificationDto);
	}

	@Override
	public BisMessageNotificationDto details(String id) {
		return bisMessageNotificationDtoMapper.selectByPrimaryKey(id);
	}

}
