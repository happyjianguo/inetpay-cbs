package com.ylink.inetpay.cbs.mrs.service;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ylink.inetpay.cbs.common.CbsConstants;
import com.ylink.inetpay.cbs.mrs.dao.MrsAssignShieNoDtoMapper;
import com.ylink.inetpay.cbs.mrs.dao.MrsAssignShieNoSubDtoMapper;
import com.ylink.inetpay.common.core.constant.MrsCustType;
import com.ylink.inetpay.common.core.constant.MrsCustTypeReq;
import com.ylink.inetpay.common.core.util.StringUtils;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsAssignShieNoDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsAssignShieNoSubDto;
import com.ylink.inetpay.common.project.portal.vo.AssignShieNoRequestVO;

@Service("mrsAssignService")
public class MrsAssignServiceImpl implements MrsAssignService {
	private static Logger log = LoggerFactory.getLogger(MrsAssignServiceImpl.class);
	@Autowired
	private MrsAssignShieNoDtoMapper mrsAssignShieNoDtoMapper;
	@Autowired
	private MrsAssignShieNoSubDtoMapper mrsAssignShieNoSubDtoMapper;
	@Override
	@Transactional(value=CbsConstants.TX_MANAGER_MRS)
	public String findAssignNoBy3Element(String custName, String certiType, String certiNum,String source) {
		log.info(String.format("根据三要素查询系统是否存在历史配号数据,客户姓名：%s,证件类型：%s,证件号码：%s",
				custName, certiType, certiNum));
		MrsAssignShieNoDto mrsAssignShieNoDto = mrsAssignShieNoDtoMapper.findAssignNoBy3Element(custName, certiType,
				certiNum);
		if (mrsAssignShieNoDto != null) {
			log.info(String.format("存在历史配号数据,根据会员配号主键：%s,来源：%s,查询会员配号子表",
					mrsAssignShieNoDto.getId(), source));
			//如果已经有数据，则判断来源系统，对应的子表是否保存了系统来源
			MrsAssignShieNoSubDto subDto = mrsAssignShieNoSubDtoMapper.selectByRefIdAndSource(mrsAssignShieNoDto.getId(), source);
			if(subDto==null){
				//新增子表
				MrsAssignShieNoSubDto mrsAssignShieNoSubDto = new MrsAssignShieNoSubDto();
				mrsAssignShieNoSubDto.setId(mrsAssignShieNoSubDto.getIdentity());
				mrsAssignShieNoSubDto.setRefId(mrsAssignShieNoDto.getId());
				mrsAssignShieNoSubDto.setSource(source);
				mrsAssignShieNoSubDto.setCreateTime(new Date());
				mrsAssignShieNoSubDtoMapper.insert(mrsAssignShieNoSubDto);
				log.info(String.format("新增会员配号子表,会员配号主键：%s,来源：%s",
						mrsAssignShieNoDto.getId(), source));
			}
			return mrsAssignShieNoDto.getAssignShieNo();
		}
		return null;
	}

	@Override
	public String generateAssignShieNo(String custType) {
		// 采用12位字符型编码，初期只用数字，采用顺序号编码。
		// 暂定2XXXXXXXXXXX为自然人账户编码区间，1XXXXXXXXXXX为非自然人账户（包括机构、产品）编码区间。
		String assignShieNo = "";
		String seqNo = "";
		if (MrsCustType.MRS_CUST_TYPE_01.getValue().equals(custType)) {
			seqNo = mrsAssignShieNoDtoMapper.getMrsAssignShieNoVal();
			assignShieNo = MrsCustTypeReq.MRS_CUST_TYPE_2.getValue();
		} else {
			seqNo = mrsAssignShieNoDtoMapper.getMrsAssignOrganVal();
			assignShieNo = MrsCustTypeReq.MRS_CUST_TYPE_1.getValue();
		}
		// 左补零
		seqNo = StringUtils.lpadZero(seqNo, 11);
		log.info("账户平台配号接口,客户号生成=[{}]",assignShieNo + seqNo);
		return assignShieNo + seqNo;
	}

	@Override
	@Transactional(value=CbsConstants.TX_MANAGER_MRS)
	public void saveAssignShieNo(AssignShieNoRequestVO assignShieNoRequestVO, String assignShieNo) {
		MrsAssignShieNoDto mrsAssignShieNoDto = new MrsAssignShieNoDto();
		BeanUtils.copyProperties(assignShieNoRequestVO, mrsAssignShieNoDto);
		mrsAssignShieNoDto.setId(mrsAssignShieNoDto.getIdentity());
		mrsAssignShieNoDto.setCreateTime(new Date());
		mrsAssignShieNoDto.setAssignShieNo(assignShieNo);
		mrsAssignShieNoDtoMapper.insert(mrsAssignShieNoDto);
		//新增子表
		MrsAssignShieNoSubDto mrsAssignShieNoSubDto = new MrsAssignShieNoSubDto();
		mrsAssignShieNoSubDto.setId(mrsAssignShieNoSubDto.getIdentity());
		mrsAssignShieNoSubDto.setRefId(mrsAssignShieNoDto.getId());
		mrsAssignShieNoSubDto.setSource(mrsAssignShieNoDto.getSource());
		mrsAssignShieNoSubDto.setCreateTime(new Date());
		mrsAssignShieNoSubDtoMapper.insert(mrsAssignShieNoSubDto);
		log.info(String.format("新增会员配号表和会员配号子表数据,会员配号主键：%s,会员配号子表主键：%s",
				mrsAssignShieNoDto.getId(), mrsAssignShieNoSubDto.getId()));
	}

	@Override
	public MrsAssignShieNoSubDto findByRefId(String refId,String source) {
		return mrsAssignShieNoSubDtoMapper.selectByRefIdAndSource(refId, source);
	}

	@Override
	public void updateSubDto(MrsAssignShieNoSubDto mrsAssignShieNoSubDto) {
		mrsAssignShieNoSubDtoMapper.updateByPrimaryKey(mrsAssignShieNoSubDto);
		
	}
}
