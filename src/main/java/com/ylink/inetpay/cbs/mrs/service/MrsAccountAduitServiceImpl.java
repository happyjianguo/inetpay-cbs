package com.ylink.inetpay.cbs.mrs.service;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.ylinkpay.framework.core.model.PageData;
import org.ylinkpay.framework.web.base.util.PageMessageUtil;

import com.ylink.inetpay.cbs.common.CbsConstants;
import com.ylink.inetpay.cbs.mrs.dao.MrsAccountAduitInfoDtoMapper;
import com.ylink.inetpay.cbs.mrs.dao.MrsAduitContentDtoMapper;
import com.ylink.inetpay.cbs.mrs.dao.MrsAduitPersonDtoMapper;
import com.ylink.inetpay.cbs.mrs.dao.MrsAduitPersonHisDtoMapper;
import com.ylink.inetpay.cbs.mrs.dao.MrsConfAuditDtoMapper;
import com.ylink.inetpay.cbs.mrs.dao.MrsConfAuditItemDtoMapper;
import com.ylink.inetpay.cbs.mrs.dao.MrsLoginUserDtoMapper;
import com.ylink.inetpay.cbs.mrs.dao.MrsPortalAccountAduitDtoMapper;
import com.ylink.inetpay.cbs.mrs.dao.MrsUserAccountDtoMapper;
import com.ylink.inetpay.common.core.constant.ConfAuditBusiType;
import com.ylink.inetpay.common.core.constant.EAduitTypeEnum;
import com.ylink.inetpay.common.core.constant.EDistributTypeEnum;
import com.ylink.inetpay.common.core.constant.EOperateTypeEnum;
import com.ylink.inetpay.common.core.constant.EStartSystemEnum;
import com.ylink.inetpay.common.core.constant.EStatusEnum;
import com.ylink.inetpay.common.core.constant.LoginUserIsMain;
import com.ylink.inetpay.common.core.constant.LoginUserStatus;
import com.ylink.inetpay.common.core.constant.MrsCustTypeEnum;
import com.ylink.inetpay.common.core.util.ExceptionProcUtil;
import com.ylink.inetpay.common.project.cbs.app.MrsAccountAduitInfoAppService;
import com.ylink.inetpay.common.project.cbs.app.MrsAccountAppService;
import com.ylink.inetpay.common.project.cbs.app.MrsLoginUserAppService;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsAccountAduitInfoDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsAccountDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsAduitAttachmentDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsAduitContentDtoWithBLOBs;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsAduitPersonDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsAduitPersonHisDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsCertFileDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsConfAuditDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsConfAuditItemDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsContactListDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsLoginUserDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsOrganDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsPersonDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsPortalAccountAduitDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsProductDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsToJsonByUserDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsToJsonDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsUserAccountDto;
import com.ylink.inetpay.common.project.cbs.dto.ucs.sec.UcsSecUserDto;
import com.ylink.inetpay.common.project.cbs.exception.CbsCheckedException;
import com.ylink.inetpay.common.project.cbs.exception.CbsUncheckedException;
import com.ylink.inetpay.common.project.cbs.exception.ECbsErrorCode;
import com.ylink.inetpay.common.project.cbs.vo.mrs.SaveAduitPersonResponseVo;

import net.sf.json.JSONObject;

@Service("mrsAccountAduitService")
public class MrsAccountAduitServiceImpl implements MrsAccountAduitService {
	
	
	protected static final Logger logger = LoggerFactory.getLogger(MrsAccountAduitServiceImpl.class);
	@Autowired
	private MrsAduitPersonHisDtoMapper mrsAduitPersonHisDtoMapper;
	@Autowired
	private MrsAduitPersonDtoMapper mrsAduitPersonDtoMapper;
	@Autowired
    private MrsAccountAduitInfoAppService mrsAccountAduitInfoAppService;
	@Autowired
	private MrsAduitContentDtoMapper mrsAduitContentDtoMapper;
	@Autowired
	private MrsLoginUserDtoMapper mrsLoginUserDtoMapper;
	@Autowired
	private MrsConfAuditDtoMapper mrsConfAuditDtoMapper;
	@Autowired
	private MrsAccountAduitInfoDtoMapper mrsAccountAduitInfoDtoMapper;
	@Autowired
	private MrsConfAuditItemDtoMapper mrsConfAuditItemDtoMapper;
	//保存送审信息返回
	private SaveAduitPersonResponseVo response;
	@Autowired
	private MrsUserAccountDtoMapper mrsUserAccountDtoMapper;
	@Autowired
	private MrsPortalAccountAduitDtoMapper mrsPortalAccountAduitDtoMapper;

