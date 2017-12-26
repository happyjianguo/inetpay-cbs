package com.ylink.inetpay.cbs.cls.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.ylink.inetpay.cbs.cls.dao.ClsPayBookDao;
import com.ylink.inetpay.common.project.clear.dto.ClsPayBook;
@Service("clsPayBookService")
public class ClsPayBookServiceImpl implements ClsPayBookService{
	@Autowired
	private ClsPayBookDao clsPayBookDao;
	/**
	 * 查询支付流水
	 */
	
	public PageData<ClsPayBook> queryClsPayBook(PageData<ClsPayBook> pageData, ClsPayBook clsPayBook) {
		PageHelper.startPage(pageData.getPageNumber(),pageData.getPageSize());
		List<ClsPayBook> list =clsPayBookDao.queryClsPayBook(clsPayBook);
		Page<ClsPayBook> page = (Page<ClsPayBook>) list;
		pageData.setTotal(page.getTotal());
		pageData.setRows(list);
		return pageData;
	}
}
