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
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;

/**
 * @author legendshop
 */
@Data
@Schema(description = "商品咨询DTO")
public class ProductConsultDTO {
	/**
	 * ID
	 */
	@Schema(description = "id")
	private Long id;


	/**
	 * 商品ID
	 */
	@Schema(description = "商品id")
	@NotNull(message = "商品id不能为空")
	private Long productId;


	/**
	 * 商品名称
	 */
	@Schema(description = "商品名称")
	private String productName;

	/**
	 * 商品url
	 */
	@Schema(description = "商品图片")
	private String pic;


	/**
	 * 商家ID
	 */
	@Schema(description = "商家id")
	@NotNull(message = "商家id不能为空")
	private Long shopId;

	/**
	 * 店铺名称
	 */
	@Schema(description = "店铺名称")
	private String shopName;

	/**
	 * 咨询内容
	 */
	@Schema(description = "咨询内容")
	@NotNull(message = "咨询内容不能为空")
	private String content;

	/**
	 * 咨询类型 1: 商品咨询, 2:库存配送, 3:售后咨询'
	 */
	@Schema(description = "咨询类型 1: 商品咨询, 2:库存配送, 3:售后咨询'")
	private Integer pointType;

	/**
	 * 回答
	 */
	@Schema(description = "回答")
	private String answer;
	/*

	 */

	/**
	 * 咨询时间
	 */
	@Schema(description = "咨询时间")
	private Date recDate;


	/**
	 * ip 来源
	 */
	@Schema(description = "ip来源")
	private Integer postIp;


	/**
	 * 回答时间
	 */
	@Schema(description = "回答时间")
	private Date answerTime;

	/**
	 * 提问人
	 */
	@Schema(description = "提问人")
	private Long askUserId;

	/**
	 * 提问人name
	 */
	@Schema(description = "提问用户名字")
	private String askUserName;

	/**
	 * 提问人
	 */
	@Schema(description = "提问用户手机")
	private String askUserIphone;


	/**
	 * 回复状态 0未回复 1已回复
	 */
	@Schema(description = "回复状态")
	private Integer replySts;

	/**
	 * 商家上线标记 0：下线 1：上线
	 */
	@Schema(description = "商家上线标记 0下线 1上线")
	private Integer status;

	/**
	 * 商家删除标记 0：下线 1：上线
	 */
	@Schema(description = "商家删除标记 0保留 1删除")
	private Integer delSts;
	/**
	 * 商品失效
	 */
	@Schema(description = "商品失效 true 失效 ")
	private Boolean productInvalid;

	/**
	 * 回复人名称
	 */
	@Schema(description = "回复人名称")
	private String replyName;

}
