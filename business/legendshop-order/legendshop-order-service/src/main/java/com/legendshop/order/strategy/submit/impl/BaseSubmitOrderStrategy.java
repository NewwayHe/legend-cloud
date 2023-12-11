/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.strategy.submit.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.legendshop.activity.api.CouponApi;
import com.legendshop.activity.dto.CouponOrderDTO;
import com.legendshop.activity.dto.ShopCouponDTO;
import com.legendshop.basic.api.MessageApi;
import com.legendshop.basic.api.SysParamsApi;
import com.legendshop.basic.dto.OrderSettingDTO;
import com.legendshop.basic.enums.SysParamNameEnum;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.enums.VisitSourceEnum;
import com.legendshop.common.core.expetion.BusinessException;
import com.legendshop.common.util.RandomUtil;
import com.legendshop.order.bo.AssemblyOrderBO;
import com.legendshop.order.bo.ConfirmOrderBO;
import com.legendshop.order.dto.*;
import com.legendshop.order.entity.Order;
import com.legendshop.order.enums.OrderDeleteStatusEnum;
import com.legendshop.order.enums.OrderRefundReturnStatusEnum;
import com.legendshop.order.enums.OrderStatusEnum;
import com.legendshop.order.enums.OrderTypeEnum;
import com.legendshop.order.mq.producer.OrderProducerService;
import com.legendshop.order.service.*;
import com.legendshop.order.service.convert.OrderConverter;
import com.legendshop.order.strategy.submit.SubmitOrderStrategy;
import com.legendshop.pay.api.UserWalletApi;
import com.legendshop.pay.api.UserWalletBusinessApi;
import com.legendshop.pay.dto.UserWalletOperationDTO;
import com.legendshop.pay.dto.UserWalletPayDTO;
import com.legendshop.pay.enums.WalletBusinessTypeEnum;
import com.legendshop.product.api.CategoryApi;
import com.legendshop.product.api.ProductApi;
import com.legendshop.product.api.SkuApi;
import com.legendshop.product.api.StockApi;
import com.legendshop.product.bo.CategoryBO;
import com.legendshop.product.bo.SkuBO;
import com.legendshop.product.dto.ProductDTO;
import com.legendshop.product.enums.PreSellPayType;
import com.legendshop.product.enums.ProductDeliveryTypeEnum;
import com.legendshop.product.enums.ProductStatusEnum;
import com.legendshop.shop.api.ShopDetailApi;
import com.legendshop.shop.dto.ShopDetailDTO;
import com.legendshop.shop.enums.ShopDetailStatusEnum;
import com.legendshop.user.api.OrdinaryUserApi;
import com.legendshop.user.api.UserAddressApi;
import com.legendshop.user.api.UserContactApi;
import com.legendshop.user.api.UserInvoiceApi;
import com.legendshop.user.bo.UserAddressBO;
import com.legendshop.user.bo.UserContactBO;
import com.legendshop.user.bo.UserInvoiceBO;
import com.legendshop.user.dto.OrdinaryUserDTO;
import com.legendshop.user.dto.UserAddressDTO;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLDecoder;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 提交订单的模板策略
 *
 * @author legendshop
 */
@Slf4j
@Service
public abstract class BaseSubmitOrderStrategy implements SubmitOrderStrategy {

	@Autowired
	protected OrderUtil orderUtil;

	@Autowired
	protected SkuApi skuApi;

	@Autowired
	protected CouponUtil couponUtil;

	@Autowired
	protected CartService cartService;

	@Autowired
	protected StockApi stockApi;

	@Autowired
	protected CouponApi couponApi;

	@Autowired
	protected OrderService orderService;

	@Autowired
	protected ProductApi productApi;

	@Autowired
	private CategoryApi categoryApi;

	@Autowired
	private SysParamsApi sysParamsApi;

	@Autowired
	protected MessageApi messagePushClient;

	@Autowired
	protected OrderConverter orderConverter;

	@Autowired
	private UserWalletApi userWalletApi;

	@Autowired
	protected DistributionUtil distributionUtil;

	@Autowired
	protected OrderItemService orderItemService;

	@Autowired
	protected ShopDetailApi shopDetailApi;

	@Autowired
	protected OrderCacheService orderCacheService;

	@Autowired
	protected UserAddressApi userAddressApi;

	@Autowired
	protected UserInvoiceApi userInvoiceApi;

	@Autowired
	protected OrdinaryUserApi ordinaryUserApi;

	@Autowired
	protected OrderInvoiceService orderInvoiceService;

	@Autowired
	protected OrderHistoryService orderHistoryService;

	@Autowired
	protected PreSellOrderService preSellOrderService;

	@Autowired
	protected OrderProducerService orderProducerService;

	@Autowired
	protected OrderUserAddressService orderUserAddressService;

	@Autowired
	protected UserWalletBusinessApi userWalletBusinessApi;

	@Autowired
	protected UserContactApi userContactApi;

