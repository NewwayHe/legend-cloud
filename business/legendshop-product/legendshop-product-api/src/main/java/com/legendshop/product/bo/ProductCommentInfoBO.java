/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.bo;


import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 商品评论、追加评论BO
 *
 * @author legendshop
 */
@Schema(description = "商品评论、追加评论BO")
@Data
public class ProductCommentInfoBO implements Serializable {


	@Schema(description = "评论ID")
	private Long id;


	@Schema(description = "商品ID")
	private Long productId;


	@Schema(description = "商品名称")
	private String productName;

	@Schema(description = "商品所属商家店铺ID")
	private Long shopId;

	@Schema(description = "商品所属商家店铺名")
	private String shopName;


	@Schema(description = "用户ID")
	private Long userId;


	@Schema(description = "用户名")
	private String nickName;


	@Schema(description = "用户头像")
	private String portrait;


	@Schema(description = "评论内容")
	private String content;


	@Schema(description = "评论图片")
	private String photos;


	@Schema(description = "评论图片列表")
	private List<String> photoList;


	@Schema(description = "评论时间")
	private Date addTime;


	@Schema(description = "评论状态：待审核为0，审核通过为1，审核不通过为-1")
	private Integer status;


	@Schema(description = "评论得分")
	private Integer score;


	@Schema(description = "店铺得分")
	private Integer shopScore;


	@Schema(description = "物流得分")
	private Integer logisticsScore;


	@Schema(description = "购买属性")
	private String attribute;


	@Schema(description = "购物时间")
	private Date buyTime;


	@Schema(description = "是否匿名(1:是  0:否)")
	private Boolean anonymousFlag;


	@Schema(description = "是否已追加评论")
	private Boolean addCommFlag;


	@Schema(description = "商家回复内容")
	private String shopReplyContent;


	@Schema(description = "商家回复时间")
	private Date shopReplyTime;


	@Schema(description = "商家是否已回复")
	private Boolean replyFlag;


	@Schema(description = "追加评论id")
	private Long addId;


	@Schema(description = "追加评论内容")
	private String addContent;


	@Schema(description = "追加评论图片")
	private String addPhotos;

	@Schema(description = "追加评论图片列表")
	private List<String> addPhotoList;


	@Schema(description = "追加评论状态")
	private Integer addStatus;


	@Schema(description = "追加创建时间")
	private Date addAddTime;


	@Schema(description = "商家是否已回复追加评论")
	private Boolean addReplyFlag;


	@Schema(description = "追加商家回复")
	private String addShopReplyContent;


	@Schema(description = "追加商家回复时间")
	private Date addShopReplyTime;


	@Schema(description = "多少天后追加的评论,如果为0则显示为用户当天追加评论")
	private Integer AppendDays;


	@Schema(description = "订单号")
	private String orderNumber;


	@Schema(description = "商品图片")
	private String prodPic;


	@Schema(description = "评论状态： 0 未评价 1 已评价")
	private Boolean commFlag;


	@Schema(description = "订单项Id")
	private Long orderItemId;


	@Schema(description = "新加字段是否已经点赞")
	private Boolean isAlreadyUseful;


	@Schema(description = "评论平均得分")
	private Double averageScore;


	@Schema(description = "评论审核时间")
	private Date auditTime;


	@Schema(description = "追评审核时间")
	private Date addAuditTime;

	/**
	 * 评论内容得积分开关
	 */
	@Schema(description = "评论内容得积分开关")
	private Boolean commentContentFlag = false;

	/**
	 * 评论字数
	 */
	@Schema(description = "评论字数")
	private Integer contentNumber;

	/**
	 * 可得积分
	 */
	@Schema(description = "可得积分")
	private Integer contentIntegral;

	/**
	 * 评论图片得积分开关
	 */
	@Schema(description = "评论图片得积分开关")
	private Boolean picFlag = false;

	/**
	 * 图片数量
	 */
	@Schema(description = "图片数量")
	private Integer picNumber;

	/**
	 * 可得积分
	 */
	@Schema(description = "可得积分")
	private Integer picIntegral;

	/**
	 * 删除类型  默认o 删除1
	 */
	@Schema(description = "删除类型  默认o 删除1")
	private Integer deleteType;

	public Integer getAppendDays() {
		if (null != this.addCommFlag && this.addCommFlag) {
			if (null != this.buyTime && null != this.addAddTime) {
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(this.buyTime);

				int day1 = calendar.get(Calendar.DAY_OF_YEAR);

				calendar.setTime(this.addAddTime);
				int day2 = calendar.get(Calendar.DAY_OF_YEAR);

				return day2 - day1;
			}
		}

		return null;
	}

	public List<String> getPhotoList() {
		if (StrUtil.isNotBlank(this.photos)) {
			return (List<String>) JSONUtil.toList(JSONUtil.parseArray(this.photos), String.class);
		}
		return new ArrayList<>();
	}

	public List<String> getAddPhotoList() {
		if (StrUtil.isNotBlank(this.addPhotos)) {
			return (List<String>) JSONUtil.toList(JSONUtil.parseArray(this.addPhotos), String.class);
		}
		return new ArrayList<>();
	}
}
