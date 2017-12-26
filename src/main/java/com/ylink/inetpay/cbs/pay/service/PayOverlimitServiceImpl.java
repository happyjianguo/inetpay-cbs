package com.ylink.inetpay.cbs.pay.service;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.ylink.inetpay.cbs.pay.dao.PayOverlimitDtoMapper;
import com.ylink.inetpay.common.core.constant.EPayOverrBusinessType;
import com.ylink.inetpay.common.project.pay.dto.PayOverlimitDto;
@Service("payOverlimitService")
public class PayOverlimitServiceImpl implements PayOverlimitService {
	@Autowired
	private PayOverlimitDtoMapper payOverlimitDtoMapper;

	@Override
	public PageData<PayOverlimitDto> singleOverLimitList(
			PageData<PayOverlimitDto> pageDate, PayOverlimitDto payOverlimitDto){
		PageHelper.startPage(pageDate.getPageNumber(), pageDate.getPageSize());
		List<PayOverlimitDto> findListPage=null;
		findListPage=payOverlimitDtoMapper.limitList(payOverlimitDto,EPayOverrBusinessType.SINGLE_LIMIT,EPayOverrBusinessType.SINGLE_WARNING);
		/*if(payOverlimitDto.getOverType()==null){
			findListPage=payOverlimitDtoMapper.list(payOverlimitDto);
		}else{
			findListPage=payOverlimitDtoMapper.limitList(payOverlimitDto,EPayOverrBusinessType.SINGLE_LIMIT,EPayOverrBusinessType.SINGLE_WARNING);
			
		}*/
		Page<PayOverlimitDto> page=(Page<PayOverlimitDto>) findListPage;
		pageDate.setTotal(page.getTotal());
		pageDate.setRows(findListPage);
		return pageDate;
	}
	@Override
	public PageData<PayOverlimitDto> totalOverLimitList(
			PageData<PayOverlimitDto> pageDate, PayOverlimitDto payOverlimitDto){
		PageHelper.startPage(pageDate.getPageNumber(), pageDate.getPageSize());
		List<PayOverlimitDto> findListPage=null;
		findListPage=payOverlimitDtoMapper.limitList(payOverlimitDto,EPayOverrBusinessType.CUMULATIVE_LIMIT,EPayOverrBusinessType.CUMULATIVE_WARNING);
		/*if(payOverlimitDto.getOverType()==null){
			findListPage=payOverlimitDtoMapper.list(payOverlimitDto);
		}else{
			findListPage=payOverlimitDtoMapper.limitList(payOverlimitDto,EPayOverrBusinessType.CUMULATIVE_LIMIT,EPayOverrBusinessType.CUMULATIVE_WARNING);
		}*/
		Page<PayOverlimitDto> page=(Page<PayOverlimitDto>) findListPage;
		pageDate.setTotal(page.getTotal());
		pageDate.setRows(findListPage);
		return pageDate;
	}

	@Override
	public PayOverlimitDto details(String id) {
		return payOverlimitDtoMapper.selectByPrimaryKey(id);
	}
}
