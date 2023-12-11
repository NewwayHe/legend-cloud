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
import com.legendshop.user.dao.OrdinaryRoleMenuDao;
import com.legendshop.user.entity.OrdinaryRoleMenu;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author legendshop
 */
@Repository
public class OrdinaryRoleMenuDaoImpl extends GenericDaoImpl<OrdinaryRoleMenu, Long> implements OrdinaryRoleMenuDao {
	@Override
	public void deleteByRoleId(Long roleId) {
		super.update("delete from ls_ordinary_role_menu where role_id = ?", roleId);
	}

	@Override
	public void batchSave(List<OrdinaryRoleMenu> roleMenuList) {
		super.save(roleMenuList);
	}
}
