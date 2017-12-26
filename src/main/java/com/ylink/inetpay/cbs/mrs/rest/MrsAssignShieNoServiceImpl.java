package com.ylink.inetpay.cbs.mrs.rest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.ylink.eu.util.tools.StringUtil;
import com.ylink.inetpay.cbs.common.CbsConfig;
import com.ylink.inetpay.cbs.mrs.service.MrsAssignService;
import com.ylink.inetpay.common.core.constant.EBisExceptionLogNlevel;
import com.ylink.inetpay.common.core.constant.EBisExceptionLogType;
import com.ylink.inetpay.common.core.constant.EBisSmsSystem;
import com.ylink.inetpay.common.core.constant.MrsCredentialsType;
import com.ylink.inetpay.common.core.constant.MrsCustType;
import com.ylink.inetpay.common.core.constant.MrsReturnStatus;
import com.ylink.inetpay.common.core.constant.MrsSource;
import com.ylink.inetpay.common.core.exception.CodeCheckedException;
import com.ylink.inetpay.common.project.cbs.app.BisExceptionLogAppService;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisExceptionLogDto;
import com.ylink.inetpay.common.project.portal.vo.AssignShieNoRequestVO;
import com.ylink.inetpay.common.project.portal.vo.AssignShieNoResponseVO;
import com.ylink.inetpay.common.project.portal.vo.AssignShieNoRetrunVO;
import net.sf.json.JSONObject;
@Service("mrsAssignShieNoService")
public class MrsAssignShieNoServiceImpl implements MrsAssignShieNoService {

	private static Logger log = LoggerFactory.getLogger(MrsAssignShieNoServiceImpl.class);

