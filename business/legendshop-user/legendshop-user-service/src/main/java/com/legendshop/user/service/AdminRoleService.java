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
import com.legendshop.common.core.service.BaseService;
import com.legendshop.user.dto.AdminRoleDTO;
import com.legendshop.user.query.RoleQuery;

import java.util.List;

/**
 * @author legendshop
 */
public interface AdminRoleService extends BaseService<AdminRoleDTO> {

	/**
	 * 分页查询所有角色
	 *
	 * @param roleQuery
	 * @return
	 */
	PageSupport<AdminRoleDTO> page(RoleQuery roleQuery);


	List<AdminRoleDTO> queryAll();

}
