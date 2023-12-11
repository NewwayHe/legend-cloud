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
 * 商家用户表(ShopUser)实体类
 *
 * @author legendshop
 */
@Data
@Entity
@Table(name = "ls_shop_user")
public class ShopUser extends BaseEntity implements GenericEntity<Long> {

	private static final long serialVersionUID = -51623750880725838L;

	/**
	 * 商家id
	 */
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "generator")
	@TableGenerator(name = "generator", pkColumnValue = "SHOP_USER_SEQ")
	private Long id;


	/**
	 * 商家id
	 */
	@Transient
	private Long shopId;

	/**
	 * 名称
	 */
	@Column(name = "username")
	private String username;


	/**
	 * 密码
	 */
	@Column(name = "password")
	private String password;


	/**
	 * 状态
	 */
	@Column(name = "del_flag")
	private Boolean delFlag;


	/**
	 * 注释
	 */
	@Column(name = "lock_flag")
	private Boolean lockFlag;


	/**
	 * 手机号码
	 */
	@Column(name = "mobile")
	private String mobile;


	/**
	 * 头像
	 */
	@Column(name = "avatar")
	private String avatar;


}
