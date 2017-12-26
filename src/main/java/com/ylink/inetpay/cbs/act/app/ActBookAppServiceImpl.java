package com.ylink.inetpay.cbs.act.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.cbs.act.service.ActBookService;
import com.ylink.inetpay.common.core.dto.ReporHeadDto;
import com.ylink.inetpay.common.project.account.dto.ActBookDto;
import com.ylink.inetpay.common.project.cbs.app.CbsActBookAppService;

@Service("cbsActBookAppService")
public class ActBookAppServiceImpl implements CbsActBookAppService {
	@Autowired
	ActBookService actBookService;

	@Override
	public PageData<ActBookDto> queryAllData(PageData<ActBookDto> pageDate,
			ActBookDto ActBookDto) {
		return actBookService.queryAllData(pageDate, ActBookDto);
	}

	@Override
	public ActBookDto selectByBookId(String bookId) {
		return actBookService.selectByBookId(bookId);
	}
 
	@Override
	public ActBookDto selectByPayId(String payId){
		return actBookService.selectByPayId(payId);
	}
	
	@Override
	public ReporHeadDto reportSumData(ActBookDto actBookDto) {
		//所有总笔数和总金额
		ReporHeadDto reporAllDto = new ReporHeadDto();
		reporAllDto = actBookService.reportSumData(actBookDto);
		return reporAllDto;
	}
}
