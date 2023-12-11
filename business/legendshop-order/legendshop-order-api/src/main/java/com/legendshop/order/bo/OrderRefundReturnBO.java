/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.bo;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.legendshop.order.dto.OrderAftersalesInformationDTO;
import com.legendshop.order.dto.OrderItemDTO;
import com.legendshop.order.dto.OrderPayTypeDTO;
import com.legendshop.order.dto.RefundReturnLogisticsDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * 退款列表显示BO
 *
 * @author legendshop
 */
@Data
@Schema(description = "退款列表显示BO")
public class OrderRefundReturnBO implements Serializable {

	private static final long serialVersionUID = 4648808871766795299L;
	/**
	 * 主键id
	 */
	@Schema(description = "主键id")
	private Long id;


	/**
	 * 买家ID
	 */
	@Schema(description = "买家ID")
	private Long userId;


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
	 * 订购ID
	 */
	@Schema(description = "订购ID")
	private Long orderId;

	/**
	 * 订单号
	 */
	@Schema(description = "订单号")
	private String orderNumber;


	/**
	 * 订单商品ID,全部退款是0
	 */
	@Schema(description = "订单商品ID,全部退款是0")
	private Long productId;


	/**
	 * 商品名称
	 */
	@Schema(description = "商品名称")
	private String productName;


	/**
	 * 订单SKU ID,全部退款是0
	 */
	@Schema(description = "订单SKU ID,全部退款是0")
	private Long skuId;


	/**
	 * 退货数量
	 */
	@Schema(description = "退货数量")
	private Integer goodsNum;


	/**
	 * 订单总金额
	 */
	@Schema(description = "订单总金额")
	private BigDecimal orderMoney;


	/**
	 * 订单项ID
	 */
	@Schema(description = "订单项ID")
	private Long orderItemId;


	/**
	 * 退款编号
	 */
	@Schema(description = "退款编号")
	private String refundSn;

	@Schema(description = "商品快照id")
	private Long snapshotId;

	@Schema(description = "收件人姓名")
	private String receiver;

	@Schema(description = "收件人手机号")
	private String mobile;

	@Schema(description = "用户名")
	private String nickName;

	@Schema(description = "用户手机号")
	private String userMobile;

	/**
	 * 流水号(订单支付流水号)
	 */
	@Schema(description = "流水号(订单支付流水号)")
	private String paySerialNumber;


	/**
	 * ls_order_settlement 清算单据号
	 */
	@Schema(description = "清算单据号")
	private String settlementNumber;


	/**
	 * ls_order_settlement 创建时间
	 */
	@Schema(description = "清算时间")
	private Date settlementTime;


	/**
	 * 第三方退款单号(微信退款单号)
	 */
	@Schema(description = "第三方退款单号(微信退款单号)")
	private String outRefundNo;


	/**
	 * 是否结算[用于结算档期统计]
	 */
	@Schema(description = "是否结算[用于结算档期统计]")
	private Boolean billFlag;


	/**
	 * 订单结算编号[用于结算档期统计]
	 */
	@Schema(description = "订单结算编号[用于结算档期统计]")
	private String billSn;


	/**
	 * 订单支付Id
	 */
	@Schema(description = "订单支付Id")
	private String payTypeId;


	/**
	 * 订单支付名称
	 */
	@Schema(description = "订单支付名称")
	private String payTypeName;


	/**
	 * 退款金额
	 */
	@Schema(description = "退款金额")
	private BigDecimal refundAmount;


	/**
	 * 商品图片
	 */
	@Schema(description = "商品图片")
	private String productImage;


	/**
	 * 申请类型:1,仅退款,2退款退货,-1已撤销
	 */
	@Schema(description = "申请类型:1,仅退款,2退款退货,-1已撤销")
	private Integer applyType;


	@Schema(description = "卖家处理状态:0为待审核,1为同意,-1为不同意")
	private Integer sellerStatus;


	/**
	 * 申请状态:1待卖家处理 ,2为待管理员处理,3为已完成 -1已取消  -2用户超时未发货，系统取消售后 -1用户取消售后
	 */
	@Schema(description = "申请状态:1待卖家处理 ,2为待管理员处理,3为已完成 -1已取消 -2用户超时未发货，系统取消售后 -1用户取消售后,-4平台拒绝")
	private Integer applyStatus;


	/**
	 * 退货类型:1为不用退货,2为需要退货,默认为0
	 */
	@Schema(description = "退货类型:1为不用退货,2为需要退货,默认为0")
	private Integer returnType;


	/**
	 * 物流状态:1为待发货,3为待签收,5为待收货,7为已收货,默认为0
	 */
	@Schema(description = "1为待发货,3为待签收,5为待收货,7为已收货,默认为0")
	private Integer goodsStatus;


	/**
	 * 处理退款状态: 0:退款处理中 1:退款成功 -1:退款失败
	 */
	@Schema(description = "处理退款状态: 0:退款处理中 1:退款成功 -1:退款失败")
	private Integer handleSuccessStatus;

	/**
	 * 订单类型  {@link com.legendshop.order.enums.OrderTypeEnum}
	 */
	@Schema(description = "订单类型 O:普通订单  P:预售订单  G：团购订单  S：秒杀订单  MG：拼团订单")
	private String orderType;


	/**
	 * 处理退款的方式
	 */
	@Schema(description = "处理退款的方式")
	private String handleType;

	/**
	 * 下单时间
	 */
	@Schema(description = "下单时间")
	private Date payTime;

	/**
	 * 申请时间
	 */
	@Schema(description = "申请时间")
	private Date createTime;


