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
import com.legendshop.product.bo.BrandBO;
import com.legendshop.product.dto.BrandDTO;
import com.legendshop.product.entity.Brand;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * 品牌转换器
 *
 * @author legendshop
 */
@Mapper
public interface BrandConverter extends BaseConverter<Brand, BrandDTO> {

	/**
	 * to bo list
	 *
	 * @param brandListByUserId
	 * @return
	 */
	List<BrandBO> convert2BoList(List<Brand> brandListByUserId);

	/**
	 * to bo
	 *
	 * @param brand
	 * @return
	 */
	BrandBO convert2Bo(Brand brand);


	/**
	 * to bo page list
	 *
	 * @param queryBrand
	 * @return
	 */
	PageSupport<BrandBO> convert2BoPage(PageSupport<Brand> queryBrand);

	/**
	 * to source
	 *
	 * @param originBrand
	 * @return
	 */
	Brand from(BrandBO originBrand);

}
