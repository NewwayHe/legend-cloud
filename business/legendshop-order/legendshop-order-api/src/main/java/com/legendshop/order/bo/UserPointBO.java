/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.bo;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author legendshop
 */
@Data
public class UserPointBO implements Serializable {
	private static final long serialVersionUID = -507085849129770188L;


	private Long id;

	@Schema(description = "商家id")
	private Long shopId;

	@Schema(description = "自提点id")
	private Long sincePointId;

	@Schema(description = "用户id")
	private Long userId;

	@Schema(description = "创建时间")
	private Date createTime;

	@Schema(description = "更新时间")
	private Date updateTime;
}
