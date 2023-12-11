/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.service.convert;

import com.legendshop.common.core.service.BaseConverter;
import com.legendshop.product.bo.ProductPropertyImageBO;
import com.legendshop.product.dto.ProductPropertyImageDTO;
import com.legendshop.product.entity.ProductPropertyImage;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * @author legendshop
 */
@Mapper
public interface ProductPropertyImageConverter extends BaseConverter<ProductPropertyImage, ProductPropertyImageDTO> {

	/**
	 * to bo List
	 *
	 * @param productPropertyImageList
	 * @return
	 */
	List<ProductPropertyImageBO> convert2BoList(List<ProductPropertyImage> productPropertyImageList);
}
