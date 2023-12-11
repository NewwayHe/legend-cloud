/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import com.legendshop.activity.api.CouponApi;
import com.legendshop.activity.dto.*;
import com.legendshop.activity.enums.CouponSelectStatusEnum;
import com.legendshop.activity.enums.CouponUseTypeEnum;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.expetion.BusinessException;
import com.legendshop.order.bo.ConfirmOrderBO;
import com.legendshop.order.dto.SubmitOrderShopDTO;
import com.legendshop.order.dto.SubmitOrderSkuDTO;
import com.legendshop.order.enums.CouponOrderStatusEnum;
import com.legendshop.order.service.CouponUtil;
import com.legendshop.order.service.convert.OrderConverter;
import com.legendshop.product.dto.ProductItemDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

import static com.legendshop.activity.enums.CouponSelectStatusEnum.SELECTED;

/**
 * 优惠券工具
 *
 * @author legendshop
 * @create: 2021-01-15 10:41
 */
@Slf4j
@Service
@AllArgsConstructor
public class CouponUtilImpl implements CouponUtil {

	private final CouponApi couponApi;
	private final OrderConverter orderConverter;

	@Override
	public Map<Long, ShopCouponDTO> buildPlatformCouponMap(List<SubmitOrderShopDTO> shopOrderList) {
		if (CollUtil.isEmpty(shopOrderList)) {
			return Collections.emptyMap();
		}
		Map<Long, ShopCouponDTO> shopCouponMap = new HashMap<>(shopOrderList.size());
		shopOrderList.forEach(submitOrderShopDTO -> {
			Long shopId = submitOrderShopDTO.getShopId();
			List<SubmitOrderSkuDTO> skuList = submitOrderShopDTO.getSkuList();
			BigDecimal productTotalAmount = submitOrderShopDTO.getProductTotalAmount();
			// 获取商品促销及店铺优惠券后的价格 // TODO 先快速完成，没有保留满足促销价但不满足促销店铺优惠价的平台优惠券，可能体验不好
			BigDecimal productCouponAfterAmount = submitOrderShopDTO.getShopCouponAfterAmount();
			shopCouponMap.put(shopId, new ShopCouponDTO(shopId, orderConverter.convertProductItemDTO(skuList), productTotalAmount, productCouponAfterAmount));
		});
		return shopCouponMap;
	}

	@Override
	public R<ConfirmOrderBO> handleSelectPlatformCoupon(ConfirmOrderBO confirmOrderBo, List<Long> couponIds) {
		List<CouponItemDTO> platformCoupons = confirmOrderBo.getPlatformCoupons();
		if (CollUtil.isNotEmpty(platformCoupons)) {
			Map<Long, ShopCouponDTO> shopCouponMap = this.buildPlatformCouponMap(confirmOrderBo.getShopOrderList());
			R<List<CouponItemDTO>> couponResult = couponApi.handleSelectPlatformCoupons(new SelectPlatformCouponDTO(new ArrayList<>(shopCouponMap.values()), platformCoupons, couponIds));
			if (!couponResult.getSuccess() || CollUtil.isEmpty(couponResult.getData())) {
				return R.ok(confirmOrderBo);
			}
			confirmOrderBo.setPlatformCoupons(couponResult.getData());
			//平台优惠券分摊
			this.platformCouponsShardCalculation(confirmOrderBo, shopCouponMap);
		}
		return R.ok(confirmOrderBo);
	}

