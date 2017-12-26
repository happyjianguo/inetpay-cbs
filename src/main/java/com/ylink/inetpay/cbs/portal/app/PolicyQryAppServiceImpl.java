package com.ylink.inetpay.cbs.portal.app;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ylink.eu.util.tools.CollectionUtil;
import com.ylink.inetpay.cbs.common.CbsConfig;
import com.ylink.inetpay.cbs.portal.service.PolicyQryService;
import com.ylink.inetpay.common.core.constant.EBisExceptionLogNlevel;
import com.ylink.inetpay.common.core.constant.EBisExceptionLogType;
import com.ylink.inetpay.common.core.constant.EBisSmsSystem;
import com.ylink.inetpay.common.project.cbs.app.BisExceptionLogAppService;
import com.ylink.inetpay.common.project.cbs.app.PolicyQryAppService;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisExceptionLogDto;
import com.ylink.inetpay.common.project.portal.enums.ErrorMsgEnum;
import com.ylink.inetpay.common.project.portal.vo.UserCheckVO;
import com.ylink.inetpay.common.project.portal.vo.customerpolicyqry.CustomerPolicyQryReqVO;
import com.ylink.inetpay.common.project.portal.vo.customerpolicyqry.CustomerPolicyQryResultObj;
import com.ylink.inetpay.common.project.portal.vo.policy.PolicyJson;
import com.ylink.inetpay.common.project.portal.vo.policyqry.PolicyQryReqVO;
import com.ylink.inetpay.common.project.portal.vo.policyqry.PolicyQryResultObj;
import com.ylink.inetpay.common.project.portal.vo.policyqry.PortalPolicyRespVO;
import com.ylink.inetpay.common.project.portal.vo.policyqry.PrpCepolicyVO;
import com.ylink.inetpay.common.project.portal.vo.policyqry.PrpCinsuredPropVO;
import com.ylink.inetpay.common.project.portal.vo.policyqry.PrpCinsuredVO;
import com.ylink.inetpay.common.project.portal.vo.policyqry.PrpCmainVO;

@Service("policyQryAppService")
public class PolicyQryAppServiceImpl implements PolicyQryAppService {

	private static Logger log = LoggerFactory.getLogger(PolicyQryAppServiceImpl.class);
	
	@Autowired
	private PolicyQryService policyQryService;
	@Autowired
	private  BisExceptionLogAppService bisExceptionLogAppService;
	@Override
	public PortalPolicyRespVO doPolicyQry(PolicyQryReqVO reqVo) {
		PortalPolicyRespVO respVo = null;
		try {
			PolicyQryResultObj resultObj = policyQryService.doPolicyQry(reqVo);
			if(resultObj == null) {
				log.info("查询结果为空...");
				return null;
			}
			UserCheckVO checkVo = resultObj.getUserCheckVo();
			if(!checkVo.isCheckValue()) {
				log.info("查询失败:" + checkVo.getMsg());
				respVo = new PortalPolicyRespVO();
				respVo.setUserCheckVo(checkVo);
				return respVo;
			}
			respVo = generatorPortalVo(resultObj);
			respVo.setUserCheckVo(new UserCheckVO(true));
			return respVo;
		} catch (Exception e) {
			log.error("保单详情查询失败:", e);
			respVo = new PortalPolicyRespVO();
			respVo.setUserCheckVo(new UserCheckVO(false, ErrorMsgEnum.POLICY_QRY_FAIL));
			saveErrorExcetpionLog(String.format("保单详情查询失败,保单号：%s,投被保人证件号码：%s,保单角色：%s,投被保人证件类型：%s",
					reqVo.getPolicyNo(),
					reqVo.getIdentifyNumber(),
					reqVo.getInsuredQueryType(),
					reqVo.getIdentifyType()));
			return respVo;
		}
	}
	
	@Override
	public CustomerPolicyQryResultObj doCustomerPolicyQry(CustomerPolicyQryReqVO reqVo) {
		try {
			CustomerPolicyQryResultObj resultObj = policyQryService.doCustomerPolicyQry(reqVo);
			if(resultObj == null) {
				log.info("查询结果为空...");
				return null;
			}
			UserCheckVO checkVo = resultObj.getUserCheckVo();
			if(!checkVo.isCheckValue()) {
				log.info("查询失败:" + checkVo.getMsg());
				return resultObj;
			}
			resultObj.setUserCheckVo(new UserCheckVO(true));
			return resultObj;
		} catch (Exception e) {
			log.error("客户保单查询(带分页)失败:", e);
			CustomerPolicyQryResultObj resultObj = new CustomerPolicyQryResultObj();
			resultObj.setUserCheckVo(new UserCheckVO(false, ErrorMsgEnum.CUSTOMER_POLICY_QRY_FAIL));
			saveErrorExcetpionLog(String.format("客户保单查询(带分页)失败, 客户编号：%s,一户通账号：%s,保单号：%s",
					reqVo.getInsuredCode(),
					reqVo.getAccountCode(),
					reqVo.getPolicyNo()));
			return resultObj;
		}
	}

