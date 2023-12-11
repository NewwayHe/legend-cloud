/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.dto;


import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 店铺评分统计表(ShopCommStat)实体类
 *
 * @author legendshop
 */
@Data
public class ShopCommentStatisticsDTO implements Serializable {


	private static final long serialVersionUID = 4678621987893802554L;
	/**
	 * 主键
	 */
	private Long id;


	/**
	 * 商家ID
	 */
	private Long shopId;


	/**
	 * 评分次数
	 */
	private Integer count;


	/**
	 * 总分数
	 */
	private Integer score;

	/**
	 * 平均分
	 */
	private BigDecimal avg;
}
