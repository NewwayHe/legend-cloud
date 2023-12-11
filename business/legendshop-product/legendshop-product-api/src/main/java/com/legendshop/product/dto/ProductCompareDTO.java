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

import java.io.Serializable;
import java.util.Map;

/**
 * 商品对比Dto
 *
 * @author legendshop
 */
@Data
public class ProductCompareDTO implements Serializable {

	private static final long serialVersionUID = 8269196458836894493L;

	private IdNameImgDTO[] idNameImg = new IdNameImgDTO[4];

	private Double[] price = new Double[4];

	private String[] brandName = new String[4];

	private String[] categoryName = new String[4];

	private Map<IdNameDTO, String[]> paramMap = null;

}
