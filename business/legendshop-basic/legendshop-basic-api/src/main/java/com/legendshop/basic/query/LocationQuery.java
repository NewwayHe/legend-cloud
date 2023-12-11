/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.query;

import cn.legendshop.jpaplus.support.PageParams;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 区域管理
 *
 * @author legendshop
 */
@Data
@Schema(description = "区域管理分页参数")
public class LocationQuery extends PageParams {

	private static final long serialVersionUID = -7847059065665558833L;
	@Schema(description = "地区id")
	private long id;
	@Schema(description = "地区级别")
	private Integer grade;
	@Schema(description = "地区编码")
	private String code;
	@Schema(description = "上级地址ID")
	private Long parentId;

	@Schema(description = "地区名称")
	private String name;
}
