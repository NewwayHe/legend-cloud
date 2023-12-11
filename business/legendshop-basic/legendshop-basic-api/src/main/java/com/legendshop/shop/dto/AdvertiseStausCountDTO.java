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

/**
 * @author legendshop
 */
@Data
public class AdvertiseStausCountDTO {

	/**
	 * 广告状态:0未开始,1开始,2暂停,3结束
	 */
	@Schema(description = "广告状态:0未开始,1开始,2暂停,3结束")
	private Integer status;

	/**
	 * 广告数量
	 */
	@Schema(description = "广告数量")
	private Integer count;

	public AdvertiseStausCountDTO() {

	}

	public AdvertiseStausCountDTO(Integer status, Integer count) {
		this.status = status;
		this.count = count;
	}

}
