/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.bo;


import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 物流评分统计BO
 *
 * @author legendshop
 */
@Data
public class LogisticsCommentStatisticsBO implements Serializable {

	private static final long serialVersionUID = -3367157942312169094L;

	/**
	 * 主键
	 */
	private Long id;

	/**
	 * 店铺ID
	 */
	private Long shopId;

	/**
	 * 物流公司ID
	 */
	private Long logisticsCompanyId;


	/**
	 * 点评次数
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
