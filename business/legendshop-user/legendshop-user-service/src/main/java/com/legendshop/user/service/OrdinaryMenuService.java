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
import com.legendshop.user.dto.OrdinaryMenuDTO;

import java.util.List;

/**
 * @author legendshop
 */
public interface OrdinaryMenuService {

	List<MenuBO> getByRoleId(Long roleId);


	R<OrdinaryMenuDTO> getById(Long menuId);


	R<List<OrdinaryMenuDTO>> queryAll();

	R<Long> save(OrdinaryMenuDTO menu);

	R<Void> update(OrdinaryMenuDTO menu);

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
	 * 根据角色ID获取菜单
	 *
	 * @param roleIds
	 * @return
	 */
	List<MenuBO> queryByRoleId(List<Long> roleIds);
}
