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
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.legendshop.activity.enums.ShopOperationTypeEnum;
import com.legendshop.basic.api.MessageApi;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.expetion.BusinessException;
import com.legendshop.common.util.RandomUtil;
import com.legendshop.order.dao.*;
import com.legendshop.order.dto.OrderDTO;
import com.legendshop.order.dto.PreSellOrderBillDTO;
import com.legendshop.order.dto.PreSellOrderDTO;
import com.legendshop.order.entity.*;
import com.legendshop.order.enums.*;
import com.legendshop.order.mq.producer.OrderProducerService;
import com.legendshop.order.service.OrderRefundReturnService;
import com.legendshop.order.service.PreSellOrderService;
import com.legendshop.order.service.convert.PreSellOrderConverter;
import com.legendshop.pay.api.RefundApi;
import com.legendshop.product.api.PreSellProductApi;
import com.legendshop.product.api.StockApi;
import com.legendshop.product.dto.BatchUpdateStockDTO;
import com.legendshop.product.dto.PreSellProductDTO;
import com.legendshop.product.enums.PreSellPayType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 预售订单服务实现类
 *
 * @author legendshop
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PreSellOrderServiceImpl implements PreSellOrderService {

	final OrderDao orderDao;

	final StockApi stockApi;

	final OrderItemDao orderItemDao;

	final RefundApi refundApi;

	final OrderHistoryDao orderHistoryDao;

	final PreSellOrderDao presellOrderDao;

	final MessageApi messagePushClient;

	final OrderProducerService orderProducerService;

	final PreSellOrderConverter presellOrderConverter;

	final PreSellProductApi presellProductApi;

	final OrderRefundReturnDao orderRefundReturnDao;

	final OrderRefundReturnService orderRefundReturnService;


	@Override
	public List<PreSellOrderBillDTO> getBillUnPayFinalPreSellOrder(Date endDate, Integer orderStatus) {
		return presellOrderDao.getBillUnPayFinalPreSellOrder(endDate, orderStatus);
	}


	@Override
	public Long savePreSellOrder(OrderDTO order, Long preSellProductId) {
		// 查询对应的预售商品，获取预售规则
		PreSellProductDTO preSellProduct = this.presellProductApi.getByProductId(preSellProductId).getData();
		PreSellOrder presellOrder = new PreSellOrder();
		BeanUtils.copyProperties(preSellProduct, presellOrder);
		presellOrder.setOrderId(order.getId());
		presellOrder.setPayDepositFlag(false);
		presellOrder.setPayFinalFlag(false);
		presellOrder.setUpdateTime(new Date());
		presellOrder.setCreateTime(new Date());

		BigDecimal actualTotalPrice = order.getActualTotalPrice().subtract(order.getFreightPrice());


		// 根据类型设置预售商品的定金与尾款处理，定金支付，定金不享受优惠
		if (PreSellPayType.DEPOSIT.value().equals(preSellProduct.getPayPctType())) {
			// 订单总金额 (未计算优惠)
			BigDecimal totalPrice = order.getTotalPrice();
			// 定金支付比例
			BigDecimal payPct = preSellProduct.getPayPct();
			BigDecimal depositPrice = totalPrice.multiply(payPct.divide(BigDecimal.valueOf(100), 2, RoundingMode.DOWN));
			// 尾款享受优惠折扣，但不能小于0
			BigDecimal finalPrice = actualTotalPrice.subtract(depositPrice);
			//尾款享受参与满减满折、限时折扣、店铺优惠券、平台优惠券
			if (finalPrice.compareTo(BigDecimal.ZERO) < 0) {
				depositPrice = depositPrice.add(finalPrice);
				finalPrice = BigDecimal.ZERO;
			}
			// 设置支付金额
			presellOrder.setPreDepositPrice(depositPrice);
			presellOrder.setFinalPrice(finalPrice);
		} else {
			// 订单实付 (减去了优惠)
			presellOrder.setPreDepositPrice(actualTotalPrice);
			//全额享受参与满减满折、限时折扣、店铺优惠券、平台优惠券？？

			//全额付款尾款置为0
			presellOrder.setFinalPrice(BigDecimal.ZERO);
		}
		log.info("预售实付金额： {}", order.getActualTotalPrice());

		return this.presellOrderDao.save(presellOrder);
	}

	@Override
	public int update(List<PreSellOrderDTO> preSellOrderList) {
		return this.presellOrderDao.update(this.presellOrderConverter.from(preSellOrderList));
	}

	@Override
	public int update(PreSellOrderDTO preSellOrderDTO) {
		return this.presellOrderDao.update(this.presellOrderConverter.from(preSellOrderDTO));
	}

	@Override
	public List<PreSellOrderDTO> queryByOrderIds(List<Long> orderIds) {
		return this.presellOrderConverter.to(this.presellOrderDao.queryByOrderIds(orderIds));
	}


	@Override
	@Transactional(rollbackFor = Exception.class)
	public int cancelOrderByIds(List<Long> orderIds) {
		if (CollectionUtils.isEmpty(orderIds)) {
			return 0;
		}
		List<PreSellOrder> preSellOrderList = this.presellOrderDao.queryByOrderIds(orderIds);
		if (CollectionUtils.isEmpty(preSellOrderList)) {
			return 0;
		}
		return cancelPreSellOrderList(this.presellOrderConverter.to(preSellOrderList));
	}

	@Override
	public int cancelPreSellOrderList(List<PreSellOrderDTO> orderList) {
		if (CollectionUtils.isEmpty(orderList)) {
			return 0;
		}
		Date now = new Date();
		List<Long> orderIds = orderList.stream().map(PreSellOrderDTO::getOrderId).collect(Collectors.toList());
		this.orderDao.cancelPreSellOrder(orderIds);

		// 取消订单有可能失败，所以需要重新查询
		List<Long> closeIds = this.orderDao.queryCloseOrderIds(orderIds);
		if (CollUtil.isEmpty(closeIds)) {
			return 0;
		}

		List<OrderHistory> historyList = new ArrayList<>(orderIds.size());
		closeIds.forEach(e -> {
			OrderHistory subHistory = new OrderHistory();
			subHistory.setCreateTime(now);
			subHistory.setOrderId(e);
			subHistory.setStatus(OrderHistoryEnum.ORDER_OVER_TIME.value());
			subHistory.setReason("预售尾款付款时间结束，系统于" + DateUtil.formatDateTime(DateUtil.date()) + "自动关闭订单");
			historyList.add(subHistory);
		});
		this.orderHistoryDao.save(historyList);

		// 退还普通商品库存
		for (Long orderId : closeIds) {
			List<OrderItem> orderItems = orderItemDao.getByOrderId(orderId);
			List<BatchUpdateStockDTO> updateStockDTOS = new ArrayList<>();
			for (OrderItem orderItem : orderItems) {
				updateStockDTOS.add(new BatchUpdateStockDTO(orderItem.getProductId(), orderItem.getSkuId(), orderItem.getBasketCount(), OrderTypeEnum.PRE_SALE.getValue(), ShopOperationTypeEnum.SEND_BACK.value()));
			}
			if (CollUtil.isNotEmpty(updateStockDTOS)) {
				//扣减普通商品库存
				R deductionInventory = stockApi.batchDeductionInventory(updateStockDTOS);
				if (!deductionInventory.success()) {
					throw new BusinessException(deductionInventory.getMsg());
				}
			}
		}
		return orderIds.size();
	}

	@Override
	public R<Void> shopActiveCancelPreSellOrder(Long shopId, Long orderId, String reason) {
		Order order = this.orderDao.getById(orderId);
		if (null == order) {
			return R.fail("取消失败，订单不存在！");
		}
		if (!shopId.equals(order.getShopId())) {
			log.info("商家【{}】正在取消另一个商家【{}】的预售订单，订单号： {}", shopId, order.getShopId(), order.getOrderNumber());
			return R.fail("非法操作！");
		}
		if (!OrderStatusEnum.PRESALE_DEPOSIT.getValue().equals(order.getStatus())) {
			return R.fail("取消失败，订单状态已修改，请刷新页面重新提交！");
		}

		if (!OrderRefundReturnStatusEnum.ORDER_NO_REFUND.value().equals(order.getRefundStatus())) {
			return R.fail("取消失败，订单状态已修改，请刷新页面重新提交！");
		}

		PreSellOrder preSellOrder = this.presellOrderDao.getByOrderId(orderId);

		Boolean payDepositFlag = preSellOrder.getPayDepositFlag();
		Boolean payFinalFlag = preSellOrder.getPayFinalFlag();

		if (payDepositFlag && payFinalFlag) {
			// 支付了尾款
			return R.fail("用户尾款已支付，无法取消订单，如有需要请联系用户发起退款请求！");
		}

		if (!payDepositFlag) {
			this.orderDao.cancelPreSellOrder(Collections.singletonList(orderId));
			// 未付定金，直接则取消订单
		}
		if (payDepositFlag) {
			//因为预售订单，理论上应该只有一个订单和一个订单项，如果后续改成可以与其它商品一起下单（即多订单多订单项）时，需要修改这个退款逻辑
			OrderItem orderItem = orderItemDao.getFirstOrderItemByOrderId(order.getId());
			//构建退款单
			OrderRefundReturn orderRefundReturn = this.buildOrderRefundReturn(order, orderItem);

			orderRefundReturn.setBuyerMessage("");
			orderRefundReturn.setReason(reason);
			//全单退款的售后商品数等于订单的总商品件数
			orderRefundReturn.setGoodsNum(order.getProductQuantity());
			orderRefundReturn.setOrderItemMoney(order.getActualTotalPrice());
			// 退还定金
			orderRefundReturn.setRefundAmount(preSellOrder.getPreDepositPrice());
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
			orderRefundReturn.setId(refundId);

		}

		log.info("###### 保存订单历史 ####### ");
		OrderHistory subHistory = new OrderHistory();
		subHistory.setCreateTime(new Date());
		subHistory.setOrderId(orderId);
		subHistory.setStatus(OrderHistoryEnum.ORDER_OVER_TIME.value());
		String reasonStr = "商家发起主动退款，订单 " + order.getOrderNumber() + " 于 " + DateUtil.formatDateTime(DateUtil.date()) + " 关闭订单";
		if (StrUtil.isNotBlank(reason)) {
			reasonStr += "，关闭原因：" + reason;
		}
		subHistory.setReason(reasonStr);
		this.orderHistoryDao.save(subHistory);
		return R.ok();
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
		orderRefundReturn.setRefundSource(OrderRefundSouceEnum.SHOP.value());
		//申请状态：平台审核通过（如有需要可以改成商家审核通过，然后走平台审核流程）
		orderRefundReturn.setApplyStatus(OrderRefundReturnStatusEnum.APPLY_WAIT_ADMIN.value());
		//卖家处理状态：卖家同意
		orderRefundReturn.setSellerStatus(OrderRefundReturnStatusEnum.SELLER_AGREE.value());
		//处理退款状态：退款处理中
		orderRefundReturn.setHandleSuccessStatus(OrderRefundReturnStatusEnum.HANDLE_PROCESSS.value());
		return orderRefundReturn;
	}

	@Override
	public PreSellOrderDTO getById(Long id) {
		return this.presellOrderConverter.to(this.presellOrderDao.getById(id));
	}

	@Override
	public PreSellOrderDTO getByOrderId(Long id) {
		return this.presellOrderConverter.to(this.presellOrderDao.getByOrderId(id));
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public R<Void> balanceHandler() {
		// 默认处理一条后的预售，建议与定时器执行周期一致
		Date now = new Date();
		DateTime time = DateUtil.offsetDay(now, 1);
		List<PreSellOrder> preSellOrderList = this.presellOrderDao.queryUnpaidBalanceOrderByDate(time);
		if (CollectionUtils.isEmpty(preSellOrderList)) {
			return R.ok();
		}
		// 超时处理订单，直接关闭订单
		List<PreSellOrder> timeOutOrder = preSellOrderList.stream().filter(p -> p.getFinalMEnd().before(now)).collect(Collectors.toList());
		cancelPreSellOrderList(this.presellOrderConverter.to(timeOutOrder));

		// 即将超时订单，放入队列，精确关闭
		List<PreSellOrder> aboutTimeOutOrder = preSellOrderList.stream().filter(p -> p.getFinalMEnd().after(now)).collect(Collectors.toList());
		if (CollectionUtils.isEmpty(aboutTimeOutOrder)) {
			return R.ok();
		}
		for (PreSellOrder presellOrder : aboutTimeOutOrder) {
			Long orderId = presellOrder.getOrderId();

			this.orderProducerService.balanceHandler(orderId, presellOrder.getFinalMEnd());
		}
		return R.ok();
	}
}