	@Autowired
	private MrsLoginUserAppService mrsLoginUserAppService;
	@Autowired
	private MrsAccountAppService mrsAccountAppService;

	@Override
	public void saveAfAduit(String type, UcsSecUserDto currentUser, MrsAccountAduitInfoDto mrsAccountAduitInfoDto,
			String aduitId, String remark) throws Exception {
		logger.info("开始个人审核，当前用户ID是：{}，审核主要信息表ID是：{}", new Object[] { currentUser.getId(), aduitId });
		logger.info("插入审核信息记录表");
		// 插入审核信息记录表
		MrsAduitPersonHisDto mrsAduitPersonHisDto = new MrsAduitPersonHisDto();
		mrsAduitPersonHisDto.setId(UUID.randomUUID().toString());
		mrsAduitPersonHisDto.setAduitId(aduitId);
		mrsAduitPersonHisDto.setAduitRemark(remark);
		mrsAduitPersonHisDto.setAduitStatus(EAduitTypeEnum.ADUIT_PASS);
		mrsAduitPersonHisDto.setAduitTime(new Date());
		mrsAduitPersonHisDto.setAduitUserId(currentUser.getId());
		mrsAduitPersonHisDto.setAduitUserName(currentUser.getLoginName());
		mrsAduitPersonHisDto.setCreateTime(new Date());
		mrsAduitPersonHisDtoMapper.insertSelective(mrsAduitPersonHisDto);

		logger.info("修改审核人信息表");
		// 修改审核人信息表
		MrsAduitPersonDto mrsAduitPersonDto = mrsAduitPersonDtoMapper.selectByAduitId(aduitId, currentUser.getId());
		mrsAduitPersonDto.setAduitStatus(EAduitTypeEnum.ADUIT_PASS);
		mrsAduitPersonDtoMapper.updateByPrimaryKeySelective(mrsAduitPersonDto);

		// 查询审核主要信息表，判断是否为最后一个审核人，若不是，则只改变
		if (mrsAccountAduitInfoDto.getAduitNum() + 1 < mrsAccountAduitInfoDto.getAduitTotal()) {
			// 还不是最后一个审核人，只更新审核主要信息表记录
			mrsAccountAduitInfoDto.setAduitNum((short) (mrsAccountAduitInfoDto.getAduitNum() + 1));
			mrsAccountAduitInfoDto.setStatus(EAduitTypeEnum.ADUIT_ING);
			mrsAccountAduitInfoAppService.updateByPrimaryKey(mrsAccountAduitInfoDto);
			return;
		} else {
			mrsAccountAduitInfoDto.setStatus(EAduitTypeEnum.ADUIT_PASS);
			mrsAccountAduitInfoDto.setAduitNum((short) (mrsAccountAduitInfoDto.getAduitNum() + 1));
			mrsAccountAduitInfoAppService.updateByPrimaryKey(mrsAccountAduitInfoDto);

			logger.info("审核信息表ID查询审核内容表");
			// 所有信息入库
			// 根据审核信息表ID查询审核内容表
			List<MrsAduitContentDtoWithBLOBs> list = mrsAduitContentDtoMapper.selectByAuditId(aduitId);
			if (list == null || list.size() != 1) {
				throw new RuntimeException("数据重复或找不到");
			}
			logger.info("转json");
			String json = list.get(0).getNewValue();
			MrsToJsonByUserDto dto = null;
			try {
				dto = jsonToDto(json);
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException("数据转换异常");
			}
			
			try{
				
				logger.info("保存用户信息表");
				// 保存用户信息表
				MrsLoginUserDto mrsLoginUserDto = dto.getMrsLoginUserDto();
				mrsLoginUserDto.setId(UUID.randomUUID().toString());
				mrsLoginUserDto.setCreateTime(new Date());
				mrsLoginUserDto.setUserStatus(LoginUserStatus.LOGIN_USER_STATUS_0.getValue());// 用户状态设置为有效
				mrsLoginUserDtoMapper.insertSelective(mrsLoginUserDto);
				
				logger.info("插入账户登陆用户关联信息表");
				List<MrsAccountDto> mrsAccountDtoList = dto.getMrsAccountDtoList();
				for(MrsAccountDto mrsAccountDto:mrsAccountDtoList){
					MrsUserAccountDto mrsUserAccountDto = new MrsUserAccountDto();
					mrsUserAccountDto.setId(UUID.randomUUID().toString());
					mrsUserAccountDto.setCreateTime(new Date());
					mrsUserAccountDto.setIsMain(mrsAccountDto.getIsMain().getValue());
					mrsUserAccountDto.setLoginId(mrsLoginUserDto.getId());
					mrsUserAccountDto.setAccountId(mrsAccountDto.getId());
					mrsUserAccountDtoMapper.insertSelective(mrsUserAccountDto);
				}
				
				
			}catch(Exception e){
				ExceptionProcUtil.getExceptionDesc(e);
				throw new RuntimeException(ExceptionProcUtil.getExceptionDesc(e));
			}
			
		}

	}
	
	
	@Override
	public void rbAduit(UcsSecUserDto currentUser, MrsAccountAduitInfoDto mrsAccountAduitInfoDto, String aduitId,
			String remark) {
		// 插入审核信息记录表
		try {
			MrsAduitPersonHisDto mrsAduitPersonHisDto = new MrsAduitPersonHisDto();
			mrsAduitPersonHisDto.setId(UUID.randomUUID().toString());
			mrsAduitPersonHisDto.setAduitId(aduitId);
			mrsAduitPersonHisDto.setAduitRemark(remark);
			mrsAduitPersonHisDto.setAduitStatus(EAduitTypeEnum.ADUIT_REJUST);// 审核拒绝
			mrsAduitPersonHisDto.setAduitTime(new Date());
			mrsAduitPersonHisDto.setAduitUserId(currentUser.getId());
			mrsAduitPersonHisDto.setAduitUserName(currentUser.getLoginName());
			mrsAduitPersonHisDto.setCreateTime(new Date());
			mrsAduitPersonHisDtoMapper.insertSelective(mrsAduitPersonHisDto);

			// 修改审核主要信息表
			// 根据审核信息表ID查询审核信息表
			mrsAccountAduitInfoDto.setStatus(EAduitTypeEnum.ADUIT_REJUST);
			mrsAccountAduitInfoDto.setAduitNum((short) (mrsAccountAduitInfoDto.getAduitNum() + 1));
			mrsAccountAduitInfoAppService.updateByPrimaryKey(mrsAccountAduitInfoDto);

			// 修改审核人信息表
			MrsAduitPersonDto mrsAduitPersonDto = mrsAduitPersonDtoMapper.selectByAduitId(aduitId, currentUser.getId());
			mrsAduitPersonDto.setAduitStatus(EAduitTypeEnum.ADUIT_REJUST);
			mrsAduitPersonDtoMapper.updateByPrimaryKeySelective(mrsAduitPersonDto);

			// 修改客户端开通一户通信息审核关联表
			MrsPortalAccountAduitDto mrsPortalAccountAduitDto = mrsPortalAccountAduitDtoMapper.selectByAduitId(aduitId);
			if(mrsPortalAccountAduitDto != null){
				mrsPortalAccountAduitDto.setStatus(EStatusEnum.UNEFFECTIVE.getValue());
				mrsPortalAccountAduitDtoMapper.updateByPrimaryKey(mrsPortalAccountAduitDto);
			}

		} catch (Exception e) {
			ExceptionProcUtil.getExceptionDesc(e);
			throw new RuntimeException(ExceptionProcUtil.getExceptionDesc(e));
		}
	}
	
