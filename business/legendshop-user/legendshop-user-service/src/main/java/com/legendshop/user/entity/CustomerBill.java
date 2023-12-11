/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.entity;


import cn.legendshop.jpaplus.persistence.*;
import cn.legendshop.jpaplus.support.GenericEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 客户账单(CustomerBill)实体类
 *
 * @author legendshop
 */
@Data
@Entity
@Table(name = "ls_customer_bill")
public class CustomerBill implements GenericEntity<Long> {

	private static final long serialVersionUID = 323056826577441169L;

	/**
	 * id
	 */
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "generator")
	@TableGenerator(name = "generator", pkColumnValue = "CUSTOMER_BILL_SEQ")
	private Long id;


	/**
	 * 账单性质 收入支出 参考ModeTypeEnum
	 */
	@Column(name = "mode")
	private String mode;

	/**
	 * 账单类型 参考CustomerBillTypeEnum
	 */
	@Column(name = "type")
	private String type;

	/**
	 * 账单归属方类型 参考IdentityTypeEnum
	 */
	@Column(name = "owner_type")
	private String ownerType;

	/**
	 * 账单归属方用户ID
	 */
	@Column(name = "owner_id")
	private Long ownerId;

	/**
	 * 交易说明
	 */
	@Column(name = "trade_explain")
	private String tradeExplain;

	/**
	 * 交易金额
	 */
	@Column(name = "amount")
	private BigDecimal amount;

	/**
	 * 支付方式ID
	 */
	@Column(name = "pay_type_id")
	private String payTypeId;

	/**
	 * 支付方式名称
	 */
	@Column(name = "pay_type_name")
	private String payTypeName;

	/**
	 * 业务单号
	 */
	@Column(name = "biz_order_no")
	private String bizOrderNo;

	/**
	 * 内部支付流号
	 */
	@Column(name = "inner_payment_no")
	private String innerPaymentNo;


	/**
	 * 关联业务单号
	 */
	@Column(name = "related_biz_order_no")
	private String relatedBizOrderNo;

	/**
	 * 交易状态
	 */
	@Column(name = "status")
	private Integer status;

	/**
	 * 账单创建时间
	 */
	@Column(name = "create_time")
	private Date createTime;

	/**
	 * 用户删除状态
	 */
	@Column(name = "del_flag")
	private Boolean delFlag;

	/**
	 * 备注 订单支付 定金支付 尾款支付 给前端做拼接展示
	 */
	@Column(name = "remark")
	private String remark;
}