	/**
	 * 卖家处理时间
	 */
	@Schema(description = "卖家处理时间")
	private Date sellerTime;


	/**
	 * 管理员处理时间 [最后审核的处理时间--用于统计退货金额]
	 */
	@Schema(description = "管理员处理时间")
	private Date adminTime;


	/**
	 * 退款原因
	 */
	@Schema(description = "退款原因")
	private String reason;


	/**
	 * 退货照片凭证
	 */
	@Schema(description = "退货照片凭证")
	private String photoVoucher;


	/**
	 * 退款说明
	 */
	@Schema(description = "退款说明")
	private String buyerMessage;


	/**
	 * 卖家备注
	 */
	@Schema(description = "卖家备注")
	private String sellerMessage;


	/**
	 * 管理员备注
	 */
	@Schema(description = "管理员备注")
	private String adminMessage;
	/**
	 * 物流公司id
	 */
	@Schema(description = "物流公司id")
	private Long logisticsId;
	/**
	 * 物流公司名称
	 */
	@Schema(description = "物流公司名称")
	private String logisticsCompanyName;

	/**
	 * 物流公司code
	 */
	@Schema(description = "物流公司code")
	private String logisticsCompanyCode;


	/**
	 * 物流单号
	 */
	@Schema(description = "物流单号")
	private String logisticsNumber;


	/**
	 * 发货时间
	 */
	@Schema(description = "发货时间")
	private Date shipTime;


	/**
	 * 收货时间
	 */
	@Schema(description = "收货时间")
	private Date receiveTime;


	/**
	 * 第三方退款金额
	 */
	@Schema(description = "第三方退款金额")
	private BigDecimal thirdPartyRefund;

	@Schema(description = "判断是否是预售商品退款")
	private Boolean preSellFlag;

	/**
	 * 平台退款金额(预存款)
	 */
	@Schema(description = "平台退款金额(预存款)")
	private BigDecimal platformRefund;

	/**
	 * 退款单创建来源  0 用户 1 商家 2 平台
	 */
	@Schema(description = "退款单创建来源  0 用户 1 商家 2 平台")
	private Integer refundSource;


	/**
	 * 订单商品数量
	 */
	@Schema(description = "订单商品数量")
	private Integer productNum;


	/**
	 * 订单商品规格属性
	 */
	@Schema(description = "订单商品规格属性")
	private String productAttribute;


	/**
	 * 订单商品金额
	 */
	@Schema(description = "订单商品金额")
	private BigDecimal orderItemMoney;

	/**
	 * 订单项退款状态
	 */
	@Schema(description = "订单项退款状态")
	private Integer itemRefundStatus;

	/**
	 * 订单状态
	 */
	@Schema(description = "订单状态")
	private Integer orderStatus;

	/**
	 * 订单完成时间
	 */
	@Schema(description = "订单完成时间")
	private Date completeTime;

	@Schema(description = "订单项集合")
	List<OrderItemDTO> orderItemDTOList;


	@Schema(description = "物流信息")
	private RefundReturnLogisticsDTO refundReturnLogisticsDTO;


	@Schema(description = "退的积分")
	private Integer integral;

	@Schema(description = "总共抵扣金额")
	private BigDecimal totalDeductionAmount;

	@Schema(description = "使用积分")
	private Integer totalIntegral;

	@Schema(description = "是否可以重新申请")
	private Boolean canReapply;


	/**
	 * 退货收货人
	 */
	@Schema(description = "退货收货人")
	private String returnConsignee;
	/**
	 * 退货收货人电话号码
	 */
	@Schema(description = "退货收货人电话号码")
	private String returnConsigneePhone;


	/**
	 * 退货省份Id.
	 */
	@Schema(description = "退货省份Id")
	private Long returnProvinceId;

	/**
	 * 退货城市Id.
	 */
	@Schema(description = "退货城市Id")
	private Long returnCityId;

	/**
	 * 退货地区Id.
	 */
	@Schema(description = "退货地区Id")
	private Long returnAreaId;


	/**
	 * 退货街道Id.
	 */
	@Schema(description = "退货街道Id")
	private Long returnStreetId;

	/**
	 * 退货详细地址2.
	 */
	@Schema(description = "退货详细地址2")
	private String returnShopAddr;

	/**
	 * 退货详细地址1.
	 */
	@Schema(description = "退货详细地址1")
	private String returnShopAddress;

	@Schema(description = "运费")
	private BigDecimal freightPrice;

	@Schema(description = "退款明细")
	private List<OrderPayTypeDTO> refundAmountDetailList;

	@Schema(description = "活动id")
	private Long activeId;

	@Schema(description = "售后描述")
	private String description;

	@Schema(description = "商品倒计时")
	private Long autoCancelRefundTime;

	@Schema(description = "取消时间")
	private Date cancellationTime;

	@Schema(description = "取消创建来源  1用户取消售后 2逾期取消 3待用户寄回 4 系统自动处理 5 商家已确认收货 6商家弃货")
	private Integer cancellationType;

	@Schema(description = "售后信息")
	private OrderAftersalesInformationDTO orderAftersalesInformationDTO;

	@Schema(description = "配送方式  0: 商家配送  ")
	private Integer deliveryType;


	@Schema(description = "用户订单地址ID")
	private Long addressOrderId;

	public List<String> getPhotoVoucher() {
		if (ObjectUtil.isNotEmpty(photoVoucher)) {
			return JSONUtil.toList(JSONUtil.parseArray(photoVoucher), String.class);
		}
		return Collections.emptyList();
	}
}
