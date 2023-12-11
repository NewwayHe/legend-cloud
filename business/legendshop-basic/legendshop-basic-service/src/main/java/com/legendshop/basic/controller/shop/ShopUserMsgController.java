/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.controller.shop;

import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.basic.bo.MsgBO;
import com.legendshop.basic.enums.MsgReceiverTypeEnum;
import com.legendshop.basic.query.MsgQuery;
import com.legendshop.basic.service.MessageService;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.security.dto.ShopUserDetail;
import com.legendshop.common.security.utils.SecurityUtils;
import com.legendshop.common.security.utils.UserTokenUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author legendshop
 */
@RestController
@RequestMapping(value = "/s/msg", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "系统通知")
public class ShopUserMsgController {

	@Autowired
	private MessageService messageService;
	@Autowired
	private UserTokenUtil userTokenUtil;

	@Operation(summary = "【商家】系统通知列表")
	@GetMapping("/page")
	public R<PageSupport<MsgBO>> queryAdminMsg(MsgQuery query) {
		ShopUserDetail shopUser = SecurityUtils.getShopUser();
		//修改为用店铺Id查询
		query.setReceiverId(shopUser.getShopId());
		query.setUserType(MsgReceiverTypeEnum.SHOP_USER.value());
		//系统通知
		PageSupport<MsgBO> collect = messageService.queryMyMsgPage(query);
		return R.ok(collect);
	}


	@Operation(summary = "【商家】系统通知详情")
	@GetMapping
	public R<MsgBO> getMsg(@RequestParam Long msgId, HttpServletRequest request) {
		Long userId = SecurityUtils.getShopUser().getShopId();
		return R.ok(messageService.getByMsgId(userId, msgId, MsgReceiverTypeEnum.SHOP_USER.value()));
	}

	@Operation(summary = "【商家】系统通知列表未读数量")
	@GetMapping("/unread")
	public R<Integer> getUnreadNum() {
		return messageService.unreadMsg(SecurityUtils.getShopUser().getShopId(), MsgReceiverTypeEnum.SHOP_USER);
	}

	@Operation(summary = "【商家】系统通知列表所有信息未读数量")
	@GetMapping("/unallread")
	public R<Integer> getUnAllReadNum() {
		return R.ok(messageService.getUnAllReadNum(SecurityUtils.getShopUser().getShopId()));
	}

	@Operation(summary = "【商家】一键清除消息未读标记")
	@GetMapping("/cleanUnread")
	public R<Void> cleanUnread() {
		messageService.cleanUnread(SecurityUtils.getShopUser().getShopId(), MsgReceiverTypeEnum.SHOP_USER.value());
		return R.ok();
	}
}
