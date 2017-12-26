package com.ylink.inetpay.cbs.mrs.App;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.cbs.mrs.service.MrsAccountAduitInfoService;
import com.ylink.inetpay.common.project.cbs.app.MrsAccountAduitInfoAppService;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsAccountAduitInfoDto;

@Service("mrsAccountAduitInfoAppService")
public class MrsAccountAduitInfoAppServiceImpl implements MrsAccountAduitInfoAppService{
    
	@Autowired
	private MrsAccountAduitInfoService mrsAccountAduitInfoService;
	
	@Override
	public MrsAccountAduitInfoDto selectByPrimaryKey(String id) {
		return mrsAccountAduitInfoService.selectByPrimaryKey(id);
	}

	@Override
	public void updateByPrimaryKey(MrsAccountAduitInfoDto record) {
		mrsAccountAduitInfoService.updateByPrimaryKey(record);
	}

	@Override
	public PageData<MrsAccountAduitInfoDto> findListPage(PageData<MrsAccountAduitInfoDto> pageData,
			MrsAccountAduitInfoDto infoDto) {
		return mrsAccountAduitInfoService.findListPage(pageData, infoDto);
	}

	@Override
	public PageData<MrsAccountAduitInfoDto> getByCont(PageData<MrsAccountAduitInfoDto> pageData,
			MrsAccountAduitInfoDto queryParam) {
		return mrsAccountAduitInfoService.getByCont(pageData, queryParam);
	}

}
