package com.ylink.inetpay.cbs.bis.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.ylinkpay.framework.core.model.PageData;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.ylink.eu.util.tools.DateUtil;
import com.ylink.inetpay.cbs.act.service.ActAccountService;
import com.ylink.inetpay.cbs.bis.dao.BisAuditDtoMapper;
import com.ylink.inetpay.cbs.bis.dao.BisBillHandleDtoMapper;
import com.ylink.inetpay.cbs.bis.dao.BisTransferHandleDao;
import com.ylink.inetpay.cbs.common.CbsConstants;
import com.ylink.inetpay.cbs.ucs.sec.service.UcsSecUserServiceImpl;
import com.ylink.inetpay.common.core.constant.BISAuditStatus;
import com.ylink.inetpay.common.core.constant.EAllocateType;
import com.ylink.inetpay.common.core.constant.EOrderStatus;
import com.ylink.inetpay.common.core.constant.EPayAdjustType;
import com.ylink.inetpay.common.core.constant.EResultCode;
import com.ylink.inetpay.common.core.constant.HandleCheckStatus;
import com.ylink.inetpay.common.core.dto.ResultCodeDto;
import com.ylink.inetpay.common.project.account.dto.ActAccountDto;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisAuditDto;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisBillHandleDto;
import com.ylink.inetpay.common.project.pay.app.PayAccountAdjustAppService;
import com.ylink.inetpay.common.project.pay.dto.PayAccountAdjustDto;

@Service("bisBillHandleService")
public class BisBillHandleServiceImpl implements BisBillHandleService {
	@Autowired
	BisBillHandleDtoMapper bisBillHandleDtoMapper;
	private static Logger _log = LoggerFactory.getLogger(UcsSecUserServiceImpl.class);
	@Autowired
	private BisAuditDtoMapper bisAuditDtoMapper;
	@Autowired
	private BisTransferHandleDao bisTransferHandleDao;
	@Autowired
	private PayAccountAdjustAppService payAccountAdjustAppService;
	@Autowired
	private ActAccountService actAccountService ;

	@Override
	public PageData<BisBillHandleDto> getByCond(BisBillHandleDto bisBillHandleDto,
			PageData<BisBillHandleDto> pageData) {
		PageHelper.startPage(pageData.getPageNumber(), pageData.getPageSize());
		List<BisBillHandleDto> list = bisBillHandleDtoMapper.getByCond(bisBillHandleDto);
		//获取借方、贷方名称
		setDrCrSubjectName(list);
		Page<BisBillHandleDto> page = (Page<BisBillHandleDto>) list;
		pageData.setTotal(page.getTotal());
		pageData.setRows(list);
		return pageData;
	}

	@Override
	public void insert(BisBillHandleDto bisBillHandleDto) {
		bisBillHandleDto.setTradeId(DateUtil.getYearMonthDay$(new Date()) + EAllocateType.CASH.getValue()
				+ bisTransferHandleDao.getSeqenceVals());
		try{
			bisBillHandleDtoMapper.insert(bisBillHandleDto);
		}catch(Exception e){
			_log.error("数据异常！");
		}
		
	}

	@Override
	public void update(BisBillHandleDto bisBillHandleDto) {
		bisBillHandleDtoMapper.update(bisBillHandleDto);

	}

	@Override
	public BisBillHandleDto details(String id) {
		BisBillHandleDto billHandleDto = bisBillHandleDtoMapper.details(id);
		ArrayList<BisBillHandleDto> billHandleDtoList = new ArrayList<BisBillHandleDto>();
		billHandleDtoList.add(billHandleDto);
		setDrCrSubjectName(billHandleDtoList);
		return billHandleDto;
	}
	
	/**
	 * 获取借方科目名称和贷方科目名称
	 * @param BisBillHandleDtos
	 */
	public void setDrCrSubjectName(List<BisBillHandleDto> BisBillHandleDtos){
		if(BisBillHandleDtos==null || BisBillHandleDtos.isEmpty()){
			return ;
		}
		//获取借方，贷方科目名称
		ArrayList<String> accountIds = new ArrayList<String>();
		for (BisBillHandleDto dto : BisBillHandleDtos) {
			accountIds.add(dto.getDrAccountId());
			accountIds.add(dto.getCrAccountId());
		}
		List<ActAccountDto> accountDtos=actAccountService.findListByAccountIds(accountIds);
		HashMap<String, ActAccountDto> accountDtoMap = new HashMap<String, ActAccountDto>();
		for (ActAccountDto actAccountDto : accountDtos) {
			accountDtoMap.put(actAccountDto.getAccountId(), actAccountDto);
		}
		for (BisBillHandleDto dto : BisBillHandleDtos) {
			ActAccountDto crActAccountDto = accountDtoMap.get(dto.getCrAccountId());
			if(crActAccountDto!=null){
				dto.setCrSubjectName(crActAccountDto.getSubjectNo2Name());
				dto.setCrName(crActAccountDto.getCustName());
			}
			ActAccountDto drActAccountDto = accountDtoMap.get(dto.getDrAccountId());
			if(drActAccountDto!=null){
				dto.setDrSubjectName(drActAccountDto.getSubjectNo2Name());
				dto.setDrName(drActAccountDto.getCustName());
			}
		}
	}

