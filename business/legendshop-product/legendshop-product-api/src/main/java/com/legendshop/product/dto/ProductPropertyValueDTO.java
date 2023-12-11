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
 * 属性值(ProdPropValue)实体类
 *
 * @author legendshop
 */
@Schema(description = "属性值DTO")
@Data
public class ProductPropertyValueDTO extends BaseDTO implements Serializable {


	private static final long serialVersionUID = -5033953286968421107L;
	/**
	 * 属性值ID
	 */
	@Schema(description = "属性值ID")
	private Long id;


	/**
	 * 属性ID
	 */
	@Schema(description = "属性ID")
	private Long propId;

	/**
	 * 店铺id
	 */
	@Schema(description = "店铺id")
	private Long shopId;

	/**
	 * 属性值名称
	 */
	@Schema(description = "属性值名称")
	@NotBlank(message = "属性值名称不能为空")
	private String name;

	/**
	 * 删除标识【true：已删除  false：正常使用】
	 */
	@Schema(description = " 删除标识【true：已删除  false：正常使用】")
	private Boolean deleteFlag;

	/**
	 * 图片路径
	 */
	@Schema(description = "图片路径")
	private String pic;

	/**
	 * 图片列表
	 */
	@Schema(description = "图片列表")
	private List<String> imageList;


	/**
	 * 排序
	 */
	@Schema(description = "排序")
	private Integer sequence;


	/**
	 * 是否被sku选中
	 */
	@Schema(description = "是否被sku选中")
	private Boolean selectFlag;


	/**
	 * 商品组图
	 */
	@Schema(description = "商品组图")
	private List<String> imgList;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (obj == null) {
			return false;
		}

		if (getClass() != obj.getClass()) {
			return false;
		}

		ProductPropertyValueDTO other = (ProductPropertyValueDTO) obj;
		if (id == null) {
			if (other.id != null) {
				return false;
			}
			return false;
		} else if (!id.equals(other.id)) {
			return false;
		}

		return true;
	}

}
