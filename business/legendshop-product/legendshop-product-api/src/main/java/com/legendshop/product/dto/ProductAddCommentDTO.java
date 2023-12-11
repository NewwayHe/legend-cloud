/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.dto;

import com.legendshop.common.core.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 商品二次评论表DTO
 *
 * @author legendshop
 */
@Schema(description = "商品追加评论DTO")
@Data
public class ProductAddCommentDTO extends BaseDTO implements Serializable {

	private static final long serialVersionUID = -37753905842051072L;

	/**
	 * 主键ID
	 */
	@Schema(description = "主键ID")
	private Long id;

	/**
	 * 用户ID
	 */
	@Schema(description = "用户ID")
	private Long userId;

	/**
	 * 商品评论ID
	 */
	@Schema(description = "商品评论ID")
	@NotNull(message = "评论id不能为空")
	private Long productCommentId;


	/**
	 * 评论内容
	 */
	@Schema(description = "评论内容")
	@NotNull(message = "评论内容不能为空")
	private String content;


	/**
	 * 来源IP
	 */
	@Schema(description = "来源IP")
	private String postIp;


	/**
	 * 评论图片, 多个图片以逗号隔开
	 */
	@Schema(description = "评论图片, 多个图片以逗号隔开")
	private String photos;


	/**
	 * 0:待审核  1： 审核通过  -1：审核拒绝
	 */
	@Schema(description = "0:待审核  1： 审核通过  -1：审核拒绝")
	private Integer status;


	/**
	 * 创建时间
	 */
	@Schema(description = "创建时间")
	private Date createTime;


	/**
	 * 商家是否已回复
	 */
	@Schema(description = "商家是否已回复")
	private Boolean replyFlag;


	/**
	 * 商家回复
	 */
	@Schema(description = "商家回复")
	private String shopReplyContent;


	/**
	 * 商家回复时间
	 */
	@Schema(description = "商家回复时间")
	private Date shopReplyTime;

	/**
	 * 图片列表
	 */
	@Schema(description = "图片列表")
	private List<String> photoList;

	/**
	 * 评论审核时间
	 */
	@Schema(description = "评论审核时间")
	private Date auditTime;

	/**
	 * 删除类型  默认o 删除1
	 */
	@Schema(description = "删除类型  默认o 删除1")
	private Integer deleteType;

	@Schema(description = "视频列表")
	private String video;
}