	@Transactional(value = CbsConstants.TX_MANAGER_BIS)
	@Override
	public boolean auditBill(BisBillHandleDto bisBillHandleDto, BisAuditDto bisAuditDto) {
		_log.info("开始审核手工对账----------------------------！");
		if (bisBillHandleDto == null || bisAuditDto == null) {
			_log.error("审核失败，传参为空！bisTransferHandleDto{}，bisAuditDto{}", bisBillHandleDto, bisAuditDto);
			return false;
		}
		try {
			// 锁定单条数据
			BisBillHandleDto bisBillHandleDtox = bisBillHandleDtoMapper.details(bisBillHandleDto.getId());
			// 审核成功
			if ((HandleCheckStatus.PASS.getValue()).equals(bisBillHandleDto.getCheckStatus().getValue())
					&& (BISAuditStatus.AUDIT_PASS.getValue()).equals(bisAuditDto.getAuditStatus().getValue())) {
				// 修改经办数据
				bisBillHandleDtoMapper.update(bisBillHandleDto);
				// 插入审核表数据
				bisAuditDtoMapper.insert(bisAuditDto);
				// 支付接口（待定）
				_log.info("手工对账入参bisBillHandleDtox{}",bisBillHandleDtox);
				ResultCodeDto<PayAccountAdjustDto> result = payAccountAdjustAppService.manualAccountAmount(
						bisBillHandleDtox.getDrAccountId(), bisBillHandleDtox.getCrAccountId(),
						bisBillHandleDtox.getAmount(), bisBillHandleDtox.getTradeId(),bisBillHandleDtox.getRemark());
				_log.info("手工对账返回值result{}",result);
				if (EResultCode.SUCCESS.getValue().equals(result.getResultCode().getValue())) {
					_log.info("手工对账成功！");
					bisBillHandleDto.setPayStatus(EOrderStatus.ORDER_STATUS_SUCCESS);
					bisBillHandleDto.setPayMemo("成功");
				} else if (EResultCode.FAIL.getValue().equals(result.getResultCode().getValue())) {
					_log.info("手工对账失败！");
					bisBillHandleDto.setPayStatus(EOrderStatus.ORDER_STATUS_FAIL);
					bisBillHandleDto.setPayMemo(result.getMsgDetail());
				} else {
					_log.info("手工对账处理中！");
					bisBillHandleDto.setPayStatus(EOrderStatus.ORDER_STATUS_WORKING);
					bisBillHandleDto.setPayMemo("处理中");
				}
				bisBillHandleDtoMapper.updatePayStatus(bisBillHandleDto);
				return true;
			}
			// 审核拒绝
			if ((HandleCheckStatus.REFUSE.getValue()).equals(bisBillHandleDto.getCheckStatus().getValue())
					&& (BISAuditStatus.AUDIT_REJECT.getValue()).equals(bisAuditDto.getAuditStatus().getValue())) {
				// 修改经办数据
				bisBillHandleDtoMapper.update(bisBillHandleDto);
				// 插入审核表数据
				bisAuditDtoMapper.insert(bisAuditDto);
				return true;
			}
		} catch (Exception e) {
			throw new RuntimeException("数据库异常,回滚数据",e);
		}

		return false;
	}

	@Override
	public BisBillHandleDto findByOrderAndPayAdjustType(String tradeId, String id) {
		BisBillHandleDto bisBillHandleDto = new BisBillHandleDto();
		ResultCodeDto<PayAccountAdjustDto> findByOrderAndPayAdjustType = payAccountAdjustAppService.findByOrderAndPayAdjustType(tradeId,EPayAdjustType.MANUAL);
		if (findByOrderAndPayAdjustType == null) {
			_log.error("查无此订单！");
			return null;
		}
		EResultCode resultCode = findByOrderAndPayAdjustType.getResultCode();
		if (EResultCode.SUCCESS.getValue().equals(resultCode)) {
			bisBillHandleDto.setPayStatus(EOrderStatus.ORDER_STATUS_SUCCESS);
		} else if (EResultCode.FAIL.getValue()
				.equals(resultCode)) {
			bisBillHandleDto.setPayStatus(EOrderStatus.ORDER_STATUS_FAIL);
		} else {
			bisBillHandleDto.setPayStatus(EOrderStatus.ORDER_STATUS_WORKING);
		}
		bisBillHandleDto.setId(id);
		bisBillHandleDtoMapper.updatePayStatus(bisBillHandleDto);
		return bisBillHandleDto;
	}

}
