/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.controller.user;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.servlet.JakartaServletUtil;
import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.security.utils.UserTokenUtil;
import com.legendshop.shop.dto.UserFeedbackDTO;
import com.legendshop.shop.query.UserFeedBackQuery;
import com.legendshop.shop.service.UserFeedBackService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * @author legendshop
 */
@Tag(name = "用户反馈")
@RestController
@RequestMapping(value = "/feedback", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class UserFeedbackController {

	final HttpServletRequest request;
	final UserFeedBackService userFeedBackService;
	final UserTokenUtil userTokenUtil;


	@Operation(summary = "【用户】用户提交反馈内容")
	@PostMapping
	public R save(HttpServletRequest request, @Valid @RequestBody UserFeedbackDTO userFeedbackDTO) {

		Long userId = userTokenUtil.getUserId(request);
		String username = userTokenUtil.getUserName(request);

		if (ObjectUtil.isEmpty(userId)) {
			userId = 0L;
		}

		boolean isUserIdEmpty = userId == 0 || ObjectUtil.isEmpty(userId);
		boolean isMobileEmpty = ObjectUtil.isEmpty(userFeedbackDTO.getMobile());
		//如果没有登录，则手机号不能为空
		if (isUserIdEmpty && isMobileEmpty) {
			return R.fail("请输入手机号");
		}
		if (StringUtils.isBlank(username)) {
			username = "";
		}
		userFeedbackDTO.setUserId(userId);
		userFeedbackDTO.setName(username);
		String clientIP = JakartaServletUtil.getClientIP(request);
		userFeedbackDTO.setIp(clientIP);
		return R.ok(userFeedBackService.saveUserFeedback(userFeedbackDTO));
	}

	@Operation(summary = "【后台】首页未回复消息")
	@GetMapping("/unanswered/page")
	public R<PageSupport<UserFeedbackDTO>> unanswered(UserFeedBackQuery query) {
		query.setStatus(0);
		return R.ok(userFeedBackService.getUserFeedBackPage(query));
	}
}
