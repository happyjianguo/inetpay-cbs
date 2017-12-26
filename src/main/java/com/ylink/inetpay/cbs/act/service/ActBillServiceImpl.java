package com.ylink.inetpay.cbs.act.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.ylink.inetpay.cbs.act.dao.ActBillDtoMapper;
import com.ylink.inetpay.cbs.act.dao.ActSubjectDtoMapper;
import com.ylink.inetpay.cbs.mrs.service.MrsAccountService;
import com.ylink.inetpay.common.core.dto.ReporHeadDto;
import com.ylink.inetpay.common.project.account.dto.ActBillDto;
import com.ylink.inetpay.common.project.account.dto.ActSubjectDto;

@Service("actBillService")
public class ActBillServiceImpl implements ActBillService {

	private static Logger log = LoggerFactory.getLogger(ActBillServiceImpl.class);
	@Autowired
	ActBillDtoMapper actBillDtoMapper;
	@Autowired
	ActSubjectDtoMapper actSubjectDtoMapper;
	@Autowired
	private MrsAccountService mrsAccountService;
	@Override
	public PageData<ActBillDto> queryAllData(
			PageData<ActBillDto> pageDate, ActBillDto ActBillDto) {

		PageHelper.startPage(pageDate.getPageNumber(), pageDate.getPageSize());
		List<ActBillDto> list = actBillDtoMapper.queryAllData(ActBillDto);
		List<String> subjectsNo = new ArrayList<String>();
		for(ActBillDto dtos:list){
			subjectsNo.add(dtos.getAccountId().substring(0, 7));
			subjectsNo.add(dtos.getAccountId().substring(0, 6));
		}
		//获取记账科目数据
		if(subjectsNo!=null&&!subjectsNo.isEmpty()){
			List<ActSubjectDto> dtoList = actSubjectDtoMapper.findBysubjectsNo(subjectsNo);
			Map<String,String> dtoMap = listToMap(dtoList);
			for(ActBillDto dto:list){
				if(dtoMap.get(dto.getAccountId().substring(0, 7))!=null){
					dto.setSubjectName(dtoMap.get(dto.getAccountId().substring(0,7)));
				}else{
					dto.setSubjectName(dtoMap.get(dto.getAccountId().substring(0,6)));
				}
				
			} 
		}
		Page<ActBillDto> page = (Page<ActBillDto>) list;
		pageDate.setTotal(page.getTotal());
		pageDate.setRows(list);
		return pageDate;
	}

	@Override
	public ActBillDto selectByBillId(String billId) {
		return actBillDtoMapper.selectByBillId(billId);
	}

	@Override
	public ReporHeadDto reportSumData(ActBillDto ActBillDto) {
		return actBillDtoMapper.reportSumData(ActBillDto);
	}
	
	public Map<String,String> listToMap(List<ActSubjectDto> list){
		Map<String,String> map = new HashMap<String,String>();
	    for(ActSubjectDto dto:list){
	    	map.put(dto.getSubjectNo(),dto.getSubjectName());
	    }
	    return map;
	}

	@Override
	public PageData<ActBillDto> queryAllDataByPortal(PageData<ActBillDto> pageDate,
			ActBillDto actBillDto,
			String subAcctType) {
		List<String> acctTypeNos = new ArrayList<String>();
		acctTypeNos.add(subAcctType);
		// 二级科目
		List<String> subjectNoList = new ArrayList<String>();
		// 按子账户类型查询对应的二级科目，再通通过二级科目与账户表关联到分录数据
		subjectNoList = mrsAccountService.findSub2NoByAcctTypeNos(acctTypeNos);
		if(null == subjectNoList){
			log.info("根据子账户类型[{}]查询对应的二级科目没有获取到对应的账户数据", subAcctType);
			return pageDate;
		}
		/*List<MrsAccountDto> actList = mrsAccountService.findAccountDtoBySubAcctType(subAcctType);
		// 查询用户账户（根据用户编号）
		if (actList != null && actList.size() > 0) {
			for (MrsAccountDto actDto : actList) {
				subjectNoList.add(actDto.getSubjectNo2());
			}
		} else {
			log.info("根据子账户类型[{}]查询对应的二级科目没有获取到对应的账户数据", subAcctType);
			return pageDate;
		}*/
			
		PageHelper.startPage(pageDate.getPageNumber(), pageDate.getPageSize());
		List<ActBillDto> list = actBillDtoMapper.queryAllDataByPortal(actBillDto, subjectNoList);
		List<String> subjectsNo = new ArrayList<String>();
		for (ActBillDto dtos : list) {
			subjectsNo.add(dtos.getAccountId().substring(0, 7));
			subjectsNo.add(dtos.getAccountId().substring(0, 6));
		}
		// 获取记账科目数据
		if (subjectsNo != null && !subjectsNo.isEmpty()) {
			List<ActSubjectDto> dtoList = actSubjectDtoMapper.findBysubjectsNo(subjectsNo);
			Map<String, String> dtoMap = listToMap(dtoList);
			for (ActBillDto dto : list) {
				if (dtoMap.get(dto.getAccountId().substring(0, 7)) != null) {
					dto.setSubjectName(dtoMap.get(dto.getAccountId().substring(0, 7)));
				} else {
					dto.setSubjectName(dtoMap.get(dto.getAccountId().substring(0, 6)));
				}

			}
		}
		Page<ActBillDto> page = (Page<ActBillDto>) list;
		pageDate.setTotal(page.getTotal());
		pageDate.setRows(list);
		return pageDate;
	}

	@Override
	public ActBillDto selectByCustId(ActBillDto actBillDto) {
		List<ActBillDto> list = new ArrayList<ActBillDto>();
		ActBillDto ActBill = new ActBillDto();
		try {
			list = actBillDtoMapper.queryAllData(actBillDto);
			ActBill = list.get(0);
		} catch (Exception e) {
			log.error("错误原因"+e);
		}
		
		return ActBill;
	}

}
