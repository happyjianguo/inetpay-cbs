package com.ylink.inetpay.cbs.cls.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.ylink.inetpay.cbs.cls.dao.ClsMerCheckDao;
import com.ylink.inetpay.common.project.clear.dto.ClsAccessCheck;

@Service("clsMerCheckStatusService")
public class ClsMerCheckStatusServiceImpl implements ClsMerCheckStatusService {

	@Autowired
	private ClsMerCheckDao merCheckDao;
	
	/**
	 * @方法描述:  根据id查找
	 * @作者： 1603254
	 * @日期： 2016-5-30-下午2:31:47
	 * @param id
	 * @return 
	 * @返回类型： ClsMerCheck
	*/
	public ClsAccessCheck queryById(String id){
		return merCheckDao.queryById(id);
	}
	
	/**
	 * @方法描述: 根据条件查询
	 * @作者： 1603254
	 * @日期： 2016-5-30-下午2:31:58
	 * @param pageData
	 * @param check
	 * @return 
	 * @返回类型： PageData<ClsMerCheck>
	*/
	public PageData<ClsAccessCheck> queryMerCheck(PageData<ClsAccessCheck> pageData,
			ClsAccessCheck check){
		PageHelper.startPage(pageData.getPageNumber(),pageData.getPageSize());
		List<ClsAccessCheck> list=merCheckDao.queryMerCheck(check);
		Page<ClsAccessCheck> page=(Page<ClsAccessCheck>) list;
		pageData.setTotal(page.getTotal());
		pageData.setRows(list);
		return pageData;
	}
	
	
	

	/**
	 * @方法描述:  更新状态
	 * @作者： 1603254
	 * @日期： 2016-5-30-下午2:53:14
	 * @param check 
	 * @返回类型： void
	*/
	public void updateMerCheck(ClsAccessCheck check){
		merCheckDao.updateMerCheck(check);
	}
}

