/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.data.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * (ShopView)DTO
 *
 * @author legendshop
 * @since 2021-06-17 13:44:29
 */
@Data
@Accessors(chain = true)
@Schema(description = "店铺浏览数据DTO")
public class ShopViewDTO implements Serializable {

	private static final long serialVersionUID = 982870632935123273L;

	/**
	 * ID
	 */
	@Schema(description = "ID")
	private Long id;

	/**
	 * 用户id
	 */
	@Schema(description = "用户id")
	private Long userId;

	/**
	 * 店铺id
	 */
	@Schema(description = "店铺id")
	private Long shopId;

	/**
	 * 创建时间
	 */
	@Schema(description = "创建时间")
	private Date createTime;

	/**
	 * 访问总人数
	 */
	@Schema(description = "访问总人数")
	private Integer viewPeople;

	/**
	 * 访问总次数
	 */
	@Schema(description = "访问总次数")
	private Integer viewFrequency;

	/**
	 * 来源
	 */
	@Schema(description = "来源")
	private String source;

}
