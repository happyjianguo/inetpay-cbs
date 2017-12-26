package com.ylink.inetpay.cbs.mrs.service;


import java.util.List;

import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.common.core.constant.EAduitTypeEnum;
import com.ylink.inetpay.common.core.constant.MrsAccountAuthStatus;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsAduitAttachmentDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsAduitInfoDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsAduitPersonDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsAduitPersonHisDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsToJsonDto;
import com.ylink.inetpay.common.project.cbs.dto.ucs.sec.UcsSecUserDto;
import com.ylink.inetpay.common.project.cbs.exception.CbsCheckedException;
import com.ylink.inetpay.common.project.cbs.exception.CbsUncheckedException;
import com.ylink.inetpay.common.project.cbs.vo.mrs.MrsAttachmentVo;
import com.ylink.inetpay.common.project.cbs.vo.mrs.MrsSubAccountVo;
import com.ylink.inetpay.common.project.cbs.vo.mrs.SaveAduitProductResponseVo;

public interface MrsAduitService {
	
	PageData<MrsAduitInfoDto> findListPage(PageData<MrsAduitInfoDto> pageData,
			MrsAduitInfoDto dto);
    
	public MrsAduitInfoDto selectByPrimaryKey(String id);
	
	public void deleteByPrimaryKey(String id);
	
	void updateByPrimaryKey(MrsAduitInfoDto record);

	
	/**
	 * 根据审核信息主键查询审核主要信息表
	 */
	public PageData<MrsAduitInfoDto> getByCont(PageData<MrsAduitInfoDto> pageData,MrsAduitInfoDto queryParam);
	
	/**
	 * 保存审记录信息表
	 */
	int insertSelective(MrsAduitPersonHisDto dto);
	
	int updateByPrimaryKeySelective(MrsAduitInfoDto mrsAduitInfoDto) ;
	
	MrsAduitPersonDto selectByAduitId(String aduitId,String aduitUserId);
	
	int updateAduitPerson(MrsAduitPersonDto mrsAduitPersonDto);
	
	/**
	 * 审核完成后进行审核人表、审核记录表、审核信息表的更新
	 */
	public MrsToJsonDto saveAfAduit(String type,UcsSecUserDto currentUser,MrsAduitInfoDto mrsAduitInfoDto,EAduitTypeEnum eAduitTypeEnum,String aduitId,String remark) throws CbsCheckedException;
	
	public MrsToJsonDto updateAfAduit(String type, String id, String remark,MrsAduitInfoDto mrsAduitInfoDto,UcsSecUserDto currentUser) throws CbsUncheckedException;
	public void rbAduit(UcsSecUserDto currentUser,MrsAduitInfoDto mrsAduitInfoDto, String aduitId,String remark);
	
	public List<String> findMrsAduitAttachmentDtoByAduitId(String aduitId);
	
	public List<MrsAttachmentVo> findFiled(String aduitId, List<MrsAduitAttachmentDto> mrsAduitAttachmentDtoList);
	
	public MrsToJsonDto approveAduit(MrsAduitInfoDto mrsAduitInfoDto, MrsAccountAuthStatus mrsAccountAuthStatus,UcsSecUserDto currentUser,String remark)
			throws CbsCheckedException;
	
	public void updateActAccountStatus(String type, String id, String remark, MrsAduitInfoDto mrsAduitInfoDto,
			UcsSecUserDto currentUser) throws CbsCheckedException;
	
	public void removeStatus(String type, String id, String remark, MrsAduitInfoDto mrsAduitInfoDto,
			UcsSecUserDto currentUser) throws CbsCheckedException;
	
	public MrsToJsonDto approveAduitAndSave(UcsSecUserDto currentUser, MrsAduitInfoDto mrsAduitInfoDto,
			String aduitId, String remark) throws CbsCheckedException ;
	/**
	 * 
	 *方法描述：根据审核主键，登录用户编号校验该用户是否已经审核过该条数据
	 * 创建人：ydx
	 * 创建时间：2017年3月24日 下午9:49:39
	 * @param aduitId
	 * @param loginId
	 * @return
	 */
	boolean checkDataIsAduit(String aduitId, String loginId ) throws CbsCheckedException;
    
	/**
	 * 子账户信息变更
	 * @param mrsSubAccountVo
	 * @return
	 */
	public SaveAduitProductResponseVo doUpdataSubAccountStatus(MrsSubAccountVo mrsSubAccountVo,String flag);
    
	/**
	 * 子账户信息变更审核
	 * @param type
	 * @param id
	 * @param remark
	 * @param mrsAduitInfoDto
	 * @param currentUser
	 * @throws CbsCheckedException
	 */
	public void updateSubAccountStatus(String type, String id, String remark, MrsAduitInfoDto mrsAduitInfoDto,
			UcsSecUserDto currentUser) throws CbsCheckedException;
}

