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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author legendshop
 */
@Tag(name = "系统通知")
@RestController
@RequestMapping(value = "/admin/msg", produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminUserMsgController {

	@Autowired
	private MessageService messageService;

	@Operation(summary = "【后台】系统通知列表")
	@GetMapping("/page")
	public R<PageSupport<MsgBO>> queryMsg(MsgQuery query) {
		query.setReceiverId(-1L);
		query.setUserType(MsgReceiverTypeEnum.ADMIN_USER.value());
		PageSupport<MsgBO> collect = messageService.queryMyMsgPage(query);
		return R.ok(collect);
	}

	@Operation(summary = "【平台】系统通知列表未读数量")
	@GetMapping("/unread")
	public R<Integer> getAdminUnreadNum() {
		return R.ok(messageService.getAdminUnreadNum(-1L));
	}


	@Operation(summary = "【后台】系统通知详情")
	@GetMapping
	public R<MsgBO> getMsg(@RequestParam Long msgId) {
		return R.ok(messageService.getByMsgId(-1L, msgId, MsgReceiverTypeEnum.ADMIN_USER.value()));
	}

	@Operation(summary = "【后台】一键清除消息未读标记")
	@GetMapping("/cleanUnread")
	public R<Void> cleanUnread() {
		messageService.cleanUnread(-1L, MsgReceiverTypeEnum.ADMIN_USER.value());
		return R.ok();
	}
}
