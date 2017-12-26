package com.ylink.inetpay.cbs.mrs.App;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ylink.inetpay.cbs.mrs.service.MrsAduitContentService;
import com.ylink.inetpay.common.project.cbs.app.MrsAduitContentAppService;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsAduitContentDtoWithBLOBs;
@Service("mrsAduitContentAppService")
public class MrsAduitContentAppServiceImpl implements MrsAduitContentAppService {

	@Autowired
	MrsAduitContentService mrsAduitContentService;
	
	
	@Override
	public List<MrsAduitContentDtoWithBLOBs> selectByAuditId(String id) {
		return mrsAduitContentService.selectByAuditId(id);
	}

	@Override
	public void saveMrsAduitContentDto(MrsAduitContentDtoWithBLOBs dto) {
		mrsAduitContentService.saveMrsAduitContentDto(dto);
	}

	@Override
	public void deleteByAduitId(String id) {
		mrsAduitContentService.deleteByAduitId(id);
	}

}
