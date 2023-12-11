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
import com.legendshop.product.api.PreSellProductApi;
import com.legendshop.product.bo.SkuBO;
import com.legendshop.product.dto.PreSellProductDTO;
import com.legendshop.product.dto.ProductDTO;
import com.legendshop.product.enums.PreSellPayType;
import com.legendshop.product.enums.ProductStatusEnum;
import com.legendshop.shop.dto.ShopDetailDTO;
import com.legendshop.shop.enums.ShopDetailStatusEnum;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

import static com.legendshop.activity.enums.CouponSelectStatusEnum.SELECTED;

/**
 * 预售订单确认策略
 *
 * @author legendshop
 */
@Slf4j
@Service
public class PreSaleConfirmOrderStrategy extends BaseConfirmOrderStrategy implements ConfirmOrderStrategy {

	@Resource
	private PreSellProductApi preSellProductApi;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public R<ConfirmOrderBO> check(ConfirmOrderDTO confirmOrderDTO) {
		log.info("进入预售订单下单检查以及组装商品信息策略, params: {}", JSONUtil.toJsonStr(confirmOrderDTO));
		return super.check(confirmOrderDTO);
	}


	@Override
	@Transactional(rollbackFor = Exception.class)
	public R<ConfirmOrderBO> confirm(ConfirmOrderBO confirmOrderBo) {
		log.info("进入预售订单确认策略, params: {}", JSONUtil.toJsonStr(confirmOrderBo));
		return super.confirm(confirmOrderBo);
	}


