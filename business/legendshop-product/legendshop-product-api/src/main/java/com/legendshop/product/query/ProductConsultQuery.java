/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.query;

import cn.legendshop.jpaplus.support.PageParams;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.Date;

/**
 * @author legendshop
 */
@Schema(description = "商品咨询查询参数")
@Data
public class ProductConsultQuery extends PageParams {

	private static final long serialVersionUID = -2625272549184223496L;
	/**
	 * ID
	 */
	@Schema(description = "id")
	private Long id;


	/**
	 * 商品ID
	 */
	@Schema(description = "商品id")
	@NotEmpty(message = "商品id不能为空")
	private Long productId;


	/**
	 * 商品名称
	 */
	@Schema(description = "商品名称")
	@NotEmpty(message = "商品名称不能为空")
	private String productName;

	/**
	 * 商家ID
	 */
	@Schema(description = "商家id")
	private Long shopId;

	/**
	 * 店铺名称
	 */
	@Schema(description = " 店铺名称")
	private String shopName;

	/**
	 * 咨询类型 1: 商品咨询, 2:库存配送, 3:售后咨询'
	 */
	@Schema(description = "咨询类型 1: 商品咨询, 2:库存配送, 3:售后咨询")
	private Integer pointType;


	/**
	 * 咨询内容
	 */
	@Schema(description = "咨询内容")
	@NotEmpty(message = "咨询内容不能为空")
	private String content;


	/**
	 * 回答
	 */
	@Schema(description = "回答")
	private String answer;


	/**
	 * 咨询时间
	 */
	@Schema(description = "咨询时间")
	private Date recDate;

	/**
	 * 开始时间
	 */
	@Schema(description = "搜索开始时间")
	private String startDate;
	/**
	 * 结束时间
	 */
	@Schema(description = "搜索结束时间")
	private String endDate;

	/**
	 * ip 来源
	 */
	@Schema(description = "ip来源")
	private Integer postIp;


	/**
	 * 回答时间
	 */
	@Schema(description = "回答时间")
	private Date answerTime;

	/**
	 * 提问人
	 */
	@Schema(description = "提问人")
	private Long askUserId;


	/**
	 * 删除状态 0：保留 1：删除
	 */
	@Schema(description = "状态0：保留 1：删除")
	private Integer delSts;
	/**
	 * 状态 0：下线 1：上线
	 */
	@Schema(description = "状态 0下线 1上线")
	private Integer status;
	/**
	 * 回复状态 0未回复 1已回复
	 */
	@Schema(description = "回复状态")
	private Integer replySts;
	/**
	 * 回复人名称
	 */
	@Schema(description = "回复人名称")
	private String replyName;


}
