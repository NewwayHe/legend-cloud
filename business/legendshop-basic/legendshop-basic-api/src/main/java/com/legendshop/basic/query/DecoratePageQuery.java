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
 * 装修页面搜索参数
 *
 * @author legendshop
 */
@Data
@Schema(description = "装修页面搜索参数")
public class DecoratePageQuery extends PageParams {


	@Schema(description = "页面名称")
	private String name;


	@Schema(description = "页面类型 [INDEX：首页 POSTER：海报页]")
	private String category;


	@Schema(description = "状态 [-1：未发布 0:已修改未发布 1:已发布 ]")
	private Integer status;


	@Schema(description = "是否已使用")
	private Boolean useFlag;


	@Schema(description = "来源[pc：pc端 mobile：移动端]")
	private String source;

	@Schema(description = "类型 1 官方  2 原创")
	private Integer type;

	/**
	 * 是否需要设置第一个为空
	 */
	@Schema(description = "是否需要设置第一个为空")
	private Boolean isSetFirstEmpty;

}
