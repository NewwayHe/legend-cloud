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

import java.util.Date;

/**
 * @author legendshop
 */
@Data
@Entity
@Table(name = "ls_shop_sub_user")
public class ShopSubUser extends BaseEntity implements GenericEntity<Long> {

	private static final long serialVersionUID = -2274252742558007570L;
	/**
	 * 子账号Id
	 */
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "generator")
	@TableGenerator(name = "generator", pkColumnValue = "SHOP_SUB_USER_SEQ")
	private Long id;

	/**
	 * 名称
	 */
	@Column(name = "shop_user_id")
	private String shopUserId;

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
	 * 删除状态
	 */
	@Column(name = "del_flag")
	private Boolean delFlag;


	/**
	 * 锁定状态
	 */
	@Column(name = "lock_flag")
	private Boolean lockFlag;


	/**
	 * 手机号码
	 */
	@Column(name = "user_account")
	private String userAccount;


	/**
	 * 头像
	 */
	@Column(name = "avatar")
	private String avatar;

	/**
	 * 更新时间
	 */
	@Column(name = "update_time")
	private Date updateTime;

	/**
	 * 创建时间
	 */
	@Column(name = "create_time")
	private Date createTime;

}
