package com.ylink.inetpay.cbs.bis.service;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.ylink.inetpay.cbs.act.service.ActaccountDateService;
import com.ylink.inetpay.cbs.bis.dao.BisBusinessDtoMapper;
import com.ylink.inetpay.cbs.bis.dao.BisProductDtoMapper;
import com.ylink.inetpay.cbs.bis.dao.BisTradeTypeParamDtoMapper;
import com.ylink.inetpay.cbs.bis.dao.BisTradeTypeSettCycleDtoMapper;
import com.ylink.inetpay.cbs.ucs.sec.service.UcsSecUserServiceImpl;
import com.ylink.inetpay.common.core.constant.EBisBusinessSettleCycleType;
import com.ylink.inetpay.common.core.util.DateUtils;
import com.ylink.inetpay.common.project.cbs.constant.bis.ESettleDayType;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisBusinessDto;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisProductDto;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisProductTradeTypePojoDto;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisTradeTypeParamDto;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisTradeTypeSettCycleDto;
import com.ylink.inetpay.common.project.cbs.exception.CbsUncheckedException;

/**
 * Created by pst25 on 2017/3/29.
 */
@Service("bisProductService")
public class BisProductServiceImpl implements BisProductService {

    @Autowired
    BisProductDtoMapper bisProductDtoMapper;
    @Autowired
    private BisTradeTypeParamDtoMapper bisTradeTypeParamDtoMapper;
    @Autowired
    private BisTradeTypeSettCycleDtoMapper bisTradeTypeSettCycleDtoMapper;
    @Autowired
    private ActaccountDateService actaccountDateService;
    @Autowired
    private BisBusinessDtoMapper bisBusinessDtoMapper;
    private static Logger _log = LoggerFactory.getLogger(UcsSecUserServiceImpl.class);
    @Override
    public BisProductDto findById(String id) {
        return bisProductDtoMapper.selectByPrimaryKey(id);
    }

	@Override
	public PageData<BisProductTradeTypePojoDto> pageList(PageData<BisProductTradeTypePojoDto> pageData,
			BisProductTradeTypePojoDto queryParam) {
		PageHelper.startPage(pageData.getPageNumber(),pageData.getPageSize());
		List<BisProductTradeTypePojoDto> list=bisProductDtoMapper.list(queryParam);
		Page<BisProductTradeTypePojoDto> page =(Page<BisProductTradeTypePojoDto>) list;
		pageData.setTotal(page.getTotal());
		pageData.setRows(list);
		return pageData;
	}

