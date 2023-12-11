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
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import cn.legendshop.jpaplus.support.PageSupport;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.legendshop.basic.api.LocationApi;
import com.legendshop.basic.api.MessageApi;
import com.legendshop.basic.api.SysParamItemApi;
import com.legendshop.basic.api.SysParamsApi;
import com.legendshop.basic.dto.*;
import com.legendshop.basic.enums.MsgReceiverTypeEnum;
import com.legendshop.basic.enums.MsgSendParamEnum;
import com.legendshop.basic.enums.MsgSendTypeEnum;
import com.legendshop.basic.enums.SysParamNameEnum;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.expetion.BusinessException;
import com.legendshop.common.logistics.kuaidi100.contant.QueryTrackStatusEnum;
import com.legendshop.common.logistics.kuaidi100.response.SubscribePushParamResp;
import com.legendshop.common.rabbitmq.constants.AmqpConst;
import com.legendshop.common.rabbitmq.producer.LogisticsProducerService;
import com.legendshop.common.util.RandomUtil;
import com.legendshop.order.bo.OrderRefundReturnBO;
import com.legendshop.order.dao.*;
import com.legendshop.order.dto.*;
import com.legendshop.order.entity.*;
import com.legendshop.order.enums.*;
import com.legendshop.order.excel.OrderCancelExportDTO;
import com.legendshop.order.excel.OrderRefundExportDTO;
import com.legendshop.order.excel.ShopOrderBillRefundExportDTO;
import com.legendshop.order.mq.producer.OrderProducerService;
import com.legendshop.order.query.*;
import com.legendshop.order.service.LogisticsCompanyService;
import com.legendshop.order.service.OrderLogisticsService;
import com.legendshop.order.service.OrderRefundReturnService;
import com.legendshop.order.service.convert.OrderItemConverter;
import com.legendshop.order.service.convert.OrderRefundReturnConverter;
import com.legendshop.order.strategy.refund.ConfirmRefundStrategyContext;
import com.legendshop.pay.api.PayRefundSettlementApi;
import com.legendshop.pay.api.PaySettlementItemApi;
import com.legendshop.pay.dto.PayRefundSettlementDTO;
import com.legendshop.product.api.CategoryApi;
import com.legendshop.product.api.ProductApi;
import com.legendshop.product.bo.CategoryBO;
import com.legendshop.product.dto.ProductDTO;
import com.legendshop.product.enums.ProductDeliveryTypeEnum;
import com.legendshop.shop.api.ShopDetailApi;
import com.legendshop.shop.dto.ShopDetailDTO;
import com.legendshop.user.api.UserDetailApi;
import com.legendshop.user.dto.UserAddressDTO;
import com.legendshop.user.dto.UserDetailDTO;
import com.rabbitmq.client.Channel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 退款服务
 *
 * @author legendshop
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OrderRefundReturnServiceImpl implements OrderRefundReturnService {
	final LocationApi locationApi;
	final ShopDetailApi shopDetailApi;

	final OrderDao orderDao;

	final OrderItemDao orderItemDao;

	final ProductApi productApi;

	final CategoryApi categoryApi;

	final PreSellOrderDao preSellOrderDao;

	final OrderHistoryDao orderHistoryDao;

	final MessageApi messagePushClient;

	final SysParamsApi sysParamsApi;

	final SysParamItemApi sysParamItemApi;

	final OrderRefundReturnConverter converter;

	final OrderItemConverter orderItemConverter;

	final OrderRefundReturnDao orderRefundReturnDao;

	final OrderProducerService orderProducerService;

	final OrderLogisticsService orderLogisticsService;

	final LogisticsProducerService logisticsProducerService;

	final PayRefundSettlementApi payRefundSettlementApi;

	final OrderRefundReturnConverter orderRefundReturnConverter;

	final ConfirmRefundStrategyContext confirmRefundStrategyContext;

	final LogisticsCompanyService logisticsCompanyService;

	final UserDetailApi userDetailApi;

	final PaySettlementItemApi paySettlementItemApi;

	final OrderUserAddressDao orderUserAddressDao;

	/**
	 * 平台批量确认退款
	 *
	 * @param confirmRefundDTOS
	 * @return
	 */
	@Override
	public R<Void> confirmRefund(List<ConfirmRefundDTO> confirmRefundDTOS) {
		confirmRefundDTOS.forEach(confirmRefundStrategyContext::executeStrategy);
		return R.ok();
	}

	/**
	 * 平台确认退款
	 *
	 * @param confirmRefundDTO
	 * @return
	 */
	@Override
	public R confirmRefund(ConfirmRefundDTO confirmRefundDTO) {
		return confirmRefundStrategyContext.executeStrategy(confirmRefundDTO);
	}

	@Override
	public R confirmRefundList(ConfirmRefundListDTO confirmRefundDTO) {
		confirmRefundDTO.getConfirmRefundDTO().forEach(e -> e.setAdminMessage(confirmRefundDTO.getAdminMessage()));
		confirmRefundDTO.getConfirmRefundDTO().forEach(confirmRefundStrategyContext::executeStrategy);
		return R.ok();
	}


	@Override
	public PageSupport<OrderRefundReturnDTO> getByBillSnPage(OrderRefundReturnQuery orderRefundReturnQuery) {
		return orderRefundReturnConverter.page(orderRefundReturnDao.getByBillSnPage(orderRefundReturnQuery));
	}

	/**
	 * 申请全额退款
	 *
	 * @param applyOrderRefundDTO
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean applyOrderRefund(ApplyOrderRefundDTO applyOrderRefundDTO) {
		Order order = orderDao.getByOrderIdAndUserId(applyOrderRefundDTO.getOrderId(), applyOrderRefundDTO.getUserId());
		if (ObjectUtil.isEmpty(order)) {
			throw new BusinessException("对不起,您操作的订单不存在或已被删除!");
		}
		//判断该订单是否支持退款，需要处于待发货且退款状态为默认状态的订单才支持全额退款
		//待成团的也支持
		if (ProductDeliveryTypeEnum.EXPRESS_DELIVERY.getCode().equals(order.getDeliveryType())) {
			if ((!OrderStatusEnum.WAIT_DELIVERY.getValue().equals(order.getStatus()) && !OrderStatusEnum.WAIT.getValue().equals(order.getStatus()))
					|| !OrderRefundReturnStatusEnum.ORDER_NO_REFUND.value().equals(order.getRefundStatus())) {
				throw new BusinessException("对不起,您操作的订单不允许退款!");
			}
		} else {
			if ((!OrderStatusEnum.TAKE_DELIVER.getValue().equals(order.getStatus()) && !OrderStatusEnum.WAIT.getValue().equals(order.getStatus()))
					|| !OrderRefundReturnStatusEnum.ORDER_NO_REFUND.value().equals(order.getRefundStatus())) {
				throw new BusinessException("对不起,您操作的订单不允许退款!");
			}
		}
		//因为是全额退款，因此商品相关信息存储订单下的第一个订单项信息
		OrderItem orderItem = orderItemDao.getFirstOrderItemByOrderId(order.getId());
		//构建退款单
		OrderRefundReturn orderRefundReturn = this.buildOrderRefundReturn(order, orderItem);

		// 全单退款的积分也是订单的积分
		orderRefundReturn.setIntegral(order.getTotalIntegral());
		orderRefundReturn.setBuyerMessage(applyOrderRefundDTO.getBuyerMessage());
		orderRefundReturn.setReason(applyOrderRefundDTO.getReason());
		//全单退款的售后商品数等于订单的总商品件数
		orderRefundReturn.setGoodsNum(order.getProductQuantity());
		//全单退款的订单金额同时也是订单项金额和退款金额
		orderRefundReturn.setOrderItemMoney(order.getActualTotalPrice());
		orderRefundReturn.setRefundAmount(order.getActualTotalPrice());
		//申请类型：仅退款
		orderRefundReturn.setApplyType(OrderRefundReturnTypeEnum.REFUND.value());
		//单品id和商品id存储为0，用于辨别是否是全额退款
		orderRefundReturn.setSkuId(0L);
		orderRefundReturn.setProductId(0L);
		log.info("###### 开始保存退款记录 ####### ");
		Long refundId = orderRefundReturnDao.save(orderRefundReturn);
		if (refundId <= 0) {
			log.error("退款订单保存失败");
			throw new BusinessException("退款订单保存失败");
		}
		//更新订单退款状态
		orderDao.updateRefundState(order.getId(), OrderRefundReturnStatusEnum.ORDER_REFUND_PROCESSING.value());
		//更新订单项退款状态
		orderItemDao.updateRefundInfoByOrderId(order.getId(), refundId);
		log.info("###### 保存订单历史 ####### ");
		saveOrderHistory(orderRefundReturn.getOrderId(), OrderHistoryEnum.ORDER_RETURNMONEY.value(), "[退货/退款]" + applyOrderRefundDTO.getUserId() + "于" + DateUtil.date() + "发起订单退款");
		log.info("###### 发送退款通知站内信 ####### ");
		orderRefundReturn.setId(refundId);
		// 发送退款通知站内信给商家
		List<MsgSendParamDTO> msgSendParamDTOS = new ArrayList<>();
		//替换参数内容
		MsgSendParamDTO refundSnDTO = new MsgSendParamDTO(MsgSendParamEnum.REFUND_SN, orderRefundReturn.getRefundSn(), "black");
		MsgSendParamDTO productNameDTO = new MsgSendParamDTO(MsgSendParamEnum.PRODUCT_NAME, orderRefundReturn.getProductName(), "black");
		msgSendParamDTOS.add(refundSnDTO);
		msgSendParamDTOS.add(productNameDTO);
		messagePushClient.push(new MessagePushDTO()
				.setMsgReceiverTypeEnum(MsgReceiverTypeEnum.SHOP_USER)
				.setReceiveIdArr(new Long[]{orderRefundReturn.getShopId()})
				.setMsgSendType(MsgSendTypeEnum.ORDER_REFUND)
				.setSysParamNameEnum(SysParamNameEnum.ORDER_REFUND_TO_SHOP)
				.setMsgSendParamDTOList(msgSendParamDTOS)
				.setDetailId(orderRefundReturn.getId())
		);
		log.info("###### 插入商家自动同意退款的定时任务列表 ####### ");
		orderProducerService.autoAgreeRefund(refundId);
		return true;
	}

	/**
	 * 申请单项退款
	 *
	 * @param applyRefundDTO
	 * @param refundAmount
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public R<String> applyOrderItem(ApplyOrderItemRefundDTO applyRefundDTO, BigDecimal refundAmount) {
		Order order = orderDao.getByOrderIdAndUserId(applyRefundDTO.getOrderId(), applyRefundDTO.getUserId());
		if (ObjectUtil.isEmpty(order)) {
			throw new BusinessException("对不起,您操作的订单不存在或已被删除!");
		}
		Long orderItemId = applyRefundDTO.getOrderItemIds().get(0);
		OrderItem orderItem = orderItemDao.getById(orderItemId);
		if (ObjectUtil.isEmpty(orderItem)) {
			throw new BusinessException("对不起,您操作的订单不存在或已被删除!");
		}
		//校验退款金额
		if (refundAmount.compareTo(BigDecimal.ZERO) < 0 || refundAmount.compareTo(orderItem.getActualAmount()) > 0) {
			throw new BusinessException("对不起,您提交的金额有误!");
		}
		//检查订单状态是否满足退款条件,只有处于发货中和已完成之间的订单才可退款
		if (OrderStatusEnum.CONSIGNMENT.getValue() > order.getStatus() || OrderStatusEnum.SUCCESS.getValue() < order.getStatus()) {
			throw new BusinessException("对不起,您的订单状态不满足退款条件!");
		}
		if (OrderStatusEnum.SUCCESS.getValue().equals(order.getStatus())) {
			//检查订单是否在允许退款的时间内
			if (!this.isAllowOrderReturn(orderItem.getReturnDeadline())) {
				return R.fail("该商品已过售后期，退款失败！");
			}
		}
		//检查订单项是否重复申请
		if (OrderRefundReturnStatusEnum.ITEM_REFUND_PROCESSING.value().equals(orderItem.getRefundStatus())
				|| OrderRefundReturnStatusEnum.ITEM_REFUND_FINISH.value().equals(orderItem.getRefundStatus())) {

			// 发布重复提交异常站内信通知用户
			List<MsgSendParamDTO> msgSendParamDTOS = new ArrayList<>();
			//替换参数内容
			MsgSendParamDTO refundSnDTO = new MsgSendParamDTO(MsgSendParamEnum.PRODUCT_NAME, orderItem.getProductName(), "black");
			msgSendParamDTOS.add(refundSnDTO);
			messagePushClient.push(new MessagePushDTO()
					.setMsgReceiverTypeEnum(MsgReceiverTypeEnum.ORDINARY_USER)
					.setReceiveIdArr(new Long[]{applyRefundDTO.getUserId()})
					.setMsgSendType(MsgSendTypeEnum.ORDER_REFUND_REPEAT)
					.setSysParamNameEnum(SysParamNameEnum.REFUND_REPEAT_TO_USER)
					.setMsgSendParamDTOList(msgSendParamDTOS)
					.setDetailId(orderItem.getOrderId())
			);
			throw new BusinessException("对不起,该商品已提交售后，不能重复提交!");
		}
		//构建退款单
		OrderRefundReturn orderRefundReturn = this.buildOrderRefundReturn(order, orderItem);

		orderRefundReturn.setRefundAmount(refundAmount);
		orderRefundReturn.setBuyerMessage(applyRefundDTO.getBuyerMessage());
		orderRefundReturn.setReason(applyRefundDTO.getReason());
		orderRefundReturn.setPhotoVoucher(applyRefundDTO.getPhotoVoucher());
		//申请类型：仅退款
		orderRefundReturn.setApplyType(OrderRefundReturnTypeEnum.REFUND.value());
		log.info("###### 开始保存退款记录 ####### ");
		Long refundId = orderRefundReturnDao.save(orderRefundReturn);
		if (refundId <= 0) {
			log.error("退款订单保存失败");
			throw new BusinessException("退款订单保存失败");
		}
		// 判断是否最后一个发起退款的订单项
		Long count = orderItemDao.queryRefundItemCount(orderItem.getOrderNumber(), OrderRefundReturnStatusEnum.ITEM_NO_REFUND.value());
		if (count == 1) {
			//更新订单退款状态
			orderDao.updateRefundState(orderRefundReturn.getOrderId(), OrderRefundReturnStatusEnum.ORDER_REFUND_PROCESSING.value());
		}
		//更新订单项退款状态
		orderItemDao.updateRefundInfoById(orderItemId, refundId, refundAmount, OrderRefundReturnTypeEnum.REFUND.value());
		log.info("###### 保存订单历史 ####### ");
		saveOrderHistory(orderRefundReturn.getOrderId(), OrderHistoryEnum.ORDER_RETURNMONEY.value(), "[退货/退款]" + applyRefundDTO.getUserId() + "于" + DateUtil.date() + "发起订单退款");
		log.info("###### 发送退款通知站内信 ####### ");
		orderRefundReturn.setId(refundId);

		// 发送退款通知站内信给商家
		List<MsgSendParamDTO> msgSendParamDTOS = new ArrayList<>();
		//替换参数内容
		MsgSendParamDTO refundSnDTO = new MsgSendParamDTO(MsgSendParamEnum.REFUND_SN, orderRefundReturn.getRefundSn(), "black");
		MsgSendParamDTO productNameDTO = new MsgSendParamDTO(MsgSendParamEnum.PRODUCT_NAME, orderRefundReturn.getProductName(), "black");
		msgSendParamDTOS.add(refundSnDTO);
		msgSendParamDTOS.add(productNameDTO);
		messagePushClient.push(new MessagePushDTO()
				.setMsgReceiverTypeEnum(MsgReceiverTypeEnum.SHOP_USER)
				.setReceiveIdArr(new Long[]{orderRefundReturn.getShopId()})
				.setMsgSendType(MsgSendTypeEnum.ORDER_REFUND)
				.setSysParamNameEnum(SysParamNameEnum.ORDER_REFUND_TO_SHOP)
				.setMsgSendParamDTOList(msgSendParamDTOS)
				.setDetailId(orderRefundReturn.getId())
		);
		log.info("###### 插入商家自动同意退款的定时任务列表 ####### ");
		orderProducerService.autoAgreeRefund(refundId);
		//返回文案时间
		return R.ok();
	}

	/**
	 * 申请退货退款
	 *
	 * @param applyRefundDTO
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public R<String> applyOrderItem(ApplyOrderItemRefundDTO applyRefundDTO) {
		//通过订单id和userid获取订单
		Order order = orderDao.getByOrderIdAndUserId(applyRefundDTO.getOrderId(), applyRefundDTO.getUserId());
		if (ObjectUtil.isEmpty(order)) {
			throw new BusinessException("对不起,您操作的订单不存在或已被删除!");
		}
		//检查订单状态是否满足退款条件,只有处于发货中和已完成之间的订单才可退款
		if (OrderStatusEnum.CONSIGNMENT.getValue() > order.getStatus() || OrderStatusEnum.SUCCESS.getValue() < order.getStatus()) {
			throw new BusinessException("对不起,您的订单状态不满足退款条件!");
		}
		List<Long> orderItemIds = applyRefundDTO.getOrderItemIds();
		StringBuffer refuseProducts = new StringBuffer();
		for (Long orderItemId : orderItemIds) {
			//获取订单项数据
			OrderItem orderItem = orderItemDao.getById(orderItemId);
			if (OrderStatusEnum.SUCCESS.getValue().equals(order.getStatus())) {
				//检查订单是否在允许退款的时间内
				if (!this.isAllowOrderReturn(orderItem.getReturnDeadline())) {
					refuseProducts.append(orderItem.getProductName()).append(",");
					continue;
				}
			}
			//检查订单项是否重复申请
			if (OrderRefundReturnStatusEnum.ITEM_REFUND_PROCESSING.value().equals(orderItem.getRefundStatus())
					|| OrderRefundReturnStatusEnum.ITEM_REFUND_FINISH.value().equals(orderItem.getRefundStatus())) {
				// 发布重复提交异常站内信通知用户
				List<MsgSendParamDTO> msgSendParamDTOS = new ArrayList<>();
				//替换参数内容
				MsgSendParamDTO refundSnDTO = new MsgSendParamDTO(MsgSendParamEnum.PRODUCT_NAME, orderItem.getProductName(), "black");
				msgSendParamDTOS.add(refundSnDTO);
				messagePushClient.push(new MessagePushDTO()
						.setMsgReceiverTypeEnum(MsgReceiverTypeEnum.ORDINARY_USER)
						.setReceiveIdArr(new Long[]{orderItem.getUserId()})
						.setMsgSendType(MsgSendTypeEnum.ORDER_REFUND_REPEAT)
						.setSysParamNameEnum(SysParamNameEnum.REFUND_REPEAT_TO_USER)
						.setMsgSendParamDTOList(msgSendParamDTOS)
						.setDetailId(orderItem.getOrderId())
				);
				continue;
			}
			//构建退款单
			OrderRefundReturn orderRefundReturn = this.buildOrderRefundReturn(order, orderItem);

			orderRefundReturn.setBuyerMessage(applyRefundDTO.getBuyerMessage());
			orderRefundReturn.setReason(applyRefundDTO.getReason());
			orderRefundReturn.setPhotoVoucher(applyRefundDTO.getPhotoVoucher());
			//退货退订单项的实际金额，不含邮费
			orderRefundReturn.setRefundAmount(orderItem.getActualAmount());
			//申请类型：退货
			orderRefundReturn.setApplyType(OrderRefundReturnTypeEnum.REFUND_RETURN.value());
			orderRefundReturn.setIntegral(orderItem.getIntegral());
			log.info("###### 开始保存退货退款记录 ####### ");
			Long refundId = orderRefundReturnDao.save(orderRefundReturn);
			if (refundId <= 0) {
				log.error("退款订单保存失败");
				throw new BusinessException("退款订单保存失败");
			}
			//更新订单项退款状态
			orderItemDao.updateRefundInfoById(orderItemId, refundId, orderItem.getActualAmount(), OrderRefundReturnTypeEnum.REFUND_RETURN.value());
			log.info("###### 保存订单历史 ####### ");
			saveOrderHistory(orderRefundReturn.getOrderId(), OrderHistoryEnum.ORDER_RETURNGOOD.value(), "[退货/退款]" + applyRefundDTO.getUserId() + "于" + DateUtil.date() + "发起订单退货退款");
			log.info("###### 发送退货退款通知站内信 ####### ");
			orderRefundReturn.setId(refundId);

			// 发送退款通知站内信给商家
			List<MsgSendParamDTO> msgSendParamDTOS = new ArrayList<>();
			//替换参数内容
			MsgSendParamDTO refundSnDTO = new MsgSendParamDTO(MsgSendParamEnum.REFUND_SN, orderRefundReturn.getRefundSn(), "black");
			MsgSendParamDTO productNameDTO = new MsgSendParamDTO(MsgSendParamEnum.PRODUCT_NAME, orderRefundReturn.getProductName(), "black");
			msgSendParamDTOS.add(refundSnDTO);
			msgSendParamDTOS.add(productNameDTO);
			messagePushClient.push(new MessagePushDTO()
					.setMsgReceiverTypeEnum(MsgReceiverTypeEnum.SHOP_USER)
					.setReceiveIdArr(new Long[]{orderRefundReturn.getShopId()})
					.setMsgSendType(MsgSendTypeEnum.ORDER_REFUND)
					.setSysParamNameEnum(SysParamNameEnum.ORDER_REFUND_TO_SHOP)
					.setMsgSendParamDTOList(msgSendParamDTOS)
					.setDetailId(orderRefundReturn.getId())
			);

			log.info("###### 插入商家自动同意退款的定时任务列表 ####### ");
			orderProducerService.autoAgreeRefund(refundId);

		}
		// 判断是否最后一个发起退款的订单项
		Long count = orderItemDao.queryRefundItemCount(order.getOrderNumber(), OrderRefundReturnStatusEnum.ITEM_NO_REFUND.value());
		if (count == 0) {
			//更新订单退款状态

			orderDao.updateRefundState(order.getId(), OrderRefundReturnStatusEnum.ORDER_REFUND_PROCESSING.value());
		}
		if (ObjectUtil.isNotEmpty(refuseProducts)) {
			return R.fail("该商品已过售后期，退款失败");
		}
		return R.ok(refuseProducts.toString());
	}

	/**
	 * 组装退款参数
	 *
	 * @param order
	 * @param orderItem
	 * @return
	 */
	private OrderRefundReturn buildOrderRefundReturn(Order order, OrderItem orderItem) {
		OrderRefundReturn orderRefundReturn = new OrderRefundReturn();

		orderRefundReturn.setOrderNumber(order.getOrderNumber());
		orderRefundReturn.setUserId(order.getUserId());
		orderRefundReturn.setShopId(order.getShopId());
		orderRefundReturn.setShopName(order.getShopName());
		orderRefundReturn.setOrderId(order.getId());
		orderRefundReturn.setPaySettlementSn(order.getPaySettlementSn());
		orderRefundReturn.setOrderMoney(order.getActualTotalPrice());
		orderRefundReturn.setOrderType(order.getOrderType());
		if (ObjectUtil.isNotEmpty(orderItem.getIntegral())) {
			orderRefundReturn.setIntegral(orderItem.getIntegral());
		} else {
			orderRefundReturn.setIntegral(0);
		}
		orderRefundReturn.setGoodsNum(orderItem.getBasketCount());
		orderRefundReturn.setOrderItemMoney(orderItem.getActualAmount());
		orderRefundReturn.setProductName(orderItem.getProductName());
		orderRefundReturn.setProductImage(orderItem.getPic());
		orderRefundReturn.setProductAttribute(orderItem.getAttribute());
		orderRefundReturn.setOrderItemId(orderItem.getId());
		orderRefundReturn.setSkuId(orderItem.getSkuId());
		orderRefundReturn.setProductId(orderItem.getProductId());

		orderRefundReturn.setRefundSn(RandomUtil.getRandomSn());
		orderRefundReturn.setCreateTime(DateUtil.date());
		orderRefundReturn.setRefundSource(OrderRefundSouceEnum.USER.value());
		//申请状态：待卖家处理
		orderRefundReturn.setApplyStatus(OrderRefundReturnStatusEnum.APPLY_WAIT_SELLER.value());
		//商家申请取消订单

		//卖家处理状态：待审核
		orderRefundReturn.setSellerStatus(OrderRefundReturnStatusEnum.SELLER_WAIT_AUDIT.value());
		//处理退款状态：退款处理中
		orderRefundReturn.setHandleSuccessStatus(OrderRefundReturnStatusEnum.HANDLE_PROCESSS.value());
		return orderRefundReturn;
	}


	/**
	 * 保存进订单历史
	 *
	 * @param orderId
	 * @param status
	 * @param reason
	 */
	private void saveOrderHistory(Long orderId, String status, String reason) {
		OrderHistory OrderHistory = new OrderHistory();
		DateTime date = DateUtil.date();
		OrderHistory.setCreateTime(date);
		OrderHistory.setOrderId(orderId);
		OrderHistory.setStatus(status);
		OrderHistory.setReason(reason);
		orderHistoryDao.save(OrderHistory);
	}

	/**
	 * 自动审核通过商家超时未同意的订单
	 *
	 * @param refundId
	 * @param message
	 * @param channel
	 */
	@Transactional(rollbackFor = Exception.class)
	@RabbitListener(bindings = @QueueBinding(value = @Queue(value = AmqpConst.DELAY_AGREE_REFUND_QUEUE, durable = "true"),
			exchange = @Exchange(value = AmqpConst.DELAY_EXCHANGE,
					arguments = @Argument(name = "x-delayed-type", value = "direct"), delayed = Exchange.TRUE),
			key = AmqpConst.DELAY_AGREE_REFUND_ROUTING_KEY))
	public void agreeRefundDelay(Long refundId, Message message, Channel channel) throws IOException {

		log.info("收到自动同意退款消息:退款订单ID:" + refundId + ",当前时间：" + DateUtil.date() + "延迟时间：" + message.getMessageProperties().getReceivedDelay() + "秒");
		try {
			OrderRefundReturn orderRefundReturn = orderRefundReturnDao.getById(refundId);
			if (ObjectUtil.isEmpty(orderRefundReturn)) {
				log.error("mq超时自动审核通过退款订单失败,丢掉队列");
				channel.basicNack(message.getMessageProperties().getDeliveryTag(), true, false);
				throw new BusinessException("售后订单已不存在，请刷新页面或联系管理员");
			}
			//判断该退款订单是否已被处理
			if (OrderRefundReturnStatusEnum.SELLER_WAIT_AUDIT.value().equals(orderRefundReturn.getSellerStatus())
					|| OrderRefundReturnStatusEnum.APPLY_WAIT_SELLER.value().equals(orderRefundReturn.getApplyStatus())) {
				//对仅退款订单的处理
				if (OrderRefundReturnTypeEnum.REFUND.value().equals(orderRefundReturn.getApplyType())) {
					orderRefundReturn.setSellerTime(DateUtil.date());
					orderRefundReturn.setSellerMessage("超时未审核，自动通过");
					orderRefundReturn.setSellerStatus(OrderRefundReturnStatusEnum.SELLER_AGREE.value());
					//售后描述
					orderRefundReturn.setDescription("已退款");
					orderRefundReturn.setApplyStatus(OrderRefundReturnStatusEnum.APPLY_WAIT_ADMIN.value());
					boolean flag = auditRefund(orderRefundReturn.getId(), Boolean.TRUE, "系统自动审核通过", orderRefundReturn.getShopId());
					if (!flag) {
						log.error("mq超时自动审核通过退款订单失败,消除队列");
						channel.basicNack(message.getMessageProperties().getDeliveryTag(), true, false);
						throw new BusinessException("mq超时自动审核通过退款订单失败");
					}

					if (orderRefundReturnDao.update(orderRefundReturn) <= 0) {
						log.error("mq超时自动审核通过退款订单失败,重新放回队列");
						channel.basicReject(message.getMessageProperties().getDeliveryTag(), true);

						return;
					}
					//保存进订单历史
					saveOrderHistory(orderRefundReturn.getOrderId(), OrderHistoryEnum.AGREED_RETURNMONEY.value(), orderRefundReturn.getShopId() + "商家于" + DateUtil.date() + "自动同意退款");
					//发送站内信通知平台
					sendAuditPassToAdmin(orderRefundReturn.getRefundSn(), orderRefundReturn.getId());

					// 发送平台自动同意退款队列
					orderProducerService.autoAgreeAdminRefund(refundId, OrderTypeEnum.fromCode(orderRefundReturn.getOrderType()));
				}
				//对退款退货订单的处理
				if (OrderRefundReturnTypeEnum.REFUND_RETURN.value().equals(orderRefundReturn.getApplyType())) {
					orderRefundReturn.setSellerTime(DateUtil.date());
					orderRefundReturn.setSellerMessage("超时未审核，自动通过");
					//自动同意
					orderRefundReturn.setSellerStatus(OrderRefundReturnStatusEnum.SELLER_AGREE.value());
					//不弃货
					orderRefundReturn.setReturnType(OrderRefundReturnTypeEnum.NEED_GOODS.value());
					orderRefundReturn.setGoodsStatus(OrderRefundReturnStatusEnum.LOGISTICS_WAIT_DELIVER.value());
					//弃货
					boolean flag = auditRefundGood(orderRefundReturn.getId(), Boolean.TRUE, Boolean.FALSE, "系统自动审核通过", orderRefundReturn.getShopId());
					if (flag && orderRefundReturnDao.update(orderRefundReturn) <= 0) {
						log.error("mq超时自动审核通过退款订单失败,重新放回队列");
						channel.basicReject(message.getMessageProperties().getDeliveryTag(), true);
						return;
					}
					//保存进订单历史
					saveOrderHistory(orderRefundReturn.getOrderId(), OrderHistoryEnum.DISAGREED_RETURNGOOD.value(), orderRefundReturn.getShopId() + "商家于" + DateUtil.date() + "自动同意退货退款");
					//插入用户超时不发货自动取消售后状态的定时器
					orderProducerService.autoCancelRefund(orderRefundReturn.getId());
				}
				//发送站内信通知用户
				sendAuditToUser(orderRefundReturn.getRefundSn(), orderRefundReturn.getId(), orderRefundReturn.getUserId());
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("超时自动同意退款/退货失败，原因,{}", e.toString());
		} finally {
			log.info("超时自动同意退款/退货成功");
			channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
		}

	}

	/**
	 * 自动审核通过商家超时未同意的订单
	 *
	 * @param message
	 * @param channel
	 */
	@Transactional(rollbackFor = Exception.class)
	@RabbitListener(bindings = @QueueBinding(value = @Queue(value = AmqpConst.DELAY_ADMIN_AGREE_REFUND_QUEUE, durable = "true"),
			exchange = @Exchange(value = AmqpConst.DELAY_EXCHANGE,
					arguments = @Argument(name = "x-delayed-type", value = "direct"), delayed = Exchange.TRUE),
			key = AmqpConst.DELAY_ADMIN_AGREE_REFUND_ROUTING_KEY))
	public void adminAgreeRefundDelay(String dtoJson, Message message, Channel channel) throws IOException {
		ConfirmRefundDTO dto = JSONUtil.toBean(dtoJson, ConfirmRefundDTO.class);

		log.info("收到平台自动同意退款消息:退款订单ID:" + dto.getRefundId() + ",当前时间：" + DateUtil.date() + "延迟时间：" + message.getMessageProperties().getReceivedDelay() + "秒");
		try {
			this.confirmRefund(dto);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("平台超时自动同意退款/退货失败，原因,{}", e.toString());
		}
		log.info("平台超时自动同意退款/退货结束~");
		channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
	}

	/**
	 * 自动取消用户超时未发货的订单
	 *
	 * @param refundId
	 * @param message
	 * @param channel
	 */
	@Transactional(rollbackFor = Exception.class)
	@RabbitListener(bindings = @QueueBinding(value = @Queue(value = AmqpConst.DELAY_CANCEL_REFUND_QUEUE, durable = "true"),
			exchange = @Exchange(value = AmqpConst.DELAY_EXCHANGE,
					arguments = @Argument(name = "x-delayed-type", value = "direct"), delayed = Exchange.TRUE),
			key = AmqpConst.DELAY_CANCEL_REFUND_ROUTING_KEY))
	public void cancelRefundDelay(Long refundId, Message message, Channel channel) throws IOException {

		log.info("收到自动取消售后消息:订单号:" + refundId + ",当前时间：" + DateUtil.date() + "延迟时间：" + message.getMessageProperties().getReceivedDelay() + "秒");
		OrderRefundReturn orderRefundReturn = orderRefundReturnDao.getById(refundId);
		if (ObjectUtil.isEmpty(orderRefundReturn)) {
			log.error("售后订单已不存在，请刷新页面或联系管理员");
			channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
			return;
		}
		//判断该退款订单是否仍然处于待发货状态
		if (OrderRefundReturnStatusEnum.LOGISTICS_WAIT_DELIVER.value().equals(orderRefundReturn.getGoodsStatus())) {

			orderRefundReturn.setAdminMessage("用户超时未发货，系统取消售后");
			orderRefundReturn.setApplyStatus(OrderRefundReturnStatusEnum.CANCEL_APPLY.value());
			//取消时间
			orderRefundReturn.setCancellationTime(DateUtil.date());
			//取消类型
			orderRefundReturn.setCancellationType(OrderRefundReturnCancellationEnum.OVERDUECANEL.getValue());
			if (orderRefundReturnDao.update(orderRefundReturn) <= 0) {
				log.error("mq超时自动取消售后订单失败,重新放回队列");
				channel.basicReject(message.getMessageProperties().getDeliveryTag(), true);
				return;
			}
			//保存进订单历史
			StringBuilder sb = new StringBuilder();
			sb.append("系统于").append(DateUtil.date()).append("取消超时未发货的售后订单");
			saveOrderHistory(orderRefundReturn.getOrderId(), OrderHistoryEnum.REFUND_OVER_TIME.value(), sb.toString());

			// 更新订单项售后状态
			orderItemDao.updateRefundState(orderRefundReturn.getOrderItemId(), OrderRefundReturnStatusEnum.ITEM_NO_REFUND.value());
			// 综合订单的所有售后单判断订单售后状态
			changeOrderRefundStatus(orderRefundReturn.getOrderId(), refundId);
		}
		log.info("超时自动取消售后成功");
		channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
	}

	/**
	 * 发送审核通过站内信、微信给用户
	 *
	 * @param refundSn
	 * @param id
	 * @param userId
	 */
	private void sendAuditToUser(String refundSn, Long id, Long userId) {

		//模板替换数据
		List<MsgSendParamDTO> msgSendParamDTOS = new ArrayList<>();
		//first.DATA，可自定义
		MsgSendParamDTO first = new MsgSendParamDTO(MsgSendParamEnum.FIRST, "商家已经审核通过退款，请发回商品", "black");
		MsgSendParamDTO KEYWORD1 = new MsgSendParamDTO(MsgSendParamEnum.KEYWORD1, id.toString(), "black");
		//售后信息
		MsgSendParamDTO KEYWORD2 = new MsgSendParamDTO(MsgSendParamEnum.KEYWORD2, refundSn + "商家已处理，快去看看~", "black");
		//（备注）商家自定义
		MsgSendParamDTO KEYWORD3 = new MsgSendParamDTO(MsgSendParamEnum.KEYWORD3, " ", "black");
		//处理时间
		MsgSendParamDTO KEYWORD4 = new MsgSendParamDTO(MsgSendParamEnum.KEYWORD4, "", "black");
		//remark可自定义，暂时用价格替代
		MsgSendParamDTO REMARK = new MsgSendParamDTO(MsgSendParamEnum.REMARK, "", "black");
		msgSendParamDTOS.add(first);
		msgSendParamDTOS.add(KEYWORD1);
		msgSendParamDTOS.add(KEYWORD2);
		msgSendParamDTOS.add(KEYWORD3);
		msgSendParamDTOS.add(KEYWORD4);
		msgSendParamDTOS.add(REMARK);

		MsgSendParamDTO refundSnDTO = new MsgSendParamDTO(MsgSendParamEnum.REFUND_SN, refundSn, "black");
		msgSendParamDTOS.add(refundSnDTO);

		//模板参数替换内容
		List<MsgSendParamDTO> urlParamList = new ArrayList<>();
		MsgSendParamDTO url = new MsgSendParamDTO(MsgSendParamEnum.ORDER_ID, id.toString(), "#173177");
		urlParamList.add(url);


		messagePushClient.push(new MessagePushDTO()
				.setMsgReceiverTypeEnum(MsgReceiverTypeEnum.ORDINARY_USER)
				.setReceiveIdArr(new Long[]{userId})
				.setMsgSendType(MsgSendTypeEnum.ORDER_REFUND)
				.setSysParamNameEnum(SysParamNameEnum.REFUND_AUDIT_TO_USER)
				.setMsgSendParamDTOList(msgSendParamDTOS)
				.setDetailId(id)
				//跳转路径参数 http://xxxx?xx=xx&xx=xx
				.setUrlParamList(urlParamList)
		);
	}


	/**
	 * 发送审核通过站内信给系统
	 *
	 * @param refundSn
	 * @param refundId
	 */
	private void sendAuditPassToAdmin(String refundSn, Long refundId) {
		List<MsgSendParamDTO> msgSendParamDTOS = new ArrayList<>();
		//替换参数内容
		MsgSendParamDTO refundSnDTO = new MsgSendParamDTO(MsgSendParamEnum.REFUND_SN, refundSn, "black");
		msgSendParamDTOS.add(refundSnDTO);
		messagePushClient.push(new MessagePushDTO()
				.setMsgReceiverTypeEnum(MsgReceiverTypeEnum.ADMIN_USER)
				.setReceiveIdArr(new Long[]{-1L})
				.setMsgSendType(MsgSendTypeEnum.ORDER_REFUND)
				.setSysParamNameEnum(SysParamNameEnum.REFUND_PASS_TO_ADMIN)
				.setMsgSendParamDTOList(msgSendParamDTOS)
				.setDetailId(refundId)
		);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean cancelApply(String refundSn, Long userId) {
		//通过退款编号和用户Id获取退款订单
		OrderRefundReturn orderRefundReturn = orderRefundReturnDao.getReturnByRefundSnByUserId(refundSn, userId);
		if (ObjectUtil.isEmpty(orderRefundReturn)) {
			throw new BusinessException("对不起,您操作的记录不存在或已被删除!");
		}
		//只有审核状态处于待卖家审核状态，才允许退款
		if (!OrderRefundReturnStatusEnum.APPLY_WAIT_SELLER.value().equals(orderRefundReturn.getApplyStatus())) {
			throw new BusinessException("该订单已在审核流程，无法撤销，请联系商家");
		}
		//修改退款单状态
		orderRefundReturn.setApplyStatus(OrderRefundReturnStatusEnum.UNDO_APPLY.value());
		//用户取消时间
		orderRefundReturn.setCancellationTime(DateUtil.date());
		//取消类型
		orderRefundReturn.setCancellationType(OrderRefundReturnCancellationEnum.USERCANEL.getValue());
		if (orderRefundReturnDao.update(orderRefundReturn) <= 0) {
			log.error("取消售后申请失败");
			throw new BusinessException("取消售后申请失败");
		}

		//用户取消售后根据订单项修改 更新订单项的退款,退货状态
		List<OrderItem> orderItems = orderItemDao.getByOrderId(orderRefundReturn.getOrderId());
		for (OrderItem orderItem : orderItems) {
			orderItem.setRefundStatus(OrderRefundReturnStatusEnum.ITEM_NO_REFUND.value());
			orderItemDao.updateRefundState(orderItem.getId(), OrderRefundReturnStatusEnum.ITEM_NO_REFUND.value());
		}
		// 综合订单的所有售后单判断订单售后状态
		changeOrderRefundStatus(orderRefundReturn.getOrderId(), orderRefundReturn.getId());

		return true;
	}

	/**
	 * 审核退款订单
	 *
	 * @param refundId
	 * @param auditFlag
	 * @param sellerMessage
	 * @param shopId
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean auditRefund(Long refundId, Boolean auditFlag, String sellerMessage, Long shopId) {
		OrderRefundReturn orderRefundReturn = orderRefundReturnDao.getById(refundId);
		if (ObjectUtil.isEmpty(orderRefundReturn)) {
			throw new BusinessException("对不起，你要操作的记录已不存在");
		}
		if (!shopId.equals(orderRefundReturn.getShopId()) || !OrderRefundReturnTypeEnum.REFUND.value().equals(orderRefundReturn.getApplyType())) {
			throw new BusinessException("非法操作");
		}
		if (!OrderRefundReturnStatusEnum.APPLY_WAIT_SELLER.value().equals(orderRefundReturn.getApplyStatus())
				|| !OrderRefundReturnStatusEnum.SELLER_WAIT_AUDIT.value().equals(orderRefundReturn.getSellerStatus())) {
			throw new BusinessException("对不起，该售后订单不处于待确认状态");
		}
		orderRefundReturn.setSellerTime(DateUtil.date());
		orderRefundReturn.setSellerMessage(sellerMessage);
		//审核同意
		if (auditFlag) {
			orderProducerService.autoAgreeAdminRefund(refundId, OrderTypeEnum.fromCode(orderRefundReturn.getOrderType()));
			orderRefundReturn.setSellerStatus(OrderRefundReturnStatusEnum.SELLER_AGREE.value());
			orderRefundReturn.setApplyStatus(OrderRefundReturnStatusEnum.APPLY_WAIT_ADMIN.value());
			if (orderRefundReturnDao.update(orderRefundReturn) > 0) {
				//保存进订单历史
				saveOrderHistory(orderRefundReturn.getOrderId(), OrderHistoryEnum.AGREED_RETURNMONEY.value(), orderRefundReturn.getShopId() + "商家于" + DateUtil.date() + "同意退款");
				//发送站内信通知平台
				sendAuditPassToAdmin(orderRefundReturn.getRefundSn(), orderRefundReturn.getId());
				//发送站内信通知用户
				sendAuditToUser(orderRefundReturn.getRefundSn(), orderRefundReturn.getId(), orderRefundReturn.getUserId());
				return true;
			}
		} else {
			orderRefundReturn.setSellerStatus(OrderRefundReturnStatusEnum.SELLER_DISAGREE.value());
			orderRefundReturn.setApplyStatus(OrderRefundReturnStatusEnum.REJECTED_APPLY.value());
			if (orderRefundReturnDao.update(orderRefundReturn) > 0) {
				//更新订单退款状态
				orderDao.updateRefundState(orderRefundReturn.getOrderId(), OrderRefundReturnStatusEnum.ORDER_NO_REFUND.value());
				//更新订单项退款状态
				orderItemDao.updateByRefundId(orderRefundReturn.getId(), OrderRefundReturnStatusEnum.ITEM_NO_REFUND.value());
				//保存进订单历史
				saveOrderHistory(orderRefundReturn.getOrderId(), OrderHistoryEnum.DISAGREED_RETURNMONEY.value(), orderRefundReturn.getShopId() + "商家于" + DateUtil.date() + "拒绝退款");
				//发送站内信通知平台
				sendAuditPassToAdmin(orderRefundReturn.getRefundSn(), orderRefundReturn.getId());
				//发送站内信通知用户
				sendAuditToUser(orderRefundReturn.getRefundSn(), orderRefundReturn.getId(), orderRefundReturn.getUserId());
				return true;
			}
		}
		return false;
	}


	/**
	 * 审核退货订单
	 *
	 * @param refundId
	 * @param auditFlag
	 * @param abandonedGoodFlag
	 * @param sellerMessage
	 * @param shopId
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean auditRefundGood(Long refundId, Boolean auditFlag, Boolean abandonedGoodFlag, String sellerMessage, Long shopId) {
		OrderRefundReturn orderRefundReturn = orderRefundReturnDao.getById(refundId);
		if (ObjectUtil.isEmpty(orderRefundReturn)) {
			return false;
//			throw new BusinessException("对不起，你要操作的记录已不存在");
		}
		if (!shopId.equals(orderRefundReturn.getShopId()) || !OrderRefundReturnTypeEnum.REFUND_RETURN.value().equals(orderRefundReturn.getApplyType())) {
			return false;
//			throw new BusinessException("非法操作");
		}
		if (!OrderRefundReturnStatusEnum.APPLY_WAIT_SELLER.value().equals(orderRefundReturn.getApplyStatus())
				|| !OrderRefundReturnStatusEnum.SELLER_WAIT_AUDIT.value().equals(orderRefundReturn.getSellerStatus())) {
			return false;
//			throw new BusinessException("对不起，该售后订单不处于待确认状态");
		}
		orderRefundReturn.setSellerTime(DateUtil.date());
		orderRefundReturn.setSellerMessage(sellerMessage);
		//审核同意
		if (auditFlag) {
			orderRefundReturn.setSellerStatus(OrderRefundReturnStatusEnum.SELLER_AGREE.value());
			//商家选择弃货
			if (abandonedGoodFlag) {
				orderRefundReturn.setApplyStatus(OrderRefundReturnStatusEnum.APPLY_WAIT_ADMIN.value());
				orderRefundReturn.setReturnType(OrderRefundReturnTypeEnum.NO_NEED_GOODS.value());
				orderRefundReturn.setDescription("商家选择弃货");
				orderRefundReturn.setCancellationType(OrderRefundReturnCancellationEnum.ABANDONEDGOODS.getValue());
				if (orderRefundReturnDao.update(orderRefundReturn) > 0) {
					//保存进订单历史
					saveOrderHistory(orderRefundReturn.getOrderId(), OrderHistoryEnum.DISAGREED_RETURNGOOD.value(), orderRefundReturn.getShopId() + "商家于" + DateUtil.date() + "同意退货退款并选择弃货");
					//发送站内信通知平台
					sendAuditPassToAdmin(orderRefundReturn.getRefundSn(), orderRefundReturn.getId());
					//发送站内信通知用户
					sendAuditToUser(orderRefundReturn.getRefundSn(), orderRefundReturn.getId(), orderRefundReturn.getUserId());
					return true;
				}
			}
			//需要货物
			else {
				//校验是否有退货地址
				ShopDetailDTO shopDetailDTO = shopDetailApi.getById(shopId).getData();
				;
				checkReturnAddress(shopDetailDTO);

				orderRefundReturn.setReturnType(OrderRefundReturnTypeEnum.NEED_GOODS.value());
				orderRefundReturn.setGoodsStatus(OrderRefundReturnStatusEnum.LOGISTICS_WAIT_DELIVER.value());
				if (orderRefundReturnDao.update(orderRefundReturn) > 0) {
					//保存进订单历史
					saveOrderHistory(orderRefundReturn.getOrderId(), OrderHistoryEnum.DISAGREED_RETURNGOOD.value(), orderRefundReturn.getShopId() + "商家于" + DateUtil.date() + "同意退货退款");
					//发送站内信通知用户
					sendAuditToUser(orderRefundReturn.getRefundSn(), orderRefundReturn.getId(), orderRefundReturn.getUserId());
					//插入用户超时不发货自动取消售后状态的定时器
					orderProducerService.autoCancelRefund(refundId);
					return true;
				}
			}
		} else {
			orderRefundReturn.setSellerStatus(OrderRefundReturnStatusEnum.SELLER_DISAGREE.value());
			if (orderRefundReturnDao.update(orderRefundReturn) > 0) {
				//更新订单退款状态
				orderDao.updateRefundState(orderRefundReturn.getOrderId(), OrderRefundReturnStatusEnum.ORDER_NO_REFUND.value());
				//更新订单项退款状态
				orderItemDao.updateByRefundId(orderRefundReturn.getId(), OrderRefundReturnStatusEnum.ITEM_NO_REFUND.value());
				//保存进订单历史
				saveOrderHistory(orderRefundReturn.getOrderId(), OrderHistoryEnum.DISAGREED_RETURNGOOD.value(), orderRefundReturn.getShopId() + "商家于" + DateUtil.date() + "拒绝退货退款");
				//发送站内信通知平台
				sendAuditPassToAdmin(orderRefundReturn.getRefundSn(), orderRefundReturn.getId());
				//发送站内信通知用户
				sendAuditToUser(orderRefundReturn.getRefundSn(), orderRefundReturn.getId(), orderRefundReturn.getUserId());
				return true;
			}
		}
		return false;
	}

	/**
	 * 用户确认发货
	 *
	 * @param dto
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean confirmShip(UserReturnReturnLogisticsDTO dto) {
		OrderRefundReturn orderRefundReturn = orderRefundReturnDao.getById(dto.getRefundId());
		if (ObjectUtil.isEmpty(orderRefundReturn)) {
			throw new BusinessException("对不起，你要操作的记录已不存在");
		}
		if (!dto.getUserId().equals(orderRefundReturn.getUserId()) || !OrderRefundReturnStatusEnum.SELLER_AGREE.value().equals(orderRefundReturn.getSellerStatus())) {
			throw new BusinessException("非法操作");
		}
		LogisticsCompanyDTO companyDTO = logisticsCompanyService.getById(dto.getLogisticsId());
		if (ObjectUtil.isEmpty(companyDTO)) {
			throw new BusinessException("物流公司不存在！");
		}
		orderRefundReturn.setLogisticsNumber(dto.getLogisticsNumber());
		orderRefundReturn.setLogisticsId(dto.getLogisticsId());
		orderRefundReturn.setLogisticsCompanyCode(companyDTO.getCompanyCode());
		orderRefundReturn.setLogisticsCompanyName(companyDTO.getName());
		orderRefundReturn.setShipTime(DateUtil.date());
		orderRefundReturn.setGoodsStatus(OrderRefundReturnStatusEnum.LOGISTICS_RECEIVING.value());
		orderRefundReturn.setDescription("待用户寄回商品");
		orderRefundReturn.setCancellationType(OrderRefundReturnCancellationEnum.USERSENDSBACK.getValue());

		if (orderRefundReturnDao.update(orderRefundReturn) > 0) {
			//保存进订单历史
			saveOrderHistory(orderRefundReturn.getOrderId(), OrderHistoryEnum.BUYERS_RETURNGOOD.value(), orderRefundReturn.getUserId() + "用户于" + DateUtil.date() + "发货");
			//发送用户退款发货通知站内信给商家
			List<MsgSendParamDTO> msgSendParamDTOS = new ArrayList<>();
			//替换参数内容
			MsgSendParamDTO refundSnDTO = new MsgSendParamDTO(MsgSendParamEnum.REFUND_SN, orderRefundReturn.getRefundSn(), "black");
			msgSendParamDTOS.add(refundSnDTO);
			messagePushClient.push(new MessagePushDTO()
					.setMsgReceiverTypeEnum(MsgReceiverTypeEnum.SHOP_USER)
					.setReceiveIdArr(new Long[]{orderRefundReturn.getShopId()})
					.setMsgSendType(MsgSendTypeEnum.ORDER_REFUND)
					.setSysParamNameEnum(SysParamNameEnum.REFUND_SHIP_TO_SHOP)
					.setMsgSendParamDTOList(msgSendParamDTOS)
					.setDetailId(orderRefundReturn.getId())
			);

			//mq订阅退货物流信息
			logisticsProducerService.refundPoll(dto.getCompany(), dto.getLogisticsNumber());

			// 插入商家超时不收货自动确认收货的定时器 TODO 因为目前不知道用户输入的物流单号是否正确，所以从用户发货开始计算自动确认收货时间
			orderProducerService.autoRefundConfirmDelivery(orderRefundReturn.getId());
			return true;
		}
		return false;
	}

	/**
	 * 物流消息显示签收后、物流接口回调修改订单状态为已签收即为待收货
	 *
	 * @param subscribePushParamResp
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean refundCallBack(SubscribePushParamResp subscribePushParamResp) {
		log.info("回调的快递单号为：{} ,状态为{} ", subscribePushParamResp.getLastResult().getNu(), subscribePushParamResp.getLastResult().getState());
		//state	integer	0	快递单当前状态，包括0在途，1揽收，2疑难，3签收，4退签，5派件，6退回，7转投 等7个状态
		if (!subscribePushParamResp.getLastResult().getState().equals(QueryTrackStatusEnum.SIGN_FOR.getValue())) {
			//非签收状态不需要更新。
			return Boolean.TRUE;
		}
		OrderRefundReturn orderRefundReturn = orderRefundReturnDao.getByLogisticsNumber(subscribePushParamResp.getLastResult().getNu());
		if (ObjectUtil.isEmpty(orderRefundReturn)) {
			throw new BusinessException("订单已不存在，请刷新页面或联系管理员");
		}
		orderRefundReturn.setLogisticsReceivingTime(DateUtil.date());
		orderRefundReturn.setGoodsStatus(OrderRefundReturnStatusEnum.LOGISTICS_WAIT_RECEIVE.value());
		//保存进订单历史
		saveOrderHistory(orderRefundReturn.getOrderId(), OrderHistoryEnum.LOGISTICS_RECEIVING.value(), orderRefundReturn.getLogisticsNumber() + "退货物流单于" + DateUtil.date() + "已被签收");
		if (orderRefundReturnDao.updateLogisticsWaitReceive(orderRefundReturn.getId()) > 0) {
			log.info("更新物流对应的退货订单成功!");
			return true;
		}
		log.error("更新物流对应的退货订单失败!");

		return false;
	}

	@Override
	public PageSupport<OrderRefundReturnBO> page(OrderRefundReturnQuery query) {
		PageSupport<OrderRefundReturnBO> pageSupport = orderRefundReturnDao.page(query);
		List<OrderRefundReturnBO> resultList = pageSupport.getResultList();
		if (CollUtil.isEmpty(resultList)) {
			return pageSupport;
		}

		List<String> orderNumbers = resultList.stream().map(OrderRefundReturnBO::getOrderNumber).collect(Collectors.toList());
		List<OrderItemDTO> orderItemList = orderItemConverter.to(orderItemDao.queryByOrderNumbers(orderNumbers));
		List<Long> productIdList = orderItemList.stream().map(OrderItemDTO::getProductId).collect(Collectors.toList());
		R<List<ProductDTO>> productList = productApi.queryAllByIds(productIdList);
		if (CollUtil.isNotEmpty(productList.getData())) {
			Map<Long, ProductDTO> productMap = productList.getData().stream().collect(Collectors.toMap(ProductDTO::getId, e -> e));
			for (OrderItemDTO orderItemDTO : orderItemList) {
				if (!productMap.containsKey(orderItemDTO.getProductId())) {
					continue;
				}
				ProductDTO productDTO = productMap.get(orderItemDTO.getProductId());
				orderItemDTO.setProductStatus(productDTO.getStatus());
			}
		}
		Map<Long, List<OrderItemDTO>> orderItemMap = orderItemList.stream().collect(Collectors.groupingBy(OrderItemDTO::getOrderId));
		for (OrderRefundReturnBO orderRefundReturnBO : resultList) {
			if (!orderItemMap.containsKey(orderRefundReturnBO.getOrderId())) {
				continue;
			}

			List<OrderItemDTO> dtoList = orderItemMap.get(orderRefundReturnBO.getOrderId());
			//如果是全单退款，则需要获取该订单的所有订单项
			if (orderRefundReturnBO.getSkuId() != 0) {
				dtoList = dtoList.stream().filter(e -> e.getId().equals(orderRefundReturnBO.getOrderItemId())).collect(Collectors.toList());
			}

			orderRefundReturnBO.setOrderItemDTOList(dtoList);
		}
		return pageSupport;
	}

	@Override
	public OrderRefundReturnBO getUserRefundDetail(Long refundId, Long userId) {
		OrderRefundReturnBO refundDetail = this.getRefundDetail(refundId, userId, null);

		if (!userId.equals(refundDetail.getUserId())) {
			throw new BusinessException("非法操作");
		}
		if (refundDetail.getShopId() != null) {
			//更具商家ID获取退货信息赋值
			ShopDetailDTO data = shopDetailApi.getById(refundDetail.getShopId()).getData();
			log.info(String.valueOf(data));
			refundDetail.setReturnConsignee(data.getReturnConsignee());
			refundDetail.setReturnConsigneePhone(data.getReturnConsigneePhone());
			refundDetail.setReturnProvinceId(data.getReturnProvinceId());
			refundDetail.setReturnCityId(data.getReturnCityId());
			refundDetail.setReturnAreaId(data.getReturnAreaId());
			refundDetail.setReturnStreetId(data.getReturnStreetId());

			//拼接省城市地区街道
			if (data.getReturnProvinceId() != null) {
				LocationDetailDTO locationDetailDTO = new LocationDetailDTO();
				locationDetailDTO.setProvinceId(data.getReturnProvinceId());
				locationDetailDTO.setCityId(data.getReturnCityId());
				locationDetailDTO.setAreaId(data.getReturnAreaId());
				locationDetailDTO.setStreetId(data.getReturnStreetId());
				R<LocationDetailDTO> detailAddress = locationApi.getDetailAddress(locationDetailDTO);
				if (detailAddress.success()) {
					refundDetail.setReturnShopAddress(detailAddress.getData().getDetailAddress());
				}
			}
			//详细地址
			refundDetail.setReturnShopAddr(data.getReturnShopAddr());

			//用户信息
			UserAddressDTO userAddressDTO = orderDao.getAddressByOrderId(refundDetail.getOrderId());
			if (ObjectUtil.isNotNull(userAddressDTO)) {
				refundDetail.setMobile(userAddressDTO.getMobile());
				refundDetail.setReceiver(userAddressDTO.getReceiver());
			}
			R<UserDetailDTO> userDetail = userDetailApi.getUserDetailById(userId);
			if (ObjectUtil.isNotNull(userDetail.getData())) {
				refundDetail.setNickName(userDetail.getData().getNickName());
			}
		}
		return refundDetail;
	}

	private OrderRefundReturnBO getRefundDetail(Long refundId, Long userId, Long shopId) {
		OrderRefundReturnBO orderRefundReturnBO;
		if (userId != null) {
			orderRefundReturnBO = orderRefundReturnDao.getUserRefundDetail(refundId, userId);
		} else if (shopId != null) {
			orderRefundReturnBO = orderRefundReturnDao.getShopRefundDetail(refundId, shopId);
		} else {
			orderRefundReturnBO = orderRefundReturnDao.getAdminRefundDetail(refundId);
		}
		if (ObjectUtil.isEmpty(orderRefundReturnBO)) {
			throw new BusinessException("您要操作的订单不存在或已被删除");
		}
		//是否自提点
		boolean isSinceMention = false;

		{
			// 查询订单收货人跟手机号码
			OrderUserAddress orderUserAddress = orderUserAddressDao.getByOrderId(orderRefundReturnBO.getOrderId());
			if (ObjectUtil.isNotNull(orderUserAddress)) {
				orderRefundReturnBO.setReceiver(orderUserAddress.getReceiver());
				orderRefundReturnBO.setMobile(orderUserAddress.getMobile());
			}

		}
		List<OrderItemDTO> orderItemDTOList = new ArrayList<>();
		//如果是全单退款，则需要获取该订单的所有订单项
		if (orderRefundReturnBO.getSkuId() == 0) {
			orderItemDTOList = orderItemConverter.to(orderItemDao.getByOrderId(orderRefundReturnBO.getOrderId()));
		} else {
			OrderItemDTO orderItemDTO = orderItemConverter.to(orderItemDao.getById(orderRefundReturnBO.getOrderItemId()));
			orderItemDTOList.add(orderItemDTO);
		}
		// 预售订单返回预售支付金额
		Order order = orderDao.getById(orderRefundReturnBO.getOrderId());
		if (OrderTypeEnum.PRE_SALE.getValue().equals(order.getOrderType())) {
			PreSellOrder sellOrder = preSellOrderDao.getByOrderId(order.getId());
			orderItemDTOList.get(0).setActualAmount(sellOrder.getActualAmount());
		}
		orderRefundReturnBO.setOrderItemDTOList(orderItemDTOList);

		Date shipTime = orderRefundReturnBO.getShipTime();
		orderRefundReturnBO.setAutoCancelRefundTime(0L);
		if (OrderRefundReturnTypeEnum.REFUND.value().equals(orderRefundReturnBO.getApplyType())) {
			if (OrderRefundReturnStatusEnum.SELLER_WAIT_AUDIT.value().equals(orderRefundReturnBO.getSellerStatus())) {
				orderRefundReturnBO.setAutoCancelRefundTime(autoShopCancelRefundTime(DateUtil.date(orderRefundReturnBO.getCreateTime()).toCalendar()) < 0 ? 0L : autoShopCancelRefundTime(DateUtil.date(orderRefundReturnBO.getCreateTime()).toCalendar()));
			}
		}
		if (OrderRefundReturnTypeEnum.REFUND_RETURN.value().equals(orderRefundReturnBO.getApplyType())) {
			//todo 设置文案时间
			//第一申请使用申请时间倒计时 待确认
			//待确认 时间
			orderRefundReturnBO.setAutoCancelRefundTime(autoShopCancelRefundTime(DateUtil.date(orderRefundReturnBO.getCreateTime()).toCalendar()) < 0 ? 0L : autoShopCancelRefundTime(DateUtil.date(orderRefundReturnBO.getCreateTime()).toCalendar()));

			//待收货（待用户寄回商品） 时间
			if (OrderRefundReturnStatusEnum.SELLER_AGREE.value().equals(orderRefundReturnBO.getSellerStatus())) {
				orderRefundReturnBO.setAutoCancelRefundTime(autoCancelRefundTime(DateUtil.date(orderRefundReturnBO.getCreateTime()).toCalendar()) < 0 ? 0L : autoCancelRefundTime(DateUtil.date(orderRefundReturnBO.getCreateTime()).toCalendar()));
			}

			if (ObjectUtil.isNotEmpty(shipTime)) {
				//goods_status 待收货（售后商品待收货验收）
				if (OrderRefundReturnStatusEnum.LOGISTICS_RECEIVING.value().equals(orderRefundReturnBO.getGoodsStatus())) {
					orderRefundReturnBO.setAutoCancelRefundTime(autoShopCancelRefundTime(DateUtil.date(shipTime).toCalendar()) < 0 ? 0L : autoShopCancelRefundTime(DateUtil.date(shipTime).toCalendar()));
				}
			}
		}


		orderRefundReturnBO.setCanReapply(false);
		if (null != userId) {
			orderRefundReturnBO.setCanReapply(true);
			for (OrderItemDTO orderItemDTO : orderItemDTOList) {
				// 如果已经超过售后期，则不可申请
				if (!this.isAllowOrderReturn(orderItemDTO.getReturnDeadline())) {
					orderRefundReturnBO.setCanReapply(false);
					break;
				}

				// 如果正在申请或已完成，则不可申请
				if (OrderRefundReturnStatusEnum.ITEM_REFUND_PROCESSING.value().equals(orderItemDTO.getRefundStatus())
						|| OrderRefundReturnStatusEnum.ITEM_REFUND_FINISH.value().equals(orderItemDTO.getRefundStatus())) {
					orderRefundReturnBO.setCanReapply(false);
					break;
				}
			}
		}

		List<OrderPayTypeDTO> payTypeDTOList = new ArrayList<>();
		//同意并退款后，显示退款明细
		if (OrderRefundHandleStatusEnum.SUCCESS.value().equals(orderRefundReturnBO.getHandleSuccessStatus().toString())) {
			log.info("#######订单：{}，同意退款后显示退款明细#######", orderRefundReturnBO.getOrderNumber());
			//获取支付结算单
			R<List<PayRefundSettlementDTO>> paySettlementDTOList = payRefundSettlementApi.queryByRefundSn(orderRefundReturnBO.getRefundSn());
			if (ObjectUtil.isNull(paySettlementDTOList.getData())) {
				throw new BusinessException("退款金额明细结算有误");
			}
			log.info("#######开始组装退款明细#######");
			List<PayRefundSettlementDTO> paySettlementItemDTOList = paySettlementDTOList.getData();
			//遍历支付结算单，获取支付类型、支付金额
			for (PayRefundSettlementDTO paySettlementItemDTO : paySettlementItemDTOList) {
				OrderPayTypeDTO orderPayTypeDTO = new OrderPayTypeDTO();
				orderPayTypeDTO.setPayName(PayTypeEnum.getName(paySettlementItemDTO.getPayRefundType()));
				orderPayTypeDTO.setPayAmount(paySettlementItemDTO.getRefundAmount());
				payTypeDTOList.add(orderPayTypeDTO);
			}

			//其余请款显示退款金额
		} else {
			log.info("#######订单：{}，显示退款金额#######", orderRefundReturnBO.getOrderNumber());
			OrderPayTypeDTO orderPayTypeDTO = new OrderPayTypeDTO();
			orderPayTypeDTO.setPayAmount(orderRefundReturnBO.getRefundAmount());
			orderPayTypeDTO.setPayName("金额");
			payTypeDTOList.add(orderPayTypeDTO);
		}

		//获取退款积分
		if (ObjectUtil.isNotNull(orderRefundReturnBO.getIntegral())) {
			log.info("#######订单：{}，显示退款积分#######", orderRefundReturnBO.getOrderNumber());
			OrderPayTypeDTO orderPayTypeDTO = new OrderPayTypeDTO();
			orderPayTypeDTO.setPayName("积分");
			orderPayTypeDTO.setPayAmount(BigDecimal.valueOf(orderRefundReturnBO.getIntegral()));
			payTypeDTOList.add(orderPayTypeDTO);
		}
		orderRefundReturnBO.setRefundAmountDetailList(payTypeDTOList);

		if (ObjectUtil.isNotEmpty(orderRefundReturnBO.getGoodsStatus()) && OrderRefundReturnStatusEnum.LOGISTICS_WAIT_DELIVER.value() < orderRefundReturnBO.getGoodsStatus()) {
			R<RefundReturnLogisticsDTO> orderLogisticsDTOR = orderLogisticsService.queryRefundReturnLogisticsInformation(orderRefundReturnBO);
			if (orderLogisticsDTOR.success()) {
				orderRefundReturnBO.setRefundReturnLogisticsDTO(orderLogisticsDTOR.getData());
			}
		}

		if (OrderStatusEnum.UNPAID.getValue() > orderRefundReturnBO.getOrderStatus() && OrderStatusEnum.WAIT_DELIVERY.getValue() < orderRefundReturnBO.getOrderStatus()) {
			orderRefundReturnBO.setFreightPrice(null);
		}

		return orderRefundReturnBO;
	}

	@Override
	public OrderRefundReturnBO getShopRefundDetail(Long refundId, Long shopId) {
		return this.getRefundDetail(refundId, null, shopId);
	}

	@Override
	public OrderRefundReturnBO getAdminRefundDetail(Long refundId) {
		return this.getRefundDetail(refundId, null, null);
	}

	@Override
	public Long save(OrderRefundReturnDTO orderRefundReturnDTO) {
		return orderRefundReturnDao.save(converter.from(orderRefundReturnDTO));
	}

	/**
	 * 插入商家超时不收货自动确认收货的定时器
	 *
	 * @param refundId the 退款Id
	 */
	@RabbitListener(bindings = @QueueBinding(value = @Queue(value = AmqpConst.DELAY_REFUND_CONFIRM_DELIVERY_QUEUE, durable = "true"),
			exchange = @Exchange(value = AmqpConst.DELAY_EXCHANGE,
					arguments = @Argument(name = "x-delayed-type", value = "direct"), delayed = Exchange.TRUE),
			key = AmqpConst.DELAY_REFUND_CONFIRM_DELIVERY_ROUTING_KEY))
	public void receiveDelay(Long refundId, Message message, Channel channel) throws IOException {
		log.info("收到自动确认收货消息:订单号:" + refundId + ",当前时间：" + DateUtil.date() + "延迟时间：" + message.getMessageProperties().getReceivedDelay() + "秒");
		OrderRefundReturn orderRefundReturn = orderRefundReturnDao.getById(refundId);
		if (ObjectUtil.isEmpty(orderRefundReturn)) {
			channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
			throw new BusinessException("订单已不存在，请刷新页面或联系管理员");
		}
		if (orderRefundReturnDao.updateLogisticsReceived(orderRefundReturn.getId()) <= 0) {
			log.info("mq超时自动确认收货失败");
			channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
			return;
		}

		//根据快递100查询退货物流信息 是否有效
		R<RefundReturnLogisticsDTO> orderLogisticsDTOR = orderLogisticsService.queryRefundReturnLogisticsInformation(Convert.convert(OrderRefundReturnBO.class, orderRefundReturn));
		//售后描述
		orderRefundReturn.setDescription("售后商品待收货验收");
		orderRefundReturn.setSellerStatus(OrderRefundReturnStatusEnum.SELLER_AGREE.value());
		orderRefundReturn.setCancellationType(OrderRefundReturnCancellationEnum.ORDERABNORMAL.getValue());

		if (orderLogisticsDTOR.success()) {
			if (StrUtil.isNotEmpty(orderLogisticsDTOR.getData().getTrackingInformation())) {
				//售后描述
				orderRefundReturn.setDescription("系统自动处理");
				//自动同意
				orderRefundReturn.setCancellationType(OrderRefundReturnCancellationEnum.SYSTEMAUTOMATICALLY.getValue());
				orderRefundReturn.setApplyStatus(OrderRefundReturnStatusEnum.APPLY_WAIT_ADMIN.value());
				orderRefundReturn.setGoodsStatus(OrderRefundReturnStatusEnum.LOGISTICS_RECEIVED.value());
				//保存进订单历史
				saveOrderHistory(orderRefundReturn.getOrderId(), OrderHistoryEnum.TAKE_DELIVER_TIME.value(), orderRefundReturn.getShopId() + "商家于" + DateUtil.date() + "自动确认收货");
				log.info("超时自动确认收货成功");
				orderProducerService.autoAgreeAdminRefund(refundId, OrderTypeEnum.fromCode(orderRefundReturn.getOrderType()));
			}
			log.info("不存在物流信息!");
		}

		orderRefundReturnDao.update(orderRefundReturn);
		//发送站内信通知平台
		sendAuditPassToAdmin(orderRefundReturn.getRefundSn(), orderRefundReturn.getId());
		channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
	}


	/**
	 * 商家确认收货
	 *
	 * @param refundId
	 * @param shopId
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean confirmDeliver(Long refundId, Long shopId) {
		OrderRefundReturn orderRefundReturn = orderRefundReturnDao.getById(refundId);
		if (ObjectUtil.isEmpty(orderRefundReturn)) {
			throw new BusinessException("您要操作的订单不存在或已被删除，请刷新页面或联系管理员");
		}
		if (!shopId.equals(orderRefundReturn.getShopId())) {
			throw new BusinessException("非法操作");
		}
		orderRefundReturn.setReceiveTime(DateUtil.date());
		orderRefundReturn.setSellerStatus(OrderRefundReturnStatusEnum.SELLER_AGREE.value());
		orderRefundReturn.setApplyStatus(OrderRefundReturnStatusEnum.APPLY_WAIT_ADMIN.value());
		orderRefundReturn.setGoodsStatus(OrderRefundReturnStatusEnum.LOGISTICS_RECEIVED.value());
		//售后描述
		orderRefundReturn.setDescription("商家已确认收货");
		orderRefundReturn.setCancellationType(OrderRefundReturnCancellationEnum.SHOPCONFIRMRECEIPT.getValue());
		if (orderRefundReturnDao.update(orderRefundReturn) > 0) {
			log.info("###### 插入商家自动同意退款的定时任务列表 ####### ");
			orderProducerService.autoAgreeAdminRefund(refundId, OrderTypeEnum.fromCode(orderRefundReturn.getOrderType()));
			//保存进订单历史
			saveOrderHistory(orderRefundReturn.getOrderId(), OrderHistoryEnum.SELLER_RETURNGOOD.value(), orderRefundReturn.getShopId() + "商家于" + DateUtil.date() + "确认收货");
			//发送站内信通知平台
			sendAuditPassToAdmin(orderRefundReturn.getRefundSn(), orderRefundReturn.getId());
			return true;
		}
		return false;
	}


	@Override
	public List<OrderRefundReturnDTO> getListByStatusAndBillFlag(OrderRefundReturnBillQuery query) {
		return orderRefundReturnConverter.to(orderRefundReturnDao.getListByStatusAndBillFlag(query));
	}

	@Override
	public void updateBillStatusAndSn(List<Long> ids, String billSn) {
		orderRefundReturnDao.updateBillStatusAndSn(ids, billSn);
	}

	@Override
	public PageSupport<ShopOrderBillRefundDTO> getShopOrderBillRefundPage(ShopOrderBillOrderQuery shopOrderBillOrderQuery) {
		return orderRefundReturnDao.getShopOrderBillRefundPage(shopOrderBillOrderQuery);
	}

	@Override
	public List<ShopOrderBillRefundExportDTO> exportShopOrderBillRefundPage(ShopOrderBillOrderQuery shopOrderBillOrderQuery) {
		return orderRefundReturnDao.exportShopOrderBillRefundPage(shopOrderBillOrderQuery);
	}

	@Override
	public List<OrderRefundExportDTO> export(OrderRefundReturnQuery query) {

		List<OrderRefundExportDTO> exportList = new ArrayList<>(0);
		query.setPageSize(999999999);
		PageSupport<OrderRefundReturnBO> pageSupport = this.orderRefundReturnDao.page(query);
		List<OrderRefundReturnBO> resultList = pageSupport.getResultList();
		if (CollectionUtils.isEmpty(resultList)) {
			return exportList;
		}
		exportList = new ArrayList<>(resultList.size());

		for (OrderRefundReturnBO bo : resultList) {
			OrderRefundExportDTO dto = converter.converter2ExportDTO(bo);
			exportList.add(dto);
			dto.setRefundNumber(bo.getRefundSn());
			dto.setOrderAmount(bo.getOrderItemMoney().setScale(2, RoundingMode.DOWN).toString());
			dto.setCreateTime(DateUtil.formatDateTime(bo.getCreateTime()));
			if (ObjectUtil.isNotEmpty(bo.getApplyType())) {
				dto.setApplyType(OrderRefundTypeEnum.getDes(bo.getApplyType()));
			}
			dto.setProductName(bo.getProductName());
			if (ObjectUtil.isNotEmpty(bo.getOrderStatus())) {
				dto.setOrderStatus(OrderStatusEnum.getDes(bo.getOrderStatus()));
			}
			dto.setReceiverInfo(bo.getNickName() + "  " + bo.getMobile());
			if (OrderRefundReturnStatusEnum.APPLY_FINISH.value().equals(bo.getApplyStatus())) {
				dto.setRefundStatus("已完成");
			}
			if (OrderRefundReturnStatusEnum.ORDER_UNDO_APPLY.value().equals(bo.getApplyStatus())) {
				dto.setRefundStatus("已取消");
			}
			if (OrderRefundReturnStatusEnum.REJECTED_APPLY.value().equals(bo.getApplyStatus())) {
				dto.setRefundStatus("已拒绝");
			}
			if (OrderRefundReturnStatusEnum.APPLY_WAIT_ADMIN.value().equals(bo.getApplyStatus())
					&& OrderRefundReturnStatusEnum.SELLER_AGREE.value().equals(bo.getSellerStatus())) {
				dto.setRefundStatus("待平台确认");
			}
			if (OrderRefundReturnStatusEnum.APPLY_WAIT_SELLER.value().equals(bo.getApplyStatus())
					&& OrderRefundReturnStatusEnum.SELLER_WAIT_AUDIT.value().equals(bo.getSellerStatus())) {
				dto.setRefundStatus("待卖家处理");
			}
			if (OrderRefundReturnStatusEnum.APPLY_WAIT_SELLER.value().equals(bo.getApplyStatus())
					&& OrderRefundReturnStatusEnum.SELLER_AGREE.value().equals(bo.getSellerStatus())) {
				dto.setRefundStatus("退货物流中");
			}
		}
		return exportList;
	}


	@Override
	public R<ApplyRefundReturnDTO> getApplyRefundReturn(Long orderId, Long orderItemId, Long userId) {
		Order order = orderDao.getByOrderIdAndUserId(orderId, userId);
		if (ObjectUtil.isEmpty(order)) {
			return R.fail("对不起,您操作的订单不存在或已被删除!");
		}

		//检查订单状态是否满足退款条件,只有付款的订单才可退款
		if (OrderStatusEnum.UNPAID.getValue() < order.getStatus() && !OrderRefundReturnStatusEnum.ORDER_NO_REFUND.value().equals(order.getRefundStatus())) {
			return R.fail("对不起,您的订单状态不满足退款条件!");
		}

		// 已付定金的只能商家进行退款
		if (OrderStatusEnum.PRESALE_DEPOSIT.getValue().equals(order.getStatus())) {
			return R.fail("预售定金请联系商家进行退款!");
		}

		if (ObjectUtil.isNotEmpty(order.getFinalReturnDeadline())
				&& OrderStatusEnum.SUCCESS.getValue().equals(order.getStatus())
				&& DateUtil.date().after(order.getFinalReturnDeadline())) {
			return R.fail("对不起，你的订单已过售后期，无法申请退款~");
		}

		List<OrderItem> orderItemList = orderItemDao.getByOrderId(orderId, orderItemId, userId);
		if (CollUtil.isEmpty(orderItemList)) {
			return R.fail("找不到该订单");
		}

		// 获取未发起退款的订单
		List<OrderItem> applyRefundList = orderItemList.stream()
				.filter(item -> {
					if (OrderStatusEnum.SUCCESS.getValue().equals(order.getStatus()) && ObjectUtil.isNotEmpty(item.getReturnDeadline())) {
						return OrderRefundReturnStatusEnum.ITEM_NO_REFUND.value().equals(item.getRefundStatus()) && DateUtil.date().before(item.getReturnDeadline());
					} else {
						return OrderRefundReturnStatusEnum.ITEM_NO_REFUND.value().equals(item.getRefundStatus());
					}
				}).collect(Collectors.toList());
		if (CollUtil.isEmpty(applyRefundList)) {
			return R.fail("找不到可退款的订单");
		}

		ApplyRefundReturnDTO applyRefundReturnDTO = new ApplyRefundReturnDTO();
		applyRefundReturnDTO.setOrderItemList(converter.converter2ApplyRefundReturnItemDTO(applyRefundList));
		applyRefundReturnDTO.setRefundReasonList(getRefundReason());
		return R.ok(applyRefundReturnDTO);
	}

	@Override
	public List<String> getRefundReason() {
		return sysParamsApi.getSysParamItemsByParamName(SysParamNameEnum.ORDER_REFUND_REASON.name()).getData()
				.stream()
				.map(SysParamItemDTO::getValue)
				.collect(Collectors.toList());
	}

	@Override
	public R<String> applyOrder(ApplyOrderItemRefundDTO applyOrderItemRefundDTO) {
		OrderRefundTypeEnum orderRefundTypeEnum = OrderRefundTypeEnum.fromCode(applyOrderItemRefundDTO.getRefundType());
		if (null != orderRefundTypeEnum) {
			switch (orderRefundTypeEnum) {
				case REFUND:
					if (null == applyOrderItemRefundDTO.getRefundAmount()) {
						return R.fail("请输入退款金额");
					}
					if (applyOrderItemRefundDTO.getOrderItemIds().size() > 1) {
						return R.fail("一次只能退款一个订单项，请重新申请");
					}
					return applyOrderItem(applyOrderItemRefundDTO, applyOrderItemRefundDTO.getRefundAmount());
				case RETURN:
					return applyOrderItem(applyOrderItemRefundDTO);
				default:
			}
		}
		return R.fail("参数异常");
	}

	@Override
	public boolean isAllowOrderReturn(Date completeTime, Long productId) {
		//通过商品id获取商品绑定的最近以及类目
		ProductDTO product = productApi.getDtoByProductId(productId).getData();
		Integer days = 0;
		if (ObjectUtil.isNotEmpty(product)) {
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
		}
		//如果类目没设置退换货时间，则退换货时间为系统默认配置的时间
		if (days == null || days == 0) {
			// 获取系统的订单运行退换货时间配置
			// 处理拆分为微服务后，强转失败问题
			ObjectMapper mapper = new ObjectMapper();
			OrderSettingDTO orderSetting = mapper.convertValue(sysParamsApi.getConfigDtoByParamName(SysParamNameEnum.ORDER_SETTING.name(), OrderSettingDTO.class).getData(), OrderSettingDTO.class);
			days = orderSetting.getRefundOrExchangeValidDay();
		}

		Date finalTime = DateUtil.offsetDay(completeTime, days);
		return !DateUtil.date().isAfter(finalTime);
	}

	@Override
	public BigDecimal getRefundAmountByUserId(Long userId) {
		return orderRefundReturnDao.getRefundAmountByUserId(userId);
	}

	@Override
	public BigDecimal getRefundAmountByOrderNumbers(List<String> orderNumbers) {
		return orderRefundReturnDao.getRefundAmountByOrderNumbers(orderNumbers);
	}

	@Override
	public OrderRefundReturnDTO getByRefundSn(String refundSn) {
		return converter.to(orderRefundReturnDao.getByRefundSn(refundSn));
	}

	@Override
	public R<Void> refundHandler(String payRefundSn, Boolean refundStatus) {
		R<PayRefundSettlementDTO> refundSettlementDTOR = payRefundSettlementApi.getByPayRefundSn(payRefundSn);
		OrderRefundReturn refundReturn = orderRefundReturnDao.getByRefundSn(refundSettlementDTOR.getData().getRefundSn());
		log.info("退款处理, 支付退款订单号: {}, 退款状态: {}", payRefundSn, refundStatus);
		if (!refundStatus) {
			refundReturn.setHandleSuccessStatus(OrderRefundReturnStatusEnum.HANDLE_FAIL.value());
			orderRefundReturnDao.update(refundReturn);
			return R.ok();
		}
		return confirmRefundStrategyContext.executeRefundHandlerStrategy(refundReturn);
	}

	@Override
	public boolean insertRemark(Long refundId, String remark) {
		OrderRefundReturn refundReturn = orderRefundReturnDao.getById(refundId);
		if (ObjectUtil.isEmpty(refundReturn)) {
			throw new BusinessException("该售后订单已不存在，请刷新页面或联系管理员");
		}
		if (ObjectUtil.isNotNull(refundReturn.getAdminMessage())) {
			throw new BusinessException("该售后订单已备注，不能重复备注");
		}
		refundReturn.setAdminMessage(remark);
		return orderRefundReturnDao.update(refundReturn) > 0;
	}

	private boolean isAllowOrderReturn(Date returnDeadline) {
		if (null == returnDeadline) {
			return true;
		}
		return !DateUtil.date().isAfter(returnDeadline);
	}

	@Override
	public R<Void> refundCallback(String refundSn) {
		OrderRefundReturn refund = this.orderRefundReturnDao.getReturnByRefundSn(refundSn);
		String orderType = OrderTypeEnum.getNameByValue(refund.getOrderType());
		return this.confirmRefundStrategyContext.callbackStrategy(orderType, refundSn);
	}

	@Override
	public List<OrderRefundReturnDTO> queryById(List<Long> ids) {
		return this.converter.to(orderRefundReturnDao.queryAllByIds(ids));
	}

	private void changeOrderRefundStatus(Long orderId, Long refundId) {

		List<OrderRefundReturn> returnList = orderRefundReturnDao.queryByOrderId(orderId);
		long count = returnList.stream()
				.filter(a -> !Objects.equals(a.getId(), refundId)
						&& !Objects.equals(a.getApplyStatus(), OrderRefundReturnStatusEnum.UNDO_APPLY.value())
						&& !Objects.equals(a.getApplyStatus(), OrderRefundReturnStatusEnum.CANCEL_APPLY.value())
						&& !Objects.equals(a.getApplyStatus(), OrderRefundReturnStatusEnum.REJECTED_APPLY.value()))
				.count();
		if (count == 0) {
			orderDao.updateRefundState(orderId, OrderRefundReturnStatusEnum.ORDER_NO_REFUND.value());
		}
	}

	@Override
	public List<OrderRefundReturn> queryUntreatedOrderRefund() {
		return orderRefundReturnDao.queryUntreatedOrderRefund();
	}

	@Override
	public PageSupport<OrderCancelReasonDTO> pageCancelOrder(OrderCancelReasonQuery query) {
		//商家申请
		query.setRefundSource(OrderRefundSouceEnum.SHOP.value());
		//商家主动申请帮用户取消退款
		query.setApplyType(OrderRefundReturnTypeEnum.REFUND_CANEL.value());

		PageSupport<OrderCancelReasonDTO> pageSupport = orderRefundReturnDao.pageCancelOrder(query);
		List<OrderCancelReasonDTO> resultList = pageSupport.getResultList();
		if (CollUtil.isEmpty(resultList)) {
			return pageSupport;
		}
		for (OrderCancelReasonDTO orderCancelReasonDTO : resultList) {
			if (OrderRefundReturnStatusEnum.SELLER_AGREE.value().equals(orderCancelReasonDTO.getSellerStatus())
					&& OrderRefundReturnStatusEnum.APPLY_WAIT_ADMIN.value().equals(orderCancelReasonDTO.getApplyStatus())) {
				orderCancelReasonDTO.setRemark("商家已申请，待平台审核");
			}
			if (OrderRefundReturnStatusEnum.APPLY_FINISH.value().equals(orderCancelReasonDTO.getApplyStatus())) {
				orderCancelReasonDTO.setRemark("平台审核通过");
			}
			if (OrderRefundReturnStatusEnum.APPLY_ADMIN_REJECT.value().equals(orderCancelReasonDTO.getApplyStatus())) {
				orderCancelReasonDTO.setRemark("平台审核拒绝,平台审核意见:" + orderCancelReasonDTO.getAdminMessage());
			}
			if (OrderRefundReturnStatusEnum.SELLER_WITHDRAW.value().equals(orderCancelReasonDTO.getSellerStatus())) {
				orderCancelReasonDTO.setRemark("商家主动撤回申请");
			}
		}
		List<String> orderNumbers = resultList.stream().map(OrderCancelReasonDTO::getOrderNumber).collect(Collectors.toList());
		List<OrderItemDTO> orderItemList = orderItemConverter.to(orderItemDao.queryByOrderNumbers(orderNumbers));

		List<Long> productIdList = orderItemList.stream().map(OrderItemDTO::getProductId).collect(Collectors.toList());
		R<List<ProductDTO>> productList = productApi.queryAllByIds(productIdList);
		if (CollUtil.isNotEmpty(productList.getData())) {
			Map<Long, ProductDTO> productMap = productList.getData().stream().collect(Collectors.toMap(ProductDTO::getId, e -> e));
			for (OrderItemDTO orderItemDTO : orderItemList) {
				if (!productMap.containsKey(orderItemDTO.getProductId())) {
					continue;
				}
				ProductDTO productDTO = productMap.get(orderItemDTO.getProductId());
				orderItemDTO.setProductStatus(productDTO.getStatus());
			}
		}
		Map<Long, List<OrderItemDTO>> orderItemMap = orderItemList.stream().collect(Collectors.groupingBy(OrderItemDTO::getOrderId));
		for (OrderCancelReasonDTO orderCancelReasonDTO : resultList) {
			if (!orderItemMap.containsKey(orderCancelReasonDTO.getOrderId())) {
				continue;
			}
			List<OrderItemDTO> dtoList = orderItemMap.get(orderCancelReasonDTO.getOrderId());
			//如果是全单退款，则需要获取该订单的所有订单项
			orderCancelReasonDTO.setOrderItemDTOList(dtoList);
		}
		return pageSupport;
	}

	@Override
	public List<OrderCancelExportDTO> getFlowExcelCancel(OrderCancelReasonQuery query) {
		query.setRefundSource(OrderRefundSouceEnum.SHOP.value());
		//商家主动申请帮用户取消退款
		query.setApplyType(OrderRefundReturnTypeEnum.REFUND_CANEL.value());
		List<OrderCancelExportDTO> exportList = new ArrayList<>(0);
		query.setPageSize(999999999);

		PageSupport<OrderCancelReasonDTO> pageSupport = orderRefundReturnDao.pageCancelOrder(query);
		List<OrderCancelReasonDTO> resultList = pageSupport.getResultList();
		if (CollectionUtils.isEmpty(resultList)) {
			return exportList;
		}
		exportList = new ArrayList<>(resultList.size());
		for (OrderCancelReasonDTO reasonDTO : resultList) {
			OrderCancelExportDTO orderCancelExportDTO = new OrderCancelExportDTO();
			//订单号
			orderCancelExportDTO.setOrderNumber(reasonDTO.getOrderNumber());
			//售后号
			orderCancelExportDTO.setRefundNumber(reasonDTO.getRefundSn());
			//订单商品规格属性
			orderCancelExportDTO.setProductAttribute(reasonDTO.getProductAttribute());
			//申请时间
			orderCancelExportDTO.setCreateTime(DateUtil.formatTime(reasonDTO.getCreateTime()));
			//商品名称
			orderCancelExportDTO.setProductName(reasonDTO.getProductName());
			//操作账号和时间
			if (ObjectUtil.isEmpty(query.getShopId())) {
				//平台 操作账号和时间
				orderCancelExportDTO.setAccountTime(reasonDTO.getAdminOperator() + reasonDTO.getAdminOperatorTime());
			}
			//商家 操作账号和时间
			orderCancelExportDTO.setAccountTime(reasonDTO.getShopOperator() + reasonDTO.getShopOperatorTime());
			//订单金额
			String orderMoney = reasonDTO.getOrderMoney() + " (含运费：" + reasonDTO.getFreightPrice() + ")";
			orderCancelExportDTO.setOrderMoney(orderMoney);
			//取消原因
			orderCancelExportDTO.setReason(reasonDTO.getReason() != null ? reasonDTO.getReason() : "");
			//备注说明
			orderCancelExportDTO.setSellerMessage(reasonDTO.getSellerMessage() != null ? reasonDTO.getSellerMessage() : "");

			//状态
			if (OrderRefundReturnStatusEnum.APPLY_WAIT_ADMIN.value().equals(reasonDTO.getApplyStatus())) {
				orderCancelExportDTO.setStatus("待审核");
			}
			if (OrderRefundReturnStatusEnum.APPLY_FINISH.value().equals(reasonDTO.getApplyStatus())) {
				orderCancelExportDTO.setStatus("已同意");
			}
			if (OrderRefundReturnStatusEnum.APPLY_CANCEL.value().equals(reasonDTO.getApplyStatus())) {
				orderCancelExportDTO.setStatus("已取消");
			}
			if (OrderRefundReturnStatusEnum.APPLY_ADMIN_REJECT.value().equals(reasonDTO.getApplyStatus())) {
				orderCancelExportDTO.setStatus("已拒绝");
			}
			//备注
			orderCancelExportDTO.setAdminMessage(reasonDTO.getAdminMessage() != null ? reasonDTO.getAdminMessage() : "");
			exportList.add(orderCancelExportDTO);
		}
		return exportList;
	}

	@Override
	public List<SysParamItemDTO> queryCancelReason(String reason) {
		return sysParamsApi.getSysParamItemsByParamName(reason).getData();
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public R<String> auditWithdrawGood(List<Long> refundIds, Long shopId) {
		if (CollUtil.isEmpty(refundIds)) {
			R.fail("审核失败,请选择商品");
		}
		List<OrderRefundReturn> orderRefundReturnList = orderRefundReturnDao.getByRefundId(refundIds);
		List<Order> orderList = orderDao.getByOrderIdList(orderRefundReturnList.stream().map(OrderRefundReturn::getOrderId).collect(Collectors.toList()));
		for (OrderRefundReturn orderRefundReturn : orderRefundReturnList) {
			if (ObjectUtil.isEmpty(orderRefundReturn)) {
				throw new BusinessException("对不起，你要操作的记录已不存在");
			}
			if (!shopId.equals(orderRefundReturn.getShopId())) {
				throw new BusinessException("非法操作");
			}
			if (!OrderRefundReturnStatusEnum.SELLER_AGREE.value().equals(orderRefundReturn.getSellerStatus())) {
				throw new BusinessException("对不起，该售后订单不处于待审核状态");
			}
			orderRefundReturn.setSellerTime(DateUtil.date());
			//审核同意
			orderRefundReturn.setApplyStatus(OrderRefundReturnStatusEnum.APPLY_CANCEL.value());
			//卖家处理状态撤回
			orderRefundReturn.setSellerStatus(OrderRefundReturnStatusEnum.SELLER_WITHDRAW.value());

			orderRefundReturn.setApplyType(OrderRefundReturnTypeEnum.REFUND_CANEL.value());

			if (orderRefundReturnDao.update(orderRefundReturn) > 0) {
				//保存进订单历史
				saveOrderHistory(orderRefundReturn.getOrderId(), OrderHistoryEnum.ORDER_CANCEL.value(),
						orderRefundReturn.getShopId() + "商家于" + DateUtil.date() + "撤回订单");
				//已结束
				orderList.forEach(e -> {
					e.setRefundStatus(OrderRefundReturnStatusEnum.ITEM_REFUND_OVER.value());
					orderDao.update(e);
				});
			}
		}
		return R.ok();
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public R<String> applyCancelOrderRefund(ApplyOrderCancelDTO applyOrderCancelDTO) {
		//通过订单id和shopid获取订单
		Order order = orderDao.getByOrderIdAndShopId(applyOrderCancelDTO.getOrderId(), applyOrderCancelDTO.getShopId());
		if (ObjectUtil.isEmpty(order)) {
			throw new BusinessException("对不起,您操作的订单不存在或已被删除!");
		}

		//待发货和已付定金的取消订单
		if (OrderStatusEnum.WAIT_DELIVERY.getValue().equals(order.getStatus())
				|| OrderStatusEnum.PRESALE_DEPOSIT.getValue().equals(order.getStatus())
				|| OrderStatusEnum.WAIT.getValue().equals(order.getStatus())) {

			//获取订单项数据
			OrderItem orderItem = orderItemDao.getFirstOrderItemByOrderId(order.getId());
			//构建取消订单退款单
			OrderRefundReturn orderRefundReturn = this.buildOrderRefundShopReturn(order, orderItem);
			//商家操作人
			orderRefundReturn.setShopOperator(applyOrderCancelDTO.getOperator());
			//商家操作时间
			orderRefundReturn.setShopOperatorTime(DateUtil.date());
			//退款数量
			orderRefundReturn.setGoodsNum(order.getProductQuantity());
			//留言
			orderRefundReturn.setReason(applyOrderCancelDTO.getReason());
			//说明
			orderRefundReturn.setSellerMessage(applyOrderCancelDTO.getSellerMessage());
			//申请类型：商家主动申请帮用户取消退款
			orderRefundReturn.setApplyType(OrderRefundReturnTypeEnum.REFUND_CANEL.value());
			//退款金额
			orderRefundReturn.setOrderItemMoney(order.getActualTotalPrice());
			orderRefundReturn.setRefundAmount(order.getActualTotalPrice());

			orderRefundReturn.setSkuId(0L);
			orderRefundReturn.setProductId(0L);

			// 预售订单返回预售支付金额
			if (OrderTypeEnum.PRE_SALE.getValue().equals(order.getOrderType())) {
				PreSellOrder sellOrder = preSellOrderDao.getByOrderId(order.getId());
				//是否支付尾款
				Boolean payFinalFlag = sellOrder.getPayFinalFlag();
				if (payFinalFlag) {
					// 支付了尾款
					orderRefundReturn.setRefundAmount(NumberUtil.add(sellOrder.getPreDepositPrice(), NumberUtil.sub(sellOrder.getFinalPrice(), order.getFreightPrice())));
				}
				//预售订单的退款金额 定金
				orderRefundReturn.setRefundAmount(sellOrder.getPreDepositPrice());
			}

			log.info("###### 开始保存取消订单记录 ####### ");
			Long refundId = orderRefundReturnDao.save(orderRefundReturn);
			if (refundId <= 0) {
				log.error("取消订单保存失败");
				throw new BusinessException("取消订单保存失败");
			}

			orderRefundReturn.setId(refundId);
			//售后状态 改为在处理中
			//更新订单项退款状态
			order.setRefundStatus(OrderRefundReturnStatusEnum.ITEM_REFUND_PROCESSING.value());
			//更新订单退款状态
			orderDao.update(order);
			//更新订单项退款状态
			orderItemDao.updateRefundInfoByOrderId(order.getId(), refundId);
			log.info("###### 保存订单历史 ####### ");
			saveOrderHistory(orderRefundReturn.getOrderId(), OrderHistoryEnum.ORDER_CANCEL.value(), "商家" + applyOrderCancelDTO.getShopId() + "于" + DateUtil.date() + "发起订单取消");

			// 发送取消通知站内信给平台
			List<MsgSendParamDTO> msgSendParamDTOS = new ArrayList<>();
			//替换参数内容
			MsgSendParamDTO refundSnDTO = new MsgSendParamDTO(MsgSendParamEnum.REFUND_SN, orderRefundReturn.getRefundSn(), "black");
			MsgSendParamDTO productNameDTO = new MsgSendParamDTO(MsgSendParamEnum.PRODUCT_NAME, orderRefundReturn.getProductName(), "black");
			msgSendParamDTOS.add(refundSnDTO);
			msgSendParamDTOS.add(productNameDTO);
			messagePushClient.push(new MessagePushDTO()
					.setMsgReceiverTypeEnum(MsgReceiverTypeEnum.ADMIN_USER)
					.setReceiveIdArr(new Long[]{orderRefundReturn.getShopId()})
					.setMsgSendType(MsgSendTypeEnum.ORDER_REFUND)
					.setSysParamNameEnum(SysParamNameEnum.ORDER_REFUND_TO_SHOP)
					.setMsgSendParamDTOList(msgSendParamDTOS)
					.setDetailId(orderRefundReturn.getId())
			);
			log.info("###### 插入平台自动同意退款的定时任务列表 ####### ");
			orderProducerService.autoAgreeAdminRefund(refundId, OrderTypeEnum.fromCode(order.getOrderType()));
			return R.ok();
		}
		return R.fail("对不起,您操作的订单无法取消");
	}


	@Override
	public PageSupport<OrderRefundReturnBO> pageOrderRefundUser(OrderRefundReturnQuery query) {
		PageSupport<OrderRefundReturnBO> pageSupport = orderRefundReturnDao.pageOrderRefundUser(query);
		List<OrderRefundReturnBO> resultList = pageSupport.getResultList();
		if (CollUtil.isEmpty(resultList)) {
			return pageSupport;
		}

		List<String> orderNumbers = resultList.stream().map(OrderRefundReturnBO::getOrderNumber).collect(Collectors.toList());
		List<OrderItemDTO> orderItemList = orderItemConverter.to(orderItemDao.queryByOrderNumbers(orderNumbers));
		List<Long> productIdList = orderItemList.stream().map(OrderItemDTO::getProductId).collect(Collectors.toList());
		R<List<ProductDTO>> productList = productApi.queryAllByIds(productIdList);
		if (CollUtil.isNotEmpty(productList.getData())) {
			Map<Long, ProductDTO> productMap = productList.getData().stream().collect(Collectors.toMap(ProductDTO::getId, e -> e));
			for (OrderItemDTO orderItemDTO : orderItemList) {
				if (!productMap.containsKey(orderItemDTO.getProductId())) {
					continue;
				}
				ProductDTO productDTO = productMap.get(orderItemDTO.getProductId());
				orderItemDTO.setProductStatus(productDTO.getStatus());
			}
		}
		Map<Long, List<OrderItemDTO>> orderItemMap = orderItemList.stream().collect(Collectors.groupingBy(OrderItemDTO::getOrderId));
		for (OrderRefundReturnBO orderRefundReturnBO : resultList) {
			if (!orderItemMap.containsKey(orderRefundReturnBO.getOrderId())) {
				continue;
			}

			List<OrderItemDTO> dtoList = orderItemMap.get(orderRefundReturnBO.getOrderId());
			//如果是全单退款，则需要获取该订单的所有订单项
			if (orderRefundReturnBO.getSkuId() != 0) {
				dtoList = dtoList.stream().filter(e -> e.getId().equals(orderRefundReturnBO.getOrderItemId())).collect(Collectors.toList());
			}

			orderRefundReturnBO.setOrderItemDTOList(dtoList);
		}
		return pageSupport;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public R confirmRefundCanelList(ConfirmRefundCanelListDTO confirmRefundDTO) {
		if (CollUtil.isEmpty(confirmRefundDTO.getConfirmRefundDTO())) {
			R.fail("审核失败,请选择商品!");
		}
		//退款id
		List<Long> refundIds = confirmRefundDTO.getConfirmRefundDTO().stream().map(ConfirmRefundDTO::getRefundId).collect(Collectors.toList());
		//根据退款id查询OrderRefundReturn
		List<OrderRefundReturn> refundReturnList = orderRefundReturnDao.getByRefundId(refundIds);
		//拒绝
		if (!confirmRefundDTO.isAgree()) {
			for (OrderRefundReturn orderRefundReturn : refundReturnList) {
				//平台操作人
				orderRefundReturn.setAdminOperator(confirmRefundDTO.getAdminOperator());
				//平台操作时间
				orderRefundReturn.setShopOperatorTime(DateUtil.date());
				//平台拒绝
				orderRefundReturn.setApplyStatus(OrderRefundReturnStatusEnum.APPLY_ADMIN_REJECT.value());
				//当前时间
				orderRefundReturn.setAdminTime(DateUtil.date());
				//当前时间
				orderRefundReturn.setAdminOperatorTime(DateUtil.date());
				//平台建议
				orderRefundReturn.setAdminMessage(confirmRefundDTO.getAdminMessage());
				//平台
				List<Long> orderIds = refundReturnList.stream().map(OrderRefundReturn::getOrderId).collect(Collectors.toList());
				List<Order> orderList = orderDao.getByOrderIdList(orderIds);
				//商家申请取消订单平台拒绝 改为已结束
				orderList.forEach(e -> {
					e.setRefundStatus(OrderRefundReturnStatusEnum.ITEM_REFUND_OVER.value());
					orderDao.update(e);
				});
				//更新订单退款状态
				orderRefundReturnDao.update(orderRefundReturn);
			}
			return R.ok();
		}
		//同意
		for (OrderRefundReturn orderRefundReturn : refundReturnList) {
			//平台建议
			orderRefundReturn.setAdminMessage(confirmRefundDTO.getAdminMessage());
			orderRefundReturn.setAdminOperator(confirmRefundDTO.getAdminOperator());
			orderRefundReturn.setAdminTime(DateUtil.date());
			orderRefundReturn.setAdminOperatorTime(DateUtil.date());
			orderRefundReturnDao.update(orderRefundReturn);
		}
		confirmRefundDTO.getConfirmRefundDTO().forEach(confirmRefundStrategyContext::executeStrategy);
		return R.ok();
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean batchAuditRefund(OrderBulkRefundQuery orderBulkRefundQuery) {
		if (CollUtil.isEmpty(orderBulkRefundQuery.getRefundIds())) {
			R.fail("审核失败");
		}
		List<OrderRefundReturn> orderRefundReturnList = orderRefundReturnDao.getByRefundId(orderBulkRefundQuery.getRefundIds());
		for (OrderRefundReturn orderRefundReturn : orderRefundReturnList) {
			if (ObjectUtil.isEmpty(orderRefundReturn)) {
				throw new BusinessException("对不起，你要操作的记录已不存在");
			}
			if (!OrderRefundReturnStatusEnum.APPLY_WAIT_SELLER.value().equals(orderRefundReturn.getApplyStatus())
					|| !OrderRefundReturnStatusEnum.SELLER_WAIT_AUDIT.value().equals(orderRefundReturn.getSellerStatus())) {
				return false;
			}
			orderRefundReturn.setSellerTime(DateUtil.date());
			orderRefundReturn.setSellerMessage(orderBulkRefundQuery.getSellerMessage());
			//审核同意
			if (orderBulkRefundQuery.getAuditFlag()) {
				orderProducerService.autoAgreeAdminRefund(orderRefundReturn.getId(), OrderTypeEnum.fromCode(orderRefundReturn.getOrderType()));
				orderRefundReturn.setSellerStatus(OrderRefundReturnStatusEnum.SELLER_AGREE.value());
				orderRefundReturn.setApplyStatus(OrderRefundReturnStatusEnum.APPLY_WAIT_ADMIN.value());
				ObjectMapper mapper = new ObjectMapper();
				OrderSettingDTO orderSetting = mapper.convertValue(sysParamsApi.getNotCacheConfigByName(SysParamNameEnum.ORDER_SETTING.name(), OrderSettingDTO.class).getData(), OrderSettingDTO.class);
				//平台自动审核
				if (orderSetting.getAutomaticAuditAfterSalesOrder()) {
					orderRefundReturn.setApplyStatus(OrderRefundReturnStatusEnum.APPLY_FINISH.value());
				}
				if (orderRefundReturnDao.update(orderRefundReturn) > 0) {
					//保存进订单历史
					saveOrderHistory(orderRefundReturn.getOrderId(), OrderHistoryEnum.AGREED_RETURNMONEY.value(), orderRefundReturn.getShopId() + "商家于" + DateUtil.date() + "同意退款");
					//发送站内信通知平台
					sendAuditPassToAdmin(orderRefundReturn.getRefundSn(), orderRefundReturn.getId());
					//发送站内信通知用户
					sendAuditToUser(orderRefundReturn.getRefundSn(), orderRefundReturn.getId(), orderRefundReturn.getUserId());
				}
			} else {
				orderRefundReturn.setSellerStatus(OrderRefundReturnStatusEnum.SELLER_DISAGREE.value());
				orderRefundReturn.setApplyStatus(OrderRefundReturnStatusEnum.REJECTED_APPLY.value());
				if (orderRefundReturnDao.update(orderRefundReturn) > 0) {
					//更新订单退款状态
					orderDao.updateRefundState(orderRefundReturn.getOrderId(), OrderRefundReturnStatusEnum.ORDER_NO_REFUND.value());
					//更新订单项退款状态
					orderItemDao.updateByRefundId(orderRefundReturn.getId(), OrderRefundReturnStatusEnum.ITEM_NO_REFUND.value());
					//保存进订单历史
					saveOrderHistory(orderRefundReturn.getOrderId(), OrderHistoryEnum.DISAGREED_RETURNMONEY.value(), orderRefundReturn.getShopId() + "商家于" + DateUtil.date() + "拒绝退款");
					//发送站内信通知平台
					sendAuditPassToAdmin(orderRefundReturn.getRefundSn(), orderRefundReturn.getId());
					//发送站内信通知用户
					sendAuditToUser(orderRefundReturn.getRefundSn(), orderRefundReturn.getId(), orderRefundReturn.getUserId());
				}
			}
		}
		return true;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean batchAuditRefundGood(Long shopId, OrderBulkRefundQuery query) {
		if (CollUtil.isEmpty(query.getRefundIds())) {
			R.fail("审核失败");
		}
		List<OrderRefundReturn> refundReturnList = orderRefundReturnDao.getByRefundId(query.getRefundIds());
		for (OrderRefundReturn orderRefundReturn : refundReturnList) {
			if (ObjectUtil.isEmpty(orderRefundReturn)) {
				return false;
			}
			if (!OrderRefundReturnStatusEnum.APPLY_WAIT_SELLER.value().equals(orderRefundReturn.getApplyStatus())
					|| !OrderRefundReturnStatusEnum.SELLER_WAIT_AUDIT.value().equals(orderRefundReturn.getSellerStatus())) {
				return false;
			}
			orderRefundReturn.setSellerTime(DateUtil.date());
			orderRefundReturn.setSellerMessage(query.getSellerMessage());
			//审核同意
			if (query.getAuditFlag()) {
				orderRefundReturn.setSellerStatus(OrderRefundReturnStatusEnum.SELLER_AGREE.value());
				//商家选择弃货
				if (query.getAbandonedGoodFlag()) {
					orderRefundReturn.setApplyStatus(OrderRefundReturnStatusEnum.APPLY_WAIT_ADMIN.value());
					orderRefundReturn.setReturnType(OrderRefundReturnTypeEnum.NO_NEED_GOODS.value());
					if (orderRefundReturnDao.update(orderRefundReturn) > 0) {
						//保存进订单历史
						saveOrderHistory(orderRefundReturn.getOrderId(), OrderHistoryEnum.DISAGREED_RETURNGOOD.value(), orderRefundReturn.getShopId() + "商家于" + DateUtil.date() + "同意退货退款并选择弃货");
						//发送站内信通知平台
						sendAuditPassToAdmin(orderRefundReturn.getRefundSn(), orderRefundReturn.getId());
						//发送站内信通知用户
						sendAuditToUser(orderRefundReturn.getRefundSn(), orderRefundReturn.getId(), orderRefundReturn.getUserId());
					}
					ObjectMapper mapper = new ObjectMapper();
					OrderSettingDTO orderSetting = mapper.convertValue(sysParamsApi.getNotCacheConfigByName(SysParamNameEnum.ORDER_SETTING.name(), OrderSettingDTO.class).getData(), OrderSettingDTO.class);
					//平台自动审核
					if (orderSetting.getAutomaticAuditAfterSalesOrder()) {
						orderRefundReturn.setApplyStatus(OrderRefundReturnStatusEnum.APPLY_FINISH.value());
					}
				}
				//需要货物
				else {
					//校验是否有退货地址
					ShopDetailDTO shopDetailDTO = shopDetailApi.getById(shopId).getData();
					;
					checkReturnAddress(shopDetailDTO);

					orderRefundReturn.setReturnType(OrderRefundReturnTypeEnum.NEED_GOODS.value());
					orderRefundReturn.setGoodsStatus(OrderRefundReturnStatusEnum.LOGISTICS_WAIT_DELIVER.value());
					if (orderRefundReturnDao.update(orderRefundReturn) > 0) {
						//保存进订单历史
						saveOrderHistory(orderRefundReturn.getOrderId(), OrderHistoryEnum.DISAGREED_RETURNGOOD.value(), orderRefundReturn.getShopId() + "商家于" + DateUtil.date() + "同意退货退款");
						//发送站内信通知用户
						sendAuditToUser(orderRefundReturn.getRefundSn(), orderRefundReturn.getId(), orderRefundReturn.getUserId());
						//插入用户超时不发货自动取消售后状态的定时器
						orderProducerService.autoCancelRefund(orderRefundReturn.getId());
					}
				}
			} else {
				orderRefundReturn.setSellerStatus(OrderRefundReturnStatusEnum.SELLER_DISAGREE.value());
				if (orderRefundReturnDao.update(orderRefundReturn) > 0) {
					//更新订单退款状态
					orderDao.updateRefundState(orderRefundReturn.getOrderId(), OrderRefundReturnStatusEnum.ORDER_NO_REFUND.value());
					//更新订单项退款状态
					orderItemDao.updateByRefundId(orderRefundReturn.getId(), OrderRefundReturnStatusEnum.ITEM_NO_REFUND.value());
					//保存进订单历史
					saveOrderHistory(orderRefundReturn.getOrderId(), OrderHistoryEnum.DISAGREED_RETURNGOOD.value(), orderRefundReturn.getShopId() + "商家于" + DateUtil.date() + "拒绝退货退款");
					//发送站内信通知平台
					sendAuditPassToAdmin(orderRefundReturn.getRefundSn(), orderRefundReturn.getId());
					//发送站内信通知用户
					sendAuditToUser(orderRefundReturn.getRefundSn(), orderRefundReturn.getId(), orderRefundReturn.getUserId());
				}
			}
		}
		return true;
	}

	@Override
	public List<String> queryAfterSalesReason(Long shopId, Long applyType) {
		List<OrderRefundReturn> orderRefundReturnList = orderRefundReturnDao.queryAfterSalesReason(shopId, applyType);
		List<String> list = orderRefundReturnList.stream().map(OrderRefundReturn::getReason).filter(StrUtil::isNotEmpty).distinct().collect(Collectors.toList());
		return list;
	}

	/**
	 * 组装退款参数
	 *
	 * @param order
	 * @param orderItem
	 * @return
	 */
	private OrderRefundReturn buildOrderRefundShopReturn(Order order, OrderItem orderItem) {
		OrderRefundReturn orderRefundReturn = new OrderRefundReturn();

		orderRefundReturn.setOrderNumber(order.getOrderNumber());
		orderRefundReturn.setUserId(order.getUserId());
		orderRefundReturn.setShopId(order.getShopId());
		orderRefundReturn.setShopName(order.getShopName());
		orderRefundReturn.setOrderId(order.getId());
		orderRefundReturn.setPaySettlementSn(order.getPaySettlementSn());
		orderRefundReturn.setOrderMoney(order.getActualTotalPrice());
		orderRefundReturn.setOrderType(order.getOrderType());
		orderRefundReturn.setIntegral(order.getTotalIntegral());

		orderRefundReturn.setGoodsNum(orderItem.getBasketCount());
		orderRefundReturn.setOrderItemMoney(orderItem.getActualAmount());
		orderRefundReturn.setProductName(orderItem.getProductName());
		orderRefundReturn.setProductImage(orderItem.getPic());
		orderRefundReturn.setProductAttribute(orderItem.getAttribute());
		orderRefundReturn.setOrderItemId(orderItem.getId());
		orderRefundReturn.setSkuId(orderItem.getSkuId());
		orderRefundReturn.setProductId(orderItem.getProductId());

		orderRefundReturn.setRefundSn(RandomUtil.getRandomSn());
		orderRefundReturn.setCreateTime(DateUtil.date());
		//商家处理时间
		orderRefundReturn.setSellerTime(DateUtil.date());
		orderRefundReturn.setRefundSource(OrderRefundSouceEnum.SHOP.value());
		//申请状态：平台审核通过（如有需要可以改成商家审核通过，然后走平台审核流程）
		orderRefundReturn.setApplyStatus(OrderRefundReturnStatusEnum.APPLY_WAIT_ADMIN.value());
		//卖家处理状态：卖家同意
		orderRefundReturn.setSellerStatus(OrderRefundReturnStatusEnum.SELLER_AGREE.value());
		//处理退款状态：退款处理中
		orderRefundReturn.setHandleSuccessStatus(OrderRefundReturnStatusEnum.HANDLE_PROCESSS.value());
		return orderRefundReturn;
	}

	/**
	 * 获取审核时间
	 */
	private Long autoShopCancelRefundTime(Calendar userDate) {
		//获取系统配置
		ObjectMapper mapper = new ObjectMapper();
		OrderSettingDTO orderSetting = mapper.convertValue(sysParamsApi.getConfigDtoByParamName(SysParamNameEnum.ORDER_SETTING.name(), OrderSettingDTO.class).getData(), OrderSettingDTO.class);
		Integer autoShopCancelRefundTime = Optional.ofNullable(orderSetting).map(OrderSettingDTO::getAutoAgreeRefundDay).get();
		//用户到期时间
		userDate.add(Calendar.DATE, autoShopCancelRefundTime);
		Long timeInMillis = userDate.getTimeInMillis();
		return timeInMillis;
	}

	/**
	 * 获取审核时间
	 */
	private Long autoCancelRefundTime(Calendar userDate) {
		//获取系统配置
		ObjectMapper mapper = new ObjectMapper();
		OrderSettingDTO orderSetting = mapper.convertValue(sysParamsApi.getConfigDtoByParamName(SysParamNameEnum.ORDER_SETTING.name(), OrderSettingDTO.class).getData(), OrderSettingDTO.class);
		Integer autoCancelRefundDay = Optional.ofNullable(orderSetting).map(OrderSettingDTO::getAutoCancelRefundDay).get();
		//用户到期时间
		userDate.add(Calendar.DATE, autoCancelRefundDay);
		Long timeInMillis = userDate.getTimeInMillis();
		return timeInMillis;
	}

	private void checkReturnAddress(ShopDetailDTO shopDetailDTO) {
		if (shopDetailDTO != null) {
			if (StrUtil.isBlank(shopDetailDTO.getReturnConsignee())) {
				throw new BusinessException("请填写退货收货人");
			}
			if (StrUtil.isBlank(shopDetailDTO.getReturnConsigneePhone())) {
				throw new BusinessException("请填写退货收货人电话");
			}
			if (shopDetailDTO.getReturnProvinceId() == null
					|| shopDetailDTO.getReturnCityId() == null
					|| shopDetailDTO.getReturnAreaId() == null
					|| StrUtil.isBlank(shopDetailDTO.getReturnShopAddr())) {
				throw new BusinessException("请填写退货地址！");
			}
		} else {
			throw new BusinessException("无法查找到店铺的信息！");
		}
	}
}

