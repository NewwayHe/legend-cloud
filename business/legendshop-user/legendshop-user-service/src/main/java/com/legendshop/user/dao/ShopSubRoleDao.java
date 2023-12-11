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
import com.legendshop.user.entity.ShopSubRole;
import com.legendshop.user.query.ShopRoleQuery;

import java.util.List;

/**
 * 商家子账号角色Dao
 *
 * @author legendshop
 */
public interface ShopSubRoleDao extends GenericDao<ShopSubRole, Long> {

	/**
	 * 根据用户ID获取店铺子角色列表。
	 *
	 * @param userId 用户ID
	 * @return 店铺子角色列表
	 */
	List<ShopSubRole> getByUserId(Long userId);

	/**
	 * 根据店铺ID获取店铺子角色列表。
	 *
	 * @param shopId 店铺ID
	 * @return 店铺子角色列表
	 */
	List<ShopSubRole> getShopSubRoleByShopId(Long shopId);

	/**
	 * 获取店铺子角色的分页列表。
	 *
	 * @param query 店铺角色查询对象
	 * @return 店铺子角色的分页列表
	 */
	PageSupport<ShopSubRole> getShopSubRolePage(ShopRoleQuery query);

	/**
	 * 根据条件查询所有店铺子角色列表。
	 *
	 * @param query 店铺角色查询对象
	 * @return 满足条件的店铺子角色列表
	 */
	List<ShopSubRole> queryAll(ShopRoleQuery query);


	/**
	 * 删除员工职位
	 *
	 * @param id
	 */
	@Deprecated
	Integer deleteByRoleId(Long id);

	/**
	 * 获取该职位下的所有员工
	 *
	 * @param id
	 * @return
	 */
	List<Long> queryByRoleId(Long id);
}
