package com.ylink.inetpay.cbs.mrs.App;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.ylink.eu.util.tools.CollectionUtil;
import com.ylink.eu.util.tools.StringUtil;
import com.ylink.inetpay.cbs.mrs.service.MrsAccountService;
import com.ylink.inetpay.cbs.mrs.service.MrsDataAuditChangeService;
import com.ylink.inetpay.cbs.mrs.service.MrsLoginUserService;
import com.ylink.inetpay.cbs.mrs.service.MrsOrganService;
import com.ylink.inetpay.common.core.constant.EAuditStatus;
import com.ylink.inetpay.common.core.util.JsonUtil;
import com.ylink.inetpay.common.project.cbs.app.MrsOrganAppService;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsAduitAttachmentDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsDataAuditChangeDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsLoginUserDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsOrganDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsUserAccountDto;
import com.ylink.inetpay.common.project.cbs.vo.bis.ResponseMessage;
import com.ylink.inetpay.common.project.cbs.vo.mrs.MrsActAccountVo;
import com.ylink.inetpay.common.project.cbs.vo.mrs.MrsOrganVo;
import com.ylink.inetpay.common.project.cbs.vo.mrs.MrsRemoveAccountVo;
import com.ylink.inetpay.common.project.cbs.vo.mrs.SaveAduitPersonResponseVo;
import com.ylink.inetpay.common.project.cbs.vo.mrs.SaveAduitProductResponseVo;
import com.ylink.inetpay.common.project.portal.dto.AccountMsg;
import com.ylink.inetpay.common.project.portal.enums.ErrorMsgEnum;
import com.ylink.inetpay.common.project.portal.exception.PortalCheckedException;
import com.ylink.inetpay.common.project.portal.vo.UploadOrganPojo;
import com.ylink.inetpay.common.project.portal.vo.UserCheckVO;
import com.ylink.inetpay.common.project.portal.vo.customer.OrganVO;

@Service("mrsOrganAppService")
public class MrsOrganAppServiceImpl implements MrsOrganAppService {

	private static Logger log = LoggerFactory.getLogger(MrsOrganAppServiceImpl.class);
	
	@Autowired
	private MrsOrganService mrsOrganService;
	@Autowired
	private MrsLoginUserService mrsLoginUserService;
	@Autowired
	private MrsAccountService mrsAccountService;
	@Autowired
	private MrsDataAuditChangeService mrsDataAuditChangeService;
	@Override
	public List<MrsOrganDto> findOrganByParams(String customerType, String accountStatus){
		return mrsOrganService.findOrganByParams(customerType, accountStatus);
	}

	@Override
	public PageData<MrsOrganDto> findOrgan(PageData<MrsOrganDto> pageData, MrsOrganDto searchDto) {
		return mrsOrganService.findOrgan(pageData, searchDto);
	}

	@Override
	public MrsOrganDto findById(String id) {
		return mrsOrganService.findById(id);
	}
	
	@Override
	public MrsOrganDto findByCustId(String custId) throws PortalCheckedException {
		try {
			return mrsOrganService.findByCustId(custId);
		} catch (Exception e) {
			log.error("根据CustId查找机构信息失败:",e);
			throw new PortalCheckedException("查询失败");
		}
	}

	@Override
	public void update(MrsOrganDto dto) throws PortalCheckedException {
		try {
			mrsOrganService.update(dto);
		} catch (Exception e) {
			log.error("修改机构信息失败:",e);
			throw new PortalCheckedException("修改失败");
		}
	}
	@Override
	public void updateOrgan(MrsOrganDto dto,String loginName) throws PortalCheckedException {
		try {
			mrsOrganService.updateOrgan(dto,loginName);
		} catch (Exception e) {
			log.error("修改机构信息失败:",e);
			throw new PortalCheckedException("修改失败");
		}
	}
	@Override
	public MrsOrganDto findByExtOrgId(String extOrgId) {
		return mrsOrganService.findByExtOrgId(extOrgId);
	}

	@Override
	public OrganVO portalFindByCustId(String custId) {
		log.debug("MrsOrganAppServiceImpl.portalFindByCustId run....");
		OrganVO vo = null;
		try {
			vo = mrsOrganService.findOrganVoByCustId(custId);
			if(vo == null){
				log.error("客户[custId = "+custId+"]信息不存在");
				UserCheckVO checkVo = new UserCheckVO(false, ErrorMsgEnum.USER_NULL);
				vo = new OrganVO();
				vo.setCheckVo(checkVo);
				return vo;
			}
			vo.setCheckVo(new UserCheckVO(true));
			return vo;
		} catch (Exception e){
			log.error("查询失败:", e);
			vo = new OrganVO();
			vo.setCheckVo(new UserCheckVO(false, ErrorMsgEnum.SEARCH_FAIL));
			return vo;
		}
	}

