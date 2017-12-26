package com.ylink.inetpay.cbs.bis.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.ylinkpay.framework.core.model.PageData;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.ylink.eu.util.tools.DateUtil;
import com.ylink.inetpay.cbs.bis.dao.BisAuditDtoMapper;
import com.ylink.inetpay.cbs.bis.dao.BisTransferHandleDao;
import com.ylink.inetpay.cbs.chl.dao.TbChlBankAccountMapper;
import com.ylink.inetpay.cbs.chl.service.ChlBankAccountService;
import com.ylink.inetpay.cbs.chl.service.ChlBankService;
import com.ylink.inetpay.cbs.common.CbsConstants;
import com.ylink.inetpay.cbs.util.CbsExceptionLogDtoUtils;
import com.ylink.inetpay.common.core.constant.BISAuditStatus;
import com.ylink.inetpay.common.core.constant.BISAuditType;
import com.ylink.inetpay.common.core.constant.EAllocateType;
import com.ylink.inetpay.common.core.constant.EBisExceptionLogNlevel;
import com.ylink.inetpay.common.core.constant.EBisExceptionLogType;
import com.ylink.inetpay.common.core.constant.ECurrenoy;
import com.ylink.inetpay.common.core.constant.EResultCode;
import com.ylink.inetpay.common.core.constant.HandleCheckStatus;
import com.ylink.inetpay.common.core.constant.PayStatusEnum;
import com.ylink.inetpay.common.core.dto.ResultCodeDto;
import com.ylink.inetpay.common.core.util.ExceptionProcUtil;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisAuditDto;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisTransferHandleDto;
import com.ylink.inetpay.common.project.cbs.exception.CbsUncheckedException;
import com.ylink.inetpay.common.project.channel.dto.TbChlBank;
import com.ylink.inetpay.common.project.channel.dto.TbChlBankAccount;
import com.ylink.inetpay.common.project.pay.app.PayOutPayAppService;
import com.ylink.inetpay.common.project.pay.dto.PayAmtAllocateDto;
import com.ylink.inetpay.common.project.pay.pojo.CashTransferPojo;

@Service("bisTransferHandleService")
public class BisTransferHandleServiceImpl implements BisTransferHandleService {
	@Autowired
	private BisTransferHandleDao bisTransferHandleDao;
	@Autowired
	private BisAuditDtoMapper bisAuditDtoMapper;
	@Autowired
	BisAuditRuleService bisAuditRuleService;
	@Autowired
	TbChlBankAccountMapper tbChlBankAccountMapper;
	@Autowired
	PayOutPayAppService payOutPayAppService;
	@Autowired
	ChlBankService chlBankService;
	@Autowired
	ChlBankAccountService chlBankAccountService;
	@Autowired
	BisExceptionLogService bisExceptionLogService;

	private static Logger _log = LoggerFactory.getLogger(BisTransferHandleServiceImpl.class);

	@Override
	public void insert(BisTransferHandleDto bisTransferHandleDto) {
		TbChlBankAccount innerAcctAccount = chlBankAccountService.findByBankAccNo(bisTransferHandleDto.getInnerAcct());
		if(innerAcctAccount==null){
			_log.error("保存头寸调拨失败：入金银行【"+bisTransferHandleDto.getInnerAcct()+"】不存在");
			throw new CbsUncheckedException("","保存头寸调拨失败：入金银行【"+bisTransferHandleDto.getInnerAcct()+"】不存在");
		}
		bisTransferHandleDto.setInnerCustId(innerAcctAccount.getCustId());
		TbChlBankAccount outerAcctAccount = chlBankAccountService.findByBankAccNo(bisTransferHandleDto.getOuterAcct());
		if(outerAcctAccount==null){
			_log.error("保存头寸调拨失败：出金银行【"+bisTransferHandleDto.getOuterAcct()+"】不存在");
			throw new CbsUncheckedException("","保存头寸调拨失败：出金银行【"+bisTransferHandleDto.getOuterAcct()+"】不存在");
		}
		bisTransferHandleDto.setOuterCustId(outerAcctAccount.getCustId());
		int num = bisAuditRuleService.getNum(BISAuditType.HANDLE.getValue(), bisTransferHandleDto.getAmount());
		bisTransferHandleDto.setnCheckNum(num);
		bisTransferHandleDto.setTradeId(DateUtil.getYearMonthDay$(new Date()) + EAllocateType.CASH.getValue()
				+ bisTransferHandleDao.getSeqenceVals());
		bisTransferHandleDto.setInnerBankName(getBankName(bisTransferHandleDto.getInnerBankType()));
		bisTransferHandleDto.setOuterBankName(getBankName(bisTransferHandleDto.getOuterBankType()));
		//如果审核次数为0，直接调用头寸调拨
		if(num==0){
			doPay(bisTransferHandleDto);
			bisTransferHandleDto.setCheckStatus(HandleCheckStatus.PASS);
		}else{
			bisTransferHandleDto.setPayStatus(PayStatusEnum.WAITPAY);
			bisTransferHandleDto.setCheckStatus(HandleCheckStatus.WAIT);
		}
		bisTransferHandleDao.insert(bisTransferHandleDto);
	}
	
