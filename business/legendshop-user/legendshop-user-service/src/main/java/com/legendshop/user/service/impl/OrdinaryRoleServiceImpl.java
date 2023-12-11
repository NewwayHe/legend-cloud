/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.service.impl.BaseServiceImpl;
import com.legendshop.user.bo.OrdinaryRoleBO;
import com.legendshop.user.dao.OrdinaryRoleDao;
import com.legendshop.user.dao.OrdinaryRoleMenuDao;
import com.legendshop.user.dao.OrdinaryUserRoleDao;
import com.legendshop.user.dto.OrdinaryRoleDTO;
import com.legendshop.user.entity.OrdinaryRole;
import com.legendshop.user.entity.OrdinaryUserRole;
import com.legendshop.user.query.OrdinaryRoleQuery;
import com.legendshop.user.service.OrdinaryRoleService;
import com.legendshop.user.service.convert.OrdinaryRoleConverter;
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
public class OrdinaryRoleServiceImpl extends BaseServiceImpl<OrdinaryRoleDTO, OrdinaryRoleDao, OrdinaryRoleConverter> implements OrdinaryRoleService {

	private final OrdinaryRoleDao ordinaryRoleDao;
	private final OrdinaryUserRoleDao ordinaryUserRoleDao;
	private final OrdinaryRoleMenuDao roleMenuDao;
	private final OrdinaryRoleConverter roleConverter;

	@Override
	public PageSupport<OrdinaryRoleDTO> page(OrdinaryRoleQuery roleQuery) {
		return roleConverter.page(ordinaryRoleDao.queryRolePage(roleQuery));
	}

	@Override
	public List<OrdinaryRoleDTO> queryAll(OrdinaryRoleQuery roleQuery) {
		return roleConverter.to(ordinaryRoleDao.queryAll(roleQuery));
	}

	@Override
	public List<OrdinaryRoleDTO> queryByUserId(Long id) {
		return roleConverter.to(ordinaryRoleDao.queryByUserId(id));
	}

	@Override
	public R<Void> removeByUserId(Long userId, Long roleId) {
		Integer result = ordinaryUserRoleDao.removeByUserIdAndRoleId(userId, roleId);
		if (result <= 0) {
			return R.fail("移除失败");
		}
		return R.ok();
	}

	@Override
	public R<PageSupport<OrdinaryRoleBO>> getRolePageByUserId(OrdinaryRoleQuery ordinaryRoleQuery) {

		PageSupport<OrdinaryRole> rolePageSupport = ordinaryRoleDao.queryRolePage(ordinaryRoleQuery);
		PageSupport<OrdinaryRoleBO> pageSupportR = roleConverter.toBoPage(rolePageSupport);
		List<OrdinaryRoleBO> resultList = pageSupportR.getResultList();
		if (CollectionUtil.isNotEmpty(resultList)) {
			List<OrdinaryRole> ordinaryRoleList = ordinaryRoleDao.getByUserId(ordinaryRoleQuery.getUserId());
			resultList.forEach(e -> {
				ordinaryRoleList.forEach(r -> {
					if (e.getId().equals(r.getId())) {
						e.setSelectFlag(true);
					}
				});
			});
		}
		return R.ok(pageSupportR);
	}

	@Override
	public R<Void> updateRoleForUser(Long userId, List<Long> roleIds) {

		// 移除关联角色
		Integer result = ordinaryUserRoleDao.removeByUserId(userId);
		if (result < 0) {
			return R.fail("关联角色失败，请重试");
		}
		// 更新关联角色
		for (Long roleId : roleIds) {
			Long saveResult = ordinaryUserRoleDao.save(new OrdinaryUserRole(userId, roleId));
			if (saveResult <= 0) {
				return R.fail("关联角色失败，请重试");
			}
		}
		return R.ok();
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Boolean deleteById(Long id) {
		roleMenuDao.deleteByRoleId(id);
		return super.deleteById(id);
	}
}
