package com.ylink.inetpay.cbs.chl.app;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.cbs.chl.service.ChlCardBinService;
import com.ylink.inetpay.common.project.cbs.app.CbsChlCardBinAppService;
import com.ylink.inetpay.common.project.cbs.exception.CbsCheckedException;
import com.ylink.inetpay.common.project.channel.dto.TbChlCardBin;
@Service("cbsChlCardBinAppService")
public class CbsChlCardBinAppServiceImpl implements CbsChlCardBinAppService {

	@Autowired
	private ChlCardBinService chlCardBinService;
	
	@Override
	public PageData<TbChlCardBin> findListPage(PageData<TbChlCardBin> pageDate, TbChlCardBin tbChlCardBin)
			throws CbsCheckedException {
		return chlCardBinService.findListPage(pageDate, tbChlCardBin);
	}

	@Override
	public void updateCardBin(TbChlCardBin tbChlCardBin) throws CbsCheckedException {
		chlCardBinService.updateCardBin(tbChlCardBin);
	}
	
	@Override
	public void addCardBin(TbChlCardBin tbChlCardBin)throws CbsCheckedException {
		chlCardBinService.addCardBin(tbChlCardBin);
	}

	@Override
	public void deleteCardBin(String id) throws CbsCheckedException {
		chlCardBinService.deleteCardBin(id);
		
	}

	@Override
	public TbChlCardBin datailCardBin(String id) throws CbsCheckedException {
		return chlCardBinService.datailCardBin(id);
	}

	@Override
	public List<TbChlCardBin> getCardBinByCardSign(String CardSign) {
		return chlCardBinService.getCardBinByCardSign(CardSign);
	}
	
	
}
