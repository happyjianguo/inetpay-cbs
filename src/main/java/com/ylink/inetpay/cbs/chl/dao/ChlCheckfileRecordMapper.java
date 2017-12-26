package com.ylink.inetpay.cbs.chl.dao;

import java.util.List;

import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

import com.ylink.inetpay.common.project.channel.dto.TbChlCheckfileRecordDto;

/**
 * @类名称： ChlCheckfileRecordDao
 * @类描述： 资金渠道对账文件表 dao
 * @创建人： 1603254
 * @创建时间： 2016-5-30 上午10:05:13
 *
 * @修改人： 1603254
 * @操作时间： 2016-5-30 上午10:05:13
 * @操作原因： 
 * 
 */
@MybatisMapper("chlCheckfileRecordMapper")
public interface ChlCheckfileRecordMapper {

	
	/**
	 * @方法描述:  根据id查找对象
	 * @作者： 1603254
	 * @日期： 2016-5-30-上午10:06:29
	 * @param id
	 * @return 
	 * @返回类型： TbChlCheckfileRecordDto
	*/
	public TbChlCheckfileRecordDto queryById(String id);
	
	
	/**
	 * @方法描述: 查找 TbChlCheckfileRecordDto
	 * @作者： 1603254
	 * @日期： 2016-5-30-上午10:06:27
	 * @return 
	 * @返回类型： List<TbChlCheckfileRecordDto>
	*/
	public List<TbChlCheckfileRecordDto> queryChlCheckfileRecord(TbChlCheckfileRecordDto check);
	
	
	/**
	 * @方法描述:  修改状态
	 * @作者： 1603254
	 * @日期： 2016-5-30-上午10:29:45
	 * @param check 
	 * @返回类型： void
	*/
	public void changeStatus(TbChlCheckfileRecordDto check);
}
