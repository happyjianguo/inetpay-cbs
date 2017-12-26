package com.ylink.inetpay.cbs.ucs.sec.dao;
import static org.junit.Assert.fail;

import java.util.Date;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ylink.inetpay.cbs.UCBaseTest;
import com.ylink.inetpay.common.core.constant.EUcsSecUserStatus;
import com.ylink.inetpay.common.project.cbs.dto.ucs.sec.UcsSecUserDto;
public class UcsSecUserDtoMapperTest extends UCBaseTest {

	@Autowired
	private UcsSecUserDtoMapper ucsSecUserDtoMapper;
	
	@Test
	public void testDeleteByPrimaryKey() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testInsert() {
		for(int i=1 ; i <1 ; i++){
			UcsSecUserDto record = new UcsSecUserDto();
			record.setLoginName("test" +i);
			record.setRealName("test"  +i);
			record.setPassword("122");
			record.setMobile("123");
			record.setEmail("11@qq.com");
			record.setStatus(EUcsSecUserStatus.NORMAL);
			record.setCreater("test");
			record.setCreateTime(new Date());
			ucsSecUserDtoMapper.insert(record);
		}
	}

	@Test
	public void testInsertSelective() {
		fail("Not yet implemented");
	}

	@Test
	public void testSelectByPrimaryKey() {
		fail("Not yet implemented");
	}

	@Test
	public void testUpdateByPrimaryKeySelective() {
		fail("Not yet implemented");
	}

	@Test
	public void testUpdateByPrimaryKey() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetByLoginName() {
		UcsSecUserDto dto = ucsSecUserDtoMapper.getByLoginName("root");
		Assert.assertEquals("307EDCF7A1E32967E050A8C09A0158A9", dto.getId());
	}

	@Test
	public void testCheckLoginName() {
		fail("Not yet implemented");
	}

	@Test
	public void testCheckEmail() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetWithRoles() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetWithRolesByLoginName() {
		fail("Not yet implemented");
	}

	@Test
	public void testListWidthRoles() {
		fail("Not yet implemented");
	}

	@Test
	public void testListWidthRolesPage() {
		fail("Not yet implemented");
	}

	@Test
	public void testListWidthRolesPageCount() {
		fail("Not yet implemented");
	}

	@Test
	public void testAddUserRoles() {
		fail("Not yet implemented");
	}

	@Test
	public void testDeleteUserRoles() {
		fail("Not yet implemented");
	}

	@Test
	public void testUpdateLockTime() {
		fail("Not yet implemented");
	}

	@Test
	public void testUpdateStatus() {
		fail("Not yet implemented");
	}

}
