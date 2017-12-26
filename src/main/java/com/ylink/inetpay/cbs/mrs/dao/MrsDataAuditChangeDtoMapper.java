package com.ylink.inetpay.cbs.mrs.dao;

import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsDataAuditChangeDto;
@MybatisMapper("mrsDataAuditChangeDtoMapper")
public interface MrsDataAuditChangeDtoMapper {
    int deleteByPrimaryKey(String id);

    int insert(MrsDataAuditChangeDto record);

    int insertSelective(MrsDataAuditChangeDto record);

    MrsDataAuditChangeDto selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(MrsDataAuditChangeDto record);

    int updateByPrimaryKey(MrsDataAuditChangeDto record);
    /**
	 * 检查是否有待审核数据
	 * @param refId
	 * @return
	 */
    MrsDataAuditChangeDto selectByRefId(String refId);
    /**
  	 * 获取序列号
  	 * 
  	 * @return
  	 */
  	String getPayNoVal();
  	
  	
}