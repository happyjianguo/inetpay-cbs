package com.ylink.inetpay.cbs.act.service;

import java.util.List;

import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.common.core.constant.EBookType;
import com.ylink.inetpay.common.project.account.dto.ActSubjectDto;
import com.ylink.inetpay.common.project.account.dto.ActSubjectListDto;

public interface ActSubjectService {
	/**
	 * 根据参数查询所有账务汇总数据,返回科目树形结果
	 * 
	 * @param actSubjectDto
	 * @return
	 */
	ActSubjectListDto queryAllDataByTree(ActSubjectDto actSubjectDto);
	
	ActSubjectDto findById(String id);
	
	List<ActSubjectDto> findBySubjecNo(String subjectNo);
	
	PageData<ActSubjectDto> listPage(PageData<ActSubjectDto> pageData, ActSubjectDto actSubjectDto);

	List<ActSubjectDto> getSubjects(String value);
	List<ActSubjectDto> findLevelOneSubject();
	/**
	 * 根据科目获取子科目
	 * @param subjectNo
	 * @return
	 */
	List<ActSubjectDto> findByParentSubjectNo(String subjectNo);
	/**
	 * 根据账本类型获取一级科目
	 * @param bookTypeEnum
	 * @return
	 */
	List<ActSubjectDto> findLevelOneSubject(EBookType bookTypeEnum);
	/**
	 * 
	 *方法描述：根据  账户类型编号联合查询账户类型所属科目信息
	 * 创建人：ydx
	 * 创建时间：2017年4月1日 下午3:51:26
	 * @param acctTypeNo  账户类型编号
	 * @return
	 */
	List<ActSubjectDto> findByAcctTypeNo(String acctTypeNo);
}
