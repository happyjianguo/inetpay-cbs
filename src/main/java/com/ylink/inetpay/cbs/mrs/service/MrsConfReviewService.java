package com.ylink.inetpay.cbs.mrs.service;

import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.common.core.constant.EConfReviewBusiType;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsConfReviewDto;

/**
 * 
 * @author pst10
 * 类名称：MrsConfAcctService
 * 类描述：子账户操作服务
 * 创建时间：2017年3月31日 下午7:54:09
 */
public interface MrsConfReviewService {
	/**
	 * 
	 *方法描述：
	 * 创建人：pst10
	 * 创建时间：2017年5月10日 上午10:57:06
	 * @param pageData
	 * @param searchDto
	 * @return
	 */
	public PageData<MrsConfReviewDto> findPage(PageData<MrsConfReviewDto> pageData, MrsConfReviewDto searchDto);
	/**
	 * 
	 *方法描述：根据主键查询机构复核业务信息
	 * 创建人：pst10
	 * 创建时间：2017年5月10日 上午10:59:41
	 * @param id
	 * @return
	 */
	MrsConfReviewDto selectById(String id);
	/**
	 * 
	 *方法描述：
	 * 创建人：pst10
	 * 创建时间：2017年5月10日 上午11:05:19
	 * @param mrsConfReviewDto
	 */
	public void addOrUpdateConfReview(MrsConfReviewDto mrsConfReviewDto);
	/**
	 * 
	 *方法描述：根据一户通编号，机构复核业务主键查询信息
	 * 创建人：pst10
	 * 创建时间：2017年5月10日 下午2:27:06
	 * @param custId 一户通编号
	 * @param id 主键
	 * @return
	 */
	public MrsConfReviewDto selectByCustIdAndId(String custId,String id);
	/**
	 * 
	 *方法描述：根据一户通编号查询机构复核业务，
	 *如果不存在返回默认的配置信息（配置会有条默认的配置信息）
	 * 创建人：pst10
	 * 创建时间：2017年5月10日 下午2:27:42
	 * @param custId
	 * @return
	 */
	public MrsConfReviewDto selectByCustId(String custId);
	/**
	 * 
	 *方法描述：查询机构复核业务数据，配置的全局复核的参数，默认是全局的
	 * 创建人：pst10
	 * 创建时间：2017年5月10日 下午3:02:42
	 * @return
	 */
	public MrsConfReviewDto selectByIsGlobal();
	/**
	 * 
	 *方法描述：根据一户通号码，机构复核业务主键校验机构是否已经设置数据
	 * 创建人：pst10
	 * 创建时间：2017年5月10日 下午4:02:35
	 * @param custId 一户通号码
	 * @param id 机构复核业务数据主键
	 * @return 如果没有设置返回true ,如果已经设置返回false
	 */
	public boolean checkByCustIdAndId(String custId,String id);
	/**
	 * 
	 *方法描述：根据一户通号码 业务类型判断是否需要复核。如果当前一户通没有设置，
	 *默认所有业务都需要复核
	 * 创建人：pst10
	 * 创建时间：2017年5月17日 上午10:35:42
	 * @param custId 一户通编号
	 * @param Type 业务类型
	 * @return true需要复核，flase不需复核
	 */
	public boolean checkByCustIdAndType(String custId, EConfReviewBusiType busiType);
}
