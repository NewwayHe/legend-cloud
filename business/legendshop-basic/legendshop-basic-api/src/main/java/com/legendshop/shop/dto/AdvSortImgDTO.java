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
import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;

/**
 * (AdvSortImg)DTO
 *
 * @author legendshop
 * @since 2021-07-09 15:29:10
 */
@Data
@Schema(description = "广告分类DTO")
public class AdvSortImgDTO extends BaseDTO implements Serializable {

	private static final long serialVersionUID = -83250244698203197L;

	/**
	 * 主键id
	 */
	@Schema(description = "主键id")
	private Long id;

	/**
	 * 广告名称
	 */
	@Schema(description = "广告名称")
	private String advImgName;

	/**
	 * 图片跳转链接
	 */
	@Schema(description = "图片跳转链接")
	private HashMap<String, Object> advUrl;

	/**
	 * 图片地址
	 */
	@Schema(description = "图片地址")
	private String advPath;

	/**
	 * 关联分类id
	 */
	@Schema(description = "广告分类id")
	private Long categoryId;

	/**
	 * 状态  1：上线   0：下线
	 */
	@Schema(description = "状态  1：上线   0：下线")
	private Integer status;

	@Schema(description = "分类名称")
	private String categoryName;

}
