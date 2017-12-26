package com.ylink.inetpay.cbs.bis.service;

import java.util.ArrayList;
import java.util.List;

import com.ylink.inetpay.common.project.cbs.dto.bis.BisBusinessParamDto;
import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.common.project.cbs.dto.bis.BisBusinessDto;
import com.ylink.inetpay.common.project.cbs.vo.bis.BisBusinessVO;

public interface BisBusinessService {

	PageData<BisBusinessDto> getBisBusiness(PageData<BisBusinessDto> pageData, BisBusinessVO businessVo);
	
	BisBusinessDto findById(String id);

	void addBusinessDto(BisBusinessDto dto);

	void deleteById(String id);

	void updateBusinessDto(BisBusinessDto dto);

	List<BisBusinessDto> find(String businessCode, String payeeCode, String status);

	List<BisBusinessParamDto> listBusiParamsByBusiCode(String busiCode);

	List<BisBusinessDto> findBusinessByaccessorCode(String id);
	/**
	 * 根据业务代码获取业务参数列表
	 * @param businessCode
	 * @return
	 */
	List<BisBusinessParamDto> findBusinessParamsByBusinessCode(String businessCode);
	/**
	 * 批量修改业务参数
	 * @param params
	 * @return
	 */
	long batchUpdateParam(ArrayList<BisBusinessParamDto> params);
	/**
	 * 获取所有业务
	 * @return
	 */
	List<BisBusinessDto> list();
	/**
	 * 获取所有启用的业务
	 * @return
	 */
	List<BisBusinessDto> findAllBusiness();
 

}
