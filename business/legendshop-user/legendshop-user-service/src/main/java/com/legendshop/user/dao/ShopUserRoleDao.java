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
import com.legendshop.user.entity.ShopUserRole;

/**
 * @author legendshop
 */
public interface ShopUserRoleDao extends GenericDao<ShopUserRole, Long> {

	/**
	 * 根据用户ID和角色ID移除用户角色
	 *
	 * @param userId
	 * @param roleId
	 * @return
	 */
	Integer removeByUserIdAndRoleId(Long userId, Long roleId);

	/**
	 * 移除用户关联的角色
	 *
	 * @param userId
	 */
	Integer removeByUserId(Long userId);
}
