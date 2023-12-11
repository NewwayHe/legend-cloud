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
import org.hibernate.validator.constraints.Range;

import java.io.Serializable;
import java.util.Date;

/**
 * 商品评论( ProductComment)DTO
 *
 * @author legendshop
 */
@Schema(description = "商品评论DTO")
@Data
public class ProductCommentDTO extends BaseDTO implements Serializable {


	@Schema(description = "主键ID")
	private Long id;


	@Schema(description = "商品ID")
	private Long productId;


	@Schema(description = "商品名称")
	private String productName;


	@Schema(description = "商品图片")
	private String productPic;


	@Schema(description = "点评人ID")
	private Long userId;


	@Schema(description = "所属商家ID")
	private Long shopId;


	@Schema(description = "物流公司ID")
	private Long logisticsCompanyId;


	@Schema(description = "评论内容")
	@NotNull(message = "评论内容不能为空")
	private String content;


	@Schema(description = "IP来源")
	private String postIp;


	@Schema(description = "宝贝评分 1-5分")
	@Range(min = 1, max = 5, message = "宝贝评分不合法, 请重新选择")
	@NotNull(message = "宝贝评分不能为空")
	private Integer score;


	@Schema(description = "店铺评分 1-5分")
	@Range(min = 1, max = 5, message = "店铺评分不合法, 请重新选择")
	@NotNull(message = "店铺评分不能为空")
	private Integer shopScore;


	@Schema(description = "物流评分 1-5分")
	@Range(min = 1, max = 5, message = "物流评分不合法, 请重新选择")
	@NotNull(message = "评分不能为空")
	private Integer logisticsScore;


	@Schema(description = "综合分数")
	private Double compositeScore;

	private Double composite;

	@Schema(description = "订单项id")
	@NotNull(message = "订单项id不能为空")
	private Long orderItemId;


	@Schema(description = "评论图片, 多个图片以逗号隔开")
	private String photos;

	@Schema(description = "评论图片数量")
	private Integer photosSize;


	@Schema(description = "是否匿名 (1:是  0:否)")
	private Boolean anonymousFlag;


	@Schema(description = "是否已追加评论")
	private Boolean addCommFlag;


	@Schema(description = "是否显示 1:为显示，0:待审核， -1：不通过审核，不显示。 如果需要审核评论，则是0,，否则1")
	private Integer status;


	@Schema(description = "评论时间")
	private Date createTime;


	@Schema(description = "商家是否已回复")
	private Boolean replyFlag;


	@Schema(description = "商家回复内容")
	private String shopReplyContent;


	@Schema(description = "商家回复时间")
	private Date shopReplyTime;


	@Schema(description = "评论数")
	private Integer count;


	@Schema(description = "待回复数")
	private Integer replyCount;

	@Schema(description = "追加评论图片")
	private String addPhotos;

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

	/**
	 * 评论视频
	 */
	@Schema(description = "评论视频, 多个图片以逗号隔开")
	private String video;
}
