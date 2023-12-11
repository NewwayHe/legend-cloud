/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.entity;


import cn.legendshop.jpaplus.persistence.*;
import cn.legendshop.jpaplus.support.GenericEntity;
import lombok.Data;

/**
 * 评论统计表(ProductCommentStatistics)实体类
 *
 * @author legendshop
 */
@Data
@Entity
@Table(name = "ls_product_comment_statistics")
public class ProductCommentStatistics implements GenericEntity<Long> {

	private static final long serialVersionUID = 616296663907906416L;

	/**
	 * 主键
	 */
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "generator")
	@TableGenerator(name = "generator", pkColumnValue = "PRODUCT_COMMENT_STATISTICS_SEQ")
	private Long id;


	/**
	 * 商品ID
	 */
	@Column(name = "product_id")
	private Long productId;


	/**
	 * 评论数
	 */
	@Column(name = "comments")
	private Integer comments;


	/**
	 * 评论分总数
	 */
	@Column(name = "score")
	private Integer score;

}
