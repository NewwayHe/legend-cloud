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
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 角色DTO
 *
 * @author legendshop
 */
@Data
@Schema(description = "更新商家用户角色")
public class UpdateShopRoleDTO implements Serializable {


	@Schema(description = "商家用户ID")
	private Long userId;

	@Schema(description = "角色ID集合")
	private List<Long> roleIds;

}
