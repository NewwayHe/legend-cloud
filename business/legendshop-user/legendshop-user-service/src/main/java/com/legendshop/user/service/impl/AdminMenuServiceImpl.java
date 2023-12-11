/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.service.impl;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.legendshop.jpaplus.support.Sorting;
import com.legendshop.common.core.constant.CacheConstants;
import com.legendshop.common.core.constant.CommonConstants;
import com.legendshop.common.core.constant.R;
import com.legendshop.user.bo.MenuBO;
import com.legendshop.user.dao.AdminMenuDao;
import com.legendshop.user.dao.AdminRoleMenuDao;
import com.legendshop.user.dto.AdminMenuDTO;
import com.legendshop.user.dto.MenuTree;
import com.legendshop.user.dto.TreeSelectMenuDTO;
import com.legendshop.user.entity.AdminMenu;
import com.legendshop.user.service.AdminMenuService;
import com.legendshop.user.service.convert.AdminMenuConverter;
import com.legendshop.user.utils.TreeUtil;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 后台菜单管理器
 *
 * @author legendshop
 */
@Service
@AllArgsConstructor
public class AdminMenuServiceImpl implements AdminMenuService {

	private final AdminMenuDao adminMenuDao;
	private final AdminRoleMenuDao roleMenuDao;
	private final AdminMenuConverter adminMenuConverter;

	@Override
	@Cacheable(value = CacheConstants.ADMIN_MENU_DETAILS, key = "#roleId  + '_admin_menu'", unless = "#result.isEmpty()")
	public List<MenuBO> getByRoleId(Long roleId) {
		return adminMenuDao.getByRoleId(roleId);
	}

	@Override
	public List<MenuTree> treeMenu(boolean lazy, Long parentId) {
		if (!lazy) {
			return TreeUtil.buildTree(adminMenuConverter.toMenuBOList(adminMenuDao.queryAll(new Sorting(Sorting.Ordering.asc("sort")))),
					CommonConstants.MENU_TREE_ROOT_ID);
		}

		Long parent = parentId == null ? CommonConstants.MENU_TREE_ROOT_ID : parentId;
		return TreeUtil.buildTree(adminMenuConverter.toMenuBOList(
				adminMenuDao.getByParentId(parentId)),
				parent);
	}

	@Override
	@CacheEvict(value = CacheConstants.ADMIN_MENU_DETAILS, allEntries = true)
	public void save(AdminMenuDTO adminMenuDTO) {
		adminMenuDTO.setCreateTime(DateUtil.date());
		adminMenuDao.save(adminMenuConverter.from(adminMenuDTO));
	}

	@Override
	@CacheEvict(value = CacheConstants.ADMIN_MENU_DETAILS, allEntries = true)
	public R update(AdminMenuDTO adminMenuDTO) {
		adminMenuDTO.setUpdateTime(DateUtil.date());
		return R.ok(adminMenuDao.update(adminMenuConverter.from(adminMenuDTO)));
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	@CacheEvict(value = CacheConstants.ADMIN_MENU_DETAILS, allEntries = true)
	public R deleteById(Long id) {
		// 查询父节点为当前节点的节点
		List<AdminMenu> adminMenuList = adminMenuDao.getByParentId(id);
		if (CollUtil.isNotEmpty(adminMenuList)) {
			return R.fail("菜单含有下级不能删除");
		}
		roleMenuDao.deleteByMenuId(id);
		// 删除当前菜单及其子菜单
		return R.ok(adminMenuDao.deleteById(id));
	}

	@Override
	public List<TreeSelectMenuDTO> queryAdminMenuTreeSelectList() {

		List<AdminMenu> adminMenus = adminMenuDao.queryByType(CommonConstants.FAIL.toString());
		List<AdminMenuDTO> adminMenuDTOS = adminMenuConverter.to(adminMenus);

		// 递归转换成树结构
		List<AdminMenuDTO> convertResult = convert2AdminTree(adminMenuDTOS);
		return adminMenuConverter.convert2TreeSelectAdminMenuDTOList(convertResult);
	}

	private List<AdminMenuDTO> convert2AdminTree(List<AdminMenuDTO> rootList) {
		List<AdminMenuDTO> nodeList = new ArrayList<>();
		for (AdminMenuDTO adminMenu : rootList) {
			//表明是一级父类
			if (adminMenu.getParentId().longValue() == CommonConstants.CATEGORY_TREE_ROOT_ID) {
				nodeList.add(adminMenu);
			}
			// 有下级才添加
			List<AdminMenuDTO> adminMenuDTOS = setChild(adminMenu.getId(), rootList);
			if (CollectionUtil.isNotEmpty(adminMenuDTOS)) {
				adminMenu.setChildren(adminMenuDTOS);
			}
		}
		return nodeList;
	}

	private static List<AdminMenuDTO> setChild(Long id, List<AdminMenuDTO> rootList) {
		List<AdminMenuDTO> childList = new ArrayList<>();
		for (AdminMenuDTO adminMenuDTO : rootList) {
			if (id.longValue() == adminMenuDTO.getParentId()) {
				childList.add(adminMenuDTO);
			}
		}
		for (AdminMenuDTO adminMenuDTO : childList) {
			adminMenuDTO.setChildren(setChild(adminMenuDTO.getId(), rootList));
		}
		if (childList.isEmpty()) {
			return null;
		}
		return childList;
	}


}
