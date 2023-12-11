/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author legendshop
 */
@Data
@Schema(description = "售后信息")
public class OrderAftersalesInformationDTO implements Serializable {

	@Schema(description = "创建来源  0 用户 1 商家 2 平台")
	private Integer refundSource;

	@Schema(description = "取消原因")
	private String reason;

	@Schema(description = "备注说明")
	private String sellerMessage;

	@Schema(description = "状态信息")
	private String statusInformation;

	@Schema(description = "申请时间")
	private Date createTime;

	@Schema(description = "售后号")
	private String refundSn;

	@Schema(description = "售后类型")
	private Integer applyType;

	@Schema(description = "售后状态 0:默认,1:在处理,2:处理完成 3已结束")
	private Integer refundStatus;

}
