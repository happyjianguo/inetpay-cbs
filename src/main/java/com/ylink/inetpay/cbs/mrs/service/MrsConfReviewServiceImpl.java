package com.ylink.inetpay.cbs.mrs.service;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.ylink.inetpay.cbs.mrs.dao.MrsConfReviewDtoMapper;
import com.ylink.inetpay.common.core.constant.EConfReviewBusiType;
import com.ylink.inetpay.common.core.constant.EYesNo;
import com.ylink.inetpay.common.core.util.ExceptionProcUtil;
import com.ylink.inetpay.common.core.util.StringUtils;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsConfReviewDto;
import com.ylink.inetpay.common.project.cbs.exception.CbsUncheckedException;
import com.ylink.inetpay.common.project.cbs.exception.ECbsErrorCode;
/**
 * 
 * @author pst10
 * 类名称：MrsConfReviewServiceImpl
 * 类描述：机构复核业务操作服务
 * 创建时间：2017年5月10日 上午11:17:58
 */
@Service("mrsConfReviewService")
public class MrsConfReviewServiceImpl implements MrsConfReviewService{
	
	private static Logger _log = LoggerFactory.getLogger(MrsConfReviewServiceImpl.class);
	
	@Autowired
	private MrsConfReviewDtoMapper mrsConfReviewDtoMapper;
	@Override
	public PageData<MrsConfReviewDto> findPage(PageData<MrsConfReviewDto> pageData, MrsConfReviewDto searchDto) {
		PageHelper.startPage(pageData.getPageNumber(), pageData.getPageSize());
		List<MrsConfReviewDto> list = mrsConfReviewDtoMapper.list(searchDto);
		Page<MrsConfReviewDto> page = (Page<MrsConfReviewDto>) list;
		pageData.setTotal(page.getTotal());
		pageData.setRows(list);
		return pageData;
	}

	@Override
	public MrsConfReviewDto selectById(String id) {
		return mrsConfReviewDtoMapper.selectById(id);
	}

	@Override
	public void addOrUpdateConfReview(MrsConfReviewDto mrsConfReviewDto) {
		try {
			if(mrsConfReviewDto == null){
				_log.error("新增或者修改机构复核信息失败：{传入参数异常}");
				throw new CbsUncheckedException(ECbsErrorCode.SYS_DATA_NULL);
			}
			//根据一户通好查询机构是否已经配置数据
			boolean bn = checkByCustIdAndId(mrsConfReviewDto.getCustId(), mrsConfReviewDto.getId());
			if( !bn ){
				_log.error("设置机构复核业务数据时。该机构已经配置复核数据。不需要重复添加！");
				throw new CbsUncheckedException(ECbsErrorCode.SYS_REPEAT);
			}
			//新增
			if(StringUtils.isEmpty(mrsConfReviewDto.getId())){
				
				mrsConfReviewDto.setId(mrsConfReviewDto.getIdentity());
				mrsConfReviewDto.setIsGlobal(EYesNo.NO.getValue());
				mrsConfReviewDto.setCreateTime(new Date());
				
				mrsConfReviewDtoMapper.insertSelective(mrsConfReviewDto);
			}else {
				//修改
				MrsConfReviewDto oldDto = mrsConfReviewDtoMapper.selectByPrimaryKey(mrsConfReviewDto.getId());
				if(oldDto == null){
					_log.error("修改机构业务数据时：{}，原始配置数据不存在！",mrsConfReviewDto.getCustId());
					throw new CbsUncheckedException(ECbsErrorCode.ADUIT_0087);
				}
				//根据一户通号码查询
				/*if(oldDto.getCustId().equals(mrsConfReviewDto.getCustId())){
					_log.error("修改机构业务数据时,{}不能修改其他{}机构配置数据！",mrsConfReviewDto.getCustId(),oldDto.getCustId());
					throw new CbsUncheckedException(ECbsErrorCode.SYS_ERROR);
				}*/
				oldDto.setBathPayReview(mrsConfReviewDto.getBathPayReview());
				oldDto.setBindCardReview(mrsConfReviewDto.getBindCardReview());
				oldDto.setRechargeReview(mrsConfReviewDto.getRechargeReview());
				oldDto.setUnbindCardReview(mrsConfReviewDto.getUnbindCardReview());
				oldDto.setWithdrawReview(mrsConfReviewDto.getWithdrawReview());
				oldDto.setUpdateTime(new Date());
				oldDto.setRemark(mrsConfReviewDto.getRemark());
				mrsConfReviewDtoMapper.updateByPrimaryKeySelective(oldDto);
			}
			
		} catch (Exception e) {
			_log.error("新增或者修改机构复核信息失败：{}",ExceptionProcUtil.getExceptionDesc(e));
			throw new CbsUncheckedException(ECbsErrorCode.SYS_ERROR);
		}
	}

