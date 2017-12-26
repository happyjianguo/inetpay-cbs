package com.ylink.inetpay.cbs.mrs.App;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.cbs.mrs.service.MrsAccountAduitService;
import com.ylink.inetpay.common.project.cbs.app.MrsAccountAduitAppService;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsAccountAduitInfoDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsLoginUserDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsToJsonByUserDto;
import com.ylink.inetpay.common.project.cbs.dto.ucs.sec.UcsSecUserDto;
import com.ylink.inetpay.common.project.cbs.exception.CbsCheckedException;
import com.ylink.inetpay.common.project.cbs.vo.mrs.SaveAduitPersonResponseVo;

@Service("mrsAccountAduitAppService")
public class MrsAccountAduitAppServiceImpl implements MrsAccountAduitAppService {
	
	@Autowired
	private MrsAccountAduitService mrsAccountAduitService;

	@Override
	public void saveAfAduit(String type, UcsSecUserDto currentUser, MrsAccountAduitInfoDto mrsAccountAduitInfoDto,
			String aduitId, String remark) throws Exception {
		mrsAccountAduitService.saveAfAduit(type, currentUser, mrsAccountAduitInfoDto, aduitId, remark);
		
	}

	@Override
	public SaveAduitPersonResponseVo saveAduitUser(MrsToJsonByUserDto userVo) {
		return mrsAccountAduitService.saveAduitUser(userVo);
	}

	@Override
	public void rbAduit(UcsSecUserDto currentUser, MrsAccountAduitInfoDto mrsAccountAduitInfoDto, String aduitId,
			String remark) {
		mrsAccountAduitService.rbAduit(currentUser,mrsAccountAduitInfoDto,aduitId,remark);
	}

	@Override
	public SaveAduitPersonResponseVo saveUpdateUser(String id,MrsToJsonByUserDto personVo) {
		return mrsAccountAduitService.saveUpdateUser(id,personVo);
	}

	@Override
	public void updateAfAduit(String id, String remark, MrsAccountAduitInfoDto mrsAccountAduitInfoDto,
			UcsSecUserDto currentUser) throws CbsCheckedException {
		mrsAccountAduitService.updateAfAduit(id, remark, mrsAccountAduitInfoDto,currentUser);
	}

}
