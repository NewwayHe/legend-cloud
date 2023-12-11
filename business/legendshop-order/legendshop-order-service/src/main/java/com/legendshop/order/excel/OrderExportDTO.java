/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.NumberFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单导出DTO
 *
 * @author legendshop
 */
@Data
@ContentRowHeight(20)
@HeadRowHeight(20)
public class OrderExportDTO {

	@ExcelProperty("商品")
	@ColumnWidth(50)
	private String productName;

	@ExcelProperty("单价")
	@ColumnWidth(50)
	@NumberFormat("#.##")
	private BigDecimal totalPrice;

	@ExcelProperty("数量")
	@ColumnWidth(50)
	private Integer productQuantity;

	/**
	 * {@link com.legendshop.order.enums.OrderTypeEnum}
	 */
	@ExcelProperty("订单类型")
	@ColumnWidth(50)
	private String orderType;

	@ExcelProperty("买家昵称")
	@ColumnWidth(50)
	private String nickName;

	@ExcelProperty("收货人")
	@ColumnWidth(50)
	private String receiver;


	@ExcelProperty("收货人手机号码")
	@ColumnWidth(50)
	private String receiverMobile;


	@ExcelProperty("实付款")
	@ColumnWidth(50)
	@NumberFormat("#.##")
	private BigDecimal actualTotalPrice;

	@ExcelProperty("运费")
	@ColumnWidth(50)
	@NumberFormat("#.##")
	private BigDecimal freightPrice;

	/**
	 * {@link com.legendshop.order.enums.OrderStatusEnum}
	 */
	@ExcelProperty("交易状态")
	@ColumnWidth(50)
	private String status;


	@ExcelProperty("下单时间")
	@ColumnWidth(50)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;


	@ExcelProperty("订单号")
	@ColumnWidth(50)
	private String orderNumber;

	@ExcelProperty("店铺名称")
	@ColumnWidth(50)
	private String shopName;

	@ExcelProperty("促销活动优惠总金额")
	@ColumnWidth(50)
	@NumberFormat("#.##")
	private BigDecimal discountTotalAmount;


	@ExcelProperty("商家优惠券金额")
	@ColumnWidth(50)
	@NumberFormat("#.##")
	private BigDecimal couponAmount;


	@ExcelProperty("平台优惠券金额")
	@ColumnWidth(50)
	@NumberFormat("#.##")
	private BigDecimal platformCouponAmount;


	@ExcelProperty("支付状态")
	@ColumnWidth(50)
	private String payedFlag;


	@ExcelProperty("支付类型名称")
	@ColumnWidth(50)
	private String payTypeName;


	@ExcelProperty("支付流水号")
	@ColumnWidth(50)
	private String paySettlementSn;


	@ExcelProperty("支付时间")
	@ColumnWidth(50)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date payTime;


	@ExcelProperty("收货地址")
	@ColumnWidth(50)
	private String receiverAddress;


	@ExcelProperty("买家留言")
	@ColumnWidth(50)
	private String message;


	@ExcelProperty("买家手机号码")
	@ColumnWidth(50)
	private String userMobile;


//	@ExcelProperty("商品属性")
//	@ColumnWidth(50)
//	private String attribute;
//
//
//	@ExcelProperty("商品价格")
//	@ColumnWidth(50)
//	@NumberFormat("#.##")
//	private BigDecimal price;


//	/**
//	 * {@link com.legendshop.order.enums.OrderRefundReturnStatusEnum}
//	 */
//	@ExcelProperty("退款状态")
//	@ColumnWidth(50)
//	private String refundStatus;

}
