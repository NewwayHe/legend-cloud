/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 商家角色表(ShopRole)实体类
 *
 * @author legendshop
 */
@Data
@Schema(description = "商家子账号角色")
public class ShopSubRoleDTO implements Serializable {


	private static final long serialVersionUID = -6628917457542021486L;

	/**
	 * 主键
	 */
	@Schema(description = "主键Id")
	private Long id;

	/**
	 * 店铺ID
	 */
	@Schema(description = "店铺ID")
	private Long shopId;

	/**
	 * 角色名称
	 */
	@NotNull
	@Schema(description = "角色名称")
	private String roleName;

	@Schema(description = "角色编码")
	private String roleCode;

	@Schema(description = "角色描述")
	private String roleDesc;

	@Schema(description = "角色启用状态")
	private Boolean delFlag;
	@Schema(description = "创建时间")
	private Date createTime;
	@Schema(description = "更新时间")
	private Date updateTime;


	private List<Long> menuIdList;

}
