package com.ylink.inetpay.cbs.cls.service;

import java.text.DecimalFormat;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.ylink.inetpay.cbs.cls.dao.ClsProfitReportDao;
import com.ylink.inetpay.cbs.cls.dao.ClsShareReportDao;
import com.ylink.inetpay.common.core.constant.EAccountParentType;
import com.ylink.inetpay.common.core.constant.EProfitType;
import com.ylink.inetpay.common.core.util.DateUtils;
import com.ylink.inetpay.common.project.clear.dto.ClsProfitReport;

/**
 * @类名称： ClsProfitReportService
 * @类描述： 利润报表
 * @创建人： 1603254
 * @创建时间： 2016-6-2 下午3:35:28
 *
 * @修改人： 1603254
 * @操作时间： 2016-6-2 下午3:35:28
 * @操作原因： 
 * 
 */
@Service("clsProfitReportService")
public class ClsProfitReportServiceImpl implements ClsProfitReportService{

	@Autowired
	private ClsProfitReportDao profitReportDao;
	@Autowired
	private ClsShareReportDao shareReportDao;
	
	private static final Logger logger=LoggerFactory.getLogger(ClsProfitReportServiceImpl.class);
	
	/**
	 * @方法描述: 利润报表 查询
	 * @作者： 1603254
	 * @日期： 2016-6-2-下午3:36:50
	 * @param pageData
	 * @param report
	 * @return 
	 * @返回类型： PageData<ClsProfitReport>
	*/
	public PageData<ClsProfitReport> queryProfitReport(PageData<ClsProfitReport> pageData,
			ClsProfitReport report){
		PageHelper.startPage(pageData.getPageNumber(),pageData.getPageSize());
		List<ClsProfitReport> list=profitReportDao.queryProfitReport(report.getAcctDate());
		if(list !=null && list.size()>0){
			//统计利润
			Long lastIncome=0L,currIncome=0L,currPay=0L,lastPay=0L;
			for(ClsProfitReport r:list){
				if(r.getSubjectOne() == EProfitType.PAY){
					currPay+=r.getCurrAmt();
					lastPay+=r.getLastAmt();
				}else if(r.getSubjectOne() == EProfitType.INCOME){
					currIncome+=r.getCurrAmt().longValue();
					lastIncome+=r.getLastAmt();
				}
			}
			ClsProfitReport r=new ClsProfitReport();
			r.setLastAmt(lastIncome-lastPay);
			r.setCurrAmt(currIncome-currPay);
			generateRate(r);
	        r.setSubjectOne(EProfitType.PROFIT);
			list.add(r);
		}
		Page<ClsProfitReport> page=(Page<ClsProfitReport>) list;
		pageData.setTotal(page.getTotal());
		pageData.setRows(list);
		return pageData;
	}
	
	
	private void generateRate(ClsProfitReport r){
		if(r.getLastAmt() == 0  && r.getCurrAmt()==0){
			r.setChangeRate(new Double(0));
		}else if(r.getLastAmt() != 0  && r.getCurrAmt()==0){
			r.setChangeRate(new Double(100));
		}else if(r.getLastAmt() == 0  && r.getCurrAmt()!=0){
			r.setChangeRate(new Double(100));
		}else if(r.getLastAmt().equals(r.getCurrAmt())){
			r.setChangeRate(new Double(0));
		}else if(r.getLastAmt() != 0  && r.getCurrAmt()!=0){
			DecimalFormat   format=new  DecimalFormat("#.00");
			Double d1=new Double(r.getCurrAmt());
			Double d2=new Double(r.getLastAmt());
			Double result=(d1-d2)/d1;
			result = result > 0 ? result : -result;
			r.setChangeRate(Double.parseDouble(format.format(result*100)));
		}
	}
}
