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
import com.legendshop.user.bo.OrdinaryRoleBO;
import com.legendshop.user.dto.OrdinaryRoleDTO;
import com.legendshop.user.query.OrdinaryRoleQuery;

import java.util.List;

/**
 * @author legendshop
 */
public interface OrdinaryRoleService extends BaseService<OrdinaryRoleDTO> {

	/**
	 * 分页查询所有角色
	 *
	 * @param roleQuery the roleQuery
	 * @return pageList
	 */
	PageSupport<OrdinaryRoleDTO> page(OrdinaryRoleQuery roleQuery);


	List<OrdinaryRoleDTO> queryAll(OrdinaryRoleQuery roleQuery);

	List<OrdinaryRoleDTO> queryByUserId(Long id);

	/**
	 * 根据用户ID和角色ID移除用户角色
	 *
	 * @param userId
	 * @param roleId
	 * @return
	 */
	R<Void> removeByUserId(Long userId, Long roleId);

	/**
	 * 获取角色选择弹窗数据
	 *
	 * @param ordinaryRoleQuery
	 * @return
	 */
	R<PageSupport<OrdinaryRoleBO>> getRolePageByUserId(OrdinaryRoleQuery ordinaryRoleQuery);

	/**
	 * 更新指定用户关联角色
	 *
	 * @param userId
	 * @param roleIds
	 * @return
	 */
	R<Void> updateRoleForUser(Long userId, List<Long> roleIds);
}
