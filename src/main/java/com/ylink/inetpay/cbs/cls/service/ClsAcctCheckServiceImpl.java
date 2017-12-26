package com.ylink.inetpay.cbs.cls.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.ylink.inetpay.cbs.cls.dao.ClsAcctCheckDao;
import com.ylink.inetpay.common.project.clear.dto.ClsAcctCheck;

/**
 * @类名称： ClsAcctCheckService
 * @类描述： 内部对账监控服务类
 * @创建人： 1603254
 * @创建时间： 2016-5-27 下午4:05:32
 *
 * @修改人： 1603254
 * @操作时间： 2016-5-27 下午4:05:32
 * @操作原因： 
 * 
 */
@Service("clsAcctCheckService")
public class ClsAcctCheckServiceImpl implements ClsAcctCheckService {

	@Autowired
	private ClsAcctCheckDao acctCheckDao;
	
	/**
	 * @方法描述:  查询对账监控的记录
	 * @作者： 1603254
	 * @日期： 2016-5-27-下午4:06:40
	 * @param pageData
	 * @param book
	 * @return 
	 * @返回类型： PageData<ClsAcctBook>
	*/
	public PageData<ClsAcctCheck> findAcctCheck(PageData<ClsAcctCheck> pageData,
			ClsAcctCheck check){
		PageHelper.startPage(pageData.getPageNumber(),pageData.getPageSize());
		List<ClsAcctCheck> list=acctCheckDao.queryClsAcctCheck(check);
		Page<ClsAcctCheck> page=(Page<ClsAcctCheck>) list;
		pageData.setTotal(page.getTotal());
		pageData.setRows(list);
		return pageData;
	}
	
	
	
	/**
	 * @方法描述:  重新执行失败的记录
	 * @作者： 1603254
	 * @日期： 2016-5-27-下午4:07:19
	 * @param id
	 * @return 
	 * @返回类型： String
	*/
	public ClsAcctCheck findById(String id){
		return acctCheckDao.queryClsAcctCheckById(id);
	}

	/**
	 * @方法描述: 更新状态为未处理
	 * @作者： 1603254
	 * @日期： 2016-5-27-下午4:33:25 
	 * @返回类型： void
	*/
	public void updateDealStatusToUnProcess(String id) {
		acctCheckDao.updateDealStatusToUnProcess(id);
	}

}
