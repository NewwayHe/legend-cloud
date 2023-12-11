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
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 参数组表(ParamGroup)DTO
 *
 * @author legendshop
 */
@Data
@Schema(description = "参数组DTO")
public class ParamGroupDTO extends BaseDTO implements Serializable {


	private static final long serialVersionUID = -6734824650697007914L;

	/**
	 * 主键
	 */
	@Schema(description = "主键")
	private Long id;

	/**
	 * 店铺ID
	 */
	@Schema(description = "店铺ID")
	private Long shopId;


	/**
	 * 名称
	 */
	@Schema(description = "名称")
	@NotBlank(message = "参数组名称不能为空")
	private String name;


	/**
	 * 副标题
	 */
	@Schema(description = "副标题")
	@NotBlank(message = "参数组副标题不能为空")
	private String memo;


	/**
	 * 规格来源 {@link com.legendshop.product.enums.ProductPropertySourceEnum}
	 */
	@Schema(description = "规格来源:USER商家自定义 SYS平台")
	private String source;


	/**
	 * 参数列表
	 */
	@Schema(description = "参数列表")
	private List<ProductParamDTO> params;

	/**
	 * 参数id列表
	 */
	@Schema(description = "参数id列表")
	private List<Long> paramsIdList;

}
