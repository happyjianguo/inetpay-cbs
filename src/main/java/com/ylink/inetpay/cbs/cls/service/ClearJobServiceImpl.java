package com.ylink.inetpay.cbs.cls.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.ylink.inetpay.cbs.cls.dao.ClearJobDao;
import com.ylink.inetpay.common.project.clear.dto.ClsClearJob;
@Service("clearJobService")
public class ClearJobServiceImpl implements ClearJobService{
	@Autowired
	private ClearJobDao clearJobDao;
	/**
	 * 查询清分任务监控借口实现类
	 */

	public PageData<ClsClearJob> queryClearJob(PageData<ClsClearJob> pageDate,
			ClsClearJob clsClearJob) {
		PageHelper.startPage(pageDate.getPageNumber(),pageDate.getPageSize());
		List<ClsClearJob> list=clearJobDao.queryAll(clsClearJob);
		Page<ClsClearJob> page=(Page<ClsClearJob>) list;
		pageDate.setTotal(page.getTotal());
		pageDate.setRows(list);
		return pageDate;
	}

	/***
	 * 查询清分任务详情
	 */
	public ClsClearJob detail(String id){
		return clearJobDao.detail(id);
	}

}
