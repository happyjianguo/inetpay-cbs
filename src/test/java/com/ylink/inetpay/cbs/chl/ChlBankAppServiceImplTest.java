package com.ylink.inetpay.cbs.chl;

import org.springframework.beans.factory.annotation.Autowired;

import com.ylink.inetpay.cbs.OtherBaseTest;
import com.ylink.inetpay.cbs.chl.cache.TbChlBankDtoCache;
import com.ylink.inetpay.common.project.cbs.app.ChlBankAppService;

/**
 * 资金渠道银行信息表
 * @author haha
 *
 */
public class ChlBankAppServiceImplTest extends OtherBaseTest{
	@Autowired
	private ChlBankAppService chlBankAppService;
	@Autowired
	private TbChlBankDtoCache tbChlBankDtoCache;
	
	/*//@Test
	public void findListPageTest() throws CbsCheckedException{
		TbChlBankDto details = chlBankAppService.details("0001");
		PageData<TbChlBankDto> findListPage = chlBankAppService.findListPage(new PageData<TbChlBankDto>(),details);
		for (TbChlBankDto dto : findListPage.getRows()) {
			System.out.println(dto);
		}
	}
	
	//@Test
	public void detailsTest() throws CbsCheckedException{
		TbChlBankDto details = chlBankAppService.details("0001");
		System.out.println(details);
	}
	
	//@Test
	public void getBanksTest() throws CbsCheckedException{
		List<TbChlBankDto> banks = chlBankAppService.getBanks();
		for (TbChlBankDto tbChlBankDto : banks) {
			System.out.println(tbChlBankDto);
		}
	}
	//@Test
	public void findByBankTypeTest() throws CbsCheckedException{
		TbChlBankDto findByBankType = chlBankAppService.findByBankType(EBankType.BANKTYPE_WEIXIN);
		System.out.println(findByBankType);
	}
	//@Test
	public void findByChlBankPropTest() throws CbsCheckedException{
		List<TbChlBankDto> findByChlBankProp = chlBankAppService.findByChlBankProp(EChlBankProp.JOIN_BANK);
		for (TbChlBankDto tbChlBankDto : findByChlBankProp) {
			System.out.println(tbChlBankDto);
		}
	}
	@Test
	public void getBankTest() throws CbsCheckedException{
		List<TbChlBankDto> lista=tbChlBankDtoCache.getBanks();
		lista=tbChlBankDtoCache.getBanks();
		List<TbChlBankDto> listc=null;
		for(TbChlBankDto a:lista)
		{
			TbChlBankDto bankDto=chlBankAppService.findByBankType(a.getBankType());
			bankDto.setBankName(bankDto.getBankName());
			chlBankAppService.updateBank(bankDto);
			listc=tbChlBankDtoCache.getBanks();
			bankDto=tbChlBankDtoCache.selectByBankType(bankDto.getBankType());
			System.out.println(bankDto);
		}
		for(TbChlBankDto c:listc)
		{
			System.out.println(c);
		}
	}*/
}
