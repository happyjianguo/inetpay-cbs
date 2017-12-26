package com.ylink.inetpay.cbs.mrs.service;

import java.util.List;

import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.common.project.account.dto.ActSubjectDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsConfAcctDto;
import com.ylink.inetpay.common.project.cbs.exception.CbsUncheckedException;
/**
 * 
 * @author pst10
 * 类名称：MrsConfAcctService
 * 类描述：子账户操作服务
 * 创建时间：2017年3月31日 下午7:54:09
 */
public interface MrsConfAcctService {
	/**
	 * 
	 *方法描述：子账户信息分页查询
	 * 创建人：ydx
	 * 创建时间：2017年3月31日 下午7:48:55
	 * @param page
	 * @param seachDto
	 * @return
	 */
	public PageData<MrsConfAcctDto> findPage(PageData<MrsConfAcctDto> page , MrsConfAcctDto seachDto);
	/**
	 * 
	 *方法描述：根据主键查询子账户信息
	 * 创建人：ydx
	 * 创建时间：2017年3月31日 下午7:48:59
	 * @param id
	 * @return
	 */
	public MrsConfAcctDto selectById(String id);
	/**
	 * 
	 *方法描述：新增或者修改子账户信息
	 * 创建人：ydx
	 * 创建时间：2017年3月31日 下午7:49:04
	 * @param mrsConfAcctDto
	 */
	public void addOrUpdateMrsConfAcct(MrsConfAcctDto mrsConfAcctDto) throws CbsUncheckedException;
	/**
	 * 
	 *方法描述：校验子账户名称唯一 新增时id为空
	 * 创建人：ydx
	 * 创建时间：2017年3月31日 下午7:49:08
	 * @param name 名称
	 * @param id 主键
	 * @return
	 */
	public boolean checkMrsConfAcct(String name,String id);
	/**
	 * 
	 *方法描述：查询所有子账户信息
	 * 创建人：ydx
	 * 创建时间：2017年4月10日 下午3:11:48
	 * @return
	 */
	public List<MrsConfAcctDto> findAll();
	/**
	 *  
	 *方法描述：根据子账号编号查询子账号所属类别下
	 *所有科目信息
	 * 创建人：ydx
	 * 创建时间：2017年4月11日 下午7:44:42
	 * @param id
	 * @return
	 */
	public List<ActSubjectDto> findActByConfAcctId(String id);
}
