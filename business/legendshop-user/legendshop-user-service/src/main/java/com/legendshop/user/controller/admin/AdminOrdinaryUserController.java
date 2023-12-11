/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.controller.admin;

import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.basic.api.MessageApi;
import com.legendshop.basic.api.WxApi;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.dto.BasicBatchUpdateStatusDTO;
import com.legendshop.common.log.annotation.SystemLog;
import com.legendshop.common.security.utils.SecurityUtils;
import com.legendshop.user.bo.OrdinaryUserBO;
import com.legendshop.user.dto.OrdinaryUserDTO;
import com.legendshop.user.query.OrdinaryUserQuery;
import com.legendshop.user.service.OrdinaryUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 后台普通用户管理器
 *
 * @author legendshop
 */
@Tag(name = "普通用户管理")
@RestController
@RequestMapping(value = "/admin/ordinary/user", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class AdminOrdinaryUserController {

	private final OrdinaryUserService ordinaryUserService;
	private final MessageApi messageApi;
	private final WxApi wxApi;

	@GetMapping("/page")
	@PreAuthorize("@pms.hasPermission('admin_user_ordinaryUserManage_page')")
	@Operation(summary = "【后台】查询用户基本信息列表")
	public R<PageSupport<OrdinaryUserBO>> page(OrdinaryUserQuery ordinaryUserQuery) {
		return R.ok(this.ordinaryUserService.pageTwo(ordinaryUserQuery));
	}

	@PutMapping("/updateLoginPassword")
	@PreAuthorize("@pms.hasPermission('admin_user_ordinaryUserManage_updateLoginPassword')")
	@Operation(summary = "【后台】更新用户登录密码")
	public R<Void> updateLoginPassword(@RequestBody OrdinaryUserDTO userDTO) {
		return R.process(ordinaryUserService.updateLoginPassword(userDTO), "更新用户密码失败");
	}

	@PutMapping(value = "/updateStatus/{userId}")
	@PreAuthorize("@pms.hasPermission('admin_user_ordinaryUserManage_updateStatus')")
	@Operation(summary = "【后台】修改在线状态")
	@Parameter(name = "id", description = "用户Id")
	public R<Void> updateStatus(@PathVariable Long userId) {
		return R.process(this.ordinaryUserService.updateStatus(userId), "更新用户状态失败！");
	}


	@PostMapping(value = "/sendMessage")
	@PreAuthorize("@pms.hasPermission('admin_user_ordinaryUserManage_sendMessage')")
	@Operation(summary = "【后台】发送普通用户站内信")
	@Parameters({
			@Parameter(name = "userId", description = "用户Id"),
			@Parameter(name = "content", description = "站内信内容")
	})
	public R<Void> sendMessage(@RequestParam Long userId, @RequestParam String content) {

		R<Void> result = wxApi.checkContent(content);
		if (!result.success()) {
			return result;
		}
		Long adminUserId = SecurityUtils.getAdminUser().getUserId();
		return messageApi.send(adminUserId, userId, content);
	}


	/**
	 * 批量更新状态
	 *
	 * @param basicBatchUpdateStatusDTO
	 * @return
	 */
	@PutMapping("/batch/update/status")
	@SystemLog("批量更新状态")
	@PreAuthorize("@pms.hasPermission('admin_ordinary_user_batch_update_status')")
	@Operation(summary = "【后台】批量更新状态")
	public R<Void> batchUpdateStatus(@RequestBody @Valid BasicBatchUpdateStatusDTO basicBatchUpdateStatusDTO) {
		return R.process(ordinaryUserService.batchUpdateStatus(basicBatchUpdateStatusDTO), "批量更新失败");
	}
}
