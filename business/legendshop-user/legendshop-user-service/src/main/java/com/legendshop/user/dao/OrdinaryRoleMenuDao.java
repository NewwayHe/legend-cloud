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
import com.legendshop.user.entity.OrdinaryRoleMenu;

import java.util.List;

/**
 * @author legendshop
 */
public interface OrdinaryRoleMenuDao extends GenericDao<OrdinaryRoleMenu, Long> {
	void deleteByRoleId(Long roleId);

	void batchSave(List<OrdinaryRoleMenu> roleMenuList);
}
