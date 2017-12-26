package com.ylink.inetpay.cbs.bis.app;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.ylinkpay.framework.core.model.PageData;

import com.ylink.eu.util.tools.StringUtil;
import com.ylink.inetpay.cbs.act.service.ActaccountDateService;
import com.ylink.inetpay.cbs.bis.service.BisBusinessService;
import com.ylink.inetpay.cbs.bis.service.BisTradeTypeSettCycleService;
import com.ylink.inetpay.cbs.common.CbsConstants;
import com.ylink.inetpay.cbs.mrs.service.MrsAccountService;
import com.ylink.inetpay.cbs.ucs.sec.service.WorkCalendarService;
import com.ylink.inetpay.common.core.constant.EBisBusinessSettleCycleType;
import com.ylink.inetpay.common.core.constant.MrsBusiStatus;
import com.ylink.inetpay.common.core.util.DateUtils;
import com.ylink.inetpay.common.project.cbs.app.BisBusinessAppService;
import com.ylink.inetpay.common.project.cbs.constant.bis.ESettleDayType;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisBusinessDto;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisBusinessParamDto;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisTradeTypeSettCycleDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsAccountDto;
import com.ylink.inetpay.common.project.cbs.exception.CbsCheckedException;
import com.ylink.inetpay.common.project.cbs.exception.ECbsErrorCode;
import com.ylink.inetpay.common.project.cbs.vo.bis.BisBusinessVO;
import com.ylink.inetpay.common.project.clear.app.ClearCATAppService;
import com.ylink.inetpay.common.project.clear.exception.ClearCheckedException;

@Service("bisBusinessAppService")
public class BisBusinessAppServiceImpl implements BisBusinessAppService {
	
	@Autowired
	private BisBusinessService businessService;
	@Autowired
	private ActaccountDateService actaccountDateService;
	@Autowired
	private ClearCATAppService clearCATAppService;
	@Autowired
	private MrsAccountService mrsAccountService;
	@Autowired
	private WorkCalendarService workCalendarService;
	/*@Autowired
	private BisProductService bisProductService;*/
	@Autowired
	private BisTradeTypeSettCycleService BisTradeTypeSettCycleService;
	
	private static Logger log = LoggerFactory.getLogger(BisBusinessAppServiceImpl.class);
	
	@Override
	public void deleteById(String id) {
		businessService.deleteById(id);
	}

	@Override
	public PageData<BisBusinessDto> getBisBusiness(PageData<BisBusinessDto> pageData, BisBusinessVO businessVo) throws CbsCheckedException {
		return businessService.getBisBusiness(pageData, businessVo);
	}
	
	@Override
	public BisBusinessDto findById(String id) throws CbsCheckedException{
		return businessService.findById(id);
	}

	@Override
	public void addBusinessDto(BisBusinessDto dto) throws CbsCheckedException{
		//新增时候将首次结算日和上次结算日改成当前帐务日期(后期需要在第一次生成任务时候修改首次结算日和上次结算日，updateBusinessDto，如果传入空时结算时候需要取结算以前的)
		String accountDate = actaccountDateService.getAccountDate();
		dto.setLastSettleDay(accountDate);
		//dto.setFirstSettleDay(accountDate);
		dto.setId(UUID.randomUUID().toString());
		if(!StringUtils.isBlank(dto.getPayeeCode())){
			MrsAccountDto PayeeAccountDto=mrsAccountService.findByCustId(dto.getPayeeCode());
			if(PayeeAccountDto==null){
				throw new CbsCheckedException("","收款方一户通账户【"+dto.getPayeeCode()+"】不存在");
			}
			dto.setPayeeName(PayeeAccountDto.getAccountName());
		}
		if(!StringUtils.isBlank(dto.getPayerCode())){
			MrsAccountDto PayerAccountDto=mrsAccountService.findByCustId(dto.getPayerCode());
			if(PayerAccountDto==null){
				throw new CbsCheckedException("","付款方一户通账户【"+dto.getPayerCode()+"】不存在");
			}
			dto.setPayerName(PayerAccountDto.getAccountName());
		}
		//如果按月或者按年，结算日为指定日期，更新结算天
		if((EBisBusinessSettleCycleType.CYCLE_MONTH.getValue().equals(dto.getSettleCycleType()) || 
				EBisBusinessSettleCycleType.CYCLE_YEAR.getValue().equals(dto.getSettleCycleType()))	&&
				ESettleDayType.SPECIFY_DAY.getValue().equals(dto.getSettleDayType())){
			dto.setDayOfMonth(Short.valueOf(dto.getSettleDay().substring(6)));
		}
		isRealTime(dto);
		businessService.addBusinessDto(dto);
	}
	