	@Override
	public MrsConfReviewDto selectByCustIdAndId(String custId, String id) {
		return mrsConfReviewDtoMapper.selectByCustIdAndId(custId,id);
	}

	@Override
	public MrsConfReviewDto selectByCustId(String custId) {
		MrsConfReviewDto confReview = null;
		//先根据一户通号码查询配置信息
		confReview = mrsConfReviewDtoMapper.selectByCustId(custId);
		//如果一户通号码没有设置，默认查询全局设置变量
		if(confReview == null){
			confReview = mrsConfReviewDtoMapper.selectByIsGlobal();
		}
		return confReview;
	}

	@Override
	public MrsConfReviewDto selectByIsGlobal() {
		return mrsConfReviewDtoMapper.selectByIsGlobal();
	}

	@Override
	public boolean checkByCustIdAndId(String custId, String id) {
		try {
			MrsConfReviewDto confReview = selectByCustIdAndId(custId , id);
			if(confReview == null ){
				return true;
			}
		} catch (Exception e) {
			_log.error("校验机构复核参数数据失败：{}",ExceptionProcUtil.getExceptionDesc(e));
		}
		return false;
	}

	@Override
	public boolean checkByCustIdAndType(String custId, EConfReviewBusiType busiType) {
		try {
			MrsConfReviewDto confReview = null;
			confReview = selectByCustId(custId);
			//如果没有设置默认查询全局设置 ,默认都需要复核
			if(confReview == null ){
				//查询设置的全局机构复核业务参数
				confReview = selectByIsGlobal();
				//如果没有设置全局机构复核业务参数，返回需要复核
				if( confReview == null ){
					_log.error("机构一户通没有设置复核业务参数！");
					return true;
				}
			}
			if( EConfReviewBusiType.BIND_CARD.equals(busiType) ){
				
				return EYesNo.YES.getValue().equals(confReview.getBindCardReview());
				
			}else if (EConfReviewBusiType.UNBIND_CARD.equals(busiType)) {
				
				return EYesNo.YES.getValue().equals(confReview.getUnbindCardReview());
				
			}else if (EConfReviewBusiType.RECHARGE.equals(busiType)) {
				
				return EYesNo.YES.getValue().equals(confReview.getRechargeReview());
				
			}else if (EConfReviewBusiType.WITHDRAW.equals(busiType)) {
				
				return EYesNo.YES.getValue().equals(confReview.getWithdrawReview());
				
			}else if (EConfReviewBusiType.BATHPAY.equals(busiType)) {
				
				return EYesNo.YES.getValue().equals(confReview.getBathPayReview());
				
			}else {
				//不知道的业务类型 暂时设置的需要复核
				return true;
			}
			
			
		} catch (Exception e) {
			_log.error("根据业务类型，一户通号码，校验机构复核参数数据失败：{}",ExceptionProcUtil.getExceptionDesc(e));
			return false;
		}
		
	}
	

}
