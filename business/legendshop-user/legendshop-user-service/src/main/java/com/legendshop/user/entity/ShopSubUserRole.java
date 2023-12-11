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

/**
 * 角色表(ls_shop_sub_user_role)实体类
 *
 * @author legendshop
 */
@Data
@Entity
@Table(name = "ls_shop_sub_user_role")
public class ShopSubUserRole implements GenericEntity<Long> {
	/**
	 * 主键
	 */
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "generator")
	@TableGenerator(name = "generator", pkColumnValue = "SHOP_SUB_ROLE_ROLE_SEQ")
	private Long id;

	@Column(name = "shop_sub_user_id")
	private Long shopSubUserId;

	@Column(name = "role_id")
	private Long roleId;


}
