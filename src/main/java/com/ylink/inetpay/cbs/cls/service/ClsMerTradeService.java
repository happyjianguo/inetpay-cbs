package com.ylink.inetpay.cbs.cls.service;

import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.common.core.dto.ReporHeadDto;
import com.ylink.inetpay.common.project.clear.dto.ClsTradeDetailVo;
import com.ylink.inetpay.common.project.clear.dto.ClsTradeStationVo;

public interface ClsMerTradeService {

	public PageData<ClsTradeDetailVo> queryTradeDetail(PageData<ClsTradeDetailVo>  pageData,ClsTradeDetailVo detail);

	public PageData<ClsTradeDetailVo> queryClsMerTradeDetail(PageData<ClsTradeDetailVo>  pageData,ClsTradeDetailVo detail);
	
	public ReporHeadDto queryMerTradeDetailSummary(ClsTradeDetailVo detail);

	public PageData<ClsTradeStationVo> queryMerTradeSummary(PageData<ClsTradeStationVo> pageData,
			ClsTradeStationVo station,Integer type);
	
	public PageData<ClsTradeStationVo> queryTradeStation(PageData<ClsTradeStationVo> pageData,
			ClsTradeStationVo station);
	
}
