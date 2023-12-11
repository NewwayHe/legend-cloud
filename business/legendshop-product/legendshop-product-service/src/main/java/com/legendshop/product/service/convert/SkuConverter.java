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
import com.legendshop.product.bo.SkuBO;
import com.legendshop.product.dto.SkuDTO;
import com.legendshop.product.entity.Sku;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * @author legendshop
 */
@Mapper
public interface SkuConverter extends BaseConverter<Sku, SkuDTO> {

	/**
	 * to bo
	 *
	 * @param sku
	 * @return
	 */
	SkuBO convert2BO(Sku sku);

	/**
	 * to bo List
	 *
	 * @param skuList
	 * @return
	 */
	List<SkuBO> convert2BoList(List<Sku> skuList);

	/**
	 * to bo pageList
	 *
	 * @param ps
	 * @return
	 */
	PageSupport<SkuBO> convert2BoPageList(PageSupport<Sku> ps);

	/**
	 * to sku
	 *
	 * @param skuBO
	 * @return
	 */
	Sku toEntity(SkuBO skuBO);

	List<SkuDTO> boListConvert2DTOList(List<SkuBO> skuList);

	List<Sku> toEntityList(List<SkuBO> skuList);

	SkuDTO boConvert2DTO(SkuBO sku);

	/**
	 * to dto List
	 *
	 * @param skuList
	 * @return
	 */
	List<SkuDTO> toDTOList(List<SkuBO> skuList);
}
