/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.dto;


import com.legendshop.basic.enums.DisplayPageEnum;
import com.legendshop.common.core.annotation.EnumValid;
import com.legendshop.common.core.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 新闻栏目(NewsCat)实体类
 *
 * @author legendshop
 */
@Schema(description = "帮助栏目查询参数")
@Data
public class NewsCategoryDTO extends BaseDTO implements Serializable {


	private static final long serialVersionUID = -7556036925118881293L;
	/**
	 * 帮助栏目ID
	 */
	@Schema(description = "帮助栏目ID")
	private Long id;


	/**
	 * 用户ID
	 */
	@Schema(description = "用户ID")
	private Long userId;


	/**
	 * 栏目名称
	 */
	@Schema(description = "栏目名")
	@NotBlank(message = "帮助栏目名称不能为空")
	private String newsCategoryName;


	/**
	 * 状态
	 */
	@Schema(description = "状态")
	private Integer status;


	/**
	 * 顺序
	 */
	@Schema(description = "顺序")
	private Integer seq;


	/**
	 * 图片
	 */
	@Schema(description = "图片")
	private String pic;

	/**
	 * 该栏目下的文章
	 */
	@Schema(description = "该栏目下的文章")
	private List<NewsDTO> items;

	/**
	 * 显示页面  {@link com.legendshop.basic.enums.DisplayPageEnum}
	 */
	@Schema(description = "显示页面 [0.全不显示  1.用户端显示  2.商家端显示  3.全部显示]")
	@EnumValid(target = DisplayPageEnum.class, message = "显示页面状态异常")
	private Integer displayPage;

}
