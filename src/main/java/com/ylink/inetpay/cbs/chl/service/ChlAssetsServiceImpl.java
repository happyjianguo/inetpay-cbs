package com.ylink.inetpay.cbs.chl.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ylink.inetpay.cbs.act.dao.ActAccountDtoMapper;
import com.ylink.inetpay.cbs.act.dao.ActHistoryAccountMapper;
import com.ylink.inetpay.cbs.act.dao.ActSubjectDtoMapper;
import com.ylink.inetpay.common.project.account.dto.ActHistoryAccountDto;
import com.ylink.inetpay.common.project.account.dto.ActSubjectDto;

@Service("chlAssetsService")
public class ChlAssetsServiceImpl implements ChlAssetsService {
	 @Autowired
	 private ActSubjectDtoMapper actSubjectDtoMapper;
	@Autowired
	private ActHistoryAccountMapper actHistoryAccountMapper;
	
	@Override
	public List<ActSubjectDto> findActSubject(String lastAccountDate) {
		return actSubjectDtoMapper.findActSubject(lastAccountDate);
	}

	@Override
	public List<ActSubjectDto> queryActSubjectDto() {
		return actSubjectDtoMapper.queryActSubjectDto();
	}

	@Override
	public List<ActHistoryAccountDto> findAccountBySubjectNo2AndAccountDate(List<String> subject1002List,
			String lastAccountDate) {
		 
		return  actHistoryAccountMapper.findAccountBySubjectNo2AndAccountDate(subject1002List,lastAccountDate);
	}
}
