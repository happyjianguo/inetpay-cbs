package com.ylink.inetpay.cbs.mrs.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.ylinkpay.framework.core.model.PageData;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.ylink.inetpay.cbs.common.CbsConstants;
import com.ylink.inetpay.cbs.mrs.dao.MrsConfAcctDtoMapper;
import com.ylink.inetpay.cbs.mrs.dao.MrsConfSubAcctDtoMapper;
import com.ylink.inetpay.common.core.constant.MrsConfSubRelationType;
import com.ylink.inetpay.common.core.constant.MrsCustomerType;
import com.ylink.inetpay.common.core.constant.MrsPlatformCode;
import com.ylink.inetpay.common.core.util.StringUtils;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsConfAcctDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsConfSubAcctDto;
import com.ylink.inetpay.common.project.cbs.exception.CbsUncheckedException;
import com.ylink.inetpay.common.project.cbs.exception.ECbsErrorCode;
/**
 * 
 * @author pst10
 * 类名称：MrsConfSubAcctServiceImpl
 * 类描述：子账户配置查询服务类
 * 创建时间：2017年2月14日 下午5:40:57
 */
@Service("mrsConfSubAcctService")
public class MrsConfSubAcctServiceImpl implements MrsConfSubAcctService {
	
	private static Logger _log = LoggerFactory.getLogger(MrsConfSubAcctServiceImpl.class);
	@Autowired
	private MrsConfSubAcctDtoMapper mrsConfSubAcctDtoMapper;
	@Autowired
	private MrsConfAcctDtoMapper mrsConfAcctDtoMapper;

	@Override
	public List<MrsConfSubAcctDto> findByUserTypeAndRationType(MrsCustomerType custType,
			MrsConfSubRelationType relationType,String platformCode) {
		List<MrsConfSubAcctDto> subAccts;
		String paltfrom = null;
		if(!StringUtils.isBlank(platformCode)){
			paltfrom = platformCode;
		}else {
			paltfrom = MrsPlatformCode.ACCOUNT.getValue();
		}
		
		if(MrsCustomerType.MCT_0.equals(custType)){
			subAccts = mrsConfSubAcctDtoMapper.findByPersonType(relationType.getValue(),paltfrom);
		}else if (MrsCustomerType.MCT_1.equals(custType)) {
			subAccts = mrsConfSubAcctDtoMapper.findByOrganType(relationType.getValue(),paltfrom);
		}else if(MrsCustomerType.MCT_2.equals(custType)){
			subAccts = mrsConfSubAcctDtoMapper.findByProductType(relationType.getValue(),paltfrom);
		}else {
			subAccts = new ArrayList<MrsConfSubAcctDto>();
		}
		return subAccts;
	}
	@Override
	public List<MrsConfSubAcctDto> findByAccountName(String accountName) {
		return mrsConfSubAcctDtoMapper.findByAccountName(accountName);
	}
	@Override
	public PageData<MrsConfSubAcctDto> findPage(PageData<MrsConfSubAcctDto> pageData, MrsConfSubAcctDto searchDto) {
		PageHelper.startPage(pageData.getPageNumber(), pageData.getPageSize());
		List<MrsConfSubAcctDto> list = mrsConfSubAcctDtoMapper.list(searchDto);
		Page<MrsConfSubAcctDto> page = (Page<MrsConfSubAcctDto>) list;
		pageData.setTotal(page.getTotal());
		pageData.setRows(list);
		return pageData;
	}

	@Override
	public MrsConfSubAcctDto selectById(String id) {
		return mrsConfSubAcctDtoMapper.selectByPrimaryKey(id);
	}

