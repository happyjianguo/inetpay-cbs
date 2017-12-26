package com.ylink.inetpay.cbs.bis.app;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.cbs.bis.service.BisSysParamService;
import com.ylink.inetpay.common.project.cbs.app.BisSysParamAppService;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisSysParamDto;
import com.ylink.inetpay.common.project.cbs.exception.CbsCheckedException;
@Service("bisSysParamAppService")
public class BisSysParamAppServiceImpl implements BisSysParamAppService {
	@Resource
	private BisSysParamService bisSysParamService;
	@Override
	public PageData<BisSysParamDto> findListPage(PageData<BisSysParamDto> pageVO,
			BisSysParamDto bisSysParamDto) throws CbsCheckedException {
		return bisSysParamService.findListPage(pageVO, bisSysParamDto);
	}

	/*@Override
	public Map<String, String> mapAllCode() throws CbsCheckedException {
		return bisSysParamService.mapAllCode();
	}*/

	@Override
	public String getValue(String parmCode) throws CbsCheckedException {
		return bisSysParamService.getValue(parmCode);
	}

	@Override
	public void updateSelective(BisSysParamDto record)
			throws CbsCheckedException {
		bisSysParamService.updateSelective(record);
	}

	@Override
	public BisSysParamDto details(String id) throws CbsCheckedException {
		return bisSysParamService.details(id);
	}

	@Override
	public List<BisSysParamDto> getLimitParams(String groupName) {
		return bisSysParamService.getLimitParams(groupName);
	}

	@Override
	public BisSysParamDto selectById(String id) {
		return bisSysParamService.selectById(id);
	}

	@Override
	public BisSysParamDto selectByKey(String key) {
		return bisSysParamService.selectByKey(key);
	}

	@Override
	public List<BisSysParamDto> findByParamNames(List<String> paramNames) {
		return bisSysParamService.findByParamNames(paramNames);
	}

}
