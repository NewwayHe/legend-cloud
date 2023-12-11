/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.dto;

import com.legendshop.basic.enums.OpStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 用于批量处理商品状态的类
 *
 * @author legendshop
 */
@Data
@Schema(description = "批量处理商品状态")
public class ProductBranchOpDTO implements Serializable {

	private static final long serialVersionUID = -2568116519680001952L;

	/**
	 * id集合
	 */
	@NotEmpty(message = "处理的id集合不能为空")
	@Schema(description = "商品id集合")
	private List<Long> ids;

	/**
	 * 状态 {@link OpStatusEnum}
	 */
	@NotNull(message = "状态标识不能为空")
	@Schema(description = "商品审核状态, 2、商品违规下线 3、锁定")
	@Max(value = 3, message = "状态标识不匹配")
	@Min(value = 2, message = "状态标识不匹配")
	private Integer status;

	/**
	 * 审核意见
	 */
	@NotNull(message = "审核意见不能为空")
	@Schema(description = "审核意见")
	private String auditOpinion;

	/**
	 * 审核人
	 */
	@Schema(description = "审核人")
	private String auditUsername;
}