	@Override
	public BisProductTradeTypePojoDto findProductTradeById(String id,String settleCycleId) {
		BisProductDto productDto = bisProductDtoMapper.selectByPrimaryKey(id);
		BisTradeTypeSettCycleDto tradeTypeSettCycleDto=null;
		if(StringUtils.isNotBlank(settleCycleId)){
			tradeTypeSettCycleDto = bisTradeTypeSettCycleDtoMapper.selectByPrimaryKey(settleCycleId);
		}
		if(productDto==null){
			throw new CbsUncheckedException("","根据ID【"+id+"】获取产品信息为空");
		}
		BisProductTradeTypePojoDto bisProductTradeTypePojoDto = new BisProductTradeTypePojoDto();
		bisProductTradeTypePojoDto.setId(productDto.getId());
		bisProductTradeTypePojoDto.setProductCode(productDto.getProductCode());
		bisProductTradeTypePojoDto.setProductName(productDto.getProductName());
		bisProductTradeTypePojoDto.setBusinessCode(productDto.getBusinessCode());
		BisBusinessDto businessDto=bisBusinessDtoMapper.findByBusinessCode(productDto.getBusinessCode());
		if(businessDto!=null){
			bisProductTradeTypePojoDto.setBusinessName(businessDto.getBusinessName());
		}
		bisProductTradeTypePojoDto.setCreaterReviserName(productDto.getCreaterReviserName());
		bisProductTradeTypePojoDto.setReviserName(productDto.getReviserName());
		bisProductTradeTypePojoDto.setCreateDate(productDto.getCreateDate());
		bisProductTradeTypePojoDto.setModifyDate(productDto.getModifyDate());
		if(tradeTypeSettCycleDto!=null){
			bisProductTradeTypePojoDto.setTradeTypeSettCycleId(tradeTypeSettCycleDto.getId());
			bisProductTradeTypePojoDto.setTradeType(tradeTypeSettCycleDto.getTradeType());
			bisProductTradeTypePojoDto.setTradeTypeName(tradeTypeSettCycleDto.getTradeTypeName());
			bisProductTradeTypePojoDto.setOffsetStyle(tradeTypeSettCycleDto.getOffsetStyle());
			bisProductTradeTypePojoDto.setAssuranceStyle(tradeTypeSettCycleDto.getAssuranceStyle());
			bisProductTradeTypePojoDto.setSettleCycle(tradeTypeSettCycleDto.getSettleCycle());
			bisProductTradeTypePojoDto.setSettleCycleType(tradeTypeSettCycleDto.getSettleCycleType());
			bisProductTradeTypePojoDto.setFirstSettleDay(tradeTypeSettCycleDto.getFirstSettleDay());
			bisProductTradeTypePojoDto.setLastSettleDay(tradeTypeSettCycleDto.getLastSettleDay());
			bisProductTradeTypePojoDto.setSettleDay(tradeTypeSettCycleDto.getSettleDay());
			bisProductTradeTypePojoDto.setBookType(tradeTypeSettCycleDto.getBookType());
			bisProductTradeTypePojoDto.setTnSettle(tradeTypeSettCycleDto.getTnSettle());
			bisProductTradeTypePojoDto.setSettleDayType(tradeTypeSettCycleDto.getSettleDayType());
			bisProductTradeTypePojoDto.setSettleHour(tradeTypeSettCycleDto.getSettleHour());
			bisProductTradeTypePojoDto.setSettleMinute(tradeTypeSettCycleDto.getSettleMinute());
			bisProductTradeTypePojoDto.setDayOfMonth(tradeTypeSettCycleDto.getDayOfMonth());
			bisProductTradeTypePojoDto.setFeeMode(tradeTypeSettCycleDto.getFeeMode());
			bisProductTradeTypePojoDto.setPayerFeeType(tradeTypeSettCycleDto.getPayerFeeType());
			bisProductTradeTypePojoDto.setPayeeFeeType(tradeTypeSettCycleDto.getPayeeFeeType());
			bisProductTradeTypePojoDto.setPayerFeeRate(tradeTypeSettCycleDto.getPayerFeeRate());
			bisProductTradeTypePojoDto.setPayeeFeeRate(tradeTypeSettCycleDto.getPayeeFeeRate());
			bisProductTradeTypePojoDto.setPayerFeeLow(tradeTypeSettCycleDto.getPayerFeeLow());
			bisProductTradeTypePojoDto.setPayeeFeeLow(tradeTypeSettCycleDto.getPayeeFeeLow());
			bisProductTradeTypePojoDto.setPayerFeeTop(tradeTypeSettCycleDto.getPayerFeeTop());
			bisProductTradeTypePojoDto.setPayeeFeeTop(tradeTypeSettCycleDto.getPayeeFeeTop());
			bisProductTradeTypePojoDto.setFeeTnSettle(tradeTypeSettCycleDto.getFeeTnSettle());
			bisProductTradeTypePojoDto.setFeeSettleCycle(tradeTypeSettCycleDto.getFeeSettleCycle());
			bisProductTradeTypePojoDto.setFeeSettleCycleType(tradeTypeSettCycleDto.getFeeSettleCycleType());
			bisProductTradeTypePojoDto.setFeeFirstSettleDay(tradeTypeSettCycleDto.getFeeFirstSettleDay());
			bisProductTradeTypePojoDto.setFeeLastSettleDay(tradeTypeSettCycleDto.getFeeLastSettleDay());
			bisProductTradeTypePojoDto.setFeeSettleDay(tradeTypeSettCycleDto.getFeeSettleDay());
			bisProductTradeTypePojoDto.setFeeSettleDayType(tradeTypeSettCycleDto.getFeeSettleDayType());
			bisProductTradeTypePojoDto.setFeeSettleHour(tradeTypeSettCycleDto.getFeeSettleHour());
			bisProductTradeTypePojoDto.setFeeSettleMinute(tradeTypeSettCycleDto.getFeeSettleMinute());
			bisProductTradeTypePojoDto.setFeeDayOfMonth(tradeTypeSettCycleDto.getFeeDayOfMonth());
		}
		return bisProductTradeTypePojoDto;
	}

