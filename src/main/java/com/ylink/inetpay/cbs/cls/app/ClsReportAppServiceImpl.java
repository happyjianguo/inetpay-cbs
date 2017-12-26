package com.ylink.inetpay.cbs.cls.app;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.cbs.act.service.ActBalanceService;
import com.ylink.inetpay.cbs.cls.service.ClsActiveService;
import com.ylink.inetpay.cbs.cls.service.ClsBankFeeReportService;
import com.ylink.inetpay.cbs.cls.service.ClsDebtReportService;
import com.ylink.inetpay.cbs.cls.service.ClsMerSettleService;
import com.ylink.inetpay.cbs.cls.service.ClsMerTradeService;
import com.ylink.inetpay.cbs.cls.service.ClsProfitReportService;
import com.ylink.inetpay.cbs.cls.service.ClsReportService;
import com.ylink.inetpay.cbs.cls.service.ClsShareReportService;
import com.ylink.inetpay.common.core.dto.ReporHeadDto;
import com.ylink.inetpay.common.project.account.dto.ActBalanceDto;
import com.ylink.inetpay.common.project.account.dto.ActBillBalanceDto;
import com.ylink.inetpay.common.project.cbs.app.ClsReportAppService;
import com.ylink.inetpay.common.project.clear.dto.ClsBankFeeRepDto;
import com.ylink.inetpay.common.project.clear.dto.ClsChannelBill;
import com.ylink.inetpay.common.project.clear.dto.ClsDebtReport;
import com.ylink.inetpay.common.project.clear.dto.ClsMerSett;
import com.ylink.inetpay.common.project.clear.dto.ClsProfitReport;
import com.ylink.inetpay.common.project.clear.dto.ClsReserveReport;
import com.ylink.inetpay.common.project.clear.dto.ClsShareDetail;
import com.ylink.inetpay.common.project.clear.dto.ClsShareReport;
import com.ylink.inetpay.common.project.clear.dto.ClsTradeDetailVo;
import com.ylink.inetpay.common.project.clear.dto.ClsTradeStationVo;

/**
 * @类名称： ClsReportAppService
 * @类描述： 报表管理
 * @创建人： 1603254
 * @创建时间： 2016-5-30 下午4:56:12
 *
 * @修改人： 1603254
 * @操作时间： 2016-5-30 下午4:56:12
 * @操作原因： 
 * 
 */
@Service("clsReportAppService")
public class ClsReportAppServiceImpl  implements ClsReportAppService{
 
	@Autowired 
	private ActBalanceService balanceService;
	@Autowired
	private ClsDebtReportService debtReportService;
	@Autowired
	private ClsReportService reportService;
	@Autowired
	private ClsMerSettleService settleService;
	@Autowired
	private ClsMerTradeService tradeService;
	@Autowired
	private ClsShareReportService shareReportService;
	@Autowired
	private ClsActiveService activeService;
	@Autowired
	private ClsProfitReportService profitReportService;
	@Autowired
	private ClsBankFeeReportService clsBankFeeReportService;
	/**
	 * @方法描述:  试算平衡报表查询（账务系统）
	 * @作者： 1603254
	 * @日期： 2016-5-30-下午4:03:44
	 * @param pageData
	 * @param balance
	 * @return 
	 * @返回类型： PageData<ActBalanceDto>
	*/
	public ActBalanceDto queryBalance(String acctDate){
		return balanceService.queryBalance(acctDate);
	}
	
	/**
	 * @方法描述:  记账分录试算平衡表 查询
	 * @作者： 1603254
	 * @日期： 2016-7-1-下午3:59:17
	 * @param actDate
	 * @return 
	 * @返回类型： ActBillBalanceDto
	*/
	public ActBillBalanceDto queryBillBalance(String actDate){
		return balanceService.queryBillBalance(actDate);
	}
	