	/**
	 * 如果是时时,结息日，结息类型都为空
	 */
	public void isRealTime(BisBusinessDto dto){
		if(EBisBusinessSettleCycleType.REAL_TIME.getValue().equals(dto.getSettleCycleType())){
			dto.setSettleDay(null);
			dto.setSettleDayType(null);
			dto.setSettleHour(null);
			dto.setSettleMinute(null);
			dto.setSettleCycle((short)0);
		}
	}

	
	@Override
	public void updateBusinessDto(BisBusinessDto dto) throws CbsCheckedException{
		businessService.updateBusinessDto(dto);
	}

	@Override
	public List<BisBusinessDto> find(String businessCode, String payeeCode, String status) {
		return businessService.find(businessCode, payeeCode, status);
	}

	@Override
	public void start(String id, String type) throws CbsCheckedException{
		log.info("启用/停用开始");
		BisBusinessDto dto = findById(id);
		String accountDate = actaccountDateService.getAccountDate();
		String accountDate_2 = null;
		String accountDate_1 = null;
		try {
			accountDate_2 = DateUtils.getTime_2(accountDate);
			accountDate_1 = DateUtils.getTime_1(accountDate);
		} catch (ParseException e) {
			throw new CbsCheckedException("获取账务日期+2天失败");
		}
		if("start".equals(type)) {
			List<BisTradeTypeSettCycleDto> tradeTypes=BisTradeTypeSettCycleService.findProductByBusinessCode(dto.getBusinessCode());
			if(tradeTypes!=null && !tradeTypes.isEmpty()){
				for (BisTradeTypeSettCycleDto tradeTypeDto : tradeTypes) {
					// 启用时如果结算日为空  则需要计算下一个结算日，并生成结息记录
					if(EBisBusinessSettleCycleType.REAL_TIME!=tradeTypeDto.getSettleCycleType()){
						if(StringUtil.isEmpty(tradeTypeDto.getSettleDay()) || accountDate.compareTo(tradeTypeDto.getSettleDay())>0) {
							String settleCycleType = tradeTypeDto.getSettleCycleType().getValue(); // 结算周期(日，月，年）
							int settleCycle = tradeTypeDto.getSettleCycle();
							String nextSettleDay = accountDate_1;
							if(ESettleDayType.SPECIFY_DAY.getValue()==tradeTypeDto.getSettleDayType().getValue()){
								//只有t+0的情况才可能为0
								if(settleCycle!=0){
									do{
										nextSettleDay = DateUtils.getNextSettleDay(nextSettleDay, settleCycle , settleCycleType);
									}while(DateUtils.compareDate(nextSettleDay, accountDate_2) < 0 );
								}
							}else{
								nextSettleDay=getWorkDay(settleCycleType, tradeTypeDto.getSettleDayType().getValue(), settleCycle, "update");
							}
							log.info("开始创建结算任务开始【"+new Date().getTime()+"】");
							try {
								clearCATAppService.createMerSett(dto, tradeTypeDto, tradeTypeDto.getLastSettleDay(), accountDate_1);
							} catch (ClearCheckedException e) {
								log.error("生成结算任务失败:{}",e);
								throw new CbsCheckedException(ECbsErrorCode.SYS_ERROR.getValue(), e.getMessage());
							}
							log.info("结束创建结算任务开始【"+new Date().getTime()+"】");
							tradeTypeDto.setSettleDay(nextSettleDay);
							log.info("修改产品下一结算日为【"+nextSettleDay+"】");
							tradeTypeDto.setLastSettleDay(accountDate_1);
							BisTradeTypeSettCycleService.updateSelective(tradeTypeDto);
						}
					}
				}
			}else{
				// 启用时如果结算日为空  则需要计算下一个结算日，并生成结息记录
				if(!EBisBusinessSettleCycleType.REAL_TIME.getValue().equals(dto.getSettleCycleType())){
					if(StringUtil.isEmpty(dto.getSettleDay()) || accountDate.compareTo(dto.getSettleDay())>0) {
						String settleCycleType = dto.getSettleCycleType(); // 结算周期(日，月，年）
						int settleCycle = dto.getSettleCycle();
						String nextSettleDay = accountDate_1;
						if(ESettleDayType.SPECIFY_DAY.getValue().equals(dto.getSettleDayType())){
							//只有t+0的情况才可能为0
							if(settleCycle!=0){
								do{
									nextSettleDay = DateUtils.getNextSettleDay(nextSettleDay, settleCycle , settleCycleType);
								}while(DateUtils.compareDate(nextSettleDay, accountDate_2) < 0 );
							}
						}else{
							nextSettleDay=getWorkDay(settleCycleType, dto.getSettleDayType(), settleCycle, "update");
						}
						log.info("开始创建结算任务开始【"+new Date().getTime()+"】");
						createMerSett(accountDate, dto,getBusinessDto(dto),accountDate_1);
						log.info("结束创建结算任务开始【"+new Date().getTime()+"】");
						dto.setSettleDay(nextSettleDay);
					}
				}
			}
			dto.setStatus(MrsBusiStatus.STATUS_0.getValue());
		} else if("end".equals(type)){
			// 停用
			dto.setStatus(MrsBusiStatus.STATUS_1.getValue());
		}
		isRealTime(dto);	
		updateBusinessDto(dto);
		log.info("启用/停用结束");
	}
	/*public static void main(String[] args) {
		String accountDate="20170724";
		String accountDate_2 = null;
		String accountDate_1 = null;
		try {
			accountDate_2 = DateUtils.getTime_2(accountDate);
			accountDate_1 = DateUtils.getTime_1(accountDate);
		} catch (ParseException e) {
		}
		String nextSettleDay=accountDate_1;
		do{
			nextSettleDay = DateUtils.getNextSettleDay(nextSettleDay, 2 , "1");
		}while(DateUtils.compareDate(nextSettleDay, accountDate_2) < 0 );
		System.out.println(nextSettleDay);
	}
	*/
	public String getWorkDay(String settleCycleType,String settleDayType,int settleCycle,String type) throws CbsCheckedException{
		EBisBusinessSettleCycleType settleCycleEnum = EBisBusinessSettleCycleType.getEnum(settleCycleType);
		if(settleCycleEnum==null){
			throw new CbsCheckedException("","系统不支持类型为【"+settleCycleType+"】的结算周期");
		}
		ESettleDayType settleDayTypeEnum = ESettleDayType.getEnum(settleDayType);
		if(settleDayTypeEnum==null){
			throw new CbsCheckedException("","系统不支持类型为【"+settleDayType+"】的结算日类型");
		}
		if(EBisBusinessSettleCycleType.REAL_TIME==settleCycleEnum){
			throw new CbsCheckedException("","结算周期为【"+settleCycleEnum.getDisplayName()+"】，无需设置");
		}else{
			if((EBisBusinessSettleCycleType.CYCLE_DAY==settleCycleEnum || EBisBusinessSettleCycleType.CYCLE_WEEK==settleCycleEnum) && ESettleDayType.SPECIFY_DAY!=settleDayTypeEnum){
				throw new CbsCheckedException("","结算周期为【日、周】，只能指定工作日");
			}
		}
		/*if(ESettleDayType.SPECIFY_DAY==settleDayTypeEnum){
			throw new CbsCheckedException("","结算日类型为【"+settleDayType+"】,无需调用");
		}*/
		String workDay;
		try {
			String accountDate = actaccountDateService.getAccountDate();
			if(StringUtils.isBlank(accountDate)){
				throw new CbsCheckedException("","指定结算日失败：账务日期为空");
			}
			String accountDate_2;
			if("save".equals(type)){
				accountDate_2 = DateUtils.getTime_1(accountDate);
			}else{
				accountDate_2 = DateUtils.getTime_2(accountDate);
			}
			if(EBisBusinessSettleCycleType.CYCLE_MONTH==settleCycleEnum){
				//int month=Integer.valueOf(accountDate.substring(4,6));
				if(ESettleDayType.FIRST_WORK_DAY==settleDayTypeEnum){
					workDay = workCalendarService.getMonthFirstWorkDay(0);
					if(accountDate_2.compareTo(workDay)>0){
						workDay = workCalendarService.getMonthFirstWorkDay(settleCycle);
					}
				}else if(ESettleDayType.SECOND_WORK_DAY==settleDayTypeEnum){
					workDay = workCalendarService.getMonthSecondWorkDay(0);
					if(accountDate_2.compareTo(workDay)>0){
						workDay = workCalendarService.getMonthSecondWorkDay(settleCycle);
					}
				}else if(ESettleDayType.SECOND_LAST_WORK_DAY==settleDayTypeEnum){
					workDay = workCalendarService.getMonthSecondLastWorkDay(0);
					if(accountDate_2.compareTo(workDay)>0){
						workDay = workCalendarService.getMonthSecondLastWorkDay(settleCycle);
					}
				}else{
					workDay = workCalendarService.getMonthLastWorkDay(0);
					if(accountDate_2.compareTo(workDay)>0){
						workDay = workCalendarService.getMonthLastWorkDay(1);
					}
				}
			}else{
				//int year=Integer.valueOf(accountDate.substring(0,4));
				if(ESettleDayType.FIRST_WORK_DAY==settleDayTypeEnum){
					workDay = workCalendarService.getYearFirstWorkDay(0);
					if(accountDate_2.compareTo(workDay)>0){
						workDay = workCalendarService.getYearFirstWorkDay(settleCycle);
					}
				}else if(ESettleDayType.SECOND_WORK_DAY==settleDayTypeEnum){
					workDay = workCalendarService.getYearSecondWorkDay(0);
					if(accountDate_2.compareTo(workDay)>0){
						workDay = workCalendarService.getYearSecondWorkDay(settleCycle);
					}
				}else if(ESettleDayType.SECOND_LAST_WORK_DAY==settleDayTypeEnum){
					workDay = workCalendarService.getYearSecondLastWorkDay(0);
					if(accountDate_2.compareTo(workDay)>0){
						workDay = workCalendarService.getYearSecondLastWorkDay(settleCycle);
					}
				}else{
					workDay = workCalendarService.getYearLastWorkDay(0);
					if(accountDate_2.compareTo(workDay)>0){
						workDay = workCalendarService.getYearLastWorkDay(settleCycle);
					}
				}
			}
			if(StringUtils.isBlank(workDay)){
				throw new CbsCheckedException("","获取指定工作日期失败，获取为空");
			}
		} catch (Exception e) {
			throw new CbsCheckedException("","获取指定工作日期失败，请稍后重试");
		}
		return workDay;
	}
	