	/**
	 * json转对象
	 */
	@SuppressWarnings("rawtypes")
	public MrsToJsonByUserDto jsonToDto(String json){
		//2个地方 MrsAduitServiceImpl 200行
		JSONObject obj = JSONObject.fromObject(json);//将json字符串转换为json对象
		Map<String, Class> classMap = new HashMap<String, Class>();  
        classMap.put("mrsAccountDtoList", MrsAccountDto.class);  
        return  (MrsToJsonByUserDto)JSONObject.toBean(obj,MrsToJsonByUserDto.class,classMap);
	}
	
	/**
	 * 
	 */
	@Transactional(value = CbsConstants.TX_MANAGER_MRS)
	public SaveAduitPersonResponseVo saveAduitUser(MrsToJsonByUserDto userVo) {
		response = new SaveAduitPersonResponseVo();
		try {
			
			//基本校验 校验传递的对象是否为空
			response = validateBasePerson(userVo);
			if(response!=null){
				return response;
			}
			response = new SaveAduitPersonResponseVo();
			//设置一户通
			userVo.getMrsLoginUserDto().setOpenTime(new Date());
			userVo.getMrsLoginUserDto().setUserStatus(LoginUserStatus.LOGIN_USER_STATUS_0.getValue());
			
			/**
			 *校验用户信息，用户昵称，用户手机号
			 */
			MrsLoginUserDto loginUser;
			loginUser = mrsLoginUserDtoMapper.findByAlias(userVo.getMrsLoginUserDto().getAlias());
			if(loginUser!=null){
				response.setIsSucess(false);
				response.setMsgCode("");
				response.setMsgInfo("添加个人用户信息送审时，用户昵称已经存在！");
				return response;
			}
			loginUser = mrsLoginUserDtoMapper.findByMobile(userVo.getMrsLoginUserDto().getMobile());
			if(loginUser!=null){
				response.setIsSucess(false);
				response.setMsgCode("");
				response.setMsgInfo("添加个人用户信息送审时，用户手机已经存在！");
				return response;
			}
			
			/**
			 * 设置审核相关信息
			 * 1,设置审核人
			 * 2，设置审核主要信息
			 * 3，根据选择配置子账户信息
			 * 4，设置审核内容信息
			 * 5，上传附件到文件服务器
			 */
			//1设置审核人，查询基础配置设置个人开户审核配置人数
			//审核主要信息
			MrsAccountAduitInfoDto mrsAccountAduitInfoDto = new MrsAccountAduitInfoDto();
			
			MrsConfAuditDto mrsConfAuditDto = mrsConfAuditDtoMapper.findByBusiTypeAndSendType(
					ConfAuditBusiType.OP_ADDUSER.getValue(), EStartSystemEnum.SYS_COUNTER.getValue());
			if(mrsConfAuditDto == null){
				response.setIsSucess(false);
				response.setMsgCode("");
				response.setMsgInfo("柜台端开户没有配置审核信息！");
				return response;
			}
			
			mrsAccountAduitInfoDto.setAduitTotal(mrsConfAuditDto.getAuditNo());
			mrsAccountAduitInfoDto.setAduitNum((short)0);
			mrsAccountAduitInfoDto.setBusiType(EOperateTypeEnum.OP_ADD);
			mrsAccountAduitInfoDto.setCreateOperator(userVo.getCurrentUser().getLoginName());
			mrsAccountAduitInfoDto.setEmail(userVo.getMrsLoginUserDto().getEmail());
			mrsAccountAduitInfoDto.setNikeName(userVo.getMrsLoginUserDto().getAlias());
			mrsAccountAduitInfoDto.setPhoneNo(userVo.getMrsLoginUserDto().getMobile());
//			mrsAccountAduitInfoDto.setRemark(userVo.getCreateRemark());
			mrsAccountAduitInfoDto.setUserId(userVo.getCurrentUser().getId());//新增用户暂填经办人id
			if(!StringUtils.isBlank(userVo.getCreateRemark())){
				mrsAccountAduitInfoDto.setRemark(userVo.getCreateRemark());
			}
			//个人
			mrsAccountAduitInfoDto.setCreateOperator(userVo.getMrsLoginUserDto().getCreateOperator());
			mrsAccountAduitInfoDto.setCreateTime(new Date());
			mrsAccountAduitInfoDto.setStatus(EAduitTypeEnum.ADUIT_WAIT);
			mrsAccountAduitInfoDto.setId(UUID.randomUUID().toString());
			
			//创建审核主要信息
			mrsAccountAduitInfoDtoMapper.insertSelective(mrsAccountAduitInfoDto);
			
			List<MrsConfAuditItemDto> mrsConfAuditItems = mrsConfAuditItemDtoMapper.selectByAuditId(mrsConfAuditDto.getId());
			
			List<MrsAduitPersonDto> mrsAduitPersonDtos = new ArrayList<MrsAduitPersonDto>();
			//创建审核人信息
			for(MrsConfAuditItemDto item : mrsConfAuditItems){
				MrsAduitPersonDto aduitPerson = new MrsAduitPersonDto();
				aduitPerson.setAduitId(mrsAccountAduitInfoDto.getId());
				aduitPerson.setAduitStatus(EAduitTypeEnum.ADUIT_WAIT);
				aduitPerson.setAduitUserId(item.getUserId());
				aduitPerson.setAduitUserName(item.getUserName());
				aduitPerson.setType(EDistributTypeEnum.AUTO_DISTRIBUT);
				aduitPerson.setStatus(EStatusEnum.EFFECTIVE);
				aduitPerson.setId(UUID.randomUUID().toString());
				aduitPerson.setCreateTime(new Date());
				mrsAduitPersonDtos.add(aduitPerson);
			}
			mrsAduitPersonDtoMapper.batchInsert(mrsAduitPersonDtos);
			
			//创建审核内容信息
			MrsAduitContentDtoWithBLOBs blobs = new MrsAduitContentDtoWithBLOBs();
			JSONObject jsons = JSONObject.fromObject(userVo);//将java对象转换为json对象
			String str = jsons.toString();
			
			blobs.setAduitId(mrsAccountAduitInfoDto.getId());
			blobs.setId(UUID.randomUUID().toString());
			blobs.setNewValue(str);
			blobs.setOldValue("");
			mrsAduitContentDtoMapper.insertSelective(blobs);
			
			response.setIsSucess(true);
			response.setMsgCode("00");
			response.setMsgInfo("柜台端开户送审信息成功！");
			return response;
		} catch (Exception e) {
			e.printStackTrace();
			response = new SaveAduitPersonResponseVo();
			response.setIsSucess(false);
			response.setMsgCode("");
			response.setMsgInfo("柜台端开户送审信息失败！"+e.getMessage());
			return response;
		}
		
	}
	
