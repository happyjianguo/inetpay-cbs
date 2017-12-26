package com.ylink.inetpay.cbs.bis.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.cbs.bis.service.BisProductService;
import com.ylink.inetpay.common.project.cbs.app.BisProductAppService;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisProductDto;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisProductTradeTypePojoDto;
import com.ylink.inetpay.common.project.cbs.exception.CbsCheckedException;

/**
 * Created by pst25 on 2017/3/29.
 */
@Service("bisProductAppService")
public class BisProductAppServiceImpl implements BisProductAppService {

    @Autowired
    BisProductService bisProductService;

    @Override
    public BisProductDto findById(String id) throws CbsCheckedException {
        return bisProductService.findById(id);
    }

	@Override
	public PageData<BisProductTradeTypePojoDto> pageList(PageData<BisProductTradeTypePojoDto> pageData,
			BisProductTradeTypePojoDto queryParam) {
		return bisProductService.pageList(pageData,queryParam);
	}

	@Override
	public BisProductTradeTypePojoDto findProductTradeById(String id,String settleCycleId) {
		return bisProductService.findProductTradeById(id,settleCycleId);
	}

	@Override
	public void save(BisProductDto bisProductDto) {
		bisProductService.save(bisProductDto);
	}

	@Override
	public void update(BisProductTradeTypePojoDto productDto) {
		bisProductService.update(productDto);
	}

	@Override
	public BisProductDto findByProductCode(String productCode) {
		return bisProductService.findByProductCode(productCode);
	}
}
