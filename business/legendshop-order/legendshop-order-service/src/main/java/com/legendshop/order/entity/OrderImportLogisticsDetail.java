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
 * (OrderImportLogisticsDetail)实体类
 *
 * @author legendshop
 * @since 2022-04-25 14:08:30
 */
@Data
@Entity
@Table(name = "ls_order_import_logistics_detail")
public class OrderImportLogisticsDetail implements GenericEntity<Long> {

	private static final long serialVersionUID = 187397317479739095L;


	@Column(name = "id")
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "generator")
	@TableGenerator(name = "generator", pkColumnValue = "orderImportLogisticsDetail_SEQ")
	private Long id;

	/**
	 * 导入信息Id
	 */
	@Column(name = "import_id")
	private Integer importId;

	/**
	 * 收货人
	 */
	@Column(name = "nike_name")
	private String nikeName;


	/**
	 * 收货人手机号
	 */
	@Column(name = "mobile")
	private String mobile;

	/**
	 * 订单Id
	 */
	@Column(name = "order_id")
	private Integer orderId;

	/**
	 * 订单号
	 */
	@Column(name = "number")
	private String number;

	/**
	 * 物流公司Id
	 */
	@Column(name = "logistics_company_id")
	private Integer logisticsCompanyId;

	/**
	 * 物流公司名称
	 */
	@Column(name = "logistics_company")
	private String logisticsCompany;

	/**
	 * 物流公司编码
	 */
	@Column(name = "company_code")
	private String companyCode;

	/**
	 * 物流单号
	 */
	@Column(name = "logistics_number")
	private String logisticsNumber;

	/**
	 * 推送结果
	 */
	@Column(name = "result")
	private Boolean result;

	/**
	 * 错误描述
	 */
	@Column(name = "fail_reason")
	private String failReason;

	/**
	 * 创建时间
	 */
	@Column(name = "create_time")
	private Date createTime;

}
