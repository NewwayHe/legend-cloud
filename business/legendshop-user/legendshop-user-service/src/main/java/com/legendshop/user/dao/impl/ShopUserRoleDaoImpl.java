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
import com.legendshop.user.dao.ShopUserRoleDao;
import com.legendshop.user.entity.ShopUserRole;
import org.springframework.stereotype.Repository;

/**
 * @author legendshop
 */
@Repository
public class ShopUserRoleDaoImpl extends GenericDaoImpl<ShopUserRole, Long> implements ShopUserRoleDao {


	@Override
	public Integer removeByUserIdAndRoleId(Long userId, Long roleId) {
		String sql = "delete from  ls_shop_user_role r where r.user_id = ? and r.role_id = ?";
		return update(sql, userId, roleId);
	}

	@Override
	public Integer removeByUserId(Long userId) {
		String sql = "delete from ls_shop_user_role r where r.user_id = ?";
		return update(sql, userId);
	}

}
