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
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.legendshop.jpaplus.GenericDao;
import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.basic.enums.SmsTemplateTypeEnum;
import com.legendshop.common.core.constant.CacheConstants;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.dto.BasicBatchUpdateStatusDTO;
import com.legendshop.common.core.enums.UserTypeEnum;
import com.legendshop.common.core.enums.VisitSourceEnum;
import com.legendshop.common.core.service.BaseConverter;
import com.legendshop.common.util.RandomUtil;
import com.legendshop.user.bo.MenuBO;
import com.legendshop.user.bo.OrdinaryUserBO;
import com.legendshop.user.dao.*;
import com.legendshop.user.dto.*;
import com.legendshop.user.entity.OrdinaryRole;
import com.legendshop.user.entity.OrdinaryUser;
import com.legendshop.user.entity.OrdinaryUserRole;
import com.legendshop.user.entity.UserDetail;
import com.legendshop.user.enums.RoleEnum;
import com.legendshop.user.enums.UserSexEnum;
import com.legendshop.user.query.OrdinaryUserQuery;
import com.legendshop.user.service.BaseUserService;
import com.legendshop.user.service.OrdinaryUserService;
import com.legendshop.user.service.PassportService;
import com.legendshop.user.service.convert.UserConverter;
import com.legendshop.user.service.convert.UserDetailConverter;
import com.legendshop.user.utils.VerifyCodeUtil;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 普通用户接口实现
 *
 * @author legendshop
 */
@Slf4j
@Service
@AllArgsConstructor
public class OrdinaryUserServiceImpl extends AbstractUserInfo implements OrdinaryUserService {

	static final PasswordEncoder ENCODER = new BCryptPasswordEncoder();

	final UserConverter userConverter;
	final UserDetailDao userDetailDao;
	final UserDetailConverter userDetailConverter;
	final OrdinaryUserRoleDao userRoleDao;
	final OrdinaryMenuDao ordinaryMenuDao;
	final OrdinaryUserDao ordinaryUserDao;
	final OrdinaryRoleDao ordinaryRoleDao;
	final PassportService passportService;
	final BaseUserService baseUserService;

	@Override
	public R<UserInfo> getUserInfo(String username) throws UsernameNotFoundException {
		return buildUserInfo(username);
	}

	@Override
	public String getMobileByUsername(String username) {
		return ordinaryUserDao.getMobileByUsername(username);
	}

	@Override
	public PageSupport<OrdinaryUserBO> pageTwo(OrdinaryUserQuery ordinaryUserQuery) {
		PageSupport<OrdinaryUserBO> pageSupport = ordinaryUserDao.pageTwo(ordinaryUserQuery);
		return pageSupport;
	}

	@Override
	public List<Long> getRoleIds(Long userId) {
		return this.ordinaryRoleDao.getByUserId(userId).stream().map(OrdinaryRole::getId).collect(Collectors.toList());
	}

	@Override
	public List<MenuBO> getMenuBO(List<Long> roleIds) {
		return this.ordinaryMenuDao.getByRoleIds(roleIds);
	}


	@Override
	public boolean isMobileExist(String phone) {
		return ordinaryUserDao.isMobileExist(phone);
	}

	@Override
	public OrdinaryUserDTO getByMobile(String phone) {
		return userConverter.to(ordinaryUserDao.getByMobile(phone));
	}

	@Override
	public OrdinaryUserDTO getByUserId(Long userId) {
		return userConverter.to(ordinaryUserDao.getById(userId));
	}

	@Override
	public List<OrdinaryUserDTO> queryByMobile(List<String> mobileList) {
		return userConverter.to(ordinaryUserDao.queryByMobile(mobileList));
	}

