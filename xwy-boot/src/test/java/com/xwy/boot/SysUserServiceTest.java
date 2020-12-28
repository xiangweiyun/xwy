package com.xwy.boot;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.xwy.boot.service.SysUserService;
import com.xwy.entity.SysUser;

/**
 * 急救患者信息服务测试.
 * 
 * @author xiangweiyun
 * @date: 2019-09-24 15:44:41
 * @Copyright: Copyright (c) 2020
 * @Company: XWY有限公司
 * @Version: V1.0
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class SysUserServiceTest {
	@Autowired
	private SysUserService sysUserService;

	@Test
	public void save() {
		SysUser sysUser = new SysUser();
		sysUser.setUsername("xdd4");
		sysUser.setName("向大大4");
		sysUserService.add(sysUser);
	}
}