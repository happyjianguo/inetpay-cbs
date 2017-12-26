 package com.ylink.inetpay.cbs.bis.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.ylink.inetpay.cbs.bis.dao.BisBusinessParamDtoMapper;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisBusinessParamDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.ylink.inetpay.cbs.bis.dao.BisBusinessDtoMapper;
import com.ylink.inetpay.common.core.constant.MrsBusiStatus;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisBusinessDto;
import com.ylink.inetpay.common.project.cbs.vo.bis.BisBusinessVO;
import com.ylink.inetpay.common.project.clear.constant.BusinessParamKeyConstants;

@Service("bisBusinessService")
public class BisBusinessServiceImpl implements BisBusinessService {

	@Autowired
	private BisBusinessDtoMapper busiMapper;
	@Autowired
	private BisBusinessParamDtoMapper businessParamDtoMapper;
	@Autowired
	private BisBusinessParamDtoMapper bisBusinessParamDtoMapper;
	
	@Override
	public PageData<BisBusinessDto> getBisBusiness(PageData<BisBusinessDto> pageData, BisBusinessVO businessVo) {
		PageHelper.startPage(pageData.getPageNumber(), pageData.getPageSize());
		List<BisBusinessDto> list=busiMapper.list(businessVo);
		Page<BisBusinessDto> page = (Page<BisBusinessDto>) list;
		pageData.setTotal(page.getTotal());
		pageData.setRows(list);
		return pageData;
	}

	@Override
	public BisBusinessDto findById(String id) {
		return busiMapper.selectByPrimaryKey(id);
	}

	@Override
	public void addBusinessDto(BisBusinessDto dto) {
		busiMapper.insert(dto);
		//保存业务设置参数
		//BisBusinessParamDto
		List<BisBusinessParamDto> listByBusiCode = bisBusinessParamDtoMapper.listByBusiCode(dto.getBusinessCode());
		if(listByBusiCode!=null && !listByBusiCode.isEmpty()){
			return ;
		}
		BusinessParamKeyConstants[] params = BusinessParamKeyConstants.getDefaults();
		ArrayList<BisBusinessParamDto> BisBusinessParamDtoList = new ArrayList<BisBusinessParamDto>();
		for (int i = 0; i < params.length; i++) {
			BusinessParamKeyConstants param = params[i];
			BisBusinessParamDto bisBusinessParamDto = new BisBusinessParamDto();
			bisBusinessParamDto.setId(UUID.randomUUID().toString());
			bisBusinessParamDto.setBusinessCode(dto.getBusinessCode());
			bisBusinessParamDto.setParamCode(param.getValue());
			bisBusinessParamDto.setParamValue(null);
			bisBusinessParamDto.setRemark(param.getDisplayName());
			bisBusinessParamDto.setNorder((short)i);
			BisBusinessParamDtoList.add(bisBusinessParamDto);
		}
		bisBusinessParamDtoMapper.batchSave(BisBusinessParamDtoList);
	}

	@Override
	public void deleteById(String id) {
		busiMapper.deleteByPrimaryKey(id);
	}

	@Override
	public void updateBusinessDto(BisBusinessDto dto) {
		busiMapper.updateByPrimaryKey(dto);
	}

	@Override
	public List<BisBusinessDto> find(String businessCode, String payeeCode, String status) {
		return busiMapper.find(businessCode, payeeCode, status);
	}

	@Override
	public List<BisBusinessParamDto> listBusiParamsByBusiCode(String busiCode) {
		return businessParamDtoMapper.listByBusiCode(busiCode);
	}

	@Override
	public List<BisBusinessDto> findBusinessByaccessorCode(String id) {
		 
		return busiMapper.queryAccessorCode(id);
	}

	@Override
	public List<BisBusinessParamDto> findBusinessParamsByBusinessCode(String businessCode) {
		return bisBusinessParamDtoMapper.listByBusiCode(businessCode);
	}

	@Override
	public long batchUpdateParam(ArrayList<BisBusinessParamDto> params) {
		long num=0;
		for (BisBusinessParamDto bisBusinessParamDto : params) {
			num=num+bisBusinessParamDtoMapper.updateBusinessParam(bisBusinessParamDto);
		}
		return num;
	}

	@Override
	public List<BisBusinessDto> list() {
		return busiMapper.list(new BisBusinessVO());
	}

	@Override
	public List<BisBusinessDto> findAllBusiness() {
		BisBusinessVO vo = new BisBusinessVO();
		vo.setStatus(MrsBusiStatus.STATUS_0.getValue());
		return busiMapper.list(vo);
	}
}
