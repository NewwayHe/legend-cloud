/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.query;

import cn.legendshop.jpaplus.support.PageParams;
import com.legendshop.shop.enums.PubTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author legendshop
 */
@Data
@Schema(description = "公告查询Query")
public class PubQuery extends PageParams {

	@Schema(description = "标题")
	private String title;

	/**
	 * {@link PubTypeEnum}
	 */
	@Schema(description = "类型 [0：买家公告 1：卖家公告]")
	private Integer type;

	@Schema(description = "状态 [0：下线 1：上线]")
	private Integer status;

	@Schema(description = "用户ID(商家ID)")
	private Long userId;

	/**
	 * ReceiverEnum
	 */
	@Schema(description = "接收人类型")
	private Integer receiverType;
}
