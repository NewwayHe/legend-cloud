/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.mq.producer;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.legendshop.basic.api.AmqpTaskApi;
import com.legendshop.basic.api.SysParamsApi;
import com.legendshop.basic.dto.AmqpTaskDTO;
import com.legendshop.basic.dto.OrderSettingDTO;
import com.legendshop.basic.enums.SysParamNameEnum;
import com.legendshop.common.core.constant.RequestHeaderConstant;
import com.legendshop.common.rabbitmq.constants.AmqpConst;
import com.legendshop.common.rabbitmq.util.AmqpSendMsgUtil;
import com.legendshop.data.dto.DataUserPurchasingDTO;
import com.legendshop.order.dto.ConfirmRefundDTO;
import com.legendshop.order.dto.OrderDTO;
import com.legendshop.order.entity.Order;
import com.legendshop.order.enums.OrderTypeEnum;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 订单队列提供者
 *
 * @author legendshop
 */
@Slf4j
@Service
public class OrderProducerServiceImpl implements OrderProducerService {

	@Autowired
	private AmqpTaskApi amqpTaskApi;

	@Autowired
	private AmqpSendMsgUtil amqpSendMsgUtil;

	@Autowired
	private SysParamsApi sysParamsApi;

	@Override
	public void autoConfirmDelivery(Long orderId, Integer receivingDay) {
		String msg = "发送时间：" + new Date() + " orderId:" + orderId;
		// 获取系统的订单自动确认收货天数配置
		// 处理拆分为微服务后，强转失败问题
		if (receivingDay == null) {
			// 旧数据兼容，优先获取订单上的自动收货时间
			ObjectMapper mapper = new ObjectMapper();
			OrderSettingDTO orderSetting = mapper.convertValue(sysParamsApi.getConfigDtoByParamName(SysParamNameEnum.ORDER_SETTING.name(), OrderSettingDTO.class).getData(), OrderSettingDTO.class);
			receivingDay = orderSetting.getAutoReceiveProductDay();
		}

		AmqpTaskDTO amqpTaskDTO = new AmqpTaskDTO();
		amqpTaskDTO.setExchange(AmqpConst.DELAY_EXCHANGE);
		amqpTaskDTO.setRoutingKey(AmqpConst.DELAY_CONFIRMATION_RECEIVING_ROUTING_KEY);
		amqpTaskDTO.setMessage(msg);
		amqpTaskDTO.setDelayTime(DateUtil.offsetDay(DateUtil.date(), receivingDay));
		log.info("发送自动确认收货mq{}", amqpTaskDTO);
		amqpTaskApi.convertAndSend(amqpTaskDTO);
	}

	@Override
	public void autoRefundConfirmDelivery(Long refundId) {
		// 获取系统的订单自动确认收货天数配置
		// 处理拆分为微服务后，强转失败问题
		ObjectMapper mapper = new ObjectMapper();
		OrderSettingDTO orderSetting = mapper.convertValue(sysParamsApi.getConfigDtoByParamName(SysParamNameEnum.ORDER_SETTING.name(), OrderSettingDTO.class).getData(), OrderSettingDTO.class);

		AmqpTaskDTO amqpTaskDTO = new AmqpTaskDTO();
		amqpTaskDTO.setExchange(AmqpConst.DELAY_EXCHANGE);
		amqpTaskDTO.setRoutingKey(AmqpConst.DELAY_REFUND_CONFIRM_DELIVERY_ROUTING_KEY);
		amqpTaskDTO.setMessage(String.valueOf(refundId));
		amqpTaskDTO.setDelayTime(DateUtil.offsetDay(DateUtil.date(), orderSetting.getShopAutoReceiveProductDay().intValue()));
		amqpTaskApi.convertAndSend(amqpTaskDTO);
	}

	@Override
	public void balanceHandler(Long orderId, Date delay) {

		AmqpTaskDTO amqpTaskDTO = new AmqpTaskDTO();
		amqpTaskDTO.setExchange(AmqpConst.DELAY_EXCHANGE);
		amqpTaskDTO.setRoutingKey(AmqpConst.DELAY_CANCEL_UNPAID_PRE_SELL_ORDER_ROUTING_KEY);
		amqpTaskDTO.setMessage(String.valueOf(orderId));
		amqpTaskDTO.setDelayTime(delay);
		amqpTaskApi.convertAndSend(amqpTaskDTO);
	}

