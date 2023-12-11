/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.activity.coupon.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.legendshop.activity.coupon.AbstractCouponExecutor;
import com.legendshop.activity.dto.CouponItemDTO;
import com.legendshop.activity.dto.CouponProductDTO;
import com.legendshop.activity.enums.CouponRuleEnum;
import com.legendshop.activity.enums.CouponSelectStatusEnum;
import com.legendshop.product.dto.ProductItemDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import static com.legendshop.activity.enums.CouponUseTypeEnum.GENERAL;
import static com.legendshop.activity.enums.CouponUseTypeEnum.INCLUDE;

/**
 * 商家满减优惠券结算规则执行器
 *
 * @author legendshop
 */
@Component("FULL_REDUCTION")
@Slf4j
public class FullReductionExecutor extends AbstractCouponExecutor {


	@Override
	public CouponRuleEnum couponRule() {
		return CouponRuleEnum.FULL_REDUCTION;
	}


	@Override
	public void processSelectCoupons(List<CouponItemDTO> couponList, List<ProductItemDTO> productItems, CouponItemDTO selectCoupon) {
		if (ObjectUtil.isNull(selectCoupon)) {
			couponList.forEach(couponItemDTO -> couponItemDTO.setSelectStatus(CouponSelectStatusEnum.OPTIONAL.getStatus()));
			return;
		}
		//店铺满减
		List<CouponItemDTO> shopFullReduction = couponList.stream().filter(couponItemDTO -> couponItemDTO.getUseType().equals(GENERAL.getValue())).collect(Collectors.toList());
		//商品满减
		List<CouponItemDTO> productFullReduction = couponList.stream().filter(couponItemDTO -> !couponItemDTO.getUseType().equals(GENERAL.getValue())).collect(Collectors.toList());
		if (selectCoupon.getUseType().equals(GENERAL.getValue())) {
			if (CollUtil.isNotEmpty(shopFullReduction)) {
				//查询到用户选择的店铺满减券
				//用户选中的优惠券
				shopFullReduction.forEach(shopCoupon -> {
					if (shopCoupon.getCouponId().equals(selectCoupon.getCouponId())) {
						shopCoupon.setSelectStatus(CouponSelectStatusEnum.SELECTED.getStatus());
					} else {
						shopCoupon.setSelectStatus(CouponSelectStatusEnum.UN_OPTIONAL.getStatus());
					}
				});
				//有了店铺满减券，所有商品满减券全部不能用
				if (CollUtil.isNotEmpty(productFullReduction)) {
					productFullReduction.forEach(productCoupon -> productCoupon.setSelectStatus(CouponSelectStatusEnum.UN_OPTIONAL.getStatus()));
				}

			}
		} else {
			//如果商品满减券不为空
			if (CollUtil.isNotEmpty(productFullReduction)) {
				List<CouponProductDTO> includeProductList = couponProductService.queryByCouponId(selectCoupon.getCouponId());
				//获取符合的商品
				List<ProductItemDTO> availableProductItemList = productItems.stream()
						.filter(productItemDTO -> includeProductList.stream().anyMatch(couponProduct -> {
							if (selectCoupon.getUseType().equals(INCLUDE.getValue())) {
								return productItemDTO.getSkuId().equals(couponProduct.getSkuId());
							} else {
								return !productItemDTO.getSkuId().equals(couponProduct.getSkuId());
							}
						})).collect(Collectors.toList());
				if (CollUtil.isNotEmpty(availableProductItemList)) {
					selectCoupon.setSelectStatus(CouponSelectStatusEnum.SELECTED.getStatus());
				}
			}
		}
	}

	/**
	 * 过滤商品列表，查出符合的商品
	 *
	 * @param selectCouponItem
	 * @param productItems
	 * @return
	 */
	@Override
	protected List<ProductItemDTO> filterProducts(CouponItemDTO selectCouponItem, List<ProductItemDTO> productItems) {
		if (selectCouponItem.getUseType().equals(GENERAL.getValue())) {
			return productItems;
		}
		return super.filterProductItems(selectCouponItem, productItems);
	}

	/**
	 * 过滤不是自己的优惠券
	 *
	 * @param couponList
	 * @return
	 */
	@Override
	public List<CouponItemDTO> filter(List<CouponItemDTO> couponList) {
		return couponList.stream().filter(couponItemDTO -> couponItemDTO.getMinPoint().compareTo(BigDecimal.ZERO) > 0).collect(Collectors.toList());
	}

}
