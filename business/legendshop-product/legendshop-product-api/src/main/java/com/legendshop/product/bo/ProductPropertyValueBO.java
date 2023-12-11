/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.bo;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * 属性值(ProdPropValue)实体类
 *
 * @author legendshop
 */
@Data
@Schema(description = "属性值BO")
public class ProductPropertyValueBO implements Serializable {


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
	private String name;

	/**
	 * 删除标识【true：已删除  false：正常使用】
	 */
	@Schema(description = "删除标识【true：已删除  false：正常使用】")
	private Boolean deleteFlag;

	/**
	 * 图片路径
	 */
	@Schema(description = "图片路径")
	private String pic;

	/**
	 * sku组图数组
	 */
	@Schema(description = "sku组图数组")
	private List<String> imgList;

	/**
	 * 排序
	 */
	@Schema(description = "排序")
	private Integer sequence;


	/**
	 * 属性值别名
	 */
	@Schema(description = "属性值别名")
	private String alias;


	/**
	 * 属性值图片第一张
	 */
	@Schema(description = "属性值图片第一张")
	private String url;

	/**
	 * 是否被sku选中
	 */
	@Schema(description = "是否被sku选中")
	private Boolean selectFlag = false;

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof ProductPropertyValueBO)) {
			return false;
		}
		ProductPropertyValueBO that = (ProductPropertyValueBO) o;
		return getId().equals(that.getId()) &&
				getName().equals(that.getName());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getId(), getName());
	}

}
