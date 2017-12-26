package com.ylink.inetpay.cbs.cls.dao;

import java.util.List;

import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

import com.ylink.inetpay.common.core.dto.ReporHeadDto;
import com.ylink.inetpay.common.project.clear.dto.CLsMerchantSettleVo;
import com.ylink.inetpay.common.project.clear.dto.ClsChannelBillVo;
import com.ylink.inetpay.common.project.clear.dto.ClsMerSett;

/**
 * @类名称： ClsMerSettleDao
 * @类描述： 商户结算dao
 * @创建人： 1603254
 * @创建时间： 2016-5-25 下午2:01:38
 *
 * @修改人： 1603254
 * @操作时间： 2016-5-25 下午2:01:38
 * @操作原因： 
 * 
 */
@MybatisMapper("clsMerSettleDao")
public interface ClsMerSettleDao {

	/**
	 * @方法描述: 查询商户结算记录
	 * @作者： 1603254
	 * @日期： 2016-5-25-下午2:34:44
	 * @return 
	 * @返回类型： List<CLsMerchantSettleVo>
	*/
	List<CLsMerchantSettleVo> queryClsMerSett(CLsMerchantSettleVo settle);
	
	/**
	 * @方法描述: 查询商户结算记录
	 * @作者： 1603254
	 * @日期： 2016-5-25-下午2:34:44
	 * @return 
	 * @返回类型： List<CLsMerchantSettleVo>
	 */
	List<ClsMerSett> queryMerSett(ClsMerSett merSett);
	
	/**
	 * @方法描述:  根据id查找
	 * @作者： 1603254
	 * @日期： 2016-5-25-下午4:19:38
	 * @param id
	 * @return 
	 * @返回类型： ClsChannelBillVo
	*/
	public CLsMerchantSettleVo queryById(String id);
	
	
	/**
	 * @方法描述: 
	 * @作者： 1603254
	 * @日期： 2016-5-31-上午11:45:49
	 * @param settle
	 * @return 
	 * @返回类型： ReporHeadDto
	*/
	public ReporHeadDto queryMerSettSummary(ClsMerSett settle);
	
	/**
	 * 获取重新结算审核列表
	 * @param queryParam
	 * @return
	 */
	List<CLsMerchantSettleVo> findMerSettleAudit(CLsMerchantSettleVo queryParam);
}
