/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.query;


import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.legendshop.jpaplus.support.PageParams;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 商品评论搜索DTO
 *
 * @author legendshop
 */
@Schema(description = "商品评论查询参数")
@Data
@Accessors(chain = true)
public class ProductCommentQuery extends PageParams {

	/**
	 * 商品ID
	 */
	@Schema(description = "商品ID")
	private Long productId;

	/**
	 * {@link com.legendshop.product.enums.ProductCommConditionEnum}
	 */
	@Schema(description = "条件 全部: all, 好评: good, 中评: medium, 差评: poor, 有图: photo, 追评: append")
	private String condition;

	/**
	 * 订单编号
	 */
	@Schema(description = "订单编号")
	private String orderNumber;

	/**
	 * 订单ID
	 */
	@Schema(description = "订单ID")
	private Long orderId;

	/**
	 * 用户ID
	 */
	@Schema(description = "用户ID")
	private Long userId;

	/**
	 * 用户姓名
	 */
	@Schema(description = "用户名")
	private String nickName;

	/**
	 * 店铺ID
	 */
	@Schema(description = "店铺ID")
	private Long shopId;

	/**
	 * 店铺名称
	 */
	@Schema(description = "店铺名称")
	private String shopName;

	/**
	 * 状态
	 */
	@Schema(description = "状态[0：待审核，1：已通过，-1：已拒绝，3：全部]")
	private String status;

	/**
	 * 商品名称
	 */
	@Schema(description = "商品名称")
	private String productName;

	/**
	 * 排序方式  按评论时间排序： addtime   按评论平均分数排：averageScore
	 */
	@Schema(description = "排序方式  按评论时间排序： addtime   按评论平均分数排：averageScore  count:评价数 compositeScore:综合评分")
	private String orderBy;

	@Schema(description = "排序方式")
	private String prop;

	@Schema(description = "排序的方向：asc 或者 desc")
	private String order;

	@Schema(description = "分数范围 [1：0-1分  2：1-2分 3：2-3分 4：3-4分  5：4-5分]")
	private Integer ScoreRange;

	/**
	 * 评论开始时间
	 */
	@Schema(description = "评论开始时间")
	private String startTime;

	/**
	 * 评论结束时间
	 */
	@Schema(description = "评论结束时间")
	private String endTime;

	/**
	 * 追加评论开始时间
	 */
	@Schema(description = "追加评论开始时间")
	private String addStartTime;

	@Schema(description = "商家回复状态 0:待回复， 1：已回复  3.全部评论 ")
	private String replyStatus;

	/**
	 * 追加评论结束时间
	 */
	@Schema(description = "追加评论结束时间")
	private String addEndTime;

	/**
	 * 删除类型 默认0 删除1
	 */
	@Schema(description = "删除类型")
	private String deleteType;

	public String getCondition() {
		if (StrUtil.isBlank(condition)) {
			condition = "all";
		}
		return condition;
	}

//	public String getOrderBy() {
//		if(StrUtil.isBlank(orderBy)){
//			orderBy = "addtime";
//		}
//		return orderBy;
//	}

	public String getStatus() {
		if (StrUtil.isBlank(status)) {
			status = "3";
		}
		return status;
	}

	public String getReplyStatus() {
		if (ObjectUtil.isNull(replyStatus)) {
			replyStatus = "3";
		}
		return replyStatus;
	}

}

