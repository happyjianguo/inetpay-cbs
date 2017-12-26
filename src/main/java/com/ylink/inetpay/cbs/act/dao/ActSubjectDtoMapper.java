package com.ylink.inetpay.cbs.act.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

import com.ylink.inetpay.common.core.constant.EActBusiRefSubBusiType;
import com.ylink.inetpay.common.core.constant.EBookType;
import com.ylink.inetpay.common.project.account.dto.ActHistoryAccountDto;
import com.ylink.inetpay.common.project.account.dto.ActSubjectDto;

@MybatisMapper("actSubjectDtoMapper")
public interface ActSubjectDtoMapper {
	int deleteByPrimaryKey(String id);

	int insert(ActSubjectDto record);

	int insertSelective(ActSubjectDto record);

	ActSubjectDto selectByPrimaryKey(String id);

	int updateByPrimaryKeySelective(ActSubjectDto record);

	int updateByPrimaryKey(ActSubjectDto record);

	/**
	 * 根据参数查询所有账务汇总数据
	 * 
	 * @param actSubjectDto
	 * @return
	 */
	List<ActSubjectDto> queryAllData(ActSubjectDto actSubjectDto);
	List<ActSubjectDto> listAll();
	
	List<ActSubjectDto> findBySubjecNo(String subjectNo);
	
	List<ActSubjectDto> list(ActSubjectDto actSubjectDto);
	/**
	 * 根据账本类型获取科目列表
	 * @param value
	 * @return
	 */
	List<ActSubjectDto> getSubjects(String value);
	
	List<ActSubjectDto> findLevelOneSubject();
	/**
	 * 根据科目获取子科目
	 * @param subjectNo
	 * @return
	 */
	List<ActSubjectDto> findByParentSubjectNo(String subjectNo);
	/**
	 * 根据账本类型获取一级科目列表
	 * @param value
	 * @return
	 */
	List<ActSubjectDto> findLevelOneSubjectByBookType(@Param("bookType")EBookType bookType);
    
	List<ActSubjectDto> findBysubjectsNo(@Param("subjectsNo") List<String> subjectsNo);
	
	ActSubjectDto getSubjectName(@Param("subjectNo2")String subjectNo2);
	/**
	 * 
	 *方法描述：根据类型编号查询科目信息
	 * 创建人：ydx
	 * 创建时间：2017年4月1日 下午4:15:47
	 * @param acctTypeNo
	 * @return
	 */
	List<ActSubjectDto> findByAcctTypeNo(@Param("acctTypeNo")String acctTypeNo,@Param("busiTypes")List<EActBusiRefSubBusiType> busiTypes);
	/**
	 * 获取二级科目
	 * @param lastAccountDate
	 * @return
	 */
	List<ActSubjectDto> findActSubject(@Param("lastAccountDate")String lastAccountDate);
	/**
	 * 获取所有一级科目
	 * @return
	 */
	List<ActSubjectDto> queryActSubjectDto();

 
}