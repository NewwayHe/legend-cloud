/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.activity.coupon;

import cn.hutool.core.collection.CollUtil;
import com.legendshop.activity.dto.CouponItemDTO;
import com.legendshop.activity.dto.SelectPlatformCouponDTO;
import com.legendshop.activity.dto.ShopCouponDTO;
import com.legendshop.activity.enums.CouponRuleEnum;
import com.legendshop.product.dto.ProductItemDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static com.legendshop.activity.enums.CouponSelectStatusEnum.*;
import static com.legendshop.activity.enums.CouponUseTypeEnum.*;

/**
 * 优惠券结算规则执行管理器
 * 即根据用户的请求找到对应的 Executor 去做计算
 *
 * @author legendshop
 */
@Slf4j
@Component
@AllArgsConstructor
public class CouponExecuteManager {

	/**规则执行映射*/
	private final Map<String, CouponRuleExecutor> executor;

	public ShopCouponDTO handleSelectCoupons(ShopCouponDTO shopCouponDTO) {
		List<CouponItemDTO> couponItems = shopCouponDTO.getCouponItems();
		List<ProductItemDTO> productItems = shopCouponDTO.getProductItems();
		List<Long> couponIds = shopCouponDTO.getCouponIds();
		if (CollUtil.isNotEmpty(couponIds)) {
			//用户传过来，用户选中优惠劵信息
			List<CouponItemDTO> newSelectCouponList = couponItems.stream().filter(couponItemDTO -> couponIds.stream().anyMatch(id -> couponItemDTO.getCouponId().equals(id))).collect(Collectors.toList());

			// 首先判断用户操作是否合法
			// 获取已选中的优惠券数量
			long selectedCount = couponItems.stream().filter(coupon -> SELECTED.getStatus().equals(coupon.getSelectStatus())).count();

			// 如果用户同时操作了两个或以上的优惠券，直接返回
			if (Math.abs(selectedCount - couponIds.size()) > 1) {
				return shopCouponDTO;
			}

			// 然后判断用户选中的优惠券是否可用
			if (newSelectCouponList.stream().anyMatch(coupon -> UN_OPTIONAL.getStatus().equals(coupon.getSelectStatus()))) {
				return shopCouponDTO;
			}

			// 默认所有优惠卷不可用
			couponItems.forEach(couponItemDTO -> couponItemDTO.setSelectStatus(UN_OPTIONAL.getStatus()));

			//先把我选中的选中先
			newSelectCouponList.forEach(couponItemDTO -> {
				couponItemDTO.setSelectStatus(SELECTED.getStatus());
			});

			//移除已选中的优惠券
			List<CouponItemDTO> otherCouponList = couponItems.stream().filter(couponItemDTO -> couponIds.stream().noneMatch(id -> couponItemDTO.getCouponId().equals(id))).collect(Collectors.toList());

			//处理互斥关系优惠券
			processOther(otherCouponList, newSelectCouponList, productItems);
		} else {
			//用户选择空的优惠券的话都是可用
			couponItems.forEach(couponItemDTO -> couponItemDTO.setSelectStatus(OPTIONAL.getStatus()));


		}
		return shopCouponDTO;
	}