	public BisBusinessDto getBusinessDto(BisBusinessDto sysDto){
		BisBusinessDto dto = new BisBusinessDto();
		if(MrsBusiStatus.STATUS_1.getValue().equals(sysDto.getStatus())){
			dto.setStatus(MrsBusiStatus.STATUS_0.getValue());
		}else{
			dto.setStatus(MrsBusiStatus.STATUS_1.getValue());
		}
		dto.setId(sysDto.getId());
		dto.setBusinessCode(sysDto.getBusinessCode());
		dto.setBusinessName(sysDto.getBusinessName());
		dto.setPayeeName(sysDto.getPayeeName());
		dto.setPayeeCode(sysDto.getPayeeCode());
		dto.setSettleCycle(sysDto.getSettleCycle());
		dto.setSettleCycleType(sysDto.getSettleCycleType());
		dto.setSettleType(sysDto.getSettleType());
		dto.setFirstSettleDay(sysDto.getFirstSettleDay());
		dto.setLastSettleDay(sysDto.getLastSettleDay());
		dto.setSettleDay(sysDto.getSettleDay());
		dto.setCreateDate(sysDto.getCreateDate());
		dto.setModifyDate(sysDto.getModifyDate());
		dto.setCreater(sysDto.getCreater());
		dto.setReviser(sysDto.getReviser());
		dto.setSettleMode(sysDto.getSettleMode());
		dto.setPayerCode(sysDto.getPayerCode());
		dto.setPayerName(sysDto.getPayerName());
		dto.setAccessorCode(sysDto.getAccessorCode());
		dto.setBookType(sysDto.getBookType());
		dto.setTnSettle(sysDto.getTnSettle());
		dto.setSettleDayType(sysDto.getSettleDayType());
		dto.setSettleHour(sysDto.getSettleHour());
		dto.setSettleMinute(sysDto.getSettleMinute());
		dto.setDayOfMonth(sysDto.getDayOfMonth());
		return dto;
	}
	
