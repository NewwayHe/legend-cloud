/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.controller.user;

import cn.hutool.core.util.ObjectUtil;
import com.legendshop.activity.api.CouponApi;
import com.legendshop.activity.dto.CouponItemDTO;
import com.legendshop.activity.dto.ShopCouponDTO;
import com.legendshop.activity.enums.CouponSelectStatusEnum;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.enums.VisitSourceEnum;
import com.legendshop.common.core.expetion.BusinessException;
import com.legendshop.common.security.utils.SecurityUtils;
import com.legendshop.order.bo.ConfirmOrderBO;
import com.legendshop.order.dto.ConfirmOrderDTO;
import com.legendshop.order.dto.InvoiceChangeDTO;
import com.legendshop.order.dto.SubmitOrderShopDTO;
import com.legendshop.order.dto.UseWalletDTO;
import com.legendshop.order.service.CouponUtil;
import com.legendshop.order.service.OrderBusinessService;
import com.legendshop.order.service.OrderCacheService;
import com.legendshop.order.service.OrderUtil;
import com.legendshop.pay.api.UserWalletApi;
import com.legendshop.user.bo.UserInvoiceBO;
import io.seata.spring.annotation.GlobalTransactional;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 购物下单控制器
 *
 * @author legendshop
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "下单相关")
public class UserSubmitOrderController {

	private final OrderUtil orderUtil;
	private final CouponUtil couponUtil;
	private final CouponApi couponApi;
	private final UserWalletApi userWalletApi;
	private final OrderCacheService orderCacheService;
	private final OrderBusinessService orderBusinessService;

	@Operation(summary = "【用户】下单检查,组装商品信息", description = "立即购买、购物车结算以及参与其他营销活动之前的数据校验以及商品数据组装")
	@PostMapping(value = "/check")
	public R<ConfirmOrderBO> orderCheck(@RequestBody ConfirmOrderDTO confirmOrderDTO) {

		//检查下单信息，
		Long userId = SecurityUtils.getUserId();
		confirmOrderDTO.setUserId(userId);
		R<ConfirmOrderBO> orderInfoResult = orderBusinessService.checkAndAssemblyConfirmOrder(confirmOrderDTO);
		if (!orderInfoResult.success()) {
			return R.fail(orderInfoResult.getMsg());
		}

		//组装订单信息
		orderInfoResult.getData().setAddressId(confirmOrderDTO.getAddressId());
		orderInfoResult = orderBusinessService.getOrderInfo(orderInfoResult.getData());
		if (!orderInfoResult.success()) {
			return R.fail(orderInfoResult.getMsg());
		}

		return orderInfoResult;
	}


	@Operation(summary = "【用户】获取确认订单信息", description = "组装收货地址，计算运费，区域限售，计算促销优惠，按预生成订单缓存")
	@Parameters({
			@Parameter(name = "confirmOrderId", description = "预提交订单ID", required = true),
			@Parameter(name = "userAddrId", description = "用户选择的收货地址", required = true)
	})
	@GetMapping(value = "/confirm/info")
	public R<ConfirmOrderBO> getOrderInfo(String confirmOrderId) {

		Long userId = SecurityUtils.getUserId();
		// 获取预订单缓存
		ConfirmOrderBO confirmOrderBo = orderCacheService.getConfirmOrderInfoCache(userId, confirmOrderId);
		if (ObjectUtil.isNull(confirmOrderBo)) {
			return R.fail("购物信息已失效，请重新下单");
		}
		return R.ok(confirmOrderBo);
	}

	@Operation(summary = "【用户】更新地址信息", description = "如果用户在确认订单页面切换收货地址，则对当前订单商品重新计算运费")
	@Parameters({
			@Parameter(name = "confirmOrderId", description = "预提交订单ID", required = true),
			@Parameter(name = "userAddrId", description = "用户选择的收货地址", required = true)
	})
	@GetMapping(value = "/address/change")
	public R<ConfirmOrderBO> addressChange(String confirmOrderId, Long userAddrId) {

		Long userId = SecurityUtils.getUserId();
		// 获取预订单缓存
		ConfirmOrderBO confirmOrderBo = orderCacheService.getConfirmOrderInfoCache(userId, confirmOrderId);
		if (ObjectUtil.isNull(confirmOrderBo)) {
			return R.fail("购物信息已失效，请重新下单");
		}
		R<ConfirmOrderBO> orderInfoResult = orderUtil.handlerDelivery(userAddrId, confirmOrderBo);
		if (!orderInfoResult.success()) {
			return R.fail(orderInfoResult.getMsg());
		}
		// 更新预订单缓存
		orderCacheService.putConfirmOrderInfoCache(userId, orderInfoResult.getData());
		return orderInfoResult;
	}


