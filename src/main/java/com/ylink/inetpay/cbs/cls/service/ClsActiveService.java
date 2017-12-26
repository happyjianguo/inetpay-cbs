package com.ylink.inetpay.cbs.cls.service;

import java.util.List;

import org.ylinkpay.framework.core.model.PageData;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.ylink.inetpay.common.project.clear.dto.ClsBankCheck;
import com.ylink.inetpay.common.project.clear.dto.ClsTradeStationVo;

/**
 * @类名称： ClsActiveService
 * @类描述： 活跃情况查询
 * @创建人： 1603254
 * @创建时间： 2016-5-31 下午6:29:43
 *
 * @修改人： 1603254
 * @操作时间： 2016-5-31 下午6:29:43
 * @操作原因： 
 * 
 */
public interface ClsActiveService {

	
	/**
	 * @方法描述:  
	 * @作者： 1603254
	 * @日期： 2016-5-31-下午6:31:28
	 * @param pageData
	 * @param station
	 * @return 
	 * @返回类型： PageData<ClsTradeStationVo>
	*/
	public PageData<ClsTradeStationVo> queryActiveUser(PageData<ClsTradeStationVo> pageData,
			ClsTradeStationVo station);
	
	/**
	 * @方法描述:   
	 * @作者： 1603254
	 * @日期： 2016-5-31-下午6:31:30
	 * @param pageData
	 * @param station
	 * @return 
	 * @返回类型： PageData<ClsTradeStationVo>
	*/
	public PageData<ClsTradeStationVo> queryActiveMer(PageData<ClsTradeStationVo> pageData,
			ClsTradeStationVo station);
	 
}