	/**
	 * 生成结算任务
	 * @param accountDate
	 * @param dto
	 * @throws CbsCheckedException
	 */
	public void createMerSett(String accountDate,BisBusinessDto oldBisBusinessDto,BisBusinessDto newBisBusinessDto,String accountDate_1) throws CbsCheckedException{
		try {
			clearCATAppService.createMerSett(oldBisBusinessDto, newBisBusinessDto, accountDate);
			// 更新上一结算日
			oldBisBusinessDto.setLastSettleDay(accountDate_1);
			//如果第一次结算，首次结算日和上次结算日都为当前账务日期
			if(StringUtils.isBlank(oldBisBusinessDto.getFirstSettleDay())){
				oldBisBusinessDto.setFirstSettleDay(accountDate_1);
			}
		} catch (ClearCheckedException e) {
			log.error("生成结算任务失败:{}",e);
			throw new CbsCheckedException(ECbsErrorCode.SYS_ERROR.getValue(), e.getMessage());
		}
	}

	@Override
	@Transactional(value=CbsConstants.TX_MANAGER_BIS)
	public void doUpdate(BisBusinessDto dto, String loginName) throws CbsCheckedException {
		BisBusinessDto sysDto = findById(dto.getId());
		if(!StringUtils.isBlank(dto.getPayeeCode())){
			MrsAccountDto PayeeAccountDto=mrsAccountService.findByCustId(dto.getPayeeCode());
			if(PayeeAccountDto==null){
				throw new CbsCheckedException("","收款方一户通账户【"+dto.getPayeeCode()+"】不存在");
			}
			sysDto.setPayeeName(PayeeAccountDto.getAccountName());
		}
		if(!StringUtils.isBlank(dto.getPayerCode())){
			MrsAccountDto PayerAccountDto=mrsAccountService.findByCustId(dto.getPayerCode());
			if(PayerAccountDto==null){
				throw new CbsCheckedException("","付款方一户通账户【"+dto.getPayerCode()+"】不存在");
			}
			sysDto.setPayerName(PayerAccountDto.getAccountName());
		}
		String accountDate = actaccountDateService.getAccountDate();
		String time_1;
		try {
			time_1 = DateUtils.getTime_1(accountDate);
		} catch (ParseException e) {
			throw new CbsCheckedException("","修改业务设置失败，账务日期转型失败");
		}
		List<BisTradeTypeSettCycleDto> tradeTypes=BisTradeTypeSettCycleService.findProductByBusinessCode(dto.getBusinessCode());
		if(tradeTypes==null || tradeTypes.size()==0){
			if(sysDto.getLastSettleDay().compareTo(time_1)<0 /*&& EBisBusinessSettleCycleType.REAL_TIME.getValue()!=sysDto.getSettleCycleType()*/){
				if(!(EBisBusinessSettleCycleType.REAL_TIME.getValue().equals(sysDto.getSettleCycleType())&& dto.getSettleCycleType().equals(sysDto.getSettleCycleType()))){
					createMerSett(accountDate, sysDto, dto,time_1);
				}
			}
		}else{
			log.info("如果业务存在产品，对业务的修改无效，不需要生成任务");
		}
		sysDto.setPayeeCode(dto.getPayeeCode());
		//sysDto.setPayeeName(dto.getPayeeName());
		sysDto.setBusinessName(dto.getBusinessName());
		sysDto.setSettleCycle(dto.getSettleCycle());
		sysDto.setSettleCycleType(dto.getSettleCycleType());
		sysDto.setSettleType(dto.getSettleType());
		sysDto.setSettleMode(dto.getSettleMode());
		sysDto.setSettleDay(dto.getSettleDay());
		sysDto.setModifyDate(new Date());
		sysDto.setReviser(loginName);
		sysDto.setSettleDay(dto.getSettleDay());
		sysDto.setPayerCode(dto.getPayerCode());
		//sysDto.setPayerName(dto.getPayerName());
		
		sysDto.setAccessorCode(dto.getAccessorCode());
		sysDto.setBookType(dto.getBookType());
		sysDto.setTnSettle(dto.getTnSettle());
		sysDto.setSettleDayType(dto.getSettleDayType());
		sysDto.setSettleHour(dto.getSettleHour());
		sysDto.setSettleMinute(dto.getSettleMinute());
		//如果按月或者按年，结算日为指定日期，更新结算天
		if((EBisBusinessSettleCycleType.CYCLE_MONTH.getValue().equals(dto.getSettleCycleType()) || 
				EBisBusinessSettleCycleType.CYCLE_YEAR.getValue().equals(dto.getSettleCycleType()))	&&
				ESettleDayType.SPECIFY_DAY.getValue().equals(dto.getSettleDayType())){
			sysDto.setDayOfMonth(Short.valueOf(dto.getSettleDay().substring(6)));
		}
		isRealTime(sysDto);
		updateBusinessDto(sysDto);
	}

	@Override
	public List<BisBusinessParamDto> listBusiParamsByBusiCode(String busiCode) throws CbsCheckedException {
		return businessService.listBusiParamsByBusiCode(busiCode);
	}

	@Override
	public List<BisBusinessDto> findBusinessByaccessorCode(String id) {
		return businessService.findBusinessByaccessorCode(id);
	}

	@Override
	public List<BisBusinessParamDto> findBusinessParamsByBusinessCode(String businessCode) {
		return businessService.findBusinessParamsByBusinessCode(businessCode);
	}

	@Override
	public long batchUpdateParam(ArrayList<BisBusinessParamDto> params) {
		return businessService.batchUpdateParam(params);
	}

	@Override
	public List<BisBusinessDto> list() {
		return businessService.list();
	}

	@Override
	public List<BisBusinessDto> findAllBusiness() {
		return businessService.findAllBusiness();
	}
}
