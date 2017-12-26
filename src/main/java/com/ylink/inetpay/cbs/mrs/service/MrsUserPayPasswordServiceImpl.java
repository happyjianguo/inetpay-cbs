package com.ylink.inetpay.cbs.mrs.service;

import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.ylinkpay.framework.core.util.MD5Utils;

import com.shie.constant.SHIEConfigConstant;
import com.ylink.inetpay.cbs.common.CbsConstants;
import com.ylink.inetpay.cbs.mrs.dao.MrsUserPayPasswordDtoMapper;
import com.ylink.inetpay.cbs.mrs.dao.MrsUserPayPasswordHisDtoMapper;
import com.ylink.inetpay.common.project.cbs.constant.mrs.CreateType;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsUserPayPasswordDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsUserPayPasswordHisDto;
import com.ylink.inetpay.common.project.portal.enums.ErrorMsgEnum;
import com.ylink.inetpay.common.project.portal.vo.RespCheckVO;
import com.ylink.inetpay.common.project.portal.vo.safe.EditPayPwdVo;

@Service("mrsUserPayPasswordService")
public class MrsUserPayPasswordServiceImpl implements MrsUserPayPasswordService{
	
	@Autowired
	private MrsUserPayPasswordDtoMapper mrsUserPayPasswordDtoMapper;
	
	@Autowired
	private MrsUserPayPasswordHisDtoMapper mrsUserPayPasswordHisDtoMapper;

	@Override
	public MrsUserPayPasswordDto findByCustId(String custId) {
		return mrsUserPayPasswordDtoMapper.selectByCustId(custId);
	}

	@Override
	@Transactional(value = CbsConstants.TX_MANAGER_MRS)
	public RespCheckVO startPwd(String Ip,String name,MrsUserPayPasswordDto record) {
		try {
			record.setCreateTime(new Date());
			record.setId(UUID.randomUUID().toString());
			mrsUserPayPasswordDtoMapper.insert(record);
			
			//支付密码变更表
			MrsUserPayPasswordHisDto userPayPasswordHisDto  = new MrsUserPayPasswordHisDto();
			userPayPasswordHisDto.setId(UUID.randomUUID().toString());
			userPayPasswordHisDto.setCustId(record.getCustId());
			userPayPasswordHisDto.setCreateType(CreateType.STRAT.getValue());
			userPayPasswordHisDto.setCreateOperator(name);
			userPayPasswordHisDto.setCreateIp(Ip);
			userPayPasswordHisDto.setCreateTime(new Date());
			userPayPasswordHisDto.setPwdId(record.getId());
			userPayPasswordHisDto.setNewPassword(record.getPassword());
			mrsUserPayPasswordHisDtoMapper.insert(userPayPasswordHisDto);
		} catch (Exception e) {
			return new RespCheckVO(false,ErrorMsgEnum.SYSTEM_ERROR);
		}
		return new RespCheckVO(true);
		
	}

	@Override
	@Transactional(value = CbsConstants.TX_MANAGER_MRS)
	public RespCheckVO editPayPwd(String name,String Ip, String custId, EditPayPwdVo vo) {
		
		try {
			//支付密码表
			MrsUserPayPasswordDto mrsUserPayPasswordDto = mrsUserPayPasswordDtoMapper.selectByCustId(custId);
			mrsUserPayPasswordDto.setPassword(MD5Utils.MD5(vo.getNewPayPwd() + SHIEConfigConstant.SALT));
			mrsUserPayPasswordDtoMapper.updateByPrimaryKeySelective(mrsUserPayPasswordDto);
			//支付密码变更表
			MrsUserPayPasswordHisDto userPayPasswordHisDto  = new MrsUserPayPasswordHisDto();
			userPayPasswordHisDto.setId(UUID.randomUUID().toString());
			userPayPasswordHisDto.setCustId(custId);
			userPayPasswordHisDto.setCreateType(CreateType.EDIT.getValue());
			userPayPasswordHisDto.setCreateOperator(name);
			userPayPasswordHisDto.setCreateIp(Ip);
			userPayPasswordHisDto.setCreateTime(new Date());
			userPayPasswordHisDto.setPwdId(mrsUserPayPasswordDto.getId());
			userPayPasswordHisDto.setOldPassword(MD5Utils.MD5(vo.getOldPayPwd() + SHIEConfigConstant.SALT));
			userPayPasswordHisDto.setNewPassword(MD5Utils.MD5(vo.getNewPayPwd() + SHIEConfigConstant.SALT));
			mrsUserPayPasswordHisDtoMapper.insert(userPayPasswordHisDto);
		} catch (Exception e) {
			return new RespCheckVO(false,ErrorMsgEnum.SYSTEM_ERROR);
		}
		return new RespCheckVO(true);
	}

	@Override
	@Transactional(value = CbsConstants.TX_MANAGER_MRS)
	public RespCheckVO resetPwd(String Ip, String name, MrsUserPayPasswordDto record) {
		try {
			short num = 0;
			String oldPwd = null;
			MrsUserPayPasswordDto dto = new MrsUserPayPasswordDto();
			dto = mrsUserPayPasswordDtoMapper.selectByCustId(record.getCustId());
			if (dto == null) {
				dto.setId(UUID.randomUUID().toString());
				dto.setCreateTime(new Date());
				dto.setCustId(record.getCustId());
				dto.setPassword(record.getPassword());
				dto.setErrorNum(num);
				dto.setLastErrTime(null);
				mrsUserPayPasswordDtoMapper.insertSelective(dto);
			}else {
				oldPwd = dto.getPassword();
				dto.setPassword(record.getPassword());
				dto.setErrorNum(num);
				dto.setLastErrTime(null);
				mrsUserPayPasswordDtoMapper.updateByPrimaryKeySelective(dto);
			}
			
			//支付密码变更表
			MrsUserPayPasswordHisDto userPayPasswordHisDto  = new MrsUserPayPasswordHisDto();
			userPayPasswordHisDto.setId(UUID.randomUUID().toString());
			userPayPasswordHisDto.setCustId(record.getCustId());
			userPayPasswordHisDto.setCreateType(CreateType.RERST.getValue());
			userPayPasswordHisDto.setCreateOperator(name);
			userPayPasswordHisDto.setCreateIp(Ip);
			userPayPasswordHisDto.setCreateTime(new Date());
			userPayPasswordHisDto.setPwdId(dto.getId());
			userPayPasswordHisDto.setOldPassword(oldPwd);
			userPayPasswordHisDto.setNewPassword(record.getPassword());
			mrsUserPayPasswordHisDtoMapper.insert(userPayPasswordHisDto);
		} catch (Exception e) {
			return new RespCheckVO(false,ErrorMsgEnum.SYSTEM_ERROR);
		}
		return new RespCheckVO(true);
		
	}
	
}
