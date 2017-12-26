package com.ylink.inetpay.cbs.chl.app;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ylink.inetpay.cbs.chl.service.ChlAssetsService;
import com.ylink.inetpay.common.project.account.app.ActHistoryAccountAppService;
import com.ylink.inetpay.common.project.account.dto.ActHistoryAccountDto;
import com.ylink.inetpay.common.project.account.dto.ActSubjectDto;
import com.ylink.inetpay.common.project.channel.app.ChlAssetsAppService;
/**
 * 资产负载报表
 * @author yc
 *
 */
@Service("chlAssetsAppService")
public class ChlAssetsAppServiceImpl implements ChlAssetsAppService {
	@Autowired
	private  ChlAssetsService chlAssetsService;

	/**
	 * 获取所有二级科目对象
	 */
	@Override
	public List<ActSubjectDto> findActSubject(String lastAccountDate) {
		return chlAssetsService.findActSubject(lastAccountDate);
	}
	/**
	 * 获取一级科目
	 */
	@Override
	public List<ActSubjectDto> queryActSubjectDto() {
		 
		return chlAssetsService.queryActSubjectDto();
	}
	@Override
	public List<ActHistoryAccountDto> findAccountBySubjectNo2AndAccountDate(List<String> subject1002List,
			String lastAccountDate) {
		return chlAssetsService.findAccountBySubjectNo2AndAccountDate(subject1002List,lastAccountDate);
	}
}
