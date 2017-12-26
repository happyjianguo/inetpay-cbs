package com.ylink.inetpay.cbs.mrs.service;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.ylinkpay.framework.core.model.PageData;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.ylink.inetpay.cbs.chl.service.ChlBankService;
import com.ylink.inetpay.cbs.common.CbsConstants;
import com.ylink.inetpay.cbs.mrs.dao.MrsPortalReviewAduitDtoMapper;
import com.ylink.inetpay.cbs.mrs.dao.MrsRechargeAduitDtoMapper;
import com.ylink.inetpay.common.core.util.ExceptionProcUtil;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsPortalReviewAduitDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsRechargeAduitDto;
import com.ylink.inetpay.common.project.cbs.exception.CbsUncheckedException;
import com.ylink.inetpay.common.project.channel.dto.TbChlBank;

@Service("mrsRechargeAuditService")
public class MrsRechargeAuditServiceImpl implements MrsRechargeAuditService{
	
	public final static Logger logger = LoggerFactory.getLogger(MrsRechargeAuditServiceImpl.class);
	@Autowired
	private MrsRechargeAduitDtoMapper mrsRechargeAduitDtoMapper;
	@Autowired
	private MrsPortalReviewAduitDtoMapper mrsPortalReviewAduitDtoMapper;
	@Autowired
	private MrsWithdrawAuditService mrsWithdrawAuditService;
	@Autowired
	ChlBankService chlBankService;
	@Override
	public PageData<MrsRechargeAduitDto> queryAllData(PageData<MrsRechargeAduitDto> pageDate,
			MrsRechargeAduitDto mrsRechargeAduitDto) {
		PageHelper.startPage(pageDate.getPageNumber(),pageDate.getPageSize());
		List<MrsRechargeAduitDto> list = mrsRechargeAduitDtoMapper.queryAllData(mrsRechargeAduitDto);
		List<TbChlBank> banks = chlBankService.getBanks();
		for (int i = 0; i < list.size(); i++) {
			String bankType = list.get(i).getBankType();
			for (int j = 0; j < banks.size(); j++) {
				if (bankType.equals(banks.get(j).getBankType())) {
					MrsRechargeAduitDto mrsRechargeAduit = list.get(i);
					mrsRechargeAduit.setBankNameZ(banks.get(j).getBankName());
					list.set(i,mrsRechargeAduit);
				}
				
			}
			
		}
		Page<MrsRechargeAduitDto> page = (Page<MrsRechargeAduitDto>) list;
		pageDate.setTotal(page.getTotal());
		pageDate.setRows(list);
		return pageDate;
	}
	@Transactional(value = CbsConstants.TX_MANAGER_MRS)
	@Override
	public void saveRechargeAudit(MrsRechargeAduitDto RechargeAduitDto,MrsPortalReviewAduitDto mrsPortalReviewAduitDto){
		try {
			//主键换成有规则的流水
			RechargeAduitDto.setId(mrsWithdrawAuditService.getMrsAuditOrderNosVal());
			mrsRechargeAduitDtoMapper.insertSelective(RechargeAduitDto);
			mrsPortalReviewAduitDto.setId(mrsPortalReviewAduitDto.getIdentity());
			mrsPortalReviewAduitDto.setCreateTime(new Date());
			mrsPortalReviewAduitDto.setBusiNo(RechargeAduitDto.getId());
			mrsPortalReviewAduitDtoMapper.insert(mrsPortalReviewAduitDto);
		} catch (Exception e) {
			ExceptionProcUtil.getExceptionDesc(e);
			logger.error("保存充值复核业务信息出错{}",e);
			throw new CbsUncheckedException("99", "保存充值复核业务信息出错");
		}
	}
	
	@Transactional(value = CbsConstants.TX_MANAGER_MRS)
	@Override
	public void updateRechargeAudit(MrsRechargeAduitDto RechargeAduitDto,MrsPortalReviewAduitDto mrsPortalReviewAduitDto){
		try {
			int updateFlag = mrsRechargeAduitDtoMapper.updateByAuditStatus(RechargeAduitDto);
			if (updateFlag != 1) {
				logger.error("更新充值复核信息出错{}");
				throw new CbsUncheckedException("99", "更新充值复核信息出错");
			}
			mrsPortalReviewAduitDto.setBusiNo(RechargeAduitDto.getId());
			mrsPortalReviewAduitDto.setAduitTime(new Date());
			mrsPortalReviewAduitDtoMapper.updateByRechargeId(mrsPortalReviewAduitDto);
		} catch (Exception e) {
			ExceptionProcUtil.getExceptionDesc(e);
			logger.error("更新充值复核业务信息出错{}",e);
			throw new CbsUncheckedException("99", "更新充值复核业务信息出错");
		}
	}
	
	@Override
	public MrsRechargeAduitDto selectByPrimaryKey(String id) {
		return mrsRechargeAduitDtoMapper.selectByPrimaryKey(id);
	}

	@Override
	public void deleteByPrimaryKey(String id) {
		mrsRechargeAduitDtoMapper.deleteByPrimaryKey(id);
	}

	@Override
	public int insertSelective(MrsRechargeAduitDto dto) {
		return mrsRechargeAduitDtoMapper.insertSelective(dto);
	}

	@Override
	public int updateByPrimaryKeySelective(MrsRechargeAduitDto mrsAduitInfoDto) {
		return mrsRechargeAduitDtoMapper.updateByPrimaryKeySelective(mrsAduitInfoDto);
	}

	
}
