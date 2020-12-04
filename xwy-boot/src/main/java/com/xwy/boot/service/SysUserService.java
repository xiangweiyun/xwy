package com.xwy.boot.service;

import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xwy.entity.SysUser;

/**
 * <p>
 * 系统_用户 服务类
 * </p>
 *
 * @author xiangwy
 * @since 2020-12-01
 */
public interface SysUserService extends IService<SysUser> {

	/**
	 * 新增用户
	 * 
	 * @author: xiangwy
	 * @date: 2020-12-03 15:49:53
	 * @param sysUser
	 */
	public void add(SysUser sysUser);

	/**
	 * 重置用户密码
	 * 
	 * @author: xiangwy
	 * @date: 2020-12-03 09:21:54
	 * @param id
	 */
	public void resetPwd(Long id);

	/**
	 * 用户修改密码
	 * 
	 * @author: xiangwy
	 * @date: 2020-12-03 09:22:01
	 * @param id
	 * @param newpwd
	 */
	public void updatePwd(Long id, String newpwd);

	/**
	 * 根据username和password查询
	 * 
	 * @param username
	 * @param password
	 * @return
	 */
	public SysUser getByUsernameAndPassword(String username, String password);

	/**
	 * 根据email和password查询
	 * 
	 * @param email
	 * @param password
	 * @return
	 */
	public SysUser getByEmailAndPassword(String email, String password);

	/**
	 * 根据mobile和password查询
	 * 
	 * @param mobile
	 * @param password
	 * @return
	 */
	public SysUser getByMobileAndPassword(String mobile, String password);

	/**
	 * 获取登录后的信息
	 * 
	 * @author: xiangwy
	 * @date: 2020-12-03 09:22:12
	 * @param userId
	 * @param menuPerms 是否获取菜单权限
	 * @return
	 */
	public Map<String, Object> getLoginDatas(Long userId, boolean menuPerms);
}