	public void doPay(BisTransferHandleDto bisTransferHandleDto){
		// 支付接口
		CashTransferPojo cashTransferPojo = convertTbChlBankAccount(bisTransferHandleDto.getOuterAcct(),
				bisTransferHandleDto.getOuterBankType(), bisTransferHandleDto.getInnerAcct(),
				bisTransferHandleDto.getInnerBankType(), bisTransferHandleDto.getAmount(),
				bisTransferHandleDto.getTradeId(), bisTransferHandleDto.getMemo());
		try {
			_log.info("调支付接口！cashTransferPojo{}", cashTransferPojo);
			ResultCodeDto<PayAmtAllocateDto> resultCodeDto = payOutPayAppService.cashTransfer(cashTransferPojo);
			_log.info("调支付接口返回结果！resultCodeDto{}", resultCodeDto);
			if (EResultCode.SUCCESS.getValue().equals(resultCodeDto.getResultCode().getValue())) {
				_log.info("头寸调拨成功！");
				bisTransferHandleDto.setPayStatus(PayStatusEnum.SUCCESS);
				bisTransferHandleDto.setPayMemo("支付成功");
			} else if (EResultCode.FAIL.getValue().equals(resultCodeDto.getResultCode().getValue())) {
				_log.info("头寸调拨失败！");
				bisTransferHandleDto.setPayStatus(PayStatusEnum.FAIL);
				bisTransferHandleDto.setPayMemo(resultCodeDto.getMsgDetail());
			} else {
				_log.info("头寸调拨未知！");
				bisTransferHandleDto.setPayStatus(PayStatusEnum.UNKNOWN);
				bisTransferHandleDto.setPayMemo("未知");
			}
		} catch (Exception e) {
			bisTransferHandleDto.setPayStatus(PayStatusEnum.UNKNOWN);
			bisTransferHandleDto.setPayMemo("未知");
		}
	}

	@Override
	public void update(BisTransferHandleDto bisTransferHandleDto) {
		bisTransferHandleDao.update(bisTransferHandleDto);
	}

	@Override
	public PageData<BisTransferHandleDto> getByCond(BisTransferHandleDto bisTransferHandleDto,
			PageData<BisTransferHandleDto> pageData) {
		PageHelper.startPage(pageData.getPageNumber(), pageData.getPageSize());
		List<BisTransferHandleDto> list = bisTransferHandleDao.getByCond(bisTransferHandleDto);
		Page<BisTransferHandleDto> page = (Page<BisTransferHandleDto>) list;
		pageData.setTotal(page.getTotal());
		pageData.setRows(list);
		return pageData;
	}
	
