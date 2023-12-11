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
import cn.hutool.core.util.ObjectUtil;
import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.enums.BaseStatusEnum;
import com.legendshop.user.bo.ShopRoleBO;
import com.legendshop.user.dao.ShopRoleDao;
import com.legendshop.user.dao.ShopSubRoleMenuDao;
import com.legendshop.user.dao.ShopUserRoleDao;
import com.legendshop.user.dto.ShopRoleDTO;
import com.legendshop.user.entity.ShopRole;
import com.legendshop.user.entity.ShopSubRoleMenu;
import com.legendshop.user.entity.ShopUserRole;
import com.legendshop.user.query.ShopRoleQuery;
import com.legendshop.user.service.ShopRoleService;
import com.legendshop.user.service.convert.ShopRoleConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 商家角色服务
 *
 * @author legendshop
 */
@Service
@RequiredArgsConstructor
public class ShopRoleServiceImpl implements ShopRoleService {

	final ShopRoleDao shopRoleDao;

	final ShopRoleConverter converter;

	final ShopSubRoleMenuDao shopSubRoleMenuDao;

	final ShopUserRoleDao shopUserRoleDao;

	@Override
	public ShopRoleDTO getShopRole(Long id) {
		ShopRoleDTO dto = converter.to(shopRoleDao.getById(id));
		List<ShopSubRoleMenu> roleMenuList = this.shopSubRoleMenuDao.queryByRoleId(dto.getId());
		dto.setMenuIdList(roleMenuList.stream().map(ShopSubRoleMenu::getMenuId).collect(Collectors.toList()));
		return dto;
	}

	@Override
	public R<Void> deleteShopRole(Long id) {
		return R.process(this.shopRoleDao.deleteById(id) > 0, "没有可删除的记录");
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public R<Long> saveShopRole(ShopRoleDTO shopSubRoleDTO) {
		if (ObjectUtil.isNotEmpty(shopSubRoleDTO.getId())) {
			this.updateShopRole(shopSubRoleDTO);
			return R.ok(shopSubRoleDTO.getId());
		}
		if (null == shopSubRoleDTO.getCreateTime()) {
			shopSubRoleDTO.setCreateTime(new Date());
		}
		if (null == shopSubRoleDTO.getUpdateTime()) {
			shopSubRoleDTO.setUpdateTime(new Date());
		}
		shopSubRoleDTO.setDelFlag(BaseStatusEnum.SUCCESS.status());
		Long roleId = this.shopRoleDao.save(converter.from(shopSubRoleDTO));
		if (!CollectionUtils.isEmpty(shopSubRoleDTO.getMenuIdList())) {
			shopSubRoleMenuDao.save(shopSubRoleDTO.getMenuIdList(), roleId);
		}
		return R.ok(roleId);
	}

	@Override
	public R<Void> updateShopRole(ShopRoleDTO shopRoleDTO) {
		if (null == shopRoleDTO || null == shopRoleDTO.getId()) {
			return R.fail("没有可以更新的记录");
		}
		if (!CollectionUtils.isEmpty(shopRoleDTO.getMenuIdList())) {
			// 简单写法：删除旧关联权限，绑定新权限，需要前端将更新后的所有权限Id传入
			shopSubRoleMenuDao.deleteByRoleId(shopRoleDTO.getId());
			shopSubRoleMenuDao.save(shopRoleDTO.getMenuIdList(), shopRoleDTO.getId());
		}
		return R.process(shopRoleDao.update(converter.from(shopRoleDTO)) > 0, "没有可以更新的记录");
	}

	@Override
	public PageSupport<ShopRoleDTO> getShopRolePage(ShopRoleQuery query) {
		return converter.page(shopRoleDao.queryRolePage(query));
	}

	@Override
	public List<ShopRoleDTO> queryAll(ShopRoleQuery query) {
		return converter.to(shopRoleDao.queryAll(query));
	}

	@Override
	public List<ShopRoleDTO> getByUserId(Long shopUserId) {
		return converter.to(shopRoleDao.getByUserId(shopUserId));
	}


	@Override
	public R<Void> removeByUserId(Long userId, Long roleId) {
		Integer result = shopUserRoleDao.removeByUserIdAndRoleId(userId, roleId);
		if (result <= 0) {
			return R.fail("移除失败");
		}
		return R.ok();
	}

	@Override
	public R<PageSupport<ShopRoleBO>> getRolePageByUserId(ShopRoleQuery shopRoleQuery) {

		PageSupport<ShopRole> rolePageSupport = shopRoleDao.queryRolePage(shopRoleQuery);
		PageSupport<ShopRoleBO> pageSupportR = converter.toBoPage(rolePageSupport);
		List<ShopRoleBO> resultList = pageSupportR.getResultList();
		if (CollectionUtil.isNotEmpty(resultList)) {
			List<ShopRole> shopRoleList = shopRoleDao.getByUserId(shopRoleQuery.getUserId());
			resultList.forEach(e -> {
				shopRoleList.forEach(r -> {
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
		Integer result = shopUserRoleDao.removeByUserId(userId);
		if (result < 0) {
			return R.fail("关联角色失败，请重试");
		}
		// 更新关联角色
		for (Long roleId : roleIds) {
			Long saveResult = shopUserRoleDao.save(new ShopUserRole(userId, roleId));
			if (saveResult <= 0) {
				return R.fail("关联角色失败，请重试");
			}
		}
		return R.ok();
	}
}
