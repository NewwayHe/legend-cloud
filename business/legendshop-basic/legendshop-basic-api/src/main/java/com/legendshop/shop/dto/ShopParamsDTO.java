/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.dto;

import com.legendshop.common.core.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 商家主配置(ShopParams)DTO
 *
 * @author legendshop
 * @since 2020-11-03 10:59:34
 */
@Data
@Schema(description = "商家主配置DTO")
public class ShopParamsDTO extends BaseDTO implements Serializable {

	private static final long serialVersionUID = 163031908257218134L;

	@Schema(description = "id")
	private Long id;

	/**
	 * 商家id
	 */
	@Schema(description = "商家id")
	private Long shopId;

	/**
	 * 名称ShopParamNameEnum
	 */
	@Schema(description = "名称ShopParamNameEnum")
	private String name;

	/**
	 * 描述
	 */
	@Schema(description = "描述")
	private String des;

	/**
	 * 类型ShopParamTypeEnum
	 */
	@Schema(description = "类型ShopParamTypeEnum")
	private String type;

	/**
	 * 分组ShopParamGroupEnum
	 */
	@Schema(description = "分组ShopParamGroupEnum")
	private String groupBy;

	/**
	 * 排序
	 */
	@Schema(description = "排序")
	private Integer sort;

	/**
	 * 创建时间
	 */
	@Schema(description = "创建时间")
	private Date updateTime;

	/**
	 * 备注
	 */
	@Schema(description = "备注")
	private String remark;

}