	@Override
	public void save(BisProductDto bisProductDto) {
		bisProductDtoMapper.insert(bisProductDto);
	}

	@Override
	public void update(BisProductTradeTypePojoDto productDto) {
		
		BisProductDto bisProductDto=bisProductDtoMapper.findByProductCode(productDto.getProductCode());
		if(bisProductDto!=null){
			BisTradeTypeParamDto bisTradeTypeParamDto=bisTradeTypeParamDtoMapper.findByTradeTypeCode(productDto.getTradeType());
			if(bisTradeTypeParamDto==null){
				throw new CbsUncheckedException("","交易类型【"+productDto.getTradeType()+"】不存在");
			}
			String accountDate;
			String accountDate_1;
			try {
				accountDate = actaccountDateService.getAccountDate();
				accountDate_1 = DateUtils.getTime_1(accountDate);
			} catch (ParseException e) {
				_log.error("修改产品结算日期失败：",e);
				throw new CbsUncheckedException("","改产品结算日期失败:获取账务日期异常");
			}
			BisTradeTypeSettCycleDto bisTradeTypeSettCycleDto = bisTradeTypeSettCycleDtoMapper.selectByPrimaryKey(productDto.getTradeTypeSettCycleId());
			if(bisTradeTypeSettCycleDto==null){
				bisTradeTypeSettCycleDto = new BisTradeTypeSettCycleDto();
				bisTradeTypeSettCycleDto.setId(bisTradeTypeSettCycleDto.getIdentity());
				bisTradeTypeSettCycleDto.setCreateDate(new Date());
				bisTradeTypeSettCycleDto.setCreaterReviserName(productDto.getCreaterReviserName());
				
				
				bisTradeTypeSettCycleDto.setLastSettleDay(accountDate);
				bisTradeTypeSettCycleDto.setFeeLastSettleDay(accountDate);
				copyTradeTypeSettCycle(bisTradeTypeSettCycleDto, productDto, bisTradeTypeParamDto, bisProductDto);
				bisTradeTypeSettCycleDtoMapper.insert(bisTradeTypeSettCycleDto);
			}else{
				//生成结算记录
				bisTradeTypeSettCycleDto.setReviserName(productDto.getReviserName());
				bisTradeTypeSettCycleDto.setModifyDate(new Date());
				
				bisTradeTypeSettCycleDto.setLastSettleDay(accountDate_1);
				bisTradeTypeSettCycleDto.setFeeLastSettleDay(accountDate_1);
				if(StringUtils.isEmpty(bisTradeTypeSettCycleDto.getFirstSettleDay())){
					bisTradeTypeSettCycleDto.setFirstSettleDay(accountDate_1);
				}
				if(StringUtils.isEmpty(bisTradeTypeSettCycleDto.getFeeFirstSettleDay())){
					bisTradeTypeSettCycleDto.setFeeFirstSettleDay(accountDate_1);
				}
				copyTradeTypeSettCycle(bisTradeTypeSettCycleDto, productDto, bisTradeTypeParamDto, bisProductDto);
				bisTradeTypeSettCycleDtoMapper.updateByPrimaryKey(bisTradeTypeSettCycleDto);
			}
			bisProductDto.setModifyDate(new Date());
			bisProductDto.setReviserName(productDto.getReviserName());
			bisProductDtoMapper.updateByPrimaryKey(bisProductDto);
		}else{
			throw new CbsUncheckedException("","产品【"+productDto.getProductCode()+"】不存在");
		}
	}
	