	@Override
	public UserCheckVO portalUpdate(OrganVO vo) {
		try {
			log.debug("MrsOrganAppServiceImpl.portalUpdate run....");
			MrsOrganDto dto = mrsOrganService.findById(vo.getId());
			if(dto == null){
				log.error("客户MrsOrganDto[id = "+vo.getId()+"]信息不存在");
				UserCheckVO checkVo = new UserCheckVO(false, ErrorMsgEnum.USER_NULL);
				return checkVo;
			}
			String custId = dto.getCustId();
			BeanUtils.copyProperties(vo, dto);
			dto.setCustId(custId);
			mrsOrganService.updateBaseAndSync(dto);
			return new UserCheckVO(true);
		} catch (Exception e) {
			log.error("更新失败:", e);
			return new UserCheckVO(false, ErrorMsgEnum.UPDATE_FAIL);
		}
	}

	@Override
	public MrsLoginUserDto uploadFile(String custId, List<UploadOrganPojo> uploadPojoList) {	
		// 更新FileId到库
		try {
			UserCheckVO vo = mrsOrganService.updateFileId(custId, uploadPojoList);
			if(!vo.isCheckValue()) {
				log.error("文件保存失败");
				MrsLoginUserDto dto = new MrsLoginUserDto();
				dto.setUserCheckVo(vo);
				return dto ;
			}
			MrsLoginUserDto loginUser = mrsLoginUserService.selectByCustId(custId);
			loginUser.setLoginPwd("");
			loginUser.setPayPwd("");
			// 根据登陆信息获取账户信息
			List<AccountMsg> accountMsgs = mrsAccountService.findOrganAccountMsgByLoginUserId(loginUser.getId());
			if(CollectionUtil.isEmpty(accountMsgs)) {
				log.info("根据登陆ID[id="+loginUser.getId()+"]没有找到账户信息");
				// 二期改造    表示客户只有客户信息没有账户信息
				loginUser.setHasAcnt(false);
			} else {
				loginUser.setHasAcnt(true);
				loginUser.setAccountMsgs(accountMsgs);
				if(StringUtil.isEmpty(loginUser.getMobile())) {
					loginUser.setBindMobile(false);
				}else {
					loginUser.setBindMobile(true);
				}
				if(StringUtil.isEmpty(loginUser.getEmail())) {
					loginUser.setBindEmail(false);
				}else {
					loginUser.setBindEmail(true);
				}
			}
			UserCheckVO checkVo = new UserCheckVO(true);
			loginUser.setUserCheckVo(checkVo);
			return loginUser;
		} catch (Exception e) {
			log.error("文件保存失败：", e);
			UserCheckVO checkVo = new UserCheckVO(false, ErrorMsgEnum.SYSTEM_ERROR);
			MrsLoginUserDto dto = new MrsLoginUserDto();
			dto.setUserCheckVo(checkVo);
			return dto ;
		}
	}
	@Override
	public MrsLoginUserDto uploadOrganFile(String custId,String loginUserId, List<MrsAduitAttachmentDto> mrsAduitAttachmentDtoList) {	
		// 更新FileId到库
		try {
			UserCheckVO vo = mrsOrganService.saveAduitOrganByPortalRest(custId, loginUserId, mrsAduitAttachmentDtoList);
			if(!vo.isCheckValue()) {
				log.error("文件保存失败");
				MrsLoginUserDto dto = new MrsLoginUserDto();
				dto.setUserCheckVo(vo);
				return dto ;
			}
			MrsLoginUserDto loginUser = mrsLoginUserService.selectByCustId(custId);
			loginUser.setLoginPwd("");
			loginUser.setPayPwd("");
			// 根据登陆信息获取账户信息
			List<AccountMsg> accountMsgs = mrsAccountService.findOrganAccountMsgByLoginUserId(loginUser.getId());
			if(CollectionUtil.isEmpty(accountMsgs)) {
				log.info("根据登陆ID[id="+loginUser.getId()+"]没有找到账户信息");
				// 二期改造    表示客户只有客户信息没有账户信息
				loginUser.setHasAcnt(false);
			} else {
				loginUser.setHasAcnt(true);
				loginUser.setAccountMsgs(accountMsgs);
				if(StringUtil.isEmpty(loginUser.getMobile())) {
					loginUser.setBindMobile(false);
				}else {
					loginUser.setBindMobile(true);
				}
				if(StringUtil.isEmpty(loginUser.getEmail())) {
					loginUser.setBindEmail(false);
				}else {
					loginUser.setBindEmail(true);
				}
			}
			
			UserCheckVO checkVo = new UserCheckVO(true);
			loginUser.setUserCheckVo(checkVo);
			return loginUser;
		} catch (Exception e) {
			log.error("文件保存失败：", e);
			UserCheckVO checkVo = new UserCheckVO(false, ErrorMsgEnum.SYSTEM_ERROR);
			MrsLoginUserDto dto = new MrsLoginUserDto();
			dto.setUserCheckVo(checkVo);
			return dto ;
		}
	}
	@Override
	public PageData<MrsOrganDto> findOrganByUpdateAudit(PageData<MrsOrganDto> pageData, MrsOrganDto searchDto) {
		return mrsOrganService.findOrganByUpdateAudit(pageData, searchDto);
	}
	@Override
	public ResponseMessage auditOrgan(String auditType, String auditId, String renson, String loginName) {
		ResponseMessage responseMessage = new ResponseMessage();	
		// 根据审核主键ID查审核信息
		MrsDataAuditChangeDto baseDataAuditChangeDto = mrsDataAuditChangeService.getMrsDataAuditChangeById(auditId);
		if(baseDataAuditChangeDto.getAuditStatus()!=EAuditStatus.AUDIT_WAIT){
			responseMessage.setMessage("审核失败,数据已被其他管理员审核！");
		}else{
			MrsOrganDto organDto = JsonUtil.JsonToObject(baseDataAuditChangeDto.getChangeContent(), MrsOrganDto.class);
			if(EAuditStatus.AUDIT_PASS.getValue().equals(auditType)){//通过
				//更新信息
				mrsOrganService.update(organDto);
				baseDataAuditChangeDto.setAuditStatus(EAuditStatus.AUDIT_PASS);//复核核状态【1-通过】
				//同步数据到客户系统
				mrsOrganService.updateAndSync(organDto);
			}else if(EAuditStatus.AUDIT_FAIL.getValue().equals(auditType)){//拒绝
				baseDataAuditChangeDto.setAuditStatus(EAuditStatus.AUDIT_FAIL);//复核核状态【2-拒绝】
				baseDataAuditChangeDto.setAuditReason(renson);
			}
			baseDataAuditChangeDto.setAuditUser(loginName);//复核用户
			baseDataAuditChangeDto.setAuditTime(new Date());//复核时间
			//更新复核信息
			mrsDataAuditChangeService.updateBaseDataAuditChange(baseDataAuditChangeDto);
			responseMessage.setMessage(baseDataAuditChangeDto.getChangeType().getDisplayName()+"操作审核成功！");
			responseMessage.setSuccess(true);
		}
		return responseMessage;
	
	}

