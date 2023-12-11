/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.enums.BaseStatusEnum;
import com.legendshop.common.core.expetion.BusinessException;
import com.legendshop.user.dao.ShopSubRoleDao;
import com.legendshop.user.dao.ShopSubRoleMenuDao;
import com.legendshop.user.dto.ShopSubRoleDTO;
import com.legendshop.user.entity.ShopSubRoleMenu;
import com.legendshop.user.query.ShopRoleQuery;
import com.legendshop.user.service.ShopSubRoleService;
import com.legendshop.user.service.convert.ShopSubRoleConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 商家角色服务
 *
 * @author legendshop
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ShopSubRoleServiceImpl implements ShopSubRoleService {

	final ShopSubRoleDao shopSubRoleDao;

	final ShopSubRoleConverter converter;

	final ShopSubRoleMenuDao shopSubRoleMenuDao;

	@Override
	public ShopSubRoleDTO getShopSubRole(Long id) {
		ShopSubRoleDTO dto = converter.to(shopSubRoleDao.getById(id));
		List<ShopSubRoleMenu> roleMenuList = this.shopSubRoleMenuDao.queryByRoleId(dto.getId());
		dto.setMenuIdList(roleMenuList.stream().map(ShopSubRoleMenu::getMenuId).collect(Collectors.toList()));
		return dto;
	}

	@Override
	public R<Void> deleteShopSubRole(Long id) {
		//删除员工对应的职位
		List<Long> shopSubRoleId = shopSubRoleDao.queryByRoleId(id);
		if (CollUtil.isNotEmpty(shopSubRoleId)) {
			throw new BusinessException("删除失败，当前职位正在使用");
		}
		return R.process(this.shopSubRoleDao.deleteById(id) > 0, "没有可删除的记录");
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public R<Long> saveShopSubRole(ShopSubRoleDTO shopSubRoleDTO) {
		if (ObjectUtil.isNotEmpty(shopSubRoleDTO.getId())) {
			this.updateShopSubRole(shopSubRoleDTO);
			return R.ok(shopSubRoleDTO.getId());
		}
		if (null == shopSubRoleDTO.getCreateTime()) {
			shopSubRoleDTO.setCreateTime(new Date());
		}
		if (null == shopSubRoleDTO.getUpdateTime()) {
			shopSubRoleDTO.setUpdateTime(new Date());
		}
		shopSubRoleDTO.setDelFlag(BaseStatusEnum.SUCCESS.status());
		Long roleId = this.shopSubRoleDao.save(converter.from(shopSubRoleDTO));
		if (!CollectionUtils.isEmpty(shopSubRoleDTO.getMenuIdList())) {
			shopSubRoleMenuDao.save(shopSubRoleDTO.getMenuIdList(), roleId);
		}
		return R.ok(roleId);
	}

	@Override
	public R<Void> updateShopSubRole(ShopSubRoleDTO shopRoleDTO) {
		if (null == shopRoleDTO || null == shopRoleDTO.getId()) {
			return R.fail("没有可以更新的记录");
		}
		if (!CollectionUtils.isEmpty(shopRoleDTO.getMenuIdList())) {
			// 简单写法：删除旧关联权限，绑定新权限，需要前端将更新后的所有权限Id传入
			shopSubRoleMenuDao.deleteByRoleId(shopRoleDTO.getId());
			shopSubRoleMenuDao.save(shopRoleDTO.getMenuIdList(), shopRoleDTO.getId());
		}
		return R.process(shopSubRoleDao.update(converter.from(shopRoleDTO)) > 0, "没有可以更新的记录");
	}

	@Override
	public List<ShopSubRoleDTO> getShopSubRolesByShopId(Long shopId) {
		List<ShopSubRoleDTO> dtoList = converter.to(shopSubRoleDao.getShopSubRoleByShopId(shopId));
		List<Long> roleIds = dtoList.stream().map(ShopSubRoleDTO::getId).collect(Collectors.toList());
		List<ShopSubRoleMenu> roleMenuList = this.shopSubRoleMenuDao.queryByRoleIds(roleIds);
		// 根据roleId分组
		Map<Long, List<ShopSubRoleMenu>> listMap = roleMenuList.stream().collect(Collectors.groupingBy(ShopSubRoleMenu::getRoleId));
		dtoList.forEach(e -> {
			e.setMenuIdList(listMap.get(e.getId()).stream().map(ShopSubRoleMenu::getMenuId).collect(Collectors.toList()));
		});
		return dtoList;
	}

	@Override
	public PageSupport<ShopSubRoleDTO> getShopSubRolePage(ShopRoleQuery query) {
		return converter.page(shopSubRoleDao.getShopSubRolePage(query));
	}

	@Override
	public List<ShopSubRoleDTO> queryAll(ShopRoleQuery query) {
		return converter.to(shopSubRoleDao.queryAll(query));
	}
}
