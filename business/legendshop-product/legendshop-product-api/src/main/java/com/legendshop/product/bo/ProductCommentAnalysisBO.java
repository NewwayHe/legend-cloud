/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.bo;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 评论统计分析
 *
 * @author legendshop
 */
@Data
@Schema(description = "评论统计分析")
public class ProductCommentAnalysisBO implements Serializable {


	@Schema(description = "全部评论")
	private Long all;


	@Schema(description = "好评数")
	private Long good;


	@Schema(description = "中评数")
	private Long medium;


	@Schema(description = "差评数")
	private Long poor;


	@Schema(description = "有图")
	private Long photo;


	@Schema(description = "有追评")
	private Long append;

	public ProductCommentAnalysisBO() {
		this.all = 0L;
		this.good = 0L;
		this.medium = 0L;
		this.poor = 0L;
		this.photo = 0L;
		this.append = 0L;
	}
}
