package com.ylink.inetpay.cbs.cls.app;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.cbs.cls.service.ClearJobService;
import com.ylink.inetpay.cbs.cls.service.ClsLiquidationJobService;
import com.ylink.inetpay.cbs.cls.service.ClsSettJobService;
import com.ylink.inetpay.common.project.cbs.app.ClearJobAppService;
import com.ylink.inetpay.common.project.cbs.app.ClsLiquidationJobAppService;
import com.ylink.inetpay.common.project.cbs.app.ClsSettJobAppService;
import com.ylink.inetpay.common.project.clear.dto.ClsClearJob;
import com.ylink.inetpay.common.project.clear.dto.ClsLiquidationJob;
import com.ylink.inetpay.common.project.clear.dto.ClsSettJob;

/**
 * @类名称： 结算服务实现类
 * @类描述： 
 * @创建人： 1603254
 * @创建时间： 2016-5-25 上午11:46:02
 *
 * @修改人： 1603254
 * @操作时间： 2016-5-25 上午11:46:02
 * @操作原因： 
 * 
 */
@Service("clsSettJobAppService")
public class ClsSettJobAppServiceImpl  implements ClsSettJobAppService{
	@Autowired
	private ClsSettJobService clsSettJobService;
	/**
	 * 查询清分任务监控
	 */
	public PageData<ClsSettJob> findClsSettJob(PageData<ClsSettJob> pageData,
			ClsSettJob clsSettJob) {
		return clsSettJobService.queryClsSettJob(pageData, clsSettJob);
	}
	@Override
	public ClsSettJob detail(String id) {
		 
		return clsSettJobService.detail(id);
	}

}
