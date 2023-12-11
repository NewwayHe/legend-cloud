/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.activity.handle;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import com.legendshop.activity.coupon.CouponExecuteManager;
import com.legendshop.activity.dto.*;
import com.legendshop.activity.enums.CouponSelectStatusEnum;
import com.legendshop.activity.enums.CouponUnavailableTypeEnum;
import com.legendshop.activity.service.CouponProductService;
import com.legendshop.activity.service.CouponShopService;
import com.legendshop.activity.service.convert.CouponConverter;
import com.legendshop.product.dto.ProductItemDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static com.legendshop.activity.enums.CouponUseTypeEnum.*;
import static java.util.stream.Collectors.toList;

/**
 * @author legendshop
 */
@Slf4j
@Component
@AllArgsConstructor
public class CouponHandle {
	private final CouponProductService couponProductService;
	private final CouponShopService couponShopService;
	private final CouponConverter couponConverter;
	private final CouponExecuteManager couponExecuteManager;

	/**
	 * 处理优惠券
	 *
	 * @param couponList    用户领取的优惠券列表
	 * @param shopCouponMap 下单传递的店铺集合
	 * @return
	 */

	/**
	 * 优惠券的组合关系
	 * ----------------------------------------------------------
	 * A.店铺无门槛优惠券只能用一张，其他的商品无门槛或店铺无门槛优惠券都不能用
	 * B.商品无门槛也可以用多张，其他店铺无门槛优惠券都不能用
	 * C.店铺满减券只能用一张，其他的商品满减券都不能用
	 * D.商品满减券可以用多张 ，满足的商品即可，其他的店铺满减券不能用
	 * <p>
	 * AB互斥、CD互斥
	 * AC、AD、BC、BD 叠加
	 * ----------------------------------------------------------
	 */
	public List<CouponItemExtDTO> handleFilter(List<CouponItemExtDTO> couponList, Map<Long, ShopCouponDTO> shopCouponMap) {
		//根据用户下单的店铺id和优惠券的店铺id比较，过滤出shopId相等的，也就是符合条件的优惠券
		List<CouponItemExtDTO> availableCouponList = couponList
				.stream()
				.filter(coupon -> ObjectUtil.isNotNull(shopCouponMap.get(coupon.getShopId())))
				.collect(toList());
		//过滤出有门槛的优惠券
		List<Long> removeIds = new ArrayList<>();
		availableCouponList.forEach(coupon -> {
			if (coupon.getUseType().equals(GENERAL.getValue())) {
				//比较价格满不满足
				CouponUnavailableTypeEnum unavailableTypeEnum = handleFilterGeneralCoupon(coupon, shopCouponMap.get(coupon.getShopId()));
				if (!unavailableTypeEnum.equals(CouponUnavailableTypeEnum.AVAILABLE)) {
					removeIds.add(coupon.getId());

					//不可用优惠劵，设置不可用原因
					ShopCouponDTO shopCouponDTO = shopCouponMap.get(coupon.getShopId());
					CouponItemDTO couponItemDTO = couponConverter.converterCouponItemDTO(coupon);
					couponItemDTO.setUnAvailableReason(unavailableTypeEnum.value());

					if (shopCouponDTO.getUnavailableCouponItems() == null) {
						shopCouponDTO.setUnavailableCouponItems(new ArrayList<>());
					}
					shopCouponDTO.getUnavailableCouponItems().add(couponItemDTO);
				}
			} else {
				CouponUnavailableTypeEnum couponUnavailableTypeEnum = handleFilterUnGeneralCoupon(coupon, shopCouponMap.get(coupon.getShopId()));
				if (!couponUnavailableTypeEnum.equals(CouponUnavailableTypeEnum.AVAILABLE)) {
					removeIds.add(coupon.getId());
					//不可用优惠劵，设置不可用原因
					ShopCouponDTO shopCouponDTO = shopCouponMap.get(coupon.getShopId());
					CouponItemDTO couponItemDTO = couponConverter.converterCouponItemDTO(coupon);
					couponItemDTO.setUnAvailableReason(couponUnavailableTypeEnum.value());

					if (shopCouponDTO.getUnavailableCouponItems() == null) {
						shopCouponDTO.setUnavailableCouponItems(new ArrayList<>());
					}
					shopCouponDTO.getUnavailableCouponItems().add(couponItemDTO);
				}
			}
		});

		return availableCouponList.stream().filter(coupon -> !removeIds.contains(coupon.getId())).collect(toList());

	}


