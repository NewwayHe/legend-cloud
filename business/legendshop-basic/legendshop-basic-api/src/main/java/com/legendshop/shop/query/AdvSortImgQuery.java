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
import lombok.Data;

import java.io.Serializable;

/**
 * (AdvSortImg)Query分页查询对象
 *
 * @author legendshop
 * @since 2021-07-09 15:29:10
 */
@Data
@Schema(description = "分类广告分页查询")
public class AdvSortImgQuery extends PageParams implements Serializable {

	private static final long serialVersionUID = -66674124635222186L;

	@Schema(description = "状态  1：上线   0：下线")
	private Integer status;

	@Schema(description = "分类id")
	private Long categoryId;

	@Schema(description = "广告名称")
	private String advImgName;

	/**
	 * 排序方式
	 */
	@Schema(description = "排序方式")
	private String prop;

	/**
	 * 升降序
	 */
	@Schema(description = "升降序")
	private String order;
}
