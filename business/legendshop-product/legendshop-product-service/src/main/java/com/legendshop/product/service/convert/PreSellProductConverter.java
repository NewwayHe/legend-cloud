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
import com.legendshop.product.bo.PreSellProductBO;
import com.legendshop.product.dto.PreSellProductDTO;
import com.legendshop.product.entity.PreSellProduct;
import org.mapstruct.Mapper;

/**
 * 预售商品表(PreSellProduct)转换器
 *
 * @author legendshop
 * @since 2020-08-18 10:14:18
 */
@Mapper
public interface PreSellProductConverter extends BaseConverter<PreSellProduct, PreSellProductDTO> {

	/**
	 * to bo
	 *
	 * @param productDTO
	 * @return
	 */
	PreSellProductBO convert2BO(PreSellProductDTO productDTO);
}
