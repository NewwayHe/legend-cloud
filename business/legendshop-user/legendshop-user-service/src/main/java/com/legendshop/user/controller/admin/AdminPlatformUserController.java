/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.controller.admin;


import cn.hutool.extra.servlet.JakartaServletUtil;
import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.constant.RequestHeaderConstant;
import com.legendshop.common.core.enums.VisitSourceEnum;
import com.legendshop.common.log.annotation.SystemLog;
import com.legendshop.common.security.dto.AdminUserDetail;
import com.legendshop.common.security.utils.SecurityUtils;
import com.legendshop.user.dto.AdminUserDTO;
import com.legendshop.user.query.AdminUserQuery;
import com.legendshop.user.service.AdminUserService;
import com.legendshop.user.vo.AdminUserVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 平台用户管理控制器
 *
 * @author legendshop
 */
@RestController
@RequestMapping(value = "/admin/platform/user", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "后台用户模块")
@AllArgsConstructor
public class AdminPlatformUserController {

	private final AdminUserService adminUserService;

	private final HttpServletRequest request;

	/**
	 * 获取当前用户信息
	 *
	 * @return
	 */
	@GetMapping
	@Operation(summary = "【后台】获取当前用户信息")
	public R<AdminUserDTO> adminUser() {
		Long userId = SecurityUtils.getAdminUser().getUserId();
		return R.ok(adminUserService.getById(userId));
	}

	/**
	 * 分页查询管理员用户
	 *
	 * @param adminUserQuery
	 * @return
	 */
	@GetMapping("/page")
	@PreAuthorize("@pms.hasPermission('admin_user_platformUserManage_page')")
	@Operation(summary = "【后台】分页查询管理员用户")
	public R<PageSupport<AdminUserVO>> page(AdminUserQuery adminUserQuery) {
		return R.ok(adminUserService.page(adminUserQuery));
	}

	/**
	 * 保存添加管理员
	 *
	 * @param adminUserDTO
	 * @return
	 */
	@PostMapping
	@SystemLog(value = "保存管理员")
	@PreAuthorize("@pms.hasPermission('admin_user_platformUserManage_save')")
	@Operation(summary = "【后台】保存管理员")
	public R save(@Valid @RequestBody AdminUserDTO adminUserDTO) {
		AdminUserDetail adminUser = SecurityUtils.getAdminUser();
		adminUserDTO.setOperatorId(adminUser.getUserId());
		adminUserDTO.setOperatorName(adminUser.getUsername());
		adminUserDTO.setClientIP(JakartaServletUtil.getClientIP(request));
		String source = request.getHeader(RequestHeaderConstant.SOURCE_KEY);
		adminUserDTO.setSource(VisitSourceEnum.getDescByName(source));
		return this.adminUserService.save(adminUserDTO);
	}

	/**
	 * 修改管理员
	 *
	 * @param adminUserDTO
	 * @return
	 */
	@PutMapping
	@SystemLog(value = "更新管理员")
	@PreAuthorize("@pms.hasPermission('admin_user_platformUserManage_update')")
	@Operation(summary = "【后台】更新管理员")
	public R<Boolean> update(@Valid @RequestBody AdminUserDTO adminUserDTO) {
		AdminUserDetail adminUser = SecurityUtils.getAdminUser();
		adminUserDTO.setOperatorId(adminUser.getUserId());
		adminUserDTO.setOperatorName(adminUser.getUsername());
		adminUserDTO.setClientIP(JakartaServletUtil.getClientIP(request));
		String source = request.getHeader(RequestHeaderConstant.SOURCE_KEY);
		adminUserDTO.setSource(VisitSourceEnum.getDescByName(source));
		return R.ok(adminUserService.update(adminUserDTO));
	}

	/**
	 * 删除管理员
	 *
	 * @param id
	 * @return
	 */
	@DeleteMapping("/{id}")
	@SystemLog(value = "删除管理员")
	@PreAuthorize("@pms.hasPermission('admin_user_platformUserManage_del')")
	@Operation(summary = "【后台】删除管理员")
	@Parameter(name = "id", description = "菜单Id", required = true)
	public R deleteById(@PathVariable Long id) {
		Long userId = SecurityUtils.getAdminUser().getUserId();
		if (userId.equals(id)) {
			return R.fail("删除失败，当前登录用户不能删除");
		}

		AdminUserDTO adminUserDTO = adminUserService.getById(id);

		AdminUserDetail adminUser = SecurityUtils.getAdminUser();
		adminUserDTO.setOperatorId(adminUser.getUserId());
		adminUserDTO.setOperatorName(adminUser.getUsername());
		adminUserDTO.setClientIP(JakartaServletUtil.getClientIP(request));
		String source = request.getHeader(RequestHeaderConstant.SOURCE_KEY);
		adminUserDTO.setSource(VisitSourceEnum.getDescByName(source));

		return R.ok(adminUserService.delete(adminUserDTO));
	}

}
