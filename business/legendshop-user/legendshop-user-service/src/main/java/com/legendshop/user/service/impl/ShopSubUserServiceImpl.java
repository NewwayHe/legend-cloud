/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.legendshop.jpaplus.support.EntityCriterion;
import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.enums.BaseStatusEnum;
import com.legendshop.common.core.expetion.BusinessException;
import com.legendshop.user.bo.MenuBO;
import com.legendshop.user.dao.*;
import com.legendshop.user.dto.ShopSubUserDTO;
import com.legendshop.user.dto.SysUserDTO;
import com.legendshop.user.dto.UserInfo;
import com.legendshop.user.entity.ShopSubRole;
import com.legendshop.user.entity.ShopSubUser;
import com.legendshop.user.entity.ShopSubUserRole;
import com.legendshop.user.entity.ShopUser;
import com.legendshop.user.query.ShopSubUserQuery;
import com.legendshop.user.service.ShopSubUserService;
import com.legendshop.user.service.convert.ShopSubUserConverter;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 商家用户服务
 *
 * @author legendshop
 */
@Service
@AllArgsConstructor
public class ShopSubUserServiceImpl extends AbstractUserInfo implements ShopSubUserService {

	private final ShopUserDao shopUserDao;
	private final ShopMenuDao shopMenuDao;
	private final ShopSubUserDao shopSubUserDao;
	private final ShopSubRoleDao shopSubRoleDao;
	private final ShopSubUserRoleDao shopSubUserRoleDao;
	private final ShopSubUserConverter converter;
	private static final PasswordEncoder ENCODER = new BCryptPasswordEncoder();


	@Override
	public List<ShopSubUserDTO> getShopSubUseByShopId(Long shopId) {
		return this.converter.to(this.shopSubUserDao.queryByProperties(new EntityCriterion().eq("shopId", shopId)));
	}

	@Override
	public List<ShopSubUserDTO> queryAllShopSubUser() {
		return this.converter.to(this.shopSubUserDao.queryAll());
	}

