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
import com.legendshop.user.dao.OrdinaryRoleMenuDao;
import com.legendshop.user.entity.OrdinaryRoleMenu;
import com.legendshop.user.service.OrdinaryRoleMenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author legendshop
 */
@Service
@RequiredArgsConstructor
public class OrdinaryRoleMenuServiceImpl implements OrdinaryRoleMenuService {
	final OrdinaryRoleMenuDao roleMenuDao;

	@Override
	@Transactional(rollbackFor = Exception.class)
	@CacheEvict(value = CacheConstants.USER_MENU_DETAILS, key = "#roleId + '_ORDINARY_menu'")
	public Boolean saveRoleMenus(String roleCode, Long roleId, String menuIds) {
		//首先删除角色原有权限
		roleMenuDao.deleteByRoleId(roleId);

		if (StrUtil.isBlank(menuIds)) {
			return Boolean.TRUE;
		}
		//收集菜单和角色列表
		List<OrdinaryRoleMenu> roleMenuList = Arrays.stream(menuIds.split(",")).map(menuId -> {
			OrdinaryRoleMenu roleMenu = new OrdinaryRoleMenu();
			roleMenu.setRoleId(roleId);
			roleMenu.setMenuId(Long.valueOf(menuId));
			return roleMenu;
		}).collect(Collectors.toList());
		roleMenuDao.batchSave(roleMenuList);
		return Boolean.TRUE;
	}
}
