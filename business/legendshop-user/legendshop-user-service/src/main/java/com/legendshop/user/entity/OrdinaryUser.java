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
 * 用户表(User)实体类
 *
 * @author legendshop
 */
@Data
@Entity
@Table(name = "ls_ordinary_user")
public class OrdinaryUser extends BaseEntity implements GenericEntity<Long> {

	private static final long serialVersionUID = 672473978338963121L;

	/**
	 * 用户ID
	 */
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "generator")
	@TableGenerator(name = "generator", pkColumnValue = "USER_SEQ")
	private Long id;


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
	 * 删除标记,1:正常,0:已删除
	 */
	@Column(name = "del_flag")
	private Boolean delFlag;


	/**
	 * 锁定状态,1:正常,0:已锁定
	 */
	@Column(name = "lock_flag")
	private Boolean lockFlag;


	/**
	 * 用户昵称
	 */
	@Column(name = "nick_name")
	private String nickName;

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
