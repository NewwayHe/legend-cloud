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
import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.user.entity.ShopRole;
import com.legendshop.user.query.ShopRoleQuery;

import java.util.List;

/**
 * 商家子账号角色Dao
 *
 * @author legendshop
 */
public interface ShopRoleDao extends GenericDao<ShopRole, Long> {


	List<ShopRole> getByUserId(Long userId);

	PageSupport<ShopRole> queryRolePage(ShopRoleQuery query);

	List<ShopRole> queryAll(ShopRoleQuery query);
}
