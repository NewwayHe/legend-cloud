/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.bo;


import com.legendshop.common.core.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 商品评论( ProductComment)BO
 *
 * @author legendshop
 */
@Data
public class ProductCommentBO extends BaseDTO implements Serializable {


	/**
	 * ID
	 */
	@Schema(description = "Id")
	private Long id;

	/**
	 * 商品ID
	 */
	@Schema(description = "商品ID")
	private Long productId;

	@Schema(description = "商品名称")
	private String productName;

	@Schema(description = "商品图片")
	private String productPic;

	/**
	 * 点评人ID
	 */
	@Schema(description = "点评人ID")
	private Long userId;

	/**
	 * 用户昵称
	 */
	@Schema(description = "用户昵称")
	private String nickName;

	/**
	 * 用户头像
	 */
	@Schema(description = "用户头像")
	private String avatar;

	/**
	 * 所属商家ID
	 */
	@Schema(description = "所属商家ID")
	private Long shopId;

	@Schema(description = "物流公司ID")
	private Long logisticsCompanyId;

	/**
	 * 评论内容
	 */
	@Schema(description = "评论内容")
	private String content;


	/**
	 * IP来源
	 */
	@Schema(description = "IP来源")
	private String postIp;


	/**
	 * 宝贝得分，1-5分
	 */
	@Schema(description = "宝贝得分")
	private Integer score;


	/**
	 * 店铺评分, 1-5分
	 */
	@Schema(description = "店铺评分")
	private Integer shopScore;

	/**
	 * 物流评分，1-5分
	 */
	@Schema(description = "物流评分")
	private Integer logisticsScore;

	@Schema(description = "综合分数")
	private Double compositeScore;

	/**
	 * 订单项id
	 */
	@Schema(description = "订单项id")
	private Long orderItemId;


	/**
	 * 评论图片, 多个图片以逗号隔开
	 */
	@Schema(description = "评论图片, 多个图片以逗号隔开")
	private String photos;

	/**
	 * 评论视频
	 */
	@Schema(description = "评论视频")
	private String video;

	/**
	 * 是否匿名(1:是  0:否)
	 */
	@Schema(description = "是否匿名")
	private Boolean anonymousFlag;


	/**
	 * 是否已追加评论
	 */
	@Schema(description = "是否已追加评论")
	private Boolean addCommFlag;


	/**
	 * 0:待审核  1： 审核通过  -1：审核拒绝
	 */
	@Schema(description = "0:待审核  1： 审核通过  -1：审核拒绝")
	private Integer status;


	/**
	 * 评论时间
	 */
	@Schema(description = "评论时间")
	private Date createTime;


	/**
	 * 商家是否已回复
	 */
	@Schema(description = "商家是否已回复")
	private Boolean replyFlag;


	/**
	 * 商家回复内容
	 */
	@Schema(description = "商家回复内容")
	private String shopReplyContent;


	/**
	 * 商家回复时间
	 */
	@Schema(description = "商家回复时间")
	private Date shopReplyTime;

	@Schema(description = "追评集合")
	private List<ProductAddCommentBO> addCommentList;

	/**
	 * 删除类型  默认o 删除1
	 */
	@Schema(description = "删除类型  默认o 删除1")
	private Integer deleteType;

}
