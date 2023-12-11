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
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

/**
 * 新闻(News)实体类
 *
 * @author legendshop
 */
@Schema(description = "帮助文章DTO")
@Data
public class NewsDTO extends BaseDTO implements Serializable {


	private static final long serialVersionUID = 8247564889242546869L;
	/**
	 * 帮助文章ID
	 */
	@Schema(description = "帮助文章ID")
	private Long id;


	/**
	 * 用户ID
	 */
	@Schema(description = "用户ID")
	private Long userId;


	/**
	 * 帮助文章分类
	 */
	@Schema(description = "栏目id")
	private Long newsCategoryId;


	/**
	 * 帮助文章标题
	 */
	@Schema(description = "帮助文章标题")
	@NotBlank(message = "新闻标题不能为空")
	private String newsTitle;


	/**
	 * 帮助文章提要
	 */
	@Schema(description = "帮助文章提要")
	private String newsBrief;


	/**
	 * 帮助文章内容
	 */
	@Schema(description = "帮助文章内容")
	private String newsContent;


	/**
	 * 帮助文章状态，1：上线，0：下线
	 */
	@Schema(description = "帮助文章状态")
	private Integer status;


	/**
	 * 是否高亮,1:yes,0:no
	 */
	@Schema(description = "是否高亮,1:yes,0:no")
	private Integer highLine;

	@Schema(description = "顺序")
	private Integer seq;

	/**
	 * 帮助文章位置[底部、头部、...]
	 */
	@Schema(description = "帮助文章位置")
	private Integer position;

	/**
	 * 类型 1：内链  2外链
	 */
	@Schema(description = "类型 1：内链  2外链")
	private Integer type;

	/**
	 * 路径
	 */
	@Length(max = 150, message = "跳转路径最多不能超过150个字符")
	@Schema(description = "路径")
	private String url;

	/**
	 * 显示页面端  {@link com.legendshop.basic.enums.DisplayPageEnum}
	 */
	@Schema(description = "显示页面 [0.全不显示  1.用户端显示  2.商家端显示  3.全部显示]")
	@EnumValid(target = DisplayPageEnum.class, message = "显示页面状态异常")
	private Integer displayPage;

}
