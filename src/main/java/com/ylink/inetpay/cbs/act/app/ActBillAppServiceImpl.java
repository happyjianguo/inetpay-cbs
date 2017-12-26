package com.ylink.inetpay.cbs.act.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.cbs.act.service.ActBillService;
import com.ylink.inetpay.common.core.constant.EAccountDrCr;
import com.ylink.inetpay.common.core.dto.ReporHeadDto;
import com.ylink.inetpay.common.project.account.dto.ActBillDto;
import com.ylink.inetpay.common.project.cbs.app.CbsActBillAppService;

@Service("cbsActBillAppService")
public class ActBillAppServiceImpl implements CbsActBillAppService {
	@Autowired
	ActBillService actBillService;

	@Override
	public PageData<ActBillDto> queryAllData(PageData<ActBillDto> pageDate,
			ActBillDto actBillDto) {
		return actBillService.queryAllData(pageDate, actBillDto);
	}

	@Override
	public ActBillDto selectByBillId(String billId) {
		return actBillService.selectByBillId(billId);
	}
	@Override
	public ReporHeadDto reportSumData(ActBillDto actBillDto) {
		//所有总笔数和总金额
		ReporHeadDto reporAllDto = new ReporHeadDto();
		//所有借方的总笔数和总金额
		ReporHeadDto reporDrDto = new ReporHeadDto();
		//所有贷方的总笔数和总金额
		ActBillDto crDto = actBillDto;
		ReporHeadDto reporCrDto = new ReporHeadDto();
		reporAllDto = actBillService.reportSumData(actBillDto);
		actBillDto.setDrCrFlag(EAccountDrCr.DR);
		reporDrDto = actBillService.reportSumData(actBillDto);
		if(reporDrDto!=null){
			reporAllDto.setAllDrNum(reporDrDto.getAllNum());
			reporAllDto.setAllDrAmt(reporDrDto.getAllAmt());
		}
		crDto.setDrCrFlag(EAccountDrCr.CR);
		reporCrDto = actBillService.reportSumData(crDto);
		if(reporCrDto!=null){
			reporAllDto.setAllCrNum(reporCrDto.getAllNum());
			reporAllDto.setAllCrAmt(reporCrDto.getAllAmt());
		}
		return reporAllDto;
	}

	@Override
	public PageData<ActBillDto> queryAllDataByPortal(PageData<ActBillDto> pageDate, ActBillDto actBillDto, String subAcctType) {
		return actBillService.queryAllDataByPortal(pageDate, actBillDto,subAcctType);
	}

	@Override
	public ActBillDto selectByCustId(ActBillDto actBillDto) {
		return actBillService.selectByCustId(actBillDto);
	}
}
