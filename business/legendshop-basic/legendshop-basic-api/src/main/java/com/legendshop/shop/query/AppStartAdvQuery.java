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
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * 活动广告页查询条件
 *
 * @author legendshop
 */
@Schema(description = "活动广告页查询参数")
@Getter
@Setter
public class AppStartAdvQuery extends PageParams {

	private static final long serialVersionUID = 7464025569153978393L;

	@Schema(description = "广告名")
	private String name;

	@Schema(description = "状态")
	private Integer status;

	@Schema(description = "id")
	private Long id;

}
