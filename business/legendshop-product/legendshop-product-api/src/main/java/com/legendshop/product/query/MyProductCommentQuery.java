/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.query;

import cn.hutool.core.util.StrUtil;
import cn.legendshop.jpaplus.support.PageParams;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 商品评论搜索DTO
 *
 * @author legendshop
 */
@Schema(description = "商品评论搜索DTO")
@Data
@Accessors(chain = true)
public class MyProductCommentQuery extends PageParams {

	/**
	 * 商品ID
	 */
	@Schema(description = "商品ID")
	private Long productId;

	/**
	 * 订单编号
	 */
	@Schema(description = "订单编号")
	private String orderNumber;

	/**
	 * 用户ID
	 */
	@Schema(description = "用户ID")
	private Long userId;

	/**
	 * 查询条件  {@link com.legendshop.product.enums.MyProductCommentEnum};
	 */
	@Schema(description = "查询条件 [0.全部  1.待评论 2.待追评和已追评]")
	private String condition;

	/**
	 * 商品名称
	 */
	@Schema(description = "商品名称")
	private String productName;

	/**
	 * 评论开始时间
	 */
	@Schema(description = "评论开始时间")
	private String startTime;

	/**
	 * 评论结束时间
	 */
	@Schema(description = "评论结束时间")
	private String endTime;

	/**
	 * 追加评论开始时间
	 */
	@Schema(description = "追加评论开始时间")
	private String addStartTime;

	/**
	 * 追加评论结束时间
	 */
	@Schema(description = "追加评论结束时间")
	private String addEndTime;

	public String getCondition() {
		if (StrUtil.isBlank(condition)) {
			condition = "0";
		}
		return condition;
	}
}

