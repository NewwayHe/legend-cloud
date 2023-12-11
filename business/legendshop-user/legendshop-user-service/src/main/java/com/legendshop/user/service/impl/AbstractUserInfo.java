/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.service.impl;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.security.enums.LegendshopSecurityErrorEnum;
import com.legendshop.user.bo.MenuBO;
import com.legendshop.user.dto.SysUserDTO;
import com.legendshop.user.dto.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 用户信息抽象类
 *
 * @author legendshop
 */
@Slf4j
public abstract class AbstractUserInfo {

	protected abstract List<Long> getRoleIds(Long userId);

	protected abstract List<MenuBO> getMenuBO(List<Long> roleIds);

	protected abstract SysUserDTO getSysUser(String username);

	/**
	 * 构建用户信息
	 *
	 * @param username 用户名
	 * @return userInfo
	 */
	public R<UserInfo> buildUserInfo(String username) throws UsernameNotFoundException {
		UserInfo userInfo = new UserInfo();
		SysUserDTO sysUser = getSysUser(username);
		if (ObjectUtil.isNull(sysUser)) {
			log.error("登录用户不存在！username ：{}", username);
			return R.fail(LegendshopSecurityErrorEnum.USER_NOT_FOUND.name());
		}
		userInfo.setSysUser(sysUser);
		List<Long> roleIds = this.getRoleIds(sysUser.getId());

		List<MenuBO> menuList = this.getMenuBO(roleIds);

		//设置角色权限
		userInfo.setRoles(ArrayUtil.toArray(roleIds, Long.class));
		if (CollectionUtils.isEmpty(menuList)) {
			userInfo.setPermissions(new String[]{});
			return R.ok(userInfo);
		}

		Set<String> permissions = menuList.stream().filter(
				menu -> StrUtil.isNotBlank(menu.getPermission()))
				.flatMap(menuBO -> Arrays.stream(menuBO.getPermission().split(","))).collect(Collectors.toSet());


		// 设置权限列表（menu.permission）
		userInfo.setPermissions(ArrayUtil.toArray(permissions, String.class));
		return R.ok(userInfo);
	}

}
