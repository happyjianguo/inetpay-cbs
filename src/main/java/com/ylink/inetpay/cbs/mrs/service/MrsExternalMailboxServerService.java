package com.ylink.inetpay.cbs.mrs.service;

import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsExternalMailboxServerDto;
import com.ylink.inetpay.common.project.cbs.exception.CbsCheckedException;

/**
 * 外部邮箱服务器管理类
 * @author haha
 *
 */
public interface MrsExternalMailboxServerService {
	/**
	 * 外部邮箱服务器列表
	 * @param pageData
	 * @param mrsExternalMailboxServerDto
	 * @return
	 * @throws CbsCheckedException
	 */
	public PageData<MrsExternalMailboxServerDto> findListPage(PageData<MrsExternalMailboxServerDto> pageData,MrsExternalMailboxServerDto mrsExternalMailboxServerDto);
	/**
	 * 新增外部邮箱服务器
	 * @param pageData
	 * @param mrsExternalMailboxServerDto
	 * @return
	 * @throws CbsCheckedException
	 */
	public void insert(MrsExternalMailboxServerDto mrsExternalMailboxServerDto);
	/**
	 * 修改外部邮箱服务器
	 * @param pageData
	 * @param mrsExternalMailboxServerDto
	 * @return
	 * @throws CbsCheckedException
	 */
	public void update(MrsExternalMailboxServerDto mrsExternalMailboxServerDto);
	/**
	 * 删除外部邮箱服务器
	 * @param pageData
	 * @param mrsExternalMailboxServerDto
	 * @return
	 * @throws CbsCheckedException
	 */
	public void delete(String id)throws CbsCheckedException;
	/**
	 * 外部邮箱服务器详情
	 * @param pageData
	 * @param mrsExternalMailboxServerDto
	 * @return
	 * @throws CbsCheckedException
	 */
	public MrsExternalMailboxServerDto details(String id);
	/**
	 * 根据邮箱后缀获取外部邮箱服务器地址
	 * @param pageData
	 * @param mrsExternalMailboxServerDto
	 * @return
	 * @throws CbsCheckedException
	 */
	public String getServerPath(String SUFFI);
}