	@Override
	public PageData<BisTransferHandleDto> getByConds(BisTransferHandleDto bisTransferHandleDto,
			PageData<BisTransferHandleDto> pageData,String loginName) {
		PageHelper.startPage(pageData.getPageNumber(), pageData.getPageSize());
		List<BisTransferHandleDto> list = bisTransferHandleDao.getByCond(bisTransferHandleDto);
        List<String> ids = new ArrayList<>();
        List<String> all_id = new ArrayList<>();
        for(BisTransferHandleDto dto : list){
        	dto.setAudit(true);
            ids.add(dto.getId());
        }
        if(ids != null && !ids.isEmpty()){
        	List<BisAuditDto> bus_ids = bisAuditDtoMapper.findBisAuditDtoByLoginName(loginName,ids,BISAuditType.HANDLE);
        	
    	  for(BisAuditDto dto :bus_ids){
              all_id.add(dto.getBusId());
          }
    	  
    	  for(String id:all_id){
              for(BisTransferHandleDto dto : list){
                  if(dto.getId().equals(id)){
                      dto.setAudit(false);
                  }
              }
          }
        }
		Page<BisTransferHandleDto> page = (Page<BisTransferHandleDto>) list;
		pageData.setTotal(page.getTotal());
		pageData.setRows(list);
		return pageData;
	}

	@Override
	public BisTransferHandleDto details(String id) {
		return bisTransferHandleDao.details(id);
	}

	@Override
	public void updateAll(BisTransferHandleDto bisTransferHandleDto) {
		bisTransferHandleDao.updateAll(bisTransferHandleDto);

	}

	@Override
	public BisTransferHandleDto getById(String id) {
		return bisTransferHandleDao.getById(id);
	}

	@Transactional(value = CbsConstants.TX_MANAGER_BIS)
	@Override
	public boolean auditTransfer(BisTransferHandleDto bisTransferHandleDto, BisAuditDto bisAuditDto) {
		_log.info("开始审核头寸调拨----------------------------！bisTransferHandleDto{}", bisTransferHandleDto);
		if (bisTransferHandleDto == null || bisAuditDto == null) {
			_log.error("审核失败，传参为空！bisTransferHandleDto{}，bisAuditDto{}", bisTransferHandleDto, bisAuditDto);
			return false;
		}
		// 锁定数据
		BisTransferHandleDto bisTransferHandleDtox = bisTransferHandleDao.details(bisTransferHandleDto.getId());
		try {
			// 审核成功，并且审核次数想同
			if (bisTransferHandleDto.getaCheckNum() == bisTransferHandleDtox.getnCheckNum()
					&& (HandleCheckStatus.PASS.getValue()).equals(bisTransferHandleDto.getCheckStatus().getValue())
					&& (BISAuditStatus.AUDIT_PASS.getValue()).equals(bisAuditDto.getAuditStatus().getValue())) {
				// 修改经办数据
				bisTransferHandleDao.update(bisTransferHandleDto);
				// 插入审核表数据
				bisAuditDtoMapper.insert(bisAuditDto);
				// 支付接口
				CashTransferPojo cashTransferPojo = convertTbChlBankAccount(bisTransferHandleDtox.getOuterAcct(),
						bisTransferHandleDtox.getOuterBankType(), bisTransferHandleDtox.getInnerAcct(),
						bisTransferHandleDtox.getInnerBankType(), bisTransferHandleDtox.getAmount(),
						bisTransferHandleDtox.getTradeId(), bisTransferHandleDtox.getMemo());
				if (cashTransferPojo == null) {
					return false;
				}
							
				try {
					_log.info("调支付接口！cashTransferPojo{}", cashTransferPojo);
					ResultCodeDto<PayAmtAllocateDto> resultCodeDto = payOutPayAppService.cashTransfer(cashTransferPojo);
					_log.info("调支付接口返回结果！resultCodeDto{}", resultCodeDto);
					if (EResultCode.SUCCESS.getValue().equals(resultCodeDto.getResultCode().getValue())) {
						_log.info("头寸调拨成功！");
						bisTransferHandleDto.setPayStatus(PayStatusEnum.SUCCESS);
						bisTransferHandleDto.setPayMemo("支付成功");
					} else if (EResultCode.FAIL.getValue().equals(resultCodeDto.getResultCode().getValue())) {
						_log.info("头寸调拨失败！");
						bisTransferHandleDto.setPayStatus(PayStatusEnum.FAIL);
						bisTransferHandleDto.setPayMemo(resultCodeDto.getMsgDetail());
					} else {
						_log.info("头寸调拨未知！");
						bisTransferHandleDto.setPayStatus(PayStatusEnum.UNKNOWN);
						bisTransferHandleDto.setPayMemo("未知");
					}
					// 更新支付状态
					bisTransferHandleDao.updatePayStatus(bisTransferHandleDto);
					return true;
				} catch (Exception e) {
					bisTransferHandleDto.setPayStatus(PayStatusEnum.UNKNOWN);
					bisTransferHandleDto.setPayMemo("未知");
					bisTransferHandleDao.updatePayStatus(bisTransferHandleDto);
					return true;
				}

			}
			// 审核成功，次数不相等
			if (bisTransferHandleDto.getaCheckNum() < bisTransferHandleDtox.getnCheckNum()
					&& (HandleCheckStatus.WAIT.getValue()).equals(bisTransferHandleDto.getCheckStatus().getValue())
					&& (BISAuditStatus.AUDIT_PASS.getValue()).equals(bisAuditDto.getAuditStatus().getValue())) {
				// 修改经办数据
				bisTransferHandleDao.update(bisTransferHandleDto);
				// 插入审核表数据
				bisAuditDtoMapper.insert(bisAuditDto);
				return true;
			}

			// 审核拒绝
			if ((HandleCheckStatus.REFUSE.getValue()).equals(bisTransferHandleDto.getCheckStatus().getValue())
					&& (BISAuditStatus.AUDIT_REJECT.getValue()).equals(bisAuditDto.getAuditStatus().getValue())) {
				// 修改经办数据
				//bisTransferHandleDto.setPayStatus(PayStatusEnum.SUCCESS);
				bisTransferHandleDao.update(bisTransferHandleDto);
				// 插入审核表数据
				bisAuditDtoMapper.insert(bisAuditDto);
				return true;
			}
		} catch (Exception e) {
			throw new RuntimeException("数据库异常,回滚数据", e);
		}
		return false;
	}

