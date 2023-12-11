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
import java.math.BigDecimal;

/**
 * 评论统计表(ProdCommStat)实体类
 *
 * @author legendshop
 */
@Data
public class ProductCommentStatisticsBO implements Serializable {


	private static final long serialVersionUID = 8532006110624487626L;
	/**
	 * 主键
	 */
	private Long id;

	/**
	 * 店铺ID
	 */
	private Long shopId;

	/**
	 * 商品ID
	 */
	private Long productId;


	/**
	 * 评论数
	 */
	private Integer comments;


	/**
	 * 评论分总数
	 */
	private Integer score;

	/**
	 * 平均分
	 */
	private BigDecimal avg;


}
