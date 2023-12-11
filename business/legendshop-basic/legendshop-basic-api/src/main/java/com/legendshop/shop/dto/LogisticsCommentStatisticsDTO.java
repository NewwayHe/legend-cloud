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

/**
 * 物流评分统计DTO
 *
 * @author legendshop
 */
@Data
public class LogisticsCommentStatisticsDTO implements Serializable {


	/**
	 * 主键
	 */
	private Long id;


	/**
	 * 物流公司ID
	 */
	private Long logisticsCompanyId;

	/**
	 * 商家id
	 */
	private Long shopId;


	/**
	 * 点评次数
	 */
	private Integer count;


	/**
	 * 总分数
	 */
	private Integer score;

}
