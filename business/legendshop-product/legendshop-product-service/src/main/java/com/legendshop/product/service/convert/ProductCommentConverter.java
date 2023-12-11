/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.service.convert;

import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.common.core.service.BaseConverter;
import com.legendshop.product.bo.ProductCommentBO;
import com.legendshop.product.dto.ProductCommentDTO;
import com.legendshop.product.entity.ProductComment;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * 商品评论转换器
 *
 * @author legendshop
 */
@Mapper
public interface ProductCommentConverter extends BaseConverter<ProductComment, ProductCommentDTO> {

	/**
	 * to bo
	 *
	 * @param productComment
	 * @return
	 */
	ProductCommentBO convert2BO(ProductComment productComment);

	/**
	 * to bo List
	 *
	 * @param productCommentList
	 * @return
	 */
	List<ProductCommentBO> convert2BoList(List<ProductComment> productCommentList);

	/**
	 * to bo pageList
	 *
	 * @param ps
	 * @return
	 */
	PageSupport<ProductCommentBO> convert2BoPageList(PageSupport<ProductComment> ps);
}