	@Override
	public void sendNewOrderMessage(List<Order> list) {
		HttpServletRequest http = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		list.forEach(a -> {
			DataUserPurchasingDTO dto = new DataUserPurchasingDTO();
			dto.setUserId(a.getUserId());
			dto.setCreateTime(new Date());
			dto.setQuantity(a.getProductQuantity());
			dto.setSource(http.getHeader(RequestHeaderConstant.SOURCE_KEY));
			dto.setTotalAmount(a.getActualTotalPrice());
			dto.setFreightPrice(a.getFreightPrice());
			dto.setOrderId(a.getId());
			dto.setShopId(a.getShopId());
			amqpSendMsgUtil.convertAndSend(com.legendshop.data.constants.AmqpConst.LEGENDSHOP_DATA_EXCHANGE, com.legendshop.data.constants.AmqpConst.LEGENDSHOP_DATA_ORDER_LOG_ROUTING_KEY, dto);
		});

	}

	@Override
	public void sendDealOrderMessage(Order order) {

		DataUserPurchasingDTO dto = new DataUserPurchasingDTO();
		dto.setUserId(order.getUserId());
		dto.setCreateTime(new Date());
		dto.setQuantity(order.getProductQuantity());
		dto.setDealAmount(order.getActualTotalPrice());
		dto.setShopId(order.getShopId());
		dto.setOrderId(order.getId());
		amqpSendMsgUtil.convertAndSend(com.legendshop.data.constants.AmqpConst.LEGENDSHOP_DATA_EXCHANGE, com.legendshop.data.constants.AmqpConst.LEGENDSHOP_DATA_ORDER_LOG_ROUTING_KEY, dto);

	}

	@Override
	public void autoAgreeRefund(Long refundId) {
		// 获取系统的订单自动同意退款天数配置
		// 处理拆分为微服务后，强转失败问题
		ObjectMapper mapper = new ObjectMapper();
		OrderSettingDTO orderSetting = mapper.convertValue(sysParamsApi.getConfigDtoByParamName(SysParamNameEnum.ORDER_SETTING.name(), OrderSettingDTO.class).getData(), OrderSettingDTO.class);

		AmqpTaskDTO amqpTaskDTO = new AmqpTaskDTO();
		amqpTaskDTO.setExchange(AmqpConst.DELAY_EXCHANGE);
		amqpTaskDTO.setRoutingKey(AmqpConst.DELAY_AGREE_REFUND_ROUTING_KEY);
		amqpTaskDTO.setMessage(String.valueOf(refundId));
		amqpTaskDTO.setDelayTime(DateUtil.offsetDay(DateUtil.date(), orderSetting.getAutoAgreeRefundDay()));
		amqpTaskApi.convertAndSend(amqpTaskDTO);
	}

	@Override
	public void autoAgreeAdminRefund(Long refundId, OrderTypeEnum orderTypeEnum) {
		log.info("开始发送平台自动同意退款队列~");
		if (null == refundId || null == orderTypeEnum) {
			log.info("平台自动同意退款队列发送失败，参数为空，refundId: {}, orderType: {}", refundId, orderTypeEnum);
			return;
		}
		ConfirmRefundDTO confirmRefundDTO = new ConfirmRefundDTO();
		confirmRefundDTO.setRefundId(refundId);
		confirmRefundDTO.setOrderType(orderTypeEnum.getValue());
		confirmRefundDTO.setAdminMessage("平台自动同意退款~");
		// 获取系统的订单自动同意退款天数配置
		// 处理拆分为微服务后，强转失败问题
		ObjectMapper mapper = new ObjectMapper();
		OrderSettingDTO orderSetting = mapper.convertValue(sysParamsApi.getNotCacheConfigByName(SysParamNameEnum.ORDER_SETTING.name(), OrderSettingDTO.class).getData(), OrderSettingDTO.class);
		//平台自动审核
		if (orderSetting.getAutomaticAuditAfterSalesOrder()) {
			amqpSendMsgUtil.convertAndSend(AmqpConst.DELAY_EXCHANGE,
					AmqpConst.DELAY_ADMIN_AGREE_REFUND_ROUTING_KEY, JSONUtil.toJsonStr(confirmRefundDTO), -1L, ChronoUnit.SECONDS, true);
		}
//		amqpSendMsgUtil.convertAndSend(AmqpConst.DELAY_EXCHANGE,
//				AmqpConst.DELAY_ADMIN_AGREE_REFUND_ROUTING_KEY, confirmRefundDTO, (long) orderSetting.getAutoAgreeRefundDay(), ChronoUnit.DAYS, true);
		//平台自动不自动审核
//		AmqpTaskDTO amqpTaskDTO = new AmqpTaskDTO();
//		amqpTaskDTO.setExchange(AmqpConst.DELAY_EXCHANGE);
//		amqpTaskDTO.setRoutingKey(AmqpConst.DELAY_ADMIN_AGREE_REFUND_ROUTING_KEY);
//		amqpTaskDTO.setMessage(JSONUtil.toJsonStr(confirmRefundDTO));
//		amqpTaskDTO.setDelayTime(DateUtil.offsetDay(DateUtil.date(), orderSetting.getAutoAgreeRefundDay()));
//		amqpTaskApi.convertAndSend(amqpTaskDTO);
	}

