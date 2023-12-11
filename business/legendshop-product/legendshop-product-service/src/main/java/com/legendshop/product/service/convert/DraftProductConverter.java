/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.service.convert;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import com.legendshop.common.core.service.BaseConverter;
import com.legendshop.product.bo.ProductBO;
import com.legendshop.product.bo.ProductUpdateBO;
import com.legendshop.product.dto.DraftProductDTO;
import com.legendshop.product.dto.ProductDTO;
import com.legendshop.product.dto.ProductPropertyValueDTO;
import com.legendshop.product.dto.ProductQuotaDTO;
import com.legendshop.product.entity.DraftProduct;
import com.legendshop.product.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.ArrayList;
import java.util.List;

/**
 * 商品SPU草稿表(DraftProduct)转换器
 *
 * @author legendshop
 * @since 2022-05-08 09:37:12
 */
@Mapper
public interface DraftProductConverter extends BaseConverter<DraftProduct, DraftProductDTO> {

	/**
	 * 商品DTO转换成草稿
	 *
	 * @param productDTO
	 * @return
	 */
	@Mappings({
			@Mapping(target = "id", ignore = true),
			@Mapping(target = "status", ignore = true),
			@Mapping(target = "createTime", ignore = true),
			@Mapping(target = "updateTime", ignore = true),
			@Mapping(target = "productId", source = "id"),
			@Mapping(target = "propertyImage", expression = "java(cn.hutool.json.JSONUtil.toJsonStr(productDTO.getImageList()))"),
			@Mapping(target = "preSellProduct", expression = "java(cn.hutool.json.JSONUtil.toJsonStr(productDTO.getPreSellProductDTO()))"),
			@Mapping(target = "customPropertyValue", expression = "java(this.convert2CustomPropertyValue(productDTO))"),
	})
	DraftProduct convert2DraftProduct(ProductDTO productDTO);

	/**
	 * 草稿商品转商品
	 *
	 * @param draftProduct
	 * @return
	 */
	@Mappings({
			@Mapping(target = "id", source = "productId"),
			@Mapping(target = "status", ignore = true),
			@Mapping(target = "createTime", ignore = true),
			@Mapping(target = "updateTime", ignore = true),
	})
	Product convert2Product(DraftProduct draftProduct);

	/**
	 * 草稿商品转商品BO
	 *
	 * @param draftProduct
	 * @return
	 */
	@Mappings({
			@Mapping(target = "id", source = "productId"),
			@Mapping(target = "status", ignore = true),
			@Mapping(target = "createTime", ignore = true),
			@Mapping(target = "updateTime", ignore = true),
	})
	ProductBO convert2ProductBO(DraftProduct draftProduct);

	/**
	 * 草稿商品转商品BO
	 *
	 * @param draftProduct
	 * @return
	 */
	@Mappings({
			@Mapping(target = "id", source = "productId"),
			@Mapping(target = "status", ignore = true),
			@Mapping(target = "createTime", ignore = true),
			@Mapping(target = "updateTime", ignore = true),
			@Mapping(target = "specificationList", expression = "java(cn.hutool.json.JSONUtil.toList(cn.hutool.json.JSONUtil.parseArray(draftProduct.getSpecification()), com.legendshop.product.dto.ProductPropertyDTO.class))"),
			@Mapping(target = "globalCategoryId", expression = "java(this.convert2GlobalCategoryId(draftProduct))"),
			@Mapping(target = "shopCategoryId", expression = "java(this.convert2ShopCategoryId(draftProduct))"),
			@Mapping(target = "img", expression = "java(cn.hutool.json.JSONUtil.toList(cn.hutool.json.JSONUtil.parseArray(draftProduct.getImgPath()), java.lang.String.class))"),
			@Mapping(target = "productQuotaDTO", expression = "java(this.convert2ProductInfoQuota(draftProduct))"),
			@Mapping(target = "preSellProductDTO", expression = "java(cn.hutool.json.JSONUtil.toBean(draftProduct.getPreSellProduct(), com.legendshop.product.dto.PreSellProductDTO.class))"),
	})
	ProductUpdateBO convert2ProductUpdateBO(DraftProduct draftProduct);

	/**
	 * 平台类目转换
	 *
	 * @param draftProduct
	 * @return
	 */
	default List<Long> convert2GlobalCategoryId(DraftProduct draftProduct) {
		List<Long> globalCategoryId = new ArrayList<>();
		globalCategoryId.add(draftProduct.getGlobalFirstCatId());
		if (null != draftProduct.getGlobalSecondCatId()) {
			globalCategoryId.add(draftProduct.getGlobalSecondCatId());
		}
		if (null != draftProduct.getGlobalThirdCatId()) {
			globalCategoryId.add(draftProduct.getGlobalThirdCatId());
		}
		return globalCategoryId;
	}

	/**
	 * 店铺类目转换
	 *
	 * @param draftProduct
	 * @return
	 */
	default List<Long> convert2ShopCategoryId(DraftProduct draftProduct) {
		List<Long> shopCategoryId = new ArrayList<>();
		shopCategoryId.add(draftProduct.getShopFirstCatId());
		if (null != draftProduct.getShopSecondCatId()) {
			shopCategoryId.add(draftProduct.getShopSecondCatId());
		}
		if (null != draftProduct.getShopThirdCatId()) {
			shopCategoryId.add(draftProduct.getShopThirdCatId());
		}
		return shopCategoryId;
	}

	/**
	 * 草稿商品转商品BO
	 *
	 * @param draftProduct
	 * @return
	 */
	@Mappings({
			@Mapping(target = "id", source = "productId"),
			@Mapping(target = "status", ignore = true),
			@Mapping(target = "createTime", ignore = true),
			@Mapping(target = "updateTime", ignore = true),
	})
	ProductDTO convert2ProductDTO(DraftProduct draftProduct);


	/**
	 * 自定义参数转换
	 *
	 * @param productDTO
	 * @return
	 */
	default String convert2CustomPropertyValue(ProductDTO productDTO) {
		List<ProductPropertyValueDTO> productPropertyValueList = new ArrayList<>();
		if (productDTO.getCustomPropertyValueList() != null && productDTO.getCustomPropertyValueList().size() > 0) {
			//收集用户自定义的规格、参数值
			productDTO.getCustomPropertyValueList().forEach(e -> {
				e.getProdPropList().forEach(i -> {
					ProductPropertyValueDTO productPropertyValue = new ProductPropertyValueDTO();
					productPropertyValue.setName(i.getName());
					productPropertyValue.setId(i.getId());
					productPropertyValue.setPropId(e.getId());
					productPropertyValue.setShopId(productDTO.getShopId());
					productPropertyValue.setSequence(0);//排序暂时取0
					productPropertyValue.setDeleteFlag(false);
					productPropertyValue.setCreateTime(DateUtil.date());
					productPropertyValueList.add(productPropertyValue);
				});
			});
		}

		//保存属性值
		if (CollUtil.isNotEmpty(productPropertyValueList) && productPropertyValueList.size() > 0) {
			return JSONUtil.toJsonStr(productPropertyValueList);
		}
		return null;
	}

	/**
	 * 转成成限购对象
	 *
	 * @param product
	 * @return
	 */
	default ProductQuotaDTO convert2ProductInfoQuota(DraftProduct product) {
		ProductQuotaDTO request = new ProductQuotaDTO();
		request.setQuotaCount(product.getQuotaCount());
		request.setQuotaTime(product.getQuotaTime());
		request.setQuotaType(product.getQuotaType());
		return request;
	}
}

