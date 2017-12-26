package com.ylink.inetpay.cbs.mrs.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsBankBusiDto;

@MybatisMapper("mrsBankBusiDtoMapper")
public interface MrsBankBusiDtoMapper {
    int deleteByPrimaryKey(String id);

    int insert(MrsBankBusiDto record);

    int insertSelective(MrsBankBusiDto record);

    MrsBankBusiDto selectByPrimaryKey(String id);
    
    MrsBankBusiDto findIsDefault(String custId);

    int updateByPrimaryKeySelective(MrsBankBusiDto record);
    
    List<MrsBankBusiDto> selectByParam(MrsBankBusiDto record);

    int updateByPrimaryKey(MrsBankBusiDto record);
    /**
	 * 根据渠道编码和银行卡号，状态
	 * @param channelCode 渠道编码
	 * @param accNo 银行卡号
	 * @param bindStatus 银行状态
	 * @return
	 */
    List<MrsBankBusiDto> findBanksBychnlAndAccno(@Param("channelCode") String channelCode,
    		@Param("accNo")  String accNo,
    		@Param("bindStatus") String bindStatus);
    /**
	 * 根据一户通主键查找
	 * @param custId
	 * @return
	 */
	List<MrsBankBusiDto> findByCustId(String custId);
	/**
	 * 根据一户通号，查找授信的，状态为复核成功或无需复核，绑定状态：绑定成功
	 * @param custId
	 * @return
	 */
	public List<MrsBankBusiDto> findBanksByCustId(String custId);
	
	/**
	 * 根据一户通号，绑定状态：绑定成功
	 * @param custId
	 * @return
	 */
	public List<MrsBankBusiDto> findBandByCustId(String custId);
	/**
	 * 根据行别、用户名、卡号加起来的唯一性找银行卡
	 * @param bankType
	 * @param custName
	 * @param accNo
	 * @return
	 */
	MrsBankBusiDto findByOnly(String bankType,String custName,String accNo);
	
	/**
	 * 根据一户通主键和审核状态查找
	 * @param custId
	 * @return
	 */
	List<MrsBankBusiDto> findByCustIdAndAduitStatus(@Param("custId")String custId,@Param("aduitStatus")String aduitStatus);
	
	
	/**
	 * 根据一户通主键和审核状态查找且处理成功
	 * @param custId
	 * @return
	 */
	List<MrsBankBusiDto> findByCustIdAndAduitStatusAndSuccess(@Param("custId")String custId,@Param("aduitStatus")String aduitStatus);
	
	 /**
     * 分页查询银行的信息
     * @param dto
     * @return
     */
    List<MrsBankBusiDto> pageBank(MrsBankBusiDto dto);
    
    /**
	 * 根据卡号、一户通号、绑卡类型查询
	 * @param accNo
	 * @param custId
	 * @param payType 枚举：BankPayType
	 * @return
	 */
	MrsBankBusiDto findByAccnoAndCustidAndPaytype(@Param("accNo") String accNo,@Param("custId") String custId, @Param("payType") String payType );
	
	/**
	 * 根据一户通号查询(快捷专用:绑定(02)、绑卡类型(01))
	 * @param accNo
	 * @param custId
	 * @param payType 枚举：BankPayType
	 * @return
	 */
	List<MrsBankBusiDto> findByBindStatusAndCustidAndPaytype(String custId);
	
	 /**
	 * 根据卡号、一户通号查询
	 * @param accNo
	 * @param custId
	 * @return
	 */
	List<MrsBankBusiDto> findByAccnoAndCustid(@Param("accNo") String accNo,
		@Param("custId") String custId );
}