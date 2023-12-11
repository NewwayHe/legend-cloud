/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.service;

import com.legendshop.common.core.constant.R;
import com.legendshop.user.bo.MenuBO;
import com.legendshop.user.dto.MenuTree;
import com.legendshop.user.dto.ShopMenuDTO;
import com.legendshop.user.dto.TreeSelectMenuDTO;

import java.util.List;

/**
 * @author legendshop
 */
public interface ShopMenuService {

	List<MenuBO> getByRoleId(Long roleId, String userType);

	R<ShopMenuDTO> getById(Long menuId);


	R<List<ShopMenuDTO>> queryAll();

	R<Long> save(ShopMenuDTO menu);

	R<Void> update(ShopMenuDTO menu);

	R<Void> del(Long menuId);

	/**
	 * 加载树形菜单
	 *
	 * @param lazy
	 * @param parentId
	 * @return
	 */
	List<MenuTree> treeMenu(Boolean lazy, Long parentId);

	/**
	 * 获取商家菜单树结构集合
	 *
	 * @return
	 */
	List<TreeSelectMenuDTO> queryShopMenuTreeSelectList();

}
