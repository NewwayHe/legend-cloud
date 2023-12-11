/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.dto;


import com.legendshop.common.core.annotation.MobileValid;
import com.legendshop.common.core.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 商家用户DTO
 *
 * @author legendshop
 */
@Schema(description = "商家用户DTO")
@Data
public class ShopUserDTO extends BaseDTO implements Serializable {


	private static final long serialVersionUID = 8933512659905938055L;
	/**
	 * 商家id
	 */
	@Schema(description = "商家id")
	private Long id;

	/**
	 * 名称
	 */
	@Schema(description = "名称")
	private String username;


	@Schema(description = "用户类型")
	private String userType;

	/**
	 * 手机号码
	 */
	@Schema(description = "手机号码")
	@MobileValid
	private String mobile;

	/**
	 * 密码
	 */
	@Schema(description = "密码")
	private String password;


	/**
	 * 状态
	 */
	@Schema(description = "状态")
	private Boolean delFlag;


	/**
	 * 注释
	 */
	@Schema(description = "注释")
	private Boolean lockFlag;


	/**
	 * 头像
	 */
	@Schema(description = "头像")
	private String avatar;

	/**
	 * 店铺头像
	 */
	@Schema(description = "店铺头像")
	private String shopAvatar;

	public ShopUserDTO() {
	}

	public ShopUserDTO(String mobile) {
		this.mobile = mobile;
	}
}