	/**
	 * 优惠券的组合关系
	 * ----------------------------------------------------------
	 * 平台优惠卷有且只有一张对应到订单
	 * 每个商品只能有且只有一张商家优惠卷
	 * ----------------------------------------------------------
	 *
	 * @param couponItems         the 全部优惠卷
	 * @param newSelectCouponList the 用户主动选中优惠卷
	 * @param productItems        the 下单商品sku
	 */
	private void processOther(List<CouponItemDTO> couponItems, List<CouponItemDTO> newSelectCouponList, List<ProductItemDTO> productItems) {
		//选择的店铺优惠券
		List<CouponItemDTO> shopNoThresholdList = newSelectCouponList.stream().filter(couponItemDTO -> couponItemDTO.getUseType().equals(GENERAL.getValue())).collect(Collectors.toList());
		//选择商品优惠券
		List<CouponItemDTO> productNoThresholdList = newSelectCouponList.stream().filter(couponItemDTO -> !couponItemDTO.getUseType().equals(GENERAL.getValue())).collect(Collectors.toList());

		if (!CollectionUtils.isEmpty(shopNoThresholdList)) {
			return;
		}

		if (CollectionUtils.isEmpty(productNoThresholdList)) {
			// 商品与商家券都为空，设置所有优惠卷可用
			couponItems.forEach(couponItemDTO -> couponItemDTO.setSelectStatus(OPTIONAL.getStatus()));
		}

		// 商品卷不为空，全场优惠卷不可用
		couponItems.stream().filter(couponItemDTO -> couponItemDTO.getUseType().equals(GENERAL.getValue())).collect(Collectors.toList()).forEach(couponItemDTO -> couponItemDTO.setSelectStatus(UN_OPTIONAL.getStatus()));
		List<CouponItemDTO> prodCouponItemList = couponItems.stream().filter(couponItemDTO -> !couponItemDTO.getUseType().equals(GENERAL.getValue())).collect(Collectors.toList());


		// 选中优惠卷对应的sku
		List<Long> selectSkuIds = productNoThresholdList.stream().map(CouponItemDTO::getProductItems).flatMap(Collection::stream).collect(Collectors.toList()).stream().map(ProductItemDTO::getSkuId).collect(Collectors.toList());

		// 所有商品的sku
		List<Long> prodSkuIds = productItems.stream().map(ProductItemDTO::getSkuId).collect(Collectors.toList());

		// 得到未选中优惠卷的商品
		prodSkuIds.removeAll(selectSkuIds);

		// 获取还可以用的优惠卷
		prodCouponItemList.forEach(e -> {
			List<Long> couponSkuIds = e.getProductItems().stream().map(ProductItemDTO::getSkuId).collect(Collectors.toList());

			// selectSkuIds  全部选中优惠劵  里面的商品
			// couponSkuIds   当前优惠劵 里面的可用商品
			// 如果 当前优惠劵 里面的可用商品  已被其它优惠劵中的商品重合，则此优惠劵不能用
			List<Long> containSelectSkuIds;
			if (e.getUseType().equals(INCLUDE.getValue())) {
				containSelectSkuIds = selectSkuIds.stream().filter(couponSkuIds::contains).collect(Collectors.toList());
			} else {
				containSelectSkuIds = selectSkuIds.stream().filter(s -> !couponSkuIds.contains(s)).collect(Collectors.toList());
			}
			// 不为空，则包含，下一个
			if (CollUtil.isNotEmpty(containSelectSkuIds)) {
				return;
			}


			// prodSkuIds  剩余没用使用优惠劵的商品
			// couponSkuIds 当前优惠劵 里面的可用商品
			// 当前优惠劵 里面的可用商品有  剩余没用使用优惠劵的商品  ，则优惠劵可用
			//获取可用skuIds
			List<Long> couponAvailableSkuIds;
			if (e.getUseType().equals(INCLUDE.getValue())) {
				couponAvailableSkuIds = prodSkuIds.stream().filter(couponSkuIds::contains).collect(Collectors.toList());
			} else {
				couponAvailableSkuIds = prodSkuIds.stream().filter(s -> !couponSkuIds.contains(s)).collect(Collectors.toList());
			}
			// 没有可用，下一个
			if (CollectionUtils.isEmpty(couponAvailableSkuIds)) {
				return;
			}
			// 判断是否有门槛
			if (e.getMinPoint().compareTo(BigDecimal.ZERO) == 0) {
				// 无门槛，可用
				e.setSelectStatus(OPTIONAL.getStatus());
				return;
			}
			// 判断是否满足门槛
			List<ProductItemDTO> couponProdItemList = productItems.stream()
					.filter(p -> couponAvailableSkuIds.contains(p.getSkuId()))
					.collect(Collectors.toList());
			if (couponProdItemList.stream()
					.map(ProductItemDTO::getTotalDiscountedPrice)
					.reduce(BigDecimal.ZERO, BigDecimal::add)
					.compareTo(e.getMinPoint()) >= 0) {
				e.setSelectStatus(OPTIONAL.getStatus());
			}
		});
	}


	private CouponRuleExecutor switchRule(BigDecimal minPoint) {
		CouponRuleEnum rule = CouponRuleEnum.getRule(minPoint);
		return executor.get(rule.name());
	}

	/**
	 * 处理优惠券分摊金额
	 *
	 * @param shopCouponDTO
	 */
	public ShopCouponDTO handlerShopCouponsShard(ShopCouponDTO shopCouponDTO) {
		List<ProductItemDTO> productItems = shopCouponDTO.getProductItems();
		List<CouponItemDTO> selectItems = shopCouponDTO.getSelectItems();
		//直接计算分摊金额
		selectItems.forEach(selectCouponItem -> {
			CouponRuleExecutor couponRuleExecutor = switchRule(selectCouponItem.getMinPoint());
			couponRuleExecutor.handlerCouponsShard(selectCouponItem, productItems);
		});
		return shopCouponDTO;
	}


	/* --------------------------------- 以下是平台优惠券处理 --------------------------------- */

