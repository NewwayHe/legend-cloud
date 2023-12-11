/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 用户发票详情
 *
 * @author legendshop
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInvoiceDetailDTO {

	/**
	 * 用户发票
	 */

	@Schema(description = "是否开具")
	private Boolean hasInvoiceFlag;

	@Schema(description = "用户发票类型  NORMAL:增值税普票, DEDICATED:增值税专票")
	private String type;

	@Schema(description = "普票类型，PERSONAL:个人，COMPANY:单位")
	private String titleType;

	@Schema(description = "发票内容 默认2：商品明细")
	private Integer content;

	@Schema(description = "抬头信息")
	private String company;

	@Schema(description = "申请时间")
	private Date createTime;

	@Schema(description = "收件人")
	private String name;

	@Schema(description = "电话")
	private String mobile;

	@Schema(description = "地址")
	private String address;

	@Schema(description = "店铺名字")
	private String shopName;

	@Schema(description = "图片路径")
	private String path;

	@Schema(description = "商品名字")
	private String productName;

	@Schema(description = "价格")
	private BigDecimal price;

	@Schema(description = "商品总价")
	private BigDecimal totalPrice;

	@Schema(description = "店铺id")
	private Long shopId;

	@Schema(description = "购买件数")
	private Integer counts;

	@Schema(description = "省份id")
	private Long provinceId;

	@Schema(description = "城市id")
	private Long cityId;

	@Schema(description = "地区id")
	private Long areaId;

	@Schema(description = "街道id")
	private Long streetId;

	@Schema(description = "订单id")
	private Long orderId;
	@Schema(description = "订单项信息")
	private OrderInvoiceDTO orderInvoiceDTO;

}
