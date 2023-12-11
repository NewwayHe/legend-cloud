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
import com.legendshop.common.core.service.BaseService;
import com.legendshop.user.dto.AdminUserDTO;
import com.legendshop.user.dto.UserInfo;
import com.legendshop.user.query.AdminUserQuery;
import com.legendshop.user.vo.AdminUserVO;

import java.util.List;

/**
 * 管理员服务.
 *
 * @author legendshop
 */
public interface AdminUserService extends BaseService<AdminUserDTO> {

	/**
	 * 分页查询所有管理员
	 *
	 * @param adminUserQuery
	 * @return
	 */
	PageSupport<AdminUserVO> page(AdminUserQuery adminUserQuery);

	/**
	 * 构建管理员的UserInfo给登录用
	 *
	 * @param username
	 * @return
	 */
	R<UserInfo> getUserInfo(String username);

	/**
	 * 根据类目id查看有权限访问的所有后台管理员id
	 *
	 * @param menuId
	 * @return
	 */
	R<List<Long>> queryUserByMenuId(Long menuId);

	/**
	 * 根据菜单名字查询具有菜单权限的所有平台用户ID
	 *
	 * @param menuName 菜单名字
	 * @return
	 */
	List<Long> queryUserIdsByMenuName(String menuName);

	List<Long> getAdmin();

	R updateStatusByUserName(String username, Boolean status);
}
