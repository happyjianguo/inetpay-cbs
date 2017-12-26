package com.ylink.inetpay.cbs.mrs.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsContactListDto;

public interface MrsContactListService {
	/**
	 * 保存联系人
	 * @param dto
	 * @return
	 */
	public int insert(MrsContactListDto dto);

	/**
	 * 更新联系人
	 * @param dto
	 * @return
	 */
	public int updateByPrimaryKey(MrsContactListDto dto);
	/**
	 * 
	 *方法描述：根据一户通编号查询联系人信息
	 * 创建人：ydx
	 * 创建时间：2017年2月20日 下午9:42:56
	 * @param custId
	 * @return
	 */
	public List<MrsContactListDto> findByCustId(String custId);
	/**
	 * 
	 *方法描述：根据查询条件查询联系人信息
	 * 创建人：ydx
	 * 创建时间：2017年3月19日 下午3:19:52
	 * @param param
	 * @return
	 */
	public List<MrsContactListDto> queryAllData(MrsContactListDto param);
    
	/**
     * 根据一互通账户、联系人姓名删除联系人
     * @param custId
     * @param name
     */
    public void deleteByNameAndCustId(String custId,String name);
   
    /**
     * 根据ID查询联系人信息
     * @param id
     * @return
     */
    public MrsContactListDto selectByPrimaryKey(String id);
    
    /**
     * 根据ID删除联系人
     * @param id
     */
    public void deleteByPrimaryKey(String id);
    
    /**
     * 根据ID,一户通账号查询联系人信息
     * @param id
     * @param custId
     * @return
     */
    MrsContactListDto findByIdandCustId(String id,String custId);
    
}
