/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.api;

import com.legendshop.basic.dto.MessagePushDTO;
import com.legendshop.basic.enums.MsgSendTypeEnum;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.constant.ServiceNameConstants;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 消息推送
 *
 * @author legendshop
 */
@FeignClient(contextId = "messageApi", value = ServiceNameConstants.BASIC_SERVICE)
public interface MessageApi {

	String PREFIX = ServiceNameConstants.BASIC_SERVICE_RPC_PREFIX;

	/**
	 * 推送单条消息
	 *
	 * @param messagePushDTO 消息推送信息DTO
	 * @return 推送结果
	 */
	@PostMapping(value = PREFIX + "/message/push")
	R push(@RequestBody MessagePushDTO messagePushDTO);

	/**
	 * 平台发送用户消息站内信
	 *
	 * @param adminUserId 平台用户ID
	 * @param receiverId  接收者ID
	 * @param content     消息内容
	 * @return 发送结果
	 */
	@PostMapping(value = PREFIX + "/message/send")
	R<Void> send(@RequestParam(value = "adminUserId") Long adminUserId, @RequestParam(value = "receiverId") Long receiverId, @RequestParam(value = "content") String content);


	/**
	 * 平台向店铺发送消息站内信
	 *
	 * @param adminUserId 发送消息的平台用户ID
	 * @param receiverId  接收消息的店铺ID
	 * @param content     消息内容
	 * @return 发送结果
	 */
	@PostMapping(value = PREFIX + "/message/sendToShop")
	R<Void> sendToShop(@RequestParam(value = "adminUserId") Long adminUserId, @RequestParam(value = "receiverId") Long receiverId, @RequestParam(value = "content") String content);

	/**
	 * 推送消息
	 *
	 * @param messagePushDTOList
	 * @return
	 */
	@PostMapping(value = PREFIX + "/message/pushList")
	R<Void> pushList(@RequestBody List<MessagePushDTO> messagePushDTOList);

	/**
	 * 给平台用户所有具站内信权限的用户发送站内信
	 *
	 * @param content  发送内容
	 * @param detailId 详情ID 商品传商品ID 团购活动传团购活动ID
	 * @return Boolean true 发送成功 false 发送失败
	 */
	@PostMapping(value = PREFIX + "/message/sendToAllAdmin")
	R<Boolean> sendToAllAdmin(@RequestParam(value = "content") String content,
							  @RequestParam(value = "detailId", required = false) Long detailId,
							  @RequestParam(value = "msgSendTypeEnum") MsgSendTypeEnum msgSendTypeEnum);

	/**
	 * 平台发送管理员消息站内信
	 *
	 * @param detailId   详情ID
	 * @param receiverId 接收者ID
	 * @param content    消息内容
	 * @return 发送结果
	 */
	@PostMapping(value = PREFIX + "/message/sendAdmin")
	R<Void> sendAdmin(@RequestParam(value = "detailId") Long detailId, @RequestParam(value = "receiverId") Long receiverId, @RequestParam(value = "content") String content);
}
