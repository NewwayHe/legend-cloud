/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.legendshop.jpaplus.support.Sorting;
import com.legendshop.common.core.constant.CacheConstants;
import com.legendshop.common.core.constant.CommonConstants;
import com.legendshop.common.core.constant.R;
import com.legendshop.user.bo.MenuBO;
import com.legendshop.user.dao.ShopMenuDao;
import com.legendshop.user.dto.MenuTree;
import com.legendshop.user.dto.ShopMenuDTO;
import com.legendshop.user.dto.TreeSelectMenuDTO;
import com.legendshop.user.entity.ShopMenu;
import com.legendshop.user.service.ShopMenuService;
import com.legendshop.user.service.convert.ShopMenuConverter;
import com.legendshop.user.utils.TreeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author legendshop
 */
@Service
@RequiredArgsConstructor
public class ShopMenuServiceImpl implements ShopMenuService {

	final ShopMenuDao shopMenuDao;

	final ShopMenuConverter converter;

	@Override
	@Cacheable(value = CacheConstants.SHOP_MENU_DETAILS, key = "#roleId +'_'+ #userType + '_menu'", unless = "#result.isEmpty()")
	public List<MenuBO> getByRoleId(Long roleId, String userType) {
		return this.shopMenuDao.getByRoleId(roleId, userType);
	}

	@Override
	public R<ShopMenuDTO> getById(Long menuId) {
		return R.ok(converter.to(this.shopMenuDao.getById(menuId)));
	}

	@Override
	public R<List<ShopMenuDTO>> queryAll() {
		return R.ok(converter.to(shopMenuDao.queryAll()));
	}

	@Override
	@CacheEvict(value = CacheConstants.SHOP_MENU_DETAILS, allEntries = true)
	public R<Long> save(ShopMenuDTO menu) {
		return R.ok(this.shopMenuDao.save(converter.from(menu)));
	}

	@Override
	@CacheEvict(value = CacheConstants.SHOP_MENU_DETAILS, allEntries = true)
	public R<Void> update(ShopMenuDTO menu) {
		if (null == menu || null == menu.getId()) {
			return R.fail("更新Id不能为空");
		}
		return R.process(this.shopMenuDao.update(converter.from(menu)) > 0, "没有需要更新的记录");
	}

	@Override
	@CacheEvict(value = CacheConstants.SHOP_MENU_DETAILS, allEntries = true)
	public R<Void> del(Long menuId) {
		return R.process(this.shopMenuDao.deleteById(menuId) > 0, "没有需要删除的记录");
	}

	@Override
	public List<MenuTree> treeMenu(Boolean lazy, Long parentId) {
		if (!lazy) {
			return TreeUtil.buildTree(converter.toMenuBOList(this.shopMenuDao.queryAll(new Sorting(Sorting.Ordering.asc("sort")))),
					CommonConstants.MENU_TREE_ROOT_ID);
		}

		Long parent = parentId == null ? CommonConstants.MENU_TREE_ROOT_ID : parentId;
		return TreeUtil.buildTree(converter.toMenuBOList(this.shopMenuDao.getByParentId(parentId)), parent);
	}

	@Override
	public List<TreeSelectMenuDTO> queryShopMenuTreeSelectList() {
		List<ShopMenu> shopMenus = shopMenuDao.queryByType(CommonConstants.FAIL.toString());
		List<ShopMenuDTO> shopMenuDTOList = converter.to(shopMenus);

		// 递归转换成树结构
		List<ShopMenuDTO> convertResult = convert2ShopTree(shopMenuDTOList);
		return converter.convert2TreeSelectShopMenuDTOList(convertResult);
	}

	private List<ShopMenuDTO> convert2ShopTree(List<ShopMenuDTO> rootList) {
		List<ShopMenuDTO> nodeList = new ArrayList<>();
		for (ShopMenuDTO shopMenu : rootList) {
			//表明是一级父类
			if (shopMenu.getParentId().longValue() == CommonConstants.CATEGORY_TREE_ROOT_ID) {
				nodeList.add(shopMenu);
			}
			// 有下级才添加
			List<ShopMenuDTO> shopMenuDTOS = setChild(shopMenu.getId(), rootList);
			if (CollectionUtil.isNotEmpty(shopMenuDTOS)) {
				shopMenu.setChildren(shopMenuDTOS);
			}
		}
		return nodeList;
	}

	private static List<ShopMenuDTO> setChild(Long id, List<ShopMenuDTO> rootList) {
		List<ShopMenuDTO> childList = new ArrayList<>();
		for (ShopMenuDTO shopMenuDTO : rootList) {
			if (id.longValue() == shopMenuDTO.getParentId()) {
				childList.add(shopMenuDTO);
			}
		}
		for (ShopMenuDTO shopMenuDTO : childList) {
			shopMenuDTO.setChildren(setChild(shopMenuDTO.getId(), rootList));
		}
		if (childList.isEmpty()) {
			return null;
		}
		return childList;
	}
}
