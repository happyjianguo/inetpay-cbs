package com.ylink.inetpay.cbs.bis.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.ylinkpay.framework.core.model.PageData;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.ylink.inetpay.cbs.bis.dao.BisEmailAdministratorDtoMapper;
import com.ylink.inetpay.cbs.common.CbsConstants;
import com.ylink.inetpay.common.project.cbs.constant.bis.EMessageType;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisEmailAdministratorDto;
import com.ylink.inetpay.common.project.cbs.exception.CbsUncheckedException;
 
@Service("bisEmailAdministratorService")
@Transactional(value=CbsConstants.TX_MANAGER_BIS)
public class BisEmailAdministratorServiceImpl implements BisEmailAdministratorService {
	@Autowired
	private BisEmailAdministratorDtoMapper bisEmailAdministratorDtoMapper;

	@Override
	public PageData<BisEmailAdministratorDto> pageList(PageData<BisEmailAdministratorDto> pageData,
			BisEmailAdministratorDto queryParam) {
		if(StringUtils.isNotBlank(queryParam.getUserName())){
			List<BisEmailAdministratorDto> dtos = bisEmailAdministratorDtoMapper.findByUserName(queryParam.getUserName());
			setMessageTypes(queryParam,dtos);
		}
		if(StringUtils.isNotBlank(queryParam.getEmail())){
			List<BisEmailAdministratorDto> dtos = bisEmailAdministratorDtoMapper.findByEmail(queryParam.getEmail());
			setMessageTypes(queryParam,dtos);
		}
		PageHelper.startPage(pageData.getPageNumber(), pageData.getPageSize());
		List<BisEmailAdministratorDto> list = bisEmailAdministratorDtoMapper.list(queryParam);
		if(list!=null && !list.isEmpty()){
			//将多条记录按照类型封装到一个对象中去
			HashMap<EMessageType, BisEmailAdministratorDto> emailAdministratorMap = new HashMap<EMessageType,BisEmailAdministratorDto>();
			for (BisEmailAdministratorDto dto : list) {
				BisEmailAdministratorDto mapDto = emailAdministratorMap.get(dto.getMessageType());
				if(mapDto!=null){
					mapDto.setEmailUserNames(mapDto.getEmailUserNames()+dto.getUserName()+"("+dto.getEmail()+");");
				}else{
					mapDto=new BisEmailAdministratorDto();
					mapDto.setMessageType(dto.getMessageType());
					mapDto.setEmailUserNames(dto.getUserName()+"("+dto.getEmail()+");");
					emailAdministratorMap.put(dto.getMessageType(), mapDto);
				}
			}
			//移除原先的记录
			list.clear();
			Set<EMessageType> keySet = emailAdministratorMap.keySet();
			for (EMessageType eMessageType : keySet) {
				list.add(emailAdministratorMap.get(eMessageType));
			}
		}
		Page<BisEmailAdministratorDto> pageList = (Page<BisEmailAdministratorDto> )list;
		pageData.setRows(pageList);
		pageData.setTotal(pageList.getTotal());
		return pageData;
	}
	
	/**
	 * @param queryParam
	 * @param dtos
	 */
	public void setMessageTypes(BisEmailAdministratorDto queryParam,List<BisEmailAdministratorDto> dtos){
		if(dtos!=null && !dtos.isEmpty()){
			ArrayList<EMessageType> messageTypes = new ArrayList<EMessageType>();
			HashMap<EMessageType, EMessageType> messageTypeMap = new HashMap<EMessageType,EMessageType>();
			for (BisEmailAdministratorDto dto : dtos) {
				EMessageType messageType = dto.getMessageType();
				EMessageType messageTypeValue = messageTypeMap.get(messageType);
				if(messageTypeValue==null){
					messageTypes.add(messageType);
					messageTypeMap.put(messageType, messageType);
				}
			}
			if(!messageTypes.isEmpty()){
				List<EMessageType> dtoMessageTypes = queryParam.getMessageTypes();
				if(dtoMessageTypes!=null && !dtoMessageTypes.isEmpty()){
					dtoMessageTypes.addAll(messageTypes);
				}else{
					queryParam.setMessageTypes(messageTypes);
				}
			}
		}
	}

	@Override
	public long save(BisEmailAdministratorDto dto) {
		return bisEmailAdministratorDtoMapper.insert(dto);
	}

	@Override
	public BisEmailAdministratorDto view(String id) {
		return bisEmailAdministratorDtoMapper.selectByPrimaryKey(id);
	}

	@Override
	public long delete(String id) {
		return bisEmailAdministratorDtoMapper.deleteByPrimaryKey(id);
	}

	@Override
	public long update(BisEmailAdministratorDto dto) {
		return bisEmailAdministratorDtoMapper.updateByPrimaryKey(dto);
	}

	@Override
	public long updateByMessageType(EMessageType messageType, List<BisEmailAdministratorDto> dtos) {
		bisEmailAdministratorDtoMapper.deleteByMessageType(messageType);
		return bisEmailAdministratorDtoMapper.batchSave(dtos);
	}

	@Override
	public long batchSaveByMessageType(EMessageType messageType, List<BisEmailAdministratorDto> dtos) {
		long userNum=bisEmailAdministratorDtoMapper.isExistByMessageType(messageType);
		if(userNum>0){
			throw new CbsUncheckedException("","信息类型【"+messageType.getDisplayName()+"】已存在不能重复新增");
		}
		return bisEmailAdministratorDtoMapper.batchSave(dtos);
	}

	@Override
	public long deleteByMessageType(EMessageType messageType) {
		return bisEmailAdministratorDtoMapper.deleteByMessageType(messageType);
	}

	@Override
	public List<BisEmailAdministratorDto> viewByMessageType(EMessageType messageType) {
		return bisEmailAdministratorDtoMapper.viewByMessageType(messageType);
	}
}

