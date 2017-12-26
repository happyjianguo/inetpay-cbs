package com.ylink.inetpay.cbs.mrs.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ylink.inetpay.cbs.mrs.dao.MrsContactListDtoMapper;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsContactListDto;

@Service("mrsContactListService")
public class MrsContactListServiceImpl implements MrsContactListService {
	
	@Autowired
	private MrsContactListDtoMapper mrsContactListDtoMapper;

	@Override
	public int insert(MrsContactListDto dto) {
		return mrsContactListDtoMapper.insert(dto);
	}

	@Override
	public int updateByPrimaryKey(MrsContactListDto dto) {
		return mrsContactListDtoMapper.updateByPrimaryKey(dto);
	}

	@Override
	public List<MrsContactListDto> findByCustId(String custId) {
		return mrsContactListDtoMapper.findByCustId(custId);
	}

	@Override
	public List<MrsContactListDto> queryAllData(MrsContactListDto param) {
		return mrsContactListDtoMapper.queryAllData(param);
	}

	@Override
	public void deleteByNameAndCustId(String custId, String name) {
		mrsContactListDtoMapper.deleteByNameAndCustId(custId, name);
	}

	@Override
	public MrsContactListDto selectByPrimaryKey(String id) {
		return mrsContactListDtoMapper.selectByPrimaryKey(id);
	}

	@Override
	public void deleteByPrimaryKey(String id) {
		mrsContactListDtoMapper.deleteByPrimaryKey(id);
	}

	@Override
	public MrsContactListDto findByIdandCustId(String id, String custId) {
		return mrsContactListDtoMapper.findByIdandCustId(id, custId);
	}

}
