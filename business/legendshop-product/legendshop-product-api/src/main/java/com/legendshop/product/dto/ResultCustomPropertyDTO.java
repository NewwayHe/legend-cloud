/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.dto;

import lombok.Data;

import java.util.List;

/**
 * 用户自定义属性
 * 一个属性有多个属性值
 *
 * @author legendshop
 */
@Data
public class ResultCustomPropertyDTO {

	private ProductPropertyDTO productPropertyDTO;

	private List<ProductPropertyValueDTO> propertyValueList;

}
