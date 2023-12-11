/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 运费设置
 *
 * @author legendshop
 */
@Data
@Schema(description = "运费设置")
public class TransSettingDTO implements Serializable {

	private static final long serialVersionUID = -7551538512228438177L;

	/**
	 * 模板id
	 */
	@Schema(description = "模板id")
	private Long transId;

	/**
	 * 类型父id
	 */
	@Schema(description = "类型父id")
	private Long parentId;

	/**
	 * {@link com.legendshop.product.enums.TransCityTypeEnum}
	 */
	@Schema(description = "配送区域类型 1、区域限售 2、运费计算 3、条件包邮 4、固定运费")
	private Integer type;

	@NotEmpty(message = "省份不能为空")
	@Schema(description = "选中的省份")
	private List<TransProvinceDTO> provinceList;
}
