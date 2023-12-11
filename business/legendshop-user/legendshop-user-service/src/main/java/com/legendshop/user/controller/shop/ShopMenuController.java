/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.controller.shop;

import cn.hutool.core.util.ObjectUtil;
import com.legendshop.common.core.constant.CommonConstants;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.security.utils.SecurityUtils;
import com.legendshop.user.bo.MenuBO;
import com.legendshop.user.dto.MenuTree;
import com.legendshop.user.service.ShopMenuService;
import com.legendshop.user.utils.TreeUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 商家菜单管理
 *
 * @author legendshop
 */
@Tag(name = "商家菜单管理")
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/shop/menu", produces = MediaType.APPLICATION_JSON_VALUE)
public class ShopMenuController {

	private final ShopMenuService shopMenuService;

	/**
	 * 查询商家菜单
	 *
	 * @return
	 */
	@GetMapping
	public R<List<MenuTree>> getShopMenu(String type, Long parentId) {
		// 获取符合条件的菜单
		Set<MenuBO> all = new HashSet<>();
		SecurityUtils.getRoles().forEach(roleId -> all.addAll(shopMenuService.getByRoleId(roleId, SecurityUtils.getUserType())));
		return R.ok(TreeUtil.filterMenu(all, type, parentId));
	}

	/**
	 * 返回树形菜单集合
	 *
	 * @return 树形菜单
	 */
	@GetMapping(value = "/tree")
	@PreAuthorize("@pms.hasPermission('s_shop_menu_get')")
	@Operation(summary = "【商家】返回商家菜单树形菜单集合 s_shop_menu_get")
	public R<List<MenuTree>> getTree() {
		// 获取符合条件的菜单
		Set<MenuBO> all = new HashSet<>();
		SecurityUtils.getRoles().forEach(roleId -> all.addAll(shopMenuService.getByRoleId(roleId, SecurityUtils.getUserType())));
		return R.ok(TreeUtil.buildTree(all.stream().sorted((o1, o2) -> {
			if (ObjectUtil.isAllNotEmpty(o1.getSort(), o2.getSort())) {
				return o1.getSort().compareTo(o2.getSort());
			}
			return ObjectUtil.isNotEmpty(o1.getSort()) ? 1 : -1;
		}).collect(Collectors.toList()), CommonConstants.MENU_TREE_ROOT_ID));
	}
}
