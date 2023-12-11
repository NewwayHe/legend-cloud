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
import com.legendshop.product.bo.SkuBO;
import com.legendshop.product.dto.DraftSkuDTO;
import com.legendshop.product.dto.SkuDTO;
import com.legendshop.product.entity.DraftSku;
import com.legendshop.product.entity.Sku;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

/**
 * 单品SKU草稿表(DraftSku)转换器
 *
 * @author legendshop
 * @since 2022-05-08 09:37:07
 */
@Mapper
public interface DraftSkuConverter extends BaseConverter<DraftSku, DraftSkuDTO> {

	/**
	 * 将sku转换成草稿
	 *
	 * @param skuDTO
	 * @return
	 */
	@Mappings({
			@Mapping(target = "id", ignore = true),
			@Mapping(target = "skuId", source = "id"),
			@Mapping(target = "createTime", ignore = true),
			@Mapping(target = "updateTime", ignore = true),
	})
	DraftSku convert2DraftSku(SkuDTO skuDTO);

	/**
	 * 将skuList转换成草稿
	 *
	 * @param skuDTO
	 * @return
	 */
	List<DraftSku> convert2DraftSku(List<SkuDTO> skuDTO);

	/**
	 * 草稿sku转sku
	 *
	 * @param draftSkus
	 * @return
	 */
	List<Sku> convert2Sku(List<DraftSku> draftSkus);

	/**
	 * 草稿sku转sku
	 *
	 * @param draftSkus
	 * @return
	 */
	@Mappings({
			@Mapping(target = "id", source = "skuId"),
			@Mapping(target = "stocks", expression = "java(0)"),
			@Mapping(target = "actualStocks", expression = "java(0)"),
			@Mapping(target = "buys", expression = "java(0)"),
			@Mapping(target = "integralFlag", expression = "java(Boolean.FALSE)"),
			@Mapping(target = "integralDeductionFlag", expression = "java(Boolean.FALSE)"),
	})
	Sku convert2Sku(DraftSku draftSkus);


	/**
	 * 草稿sku转sku
	 *
	 * @param draftSkus
	 * @return
	 */
	List<SkuBO> convert2SkuBO(List<DraftSku> draftSkus);

	/**
	 * 草稿sku转sku
	 *
	 * @param draftSkus
	 * @return
	 */
	@Mappings({
			@Mapping(target = "id", source = "skuId"),
			@Mapping(target = "stocks", expression = "java(0)"),
			@Mapping(target = "actualStocks", expression = "java(0)"),
			@Mapping(target = "buys", expression = "java(0)"),
//			@Mapping(target = "integralFlag", expression = "java(Boolean.FALSE)"),
//			@Mapping(target = "integralDeductionFlag", expression = "java(Boolean.FALSE)"),
	})
	SkuBO convert2SkuBO(DraftSku draftSkus);


}

