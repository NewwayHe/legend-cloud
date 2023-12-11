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
import cn.hutool.json.JSONUtil;
import cn.legendshop.jpaplus.GenericDao;
import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.basic.api.OperatorLogApi;
import com.legendshop.basic.query.OperatorLogQuery;
import com.legendshop.common.core.constant.CacheConstants;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.dto.OperatorLogDTO;
import com.legendshop.common.core.enums.EventTypeEnum;
import com.legendshop.common.core.enums.UserTypeEnum;
import com.legendshop.common.core.service.BaseConverter;
import com.legendshop.common.rabbitmq.constants.AmqpConst;
import com.legendshop.common.rabbitmq.util.AmqpSendMsgUtil;
import com.legendshop.user.bo.MenuBO;
import com.legendshop.user.dao.AdminMenuDao;
import com.legendshop.user.dao.AdminRoleDao;
import com.legendshop.user.dao.AdminUserDao;
import com.legendshop.user.dao.AdminUserRoleDao;
import com.legendshop.user.dto.AdminUserDTO;
import com.legendshop.user.dto.SysUserDTO;
import com.legendshop.user.dto.UserInfo;
import com.legendshop.user.entity.AdminRole;
import com.legendshop.user.entity.AdminUser;
import com.legendshop.user.entity.AdminUserRole;
import com.legendshop.user.enums.RoleTypeEnum;
import com.legendshop.user.query.AdminUserQuery;
import com.legendshop.user.service.AdminUserService;
import com.legendshop.user.service.convert.AdminUserConverter;
import com.legendshop.user.vo.AdminUserVO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 管理员用户服务.
 *
 * @author legendshop
 */
@Service
@Slf4j
@AllArgsConstructor
public class AdminUserServiceImpl extends AbstractUserInfo implements AdminUserService {

	private static final PasswordEncoder ENCODER = new BCryptPasswordEncoder();

	private final AdminUserRoleDao adminUserRoleDao;
	private final AdminMenuDao adminMenuDao;
	private final AdminUserDao adminUserDao;
	private final AdminRoleDao adminRoleDao;
	private final AdminUserConverter adminUserConverter;
	private final AmqpSendMsgUtil amqpSendMsgUtil;
	private final OperatorLogApi operatorLogApi;


	@Override
	@Transactional(rollbackFor = Exception.class)
	public R save(AdminUserDTO adminUserDTO) {
		AdminUser adminUser = adminUserDao.getUserByUsername(adminUserDTO.getUsername().trim());
		if (ObjectUtil.isNotEmpty(adminUser)) {
			log.info("该用户名，{}，已存在", adminUserDTO.getUsername());
			return R.fail("用户名已存在");
		}
		adminUserDTO.setDelFlag(Boolean.TRUE);
		adminUserDTO.setPassword(ENCODER.encode(adminUserDTO.getPassword()));
		adminUserDTO.setCreateTime(DateUtil.date());
		Long userId = adminUserDao.save(adminUserConverter.from(adminUserDTO));
		//保存用户角色
		List<AdminUserRole> adminUserRoleList = adminUserDTO.getRoleIdList().stream().map(role -> {
			AdminUserRole adminUserRole = new AdminUserRole();
			adminUserRole.setUserId(userId);
			adminUserRole.setRoleId(role);
			return adminUserRole;
		}).collect(Collectors.toList());
		adminUserRoleDao.saveUserRoleList(adminUserRoleList);

		//发送mq消息
		OperatorLogDTO operatorLogDTO = new OperatorLogDTO();
		operatorLogDTO.setUserId(userId);
		operatorLogDTO.setUserName(adminUserDTO.getUsername());
		operatorLogDTO.setEventType(EventTypeEnum.ADD_ACCOUNT.getEventType());
		operatorLogDTO.setAfterModification(JSONUtil.toJsonStr(adminUserDTO));
		operatorLogDTO.setOperatorId(adminUserDTO.getOperatorId());
		operatorLogDTO.setOperatorName(adminUserDTO.getOperatorName());
		operatorLogDTO.setRemoteIp(adminUserDTO.getClientIP());
		operatorLogDTO.setSource(adminUserDTO.getSource());
		operatorLogDTO.setCreateTime(DateUtil.date());
		this.saveOperatorLog(operatorLogDTO);

		return R.ok();
	}

	@Override
	public R save(List<AdminUserDTO> dto) {
		return null;
	}


