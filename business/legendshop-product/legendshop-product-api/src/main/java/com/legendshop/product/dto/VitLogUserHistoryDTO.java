/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户足迹
 *
 * @author legendshop
 */
@Data
@Schema(description = "用户足迹")
public class VitLogUserHistoryDTO implements Serializable {
	private static final long serialVersionUID = 194550812846456795L;

	/**
	 * 商品ID
	 */
	@Schema(description = "商品ID")
	private Long productId;

	/**
	 * 商品名称
	 */
	@Schema(description = "商品名称")
	private String productName;

	/**
	 * 商品图片
	 */
	@Schema(description = "商品图片")
	private String pic;

	/**
	 * 商品售价
	 */
	@Schema(description = "商品售价")
	private String price;

	/**
	 * 访问次数
	 */
	@Schema(description = "访问次数")
	private Integer visitNum;

	/**
	 * 访问时间
	 */
	@Schema(description = "访问时间")
	private Date createTime;

	/**
	 * 格式化后的时间，用于前端归类
	 */
	@Schema(description = "格式化后的时间，用于前端归类")
	private String dateTime;
}
