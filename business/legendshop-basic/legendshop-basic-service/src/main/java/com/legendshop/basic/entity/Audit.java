/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.entity;


import cn.legendshop.jpaplus.persistence.*;
import cn.legendshop.jpaplus.support.GenericEntity;
import com.legendshop.basic.enums.AuditTypeEnum;
import lombok.Data;

import java.util.Date;

/**
 * 审核表(ShopDetailAudit)实体类
 *
 * @author legendshop
 */
@Data
@Entity
@Table(name = "ls_audit")
public class Audit implements GenericEntity<Long> {

	private static final long serialVersionUID = -70125282915631934L;

	/**
	 * 店铺ID
	 */
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "generator")
	@TableGenerator(name = "generator", pkColumnValue = "SHOP_AUDIT_SEQ")
	private Long id;


	/**
	 * 通用ID(审核类型为商品，就为商品id)
	 */
	@Column(name = "common_id")
	private Long commonId;


	/**
	 * 审核操作状态
	 * {@link com.legendshop.basic.enums.OpStatusEnum}
	 */
	@Column(name = "op_status")
	private Integer opStatus;


	/**
	 * 审核类型
	 * {@link AuditTypeEnum}
	 */
	@Column(name = "audit_type")
	private String auditType;

	/**
	 * 审核意见
	 */
	@Column(name = "audit_opinion")
	private String auditOpinion;


	/**
	 * 审核意见
	 */
	@Column(name = "audit_username")
	private String auditUsername;

	/**
	 * 审核时间
	 */
	@Column(name = "audit_time")
	private Date auditTime;


}
