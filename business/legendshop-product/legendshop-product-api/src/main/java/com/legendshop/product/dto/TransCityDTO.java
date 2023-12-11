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
import lombok.Data;

import java.io.Serializable;

/**
 * 每个城市的运费设置(TransCity)DTO
 *
 * @author legendshop
 * @since 2020-09-04 17:14:16
 */
@Data
@Schema(description = "每个城市的运费设置(TransCity)DTO")
public class TransCityDTO extends BaseDTO implements Serializable {

	private static final long serialVersionUID = 794211128988362558L;

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
	 * 城市id
	 */
	@Schema(description = "城市id")
	private Long cityId;

	/**
	 * {@link com.legendshop.product.enums.TransCityTypeEnum}
	 */
	@Schema(description = "配送区域类型 1、区域限售 2、运费计算 3、条件包邮 4、固定运费")
	private Integer type;

	/**
	 * 省份编码
	 */
	@Schema(description = "省份id")
	private Long provinceId;

	@Schema(description = "选中状态")
	private Boolean selectFlag;
}
