/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.dao;

import cn.legendshop.jpaplus.GenericDao;
import com.legendshop.user.entity.ShopSubUserRole;

import java.util.List;

/**
 * 商家子账号角色对应
 *
 * @author legendshop
 */
public interface ShopSubUserRoleDao extends GenericDao<ShopSubUserRole, Long> {

	List<Long> save(List<Long> roleIds, Long userId);

	int del(Long userId);
}
