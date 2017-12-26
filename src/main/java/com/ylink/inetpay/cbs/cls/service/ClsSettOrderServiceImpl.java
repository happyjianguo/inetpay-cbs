package com.ylink.inetpay.cbs.cls.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.ylink.inetpay.cbs.cls.dao.ClsAuditDtoMapper;
import com.ylink.inetpay.cbs.cls.dao.ClsSettOrderDao;
import com.ylink.inetpay.common.core.constant.BISAuditStatus;
import com.ylink.inetpay.common.core.constant.BISAuditType;
import com.ylink.inetpay.common.core.constant.CLSReviewStatus;
import com.ylink.inetpay.common.project.cbs.app.MrsAccountAppService;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsAccountDto;
import com.ylink.inetpay.common.project.cbs.dto.ucs.sec.UcsSecUserDto;
import com.ylink.inetpay.common.project.cbs.exception.CbsUncheckedException;
import com.ylink.inetpay.common.project.cbs.exception.ECbsErrorCode;
import com.ylink.inetpay.common.project.clear.app.ClearCATAppService;
import com.ylink.inetpay.common.project.clear.dto.ClsAuditDto;
import com.ylink.inetpay.common.project.clear.dto.ClsSettOrder;
import com.ylink.inetpay.common.project.clear.exception.ClearCheckedException;

@Service("clsSettOrderService")
public class ClsSettOrderServiceImpl implements ClsSettOrderService {
	@Autowired
	private ClsSettOrderDao clsSettOrderDao;
	@Autowired
	private ClsAuditDtoMapper clsAuditDtoMapper;
	@Autowired
	private MrsAccountAppService mrsAccountAppService;
	@Autowired
	private ClearCATAppService clearCATAppService;
	
	public PageData<ClsSettOrder> queryClsSettOrder(PageData<ClsSettOrder> pageDate, ClsSettOrder clsSettOrder) {
		List<MrsAccountDto> orgList =new ArrayList<MrsAccountDto>();
		String orgName = clsSettOrder.getOrgName();
		if(!StringUtils.isBlank(orgName)){
			orgList=mrsAccountAppService.findOrgListByName(orgName);
		}
		List<String> merCodesList = new ArrayList<String>();
		if(orgList!=null && !orgList.isEmpty()){
			for (MrsAccountDto orgDto: orgList) {
				if(StringUtils.isNotBlank(orgDto.getExtOrgId())){
					merCodesList.add(orgDto.getExtOrgId());	
				}
			}
		}
		PageHelper.startPage(pageDate.getPageNumber(), pageDate.getPageSize());
		List<ClsSettOrder> list = clsSettOrderDao.queryClsSettOrder(clsSettOrder,merCodesList);
		
		
		if(!StringUtils.isBlank(orgName) && orgList!=null && !orgList.isEmpty()){
			Map<String,String> map = listToMap(orgList);
			for(ClsSettOrder order:list){
				order.setOrgName(map.get(order.getComCode()));
			}
		}else{
			//保存机构代码
			List<String> merCodes = new ArrayList<String>();
			if(!list.isEmpty()){
				for(ClsSettOrder code:list){
					merCodes.add(code.getComCode());
				}
			}
			if(!merCodes.isEmpty()){
				List<MrsAccountDto> dtoList = mrsAccountAppService.findNameByCode(merCodes);
				Map<String,String> map = listToMap(dtoList);
				for(ClsSettOrder order:list){
					order.setOrgName(map.get(order.getComCode()));
				}
			}
		}
		// 遍历list集合，得到每一条数据的id
		for (ClsSettOrder param : list) {
			//调用bisauditmapper，获取id和登录名下的所有复核数据
			List<ClsAuditDto> bisAuditDtos = clsAuditDtoMapper.isAudit(param.getId(), param.getLoginName());
			//如果复核数据大于零，则证明已有复核数据，不能再复核
			if (bisAuditDtos.size()>0) {
				param.setFlag(false);
			}
			//否则设置为true
			param.setFlag(true);
		}
		Page<ClsSettOrder> page = (Page<ClsSettOrder>) list;
		pageDate.setTotal(page.getTotal());
		pageDate.setRows(list);
		return pageDate;
	}

