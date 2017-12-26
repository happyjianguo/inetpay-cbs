package com.ylink.inetpay.cbs.mrs.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shie.constant.SHIEConfigConstant;
import com.ylink.eu.util.tools.CollectionUtil;
import com.ylink.eu.util.tools.StringUtil;
import com.ylink.inetpay.cbs.bis.service.BisExceptionLogService;
import com.ylink.inetpay.cbs.common.CbsConfig;
import com.ylink.inetpay.cbs.common.CbsConstants;
import com.ylink.inetpay.cbs.mrs.dao.MrsNotifyDtoMapper;
import com.ylink.inetpay.common.core.constant.EBisExceptionLogNlevel;
import com.ylink.inetpay.common.core.constant.EBisExceptionLogType;
import com.ylink.inetpay.common.core.constant.EBisSmsSystem;
import com.ylink.inetpay.common.core.constant.MrsNotifyStatus;
import com.ylink.inetpay.common.core.constant.MrsNotifyType;
import com.ylink.inetpay.common.core.util.GsonUtil;
import com.ylink.inetpay.common.core.util.StringUtils;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisExceptionLogDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsNotifyDto;
import com.ylink.inetpay.common.project.cbs.exception.CbsCheckedException;
import com.ylink.inetpay.common.project.portal.vo.sync.SyncFileInfo;
import com.ylink.inetpay.common.project.portal.vo.sync.SyncFileRespVO;
import com.ylink.inetpay.common.project.portal.vo.sync.SyncProductRespVO;
import com.ylink.inetpay.common.project.portal.vo.sync.SyncRespVO;

import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;

@Service
public class MrsNotifyServiceImpl implements MrsNotifyService{

	private static Logger log = LoggerFactory.getLogger(MrsNotifyServiceImpl.class);
	
	@Autowired
	private MrsNotifyDtoMapper mrsNotifyDtoMapper;
	@Autowired
	private SyncCustomerService syncCustomerService;
	@Autowired
	private MrsAccountService mrsAccountService;
	@Autowired
	private BisExceptionLogService bisExceptionLogService;
	@Override
	@Transactional(value = CbsConstants.TX_MANAGER_MRS)
	public void syncPersonInfo(MrsNotifyDto dto) throws CbsCheckedException {
		String id = dto.getId();
		int notifyNum = dto.getNotifyNum() == null ? 0 : dto.getNotifyNum();
		Date date = new Date();
		SyncRespVO respVo = syncCustomerService.syncPerson(dto.getReqJson());
		if(respVo.getUserCheckVo().isCheckValue()) {
			// 处理成功
			String customerCode = respVo.getCustomerCode();
			notifyNum++;
			mrsNotifyDtoMapper.updateStatusById(id, MrsNotifyStatus.NOTIFY_SUCCESS.getValue(), notifyNum, date) ;
			// 更新客户编号到Person表和Account表中
			mrsAccountService.updatePersonCustomerCode(dto.getCustId(), customerCode);
			syncFileInfoFile(dto.getCustId(), customerCode);
		} else {
			// 处理失败
			log.info("处理失败");
			notifyNum++;
			MrsNotifyStatus notifyStatus = MrsNotifyStatus.NOTIFY_FAIL;
			if(notifyNum == SHIEConfigConstant.MAX_NOTIFY_NUM) {
				notifyStatus = MrsNotifyStatus.NOTIFY_CLOSE;
			}
			mrsNotifyDtoMapper.updateStatusById(id, notifyStatus.getValue(), notifyNum, date) ;
			saveErrorExcetpionLog(String.format("个人信息同步失败,id = %s,custId = %s,resultCode = %s,resultMsg = %s",
					dto.getId(),dto.getCustId(),
					respVo.getResultCode(),respVo.getResultMsg()));
		}
	}
	/**
	 * 
	 *方法描述：同步完基本信息以后，同步附件信息
	 * 创建人：ydx
	 * 创建时间：2017年4月27日 上午9:31:29
	 * @param custId
	 * @param customerCode
	 * @throws CbsCheckedException
	 */
	private void syncFileInfoFile(String custId,String customerCode) throws CbsCheckedException{
		
		// 更新客户编号  通知表  附件
		updateCustomerCodeByCustIdForFile(custId, customerCode);
		//查询附件信息
		List<MrsNotifyDto> notifyList = mrsNotifyDtoMapper.findByCustIdAndBusiType(custId, MrsNotifyType.FILE.getValue());
		if(!CollectionUtil.isEmpty(notifyList)){
			//同步附件信息
			MrsNotifyDto dto = notifyList.get(0);
			dto.setCustomerCode(customerCode);
			syncFileInfo(dto);
			
		}
		
	}
	private void updateCustomerCodeByCustIdForFile(String custId, String customerCode){
		List<MrsNotifyDto> notifyList = mrsNotifyDtoMapper.findByCustIdForCustomerCode(custId, MrsNotifyType.FILE.getValue());
		if(null!=notifyList && notifyList.size()>0){
			List<SyncFileInfo> syncFileList = new ArrayList<SyncFileInfo>();
			JSONArray array = null;
			for(MrsNotifyDto notify : notifyList){
				notify.setCustomerCode(customerCode);
				array = JSONArray.fromObject(notify.getReqJson());
				syncFileList = JSONArray.toList(array, new SyncFileInfo(), new JsonConfig());
				for(SyncFileInfo file : syncFileList){
					if(StringUtils.isBlank(file.getCustomerCode())){
						file.setCustomerCode(customerCode);
					}
				}
				String jsonStr = GsonUtil.noAnnotaGson.toJson(syncFileList);
				log.info("客户编码回填同步字符串:" + jsonStr);
				notify.setReqJson(jsonStr);
				mrsNotifyDtoMapper.updateByPrimaryKeySelective(notify);
			}
		}
	}