	@Override
	public R<ConfirmOrderBO> selectCouponPostProcess(ConfirmOrderBO confirmOrderBo) {
		List<CouponItemDTO> platformCoupons = confirmOrderBo.getPlatformCoupons();
		if (CollUtil.isEmpty(platformCoupons)) {
			return R.ok(confirmOrderBo);
		}
		Map<Long, ShopCouponDTO> shopCouponMap = this.buildPlatformCouponMap(confirmOrderBo.getShopOrderList());

		R<List<CouponItemDTO>> r = couponApi.getBestPlatFormCoupons(new PlatFormCouponDTO(platformCoupons, shopCouponMap));

		if (!r.getSuccess()) {
			throw new BusinessException(r.getMsg());
		}
		List<CouponItemDTO> newPlatformCouponList = r.getData();
		if (CollUtil.isEmpty(newPlatformCouponList)) {
			// 平台优惠券金额全部重置为0
			confirmOrderBo.getShopOrderList().forEach(shopOrder -> {
				shopOrder.setPlatformCouponAmount(BigDecimal.ZERO);
				shopOrder.getSkuList().forEach(skuOrder -> skuOrder.setPlatformCouponAmount(BigDecimal.ZERO));
			});
			log.info("###### 该用户没有平台优惠券处理 ##### ");
			return R.ok(confirmOrderBo);
		}
		// 计算已选的平台优惠券总价
		for (CouponItemDTO coupon : platformCoupons) {
			CouponItemDTO couponItemDTO = newPlatformCouponList.stream().filter(newCoupon -> newCoupon.getUserCouponId().equals(coupon.getUserCouponId())).findAny().orElse(null);
			if (null != couponItemDTO) {
				coupon.setSelectStatus(couponItemDTO.getSelectStatus());
			} else {
				coupon.setSelectStatus(CouponSelectStatusEnum.UN_OPTIONAL.getStatus());
			}
		}
		//处理平台优惠券分摊
		this.platformCouponsShardCalculation(confirmOrderBo, shopCouponMap);
		return R.ok(confirmOrderBo);
	}

