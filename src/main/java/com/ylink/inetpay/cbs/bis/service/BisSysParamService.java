package com.ylink.inetpay.cbs.bis.service;
import java.util.List;

import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.common.project.cbs.dto.bis.BisSysParamDto;
import com.ylink.inetpay.common.project.cbs.exception.CbsCheckedException;
public interface BisSysParamService {
		/**
		 * @方法描述: 分页查询
		 * @作者： hinode
		 * @日期： 2016-4-28-下午4:21:41
		 * @param pageVO
		 * @param bisSysParamDto
		 * @return
		 * @throws CbsCheckedException 
		 * @返回类型： List<BisSysParamDto>
		 */
		public PageData<BisSysParamDto> findListPage(PageData<BisSysParamDto> pageVO, BisSysParamDto bisSysParamDto);
		/**
		 * @方法描述:查询所有的系统参数
		 * @作者： hinode
		 * @日期： 2016-4-28-下午4:22:00
		 * @return
		 * @throws CbsCheckedException 
		 * @返回类型： Map<String,String> 第一个String为参数代码，第二个String为参数值
		 */
		//public Map<String, String> mapAllCode();

		/**
		 * @方法描述: 根据参数代码查询参数值
		 * @作者： hinode
		 * @日期： 2016-4-28-下午4:22:52
		 * @param parmCode
		 * @return
		 * @throws CbsCheckedException 
		 * @返回类型： String
		 */
		public String getValue(String parmCode);
		
		/**
		 * @方法描述:修改参数信息
		 * @作者： hinode
		 * @日期： 2016-4-28-下午4:23:25
		 * @param record
		 * @throws CbsCheckedException 
		 * @返回类型： void
		 */
		public void updateSelective(BisSysParamDto record);
		/**
		 * 根据id获取系统参数详情
		 * @param id
		 * @return
		 * @throws CbsCheckedException
		 */
		public BisSysParamDto details(String id);
		/**
		 * 
		 * @return
		 */
		public List<BisSysParamDto> getLimitParams(String groupName);
		public BisSysParamDto selectById(String id);
		public BisSysParamDto selectByKey(String key);
		/**
		 * 查询多个系统参数信息
		 * 
		 * @param paramNames
		 * @return
		 */
		List<BisSysParamDto> findByParamNames(List<String> paramNames);
		/**
		 * 保存商户渠道信息
		 * @param merchantChannelId
		 * @param string
		 */
		public void saveMerChantChannelId(String merchantChannelId, String string);
}