	@Override
	@Transactional(value = CbsConstants.TX_MANAGER_MRS)
	public void addOrUpdateMrsConfSubAcct(MrsConfSubAcctDto confSubAcct) throws CbsUncheckedException{
		try {
			if(confSubAcct == null){
				_log.info("新增或修改子账户配置信息，调用核心失败！子账户配置信息为空！");
				throw new CbsUncheckedException(ECbsErrorCode.SYS_DATA_NULL);
			}
			boolean check = checkMrsConfSubAcct(confSubAcct.getSubAccountCode(), 
					confSubAcct.getPlatformCode(), confSubAcct.getAccountType(), 
					StringUtils.isBlank(confSubAcct.getId()) ? null : confSubAcct.getId() );
			if(!check){
				_log.info("新增或修改子账户配置信息，子账户渠道【"+confSubAcct.getPlatformCodeName()+"】，"
						+ "一户通类型【"+confSubAcct.getAccountTypeName()+"】，"
								+ "子账户编号【"+confSubAcct.getAccountTypeName()+"】，已经存在！不可重复添加。");
				throw new CbsUncheckedException(ECbsErrorCode.ADUIT_0087);
			}
			if(StringUtils.isBlank(confSubAcct.getId())){
				if(StringUtils.isBlank(confSubAcct.getSubAccountCode())){
					_log.info("新增子账户配置信息，调用核心失败！请选择子账户信息！");
					throw new CbsUncheckedException(ECbsErrorCode.SYS_DATA_NULL);
				}else {
					MrsConfAcctDto acct = mrsConfAcctDtoMapper.selectByPrimaryKey(confSubAcct.getSubAccountCode());
					if(acct == null){
						_log.info("新增子账户配置信息时，【"+confSubAcct.getId()+"】原数据不存在！");
						throw new CbsUncheckedException(ECbsErrorCode.ADUIT_0087);
					}
					//新增
					confSubAcct.setSubAccountName(acct.getName());
					confSubAcct.setCreateTime(new Date());
					confSubAcct.setId(UUID.randomUUID().toString());
					mrsConfSubAcctDtoMapper.insertSelective(confSubAcct);
				}
				
			}else {
				//修改
				MrsConfSubAcctDto dto = mrsConfSubAcctDtoMapper.selectByPrimaryKey(confSubAcct.getId());
				if(dto == null ){
					_log.info("修改子账户配置信息时，【"+confSubAcct.getId()+"】原数据不存在！");
					throw new CbsUncheckedException(ECbsErrorCode.ADUIT_0087);
				}
				dto.setAccountType(confSubAcct.getAccountType());
				dto.setOrganType(confSubAcct.getOrganType());
				dto.setPersonType(confSubAcct.getPersonType());
				dto.setProductType(confSubAcct.getProductType());
				dto.setPlatformCode(confSubAcct.getPlatformCode());
				dto.setRemark(confSubAcct.getRemark());
//				dto.setSubAccountCode(confSubAcct.getSubAccountCode());
//				if(StringUtils.isBlank(confSubAcct.getSubAccountCode())){
//					_log.info("修改子账户配置信息，调用核心失败！请选择子账户信息！");
//					throw new CbsUncheckedException(ECbsErrorCode.SYS_DATA_NULL);
//				}
//				MrsConfAcctDto acct = mrsConfAcctDtoMapper.selectByPrimaryKey(confSubAcct.getSubAccountCode());
//				if(acct == null){
//					_log.info("新增子账户配置信息时，【"+confSubAcct.getId()+"】原数据不存在！");
//					throw new CbsUncheckedException(ECbsErrorCode.ADUIT_0087);
//				}
//				dto.setSubAccountName(acct.getName());
				dto.setUpdateTime(new Date());
				
				mrsConfSubAcctDtoMapper.updateByPrimaryKeySelective(dto);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			_log.info("新增或者修改用户，调用核心失败！"+e.getMessage());
			throw new CbsUncheckedException(ECbsErrorCode.SYS_ERROR);
		}
		
		
	}

	@Override
	public boolean checkMrsConfSubAcct(String subAccountCode, String platformCode, String accountType, String id) {
		try {
			
			MrsConfSubAcctDto dto = mrsConfSubAcctDtoMapper.checkMrsConfSubAcct(subAccountCode,platformCode,accountType,id);
			if(dto==null){
				return true;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			_log.info("校验子账户配置信息查询出错！"+e.getMessage());
		}
		
		return false;
	}
	@Override
	public List<MrsConfSubAcctDto> findBy4Elment(MrsCustomerType custType, MrsConfSubRelationType relationType,
			String platformCode, String accountType) {
		List<MrsConfSubAcctDto> subAccts;
		String paltfrom = null;
		if(!StringUtils.isBlank(platformCode)){
			paltfrom = platformCode;
		}else {
			paltfrom = MrsPlatformCode.ACCOUNT.getValue();
		}
		
		if(MrsCustomerType.MCT_0.equals(custType)){
			subAccts = mrsConfSubAcctDtoMapper.findByPersonType3Element(relationType.getValue(),paltfrom,accountType);
		}else if (MrsCustomerType.MCT_1.equals(custType)) {
			subAccts = mrsConfSubAcctDtoMapper.findByOrganType3Element(relationType.getValue(),paltfrom,accountType);
		}else if(MrsCustomerType.MCT_2.equals(custType)){
			subAccts = mrsConfSubAcctDtoMapper.findByProductType3Element(relationType.getValue(),paltfrom,accountType);
		}else {
			subAccts = new ArrayList<MrsConfSubAcctDto>();
		}
		return subAccts;
	}
	@Override
	public List<MrsConfSubAcctDto> findBy4Elments(MrsCustomerType custType, MrsConfSubRelationType relationType,
			String platformCode, String accountType) {
		List<MrsConfSubAcctDto> subAccts;
		String paltfrom = null;
		if(!StringUtils.isBlank(platformCode)){
			paltfrom = platformCode;
		}else {
			paltfrom = MrsPlatformCode.ACCOUNT.getValue();
		}
		
		if(MrsCustomerType.MCT_0.equals(custType)){
			subAccts = mrsConfSubAcctDtoMapper.findByPersonType3Elements(relationType.getValue(),paltfrom,accountType);
		}else if (MrsCustomerType.MCT_1.equals(custType)) {
			subAccts = mrsConfSubAcctDtoMapper.findByOrganType3Elements(relationType.getValue(),paltfrom,accountType);
		}else if(MrsCustomerType.MCT_2.equals(custType)){
			subAccts = mrsConfSubAcctDtoMapper.findByProductType3Elements(relationType.getValue(),paltfrom,accountType);
		}else {
			subAccts = new ArrayList<MrsConfSubAcctDto>();
		}
		return subAccts;
	}

	
}