	/**
	 * 处理过滤平台优惠券
	 *
	 * @param platFormCoupons
	 */
	public List<CouponItemExtDTO> handleFilterPlatForm(List<CouponItemExtDTO> platFormCoupons, Map<Long, ShopCouponDTO> shopCouponMap) {
		//过滤出有门槛的优惠券
		List<Long> removeIds = new ArrayList<>();
		platFormCoupons.forEach(coupon -> {
			if (MapUtil.isEmpty(shopCouponMap)) {
				coupon.setUnAvailableReason(CouponUnavailableTypeEnum.DISTRIBUTION.value());
				coupon.setSelectStatus(CouponSelectStatusEnum.UN_AVAILABLE.getStatus());
				return;
			}
			CouponUnavailableTypeEnum couponUnavailableTypeEnum;

			if (coupon.getUseType().equals(GENERAL.getValue())) {
				//比较价格满不满足
				couponUnavailableTypeEnum = handleFilterGeneralCouponFormCoupon(coupon, shopCouponMap);
			} else {
				couponUnavailableTypeEnum = handleFilterUnGeneralPlatFormCoupon(coupon, shopCouponMap);
			}

			if (couponUnavailableTypeEnum.equals(CouponUnavailableTypeEnum.AVAILABLE)) {
				coupon.setSelectStatus(CouponSelectStatusEnum.OPTIONAL.getStatus());
			} else if (couponUnavailableTypeEnum.equals(CouponUnavailableTypeEnum.AFTER_DISCOUNT_MIN_POINT)) {
				coupon.setUnAvailableReason(couponUnavailableTypeEnum.value());
				coupon.setSelectStatus(CouponSelectStatusEnum.UN_OPTIONAL.getStatus());
			} else {
				coupon.setUnAvailableReason(couponUnavailableTypeEnum.value());
				coupon.setSelectStatus(CouponSelectStatusEnum.UN_AVAILABLE.getStatus());
			}
		});

		return platFormCoupons;
	}

	/**
	 * 处理商家优惠券过滤
	 *
	 * @param coupon
	 * @param shopCouponDTO
	 * @return
	 */
	private CouponUnavailableTypeEnum handleFilterGeneralCoupon(CouponDTO coupon, ShopCouponDTO shopCouponDTO) {
		if (coupon.getMinPoint().compareTo(BigDecimal.ZERO) == 0) {
			return CouponUnavailableTypeEnum.AVAILABLE;
		}
		//只有一个
		List<ProductItemDTO> productItems = shopCouponDTO.getProductItems();
		// 计算门槛应该用 商品促销优惠后的价格 * 商品数量 得出的商品优惠后总价格去计算
		BigDecimal totalAmount = productItems.stream().map(item -> NumberUtil.mul(item.getPrice(), item.getTotalCount())).reduce(BigDecimal.ZERO, BigDecimal::add);
		if (totalAmount.compareTo(coupon.getMinPoint()) < 0) {
			return CouponUnavailableTypeEnum.MIN_POINT;
		}
		return CouponUnavailableTypeEnum.AVAILABLE;
	}

