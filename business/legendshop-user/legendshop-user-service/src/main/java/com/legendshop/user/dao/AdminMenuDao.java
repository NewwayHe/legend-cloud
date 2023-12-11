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
import com.legendshop.user.bo.MenuBO;
import com.legendshop.user.entity.AdminMenu;

import java.util.List;

/**
 * 后台菜单Dao
 *
 * @author legendshop
 */
public interface AdminMenuDao extends GenericDao<AdminMenu, Long> {

	List<MenuBO> getByRoleId(Long roleId);

	List<MenuBO> getByRoleIds(List<Long> roleIds);

	List<AdminMenu> getByParentId(Long parentId);

	List<AdminMenu> queryByType(String type);
}
