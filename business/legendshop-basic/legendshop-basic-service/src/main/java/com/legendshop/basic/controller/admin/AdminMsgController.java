/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.controller.admin;

import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.basic.bo.MsgBO;
import com.legendshop.basic.enums.MsgReceiverTypeEnum;
import com.legendshop.basic.query.MsgQuery;
import com.legendshop.basic.service.MessageService;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.security.utils.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author legendshop
 */
@RestController
@Tag(name = "后台系统通知")
@RequiredArgsConstructor
@RequestMapping(value = "/p/msg/admin", produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminMsgController {

	private final MessageService messageService;


	@Operation(summary = "【后台】系统通知列表")
	@GetMapping("/page")
	public R<PageSupport<MsgBO>> queryUserMsg(MsgQuery query) {
		query.setReceiverId(SecurityUtils.getAdminUser().getUserId());
		query.setUserType(MsgReceiverTypeEnum.ADMIN_USER.value());
		PageSupport<MsgBO> collect = messageService.queryMyMsgPage(query);
		return R.ok(collect);
	}

	@GetMapping
	@Operation(summary = "【后台】系统通知详情")
	public R<MsgBO> getMsg(@RequestParam Long msgId) {
		return R.ok(messageService.getByMsgId(-1L, msgId, MsgReceiverTypeEnum.ADMIN_USER.value()));
	}


}
