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
import com.legendshop.user.dto.AdminMenuDTO;
import com.legendshop.user.dto.MenuTree;
import com.legendshop.user.dto.TreeSelectMenuDTO;

import java.util.List;

/**
 * 菜单管理服务.
 *
 * @author legendshop
 */
public interface AdminMenuService {


	/**
	 * 根据角色id查询菜单
	 *
	 * @param roleId
	 * @return
	 */
	List<MenuBO> getByRoleId(Long roleId);

	/**
	 * 加载树形菜单
	 *
	 * @param lazy
	 * @param parentId
	 * @return
	 */
	List<MenuTree> treeMenu(boolean lazy, Long parentId);

	/**
	 * 保存菜单
	 *
	 * @param adminMenuDTO
	 */
	void save(AdminMenuDTO adminMenuDTO);


	/**
	 * 更新菜单
	 *
	 * @param adminMenuDTO
	 * @return
	 */
	R update(AdminMenuDTO adminMenuDTO);

	/**
	 * 根据id删除菜单
	 *
	 * @param id
	 * @return
	 */
	R deleteById(Long id);

	/**
	 * 获取平台菜单树结构集合
	 *
	 * @return
	 */
	List<TreeSelectMenuDTO> queryAdminMenuTreeSelectList();
}
