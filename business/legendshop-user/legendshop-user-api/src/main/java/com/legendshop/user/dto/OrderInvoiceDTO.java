/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * 发票订单dto
 *
 * @author legendshop
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderInvoiceDTO {

	private static final long serialVersionUID = -8432541515212546651L;

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public BigDecimal getActualTotalPrice() {
		return actualTotalPrice;
	}

	public void setActualTotalPrice(BigDecimal actualTotalPrice) {
		this.actualTotalPrice = actualTotalPrice;
	}

	public Integer getProductQuantity() {
		return productQuantity;
	}

	public void setProductQuantity(Integer productQuantity) {
		this.productQuantity = productQuantity;
	}

	public List<OrderItemInvoiceDTO> getOrderItemList() {
		return orderItemList;
	}

	public void setOrderItemList(List<OrderItemInvoiceDTO> orderItemList) {
		this.orderItemList = orderItemList;
	}

	/**
	 * 店铺名称
	 */
	private String shopName;

	/**
	 * 总价格
	 */
	private BigDecimal actualTotalPrice;

	/**
	 * 总数量
	 */
	private Integer productQuantity;

	/**
	 * 订单项商品信息
	 */
	List<OrderItemInvoiceDTO> orderItemList;
}
