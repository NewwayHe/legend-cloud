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
import com.legendshop.product.dto.ProductImportDetailDTO;
import com.legendshop.product.dto.ProductImportErrorDetailDTO;
import com.legendshop.product.entity.ProductImportDetail;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * @author legendshop
 */
@Mapper
public interface ProductImportDetailConverter extends BaseConverter<ProductImportDetail, ProductImportDetailDTO> {
	/**
	 * 将商品导入详情列表转换为商品导入错误详情DTO列表的方法。
	 *
	 * @param productImportDetailList 商品导入详情列表
	 * @return 商品导入错误详情DTO列表
	 */
	List<ProductImportErrorDetailDTO> converterTo(List<ProductImportDetail> productImportDetailList);
}
