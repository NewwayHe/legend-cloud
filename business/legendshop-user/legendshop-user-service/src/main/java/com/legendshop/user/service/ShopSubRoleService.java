/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.service;

import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.common.core.constant.R;
import com.legendshop.user.dto.ShopSubRoleDTO;
import com.legendshop.user.query.ShopRoleQuery;

import java.util.List;

/**
 * 商家角色服务
 *
 * @author legendshop
 */
public interface ShopSubRoleService {

	/**
	 * 获取指定商店子角色的详细信息。
	 *
	 * @param id 商店子角色ID
	 * @return 包含指定商店子角色详细信息的对象
	 */
	ShopSubRoleDTO getShopSubRole(Long id);

	/**
	 * 删除指定商店子角色。
	 *
	 * @param id 商店子角色ID
	 * @return 操作结果，表示删除是否成功
	 */
	R<Void> deleteShopSubRole(Long id);

	/**
	 * 保存商店子角色信息。
	 *
	 * @param shopRoleDto 商店子角色DTO对象
	 * @return 保存的商店子角色ID
	 */
	R<Long> saveShopSubRole(ShopSubRoleDTO shopRoleDto);

	/**
	 * 更新商店子角色信息。
	 *
	 * @param shopRoleDto 商店子角色DTO对象
	 * @return 操作结果，表示更新是否成功
	 */
	R<Void> updateShopSubRole(ShopSubRoleDTO shopRoleDto);

	/**
	 * 根据商店ID获取该商店下的所有子角色列表。
	 *
	 * @param shopId 商店ID
	 * @return 包含指定商店下所有子角色的列表
	 */
	List<ShopSubRoleDTO> getShopSubRolesByShopId(Long shopId);

	/**
	 * 根据查询条件获取商店子角色的分页列表。
	 *
	 * @param query 商店角色查询条件
	 * @return 包含分页商店子角色结果的PageSupport对象
	 */
	PageSupport<ShopSubRoleDTO> getShopSubRolePage(ShopRoleQuery query);

	/**
	 * 根据查询条件获取商店子角色列表。
	 *
	 * @param query 商店角色查询条件
	 * @return 包含符合查询条件的商店子角色列表
	 */
	List<ShopSubRoleDTO> queryAll(ShopRoleQuery query);

}
