/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.service.impl;

import cn.legendshop.jpaplus.support.Sorting;
import com.legendshop.common.core.constant.CacheConstants;
import com.legendshop.common.core.constant.CommonConstants;
import com.legendshop.common.core.constant.R;
import com.legendshop.user.bo.MenuBO;
import com.legendshop.user.dao.OrdinaryMenuDao;
import com.legendshop.user.dto.MenuTree;
import com.legendshop.user.dto.OrdinaryMenuDTO;
import com.legendshop.user.service.OrdinaryMenuService;
import com.legendshop.user.service.convert.OrdinaryMenuConverter;
import com.legendshop.user.utils.TreeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author legendshop
 */
@Service
@RequiredArgsConstructor
public class OrdinaryMenuServiceImpl implements OrdinaryMenuService {

	final OrdinaryMenuDao ordinaryMenuDao;

	final OrdinaryMenuConverter converter;

	@Override
	@Cacheable(value = CacheConstants.USER_MENU_DETAILS, key = "#roleId  + '_ORDINARY_menu'", unless = "#result.isEmpty()")
	public List<MenuBO> getByRoleId(Long roleId) {
		return this.ordinaryMenuDao.getByRoleId(roleId);
	}

	@Override
	public R<OrdinaryMenuDTO> getById(Long menuId) {
		return R.ok(converter.to(this.ordinaryMenuDao.getById(menuId)));
	}

	@Override
	public R<List<OrdinaryMenuDTO>> queryAll() {
		return R.ok(converter.to(ordinaryMenuDao.queryAll()));
	}

	@Override
	public R<Long> save(OrdinaryMenuDTO menu) {
		Date now = new Date();
		menu.setCreateTime(now);
		menu.setUpdateTime(now);
		menu.setHiddenFlag(Boolean.FALSE);
		return R.ok(this.ordinaryMenuDao.save(converter.from(menu)));
	}

	@Override
	public R<Void> update(OrdinaryMenuDTO menu) {
		if (null == menu || null == menu.getId()) {
			return R.fail("更新Id不能为空");
		}
		return R.process(this.ordinaryMenuDao.update(converter.from(menu)) > 0, "没有需要更新的记录");
	}

	@Override
	public R<Void> del(Long menuId) {
		return R.process(this.ordinaryMenuDao.deleteById(menuId) > 0, "没有需要删除的记录");
	}

	@Override
	public List<MenuTree> treeMenu(Boolean lazy, Long parentId) {
		if (!lazy) {
			return TreeUtil.buildTree(converter.toMenuBOList(this.ordinaryMenuDao.queryAll(new Sorting(Sorting.Ordering.asc("sort")))),
					CommonConstants.MENU_TREE_ROOT_ID);
		}

		Long parent = parentId == null ? CommonConstants.MENU_TREE_ROOT_ID : parentId;
		return TreeUtil.buildTree(converter.toMenuBOList(this.ordinaryMenuDao.getByParentId(parentId)), parent);
	}

	@Override
	public List<MenuBO> queryByRoleId(List<Long> roleIds) {
		return this.ordinaryMenuDao.getByRoleIds(roleIds);
	}
}