	public SaveAduitPersonResponseVo validateBasePerson(MrsToJsonByUserDto userVo) {
		SaveAduitPersonResponseVo response = new SaveAduitPersonResponseVo();
		if(userVo==null){
			response.setIsSucess(false);
			response.setMsgCode("");
			response.setMsgInfo("保存数据信息为空！");
			return response;
		}
		
		return null;
	}

	/*修改用户信息送审*/
	@Transactional(value = CbsConstants.TX_MANAGER_MRS)
	public SaveAduitPersonResponseVo saveUpdateUser(String id,MrsToJsonByUserDto personVo) {
		response = new SaveAduitPersonResponseVo();
		try {
			//基本校验 校验传递的对象是否为空
			response = validateBasePerson(personVo);
			if(response!=null){
				return response;
			}
			response = new SaveAduitPersonResponseVo();
			//设置一户通
			personVo.getMrsLoginUserDto().setOpenTime(new Date());
			personVo.getMrsLoginUserDto().setUserStatus(LoginUserStatus.LOGIN_USER_STATUS_0.getValue());
			
			//获取用户原始信息
			MrsLoginUserDto mrsLoginUserDtoOld = mrsLoginUserAppService.selectByPrimaryKey(id);
			//查找用户原始关联的一户通信息
			List<MrsLoginUserDto> list = mrsLoginUserAppService.getCustIdsByLoginUserId(id);
			List<MrsAccountDto> mrsAccountDtoList = new ArrayList<MrsAccountDto>();
			MrsToJsonByUserDto mrsToJsonByUserDtoOld=new MrsToJsonByUserDto();
			mrsToJsonByUserDtoOld.setMrsLoginUserDto(mrsLoginUserDtoOld);
			if(list != null){
				for(int i=0 ;i<list.size() ; i++){
					MrsAccountDto mrsAccountDto = mrsAccountAppService.findByCustId(list.get(i).getCoustId());
					mrsAccountDto.setIsMain(LoginUserIsMain.getEnum(list.get(i).getIsMain()));;
					mrsAccountDtoList.add(mrsAccountDto);
				}
				mrsToJsonByUserDtoOld.setMrsAccountDtoList(mrsAccountDtoList);
			}
			/**
			 * 设置审核相关信息
			 * 1,设置审核人
			 * 2，设置审核主要信息
			 * 3，根据选择配置子账户信息
			 * 4，设置审核内容信息
			 * 5，上传附件到文件服务器
			 */
			//1设置审核人，查询基础配置设置个人开户审核配置人数
			//审核主要信息
			MrsAccountAduitInfoDto mrsAccountAduitInfoDto = new MrsAccountAduitInfoDto();
			
			MrsConfAuditDto mrsConfAuditDto = mrsConfAuditDtoMapper.findByBusiTypeAndSendType(
					ConfAuditBusiType.OP_UPDATEUSER.getValue(), EStartSystemEnum.SYS_COUNTER.getValue());
			if(mrsConfAuditDto == null){
				response.setIsSucess(false);
				response.setMsgCode("");
				response.setMsgInfo("柜台端没有配置审核信息！");
				return response;
			}
			
			mrsAccountAduitInfoDto.setId(UUID.randomUUID().toString());
			mrsAccountAduitInfoDto.setBusiType(EOperateTypeEnum.OP_UPDATE);
			mrsAccountAduitInfoDto.setCreateOperator(personVo.getCurrentUser().getLoginName());
			/*if(!StringUtils.isBlank(personVo.getCreateRemark())){
				mrsAccountAduitInfoDto.setRemark(personVo.getCreateRemark());
			}*/
			mrsAccountAduitInfoDto.setRemark(personVo.getCreateRemark());
			mrsAccountAduitInfoDto.setCreateTime(new Date());//送审时间
			mrsAccountAduitInfoDto.setStatus(EAduitTypeEnum.ADUIT_WAIT);
			mrsAccountAduitInfoDto.setAduitTotal(mrsConfAuditDto.getAuditNo());
			mrsAccountAduitInfoDto.setAduitNum((short)0);
			mrsAccountAduitInfoDto.setUserId(id);
			mrsAccountAduitInfoDto.setNikeName(personVo.getMrsLoginUserDto().getAlias());
			mrsAccountAduitInfoDto.setPhoneNo(personVo.getMrsLoginUserDto().getMobile());
			mrsAccountAduitInfoDto.setEmail(personVo.getMrsLoginUserDto().getEmail());
			
			//创建审核主要信息
			mrsAccountAduitInfoDtoMapper.insertSelective(mrsAccountAduitInfoDto);
			
			List<MrsConfAuditItemDto> mrsConfAuditItems = mrsConfAuditItemDtoMapper.selectByAuditId(mrsConfAuditDto.getId());
			
			List<MrsAduitPersonDto> mrsAduitPersonDtos = new ArrayList<MrsAduitPersonDto>();
			//创建审核人信息
			for(MrsConfAuditItemDto item : mrsConfAuditItems){
				MrsAduitPersonDto aduitPerson = new MrsAduitPersonDto();
				aduitPerson.setAduitId(mrsAccountAduitInfoDto.getId());
				aduitPerson.setAduitStatus(EAduitTypeEnum.ADUIT_WAIT);
				aduitPerson.setAduitUserId(item.getUserId());
				aduitPerson.setAduitUserName(item.getUserName());
				aduitPerson.setType(EDistributTypeEnum.AUTO_DISTRIBUT);
				aduitPerson.setStatus(EStatusEnum.EFFECTIVE);
				aduitPerson.setId(UUID.randomUUID().toString());
				aduitPerson.setCreateTime(new Date());
				mrsAduitPersonDtos.add(aduitPerson);
			}
			mrsAduitPersonDtoMapper.batchInsert(mrsAduitPersonDtos);
			
			//新值
			MrsToJsonByUserDto mrsToJsonByUserDto=new MrsToJsonByUserDto();
			mrsToJsonByUserDto.setMrsLoginUserDto(personVo.getMrsLoginUserDto());
			mrsToJsonByUserDto.setMrsAccountDtoList(personVo.getMrsAccountDtoList());
			
			//创建审核内容信息
			MrsAduitContentDtoWithBLOBs blobs = new MrsAduitContentDtoWithBLOBs();
			JSONObject jsons = JSONObject.fromObject(mrsToJsonByUserDto);//将java对象转换为json对象
			String str = jsons.toString();
			
			JSONObject jsonsOld = JSONObject.fromObject(mrsToJsonByUserDtoOld);//将java对象转换为json对象
			String strOld = jsonsOld.toString();
			
			blobs.setAduitId(mrsAccountAduitInfoDto.getId());
			blobs.setId(UUID.randomUUID().toString());
			blobs.setNewValue(str);
			blobs.setOldValue(strOld);//原值
			mrsAduitContentDtoMapper.insertSelective(blobs);
			
			response.setIsSucess(true);
			response.setMsgCode("00");
			response.setMsgInfo("修改用户送审信息成功！");
			return response;
		} catch (Exception e) {
			e.printStackTrace();
			response = new SaveAduitPersonResponseVo();
			response.setIsSucess(false);
			response.setMsgCode("");
			response.setMsgInfo("修改用户送审信息失败！"+e.getMessage());
			return response;
		}
	}


