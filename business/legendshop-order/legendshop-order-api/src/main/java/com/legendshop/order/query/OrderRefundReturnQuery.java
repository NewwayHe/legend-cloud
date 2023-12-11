/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.query;

import cn.legendshop.jpaplus.support.PageParams;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 退款退货搜索对象
 *
 * @author legendshop
 */
@Schema(description = "退款退货搜索参数")
@Data
public class OrderRefundReturnQuery extends PageParams {
	/**
	 * 订单编号
	 */
	public static final Long ORDER_NO = 1L;
	/**
	 * 退款编号
	 */
	public static final Long REFUND_NO = 2L;
	/**
	 * 商家名称
	 */
	public static final Long SHOP_NAME = 3L;
	/**
	 * 会员名称
	 */
	public static final Long USER_NAME = 4L;

	private static final long serialVersionUID = 7253838348798958887L;


	/**
	 * 用户id
	 */
	private Long userId;

	/**
	 * 店铺id
	 **/
	private Long shopId;

	/**
	 * 店铺名
	 */
	@Schema(description = "店铺名")
	private String shopName;

	/**
	 * 用户名
	 */
	@Schema(description = "用户名")
	private String userName;

	/**
	 * 订单号
	 */
	@Schema(description = "订单号")
	private String orderNumber;

	/**
	 * 售后编号
	 */
	@Schema(description = "售后编号")
	private String refundSn;

	/**
	 * 商品名称
	 */
	@Schema(description = "商品名称")
	private String productName;

	/**
	 * 状态
	 */
	private String status;

	/**
	 * 申请类型 1:退款,2:退款且退货
	 */
	@Schema(description = "申请类型 1:退款,2:退款且退货")
	private Integer applyType;

	/**
	 * 开始时间
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	@Schema(description = "开始时间")
	private Date startTime;

	/**
	 * 结束时间
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	@Schema(description = "结束时间")
	private Date endTime;

	/**
	 * 卖家处理状态
	 */
	private Integer sellerState;

	/**
	 * 编号类型 1:订单编号,2:退款编号,3:会员名称
	 */
	private Integer numType;

	/**
	 * 编号
	 */
	private String number;

	/**
	 * 申请状态
	 */
	private Integer applyState;

	/**
	 * 订单结算编号
	 */

	private String billSn;

	/***
	 * 自定义组合状态义状态  用于退款退货操作状态查询
	 * sellerState  卖家处理状态   待卖家处理0  卖家同意1 卖家不同意-1
	 * applyState   申请状态 	    待卖家处理1   待管理员处理2  已完成3  -1用户取消
	 * goodsState  	物流状态      待发货1     待签收3   待收货5  已收货7
	 * returnType   退货类型
	 * 规则: customStatus = 1, 即对应  sellerStatus = 1;applyStatus= null;
	 * 	  	customStatus = 2, 即对应  sellerStatus = 2;applyStatus= 2;
	 * 		...
	 *
	 * 		    sellerStatus    goodsStatus   applyStatus
	 * (1)待商家确认   0                          1
	 * (2)已拒绝     						    -3
	 * (3)待平台审核                              2
	 * (4)待买家退货	1	     		1
	 * (5)待收货		1		    	3,5
	 * (6)已完成                                 3
	 * (7)已取消                                 -1/-2
	 * **/
	@Schema(description = "自定义组合状态义状态 (1)待商家确认 (2)已拒绝 (3)待平台审核 (4)待买家退货 (5)待收货  (6)已完成 (7)已取消  ")
	private Integer customStatus;

	@Schema(description = "订单退款状态：0:默认,1:在处理,2:处理完成")
	private Integer orderRefundStatus;

	@Schema(description = "用户昵称")
	private String nickName;

	@Schema(description = "售后原因")
	private String reason;

	/**
	 * 收货人电话
	 */
	@Schema(description = "收货人电话")
	private String receiverMobile;

	/**
	 * 收货人名称
	 */
	@Schema(description = "收货人名称")
	private String receiverName;

	@Schema(description = "是否会员")
	private Boolean isMember;
}
