/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.entity;


import cn.legendshop.jpaplus.persistence.*;
import cn.legendshop.jpaplus.support.GenericEntity;
import com.legendshop.common.core.dto.BaseEntity;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 属性名(ProdProp)实体类
 *
 * @author legendshop
 */
@Data
@Entity
@Table(name = "ls_product_property")
public class ProductProperty extends BaseEntity implements GenericEntity<Long> {

	private static final long serialVersionUID = -92062457517374885L;

	/**
	 * 属性ID
	 */
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "generator")
	@TableGenerator(name = "generator", pkColumnValue = "PRODUCT_PROPERTY_SEQ")
	private Long id;


	/**
	 * 商品ID
	 */
	@Column(name = "product_id")
	private Long productId;

	/**
	 * 属性名称
	 */
	@Column(name = "prop_name")
	private String propName;

	/**
	 * 店铺id
	 */
	@Column(name = "shop_id")
	private Long shopId;

	/**
	 * 副标题
	 */
	@Column(name = "memo")
	private String memo;

	/**
	 * 属性类型 ProductPropertyTypeEnum TXT：文本类型; PIC:图片类型;
	 */
	@Column(name = "type")
	private String type;


	/**
	 * 商品属性：{@link com.legendshop.product.enums.ProductPropertyAttributeTypeEnum}
	 */
	@Column(name = "attribute_type")
	private String attributeType;

	/**
	 * 规格来源 {@link com.legendshop.product.enums.ProductPropertySourceEnum}
	 */
	@Column(name = "source")
	private String source;

	/**
	 * 删除标识【true：已删除  false：正常使用】
	 */
	@Column(name = "delete_flag")
	private Boolean deleteFlag;

	/**
	 * 搜索标识【true：可搜索  false：不搜索】
	 */
	@Column(name = "search_flag")
	private Boolean searchFlag;

	/**
	 *	商品属性
	 */
	@Transient
	private List<ProductPropertyValue> prodPropList = new ArrayList<>();

}