	/**
	 * 处理平台通用券
	 *
	 * @param coupon
	 * @param shopCouponMap
	 * @return
	 */
	private CouponUnavailableTypeEnum handleFilterGeneralCouponFormCoupon(CouponItemExtDTO coupon, Map<Long, ShopCouponDTO> shopCouponMap) {
		// 没有门槛，直接可用
		if (coupon.getMinPoint().compareTo(BigDecimal.ZERO) == 0) {
			return CouponUnavailableTypeEnum.AVAILABLE;
		}
		List<ShopCouponDTO> collect = new ArrayList<>(shopCouponMap.values());
		// 商品总金额不满足最低使用门槛
		BigDecimal totalAmount = collect.stream().map(ShopCouponDTO::getProductTotalAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
		if (totalAmount.compareTo(coupon.getMinPoint()) < 0) {
			return CouponUnavailableTypeEnum.MIN_POINT;
		}
		// 商品折扣后金额不满足最低使用门槛
		totalAmount = collect.stream().map(ShopCouponDTO::getProductCouponAfterAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
		if (totalAmount.compareTo(coupon.getMinPoint()) < 0) {
			return CouponUnavailableTypeEnum.AFTER_DISCOUNT_MIN_POINT;
		}
		return CouponUnavailableTypeEnum.AVAILABLE;
	}


	/**
	 * 处理平台其他优惠券
	 *
	 * @param coupon
	 * @param shopCouponMap
	 * @return
	 */
	private CouponUnavailableTypeEnum handleFilterUnGeneralPlatFormCoupon(CouponItemExtDTO coupon, Map<Long, ShopCouponDTO> shopCouponMap) {
		//获取包含的商家集合
		List<CouponShopDTO> includeShopList = couponShopService.getCouponShopByCouponId(coupon.getId());
		coupon.setShopItems(includeShopList
				.stream()
				.map(CouponShopDTO::getShopId)
				.collect(Collectors.toList()));
		//获取符合的商家
		List<ShopCouponDTO> availableShopCouponList = shopCouponMap
				.values().stream()
				.filter(shopCoupon -> includeShopList.stream().anyMatch(couponShopDTO -> {
					if (coupon.getUseType().equals(INCLUDE.getValue())) {
						return shopCoupon.getShopId().equals(couponShopDTO.getShopId());
					} else {
						return !shopCoupon.getShopId().equals(couponShopDTO.getShopId());
					}
				})).collect(toList());
		if (CollUtil.isEmpty(availableShopCouponList)) {
			return CouponUnavailableTypeEnum.PRODUCT;
		}
		if (coupon.getMinPoint().compareTo(BigDecimal.ZERO) == 0) {
			return CouponUnavailableTypeEnum.AVAILABLE;
		}
		BigDecimal totalAmount = availableShopCouponList.stream().map(ShopCouponDTO::getProductTotalAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
		if (totalAmount.compareTo(coupon.getMinPoint()) < 0) {
			return CouponUnavailableTypeEnum.MIN_POINT;
		}
		totalAmount = availableShopCouponList.stream().map(ShopCouponDTO::getProductCouponAfterAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
		if (totalAmount.compareTo(coupon.getMinPoint()) < 0) {
			return CouponUnavailableTypeEnum.AFTER_DISCOUNT_MIN_POINT;
		}
		return CouponUnavailableTypeEnum.AVAILABLE;
	}

	/**
	 * 处理非通用有门槛券
	 * 商品包含券和排除券
	 *
	 * @param coupon
	 * @param shopCoupon
	 * @return
	 */
	private CouponUnavailableTypeEnum handleFilterUnGeneralCoupon(CouponDTO coupon, ShopCouponDTO shopCoupon) {
		//获取包含的商品集合
		List<CouponProductDTO> includeProductList = couponProductService.queryByCouponId(coupon.getId());
		List<ProductItemDTO> availableProductItemList = new ArrayList<>();
		if (coupon.getUseType().equals(INCLUDE.getValue())) {
			//包含券获取符合的商品
			List<ProductItemDTO> includeProductItemList = shopCoupon
					.getProductItems().stream()
					.filter(productItemDTO -> includeProductList.stream().anyMatch(
							couponProduct -> productItemDTO.getSkuId().equals(couponProduct.getSkuId()))).collect(toList());
			if (CollUtil.isEmpty(includeProductItemList)) {
				return CouponUnavailableTypeEnum.PRODUCT;
			}
			availableProductItemList = includeProductItemList;
		} else {
			//排除券获取可用的商品
			List<ProductItemDTO> excludeProductItemList = shopCoupon
					.getProductItems().stream()
					.filter(productItemDTO -> includeProductList.stream().anyMatch(
							couponProduct -> productItemDTO.getSkuId().equals(couponProduct.getSkuId()))).collect(toList());
			if (CollUtil.isNotEmpty(excludeProductItemList)) {
				return CouponUnavailableTypeEnum.PRODUCT;
			}
			availableProductItemList.addAll(shopCoupon.getProductItems());
		}


		if (CollUtil.isEmpty(availableProductItemList)) {
			return CouponUnavailableTypeEnum.PRODUCT;
		}
		if (coupon.getMinPoint().compareTo(BigDecimal.ZERO) == 0) {
			return CouponUnavailableTypeEnum.AVAILABLE;
		}
		BigDecimal totalAmount = availableProductItemList.stream().map(item -> NumberUtil.mul(item.getDiscountedPrice(), item.getTotalCount())).reduce(BigDecimal.ZERO, BigDecimal::add);
		if (totalAmount.compareTo(coupon.getMinPoint()) < 0) {
			return CouponUnavailableTypeEnum.MIN_POINT;
		}
		return CouponUnavailableTypeEnum.AVAILABLE;
	}

	/**
	 * 处理最优组合
	 *
	 * @param availableCouponList
	 * @param shopCouponMap
	 */
	public Map<Long, ShopCouponDTO> handlerShopBest(List<CouponItemExtDTO> availableCouponList, Map<Long, ShopCouponDTO> shopCouponMap) {

		//按shopId分组区分可用的优惠券
		Map<Long, List<CouponItemExtDTO>> groupShopIdCouponMap = availableCouponList.stream().collect(Collectors.groupingBy(CouponItemExtDTO::getShopId));
		groupShopIdCouponMap.forEach((shopId, couponList) -> {
			ShopCouponDTO shopCoupon = shopCouponMap.get(shopId);
			List<CouponItemExtDTO> optimalSelect = calcOptimalSelect(couponList, shopCoupon.getProductItems());


			//可用优惠券里面设置选中的最优优惠券
			List<CouponItemDTO> availableCouponItem = couponConverter.converterCouponItemFromExtDTO(couponList);

			//排除默认选中的优惠券
			List<CouponItemDTO> selectCouponItem = availableCouponItem.stream().filter(itemDTO ->
					optimalSelect.stream().anyMatch(optimalDTO -> itemDTO.getCouponId().equals(optimalDTO.getId())))
					.collect(toList());
			//设置选中
			for (CouponItemDTO item : selectCouponItem) {
				item.setSelectStatus(CouponSelectStatusEnum.SELECTED.getStatus());
			}
			//设置可用优惠券
			shopCoupon.setCouponItems(availableCouponItem);
		});
		return shopCouponMap;
	}

	/**
	 * 计算最优选择
	 *
	 * @param availableCouponList
	 * @param productItems
	 * @return
	 */
	private List<CouponItemExtDTO> calcOptimalSelect(List<CouponItemExtDTO> availableCouponList, List<ProductItemDTO> productItems) {
		//-----------------------------店铺通用券处理start-----------------------------
		//店铺通用优惠券的集合
		List<CouponItemExtDTO> shopCouponList = availableCouponList.stream().filter(c -> c.getUseType().equals(GENERAL.getValue())).collect(toList());
		BigDecimal shopCouponAmount = BigDecimal.ZERO;
		// 返回最优的优惠卷
		List<CouponItemExtDTO> optimalCouponItemList = new ArrayList<>();
		//可用优惠券查出商品券
		List<CouponItemExtDTO> productSelect = availableCouponList.stream().filter(item -> !item.getUseType().equals(GENERAL.getValue())).collect(toList());
		//设置商品券的商品
		productSelect.forEach(productSelectItem -> {
			List<ProductItemDTO> productItemDTOS = couponProductService.queryInfoByCouponId(productSelectItem.getId());
			productSelectItem.setProductItems(productItemDTOS);
		});
		// 获取全场优惠金额最大的券
		Optional<CouponItemExtDTO> shopCouponItemDTO = shopCouponList.stream().max(Comparator.comparing(CouponItemExtDTO::getAmount));
		if (shopCouponItemDTO.isPresent()) {
			shopCouponAmount = shopCouponItemDTO.get().getAmount();
		}
		//-----------------------------店铺通用券处理end-----------------------------

		//-----------------------------商品券处理start-----------------------------
		//获取所有的商品优惠券
		List<CouponItemExtDTO> productCouponList = availableCouponList.stream().filter(c -> c.getUseType().compareTo(GENERAL.getValue()) != 0).collect(toList());

		// 计算商品优惠卷
		productCouponList = optimalProductCouponList(productCouponList, productItems, new ArrayList<>(productItems.size()));
		BigDecimal prodCouponAmount = productCouponList.stream().map(CouponItemExtDTO::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);

		//-----------------------------商品券处理end-----------------------------

		// 商品有且只有一张卷可以使用
		if (shopCouponAmount.compareTo(prodCouponAmount) > 0) {
			optimalCouponItemList.add(shopCouponItemDTO.get());
		} else {
			optimalCouponItemList.addAll(productCouponList);
		}


		return optimalCouponItemList;
	}

	/**
	 * 计算商品最优商品优惠券
	 *
	 * @param couponList         the 可选优惠卷
	 * @param productItems       the 订单商品
	 * @param selectProdItemList the 已选优惠卷的商品
	 * @return 最佳优惠卷
	 */
	private List<CouponItemExtDTO> optimalProductCouponList(List<CouponItemExtDTO> couponList, List<ProductItemDTO> productItems, List<ProductItemDTO> selectProdItemList) {
		// 入参为空，直接返回
		if (CollectionUtils.isEmpty(couponList) || CollectionUtils.isEmpty(productItems)) {
			return couponList;
		}
		// 定义最佳返回值
		List<CouponItemExtDTO> selectCoupons = new ArrayList<>(couponList.size());
		BigDecimal optimalAmount = BigDecimal.ZERO;
		//循环优惠券，匹配最优优惠券
		for (CouponItemExtDTO coupon : couponList) {

			// 设置临时组合排列
			List<CouponItemExtDTO> tempResult = new ArrayList<>(couponList.size());
			//获取sku
			List<Long> skuIds = coupon.getProductItems().stream().map(ProductItemDTO::getSkuId).collect(toList());
			// 初始化优惠卷状态数组
			List<ProductItemDTO> notSelectProdList = new ArrayList<>(productItems);
			List<CouponItemExtDTO> notSelectCouponList = new ArrayList<>(couponList);

			// 判断是否包含之前选中的店铺
			long containSelectedShopIdCount = selectProdItemList
					.stream()
					.filter(pi -> {
						if (INCLUDE.getValue().equals(coupon.getUseType())) {
							// 指定券
							return skuIds.contains(pi.getSkuId());
						} else {
							// 排除券
							return !skuIds.contains(pi.getSkuId());
						}
					}).count();

			List<ProductItemDTO> selectProdList = Collections.emptyList();
			// 判断是否有商品被其他优惠卷选中，
			if (containSelectedShopIdCount == 0) {
				selectProdList = productItems
						.stream()
						.filter(pi -> {
							if (coupon.getUseType().equals(EXCLUDE.getValue())) {
								return !skuIds.contains(pi.getSkuId());
							} else {
								return skuIds.contains(pi.getSkuId());
							}
						}).collect(toList());
			}

			notSelectProdList.removeAll(selectProdList);
			selectProdItemList.addAll(selectProdList);
			notSelectCouponList.remove(coupon);
			if (!CollectionUtils.isEmpty(notSelectCouponList) && !CollectionUtils.isEmpty(notSelectProdList)) {
				List<CouponItemExtDTO> recursionCouponItems = optimalProductCouponList(notSelectCouponList, notSelectProdList, selectProdItemList);
				tempResult.addAll(recursionCouponItems);
			}
			tempResult.add(coupon);
			if (CollectionUtils.isEmpty(selectProdList)) {
				tempResult.remove(coupon);
			} else {
				//同级选中商品，使用完需要变回未选中商品，否则会影响同级的使用判断，
				//同级的选中都是从上级给到，轮询同级时，到下一个，也是同前一个是同等条件判断。
				selectProdItemList.removeAll(selectProdList);
			}

			BigDecimal tempOptimalAmount = tempResult.stream().map(CouponItemExtDTO::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
			//同一层级比价，如果比上一个选择更优惠就使用上一个，如果新的更优惠则更校招选择项。
			if (optimalAmount.compareTo(tempOptimalAmount) < 0) {
				optimalAmount = tempOptimalAmount;
				selectCoupons = tempResult;
			}
		}
		return selectCoupons;
	}


	/**
	 * 处理用户选着优惠券
	 *
	 * @param shopCouponDTO
	 */
	public ShopCouponDTO handleSelectCoupons(ShopCouponDTO shopCouponDTO) {
		return couponExecuteManager.handleSelectCoupons(shopCouponDTO);

	}


	/**
	 * 处理优惠券的分摊
	 *
	 * @param shopCouponDTO
	 */
	public ShopCouponDTO handlerShopCouponsShard(ShopCouponDTO shopCouponDTO) {
		return couponExecuteManager.handlerShopCouponsShard(shopCouponDTO);
	}


	/**
	 * 处理平台最优优惠券
	 *
	 * @param availableCouponList 可用优惠券
	 * @param shopCouponMap       店铺信息
	 * @return
	 */
	public List<CouponItemDTO> handlerPlatFormBest(List<CouponItemExtDTO> availableCouponList, Map<Long, ShopCouponDTO> shopCouponMap) {
		// 获取下单相关店铺ID
		List<Long> shopIds = shopCouponMap.values().stream().map(ShopCouponDTO::getShopId).collect(toList());

		// 获取最优选择
		//从可选部份选择

		List<CouponItemExtDTO> list = availableCouponList.stream()
				.filter(u -> !u.getSelectStatus().equals(CouponSelectStatusEnum.UN_OPTIONAL.getStatus())
						&& !u.getSelectStatus().equals(CouponSelectStatusEnum.UN_AVAILABLE.getStatus()))
				.collect(toList());

		List<CouponItemExtDTO> optimalSelectList = calcPlatformOptimalSelect(list, shopIds);
		List<Long> userCouponIds = optimalSelectList.stream().map(CouponItemExtDTO::getUserCouponId).collect(toList());
		//可用优惠券里面设置选中的最优优惠券
		List<CouponItemDTO> availableCouponItem = couponConverter.converterCouponItemFromExtDTO(availableCouponList);
		// 将最优选择设置为选中
		availableCouponItem.forEach(couponItem -> {
			if (userCouponIds.contains(couponItem.getUserCouponId())) {
				couponItem.setSelectStatus(CouponSelectStatusEnum.SELECTED.getStatus());
			} else {
				if (!CouponSelectStatusEnum.UN_AVAILABLE.getStatus().equals(couponItem.getSelectStatus())) {
					couponItem.setSelectStatus(CouponSelectStatusEnum.UN_OPTIONAL.getStatus());
				}
			}
		});
		return availableCouponItem;
	}

	/**
	 * 计算最优选择
	 *
	 * @param availableCouponList 可用优惠券
	 * @param shopIds             店铺ID
	 * @return
	 */
	private List<CouponItemExtDTO> calcPlatformOptimalSelect(List<CouponItemExtDTO> availableCouponList, List<Long> shopIds) {
		// start 开始处理平台通用优惠券
		List<CouponItemExtDTO> generalCouponList = availableCouponList.stream().filter(c -> c.getUseType().equals(GENERAL.getValue())).collect(toList());
		// 获取最优平台通用优惠券
		Optional<CouponItemExtDTO> optional = generalCouponList.stream().max(Comparator.comparing(CouponItemExtDTO::getAmount));
		BigDecimal platformAmount = BigDecimal.ZERO;
		if (optional.isPresent()) {
			platformAmount = optional.get().getAmount();
		}
		// end 平台通用优惠券处理完成


		// start 开始处理平台店铺优惠券
		List<CouponItemExtDTO> platformShopCouponList = availableCouponList.stream().filter(c -> !c.getUseType().equals(GENERAL.getValue())).collect(toList());
		// 计算平台最优解
		platformShopCouponList = optimalPlatformShopCouponList(platformShopCouponList, shopIds, new ArrayList<>(shopIds.size()));
		// 获取店铺券金额
		BigDecimal platformShopAmount = platformShopCouponList.stream().map(CouponItemExtDTO::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
		// end 平台店铺券处理完成


		// 返回最优的优惠卷
		List<CouponItemExtDTO> optimalCouponItemList = new ArrayList<>();
		if (platformAmount.compareTo(platformShopAmount) > 0) {
			optimalCouponItemList.add(optional.get());
		} else {
			optimalCouponItemList.addAll(platformShopCouponList);
		}
		return optimalCouponItemList;
	}

	/**
	 * 计算最优平台店铺优惠券
	 *
	 * @param couponList      平台店铺优惠券
	 * @param optionalShopIds 可选的店铺
	 * @param selectedShopIds 已选的店铺
	 * @return
	 */
	private List<CouponItemExtDTO> optimalPlatformShopCouponList(List<CouponItemExtDTO> couponList, List<Long> optionalShopIds, List<Long> selectedShopIds) {
		if (CollUtil.isEmpty(couponList) || CollUtil.isEmpty(optionalShopIds)) {
			return couponList;
		}

		// 定义最佳返回值
		List<CouponItemExtDTO> result = new ArrayList<>(couponList.size());
		BigDecimal optimalAmount = BigDecimal.ZERO;

		// 循环递归，获取每个平台券最优选择
		for (CouponItemExtDTO couponDTO : couponList) {
			// 获取未选店铺
			List<Long> unSelectShopIds = new ArrayList<>(optionalShopIds);

			// 获取已选店铺
			List<Long> selectShopIds = new ArrayList<>(selectedShopIds);

			// 获取未选优惠券
			List<CouponItemExtDTO> unSelectCouponList = new ArrayList<>(couponList);

			// 设置临时结果
			List<CouponItemExtDTO> tempResult = new ArrayList<>();

			// 判断是否包含之前选中的店铺
			List<Long> containSelectedShopIds = selectShopIds
					.stream()
					.filter(shopId -> {
						if (INCLUDE.getValue().equals(couponDTO.getUseType())) {
							// 指定券
							return couponDTO.getShopItems().contains(shopId);
						} else {
							// 排除券
							return !couponDTO.getShopItems().contains(shopId);
						}
					})
					.collect(toList());

			// 获取当前优惠券可用店铺
			List<Long> currentSelectShopIds = Collections.emptyList();
			if (CollUtil.isEmpty(containSelectedShopIds)) {
				currentSelectShopIds = unSelectShopIds
						.stream()
						.filter(shopId -> {
							if (INCLUDE.getValue().equals(couponDTO.getUseType())) {
								// 指定券
								return couponDTO.getShopItems().contains(shopId);
							} else {
								// 排除券
								return !couponDTO.getShopItems().contains(shopId);
							}
						}).collect(toList());
			}
			// 将当前优惠券涉及的内容更新
			unSelectShopIds.removeAll(currentSelectShopIds);
			selectShopIds.addAll(currentSelectShopIds);
			unSelectCouponList.remove(couponDTO);

			// 如果未选优惠券和未选店铺不为空，则递归获取最优解
			if (CollUtil.isNotEmpty(unSelectCouponList) && CollUtil.isNotEmpty(unSelectShopIds)) {
				List<CouponItemExtDTO> couponDtoList = optimalPlatformShopCouponList(unSelectCouponList, unSelectShopIds, selectShopIds);
				tempResult.addAll(couponDtoList);
			}

			// 如果当前优惠券有可用店铺，则加入结果里
			if (CollUtil.isNotEmpty(currentSelectShopIds)) {
				tempResult.add(couponDTO);
			}

			// 如果当前的组合优惠金额更大，则以当前组合为主
			BigDecimal tempAmount = tempResult.stream().map(CouponItemExtDTO::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
			if (optimalAmount.compareTo(tempAmount) < 0) {
				optimalAmount = tempAmount;
				result = tempResult;
			}
		}
		return result;
	}

	/**
	 * 处理用户选择的平台优惠券
	 */
	public List<CouponItemDTO> handleSelectPlatformCoupons(SelectPlatformCouponDTO selectPlatformCouponDTO) {
		return couponExecuteManager.handleSelectPlatformCoupons(selectPlatformCouponDTO);
	}
}
