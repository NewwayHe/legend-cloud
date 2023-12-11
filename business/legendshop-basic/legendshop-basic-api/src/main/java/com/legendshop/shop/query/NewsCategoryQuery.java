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

/**
 * 帮助栏目的查询DTO
 *
 * @author legendshop
 */
@Schema(description = "帮助栏目查询参数")
@Data
public class NewsCategoryQuery extends PageParams {

	private static final long serialVersionUID = -1108366170593028555L;

	@Schema(description = "栏目名")
	private String newsCategoryName;

	@Schema(description = "状态")
	private Integer status;

	@Schema(description = "查询开始时间")
	private String beginTime;

	@Schema(description = "查询结束时间")
	private String endTime;

	@Schema(description = "栏目id")
	private Long id;

	@Schema(description = "关键字")
	private String word;

	@Schema(description = "显示页面端 [0.全不显示  1.用户端显示  2.商家端显示  3.全部显示]")
	private Integer displayPage;

	@Schema(description = "文章每页数量")
	private Integer newsPageSize;
}
