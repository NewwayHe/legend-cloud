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

/**
 * 属性名(ProdProp)实体类
 *
 * @author legendshop
 */
@Data
@Schema(description = "属性名BO")
public class ProductPropertyBO implements Serializable {


	/**
	 * 属性ID
	 */
	@Schema(description = "属性ID")
	private Long id;

	/**
	 * 参数组ID
	 */
	@Schema(description = "参数组ID")
	private Long groupId;

	/**
	 * 参数组名称
	 */
	@Schema(description = "参数组名称")
	private String groupName;

	/**
	 * 类目关联管理ID
	 */
	@Schema(description = "类目关联管理ID")
	private Long aggId;

	/**
	 * 类目关联管理-商品属性关联表主键ID
	 */
	@Schema(description = "类目关联管理-商品属性关联表主键ID")
	private Long aggPropId;

	/**
	 * 类目关联管理名称
	 */
	@Schema(description = "类目关联管理名称")
	private String aggPropName;


	/**
	 * 商品ID
	 */
	@Schema(description = "商品ID")
	private Long productId;

	/**
	 * 店铺id
	 */
	@Schema(description = "店铺id")
	private Long shopId;

	/**
	 * 属性名称
	 */
	@Schema(description = "属性名称")
	private String propName;


	/**
	 * 副标题
	 */
	@Schema(description = "副标题")
	private String memo;

	/**
	 * 属性类型  TXT：文本类型; PIC:图片类型;
	 * {@link com.legendshop.product.enums.ProductPropertyTypeEnum}
	 */
	@Schema(description = " 属性类型： TXT：文本类型; PIC:图片类型")
	private String type;

	/**
	 * 商品属性： 'P':参数属性; 'S':规格属性;
	 * {@link com.legendshop.product.enums.ProductPropertyAttributeTypeEnum}
	 */
	@Schema(description = "商品属性：'P':参数属性; 'S':规格属性;")
	private String attributeType;


	/**
	 * 规格来源 "USER":用户自定义，"SYS"：系统自带
	 * {@link com.legendshop.product.enums.ProductPropertySourceEnum}
	 */
	@Schema(description = "规格来源 \"USER\":用户自定义，\"SYS\"：系统自带")
	private String source;

	/**
	 * 删除标识【true：已删除  false：正常使用】
	 */
	@Schema(description = "删除标识【true：已删除  false：正常使用】")
	private Boolean deleteFlag;

	/**
	 * 搜索标识【true：可搜索  false：不搜索】
	 */
	@Schema(description = "搜索标识【true：可搜索  false：不搜索】")
	private Boolean searchFlag;

	/**
	 * 属性对应的属性值集合
	 */
	@Schema(description = "属性对应的属性值集合")
	private String prodPropStr;

	/**
	 * 属性对应的属性值集合
	 */
	@Schema(description = "属性对应的属性值集合")
	private List<ProductPropertyValueBO> prodPropList;

	/**
	 * 类目关联管理集合
	 */
	@Schema(description = "类目关联管理集合")
	private List<ProductPropertyAggBO> aggBO;

	/**
	 * 类目关联管理
	 */
	@Schema(description = "类目关联管理")
	private String PropertyAggStr;

	@Schema(description = "参数组对应的参数集合")
	private List<ProductPropertyBO> productPropertyBOList;

	/**
	 * 添加对应的属性到属性值集合
	 *
	 * @param productPropertyValueBO
	 */
	public void addProductPropertyValueList(ProductPropertyValueBO productPropertyValueBO) {
		if (this.getId().equals(productPropertyValueBO.getPropId())) {
			prodPropList.add(productPropertyValueBO);
		}
	}
}
