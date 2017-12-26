package com.ylink.inetpay.cbs.cls.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.ylink.inetpay.cbs.cls.dao.ClsPayBillDao;
import com.ylink.inetpay.cbs.cls.dao.ClsRecordCheckDao;
import com.ylink.inetpay.common.core.constant.CLSCheckStatus;
import com.ylink.inetpay.common.core.constant.CLSReviewStatus;
import com.ylink.inetpay.common.core.dto.ReporHeadDto;
import com.ylink.inetpay.common.project.cbs.exception.CbsCheckedException;
import com.ylink.inetpay.common.project.clear.dto.ClsPayBillVo;

/**
 * @类名称： ClsCheckInnerServiceImpl
 * @类描述： 内部对账服务类
 * @创建人： 1603254
 * @创建时间： 2016-5-24 下午3:30:04
 *
 * @修改人： 1603254
 * @操作时间： 2016-5-24 下午3:30:04
 * @操作原因： 
 * 
 */
@Service("clsCheckInnerService")
public class ClsCheckInnerServiceImpl implements ClsCheckInnerService{

	@Autowired
	private ClsPayBillDao payBillDao;
	@Autowired
	private ClsRecordCheckDao recordCheckDao;
	
	/**
	 * 支付流水查询（调账列表查询）
	 * @param pageDate
	 * @param chlParamDto
	 * @return
	 * @throws CbsCheckedException
	 */
	public PageData<ClsPayBillVo> findCheckDetailAdjust(
			PageData<ClsPayBillVo> pageDate, ClsPayBillVo payBill) {
		PageHelper.startPage(pageDate.getPageNumber(),pageDate.getPageSize());
		List<ClsPayBillVo> list=payBillDao.queryPayBillAdjust(payBill);
		Page<ClsPayBillVo> page=(Page<ClsPayBillVo>) list;
		pageDate.setTotal(page.getTotal());
		pageDate.setRows(list);
		return pageDate;
	}
	
	
	/**
	 * 支付流水查询（审核列表查询）
	 * @param pageDate
	 * @param chlParamDto
	 * @return
	 * @throws CbsCheckedException
	 */
	public PageData<ClsPayBillVo> findCheckDetailReview(
			PageData<ClsPayBillVo> pageDate, ClsPayBillVo payBill) {
		PageHelper.startPage(pageDate.getPageNumber(),pageDate.getPageSize());
		List<ClsPayBillVo> list=payBillDao.queryPayBillReview(payBill);
		Page<ClsPayBillVo> page=(Page<ClsPayBillVo>) list;
		pageDate.setTotal(page.getTotal());
		pageDate.setRows(list);
		return pageDate;
	}
	
	/**
	 * 支付流水查询（审核结果列表查询）
	 * @param pageDate
	 * @param chlParamDto
	 * @return
	 * @throws CbsCheckedException
	 */
	public PageData<ClsPayBillVo> findCheckDetailReviewResult(
			PageData<ClsPayBillVo> pageDate, ClsPayBillVo payBill) {
		PageHelper.startPage(pageDate.getPageNumber(),pageDate.getPageSize());
		List<ClsPayBillVo> list=payBillDao.queryPayBillReviewResult(payBill);
		Page<ClsPayBillVo> page=(Page<ClsPayBillVo>) list;
		pageDate.setTotal(page.getTotal());
		pageDate.setRows(list);
		return pageDate;
	}
	
	/**
	 * 支付流水查询（明细查询所有审核状态）
	 * @param pageDate
	 * @param chlParamDto
	 * @return
	 * @throws CbsCheckedException
	 */
	public PageData<ClsPayBillVo> findCheckDetail(
			PageData<ClsPayBillVo> pageDate, ClsPayBillVo payBill) {
		PageHelper.startPage(pageDate.getPageNumber(),pageDate.getPageSize());
		List<ClsPayBillVo> list=payBillDao.queryPayBill(payBill);
		Page<ClsPayBillVo> page=(Page<ClsPayBillVo>) list;
		pageDate.setTotal(page.getTotal());
		pageDate.setRows(list);
		return pageDate;
	}
	
	/**
	 * @方法描述:  根据id查找
	 * @作者： 1603254
	 * @日期： 2016-5-25-下午4:19:38
	 * @param id
	 * @return 
	 * @返回类型： ClsChannelBillVo
	*/
	public ClsPayBillVo queryById(String id){
		return payBillDao.queryById(id);
	}
	
	/**
	 * @方法描述:  汇总查询
	 * @作者： 1603254
	 * @日期： 2016-5-26-上午10:57:55
	 * @param payBill
	 * @return 
	 * @返回类型： ReporHeadDto
	*/
	public ReporHeadDto queryPayBillSumarry(ClsPayBillVo payBill) {
		return payBillDao.queryPayBillSumarry(payBill);
	}
	
	public ReporHeadDto getReporHead(ClsPayBillVo payBill){
		ReporHeadDto checkInnerDetail1=null;
		ReporHeadDto checkInnerDetail2=null;
		ReporHeadDto exceptionCheckInnerDetail2=null;
		payBill.setAcctCheckStatus(CLSCheckStatus.BALENCE);
		checkInnerDetail1 = payBillDao.CheckInnerDetail(payBill);
		payBill.setAcctCheckStatus(CLSCheckStatus.UN_BALENCE);
		checkInnerDetail2 = payBillDao.CheckInnerDetail(payBill);
		exceptionCheckInnerDetail2 = payBillDao.CheckInnerDetail2(payBill);
		checkInnerDetail2.setSuccAmt(exceptionCheckInnerDetail2.getSuccAmt()+checkInnerDetail2.getSuccAmt());
		checkInnerDetail2.setSuccNum(exceptionCheckInnerDetail2.getSuccNum()+checkInnerDetail2.getSuccNum());
		ReporHeadDto reporHeadDto = new ReporHeadDto();
		reporHeadDto.setSuccAmt(checkInnerDetail1.getSuccAmt());
		reporHeadDto.setSuccNum(checkInnerDetail1.getSuccNum());
		reporHeadDto.setFailAmt(checkInnerDetail2.getSuccAmt());
		reporHeadDto.setFailNum(checkInnerDetail2.getSuccNum());
		reporHeadDto.setAllAmt(checkInnerDetail1.getSuccAmt()+checkInnerDetail2.getSuccAmt());
		reporHeadDto.setAllNum(checkInnerDetail1.getSuccNum()+checkInnerDetail2.getSuccNum());
		return reporHeadDto;
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
}