	/**
	 * @方法描述:  资产负债报表查询（清结算系统）
	 * @作者： 1603254
	 * @日期： 2016-5-30-下午4:03:44
	 * @param pageData
	 * @param balance
	 * @return 
	 */
	public List<ClsDebtReport> queryDebt(String acctDate){
		return debtReportService.queryDebtReport(acctDate);
	}
	
	
	/**
	 * @方法描述: 利润报表（账务系统，账户历史表）
	 * @作者： 1603254
	 * @日期： 2016-5-30-下午4:28:53
	 * @param acctDate
	 * @return 
	 * @返回类型： List<ActHistoryAccountDto>
	*/
	public PageData<ClsProfitReport> queryProfitReport(PageData<ClsProfitReport> pageData,
			ClsProfitReport report){
		return profitReportService.queryProfitReport(pageData, report);
	}
	
	
	/**
	 * @方法描述: 备付金报表（清结算系统）
	 * @作者： 1603254
	 * @日期： 2016-5-30-下午4:28:53
	 * @param acctDate
	 * @return 
	 * @返回类型： List<ActHistoryAccountDto>
	 */
	public List<ClsReserveReport> queryReserve(String calDate){
		return reportService.queryReserveReport(calDate);
	}
	
	
	/**
	 * @方法描述: 商户结算报表（清结算系统）
	 * @作者： 1603254
	 * @日期： 2016-5-30-下午4:28:53
	 * @param acctDate
	 * @return 
	 * @返回类型： List<ActHistoryAccountDto>
	 */
	public PageData<ClsMerSett> queryMerSett(PageData<ClsMerSett> pageData,
			ClsMerSett merSett){
		return settleService.queryClsMerSett(pageData, merSett);
	}
	
	/**
	 * @方法描述:  商户结算报表 信息汇总
	 * @作者： 1603254
	 * @日期： 2016-5-31-上午11:35:44
	 * @param merSett
	 * @return 
	 * @返回类型： ReporHeadDto
	*/
	public ReporHeadDto queryMerSettSummary(ClsMerSett merSett){
		return settleService.queryMerSettSummary(merSett);
	}
	
	/**
	 * @方法描述:  商户日交易明细报表 
	 * @作者： 1603254
	 * @日期： 2016-5-30-下午5:09:53
	 * @param pageData
	 * @param payBill
	 * @return 
	 * @返回类型： PageData<ClsPayBill>
	*/
	public PageData<ClsTradeDetailVo> queryMerDeail(PageData<ClsTradeDetailVo> pageData,
			ClsTradeDetailVo detail){
		return tradeService.queryTradeDetail(pageData, detail);
	}
	
	/**
	 * @方法描述:  商户日交易明细(查清结算系统) 
	 * @作者： 1603254
	 * @日期： 2016-5-30-下午5:09:53
	 * @param pageData
	 * @param payBill
	 * @return 
	 * @返回类型： PageData<ClsPayBill>
	 */
	public PageData<ClsTradeDetailVo> queryClsMerTradeDeail(PageData<ClsTradeDetailVo> pageData,
			ClsTradeDetailVo detail){
		return tradeService.queryClsMerTradeDetail(pageData, detail);
	}
	
	
	
	/**
	 * @方法描述:  商户日交易明细报表（清结算系统）
	 * @作者： 1603254
	 * @日期： 2016-5-30-下午5:09:53
	 * @param pageData
	 * @param payBill
	 * @return 
	 * @返回类型： PageData<ClsPayBill>
	 */
	public ReporHeadDto queryMerDeailSummary(ClsTradeDetailVo detail){
		return tradeService.queryMerTradeDetailSummary(detail);
	}
	
	
	/**
	 * @方法描述: 商户交易汇总报表（清结算系统）
	 * @作者： 1603254
	 * @日期： 2016-5-30-下午5:09:53
	 * @param pageData
	 * @param payBill
	 * @param type 取值为1或者2，  1：根据交易日期查询 ， 2：根据结算日期查询
	 * @return 
	 * @返回类型： PageData<ClsPayBill>
	 */
	public PageData<ClsTradeStationVo> queryMerSummary(PageData<ClsTradeStationVo> pageData,
			ClsTradeStationVo station,Integer type){
		return tradeService.queryMerTradeSummary(pageData, station, type);
	}
	
	/**
	 * @方法描述: 交易情况汇总报表（清结算系统）
	 * @作者： 1603254
	 * @日期： 2016-5-30-下午5:09:53
	 * @param pageData
	 * @param payBill
	 * @return 
	 * @返回类型： PageData<ClsPayBill>
	 */
	public PageData<ClsTradeStationVo> queryPayBillSummary(PageData<ClsTradeStationVo> pageData,
			ClsTradeStationVo summary){
		return tradeService.queryTradeStation(pageData,summary);
	}