	@Override
	@CacheEvict(value = CacheConstants.ADMIN_USER_DETAILS, key = "#adminUserDTO.username")
	@Transactional(rollbackFor = Exception.class)
	public Boolean update(AdminUserDTO adminUserDTO) {
		//查询修改前的最后一条数据
		OperatorLogDTO operatorLogDTO = new OperatorLogDTO();
		OperatorLogQuery operatorLogQuery = new OperatorLogQuery();
		operatorLogQuery.setUserId(adminUserDTO.getId());
		operatorLogQuery.setUserType(UserTypeEnum.ADMIN.getLoginType());
		operatorLogQuery.setEventType(EventTypeEnum.UPDATE_ACCOUNT.getEventType());
		List<OperatorLogDTO> operatorLogList = operatorLogApi.page(operatorLogQuery).getData().getResultList();
		if (CollUtil.isNotEmpty(operatorLogList)) {
			operatorLogDTO.setBeforeModification(operatorLogList.get(0).getAfterModification());
		}

		AdminUser adminUser = adminUserDao.getUserByIdUsername(adminUserDTO.getId(), adminUserDTO.getUsername());

		if (StrUtil.isNotBlank(adminUserDTO.getNewPassword()) && StrUtil.isNotBlank(adminUserDTO.getNewPassword())) {
			adminUser.setPassword(ENCODER.encode(adminUserDTO.getNewPassword()));
		}
		adminUser.setUpdateTime(DateUtil.date());
		adminUser.setLockFlag(adminUserDTO.getLockFlag());
		adminUser.setAvatar(adminUserDTO.getAvatar());
		adminUser.setDeptId(adminUserDTO.getDeptId());
		adminUserDao.update(adminUser);
		adminUserRoleDao.deleteByUserId(adminUser.getId(), RoleTypeEnum.ADMIN);

		List<Long> roles = adminUserDTO.getRoleIdList();
		if (roles != null) {
			roles.forEach(role -> {
				AdminUserRole adminUserRole = new AdminUserRole();
				adminUserRole.setUserId(adminUser.getId());
				adminUserRole.setRoleId(role);
				adminUserRoleDao.saveUserRole(adminUserRole);
			});
		}

		//发送mq消息
		operatorLogDTO.setUserId(adminUserDTO.getId());
		operatorLogDTO.setUserName(adminUserDTO.getUsername());
		operatorLogDTO.setEventType(EventTypeEnum.UPDATE_ACCOUNT.getEventType());
		operatorLogDTO.setAfterModification(JSONUtil.toJsonStr(adminUserDTO));
		operatorLogDTO.setOperatorId(adminUserDTO.getOperatorId());
		operatorLogDTO.setOperatorName(adminUserDTO.getOperatorName());
		operatorLogDTO.setRemoteIp(adminUserDTO.getClientIP());
		operatorLogDTO.setSource(adminUserDTO.getSource());
		this.saveOperatorLog(operatorLogDTO);

		return Boolean.TRUE;
	}

	@Override
	public AdminUserDTO getById(Long id) {
		AdminUserDTO userDTO = this.adminUserConverter.to(adminUserDao.getById(id));
		userDTO.setPassword(null);
		return userDTO;
	}

	@Override
	public Boolean deleteById(Long id) {
		return null;
	}

	@Override
	@CacheEvict(value = CacheConstants.ADMIN_USER_DETAILS, key = "#adminUserDTO.username")
	@Transactional(rollbackFor = Exception.class)
	public Boolean delete(AdminUserDTO adminUserDTO) {
		if ("admin".equals(adminUserDTO.getUsername())) {
			log.error("超级管理员admin不允许删除!");
			return Boolean.FALSE;
		}
		//先删除用户角色
		adminUserRoleDao.deleteByUserId(adminUserDTO.getId(), RoleTypeEnum.ADMIN);
		//再删除自己
		adminUserDao.deleteById(adminUserDTO.getId());

		//发送mq消息
		OperatorLogDTO operatorLogDTO = new OperatorLogDTO();
		operatorLogDTO.setUserId(adminUserDTO.getId());
		operatorLogDTO.setUserName(adminUserDTO.getUsername());
		operatorLogDTO.setEventType(EventTypeEnum.DEL_ACCOUNT.getEventType());
		operatorLogDTO.setBeforeModification(JSONUtil.toJsonStr(adminUserDTO));
		operatorLogDTO.setOperatorId(adminUserDTO.getOperatorId());
		operatorLogDTO.setOperatorName(adminUserDTO.getOperatorName());
		operatorLogDTO.setRemoteIp(adminUserDTO.getClientIP());
		operatorLogDTO.setSource(adminUserDTO.getSource());
		this.saveOperatorLog(operatorLogDTO);

		return Boolean.TRUE;
	}

	@Override
	public PageSupport<AdminUserVO> page(AdminUserQuery adminUserQuery) {
		return adminUserDao.page(adminUserQuery);
	}

	@Override
	public R<UserInfo> getUserInfo(String username) throws UsernameNotFoundException {
		return buildUserInfo(username);
	}

	@Override
	public R<List<Long>> queryUserByMenuId(Long menuId) {
		return R.ok(adminUserDao.queryUserByMenuId(menuId));
	}

	@Override
	public List<Long> queryUserIdsByMenuName(String menuName) {
		return adminUserDao.queryUserIdsByMenuName(menuName);
	}

	@Override
	public List<Long> getAdmin() {
		return adminUserDao.getAdmin();
	}

	@Override
	public R updateStatusByUserName(String username, Boolean status) {
		return R.ok(adminUserDao.updateStatusByUserName(username, status));
	}


	@Override
	protected List<Long> getRoleIds(Long userId) {
		return this.adminRoleDao.getByUserId(userId).stream().map(AdminRole::getId).collect(Collectors.toList());
	}

	@Override
	protected List<MenuBO> getMenuBO(List<Long> roleIds) {
		return this.adminMenuDao.getByRoleIds(roleIds);
	}

	@Override
	protected SysUserDTO getSysUser(String username) {
		AdminUser adminUser = adminUserDao.getUserByUsername(username);
		return adminUserConverter.toSysUser(adminUser);
	}

	@Override
	public GenericDao getBaseMapper() {
		return adminUserDao;
	}

	@Override
	public BaseConverter getConvert() {
		return adminUserConverter;
	}

	/**
	 * 保存操作日志
	 *
	 * @param operatorLogDTO
	 */
	private void saveOperatorLog(OperatorLogDTO operatorLogDTO) {
		operatorLogDTO.setCreateTime(DateUtil.date());
		operatorLogDTO.setUserType(UserTypeEnum.ADMIN.getLoginType());
		operatorLogDTO.setOperatorType(UserTypeEnum.ADMIN.getLoginType());
		amqpSendMsgUtil.convertAndSend(AmqpConst.OPERATOR_LOG_EXCHANGE, AmqpConst.OPERATOR_LOG_ROUTING_KEY, operatorLogDTO);
	}
}