	@Override
	@Transactional(value = CbsConstants.TX_MANAGER_MRS)
	public void syncOrganInfo(MrsNotifyDto dto) throws CbsCheckedException {
		String id = dto.getId();
		int notifyNum = dto.getNotifyNum() == null ? 0 : dto.getNotifyNum();
		Date date = new Date();
		SyncRespVO respVo = syncCustomerService.syncOrgan(dto.getReqJson());
		if(respVo.getUserCheckVo().isCheckValue()) {
			// 处理成功
			String customerCode = respVo.getCustomerCode();
			notifyNum++;
			mrsNotifyDtoMapper.updateStatusById(id, MrsNotifyStatus.NOTIFY_SUCCESS.getValue(), notifyNum, date) ;
			// 更新客户编号到Person表和Account表中
			mrsAccountService.updateOrganCustomerCode(dto.getCustId(), customerCode);
			// 更新客户编号  通知表  附件
//			updateCustomerCodeByCustIdForFile(dto.getCustId(), customerCode);
			syncFileInfoFile(dto.getCustId(), customerCode);
		} else {
			// 处理失败
			log.info("处理失败[custId={}]",dto.getCustId());
			notifyNum++;
			MrsNotifyStatus notifyStatus = MrsNotifyStatus.NOTIFY_FAIL;
			if(notifyNum == SHIEConfigConstant.MAX_NOTIFY_NUM) {
				notifyStatus = MrsNotifyStatus.NOTIFY_CLOSE;
			}
			mrsNotifyDtoMapper.updateStatusById(id, notifyStatus.getValue(), notifyNum, date) ;
			saveErrorExcetpionLog(String.format("机构信息同步失败,id = %s,custId = %s,resultCode = %s,resultMsg = %s",
					dto.getId(),dto.getCustId(),
					respVo.getResultCode(),respVo.getResultMsg()));
		}
	}
	@Override
	@Transactional(value = CbsConstants.TX_MANAGER_MRS)
	public void syncProductInfo(MrsNotifyDto dto) throws CbsCheckedException {
		String id = dto.getId();
		int notifyNum = dto.getNotifyNum() == null ? 0 : dto.getNotifyNum();
		Date date = new Date();
		SyncProductRespVO respVo = syncCustomerService.syncProduct(dto.getReqJson());
		if(respVo.getUserCheckVo().isCheckValue()) {
			// 处理成功
			String customerCode = respVo.getCustomerCode();
			notifyNum++;
			mrsNotifyDtoMapper.updateStatusById(id, MrsNotifyStatus.NOTIFY_SUCCESS.getValue(), notifyNum, date) ;
			// 更新客户编号到Person表和Account表中
			mrsAccountService.updateProductCustomerCode(dto.getCustId(), customerCode);
			// 更新客户编号  通知表  附件
//			updateCustomerCodeByCustIdForFile(dto.getCustId(), customerCode);
			syncFileInfoFile(dto.getCustId(), customerCode);
		} else {
			// 处理失败
			log.info("处理失败");
			notifyNum++;
			MrsNotifyStatus notifyStatus = MrsNotifyStatus.NOTIFY_FAIL;
			if(notifyNum == SHIEConfigConstant.MAX_NOTIFY_NUM) {
				notifyStatus = MrsNotifyStatus.NOTIFY_CLOSE;
			}
			mrsNotifyDtoMapper.updateStatusById(id, notifyStatus.getValue(), notifyNum, date) ;
			saveErrorExcetpionLog(String.format("产品信息同步失败,id = %s,custId = %s,resultCode = %s,resultMsg = %s",
					dto.getId(),dto.getCustId(),
					respVo.getResultCode(),respVo.getResultMsg()));
		}
	}
	
