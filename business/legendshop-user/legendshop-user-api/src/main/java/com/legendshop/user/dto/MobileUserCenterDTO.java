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
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author legendshop
 */
@Data
@Schema(description = "移动端个人中心")
public class MobileUserCenterDTO {

	@Schema(description = "商品收藏")
	private Integer prodFavorites;

	@Schema(description = "商家收藏")
	private Integer shopFavorites;

	@Schema(description = "文章收藏")
	private Integer articleFavorites;

	@Schema(description = "消息")
	private Integer message;

	@Schema(description = "历史足迹")
	private Integer footprint;

	@Schema(description = "优惠卷")
	private Integer coupon;

	@Schema(description = "总收藏统计数")
	private Integer totalFavorites;

	@Schema(description = "待付款")
	private Integer payment;

	@Schema(description = "待发货")
	private Integer paid;

	@Schema(description = "待签收")
	private Integer consignment;

	@Schema(description = "待收货")
	private Integer shipped;

	@Schema(description = "待评价")
	private Integer unCommCount;

	@Schema(description = "退款/售后")
	private Integer refund;

	@Schema(description = "可用余额")
	private BigDecimal availableAmount;

	@Schema(description = "待开具发票数量")
	private Integer toBeInvoicedOrderCount;

	@Schema(description = "已开具发票数量")
	private Integer invoicedOrderCount;

	public Integer getTotalFavorites() {
		return (this.prodFavorites == null ? 0 : this.prodFavorites) + (this.shopFavorites == null ? 0 : this.shopFavorites) + (this.articleFavorites == null ? 0 : this.articleFavorites);
	}
}