	@Operation(summary = "【用户】更新发票信息", description = "如果是用户开启关闭发票，则无返回数据，如果是切换发票，则返回当前选择的发票信息")
	@PostMapping(value = "/invoice/change")
	public R<UserInvoiceBO> invoiceChange(@Valid @RequestBody InvoiceChangeDTO invoiceChangeDTO) {
		Long userId = SecurityUtils.getUserId();
		invoiceChangeDTO.setUserId(userId);
		// 获取预订单缓存
		ConfirmOrderBO confirmOrderBo = orderCacheService.getConfirmOrderInfoCache(invoiceChangeDTO.getUserId(), invoiceChangeDTO.getConfirmOrderId());
		if (ObjectUtil.isNull(confirmOrderBo)) {
			return R.fail("购物信息已失效，请重新下单");
		}
		return orderBusinessService.invoiceChange(invoiceChangeDTO, confirmOrderBo);
	}

	@Operation(summary = "【用户】选择切换优惠券", description = "")
	@PostMapping(value = "/select/coupon")
	public R<ConfirmOrderBO> selectCoupon(@RequestBody List<Long> couponIds, Long shopId, @RequestParam String confirmOrderId) {
		Long userId = SecurityUtils.getUserId();
		ConfirmOrderBO confirmOrderBo = orderCacheService.getConfirmOrderInfoCache(userId, confirmOrderId);
		SubmitOrderShopDTO submitOrderShopDTO = confirmOrderBo.getShopOrderList().stream().filter(shopDTO -> shopDTO.getShopId().equals(shopId)).findAny().orElse(null);
		if (ObjectUtil.isNull(submitOrderShopDTO)) {
			return R.ok(confirmOrderBo);
		}
		ShopCouponDTO shopCouponDTO = submitOrderShopDTO.getShopCouponDTO();

		if (ObjectUtil.isNotNull(shopCouponDTO)) {
			shopCouponDTO.setCouponIds(couponIds);
			R<ShopCouponDTO> couponDTOR = couponApi.handleSelectCoupons(shopCouponDTO);
			if (!couponDTOR.getSuccess()) {
				throw new BusinessException(couponDTOR.getMsg());
			}
			//因为feignClient远程调用、只能返回实参，所以要重新赋值给bo
			ShopCouponDTO shopCouponResult = couponDTOR.getData();
			shopCouponDTO.setShopId(shopCouponResult.getShopId());
			shopCouponDTO.setProductItems(shopCouponResult.getProductItems());
			shopCouponDTO.setProductTotalAmount(shopCouponResult.getProductTotalAmount());
			shopCouponDTO.setCouponIds(shopCouponResult.getCouponIds());
			shopCouponDTO.setCouponItems(shopCouponResult.getCouponItems());
			shopCouponDTO.setSelectItems(shopCouponResult.getSelectItems());
			shopCouponDTO.setDiscountAmount(shopCouponResult.getDiscountAmount());
			shopCouponDTO.setUseCouponCount(shopCouponResult.getUseCouponCount());
		}
		List<CouponItemDTO> selectItems = new ArrayList<>();
		for (CouponItemDTO couponItemDTO : shopCouponDTO.getCouponItems()) {
			if (couponItemDTO.getSelectStatus().equals(CouponSelectStatusEnum.SELECTED.getStatus())) {
				selectItems.add(couponItemDTO);
			}
		}
		shopCouponDTO.setSelectItems(selectItems);

		//商家优惠券分摊计算
		couponUtil.shopCouponsShardCalculation(submitOrderShopDTO);

		// 商家券选择后置处理
		// 产品设计：用户选择店铺券会影响所有平台券选择，需要重新计算平台券是否可用，包括已选平台券（目前设计为商家优惠劵优先于平台优惠劵选择）
		couponUtil.selectCouponPostProcess(confirmOrderBo);

		log.info("切换商家优惠券：重新计算积分抵扣");

		// 更新预订单缓存
		orderCacheService.putConfirmOrderInfoCache(userId, confirmOrderBo);
		return R.ok(confirmOrderBo);
	}


