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
import com.legendshop.user.entity.OrdinaryRole;
import com.legendshop.user.query.OrdinaryRoleQuery;

import java.util.List;

/**
 * 账号角色Dao
 *
 * @author legendshop
 */
public interface OrdinaryRoleDao extends GenericDao<OrdinaryRole, Long> {


	List<OrdinaryRole> getByUserId(Long userId);

	PageSupport<OrdinaryRole> queryRolePage(OrdinaryRoleQuery query);

	List<OrdinaryRole> queryAll(OrdinaryRoleQuery query);

	List<OrdinaryRole> queryByUserId(Long id);
}
