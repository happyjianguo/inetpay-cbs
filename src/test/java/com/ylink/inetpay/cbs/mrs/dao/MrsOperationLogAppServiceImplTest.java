package com.ylink.inetpay.cbs.mrs.dao;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.cbs.MRBaseTest;
import com.ylink.inetpay.common.core.constant.EMrsOperationLogType;
import com.ylink.inetpay.common.project.cbs.app.MrsOperationLogAppService;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsOperationLogDto;
import com.ylink.inetpay.common.project.cbs.exception.CbsCheckedException;

/**
 * 用户操作 日志测试类
 * @author haha
 *
 */
public class MrsOperationLogAppServiceImplTest extends MRBaseTest{
	@Autowired
	private MrsOperationLogAppService mrsOperationLogAppService;
	@Test
	public void pageList()throws CbsCheckedException{
		PageData<MrsOperationLogDto> pageList = mrsOperationLogAppService.pageList(new PageData<MrsOperationLogDto>(), new MrsOperationLogDto());
		for (MrsOperationLogDto dto : pageList.getRows()) {
			System.out.println(dto);
		}
	}
	@Test
	public void add()throws CbsCheckedException{
		MrsOperationLogDto mrsOperationLogDto = new MrsOperationLogDto();
		mrsOperationLogDto.setLoginName("yanggang");
		mrsOperationLogDto.setOperateType(EMrsOperationLogType.OPEN);
		mrsOperationLogDto.setIp("192.168.123.1");
		mrsOperationLogDto.setDescription("备注");
		mrsOperationLogAppService.add(mrsOperationLogDto);
	}
}
