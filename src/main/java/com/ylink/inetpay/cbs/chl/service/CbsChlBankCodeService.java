package com.ylink.inetpay.cbs.chl.service;

import java.util.List;
import java.util.Map;

import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.common.project.cbs.exception.CbsCheckedException;
import com.ylink.inetpay.common.project.channel.dto.TbChlBankCode;
import com.ylink.inetpay.common.project.channel.exception.ChannelCheckedException;
/**
 * 银行行号查询
 * @author pst18
 *
 */
public interface CbsChlBankCodeService {
	/**
	 * 保存银行行号信息
	 * @param bankCode
	 * @throws ChannelCheckedException
	 */
	public void saveBacnkCode(TbChlBankCode bankCode) throws CbsCheckedException;
	/**
	 * 修改银行行号信息
	 * @param bankCode
	 * @throws ChannelCheckedException
	 */
	public void updateBankCodeById(TbChlBankCode bankCode) throws CbsCheckedException;
	/**
	 * 删除银行行号信息
	 * @param id
	 * @throws ChannelCheckedException
	 */
	public void deleteBankCodeId(String id) throws CbsCheckedException;
	/**
	 * 分页查询银行行号信息
	 * @param bankCode
	 * @return
	 * @throws ChannelCheckedException
	 */
	public PageData<TbChlBankCode> queryAllData(PageData<TbChlBankCode> pageData,TbChlBankCode bankCode) throws CbsCheckedException;
	/**
	 * 通过ID获取银行行号
	 * @param id
	 * @return
	 */
	public TbChlBankCode getBankCodeById(String id);
	/**
	 * 根据银行城市代码和银行行号获取精确银行信息
	 * @param cityCode
	 * @param bankCode
	 * @return
	 */
	public List<TbChlBankCode> getDepositBankCode(String cityCode, String bankType);

	
	
	/**
	 * 查询银行相关
	 * @param bankCode
	 * @return
	 */
	public List<TbChlBankCode> getList(TbChlBankCode bankCode);

    public List<TbChlBankCode> findBankNameByBankCode(String bankCode);
    
    public List<TbChlBankCode> findBankByBankName(String bankName);
    /**
	 * 获取所有的银行信息，bankType+cityCode作为key
	 * @return
	 */
	public Map<String, Map<String, TbChlBankCode>> findChlBankCodeMapByBankNames(List<String> queryBankNames);
	/**
	 * 按照bankType分组，获取没组第一条数据
	 * @return
	 */
	public Map<String, TbChlBankCode> findChlBankMapGroupByBankType();
}
