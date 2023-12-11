/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.service.impl;

import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.common.core.service.impl.BaseServiceImpl;
import com.legendshop.user.dao.AdminRoleDao;
import com.legendshop.user.dao.AdminRoleMenuDao;
import com.legendshop.user.dto.AdminRoleDTO;
import com.legendshop.user.query.RoleQuery;
import com.legendshop.user.service.AdminRoleService;
import com.legendshop.user.service.convert.AdminRoleConverter;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 角色实现类
 *
 * @author legendshop
 */
@Service
@AllArgsConstructor
public class AdminRoleServiceImpl extends BaseServiceImpl<AdminRoleDTO, AdminRoleDao, AdminRoleConverter> implements AdminRoleService {

	private final AdminRoleDao adminRoleDao;
	private final AdminRoleMenuDao roleMenuDao;
	private final AdminRoleConverter roleConverter;

	@Override
	public PageSupport<AdminRoleDTO> page(RoleQuery roleQuery) {
		return roleConverter.page(adminRoleDao.page(roleQuery));
	}

	@Override
	public List<AdminRoleDTO> queryAll() {
		return roleConverter.to(adminRoleDao.queryAll());
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Boolean deleteById(Long id) {
		roleMenuDao.deleteByRoleId(id);
		return super.deleteById(id);
	}
}
