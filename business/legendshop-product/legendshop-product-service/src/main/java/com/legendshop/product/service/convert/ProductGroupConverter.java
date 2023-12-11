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
import com.legendshop.product.bo.ProductGroupBO;
import com.legendshop.product.dto.ProductGroupDTO;
import com.legendshop.product.entity.ProductGroup;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * @author legendshop
 */
@Mapper
public interface ProductGroupConverter extends BaseConverter<ProductGroup, ProductGroupDTO> {

	/**
	 * to bo
	 *
	 * @param productGroup
	 * @return
	 */
	ProductGroupBO convert2BO(ProductGroup productGroup);

	/**
	 * to bo List
	 *
	 * @param productGroupList
	 * @return
	 */
	List<ProductGroupBO> convert2BoList(List<ProductGroup> productGroupList);

	/**
	 * to bo pageList
	 *
	 * @param ps
	 * @return
	 */
	PageSupport<ProductGroupBO> convert2BoPageList(PageSupport<ProductGroup> ps);
}
