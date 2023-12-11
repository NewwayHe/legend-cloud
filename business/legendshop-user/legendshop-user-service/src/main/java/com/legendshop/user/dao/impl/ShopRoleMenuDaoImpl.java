/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.dao.impl;

import cn.legendshop.jpaplus.impl.GenericDaoImpl;
import cn.legendshop.jpaplus.support.lambda.LambdaEntityCriterion;
import com.legendshop.user.dao.ShopRoleMenuDao;
import com.legendshop.user.entity.ShopRoleMenu;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author legendshop
 */
@Repository
public class ShopRoleMenuDaoImpl extends GenericDaoImpl<ShopRoleMenu, Long> implements ShopRoleMenuDao {
	@Override
	public void deleteByRoleId(Long roleId) {
		super.update("delete from ls_shop_role_menu where role_id = ?", roleId);
	}

	@Override
	public void batchSave(List<ShopRoleMenu> roleMenuList) {
		super.save(roleMenuList);
	}

	@Override
	public List<ShopRoleMenu> getShopRoleMenuByRoleId(Long roleId) {
		return queryByProperties(new LambdaEntityCriterion<>(ShopRoleMenu.class).eq(ShopRoleMenu::getRoleId, roleId));
	}
}
