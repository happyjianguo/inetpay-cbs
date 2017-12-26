package com.ylink.inetpay.cbs.act.dao;

import java.util.List;

import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

import com.ylink.inetpay.common.project.account.dto.ActCheckFileDto;

/**
 * 
 * 类说明：
 * 实现ActCheckFileDto 的增删查改
 * 
 * <p>
 * 详细描述：
 *   
 * @author 1603254
 *   
 * CreateDate:  2016-5-27
 */
@MybatisMapper("")
public interface ActCheckFileDtoMapper {

 
	/**
	 * 方法说明： 根据时间，处理状态查找
	 * 查询ActCheckFileDto
	 * @param  ActCheckFileDto				
	 * @return List 	查询的结果集
	 */	
	List<ActCheckFileDto> queryActCheckFile(ActCheckFileDto actCheckFile);

	/**
	 * @方法描述:  根据id查找
	 * @作者： 1603254
	 * @日期： 2016-5-27-下午2:06:26
	 * @param id
	 * @return 
	 * @返回类型： ActCheckFileDto
	*/
	ActCheckFileDto queryActCheckFileById(String id);

	/**
	 * @方法描述:  更新状态
	 * @作者： 1603254
	 * @日期： 2016-5-27-下午2:16:41
	 * @param actCheckFile 
	 * @返回类型： void
	*/
	void updateDealStatus(ActCheckFileDto actCheckFile);
	 
}