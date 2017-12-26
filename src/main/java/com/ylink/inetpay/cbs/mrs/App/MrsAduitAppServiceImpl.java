package com.ylink.inetpay.cbs.mrs.App;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.cbs.mrs.service.MrsAduitPersonHisService;
import com.ylink.inetpay.cbs.mrs.service.MrsAduitService;
import com.ylink.inetpay.common.core.constant.EAduitTypeEnum;
import com.ylink.inetpay.common.core.constant.MrsAccountAuthStatus;
import com.ylink.inetpay.common.project.cbs.app.MrsAduitAppService;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsAduitAttachmentDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsAduitInfoDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsAduitPersonDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsAduitPersonHisDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsToJsonDto;
import com.ylink.inetpay.common.project.cbs.dto.ucs.sec.UcsSecUserDto;
import com.ylink.inetpay.common.project.cbs.exception.CbsCheckedException;
import com.ylink.inetpay.common.project.cbs.vo.mrs.MrsAttachmentVo;
import com.ylink.inetpay.common.project.cbs.vo.mrs.MrsSubAccountVo;
import com.ylink.inetpay.common.project.cbs.vo.mrs.SaveAduitProductResponseVo;

@Service("mrsAduitAppService")
public class MrsAduitAppServiceImpl implements MrsAduitAppService{
    
	@Autowired
	private MrsAduitService mrsAduitService;
	@Autowired
    private MrsAduitPersonHisService mrsAduitPersonHisService;
	
	@Override
	public PageData<MrsAduitInfoDto> findListPage(PageData<MrsAduitInfoDto> pageData, MrsAduitInfoDto dto) {
		return mrsAduitService.findListPage(pageData, dto);
	}

	@Override
	public MrsAduitInfoDto selectByPrimaryKey(String id) {
		return mrsAduitService.selectByPrimaryKey(id);
	}

	@Override
	public void deleteByPrimaryKey(String id) {
		mrsAduitService.deleteByPrimaryKey(id);
	}

	@Override
	public PageData<MrsAduitPersonHisDto> findListHisPage(PageData<MrsAduitPersonHisDto> pageData,
			MrsAduitPersonHisDto hisDto) {
		return mrsAduitPersonHisService.findListHisPage(pageData, hisDto);
	}

	@Override
	public PageData<MrsAduitPersonDto> findListPerPage(PageData<MrsAduitPersonDto> pageData, MrsAduitPersonDto perDto) {
		return mrsAduitPersonHisService.findListPerPage(pageData, perDto);
	}


	@Override
	public void save(MrsAduitPersonDto record) {
		mrsAduitPersonHisService.save(record);
	}

	@Override
	public void deletePersonByPrimaryKey(String id) {
		mrsAduitPersonHisService.deleteByPrimaryKey(id);
	}

	@Override
	public void updateByPrimaryKey(MrsAduitPersonDto record) {
		mrsAduitPersonHisService.updateByPrimaryKey(record);
	}

	@Override
	public MrsAduitPersonDto selectPersonByKey(String id) {
		return mrsAduitPersonHisService.selectPersonByKey(id);
	}

	@Override
	public MrsAduitPersonDto selectByAduitUserName(String aduitUserId,String aduitId) {
		return mrsAduitPersonHisService.selectByAduitUserName(aduitUserId,aduitId);
	}

	@Override
	public List<String> getByCurrentUserId(String id) {
		return mrsAduitPersonHisService.getByCurrentUserId(id);
	}

	@Override
	public PageData<MrsAduitInfoDto> getByCont(PageData<MrsAduitInfoDto> pageData,MrsAduitInfoDto queryParam) {
		return mrsAduitService.getByCont(pageData,queryParam);
	}

	@Override
	public List<MrsAduitPersonHisDto> findListHisAll(MrsAduitPersonHisDto hisDto) {
		return mrsAduitPersonHisService.findListHisAll(hisDto);
	}

	@Override
	public int insertSelective(MrsAduitPersonHisDto dto) {
		return mrsAduitService.insertSelective(dto);
	}

	@Override
	public int updateByPrimaryKeySelective(MrsAduitInfoDto mrsAduitInfoDto) {
		return mrsAduitService.updateByPrimaryKeySelective(mrsAduitInfoDto);
	}