	/**
	 * 查询支付状态
	 */
	@Override
	public BisTransferHandleDto queryCashTransfer(String tradeId, String id) {
		ResultCodeDto<PayAmtAllocateDto> payAmtAllocateDto = payOutPayAppService.queryCashTransferAndPtOutPay(tradeId,
				EAllocateType.CASH);
		BisTransferHandleDto bisTransferHandleDto = new BisTransferHandleDto();
		bisTransferHandleDto.setId(id);
		if (payAmtAllocateDto == null) {
			_log.error("查无此订单！");
			return null;
		}

		if (EResultCode.SUCCESS.getValue().equals(payAmtAllocateDto.getResultCode().getValue())) {
			bisTransferHandleDto.setPayStatus(PayStatusEnum.SUCCESS);
		} else if (EResultCode.FAIL.getValue().equals(payAmtAllocateDto.getResultCode().getValue())) {
			bisTransferHandleDto.setPayStatus(PayStatusEnum.FAIL);
		} else {
			bisTransferHandleDto.setPayStatus(PayStatusEnum.UNKNOWN);
		}
		// 更新支付状态
		bisTransferHandleDao.updatePayStatus(bisTransferHandleDto);
		return bisTransferHandleDto;
	}
	
	/**
	 * 自动查询未知状态
	 */
	@Override
	public void autoQueryUnDownStatus(){
		//获取所有支付状态为未知的数据
		try {
			List<BisTransferHandleDto> bisTransferHandleDtos=bisTransferHandleDao.getUnDownStatusDtos(PayStatusEnum.UNKNOWN);
			for (BisTransferHandleDto bisTransferHandleDto : bisTransferHandleDtos) {
				doQueryPayStatus(bisTransferHandleDto);
			}
		} catch (Exception e) {
			bisExceptionLogService.saveLog(CbsExceptionLogDtoUtils.getBisExceptionLog(EBisExceptionLogNlevel.ERROR, EBisExceptionLogType.QUERY_UNDOWNSTATUS,"头寸调拨自动查询状态失败"));
			_log.error("对外支付自动查询状态失败："+ExceptionProcUtil.getExceptionDesc(e));
		}
	}
	