	@Override
	protected SysUserDTO getSysUser(String username) {
		OrdinaryUser ordinaryUser = ordinaryUserDao.getUser(username);
		if (ordinaryUser == null) {
			return null;
		}

		String nickname = this.userDetailDao.getNickNameByUserId(ordinaryUser.getId());
		SysUserDTO sysUserDTO = userConverter.toSysUser(ordinaryUser);
		sysUserDTO.setNickname(nickname);
		return sysUserDTO;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	@GlobalTransactional
	public R<Long> save(OrdinaryUserDTO dto) {
		// 创建用户基础信息
		if (ordinaryUserDao.isMobileExist(dto.getMobile())) {
			log.info("该手机号，{}，已存在", dto.getMobile());
			return R.fail("该手机号已经注册了！");
		}
		dto.setDelFlag(Boolean.TRUE);
		dto.setLockFlag(Boolean.TRUE);
		dto.setCreateTime(DateUtil.date());
		dto.setNickName(StrUtil.isBlank(dto.getNickName()) ? dto.getMobile().substring(dto.getMobile().length() - 4) + "手机用户" : dto.getNickName());
		if (StringUtils.isNotBlank(dto.getPassword())) {
			dto.setPassword(ENCODER.encode(dto.getPassword()));
		}
		dto.setUsername(RandomUtil.generateUserName());
		Long id = ordinaryUserDao.save(userConverter.from(dto));
		// 创建用户详情
		UserDetail detail = new UserDetail();
		detail.setUserId(id);
		detail.setRegIp(dto.getRegIp());
		// 设置来源
		if (null == dto.getSource()) {
			// 不知道来源的一律h5
			dto.setSource(VisitSourceEnum.H5);
		}
		detail.setRegSource(dto.getSource().name());
		detail.setMobile(dto.getMobile());
		detail.setScore(0);
		detail.setCreateTime(DateUtil.date());
		detail.setSex(UserSexEnum.SECRET.value());
		detail.setNickName(dto.getNickName());
		detail.setConsumptionAmount(BigDecimal.ZERO);
		detail.setConsumptionOrderCount(0);
		detail.setAvailableIntegral(BigDecimal.ZERO);
		detail.setCumulativeIntegral(BigDecimal.ZERO);
		userDetailDao.save(detail);
		// 创建用户角色
		OrdinaryUserRole userRole = new OrdinaryUserRole();
		userRole.setUserId(id);
		userRole.setRoleId(Long.parseLong(RoleEnum.ROLE_USER.value()));
		userRoleDao.save(userRole);

		return R.ok(id);
	}

	@Override
	@CacheEvict(value = CacheConstants.ORDINARY_USER_DETAILS, key = "#ordinaryUserDTO.mobile")
	@Transactional(rollbackFor = Exception.class)
	public R<Void> updatePassword(VerifyOrdinaryUserDTO ordinaryUserDTO) {
		if (!VerifyCodeUtil.validateCode(new VerifyCodeDTO(ordinaryUserDTO.getCode(), ordinaryUserDTO.getMobile(), UserTypeEnum.USER, ordinaryUserDTO.getCodeType()))) {
			return R.fail("凭证失效！");
		}
		OrdinaryUserDTO dto = this.getByMobile(ordinaryUserDTO.getMobile());
		if (ObjectUtil.isNull(dto)) {
			return R.fail("该手机号并未注册账号");
		}
		dto.setPassword(ENCODER.encode(ordinaryUserDTO.getPassword()));
		dto.setUpdateTime(DateUtil.date());
		return R.process(this.ordinaryUserDao.update(this.userConverter.from(dto)) > 0, "重置密码失败！");
	}

	@Override
	public int updateMobileByUserId(Long userId, String mobile) {
		return ordinaryUserDao.updateMobileByUserId(userId, mobile);
	}

	@Override
	public boolean updateLoginPassword(OrdinaryUserDTO userDTO) {
		userDTO.setPassword(ENCODER.encode(userDTO.getPassword()));
		return this.ordinaryUserDao.updateLoginPassword(userDTO);
	}

	@Override
	public boolean updateStatus(Long userId) {
		return this.ordinaryUserDao.updateStatus(userId);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public R<Long> register(VerifyOrdinaryUserDTO userDTO) {
		if (!VerifyCodeUtil.validateCode(new VerifyCodeDTO(userDTO.getCode(), userDTO.getMobile(), UserTypeEnum.USER, SmsTemplateTypeEnum.REGISTER))) {
			return R.fail("验证码错误或已过期！");
		}
		return this.save(userDTO);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public R<OrdinaryUserDTO> bindThirdParty(String mobilePhone, String thirdPartyIdentifier) {
		Long userId;
		boolean exist = this.isMobileExist(mobilePhone);
		if (exist) {
			log.error("手机号被占用 principal：{}", mobilePhone);
			return R.fail("手机号被占用");
		}
		PassportDTO passportDTO = this.passportService.getPassByIdentifier(thirdPartyIdentifier);
		if (null == passportDTO) {
			log.error("查询第三方授权数据失败 ThirdPartyIdentifier：{}", thirdPartyIdentifier);
			return R.fail("授权错误！");
		}
		R userResult = this.save(new OrdinaryUserDTO(mobilePhone));
		Object data = userResult.getData();
		if (!(data instanceof Long)) {
			return R.fail("用户创建失败！");
		}
		userId = (Long) data;
		R<PassportDTO> passResult = this.passportService.getByUserIdAndType(userId, passportDTO.getType());
		if (null == passResult.getData()) {
			passportDTO.setUserId(userId);
			this.passportService.update(passportDTO);
		} else {
			PassportDTO createUserPass = passResult.getData();
			Long passportId = createUserPass.getId();
			this.passportService.updateItemBindParent(passportId, thirdPartyIdentifier);
		}
		return R.ok(this.getById(userId));
	}

	@Override
	public R<Void> updateMobilePhone(VerifyOrdinaryUserDTO dto) {
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
		if (this.ordinaryUserDao.updateMobileByUserId(dto.getId(), dto.getMobile()) > 0) {
			userDetailDao.updateMobileByUserId(dto.getId(), dto.getMobile());
			VerifyCodeUtil.deleteKey(new VerifyCodeDTO(dto.getOldCode(), mobile, dto.getUserType(), SmsTemplateTypeEnum.MODIFY_BINDING_MOBILE));
			return R.ok();
		}
		return R.fail("修改手机号失败");
	}

	@Override
	public R<Void> updateAvatar(Long userId, String avatar) {
		return R.process(this.ordinaryUserDao.updateAvatar(userId, avatar) > 0, "修改头像失败！");
	}

	@Override
	public List<OrdinaryUserDTO> getByIds(List<Long> userIds) {
		if (CollectionUtils.isEmpty(userIds)) {
			return new ArrayList<>();
		}
		return this.userConverter.to(this.ordinaryUserDao.queryAllByIds(userIds));
	}

	@Override
	public boolean batchUpdateStatus(BasicBatchUpdateStatusDTO basicBatchUpdateStatusDTO) {
		return ordinaryUserDao.batchUpdateStatus(basicBatchUpdateStatusDTO);
	}

	@Override
	public OrdinaryUserDTO getUser(String identifier) {
		return this.userConverter.to(this.ordinaryUserDao.getUser(identifier));
	}

	@Override
	public List<Long> getUserIds(Integer off, Integer size) {
		return this.ordinaryUserDao.getUserIds(off, size);
	}


	@Override
	public R save(List<OrdinaryUserDTO> dto) {
		return null;
	}

	@Override
	public Boolean update(OrdinaryUserDTO dto) {
		return null;
	}

	@Override
	public GenericDao getBaseMapper() {
		return ordinaryUserDao;
	}

	@Override
	public BaseConverter getConvert() {
		return userConverter;
	}

	@Override
	public PageSupport<OrdinaryUserDTO> queryAllUser(OrdinaryUserQuery ordinaryUserQuery) {

		PageSupport<OrdinaryUserDTO> pageSupport = ordinaryUserDao.queryAllUser(ordinaryUserQuery);

		return pageSupport;
	}

	@Override
	public List<Long> getByLikeMobile(String mobile) {
		List<OrdinaryUser> ordinaryUsers = ordinaryUserDao.getByLikeMobile(mobile);
		if (CollUtil.isEmpty(ordinaryUsers)) {
			return null;
		}
		return ordinaryUsers.stream().map(OrdinaryUser::getId).collect(Collectors.toList());
	}

	@Override
	public R updateStatusByUserName(String username, Boolean status) {
		return R.ok(ordinaryUserDao.updateStatusByUserName(username, status));
	}
}

