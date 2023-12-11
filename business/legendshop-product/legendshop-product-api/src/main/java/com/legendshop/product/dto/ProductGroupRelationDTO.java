/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.dto;


import com.legendshop.common.core.dto.BaseDTO;
import com.legendshop.common.core.validator.group.Save;
import com.legendshop.common.core.validator.group.Update;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

/**
 * (ProdGroupRelevance)DTO
 *
 * @author legendshop
 */
@Schema(description = "商品分组关联DTO")
@Data
public class ProductGroupRelationDTO extends BaseDTO implements Serializable {


	private static final long serialVersionUID = -4686788700949054727L;
	/**
	 * 主键ID
	 */
	@Schema(description = "主键ID")
	private Long id;


	/**
	 * 商品分组ID
	 */
	@Schema(description = "商品分组ID")
	@NotNull(message = "分组id不能为空", groups = Update.class)
	private Long groupId;


	/**
	 * 商品ID
	 */
	@Schema(description = "商品ID")
	private Long productId;

	@Schema(description = "商品ID集合")
	@NotBlank(message = "商品id集合不能为空")
	private String productIds;

	@Schema(description = "分组ID集合")
	@NotBlank(message = "分组id集合不能为空", groups = Save.class)
	private String groupIds;

}
