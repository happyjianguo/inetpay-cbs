package com.ylink.inetpay.cbs.mrs.service;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.ylinkpay.framework.core.model.PageData;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.ylink.inetpay.cbs.act.dao.ActSubjectDtoMapper;
import com.ylink.inetpay.cbs.common.CbsConstants;
import com.ylink.inetpay.cbs.mrs.dao.MrsConfAcctDtoMapper;
import com.ylink.inetpay.common.core.constant.EActBusiRefSubBusiType;
import com.ylink.inetpay.common.core.constant.EUcsSecOperationLogType;
import com.ylink.inetpay.common.project.account.dto.ActSubjectDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsConfAcctDto;
import com.ylink.inetpay.common.project.cbs.exception.CbsUncheckedException;
import com.ylink.inetpay.common.project.cbs.exception.ECbsErrorCode;
/**
 * 
 * @author pst10
 * 类名称：MrsConfAcctServiceImpl
 * 类描述：子账户操作服务
 * 创建时间：2017年3月31日 下午7:56:37
 */
@Service("mrsConfAcctService")
public class MrsConfAcctServiceImpl implements MrsConfAcctService{
	
	private static Logger _log = LoggerFactory.getLogger(MrsConfAcctServiceImpl.class);
	@Autowired
	private MrsConfAcctDtoMapper mrsConfAcctDtoMapper;
	@Autowired
	private ActSubjectDtoMapper actSubjectDtoMapper;
	@Override
	public PageData<MrsConfAcctDto> findPage(PageData<MrsConfAcctDto> pageData, MrsConfAcctDto searchDto) {
		PageHelper.startPage(pageData.getPageNumber(), pageData.getPageSize());
		List<MrsConfAcctDto> list = mrsConfAcctDtoMapper.list(searchDto);
		Page<MrsConfAcctDto> page = (Page<MrsConfAcctDto>) list;
		pageData.setTotal(page.getTotal());
		pageData.setRows(list);
		return pageData;
	}

	@Override
	public MrsConfAcctDto selectById(String id) {
		return mrsConfAcctDtoMapper.selectByPrimaryKey(id);
	}
	
	@Override
	@Transactional(value = CbsConstants.TX_MANAGER_MRS)
	public void addOrUpdateMrsConfAcct(MrsConfAcctDto mrsConfAcctDto) throws CbsUncheckedException{
		try {
			//校验信息是否为空
			if(mrsConfAcctDto == null){
				_log.info("新增或修改子账户信息，调用核心失败！子账户信息为空！");
				throw new CbsUncheckedException(ECbsErrorCode.SYS_DATA_NULL);
			}
			
			if(!EUcsSecOperationLogType.UPDATE.getValue().equals(mrsConfAcctDto.getAddOrUpdate())){
				//新增
				MrsConfAcctDto acct = mrsConfAcctDtoMapper.selectByPrimaryKey(mrsConfAcctDto.getId());
				if(acct != null){
					_log.info("新增或修改子账号信息，调用核心失败！子账号【"+mrsConfAcctDto.getId()+"】已经存在！");
					throw new CbsUncheckedException(ECbsErrorCode.CONF_ACCT_0110);
				}
				//校验名称唯一
				boolean checked = checkMrsConfAcct(mrsConfAcctDto.getName(), null);
				if(!checked){
					_log.info("新增或修改子账户信息，调用核心失败！子账户名称【"+mrsConfAcctDto.getName()+"】已经存在！");
					throw new CbsUncheckedException(ECbsErrorCode.CONF_ACCT_0109);
				}
				mrsConfAcctDto.setCreateTime(new Date());
				mrsConfAcctDtoMapper.insertSelective(mrsConfAcctDto);
				
			}else {
				//修改
				//校验名称唯一
				boolean checked = checkMrsConfAcct(mrsConfAcctDto.getName(), mrsConfAcctDto.getId());
				if(!checked){
					_log.info("新增或修改子账户信息，调用核心失败！子账户名称【"+mrsConfAcctDto.getName()+"】已经存在！");
					throw new CbsUncheckedException(ECbsErrorCode.CONF_ACCT_0109);
				}
				MrsConfAcctDto dto = mrsConfAcctDtoMapper.selectByPrimaryKey(mrsConfAcctDto.getId());
				if(dto == null ){
					_log.info("修改子账户信息时，【"+mrsConfAcctDto.getId()+"】原数据不存在！");
					throw new CbsUncheckedException(ECbsErrorCode.ADUIT_0087);
				}
				dto.setName(mrsConfAcctDto.getName());
				dto.setRemark(mrsConfAcctDto.getRemark());
				dto.setSubAcctType(mrsConfAcctDto.getSubAcctType());
				dto.setUpdateTime(new Date());
				mrsConfAcctDtoMapper.updateByPrimaryKeySelective(dto);
			}
		} catch (Exception e) {
			e.printStackTrace();
			_log.info("新增或者修改子账户信息，调用核心失败！"+e.getMessage());
			throw new CbsUncheckedException(ECbsErrorCode.SYS_ERROR);
		}
	}

	@Override
	public boolean checkMrsConfAcct(String name, String id) {
		try {
			if(StringUtils.isBlank(name)){
				_log.info("校验子账户信息是否唯一接口，传递子账户名称为NULL");
				return false;
			}
			MrsConfAcctDto mrsConfAcctDto = mrsConfAcctDtoMapper.selectByNameAndId(name, id);
			if(mrsConfAcctDto == null){
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public List<MrsConfAcctDto> findAll() {
		return mrsConfAcctDtoMapper.findAll();
	}

	@Override
	public List<ActSubjectDto> findActByConfAcctId(String id) {
		MrsConfAcctDto conf = selectById(id);
		if(conf == null){
			return null;
		}
		return actSubjectDtoMapper.findByAcctTypeNo(conf.getSubAcctType(),EActBusiRefSubBusiType.getTypeEnum());
	}

}
