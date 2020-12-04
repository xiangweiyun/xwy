package com.xwy.boot.service.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xwy.boot.mapper.SysUserMapper;
import com.xwy.boot.service.SysUserService;
import com.xwy.common.constant.SysConstant;
import com.xwy.common.exception.XwyException;
import com.xwy.common.utils.PasswordHash;
import com.xwy.common.utils.SecurityUtils;
import com.xwy.common.utils.StringUtils;
import com.xwy.entity.SysUser;

/**
 * <p>
 * 系统_用户 服务实现类
 * </p>
 *
 * @author xiangwy
 * @since 2020-12-01
 */
@Service
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, readOnly = false)
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

	private static final Logger log = LoggerFactory.getLogger(SysUserServiceImpl.class);

	@Override
	public void add(SysUser sysUser) {
		try {
			if (StringUtils.isBlank(sysUser.getPassword())) {
				sysUser.setPassword(PasswordHash.createHash(SysConstant.INIT_PWD));
			} else {
				sysUser.setPassword(PasswordHash.createHash(sysUser.getPassword()));
			}
		} catch (Exception e) {
			log.error("用户密码加密错误");
			throw new XwyException("用户密码加密错误");
		}
		super.save(sysUser);
	}

	@Override
	public void resetPwd(Long id) {
		SysUser user = new SysUser();
		user.setId(id);
		try {
			user.setPassword(PasswordHash.createHash(SysConstant.INIT_PWD));
		} catch (Exception e) {
			log.error("用户密码加密错误");
			throw new XwyException("用户密码加密错误");
		}
		super.updateById(user);
	}

	@Override
	public void updatePwd(Long id, String newpwd) {
		SysUser user = new SysUser();
		user.setId(id);
		user.setPassword(SecurityUtils.MD5Encode(newpwd));
	}

	@Override
	public SysUser getByUsernameAndPassword(String username, String password) {
		QueryWrapper<SysUser> wrapper = Wrappers.query();
		wrapper.eq(SysUser.USERNAME, username);
		try {
			wrapper.eq(SysUser.PASSWORD, PasswordHash.createHash((password)));
		} catch (Exception e) {
			log.error("用户密码加密错误");
			throw new XwyException("用户密码加密错误");
		}
		return super.getOne(wrapper);
	}

	@Override
	public SysUser getByEmailAndPassword(String email, String password) {
		QueryWrapper<SysUser> wrapper = Wrappers.query();
		wrapper.eq(SysUser.EMAIL, email);
		try {
			wrapper.eq(SysUser.PASSWORD, PasswordHash.createHash((password)));
		} catch (Exception e) {
			log.error("用户密码加密错误");
			throw new XwyException("用户密码加密错误");
		}
		return super.getOne(wrapper);
	}

	@Override
	public SysUser getByMobileAndPassword(String mobile, String password) {
		QueryWrapper<SysUser> wrapper = Wrappers.query();
		wrapper.eq(SysUser.MOBILE, mobile);
		try {
			wrapper.eq(SysUser.PASSWORD, PasswordHash.createHash((password)));
		} catch (Exception e) {
			log.error("用户密码加密错误");
			throw new XwyException("用户密码加密错误");
		}
		return super.getOne(wrapper);
	}

	@Override
	public Map<String, Object> getLoginDatas(Long userId, boolean menuPerms) {
		Map<String, Object> rtDatas = new HashMap<String, Object>();
		SysUser sysUser = super.getById(userId);
		rtDatas.put("userInfo", sysUser);
		return rtDatas;
	}

	/**
	 * roleids 拼接成字符串 ‘id1’，‘id2’的格式
	 * 
	 * @author: xiangwy
	 * @date: 2020-12-03 10:01:18
	 * @param roleIdsroleIds
	 * @return
	 */
	@SuppressWarnings("unused")
	private String coverRoleIds(Set<String> roleIdsroleIds) {
		StringBuffer sb = new StringBuffer();
		for (String roleId : roleIdsroleIds) {
			sb.append("'").append(roleId).append("',");
		}
		String rt = sb.toString();
		if (rt.contains(",")) {
			rt = rt.substring(0, rt.lastIndexOf(","));
		}
		return rt;
	}

}
