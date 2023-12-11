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
import com.legendshop.user.bo.ShopRoleBO;
import com.legendshop.user.dto.ShopRoleDTO;
import com.legendshop.user.query.ShopRoleQuery;

import java.util.List;

/**
 * 商家角色服务
 *
 * @author legendshop
 */
public interface ShopRoleService {
	/**
	 * 获取店铺角色信息。
	 *
	 * @param id 角色ID
	 * @return 店铺角色信息
	 */
	ShopRoleDTO getShopRole(Long id);

	/**
	 * 删除店铺角色。
	 *
	 * @param id 角色ID
	 * @return 删除操作结果
	 */
	R<Void> deleteShopRole(Long id);

	/**
	 * 保存店铺角色。
	 *
	 * @param shopRoleDto 店铺角色DTO
	 * @return 保存的店铺角色ID
	 */
	R<Long> saveShopRole(ShopRoleDTO shopRoleDto);

	/**
	 * 更新店铺角色。
	 *
	 * @param shopRoleDto 店铺角色DTO
	 * @return 更新操作结果
	 */
	R<Void> updateShopRole(ShopRoleDTO shopRoleDto);

	/**
	 * 获取店铺角色分页列表。
	 *
	 * @param query 店铺角色查询对象
	 * @return 店铺角色的分页列表
	 */
	PageSupport<ShopRoleDTO> getShopRolePage(ShopRoleQuery query);

	/**
	 * 根据条件查询所有店铺角色列表。
	 *
	 * @param query 店铺角色查询对象
	 * @return 满足条件的店铺角色列表
	 */
	List<ShopRoleDTO> queryAll(ShopRoleQuery query);

	/**
	 * 根据店铺用户ID获取店铺角色列表。
	 *
	 * @param shopUserId 店铺用户ID
	 * @return 店铺用户对应的角色列表
	 */
	List<ShopRoleDTO> getByUserId(Long shopUserId);


	/**
	 * 根据用户ID和角色ID移除用户角色
	 *
	 * @param userId
	 * @param roleId
	 * @return
	 */
	R<Void> removeByUserId(Long userId, Long roleId);

	/**
	 * 获取角色选择弹窗数据
	 *
	 * @param ordinaryRoleQuery
	 * @return
	 */
	R<PageSupport<ShopRoleBO>> getRolePageByUserId(ShopRoleQuery ordinaryRoleQuery);

	/**
	 * 更新指定用户关联角色
	 *
	 * @param userId
	 * @param roleIds
	 * @return
	 */
	R<Void> updateRoleForUser(Long userId, List<Long> roleIds);
}
