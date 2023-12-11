/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.entity;


import cn.legendshop.jpaplus.persistence.*;
import cn.legendshop.jpaplus.support.GenericEntity;
import com.legendshop.common.core.dto.BaseEntity;
import lombok.Data;

import java.util.Date;

/**
 * 商家微信联系方式存储表(ShopCustomerInformation)实体类
 *
 * @author legendshop
 * @since 2021-12-28 20:20:22
 */
@Data
@Entity
@Table(name = "ls_contact_information")
public class ContactInformation extends BaseEntity implements GenericEntity<Long> {

	private static final long serialVersionUID = -61435168278541992L;

	/**
	 * id
	 */
	@Column(name = "id")
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "generator")
	@TableGenerator(name = "generator", pkColumnValue = "contactInformation_SEQ")
	private Long id;

	/**
	 * 商家id
	 */
	@Column(name = "shop_id")
	private Long shopId;

	/**
	 * 微信二维码
	 */
	@Column(name = "wv_code")
	private String wvCode;

	/**
	 * 微信号
	 */
	@Column(name = "wx_number")
	private String wxNumber;

	/**
	 * 创建时间
	 */
	@Column(name = "create_time")
	private Date createTime;

	/**
	 * 修改时间
	 */
	@Column(name = "update_time")
	private Date updateTime;

	/**
	 * 客服电话
	 */
	@Column(name = "contact_phone")
	private String contactPhone;

	/**
	 * 客服是否设置（0关闭，1开启）
	 */
	@Column(name = "open_flag")
	private Boolean openFlag;

	/**
	 * 是否是平台客服（0否，1是）
	 */
	@Column(name = "admin_flag")
	private Boolean adminFlag;

}
