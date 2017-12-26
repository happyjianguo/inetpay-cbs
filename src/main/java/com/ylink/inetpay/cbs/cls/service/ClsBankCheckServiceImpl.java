package com.ylink.inetpay.cbs.cls.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.ylink.inetpay.cbs.cls.dao.ClsBankCheckDao;
import com.ylink.inetpay.cbs.cls.dao.ClsRecordCheckDao;
import com.ylink.inetpay.common.core.constant.CLSReviewStatus;
import com.ylink.inetpay.common.project.clear.dto.ClsBankCheck;

/**
 * @类名称： ClsBankCheckServiceImpl
 * @类描述： 渠道对账文件监控管理
 * @创建人： 1603254
 * @创建时间： 2016-5-27 下午3:56:32
 *
 * @修改人： 1603254
 * @操作时间： 2016-5-27 下午3:56:32
 * @操作原因： 
 * 
 */
@Service("clsBankCheckService")
public class ClsBankCheckServiceImpl implements ClsBankCheckService{

	@Autowired
	private ClsBankCheckDao bankCheckDao;
	@Autowired
	private ClsRecordCheckDao recordCheckDao;
	
	/**
	 * @方法描述:  查询资金
	 * @作者： 1603254
	 * @日期： 2016-5-27-下午2:54:25
	 * @param pageData
	 * @param check
	 * @return 
	 * @返回类型： ClsBankCheck
	 */
	public PageData<ClsBankCheck> findBankCheck(PageData<ClsBankCheck> pageData,ClsBankCheck check){
		PageHelper.startPage(pageData.getPageNumber(),pageData.getPageSize());
		List<ClsBankCheck> list=bankCheckDao.queryClsBankCheck(check);
		Page<ClsBankCheck> page=(Page<ClsBankCheck>) list;
		pageData.setTotal(page.getTotal());
		pageData.setRows(list);
		return pageData;
	}
	
	
	
	/**
	 * @方法描述:  
	 * @作者： 1603254
	 * @日期： 2016-5-27-下午2:55:45
	 * @param id
	 * @return 
	 * @返回类型： String
	 */
	public ClsBankCheck findById(String id){
		return bankCheckDao.queryClsBankCheckById(id);
	}
	
	@Override
	public boolean isEqual(String keyId, String currentUserLoginName,
			CLSReviewStatus wait){
		if("0".equals(recordCheckDao.isEqual(keyId,currentUserLoginName,wait))){
			return false;
		}else{
			return true;
		}
	}
}
