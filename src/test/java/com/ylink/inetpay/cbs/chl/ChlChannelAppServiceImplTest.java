package com.ylink.inetpay.cbs.chl;

import org.springframework.beans.factory.annotation.Autowired;

import com.ylink.inetpay.cbs.OtherBaseTest;
import com.ylink.inetpay.cbs.cls.dao.ClsChannelBillDao;
import com.ylink.inetpay.common.project.cbs.app.ChlChannelAppService;
/**
 * 渠道信息测试类
 * @author haha
 *
 */
public class ChlChannelAppServiceImplTest extends OtherBaseTest{
	@Autowired
	private ChlChannelAppService channelAppService;
	@Autowired
	private ClsChannelBillDao  dao;
	/*@Test
	public void findListPageTest() throws CbsCheckedException{
		PageData<TbChlChannelDto> findListPage = channelAppService.findListPage(new PageData<TbChlChannelDto>(), new TbChlChannelDto());
		for (TbChlChannelDto dto : findListPage.getRows()) {
			System.out.println(dto);
		}
	}
	
	@Test
	public void detailsTest() throws CbsCheckedException{
		TbChlChannelDto details = channelAppService.details("0001");
		System.out.println(details);
	}
	
	@Test
	public void getChannelsTest() throws CbsCheckedException{
		List<TbChlChannelDto> channels = channelAppService.getChannels();
		for (TbChlChannelDto tbChlChannelDto : channels) {
			System.out.println(tbChlChannelDto);
		}
	}
	
	@Test
	public void test(){
		ClsChannelBillVo v=new ClsChannelBillVo();
		v.setStartTime(new Date());
		v.setEndTime(new Date());
		dao.queryChannel(null);
	}*/
}
