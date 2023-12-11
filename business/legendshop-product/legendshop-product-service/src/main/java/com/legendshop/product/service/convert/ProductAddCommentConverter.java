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
import com.legendshop.product.bo.ProductAddCommentBO;
import com.legendshop.product.dto.ProductAddCommentDTO;
import com.legendshop.product.entity.ProductAddComment;
import org.mapstruct.Mapper;

/**
 * @author legendshop
 */
@Mapper
public interface ProductAddCommentConverter extends BaseConverter<ProductAddComment, ProductAddCommentDTO> {

	/**
	 * to bo
	 *
	 * @param productAddCommentByCommId
	 * @return
	 */
	ProductAddCommentBO convert2BO(ProductAddComment productAddCommentByCommId);
}
