package com.ylink.inetpay.cbs.mrs.App;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ylink.inetpay.cbs.mrs.service.MrsUserPayPasswordService;
import com.ylink.inetpay.common.project.cbs.app.MrsUserPayPasswordAppService;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsUserPayPasswordDto;
import com.ylink.inetpay.common.project.portal.vo.RespCheckVO;
import com.ylink.inetpay.common.project.portal.vo.safe.EditPayPwdVo;

@Service("mrsUserPayPasswordAppService")
public class MrsUserPayPasswordAppServiceImpl implements MrsUserPayPasswordAppService{
	
	@Autowired
	private MrsUserPayPasswordService mrsUserPayPasswordService;
	
	@Override
	public MrsUserPayPasswordDto findByCustId(String custId) {
		return mrsUserPayPasswordService.findByCustId(custId);
	}

	@Override
	public RespCheckVO startPwd(String Ip,String name,MrsUserPayPasswordDto record) {
		return mrsUserPayPasswordService.startPwd(Ip, name, record);
	}

	@Override
	public RespCheckVO editPayPwd(String Name,String Ip, String custId, EditPayPwdVo vo) {
		return mrsUserPayPasswordService.editPayPwd(Name, Ip, custId, vo);
		
	}

	@Override
	public RespCheckVO resetPwd(String Ip, String name, MrsUserPayPasswordDto record) {
		return mrsUserPayPasswordService.resetPwd(Ip, name, record);
		
	}

}