	@Override
	public R<SubmitOrderShopDTO> shopCouponsShardCalculation(SubmitOrderShopDTO submitOrderShopDTO) {
		// sku商家优惠券全部重置为0
		submitOrderShopDTO.getSkuList().forEach(skuOrder -> skuOrder.setCouponAmount(BigDecimal.ZERO));
		submitOrderShopDTO.setCouponAmount(BigDecimal.ZERO);
		submitOrderShopDTO.getSkuList().forEach(submitOrderSkuDTO -> {
			submitOrderSkuDTO.setCouponAmount(BigDecimal.ZERO);
			submitOrderSkuDTO.getCouponOrderList().removeIf(CouponOrderDTO::getShopProviderFlag);
		});

		ShopCouponDTO shopCouponDTO = submitOrderShopDTO.getShopCouponDTO();
		if (ObjectUtil.isNull(shopCouponDTO)) {
			log.info("当前用户没选中商家优惠券");
			return R.ok(submitOrderShopDTO);
		}
		List<CouponItemDTO> selectItems = Optional.ofNullable(shopCouponDTO.getCouponItems()).orElse(Collections.emptyList())
				.stream()
				.filter(couponItemDTO -> couponItemDTO.getSelectStatus().equals(SELECTED.getStatus()))
				.collect(Collectors.toList());
		if (CollUtil.isEmpty(selectItems)) {
			log.info("当前用户没选中商家优惠券");
			shopCouponDTO.setSelectItems(Collections.emptyList());
			return R.ok(submitOrderShopDTO);
		}
		// 总优惠金额
		BigDecimal totalCouponAmount = selectItems.stream().map(CouponItemDTO::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
		shopCouponDTO.setSelectItems(selectItems);
		submitOrderShopDTO.setCouponAmount(totalCouponAmount);

		for (CouponItemDTO selectItem : selectItems) {
			//分摊到各个订单项里面去
			List<SubmitOrderSkuDTO> skuList = null;
			// 全场通用券，则全部商品平摊
			if (CouponUseTypeEnum.GENERAL.getValue().equals(selectItem.getUseType())) {
				skuList = submitOrderShopDTO.getSkuList();
			} else {
				// 获取指定商品
				Map<Long, ProductItemDTO> skuMap = selectItem.getProductItems().stream().collect(Collectors.toMap(ProductItemDTO::getSkuId, e -> e));

				// 指定券
				if (CouponUseTypeEnum.INCLUDE.getValue().equals(selectItem.getUseType())) {
					skuList = submitOrderShopDTO.getSkuList().stream().filter(e -> skuMap.containsKey(e.getSkuId())).collect(Collectors.toList());
				} else {
					skuList = submitOrderShopDTO.getSkuList().stream().filter(e -> !skuMap.containsKey(e.getSkuId())).collect(Collectors.toList());
				}
			}
			// 按价格排序
			skuList.sort(Comparator.comparing(SubmitOrderSkuDTO::getShopOrderAmountBeforeSubCoupon));

			//商家订单总价格
			BigDecimal shopOrderAmount = submitOrderShopDTO.getShopOrderAmountBeforeSubCoupon();
			BigDecimal tmpSku = BigDecimal.ZERO;
			for (int j = 0; j < skuList.size(); j++) {
				SubmitOrderSkuDTO submitOrderSkuDTO = skuList.get(j);
				BigDecimal sharePrice = BigDecimal.ZERO;
				//每个订单项分摊平台优惠券
				//判断是最后一次循环就直接减
				if (j != skuList.size() - 1) {
					//订单项金额占总订单金额的比例
					BigDecimal orderItemScale = NumberUtil.div(submitOrderSkuDTO.getShopOrderAmountBeforeSubCoupon(), shopOrderAmount);
					sharePrice = NumberUtil.mul(selectItem.getAmount(), orderItemScale).setScale(2, RoundingMode.DOWN);
					tmpSku = NumberUtil.add(tmpSku, sharePrice);
				} else {
					sharePrice = NumberUtil.sub(selectItem.getAmount(), tmpSku).setScale(2, RoundingMode.DOWN);
				}
				//分摊到各个订单里面去
				submitOrderSkuDTO.setCouponAmount(sharePrice);

				// 记录优惠券信息
				CouponOrderDTO couponOrderDTO = new CouponOrderDTO();
				couponOrderDTO.setCouponId(selectItem.getCouponId());
				couponOrderDTO.setUserCouponId(selectItem.getUserCouponId());
				couponOrderDTO.setShopProviderFlag(selectItem.getShopProviderFlag());
				couponOrderDTO.setStatus(CouponOrderStatusEnum.USE.value());
				couponOrderDTO.setCouponItemAmount(sharePrice);
				couponOrderDTO.setCreateTime(DateUtil.date());
				submitOrderSkuDTO.getCouponOrderList().add(couponOrderDTO);
			}
		}
		return R.ok(submitOrderShopDTO);
	}

	@Override
	public R<ConfirmOrderBO> platformCouponsShardCalculation(ConfirmOrderBO confirmOrderBO, Map<Long, ShopCouponDTO> shopCouponMap) {
		// 平台优惠券金额全部重置为0
		confirmOrderBO.getShopOrderList().forEach(shopOrder -> {
			shopOrder.setPlatformCouponAmount(BigDecimal.ZERO);
			shopOrder.getSkuList().forEach(skuOrder -> {
				skuOrder.setPlatformCouponAmount(BigDecimal.ZERO);
				skuOrder.getCouponOrderList().removeIf(e -> !e.getShopProviderFlag());
			});
		});
		// 如果没有可用平台优惠券，则直接返回
		List<CouponItemDTO> platformCoupons = confirmOrderBO.getPlatformCoupons();
		if (CollUtil.isEmpty(platformCoupons)) {
			return R.ok(confirmOrderBO);
		}
		//获取选中的抵扣金额，设置进去
		List<CouponItemDTO> selectCoupon = platformCoupons.stream()
				.filter(couponItemDTO -> couponItemDTO.getSelectStatus().equals(SELECTED.getStatus())).collect(Collectors.toList());
		if (CollUtil.isEmpty(selectCoupon) || CollUtil.isEmpty(shopCouponMap)) {
			log.info("当前用户没选中平台优惠券");
			confirmOrderBO.setPlatformAmount(BigDecimal.ZERO);
			confirmOrderBO.setUsePlatformCouponCount(0);
			return R.ok(confirmOrderBO);
		}

		for (CouponItemDTO selectItem : selectCoupon) {

			//分摊到各个订单项里面去
			List<SubmitOrderShopDTO> shopDTOS = null;
			// 全场通用券，则全部店铺平摊
			if (CouponUseTypeEnum.GENERAL.getValue().equals(selectItem.getUseType())) {
				shopDTOS = confirmOrderBO.getShopOrderList();
			} else {
				// 指定券
				if (CouponUseTypeEnum.INCLUDE.getValue().equals(selectItem.getUseType())) {
					shopDTOS = confirmOrderBO.getShopOrderList().stream().filter(e -> selectItem.getShopItems().contains(e.getShopId())).collect(Collectors.toList());
				} else {
					shopDTOS = confirmOrderBO.getShopOrderList().stream().filter(e -> !selectItem.getShopItems().contains(e.getShopId())).collect(Collectors.toList());
				}
			}

			// 按优惠券后价格排序
			shopDTOS.sort(Comparator.comparing(SubmitOrderShopDTO::getShopOrderAmountAfterShopCoupon));

			// 获取所有订单的优惠券后总金额
			BigDecimal allOrderTotalPrice = shopDTOS.stream().map(SubmitOrderShopDTO::getShopOrderAmountAfterShopCoupon).reduce(BigDecimal.ZERO, BigDecimal::add);
			BigDecimal platformCouponAmount = BigDecimal.ZERO;
			log.info("###### 开始处理平台优惠券分摊 ##### ");
			BigDecimal tmp = BigDecimal.ZERO;
			int i = 1;
			//循环计算分摊
			for (SubmitOrderShopDTO submitOrderShopDTO : shopDTOS) {
				//只分摊符合条件的商家
				if (shopCouponMap.containsKey(submitOrderShopDTO.getShopId())) {
					//商家订单总价格
					BigDecimal shopOrderAmount = submitOrderShopDTO.getShopOrderAmountAfterShopCoupon();
					if (shopOrderAmount.compareTo(BigDecimal.ZERO) <= 0) {
						continue;
					}

					if (i != shopCouponMap.keySet().size()) {
						BigDecimal orderScale = NumberUtil.div(shopOrderAmount, allOrderTotalPrice);
						//商家所分摊的平台优惠券
						platformCouponAmount = NumberUtil.mul(selectItem.getAmount(), orderScale).setScale(2, RoundingMode.DOWN);
						//分摊到各个订单里面去
						tmp = NumberUtil.add(tmp, platformCouponAmount);
					} else {
						platformCouponAmount = NumberUtil.sub(selectItem.getAmount(), tmp);
					}
					submitOrderShopDTO.setPlatformCouponAmount(platformCouponAmount);
					i++;
					//分摊到各个订单项里面去
					List<SubmitOrderSkuDTO> skuList = submitOrderShopDTO.getSkuList();
					skuList.sort(Comparator.comparing(SubmitOrderSkuDTO::getShopOrderAmountAfterShopCoupon));

					BigDecimal tmpSku = BigDecimal.ZERO;
					for (int j = 0; j < skuList.size(); j++) {
						SubmitOrderSkuDTO submitOrderSkuDTO = skuList.get(j);
						BigDecimal sharePrice = BigDecimal.ZERO;
						//每个订单项分摊平台优惠券
						//判断是最后一次循环就直接
						// 减
						if (j != skuList.size() - 1) {
							//订单项金额占总订单金额的比例
							BigDecimal orderItemScale = NumberUtil.div(submitOrderSkuDTO.getShopOrderAmountAfterShopCoupon(), shopOrderAmount);
							sharePrice = NumberUtil.mul(platformCouponAmount, orderItemScale).setScale(2, RoundingMode.DOWN);
							tmpSku = NumberUtil.add(tmpSku, sharePrice);
						} else {
							sharePrice = NumberUtil.sub(platformCouponAmount, tmpSku).setScale(2, RoundingMode.DOWN);
						}
						//分摊到各个订单里面去
						submitOrderSkuDTO.setPlatformCouponAmount(sharePrice);

						// 记录优惠券信息
						CouponOrderDTO couponOrderDTO = new CouponOrderDTO();
						couponOrderDTO.setCouponId(selectItem.getCouponId());
						couponOrderDTO.setUserCouponId(selectItem.getUserCouponId());
						couponOrderDTO.setShopProviderFlag(selectItem.getShopProviderFlag());
						couponOrderDTO.setStatus(CouponOrderStatusEnum.USE.value());
						couponOrderDTO.setCouponItemAmount(sharePrice);
						couponOrderDTO.setCreateTime(DateUtil.date());
						submitOrderSkuDTO.getCouponOrderList().add(couponOrderDTO);
					}
				}
			}

		}

		// 计算平台优惠券总优惠金额
		BigDecimal platformCouponTotalAmount = selectCoupon.stream().map(CouponItemDTO::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
		confirmOrderBO.setPlatformAmount(platformCouponTotalAmount);
		confirmOrderBO.setUsePlatformCouponCount(selectCoupon.size());
		log.info("###### 结束处理平台优惠券分摊 ##### ");
		return R.ok(confirmOrderBO);
	}
}