	/**`
	 * 构造门户返回对象
	 * @param resultObj
	 * @return
	 */
	private PortalPolicyRespVO generatorPortalVo(PolicyQryResultObj resultObj) {
		PortalPolicyRespVO portalVo = new PortalPolicyRespVO();
		PrpCepolicyVO prpCepolicyVo = resultObj.getPrpCepolicyDto();
		PrpCinsuredVO prpCinsuredVo = resultObj.getPrpCinsuredDto();
		List<PrpCinsuredPropVO> lists = resultObj.getPrpCinsuredPropDtos();
		if(CollectionUtil.isNEmpty(lists)) {
			if(lists.size() == 1) {
				portalVo.setHasOneInsured(true);
				PrpCinsuredPropVO prpCinsuredPropVO = lists.get(0);
				portalVo.setInsuredName(prpCinsuredPropVO.getInsuredName());
				portalVo.setInsuredMobile(prpCinsuredPropVO.getMobile());
				portalVo.setProvinceName(prpCinsuredPropVO.getProvinceName());
				
				portalVo.setCityName(prpCinsuredPropVO.getCityName());
				portalVo.setTownName(prpCinsuredPropVO.getTownName());
				portalVo.setAddress(prpCinsuredPropVO.getAddress());
				portalVo.setHouseTypeName(prpCinsuredPropVO.getHouseTypeName());
				portalVo.setBuildTypeName(prpCinsuredPropVO.getBuildTypeName());
				
				portalVo.setBuildStructureName(prpCinsuredPropVO.getBuildStructureName());
				portalVo.setBuildArea(prpCinsuredPropVO.getBuildArea());
				portalVo.setBuildDate(prpCinsuredPropVO.getBuildDate());
				portalVo.setAmount(prpCinsuredPropVO.getAmountStr());
				portalVo.setPremium(prpCinsuredPropVO.getPremiumStr());
			} else {
				portalVo.setHasOneInsured(false);
			}
		}
		PrpCmainVO prpCmainVO = resultObj.getPrpCmainDto();

		portalVo.setPolicyNo(resultObj.getPolicyNo());
		portalVo.setRiskName(prpCmainVO.getRiskName());
		portalVo.setRiskCode(prpCmainVO.getRiskCode());
		portalVo.setCompanyName(prpCmainVO.getCompanyName());
		portalVo.setStatusName(prpCmainVO.getStatusName());
		portalVo.setStartDate(prpCmainVO.getStartDate());
		
		portalVo.setEndDate(prpCmainVO.getEndDate());
		portalVo.setSumAmount(prpCmainVO.getSumAmountStr());
		portalVo.setSumQuantity(prpCmainVO.getSumQuantityStr());
		portalVo.setSumPremium(prpCmainVO.getSumPremiumStr());
		portalVo.setApplyName(prpCmainVO.getApplyName());

		
		portalVo.setMobile(prpCinsuredVo.getMobile());
		portalVo.setFileId(prpCepolicyVo.getFileId());
		
		return portalVo;
	}
	/**
	 * 记录异常日志
	 * @param errorMsg
	 */
	private void saveErrorExcetpionLog(String errorMsg){
		BisExceptionLogDto	dto=new BisExceptionLogDto();
		dto.setSystem(EBisSmsSystem.CBS);
		dto.setNlevel(EBisExceptionLogNlevel.ERROR);
		dto.setType(EBisExceptionLogType.MRS_ORDER_SEARCH);
		dto.setContent(errorMsg);
		dto.setAllpath(CbsConfig.getLogFullPath());
		try {
			bisExceptionLogAppService.saveLog(dto);
		} catch (Exception e) {
			log.error("保单查询,记录异常日志失败！");
		}
	}

	@Override
	public List<PolicyJson> findPolicy(String policyNo, String orgCode) {
		return policyQryService.findPolicy(policyNo,orgCode);
		
	}

	@Override
	public List<PolicyJson> find3Element(String name, String certiType, String certiCode) {
		return policyQryService.find3Element(name, certiType, certiCode);
	}
	
}
