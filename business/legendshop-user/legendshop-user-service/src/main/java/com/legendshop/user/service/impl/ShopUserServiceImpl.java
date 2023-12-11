/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.legendshop.jpaplus.GenericDao;
import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.basic.enums.SmsTemplateTypeEnum;
import com.legendshop.common.core.constant.CacheConstants;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.dto.BasicBatchUpdateStatusDTO;
import com.legendshop.common.core.enums.UserTypeEnum;
import com.legendshop.common.core.expetion.BusinessException;
import com.legendshop.common.core.service.BaseConverter;
import com.legendshop.common.util.RandomUtil;
import com.legendshop.shop.api.ShopDetailApi;
import com.legendshop.shop.dto.ShopDetailDTO;
import com.legendshop.user.bo.MenuBO;
import com.legendshop.user.bo.ShopUserDetailBO;
import com.legendshop.user.dao.ShopMenuDao;
import com.legendshop.user.dao.ShopRoleDao;
import com.legendshop.user.dao.ShopUserDao;
import com.legendshop.user.dao.ShopUserRoleDao;
import com.legendshop.user.dto.*;
import com.legendshop.user.entity.ShopRole;
import com.legendshop.user.entity.ShopUser;
import com.legendshop.user.entity.ShopUserRole;
import com.legendshop.user.enums.RoleEnum;
import com.legendshop.user.query.ShopUserQuery;
import com.legendshop.user.service.BaseUserService;
import com.legendshop.user.service.ShopRoleService;
import com.legendshop.user.service.ShopUserService;
import com.legendshop.user.service.convert.ShopUserConverter;
import com.legendshop.user.utils.VerifyCodeUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 商家用户服务
 *
 * @author legendshop
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ShopUserServiceImpl extends AbstractUserInfo implements ShopUserService {

	private static final PasswordEncoder ENCODER = new BCryptPasswordEncoder();

	private final BaseUserService baseUserService;
	private final ShopUserDao shopUserDao;
	private final ShopUserRoleDao shopUserRoleDao;
	private final ShopRoleDao shopRoleDao;
	private final ShopMenuDao shopMenuDao;
	private final ShopUserConverter converter;
	private final ShopRoleService shopRoleService;
	private final ShopDetailApi shopDetailApi;

	@Override
	public ShopUserDTO getById(Long id) {
		ShopUserDTO shopUser = converter.to(shopUserDao.getById(id));
		if (null != shopUser) {
			R<ShopDetailDTO> detailResult = shopDetailApi.getByUserId(id);
			if (detailResult.success()) {
				shopUser.setShopAvatar(Optional.ofNullable(detailResult.getData()).map(ShopDetailDTO::getShopAvatar).orElse(null));
			}
		}
		return shopUser;
	}

	@Override
	public Long saveShopUser(ShopUserDTO shopUserDTO) {
		return shopUserDao.save(converter.from(shopUserDTO));
	}

	@Override
	public int updateShopUser(ShopUserDTO shopUserDTO) {
		return shopUserDao.update(converter.from(shopUserDTO));
	}

	@Override
	public ShopUserDTO getShopUserByNameAndShopId(String name, Long shopId) {
		return converter.to(shopUserDao.getShopUser(shopId, name));
	}

	@Override
	public List<ShopUserDTO> getShopUseRoleId(Long shopRoleId) {
		return converter.to(shopUserDao.getShopUseRoleId(shopRoleId));
	}

	@Override
	public PageSupport<ShopUserDTO> queryShopUser(String curPageNO, Long shopId) {
		return converter.page(shopUserDao.queryShopUser(curPageNO, shopId));
	}

	@Override
	public PageSupport<ShopUserDTO> page(ShopUserQuery shopUserQuery) {
		return converter.page((shopUserDao.page(shopUserQuery)));
	}

	@Override
	public R<UserInfo> getUserInfo(String username) throws UsernameNotFoundException {
		return buildUserInfo(username);
	}

	@Override
	public boolean isMobileExist(String phone) {
		return shopUserDao.isMobileExist(phone);
	}

	@Override
	public ShopUserDTO getByMobile(String phone) {
		return converter.to(shopUserDao.getByMobile(phone));
	}

	@Override
	@CacheEvict(value = CacheConstants.SHOP_USER_DETAILS, key = "#shopUserDTO.mobile")
	@Transactional(rollbackFor = Exception.class)
	public boolean updatePassword(ShopUserDTO shopUserDTO, String password) {
		shopUserDTO.setPassword(ENCODER.encode(password));
		shopUserDTO.setUpdateTime(DateUtil.date());
		shopUserDao.update(converter.from(shopUserDTO));

		return Boolean.TRUE;
	}

	@Override
	public R<Void> updateMobilePhone(VerifyShopUserMobileDTO dto) {
		R<Void> validateCode = VerifyCodeUtil.validateCode(new VerifyCodeDTO(dto.getCode(), dto.getMobile(), dto.getUserType(), SmsTemplateTypeEnum.CONFIRM_MOBILE_BIND), true);
		if (!validateCode.getSuccess()) {
			return R.fail(validateCode.getMsg());
		}

		String mobile = baseUserService.getMobile(new BaseUserQuery(dto.getId(), dto.getUserType().getLoginType()));
		validateCode = VerifyCodeUtil.validateCode(new VerifyCodeDTO(dto.getOldCode(), mobile, dto.getUserType(), SmsTemplateTypeEnum.MODIFY_BINDING_MOBILE), false);
		if (!validateCode.getSuccess()) {
			return R.fail(validateCode.getMsg());
		}

		if (this.isMobileExist(dto.getMobile())) {
			log.info("该手机号，{}，已存在", dto.getMobile());
			return R.fail("该手机号已经注册了！");
		}

		boolean success = this.shopUserDao.updateMobileByUserId(dto.getId(), dto.getMobile());
		if (success) {
			VerifyCodeUtil.deleteKey(new VerifyCodeDTO(dto.getOldCode(), mobile, dto.getUserType(), SmsTemplateTypeEnum.MODIFY_BINDING_MOBILE));
		}
		return R.process(success, "换绑手机失败！");
	}

	@Override
	public boolean updateLoginPassword(ShopUserDTO shopUserDTO) {
		shopUserDTO.setPassword(ENCODER.encode(shopUserDTO.getPassword()));
		return this.shopUserDao.updateLoginPassword(shopUserDTO);
	}

	@Override
	public boolean updateStatus(Long id) {
		return this.shopUserDao.updateStatus(id);
	}

	@Override
	public R<ShopUserDetailBO> getShopUserDetail(Long shopUserId) {

		ShopUser shopUser = shopUserDao.getById(shopUserId);
		if (ObjectUtil.isNull(shopUser)) {
			throw new BusinessException("该用户不存在，请刷新后重试");
		}
		ShopUserDetailBO shopUserDetailBO = new ShopUserDetailBO();
		R<ShopDetailDTO> shopDetailDTOR = shopDetailApi.getByUserId(shopUserId);
		if (shopDetailDTOR.success()) {
			ShopDetailDTO shopDetailDTO = shopDetailDTOR.getData();
			if (ObjectUtil.isNotNull(shopDetailDTO)) {
				shopUserDetailBO.setShopId(shopDetailDTO.getId());
			}
		}
		shopUserDetailBO.setUsername(shopUser.getUsername());
		shopUserDetailBO.setMobile(shopUser.getMobile());
		shopUserDetailBO.setAvatar(shopUser.getAvatar());
		shopUserDetailBO.setCreateTime(shopUser.getCreateTime());

		List<ShopRoleDTO> shopRoleList = shopRoleService.getByUserId(shopUserId);
		shopUserDetailBO.setShopRoleDTOList(shopRoleList);
		return R.ok(shopUserDetailBO);
	}

	@Override
	public boolean batchUpdateStatus(BasicBatchUpdateStatusDTO basicBatchUpdateStatusDTO) {
		return shopUserDao.batchUpdateStatus(basicBatchUpdateStatusDTO);
	}

	@Override
	public R<Void> updateAvatar(Long userId, String avatar) {

		ShopUser shopUser = shopUserDao.getById(userId);
		if (ObjectUtil.isNull(shopUser)) {
			throw new BusinessException("该用户不存在，请刷新后重试");
		}
		return R.process(shopUserDao.updateAvatar(userId, avatar), "更新商家用户头像失败");
	}

	@Override
	public List<ShopUserDTO> getByIds(List<Long> ids) {
		return converter.to(shopUserDao.queryAllByIds(ids));
	}

	@Override
	public ShopUserDTO getByShopId(Long shopid) {
		return converter.to(shopUserDao.getByShopId(shopid));
	}

	@Override
	public List<ShopUserDTO> queryAll() {
		return converter.to(shopUserDao.queryAll());
	}

	@Override
	public R updateStatusByUserName(String username, Boolean status) {
		return R.ok(shopUserDao.updateStatusByUserName(username, status));
	}

	@Override
	protected List<Long> getRoleIds(Long userId) {
		return this.shopRoleDao.getByUserId(userId).stream().map(ShopRole::getId).collect(Collectors.toList());
	}

	@Override
	protected List<MenuBO> getMenuBO(List<Long> roleIds) {
		return this.shopMenuDao.getByRoleIds(roleIds);
	}

	@Override
	protected SysUserDTO getSysUser(String account) {
		ShopUser shopUser = shopUserDao.getByUsername(account);
		if (ObjectUtil.isNull(shopUser)) {
			return null;
		}
		return converter.toSysUser(shopUser);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public R<Void> save(VerifyShopUserDTO shopUserDTO) {
		if (!VerifyCodeUtil.validateCode(new VerifyCodeDTO(shopUserDTO.getCode(), shopUserDTO.getMobile(), UserTypeEnum.SHOP, SmsTemplateTypeEnum.REGISTER))) {
			return R.fail("验证码错误！");
		}
		if (shopUserDao.isMobileExist(shopUserDTO.getMobile())) {
			log.info("该手机号，{}，已存在", shopUserDTO.getMobile());
			return R.fail("手机号已存在");
		}
		shopUserDTO.setLockFlag(Boolean.TRUE);
		shopUserDTO.setDelFlag(Boolean.TRUE);
		shopUserDTO.setUsername(RandomUtil.generateUserName());
		shopUserDTO.setCreateTime(DateUtil.date());
		shopUserDTO.setPassword(ENCODER.encode(shopUserDTO.getPassword()));
		Long userId = shopUserDao.save(converter.from(shopUserDTO));
		//保存用户角色
		ShopUserRole userRole = new ShopUserRole();
		userRole.setRoleId(Long.parseLong(RoleEnum.ROLE_SUPPLIER.value()));
		userRole.setUserId(userId);
		shopUserRoleDao.save(userRole);
		return R.ok();
	}


	public boolean isUserExist(String userName) {
		return shopUserDao.isUserExist(userName);
	}

	@Override
	public R save(ShopUserDTO dto) {
		return null;
	}

	@Override
	public R save(List<ShopUserDTO> dto) {
		return null;
	}

	@Override
	public Boolean update(ShopUserDTO dto) {
		return null;
	}

	@Override
	public GenericDao getBaseMapper() {
		return null;
	}

	@Override
	public BaseConverter getConvert() {
		return null;
	}
}
