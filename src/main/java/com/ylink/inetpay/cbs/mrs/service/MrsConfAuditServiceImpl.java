package com.ylink.inetpay.cbs.mrs.service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.ylink.eu.util.tools.StringUtil;
import com.ylink.inetpay.cbs.mrs.dao.MrsConfAuditDtoMapper;
import com.ylink.inetpay.cbs.mrs.dao.MrsConfAuditItemDtoMapper;
import com.ylink.inetpay.cbs.ucs.sec.dao.UcsSecUserDtoMapper;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsConfAuditDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsConfAuditItemDto;
/**
 * 审核配置
 * @author pst23
 *
 */
@Service("mrsConfAuditService")
public class MrsConfAuditServiceImpl implements MrsConfAuditService {
	
	private static Logger log = LoggerFactory.getLogger(MrsConfAuditServiceImpl.class);
	
	@Autowired
	MrsConfAuditDtoMapper mrsConfAuditDtoMapper;
	@Autowired
	MrsConfAuditItemDtoMapper mrsConfAuditItemDtoMapper;
	@Autowired
	UcsSecUserDtoMapper ucsSecUserDtoMapper;
	/**
     * 根据主键查询
     */
	@Override
	public MrsConfAuditDto selectByPrimaryKey(String id) {
		return mrsConfAuditDtoMapper.selectByPrimaryKey(id);
	}
	/**
     * 根据主键更新信息
     */
	@Override
	public void updateByPrimaryKeySelective(MrsConfAuditDto mrsConfAuditDto) {
		log.info("根据[id="+mrsConfAuditDto.getId()+"]查询到审核信息");
		mrsConfAuditDto.setUpdateTime(new Date());
		try {
			mrsConfAuditDtoMapper.updateByPrimaryKeySelective(mrsConfAuditDto);
		} catch (Exception e) {
			log.info("根据[id="+mrsConfAuditDto.getId()+"]更新审核配置信息失败！");
		}
	}
	/**
	 * 根据发起端，业务类型查询审核配置信息
	 */
	@Override
	public MrsConfAuditDto findByBusiTypeAndSendType(String busiType, String sendPort) {
		return mrsConfAuditDtoMapper.findByBusiTypeAndSendType(busiType, sendPort);
	}
	/**
     * 根据参数查询所有数据
     */
	@Override
	public PageData<MrsConfAuditDto> quaryAllData(PageData<MrsConfAuditDto> pageData, MrsConfAuditDto mrsConfAuditDto) {
		PageHelper.startPage(pageData.getPageNumber(), pageData.getPageSize());
		List<MrsConfAuditDto> list = mrsConfAuditDtoMapper.quaryAllData(mrsConfAuditDto);
		Page<MrsConfAuditDto> page = (Page<MrsConfAuditDto>) list;
		pageData.setTotal(page.getTotal());
		pageData.setRows(list);
		return pageData;
	}
	/**
	 * 查询审核配置详情
	 * 根据审核配置信息主键查询审核配置明细信息
	 */
	@Override
	public List<MrsConfAuditItemDto> selectByAuditId(String auditId) {
		return mrsConfAuditItemDtoMapper.selectByAuditId(auditId);
	}
	/**
     * 更新审核配置信息(包括审核配置表和审核配置明细表)
     */
	@Override
	//@Transactional(rollbackFor=Exception.class)
	public void updateAuditAndAuditItem(MrsConfAuditDto mrsConfAuditDto, MrsConfAuditItemDto mrsConfAuditItemDto,List<String> secUserIds,List<String> secUserLoginNames) {
		String auditId = mrsConfAuditDto.getId();
		log.info("根据[id="+auditId+"]查询到审核配置信息");
		mrsConfAuditDto.setAuditNum((short)secUserIds.size());
		mrsConfAuditDto.setUpdateTime(new Date());
		try {
			mrsConfAuditDtoMapper.updateByPrimaryKeySelective(mrsConfAuditDto);
			try {
				List<MrsConfAuditItemDto> list = mrsConfAuditItemDtoMapper.selectByAuditId(auditId);
				if(null != list){
					mrsConfAuditItemDtoMapper.deleteByAuditId(auditId);
				}
				mrsConfAuditItemDto.setAuditId(mrsConfAuditDto.getId());
				if(secUserIds.size() != 0){
					for(short i=0 ; i < secUserIds.size() ; i++){
						mrsConfAuditItemDto.setId(UUID.randomUUID().toString());
						mrsConfAuditItemDto.setUserId(secUserIds.get(i));
						mrsConfAuditItemDto.setUserName(StringUtil.isNEmpty(secUserLoginNames.get(i)) ? secUserLoginNames.get(i) : "");
						mrsConfAuditItemDtoMapper.insertSelective(mrsConfAuditItemDto);
					}
				}
			} catch (Exception e) {
				log.info("根据[auditId="+mrsConfAuditDto.getId()+"]更新审核配置明细信息失败！");
				e.printStackTrace();
			}
		} catch (Exception e) {
			log.info("根据[id="+mrsConfAuditDto.getId()+"]更新审核配置信息失败！");
			e.printStackTrace();
		}
	}
	/**
     * 添加审核配置信息
     */
	@Override
	public void insertSelective(MrsConfAuditDto mrsConfAuditDto,MrsConfAuditItemDto mrsConfAuditItemDto,List<String> secUserIds,List<String> secUserLoginNames) {
		mrsConfAuditDto.setAuditNum((short)secUserIds.size());
		mrsConfAuditDto.setCreateTime(new Date());
		mrsConfAuditDto.setId(UUID.randomUUID().toString());
		try {
			mrsConfAuditDtoMapper.insertSelective(mrsConfAuditDto);
			try {
				mrsConfAuditItemDto.setAuditId(mrsConfAuditDto.getId());
				if(secUserIds.size() != 0){
					for(short i=0 ; i < secUserIds.size() ; i++){
						mrsConfAuditItemDto.setId(UUID.randomUUID().toString());
						mrsConfAuditItemDto.setUserId(secUserIds.get(i));
						mrsConfAuditItemDto.setUserName(StringUtil.isNEmpty(secUserLoginNames.get(i)) ? secUserLoginNames.get(i) : "");
						mrsConfAuditItemDtoMapper.insertSelective(mrsConfAuditItemDto);
					}
				}
			} catch (Exception e) {
				log.info("添加审核配置明细信息失败！");
				e.printStackTrace();
			}
		} catch (Exception e) {
			log.info("添加审核配置信息失败！");
			e.printStackTrace();
		}
	}
	
}
