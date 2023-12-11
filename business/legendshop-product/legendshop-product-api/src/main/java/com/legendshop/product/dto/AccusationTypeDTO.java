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
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 举报类型(AccusationType)实体类
 *
 * @author legendshop
 */
@Data
@Schema(description = "商品举报类型DTO")
public class AccusationTypeDTO extends BaseDTO implements Serializable {

	private static final long serialVersionUID = -7399057654280583630L;

	@Schema(description = "主键ID")
	private Long id;

	/**
	 * 名称
	 */
	@Schema(description = "名称")
	private String name;


	/**
	 * 状态:[0下线,1上线]
	 */
	@NotNull(message = "状态不能为空")
	@Schema(description = "状态:[0下线,1上线]")
	private Integer status;

	/**
	 * id集合
	 */
	@Schema(description = "id集合")
	private List<Long> ids;

}