	@Override
	public void updateAndSync(MrsOrganDto dto) {
		//同步数据到客户系统
		mrsOrganService.updatePicAndSync(dto);
	}

	@Override
	public int saveMrsUserAccountDto(MrsUserAccountDto mrsUserAccountDto) {
		return mrsOrganService.saveMrsUserAccountDto(mrsUserAccountDto);
	}

	@Override
	public SaveAduitPersonResponseVo saveAduitOrgan(MrsOrganVo mrsOrganVo) {
		return mrsOrganService.saveAduitOrgan(mrsOrganVo);
	}

	@Override
	public SaveAduitPersonResponseVo doUpdateSaveAduit(MrsOrganVo vo) {
		return mrsOrganService.doUpdateSaveAduit(vo);
	}

	@Override
	public SaveAduitProductResponseVo doUpdateAccountStatus(MrsActAccountVo mrsActAccountVo) {
		return mrsOrganService.doUpdateAccountStatus(mrsActAccountVo);
	}

	@Override
	public void updateByPrimaryKey(MrsOrganDto dto) {
		mrsOrganService.updateByPrimaryKey(dto);
	}

	@Override
	public void updateBaseAndSync(MrsOrganDto dto) {
		mrsOrganService.updateBaseAndSync(dto);
	}
	
	@Override
	public void updateBaseFileAndSync(MrsOrganDto dto) {
		mrsOrganService.updateBaseFileAndSync(dto);
	}

	@Override
	public boolean checkOrgan3ElmentIsMax(String customerName, String socialCreditCode, String organizeCode,
			String revenueCode, String businessLicence, String organOtherCode) {
		
		return mrsOrganService.checkOrgan3ElmentIsMax(customerName,socialCreditCode,
				organizeCode,revenueCode,businessLicence,organOtherCode);
	}

	@Override
	public List<MrsOrganDto> findBy3ElementNoEff(String name, String socialCreditCode, String organizeCode,
			String revenueCode, String businessLicence, String organOtherCode) {
		return mrsOrganService.findBy3ElementNoEff(name, socialCreditCode, organizeCode,
				revenueCode, businessLicence, organOtherCode);
	}

	@Override
	public boolean checkOrgan3ElmentUpdateIsMax(String customerName, String socialCreditCode, String organizeCode,
			String revenueCode, String businessLicence, String organOtherCode, String custId) {
		return mrsOrganService.checkOrgan3ElmentUpdateIsMax(customerName, socialCreditCode,
				organizeCode, revenueCode, businessLicence, organOtherCode, custId);
	}

	@Override
	public SaveAduitProductResponseVo removeSaveAduit(MrsRemoveAccountVo removeAccountVo) {
		return mrsOrganService.removeSaveAduit(removeAccountVo);
	}
	
}
