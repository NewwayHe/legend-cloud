/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.api;

import com.legendshop.basic.bo.UserMsgBo;
import com.legendshop.basic.dto.ReceiverDTO;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.constant.ServiceNameConstants;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author legendshop
 */
@FeignClient(contextId = "userMsgApi", value = ServiceNameConstants.BASIC_SERVICE)
public interface UserMsgApi {

	String PREFIX = ServiceNameConstants.BASIC_SERVICE_RPC_PREFIX;

	@GetMapping(value = PREFIX + "/p/msg/unread")
	R<UserMsgBo> userUnreadMsg(@RequestParam(value = "userId") Long userId);

	/**
	 * 判断已读消息是否存在 true 存在 false 不存在
	 *
	 * @param userId 用户ID
	 * @param msgId  业务ID
	 * @param type   业务类型
	 * @return
	 */
	@GetMapping(value = PREFIX + "/p/msg/isExist")
	Boolean isExist(@RequestParam(value = "userId") Long userId, @RequestParam(value = "msgId") Long msgId, @RequestParam(value = "type") Integer type);

	/**
	 * 保存已读记录
	 *
	 * @param receiverDTOS
	 */
	@PostMapping(value = PREFIX + "/p/msg/saveReceivers")
	R<Boolean> saveReceivers(@RequestBody List<ReceiverDTO> receiverDTOS);
}