	@Operation(summary = "【用户】选择平台优惠券，默认选择最优平台优惠券", description = "")
	@PostMapping(value = "/use/platformCoupon")
	public R<ConfirmOrderBO> usePlatformCoupon(@RequestBody List<Long> couponIds, @RequestParam String confirmOrderId) {
		Long userId = SecurityUtils.getUserId();
		ConfirmOrderBO confirmOrderBo = orderCacheService.getConfirmOrderInfoCache(userId, confirmOrderId);
		// 处理选择平台优惠券
		couponUtil.handleSelectPlatformCoupon(confirmOrderBo, couponIds);

		// 更新预订单缓存
		orderCacheService.putConfirmOrderInfoCache(userId, confirmOrderBo);
		return R.ok(confirmOrderBo);
	}

	@Operation(summary = "【用户】开启用户余额抵扣", description = "")
	@PostMapping(value = "/use/wallet")
	public R<ConfirmOrderBO> useWallet(@Valid @RequestBody UseWalletDTO dto) {
		Long userId = SecurityUtils.getUserId();
		return this.orderBusinessService.useWallet(userId, dto);
	}


	@Operation(summary = "【用户】使用积分开关切换", description = "")
	@PostMapping(value = "/use/switchIntegralFlag")
	public R<ConfirmOrderBO> switchIntegralFlag(@RequestParam Boolean integralFlag, @RequestParam String confirmOrderId) {
		ConfirmOrderBO confirmOrderBo = orderCacheService.getConfirmOrderInfoCache(SecurityUtils.getUserId(), confirmOrderId);
		orderBusinessService.switchIntegralFlag(integralFlag, confirmOrderBo);
		return R.ok(confirmOrderBo);
	}

	@Operation(summary = "【用户】提交订单", description = "")
	@Parameters({
			@Parameter(name = "confirmOrderId", description = "预提交订单ID", required = true),
			@Parameter(name = "orderMessage", description = "订单留言JsonStr 格式：[{'shopId':111,'message':'xxxx'},{'shopId':222,'message':'xxxx'}]", required = true),
			@Parameter(name = "pointId", description = "自提点id"),
			@Parameter(name = "contactId", description = "提货信息id"),
			@Parameter(name = "deliveryType", description = "配送类型", required = true),
	})
	@GetMapping(value = "/submit/order")
	public R submitOrder(String confirmOrderId, String orderMessage, Long pointId, Integer deliveryType, Long contactId) {

		VisitSourceEnum userRequestSource = SecurityUtils.getUserRequestSource();
		Long userId = SecurityUtils.getUserId();
		// 获取预订单缓存
		ConfirmOrderBO confirmOrderBo = orderCacheService.getConfirmOrderInfoCache(userId, confirmOrderId);
		if (ObjectUtil.isNull(confirmOrderBo)) {
			return R.fail("购物信息已失效, 请刷新页面重试!");
		}
		confirmOrderBo.setSourceEnum(userRequestSource);
		confirmOrderBo.setOrderMessage(orderMessage);
		confirmOrderBo.setPointId(pointId);
		confirmOrderBo.setDeliveryType(deliveryType);
		confirmOrderBo.setContactId(contactId);
		return orderBusinessService.submitOrder(confirmOrderBo);
	}


	@GetMapping(value = "/feasibility/test")
	@GlobalTransactional
	public R<Void> feasibilityTest(@RequestParam(value = "param") String parma) {
		R<Void> voidR = this.userWalletApi.feasibilityTest(parma);
		if (!voidR.success()) {
			throw new BusinessException(voidR.getMsg());
		}
		return voidR;
	}

}
