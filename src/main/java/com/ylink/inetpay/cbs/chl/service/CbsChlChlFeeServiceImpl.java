package com.ylink.inetpay.cbs.chl.service;

import java.math.BigDecimal;
import java.util.List;

import org.apache.xpath.operations.Number;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.ylink.inetpay.cbs.chl.dao.ChlChlFeeMapper;
import com.ylink.inetpay.common.core.constant.EStatus;
import com.ylink.inetpay.common.project.cbs.exception.CbsCheckedException;
import com.ylink.inetpay.common.project.channel.app.ChlChlFeeAppService;
import com.ylink.inetpay.common.project.channel.dto.ChlChlFeeDto;
import com.ylink.inetpay.common.project.channel.exception.ChannelCheckedException;
@Service("cbsChlChlFeeService")
public class CbsChlChlFeeServiceImpl implements CbsChlChlFeeService {
	@Autowired
	private ChlChlFeeMapper chlChlFeeMapper;
	@Autowired
	private ChlChlFeeAppService chlChlFeeAppService;
	@Override
	public PageData<ChlChlFeeDto> findAll(PageData<ChlChlFeeDto> pageData, ChlChlFeeDto queryParam) {
		PageHelper.startPage(pageData.getPageNumber(),pageData.getPageSize());
		List<ChlChlFeeDto> findListPage=chlChlFeeMapper.findAll(queryParam);
		for (ChlChlFeeDto dto : findListPage) {
			if(dto.getFeeType().getValue()=="1"){
				dto.setFeeValue(dto.getFeeValue().multiply(new BigDecimal("100")));
			}
		}
		Page<ChlChlFeeDto> page=(Page<ChlChlFeeDto>) findListPage;
		pageData.setTotal(page.getTotal());
		pageData.setRows(findListPage);
		return pageData;
	}

	@Override
	public int saveTemplate(ChlChlFeeDto template) throws CbsCheckedException {
		try {
			return chlChlFeeAppService.saveTemplate(template);
		} catch (ChannelCheckedException e) {
			throw new CbsCheckedException("",e.getMessage());
		}catch (Exception e) {
			throw new CbsCheckedException("","调用新增渠道手续费模板接口失败：通讯异常");
		}
	}

	@Override
	public int updateTemplate(ChlChlFeeDto tenplate) throws CbsCheckedException {
		try {
			return chlChlFeeAppService.updateTemplate(tenplate);
		} catch (ChannelCheckedException e) {
			throw new CbsCheckedException("",e.getMessage());
		}catch (Exception e) {
			throw new CbsCheckedException("","调用修改渠道手续费模板接口失败：通讯异常");
		}
	}

	@Override
	public int deleteTemplate(String id) throws CbsCheckedException {
		try {
			return chlChlFeeAppService.deleteTemplate(id);
		} catch (ChannelCheckedException e) {
			throw new CbsCheckedException("",e.getMessage());
		}catch (Exception e) {
			throw new CbsCheckedException("","调用删除渠道手续费模板接口失败：通讯异常");
		}
	}

	@Override
	public int blockUp(EStatus status, String id) throws CbsCheckedException{
		try {
			return chlChlFeeAppService.blockUp(status, id);
		} catch (ChannelCheckedException e) {
			throw new CbsCheckedException("",e.getMessage());
		}catch (Exception e) {
			throw new CbsCheckedException("","调用启用/停用渠道手续费模板接口失败：通讯异常");
		}
	}

	@Override
	public ChlChlFeeDto findById(String id) {
		return chlChlFeeMapper.selectByPrimaryKey(id);
	}

	@Override
	public List<ChlChlFeeDto> queryAllChannels() throws CbsCheckedException {
		return chlChlFeeMapper.queryAllChannels();
	}
	
}