	@Override
	public void syncFileInfo(MrsNotifyDto dto) throws CbsCheckedException {
		String id = dto.getId();
		int notifyNum = dto.getNotifyNum() == null ? 0 : dto.getNotifyNum();
		Date date = new Date();
		
		SyncFileRespVO respVo = null;
				
		// 验证json中是否有客户编码
		if(StringUtil.isNEmpty(dto.getCustomerCode())){
			respVo = syncCustomerService.syncFile(dto.getReqJson());
		}
		
		if(null!=respVo && respVo.getUserCheckVo().isCheckValue()) {
			notifyNum++;
			mrsNotifyDtoMapper.updateStatusById(id, MrsNotifyStatus.NOTIFY_SUCCESS.getValue(), notifyNum, date) ;
		} else {
			// 处理失败
			log.info("处理失败");
			notifyNum++;
			MrsNotifyStatus notifyStatus = MrsNotifyStatus.NOTIFY_FAIL;
			if(notifyNum == SHIEConfigConstant.MAX_NOTIFY_NUM) {
				notifyStatus = MrsNotifyStatus.NOTIFY_CLOSE;
			}
			mrsNotifyDtoMapper.updateStatusById(id, notifyStatus.getValue(), notifyNum, date) ;
			if(null!=respVo){
				saveErrorExcetpionLog(String.format("证件信息同步失败,id = %s,custId = %s,resultCode = %s,resultMsg = %s",
						dto.getId(),dto.getCustId(),
						respVo.getResultCode(),respVo.getResultMsg()));
			}else{
				saveErrorExcetpionLog(String.format("证件信息同步失败,客户编号不能为空,id = %s,custId = %s,resultCode = %s,resultMsg = %s",
						dto.getId(),dto.getCustId(),
						"",""));
			}
			
		}
	}

	@Override
	public List<MrsNotifyDto> findByStatusAndMaxNotifyNum(List<String> statusList, Integer maxNotifyNum) {
		return mrsNotifyDtoMapper.findByStatusAndMaxNotifyNum(statusList, maxNotifyNum);
	}
	
	/**
	 * 记录异常日志
	 * @param errorMsg
	 */
	private void saveErrorExcetpionLog(String errorMsg){
		BisExceptionLogDto	dto=new BisExceptionLogDto();
		dto.setSystem(EBisSmsSystem.CBS);
		dto.setNlevel(EBisExceptionLogNlevel.WARNING);
		dto.setType(EBisExceptionLogType.MRS_SYNC_ECIF);
		dto.setContent(errorMsg);
		dto.setAllpath(CbsConfig.getLogFullPath());
		bisExceptionLogService.saveLog(dto);
	}
	
}
