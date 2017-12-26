package com.ylink.inetpay.cbs.chl.service;

import java.util.List;

import com.ylink.inetpay.common.project.account.dto.ActHistoryAccountDto;
import com.ylink.inetpay.common.project.account.dto.ActSubjectDto;

/**
 * 资产负债报表
 * @author yc
 *
 */
public interface ChlAssetsService {
	
	/**
	 * 获取二级科目对象
	 * @param lastAccountDate
	 * @return
	 */
	List<ActSubjectDto> findActSubject(String lastAccountDate);
	/**
	 *  获取一级科目
	 * @return
	 */
	List<ActSubjectDto> queryActSubjectDto();
	/**
	 通二级科目获取三级
	 * @param subject1002List
	 * @param lastAccountDate
	 * @return
	 */
	List<ActHistoryAccountDto> findAccountBySubjectNo2AndAccountDate(List<String> subject1002List,
			String lastAccountDate);
	
}
