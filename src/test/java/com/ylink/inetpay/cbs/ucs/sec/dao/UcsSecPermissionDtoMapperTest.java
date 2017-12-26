package com.ylink.inetpay.cbs.ucs.sec.dao;

import static org.junit.Assert.fail;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ylink.inetpay.cbs.UCBaseTest;

public class UcsSecPermissionDtoMapperTest extends UCBaseTest {

		
	@Autowired
	UcsSecPermissionDtoMapper secPermissionDtoMapper;

	@Test
	public void testDeleteByPrimaryKey() {
		fail("Not yet implemented");
	}

	@Test
	public void testInsert() {
		fail("Not yet implemented");
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
	public void testListAll() {
		fail("Not yet implemented");
	}

	@Test
	public void testListPerms() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetPermsByRoles() {
		Set<String> roles = new HashSet<String>();
		roles.add("SUPER_ROLE");
		roles = secPermissionDtoMapper.getPermsByRoles(roles);
		System.out.println("roles.size:" + roles.size());
	}

	@Test
	public void testGetPermDtosByRoles() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetUserTopMenus() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetOuterTopMenus() {
		fail("Not yet implemented");
	}

}