	@Override
	@GlobalTransactional(rollbackFor = Exception.class)
	public R submit(ConfirmOrderBO confirmOrderBo) {
		log.info("进入提交订单模板策略方法, params: {}", JSONUtil.toJsonStr(confirmOrderBo));
		/*
		 * 每种订单类型都有共用的逻辑以及个别订单类型有一些特有的操作逻辑
		 * 因此，共用的逻辑使用模板提供的实现，有特有逻辑的方法，采用多实现方式。
		 */
		if (ProductDeliveryTypeEnum.SINCEMENTION.getCode().equals(confirmOrderBo.getDeliveryType())) {
			// 如果是自提，则将用户选择的提货信息Id更新到confirmOrderBo
			if (ObjectUtil.isNotNull(confirmOrderBo.getContactId())) {

				R<UserContactBO> contactBOR = userContactApi.getById(confirmOrderBo.getContactId());
				if (!contactBOR.success()) {
					return contactBOR;
				}
				confirmOrderBo.setUserContactBO(contactBOR.getData());
			}

		}

		// 订单提交前的校验
		R checkResult = submitOrderCheck(confirmOrderBo);
		if (!checkResult.success()) {
			return checkResult;
		}

		//是否为自提
		boolean isSinceMention = false;
		if (isSinceMention) {
			confirmOrderBo.setType(OrderTypeEnum.SINCE_MENTION);
		}
		Long userId = confirmOrderBo.getUserId();
		String orderType = confirmOrderBo.getType().getValue();

		Long orderUserAddressId = null;
		if (!isSinceMention) {
			// 非自提，保存订单用户收货地址 ，返回订单地址ID冗余到订单表
			R<Long> orderUserAddressResult = saveOrderUserAddress(confirmOrderBo.getUserAddressBO());
			if (!orderUserAddressResult.success()) {
				return orderUserAddressResult;
			}
			orderUserAddressId = orderUserAddressResult.getData();
		}

		// 按店铺生成订单 todo 应该生成父单和订单项，发起支付时要生成父子支付单，支付成功后对订单父单进行拆单，订单项绑定到对应的子单
		List<Order> orderList = new ArrayList<>();
		List<String> orderNumberList = new ArrayList<>();
		List<Long> orderIds = new ArrayList<>();
		List<SubmitOrderShopDTO> shopOrderList = confirmOrderBo.getShopOrderList();

		for (SubmitOrderShopDTO submitOrderShopDTO : shopOrderList) {
			// 处理商家优惠券分摊
			couponsShard(submitOrderShopDTO);
		}

		//计算平台优惠券的分摊
		platformCouponsShard(confirmOrderBo);

		// 保存优惠券订单关系表
		List<CouponOrderDTO> couponOrderList = new ArrayList<>();

		for (SubmitOrderShopDTO submitOrderShopDTO : shopOrderList) {
			// 用户选择开具发票，保存订单发票信息，返回订单发票ID冗余到订单表
			Long orderInvoiceId = null;

			if (submitOrderShopDTO.getUserInvoiceFlag()) {
				R<Long> orderInvoiceResult = saveOrderInvoice(submitOrderShopDTO.getUserInvoiceBo());
				if (!orderInvoiceResult.success()) {
					throw new BusinessException(orderInvoiceResult.getMsg());
				}
				orderInvoiceId = orderInvoiceResult.getData();
			}

			// 处理买家订单留言
			handlerOrderMessage(confirmOrderBo.getOrderMessage(), submitOrderShopDTO);

			// 公共组装订单  Order OrderItem
			AssemblyOrderBO assemblyOrderBo = assemblyOrder(orderType, userId, orderUserAddressId, orderInvoiceId, submitOrderShopDTO, confirmOrderBo, isSinceMention);

			// 有特有逻辑的订单 ：拼团订单需增加拼团订单状态以及拼团活动编号，团购订单增加订单状态位 （这些都是在组装订单的时候有特殊处理）
			AssemblyOrderBO assemblyOrder = uniqueLogicProcess(assemblyOrderBo, confirmOrderBo.getGroupNumber(), confirmOrderBo.getActivityId());

			// 组装特有订单扩展记录
			assemblyOrder = specificBusiness(assemblyOrder);

			// 公共保存主订单 Order
			R<Long> saveOrderResult = saveOrder(assemblyOrder.getOrderDTO());
			if (!saveOrderResult.success()) {
				throw new BusinessException(saveOrderResult.getMsg());
			}

			Long orderId = saveOrderResult.getData();
			orderIds.add(orderId);


			// 记录订单号
			orderNumberList.add(assemblyOrder.getOrderDTO().getOrderNumber());

			// 公共保存订单项 OrderItem
			R saveOrderItemResult = saveOrderItem(saveOrderResult.getData(), assemblyOrder.getOrderItemDTO());
			if (!saveOrderItemResult.success()) {
				throw new BusinessException(saveOrderItemResult.getMsg());
			}

			// 订单保存后，后续特殊活动记录操作
			assemblyOrder = specificAfterSaveOrder(assemblyOrder);


			// mq延时取消队列 定时未支付取消 取消未支付订单之前发送站内信提醒用户进行支付
			orderProducerService.autoCancelUnPayOrder(assemblyOrder.getOrderDTO(), orderType);
			// mq保存订单历史
			orderProducerService.saveOrderHistory(saveOrderResult.getData(), userId);
			// mq保存商品快照
			orderProducerService.saveProductSnapshot(JSONUtil.toJsonStr(assemblyOrder.getOrderItemDTO()));

			Order order = orderConverter.from(assemblyOrder.getOrderDTO());
			submitOrderShopDTO.setUserId(userId);

			// 扣减库存
			R deductionStockResult = deductionStockProcess(submitOrderShopDTO);
			if (!deductionStockResult.success()) {
				throw new BusinessException(deductionStockResult.getMsg());
			}

			// 处理优惠券，将订单项转成 skuId 为 key 的 Map
			Map<Long, OrderItemDTO> skuMap = assemblyOrder.getOrderItemDTO().stream().collect(Collectors.toMap(OrderItemDTO::getSkuId, e -> e));
			for (SubmitOrderSkuDTO submitOrderSkuDTO : submitOrderShopDTO.getSkuList()) {
				// 如果没有优惠券记录，则当前订单没有用券，跳过
				List<CouponOrderDTO> couponOrders = submitOrderSkuDTO.getCouponOrderList();
				if (CollUtil.isEmpty(couponOrders)) {
					continue;
				}

				// 如果 skuId 有对应的订单，则保存优惠券信息缺的订单信息
				if (skuMap.containsKey(submitOrderSkuDTO.getSkuId())) {
					OrderItemDTO orderItemDTO = skuMap.get(submitOrderSkuDTO.getSkuId());
					for (CouponOrderDTO couponOrder : couponOrders) {
						couponOrder.setOrderId(orderItemDTO.getOrderId());
						couponOrder.setOrderNumber(orderItemDTO.getOrderNumber());
						couponOrder.setOrderItemId(orderItemDTO.getId());
						couponOrder.setUserId(orderItemDTO.getUserId());
						couponOrder.setSource(orderItemDTO.getSource());
						couponOrderList.add(couponOrder);
					}
				} else {
					// 如果找不到，则代码异常，需要处理
					log.error("[submit-order] 当前skuId找不到对应的订单项，userId：{}，skuId：{}", userId, submitOrderSkuDTO.getSkuId());
				}
			}

			//清除购物车
			cartService.batchClean(userId, submitOrderShopDTO.getSkuList());
			orderList.add(order);
		}

		// 钱包余额抵扣
		this.handlerWalletDeducted(confirmOrderBo.getUseWalletInfo(), orderList);

		// 保存优惠券与订单项的关系
		if (CollUtil.isNotEmpty(couponOrderList)) {
			couponApi.batchUpdateUsedStatus(couponOrderList);
		}

		// 清除预订单缓存
		orderCacheService.evictConfirmOrderInfoCache(confirmOrderBo.getUserId(), confirmOrderBo.getId());
		log.info("###### 清除预定单缓存，下单成功！ ##### ");
		// 组装下单成功返回参数
		SubmitOrderSuccessDTO submitOrderSuccessDTO = new SubmitOrderSuccessDTO();
		submitOrderSuccessDTO.setOrderNumberList(orderNumberList);
		R<SubmitOrderSuccessDTO> result = assemblySubmitOrderSuccessProcess(submitOrderSuccessDTO);
		if (!result.success()) {
			throw new BusinessException(result.getMsg());
		}
		SubmitOrderSuccessDTO data = result.getData();

		BigDecimal actualTotal;
		// 判断是否为预售，是否只需支付定金
		if (confirmOrderBo.getType().equals(OrderTypeEnum.PRE_SALE)) {
			List<PreSellOrderDTO> preSellOrderDTOS = preSellOrderService.queryByOrderIds(orderIds);
			actualTotal = preSellOrderDTOS.stream().map(PreSellOrderDTO::getPreDepositPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
			// 如果全款支付，需要加上运费
			if (PreSellPayType.FULL_AMOUNT.value().equals(confirmOrderBo.getShopOrderList().get(0).getSkuList().get(0).getPayPctType())) {
				actualTotal = actualTotal.add(confirmOrderBo.getShopOrderList().get(0).getDeliveryAmount());
			}
		} else {
			// 判断订单金额是否为零元
			actualTotal = orderList.stream().map(Order::getActualTotalPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
		}
		// 订单金额小于零，错误
		if (actualTotal.compareTo(BigDecimal.ZERO) < 0) {
			throw new BusinessException("订单金额错误！");
		}
		data.setAmount(actualTotal);
		// mq保存用户购买力下单数据
		orderProducerService.sendNewOrderMessage(orderList);
		return R.ok(data);
	}

	/**
	 * 处理平台优惠券分摊
	 *
	 * @param confirmOrderBo
	 */
	protected abstract void platformCouponsShard(ConfirmOrderBO confirmOrderBo);

	/**
	 * 处理商家优惠券分摊
	 *
	 * @param submitOrderShopDTO
	 */
	protected abstract void couponsShard(SubmitOrderShopDTO submitOrderShopDTO);


	/**
	 * 处理平台优惠券分摊
	 *
	 * @param confirmOrderBo
	 */
	protected void handlerPlatformCouponsShard(ConfirmOrderBO confirmOrderBo) {
		log.info("###### 开始处理平台优惠券 ##### ");
		Map<Long, ShopCouponDTO> shopCouponMap = couponUtil.buildPlatformCouponMap(confirmOrderBo.getShopOrderList());
		//处理平台优惠券分摊
		couponUtil.platformCouponsShardCalculation(confirmOrderBo, shopCouponMap);
	}

	/**
	 * 用户余额金额抵扣
	 */
	protected void handlerWalletDeducted(UseWalletInfoDTO useWalletInfo, List<Order> orderList) {
		if (null == useWalletInfo) {
			return;
		}
		if (!useWalletInfo.getAllowed()) {
			return;
		}
		// 判断用户钱包是否满足
		if (!useWalletInfo.getUseWallet()) {
			return;
		}
		// 冻结用户金额（按订单号冻结）
		BigDecimal amount = useWalletInfo.getAmount();
		if (BigDecimal.ZERO.compareTo(amount) >= 0) {
			return;
		}
		R<UserWalletPayDTO> userWalletResult = this.userWalletApi.payInfo();
		UserWalletPayDTO data = userWalletResult.getData();
		if (data.getAmount().compareTo(amount) < 0) {
			throw new BusinessException("用户余额不足！");
		}
		orderList.sort(Comparator.comparing(Order::getActualTotalPrice));
		List<UserWalletOperationDTO> walletOperationList = new ArrayList<>(orderList.size());
		BigDecimal orderActual = orderList.stream().map(Order::getActualTotalPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
		if (amount.compareTo(orderActual) == 0) {
			orderList.forEach(order -> {
				UserWalletOperationDTO dto = new UserWalletOperationDTO();
				walletOperationList.add(dto);
				dto.setUserId(order.getUserId());
				dto.setBusinessId(order.getId());
				dto.setBusinessType(WalletBusinessTypeEnum.ORDER_DEDUCTION);
				dto.setAmount(data.getAmount());
				// 计算单个订单的抵扣金额
				BigDecimal actual = order.getActualTotalPrice();
				dto.setAmount(actual);
				dto.setRemarks(WalletBusinessTypeEnum.ORDER_DEDUCTION.getDesc() + ", 账号支付金额为: " + actual + ", 订单号: " + order.getOrderNumber());
			});
		} else {
			orderList.sort(Comparator.comparing(Order::getActualTotalPrice));

			BigDecimal sharePrice = amount;
			for (int i = 0; i < orderList.size(); i++) {
				Order order = orderList.get(i);
				UserWalletOperationDTO dto = new UserWalletOperationDTO();
				walletOperationList.add(dto);
				dto.setUserId(order.getUserId());
				dto.setBusinessId(order.getId());
				dto.setBusinessType(WalletBusinessTypeEnum.ORDER_DEDUCTION);

				if (i != orderList.size() - 1) {
					// 计算单个订单的抵扣金额
					BigDecimal actual = order.getActualTotalPrice();
					BigDecimal orderDivide = actual.divide(orderActual, 6, RoundingMode.DOWN);
					BigDecimal orderWalletAmount = amount.multiply(orderDivide).setScale(2, RoundingMode.DOWN);
					dto.setAmount(orderWalletAmount);
					sharePrice = sharePrice.subtract(orderWalletAmount);
				} else {
					dto.setAmount(sharePrice);
				}
				dto.setRemarks(WalletBusinessTypeEnum.ORDER_DEDUCTION.getDesc() + ", 账号支付金额为: " + dto.getAmount() + ", 订单号: " + order.getOrderNumber());
			}
		}

		// 冻结用户钱包
		BigDecimal userWalletOrderAmount = walletOperationList.stream().map(UserWalletOperationDTO::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
		if (userWalletOrderAmount.compareTo(amount) > 0) {
			log.error(" [ Order User Wallet ]  ---->  订单余额抵扣金额计算错误，选择金额：{}，订单抵扣总金额：{} ", amount, userWalletOrderAmount);
			throw new BusinessException("订单余额抵扣金额计算错误！");
		} else if (userWalletOrderAmount.compareTo(amount) < 0) {
			BigDecimal subtract = amount.subtract(userWalletOrderAmount);
			Long orderId = orderList.get(orderList.size() - 1).getId();
			walletOperationList.stream().filter(e -> e.getBusinessId().equals(orderId)).findFirst().ifPresent(detail -> detail.setAmount(detail.getAmount().add(subtract)));
		}

		userWalletBusinessApi.synchronizeFrozen(walletOperationList);
	}

	/**
	 * 处理商家优惠券的分摊
	 */
	protected void handlerShopCouponsShard(SubmitOrderShopDTO submitOrderShopDTO) {
		//处理平台优惠券分摊
		couponUtil.shopCouponsShardCalculation(submitOrderShopDTO);
	}


	/**
	 * 组装下单成功返回信息
	 *
	 * @param submitOrderSuccessDTO
	 * @return
	 */
	protected abstract R<SubmitOrderSuccessDTO> assemblySubmitOrderSuccessProcess(SubmitOrderSuccessDTO submitOrderSuccessDTO);

	/**
	 * 处理买家留言
	 */
	private void handlerOrderMessage(String orderMessage, SubmitOrderShopDTO submitOrderShopDTO) {
		try {
			orderMessage = URLDecoder.decode(orderMessage, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		if (StrUtil.isNotBlank(orderMessage) && JSONUtil.isJsonArray(orderMessage)) {
			JSONArray jsonArray = JSONUtil.parseArray(orderMessage);
			List<OrderMessageDTO> orderMessageList = JSONUtil.toList(jsonArray, OrderMessageDTO.class);
			for (OrderMessageDTO m : orderMessageList) {
				if (submitOrderShopDTO.getShopId().equals(m.getShopId())) {
					submitOrderShopDTO.setRemark(m.getMessage());
					break;
				}
			}
		}
	}


	/**
	 * 提交订单前的校验
	 */
	private R submitOrderCheck(ConfirmOrderBO confirmOrderBo) {

		log.info("###### 提交订单前的购物信息校验 ##### ");
		if (CollectionUtil.isEmpty(confirmOrderBo.getShopOrderList())) {
			return R.fail("购物商品信息已失效, 请刷新页面重试!");
		}
		if (confirmOrderBo.getDeliveryType().equals(ProductDeliveryTypeEnum.EXPRESS_DELIVERY.getCode())) {
			if (confirmOrderBo.getRegionalSalesFlag()) {
				return R.fail("存在区域限售商品，提交失败!");
			}
		}


		/* 1.用户信息验证 收货地址验证 */
		if (ProductDeliveryTypeEnum.EXPRESS_DELIVERY.getCode().equals(confirmOrderBo.getDeliveryType())) {
			// 收货地址验证
			UserAddressBO userAddress = confirmOrderBo.getUserAddressBO();
			if (ObjectUtil.isNull(userAddress)) {
				return R.fail("请选择收货地址");
			}
			UserAddressDTO userAddressDTO = userAddressApi.getById(userAddress.getId()).getData();
			if (ObjectUtil.isNull(userAddressDTO)) {
				return R.fail("收货地址已发生变化, 请刷新页面重试!");
			}
		} else {
			// 提货信息验证
			UserContactBO userContactBO = confirmOrderBo.getUserContactBO();
			if (ObjectUtil.isNull(userContactBO)) {
				return R.fail("请选择提货信息");
			}
		}
		// 用户信息验证
		OrdinaryUserDTO user = ordinaryUserApi.getById(confirmOrderBo.getUserId()).getData();
		if (ObjectUtil.isNull(user) || !user.getLockFlag()) {
			return R.fail("用户状态异常, 请刷新页面重试!");
		}

		/* 2. 店铺信息验证 商品验证  价格验证 库存校验 区域限售 */
		// 价格验证,订单总价格不能少于0
		if (confirmOrderBo.getTotalAmount().compareTo(BigDecimal.ZERO) < 0) {
			return R.fail("订单价格异常, 请刷新页面重试!");
		}
		List<SubmitOrderShopDTO> shopOrderList = confirmOrderBo.getShopOrderList();
		// 店铺状态验证
		for (SubmitOrderShopDTO submitOrderShopDTO : shopOrderList) {
			R<ShopDetailDTO> shopDetailResult = shopDetailApi.getById(submitOrderShopDTO.getShopId());
			if (!shopDetailResult.success()) {
				return R.fail("部分商品所属店铺状态异常, 请刷新页面重试!");
			}
			ShopDetailDTO shopDetailDTO = shopDetailResult.getData();
			if (ObjectUtil.isNull(shopDetailDTO) || !ShopDetailStatusEnum.ONLINE.getStatus().equals(shopDetailDTO.getStatus())) {
				return R.fail("部分商品所属店铺状态异常, 请刷新页面重试!");
			}

			if (submitOrderShopDTO.getInvoiceFlag() && submitOrderShopDTO.getUserInvoiceFlag()) {
				UserInvoiceBO userInvoiceBO = userInvoiceApi.getInvoiceBoById(submitOrderShopDTO.getInvoiceId()).getData();
				if (ObjectUtil.isNull(userInvoiceBO)) {
					return R.fail("您选择的发票信息已发生变化, 请刷新页面重试!");
				}
			}

			// （TODO） 店铺扩展校验
			R shopExtensionMethodResult = extensionMethod(submitOrderShopDTO);
			if (!shopExtensionMethodResult.success()) {
				return shopExtensionMethodResult;
			}

			List<SubmitOrderSkuDTO> skuList = submitOrderShopDTO.getSkuList();
			if (CollectionUtil.isEmpty(skuList)) {
				return R.fail("暂无可结算商品，请尝试重新选择收货地址或重新购买");
			}
			for (SubmitOrderSkuDTO skuDTO : skuList) {

				// 商品状态验证
				ProductDTO product = productApi.getDtoByProductId(skuDTO.getProductId()).getData();
				if (ObjectUtil.isNull(product)) {
					return R.fail("部分商品不存在或已被修改，请刷新后重试");
				}
				if (!ProductStatusEnum.PROD_ONLINE.value().equals(product.getStatus()) && !skuDTO.getExchangeFlag()) {
					return R.fail("部分商品状态异常，请刷新后重试");
				}

				// 库存校验(每个营销活动都有独立库存，所以应该校验独立库存)
				R stockCheckResult = stockCheckProcess(confirmOrderBo.getActivityId(), skuDTO);
				if (!stockCheckResult.success()) {
					return stockCheckResult;
				}

				// (TODO) 商品扩展校验
				R skuExtensionMethodResult = extensionMethod(skuDTO);
				if (!skuExtensionMethodResult.success()) {
					return shopExtensionMethodResult;
				}
			}
		}

		// 3. 营销活动状态验证（扩展 TODO）
		R activityCheckResult = activityCheckProcess(confirmOrderBo);
		if (!activityCheckResult.success()) {
			return activityCheckResult;
		}
		return R.ok();
	}


	/**
	 * 库存校验
	 *
	 * @param activityId
	 * @param skuDTO
	 * @return
	 */
	protected R stockCheckProcess(Long activityId, SubmitOrderSkuDTO skuDTO) {

		SkuBO sku = skuApi.getSkuById(skuDTO.getSkuId()).getData();
		if (ObjectUtil.isNull(sku)) {
			return R.fail("部分商品规格不存在或已被修改，请刷新后重试");
		}
		if (ObjectUtil.isNull(sku.getStocks()) || sku.getStocks() <= 0) {
			return R.fail("部分商品规格库存不足，请刷新后重试");
		}
		if (sku.getStocks() < skuDTO.getTotalCount()) {
			return R.fail("部分商品数量已超过现有库存，请刷新后重试");
		}
		return R.ok();
	}

	/**
	 * 保存订单用户收货地址
	 *
	 * @param userAddressBo 收货地址
	 * @return 订单用户收货地址ID
	 */
	private R<Long> saveOrderUserAddress(UserAddressBO userAddressBo) {

		log.info("###### 保存订单用户收货地址 ##### ");
		OrderUserAddressDTO orderUserAddress = new OrderUserAddressDTO();
		orderUserAddress.setReceiver(userAddressBo.getReceiver());
		orderUserAddress.setMobile(userAddressBo.getMobile());
		orderUserAddress.setUserId(userAddressBo.getUserId());
		orderUserAddress.setProvinceId(userAddressBo.getProvinceId());
		orderUserAddress.setCityId(userAddressBo.getCityId());
		orderUserAddress.setAreaId(userAddressBo.getAreaId());
		orderUserAddress.setStreetId(userAddressBo.getStreetId());
		orderUserAddress.setDetailAddress(userAddressBo.getDetailAddress());

		Long result = orderUserAddressService.save(orderUserAddress);
		if (result <= 0) {
			return R.fail("下单--保存订单收货地址失败");
		}
		return R.ok(result);
	}


	/**
	 * 保存订单发票信息
	 *
	 * @param userInvoiceBo 发票信息
	 * @return 订单发票信息ID
	 */
	private R<Long> saveOrderInvoice(UserInvoiceBO userInvoiceBo) {

		log.info("###### 保存订单用户收货地址 ##### ");

		OrderInvoiceDTO orderInvoice = new OrderInvoiceDTO();
		orderInvoice.setUserId(userInvoiceBo.getUserId());
		orderInvoice.setType(userInvoiceBo.getType());
		orderInvoice.setTitleType(userInvoiceBo.getTitleType());
		orderInvoice.setCompany(userInvoiceBo.getCompany());
		orderInvoice.setBankAccountNum(userInvoiceBo.getBankAccountNum());
		orderInvoice.setDepositBank(userInvoiceBo.getDepositBank());
		orderInvoice.setInvoiceHumNumber(userInvoiceBo.getInvoiceHumNumber());
		orderInvoice.setRegisterAddr(userInvoiceBo.getRegisterAddr());
		orderInvoice.setRegisterPhone(userInvoiceBo.getRegisterPhone());
		orderInvoice.setCreateTime(new Date());

		Long result = orderInvoiceService.save(orderInvoice);
		if (result <= 0) {
			return R.fail("下单--保存订单发票失败");
		}
		return R.ok(result);
	}


	/**
	 * 组装订单、订单项
	 *
	 * @param orderType          订单类型
	 * @param userId             用户ID
	 * @param orderUserAddressId 订单用户收货地址
	 * @param orderInvoiceId     订单发票信息ID
	 * @param submitOrderShopDTO 预订单,店铺商品信息
	 * @param isSinceMention     是否自提
	 * @return 封装的组装数据BO
	 */
	private AssemblyOrderBO assemblyOrder(String orderType, Long userId, Long orderUserAddressId, Long orderInvoiceId, SubmitOrderShopDTO submitOrderShopDTO, ConfirmOrderBO confirmOrderBo, Boolean isSinceMention) {

		log.info("###### 组装订单、订单项 ##### ");
		Date currentDate = new Date();
		// 用于接收多个订单项
		List<OrderItemDTO> orderItemDtoList = new ArrayList<>();
		// 生成订单号
		String orderNumber = RandomUtil.getRandomSn();
		// 用于组装订单商品名称，多个商品以逗号分割
		StringBuilder productName = new StringBuilder(100);

		// 先组装orderItem,order有些数据需要先依赖item组装出来
		List<SubmitOrderSkuDTO> skuList = submitOrderShopDTO.getSkuList();
		//获取商品id
		Set<Long> productIds = skuList.stream().map(SubmitOrderSkuDTO::getProductId).collect(Collectors.toSet());
		//计算售后截止时间
		Map<Long, Integer> refundDateMap = getReturnValidPeriod(new ArrayList<>(productIds));
		// 循环次数
		int j = 0;
		for (SubmitOrderSkuDTO item : skuList) {
			OrderItemDTO orderItemDTO = new OrderItemDTO();
			orderItemDTO.setOrderNumber(orderNumber);
			orderItemDTO.setUserId(userId);
			orderItemDTO.setProductId(item.getProductId());
			orderItemDTO.setSkuId(item.getSkuId());
			orderItemDTO.setOrderItemNumber(orderNumber + "-" + (++j));
			orderItemDTO.setBasketCount(item.getTotalCount());
			orderItemDTO.setProductName(item.getProductName());
			orderItemDTO.setCostPrice(item.getCostPrice());
			orderItemDTO.setDistFlag(item.getDistFlag());


			orderItemDTO.setAttribute(item.getCnProperties());
			orderItemDTO.setPic(item.getPic());
			orderItemDTO.setOriginalPrice(item.getOriginalPrice());
			orderItemDTO.setPrice(item.getPrice());
			orderItemDTO.setVolume(item.getVolume());
			orderItemDTO.setWeight(item.getWeight());
			orderItemDTO.setStockCounting(item.getStockCounting());
			orderItemDTO.setCreateTime(currentDate);
			orderItemDTO.setIntegral(ObjectUtil.isNotEmpty(item.getDeductionFlag()) && item.getDeductionFlag() || ObjectUtil.isNotEmpty(item.getExchangeFlag()) && item.getExchangeFlag() ? item.getTotalIntegral() : 0);
			orderItemDTO.setReturnValidPeriod(refundDateMap.get(item.getProductId()) == null ? 0 : refundDateMap.get(item.getProductId()));
			orderItemDTO.setDeductionAmount(ObjectUtil.isNotEmpty(item.getDeductionFlag()) && item.getDeductionFlag() ? item.getTotalDeductionAmount() : BigDecimal.ZERO);

			// 物料url
			orderItemDTO.setMaterialUrl(item.getMaterialUrl());

			//订单项退款状态默认为未发起退款
			orderItemDTO.setRefundStatus(OrderRefundReturnStatusEnum.ITEM_NO_REFUND.value());

			//订单项相关金额
			orderItemDTO.setProductTotalAmount(NumberUtil.mul(orderItemDTO.getPrice(), orderItemDTO.getBasketCount()));
			orderItemDTO.setLimitDiscountsMarketingId(item.getLimitDiscountsMarketingId());
			orderItemDTO.setLimitDiscountsMarketingPrice(item.getLimitDiscountsMarketingPrice());
			orderItemDTO.setRewardMarketingId(item.getRewardMarketingId());
			orderItemDTO.setRewardMarketingPrice(item.getRewardMarketingPrice());
			orderItemDTO.setDiscountPrice(item.getTotalDiscountAmount());
			orderItemDTO.setDiscountedPrice(item.getDiscountedPrice());
			orderItemDTO.setCouponOffPrice(item.getCouponAmount());
			orderItemDTO.setPlatformCouponOffPrice(item.getPlatformCouponAmount());
			orderItemDTO.setMarketingInfo(item.getLimitDiscountsMarketingInfo() + item.getRewardMarketingInfo());
			orderItemDTO.setActualAmount(item.getTotalActualAmountAfterCommission().subtract(Optional.ofNullable(orderItemDTO.getDeductionAmount()).orElse(BigDecimal.ZERO)));
			orderItemDTO.setSelfPurchaseAmount(item.getSelfPurchaseAmount());
			orderItemDTO.setSelfPurchaseTotalAmount(item.getSelfPurchaseTotalAmount());
			orderItemDTO.setDistCommissionCash(item.getDistCommissionCash());
			orderItemDTO.setDistFlag(item.getDistFlag());
			orderItemDTO.setDistCalcFlag(Boolean.FALSE);
			orderItemDTO.setDistType(item.getDistType());
			orderItemDTO.setDistRatio(item.getDistRatio());
			orderItemDTO.setCommissionSettlementType(item.getCommissionSettlementType());
			orderItemDTO.setSettlementPrice(item.getSettlementPrice());
			orderItemDTO.setShopId(item.getShopId());
			if (VisitSourceEnum.MP.equals(confirmOrderBo.getSourceEnum())) {
				orderItemDTO.setSource(VisitSourceEnum.H5.name());
			} else {
				orderItemDTO.setSource(confirmOrderBo.getSourceEnum().name());
			}
			orderItemDtoList.add(orderItemDTO);

			// 生成订单的商品名，多个商品名以逗号分隔
			productName.append(orderItemDTO.getProductName()).append(",");
		}
		productName.setLength(productName.length() - 1);
		if (productName.length() > 30) {
			productName = new StringBuilder(productName.substring(0, 30));
			productName.append("...");
		}

		//组装order
		OrderDTO orderDTO = new OrderDTO();
		orderDTO.setUserId(userId);
		//预售商品需要订单id,所以id提前获取，保存时候带id保存
		orderDTO.setId(orderService.creatId());
		orderDTO.setShopId(submitOrderShopDTO.getShopId());
		orderDTO.setShopName(submitOrderShopDTO.getShopName());
		orderDTO.setOrderNumber(orderNumber);
		orderDTO.setProductName(productName.toString());

		orderDTO.setMessage(submitOrderShopDTO.getRemark());
		orderDTO.setAddressOrderId(orderUserAddressId);
		orderDTO.setHasInvoiceFlag(false);
		orderDTO.setNeedInvoiceFlag(false);
		if (submitOrderShopDTO.getInvoiceFlag() && submitOrderShopDTO.getUserInvoiceFlag()) {
			orderDTO.setNeedInvoiceFlag(true);
		}
		orderDTO.setOrderInvoiceId(orderInvoiceId);
		orderDTO.setOrderType(isSinceMention ? OrderTypeEnum.SINCE_MENTION.getValue() : orderType);
		orderDTO.setStatus(OrderStatusEnum.UNPAID.getValue());
		orderDTO.setCreateTime(currentDate);
		orderDTO.setRemarkedFlag(Boolean.FALSE);
		if (VisitSourceEnum.MP.equals(confirmOrderBo.getSourceEnum())) {
			orderDTO.setSource(VisitSourceEnum.H5.name());
		} else {
			orderDTO.setSource(confirmOrderBo.getSourceEnum().name());
		}

		//计算订单总商品件数
		orderDTO.setProductQuantity(orderItemDtoList.stream().mapToInt(OrderItemDTO::getBasketCount).sum());

		//订单退款状态默认为未发起退款
		orderDTO.setRefundStatus(OrderRefundReturnStatusEnum.ORDER_NO_REFUND.value());
		//订单删除状态默认为未删除
		orderDTO.setDeleteStatus(OrderDeleteStatusEnum.NORMAL.value());
		// 订单结算状态设置为未结算
		orderDTO.setBillFlag(false);
		// 订单相关金额
		orderDTO.setFreightPrice(isSinceMention ? BigDecimal.ZERO : submitOrderShopDTO.getDeliveryAmount());
		orderDTO.setDiscountTotalAmount(submitOrderShopDTO.getDiscountAmount());
		orderDTO.setCouponAmount(submitOrderShopDTO.getCouponAmount());
		orderDTO.setPlatformCouponAmount(submitOrderShopDTO.getPlatformCouponAmount());
		orderDTO.setTotalPrice(submitOrderShopDTO.getProductTotalAmount());
		//如果是自提订单需要减去运费
		orderDTO.setActualTotalPrice(isSinceMention ? submitOrderShopDTO.getShopOrderAmountNoFreight() : submitOrderShopDTO.getShopOrderAmount());
		orderDTO.setTotalIntegral(OrderTypeEnum.INTEGRAL.getValue().equals(orderDTO.getOrderType())
				? submitOrderShopDTO.getProductTotalIntegral().intValue()
				: submitOrderShopDTO.getProductTotalDeductionIntegral().intValue());
		orderDTO.setSettlementPrice(submitOrderShopDTO.getTotalSettlementPrice());
		orderDTO.setTotalDeductionAmount(submitOrderShopDTO.getProductTotalDeductionAmount());
		orderDTO.setProportion(submitOrderShopDTO.getProportion());
		orderDTO.setPayedFlag(false);
		orderDTO.setSelfPurchaseAmount(submitOrderShopDTO.getProductTotalSelfPurchaseAmount());

		// 获得系统配置自动确认收货时间
		ObjectMapper mapper = new ObjectMapper();
		OrderSettingDTO orderSetting = mapper.convertValue(sysParamsApi.getConfigDtoByParamName(SysParamNameEnum.ORDER_SETTING.name(), OrderSettingDTO.class).getData(), OrderSettingDTO.class);
		int receivingDay = orderSetting.getAutoReceiveProductDay();
		//设置收货倒计时 天
		orderDTO.setReceivingDay(receivingDay);
		orderDTO.setCommentValidDay(orderSetting.getOrderCommentValidDay());
		orderDTO.setCancelUnpayMinutes(orderSetting.getAutoCancelUnPayOrderMinutes());

		//配送方式
		orderDTO.setDeliveryType(confirmOrderBo.getDeliveryType());
		// 返回组装完成数据
		AssemblyOrderBO assemblyOrderBo = new AssemblyOrderBO();
		assemblyOrderBo.setOrderDTO(orderDTO);
		assemblyOrderBo.setOrderItemDTO(orderItemDtoList);
		return assemblyOrderBo;
	}


	/**
	 * 订单类型特有逻辑处理
	 *
	 * @param assemblyOrderBo 封装的组装数据BO
	 * @return 返回封装的组装数据BO
	 */
	protected abstract AssemblyOrderBO uniqueLogicProcess(AssemblyOrderBO assemblyOrderBo, String groupNumber, Long activityId);


	/**
	 * 订单完成后的特殊业务处理
	 * 可以用于创建其他业务通知
	 *
	 * @param assemblyOrderBo 封装的组装数据BO
	 * @return 返回封装的组装数据BO
	 */
	protected abstract AssemblyOrderBO specificBusiness(AssemblyOrderBO assemblyOrderBo);


	/**
	 * 保存完订单执行的业务处理
	 *
	 * @param assemblyOrderBo
	 * @return
	 */
	protected abstract AssemblyOrderBO specificAfterSaveOrder(AssemblyOrderBO assemblyOrderBo);

	/**
	 * 保存订单
	 *
	 * @param orderDTO 订单DTO
	 * @return 订单ID
	 */
	private R<Long> saveOrder(OrderDTO orderDTO) {

		Long result = orderService.save(orderDTO);
		if (result <= 0) {
			return R.fail("下单--保存订单失败");
		}
		orderDTO.setId(result);
		log.info("###### 保存订单成功 订单id {} ##### ", result);
		return R.ok(result);
	}

	/**
	 * 保存订单项
	 *
	 * @param orderId          订单ID
	 * @param orderItemDtoList 订单项列表DTO
	 * @return Boolean结果
	 */
	private R saveOrderItem(Long orderId, List<OrderItemDTO> orderItemDtoList) {

		orderItemDtoList.forEach(item -> {
			item.setOrderId(orderId);
		});

		Boolean result = orderItemService.saveOrderItems(orderItemDtoList);
		if (!result) {
			R.fail("下单--保存订单项失败");
		}
		log.info("###### 保存订单项成功 所属订单id {} ##### ", orderId);
		return R.ok();
	}

	private Map<Long, Integer> getReturnValidPeriod(List<Long> productIds) {
		Map<Long, Integer> resultMap = new HashMap<>(16);
		if (CollUtil.isEmpty(productIds)) {
			return resultMap;
		}
		R<List<ProductDTO>> productDTOR = productApi.queryAllByIds(productIds);
		if (!productDTOR.getSuccess()) {
			throw new BusinessException("获取商品信息失败，请刷新重试");
		}
		List<ProductDTO> productDTOS = productDTOR.getData();
		if (CollUtil.isEmpty(productDTOS)) {
			throw new BusinessException("无法找到商品信息，计算售后截止时间失败");
		}

		// 获取系统的订单运行退换货时间配置
		// 处理拆分为微服务后，强转失败问题
		ObjectMapper mapper = new ObjectMapper();
		OrderSettingDTO orderSetting = mapper.convertValue(sysParamsApi.getConfigDtoByParamName(SysParamNameEnum.ORDER_SETTING.name(), OrderSettingDTO.class).getData(), OrderSettingDTO.class);
		for (ProductDTO product : productDTOS) {
			Integer days = 0;
			Long categoryId = null;
			if (ObjectUtil.isNotEmpty(product.getGlobalThirdCatId())) {
				categoryId = product.getGlobalThirdCatId();
			} else if (ObjectUtil.isNotEmpty(product.getGlobalSecondCatId())) {
				categoryId = product.getGlobalSecondCatId();
			} else {
				categoryId = product.getGlobalFirstCatId();
			}
			CategoryBO category = categoryApi.getById(categoryId).getData();
			days = category.getReturnValidPeriod();
			//如果类目没设置退换货时间，则退换货时间为系统默认配置的时间
			if (days == null || days < 0) {
				days = orderSetting.getRefundOrExchangeValidDay();
			}
			resultMap.put(product.getId(), days);
		}
		return resultMap;
	}


	/**
	 * 下单扣减库存
	 *
	 * @param submitOrderShopDTO 提交订单的商品
	 * @return R
	 */
	protected abstract R deductionStockProcess(SubmitOrderShopDTO submitOrderShopDTO);


	/**
	 * 商品扩展校验
	 *
	 * @param skuDTO
	 * @return
	 */
	protected R extensionMethod(SubmitOrderSkuDTO skuDTO) {
		return R.ok();
	}


	/**
	 * 店铺扩展校验
	 *
	 * @param submitOrderShopDTO
	 * @return
	 */
	protected R extensionMethod(SubmitOrderShopDTO submitOrderShopDTO) {
		return R.ok();
	}


	/**
	 * 营销活动下单前检查
	 *
	 * @param confirmOrderBo
	 * @return
	 */
	protected abstract R activityCheckProcess(ConfirmOrderBO confirmOrderBo);

	public AssemblyOrderBO handleIntegralDetail(AssemblyOrderBO assemblyOrderBo) {
		return assemblyOrderBo;
	}
}
