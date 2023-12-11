/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.entity;


import cn.legendshop.jpaplus.persistence.*;
import cn.legendshop.jpaplus.support.GenericEntity;
import lombok.Data;

/**
 * 店铺评分统计表(ShopCommentStatistics)实体类
 *
 * @author legendshop
 */
@Data
@Entity
@Table(name = "ls_shop_comment_statistics")
public class ShopCommentStatistics implements GenericEntity<Long> {

	private static final long serialVersionUID = -50073041554974952L;

	/**
	 * 主键
	 */
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "generator")
	@TableGenerator(name = "generator", pkColumnValue = "SHOP_COMMENT_STATISTICS_SEQ")
	private Long id;


	/**
	 * 商家ID
	 */
	@Column(name = "shop_id")
	private Long shopId;


	/**
	 * 评分次数
	 */
	@Column(name = "count")
	private Integer count;


	/**
	 * 总分数
	 */
	@Column(name = "score")
	private Integer score;

}
