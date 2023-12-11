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
import com.legendshop.product.bo.CategoryBO;
import com.legendshop.product.dto.CategoryDTO;
import com.legendshop.product.entity.Category;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * 分类转换器
 *
 * @author legendshop
 */
@Mapper
public interface CategoryConverter extends BaseConverter<Category, CategoryDTO> {

	/**
	 * to bo list
	 *
	 * @param categoryList
	 * @return
	 */
	List<CategoryBO> convert2BoList(List<Category> categoryList);

	/**
	 * to bo
	 *
	 * @param category
	 * @return
	 */
	CategoryBO convert2Bo(Category category);

	/**
	 * dtoList to boList
	 *
	 * @param categoryDTOS
	 * @return
	 */
	List<CategoryBO> dtoListConvert2BoList(List<CategoryDTO> categoryDTOS);

	/**
	 * to bo pageList
	 *
	 * @param ps
	 * @return
	 */
	PageSupport<CategoryBO> convert2BoPageList(PageSupport<Category> ps);
}
