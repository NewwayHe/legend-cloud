/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.entity;


import cn.legendshop.jpaplus.persistence.*;
import cn.legendshop.jpaplus.support.GenericEntity;
import lombok.Data;

import java.util.Date;

/**
 * 订单发票信息表(OrderInvoice)实体类
 *
 * @author legendshop
 */
@Data
@Entity
@Table(name = "ls_order_invoice")
public class OrderInvoice implements GenericEntity<Long> {

	private static final long serialVersionUID = -24497944605073662L;

	/**
	 * ID
	 */
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "generator")
	@TableGenerator(name = "generator", pkColumnValue = "ORDER_INVOICE_SEQ")
	private Long id;


	/**
	 * 用户id
	 */
	@Column(name = "user_id")
	private Long userId;


	/**
	 * 发票类型，NORMAL:增值税普票 DEDICATED:增值税专票  UserInvoiceTypeEnum
	 */
	@Column(name = "type")
	private String type;


	/**
	 * 普票类型，PERSONAL:个人，COMPANY:单位 UserInvoiceTitleTypeEnum
	 */
	@Column(name = "title_type")
	private String titleType;


	/**
	 * 个人普票：发票抬头信息 公司普票：发票抬头信息 增值税专票：公司名称
	 */
	@Column(name = "company")
	private String company;


	/**
	 * 发票内容 默认2：商品明细
	 */
	@Column(name = "content")
	private Integer content;


	/**
	 * 纳税人号
	 */
	@Column(name = "invoice_hum_number")
	private String invoiceHumNumber;


	/**
	 * 注册地址（增值税发票）
	 */
	@Column(name = "register_addr")
	private String registerAddr;


	/**
	 * 注册电话（增值税发票）
	 */
	@Column(name = "register_phone")
	private String registerPhone;


	/**
	 * 开户银行（增值税发票）
	 */
	@Column(name = "deposit_bank")
	private String depositBank;


	/**
	 * 开户银行账号（增值税发票）
	 */
	@Column(name = "bank_account_num")
	private String bankAccountNum;

	/**
	 * 创建时间
	 */
	@Column(name = "create_time")
	private Date createTime;

}
