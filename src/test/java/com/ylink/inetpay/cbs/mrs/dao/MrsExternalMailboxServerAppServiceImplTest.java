package com.ylink.inetpay.cbs.mrs.dao;

import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.ylinkpay.framework.core.model.PageData;
import com.ylink.inetpay.cbs.MRBaseTest;
import com.ylink.inetpay.common.project.cbs.app.MrsExternalMailboxServerAppService;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsExternalMailboxServerDto;
import com.ylink.inetpay.common.project.cbs.exception.CbsCheckedException;

public class MrsExternalMailboxServerAppServiceImplTest extends MRBaseTest{
	@Autowired
	private MrsExternalMailboxServerAppService mrsExternalMailboxServerAppService;
	@Test
	public void insertTest() throws CbsCheckedException{
		MrsExternalMailboxServerDto mrsExternalMailboxServerDto = new MrsExternalMailboxServerDto();
		mrsExternalMailboxServerDto.setCreater("yanggang");
		mrsExternalMailboxServerDto.setCreaterName("杨刚");
		mrsExternalMailboxServerDto.setRemarks("创建外部邮箱");
		mrsExternalMailboxServerDto.setServerUrl("qq邮箱 ");
		mrsExternalMailboxServerDto.setSuffi("qq.com");
		mrsExternalMailboxServerAppService.insert(mrsExternalMailboxServerDto);
	}
	@Test
	public void updateTest() throws CbsCheckedException{
		MrsExternalMailboxServerDto details = mrsExternalMailboxServerAppService.details("da7ba094-98f1-4e93-8c56-3bb421fcb7af");
		details.setReviser("yangqiang");
		details.setReviserName("杨强");
		details.setRemarks("163邮箱");
		details.setServerUrl("http://www.163.com");
		mrsExternalMailboxServerAppService.update(details);
	}
	@Test
	public void listTest() throws CbsCheckedException{
		MrsExternalMailboxServerDto details = mrsExternalMailboxServerAppService.details("da7ba094-98f1-4e93-8c56-3bb421fcb7af");
		details.setStartCreateTime(new Date());
		details.setEndUpdateTime(new Date());
		PageData<MrsExternalMailboxServerDto> findListPage = mrsExternalMailboxServerAppService.findListPage(new PageData<MrsExternalMailboxServerDto>(), details);
		for (MrsExternalMailboxServerDto dto : findListPage.getRows()) {
			System.out.println(dto);
		}
	}
	@Test
	public void getServerPath() throws CbsCheckedException{
		String serverPath = mrsExternalMailboxServerAppService.getServerPath("qq.com");
		System.out.println(serverPath);
	}
	@Test
	public void  deleteTest() throws CbsCheckedException{
		mrsExternalMailboxServerAppService.delete("da7ba094-98f1-4e93-8c56-3bb421fcb7af");
	}
}
