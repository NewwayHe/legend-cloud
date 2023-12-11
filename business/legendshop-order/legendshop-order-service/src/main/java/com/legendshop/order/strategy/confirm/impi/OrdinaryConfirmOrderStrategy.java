/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.strategy.confirm.impi;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.legendshop.activity.dto.CouponItemDTO;
import com.legendshop.activity.dto.ShopCouponDTO;
import com.legendshop.activity.enums.CouponSelectStatusEnum;
import com.legendshop.common.core.constant.CacheConstants;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.expetion.BusinessException;
import com.legendshop.order.bo.ConfirmOrderBO;
import com.legendshop.order.dto.*;
import com.legendshop.order.strategy.confirm.ConfirmOrderStrategy;
import com.legendshop.order.util.CartUtil;
import com.legendshop.product.bo.SkuBO;
import com.legendshop.product.dto.ProductDTO;
import com.legendshop.product.enums.ProductStatusEnum;
import com.legendshop.shop.dto.ShopDetailDTO;
import com.legendshop.shop.enums.ShopDetailStatusEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static com.legendshop.activity.enums.CouponSelectStatusEnum.SELECTED;

/**
 * 普通订单确认策略
 *
 * @author legendshop
 */
@Slf4j
@Service
public class OrdinaryConfirmOrderStrategy extends BaseConfirmOrderStrategy implements ConfirmOrderStrategy {

