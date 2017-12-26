package com.ylink.inetpay.cbs.chl.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.exception.BusinessRuntimeException;
import org.ylinkpay.framework.core.model.PageData;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.ylink.inetpay.cbs.chl.dao.TbChlCardBinMapper;
import com.ylink.inetpay.common.project.channel.app.ChlCardBinAppService;
import com.ylink.inetpay.common.project.channel.dto.TbChlCardBin;
@Service("chlCardBinService")
public class ChlCardBinServiceImpl implements ChlCardBinService {
	
	
	private Logger loger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private TbChlCardBinMapper tbChlCardBinMapper;
	@Autowired
	private ChlCardBinAppService chlCardBinAppService;

	@Override
	public PageData<TbChlCardBin> findListPage(PageData<TbChlCardBin> pageDate, TbChlCardBin tbChlCardBin){
		PageHelper.startPage(pageDate.getPageNumber(),pageDate.getPageSize());
		List<TbChlCardBin> findListPage=tbChlCardBinMapper.findListPage(tbChlCardBin);
		Page<TbChlCardBin> page=(Page<TbChlCardBin>) findListPage;
		pageDate.setTotal(page.getTotal());
		pageDate.setRows(findListPage);
		return pageDate;
	}

	@Override
	public void addCardBin(TbChlCardBin tbChlCardBin) {
		try {
			chlCardBinAppService.addCardBin(tbChlCardBin);
		} catch (Exception e) {
			loger.error("调用渠道新增银行卡bin异常");
			throw new BusinessRuntimeException("调用渠道新增银行卡bin异常");
		}
	}
	
	public void deleteCardBin(String id){
		try {
			chlCardBinAppService.deleteCardBin(id);
		} catch (Exception e) {
			loger.error("调用渠道删除银行卡bin异常");
			throw new BusinessRuntimeException("调用渠道删除银行卡bin异常");
		}
	}
	
	public void updateCardBin(TbChlCardBin tbChlCardBin){
		try {
			chlCardBinAppService.updateCardBin(tbChlCardBin);
		} catch (Exception e) {
			loger.error("调用渠道新增银行卡bin异常");
			throw new BusinessRuntimeException("调用渠道新增银行卡bin异常");
		}
	}

	@Override
	public TbChlCardBin datailCardBin(String id){
		return tbChlCardBinMapper.selectByPrimaryKey(id);
	}

	@Override
	public List<TbChlCardBin> getCardBinByCardSign(String CardSign) {
		return tbChlCardBinMapper.getCardBinByCardSign(CardSign);
	}
	
	
}