	@Override
	public MrsAduitPersonDto selectByAduitId(String aduitId,String aduitUserId) {
		return mrsAduitService.selectByAduitId(aduitId,aduitUserId);
	}

	@Override
	public int updateAduitPerson(MrsAduitPersonDto mrsAduitPersonDto) {
		return mrsAduitService.updateAduitPerson(mrsAduitPersonDto);
	}

	@Override
	public void updateByPrimaryKey(MrsAduitInfoDto record) {
		mrsAduitService.updateByPrimaryKey(record);
	}

	@Override
	public MrsToJsonDto saveAfAduit(String type,UcsSecUserDto currentUser, MrsAduitInfoDto mrsAduitInfoDto,
			EAduitTypeEnum eAduitTypeEnum,String aduitId,String remark) throws CbsCheckedException {
		return mrsAduitService.saveAfAduit(type,currentUser, mrsAduitInfoDto, eAduitTypeEnum, aduitId, remark);
	}

	@Override
	public void rbAduit(UcsSecUserDto currentUser,MrsAduitInfoDto mrsAduitInfoDto, String aduitId,String remark) {
		mrsAduitService.rbAduit(currentUser,mrsAduitInfoDto,aduitId,remark);
	}


	@Override
	public List<String> findMrsAduitAttachmentDtoByAduitId(String aduitId) {
		return mrsAduitService.findMrsAduitAttachmentDtoByAduitId(aduitId);
	}

	@Override
	public List<MrsAttachmentVo> findFiled(String aduitId, List<MrsAduitAttachmentDto> mrsAduitAttachmentDtoList) {
		return mrsAduitService.findFiled(aduitId,mrsAduitAttachmentDtoList);
	}

	@Override
	public MrsToJsonDto approveAduit(MrsAduitInfoDto mrsAduitInfoDto, MrsAccountAuthStatus mrsAccountAuthStatus,UcsSecUserDto currentUser,String remark)
			throws CbsCheckedException {
		 return mrsAduitService.approveAduit(mrsAduitInfoDto,mrsAccountAuthStatus,currentUser,remark);
		
	}

	@Override
	public MrsToJsonDto updateAfAduit(String type, String id,String remark, MrsAduitInfoDto mrsAduitInfoDto,UcsSecUserDto currentUser) throws CbsCheckedException {
		return mrsAduitService.updateAfAduit(type,id,remark,mrsAduitInfoDto,currentUser);
	}

	@Override
	public void updateActAccountStatus(String type, String id, String remark, MrsAduitInfoDto mrsAduitInfoDto,
			UcsSecUserDto currentUser) throws CbsCheckedException {
		mrsAduitService.updateActAccountStatus(type, id, remark, mrsAduitInfoDto, currentUser);
		
	}
	
	@Override
	public void removeStatus(String type, String id, String remark, MrsAduitInfoDto mrsAduitInfoDto,
			UcsSecUserDto currentUser) throws CbsCheckedException {
		mrsAduitService.removeStatus(type, id, remark, mrsAduitInfoDto, currentUser);
		
	}

	@Override
	public List<MrsAduitPersonDto> findByAduitId(String aduitId) {
		return mrsAduitPersonHisService.findByAduitId(aduitId);
	}

	@Override
	public MrsToJsonDto approveAduitAndSave(UcsSecUserDto currentUser, MrsAduitInfoDto mrsAduitInfoDto,
			String aduitId, String remark) throws CbsCheckedException {
		return mrsAduitService.approveAduitAndSave(currentUser,mrsAduitInfoDto,aduitId,remark);
	}

	@Override
	public boolean checkDataIsAduit(String aduitId, String loginId) throws CbsCheckedException {
		return mrsAduitService.checkDataIsAduit(aduitId,loginId);
	}

	@Override
	public SaveAduitProductResponseVo doUpdataSubAccountStatus(MrsSubAccountVo mrsSubAccountVo,String flag) {
		return mrsAduitService.doUpdataSubAccountStatus(mrsSubAccountVo, flag);
	}

	@Override
	public void updateSubAccountStatus(String type, String id, String remark, MrsAduitInfoDto mrsAduitInfoDto,
			UcsSecUserDto currentUser) throws CbsCheckedException {
		mrsAduitService.updateSubAccountStatus(type, id, remark, mrsAduitInfoDto, currentUser);   
	}
	
	
}
