package com.ylink.inetpay.cbs.act.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.ylink.inetpay.cbs.act.dao.ActBookDtoMapper;
import com.ylink.inetpay.common.core.dto.ReporHeadDto;
import com.ylink.inetpay.common.project.account.dto.ActBookDto;

@Service("actBookService")
public class ActBookServiceImpl implements ActBookService {
	@Autowired
	ActBookDtoMapper actBookDtoMapper;

	@Override
	public PageData<ActBookDto> queryAllData(PageData<ActBookDto> pageDate,
			ActBookDto actBookDto) {

		PageHelper.startPage(pageDate.getPageNumber(), pageDate.getPageSize());
		List<ActBookDto> list = actBookDtoMapper.queryAllData(actBookDto);
		Page<ActBookDto> page = (Page<ActBookDto>) list;
		pageDate.setTotal(page.getTotal());
		pageDate.setRows(list);
		return pageDate;
	}

	@Override
	public ActBookDto selectByBookId(String bookId) {
		return actBookDtoMapper.selectByBookId(bookId);
	}
	/**
	 * 根据PAYID查询
	 * 
	 * @param accountId
	 * @return
	 */
	@Override
	public ActBookDto selectByPayId(String payId){
		return actBookDtoMapper.selectByPayId(payId);
	}
	@Override
	public ReporHeadDto reportSumData(ActBookDto actBookDto) {
		return actBookDtoMapper.reportSumData(actBookDto);
	}

}
