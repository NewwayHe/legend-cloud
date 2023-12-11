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
import com.legendshop.product.bo.*;
import com.legendshop.product.dto.ProductDTO;
import com.legendshop.product.dto.ProductPlatformExportDTO;
import com.legendshop.product.dto.ProductQuotaDTO;
import com.legendshop.product.entity.Product;
import com.legendshop.product.excel.ProductExportDTO;
import com.legendshop.product.excel.ProductRecycleBinExportDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

/**
 * @author legendshop
 */
@Mapper
public interface ProductConverter extends BaseConverter<Product, ProductDTO> {

	/**
	 * to bo
	 *
	 * @param product
	 * @return
	 */
	@Mappings({
			@Mapping(target = "productQuotaDTO", expression = "java(this.convert2ProductInfoQuota(product))")
	})
	ProductBO convert2BO(Product product);


	/**
	 * to bo
	 *
	 * @param product
	 * @return
	 */
	List<Product> convert2Product(List<ProductBO> product);

	/**
	 * to bo List
	 *
	 * @param productList
	 * @return
	 */
	List<ProductBO> convert2BoList(List<Product> productList);

	/**
	 * to bo pageList
	 *
	 * @param ps
	 * @return
	 */
	PageSupport<ProductBO> convert2BoPageList(PageSupport<Product> ps);

	/**
	 * ProductBOPage to DecorateProductBOPage
	 *
	 * @param ps
	 * @return
	 */
	PageSupport<DecorateProductBO> convert2DecorateProductBOPageList(PageSupport<ProductBO> ps);

	/**
	 * ProductBO to ProductAccusationBO
	 *
	 * @param ps
	 * @return
	 */
	PageSupport<ProductAccusationBO> convert2ProductAccusationBO(PageSupport<ProductBO> ps);

	/**
	 * ProductBO to ProductAuditBO
	 *
	 * @param ps
	 * @return
	 */
	PageSupport<ProductAuditBO> convert2ProductAuditBO(PageSupport<ProductBO> ps);

	/**
	 * productList to ProductExportDTO
	 *
	 * @param productList
	 * @return
	 */
	List<ProductExportDTO> convert2ProductExportDTO(List<ProductBO> productList);

	/**
	 * convert2ProductPlatformExportDTO
	 * @param productList
	 * @return
	 */
	List<ProductPlatformExportDTO> convert2ProductPlatformExportDTO(List<ProductPlatformBO> productList);

	/**
	 * ProductBO to ProductRecycleBinExportDTO
	 *
	 * @param productList
	 * @return
	 */
	List<ProductRecycleBinExportDTO> convert2ProductRecycleBinExportDTO(List<ProductBO> productList);

	default ProductQuotaDTO convert2ProductInfoQuota(Product product) {
		ProductQuotaDTO request = new ProductQuotaDTO();
		request.setQuotaCount(product.getQuotaCount());
		request.setQuotaTime(product.getQuotaTime());
		request.setQuotaType(product.getQuotaType());
		return request;
	}

	ProductDTO convertToProductDTO(ProductBO ps);

}
