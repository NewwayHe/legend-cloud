/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.dto;


import com.legendshop.common.core.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 商家用户DTO
 *
 * @author legendshop
 */
@Data
@Schema(description = "商家子账号DTO")
public class ShopSubUserDTO extends BaseDTO implements Serializable {


	private static final long serialVersionUID = 8933512659905938055L;

	/**
	 * 主键Id
	 */
	@Schema(description = "主键Id")
	private Long id;

	private Long shopId;

	/**
	 * 商家id
	 */
	@Schema(description = "商家用户id")
	private Long shopUserId;

	/**
	 * 名称
	 */
	@Schema(description = "名称")
	@Size(min = 2, max = 32, message = "请设置用户名长度：2 ~ 32")
	@NotNull(message = "账户用户名不能为空")
	private String
			username;


	/**
	 * 密码
	 */
	@Schema(description = "密码")
	@Size(min = 4, max = 32, message = "请设置密码长度：4 ~ 32")
	private String password;


	/**
	 * 删除状态
	 */
	@Schema(description = "删除状态")
	private Boolean delFlag;


	/**
	 * 锁定状态
	 */
	@Schema(description = "锁定状态")
	private Boolean lockFlag;


	/**
	 * 手机号码
	 */
	@Schema(description = "手机号码")
	private String userAccount;


	/**
	 * 头像
	 */
	@Schema(description = "头像")
	private String avatar;

	/**
	 * 子账号权限列表
	 */
	@Schema(description = "子账号权限列表Id")
	private List<Long> roleIds;

	/**
	 * 子账号权限名称列表
	 */
	@Schema(description = "子账号权限列表名称")
	private List<String> roleNames;

}