	@Override
	@Transactional(value = CbsConstants.TX_MANAGER_MRS)
	public void updateAfAduit(String id, String remark, MrsAccountAduitInfoDto mrsAccountAduitInfoDto,
			UcsSecUserDto currentUser) throws CbsCheckedException {
		logger.info("开始用户变更审核，当前用户ID是：{}，审核主要信息表ID是：{}", new Object[] { currentUser.getId(), id });
		logger.info("插入审核信息记录表");
		// 插入审核信息记录表
		MrsAduitPersonHisDto mrsAduitPersonHisDto = new MrsAduitPersonHisDto();
		mrsAduitPersonHisDto.setId(UUID.randomUUID().toString());
		mrsAduitPersonHisDto.setAduitId(id);
		mrsAduitPersonHisDto.setAduitRemark(remark);
		mrsAduitPersonHisDto.setAduitStatus(EAduitTypeEnum.ADUIT_PASS);
		mrsAduitPersonHisDto.setAduitTime(new Date());
		mrsAduitPersonHisDto.setAduitUserId(currentUser.getId());
		mrsAduitPersonHisDto.setAduitUserName(currentUser.getLoginName());
		mrsAduitPersonHisDto.setCreateTime(new Date());
		mrsAduitPersonHisDtoMapper.insertSelective(mrsAduitPersonHisDto);

		logger.info("修改审核人信息表");
		// 修改审核人信息表
		MrsAduitPersonDto mrsAduitPersonDto = mrsAduitPersonDtoMapper.selectByAduitId(id, currentUser.getId());
		mrsAduitPersonDto.setAduitStatus(EAduitTypeEnum.ADUIT_PASS);
		mrsAduitPersonDtoMapper.updateByPrimaryKeySelective(mrsAduitPersonDto);

		// 查询审核主要信息表，判断是否为最后一个审核人，若不是，则只改变
		if (mrsAccountAduitInfoDto.getAduitNum() + 1 < mrsAccountAduitInfoDto.getAduitTotal()) {
			// 还不是最后一个审核人，只更新审核主要信息表记录
			mrsAccountAduitInfoDto.setAduitNum((short) (mrsAccountAduitInfoDto.getAduitNum() + 1));
			mrsAccountAduitInfoDto.setStatus(EAduitTypeEnum.ADUIT_ING);
			mrsAccountAduitInfoDtoMapper.updateByPrimaryKeySelective(mrsAccountAduitInfoDto);
			return;
		} else {
			mrsAccountAduitInfoDto.setStatus(EAduitTypeEnum.ADUIT_PASS);
			mrsAccountAduitInfoDto.setAduitNum((short) (mrsAccountAduitInfoDto.getAduitNum() + 1));
			mrsAccountAduitInfoDtoMapper.updateByPrimaryKeySelective(mrsAccountAduitInfoDto);

			List<MrsAduitContentDtoWithBLOBs> list = mrsAduitContentDtoMapper.selectByAuditId(id);
			if (list == null || list.size() != 1) {
				throw new CbsUncheckedException(ECbsErrorCode.ADUIT_0087);
			}

			String json = list.get(0).getNewValue();
			String oldjson = list.get(0).getOldValue();
			MrsToJsonByUserDto dto = null;
			MrsToJsonByUserDto oldDto = null;
			try {
				// json转换成对象
				dto = jsonToDto(json);
				oldDto = jsonToDto(oldjson);
			} catch (Exception e) {
				throw new CbsUncheckedException(ECbsErrorCode.ADUIT_0088);
			}
			
			MrsLoginUserDto mrsLoginUserDtoByJson = oldDto.getMrsLoginUserDto();
			
			MrsLoginUserDto mrsLoginUserDtoByBase = mrsLoginUserDtoMapper.selectByPrimaryKey(mrsLoginUserDtoByJson.getId());
			
			int mrsLoginUserDtoSize = equalsObj(mrsLoginUserDtoByBase,mrsLoginUserDtoByJson);
			if (mrsLoginUserDtoSize != 0) {
				throw new CbsUncheckedException(ECbsErrorCode.ADUIT_0096);
			}
			
			
			/** 保存所有数据 */
			try {
				// 更新一户通账户表
				logger.info("更新用户信息表");
				mrsLoginUserDtoMapper.updateByPrimaryKey(dto.getMrsLoginUserDto());
				

				logger.info("先删除所有跟用户信息关联的账户登陆用户关联信息表");
				for(MrsAccountDto mrsAccountDto:oldDto.getMrsAccountDtoList()){
					mrsUserAccountDtoMapper.deleteByUserIdAndAccountId(dto.getMrsLoginUserDto().getId(), mrsAccountDto.getId());
				}

				logger.info("更新账户登陆用户关联信息表");
				for(MrsAccountDto mrsAccountDto:dto.getMrsAccountDtoList()){
					MrsUserAccountDto mrsUserAccountDto = new MrsUserAccountDto();
					mrsUserAccountDto.setId(UUID.randomUUID().toString());
					mrsUserAccountDto.setCreateTime(new Date());
					mrsUserAccountDto.setAccountId(mrsAccountDto.getId());
					mrsUserAccountDto.setIsMain(mrsAccountDto.getIsMain().getValue());
					mrsUserAccountDto.setLoginId(dto.getMrsLoginUserDto().getId());
					mrsUserAccountDtoMapper.insert(mrsUserAccountDto);
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw new CbsUncheckedException(ECbsErrorCode.ADUIT_0095);
			}
			
		}
	}
	
	
	
	public static int equalsObj(Object obj1, Object obj2) {
		Map<String, List<Object>> map = new HashMap<String, List<Object>>();
		try {
			if (obj1 != null && obj2 != null && obj1.getClass() == obj2.getClass()) {
				Class clazz = obj1.getClass();
				PropertyDescriptor[] pds = Introspector.getBeanInfo(clazz, Object.class).getPropertyDescriptors();
				for (PropertyDescriptor pd : pds) {
					String name = pd.getName();
					Method readMethod = pd.getReadMethod();
					Object o1 = readMethod.invoke(obj1);
					Object o2 = readMethod.invoke(obj2);
					if (o1 != null && o2 != null && !o1.equals(o2)) {
						List<Object> list = new ArrayList<Object>();
						list.add(o1);
						list.add(o2);
						map.put(name, list);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map.size();
	}
	
	private MrsAccountDto accountDto(String custId) {
		MrsAccountDto accDto = new MrsAccountDto();
		try {
			accDto = mrsAccountAppService.findByCustId(custId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return accDto;
	}
}