	/**
	 * 处理选择平台优惠券
	 *
	 * @param selectPlatformCouponDTO
	 * @return
	 */
	public List<CouponItemDTO> handleSelectPlatformCoupons(SelectPlatformCouponDTO selectPlatformCouponDTO) {
		List<Long> couponIds = Optional.ofNullable(selectPlatformCouponDTO.getCouponIds()).orElse(Collections.emptyList());
		List<CouponItemDTO> platformCoupons = selectPlatformCouponDTO.getPlatformCoupons();
		List<ShopCouponDTO> shopCouponList = selectPlatformCouponDTO.getShopCouponList();

		// 获取用户选择的优惠券
		List<CouponItemDTO> newSelectCouponList = platformCoupons.stream().filter(couponItemDTO -> couponIds.contains(couponItemDTO.getCouponId())).collect(Collectors.toList());

		// 判断用户是否合法
		// 获取已选中的优惠券数量
		long selectedCount = platformCoupons.stream().filter(coupon -> SELECTED.getStatus().equals(coupon.getSelectStatus())).count();
		// 如果用户同时操作了两个或以上的优惠券，直接返回
		if (Math.abs(selectedCount - couponIds.size()) > 1) {
			return platformCoupons;
		}

		// 然后判断用户选中的优惠券是否可用
		if (newSelectCouponList.stream().anyMatch(coupon -> UN_OPTIONAL.getStatus().equals(coupon.getSelectStatus()))) {
			return platformCoupons;
		}

		// 都没问题，则默认所有优惠卷不可用
		platformCoupons.forEach(couponItemDTO -> couponItemDTO.setSelectStatus(UN_OPTIONAL.getStatus()));

		// 将用户选择的改为选中
		newSelectCouponList.forEach(couponItemDTO -> couponItemDTO.setSelectStatus(SELECTED.getStatus()));

		// 获取其它优惠券
		List<CouponItemDTO> otherCouponList = platformCoupons.stream().filter(couponItemDTO -> !couponIds.contains(couponItemDTO.getCouponId())).collect(Collectors.toList());

		// 处理其它优惠券互斥关系
		processPlatformOther(otherCouponList, newSelectCouponList, shopCouponList);

		return platformCoupons;
	}

	/**
	 * 处理互斥关系的优惠券
	 *
	 * @param otherCouponList     其它优惠券
	 * @param newSelectCouponList 用户选择的优惠券
	 * @param shopCouponList      店铺信息
	 */
	private void processPlatformOther(List<CouponItemDTO> otherCouponList, List<CouponItemDTO> newSelectCouponList, List<ShopCouponDTO> shopCouponList) {
		// 获取选中的通用券数
		long commonCount = newSelectCouponList.stream().filter(coupon -> GENERAL.getValue().equals(coupon.getUseType())).count();

		// 如果用户选择了平台通用券，则直接返回
		if (commonCount > 0) {
			return;
		}

		// 获取选中的其它券
		List<CouponItemDTO> otherSelectCouponList = newSelectCouponList.stream().filter(coupon -> !GENERAL.getValue().equals(coupon.getUseType())).collect(Collectors.toList());
		// 获取下单涉及的店铺
		List<Long> shopIds = shopCouponList.stream().map(ShopCouponDTO::getShopId).collect(Collectors.toList());
		// 获取选中的优惠券用到的店铺
		List<Long> selectShopIds = new ArrayList<>();
		otherSelectCouponList.forEach(coupon -> {
			selectShopIds.addAll(shopIds
					.stream()
					.filter(shopId -> {
						// 排除券
						if (EXCLUDE.getValue().equals(coupon.getUseType())) {
							return !coupon.getShopItems().contains(shopId);
						}
						// 排除券
						return coupon.getShopItems().contains(shopId);
					})
					.collect(Collectors.toList()));
		});
		List<Long> finalSelectShopIds = selectShopIds.stream().distinct().collect(Collectors.toList());

		// 处理剩余优惠券互斥关系
		otherCouponList.forEach(coupon -> {

			// 判断是否包含之前选中的店铺
			long containSelectedShopIdCount = finalSelectShopIds
					.stream()
					.filter(shopId -> {
						if (INCLUDE.getValue().equals(coupon.getUseType())) {
							// 指定券
							return coupon.getShopItems().contains(shopId);
						} else if (GENERAL.getValue().equals(coupon.getUseType())) {
							// 通用劵一定是包含的
							return true;
						} else {
							// 排除券
							return !coupon.getShopItems().contains(shopId);
						}
					}).count();
			// 如果包含，则不做处理
			if (containSelectedShopIdCount > 0) {
				return;
			}

			// 获取当前优惠券商品信息
			List<ShopCouponDTO> currentCouponShopList = shopCouponList
					.stream()
					.filter(shopCoupon -> {
						if (INCLUDE.getValue().equals(coupon.getUseType())) {
							// 指定券
							return coupon.getShopItems().contains(shopCoupon.getShopId());
						} else if (GENERAL.getValue().equals(coupon.getUseType())) {
							return true;
						} else {
							// 排除券
							return !coupon.getShopItems().contains(shopCoupon.getShopId());
						}
					}).collect(Collectors.toList());

			// 如果为空，不处理
			if (CollUtil.isEmpty(currentCouponShopList)) {
				return;
			}

			// 计算店铺促销后的总金额
			BigDecimal shopAmount = currentCouponShopList.stream().map(ShopCouponDTO::getProductCouponAfterAmount).reduce(BigDecimal.ZERO, BigDecimal::add);

			// 如果满足门槛，则设置为可用
			if (shopAmount.compareTo(coupon.getMinPoint()) >= 0) {
				coupon.setSelectStatus(OPTIONAL.getStatus());
			}
		});
	}
}
