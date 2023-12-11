/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.legendshop.basic.api.MessageApi;
import com.legendshop.basic.dto.MessagePushDTO;
import com.legendshop.basic.dto.MsgSendParamDTO;
import com.legendshop.basic.enums.MsgReceiverTypeEnum;
import com.legendshop.basic.enums.MsgSendParamEnum;
import com.legendshop.basic.enums.MsgSendTypeEnum;
import com.legendshop.basic.enums.SysParamNameEnum;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.expetion.BusinessException;
import com.legendshop.common.rabbitmq.constants.AmqpConst;
import com.legendshop.order.bo.ConfirmOrderBO;
import com.legendshop.order.dao.OrderDao;
import com.legendshop.order.dao.OrderHistoryDao;
import com.legendshop.order.dto.*;
import com.legendshop.order.entity.OrderHistory;
import com.legendshop.order.enums.OrderHistoryEnum;
import com.legendshop.order.enums.OrderStatusEnum;
import com.legendshop.order.enums.OrderTypeEnum;
import com.legendshop.order.service.*;
import com.legendshop.order.strategy.confirm.ConfirmOrderStrategyContext;
import com.legendshop.order.strategy.submit.SubmitOrderStrategyContext;
import com.legendshop.pay.api.PaySettlementApi;
import com.legendshop.pay.api.UserWalletApi;
import com.legendshop.pay.dto.PaySettlementDTO;
import com.legendshop.pay.dto.UserWalletPayDTO;
import com.legendshop.pay.enums.PaySettlementStateEnum;
import com.legendshop.product.api.ProductApi;
import com.legendshop.product.api.ProductSnapshotApi;
import com.legendshop.product.api.StockApi;
import com.legendshop.product.dto.ProductDTO;
import com.legendshop.product.dto.SaveProductSnapshotDTO;
import com.legendshop.user.api.UserDetailApi;
import com.legendshop.user.api.UserInvoiceApi;
import com.legendshop.user.bo.UserInvoiceBO;
import com.rabbitmq.client.Channel;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 订单核心业务服务实现
 *
 * @author legendshop
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OrderBusinessServiceImpl implements OrderBusinessService {

	private static final PasswordEncoder ENCODER = new BCryptPasswordEncoder();

	private final OrderDao orderDao;

	private final StockApi stockApi;

	private final OrderService orderService;

	private final ProductApi productApi;

	private final OrderHistoryDao orderHistoryDao;

	private final UserWalletApi userWalletApi;

	private final UserDetailApi userDetailApi;

	private final OrderItemService orderItemService;

	private final OrderCacheService orderCacheService;

	private final UserInvoiceApi userInvoiceApi;

	private final PaySettlementApi paySettlementApi;

	private final OrderHistoryService orderHistoryService;

	private final ProductSnapshotApi productSnapshotApi;
	private final MessageApi messagePushClient;
	private final SubmitOrderStrategyContext submitOrderStrategyContext;

	private final ConfirmOrderStrategyContext confirmOrderStrategyContext;

	@Override
	public R<ConfirmOrderBO> checkAndAssemblyConfirmOrder(ConfirmOrderDTO confirmOrderDTO) {
		return confirmOrderStrategyContext.executeCheckStrategy(confirmOrderDTO);
	}


	@Override
	public R<ConfirmOrderBO> getOrderInfo(ConfirmOrderBO confirmOrderBo) {
		return confirmOrderStrategyContext.executeConfirmStrategy(confirmOrderBo);
	}

	@Override
	public R<Object> submitOrder(ConfirmOrderBO confirmOrderBo) {
		return submitOrderStrategyContext.executeStrategy(confirmOrderBo);
	}


	@Override
	public R<UserInvoiceBO> invoiceChange(InvoiceChangeDTO invoiceChangeDTO, ConfirmOrderBO confirmOrderBo) {

		log.info("###### 更新发票信息 ##### ");
		UserInvoiceBO userInvoiceBo = null;
		List<SubmitOrderShopDTO> shopOrderList = confirmOrderBo.getShopOrderList();
		for (SubmitOrderShopDTO shop : shopOrderList) {
			// 只修改对应的商家订单信息
			if (!shop.getShopId().equals(invoiceChangeDTO.getShopId())) {
				continue;
			}
			// 用户选择的开启关闭发票标识
			shop.setUserInvoiceFlag(invoiceChangeDTO.getInvoiceFlag());
			//如果用户所选发票ID不为空， 则切换为用户所选的发票信息
			if (!ObjectUtil.isNotNull(invoiceChangeDTO.getInvoiceId())) {
				continue;
			}
			R<UserInvoiceBO> userInvoiceBOR = this.userInvoiceApi.getInvoiceBoById(invoiceChangeDTO.getInvoiceId());
			if (!userInvoiceBOR.getSuccess()) {
				throw new BusinessException(userInvoiceBOR.getMsg());
			}
			userInvoiceBo = userInvoiceBOR.getData();
			shop.setUserInvoiceBo(userInvoiceBo);
			shop.setInvoiceId(userInvoiceBo.getId());
			break;
		}
		// 更新预订单缓存
		orderCacheService.putConfirmOrderInfoCache(invoiceChangeDTO.getUserId(), confirmOrderBo);
		log.info("###### 更新发票信息完成 ，更新预订单缓存信息{} ##### ", JSONUtil.toJsonStr(confirmOrderBo));
		return R.ok(userInvoiceBo);
	}


	@Override
	public R<ConfirmOrderBO> useWallet(Long userId, UseWalletDTO dto) {
		// 获取预订单
		ConfirmOrderBO confirmOrderBo = this.orderCacheService.getConfirmOrderInfoCache(userId, dto.getConfirmOrderId());
		// 获取订单预选项
		UseWalletInfoDTO wallet = confirmOrderBo.getUseWalletInfo();
		wallet.setAllowed(Boolean.FALSE);
		wallet.setUseWallet(Boolean.FALSE);
		wallet.setAmount(BigDecimal.ZERO);
		// 获取用户钱包信息
		R<UserWalletPayDTO> userWalletPayResult = this.userWalletApi.payInfo();
		if (!userWalletPayResult.success()) {
			return R.fail(userWalletPayResult.getMsg());
		}
		UserWalletPayDTO data = userWalletPayResult.getData();
		data.setAmount(data.getAmount().setScale(2, RoundingMode.DOWN));
		// 钱包是否可用
		if (null == data.getAvailable() || !data.getAvailable() || BigDecimal.ZERO.compareTo(data.getAmount()) >= 0) {
			// 更新预订单缓存
			this.orderCacheService.putConfirmOrderInfoCache(userId, confirmOrderBo);
			throw new BusinessException("用户钱包余额不可用！");
		}
		//关闭使用余额
		if (null == dto.getUseWallet() || !dto.getUseWallet()) {
			// 更新预订单缓存
			this.orderCacheService.putConfirmOrderInfoCache(userId, confirmOrderBo);
			return R.ok(confirmOrderBo);
		}

		wallet.setAllowed(Boolean.TRUE);

		if (StringUtils.isBlank(dto.getPayPassword())) {
			// 更新预订单缓存
			this.orderCacheService.putConfirmOrderInfoCache(userId, confirmOrderBo);
			return R.ok(confirmOrderBo);
		}

		R<Void> result = this.userWalletApi.passwordValidation(userId, dto.getPayPassword());
		if (!result.success()) {
			return R.fail("支付密码错误！");
		}
		wallet.setUseWallet(Boolean.TRUE);

		// 提交总订单需支付金额
		BigDecimal amount = confirmOrderBo.getTotalAmount();
		if (ObjectUtil.isNotEmpty(confirmOrderBo.getDepositPrice())) {
			amount = confirmOrderBo.getDepositPrice();
		}
		// 当前用户可以用余额
		BigDecimal userWalletAmount = data.getAmount();
		if (amount.compareTo(userWalletAmount) >= 0) {
			wallet.setAmount(userWalletAmount);
		} else {
			wallet.setAmount(amount);
		}
		// 更新预订单缓存
		this.orderCacheService.putConfirmOrderInfoCache(userId, confirmOrderBo);
		return R.ok(confirmOrderBo);
	}

	@Override
	public void switchIntegralFlag(Boolean integralFlag, ConfirmOrderBO confirmOrderBo) {
		confirmOrderBo.setDeductionFlag(integralFlag);
		/*
		 * 重新处理积分抵扣
		 */
		for (SubmitOrderShopDTO s : confirmOrderBo.getShopOrderList()) {
			List<SubmitOrderSkuDTO> skuDTOList = s.getSkuList();
			//符合抵扣条件的sku
			List<SubmitOrderSkuDTO> validSkuList = skuDTOList.stream()
					.filter(sku -> ObjectUtil.isNotEmpty(sku.getDeductionFlag()))
					.collect(Collectors.toList());
			for (SubmitOrderSkuDTO skuDTO : validSkuList) {
				//更新抵扣商品属性
				skuDTO.setDeductionFlag(integralFlag);
				//实际金额调整
				skuDTO.setActualAmount(integralFlag ? NumberUtil.sub(skuDTO.getActualAmount(), skuDTO.getDeductionAmount()) : NumberUtil.add(skuDTO.getActualAmount(), skuDTO.getDeductionAmount()));
			}
		}

		// 如果是预售，则需要重新计算定金
		if (OrderTypeEnum.PRE_SALE.equals(confirmOrderBo.getType())) {
			this.executeSpecificBusinessStrategy(confirmOrderBo);
		}

		log.info("####重置每一个sku积分抵扣完毕########");

		// 更新预订单缓存
		orderCacheService.putConfirmOrderInfoCache(confirmOrderBo.getUserId(), confirmOrderBo);
	}


	@Override
	public void executeSpecificBusinessStrategy(ConfirmOrderBO confirmOrderBO) {
		confirmOrderStrategyContext.executeSpecificBusinessStrategy(confirmOrderBO);
	}

	@RabbitListener(bindings = @QueueBinding(value = @Queue(value = AmqpConst.ORDER_SAVE_HISTORY_QUEUE, durable = "true"),
			exchange = @Exchange(value = AmqpConst.ORDER_EXCHANGE), key = {AmqpConst.ORDER_SAVE_HISTORY_ROUTING_KEY}
	))
	public void saveOrderHistory(String msg, Message message, Channel channel) throws IOException {
		log.info("mq消费下单保存订单历史 接收到消息：" + msg);
		log.info("消息体：" + new String(message.getBody()));
		try {
			if (JSONUtil.isJsonArray(msg)) {

				JSONArray jsonArray = JSONUtil.parseArray(msg);
				List<Long> paramList = JSONUtil.toList(jsonArray, Long.class);

				// 操作信息
				Date currentDate = new Date();
				OrderHistoryDTO orderHistoryDTO = new OrderHistoryDTO();
				orderHistoryDTO.setStatus(OrderHistoryEnum.ORDER_SUBMIT.value());
				orderHistoryDTO.setOrderId(paramList.get(0));
				orderHistoryDTO.setReason("用户" + paramList.get(1) + "于" + DateUtil.date() + "提交订单");
				orderHistoryDTO.setCreateTime(currentDate);

				Long result = orderHistoryService.save(orderHistoryDTO);
				if (result <= 0) {
					throw new BusinessException("订单历史保存失败");
				}
				log.info("###### 保存订单历史成功 ##### ");
				channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
			}
			channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
		} catch (IOException e) {
			// 消息推送不要求重推
			channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
			e.printStackTrace();
		}
	}


	@Deprecated
	@Transactional(rollbackFor = Exception.class)
	@RabbitListener(bindings = @QueueBinding(value = @Queue(value = AmqpConst.ORDER_DEDUCTION_STOCK_QUEUE, durable = "true"),
			exchange = @Exchange(value = AmqpConst.ORDER_EXCHANGE), key = {AmqpConst.ORDER_DEDUCTION_STOCK_ROUTING_KEY}
	))
	public void deductionStock(String msg, Message message, Channel channel) throws IOException {
		log.info("mq消费下单扣减库存 接收到消息：" + msg);
		log.info("消息体：" + new String(message.getBody()));
		try {
			if (JSONUtil.isJsonObj(msg)) {

				SubmitOrderShopDTO submitOrderShopDTO = JSONUtil.toBean(msg, SubmitOrderShopDTO.class);
				log.info("########## 进入普通订单下单扣减库存判断逻辑   ##############");
				List<SubmitOrderSkuDTO> skuList = submitOrderShopDTO.getSkuList();
				for (SubmitOrderSkuDTO skuDTO : skuList) {

					// 获取商品是否为下单扣减库存
					ProductDTO productDTO = productApi.getDtoByProductId(skuDTO.getProductId()).getData();
					if (ObjectUtil.isNull(productDTO) || ObjectUtil.isNull(productDTO.getStockCounting())) {
						throw new BusinessException("下单--库存扣减失败");
					}

					if (!productDTO.getStockCounting()) {
						log.info("########## 普通订单下单扣减库存 商品{}  ##############", skuDTO);
						// 扣减库存
						boolean result = stockApi.reduceHold(skuDTO.getProductId(), skuDTO.getSkuId(), skuDTO.getTotalCount()).getData();
						channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
						if (!result) {
							throw new BusinessException("下单--库存扣减失败");
						}
						log.info("###### 普通订单下单扣减库存成功 ##### ");
					}
				}
			}
			channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
		} catch (IOException e) {
			// 消息推送不要求重推
			channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
			e.printStackTrace();
		}
	}


	@Transactional(rollbackFor = Exception.class)
	@RabbitListener(bindings = @QueueBinding(value = @Queue(value = AmqpConst.ORDER_SAVE_PRODUCT_SNAPSHOT_QUEUE, durable = "true"),
			exchange = @Exchange(value = AmqpConst.ORDER_EXCHANGE), key = {AmqpConst.ORDER_SAVE_PRODUCT_SNAPSHOT_ROUTING_KEY}
	))
	public void saveProductSnapshot(String msg, Message message, Channel channel) throws IOException {
		log.info("mq消费下单保存商品快照 接收到消息：" + msg);
		log.info("消息体：" + new String(message.getBody()));
		try {
			if (JSONUtil.isJsonArray(msg)) {

				JSONArray jsonArray = JSONUtil.parseArray(msg);
				List<OrderItemDTO> orderItemDtoList = JSONUtil.toList(jsonArray, OrderItemDTO.class);

				// 商品快照信息入参
				SaveProductSnapshotDTO saveProductSnapshotDTO = new SaveProductSnapshotDTO();
				for (OrderItemDTO item : orderItemDtoList) {

					saveProductSnapshotDTO.setProductId(item.getProductId());
					saveProductSnapshotDTO.setSkuId(item.getSkuId());
					saveProductSnapshotDTO.setAttribute(item.getAttribute());
					saveProductSnapshotDTO.setPic(item.getPic());
					saveProductSnapshotDTO.setPrice(item.getPrice());
					saveProductSnapshotDTO.setOriginalPrice(item.getOriginalPrice());
					saveProductSnapshotDTO.setBasketCount(item.getBasketCount());

					R<Long> saveResult = productSnapshotApi.saveProdSnapshot(saveProductSnapshotDTO);
					if (!saveResult.success()) {
						channel.basicNack(message.getMessageProperties().getDeliveryTag(), true, false);
						throw new BusinessException(saveResult.getMsg());
					}

					R<Void> updateResult = orderItemService.updateProductSnapshot(item.getOrderItemNumber(), saveResult.getData());
					if (!updateResult.success()) {
						channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
						throw new BusinessException("订单项更新商品快照失败 -- " + updateResult.getMsg());
					}
				}
				log.info("###### 保存商品快照成功 ##### ");
				channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
			}
			channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
		} catch (IOException e) {
			// 消息推送不要求重推
			channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
			e.printStackTrace();
		}
	}


	@GlobalTransactional
	@RabbitListener(bindings = @QueueBinding(value = @Queue(value = AmqpConst.DELAY_CANCEL_UNPAY_ORDER_QUEUE, durable = "true"),
			exchange = @Exchange(value = AmqpConst.DELAY_EXCHANGE,
					arguments = @Argument(name = "x-delayed-type", value = "direct"), delayed = Exchange.TRUE),
			key = AmqpConst.DELAY_CANCEL_UNPAY_ORDER_ROUTING_KEY))
	public void receiveDelay(Long orderId, Message message, Channel channel) {
		try {
			log.info("收到超时自动取消订单消息:订单号:" + orderId + ",当前时间：" + DateUtil.date() + "延迟时间：" + message.getMessageProperties().getReceivedDelay() + "秒");
			OrderDTO order = orderService.getById(orderId);
			if (ObjectUtil.isEmpty(order)) {
				log.error("订单已不存在,超时自动取消订单失败!");
				channel.basicNack(message.getMessageProperties().getDeliveryTag(), true, false);
				return;
			}

			// 判断支付单是否支付成功
			R<PaySettlementDTO> settlementDTOR = paySettlementApi.getPaidByOrderNumber(order.getOrderNumber());
			if (Optional.ofNullable(settlementDTOR.getData()).map(PaySettlementDTO::getState).orElse(PaySettlementStateEnum.CREATE.getCode()).equals(PaySettlementStateEnum.PAID.getCode())) {
				log.info("当前订单已支付成功，无法超时取消订单，订单号：{}", order.getOrderNumber());
				channel.basicNack(message.getMessageProperties().getDeliveryTag(), true, false);
				return;
			}

			int result = orderDao.cancelUnPayOrder(order.getId());
			if (result == 0) {
				//说明没有更新到
				log.error("订单已不存在,超时自动取消订单失败!");
				channel.basicNack(message.getMessageProperties().getDeliveryTag(), true, false);
				return;
			}
			//根据订单类型处理库存
			orderService.returnStocks(order);
			//只有普通订单才有优惠券
			if (OrderTypeEnum.ORDINARY.getValue().equals(order.getOrderType())) {
				//回退优惠券
				orderService.returnCoupon(order.getOrderNumber());
			}
			Date date = new Date();
			OrderHistory subHistory = new OrderHistory();
			subHistory.setCreateTime(date);
			subHistory.setOrderId(order.getId());
			subHistory.setStatus(OrderHistoryEnum.ORDER_OVER_TIME.value());
			subHistory.setReason(order.getUserId() + "系统于" + DateUtil.date() + "自动取消订单");
			orderHistoryDao.save(subHistory);

			// 余额退款
			orderService.giveBackUserWallet(order.getId(), false);

			channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
			log.info("超时自动取消订单订单成功!");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@RabbitListener(bindings = @QueueBinding(value = @Queue(value = AmqpConst.DELAY_CANCEL_UNPAY_ORDER_QUEUE_BEFORE, durable = "true"),
			exchange = @Exchange(value = AmqpConst.DELAY_EXCHANGE,
					arguments = @Argument(name = "x-delayed-type", value = "direct"), delayed = Exchange.TRUE),
			key = AmqpConst.DELAY_CANCEL_UNPAY_ORDER_ROUTING_KEY_BEFORE))
	public void receiveInformDelay(Long orderId, Message message, Channel channel) throws IOException {
		try {
			log.info("收到通知用户支付订单消息:订单号:" + orderId + ",当前时间：" + DateUtil.date() + "延迟时间：" + message.getMessageProperties().getReceivedDelay() + "秒");
			OrderDTO order = orderService.getById(orderId);
			if (ObjectUtil.isEmpty(order)) {
				log.error("订单已不存在,通知用户支付订单失败!");
				channel.basicNack(message.getMessageProperties().getDeliveryTag(), true, false);
				return;
			}
			if (!OrderStatusEnum.UNPAID.getValue().equals(order.getStatus())) {
				log.info("订单已支付，不再发送未支付提示消息");
				return;
			}

			//把要输出的订单时间转换为string
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String target = sdf.format(DateUtil.offsetMinute(order.getCreateTime(), 30));

			//模板替换数据
			List<MsgSendParamDTO> msgSendParamDTOS = new ArrayList<>();

			MsgSendParamDTO refundSnDTO = new MsgSendParamDTO(MsgSendParamEnum.ORDER_NUMBER, order.getOrderNumber(), "black");
			msgSendParamDTOS.add(refundSnDTO);

			//first.DATA，可自定义
			MsgSendParamDTO first = new MsgSendParamDTO(MsgSendParamEnum.FIRST, "您好，您有未支付的订单，请及时支付。", "black");
			MsgSendParamDTO KEYWORD1 = new MsgSendParamDTO(MsgSendParamEnum.KEYWORD1, order.getOrderNumber(), "black");
			MsgSendParamDTO KEYWORD2 = new MsgSendParamDTO(MsgSendParamEnum.KEYWORD2, order.getProductName(), "black");
			MsgSendParamDTO KEYWORD3 = new MsgSendParamDTO(MsgSendParamEnum.KEYWORD3, order.getActualTotalPrice().toString() + "元", "black");
			MsgSendParamDTO KEYWORD4 = new MsgSendParamDTO(MsgSendParamEnum.KEYWORD4, target, "black");
			msgSendParamDTOS.add(first);
			msgSendParamDTOS.add(KEYWORD1);
			msgSendParamDTOS.add(KEYWORD2);
			msgSendParamDTOS.add(KEYWORD3);
			msgSendParamDTOS.add(KEYWORD4);

			//模板参数替换内容 微信公众号发送通知提醒用户支付订单
			List<MsgSendParamDTO> urlParamList = new ArrayList<>();
			MsgSendParamDTO url = new MsgSendParamDTO(MsgSendParamEnum.ORDER_ID, orderId.toString(), "#173177");
			urlParamList.add(url);

			messagePushClient.push(new MessagePushDTO()
					.setMsgReceiverTypeEnum(MsgReceiverTypeEnum.ORDINARY_USER)
					//接收者Id数组
					.setReceiveIdArr(new Long[]{order.getUserId()})
					//类型
					.setMsgSendType(MsgSendTypeEnum.ORDER_TO_PLACE)
					//消息推送类型，根据类型找到设置
					.setSysParamNameEnum(SysParamNameEnum.ORDER_PLACE_SUCCESS_TO_USER)
					.setMsgSendParamDTOList(msgSendParamDTOS)
					//消息替换参数
					.setDetailId(orderId)
					//跳转路径参数 http://xxxx?xx=xx&xx=xx
					.setUrlParamList(urlParamList)
			);
			log.info("通知用户订单未支付!当前订单流水号为:{}", order.getOrderNumber());
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
		}
	}
}