	/**
	 * @方法描述: 手续费分润报表（清结算系统）
	 * @作者： 1603254
	 * @日期： 2016-5-30-下午5:09:53
	 * @param pageData
	 * @param payBill
	 * @return 
	 * @返回类型： PageData<ClsPayBill>
	 */
	public PageData<ClsShareReport> queryFeeShareSummary(PageData<ClsShareReport> pageData,
			ClsShareReport report){
		return shareReportService.queryClsShareReport(pageData, report);
	}
	
	/**
	 * @方法描述: 根据id查找分润报表记录
	 * @作者： 1603254
	 * @日期： 2016-6-2-上午10:33:39
	 * @param id
	 * @return 
	 * @返回类型： ClsShareReport
	*/
	public ClsShareReport queryShareReportById(String id){
		return shareReportService.queryById(id);
	}

	/**
	 * @方法描述: 活跃用户情况报表
	 * @作者： 1603254
	 * @日期： 2016-5-31-下午4:32:02
	 * @param pageData
	 * @param summary
	 * @return 
	 * @返回类型： PageData<ClsTradeStationVo>
	*/
	public PageData<ClsTradeStationVo> queryActiveUser(PageData<ClsTradeStationVo> pageData,
			ClsTradeStationVo station){
		return activeService.queryActiveUser(pageData, station);		
	}
	
	/**
	 * @方法描述:活跃商户情况报表
	 * @作者： 1603254
	 * @日期： 2016-5-31-下午4:32:14
	 * @param pageData
	 * @param summary
	 * @return 
	 * @返回类型： PageData<ClsTradeStationVo>
	*/
	public PageData<ClsTradeStationVo> queryActiveMer(PageData<ClsTradeStationVo> pageData,
			ClsTradeStationVo station){
		return activeService.queryActiveMer(pageData, station);
	}

 
	/**
	 * @方法描述: 查询分润明细
	 * @作者： 1603254
	 * @日期： 2016-6-2-上午9:51:35
	 * @param pageData
	 * @param detail
	 * @return 
	 * @返回类型： PageData<ClsShareDetail>
	*/
	public PageData<ClsShareDetail> queryShareDetail(PageData<ClsShareDetail> pageData,
			ClsShareDetail detail){
		return shareReportService.queryClsShareDetail(pageData, detail);
	}
	
	
	/**
	 * @方法描述:  手续费分润报表表头
	 * @作者： 1603254
	 * @日期： 2016-6-2-上午10:50:33
	 * @param report
	 * @return 
	 * @返回类型： ReporHeadDto
	*/
	public ReporHeadDto  queryShareReportSummary(ClsShareReport report){
		return	shareReportService.queryShareReportSummary(report);
	}
	
	
	/**
	 * @方法描述:  手续费分润明细报表表头
	 * @作者： 1603254
	 * @日期： 2016-6-2-上午10:50:33
	 * @param report
	 * @return 
	 * @返回类型： ReporHeadDto
	 */
	public ReporHeadDto  queryShareDetailSummary(ClsShareDetail detail){
		return	shareReportService.queryShareDetailSummary(detail);
	}

	@Override
	public PageData<ClsBankFeeRepDto> queryBankFee(PageData<ClsBankFeeRepDto> pageData, ClsBankFeeRepDto report) {
		return clsBankFeeReportService.queryBankFee(pageData, report);
	}

	@Override
	public PageData<ClsChannelBill> queryBankFeeDetail(PageData<ClsChannelBill> pageData, ClsChannelBill report) {
		return clsBankFeeReportService.queryBankFeeDetail(pageData, report);
	}

	@Override
	public List<ClsBankFeeRepDto> queryAllBankFee(ClsBankFeeRepDto report) {
		return clsBankFeeReportService.queryAllBankFee(report);
	}

	@Override
	public List<ClsChannelBill> queryAllBankFeeDetail(ClsChannelBill report) {
		return clsBankFeeReportService.queryAllBankFeeDetail(report);
	}
}
