/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.api;

import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.basic.enums.OpStatusEnum;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.security.utils.SecurityUtils;
import com.legendshop.product.bo.DecorateProductBO;
import com.legendshop.product.bo.HotSellProductBO;
import com.legendshop.product.bo.ProductBO;
import com.legendshop.product.dto.ProductDTO;
import com.legendshop.product.query.ProductQuery;
import com.legendshop.product.service.FavoriteProductService;
import com.legendshop.product.service.ProductCommentService;
import com.legendshop.product.service.ProductService;
import com.legendshop.product.service.VitLogService;
import com.legendshop.product.utils.ProductStatusChecker;
import com.legendshop.shop.dto.ShopDecorateProductQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Feign调用实现
 *
 * @author legendshop
 */
@RestController
@Validated
@RequiredArgsConstructor
public class ProductApiImpl implements ProductApi {

	final VitLogService vitLogService;
	final ProductService productService;
	final ProductStatusChecker productStatusChecker;
	final ProductCommentService productCommentService;
	final FavoriteProductService favoriteProductService;

	@Override
	public R<PageSupport<ProductDTO>> queryProductOnLine(ProductQuery query) {
		return R.ok(productService.queryProductOnLine(query));
	}

	@Override
	public R<PageSupport<ProductDTO>> queryProductOnLineEs(ProductQuery query) {
		return R.ok(productService.queryProductOnLineEs(query));
	}

	@Override
	public R<List<ProductDTO>> queryProductOnLineEsByProductId(List<Long> productIds) {
		return R.ok(productService.queryProductOnLineEsByProductId(productIds));
	}

	@Override
	public R<ProductDTO> getProductOnLineEsByProductId(Long productId) {
		return R.ok(productService.getProductOnLineEsByProductId(productId));
	}

	@Override
	public R<PageSupport<ProductBO>> getPage(ProductQuery productQuery) {
		return R.ok(productService.getPage(productQuery));
	}

	@Override
	public R<PageSupport<DecorateProductBO>> queryDecorateProductList(ProductQuery productQuery) {
		return R.ok(productService.queryDecorateProductList(productQuery));
	}

	@Override
	public R<PageSupport<DecorateProductBO>> queryDecorateProductList(ShopDecorateProductQuery shopDecorateProductQuery) {
		return R.ok(productService.queryDecorateProductList(shopDecorateProductQuery));
	}

	@Override
	public R<List<ProductDTO>> queryAllByIds(List<Long> productId) {
		return R.ok(productService.queryAllByIds(productId));
	}

	@Override
	public R<List<ProductDTO>> getProductListByGroupId(Long productGroupId, Integer pageSize) {
		return R.ok(productService.getProductListByGroupId(productGroupId, pageSize));
	}

	@Override
	public R<List<HotSellProductBO>> queryHotSellProdByShopId(Long shopId) {
		return R.ok(productService.queryHotSellProdByShopId(shopId));
	}

	@Override
	public R<ProductDTO> getDtoByProductId(Long id) {
		return R.ok(productService.getDtoByProductId(id));
	}

	@Override
	public R<ProductDTO> getOnlineDtoByProductId(Long productId) {
		return R.ok(productService.getOnlineDtoByProductId(productId));
	}

	@Override
	public R<ProductBO> getBoByProductId(Long productId) {
		R<ProductBO> productBOR = productService.getBoByProductId(productId);
		if (productStatusChecker.isProductNormal(productBOR.getData())) {
			return productBOR;
		}
		return R.fail();
	}

	@Override
	public R<Void> offLineByShopIds(List<Long> ids) {
		productService.offLineByShopIds(ids);
		return R.ok();
	}

	@Override
	public R<Long> getProductCountByShopId(Long shopId, OpStatusEnum opStatusEnum) {
		return R.ok(productService.getProductCountByShopId(shopId, opStatusEnum));
	}

	@Override
	public R<Integer> updateStocksByProductId(Long id, Integer basketCount) {
		Long shopId = SecurityUtils.getShopUser().getShopId();
		return R.ok(productService.updateStocksByProductId(id, basketCount, shopId));
	}


	@Override
	public Long getProductIndexCount() {
		return productService.getIndexCount();
	}
}
