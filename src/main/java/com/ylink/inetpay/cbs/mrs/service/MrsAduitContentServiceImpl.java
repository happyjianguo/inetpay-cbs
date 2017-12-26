package com.ylink.inetpay.cbs.mrs.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ylink.inetpay.cbs.mrs.dao.MrsAduitContentDtoMapper;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsAduitContentDtoWithBLOBs;
@Service("mrsAduitContentService")
public class MrsAduitContentServiceImpl implements MrsAduitContentService {

	@Autowired
	MrsAduitContentDtoMapper mrsAduitContentDtoMapper;
	
	@Override
	public List<MrsAduitContentDtoWithBLOBs> selectByAuditId(String id) {
		return mrsAduitContentDtoMapper.selectByAuditId(id);
	}

	@Override
	public void saveMrsAduitContentDto(MrsAduitContentDtoWithBLOBs dto) {
		mrsAduitContentDtoMapper.insertSelective(dto);
	}

	@Override
	public void deleteByAduitId(String id) {
		mrsAduitContentDtoMapper.deleteByAduitId(id);
	}

}
