/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.activity.coupon;

import cn.hutool.core.util.NumberUtil;
import com.legendshop.activity.dto.CouponItemDTO;
import com.legendshop.activity.dto.CouponProductDTO;
import com.legendshop.activity.enums.CouponUseTypeEnum;
import com.legendshop.activity.service.CouponProductService;
import com.legendshop.product.dto.ProductItemDTO;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 规则执行器抽象类
 *
 * @author legendshop
 */
public abstract class AbstractCouponExecutor implements CouponRuleExecutor {


	@Autowired
	public CouponProductService couponProductService;


	@Override
	public void handleSelectCoupons(List<CouponItemDTO> filterCouponList, List<ProductItemDTO> productItems, CouponItemDTO couponItemDTO) {
		processSelectCoupons(filterCouponList, productItems, couponItemDTO);
	}

	/**
	 * 处理优惠券分摊
	 *
	 * @param selectCouponItem
	 * @param productItems
	 */
	@Override
	public void handlerCouponsShard(CouponItemDTO selectCouponItem, List<ProductItemDTO> productItems) {
		//获取该优惠券减多少钱
		BigDecimal couponTotalAmount = selectCouponItem.getAmount();
		productItems = filterProducts(selectCouponItem, productItems);

		BigDecimal productTotalPrice = productItems.stream().map(ProductItemDTO::getTotalDiscountedPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
		BigDecimal tmp = BigDecimal.ZERO;
		//循环分摊金额
		for (int i = 0; i < productItems.size(); i++) {
			ProductItemDTO productItemDTO = productItems.get(i);
			BigDecimal couponAmount = BigDecimal.ZERO;
			//判断是最后一次循环就直接减
			if (i != productItems.size() - 1) {
				//商品总金额占所有商品总金额的比例
				BigDecimal productScale = NumberUtil.div(productItemDTO.getTotalDiscountedPrice(), productTotalPrice);
				couponAmount = NumberUtil.mul(couponTotalAmount, productScale).setScale(2, RoundingMode.DOWN);
				//分摊到各个商品里面去
				productItemDTO.setCouponAmount(NumberUtil.add(couponAmount, productItemDTO.getCouponAmount()));
				tmp = NumberUtil.add(tmp, couponAmount);
			} else {
				couponAmount = NumberUtil.sub(couponTotalAmount, tmp).setScale(2, RoundingMode.DOWN);
				productItemDTO.setCouponAmount(NumberUtil.add(productItemDTO.getCouponAmount(), couponAmount));
			}
		}
	}


	protected List<ProductItemDTO> filterProductItems(CouponItemDTO selectCouponItem, List<ProductItemDTO> productItems) {
		List<CouponProductDTO> includeProduct = couponProductService.queryByCouponId(selectCouponItem.getCouponId());
		List<Long> skuIds = includeProduct.stream().map(CouponProductDTO::getSkuId).toList();
		return productItems.stream().filter(pi -> {
					if (selectCouponItem.getUseType().equals(CouponUseTypeEnum.EXCLUDE.getValue())) {
						return !skuIds.contains(pi.getSkuId());
					} else {
						return skuIds.contains(pi.getSkuId());
					}
				}
		).collect(Collectors.toList());
	}


	/**
	 * 最小支付费用
	 *
	 * @return
	 */
	protected double minCost() {
		return 0.1;
	}

	protected abstract List<ProductItemDTO> filterProducts(CouponItemDTO selectCouponItem, List<ProductItemDTO> productItems);

	protected abstract void processSelectCoupons(List<CouponItemDTO> couponList, List<ProductItemDTO> productItems, CouponItemDTO couponItemDTO);

}
