/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.dto;


import com.legendshop.common.core.annotation.EnumValid;
import com.legendshop.common.core.dto.BaseDTO;
import com.legendshop.product.enums.ProductPropertyAttributeTypeEnum;
import com.legendshop.product.enums.ProductPropertySourceEnum;
import com.legendshop.product.enums.ProductPropertyTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 属性名(ProdProp)实体类
 *
 * @author legendshop
 */
@Data
@Schema(description = "属性名DTO")
public class ProductPropertyDTO extends BaseDTO implements Serializable {


	private static final long serialVersionUID = -6560928679367467388L;

	/**
	 * 属性ID
	 */
	@Schema(description = "属性ID")
	private Long id;


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
	@NotBlank(message = "属性名称不能为空")
	@Schema(description = "属性名称")
	@Length(max = 20, message = "属性名称字符长度不能超过20个")
	private String propName;


	/**
	 * 副标题
	 */
	@Schema(description = "副标题")
	@Length(max = 50, message = "副标题不能超过50个")
	private String memo;

	/**
	 * 属性类型 {@link com.legendshop.product.enums.ProductPropertyTypeEnum }
	 */
	@Schema(description = "属性类型：\"TXT\"：文本类型; \"PIC\":图片类型;")
	@EnumValid(target = ProductPropertyTypeEnum.class, message = "属性类型异常")
	private String type;


	/**
	 * 商品属性：{@link com.legendshop.product.enums.ProductPropertyAttributeTypeEnum}
	 */
	@Schema(description = "商品属性：\"P\":参数属性; \"S\"规格属性")
	@EnumValid(target = ProductPropertyAttributeTypeEnum.class, message = "商品属性异常")
	private String attributeType;


	/**
	 * 属性来源 {@link com.legendshop.product.enums.ProductPropertySourceEnum}
	 */
	@Schema(description = "属性来源：\"USER\"; 用户自定义，\"SYS\"：系统自带")
	@EnumValid(target = ProductPropertySourceEnum.class, message = "属性来源异常")
	private String source;

	/**
	 * 删除标识【true：已删除  false：正常使用】
	 */
	@Schema(description = "删除标识")
	private Boolean deleteFlag;

	/**
	 * 搜索标识【true：可搜索  false：不搜索】
	 */
	@Schema(description = "搜索标识")
	private Boolean searchFlag;

	/**
	 * 属性值图，最新版已经去掉，预留字段
	 */
	@Schema(description = "属性值图")
	private String valuePic;

	/**
	 * 类目关联id列表
	 */
	@Schema(description = "类目关联id列表")
	private List<Long> aggIdList;
	/**
	 * 属性对应的属性值集合
	 */
	@NotEmpty(message = "属性值集合不能为空")
	@Schema(description = "属性对应的属性值集合")
	private List<ProductPropertyValueDTO> prodPropList;

	public void addProductPropertyValueDto(Long propId, String name, Long valueId, String valuePic) {
		if (prodPropList == null) {
			prodPropList = new ArrayList<>();
		}
		ProductPropertyValueDTO productPropertyValueDto = new ProductPropertyValueDTO();
		productPropertyValueDto.setName(name);
		productPropertyValueDto.setPropId(propId);
		productPropertyValueDto.setId(valueId);
		productPropertyValueDto.setPic(valuePic);

		prodPropList.add(productPropertyValueDto);
	}

	public void addProductPropertyValueList(ProductPropertyValueDTO propValueDto) {
		if (prodPropList == null) {
			prodPropList = new ArrayList<>();
		}
		if (this.id.equals(propValueDto.getPropId())) {
			prodPropList.add(propValueDto);
		}
	}
}