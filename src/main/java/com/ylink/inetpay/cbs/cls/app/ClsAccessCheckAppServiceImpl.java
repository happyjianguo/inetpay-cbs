package com.ylink.inetpay.cbs.cls.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.cbs.cls.service.ClsAccessCheckService;
import com.ylink.inetpay.common.project.cbs.app.ClsAccessCheckAppService;
import com.ylink.inetpay.common.project.clear.dto.ClsAccessCheck;

@Service("clsAccessCheckAppService")
public class ClsAccessCheckAppServiceImpl implements ClsAccessCheckAppService {

	@Autowired
	ClsAccessCheckService clsAccessCheckService;
	
	@Override
	public PageData<ClsAccessCheck> queryAllData(PageData<ClsAccessCheck> pageData, ClsAccessCheck clsAccessCheck) {
		return clsAccessCheckService.queryAllData(pageData, clsAccessCheck);
	}
	
	@Override
	public ClsAccessCheck selectByPrimaryKey(String id) {
		return clsAccessCheckService.selectByPrimaryKey(id);
	}

	@Override
	public byte[] downloadFTPFile(ClsAccessCheck clsAccessCheck) {
		return clsAccessCheckService.download(clsAccessCheck);
	}
}
