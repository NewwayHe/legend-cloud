/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.controller.admin;

import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.validator.group.Update;
import com.legendshop.common.security.utils.SecurityUtils;
import com.legendshop.shop.dto.UserFeedbackBatchHandleDTO;
import com.legendshop.shop.dto.UserFeedbackDTO;
import com.legendshop.shop.query.UserFeedBackQuery;
import com.legendshop.shop.service.UserFeedBackService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 反馈意见控制器
 *
 * @author legendshop
 */
@Tag(name = "用户反馈")
@RestController
@RequestMapping(value = "/admin/user/feedback", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class AdminUserFeedbackController {

	private final UserFeedBackService userFeedBackService;

	@PreAuthorize("@pms.hasPermission('admin_userFeedback_page')")
	@Operation(summary = "【后台】反馈意见分页查询")
	@GetMapping("/page")
	public R<PageSupport<UserFeedbackDTO>> page(UserFeedBackQuery userFeedBackQuery) {
		PageSupport<UserFeedbackDTO> pageSupport = userFeedBackService.page(userFeedBackQuery);
		return R.ok(pageSupport);
	}

	@PreAuthorize("@pms.hasPermission('admin_userFeedback_update')")
	@Operation(summary = "【后台】回复反馈意见")
	@PutMapping
	public R<String> updateReply(@Validated(Update.class) @RequestBody UserFeedbackDTO userFeedbackDTO) {

		Long adminUserId = SecurityUtils.getAdminUser().getUserId();
		userFeedbackDTO.setRespMgntId(adminUserId);
		if (userFeedBackService.updateReply(userFeedbackDTO) > 0) {
			return R.ok();
		}
		return R.fail("回复失败");
	}


	@PreAuthorize("@pms.hasPermission('admin_userFeedback_updateListById')")
	@Operation(summary = "【后台】批量回复")
	@PostMapping("/updateListById")
	public R<String> updateListById(@Valid @RequestBody UserFeedbackBatchHandleDTO userFeedbackBatchHandleDTO) {

		Long adminUserId = SecurityUtils.getAdminUser().getUserId();
		userFeedbackBatchHandleDTO.setRespUserId(adminUserId);
		if (userFeedBackService.updateListById(userFeedbackBatchHandleDTO)) {
			return R.ok();
		}
		return R.fail("批量回复失败");
	}

}

