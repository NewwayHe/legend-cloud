/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.service.impl;

import cn.hutool.core.util.StrUtil;
import com.legendshop.common.core.constant.CacheConstants;
import com.legendshop.user.dao.AdminRoleMenuDao;
import com.legendshop.user.entity.AdminRoleMenu;
import com.legendshop.user.service.AdminRoleMenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 角色菜单的实现
 *
 * @author legendshop
 */
@Service
@RequiredArgsConstructor
public class AdminRoleMenuServiceImpl implements AdminRoleMenuService {

	final AdminRoleMenuDao roleMenuDao;

	@Override
	@Transactional(rollbackFor = Exception.class)
	@CacheEvict(value = CacheConstants.ADMIN_MENU_DETAILS, key = "#roleId + '_admin_menu'")
	public Boolean saveRoleMenus(String roleCode, Long roleId, String menuIds) {
		//首先删除角色原有权限
		roleMenuDao.deleteByRoleId(roleId);

		if (StrUtil.isBlank(menuIds)) {
			return Boolean.TRUE;
		}
		//收集菜单和角色列表
		List<AdminRoleMenu> roleMenuList = Arrays.stream(menuIds.split(",")).map(menuId -> {
			AdminRoleMenu roleMenu = new AdminRoleMenu();
			roleMenu.setRoleId(roleId);
			roleMenu.setMenuId(Long.valueOf(menuId));
			return roleMenu;
		}).collect(Collectors.toList());
		roleMenuDao.batchSave(roleMenuList);
		return Boolean.TRUE;
	}
}
