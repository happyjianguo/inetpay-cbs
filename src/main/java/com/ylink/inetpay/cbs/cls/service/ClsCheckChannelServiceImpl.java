package com.ylink.inetpay.cbs.cls.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.ylink.inetpay.cbs.chl.service.ChlBankService;
import com.ylink.inetpay.cbs.cls.dao.ClsChannelBillDao;
import com.ylink.inetpay.cbs.cls.dao.ClsRecordCheckDao;
import com.ylink.inetpay.common.core.constant.CLSReviewStatus;
import com.ylink.inetpay.common.core.dto.ReporHeadDto;
import com.ylink.inetpay.common.project.channel.dto.TbChlBank;
import com.ylink.inetpay.common.project.clear.dto.ClsChannelBillVo;

/**
 * @类名称： ClsCheckChannelService
 * @类描述： 资金渠道接口类
 * @创建人： 1603254
 * @创建时间： 2016-5-25 下午4:46:17
 *
 * @修改人： 1603254
 * @操作时间： 2016-5-25 下午4:46:17
 * @操作原因： 
 * 
 */
@Service("clsCheckChannelService")
public class ClsCheckChannelServiceImpl implements ClsCheckChannelService{

	@Autowired
	private  ClsChannelBillDao channelBillDao;
	@Autowired
	private ClsRecordCheckDao recordCheckDao;
	@Autowired
	private ChlBankService chlBankService;
	
	/**
	 * @方法描述:  查找资金渠道记录(审核)
	 * @作者： 1603254
	 * @日期： 2016-5-25-下午4:52:25
	 * @param pageDate
	 * @param channel
	 * @return 
	 * @返回类型： PageData<ClsChannelBillVo>
	*/
	public PageData<ClsChannelBillVo> findCheckChannelReview(PageData<ClsChannelBillVo> pageDate,
			ClsChannelBillVo channel){
		PageHelper.startPage(pageDate.getPageNumber(),pageDate.getPageSize());
		List<ClsChannelBillVo> list=channelBillDao.queryChannelReview(channel);
		getBankName(list);
		Page<ClsChannelBillVo> page=(Page<ClsChannelBillVo>) list;
		pageDate.setTotal(page.getTotal());
		pageDate.setRows(list);
		return pageDate;
	}
	
	/**
	 * @方法描述:  查找资金渠道记录(审核结果)
	 * @作者： 1603254
	 * @日期： 2016-5-25-下午4:52:25
	 * @param pageDate
	 * @param channel
	 * @return 
	 * @返回类型： PageData<ClsChannelBillVo>
	 */
	public PageData<ClsChannelBillVo> findCheckChannelReviewResult(PageData<ClsChannelBillVo> pageDate,
			ClsChannelBillVo channel){
		PageHelper.startPage(pageDate.getPageNumber(),pageDate.getPageSize());
		List<ClsChannelBillVo> list=channelBillDao.queryChannelReviewResult(channel);
		getBankName(list);
		Page<ClsChannelBillVo> page=(Page<ClsChannelBillVo>) list;
		pageDate.setTotal(page.getTotal());
		pageDate.setRows(list);
		return pageDate;
	}
	
	
	/**
	 * @方法描述:  查找资金渠道记录(调账)
	 * @作者： 1603254
	 * @日期： 2016-5-25-下午4:52:25
	 * @param pageDate
	 * @param channel
	 * @return 
	 * @返回类型： PageData<ClsChannelBillVo>
	 */
	public PageData<ClsChannelBillVo> findCheckChannelAdjust(PageData<ClsChannelBillVo> pageDate,
			ClsChannelBillVo channel){
		PageHelper.startPage(pageDate.getPageNumber(),pageDate.getPageSize());
		List<ClsChannelBillVo> list=channelBillDao.queryChannelAdjust(channel);
		getBankName(list);
		Page<ClsChannelBillVo> page=(Page<ClsChannelBillVo>) list;
		pageDate.setTotal(page.getTotal());
		pageDate.setRows(list);
		return pageDate;
	}

	/**
	 * @方法描述:  根据记录审核表id查找
	 * @作者： 1603254
	 * @日期： 2016-5-25-下午4:19:38
	 * @param id
	 * @return 
	 * @返回类型： ClsChannelBillVo
	 */
	public ClsChannelBillVo queryById(String id) {
		ClsChannelBillVo clsChannelBillVo = channelBillDao.queryById(id);
		String bankType = clsChannelBillVo.getBankType();
		if(!StringUtils.isBlank(bankType)){
			TbChlBank bank = chlBankService.getBankByBankType(bankType);
			if(bank!=null){
				clsChannelBillVo.setBankName(bank.getBankName());
			}
		}
		return clsChannelBillVo;
	}

	 
	public ReporHeadDto findCheckChannelSummary(ClsChannelBillVo channel) {
		ReporHeadDto report=channelBillDao.queryChannelSummary(channel);
		return report;	
	}
	
	/**
	 * @方法描述:  查找资金渠道记录
	 * @作者： 1603254
	 * @日期： 2016-5-25-下午4:52:25
	 * @param pageDate
	 * @param channel
	 * @return 
	 * @返回类型： PageData<ClsChannelBillVo>
	 */
	public PageData<ClsChannelBillVo> findCheckChannel(
			PageData<ClsChannelBillVo> pageDate, ClsChannelBillVo channel) {
		PageHelper.startPage(pageDate.getPageNumber(),pageDate.getPageSize());
		List<ClsChannelBillVo> list=channelBillDao.queryChannel(channel);
		getBankName(list);
		Page<ClsChannelBillVo> page=(Page<ClsChannelBillVo>) list;
		pageDate.setTotal(page.getTotal());
		pageDate.setRows(list);
		return pageDate;
	}
	
	@Override
	public boolean isEqual(String keyId, String currentUserLoginName,
			CLSReviewStatus wait){
		if("0".equals(recordCheckDao.isEqual(keyId,currentUserLoginName,wait))){
			return false;
		}else{
			return true;
		}
	}
	
	/**
	 * 根据银行类型获取银行名称
	 */
	@SuppressWarnings("unused")
	private void getBankName(List<ClsChannelBillVo> clsChannelBilVos){
		if(clsChannelBilVos.isEmpty()){
			return ;
		}
		ArrayList<String> bankTypes = new ArrayList<>();
		for (ClsChannelBillVo clsChannelBillVo : clsChannelBilVos) {
			bankTypes.add(clsChannelBillVo.getBankType());
		}
		if(bankTypes.isEmpty()){
			return;
		}
		List<TbChlBank> chlBanks=chlBankService.getBankByBankTypes(bankTypes);
		if(chlBanks.isEmpty()){
			return;
		}
		HashMap<String, String> bankNameMap = new HashMap<String,String>();
		for (TbChlBank tbChlBank : chlBanks) {
			bankNameMap.put(tbChlBank.getBankType(), tbChlBank.getBankName());
		}
		for (ClsChannelBillVo clsChannelBillVo : clsChannelBilVos) {
			clsChannelBillVo.setBankName(bankNameMap.get(clsChannelBillVo.getBankType()));
		}
	}
}
