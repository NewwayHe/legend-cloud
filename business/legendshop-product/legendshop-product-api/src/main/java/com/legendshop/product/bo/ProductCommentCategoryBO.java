/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.bo;

import lombok.Data;

import java.io.Serializable;

/**
 * 商品评论分类BO
 *
 * @author legendshop
 */
@Data
public class ProductCommentCategoryBO implements Serializable {


	/**
	 * 商品ID
	 */
	private Long productId;

	/**
	 * 评论总数
	 */
	private Integer total;

	/**
	 * 好评
	 */
	private Integer high;

	/**
	 * 中评
	 */
	private Integer medium;

	/**
	 * 差评
	 */
	private Integer low;

	/**
	 * 追评
	 */
	private Integer append;

	/**
	 * 有图
	 */
	private Integer photo;

	/**
	 * 待回复
	 */
	private Integer waitReply;

	/**
	 * 好评率
	 */
	private Double highRate;

	/**
	 * 中评率
	 */
	private Double mediumRate;

	/**
	 * 差评率
	 */
	private Double lowRate;

}