	public void copyTradeTypeSettCycle(BisTradeTypeSettCycleDto bisTradeTypeSettCycleDto,BisProductTradeTypePojoDto productDto
			,BisTradeTypeParamDto bisTradeTypeParamDto,BisProductDto bisProductDto){
		bisTradeTypeSettCycleDto.setSettleDay(productDto.getSettleDay());
		bisTradeTypeSettCycleDto.setFeeSettleDay(productDto.getFeeSettleDay());
		
		bisTradeTypeSettCycleDto.setBusinessCode(bisProductDto.getBusinessCode());
		bisTradeTypeSettCycleDto.setProductCode(bisProductDto.getProductCode());
		bisTradeTypeSettCycleDto.setTradeType(productDto.getTradeType());
		bisTradeTypeSettCycleDto.setTradeTypeName(bisTradeTypeParamDto.getTradeTypeName());
		bisTradeTypeSettCycleDto.setOffsetStyle(productDto.getOffsetStyle());
		bisTradeTypeSettCycleDto.setAssuranceStyle(productDto.getAssuranceStyle());
		bisTradeTypeSettCycleDto.setSettleCycle(productDto.getSettleCycle());
		bisTradeTypeSettCycleDto.setSettleCycleType(productDto.getSettleCycleType());
		bisTradeTypeSettCycleDto.setBookType(productDto.getBookType());
		bisTradeTypeSettCycleDto.setTnSettle(productDto.getTnSettle());
		bisTradeTypeSettCycleDto.setSettleDayType(productDto.getSettleDayType());
		bisTradeTypeSettCycleDto.setSettleHour(productDto.getSettleHour());
		bisTradeTypeSettCycleDto.setSettleMinute(productDto.getSettleMinute());
		//如果按月或者按年，结算日为指定日期，更新结算天
		if((EBisBusinessSettleCycleType.CYCLE_MONTH.getValue().equals(productDto.getSettleCycleType()) || 
				EBisBusinessSettleCycleType.CYCLE_YEAR.getValue().equals(productDto.getSettleCycleType()))	&&
				ESettleDayType.SPECIFY_DAY.getValue().equals(productDto.getSettleDayType())){
			bisTradeTypeSettCycleDto.setDayOfMonth(Short.valueOf(productDto.getSettleDay().substring(6)));
		}
		bisTradeTypeSettCycleDto.setFeeMode(productDto.getFeeMode());
		bisTradeTypeSettCycleDto.setPayerFeeType(productDto.getPayerFeeType());
		bisTradeTypeSettCycleDto.setPayeeFeeType(productDto.getPayeeFeeType());
		bisTradeTypeSettCycleDto.setPayerFeeRate(productDto.getPayerFeeRate());
		bisTradeTypeSettCycleDto.setPayeeFeeRate(productDto.getPayeeFeeRate());
		bisTradeTypeSettCycleDto.setPayerFeeLow(productDto.getPayerFeeLow());
		bisTradeTypeSettCycleDto.setPayeeFeeLow(productDto.getPayeeFeeLow());
		bisTradeTypeSettCycleDto.setPayerFeeTop(productDto.getPayerFeeTop());
		bisTradeTypeSettCycleDto.setPayeeFeeTop(productDto.getPayeeFeeTop());
		bisTradeTypeSettCycleDto.setFeeTnSettle(productDto.getFeeTnSettle());
		bisTradeTypeSettCycleDto.setFeeSettleCycle(productDto.getFeeSettleCycle());
		bisTradeTypeSettCycleDto.setFeeSettleCycleType(productDto.getFeeSettleCycleType());
		bisTradeTypeSettCycleDto.setFeeSettleDayType(productDto.getFeeSettleDayType());
		bisTradeTypeSettCycleDto.setFeeSettleHour(productDto.getFeeSettleHour());
		bisTradeTypeSettCycleDto.setFeeSettleMinute(productDto.getFeeSettleMinute());
		//如果按月或者按年，结算日为指定日期，更新结算天
		if((EBisBusinessSettleCycleType.CYCLE_MONTH.getValue().equals(productDto.getFeeSettleCycleType()) || 
				EBisBusinessSettleCycleType.CYCLE_YEAR.getValue().equals(productDto.getFeeSettleCycleType()))	&&
				ESettleDayType.SPECIFY_DAY.getValue().equals(productDto.getFeeSettleDayType())){
			bisTradeTypeSettCycleDto.setFeeDayOfMonth(Short.valueOf(productDto.getFeeSettleDay().substring(6)));
		}
	}

	@Override
	public BisProductDto findByProductCode(String productCode) {
		return bisProductDtoMapper.findByProductCode(productCode);
	}
}