	/**
	 * 普通订单支持的最大下单数
	 */
	private static final int MAX_COUNT = 30;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public R<ConfirmOrderBO> check(ConfirmOrderDTO confirmOrderDTO) {
		log.info("进入普通订单下单检查以及组装商品信息策略, params: {}", JSONUtil.toJsonStr(confirmOrderDTO));
		return super.check(confirmOrderDTO);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public R<ConfirmOrderBO> confirm(ConfirmOrderBO confirmOrderBo) {
		log.info("进入普通订单确认策略, params: {}", JSONUtil.toJsonStr(confirmOrderBo));
		return super.confirm(confirmOrderBo);
	}


	@Override
	protected R<List<SubmitOrderShopDTO>> checkAndAssemblyConfirmOrderProcess(ConfirmOrderDTO confirmOrderDTO) {
		log.info("###### 开始普通订单 下单检查以及商品组装 ##### ");

		// 是否立即购买
		Boolean buyNowFlag = confirmOrderDTO.getBuyNowFlag();
		List<ConfirmOrderItemDTO> confirmOrderItemDtoList = confirmOrderDTO.getConfirmOrderItemDTOList();
		if (CollectionUtils.isEmpty(confirmOrderItemDtoList)) {
			return R.fail("请先选择商品进行结算");
		}

		if (confirmOrderItemDtoList.size() > MAX_COUNT) {
			return R.fail("仅支持最多30个商品下单！！！");
		}


		// 按店铺分组
		List<SubmitOrderShopDTO> shopDtoList = new ArrayList<>();
		// 店铺商品列表
		List<SubmitOrderSkuDTO> skuDtoList = new ArrayList<>();

		// 批量获取sku信息
		R<List<SkuBO>> skuBoResult = skuApi.queryBOBySkuIds(confirmOrderItemDtoList.stream().map(ConfirmOrderItemDTO::getSkuId).distinct().collect(Collectors.toList()));
		List<SkuBO> skuBOList = Optional.ofNullable(skuBoResult.getData()).orElse(Collections.emptyList());
		Map<Long, SkuBO> skuBoMap = skuBOList.stream().collect(Collectors.toMap(SkuBO::getId, e -> e));

		// 批量获取spu信息
		R<List<ProductDTO>> productResult = productApi.queryAllByIds(skuBOList.stream().map(SkuBO::getProductId).distinct().collect(Collectors.toList()));
		List<ProductDTO> productList = Optional.ofNullable(productResult.getData()).orElse(Collections.emptyList());
		Map<Long, ProductDTO> productMap = productList.stream().collect(Collectors.toMap(ProductDTO::getId, e -> e));

		// 批量获取店铺信息
		R<List<ShopDetailDTO>> shopDetailResult = shopDetailApi.queryByIds(productList.stream().map(ProductDTO::getShopId).collect(Collectors.toList()));
		Map<Long, ShopDetailDTO> shopDetailMap = Optional.ofNullable(shopDetailResult.getData()).orElse(Collections.emptyList()).stream().collect(Collectors.toMap(ShopDetailDTO::getId, e -> e));
		for (ConfirmOrderItemDTO confirmOrderItem : confirmOrderItemDtoList) {

			// 结算校验
			SkuBO sku = skuBoMap.get(confirmOrderItem.getSkuId());
			if (ObjectUtil.isNull(sku)) {
				return R.fail(buyNowFlag ? "商品规格不存在或已被修改，请刷新后重试" : "部分商品规格不存在或已被修改，请刷新后重试");
			}
			if (ObjectUtil.isNull(sku.getStocks()) || sku.getStocks() <= 0) {
				return R.fail(buyNowFlag ? "商品规格库存不足，请刷新后重试" : "部分商品规格库存不足，请刷新后重试");
			}
			if (ObjectUtil.isNull(confirmOrderItem.getCount()) || confirmOrderItem.getCount() <= 0) {
				return R.fail(buyNowFlag ? "商品购买数量异常，请刷新后重试" : "部分商品购买数量异常，请刷新后重试");
			}
			if (sku.getStocks() < confirmOrderItem.getCount()) {
				return R.fail(buyNowFlag ? "商品数量已超过现有库存，请刷新后重试" : "部分商品数量已超过现有库存，请刷新后重试");
			}
			ProductDTO product = productMap.get(sku.getProductId());

			//限购判断
			R checkQuota = super.checkQuotaOrder(product, confirmOrderDTO);
			if (!checkQuota.success()) {
				return checkQuota;
			}
			if (ObjectUtil.isNull(product)) {
				return R.fail(buyNowFlag ? "商品不存在或已被修改，请刷新后重试" : "部分商品不存在或已被修改，请刷新后重试");
			}
			if (!ProductStatusEnum.PROD_ONLINE.value().equals(product.getStatus())) {
				return R.fail(buyNowFlag ? "商品状态异常，请刷新后重试" : "部分商品状态异常，请刷新后重试");
			}
			if (product.getPreSellFlag()) {
				return R.fail("暂不支持不同订单类型下单");
			}
			ShopDetailDTO shopDetailDTO = shopDetailMap.get(product.getShopId());
			if (ObjectUtil.isNull(shopDetailDTO)) {
				return R.fail(buyNowFlag ? "店铺不存在，请刷新后重试" : "部分店铺不存在，请刷新后重试");
			}
			if (!ShopDetailStatusEnum.ONLINE.getStatus().equals(shopDetailDTO.getStatus())) {
				return R.fail(buyNowFlag ? "店铺状态异常，请刷新后重试" : "部分店铺状态异常，请刷新后重试");
			}


			SubmitOrderSkuDTO skuDTO = new SubmitOrderSkuDTO();
			skuDTO.setSkuId(sku.getId());
			skuDTO.setCnProperties(sku.getCnProperties());
			skuDTO.setTotalCount(confirmOrderItem.getCount());
			skuDTO.setOriginalPrice(sku.getOriginalPrice());
			skuDTO.setPrice(sku.getPrice());
			skuDTO.setCostPrice(sku.getCostPrice());
			skuDTO.setWeight(sku.getWeight());
			skuDTO.setVolume(sku.getVolume());
			skuDTO.setProductId(product.getId());
			skuDTO.setProductName(product.getName());
			skuDTO.setPic(StrUtil.isNotEmpty(sku.getPic()) ? sku.getPic() : product.getPic());
			skuDTO.setShopId(shopDetailDTO.getId());
			skuDTO.setTransId(product.getTransId());
			skuDTO.setStatusFlag(true);
			skuDTO.setStockCounting(product.getStockCounting());
			skuDTO.setActualAmount(NumberUtil.mul(skuDTO.getPrice(), confirmOrderItem.getCount()));
			skuDTO.setDeliveryType(product.getDeliveryType());
			skuDtoList.add(skuDTO);
			// 组装店铺信息
			SubmitOrderShopDTO shopDTO = new SubmitOrderShopDTO();
			shopDTO.setShopId(shopDetailDTO.getId());
			shopDTO.setShopName(shopDetailDTO.getShopName());
			shopDtoList.add(shopDTO);
		}
		// 将商品列表归入所属店铺,并计算店铺商品总金额
		List<SubmitOrderShopDTO> shopList = shopDtoList.stream().distinct().collect(Collectors.toList());
		shopList.forEach(shop -> {
			List<SubmitOrderSkuDTO> orderSkuList = skuDtoList.stream().filter(sku -> sku.getShopId().equals(shop.getShopId())).collect(Collectors.toList());
			shop.setSkuList(orderSkuList);
		});

		// 如果从购物车上过来，则还需要带上之前选中的满减活动ID
		if (!buyNowFlag) {
			List<CartItemDTO> cartList = cartHandler.get(CartUtil.getBeanName(confirmOrderDTO.getUserId())).queryCartItems(confirmOrderDTO.getUserId());
			List<CartChangePromotionDTO> list = cacheManagerUtil.getCache(CacheConstants.CART_PROMOTION_ITEMS, confirmOrderDTO.getUserId());
			//没有缓存，初始化用户商品项促销活动信息
			if (CollUtil.isNotEmpty(list)) {
				cartList.forEach(u -> {
					Optional<CartChangePromotionDTO> item = list.stream().filter(i -> i.getSkuId().equals(u.getSkuId())).findFirst();
					item.ifPresent(cartChangePromotionDTO -> u.setMarketingId(cartChangePromotionDTO.getMarketingId()));
				});
			}
			Map<Long, CartItemDTO> cartMap = cartList.stream().collect(Collectors.toMap(CartItemDTO::getSkuId, e -> e));
			// Collectors.toMap 方法不允许value为null
			for (SubmitOrderSkuDTO submitOrderSkuDTO : skuDtoList) {
				submitOrderSkuDTO.setRewardMarketingId(Optional.ofNullable(cartMap.get(submitOrderSkuDTO.getSkuId())).map(CartItemDTO::getMarketingId).orElse(null));
				submitOrderSkuDTO.setLimitDiscountsMarketingId(Optional.ofNullable(cartMap.get(submitOrderSkuDTO.getSkuId())).map(CartItemDTO::getLimitDiscountsMarketingId).orElse(null));
			}
		}
		return R.ok(shopList);
	}


	@Override
	protected R<ConfirmOrderBO> getShopBestCoupons(ConfirmOrderBO confirmOrderBo) {
		log.info("###### 开始处理商品商家最优优惠券 ##### ");
		List<SubmitOrderShopDTO> shopOrderList = confirmOrderBo.getShopOrderList();
		Map<Long, ShopCouponDTO> shopCouponMap = new HashMap<>(shopOrderList.size());
		shopOrderList.forEach(submitOrderShopDTO -> {
			Long shopId = submitOrderShopDTO.getShopId();
			List<SubmitOrderSkuDTO> skuList = submitOrderShopDTO.getSkuList();
			shopCouponMap.put(shopId, new ShopCouponDTO(shopId, orderConverter.convertProductItemDTO(skuList)));
		});
		R<Map<Long, ShopCouponDTO>> mapR = couponApi.getShopBestCoupons(confirmOrderBo.getUserId(), shopCouponMap);
		if (!mapR.getSuccess()) {
			throw new BusinessException(mapR.getMsg());
		}
		Map<Long, ShopCouponDTO> shopBestCoupons = mapR.getData();
		if (CollUtil.isEmpty(shopBestCoupons)) {
			log.info("###### 当前用户订单商品没有商家优惠券处理 ##### ");
			return R.ok(confirmOrderBo);
		}
		// 更新预订单缓存
		shopOrderList.forEach(submitOrderShopDTO -> {
			ShopCouponDTO shopCouponDTO = shopBestCoupons.get(submitOrderShopDTO.getShopId());
			if (ObjectUtil.isNotEmpty(shopCouponDTO)) {
				submitOrderShopDTO.setShopCouponDTO(shopCouponDTO);
				//商家优惠券分摊计算
				couponUtil.shopCouponsShardCalculation(submitOrderShopDTO);

			}

		});
		log.info("###### 结束处理下单商家最优优惠券组合 ##### ");


		return R.ok(confirmOrderBo);
	}

	@Override
	protected R<ConfirmOrderBO> getPlatformCoupons(ConfirmOrderBO confirmOrderBO) {
		log.info("###### 开始处理平台优惠券 ##### ");
		Map<Long, ShopCouponDTO> shopCouponMap = couponUtil.buildPlatformCouponMap(confirmOrderBO.getShopOrderList());
		if (CollUtil.isEmpty(shopCouponMap)) {
			log.info("###### 该商品没有平台优惠券处理 ##### ");
			return R.ok(confirmOrderBO);
		}
		R<List<CouponItemDTO>> r = couponApi.getBestPlatFormCoupons(confirmOrderBO.getUserId(), shopCouponMap);
		if (!r.getSuccess()) {
			throw new BusinessException(r.getMsg());
		}
		List<CouponItemDTO> platFormCoupons = r.getData();
		if (CollUtil.isEmpty(platFormCoupons)) {
			log.info("###### 该用户没有平台优惠券处理 ##### ");
			return R.ok(confirmOrderBO);
		}

		confirmOrderBO.setPlatformCoupons(platFormCoupons.stream().
				filter(u -> !u.getSelectStatus().equals(CouponSelectStatusEnum.UN_AVAILABLE.getStatus())).collect(Collectors.toList()));

		// 将不可用标记为不可选，因为前端现在没空加上对不可用的优惠券的页面处理
		List<CouponItemDTO> platformUnAvailableList = platFormCoupons.stream().filter(u -> u.getSelectStatus().equals(CouponSelectStatusEnum.UN_AVAILABLE.getStatus())).collect(Collectors.toList());
		platformUnAvailableList.forEach(coupon -> coupon.setSelectStatus(CouponSelectStatusEnum.UN_OPTIONAL.getStatus()));
		confirmOrderBO.setPlatformUnAvailableCouponList(platformUnAvailableList);

		//获取选中的抵扣金额，设置进去
		List<CouponItemDTO> selectCoupon = platFormCoupons.stream().filter(couponItemDTO -> couponItemDTO.getSelectStatus().equals(SELECTED.getStatus())).collect(Collectors.toList());
		if (ObjectUtil.isNull(selectCoupon)) {
			log.info("###### 结束处理下单平台优惠券 ##### ");
			confirmOrderBO.setPlatformAmount(BigDecimal.ZERO);
			confirmOrderBO.setUsePlatformCouponCount(0);
			return R.ok(confirmOrderBO);
		}
		//处理平台优惠券分摊
		couponUtil.platformCouponsShardCalculation(confirmOrderBO, shopCouponMap);
		log.info("###### 结束处理下单平台优惠券 ##### ");


		return R.ok(confirmOrderBO);
	}

	@Override
	public R<ConfirmOrderBO> handleSpecificBusiness(ConfirmOrderBO confirmOrderBO) {
		return R.ok(confirmOrderBO);
	}

}
