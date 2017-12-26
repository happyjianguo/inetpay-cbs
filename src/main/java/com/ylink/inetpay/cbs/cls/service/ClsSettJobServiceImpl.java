package com.ylink.inetpay.cbs.cls.service;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.ylink.inetpay.cbs.cls.dao.ClsSettJobDao;
import com.ylink.inetpay.common.project.clear.dto.ClsClearJob;
import com.ylink.inetpay.common.project.clear.dto.ClsSettJob;
@Service("clsSettJobService")
public class ClsSettJobServiceImpl implements ClsSettJobService{
	@Autowired
	private ClsSettJobDao clsSettJobDao;
	/**
	 * 查询清分任务监控借口实现类
	 */
	
	public PageData<ClsSettJob> queryClsSettJob(PageData<ClsSettJob> pageDate,
			ClsSettJob clsSettJob) {
		PageHelper.startPage(pageDate.getPageNumber(),pageDate.getPageSize());
		List<ClsSettJob> list=clsSettJobDao.queryAll(clsSettJob);
		Page<ClsSettJob> page=(Page<ClsSettJob>) list;
		pageDate.setTotal(page.getTotal());
		pageDate.setRows(list);
		return pageDate;
	}
	@Override
	public ClsSettJob detail(String id) {
		 
		return clsSettJobDao.queryById(id);
	}
}