	@Autowired
	private MrsAssignService mrsAssignService;
	@Autowired
	private  BisExceptionLogAppService bisExceptionLogAppService;
	@Override
	public AssignShieNoResponseVO assignShieNo(String params) {
		log.info("账户平台配号接口,请求参数:{}", params);
		AssignShieNoRequestVO assignShieNoReqVO = new AssignShieNoRequestVO();
		AssignShieNoResponseVO assignShieNoRespVo = new AssignShieNoResponseVO();
		try {
			// 将请求Gson转换为对象
			assignShieNoReqVO = toAssignShieNoRequestVO(params);
			// 解析参数封装成request对象
			String checkResult = checkAssignShieNoParams(assignShieNoReqVO);
			if (checkResult != null) {
				log.error("参数[" + params + "]校验失败:" + checkResult);
				assignShieNoRespVo.setReturnStatus(MrsReturnStatus.RETURN_STATUS_04.getValue());
				assignShieNoRespVo.setReturnMsg(checkResult);
				return assignShieNoRespVo;
			}
			// 根据三要素查询系统是否存在历史配号数据，若存在，返回客户号
			log.info(String.format("根据三要素查询系统是否存在历史配号数据,客户姓名：%s,证件类型：%s,证件号码：%s",
					assignShieNoReqVO.getCustName(), assignShieNoReqVO.getCertiType(), assignShieNoReqVO.getCertiNum()));
			String assignShieNo = mrsAssignService.findAssignNoBy3Element(assignShieNoReqVO.getCustName(),
					assignShieNoReqVO.getCertiType(), assignShieNoReqVO.getCertiNum(),assignShieNoReqVO.getSource());
			
			// 若系统不存在“历史”配号数据，系统根据规则生成新的客户号
			if (StringUtils.isEmpty(assignShieNo)) {
				log.info("系统不存在“历史”配号数据，系统根据规则生成新的客户号");
				// 客户号生成
				assignShieNo = mrsAssignService.generateAssignShieNo(assignShieNoReqVO.getCustType());
				// 生成新会员信息
				mrsAssignService.saveAssignShieNo(assignShieNoReqVO, assignShieNo);
			}
			log.info("账户平台配号接口,返回客户号={}", assignShieNo);
			// 返回客户号
			AssignShieNoRetrunVO returnvVo = new AssignShieNoRetrunVO();
			returnvVo.setAssignNo(assignShieNo);
			assignShieNoRespVo.setReturnValue(returnvVo);
			assignShieNoRespVo.setReturnStatus(MrsReturnStatus.RETURN_STATUS_00.getValue());
			assignShieNoRespVo.setReturnMsg(MrsReturnStatus.RETURN_STATUS_00.getDisplayName());
		} catch (CodeCheckedException e) {
			log.error("会员号配号失败：" + e.getMessage());
			assignShieNoRespVo.setReturnStatus(MrsReturnStatus.RETURN_STATUS_04.getValue());
			assignShieNoRespVo.setReturnMsg(e.getMessage());
			log.error(String.format("会员号配号失败,数据不合法,配号三要素,客户姓名：%s,证件类型：%s,证件号码：%s,来源系统：%s",
					assignShieNoReqVO.getCustName(), assignShieNoReqVO.getCertiType(), assignShieNoReqVO.getCertiNum(),
					assignShieNoReqVO.getSource()));
			saveErrorExcetpionLog(String.format("会员号配号失败,数据不合法,配号三要素,客户姓名：%s,证件类型：%s,证件号码：%s,来源系统：%s",
					assignShieNoReqVO.getCustName(), assignShieNoReqVO.getCertiType(), assignShieNoReqVO.getCertiNum(),
					assignShieNoReqVO.getSource()));
			return assignShieNoRespVo;
		} catch (Exception e) {
			log.error("会员号配号失败：", e);
			assignShieNoRespVo.setReturnStatus(MrsReturnStatus.RETURN_STATUS_09.getValue());
			assignShieNoRespVo.setReturnMsg(MrsReturnStatus.RETURN_STATUS_09.getDisplayName());
			log.error(String.format("会员号配号失败,系统异常,配号三要素,客户姓名：%s,证件类型：%s,证件号码：%s,来源系统：%s",
					assignShieNoReqVO.getCustName(), assignShieNoReqVO.getCertiType(), assignShieNoReqVO.getCertiNum(),
					assignShieNoReqVO.getSource()));
			saveErrorExcetpionLog(String.format("会员号配号失败,系统异常,配号三要素,客户姓名：%s,证件类型：%s,证件号码：%s,来源系统：%s",
					assignShieNoReqVO.getCustName(), assignShieNoReqVO.getCertiType(), assignShieNoReqVO.getCertiNum(),
					assignShieNoReqVO.getSource()));
			return assignShieNoRespVo;
		}
		JSONObject jsons = JSONObject.fromObject(assignShieNoRespVo);
		log.info("账户平台配号完成,返回json对象：{}",jsons.toString());
		return assignShieNoRespVo;
	}
	/**
	 * 记录异常日志
	 * @param errorMsg
	 */
	private void saveErrorExcetpionLog(String errorMsg){
		BisExceptionLogDto	dto=new BisExceptionLogDto();
		dto.setSystem(EBisSmsSystem.CBS);
		dto.setNlevel(EBisExceptionLogNlevel.ERROR);
		dto.setType(EBisExceptionLogType.MRS_REST_CUST);
		dto.setContent(errorMsg);
		dto.setAllpath(CbsConfig.getLogFullPath());
		try {
			bisExceptionLogAppService.saveLog(dto);
		} catch (Exception e) {
			log.error("会员号配号,记录异常日志失败！");
		}
	}
	/**
	 * 检查必需参数
	 * 
	 * @param baseContent
	 * @param bizContent
	 * @return
	 */
	private String checkAssignShieNoParams(AssignShieNoRequestVO assignShieNoReqVO) {
		if (assignShieNoReqVO == null) {
			return "AssignShieNoRequestVO对象问空";
		}
		if (StringUtil.isEmpty(assignShieNoReqVO.getCustName())) {
			return "客户姓名为空";
		}
		if (assignShieNoReqVO.getCustName().length() > 30) {
			return "客户姓名超长";
		}
		if (StringUtil.isEmpty(assignShieNoReqVO.getCertiType())) {
			return "客户证件类型为空";
		}
		if (assignShieNoReqVO.getCertiType().length() > 2) {
			return "客户证件类型超长";
		}
		if(StringUtil.isNEmpty(assignShieNoReqVO.getCertiType())) {
			boolean checkFlag=false;
			for(MrsCredentialsType credentialsType: MrsCredentialsType.getMrsCredentialsTypeList()){
				if(credentialsType.getValue().equals(assignShieNoReqVO.getCertiType())){
					checkFlag=true;
				}
			}
			if(!checkFlag){
				return "证件类型不存在";
			}
		}
		if (StringUtil.isEmpty(assignShieNoReqVO.getCertiNum())) {
			return "客户证件号码为空";
		}
		if (assignShieNoReqVO.getCertiNum().length() > 50) {
			return "客户证件号码超长";
		}
		if (StringUtil.isEmpty(assignShieNoReqVO.getCustType())) {
			return "客户类型为空";
		}
		if (assignShieNoReqVO.getCustType().length() > 2) {
			return "客户类型超长";
		}
		if (StringUtil.isNEmpty(assignShieNoReqVO.getCustType())
				&& MrsCustType.getEnum(assignShieNoReqVO.getCustType()) == null) {
			return "客户类型不存在";
		}
		if (StringUtil.isEmpty(assignShieNoReqVO.getSource())) {
			return "来源系统为空";
		}
		if (assignShieNoReqVO.getSource().length() > 30) {
			return "来源系统超长";
		}
		if (StringUtil.isNEmpty(assignShieNoReqVO.getSource())
				&& MrsSource.getEnum(assignShieNoReqVO.getSource()) == null) {
			return "来源系统不存在";
		}
		return null;
	}

	/**
	 * 从json获取Bean 对象
	 * 
	 * @param jsonString
	 * @param cls
	 * @return
	 */
	private <T> T getObjectBean(String jsonString, Class<T> cls) {
		T t = null;
		Gson gson = new Gson();
		t = gson.fromJson(jsonString, cls);
		return t;
	}

	/**
	 * 将请求Gson转换为对象
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	private AssignShieNoRequestVO toAssignShieNoRequestVO(String params) throws Exception {
		AssignShieNoRequestVO requestVo = null;
		try {
			requestVo = getObjectBean(params, AssignShieNoRequestVO.class);
			if (requestVo == null) {
				log.error("转换对象为空、Gson对象转换失败!");
				throw new CodeCheckedException(MrsReturnStatus.RETURN_STATUS_04.getValue(), "JSON转换失败");
			}
			return requestVo;
		} catch (Exception e) {
			log.error("Gson转换错误：" + e.toString());
			throw new CodeCheckedException(MrsReturnStatus.RETURN_STATUS_04.getValue(), "JSON转换失败");
		}
	}

}
