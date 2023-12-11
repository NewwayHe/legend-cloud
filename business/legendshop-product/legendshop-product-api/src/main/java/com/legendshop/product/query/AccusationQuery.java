/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.query;


import cn.legendshop.jpaplus.support.PageParams;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * 举报表查询Dto
 *
 * @author legendshop
 */
@Schema(description = "商品举报查询参数")
@Getter
@Setter
public class AccusationQuery extends PageParams {

	private static final long serialVersionUID = -9009401857688147697L;

	/**
	 * 商品名称
	 */
	@Schema(description = "商品名称")
	String productName;

	/**
	 * 举报类型id
	 */
	@Schema(description = "举报类型id")
	Long typeId;

	/**
	 * 举报开始时间
	 */
	@Schema(description = "举报开始时间")
	String begDate;

	/**
	 * 举报结束时间
	 */
	@Schema(description = "举报结束时间")
	String endDate;

	/**
	 * 店铺id
	 */
	@Schema(description = "店铺id")
	Long shopId;

	/**
	 * 状态【0:未处理， 1:已经处理】
	 */
	@Schema(description = "状态【0:未处理， 1:已经处理】")
	Integer status;

	/**
	 * 处理结果【-1:无效举报 1：有效举报  -2：恶意举报】
	 */
	@Schema(description = "处理结果【-1:无效举报 1：有效举报  -2：恶意举报】")
	Integer result;

	/**
	 * 举报人id
	 */
	@Schema(description = "举报人id")
	Long userId;

	/**
	 * 举报内容
	 */
	@Schema(description = "举报内容")
	String content;

}
