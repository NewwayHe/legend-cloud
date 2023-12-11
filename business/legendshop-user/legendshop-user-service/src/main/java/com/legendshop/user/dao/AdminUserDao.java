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
import com.legendshop.user.entity.AdminUser;
import com.legendshop.user.query.AdminUserQuery;
import com.legendshop.user.vo.AdminUserVO;

import java.util.List;

/**
 * 管理员Dao.
 *
 * @author legendshop
 */
public interface AdminUserDao extends GenericDao<AdminUser, Long> {

	Long save(AdminUser adminUser, String[] roleIds);

	PageSupport<AdminUserVO> page(AdminUserQuery adminUserQuery);

	AdminUser getUserByIdUsername(Long userId, String username);

	AdminUser getUserByUsername(String username);

	List<Long> queryUserByMenuId(Long menuId);

	/**
	 * 根据菜单ID查询具有菜单权限的所有平台用户ID
	 *
	 * @param menuName 菜单ID
	 * @return
	 */
	List<Long> queryUserIdsByMenuName(String menuName);

	List<Long> getAdmin();

	int updateStatusByUserName(String username, Boolean status);

}
