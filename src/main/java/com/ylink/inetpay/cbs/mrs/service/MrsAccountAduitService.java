package com.ylink.inetpay.cbs.mrs.service;


import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsAccountAduitInfoDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsToJsonByUserDto;
import com.ylink.inetpay.common.project.cbs.dto.ucs.sec.UcsSecUserDto;
import com.ylink.inetpay.common.project.cbs.exception.CbsCheckedException;
import com.ylink.inetpay.common.project.cbs.vo.mrs.SaveAduitPersonResponseVo;

public interface MrsAccountAduitService {
	
	public void saveAfAduit(String type, UcsSecUserDto currentUser, MrsAccountAduitInfoDto mrsAccountAduitInfoDto,
			String aduitId, String remark) throws Exception;
	
	/**
	 *方法描述：保存修改用户送审信息，转为JSON存储在审核信息里面
	 * @author dxd
	 * @param 
	 */
	public SaveAduitPersonResponseVo saveUpdateUser(String id,MrsToJsonByUserDto mrsToJsonByUserDto);
	
	/**
	 * 
	 *方法描述：保存个人用户送审信息，转为JSON存储在审核信息里面
	 * @param personVo
	 */
	public SaveAduitPersonResponseVo saveAduitUser(MrsToJsonByUserDto userVo);
	
	public void rbAduit(UcsSecUserDto currentUser, MrsAccountAduitInfoDto mrsAccountAduitInfoDto, String aduitId,
			String remark);
	
	public void updateAfAduit(String id, String remark, MrsAccountAduitInfoDto mrsAccountAduitInfoDto,
			UcsSecUserDto currentUser) throws CbsCheckedException ;

}
