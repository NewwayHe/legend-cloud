/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.controller.user;

import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.basic.bo.MsgBO;
import com.legendshop.basic.bo.UserMsgBo;
import com.legendshop.basic.dto.ReceiverDTO;
import com.legendshop.basic.enums.MsgReceiverTypeEnum;
import com.legendshop.basic.query.MsgQuery;
import com.legendshop.basic.service.MessageService;
import com.legendshop.basic.service.ReceiverService;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.security.utils.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author legendshop
 */
@RestController
@Tag(name = "系统通知")
@RequiredArgsConstructor
@RequestMapping(value = "/p/msg", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserMsgController {

	private final MessageService messageService;

	private final ReceiverService receiverService;

	@Operation(summary = "【用户】系统通知列表")
	@GetMapping("/page")
	public R<PageSupport<MsgBO>> queryUserMsg(MsgQuery query) {
		query.setReceiverId(SecurityUtils.getUser().getUserId());
		query.setUserType(MsgReceiverTypeEnum.ORDINARY_USER.value());
		PageSupport<MsgBO> collect = messageService.queryMyMsgPage(query);
		return R.ok(collect);
	}


	@GetMapping
	@Operation(summary = "【用户】系统通知详情")
	public R<MsgBO> getMsg(@RequestParam Long msgId) {
		Long userId = SecurityUtils.getUser().getUserId();
		return R.ok(messageService.getByMsgId(userId, msgId, MsgReceiverTypeEnum.ORDINARY_USER.value()));
	}

	@GetMapping(value = "/unread")
	@Operation(summary = "【用户】系统通知列表未读数量")
	public R<UserMsgBo> userUnreadMsg(@RequestParam(value = "userId") Long userId) {
		return this.messageService.userUnread(userId);
	}

	@GetMapping(value = "/unReadCount")
	@Operation(summary = "【用户】系统通知未读数量")
	public R<UserMsgBo> userUnreadMsgCount() {
		Long userId = SecurityUtils.getUser().getUserId();
		return this.messageService.userUnread(userId);
	}

	@GetMapping(value = "/isExist")
	public Boolean isExist(@RequestParam(value = "userId") Long userId, @RequestParam(value = "msgId") Long msgId, @RequestParam(value = "type") Integer type) {
		return receiverService.getByUserIdAndMsgId(userId, msgId, type);
	}

	@PostMapping(value = "/saveReceivers")
	public R<Boolean> saveReceivers(@RequestBody List<ReceiverDTO> receiverDTOS) {
		return receiverService.save(receiverDTOS);
	}


	@GetMapping(value = "/userUnread")
	@Operation(summary = "【用户】PC端我的消息")
	public R<UserMsgBo> userUnread() {
		Long userId = SecurityUtils.getUserId();
		return this.messageService.userUnread(userId);
	}

	@Operation(summary = "【用户】一键清除消息未读标记")
	@GetMapping("/cleanUnread")
	public R<Void> cleanUnread() {
		Long userId = SecurityUtils.getUserId();
		messageService.cleanUnread(userId, MsgReceiverTypeEnum.ORDINARY_USER.value());
		return R.ok();
	}
}
