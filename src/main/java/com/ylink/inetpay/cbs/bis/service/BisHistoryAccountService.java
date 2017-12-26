package com.ylink.inetpay.cbs.bis.service;

import java.util.List;

import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.common.project.account.dto.ActHistoryAccountDto;

/**
 * @类名称： ActHistoryAccountService
 * @类描述： 
 * @创建人： 1603254
 * @创建时间： 2016-5-30 下午7:50:34
 *
 * @修改人： 1603254
 * @操作时间： 2016-5-30 下午7:50:34
 * @操作原因： 
 * 
 */
public interface BisHistoryAccountService {

	 /**
     * @方法描述:   查询账户历史表
     * @作者： 1603254
     * @日期： 2016-5-30-下午7:45:48
     * @param accountDate
     * @return 
     * @返回类型： List<ActHistoryAccountDto>
    */
    public List<ActHistoryAccountDto> queryHisAccount(String accountDate);
    
    /**
	 * 与科目表关联查询
	 * @param actHistoryAccountDto
	 * @return
	 */
	public PageData<ActHistoryAccountDto> getList(PageData<ActHistoryAccountDto> pageDate,ActHistoryAccountDto actHistoryAccountDto);
	/**
	 * 根据账户号查询
	 * @param accountId
	 * @return
	 */
	public ActHistoryAccountDto selectByAccountId(String accountId);
	/**
	 * 根据账户编号和账户日期获历史账户详情
	 * @param id
	 * @param accountDate
	 * @return
	 */
	public ActHistoryAccountDto selectByAccountIdAndAccountDate(String id, String accountDate);
}