	public void doQueryPayStatus(BisTransferHandleDto bisTransferHandleDto){
		ResultCodeDto<PayAmtAllocateDto> payAmtAllocateDto = payOutPayAppService.queryCashTransferAndPtOutPay(bisTransferHandleDto.getTradeId(),
				EAllocateType.CASH);
		if (payAmtAllocateDto == null) {
			_log.error("查无此订单！");
			return;
		}
		if (EResultCode.SUCCESS.getValue().equals(payAmtAllocateDto.getResultCode().getValue())) {
			bisTransferHandleDto.setPayStatus(PayStatusEnum.SUCCESS);
		} else if (EResultCode.FAIL.getValue().equals(payAmtAllocateDto.getResultCode().getValue())) {
			bisTransferHandleDto.setPayStatus(PayStatusEnum.FAIL);
		} else {
			bisTransferHandleDto.setPayStatus(PayStatusEnum.UNKNOWN);
		}
		// 更新支付状态
		bisTransferHandleDao.updatePayStatus(bisTransferHandleDto);
	}

	/**
	 * 获取账户信息
	 * 
	 * @param bankAccNo
	 * @param bankType
	 * @return
	 */
	public TbChlBankAccount getTbChlBankAccount(String bankAccNo, String bankType) {
		TbChlBankAccount record = new TbChlBankAccount();
		record.setBankAccNo(bankAccNo);
		record.setBankType(bankType);
		List<TbChlBankAccount> recordList = tbChlBankAccountMapper.findListPage(record);
		if (recordList != null && recordList.size() > 0) {
			if (recordList.size() > 1) {
				_log.info("查询出2条账号数据！");
			}
			return recordList.get(0);
		}
		return null;

	}

	/**
	 * 头寸调拨参数，bankAccNo：付款方账户，bankType：付款方银行编码，bankAccNox：收款方银行卡账号
	 * ,bankTypex：收款方银行编码，orderAmt：金额，tradeId：交易流水号， remark：备注
	 * 
	 * @param record
	 * @param recordx
	 * @param orderAmt
	 * @return
	 */
	public CashTransferPojo convertTbChlBankAccount(String bankAccNo, String bankType, String bankAccNox,
			String bankTypex, long orderAmt, String tradeId, String remark) {
		CashTransferPojo cashTransferPojo = new CashTransferPojo();
		// 付款方
		TbChlBankAccount record = getTbChlBankAccount(bankAccNo, bankType);
		if (record == null) {
			_log.error("获取付款方账号数据失败！账号为：" + bankAccNo);
			return null;
		}
		// 收款方
		TbChlBankAccount recordx = getTbChlBankAccount(bankAccNox, bankTypex);
		if (recordx == null) {
			_log.error("获取收款方账号数据失败！账号为：" + bankAccNox);
			return null;
		}
		cashTransferPojo.setCurrenoy(ECurrenoy.RMB);
		cashTransferPojo.setOrderAmt(orderAmt);
		cashTransferPojo.setOrderId(tradeId);
		// 收款银行卡账号
		cashTransferPojo.setPayeeBankCardNo(recordx.getBankAccNo());
		// 收款银行账户姓名
		cashTransferPojo.setPayeeBankCustName(recordx.getBankAccName());
		// 收款银行编码
		cashTransferPojo.setPayeeBankType(recordx.getBankType());
		// 收款方虚拟客户号
		cashTransferPojo.setPayeeCustId(recordx.getCustId());
		// 付款银行卡账号
		cashTransferPojo.setPayerBankCardNo(record.getBankAccNo());
		// 付款银行账户姓名
		cashTransferPojo.setPayerBankCustName(record.getBankAccName());
		// 付款方银行类别编码
		cashTransferPojo.setPayerBankType(record.getBankType());
		// 付款方虚拟客户号
		cashTransferPojo.setPayerCustId(record.getCustId());
		cashTransferPojo.setCentralBk(recordx.getBankCode());
		cashTransferPojo.setCity(recordx.getCityCode());
		cashTransferPojo.setPayerCentralBk(record.getBankCode());
		cashTransferPojo.setRemark(remark);
		return cashTransferPojo;
	}

	/**
	 * 获取银行名称
	 * 
	 * @param bankType
	 * @return
	 */
	public String getBankName(String bankType) {
		TbChlBank tbChlBankDto = new TbChlBank();
		tbChlBankDto.setBankType(bankType);
		List<TbChlBank> list = chlBankService.getList(tbChlBankDto);
		if (list != null && list.size() > 0) {
			return list.get(0).getBankName();
		}
		return null;

	}
}
