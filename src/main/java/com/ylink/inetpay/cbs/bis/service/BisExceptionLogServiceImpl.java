package com.ylink.inetpay.cbs.bis.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.ylinkpay.framework.core.model.PageData;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.shie.constant.SHIEConfigConstant;
import com.ylink.inetpay.cbs.bis.dao.BisEmailAdministratorDtoMapper;
import com.ylink.inetpay.cbs.bis.dao.BisExceptionLogDtoMapper;
import com.ylink.inetpay.cbs.common.CbsConstants;
import com.ylink.inetpay.common.core.constant.EBisEmailTemplateCode;
import com.ylink.inetpay.common.core.constant.EBisExceptionLogNlevel;
import com.ylink.inetpay.common.core.constant.EBisSmsSystem;
import com.ylink.inetpay.common.project.cbs.constant.bis.EIsSend;
import com.ylink.inetpay.common.project.cbs.constant.bis.EMessageType;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisEmailAdministratorDto;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisExceptionLogDto;
@Transactional(value=CbsConstants.TX_MANAGER_BIS)
@Service("bisExceptionLogService")
public class BisExceptionLogServiceImpl implements BisExceptionLogService {
	@Autowired
	private BisExceptionLogDtoMapper bisExceptionLogDtoMapper;
	@Autowired
	private BisEmailService bisEmailService;
	@Autowired
	private BisEmailAdministratorDtoMapper bisEmailAdministratorDtoMapper;
	private static Logger _log = LoggerFactory.getLogger(BisExceptionLogServiceImpl.class);
	@Override
	public PageData<BisExceptionLogDto> findListPage(
			PageData<BisExceptionLogDto> pageDate,
			BisExceptionLogDto bisExceptionLogDto) {
		PageHelper.startPage(pageDate.getPageNumber(),pageDate.getPageSize());
		List<BisExceptionLogDto> findListPage=bisExceptionLogDtoMapper.list(bisExceptionLogDto);
		Page<BisExceptionLogDto> page=(Page<BisExceptionLogDto>) findListPage;
		pageDate.setTotal(page.getTotal());
		pageDate.setRows(findListPage);
		return pageDate;
	}

	@Override
	public void saveLog(BisExceptionLogDto bisExceptionLogDto) {
		bisExceptionLogDto.setCreateTime(new Date());
		bisExceptionLogDto.setId(bisExceptionLogDto.getIdentity());
		if(bisExceptionLogDto.getContent() !=null && bisExceptionLogDto.getContent().length() > 500) {
			bisExceptionLogDto.setContent(bisExceptionLogDto.getContent().substring(0, 500));
		}
		bisExceptionLogDtoMapper.insert(bisExceptionLogDto);
	}

	@Override
	public BisExceptionLogDto details(String id) {
		return bisExceptionLogDtoMapper.selectByPrimaryKey(id);
	}

	@Override
	public void sendExceptionLog() {
		PageHelper.startPage(1,100);
		List<BisExceptionLogDto> exceptionList=bisExceptionLogDtoMapper.findAbleSendExceptionLog(EBisExceptionLogNlevel.ERROR);
		//获取收件人
		List<BisEmailAdministratorDto> exceptionAdministrators = bisEmailAdministratorDtoMapper.viewByMessageType(EMessageType.EXCEPTION_LOG);
		if(exceptionAdministrators!=null && !exceptionAdministrators.isEmpty()){
			for (BisExceptionLogDto bisExceptionLogDto : exceptionList) {
				long num=bisExceptionLogDtoMapper.updateSendStatusById(EIsSend.ALREADY_SEND,bisExceptionLogDto.getId());
				if(num>0){
					for (BisEmailAdministratorDto administratorDto : exceptionAdministrators) {
						HashMap<String, Object> params = new HashMap<String,Object>();
						params.put(SHIEConfigConstant.CURRENT_ADMINISTRATOR, administratorDto.getUserName());
						params.put(SHIEConfigConstant.ERROR_MSG, bisExceptionLogDto.getContent());
						bisEmailService.sendExceptionLogEmail(administratorDto.getEmail(),EBisSmsSystem.CBS,
								EBisEmailTemplateCode.EXCEPTION_LOG, params);
					}
				}
			}
		}else{
			_log.error("异常信息发送短信失败：没有配置异常信息接收人信息");
		}
	}

}
