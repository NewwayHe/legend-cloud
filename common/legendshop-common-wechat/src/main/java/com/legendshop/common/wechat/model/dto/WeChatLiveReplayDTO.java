/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.wechat.model.dto;

import com.legendshop.common.core.dto.BasePageRequest;
import lombok.Data;

/**
 * 回放DTO
 *
 * @author legendshop
 */
@Data
public class WeChatLiveReplayDTO extends BasePageRequest {

	private static final long serialVersionUID = -4361217531422556772L;

	/**
	 * 直播房间ID
	 */
	private Integer roomId;
}
