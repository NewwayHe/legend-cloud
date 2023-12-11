/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.bo;

import com.legendshop.user.dto.ShopRoleDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 后台-商家用户基本资料
 *
 * @author legendshop
 */
@Data
@Schema(description = "商家用户基本资料")
public class ShopUserDetailBO implements Serializable {


	@Schema(description = "用户名")
	private String username;

	@Schema(description = "头像")
	private String avatar;

	@Schema(description = "注册手机号码")
	private String mobile;

	@Schema(description = "注册时间")
	private Date createTime;

	@Schema(description = "商家用户角色")
	private List<ShopRoleDTO> shopRoleDTOList;

	@Schema(description = "店铺ID")
	private Long shopId;

	/**
	 * 可用积分
	 */
	@Schema(description = "可用积分")
	private BigDecimal availableIntegral;

	/**
	 * 累计积分
	 */
	@Schema(description = "累计积分")
	private BigDecimal cumulativeIntegral;
}
