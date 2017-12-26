package com.ylink.inetpay.cbs.bis.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.ylinkpay.framework.core.model.PageData;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.ylink.inetpay.cbs.bis.dao.BisMovingActMatchingAuditDtoMapper;
import com.ylink.inetpay.cbs.common.CbsConstants;
import com.ylink.inetpay.cbs.pay.dao.PayAccChangeNotifyDtoMapper;
import com.ylink.inetpay.common.core.constant.BISAuditStatus;
import com.ylink.inetpay.common.core.constant.EResultCode;
import com.ylink.inetpay.common.core.dto.ResultCodeDto;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisMovingActMatchingAuditDto;
import com.ylink.inetpay.common.project.cbs.exception.CbsUncheckedException;
import com.ylink.inetpay.common.project.pay.app.PayAccChangeNotifyAppService;
import com.ylink.inetpay.common.project.pay.dto.PayAccChangeNotifyDto;
 
@Service("bisMovingActMatchingAuditService")
@Transactional(value=CbsConstants.TX_MANAGER_BIS)
public class BisMovingActMatchingAuditServicempl implements BisMovingActMatchingAuditService {
	 
	 @Autowired
	 private BisMovingActMatchingAuditDtoMapper bisMovingActMatchingAuditDtoMapper;
	 @Autowired
	 private PayAccChangeNotifyDtoMapper payAccChangeNotifyDtoMapper;
	 @Autowired
	 private PayAccChangeNotifyAppService payAccChangeNotifyAppService;
	 private static Logger _log = LoggerFactory.getLogger(BisMovingActMatchingAuditServicempl.class);

	@Override
	public PageData<BisMovingActMatchingAuditDto> queryAll(PageData<BisMovingActMatchingAuditDto> pageData,BisMovingActMatchingAuditDto queryParam) {
		PageHelper.startPage(pageData.getPageNumber(), pageData.getPageSize());
		List<BisMovingActMatchingAuditDto> items = bisMovingActMatchingAuditDtoMapper.queryAll(queryParam);
		Page<BisMovingActMatchingAuditDto> page = (Page<BisMovingActMatchingAuditDto> )items;
		pageData.setRows(items);
		pageData.setTotal(page.getTotal());
		return pageData;
	}

	@Override
	public BisMovingActMatchingAuditDto detail(String id) {
		BisMovingActMatchingAuditDto  bisMovingActMatchingAuditDto = bisMovingActMatchingAuditDtoMapper.selectByPrimaryKey(id);
		return bisMovingActMatchingAuditDto;
	}

	@Override
	public void update(BisMovingActMatchingAuditDto queryParam) {
		bisMovingActMatchingAuditDtoMapper.updateByPrimaryKeySelective(queryParam);
	}

	@Override
	public List<BisMovingActMatchingAuditDto> findAllByBusIdAndStatus(String id) {
		 
		return bisMovingActMatchingAuditDtoMapper.findAllByBusIdAndStatus(id);
	}

	@Override
	public long movingAccountMatchingAudit(BisMovingActMatchingAuditDto dto) {
		if(BISAuditStatus.AUDIT_PASS==dto.getAuditStatus()){
			//未知状态，处理状态为成功的订单才可以动账匹配
			long num=payAccChangeNotifyDtoMapper.isMovingAccountMatching(dto.getAccChangeBusiId());
			String errorMsg="";
			if(num>0){
				try {
					//调用退保接口
					ResultCodeDto<PayAccChangeNotifyDto> resultCodeDto = payAccChangeNotifyAppService.manualMatchAccChange(dto.getOrderId(),dto.getAccChangeBusiId());
					if(resultCodeDto!=null && resultCodeDto.getResultCode()!=null){
						if(EResultCode.SUCCESS!=resultCodeDto.getResultCode()){
							_log.error("调用动账匹配接口失败：{}","返回结果为空",resultCodeDto.getMsgDetail());
							errorMsg="动帐匹配失败："+resultCodeDto.getMsgDetail();
						}
					}else{
						_log.error("调用动账匹配接口失败：{}","返回结果为空");
						errorMsg="动帐匹配失败：调用支付接口返回不明确";
					}
				} catch (Exception e) {
					_log.error("调用动账匹配接口失败：{}",e);
					throw new CbsUncheckedException("","动帐匹配失败：调用支付接口失败");
				}
				if(!StringUtils.isBlank(errorMsg)){
					throw new CbsUncheckedException("",errorMsg);
				}
			}else{
				throw new CbsUncheckedException("","不能动账匹配");
			}
		}
		return bisMovingActMatchingAuditDtoMapper.updateByPrimaryKeySelective(dto);
	}

	@Override
	public void save(BisMovingActMatchingAuditDto dto) {
		dto.setId(dto.getIdentity());
		bisMovingActMatchingAuditDtoMapper.insert(dto);
		
	}

}

