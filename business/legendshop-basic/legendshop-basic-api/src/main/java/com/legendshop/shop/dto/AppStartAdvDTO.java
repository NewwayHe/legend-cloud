/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.dto;


import com.legendshop.common.core.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;

/**
 * App启动广告表(AppStartAdv)实体类
 *
 * @author legendshop
 */
@Schema(description = "活动广告DTO")
@Data
public class AppStartAdvDTO extends BaseDTO implements Serializable {


	private static final long serialVersionUID = 6939242802106974616L;
	/**
	 * 主键
	 */
	@Schema(description = "主键")
	private Long id;


	/**
	 * 广告名称
	 */
	@Schema(description = "广告名称")
	@NotBlank(message = "广告名称不能为空")
	private String name;


	/**
	 * 广告图片
	 */
	@Schema(description = "广告图片")
	@NotBlank(message = "广告图片不能为空")
	private String imgUrl;


	/**
	 * 跳转的url
	 */
	@Schema(description = "跳转的url")
	@NotNull(message = "跳转的url不能为空")
	private HashMap<String, Object> url;


	/**
	 * 广告的描述
	 */
	@Schema(description = "广告的描述")
	private String description;


	/**
	 * 状态 0:下线,1:上线
	 */
	@Schema(description = "状态 0:下线,1:上线")
	private Integer status;


	/**
	 * 创建时间
	 */
	@Schema(description = "创建时间")
	private Date createTime;

}
