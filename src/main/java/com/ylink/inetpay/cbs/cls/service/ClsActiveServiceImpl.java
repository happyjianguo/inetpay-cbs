package com.ylink.inetpay.cbs.cls.service;

import java.text.DecimalFormat;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.ylink.inetpay.cbs.cls.dao.ClsPayBillDao;
import com.ylink.inetpay.cbs.pay.dao.PayBookDtoMapper;
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
@Service("clsActiveService")
public class ClsActiveServiceImpl implements ClsActiveService{

	@Autowired
	private ClsPayBillDao payBillDao;
	@Autowired
	private PayBookDtoMapper bookDtoMapper;

	
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
			ClsTradeStationVo station){
		PageHelper.startPage(pageData.getPageNumber(),pageData.getPageSize());
		List<ClsTradeStationVo> list=bookDtoMapper.queryActiveUserStation(station);
//		List<ClsTradeStationVo> list=new ArrayList<ClsTradeStationVo>(); 
//		List<String> tradeTypes=new ArrayList<String>(); 
//		EBusiType busiType = station.getBusiType();
//		if (busiType == null ||busiType == EBusiType.RECHARGE) {
//			//业务类型：充值，交易类型：【余额充值】【红包充值】
//			tradeTypes.add(ETradeType.ACCOUNT_RECHARGE.getValue());	
//			tradeTypes.add(ETradeType.REDP_RECHARGE.getValue());	
//			station.setTradeTypes(tradeTypes);
//			station.setBusiType(EBusiType.RECHARGE);
//			list.addAll(payBillDao.queryActiveUser(station));
//		}
//		if (busiType == null ||busiType == EBusiType.WITHDRAW) {
//			//提现 2种， 选【提现至提现中间户】
//			tradeTypes.clear();
//			tradeTypes.add(ETradeType.WITHDRAW_TO_TEMP_ACCOUNT.getValue());
//			station.setTradeTypes(tradeTypes);
//			station.setBusiType(EBusiType.WITHDRAW);
//			list.addAll(payBillDao.queryActiveUser(station));
////			list.addAll(payWithdrawDtoMapper.queryWithdrawStation(station));
//		}
//		if (busiType == null ||busiType == EBusiType.TRANSFER) {
//			//转账3种 【余额转账至余额】【余额转账至转账中间户】
//			tradeTypes.clear();
//			tradeTypes.add(ETradeType.ACCOUNT_TRANSFER_ACCOUNT.getValue());	
//			tradeTypes.add(ETradeType.ACCOUNT_TRANSFER_TEMP_ACCOUNT.getValue());	
//			station.setTradeTypes(tradeTypes);
//			station.setBusiType(EBusiType.TRANSFER);
//			list.addAll(payBillDao.queryActiveUser(station));
////			list.addAll(payTransferDtoMapper.queryTransferStation(station));
//		}
//		if (busiType == null ||busiType == EBusiType.PAY) {
//			//支付2种【余额支付】【红包支付】
//			tradeTypes.clear();
//			tradeTypes.add(ETradeType.ACCOUNT_PAY.getValue());	
//			tradeTypes.add(ETradeType.REDP_PAY.getValue());	
//			station.setTradeTypes(tradeTypes);
//			station.setBusiType(EBusiType.PAY);
//			list.addAll(payBillDao.queryActiveUser(station));
//		}
//		if (busiType == null ||busiType == EBusiType.REFUND) {
//			//退款5种【支付退款至红包】【支付退款至余额】
//			tradeTypes.clear();
//			tradeTypes.add(ETradeType.REFUND_TO_REDP.getValue());	
//			tradeTypes.add(ETradeType.REFUND_TO_ACCOUNT.getValue());	
//			station.setTradeTypes(tradeTypes);
//			station.setBusiType(EBusiType.REFUND);
//			list.addAll(payBillDao.queryActiveUser(station));
//		}
//		//红包发放【红包发放】
//		if (busiType == null ||busiType == EBusiType.REDP_DISTRIBUTE) {
//			tradeTypes.clear();
//			tradeTypes.add(ETradeType.REDP_DISTRIBUTE.getValue());	
//			station.setTradeTypes(tradeTypes);
//			station.setBusiType(EBusiType.REDP_DISTRIBUTE);
//			list.addAll(payBillDao.queryActiveUser(station));
//		}
		Page<ClsTradeStationVo> page=(Page<ClsTradeStationVo>) list;
		pageData.setTotal(page.getTotal());
		pageData.setRows(list);
		return pageData;
	}
	
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
			ClsTradeStationVo station){
		PageHelper.startPage(pageData.getPageNumber(),pageData.getPageSize());
		List<ClsTradeStationVo> list=bookDtoMapper.queryActiveMer(station);
		DecimalFormat   format=new  DecimalFormat("#.0000");
		for(ClsTradeStationVo t:list){
			if(t.getSuccCount() == 0){
				t.setRate(0.0);
			}else{
				double result=new Double(t.getSuccCount())/new Double(t.getAllCount());
				t.setRate(Double.parseDouble(format.format(result)));
			}	
		}
		Page<ClsTradeStationVo> page=(Page<ClsTradeStationVo>) list;
		pageData.setTotal(page.getTotal());
		pageData.setRows(list);
		return pageData;
	}
}
