package com.ylink.inetpay.cbs.mrs.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ylink.inetpay.cbs.mrs.dao.MrsAduitInfoDtoMapper;
import com.ylink.inetpay.cbs.mrs.dao.MrsAduitPersonDtoMapper;
import com.ylink.inetpay.cbs.mrs.dao.MrsConfAuditDtoMapper;
import com.ylink.inetpay.cbs.mrs.dao.MrsConfAuditItemDtoMapper;
import com.ylink.inetpay.common.core.constant.EAduitTypeEnum;
import com.ylink.inetpay.common.core.constant.EDistributTypeEnum;
import com.ylink.inetpay.common.core.constant.EOperaTypeEnum;
import com.ylink.inetpay.common.core.constant.EStartSystemEnum;
import com.ylink.inetpay.common.core.constant.EStatusEnum;
import com.ylink.inetpay.common.core.constant.MrsCustTypeEnum;
import com.ylink.inetpay.common.core.util.StringUtils;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsAduitInfoDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsAduitPersonDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsConfAuditDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsConfAuditItemDto;

@Service("mrsAduitInfoService")
public class MrsAduitInfoServiceImpl implements MrsAduitInfoService {

	private static Logger log = LoggerFactory.getLogger(MrsAduitInfoServiceImpl.class);
	
	//基础配置审核配置信息操作（配置审核人）
	@Autowired
	private MrsConfAuditDtoMapper mrsConfAuditDtoMapper;
	//审核配置明细信息
	@Autowired
	private MrsConfAuditItemDtoMapper mrsConfAuditItemDtoMapper;
	//审核配置明细信息操作
	@Autowired
	private MrsAduitPersonDtoMapper mrsAduitPersonDtoMapper;
	//审核主要信息操作
	@Autowired
	private MrsAduitInfoDtoMapper mrsAduitInfoDtoMapper;





	@Override
	public String craateMrsAduitInfo(String remark,
			String certNo,String certType,String name,
			String custId,String acountStatus,String productAuthStatus,
			EStartSystemEnum eStartSystemEnum, String loginUserName,
			EOperaTypeEnum eOperaTypeEnum,
			MrsCustTypeEnum mrsCustTypeEnum) {
		try {
			//1设置审核人，查询基础配置设置个人开户审核配置人数
			//审核主要信息
			MrsAduitInfoDto mrsAduitInfoDto = new MrsAduitInfoDto();
			
			MrsConfAuditDto mrsConfAuditDto = mrsConfAuditDtoMapper.findByBusiTypeAndSendType(
					eOperaTypeEnum.getValue(), eStartSystemEnum.getValue());
			if(mrsConfAuditDto == null){
				log.error("审核配置数据有误！业务类型为["+eOperaTypeEnum.getDisplayName()+"]"+"来源为：["+eStartSystemEnum.getDisplayName()+"]");
				throw new RuntimeException("审核配置数据有误！业务类型为["+eOperaTypeEnum.getDisplayName()+"]"+"来源为：["+eStartSystemEnum.getDisplayName()+"]");
			}
			
			mrsAduitInfoDto.setAduitTotal(mrsConfAuditDto.getAuditNo());
			mrsAduitInfoDto.setAduitNum((short)0);
			mrsAduitInfoDto.setCustId(custId);
			mrsAduitInfoDto.setBusiType(eOperaTypeEnum);
			mrsAduitInfoDto.setStartSystem(eStartSystemEnum);
			mrsAduitInfoDto.setCartNo(certNo);
			mrsAduitInfoDto.setCartType(certType);
			mrsAduitInfoDto.setName(name);
			if(!StringUtils.isBlank(remark)){
				mrsAduitInfoDto.setRemark(remark);
			}
			//个人
			mrsAduitInfoDto.setCustType(mrsCustTypeEnum);
			mrsAduitInfoDto.setCreateOperator(loginUserName);
			mrsAduitInfoDto.setCreateTime(new Date());
			mrsAduitInfoDto.setProductStatus(acountStatus);
			mrsAduitInfoDto.setProductAuthStatus(productAuthStatus);
			mrsAduitInfoDto.setStatus(EAduitTypeEnum.ADUIT_WAIT);
			mrsAduitInfoDto.setId(UUID.randomUUID().toString());
			
			List<MrsConfAuditItemDto> mrsConfAuditItems = mrsConfAuditItemDtoMapper.selectByAuditId(mrsConfAuditDto.getId());
			//创建审核人信息
			List<MrsAduitPersonDto> mrsAduitPersonDtos = new ArrayList<MrsAduitPersonDto>();
			for(MrsConfAuditItemDto item : mrsConfAuditItems){
				MrsAduitPersonDto aduitPerson = new MrsAduitPersonDto();
				aduitPerson.setAduitId(mrsAduitInfoDto.getId());
				aduitPerson.setAduitStatus(EAduitTypeEnum.ADUIT_WAIT);
				aduitPerson.setAduitUserId(item.getUserId());
				aduitPerson.setAduitUserName(item.getUserName());
				aduitPerson.setType(EDistributTypeEnum.AUTO_DISTRIBUT);
				aduitPerson.setStatus(EStatusEnum.EFFECTIVE);
				aduitPerson.setId(UUID.randomUUID().toString());
				aduitPerson.setCreateTime(new Date());
				mrsAduitPersonDtos.add(aduitPerson);
			}

			//创建审核主要信息
			mrsAduitInfoDtoMapper.insertSelective(mrsAduitInfoDto);
			mrsAduitPersonDtoMapper.batchInsert(mrsAduitPersonDtos);
			return mrsAduitInfoDto.getId();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		
		
	}

	
	
}
