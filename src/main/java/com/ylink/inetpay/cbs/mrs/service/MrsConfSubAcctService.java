package com.ylink.inetpay.cbs.mrs.service;

import java.util.List;

import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.common.core.constant.MrsConfSubRelationType;
import com.ylink.inetpay.common.core.constant.MrsCustomerType;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsConfSubAcctDto;
import com.ylink.inetpay.common.project.cbs.exception.CbsUncheckedException;


/**
 * 
 * @author pst10
 * 类名称：MrsConfSubAcctService
 * 类描述：子账户配置服务类
 * 创建时间：2017年2月14日 下午5:35:13
 */
public interface MrsConfSubAcctService {
	/**
	 * 
	 *方法描述：根据客户类型，关联关系查询子账户配置
	 * 创建人：ydx
	 * 创建时间：2017年2月14日 下午5:22:38
	 * @param userType 客户类型
	 * @param relationType 关联关系
	 * @param platformCode 渠道 为空默认为账务系统发起的开户
	 * @return
	 */
	public List<MrsConfSubAcctDto> findByUserTypeAndRationType(MrsCustomerType userType,
			MrsConfSubRelationType relationType,String platformCode);
	
	/**
	 * 根据子账户类型获取子账户信息
	 * @param accountName
	 * @return
	 */
	public List<MrsConfSubAcctDto> findByAccountName(String accountName);
	
	/**
	 * 
	 *方法描述：根据查询条件查询子账户配置信息
	 * 创建人：ydx
	 * 创建时间：2017年3月30日 上午10:41:48
	 * @param page
	 * @param seachDto
	 * @return
	 */
	public PageData<MrsConfSubAcctDto> findPage(PageData<MrsConfSubAcctDto> page , MrsConfSubAcctDto seachDto);
	/**
	 * 
	 *方法描述：根据主键查询子账户配置信息
	 * 创建人：ydx
	 * 创建时间：2017年3月30日 上午10:43:41
	 * @param id
	 * @return
	 */
	public MrsConfSubAcctDto selectById(String id);
	/**
	 * 
	 *方法描述：新增或修改子账户配置信息
	 * 创建人：ydx
	 * 创建时间：2017年3月30日 上午10:49:39
	 * @param confSubAcct
	 */
	public void addOrUpdateMrsConfSubAcct(MrsConfSubAcctDto confSubAcct) throws CbsUncheckedException;
	/**
	 * 
	 *方法描述：根据子账户编号，渠道类型，一户通类型，主键校验子账户配置数据唯一性
	 * 创建人：ydx
	 * 创建时间：2017年3月30日 上午11:01:48
	 * @param subAccountCode 子账户编号
	 * @param platformCode 渠道编码
	 * @param accountType 一户通类型
	 * @param id 主键，（新增时候Id为空，修改的时候Id不为空）
	 * @return
	 */
	public boolean checkMrsConfSubAcct(String subAccountCode,String platformCode,String accountType,String id);
	/**
	 * 
	 *方法描述：根据配置四要素查询子账户配置信息
	 * 创建人：ydx
	 * 创建时间：2017年4月14日 下午3:57:39
	 * @param userType 用户类型
	 * @param relationType 关联关系
	 * @param platformCode 渠道类型
	 * @param accountType 一户通类型
	 * @return
	 */
	public List<MrsConfSubAcctDto> findBy4Elment(MrsCustomerType userType,
			MrsConfSubRelationType relationType,String platformCode,String accountType);
	
	/**
	 * 根据子账户配置表查询子账户信息
	 * @param userType
	 * @param relationType
	 * @param platformCode
	 * @param accountType
	 * @return
	 */
	public List<MrsConfSubAcctDto> findBy4Elments(MrsCustomerType userType,
			MrsConfSubRelationType relationType,String platformCode,String accountType);
}