	@Override
	protected R<List<SubmitOrderShopDTO>> checkAndAssemblyConfirmOrderProcess(ConfirmOrderDTO confirmOrderDTO) {
		log.info("###### 开始预售订单 下单检查以及商品组装 ##### ");

		// 是否立即购买
		Boolean buyNowFlag = confirmOrderDTO.getBuyNowFlag();
		List<ConfirmOrderItemDTO> confirmOrderItemDtoList = confirmOrderDTO.getConfirmOrderItemDTOList();
		if (CollectionUtils.isEmpty(confirmOrderItemDtoList)) {
			return R.fail("请先选择商品进行结算");
		}
		if (confirmOrderItemDtoList.size() > 1) {
			return R.fail("预售商品暂不支持批量下单~");
		}
		// 按店铺分组
		List<SubmitOrderShopDTO> shopDtoList = new ArrayList<>();
		// 店铺商品列表
		List<SubmitOrderSkuDTO> skuDtoList = new ArrayList<>();
		for (ConfirmOrderItemDTO confirmOrderItem : confirmOrderItemDtoList) {

			// 结算校验
			SkuBO sku = skuApi.getSkuById(confirmOrderItem.getSkuId()).getData();
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
			ProductDTO product = productApi.getDtoByProductId(sku.getProductId()).getData();

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
			R<ShopDetailDTO> shopDetailResult = shopDetailApi.getById(product.getShopId());
			if (!shopDetailResult.success()) {
				return R.fail(buyNowFlag ? "店铺不存在，请刷新后重试" : "部分店铺不存在，请刷新后重试");
			}
			ShopDetailDTO shopDetailDTO = shopDetailResult.getData();
			if (ObjectUtil.isNull(shopDetailDTO)) {
				return R.fail(buyNowFlag ? "店铺不存在，请刷新后重试" : "部分店铺不存在，请刷新后重试");
			}
			if (!ShopDetailStatusEnum.ONLINE.getStatus().equals(shopDetailDTO.getStatus())) {
				return R.fail(buyNowFlag ? "店铺状态异常，请刷新后重试" : "部分店铺状态异常，请刷新后重试");
			}

			// 组装商品信息
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
			skuDTO.setPic(StrUtil.isNotBlank(sku.getPic()) ? sku.getPic() : product.getPic());
			skuDTO.setShopId(shopDetailDTO.getId());
			skuDTO.setTransId(product.getTransId());
			skuDTO.setStatusFlag(true);
			skuDTO.setStockCounting(product.getStockCounting());

			//预售金额计算
			PreSellProductDTO preSellProduct = preSellProductApi.getByProductId(sku.getProductId()).getData();


			if (preSellProduct.getPreSaleStart().after(new Date())) {
				return R.fail("商品未到预售时间，请刷新后重试");
			}
			skuDTO.setStatus(0);

			if (PreSellPayType.DEPOSIT.value().equals(preSellProduct.getPayPctType())) {
				if (preSellProduct.getDepositStart().after(new Date()) || preSellProduct.getDepositEnd().before(new Date())) {
					return R.fail("商品不在定金支付时间内，请刷新后重试");
				} else {
					skuDTO.setStatus(1);
				}
				// 定金支付比例
				BigDecimal payPct = preSellProduct.getPayPct();
				BigDecimal depositPrice = sku.getPrice().multiply(payPct.divide(BigDecimal.valueOf(100), 2, RoundingMode.DOWN));
				BigDecimal finalPrice = sku.getPrice().subtract(depositPrice);
				skuDTO.setPreDepositPrice(depositPrice);
				skuDTO.setFinalPrice(finalPrice);
				skuDTO.setPayPct(payPct);
				skuDTO.setPayPctType(preSellProduct.getPayPctType());
			} else {
				// 定金即为全额
				skuDTO.setPreDepositPrice(sku.getPrice());
				//全额付款尾款置为0
				skuDTO.setFinalPrice(null);
				skuDTO.setPayPctType(preSellProduct.getPayPctType());
			}
			//1100
			skuDTO.setActualAmount(NumberUtil.mul(skuDTO.getPrice(), confirmOrderItem.getCount()));
			skuDtoList.add(skuDTO);
			// 组装店铺信息
			SubmitOrderShopDTO shopDTO = new SubmitOrderShopDTO();

			shopDTO.setOriginalDepositPrice(skuDTO.getPreDepositPrice());
			shopDTO.setOriginalFinalPrice(skuDTO.getFinalPrice());
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
	protected R<ConfirmOrderBO> getShopBestCoupons(ConfirmOrderBO confirmOrderBO) {
		log.info("###### 开始处理商品商家最优优惠券 ##### ");

		List<SubmitOrderShopDTO> shopOrderList = confirmOrderBO.getShopOrderList();
		Map<Long, ShopCouponDTO> shopCouponMap = new HashMap<>(shopOrderList.size());
		shopOrderList.forEach(submitOrderShopDTO -> {
			Long shopId = submitOrderShopDTO.getShopId();
			List<SubmitOrderSkuDTO> skuList = submitOrderShopDTO.getSkuList();
			shopCouponMap.put(shopId, new ShopCouponDTO(shopId, orderConverter.convertProductItemDTO(skuList)));
		});
		R<Map<Long, ShopCouponDTO>> mapR = couponApi.getShopBestCoupons(confirmOrderBO.getUserId(), shopCouponMap);
		if (!mapR.getSuccess()) {
			throw new BusinessException(mapR.getMsg());
		}
		Map<Long, ShopCouponDTO> shopBestCoupons = mapR.getData();
		if (CollUtil.isEmpty(shopBestCoupons)) {
			log.info("###### 当前用户订单商品没有商家优惠券处理 ##### ");
			return R.ok(confirmOrderBO);
		}
		// 更新预订单缓存
		shopOrderList.forEach(submitOrderShopDTO -> {
			ShopCouponDTO shopCouponDTO = shopBestCoupons.get(submitOrderShopDTO.getShopId());
			if (ObjectUtil.isNotEmpty(shopCouponDTO)) {
				submitOrderShopDTO.setShopCouponDTO(shopCouponDTO);
				if (shopCouponDTO.getCouponItems() == null) {
					submitOrderShopDTO.setCouponAmount(BigDecimal.ZERO);
				} else {
					List<CouponItemDTO> selectItems = shopCouponDTO.getCouponItems().stream().filter(couponItemDTO -> couponItemDTO.getSelectStatus().equals(SELECTED.getStatus())).collect(Collectors.toList());
					BigDecimal reduce = selectItems.stream().map(CouponItemDTO::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
					shopCouponDTO.setSelectItems(selectItems);
					submitOrderShopDTO.setCouponAmount(reduce);
				}
			}
		});
		log.info("###### 结束处理下单商家最优优惠券组合 ##### ");
		return R.ok(confirmOrderBO);
	}

	@Override
	protected R<ConfirmOrderBO> getPlatformCoupons(ConfirmOrderBO confirmOrderBO) {
		log.info("###### 开始处理平台优惠券 ##### ");
		Map<Long, ShopCouponDTO> shopCouponMap = couponUtil.buildPlatformCouponMap(confirmOrderBO.getShopOrderList());
		R<List<CouponItemDTO>> r = couponApi.getBestPlatFormCoupons(confirmOrderBO.getUserId(), shopCouponMap);
		if (!r.getSuccess()) {
			throw new BusinessException(r.getMsg());
		}
		List<CouponItemDTO> platFormCoupons = r.getData();
		if (CollUtil.isEmpty(platFormCoupons)) {
			log.info("###### 该用户没有平台优惠券处理 ##### ");
			return R.ok(confirmOrderBO);
		}

		confirmOrderBO.setPlatformCoupons(platFormCoupons.stream().filter(u -> !u.getSelectStatus().equals(CouponSelectStatusEnum.UN_AVAILABLE.getStatus())).collect(Collectors.toList()));

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
		confirmOrderBO.setPlatformAmount(selectCoupon.stream().map(CouponItemDTO::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
		confirmOrderBO.setUsePlatformCouponCount(selectCoupon.size());
		log.info("###### 结束处理下单平台优惠券 ##### ");
		return R.ok(confirmOrderBO);
	}

	@Override
	public R<ConfirmOrderBO> handleSpecificBusiness(ConfirmOrderBO confirmOrderBO) {

		//西瓜全额：60*50=3000 李子全额：20*100=2000
		//西瓜定金：24*50=1200 尾款：2800 李子定金：300 尾款：1700           总定金：1500  总尾款：4500
		//西瓜限时折扣+满减 10*50=500 150 李子：2*100 100               650+300=950
		//商家： 200
		//平台： 25
		//总优惠： 1175    4500-1175=3825  假设优惠4600 则定金1500-100


		log.info("###### 开始处理商品预售 ##### ");
		List<SubmitOrderShopDTO> shopOrderList = confirmOrderBO.getShopOrderList();
		SubmitOrderShopDTO submitOrderShopDTO = shopOrderList.get(0);
		SubmitOrderSkuDTO submitOrderSkuDTO = submitOrderShopDTO.getSkuList().get(0);

		BigDecimal totalCount = BigDecimal.valueOf(submitOrderSkuDTO.getTotalCount());
		//计算所有sku定金总金额
		BigDecimal preDepositPrice = submitOrderSkuDTO.getPreDepositPrice();
		BigDecimal preDepositTotalPrice = preDepositPrice.multiply(totalCount);

		//商家优惠金额
		BigDecimal discountAmount = submitOrderShopDTO.getDiscountAmount();
		BigDecimal couponAmount = submitOrderShopDTO.getCouponAmount();
		BigDecimal shopCoupon = discountAmount.add(couponAmount);

		//平台优惠金额
		BigDecimal platformCouponAmount = confirmOrderBO.getPlatformAmount();

		// 所有优惠总金额
		BigDecimal amount = shopCoupon.add(platformCouponAmount).add(confirmOrderBO.getDeductionFlag() ? submitOrderSkuDTO.getDeductionAmount() : BigDecimal.ZERO).add(submitOrderSkuDTO.getSelfPurchaseTotalAmount());

		//定金付款
		if (PreSellPayType.DEPOSIT.value().equals(submitOrderSkuDTO.getPayPctType())) {
			// 计算sku尾款总金额
			BigDecimal preFinalPrice = submitOrderSkuDTO.getFinalPrice();
			BigDecimal preFinalTotalPrice = preFinalPrice.multiply(totalCount);

			// 尾款应付金额
			BigDecimal sub = NumberUtil.sub(preFinalTotalPrice, amount);
			// 如果为负数，则尾款不足以抵扣优惠金额，需要分摊到定金上
			if (sub.compareTo(BigDecimal.ZERO) < 0) {
				// 获取差值，需要将差值补到定金上
				BigDecimal difference = amount.subtract(preFinalTotalPrice);
				BigDecimal depositPrice = preDepositTotalPrice.subtract(difference);
				if (depositPrice.compareTo(BigDecimal.ZERO) < 0) {
					depositPrice = BigDecimal.ZERO;
				}
				submitOrderShopDTO.setDepositPrice(depositPrice);

				// 此时尾款为0元
				submitOrderShopDTO.setFinalPrice(BigDecimal.ZERO);

			} else {
				// 如果不为负数，则尾款可以吃完所有优惠，则定金不变
				submitOrderShopDTO.setDepositPrice(preDepositTotalPrice);

				// 尾款
				submitOrderShopDTO.setFinalPrice(sub);
			}
			BigDecimal sub1 = NumberUtil.sub(preFinalTotalPrice, shopCoupon);
			submitOrderShopDTO.setPreShopOrderAmountCoupon(sub1.compareTo(BigDecimal.ZERO) <= 0 ? preDepositTotalPrice.add(sub1) : preDepositTotalPrice);

		}
		//全额付款
		else if (PreSellPayType.FULL_AMOUNT.value().equals(submitOrderSkuDTO.getPayPctType())) {
			BigDecimal subtract = preDepositTotalPrice.subtract(amount);
			BigDecimal sub1 = NumberUtil.sub(preDepositTotalPrice, shopCoupon);
			submitOrderShopDTO.setPreShopOrderAmountCoupon(sub1.compareTo(BigDecimal.ZERO) <= 0 ? submitOrderShopDTO.getDeliveryAmount() : sub1.add(submitOrderShopDTO.getDeliveryAmount()));
			submitOrderShopDTO.setDepositPrice(subtract.compareTo(BigDecimal.ZERO) <= 0 ? BigDecimal.ZERO : subtract);
			submitOrderShopDTO.setFinalPrice(null);
		}

		confirmOrderBO.setDepositPrice(submitOrderShopDTO.getDepositPrice());
		if (PreSellPayType.FULL_AMOUNT.value().equals(submitOrderSkuDTO.getPayPctType())) {
			confirmOrderBO.setDepositPrice(confirmOrderBO.getDepositPrice().add(submitOrderShopDTO.getDeliveryAmount()));
		}
		confirmOrderBO.setFinalPrice(submitOrderShopDTO.getFinalPrice());
		log.info("###### 结束处理商品预售 ##### ");

		return R.ok(confirmOrderBO);
	}
}
