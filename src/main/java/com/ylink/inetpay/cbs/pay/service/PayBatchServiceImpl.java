package com.ylink.inetpay.cbs.pay.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.ylinkpay.framework.core.model.PageData;
import org.ylinkpay.framework.core.util.UuidUtil;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.ylink.inetpay.cbs.bis.service.BisBatchExpService;
import com.ylink.inetpay.cbs.common.CbsConstants;
import com.ylink.inetpay.cbs.mrs.dao.MrsPayBatchDetailDtoMapper;
import com.ylink.inetpay.cbs.mrs.dao.MrsPayBatchDtoMapper;
import com.ylink.inetpay.common.core.util.DateUtils;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsPayBatchDetailDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsPayBatchDto;
import com.ylink.inetpay.common.project.portal.vo.UserCheckVO;

@Service("payBatchService")
public class PayBatchServiceImpl implements PayBatchService {
	//前缀
	public static final String PORTAIL_FLAG = "P";
	@Autowired
	private MrsPayBatchDtoMapper mrsPayBatchDtoMapper;
	@Autowired
	private MrsPayBatchDetailDtoMapper mrsPayBatchDetailDtoMapper;
	@Autowired
	private BisBatchExpService bisBatchExpService;
	@Override
	public PageData<MrsPayBatchDto> queryAllData(PageData<MrsPayBatchDto> pageDate, MrsPayBatchDto mrsPayBatchDto) {
		PageHelper.startPage(pageDate.getPageNumber(), pageDate.getPageSize());
		List<MrsPayBatchDto> list = mrsPayBatchDtoMapper.queryAllData(mrsPayBatchDto);
		Page<MrsPayBatchDto> page = (Page<MrsPayBatchDto>) list;
		pageDate.setTotal(page.getTotal());
		pageDate.setRows(list);
		return pageDate;
	}
	@Override
	public PageData<MrsPayBatchDto> queryAllDataAudit(PageData<MrsPayBatchDto> pageDate, MrsPayBatchDto mrsPayBatchDto) {
		PageHelper.startPage(pageDate.getPageNumber(), pageDate.getPageSize());
		List<MrsPayBatchDto> list = mrsPayBatchDtoMapper.queryAllDataAudit(mrsPayBatchDto);
		Page<MrsPayBatchDto> page = (Page<MrsPayBatchDto>) list;
		pageDate.setTotal(page.getTotal());
		pageDate.setRows(list);
		return pageDate;
	}
	@Override
	public List<MrsPayBatchDetailDto> selectByBatchNo(String batchNo) {
		return mrsPayBatchDetailDtoMapper.selectByBatchNo(batchNo);
	}
	/**
	 * 获取批次号
	 * @return
	 */
	public String getBatchNo(){
		String batchNo=bisBatchExpService.getBatchNo();
		String formatStr = "%08d";
		batchNo = DateUtils.getCurrentDate()+String.format(formatStr,Long.valueOf(batchNo));
		return PORTAIL_FLAG+batchNo;
	}
	@Override
	public MrsPayBatchDto payBatchByKeyId(String keyId) {
		return mrsPayBatchDtoMapper.selectByPrimaryKey(keyId);
	}
	@Override
	@Transactional(value = CbsConstants.TX_MANAGER_MRS)
	public UserCheckVO savePayBatchDetail(List<MrsPayBatchDetailDto> detailList,MrsPayBatchDto mrsPayBatchDto) {
		UserCheckVO result = new UserCheckVO(true);
		if(detailList!=null && detailList.size()>0){
			//获取批次号
			String batchNo = getBatchNo();
			//交易总笔数
			long totalNum=0l;
			//交易总金额
			long totalAmount=0l;
			//订单商品代码
			String productCode="";
			//商户交易日期
			String merOrderDate= DateUtils.getCurrentDate();
			for(MrsPayBatchDetailDto detailDto:detailList){
				detailDto.setId(UuidUtil.getUUID().toString());
				detailDto.setBatchNo(batchNo);
				//商户交易日期
				detailDto.setMerOrderDate(merOrderDate);
				detailDto.setCreateTime(new Date());
				productCode=detailDto.getProductCode();
				totalNum++;
				totalAmount=totalAmount+detailDto.getAmount();
			}
			//save date
			mrsPayBatchDetailDtoMapper.batchInsert(detailList);
			mrsPayBatchDto.setId(UuidUtil.getUUID().toString());
			mrsPayBatchDto.setBatchNo(batchNo);
			mrsPayBatchDto.setProductCode(productCode);
			//交易总笔数
			mrsPayBatchDto.setTotalNum(totalNum);
			//交易总金额
			mrsPayBatchDto.setTotalAmount(totalAmount);
			//商户交易日期
			mrsPayBatchDto.setMerOrderDate(merOrderDate);
			mrsPayBatchDto.setCreateTime(new Date());
			mrsPayBatchDto.setNotifyUrl("payNotify");
			mrsPayBatchDtoMapper.insert(mrsPayBatchDto);
		}else{
			result.setCheckValue(false);
			result.setMsg("保存数据为空");
		}
		return result;
	}
	@Override
	@Transactional(value = CbsConstants.TX_MANAGER_MRS)
	public void updateByPrimaryKeySelective(MrsPayBatchDto record) {
		mrsPayBatchDtoMapper.updateByPrimaryKeySelective(record);		
	}
}
