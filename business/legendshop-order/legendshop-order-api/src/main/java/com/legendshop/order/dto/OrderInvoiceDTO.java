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
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 订单发票DTO
 *
 * @author legendshop
 */
@Data
@Schema(description = "订单发票信息")
public class OrderInvoiceDTO implements Serializable {

	private static final long serialVersionUID = 8576431265328009785L;


	@Schema(description = "主键ID")
	private Long id;


	@Schema(description = "用户id")
	private Long userId;

	@Schema(description = "用户名")
	private String userName;


	@Schema(description = "订单号")
	private String orderNumber;

	@Schema(description = "订单商品总数")
	private Integer productQuantity;

	@Schema(description = "商品总价格")
	private BigDecimal totalPrice;

	@Schema(description = "订单金额")
	private BigDecimal actualTotalPrice;

	@Schema(description = "发票类型，NORMAL:增值税普票 DEDICATED:增值税专票  UserInvoiceTypeEnum")
	private String type;


	@Schema(description = "普票类型，PERSONAL:个人，COMPANY:单位 UserInvoiceTitleTypeEnum")
	private String titleType;


	@Schema(description = "个人普票：发票抬头信息 公司普票：发票抬头信息 增值税专票：公司名称")
	private String company;


	@Schema(description = "发票内容 默认2：商品明细")
	private Integer content;


	@Schema(description = "纳税人号")
	private String invoiceHumNumber;


	@Schema(description = "注册地址（增值税发票）")
	private String registerAddr;


	@Schema(description = "注册电话（增值税发票）")
	private String registerPhone;


	@Schema(description = "开户银行（增值税发票）")
	private String depositBank;


	@Schema(description = "开户银行账号（增值税发票）")
	private String bankAccountNum;


	@Schema(description = "创建时间", hidden = true)
	private Date createTime;

	@Schema(description = "是否已开发票")
	private Boolean hasInvoiceFlag;

	@Schema(description = "商品主图路径")
	private String productPic;

	@Schema(description = "订单商品url")
	private List<String> orderProductPics;

	@Schema(description = "订单商品数量")
	private Integer orderProductNum;

	@Schema(description = "订单价格")
	private BigDecimal orderPrice;

}
