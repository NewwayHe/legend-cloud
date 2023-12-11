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
import com.legendshop.common.core.dto.BaseEntity;
import lombok.Data;

/**
 * @author legendshop
 */
@Data
@Entity
@Table(name = "ls_passport_item")
public class PassportItem extends BaseEntity implements GenericEntity<Long> {
	private static final long serialVersionUID = -4751437020243363406L;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "generator")
	@TableGenerator(name = "generator", pkColumnValue = "PASSPORT_SEQ")
	private Long id;

	@Column(name = "pass_port_id")
	private Long passPortId;

	@Column(name = "third_party_identifier")
	private String thirdPartyIdentifier;

	@Column(name = "source")
	private String source;

}
