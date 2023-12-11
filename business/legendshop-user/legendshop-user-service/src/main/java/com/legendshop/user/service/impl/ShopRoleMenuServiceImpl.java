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
import com.legendshop.common.core.comparer.DataComparer;
import com.legendshop.common.core.comparer.DataComparerResult;
import com.legendshop.common.core.comparer.DataListComparer;
import com.legendshop.common.core.constant.CacheConstants;
import com.legendshop.user.dao.ShopRoleMenuDao;
import com.legendshop.user.entity.ShopRoleMenu;
import com.legendshop.user.service.ShopRoleMenuService;
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
public class ShopRoleMenuServiceImpl implements ShopRoleMenuService {
	final ShopRoleMenuDao roleMenuDao;

	@Override
	@Transactional(rollbackFor = Exception.class)
	@CacheEvict(value = CacheConstants.SHOP_MENU_DETAILS, key = "#roleId + '_SHOP_menu'")
	public Boolean saveRoleMenus(String roleCode, Long roleId, String menuIds) {


		if (StrUtil.isBlank(menuIds)) {
			return Boolean.TRUE;
		}

		List<ShopRoleMenu> dbRoleMenuList = roleMenuDao.getShopRoleMenuByRoleId(roleId);

		//收集菜单和角色列表
		List<ShopRoleMenu> roleMenuList = Arrays.stream(menuIds.split(",")).map(menuId -> {
			ShopRoleMenu roleMenu = new ShopRoleMenu();
			roleMenu.setRoleId(roleId);
			roleMenu.setMenuId(Long.valueOf(menuId));
			return roleMenu;
		}).collect(Collectors.toList());


		//不要先删除，再重新插入，应该对比一下那些更改再决定是否删除，新增。 newway
		DataListComparer<ShopRoleMenu, ShopRoleMenu> comparer = new DataListComparer<ShopRoleMenu, ShopRoleMenu>(new ShopRoleMenuComparer());

		DataComparerResult<ShopRoleMenu> compareResult = comparer.compare(roleMenuList, dbRoleMenuList, null);
		roleMenuDao.save(compareResult.getAddList());
		roleMenuDao.delete(compareResult.getDelList());

		return Boolean.TRUE;
	}

	/**
	 * 用于对比
	 */
	private static class ShopRoleMenuComparer implements DataComparer<ShopRoleMenu, ShopRoleMenu> {

		@Override
		public boolean needUpdate(ShopRoleMenu dto, ShopRoleMenu dbObj, Object obj) {
			return false;
		}

		@Override
		public boolean isExist(ShopRoleMenu dto, ShopRoleMenu dbObj) {
			return dto.getRoleId().equals(dbObj.getRoleId()) && dto.getMenuId().equals(dbObj.getMenuId());
		}

		@Override
		public ShopRoleMenu copyProperties(ShopRoleMenu dtoj, Object obj) {
			return dtoj;
		}
	}
}
