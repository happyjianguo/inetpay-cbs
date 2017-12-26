package com.ylink.inetpay.cbs.chl.service;

import java.util.List;

import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.common.core.constant.EStatus;
import com.ylink.inetpay.common.project.cbs.exception.CbsCheckedException;
import com.ylink.inetpay.common.project.channel.dto.ChlChlFeeDto;
/**
 * 银行信息服务类
 * @author haha
 *
 */
public interface CbsChlChlFeeService {
	/**
	 * 计费模板列表
	 */
	public PageData<ChlChlFeeDto> findAll(PageData<ChlChlFeeDto> pageData,
			ChlChlFeeDto queryParam);
	/**
	 * 新增计费模板
	 * 
	 */
	public int saveTemplate(ChlChlFeeDto template)throws CbsCheckedException;
	/**
	 * 修改计费模板
	 * @param tenplate
	 * @return
	 * @throws CbsCheckedException
	 */
	public int updateTemplate(ChlChlFeeDto tenplate)throws CbsCheckedException;
	/**
	 * 删除计费模板
	 * @param id
	 * @return
	 * @throws CbsCheckedException
	 */
	public int deleteTemplate(String id)throws CbsCheckedException;
	/**
	 * 启用/停用
	 * @param status
	 * @param id
	 * @return
	 */
	public int blockUp(EStatus status,String id) throws CbsCheckedException;
	/**
	 * 根据id查看费率详情
	 * @param id
	 * @return
	 */
	public ChlChlFeeDto findById(String id);
	/**
	 * 查询所有渠道名称
	 * @return
	 */
	public List<ChlChlFeeDto> queryAllChannels()throws CbsCheckedException;
}
