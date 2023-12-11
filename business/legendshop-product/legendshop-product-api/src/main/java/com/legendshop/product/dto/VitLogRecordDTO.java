/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.dto;


import com.legendshop.product.enums.VitLogPageEnum;
import com.legendshop.product.enums.VitLogSourceEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 浏览历史记录
 *
 * @author legendshop
 */
@Data
@Schema(description = "浏览历史记录")
@Accessors(chain = true)
public class VitLogRecordDTO implements Serializable {


	/**
	 * 店铺ID
	 */
	@Schema(description = "店铺ID")
	private Long shopId;

	/**
	 * 用户ID
	 */
	@Schema(description = "用户ID")
	private Long userId;

	/**
	 * 用户唯一标识，用于未登录前识别用户身份
	 */
	@Schema(description = "用户唯一标识")
	private String user_key;

	/**
	 * 商城名称
	 */
	@Schema(description = "商城名称")
	private String shopName;


	/**
	 * 商品ID
	 */
	@Schema(description = "商品ID")
	private Long productId;


	/**
	 * 产品名称
	 */
	@Schema(description = "产品名称")
	private String productName;


	/**
	 * IP
	 */
	@Schema(description = "IP")
	private String ip;

	/**
	 * 产品图片
	 */
	@Schema(description = "产品图片")
	private String pic;


	/**
	 * 产品售价价范围
	 */
	@Schema(description = "产品售价")
	private String price;


	/**
	 * 访问页面
	 * {@link VitLogPageEnum}
	 */
	@Schema(description = "访问的页面类型, 0: 商品页, 1: 店铺页")
	private Integer page;

	/**
	 * 来源
	 * {@link VitLogSourceEnum}
	 */
	@Schema(description = "参数为pc,h5")
	private String source;


	/**
	 * 用户是否已删除
	 */
	@Schema(description = "用户是否已删除")
	private Boolean userDelFlag = false;


}
