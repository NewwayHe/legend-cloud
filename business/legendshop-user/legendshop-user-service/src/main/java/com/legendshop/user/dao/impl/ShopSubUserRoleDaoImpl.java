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
import com.legendshop.user.dao.ShopSubUserRoleDao;
import com.legendshop.user.entity.ShopSubUserRole;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author legendshop
 */
@Repository
public class ShopSubUserRoleDaoImpl extends GenericDaoImpl<ShopSubUserRole, Long> implements ShopSubUserRoleDao {


	@Override
	public List<Long> save(List<Long> roleIds, Long userId) {
		if (null == userId || CollectionUtils.isEmpty(roleIds)) {
			return new ArrayList<>();
		}
		List<ShopSubUserRole> list = new ArrayList<>(roleIds.size());
		roleIds.forEach(e -> {
			ShopSubUserRole shopSubUserRole = new ShopSubUserRole();
			shopSubUserRole.setRoleId(e);
			shopSubUserRole.setShopSubUserId(userId);
			list.add(shopSubUserRole);
		});
		return super.save(list);
	}

	@Override
	public int del(Long userId) {
		return super.update("DELETE FROM ls_shop_sub_user_role WHERE shop_sub_user_id = ?", userId);
	}
}