	@Override
	public ShopSubUserDTO getShopSubUser(Long id) {
		ShopSubUserDTO userDTO = this.converter.to(this.shopSubUserDao.getById(id));
		if (null == userDTO) {
			return null;
		}
		List<ShopSubUserRole> subUserRoles = shopSubUserRoleDao.queryByProperties(new EntityCriterion().eq("shopSubUserId", id));
		if (!CollectionUtils.isEmpty(subUserRoles)) {
			userDTO.setRoleIds(subUserRoles.stream().map(ShopSubUserRole::getRoleId).collect(Collectors.toList()));
		}
		return userDTO;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public R<Long> saveShopSubUser(ShopSubUserDTO shopUserDto) {
		if (null == shopUserDto) {
			return null;
		}
		if (null == shopUserDto.getShopUserId()) {
			throw new BusinessException("shopUserId is null");
		}
		if (StringUtils.isBlank(shopUserDto.getPassword())) {
			throw new BusinessException("password is null");
		}
		if (StringUtils.isBlank(shopUserDto.getUsername())) {
			throw new BusinessException("username is null");
		}
		if (null == shopUserDto.getDelFlag()) {
			shopUserDto.setDelFlag(BaseStatusEnum.SUCCESS.status());
		}
		if (null == shopUserDto.getLockFlag()) {
			shopUserDto.setLockFlag(BaseStatusEnum.SUCCESS.status());
		}
		if (null == shopUserDto.getCreateTime()) {
			shopUserDto.setCreateTime(new Date());
		}
		if (null == shopUserDto.getUpdateTime()) {
			shopUserDto.setUpdateTime(new Date());
		}
		// 密码加密
		shopUserDto.setPassword(ENCODER.encode(shopUserDto.getPassword()));
		//判断是否存在员工账号
		ShopSubUser shopSubUser = shopSubUserDao.getUserAccount(shopUserDto.getUserAccount(), shopUserDto.getShopUserId());
		if (ObjectUtil.isNotEmpty(shopSubUser)) {
			throw new BusinessException("存在重复的员工账号");
		}
		Long userId = this.shopSubUserDao.save(this.converter.from(shopUserDto));
		if (!CollectionUtils.isEmpty(shopUserDto.getRoleIds())) {
			shopSubUserRoleDao.save(shopUserDto.getRoleIds(), userId);
		}
		return R.ok(userId);
	}

	@Override
	public R<Void> updateShopSubUser(ShopSubUserDTO shopUserDto) {
		if (null == shopUserDto || null == shopUserDto.getId()) {
			return R.fail("数据异常");
		}
		ShopSubUser shopSubUser = shopSubUserDao.getById(shopUserDto.getId());
		if (ObjectUtil.isNull(shopSubUser)) {
			return R.fail("员工信息不存在或已被删除，请返回刷新重试");
		}
		shopSubUser.setUserAccount(shopUserDto.getUserAccount());
		shopSubUser.setUsername(shopUserDto.getUsername());
		shopSubUser.setLockFlag(shopUserDto.getLockFlag());
		shopSubUser.setDelFlag(shopUserDto.getDelFlag());
		shopSubUser.setUpdateTime(new Date());
		if (!CollectionUtils.isEmpty(shopUserDto.getRoleIds())) {
			this.shopSubUserRoleDao.del(shopUserDto.getId());
			this.shopSubUserRoleDao.save(shopUserDto.getRoleIds(), shopUserDto.getId());
		}
		int updateResult = this.shopSubUserDao.updateProperties(shopSubUser);
		if (updateResult <= 0) {
			return R.fail("更新失败，请重试");
		}
		return R.ok();
	}

	@Override
	public R<Void> updatePassword(Long id, String newPassword) {

		ShopSubUser shopSubUser = shopSubUserDao.getById(id);
		if (ObjectUtil.isNull(shopSubUser)) {
			return R.fail("员工信息不存在或已被删除，请返回刷新重试");
		}
		shopSubUser.setPassword(ENCODER.encode(newPassword));
		int updateResult = this.shopSubUserDao.updateProperties(shopSubUser, "password");
		if (updateResult <= 0) {
			return R.fail("更新密码失败，请重试");
		}
		return R.ok();
	}

	@Override
	public ShopSubUserDTO getByName(String username) {
		return this.converter.to(this.shopSubUserDao.getByProperties(new EntityCriterion().eq("username", username)));
	}

	@Override
	public List<ShopSubUserDTO> getShopSubUseRoleId(Long shopRoleId) {
		return null;
	}

	@Override
	public PageSupport<ShopSubUserDTO> queryUserPage(ShopSubUserQuery query) {
		if (null != query.getRoleId()) {
			List<ShopSubUserRole> subUserRoleList = this.shopSubUserRoleDao.queryByProperties(new EntityCriterion().eq("roleId", query.getRoleId()));
			if (!CollectionUtils.isEmpty(subUserRoleList)) {
				List<Long> userIds = subUserRoleList.stream().map(ShopSubUserRole::getShopSubUserId).collect(Collectors.toList());
				query.setSubUserIds(userIds);
			}
		}
		PageSupport<ShopSubUserDTO> page = converter.page(shopSubUserDao.queryUserPage(query));
		List<ShopSubUserDTO> resultList = page.getResultList();
		if (CollectionUtils.isEmpty(resultList)) {
			return page;
		}
		page.getResultList().forEach(e -> e.setPassword(null));
		List<Long> userIds = resultList.stream().map(ShopSubUserDTO::getId).collect(Collectors.toList());
		List<ShopSubUserRole> subUserRoles = shopSubUserRoleDao.queryByProperties(new EntityCriterion().in("shopSubUserId", userIds));

		if (!CollectionUtils.isEmpty(subUserRoles)) {
			page.getResultList().forEach(e -> {
				List<Long> roleIds = subUserRoles.stream().filter(s -> s.getShopSubUserId().equals(e.getId())).map(ShopSubUserRole::getRoleId).collect(Collectors.toList());
				e.setRoleIds(roleIds);
				// 权限名称
				List<ShopSubRole> shopSubRoles = shopSubRoleDao.getByUserId(e.getId());
				List<String> roleNames = shopSubRoles.stream().map(ShopSubRole::getRoleName).collect(Collectors.toList());
				e.setRoleNames(roleNames);
			});
		}
		return page;
	}


	@Override
	public R<UserInfo> getUserInfo(String username) throws UsernameNotFoundException {
		return buildUserInfo(username);
	}

	@Override
	public int delete(Long id) {
		if (null == id) {
			return 0;
		}
		return this.shopSubUserDao.deleteById(id);
	}

	@Override
	public int updateStatus(Long id) {
		return this.shopSubUserDao.update("UPDATE ls_shop_sub_user SET del_flag = !del_flag WHERE id = ?", id);
	}

	@Override
	public List<ShopSubUserDTO> queryShopSubAccount(Long shopUserId) {
		List<ShopSubUser> shopSubUsers = shopSubUserDao.queryAll();
		if (ObjectUtil.isEmpty(shopUserId) && shopSubUsers.isEmpty()) {
			return Collections.emptyList();
		}
		List<ShopSubUser> shopSubUserList = shopSubUsers.stream().filter(e -> e.getShopUserId().equals(shopUserId.toString())).collect(Collectors.toList());
		return this.converter.to(shopSubUserList);
	}

	@Override
	public R updateStatusByUserName(String subKey, Boolean status) {
		String[] subKeys = subKey.split("_");
		if (subKeys.length != 2) {
			return null;
		}
		String shopIdentifier = subKeys[0];
		String username = subKeys[1];
		ShopUser shopUser = this.shopUserDao.getByUsername(shopIdentifier);
		if (null == shopUser) {
			return null;
		}
		ShopSubUser shopSubUser = this.shopSubUserDao.getByProperties(new EntityCriterion().eq("userAccount", username).eq("shopUserId", shopUser.getId()));
		if (null == shopSubUser) {
			return null;
		}
		return R.ok(shopSubUserDao.updateStatusByUserName(shopSubUser.getId(), status));
	}

	@Override
	public List<Long> getRoleIds(Long userId) {
		return this.shopSubRoleDao.getByUserId(userId).stream().map(ShopSubRole::getId).collect(Collectors.toList());
	}

	@Override
	public List<MenuBO> getMenuBO(List<Long> roleIds) {
		return this.shopMenuDao.getSubUserByRoleIds(roleIds);
	}

	@Override
	public SysUserDTO getSysUser(String subKey) {
		String[] subKeys = subKey.split("_");
		if (subKeys.length != 2) {
			return null;
		}
		String shopIdentifier = subKeys[0];
		String username = subKeys[1];
		ShopUser shopUser = this.shopUserDao.getByUsername(shopIdentifier);
		if (null == shopUser) {
			return null;
		}
		ShopSubUser shopSubUser = this.shopSubUserDao.getByProperties(new EntityCriterion().eq("userAccount", username).eq("shopUserId", shopUser.getId()));
		if (null == shopSubUser) {
			return null;
		}
		SysUserDTO sysUserDTO = converter.toSysUser(shopSubUser);
		sysUserDTO.setShopId(shopUser.getShopId());
		return sysUserDTO;
	}
}
