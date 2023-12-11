/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.awt.*;
import java.io.Serializable;

/**
 * @author legendshop
 */
@Data
public class ShopDetailMsgSendConfig implements Serializable {

	private static final long serialVersionUID = -6882517947203918449L;


	@Schema(description = "图片验证码")
	private Image image;

	@Schema(description = "店铺id")
	private Long shopId;

	@Schema(description = "用户id")
	private Long userId;
}
