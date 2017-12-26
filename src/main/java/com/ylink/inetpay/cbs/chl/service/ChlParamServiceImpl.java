package com.ylink.inetpay.cbs.chl.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.ylink.inetpay.cbs.chl.dao.ChlParamMapper;
import com.ylink.inetpay.cbs.ucs.sec.service.UcsSecUserServiceImpl;
import com.ylink.inetpay.common.core.constant.EChlChannelCode;
import com.ylink.inetpay.common.core.util.ExceptionProcUtil;
import com.ylink.inetpay.common.project.cbs.exception.CbsUncheckedException;
import com.ylink.inetpay.common.project.cbs.exception.ECbsErrorCode;
import com.ylink.inetpay.common.project.channel.app.ChlParamAppService;
import com.ylink.inetpay.common.project.channel.dto.TbChlParamDto;
import com.ylink.inetpay.common.project.channel.exception.ChannelCheckedException;
@Service("chlParamService")
public class ChlParamServiceImpl implements ChlParamService {
	@Autowired
	private ChlParamMapper chlParamMapper;
	@Autowired
	private ChlParamAppService chlsystemParamAppService;
	private static Logger _log = LoggerFactory.getLogger(UcsSecUserServiceImpl.class);
	@Override
	public PageData<TbChlParamDto> findListPage(
			PageData<TbChlParamDto> pageDate, TbChlParamDto chlParamDto) {
		PageHelper.startPage(pageDate.getPageNumber(),pageDate.getPageSize());
		List<TbChlParamDto> findListPage=chlParamMapper.list(chlParamDto);
		Page<TbChlParamDto> page=(Page<TbChlParamDto>) findListPage;
		pageDate.setTotal(page.getTotal());
		pageDate.setRows(findListPage);
		return pageDate;
	}


	public TbChlParamDto selectByChannelCodeAndParam(String channelCode,String paramCode){
		TbChlParamDto chlParamDto = new TbChlParamDto();
		chlParamDto.setChannelCode(EChlChannelCode.parseOf(channelCode));
		chlParamDto.setParamCode(paramCode);
		return chlParamMapper.selectByChannelCodeAndParam(chlParamDto);
	}
	
	@Override
	public TbChlParamDto details(String channelParamId) {
		return chlParamMapper.selectByPrimaryKey(channelParamId);
	}

	@Override
	public void updateChannelParam(TbChlParamDto tbChlParamDto) {
		try {
			chlsystemParamAppService.updateByPrimaryKeySelective(tbChlParamDto);
		} catch (ChannelCheckedException e) {
			_log.error("修改渠道信息参数："+e.getMessage());
			throw new CbsUncheckedException(e.getCode(),e.getMessage());
		}catch (Exception e) {
			_log.error("修改渠道信息参数："+ExceptionProcUtil.getExceptionDesc(e));
			throw new CbsUncheckedException(ECbsErrorCode.CHANNEL_SYS_ERROR.getValue(),ECbsErrorCode.CHANNEL_SYS_ERROR.getDisplayName());
		}				
	}


	@Override
	public List<TbChlParamDto> listByChannelCodeAndParam(List<String> channelCodeList, List<String> paramCodeList) {
		return chlParamMapper.listByChannelCodeAndParam(channelCodeList, paramCodeList);
	}
}