	/**
	 * 查询结算订单详情
	 */
	public ClsSettOrder details(String id) {
		return clsSettOrderDao.queryById(id);
	}

	/**
	 * 插入复核数据
	 */
	@Override
	public ClsAuditDto queryCheckData(UcsSecUserDto user, String id,BISAuditStatus auditStatus) {
		ClsAuditDto bisAuditDto = createAuditDto(user, id,auditStatus);
		/**
		 * 2.判断该登录名下的复核数据是否存在，存在则返回false，不存在则插入数据，并返回true
		 */
		// 查询制定id和登录名下是否存在复核数据
		// 2.1生成查询实体作为查询条件
		ClsAuditDto queryParam = new ClsAuditDto();
		queryParam.setAuditor(user.getLoginName());
		queryParam.setBusId(id);
		List<ClsAuditDto> pageData = clsAuditDtoMapper.queryAllToList(queryParam);
		if (pageData !=null && !pageData.isEmpty()) {	//复核人不能是同一个人
			queryParam.setReason("复核人不能是同一个人,复核人id["+queryParam.getId()+"]");
			throw new CbsUncheckedException(ECbsErrorCode.NOTAGAIN_REVICE);
		}
		return bisAuditDto;
	}
	
	private ClsAuditDto createAuditDto(UcsSecUserDto user, String id,BISAuditStatus auditStatus){
		/**
		 * 1.新增复核成功数据到复核表
		 */
		ClsAuditDto clsAuditDto = new ClsAuditDto();
		// 设置复核状态
		clsAuditDto.setAuditStatus(auditStatus);
		// 设置业务id
		clsAuditDto.setBusId(id);
		// 设置复核类型
		clsAuditDto.setAuditType(BISAuditType.COINSURANCE_SETT);
		// 设置复核登录名
		clsAuditDto.setAuditor(user.getLoginName());
		// 设置复核人姓名
		clsAuditDto.setAuditorName(user.getRealName());
		// 设置复核时间
		clsAuditDto.setAuditTime(new Date());
		// 设置复核备注
		clsAuditDto.setRemark("结算订单复核成功操作");
		// 生成uuid
		clsAuditDto.setId(UUID.randomUUID().toString());
		return clsAuditDto;
	}
	
	private Map<String,String> listToMap(List<MrsAccountDto> list){
		Map<String,String> map = new HashMap<String,String>();
		for(MrsAccountDto dto:list){
			map.put(dto.getExtOrgId(), dto.getAccountName());
		}
		return map;
	}

	@Override
	public void settleOrderBatchAudit(UcsSecUserDto currentUser, List<String> ids,
			BISAuditStatus auditPass, CLSReviewStatus pass) {
		ArrayList<ClsAuditDto> clsAuditDtos = new ArrayList<ClsAuditDto>();
		int errorNum=0;
		for (String id : ids) {
			try {
				clsAuditDtos.add(queryCheckData(currentUser, id, auditPass));
			} catch (Exception e) {
				
				errorNum++;
				
			}
		}
		if(errorNum>0){
			throw new CbsUncheckedException("","有【"+errorNum+"】条记录已经被您复核！");
		}
		if(clsAuditDtos!=null && !clsAuditDtos.isEmpty()){
			for (ClsAuditDto dto : clsAuditDtos) {
				try {
					clearCATAppService.reviewSett(dto, dto.getBusId(), pass);
				} catch (ClearCheckedException e) {
					errorNum++;
				} catch (Exception e) {
					errorNum++;
				}
			}
		}
		if(errorNum>0){
			throw new CbsUncheckedException("","有【"+errorNum+"】条记录【"+pass.getDisplayName()+"】失败！！");
		}
	}
}
