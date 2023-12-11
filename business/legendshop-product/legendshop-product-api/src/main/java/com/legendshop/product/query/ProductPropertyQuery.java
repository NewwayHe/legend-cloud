/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.query;


import cn.legendshop.jpaplus.support.PageParams;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 属性名(ProdProp)实体类
 *
 * @author legendshop
 */
@Data
@Schema(description = "属性名参数查询")
public class ProductPropertyQuery extends PageParams {

	/**
	 * 店铺ID
	 */
	@Schema(description = "店铺ID")
	private Long shopId;

	/**
	 * 商品ID
	 */
	@Schema(description = "商品ID")
	private Long productId;


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
	 * 排序
	 */
	@Schema(description = "排序")
	private Integer sequence;


	/**
	 * 删除标识【true：已删除  false：正常使用】
	 */
	@Schema(description = "删除标识【true：已删除  false：正常使用】")
	private Boolean deleteFlag;

	/**
	 * 属性类型  TXT：文本类型; PIC:图片类型;
	 * {@link com.legendshop.product.enums.ProductPropertyTypeEnum}
	 */
	@Schema(description = "属性类型  TXT：文本类型; PIC:图片类型;")
	private String type;


	/**
	 * 商品属性： 'P':参数属性; 'S':规格属性;
	 * {@link com.legendshop.product.enums.ProductPropertyAttributeTypeEnum}
	 */
	@Schema(description = "商品属性：\"P\":参数属性; \"S\":规格属性;")
	@NotBlank(message = "商品属性不能为空")
	private String attributeType;

	/**
	 * 属性来源 {@link com.legendshop.product.enums.ProductPropertySourceEnum}
	 */
	@Schema(description = "属性来源：\"USER\"; 用户自定义，\"SYS\"：系统自带")
	private String source;

	@Schema(description = "proTypeId")
	private Long proTypeId;

	@Schema(description = "参数组id")
	private Long paramsGroupId;
}
