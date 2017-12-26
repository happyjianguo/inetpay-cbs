package com.ylink.inetpay.cbs.mrs.service;

import com.ylink.inetpay.common.core.constant.EOperaTypeEnum;
import com.ylink.inetpay.common.core.constant.EStartSystemEnum;
import com.ylink.inetpay.common.core.constant.MrsCustTypeEnum;
import com.ylink.inetpay.common.project.cbs.vo.mrs.SaveAduitPersonResponseVo;

/**
 * 
 * @author pst10
 * 类名称：MrsAduitInfoService
 * 类描述：审核数据
 * 创建时间：2017年3月3日 上午12:13:28
 */
public interface MrsAduitInfoService {
	/**
	 * 
	 *方法描述：创建审核数据
	 * 创建人：ydx
	 * 创建时间：2017年3月3日 上午12:23:49
	 * @param remark 经办意见
	 * @param certNo 证件号码
	 * @param certType 证件类型
	 * @param name 名称
	 * @param custId 一户通编号
	 * @param eStartSystemEnum 来源 客户端，柜台
	 * @param eOperaTypeEnum 业务类型
	 * @return
	 */
	public String craateMrsAduitInfo(String remark,
			String certNo,String certType,String name,
			String custId,String acountStatus,String productStatus,
			EStartSystemEnum eStartSystemEnum, String loginUserName,
			EOperaTypeEnum eOperaTypeEnum,
			MrsCustTypeEnum mrsCustTypeEnum);
}
