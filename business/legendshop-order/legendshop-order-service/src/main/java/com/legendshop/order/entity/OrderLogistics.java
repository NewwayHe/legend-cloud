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
 * 订单物流信息
 *
 * @author legendshop
 */
@Data
@Entity
@Table(name = "ls_order_logistics")
public class OrderLogistics implements GenericEntity<Long> {

	/**
	 * 主键id
	 */
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "generator")
	@TableGenerator(name = "generator", pkColumnValue = "ORDER_LOGISTICS_SEQ")
	private Long id;


	/**
	 * 订单ID
	 */
	@Column(name = "order_id")
	private Long orderId;

	/**
	 * 订单号
	 */
	@Column(name = "order_number")
	private String orderNumber;

	/**
	 * 承运物流公司ID
	 */
	@Column(name = "logistics_company_id")
	private Long logisticsCompanyId;

	/**
	 * 承运物流公司
	 */
	@Column(name = "logistics_company")
	private String logisticsCompany;

	/**
	 * 物流公司编号根据快递100查询
	 */
	@Column(name = "company_code")
	private String companyCode;

	/**
	 * 运单号
	 */
	@Column(name = "shipment_number")
	private String shipmentNumber;

	/**
	 * 物流追踪信息【JSON格式】
	 */
	@Column(name = "tracking_information")
	private String trackingInformation;

	/**
	 * 物流状态
	 * QueryTrackStatusEnum
	 */
	@Column(name = "logistics_status")
	private String logisticsStatus;


	/**
	 * 重发次数
	 */
	@Column(name = "re_num")
	private Integer reNum;

	/**
	 * 创建时间
	 */
	@Column(name = "create_time")
	private Date createTime;


	/**
	 * 更新时间
	 */
	@Column(name = "update_time")
	private Date updateTime;
}
