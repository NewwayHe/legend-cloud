/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.bo;

import com.legendshop.basic.enums.MsgSendTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 系统通知
 *
 * @author legendshop
 */
@Data
@Schema(description = "系统通知BO")
public class MsgBO implements Serializable {

	@Schema(description = "主键id")
	private Long id;

	@Schema(description = "标题")
	private String title;

	@Schema(description = "内容")
	private String content;

	@Schema(description = "状态，已读1， 未读0")
	private String status;

	@Schema(description = "建立时间")
	private Date recDate;

	@Schema(description = "详情id，例如商品ID")
	private Long detailId;

	@Schema(description = "详情子id，例如skuId")
	private Long detailItemId;

	/**
	 * {@link MsgSendTypeEnum}
	 */
	@Schema(description = "1:商品 2：订单 3:售后 0：其他")
	private Integer type;

	/**
	 * {@link MsgSendTypeEnum}
	 */
	@Schema(description = "1:商品 2：订单 3:售后 0：其他")
	private Integer typeDetail;


	/**
	 * {@link com.legendshop.basic.enums.MsgReceiverTypeEnum}
	 */
	@Schema(description = "1:普通用户， 2：商家用户，3：平台用户")
	private Integer userType;

	@Schema(description = "商品图片")
	private String pic;

	@Schema(description = "商品名称")
	private String productName;
}