	@Override
	public void autoCancelRefund(Long refundId) {
		// 获取系统的订单自动同意退款天数配置
		// 处理拆分为微服务后，强转失败问题
		ObjectMapper mapper = new ObjectMapper();
		OrderSettingDTO orderSetting = mapper.convertValue(sysParamsApi.getConfigDtoByParamName(SysParamNameEnum.ORDER_SETTING.name(), OrderSettingDTO.class).getData(), OrderSettingDTO.class);

		AmqpTaskDTO amqpTaskDTO = new AmqpTaskDTO();
		amqpTaskDTO.setExchange(AmqpConst.DELAY_EXCHANGE);
		amqpTaskDTO.setRoutingKey(AmqpConst.DELAY_CANCEL_REFUND_ROUTING_KEY);
		amqpTaskDTO.setMessage(String.valueOf(refundId));
		amqpTaskDTO.setDelayTime(DateUtil.offsetDay(DateUtil.date(), orderSetting.getAutoCancelRefundDay()));
		amqpTaskApi.convertAndSend(amqpTaskDTO);
	}

	@Override
	public void saveOrderHistory(Long orderId, Long userId) {
		List<Long> paramList = new ArrayList<>();
		paramList.add(orderId);
		paramList.add(userId);
		amqpSendMsgUtil.convertAndSend(AmqpConst.ORDER_EXCHANGE, AmqpConst.ORDER_SAVE_HISTORY_ROUTING_KEY, JSONUtil.toJsonStr(paramList), true);
	}

	@Override
	@Deprecated
	public void deductionStock(String submitOrderShopDtoJsonStr) {
		amqpSendMsgUtil.convertAndSend(AmqpConst.ORDER_EXCHANGE, AmqpConst.ORDER_DEDUCTION_STOCK_ROUTING_KEY, submitOrderShopDtoJsonStr);
	}

	@Override
	public void saveProductSnapshot(String orderItemDtoListJsonStr) {
		amqpSendMsgUtil.convertAndSend(AmqpConst.ORDER_EXCHANGE, AmqpConst.ORDER_SAVE_PRODUCT_SNAPSHOT_ROUTING_KEY, orderItemDtoListJsonStr, true);
	}

	@Override
	public void autoCancelUnPayOrder(OrderDTO orderDTO, String orderType) {
		long cancelAfterTime = orderDTO.getCancelUnpayMinutes() * 60 / 2;
		//取消未支付订单之前发送站内信  订单支付时间过半时候提醒用户支付
		amqpSendMsgUtil.convertAndSend(AmqpConst.DELAY_EXCHANGE,
				AmqpConst.DELAY_CANCEL_UNPAY_ORDER_ROUTING_KEY_BEFORE, orderDTO.getId(), cancelAfterTime, ChronoUnit.SECONDS, true);

		//判断订单类型，获取不同的订单取消时间
		long cancelTime = DateUtil.between(DateUtil.date(), DateUtil.offsetMinute(orderDTO.getCreateTime(), orderDTO.getCancelUnpayMinutes()), DateUnit.SECOND, false);
		// 提前2秒取消订单，确保不会因为订单取消业务时间过长导致前端倒计时在0秒
		cancelTime -= 2L;
		//取消未支付订单
		log.info("取消未支付订单{}", orderDTO);
		amqpSendMsgUtil.convertAndSend(AmqpConst.DELAY_EXCHANGE,
				AmqpConst.DELAY_CANCEL_UNPAY_ORDER_ROUTING_KEY, orderDTO.getId(), cancelTime, ChronoUnit.SECONDS, true);
	}

	@Override
	public void applyDivide(List<String> numberList) {
		//TODO 待补充  分帐
//		amqpSendMsgUtil.convertAndSend(AmqpConstant.DISTRIBUTION_EXCHANGE, AmqpConstant.DISTRIBUTION_DIVIDE_APPLY_ROUTING_KEY, JSONUtil.toJsonStr(numberList));
	}

	@Override
	public void autoTreatComment(Long orderId, Integer commentValidDay) {
		AmqpTaskDTO amqpTaskDTO = new AmqpTaskDTO();
		amqpTaskDTO.setExchange(AmqpConst.DELAY_EXCHANGE);
		amqpTaskDTO.setRoutingKey(AmqpConst.DELAY_TREAT_COMMENT_ROUTING_KEY);
		amqpTaskDTO.setMessage(String.valueOf(orderId));
		amqpTaskDTO.setDelayTime(DateUtil.offsetDay(DateUtil.date(), commentValidDay));
		amqpTaskApi.convertAndSend(amqpTaskDTO);
	}


}
