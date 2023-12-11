/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.service.convert;

import com.legendshop.common.core.service.BaseConverter;
import com.legendshop.shop.dto.NewsCategoryDTO;
import com.legendshop.shop.entity.NewsCategory;
import org.mapstruct.Mapper;

/**
 * @author legendshop
 */
@Mapper
public interface NewsCategoryConverter extends BaseConverter<NewsCategory, NewsCategoryDTO> {

}
