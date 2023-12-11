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
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.*;
import cn.legendshop.jpaplus.support.PageSupport;
import cn.legendshop.jpaplus.util.StringUtils;
import com.alibaba.csp.sentinel.util.StringUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.legendshop.activity.api.CouponApi;
import com.legendshop.activity.api.CouponUserApi;
import com.legendshop.activity.dto.CouponUserDTO;
import com.legendshop.activity.enums.CouponUserStatusEnum;
import com.legendshop.activity.enums.ShopOperationTypeEnum;
import com.legendshop.basic.api.FileApi;
import com.legendshop.basic.api.MessageApi;
import com.legendshop.basic.api.SysParamsApi;
import com.legendshop.basic.dto.ExportExcelTaskDTO;
import com.legendshop.basic.dto.MessagePushDTO;
import com.legendshop.basic.dto.MsgSendParamDTO;
import com.legendshop.basic.dto.OrderSettingDTO;
import com.legendshop.basic.enums.*;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.constant.StringConstant;
import com.legendshop.common.core.enums.VisitSourceEnum;
import com.legendshop.common.core.expetion.BusinessException;
import com.legendshop.common.excel.executor.ExportExcelExecutor;
import com.legendshop.common.excel.executor.ExportExcelRunnable;
import com.legendshop.common.excel.util.EasyExcelUtils;
import com.legendshop.common.logistics.kuaidi100.contant.QueryTrackStatusEnum;
import com.legendshop.common.logistics.kuaidi100.response.SubscribePushParamResp;
import com.legendshop.common.rabbitmq.constants.AmqpConst;
import com.legendshop.common.rabbitmq.producer.LogisticsProducerService;
import com.legendshop.common.rabbitmq.util.AmqpSendMsgUtil;
import com.legendshop.common.security.dto.BaseUserDetail;
import com.legendshop.common.security.utils.SecurityUtils;
import com.legendshop.data.dto.DataUserPurchasingDTO;
import com.legendshop.order.bo.*;
import com.legendshop.order.dao.*;
import com.legendshop.order.dto.*;
import com.legendshop.order.entity.*;
import com.legendshop.order.enums.*;
import com.legendshop.order.excel.InsertLogisticsDTO;
import com.legendshop.order.excel.OrderExportDTO;
import com.legendshop.order.listener.LogisticsInfoExcelListener;
import com.legendshop.order.mq.producer.OrderProducerService;
import com.legendshop.order.query.OrderSearchQuery;
import com.legendshop.order.service.*;
import com.legendshop.order.service.convert.OrderConverter;
import com.legendshop.order.service.convert.OrderItemConverter;
import com.legendshop.order.service.convert.PreSellOrderConverter;
import com.legendshop.order.service.impl.handler.LoginCartHandlerImpl;
import com.legendshop.pay.api.*;
import com.legendshop.pay.dto.*;
import com.legendshop.pay.enums.PaySettlementStateEnum;
import com.legendshop.pay.enums.SettlementTypeEnum;
import com.legendshop.pay.enums.UserWalletAmountTypeEnum;
import com.legendshop.pay.enums.WalletBusinessTypeEnum;
import com.legendshop.product.api.*;
import com.legendshop.product.dto.*;
import com.legendshop.product.enums.InventoryOperationsTypeEnum;
import com.legendshop.product.enums.PreSellPayType;
import com.legendshop.shop.api.ShopDetailApi;
import com.legendshop.shop.dto.ShopDetailDTO;
import com.legendshop.user.api.UserDetailApi;
import com.rabbitmq.client.Channel;
import io.seata.spring.annotation.GlobalTransactional;
import io.seata.tm.api.GlobalTransaction;
import io.seata.tm.api.GlobalTransactionContext;
import io.seata.tm.api.transaction.TransactionHookAdapter;
import io.seata.tm.api.transaction.TransactionHookManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static com.legendshop.order.enums.OrderTypeEnum.*;

/**
 * 订单服务实现.
 *
 * @author legendshop
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

	private final OrderDao orderDao;

	private final SkuApi skuApi;

	private final FileApi fileApi;

	private final StockApi stockApi;

	private final VitLogApi vitLogApi;

	private final CouponApi couponApi;

	private final OrderItemDao orderItemDao;

	private final PaymentApi paymentApi;

	private final ProductApi productApi;

	private final OrderConverter orderConverter;

	private final CategoryApi categoryApi;

	private final OrderHistoryDao orderHistoryDao;

	private final SysParamsApi sysParamsApi;

	private final MessageApi messagePushClient;


	private final AmqpSendMsgUtil amqpSendMsgUtil;

	private final UserWalletApi userWalletApi;

	private final ShopOrderBillDao shopOrderBillDao;

	private final ShopDetailApi shopDetailApi;

	private final CouponUserApi couponUserApi;

	private final StringRedisTemplate redisTemplate;

	private final UserDetailApi userDetailApi;

	private final OrderLogisticsDao orderLogisticsDao;

	private final ExportExcelExecutor exportExcelExecutor;

	private final LoginCartHandlerImpl loginCartHandler;

	private final OrderItemConverter orderItemConverter;

	private final PaySettlementApi paySettlementApi;

	private final WalletDetailsApi walletDetailsApi;

	private final OrderProducerService orderProducerService;

	private final OrderLogisticsService orderLogisticsService;

	private final LogisticsCompanyService logisticsCompanyService;

	private final UserWalletBusinessApi userWalletBusinessApi;

	private final LogisticsProducerService logisticsProducerService;

	private final OrderRefundReturnService orderRefundReturnService;

	private final OrderRefundReturnDao orderRefundReturnDao;

	private final PreSellOrderService preSellOrderService;

	private final PreSellOrderConverter preSellOrderConverter;

	private final PaySettlementItemApi paySettlementItemApi;

	private final LogisticsCompanyDao logisticsCompanyDao;


	private final OrderImportLogisticsService importLogisticsService;

	private final OrderImportLogisticsDetailService importLogisticsDetailService;

	private final EasyExcelUtils easyExcelUtils;

	private final OrderItemService orderItemService;

	private final MessageApi messageApi;


	@Override
	public Long save(OrderDTO orderDTO) {
		if (ObjectUtil.isNotEmpty(orderDTO.getId())) {
			return orderDao.save(orderConverter.from(orderDTO), orderDTO.getId());
		}
		return orderDao.save(orderConverter.from(orderDTO));
	}

	@Override
	public void deleteById(Long id) {
		orderDao.deleteById(id);
	}

	@Override
	public void update(OrderDTO orderDTO) {
		orderDao.update(orderConverter.from(orderDTO));
	}

	@Override
	public int update(List<OrderDTO> orderList) {
		return this.orderDao.update(this.orderConverter.from(orderList));
	}

	@Override
	public OrderDTO getById(Long orderId) {
		return orderConverter.to(orderDao.getById(orderId));
	}

	@Override
	public OrderDTO getByOrderNumber(String orderNumber) {
		return orderConverter.to(orderDao.getByOrderNumber(orderNumber));
	}

	@Override
	public Long creatId() {
		return orderDao.createId();
	}


	@Override
	public boolean updateOrderPrice(String orderNumber, BigDecimal freight, BigDecimal orderAmount, Long shopId) {
		Order order = orderDao.getByOrderNumberByShopId(orderNumber, shopId);
		if (order != null && OrderStatusEnum.UNPAID.getValue().equals(order.getStatus())) {
			OrderHistory orderHistory;
			BigDecimal totalCash = NumberUtil.add(freight, orderAmount);

			BigDecimal actualTotalPrice = order.getActualTotalPrice();
			int i1 = actualTotalPrice.compareTo(BigDecimal.ZERO);
			BigDecimal divide = BigDecimal.ZERO;
			if (i1 > 0) {

				divide = orderAmount.divide(actualTotalPrice, 2, RoundingMode.DOWN);
			} else {
				divide = orderAmount.divide(order.getTotalPrice(), 2, RoundingMode.DOWN);
			}
			//防止空指针
			if (ObjectUtil.isEmpty(freight)) {
				freight = BigDecimal.ZERO;
			}
			if (!freight.equals(order.getFreightPrice()) || !totalCash.equals(order.getActualTotalPrice())) {
				orderHistory = new OrderHistory();
				Date time = new Date();
				orderHistory.setCreateTime(time);
				orderHistory.setStatus(OrderOperationEnum.PRICE_CHANGE.value());
				orderHistory.setOrderId(order.getId());
				String reason = order.getUserId() + "于" + DateUtil.now() + "调整价格操作,原订单总金额:" + order.getActualTotalPrice() + ",原订单运费金额:" + order.getFreightPrice() + "--> 调整订单总金额为:"
						+ totalCash + ",调整运费金额为:" + freight;
				orderHistory.setReason(reason);

				//计算修改的总金额，冗余到sub表  修改金额 = 原订单总金额 - 调整后订单总金额
				BigDecimal oldChangedPrice = order.getChangedPrice();
				BigDecimal newChangedPrice = NumberUtil.sub(totalCash, order.getActualTotalPrice());
				// 原订单商品总金额 = 原订单总金额 - 原运费
				BigDecimal oldProdAmount = NumberUtil.sub(order.getActualTotalPrice(), order.getFreightPrice());

				//多次调整订单，叠加修改金额
				oldChangedPrice = NumberUtil.add(oldChangedPrice, newChangedPrice);

				//更新订单信息
				order.setChangedPrice(oldChangedPrice);
				order.setActualTotalPrice(totalCash);
				order.setFreightPrice(freight);

				//更新订单项实际实付金额（订单项实付金额 = （订单项总金额/订单总金额）* 不含运费的订单金额）
				List<OrderItem> orderItems = orderItemDao.getByOrderNumber(orderNumber);
				BigDecimal difference = orderAmount;
				for (int i = 0; i < orderItems.size(); i++) {
					OrderItem orderItem = orderItems.get(i);
					BigDecimal amount = orderItem.getActualAmount();
					int i2 = amount.compareTo(BigDecimal.ZERO);
					if (i2 == 0) {
						amount = orderItem.getProductTotalAmount();
					}
					BigDecimal itemActualAmount = NumberUtil.mul(amount, divide.setScale(2, RoundingMode.DOWN));
					orderItem.setActualAmount(itemActualAmount);
					difference = difference.subtract(itemActualAmount);
					if (i == (orderItems.size() - 1)) {
						orderItem.setActualAmount(NumberUtil.add(orderItem.getActualAmount(), difference));
					}
				}
				orderItemDao.update(orderItems);

				// 记录订单日志和系统操作日记
				orderHistoryDao.save(orderHistory);
				orderDao.update(order);

			}
			return true;
		}
		return false;
	}


	@Override
	public int remindDeliveryBySn(String orderNumber, Long shopId, Long userId) {
		R<ShopDetailDTO> shopDetailResult = shopDetailApi.getById(shopId);
		if (!shopDetailResult.success()) {
			throw new BusinessException("调用店铺信息服务接口失败");
		}
		ShopDetailDTO shopDetailDTO = shopDetailResult.getData();
		if (ObjectUtil.isEmpty(shopDetailDTO)) {
			throw new BusinessException("该店铺信息不存在");
		}
		Order order = orderDao.getByOrderNumber(orderNumber);
		if (ObjectUtil.isEmpty(order)) {
			throw new BusinessException("该订单已不存在或被删除，请刷新页面或联系管理员");
		}
		if (ObjectUtil.isNotEmpty(order.getRemindDeliveryFlag())) {
			if (order.getRemindDeliveryFlag()) {
				throw new BusinessException("您已经提醒过商家了哦, 请耐心等候吧");
			}
		}
		if (!OrderRefundReturnStatusEnum.ORDER_NO_REFUND.value().equals(order.getRefundStatus())) {
			throw new BusinessException("正在申请退款中,请耐心等候");
		}
		int i = orderDao.remindDeliveryBySN(orderNumber, userId);
		if (i > 0) {
			//发送提醒发货站内信给商家
			List<MsgSendParamDTO> msgSendParamDtos = new ArrayList<>();
			//替换参数内容
			MsgSendParamDTO refundSnDTO = new MsgSendParamDTO(MsgSendParamEnum.ORDER_NUMBER, orderNumber, "black");
			msgSendParamDtos.add(refundSnDTO);
			messagePushClient.push(new MessagePushDTO()
					.setMsgReceiverTypeEnum(MsgReceiverTypeEnum.SHOP_USER)
					//修改为填写店铺id
					.setReceiveIdArr(new Long[]{shopDetailResult.getData().getId()})
					.setMsgSendType(MsgSendTypeEnum.PROD_REMIND_DELIVERY)
					.setSysParamNameEnum(SysParamNameEnum.ORDER_REMIND_DELIVERY)
					.setMsgSendParamDTOList(msgSendParamDtos)
					.setDetailId(order.getId())
			);
		}
		return i;
	}


	@Override
	public BigDecimal getReturnRedPackOffPrice(List<String> orderNumbers) {
		return orderDao.getReturnRedPackOffPrice(orderNumbers);
	}

	@Override
	public PageSupport<OrderBO> queryOrderWithItem(OrderSearchQuery orderSearchQuery) {
		PageSupport<OrderBO> pageSupport = orderDao.queryOrderWithItem(orderSearchQuery);
		List<OrderBO> orderBOList = pageSupport.getResultList();
		if (CollUtil.isEmpty(orderBOList)) {
			return pageSupport;
		}
		//检查订单是否可以显示评价
		checkAndSetCommentValidity(orderBOList);
		//获取订单id集合
		List<Long> ids = orderBOList.stream().map(OrderBO::getId).collect(Collectors.toList());

		//根据id集合找到订单项集合
		List<OrderItemDTO> orderItemDTOList = orderItemDao.queryDetailByOrderId(ids);
		//合并同一订单的订单项
		Map<Long, List<OrderItemDTO>> itemMap = orderItemDTOList.stream().collect(Collectors.groupingBy(OrderItemDTO::getOrderId));
		// 获取售后订单项列表
		List<Long> refundIds = orderItemDTOList.stream().filter(e -> !OrderRefundReturnStatusEnum.ITEM_NO_REFUND.value().equals(e.getRefundStatus())).map(OrderItemDTO::getRefundId).collect(Collectors.toList());
		//查询未发起退款 全部订单id
		List<Long> orderRefundIds = orderBOList.stream().filter(e -> !OrderRefundReturnStatusEnum.ITEM_NO_REFUND.value().equals(e.getRefundStatus())).map(OrderBO::getId).collect(Collectors.toList());
		//根据订单id查询 退款id 组装成一个map
		List<OrderRefundReturn> orderRefundReturnList = orderRefundReturnDao.getByOrderId(orderRefundIds);
		Map<Long, List<OrderRefundReturn>> orderMap = orderRefundReturnList.stream().collect(Collectors.groupingBy(OrderRefundReturn::getOrderId));
		//订单项退款id
		List<OrderRefundReturnDTO> returnDTOList = orderRefundReturnService.queryById(refundIds);
		//订单退款id
		Map<Long, List<OrderRefundReturnDTO>> returnMap = returnDTOList.stream().collect(Collectors.groupingBy(OrderRefundReturnDTO::getOrderId));
		// 获取非普通订单，拼装特殊信息
		Map<String, List<OrderBO>> orderTypeMap = orderBOList.stream().collect(Collectors.groupingBy(OrderBO::getOrderType));
		// 预售商品信息
		List<OrderBO> preSaleOrderList = orderTypeMap.get(OrderTypeEnum.PRE_SALE.getValue());
		List<PreSellOrderDTO> preSellOrderList = new ArrayList<>();
		if (!CollectionUtils.isEmpty(preSaleOrderList)) {
			//获取预售订单id
			List<Long> orderIds = preSaleOrderList.stream().map(OrderBO::getId).collect(Collectors.toList());
			//根据id查询预售订单
			preSellOrderList = this.preSellOrderService.queryByOrderIds(orderIds);
		}
		List<PreSellOrderDTO> finalPreSellOrderList = preSellOrderList;
		//获取商品状态
		List<Long> productIdList = orderItemDTOList.stream().map(OrderItemDTO::getProductId).collect(Collectors.toList());
		R<List<ProductDTO>> productList = productApi.queryAllByIds(productIdList);
		//根据id进行分组
		Map<Long, ProductDTO> productDtoMap = new HashMap<>(16);
		if (CollUtil.isNotEmpty(productList.getData())) {
			productDtoMap = productList.getData().stream().collect(Collectors.toMap(ProductDTO::getId, e -> e));
		}
		//设置商品状态
		for (OrderItemDTO orderItemDTO : orderItemDTOList) {
			if (!productDtoMap.containsKey(orderItemDTO.getProductId())) {
				continue;
			}
			orderItemDTO.setProductStatus(productDtoMap.get(orderItemDTO.getProductId()).getStatus());
		}
		//将订单项存入对应的订单对象
		orderBOList.forEach(orderBO -> {
			//封装订单商品信息
			wrapItemInfo(orderBO, itemMap);
			//根据returnMap封装退款ID、退款类型
			wrapRefundInfoByReturnMap(orderBO, returnMap);
			//根据orderMap封装退款信息
			wrapRefundInfoByOrderMap(orderBO, orderMap);
			//封装支付信息
			wrapSettlementType(orderBO, finalPreSellOrderList);

		});
		return pageSupport;
	}

	/**
	 * 封装订单商品信息
	 */
	private void wrapItemInfo(OrderBO orderBO, Map<Long, List<OrderItemDTO>> itemMap) {
		// 如果最终退货截止日期不为null，则将其时间戳设置为订单的最终退货截止时间
		Optional.ofNullable(orderBO.getFinalReturnDeadline()).ifPresent(deadline -> orderBO.setFinalReturnDeadlineTimestamp(deadline.getTime()));

		// 获取订单物流信息
		R<OrderLogisticsDTO> orderLogisticsDtoR = orderLogisticsService.queryLogisticsInformation(orderBO);
		if (orderLogisticsDtoR.success()) {
			// 如果成功获取到物流信息，则设置订单的物流信息
			orderBO.setOrderLogisticsDTO(orderLogisticsDtoR.getData());
		}

		// 如果itemMap中包含订单ID
		if (itemMap.containsKey(orderBO.getId())) {
			// 设置订单的商品项列表为itemMap中对应订单ID的商品列表
			orderBO.setOrderItemDTOList(itemMap.get(orderBO.getId()));
		}
	}

	/**
	 * 封装退款ID、退款类型
	 */
	private void wrapRefundInfoByReturnMap(OrderBO orderBO, Map<Long, List<OrderRefundReturnDTO>> returnMap) {
		// 如果returnMap中包含订单ID
		if (returnMap.containsKey(orderBO.getId())) {
			// 获取订单对应的退款退货DTO列表
			List<OrderRefundReturnDTO> dtoList = returnMap.get(orderBO.getId());

			// 在DTO列表中查找第一个skuId等于0的元素
			Optional<OrderRefundReturnDTO> optional = dtoList.stream().filter(e -> e.getSkuId() == 0).findFirst();

			if (optional.isPresent()) {
				// 如果存在skuId等于0的元素，则表示是全单退
				orderBO.setOrderRefundType(1);
				orderBO.setRefundId(optional.get().getId());
			} else {
				// 否则表示是多单售后，取第一个元素的退款退货DTO
				OrderRefundReturnDTO orderRefundReturnDTO = dtoList.get(0);
				orderBO.setOrderRefundType(2);
				orderBO.setRefundId(orderRefundReturnDTO.getId());
			}
		}
	}


	/**
	 * 封装退款信息
	 */
	private void wrapRefundInfoByOrderMap(OrderBO orderBO, Map<Long, List<OrderRefundReturn>> orderMap) {
		//根据订单id 查询 refundId
		if (orderMap.containsKey(orderBO.getId())) {
			List<OrderRefundReturn> orderRefundReturns = orderMap.get(orderBO.getId());
			Optional<OrderRefundReturn> first = orderRefundReturns.stream().filter(e -> e.getSkuId() == 0).findFirst();
			if (first.isPresent()) {
				//如果是商申请取消订单 添加ApplyType  ApplyStatus
				if (OrderRefundReturnTypeEnum.REFUND_CANEL.value().equals(first.get().getApplyType())) {
					orderBO.setApplyType(first.get().getApplyType());
					orderBO.setApplyStatus(first.get().getApplyStatus());
					orderBO.setOrderRefundType(1);
					orderBO.setRefundId(first.get().getId());
				}
			}
		}
	}

	/**
	 * 封装支付类型
	 */
	private void wrapSettlementType(OrderBO orderBO, List<PreSellOrderDTO> finalPreSellOrderList) {
		//如果是未支付，则获取订单允许的最后支付时间
		if (OrderStatusEnum.UNPAID.getValue().equals(orderBO.getStatus())) {
			Date finalPayTime = getFinalPayTime(orderBO.getCreateTime(), orderBO.getCancelUnpayMinutes(), orderBO.getOrderType());
			orderBO.setFinalPayTime(finalPayTime.getTime());

			//极端情况：等待时间超过了定金支付的结束时间
			if (OrderTypeEnum.PRE_SALE.getValue().equals(orderBO.getOrderType())) {
				List<PreSellOrderDTO> orderDtoList = finalPreSellOrderList.stream().filter(e -> e.getOrderId().equals(orderBO.getId()) && e.getPreSaleEnd().before(finalPayTime)).collect(Collectors.toList());
				if (CollectionUtil.isNotEmpty(orderDtoList)) {
					orderDtoList.stream().filter(e -> e.getOrderId().equals(orderBO.getId())).forEach(e -> {
						orderBO.setFinalPayTime(e.getPreSaleEnd().getTime());
					});
				}
			}
		}
		if (ORDINARY.getValue().equals(orderBO.getOrderType()) || orderBO.getOrderType().equals(SINCE_MENTION.getValue())) {
			orderBO.setSettlementType(SettlementTypeEnum.ORDINARY_ORDER.getValue());
		} else if (OrderTypeEnum.PRE_SALE.getValue().equals(orderBO.getOrderType())) {
			preBussian(finalPreSellOrderList, orderBO);
		}
	}

	/**
	 * 检查订单是否可以显示评价
	 *
	 * @param orderBOList 包含订单信息的列表
	 *                    对象中需要包含订单状态(status)、完成时间(completeTime)和评价有效天数(commentValidDay)
	 */
	public void checkAndSetCommentValidity(List<OrderBO> orderBOList) {
		//已完成是否显示评价
		List<OrderBO> orderStatuList = orderBOList.stream().filter(e -> OrderStatusEnum.SUCCESS.getValue().equals(e.getStatus())).collect(Collectors.toList());

		if (CollUtil.isNotEmpty(orderStatuList)) {
			orderStatuList.forEach(order -> {
				if (ObjectUtil.isNotEmpty(order.getCompleteTime()) && ObjectUtil.isNotEmpty(order.getCommentValidDay())) {
					Date validDay = DateUtil.offsetDay(order.getCompleteTime(), order.getCommentValidDay());
					order.setCommentValidDayInvoice(validDay.before(DateUtil.date()) ? Boolean.FALSE : Boolean.TRUE);
				}
			});
		}

	}


	private void preBussian(List<PreSellOrderDTO> finalPreSellOrderList, OrderBO orderBO) {
		List<PreSellOrderBO> preSellOrderBOList = preSellOrderConverter.convert2BO(finalPreSellOrderList);
		finalPreSellOrderList.stream().filter(e -> e.getOrderId().equals(orderBO.getId())).findFirst().ifPresent(o -> {
			preSellOrderBOList.stream().filter(e -> e.getOrderId().equals(orderBO.getId())).findFirst().ifPresent(preSellOrderBO -> {
				if (preSellOrderBO.getPayPctType().equals(PreSellPayType.DEPOSIT.value())) {
					preSellOrderBO.setDepositStarts(o.getPreSaleStart().getTime());
					preSellOrderBO.setDepositEnds(o.getPreSaleStart().getTime());
					preSellOrderBO.setFinalMStarts(o.getFinalMStart().getTime());
					preSellOrderBO.setFinalMEnds(o.getFinalMEnd().getTime());
					if (!o.getPayDepositFlag() && orderBO.getStatus().equals(OrderStatusEnum.UNPAID.getValue())) {
						orderBO.setSettlementType(SettlementTypeEnum.PRE_SALE_ORDER_DEPOSIT.getValue());
					}
					if (o.getPayDepositFlag() && orderBO.getStatus().equals(OrderStatusEnum.PRESALE_DEPOSIT.getValue())) {
						orderBO.setSettlementType(SettlementTypeEnum.PRE_SALE_ORDER_FINAL.getValue());
						preSellOrderBO.setDepositPayTime(o.getDepositPayTime());
						preSellOrderBO.setPayDepositFlag(true);
					}
					if (o.getPayFinalFlag() && orderBO.getStatus().equals(OrderStatusEnum.WAIT_DELIVERY.getValue())) {
						preSellOrderBO.setPayFinalTime(o.getPayFinalTime());
						preSellOrderBO.setPayFinalFlag(true);
					}
				}
				//全额
				if (preSellOrderBO.getPayPctType().equals(PreSellPayType.FULL_AMOUNT.value()) && !o.getPayDepositFlag() && orderBO.getStatus().equals(OrderStatusEnum.UNPAID.getValue())) {
					orderBO.setSettlementType(SettlementTypeEnum.PRE_SALE_ORDER_DEPOSIT.getValue());
				}
				preSellOrderBO.setDeliveryTime(o.getPreDeliveryTime().getTime());
				preSellOrderBO.setDeliveryEndTime(o.getPreDeliveryEndTime().getTime());
/*						if(new Date().after(preSellOrderBO.getPreDeliveryTime())&&new Date().before(preSellOrderBO.getPreDeliveryEndTime())){
					orderBO.setStatus(5);
				}可能在发货时间就到完成状态了*/
				orderBO.setPreSellOrderBO(preSellOrderBO);
			});
		});
	}

	/**
	 * 获取订单允许的最后支付时间
	 */
	private Date getFinalPayTime(Date orderTime, Integer cancelUnpayMinutes, String orderType) {
		if (null == cancelUnpayMinutes) {
			// 获取订单的系统配置
			// 处理拆分为微服务后，强转失败问题
			ObjectMapper mapper = new ObjectMapper();
			OrderSettingDTO orderSetting = mapper.convertValue(sysParamsApi.getConfigDtoByParamName(SysParamNameEnum.ORDER_SETTING.name(), OrderSettingDTO.class).getData(), OrderSettingDTO.class);
		}
		return DateUtil.offsetMinute(orderTime, cancelUnpayMinutes);
	}

	@Override
	public OrderBO getAdminOrderDetail(Long orderId) {
		OrderBO orderBO = orderDao.getAdminOrderDetail(orderId);
		if (null == orderBO) {
			return null;
		}
		//获取支付方式
		List<OrderPayTypeDTO> orderPayTypeDTOList = getOrderPay(orderBO.getOrderNumber());
		orderBO.setOrderPayTypeList(orderPayTypeDTOList);

		//根据id找到订单项集合
		List<OrderItemDTO> orderItemDTOList = orderItemConverter.to(orderItemDao.getByOrderId(orderId));
		orderBO.setOrderItemDTOList(orderItemDTOList);
		// 返回订单中的分销佣金金额
		orderBO.setDistCommisAmount(orderItemDTOList.stream().filter(OrderItemDTO::getDistFlag).map(OrderItemDTO::getDistCommissionCash).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add));

		//如果是未支付，则获取订单允许的最后支付时间
		if (OrderStatusEnum.UNPAID.getValue().equals(orderBO.getStatus())) {
			Date finalPayTime = getFinalPayTime(orderBO.getCreateTime(), orderBO.getCancelUnpayMinutes(), orderBO.getOrderType());
			orderBO.setFinalPayTime(finalPayTime.getTime());
		}
		//获取物流信息
		R<OrderLogisticsDTO> orderLogisticsDtoR = orderLogisticsService.queryLogisticsInformation(orderBO);
		if (!orderLogisticsDtoR.success() && ObjectUtil.isEmpty(orderLogisticsDtoR.getData())) {
			log.info("配置错误");
		}
		orderBO.setOrderLogisticsDTO(orderLogisticsDtoR.getData());

		//获取预售信息
		if (PRE_SALE.getValue().equals(orderBO.getOrderType())) {
			//设置预售订单信息
			setPreSaleOrder(orderBO, orderId);
		}

		// 判断是否需要返回售后信息
		List<OrderItemDTO> refundOrderItemList = orderItemDTOList.stream().filter(item -> ObjectUtil.isNotEmpty(item.getRefundId())).collect(Collectors.toList());
		if (CollUtil.isEmpty(refundOrderItemList)) {
			return orderBO;
		}

		Map<Long, List<OrderItemDTO>> refundOrderItemMap = refundOrderItemList.stream().collect(Collectors.groupingBy(OrderItemDTO::getRefundId));
		List<OrderRefundReturnDTO> returnList = orderRefundReturnService.queryById(new ArrayList<>(refundOrderItemMap.keySet()));
		for (OrderRefundReturnDTO orderRefundReturnDTO : returnList) {
			if (refundOrderItemMap.containsKey(orderRefundReturnDTO.getId())) {
				refundOrderItemMap.get(orderRefundReturnDTO.getId()).forEach(orderItemDTO -> {
					orderItemDTO.setGoodsStatus(orderRefundReturnDTO.getGoodsStatus());
					orderItemDTO.setApplyStatus(orderRefundReturnDTO.getApplyStatus());
					orderItemDTO.setSellerStatus(orderRefundReturnDTO.getSellerStatus());
				});
			}
		}

		return orderBO;
	}


	/**
	 * //设置预售订单信息
	 *
	 * @param orderBO
	 * @param orderId
	 * @return
	 */
	private void setPreSaleOrder(OrderBO orderBO, Long orderId) {
		PreSellOrderBO sellOrderBO = preSellOrderConverter.convert2BO(preSellOrderService.getByOrderId(orderId));
		if (sellOrderBO.getPayPctType().equals(PreSellPayType.DEPOSIT.value())) {
			sellOrderBO.setDepositStarts(sellOrderBO.getPreSaleStart().getTime());
			sellOrderBO.setDepositEnds(sellOrderBO.getPreSaleEnd().getTime());
			sellOrderBO.setFinalMStarts(sellOrderBO.getFinalMStart().getTime());
			sellOrderBO.setFinalMEnds(sellOrderBO.getFinalMEnd().getTime());
			if (!sellOrderBO.getPayDepositFlag() && orderBO.getStatus().equals(OrderStatusEnum.UNPAID.getValue())) {
				orderBO.setSettlementType(SettlementTypeEnum.PRE_SALE_ORDER_DEPOSIT.getValue());
			}
			if (sellOrderBO.getPayDepositFlag() && orderBO.getStatus().equals(OrderStatusEnum.PRESALE_DEPOSIT.getValue())) {
				orderBO.setSettlementType(SettlementTypeEnum.PRE_SALE_ORDER_FINAL.getValue());
				sellOrderBO.setDepositPayTime(sellOrderBO.getDepositPayTime());
				sellOrderBO.setPayDepositFlag(true);
			}
			if (sellOrderBO.getPayFinalFlag() && orderBO.getStatus().equals(OrderStatusEnum.WAIT_DELIVERY.getValue())) {
				sellOrderBO.setPayFinalTime(sellOrderBO.getPayFinalTime());
				sellOrderBO.setPayFinalFlag(true);
			}
		}
		//全额
		if (sellOrderBO.getPayPctType().equals(PreSellPayType.FULL_AMOUNT.value()) && !sellOrderBO.getPayDepositFlag() && orderBO.getStatus().equals(OrderStatusEnum.UNPAID.getValue())) {
			orderBO.setSettlementType(SettlementTypeEnum.PRE_SALE_ORDER_DEPOSIT.getValue());
			if (sellOrderBO.getPayDepositFlag() && orderBO.getStatus().equals(OrderStatusEnum.PRESALE_DEPOSIT.getValue())) {
				orderBO.setStatus(5);
				sellOrderBO.setDepositPayTime(sellOrderBO.getDepositPayTime());
				sellOrderBO.setPayDepositFlag(true);
			}
		}
		sellOrderBO.setDeliveryTime(sellOrderBO.getPreDeliveryTime().getTime());
		sellOrderBO.setDeliveryEndTime(sellOrderBO.getPreDeliveryEndTime().getTime());

		orderBO.setPreSellOrderBO(sellOrderBO);
	}

	@Override
	public R<OrderBO> getUserOrderDetail(Long orderId, Long userId) {
		OrderBO orderBO = orderDao.getUserOrderDetail(orderId, userId);
		R<OrderLogisticsDTO> orderLogisticsDtoR = null;
		if (null == orderBO) {
			return R.ok();
		}
		//已完成是否显示评价
		if (OrderStatusEnum.SUCCESS.getValue().equals(orderBO.getStatus())) {
			Date validDay = DateUtil.offsetDay(orderBO.getCompleteTime(), orderBO.getCommentValidDay());
			orderBO.setCommentValidDayInvoice(validDay.before(DateUtil.date()) ? Boolean.FALSE : Boolean.TRUE);
		}
		//获取支付方式
		List<OrderPayTypeDTO> orderPayTypeDTOList = getOrderPay(orderBO.getOrderNumber());
		orderBO.setOrderPayTypeList(orderPayTypeDTOList);

		Optional.ofNullable(orderBO.getFinalReturnDeadline()).ifPresent(deadline -> orderBO.setFinalReturnDeadlineTimestamp(deadline.getTime()));
		//根据id找到订单项集合
		List<OrderItemDTO> orderItemDTOList = orderItemDao.queryDetailByOrderId(orderId);
		orderBO.setOrderItemDTOList(orderItemDTOList);

		// 返回订单中的分销佣金金额
		orderBO.setDistCommisAmount(orderItemDTOList.stream().filter(OrderItemDTO::getDistFlag).map(OrderItemDTO::getDistCommissionCash).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add));

		//如果是未支付，则获取订单允许的最后支付时间
		if (OrderStatusEnum.UNPAID.getValue().equals(orderBO.getStatus())) {
			Date finalPayTime = getFinalPayTime(orderBO.getCreateTime(), orderBO.getCancelUnpayMinutes(), orderBO.getOrderType());
			orderBO.setFinalPayTime(finalPayTime.getTime());
		}
		//获取物流信息
		orderLogisticsDtoR = orderLogisticsService.queryLogisticsInformation(orderBO);
		if (orderLogisticsDtoR.success()) {
			OrderLogisticsDTO data = orderLogisticsDtoR.getData();
			orderBO.setOrderLogisticsDTO(data);
		}

		//获取预售信息
		if (PRE_SALE.getValue().equals(orderBO.getOrderType())) {
			setPreSaleOrder(orderBO, orderId);
		}

		//获取自动收货时长
		//设置自动收货时间，若发货时间没有则为null
		if (ObjectUtil.isNotEmpty(orderBO.getReceivingDay()) && orderBO.getReceivingDay() > 0) {
			if (orderBO.getDeliveryTime() != null) {
				Date offset = DateUtil.offset(orderBO.getDeliveryTime(), DateField.DAY_OF_YEAR, orderBO.getReceivingDay());
				orderBO.setCountDownTime(offset.getTime());
			}
		}
		return R.ok(orderBO, orderLogisticsDtoR.getMsg());
	}

	public List<OrderPayTypeDTO> getOrderPay(String orderNumber) {
		//获取支付方式
		R<List<PaySettlementItemDTO>> payTypeListR = paySettlementItemApi.queryPaidByOrderNumber(orderNumber);
		List<OrderPayTypeDTO> orderPayTypeDTOList = new ArrayList<>();
		if (ObjectUtil.isNotNull(payTypeListR.getData())) {
			List<PaySettlementItemDTO> payTypeList = payTypeListR.getData();
			for (PaySettlementItemDTO paySettlementItemDTO : payTypeList) {
				OrderPayTypeDTO orderPayTypeDTO = new OrderPayTypeDTO();
				String payTypeId = paySettlementItemDTO.getPayTypeId();
				String name = PayTypeEnum.getName(payTypeId);
				orderPayTypeDTO.setPayName(name);
				orderPayTypeDTO.setPayAmount(paySettlementItemDTO.getAmount());
				orderPayTypeDTOList.add(orderPayTypeDTO);
			}
		}
		return orderPayTypeDTOList;
	}


	@Override
	public OrderBO getShopOrderDetail(Long orderId, Long shopId) {
		OrderBO orderBO = orderDao.getShopOrderDetail(orderId, shopId);

		if (ObjectUtil.isNotEmpty(orderBO)) {
			//根据id找到订单项集合
			List<OrderItemDTO> orderItemDTOList = orderItemConverter.to(orderItemDao.getByOrderId(orderId));
			orderBO.setOrderItemDTOList(orderItemDTOList);
			//组装 商家订单的售后信息
			List<OrderRefundReturn> orderRefundReturnList = orderRefundReturnDao.getByOrderId(CollUtil.newArrayList(orderBO.getId()));
			if (CollUtil.isNotEmpty(orderRefundReturnList)) {
				orderRefundReturnList.stream()
						//按照创建时间的逆序（最新的排在最前面)选择第一个售后信息
						.max(Comparator.comparing(OrderRefundReturn::getCreateTime))
						//封装售后信息
						.ifPresent(orderRefundReturn -> wrapAfterSalesInfo(orderBO, orderRefundReturn));
			}

			// 返回订单中的分销佣金金额
			orderBO.setDistCommisAmount(orderItemDTOList.stream().filter(OrderItemDTO::getDistFlag).map(OrderItemDTO::getDistCommissionCash).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add));

			//如果是未支付，则获取订单允许的最后支付时间
			if (OrderStatusEnum.UNPAID.getValue().equals(orderBO.getStatus())) {
				Date finalPayTime = getFinalPayTime(orderBO.getCreateTime(), orderBO.getCancelUnpayMinutes(), orderBO.getOrderType());
				orderBO.setFinalPayTime(finalPayTime.getTime());
			}
			// 获取物流信息
			R<OrderLogisticsDTO> orderLogisticsDtoR = orderLogisticsService.queryLogisticsInformation(orderBO);
			if (orderLogisticsDtoR.success()) {
				orderBO.setOrderLogisticsDTO(orderLogisticsDtoR.getData());
			}

			//获取预售信息
			if (PRE_SALE.getValue().equals(orderBO.getOrderType())) {
				setPreSaleOrder(orderBO, orderId);
			}
			// 判断是否需要返回售后信息
			List<OrderItemDTO> refundOrderItemList = orderItemDTOList.stream().filter(item -> ObjectUtil.isNotEmpty(item.getRefundId())).collect(Collectors.toList());
			if (CollUtil.isEmpty(refundOrderItemList)) {
				return orderBO;
			}

			Map<Long, List<OrderItemDTO>> refundOrderItemMap = refundOrderItemList.stream().collect(Collectors.groupingBy(OrderItemDTO::getRefundId));
			List<OrderRefundReturnDTO> returnList = orderRefundReturnService.queryById(new ArrayList<>(refundOrderItemMap.keySet()));
			for (OrderRefundReturnDTO orderRefundReturnDTO : returnList) {
				if (refundOrderItemMap.containsKey(orderRefundReturnDTO.getId())) {
					refundOrderItemMap.get(orderRefundReturnDTO.getId()).forEach(orderItemDTO -> {
						orderItemDTO.setGoodsStatus(orderRefundReturnDTO.getGoodsStatus());
						orderItemDTO.setApplyStatus(orderRefundReturnDTO.getApplyStatus());
						orderItemDTO.setSellerStatus(orderRefundReturnDTO.getSellerStatus());
					});
				}
			}
		}

		return orderBO;
	}

	/**
	 * 封装售后信息
	 */
	private void wrapAfterSalesInfo(OrderBO orderBO, OrderRefundReturn orderRefundReturn) {

		OrderAftersalesInformationDTO orderAftersalesInformationDTO = new OrderAftersalesInformationDTO();
		orderAftersalesInformationDTO.setCreateTime(orderRefundReturn.getCreateTime());
		//用户申请的
		if (OrderRefundSouceEnum.USER.value().equals(orderRefundReturn.getRefundSource())) {
			orderAftersalesInformationDTO.setApplyType(orderRefundReturn.getApplyType());
			orderAftersalesInformationDTO.setRefundSource(orderRefundReturn.getRefundSource());
			orderAftersalesInformationDTO.setRefundSn(orderRefundReturn.getRefundSn());
			if (OrderRefundReturnTypeEnum.REFUND.value().equals(orderRefundReturn.getApplyStatus())) {
				orderAftersalesInformationDTO.setStatusInformation("退款");
			}
			if (OrderRefundReturnTypeEnum.REFUND_RETURN.value().equals(orderRefundReturn.getApplyStatus())) {
				orderAftersalesInformationDTO.setStatusInformation("退货");
			}
			orderAftersalesInformationDTO.setRefundStatus(orderBO.getRefundStatus());
		}
		//商家申请的取消
		if (OrderRefundSouceEnum.SHOP.value().equals(orderRefundReturn.getRefundSource()) && OrderRefundReturnTypeEnum.REFUND_CANEL.value().equals(orderRefundReturn.getApplyType())) {
			orderAftersalesInformationDTO.setApplyType(orderRefundReturn.getApplyType());
			orderAftersalesInformationDTO.setReason(orderRefundReturn.getReason());
			orderAftersalesInformationDTO.setSellerMessage(orderRefundReturn.getSellerMessage());
			//组装
			orderAftersalesInformationDTO.setRefundSource(orderRefundReturn.getRefundSource());
			if (OrderRefundReturnStatusEnum.APPLY_FINISH.value().equals(orderRefundReturn.getApplyStatus())) {
				orderAftersalesInformationDTO.setStatusInformation("已完成(平台已审核通过，订单转为取消状态)");
			}
			if (OrderRefundReturnStatusEnum.APPLY_WAIT_ADMIN.value().equals(orderRefundReturn.getApplyStatus())) {
				orderAftersalesInformationDTO.setStatusInformation("待管理员审核");
			}
			if (OrderRefundReturnStatusEnum.APPLY_ADMIN_REJECT.value().equals(orderRefundReturn.getApplyStatus())) {
				orderAftersalesInformationDTO.setStatusInformation("已拒绝(平台已审核拒绝)");
			}
			if (OrderRefundReturnStatusEnum.APPLY_CANCEL.value().equals(orderRefundReturn.getApplyStatus())) {
				orderAftersalesInformationDTO.setStatusInformation("已取消");
			}
		}
		orderBO.setOrderAftersalesInformationDTO(orderAftersalesInformationDTO);
	}

	@Override
	public boolean insertRemark(String orderNumber, String remark) {
		Order order = orderDao.getByOrderNumber(orderNumber);
		if (ObjectUtil.isEmpty(order)) {
			throw new BusinessException("订单已不存在，请刷新页面或联系管理员");
		}
		if (order.getRemarkedFlag()) {
			throw new BusinessException("该订单已备注，不能重复备注");
		}
		order.setRemarkedFlag(Boolean.TRUE);
		order.setRemark(remark);
		return orderDao.update(order) > 0;
	}

	@Override
	public boolean insertRemark(String orderNumber, String remark, Long shopId) {
		Order order = orderDao.getByOrderNumberByShopId(orderNumber, shopId);
		if (ObjectUtil.isEmpty(order)) {
			throw new BusinessException("订单已不存在，请刷新页面或联系管理员");
		}
		if (order.getRemarkedFlag()) {
			throw new BusinessException("该订单已备注，不能重复备注");
		}
		order.setRemarkedFlag(Boolean.TRUE);
		order.setRemark(remark);
		return orderDao.update(order) > 0;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	@GlobalTransactional(rollbackFor = Exception.class)
	public boolean cancelOrder(List<String> orderNumbers, String reason, Long shopId) {
		List<Order> orders = orderDao.queryByNumberAndShopIdAndStatus(orderNumbers, shopId, OrderStatusEnum.UNPAID.getValue());
		if (ObjectUtil.isEmpty(orders)) {
			throw new BusinessException("订单已不存在或状态已变更，请刷新页面或联系管理员");
		}
		List<Long> orderIds = orders.stream().map(Order::getId).collect(Collectors.toList());
		int result = orderDao.batchCancelUnPayOrder(orderIds);
		if (result <= 0) {
			throw new BusinessException("取消订单失败，请刷新重试");
		}
		//取消成功后处理
		//订单历史列表
		List<OrderHistory> orderHistories = new ArrayList<>();
		for (Order order : orders) {
			//库存处理
			returnStocks(orderConverter.to(order));

			//只有普通订单才有优惠券
			if (OrderTypeEnum.ORDINARY.getValue().equals(order.getOrderType())) {
				//回退优惠券
				returnCoupon(order.getOrderNumber());
			}

			//保存订单历史
			OrderHistory orderHistory = new OrderHistory();
			StringBuilder sb = new StringBuilder();
			sb.append("商家").append(order.getShopName()).append("于").append(DateUtil.formatDateTime(DateUtil.date())).append("取消订单 ");
			orderHistory.setReason(sb.toString());
			orderHistory.setCreateTime(DateUtil.date());
			orderHistory.setOrderId(order.getId());
			orderHistory.setStatus(OrderHistoryEnum.ORDER_CANCEL.value());
			orderHistories.add(orderHistory);

		}
		orderHistoryDao.save(orderHistories);
		return true;
	}

	@Override
	@GlobalTransactional(rollbackFor = Exception.class)
	public R<Void> cancelOrder(String orderNumber, Long userId) {
		Order order = orderDao.getByOrderNumberByUserId(orderNumber, userId);
		if (ObjectUtil.isEmpty(order)) {
			return R.fail("订单已不存在，请刷新页面或联系管理员");
		}
		if (!OrderStatusEnum.UNPAID.getValue().equals(order.getStatus())) {
			return R.fail("订单状态不是待支付状态，不能取消");
		}

		// 判断支付单是否支付成功
		R<PaySettlementDTO> settlementDtoR = paySettlementApi.getPaidByOrderNumber(orderNumber);
		if (Optional.ofNullable(settlementDtoR.getData()).map(PaySettlementDTO::getState).orElse(PaySettlementStateEnum.CREATE.getCode()).equals(PaySettlementStateEnum.PAID.getCode())) {
			return R.fail("取消失败，订单已支付成功，请联系管理员处理！");
		}

		int result = orderDao.cancelUnPayOrder(order.getId());
		if (result > 0) {
			//库存处理
			returnStocks(orderConverter.to(order));

			//只有普通订单才有优惠券
			if (OrderTypeEnum.ORDINARY.getValue().equals(order.getOrderType())) {
				//回退优惠券
				returnCoupon(order.getOrderNumber());
			}


			// 退还钱包
			giveBackUserWallet(order.getId(), true);

			//保存订单历史
			OrderHistory orderHistory = new OrderHistory();
			orderHistory.setReason("用户" + userId + "于" + DateUtil.formatDateTime(DateUtil.date()) + "取消订单 ");
			orderHistory.setCreateTime(DateUtil.date());
			orderHistory.setOrderId(order.getId());
			orderHistory.setStatus(OrderHistoryEnum.ORDER_CANCEL.value());
			orderHistoryDao.save(orderHistory);
			return R.ok();
		}
		return R.fail("取消订单失败！");
	}

	/**
	 * 订单取消，余额处理
	 */
	@Override
	public void giveBackUserWallet(Long orderId, Boolean isInitiative) {
		Order order = orderDao.getById(orderId);
		if (!order.getStatus().equals(OrderStatusEnum.CLOSE.getValue())) {
			log.info("订单取消，当前订单未取消，处理异常，订单号：{}，用户ID：{}，是否主动：{}", orderId, order.getUserId(), isInitiative);
			throw new BusinessException("当前订单未取消，钱包退款失败！");
		}

		// 获取下单记录
		R<List<UserWalletDetailsDTO>> detailsResult = walletDetailsApi.findDetailsByBusinessId(orderId, WalletBusinessTypeEnum.ORDER_DEDUCTION);

		// 只需要获取冻结金额（或者可用金额）即可，因为下单的时候这两个金额是成对保存的
		List<UserWalletDetailsDTO> walletDetailsList = Optional.ofNullable(detailsResult.getData()).orElse(Collections.emptyList())
				.stream().filter(e -> UserWalletAmountTypeEnum.FROZEN_AMOUNT.equals(e.getAmountType())).collect(Collectors.toList());
		if (CollUtil.isEmpty(walletDetailsList)) {
			log.info("订单取消，未找到钱包支付记录，钱包退还跳过处理，订单号：{}，用户ID：{}，是否主动：{}", orderId, order.getUserId(), isInitiative);
			return;
		}

		// 获取支付记录
		R<List<PaySettlementItemDTO>> result = paySettlementItemApi.queryPaidByOrderNumber(order.getOrderNumber());
		if (!result.getSuccess()) {
			log.info("订单取消失败，获取支付单失败，订单号：{}，用户ID：{}，是否主动：{}", orderId, order.getUserId(), isInitiative);
			throw new BusinessException("订单取消失败，服务异常！");
		}

		// 获取钱包支付记录
		List<PaySettlementItemDTO> paySettlementItemDTOList = Optional.ofNullable(result.getData()).orElse(Collections.emptyList())
				.stream().filter(e -> e.getPayTypeId().equals(PayTypeEnum.WALLET_PAY.name())).collect(Collectors.toList());

		// 操作记录ID
		List<Long> operationList = new ArrayList<>();

		// 如果存在，则不需要扣减冻结，只需要返还可用
		if (CollUtil.isNotEmpty(paySettlementItemDTOList)) {
			for (PaySettlementItemDTO paySettlementItemDTO : paySettlementItemDTOList) {
				UserWalletOperationDTO operationDTO = new UserWalletOperationDTO();
				operationDTO.setBusinessId(paySettlementItemDTO.getId());
				operationDTO.setBusinessType(WalletBusinessTypeEnum.PAYMENT_DEDUCTION_REFUND);
				operationDTO.setUserId(order.getUserId());
				operationDTO.setAmount(paySettlementItemDTO.getAmount());
				operationDTO.setRemarks("订单支付" + (isInitiative ? "取消" : "超时") + ", 退还钱包支付金额, 支付流水号: " + paySettlementItemDTO.getPaySettlementSn());
				R<Long> recordUpdate = userWalletBusinessApi.addAvailableRecordUpdate(operationDTO);
				operationList.add(recordUpdate.getData());
			}

		} else {
			// 否则，则表明是只有冻结金额，还未支付成功，只需要返还可用（扣减冻结，返还可用）
			// 此时需要扣减冻结金额并返还可用金额
			for (UserWalletDetailsDTO userWalletDetailsDTO : walletDetailsList) {
				// 将冻结金额返还至可用金额
				UserWalletOperationDTO operationDTO = new UserWalletOperationDTO();
				operationDTO.setBusinessId(userWalletDetailsDTO.getBusinessId());
				operationDTO.setBusinessType(WalletBusinessTypeEnum.ORDER_OVERTIME_REFUND);
				operationDTO.setUserId(userWalletDetailsDTO.getUserId());
				operationDTO.setAmount(userWalletDetailsDTO.getAmount());
				operationDTO.setRemarks("订单支付" + (isInitiative ? "取消" : "超时") + ", 退还钱包支付金额, 流水号: " + userWalletDetailsDTO.getSerialNo());

				R<Long> frozenRecordUpdate = userWalletBusinessApi.deductionFrozenRecordUpdate(operationDTO);
				operationList.add(frozenRecordUpdate.getData());

				R<Long> availableRecordUpdate = userWalletBusinessApi.addAvailableRecordUpdate(operationDTO);
				operationList.add(availableRecordUpdate.getData());
			}
		}

		// 如果不为空，则需要通知钱包服务处理金额退还业务
		if (CollUtil.isNotEmpty(operationList)) {
			GlobalTransaction currentTransaction = GlobalTransactionContext.getCurrent();
			//确认收货分销佣金处理
			if (null != currentTransaction) {
				TransactionHookManager.registerHook(new TransactionHookAdapter() {
					@Override
					public void afterCommit() {
						operationList.forEach(userWalletBusinessApi::synchronizeNotify);
					}
				});
			} else {
				operationList.forEach(userWalletBusinessApi::synchronizeNotify);
			}
		}
	}


	/**
	 * 取消订单返回库存
	 *
	 * @param order
	 */
	@Override
	@GlobalTransactional(rollbackFor = Exception.class)
	public void returnStocks(OrderDTO order) {
		//获取所有订单项
		List<OrderItem> orderItems = orderItemDao.getByOrderNumber(order.getOrderNumber());
		//普通订单回退实际库存，拼团/团购订单回退虚拟库存
		if (OrderTypeEnum.ORDINARY.getValue().equals(order.getOrderType())) {
			//通过商品id进行分组
			Map<Long, List<OrderItem>> orderItemMap = orderItems.stream().collect(Collectors.groupingBy(OrderItem::getProductId));
			//获取商品集合
			List<Long> productIds = orderItems.stream().map(OrderItem::getProductId).collect(Collectors.toList());
			List<ProductDTO> products = productApi.queryAllByIds(productIds).getData();
			//筛选出下单减库存的商品
			products = products.stream().filter(productDTO -> Boolean.FALSE.equals(productDTO.getStockCounting())).collect(Collectors.toList());
			//对下单减库存的商品进行销售库存回退
			if (CollUtil.isNotEmpty(products)) {
				for (ProductDTO product : products) {
					List<OrderItem> orderItemList = orderItemMap.get(product.getId());
					orderItemList.forEach(orderItem -> stockApi.makeUpSalesStocks(orderItem.getSkuId(), orderItem.getBasketCount()));
				}
			}
		}
		OrderItem orderItem = orderItems.get(0);

	}

	/**
	 * 取消订单返回优惠券
	 *
	 * @param orderNumber
	 */
	@Override
	public void returnCoupon(String orderNumber) {
		//获取优惠劵列表
		R<List<CouponUserDTO>> r = couponUserApi.getByOrderNumber(orderNumber);
		if (!r.getSuccess()) {
			throw new BusinessException(r.getMsg());
		}
		List<CouponUserDTO> couponUsers = r.getData();
		//如果需要退优惠券
		if (ObjectUtil.isNotNull(couponUsers)) {
			couponUsers.forEach(couponUser -> {
				if (couponUser.getNonPaymentRefundableFlag()) {
					// 2021010716451961923869|2021010716454193246465
					// 如果包含“|”，就代表是平台券，并且是多单使用的平台券，不能直接返还，应该删除对应订单号并保存
					if (couponUser.getOrderNumber().contains(StringConstant.VERTICAL_BAR)) {
						List<String> orderNumbers = new ArrayList<>(Arrays.asList(couponUser.getOrderNumber().split(StringConstant.VERTICAL_BAR)));
						orderNumbers.remove(orderNumber);
						couponUser.setOrderNumber(CollUtil.join(orderNumbers, StringConstant.VERTICAL_BAR));
					} else {
						couponUser.setOrderNumber(null);
						couponUser.setStatus(CouponUserStatusEnum.UNUSED.getValue());
					}
				}
			});
			couponUserApi.update(couponUsers);
			couponApi.subUseCount(couponUsers.stream().map(CouponUserDTO::getCouponId).collect(Collectors.toList()));
		}
	}

	@Override
	public R updateLogisticsNumber(String orderNumber, String company, String logisticsNumber, Long shopId) {
		return null;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	@GlobalTransactional
	public R updateLogistics(String orderNumber, Long logisticsCompanyId, String logisticsNumber, Long shopId) {
		Order order = orderDao.getByOrderNumberByShopId(orderNumber, shopId);

		if (ObjectUtil.isEmpty(order)) {
			return R.fail("订单已不存在，请刷新页面或联系管理员");
		} else if (!OrderStatusEnum.WAIT_DELIVERY.getValue().equals(order.getStatus())) {
			return R.fail("该订单不能发货,不是已经支付状态");
		}
		List<OrderItem> subItem = orderItemDao.getByOrderNumber(orderNumber);
		if (CollUtil.isEmpty(subItem)) {
			return R.fail("订单数据有问题");
		}
		Date date = new Date();
		order.setStatus(OrderStatusEnum.CONSIGNMENT.getValue());
		order.setDeliveryTime(date);

		// 防止同时请求时，发货多次
		int shipOrderCount = orderDao.shipOrder(order.getId());
		if (shipOrderCount <= 0) {
			return R.fail("请勿重复发货！");
		}

		LogisticsCompanyDTO logisticsCompanyDTO = logisticsCompanyService.getById(logisticsCompanyId);
		if (null == logisticsCompanyDTO) {
			return R.fail("快递公司不存在！");
		}
		//订单物流号重复添加
		OrderLogistics logistics = orderLogisticsDao.getByOrderId(order.getId());
		if (ObjectUtil.isNotEmpty(logistics)) {
			return R.fail("商品已发货，请勿重复提交");
		}

		for (int i = 0; i < subItem.size(); i++) {
			// 减少实际库存 TODO 增加全局事务锁，防止脏数据死锁
			boolean conf = stockApi.addActualHold(subItem.get(i).getProductId(), subItem.get(i).getSkuId(), subItem.get(i).getBasketCount()).getData();
			if (!conf) {
				// 在支付后减库存，有可能导致超卖现象
				throw new BusinessException("库存不足，发货失败");
			}
		}

		// 保存订单物流信息
		OrderLogistics orderLogistics = new OrderLogistics();
		orderLogistics.setLogisticsCompanyId(logisticsCompanyId);
		orderLogistics.setOrderId(order.getId());
		orderLogistics.setOrderNumber(order.getOrderNumber());
		orderLogistics.setShipmentNumber(logisticsNumber);
		orderLogistics.setCompanyCode(logisticsCompanyDTO.getCompanyCode());
		orderLogistics.setLogisticsCompany(logisticsCompanyDTO.getName());
		orderLogistics.setCreateTime(DateUtil.date());
		Long saveResult = orderLogisticsDao.save(orderLogistics);
		order.setOrderLogisticsId(saveResult);
		// 更新订单

		//后台设置用户确认收货时间为0天时，用户订单直接确认收货
		if (order.getReceivingDay() == 0) {
			order.setStatus(OrderStatusEnum.SUCCESS.getValue());
		}
		int result = orderDao.update(order);
		if (result <= 0) {
			throw new BusinessException("订单发货失败~");
		}

		// 保存订单历史
		OrderHistory subHistory = new OrderHistory();
		subHistory.setCreateTime(date);
		subHistory.setOrderId(order.getId());
		subHistory.setStatus(OrderHistoryEnum.DELIVER_GOODS.value());
		subHistory.setReason(order.getUserId() + "于" + DateUtil.formatDateTime(date) + "确认发货");
		orderHistoryDao.save(subHistory);
		logisticsProducerService.poll(logisticsCompanyDTO.getCompanyCode(), logisticsNumber);

		/*添加消息到自动确认收货延迟队列*/
		// TODO 因为不知道商家输入的物流单号是否正确，所以需要在发货时就发送自动确认收货队列
		orderProducerService.autoConfirmDelivery(order.getId(), order.getReceivingDay());

		//把要输出的订单时间转换为string
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String target = sdf.format(order.getDeliveryTime());

		// 发送发货通知站内信给用户
		List<MsgSendParamDTO> msgSendParamDtoList = new ArrayList<>();

		MsgSendParamDTO refundSnDTO = new MsgSendParamDTO(MsgSendParamEnum.ORDER_NUMBER, order.getOrderNumber(), "black");
		msgSendParamDtoList.add(refundSnDTO);

		//替换参数内容
		MsgSendParamDTO first = new MsgSendParamDTO(MsgSendParamEnum.FIRST, "商家已经发货，请留意物流信息", "black");
		MsgSendParamDTO keyword1 = new MsgSendParamDTO(MsgSendParamEnum.KEYWORD1, orderNumber, "black");
		MsgSendParamDTO keyword2 = new MsgSendParamDTO(MsgSendParamEnum.KEYWORD2, target, "black");
		MsgSendParamDTO keyword3 = new MsgSendParamDTO(MsgSendParamEnum.KEYWORD3, logisticsCompanyDTO.getName(), "black");
		MsgSendParamDTO keyword4 = new MsgSendParamDTO(MsgSendParamEnum.KEYWORD4, orderLogistics.getShipmentNumber(), "black");
		MsgSendParamDTO keyword5 = new MsgSendParamDTO(MsgSendParamEnum.KEYWORD5, order.getProductName(), "black");
		//自定义
		MsgSendParamDTO remark = new MsgSendParamDTO(MsgSendParamEnum.REMARK, "感谢您的购买，欢迎下次光临", "black");
		msgSendParamDtoList.add(first);
		msgSendParamDtoList.add(keyword1);
		msgSendParamDtoList.add(keyword2);
		msgSendParamDtoList.add(keyword3);
		msgSendParamDtoList.add(keyword4);
		msgSendParamDtoList.add(keyword5);
		msgSendParamDtoList.add(remark);

		// 微信公众号发送通知提醒用户支付订单  模板参数替换内容
		List<MsgSendParamDTO> urlParamList = new ArrayList<>();
		MsgSendParamDTO url = new MsgSendParamDTO(MsgSendParamEnum.ORDER_ID, orderNumber, "#173177");
		urlParamList.add(url);


		messagePushClient.push(new MessagePushDTO()
				.setMsgReceiverTypeEnum(MsgReceiverTypeEnum.ORDINARY_USER)
				.setReceiveIdArr(new Long[]{order.getUserId()})
				.setMsgSendType(MsgSendTypeEnum.ORDER_SHIP)
				.setSysParamNameEnum(SysParamNameEnum.ORDER_SHIP_TO_USER)
				.setMsgSendParamDTOList(msgSendParamDtoList)
				//跳转路径参数 http://xxxx?xx=xx&xx=xx
				.setUrlParamList(urlParamList)
				.setDetailId(order.getId())
		);

		return R.ok();
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	@GlobalTransactional(rollbackFor = Exception.class)
	public R<Void> confirmDeliver(Long orderId, Long userId) {
		Order order = orderDao.getByOrderIdAndUserId(orderId, userId);
		if (ObjectUtil.isEmpty(order)) {
			throw new BusinessException("订单已不存在，请刷新页面或联系管理员");
		}
		int result = orderDao.confirmDeliverOrder(order.getId());
		if (result > 0) {
			Date date = new Date();
			OrderHistory subHistory = new OrderHistory();
			subHistory.setCreateTime(date);
			subHistory.setOrderId(order.getId());
			subHistory.setStatus(OrderHistoryEnum.CONFIRM_DELIVER.value());
			subHistory.setReason(order.getUserId() + "于" + DateUtil.formatDateTime(date) + "确认收货");
			orderHistoryDao.save(subHistory);

			//mq更新用户购买力数据
			orderProducerService.sendDealOrderMessage(order);
			//获取订单项
			List<OrderItemDTO> orderItems = orderItemConverter.to(orderItemDao.getByOrderId(order.getId()));
			//订单项最晚的售后截止时间
			Date finalReturnDeadline = date;
			for (OrderItemDTO orderItem : orderItems) {
				Date returnDeadline = DateUtil.offsetDay(date, orderItem.getReturnValidPeriod());

				orderItem.setReturnDeadline(returnDeadline);
				finalReturnDeadline = returnDeadline.after(date) ? returnDeadline : date;
			}
			//更新订单项售后截止时间
			orderItemDao.update(orderItemConverter.from(orderItems));
			//更新订单售后截止时间
			orderDao.updateFinalReturnDeadline(orderId, finalReturnDeadline);


		}
		return R.ok();
	}


	@Override
	public R<DistributorsOrderRecordDTO> getDistributorsOrderRecordDTO(DistributorsOrderRecordDTO distributorsOrderRecordDTO) {
		return R.ok(orderDao.getDistributorsOrderRecordDTO(distributorsOrderRecordDTO));
	}

	@Override
	public R<DistributionOrderRecordDTO> getDistributionOrderById(Long userId) {
		return R.ok(orderDao.getDistributionOrderById(userId));
	}


	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean pollCallBack(SubscribePushParamResp subscribePushParamResp) {
		log.info("回调的快递单号为：{} ,状态为{} ", subscribePushParamResp.getLastResult().getNu(), subscribePushParamResp.getLastResult().getState());
		//state	integer	0	快递单当前状态，包括0在途，1揽收，2疑难，3签收，4退签，5派件，6退回，7转投 等7个状态
		if (!subscribePushParamResp.getLastResult().getState().equals(QueryTrackStatusEnum.SIGN_FOR.getValue())) {
			//非签收状态不需要更新。
			return Boolean.TRUE;
		}
		Order order = orderDao.getByLogisticsNumber(subscribePushParamResp.getLastResult().getNu());
		if (ObjectUtil.isEmpty(order)) {
			throw new BusinessException("订单已不存在，请刷新页面或联系管理员");
		}
		int result = orderDao.deliverOrder(order.getId());
		if (result > 0) {
			Date date = new Date();
			OrderHistory subHistory = new OrderHistory();
			subHistory.setCreateTime(date);
			subHistory.setOrderId(order.getId());
			subHistory.setStatus(OrderHistoryEnum.TAKE_DELIVER.value());
			StringBuilder msg = new StringBuilder();
			msg.append(order.getUserId()).append("于").append(DateUtil.formatDateTime(date)).append("投递完成");
			subHistory.setReason(msg.toString());
			orderHistoryDao.save(subHistory);

			// 冗余订单物流追踪信息
			R<OrderLogisticsDTO> logisticsResult = orderLogisticsService.queryLogisticsInformation(order.getOrderNumber());
			if (logisticsResult.success()) {
				orderLogisticsService.update(logisticsResult.getData());
			}
			log.info("更新物流对应的订单成功!");
			return Boolean.TRUE;
		}
		log.error("更新物流对应的订单失败!");
		return Boolean.FALSE;
	}

	@Transactional(rollbackFor = Exception.class)
	@GlobalTransactional(rollbackFor = Exception.class)
	@RabbitListener(bindings = @QueueBinding(value = @Queue(value = AmqpConst.DELAY_CONFIRMATION_RECEIVING_QUEUE, durable = "true"),
			exchange = @Exchange(value = AmqpConst.DELAY_EXCHANGE, type = ExchangeTypes.DIRECT,
					arguments = @Argument(name = "x-delayed-type", value = "direct"), delayed = Exchange.TRUE),
			key = AmqpConst.DELAY_CONFIRMATION_RECEIVING_ROUTING_KEY))
	public void receiveDelay(String msg, Message message, Channel channel) throws IOException {
		log.info("收到自动确认收货消息, 接收参数: {}", msg);
		String[] split = msg.split("orderId:");

		try {
			if (ArrayUtil.isNotEmpty(split) && split.length > 1) {
				log.info("收到自动确认收货消息:订单号:" + split[1] + ",当前时间：" + DateUtil.date() + "延迟时间：" + message.getMessageProperties().getReceivedDelay() + "秒");
				Order order = orderDao.getById(Long.parseLong(split[1]));
				if (ObjectUtil.isEmpty(order)) {
					log.info("订单已不存在，请刷新页面或联系管理员");
					return;
				}
				int result = orderDao.confirmDeliverOrder(order.getId());
				if (result > 0) {
					Date date = new Date();
					OrderHistory subHistory = new OrderHistory();
					subHistory.setCreateTime(date);
					subHistory.setOrderId(order.getId());
					subHistory.setStatus(OrderHistoryEnum.TAKE_DELIVER_TIME.value());
					StringBuilder stringBuilder = new StringBuilder();
					stringBuilder.append(order.getUserId()).append("系统于").append(DateUtil.formatDateTime(date)).append("自动确认收货");
					subHistory.setReason(stringBuilder.toString());
					orderHistoryDao.save(subHistory);

					//mq更新用户购买力数据
					orderProducerService.sendDealOrderMessage(order);
					//获取订单项
					List<OrderItemDTO> orderItems = orderItemConverter.to(orderItemDao.getByOrderId(order.getId()));
					//订单项最晚的售后截止时间
					Date finalReturnDeadline = date;
					for (OrderItemDTO orderItem : orderItems) {
						Date returnDeadline = DateUtil.offsetDay(date, orderItem.getReturnValidPeriod());

						orderItem.setReturnDeadline(returnDeadline);
						finalReturnDeadline = returnDeadline.after(date) ? returnDeadline : date;
					}
					//更新订单项售后截止时间
					orderItemDao.update(orderItemConverter.from(orderItems));
					//更新订单售后截止时间
					orderDao.updateFinalReturnDeadline(order.getId(), finalReturnDeadline);

					log.info("订单自动确认收货成功!");
				} else {
					log.info("订单自动确认收货失败，数据更新失败！");
				}
			} else {
				log.info("订单自动确认收货失败，队列接收数据不正确!");
			}
		} catch (Exception e) {
			log.error("自动确认收货队列异常！", e);
		} finally {
			channel.basicAck(message.getMessageProperties().getDeliveryTag(), Boolean.TRUE);
		}
	}

	@Override
	public List<OrderDTO> getListByStatusAndBillFlag(Integer status, Integer billFlag, Date endDate) {
		List<Order> list = orderDao.getListByStatusAndBillFlag(status, billFlag, endDate);
		return orderConverter.to(list);
	}

	@Override
	public void updateBillStatusAndSn(List<Long> ids, String billSn) {
		orderDao.updateBillStatusAndSn(ids, billSn);
	}

	@Override
	public PageSupport<OrderDTO> queryBillOrderList(OrderSearchQuery orderSearchQuery) {
		PageSupport<Order> pageSupport = orderDao.queryOrderList(orderSearchQuery);
		PageSupport<OrderDTO> page = orderConverter.page(pageSupport);
		List<OrderDTO> orderDtoList = page.getResultList();
		Long billId = orderSearchQuery.getShopOrderBillId();
		ShopOrderBill orderBill = shopOrderBillDao.getById(billId);
		if (null == orderBill) {
			return page;
		}
		orderDtoList.forEach(orderDTO -> {
			BigDecimal mul = NumberUtil.mul(orderDTO.getActualTotalPrice(), orderBill.getCommisRate());
			orderDTO.setCommission(mul);
		});
		return page;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public R addAnotherOrder(String orderNumber, Long userId, String source) {
		Order order = orderDao.getByOrderNumber(orderNumber);
		if (ObjectUtil.isEmpty(order)) {
			throw new BusinessException("订单已不存在，请刷新页面或联系管理员");
		}
		List<Cart> cartList = new ArrayList<>();
		List<OrderItem> orderItems = orderItemDao.getByOrderNumber(orderNumber);
		if (CollUtil.isEmpty(orderItems)) {
			throw new BusinessException("订单已不存在，请刷新页面或联系管理员");
		}
		//获取skuId集合
		List<Long> skuIds = orderItems.stream().map(OrderItem::getSkuId).collect(Collectors.toList());
		//获取sku集合
		List<SkuDTO> skuList = skuApi.queryBySkuIds(skuIds).getData();
		if (CollUtil.isEmpty(skuList)) {
			throw new BusinessException("订单下的sku已不存在，无法再来一单");
		}
		Map<Long, List<SkuDTO>> skuMap = skuList.stream().collect(Collectors.groupingBy(SkuDTO::getId));

		// 获取已有的购物车列表
		List<CartItemDTO> cartItems = loginCartHandler.queryCartItems(userId);
		Map<Long, List<CartItemDTO>> cartItemMap = cartItems.stream().collect(Collectors.groupingBy(CartItemDTO::getSkuId));

		// 购物车重复项，用于删除
		List<Long> deleteCartIds = new ArrayList<>();

		// 购物车重复项更新
		List<CartItemDTO> updateCartList = new ArrayList<>();

		// 加入购物车记录
		List<CartVitLogDTO> cartVitLogList = new ArrayList<>();

		for (OrderItem orderItem : orderItems) {
			//获取sku信息
			List<SkuDTO> skus = skuMap.get(orderItem.getSkuId());
			if (CollUtil.isEmpty(skus)) {
				continue;
			}
			//因为id是唯一的，所以根据id分组得到的列表只会有一个sku
			SkuDTO sku = skus.get(0);
			if (sku.getStocks() <= 0) {
				return R.fail("sku库存不足，无法添加到购物车");

			}

			if (order.getOrderType().equals(PRE_SALE.getValue())) {
				orderItem.setBasketCount(1);
			}

			// 如果购物车已经存在该商品
			if (cartItemMap.containsKey(orderItem.getSkuId())) {
				List<CartItemDTO> dtoList = cartItemMap.get(orderItem.getSkuId());
				// 组装购物车Dto
				CartItemDTO cart = dtoList.get(0);
				if (dtoList.size() > 1) {
					// 如果大于1，则表明之前有重复购物车项，这里进行合并
					for (int i = 1; i < dtoList.size(); i++) {
						CartItemDTO cartItemDTO = dtoList.get(i);
						// 数量累加
						cart.setTotalCount(cart.getTotalCount() + cartItemDTO.getTotalCount());
						deleteCartIds.add(cartItemDTO.getId());
					}
				}
				cart.setPrice(sku.getPrice());
				cart.setSelectFlag(Boolean.TRUE);

				int count = orderItem.getBasketCount() + cart.getTotalCount();
				if (count > sku.getStocks()) {
					cart.setTotalCount(sku.getStocks());
				} else {
					cart.setTotalCount(count);
				}
				updateCartList.add(cart);
			} else {
				// 否则新增至购物车
				// 组装购物车Dto
				Cart cart = new Cart();
				cart.setShopId(order.getShopId());
				cart.setUserId(userId);
				cart.setProductId(orderItem.getProductId());
				cart.setSkuId(orderItem.getSkuId());
				cart.setPrice(sku.getPrice());
				if (orderItem.getBasketCount() > sku.getStocks()) {
					cart.setTotalCount(sku.getStocks());
				} else {
					cart.setTotalCount(orderItem.getBasketCount());
				}
				cart.setSelectFlag(Boolean.TRUE);
				cart.setCreateTime(DateUtil.date());
				cartList.add(cart);
			}

			// 记录加入购物车信息
			CartVitLogDTO logDTO = new CartVitLogDTO();
			logDTO.setShopId(order.getShopId());
			logDTO.setProductId(orderItem.getProductId());
			logDTO.setProductName(orderItem.getProductName());
			logDTO.setUserId(SecurityUtils.getUserId());
			logDTO.setSkuId(orderItem.getSkuId());
			logDTO.setCount(orderItem.getBasketCount());
			logDTO.setSource(source);
			cartVitLogList.add(logDTO);
		}
		if (CollUtil.isEmpty(cartList) && CollUtil.isEmpty(updateCartList)) {
			throw new BusinessException("该订单的所有商品都已失效，无法再来一单");
		}
		//批量加入到购物车
		if (CollUtil.isNotEmpty(cartList)) {
			loginCartHandler.save(userId, cartList);
		}

		// 批量更新
		if (CollUtil.isNotEmpty(updateCartList)) {
			loginCartHandler.update(userId, updateCartList);
		}

		// 批量删除重复项
		if (CollUtil.isNotEmpty(deleteCartIds)) {
			loginCartHandler.delete(userId, deleteCartIds);
		}

		vitLogApi.batchSaveProdCartView(cartVitLogList);
		return R.ok();
	}

	@Override
	public List<OrderDTO> getOrderByOrderNumbersAndUserId(List<String> orderNumberList, Long userId, Integer status) {
		return orderConverter.to(orderDao.getOrderByOrderNumbersAndUserId(orderNumberList, userId, status));
	}

	@Override
	public List<OrderDTO> queryByNumber(List<String> numberList) {
		return this.orderConverter.to(this.orderDao.queryByNumber(numberList));
	}

	@Override
	public R<Void> orderPaySuccessSkuStock(List<String> numberList) {
		List<OrderItem> orderItemList = this.orderItemDao.getByOrderNumberList(numberList);
		List<OrderItem> withItem = orderItemList.stream().filter(OrderItem::getStockCounting).collect(Collectors.toList());
		List<BatchUpdateStockDTO> updateStockDtoList = new ArrayList<>();
		for (OrderItem orderItem : withItem) {
			updateStockDtoList.add(new BatchUpdateStockDTO(orderItem.getProductId(), orderItem.getSkuId(), orderItem.getBasketCount(), ORDINARY.getValue(), ShopOperationTypeEnum.PAY_SUCCESS.value()));
		}
		if (CollUtil.isNotEmpty(updateStockDtoList)) {
			//扣减普通商品库存
			R deductionInventory = stockApi.batchDeductionInventory(updateStockDtoList);
			if (!deductionInventory.success()) {
				throw new BusinessException(deductionInventory.getMsg());
			}
		}
		return R.ok();
	}

	@Override
	public Long getSumProductQuantity(Long userId, Long activityId, OrderTypeEnum orderType) {
		return orderDao.getSumProductQuantity(userId, activityId, orderType.getValue(), OrderStatusEnum.CLOSE.getValue());
	}

	@Override
	public Long getSumSkuQuantity(Long userId, Long activityId, Long skuId, OrderTypeEnum orderType) {
		return orderDao.getSumSkuQuantity(userId, activityId, skuId, orderType.getValue(), OrderStatusEnum.CLOSE.getValue());
	}

	@Override
	public Long getSumProductQuantity(Long userId, Long activityId, OrderTypeEnum orderType, Date createTime) {
		return orderDao.getSumProductQuantity(userId, activityId, orderType.getValue(), OrderStatusEnum.CLOSE.getValue(), createTime);
	}

	@Override
	public Long getSumSkuQuantity(Long userId, Long activityId, Long skuId, OrderTypeEnum orderType, Date createTime) {
		return orderDao.getSumSkuQuantity(userId, activityId, skuId, orderType.getValue(), OrderStatusEnum.CLOSE.getValue(), createTime);
	}

	@Override
	public List<OrderDTO> getListOfActivity(Long activityId, OrderTypeEnum orderTypeEnum) {
		return orderConverter.to(orderDao.getListOfActivity(activityId, orderTypeEnum.getValue()));
	}

	@Override
	public List<OrderDTO> getPayedAndNoRefundList(Long activityId, OrderTypeEnum orderTypeEnum) {
		return orderConverter.to(orderDao.getPayedAndNoRefundList(activityId, orderTypeEnum.getValue()));
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean deleteOrder(Long userId, Long orderId) {
		Order order = orderDao.getById(orderId);

		if (ObjectUtil.isEmpty(order)) {
			throw new BusinessException("操作失败，该订单已不存在");
		}
		if (!order.getUserId().equals(userId)) {
			throw new BusinessException("请不要非法操作");
		}
		if (DateUtil.compare(order.getFinalReturnDeadline(), new Date()) > 0) {
			throw new BusinessException("操作失败，该订单还在售后期");
		}
		order.setDeleteStatus(OrderDeleteStatusEnum.DELETED.value());
		return orderDao.update(order) > 0;
	}

	@Override
	public R<List<UserOrderCountDTO>> queryUserOrderCenter(Long userId) {
		return R.ok(this.orderDao.queryUserOrderCenter(userId));
	}

	@Override
	public OrderDTO getOrderByOrderItemId(Long orderItemId) {
		return orderConverter.to(orderDao.getOrderByOrderItemId(orderItemId));
	}

	/**
	 * 修改拼团下所有订单状态
	 *
	 * @param orderIds
	 * @param status
	 * @param originalStatus
	 */
	@Override
	public void updateStatusByOrderIds(List<Long> orderIds, Integer status, Integer originalStatus) {
		orderDao.updateStatusByOrderIds(orderIds, status, originalStatus);
	}


	@Override
	public List<OrderItemDTO> getAfterSalesList(Long orderId) {
		Order order = orderDao.getById(orderId);
		List<OrderItemDTO> orderItemDTOList = orderItemConverter.to(orderItemDao.getByOrderId(orderId));
		if (ObjectUtil.isNull(order) || CollUtil.isEmpty(orderItemDTOList)) {
			throw new BusinessException("获取失败，该订单已不存在");
		}
		//检查订单状态是否满足退款条件,只有处于发货中和已完成之间的订单才可退款
		if (OrderStatusEnum.CONSIGNMENT.getValue() > order.getStatus() || OrderStatusEnum.SUCCESS.getValue() < order.getStatus()) {
			throw new BusinessException("对不起,您的订单状态不满足退款条件!");
		}
		orderItemDTOList = orderItemDTOList.stream().filter(orderItemDTO -> OrderRefundReturnStatusEnum.ITEM_NO_REFUND.value().equals(orderItemDTO.getRefundStatus())).collect(Collectors.toList());
		if (CollUtil.isEmpty(orderItemDTOList)) {
			throw new BusinessException("该订单下的商品都有申请售后");
		}
		for (OrderItemDTO orderItemDTO : orderItemDTOList) {
			if (OrderStatusEnum.SUCCESS.getValue().equals(order.getStatus())) {
				//检查订单是否在允许退款的时间内
				if (!this.isAllowOrderReturn(order.getCompleteTime(), orderItemDTO.getReturnValidPeriod())) {
					orderItemDTOList.remove(orderItemDTO);
				}
			}
		}
		if (CollUtil.isEmpty(orderItemDTOList)) {
			throw new BusinessException("对不起，您的订单已过申请售后的有效时间");
		}
		return orderItemDTOList;
	}

	@Override
	public boolean isAllowOrderReturn(Date completeTime, Integer days) {
		Date finalTime = DateUtil.offsetDay(completeTime, days);
		return !DateUtil.date().isAfter(finalTime);
	}

	@Override
	public R<Void> orderExport(OrderSearchQuery query) {
		BaseUserDetail baseUser = SecurityUtils.getBaseUser();
		//1、调用Basic 申请导出文件
		ExportExcelTaskDTO exportExcelTaskDTO = new ExportExcelTaskDTO(baseUser.getUserId(), baseUser.getUsername(), baseUser.getUserType(), ExportExcelBusinessEnum.ORDER_LIST);
		R<String> fileNameResult = fileApi.createExportExcelTask(exportExcelTaskDTO);
		if (!fileNameResult.getSuccess() || StrUtil.isBlank(fileNameResult.getData())) {
			return R.fail(StrUtil.blankToDefault(fileNameResult.getMsg(), "下载失败，服务器异常！"));
		}

		exportExcelExecutor.execute(new ExportExcelRunnable() {
			@Override
			public String getFileName() {
				return fileNameResult.getData();
			}

			@Override
			public void run() {
				List<OrderExportDTO> orderExportDTOList = orderDao.orderExport(query);
				// 描述相关字段含义
				orderExportDTOList.forEach(e -> {
					e.setOrderType(OrderTypeEnum.getDes(e.getOrderType()));
					e.setStatus(OrderStatusEnum.getDes(Integer.valueOf(e.getStatus())));
					e.setPayedFlag(OrderPayEnum.getDes(Integer.valueOf(e.getPayedFlag())));
				});
				easyExcelUtils.exportByExcel(orderExportDTOList, fileNameResult.getData(), OrderExportDTO.class);
				try {
					Thread.sleep(10000L);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});

		return R.ok();
	}

	@Override
	public R<OrderBusinessSumDTO> getOrderSumByShopId(Long shopId) {
		OrderBusinessSumDTO orderSumData = orderDao.getOrderSumByShopId(shopId);
		BigDecimal refundAmount = orderDao.getRefundAmountByShopId(shopId);
		orderSumData.setRefundAmount(refundAmount);
		if (ObjectUtil.isNotNull(orderSumData.getSalesAmount()) && ObjectUtil.isNotNull(refundAmount)) {
			//累计销售金额=用户累计付款金额-退款金额
			orderSumData.setSalesAmount(orderSumData.getSalesAmount().subtract(refundAmount));
		}
		return R.ok(orderSumData);
	}

	@Override
	public R<PaymentSuccessDTO> orderFree(List<String> orderNumberList, VisitSourceEnum sourceEnum) {
		PaymentSuccessDTO dto = new PaymentSuccessDTO();
		dto.setPaymentResult("fail");
		if (CollectionUtils.isEmpty(orderNumberList)) {
			return R.ok(dto);
		}
		if (null == sourceEnum) {
			sourceEnum = VisitSourceEnum.UNKNOWN;
		}
		PayParamsDTO payParamsDTO = new PayParamsDTO();
		payParamsDTO.setPayTypeId(PayTypeEnum.FREE_PAY.name());
		payParamsDTO.setSettlementType(SettlementTypeEnum.ORDINARY_ORDER.name());
		payParamsDTO.setBusinessOrderNumberList(orderNumberList);
		payParamsDTO.setVisitSource(sourceEnum);
		return this.paymentApi.pay(payParamsDTO);
	}

	@Override
	public R<Void> invoicing(Long shopId, String orderNumber) {
		int result = orderDao.invoicing(shopId, orderNumber);
		if (result <= 0) {
			return R.fail();
		}
		return R.ok();
	}

	@Override
	public R<Void> batchInvoicing(Long shopId, List<String> ids) {
		Boolean result = orderDao.batchInvoicing(shopId, ids);
		if (!result) {
			return R.fail();
		}
		return R.ok();
	}


	@Override
	public Long getPlatformOrderCountExceptRefundSuccess(Long userId) {
		if (userId == null || userId == 0L) {
			return 0L;
		}
		return orderDao.getPlatformOrderCountExceptRefundSuccess(userId);
	}

	@Override
	public List<ShopOrderCountDTO> getShopOrderCountExceptRefundSuccess(Long userId, List<Long> shopIds) {
		return orderDao.getShopOrderCountExceptRefundSuccess(userId, shopIds);
	}

	@Override
	public List<OrderDTO> queryAllByIds(List<Long> orderIds) {
		return orderConverter.to(orderDao.queryAllByIds(orderIds));
	}


	@Override
	public R<UserOrderStatisticsDTO> getPayedOrderStatisticsByUserId(Long userId) {
		UserOrderStatisticsDTO statisticsDTO = this.orderDao.getPayedOrderStatisticsByUserId(userId);
		if (ObjectUtil.isNotNull(statisticsDTO)) {
			//统计用户已退款的金额
			BigDecimal refundAmount = orderRefundReturnService.getRefundAmountByUserId(userId);
			statisticsDTO.setRefundAmount(refundAmount);
			statisticsDTO.setCount(Math.max((statisticsDTO.getCount() - statisticsDTO.getRefundCount()), 0));
			statisticsDTO.setTotalAmount(BigDecimal.ZERO.max(statisticsDTO.getTotalAmount().subtract(refundAmount)));
		}
		return R.ok(statisticsDTO);
	}

	@Override
	public R<UserOrderStatisticsDTO> getReceivedOrderStatisticsByUserId(Long userId) {
		UserOrderStatisticsDTO statisticsDTO = this.orderDao.getReceivedOrderStatisticsByUserId(userId);
		if (ObjectUtil.isNotNull(statisticsDTO)) {
			//统计用户已退款的金额
			BigDecimal refundAmount = orderRefundReturnService.getRefundAmountByUserId(userId);
			statisticsDTO.setRefundAmount(refundAmount);
			statisticsDTO.setCount(Math.max((statisticsDTO.getCount() - statisticsDTO.getRefundCount()), 0));
			statisticsDTO.setTotalAmount(BigDecimal.ZERO.max(statisticsDTO.getTotalAmount().subtract(refundAmount)));
		}
		return R.ok(statisticsDTO);
	}

	@Override
	public R<UserOrderStatisticsDTO> getReturnedOrderStatisticsByUserId(Long userId) {
		//获取所有已收货的订单
		List<Order> orders = orderDao.queryByUserIdAndStatus(userId, OrderStatusEnum.SUCCESS.getValue());
		//收集订单编号
		List<String> orderNumbers = orders.stream().map(Order::getOrderNumber).collect(Collectors.toList());
		UserOrderStatisticsDTO statisticsDTO = new UserOrderStatisticsDTO();
		if (CollUtil.isEmpty(orders)) {
			return R.ok(statisticsDTO);
		}
		//获取所有的订单项
		List<OrderItem> orderItems = orderItemDao.queryByOrderNumbers(orderNumbers);
		//通过订单编号分组
		Map<String, List<OrderItem>> orderItemMap = Optional.ofNullable(orderItems).orElse(Collections.emptyList()).stream().collect(Collectors.groupingBy(OrderItem::getOrderItemNumber));
		Long count = 0L;
		Long refundCount = 0L;
		BigDecimal totalAmount = BigDecimal.ZERO;
		Date now = DateUtil.date();
		for (Order order : orders) {
			List<OrderItem> orderItemList = orderItemMap.get(order.getOrderNumber());
			if (CollUtil.isEmpty(orderItems)) {
				//订单编号集合移除掉此订单
				orderNumbers.remove(order.getOrderNumber());
				continue;
			}
			//按售后截止时间倒序排序
			orderItemList = orderItemList.stream().sorted(Comparator.comparing(OrderItem::getReturnDeadline).reversed()).collect(Collectors.toList());
			//如果第一个订单项售后期还未结束，则不用计算此订单
			if (orderItemList.get(0).getReturnDeadline().after(now)) {
				//订单编号集合移除掉此订单
				orderNumbers.remove(order.getOrderNumber());
				continue;
			}
			//如果订单已退款完成，则不计算订单数量
			if (OrderRefundReturnStatusEnum.ORDER_REFUND_FINISH.value().equals(order.getRefundStatus())) {
				refundCount = refundCount + 1;
			} else {
				count = count + 1;
			}
			totalAmount = totalAmount.add(order.getActualTotalPrice());
		}
		statisticsDTO.setCount(count);
		statisticsDTO.setRefundCount(refundCount);
		//通过订单编号计算退款金额
		BigDecimal refundAmount = orderRefundReturnService.getRefundAmountByOrderNumbers(orderNumbers);
		statisticsDTO.setRefundAmount(refundAmount);
		statisticsDTO.setTotalAmount(BigDecimal.ZERO.max(totalAmount.subtract(refundAmount)));
		return R.ok(statisticsDTO);
	}


	@Override
	public OrderInfoCountsBO getShopOrderCount(Long shopId) {
		Integer waitDelivery = orderDao.getShopOrderCount(shopId, OrderStatusEnum.WAIT_DELIVERY.getValue());
		Integer unPaid = orderDao.getShopOrderCount(shopId, OrderStatusEnum.UNPAID.getValue());
		Integer takeDeliver = orderDao.getShopOrderCount(shopId, OrderStatusEnum.CONSIGNMENT.getValue());
		Integer noComm = orderDao.getShopOrderCommCount(shopId, OrderStatusEnum.SUCCESS.getValue(), 0);
		Integer refundCount = orderDao.getShopOrderRefundCount(shopId, 1);

		return OrderInfoCountsBO
				.builder()
				.waitDelivery(waitDelivery)
				.unPaid(unPaid)
				.takeDeliver(takeDeliver)
				.noComm(noComm)
				.refundCount(refundCount)
				.build();
	}

	@Override
	public PaidOrderToDayBO getShopPaidOrder(Long shopId) {

		Date[] date1 = getDate(1);
		PaidOrderCountsBO todayPaidOrder = orderDao.getShopPaidOrderCount(shopId, date1[2], date1[3]);
		PaidOrderCountsBO yesterdayPaidOrder = orderDao.getShopPaidOrderCount(shopId, date1[0], date1[1]);

		PaidOrderCountsBO comparePaidOrder = new PaidOrderCountsBO();
		comparePaidOrder.setPaidAmount(todayPaidOrder.getPaidAmount().subtract(yesterdayPaidOrder.getPaidAmount()));
		comparePaidOrder.setSubCounts(todayPaidOrder.getSubCounts() - yesterdayPaidOrder.getSubCounts());

		return PaidOrderToDayBO
				.builder()
				.todayPaidOrderCount(todayPaidOrder.getSubCounts())
				.todayPaidOrderAmount(todayPaidOrder.getPaidAmount())
				.yesterdayPaidOrderCount(yesterdayPaidOrder.getSubCounts())
				.yesterdayPaidOrderAmount(yesterdayPaidOrder.getPaidAmount())
				.comparePaidOrderCount(comparePaidOrder.getSubCounts())
				.comparePaidOrderAmount(comparePaidOrder.getPaidAmount())
				.build();
	}

	/**
	 * 根据天数获取前num天00：00：00至23：59：59的date数组
	 *
	 * @param num 天数
	 * @return dates[0]-开始时间  dates[1]-结束时间  dates[2]-今天开始时间  dates[3]-现在时间
	 */
	private Date[] getDate(Integer num) {
		Date now = DateUtil.parse(DateUtil.now());
		Date beginOfDay = DateUtil.beginOfDay(now);
		Date startDate = DateUtil.offset(beginOfDay, DateField.DAY_OF_MONTH, -num);
		Date endDate = DateUtil.offset(beginOfDay, DateField.SECOND, -1);

		Date[] dates = new Date[4];
		dates[0] = startDate;
		dates[1] = endDate;
		dates[2] = beginOfDay;
		dates[3] = now;

		return dates;
	}

	@Override
	public PendingMattersShopBO getPending(Long shopId) {

		//被举报商品 有效举报
		Integer accusationProduct = orderDao.getShopAccusationProduct(shopId);
		//未发货订单数量status=5
		Integer waitDelivery = orderDao.getShopOrderCount(shopId, OrderStatusEnum.WAIT_DELIVERY.getValue());
		//未处理售后订单数量
		Integer refundCount = orderDao.getShopOrderRefundCount(shopId, 1);
		//待开发票 需要开发票need_invoice_flag  还没开具发票has_invoice_flag
		Integer toBeInvoiced = orderDao.getShopOrderInvoiceCount(shopId);

		return PendingMattersShopBO
				.builder()
				.reportedProductCount(accusationProduct)
				.pendingOrderCount(waitDelivery)
				.pendingAfterSalesOrderCount(refundCount)
				.toBeInvoicedOrderCount(toBeInvoiced)
				.build();
	}

	@Override
	public List<PaidOrderCountsBO> getShopSales(Long shopId) {

		Date[] date = getDate(30);
		List<PaidOrderCountsBO> sales = orderDao.getShopSales(shopId, date[0], date[1]);
		List<PaidOrderCountsBO> result = handleYAxisData(PaidOrderCountsBO.class, sales, date[0], date[1]);

		return result;
	}

	@Override
	public List<OrderShopMessageNoticeBO> queryAllShopOrderNotice() {
		return orderDao.queryAllShopOrderNotice();
	}

	/**
	 * 处理Y轴数据，没有的日期，补数据
	 * 通用方法，使用方式如下
	 * 1. 需要补全的数据对象（Class）有 getDate 方法
	 * 2. 需要返回的对象（Class）有 Date 入参类型的构造函数，如 public test(Date date) {}，构造函数里有自己的补全实现，比如给所有参数赋默认值
	 *
	 * @param clazz           返回类型
	 * @param startDate       开始时间
	 * @param endDate         结束时间
	 * @param totalAmountList 需要补全时间的数据
	 * @return
	 */
	private <T> List<T> handleYAxisData(Class<T> clazz, List<T> totalAmountList, Date startDate, Date endDate) {
		if (null == startDate || null == endDate) {
			return totalAmountList;
		}
		List<T> result = new ArrayList<>();

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();

		boolean flag;
		String formatDate = "";
		Integer current = 0;
		Method getTime = ReflectUtil.getMethod(clazz, "getDate");
		try {
			//遍历时间添加result数据
			while (startDate.compareTo(endDate) <= 0) {
				flag = true;
				calendar.setTime(startDate);
				formatDate = dateFormat.format(startDate);

				//totalAmountList有数据直接添加进result
				for (int i = current; i < totalAmountList.size(); i++) {

					T dataBO = totalAmountList.get(i);
					String date = dateFormat.format(getTime.invoke(dataBO));

					if (date.equals(formatDate)) {
						result.add(dataBO);
						flag = false;
						current = i + 1;
						break;
					}
					//totalAmountList的日期超过遍历日期，跳出for循环去补全数据
					if (DateUtil.parse(date).compareTo(DateUtil.parse(formatDate)) > 0) {
						break;
					}
				}

				//如果在时间formatDate，totalAmountList没有对应的数据，添加数据为0
				if (flag) {
					T t = ReflectUtil.newInstance(clazz, DateUtil.parse(formatDate));
					result.add(t);
				}

				calendar.add(Calendar.DATE, 1);
				startDate = calendar.getTime();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}


	@Override
	public List<OrderDTO> queryReturnDeadlineIsNull() {
		return orderDao.queryReturnDeadlineIsNull();
	}

	@Override
	public R<OrderStatusNumBO> getOrderStatusNum(Long userId) {
		List<OrderStatusNumDTO> orderStatusNum = orderDao.getOrderStatusNum(userId);
		OrderStatusNumBO orderStatusNumBO = new OrderStatusNumBO();
		OrderStatusNumBO userOrderInvoiceCount = orderDao.getUserOrderInvoiceCount(userId);
		if (ObjectUtil.isNotEmpty(userOrderInvoiceCount)) {
			orderStatusNumBO.setInvoicedOrderCount(userOrderInvoiceCount.getInvoicedOrderCount());
			orderStatusNumBO.setToBeInvoicedOrderCount(userOrderInvoiceCount.getToBeInvoicedOrderCount());
		}
		//订单状态数
		if (CollUtil.isNotEmpty(orderStatusNum)) {
			for (OrderStatusNumDTO orderStatusNumDTO : orderStatusNum) {
				Integer num = orderStatusNumDTO.getNum();
				if (ObjectUtil.isEmpty(orderStatusNumDTO.getStatus())) {
					continue;
				}
				switch (orderStatusNumDTO.getStatus()) {
					case 1 -> orderStatusNumBO.setUnpaidNum(num);
					case 5 -> orderStatusNumBO.setWaitDeliveryNum(num);
					case 10 -> orderStatusNumBO.setConsignmentNum(num);
					case 15 -> orderStatusNumBO.setTakeDeliverNum(num);
					case 20 -> orderStatusNumBO.setSuccess(num);
					case -5 -> orderStatusNumBO.setClose(num);
					default -> {
					}
				}
			}
		}
		//待评价数
		OrderStatusNumDTO unCommCountNum = orderDao.getUnCommCountNum(userId);
		orderStatusNumBO.setUnCommCount(unCommCountNum.getNum());

		//售后订单数 OrderRefundReturnDao
		List<OrderStatusNumDTO> orderRefundOrderStatusNum = orderRefundReturnDao.getOrderStatusNum(userId);
		if (CollUtil.isNotEmpty(orderRefundOrderStatusNum)) {
			for (OrderStatusNumDTO orderStatusNumDTO : orderRefundOrderStatusNum) {
				Integer num = orderStatusNumDTO.getNum();
				if (ObjectUtil.isEmpty(orderStatusNumDTO.getStatus())) {
					continue;
				}
				switch (orderStatusNumDTO.getStatus()) {
					case 1 -> orderStatusNumBO.setRefundSuccessNum(num);
					case -1 -> orderStatusNumBO.setRefundFailedNum(num);
					case 0 -> orderStatusNumBO.setProcessingNum(num);
					default -> {
					}
				}
			}
		}
		return R.ok(orderStatusNumBO);
	}


	@Override
	@Transactional(rollbackFor = Exception.class)
	public R<Void> batchInsertLogistics(Long shopId, String shopName, MultipartFile file) {
		Date now = new Date();
		// 文件校验
		String fileName = file.getOriginalFilename();
		if (StringUtil.isEmpty(fileName)) {
			throw new BusinessException("错误的文件类型！");
		}
		String fileSuffixType = fileName.substring(fileName.lastIndexOf(".") + 1);

		if ("xlsx".equalsIgnoreCase(fileSuffixType) ? Boolean.FALSE : ("xls".equalsIgnoreCase(fileSuffixType) ? Boolean.FALSE :
				("xlsm".equalsIgnoreCase(fileSuffixType) ? Boolean.FALSE : Boolean.TRUE))) {
			throw new BusinessException("不支持" + fileSuffixType + "文件格式！！");
		}

		// 读取excel
		LogisticsInfoExcelListener excelListener = new LogisticsInfoExcelListener();
		try {
			EasyExcel.read(file.getInputStream(), InsertLogisticsDTO.class, excelListener).sheet().doRead();
		} catch (Exception e) {
			throw new BusinessException("错误的文件！！");
		}

		// 获取excel内容
		List<InsertLogisticsDTO> list = excelListener.getList();
		if (CollectionUtils.isEmpty(list)) {
			return R.fail("没有需要处理的订单！");
		}
		log.info("读取的物流信息如下：{}", JSON.toJSONString(list));

		// 初始化结果信息
		List<OrderImportLogisticsDetailDTO> importList = new ArrayList<>(list.size());
		for (InsertLogisticsDTO e : list) {
			if (StrUtil.isBlank(e.getOrderNumber())) {
				log.info("存在订单号为空的数据，批量发货失败！");
				importList.add(new OrderImportLogisticsDetailDTO(e.getOrderNumber(), e.getReceiver(), e.getMobile(), e.getLogisticsCompany(), e.getLogisticsNumber(), "存在订单号为空的数据，批量发货失败！", false, now));
				continue;
			}
			if (StrUtil.isBlank(e.getLogisticsCompany())) {
				log.info("存在物流公司为空的数据，批量发货失败！");
				importList.add(new OrderImportLogisticsDetailDTO(e.getOrderNumber(), e.getReceiver(), e.getMobile(), e.getLogisticsCompany(), e.getLogisticsNumber(), "存在物流公司为空的数据，批量发货失败！", false, now));
				continue;
			}

			if (StrUtil.isBlank(e.getLogisticsNumber())) {
				log.info("存在物流单号为空的数据，批量发货失败！");
				importList.add(new OrderImportLogisticsDetailDTO(e.getOrderNumber(), e.getReceiver(), e.getMobile(), e.getLogisticsCompany(), e.getLogisticsNumber(), "存在物流单号为空的数据，批量发货失败！", false, now));
				continue;
			}
			importList.add(new OrderImportLogisticsDetailDTO(e.getOrderNumber(), e.getReceiver(), e.getMobile(), e.getLogisticsCompany(), e.getLogisticsNumber(), null, true, now));
		}

		//过滤器出重复订单号
		Map<String, List<String>> insertLogisticsMap = list.stream().map(InsertLogisticsDTO::getOrderNumber).filter(Objects::nonNull).collect(Collectors.groupingBy(e -> e));
		for (List<String> orderList : insertLogisticsMap.values()) {
			if (orderList.size() > 1) {
				log.info("存在重复订单号，批量发货失败！");
				throw new BusinessException("存在重复订单号，批量发货失败！");
			}
		}


		// 导入的订单号
		List<String> numberList = list.stream().map(InsertLogisticsDTO::getOrderNumber).collect(Collectors.toList());
		// 该商家存在的订单
		List<Order> orderList = this.orderDao.queryByNumberAndShopId(shopId, numberList);
		// 待处理的订单号
		List<String> pendingNumberList = orderList.stream().map(Order::getOrderNumber).collect(Collectors.toList());
		// 待处理的  <订单号 和 订单id>
		Map<String, Long> orderMap = orderList.stream().collect(Collectors.toMap(Order::getOrderNumber, Order::getId));

		// 获取查询失败的订单
		if (numberList.size() > orderList.size()) {
			// 不存在的订单号
			List<String> notExistList = numberList.stream().filter(e -> !pendingNumberList.contains(e)).collect(Collectors.toList());
			importList.stream().filter(e -> BooleanUtil.isTrue(e.getResult())).filter(e -> notExistList.contains(e.getNumber())).forEach(e -> {
				e.setResult(false);
				e.setFailReason("订单不存在，查询失败！");
			});
		}

		// 不是未发起售后的订单
		List<String> notRefundList = orderList.stream().filter(o -> !OrderRefundReturnStatusEnum.ORDER_NO_REFUND.value().equals(o.getRefundStatus())).map(Order::getOrderNumber).collect(Collectors.toList());
		if (CollUtil.isNotEmpty(notRefundList)) {
			importList.stream().filter(e -> BooleanUtil.isTrue(e.getResult())).filter(e -> notRefundList.contains(e.getNumber())).forEach(e -> {
				e.setResult(false);
				e.setFailReason("订单状态错误，当前订单正在售后或已售后完成！");
			});
		}

		// 非待发货订单
		List<String> notHandleList = orderList.stream().filter(o -> OrderStatusEnum.WAIT_DELIVERY.getValue() > o.getStatus()).map(Order::getOrderNumber).collect(Collectors.toList());
		if (!CollectionUtils.isEmpty(notHandleList)) {
			importList.stream().filter(e -> BooleanUtil.isTrue(e.getResult())).filter(e -> notHandleList.contains(e.getNumber())).forEach(e -> {
				e.setResult(false);
				e.setFailReason("订单状态错误，该订单不处于代发货状态！");
			});
		}

		// 物流公司
		Set<String> logisticsCompanyList = importList.stream().filter(e -> BooleanUtil.isTrue(e.getResult())).map(OrderImportLogisticsDetailDTO::getLogisticsCompany).collect(Collectors.toSet());
		List<LogisticsCompanyDTO> companyList = this.logisticsCompanyService.queryByNameList(new ArrayList<>(logisticsCompanyList));
		// 设置发货物流信息
		importList.forEach(e -> companyList.stream().filter(company -> company.getName().equals(e.getLogisticsCompany())).findFirst().ifPresent(o -> {
			e.setCompanyCode(o.getCompanyCode());
			e.setLogisticsCompanyId(o.getId());
		}));

		importList.stream().filter(e -> BooleanUtil.isTrue(e.getResult())).filter(e -> BooleanUtil.isTrue(e.getResult())).filter(e -> StringUtils.isBlank(e.getCompanyCode())).forEach(e -> {
			e.setResult(false);
			e.setFailReason("物流公司名称错误，查询不到对应物流公司！");
		});

		// 重新发货订单
		List<OrderImportLogisticsDetailDTO> reShipmentList = new ArrayList<>();
		List<String> reHandleList = orderList.stream().filter(o -> OrderStatusEnum.WAIT_DELIVERY.getValue() < o.getStatus()).map(Order::getOrderNumber).collect(Collectors.toList());
		if (!CollectionUtils.isEmpty(reHandleList)) {
			importList.stream().filter(e -> BooleanUtil.isTrue(e.getResult())).filter(e -> reHandleList.contains(e.getNumber())).forEach(e -> {
				e.setOrderId(orderMap.get(e.getNumber()));
				// 如果已经标记为失败，则代表当前订单有其它错误信息，不需要重新发货
				if (!e.getResult()) {
					reHandleList.removeIf(re -> re.equals(e.getNumber()));
					return;
				}

				e.setResult(false);
				e.setFailReason("当前订单已发货，只更新物流单号");
				reShipmentList.add(e);
			});
		}

		// 获取需要发货的商品SKU
		List<OrderItem> allSubItemList = orderItemDao.getByOrderNumberList(numberList);

		// 库存操作对象封装
		List<InventoryOperationsDTO> operationsList = new ArrayList<>(allSubItemList.size());
		importList.forEach(e -> {
			if (!e.getResult()) {
				return;
			}
			String number = e.getNumber();
			List<OrderItem> itemList = allSubItemList.stream().filter(item -> item.getOrderNumber().equals(number)).collect(Collectors.toList());
			itemList.forEach(item -> {
				InventoryOperationsDTO dto = new InventoryOperationsDTO();
				operationsList.add(dto);
				dto.setNumber(item.getOrderNumber());
				dto.setSkuId(item.getSkuId());
				dto.setStocks(item.getBasketCount());
				dto.setProductId(item.getProductId());
				dto.setType(InventoryOperationsTypeEnum.ACTUAL);
			});
			e.setOrderId(orderMap.get(number));
		});

		// 按订单分组
		Map<String, List<InventoryOperationsDTO>> stocksMap = operationsList.stream().collect(Collectors.groupingBy(InventoryOperationsDTO::getNumber));
		// 按订单扣库存，防止一次性全部失败，或全部成功
		for (String number : stocksMap.keySet()) {

			// 添加异常处理，防止扣减库存失败异常抛出，导致其他订单导入物流处理失败！
			try {
				R<Void> stocksResult = this.stockApi.inventoryOperations(stocksMap.get(number));
				if (stocksResult.success()) {
					continue;
				}
			} catch (Exception ignored) {
			}

			importList.stream().filter(e -> e.getNumber().equals(number)).forEach(e -> {
				e.setResult(false);
				e.setFailReason("商品库存扣减失败，Sku实际库存不足！");
			});
		}

		// 获取完成扣库存的订单
		List<OrderImportLogisticsDetailDTO> successImportList = importList.stream().filter(OrderImportLogisticsDetailDTO::getResult).collect(Collectors.toList());

		// 获取成功扣库存的订单
		List<String> successList = successImportList.stream().map(OrderImportLogisticsDetailDTO::getNumber).collect(Collectors.toList());
		List<Order> successOrderList = orderList.stream().filter(o -> successList.contains(o.getOrderNumber())).collect(Collectors.toList());

		List<OrderLogistics> orderLogisticsList = new ArrayList<>(successOrderList.size());

		successImportList.forEach(order -> {
			OrderLogistics orderLogistics = new OrderLogistics();
			orderLogistics.setId(this.orderLogisticsDao.createId());
			orderLogistics.setOrderId(order.getOrderId());
			orderLogistics.setShipmentNumber(order.getLogisticsNumber());
			orderLogistics.setOrderNumber(order.getNumber());
			orderLogistics.setLogisticsCompanyId(order.getLogisticsCompanyId());
			orderLogistics.setLogisticsCompany(order.getLogisticsCompany());
			orderLogistics.setCompanyCode(order.getCompanyCode());
			orderLogisticsList.add(orderLogistics);
		});

		this.orderLogisticsDao.saveWithId(orderLogisticsList);

		List<OrderHistory> historyList = new ArrayList<>(successOrderList.size());
		// 修改订单状态
		successOrderList.forEach(order -> {
			order.setStatus(OrderStatusEnum.CONSIGNMENT.getValue());
			order.setDeliveryTime(now);
			// 订单历史
			OrderHistory orderHistory = new OrderHistory();
			historyList.add(orderHistory);
			orderHistory.setCreateTime(now);
			orderHistory.setOrderId(order.getId());
			orderHistory.setStatus(OrderHistoryEnum.DELIVER_GOODS.value());
			orderHistory.setReason(order.getUserId() + "于" + DateUtil.formatDateTime(now) + "批量确认发货");

			/*添加消息到自动确认收货延迟队列*/
			// TODO 因为不知道商家输入的物流单号是否正确，所以需要在发货时就发送自动确认收货队列
			orderProducerService.autoConfirmDelivery(order.getId(), order.getReceivingDay());
		});

		orderDao.update(successOrderList);

		this.orderHistoryDao.save(historyList);

		successImportList.forEach(e -> logisticsProducerService.poll(e.getCompanyCode(), e.getLogisticsNumber()));

		// 汇总数据保存
		OrderImportLogisticsDTO importLogisticsDTO = new OrderImportLogisticsDTO();
		importLogisticsDTO.setShopId(shopId);
		importLogisticsDTO.setCreateTime(now);
		importLogisticsDTO.setOperator(shopName);
		importLogisticsDTO.setCount(list.size());
		importLogisticsDTO.setSuccess((successImportList.size()));
		importLogisticsDTO.setFail(list.size() - successImportList.size());

		Long id = this.importLogisticsService.save(importLogisticsDTO);
		importList.forEach(e -> e.setImportId(id));
		if (CollUtil.isNotEmpty(importList)) {
			this.importLogisticsDetailService.save(importList);
		}
		return R.ok();
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public R<String> updateLogisticsCompanyOrder(Long id, String orderNumber, Long logisticsCompanyId, String logisticsNumber, Long shopId) {
		Order order = orderDao.getByOrderNumberByShopId(orderNumber, shopId);

		List<OrderItem> subItem = orderItemDao.getByOrderNumber(orderNumber);

		if (ObjectUtil.isEmpty(order)) {
			return R.fail("订单已不存在，请刷新页面或联系管理员");
		}
		if (OrderStatusEnum.SUCCESS.getValue().equals(order.getStatus())) {
			return R.fail("订单已完成无法修改订单");
		}
		LogisticsCompanyDTO logisticsCompanyDTO = logisticsCompanyService.getById(logisticsCompanyId);
		if (ObjectUtil.isEmpty(logisticsCompanyDTO)) {
			return R.fail("快递公司不存在！");
		}
		// 修改订单物流信息
		OrderLogistics orderLogistics = orderLogisticsDao.getById(id);
		if (ObjectUtil.isEmpty(orderLogistics)) {
			return R.fail("订单物流信息不存在！");
		}

		orderLogistics.setId(id);
		//物流公司编号根据快递100查询
		orderLogistics.setCompanyCode(logisticsCompanyDTO.getCompanyCode());
		orderLogistics.setLogisticsCompanyId(logisticsCompanyId);
		orderLogistics.setShipmentNumber(logisticsNumber);
		orderLogistics.setLogisticsCompany(logisticsCompanyDTO.getName());
		orderLogistics.setUpdateTime(DateUtil.date());
		orderLogistics.setLogisticsStatus(null);
		orderLogistics.setTrackingInformation(null);
		orderLogisticsDao.update(orderLogistics);

		// 保存订单历史
		OrderHistory subHistory = new OrderHistory();
		subHistory.setCreateTime(DateUtil.date());
		subHistory.setOrderId(order.getId());
		subHistory.setStatus(OrderHistoryEnum.DELIVER_GOODS.value());
		subHistory.setReason(order.getShopId() + "于" + DateUtil.date() + "修改发货信息");
		orderHistoryDao.save(subHistory);
		logisticsProducerService.poll(logisticsCompanyDTO.getCompanyCode(), logisticsNumber);

		/*添加消息到自动确认收货延迟队列*/
		// TODO 因为不知道商家输入的物流单号是否正确，所以需要在发货时就发送自动确认收货队列
		orderProducerService.autoConfirmDelivery(order.getId(), order.getReceivingDay());

		// 发送发货通知站内信给用户
		List<MsgSendParamDTO> msgSendParamDtoList = new ArrayList<>();
		//替换参数内容
		MsgSendParamDTO orderNumberDTO = new MsgSendParamDTO(MsgSendParamEnum.ORDER_NUMBER, order.getOrderNumber(), "black");
		msgSendParamDtoList.add(orderNumberDTO);

		messagePushClient.push(new MessagePushDTO()
				.setMsgReceiverTypeEnum(MsgReceiverTypeEnum.ORDINARY_USER)
				.setReceiveIdArr(new Long[]{order.getUserId()})
				.setMsgSendType(MsgSendTypeEnum.ORDER_SHIP)
				.setSysParamNameEnum(SysParamNameEnum.ORDER_SHIP_TO_USER)
				.setMsgSendParamDTOList(msgSendParamDtoList)
				.setDetailId(order.getId())
		);
		return R.ok();
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public R<String> updateLogisticsUser(Long refundId, Long id, String orderNumber, Long logisticsCompanyId, String logisticsNumber, Long userId) {
		OrderRefundReturn orderRefundReturn = orderRefundReturnDao.getById(refundId);
		if (ObjectUtil.isEmpty(orderRefundReturn)) {
			throw new BusinessException("对不起，你要操作的记录已不存在");
		}
		LogisticsCompanyDTO companyDTO = logisticsCompanyService.getById(logisticsCompanyId);
		if (ObjectUtil.isEmpty(companyDTO)) {
			throw new BusinessException("物流公司不存在！");
		}
		orderRefundReturn.setLogisticsNumber(logisticsNumber);
		orderRefundReturn.setLogisticsId(logisticsCompanyId);
		orderRefundReturn.setLogisticsCompanyCode(companyDTO.getCompanyCode());
		orderRefundReturn.setLogisticsCompanyName(companyDTO.getName());

		if (orderRefundReturnDao.update(orderRefundReturn) > 0) {
			//保存进订单历史
			saveOrderHistory(orderRefundReturn.getOrderId(), OrderHistoryEnum.BUYERS_RETURNGOOD.value(), orderRefundReturn.getUserId() + "用户于" + DateUtil.date() + "修改物流");
			//发送用户退款发货通知站内信给商家
			List<MsgSendParamDTO> msgSendParamDtoList = new ArrayList<>();
			//替换参数内容
			MsgSendParamDTO refundSnDTO = new MsgSendParamDTO(MsgSendParamEnum.REFUND_SN, orderRefundReturn.getRefundSn(), "black");
			msgSendParamDtoList.add(refundSnDTO);
			messagePushClient.push(new MessagePushDTO()
					.setMsgReceiverTypeEnum(MsgReceiverTypeEnum.SHOP_USER)
					.setReceiveIdArr(new Long[]{orderRefundReturn.getShopId()})
					.setMsgSendType(MsgSendTypeEnum.AOTO_SINCE_MENTION_STOCK_UP)
					.setSysParamNameEnum(SysParamNameEnum.REFUND_SHIP_TO_SHOP)
					.setMsgSendParamDTOList(msgSendParamDtoList)
					.setDetailId(orderRefundReturn.getId())
			);
			// 插入商家超时不收货自动确认收货的定时器
			orderProducerService.autoRefundConfirmDelivery(orderRefundReturn.getId());
		}
		return R.ok();
	}

	@Override
	public R<List<CustomOrderDTO>> getCustomOrder(Long userId, Long shopId) {
		if (userId == null) {
			return R.ok(Collections.emptyList());
		}

		List<OrderDTO> customOrder = orderDao.getCustomOrder(userId, shopId);
		List<String> orderNumberList = customOrder.stream().map(OrderDTO::getOrderNumber).collect(Collectors.toList());
		List<OrderItemDTO> orderItemList = orderItemDao.getByOrderNumbers(orderNumberList);
		Map<Long, List<OrderItemDTO>> orderItemMap = orderItemList.stream().collect(Collectors.groupingBy(OrderItemDTO::getOrderId));

		List<CustomOrderDTO> result = new ArrayList<>();
		CustomOrderDTO customOrderDTO = null;
		CustomOrderItemDTO customOrderItemDTO = null;
		for (OrderDTO orderDTO : customOrder) {
			customOrderDTO = new CustomOrderDTO();
			customOrderDTO.setOrderId(orderDTO.getId().toString());
			customOrderDTO.setOrderNumber(orderDTO.getOrderNumber());
			customOrderDTO.setTotalPrice(orderDTO.getActualTotalPrice());
			customOrderDTO.setCreateTime(orderDTO.getCreateTime());
			customOrderDTO.setStatus(orderDTO.getStatus());
			customOrderDTO.setTotalBasketCount(orderDTO.getProductQuantity());
			if (orderItemMap.containsKey(orderDTO.getId())) {
				List<CustomOrderItemDTO> customOrderItemDtoList = new ArrayList<>();
				List<OrderItemDTO> orderItemDtoList = orderItemMap.get(orderDTO.getId());
				for (OrderItemDTO orderItemDTO : orderItemDtoList) {
					customOrderItemDTO = new CustomOrderItemDTO();
					customOrderItemDTO.setPic(orderItemDTO.getPic());
					customOrderItemDTO.setProductName(orderItemDTO.getProductName());
					customOrderItemDTO.setProductId(orderItemDTO.getProductId());
					customOrderItemDTO.setOriginalPrice(orderItemDTO.getOriginalPrice());
					customOrderItemDTO.setPrice(orderItemDTO.getPrice());
					customOrderItemDtoList.add(customOrderItemDTO);
				}
				customOrderDTO.setItemList(customOrderItemDtoList);
				result.add(customOrderDTO);
			}
		}

		return R.ok(result);
	}


	@Override
	public List<OrderDTO> unReceiptOrder() {
		return orderDao.unReceiptOrder();
	}

	@Override
	public R<Void> userPurchasingDataCleaningJobHandle() {

		// 下单数据
		List<Order> orderList = orderDao.queryAll();
		for (Order order : orderList) {
			DataUserPurchasingDTO dto = new DataUserPurchasingDTO();
			dto.setUserId(order.getUserId());
			dto.setQuantity(order.getProductQuantity());
			dto.setCreateTime(order.getCreateTime());
			BigDecimal actualTotalPrice = order.getActualTotalPrice();
			dto.setTotalAmount(actualTotalPrice);
			dto.setFreightPrice(order.getFreightPrice());
			if (order.getPayedFlag() && !OrderStatusEnum.PRESALE_DEPOSIT.getValue().equals(order.getStatus())) {
				dto.setPayAmount(actualTotalPrice);
			}
			if (OrderStatusEnum.SUCCESS.getValue().equals(order.getStatus())) {
				dto.setDealAmount(actualTotalPrice);
			}
			dto.setShopId(order.getShopId());
			dto.setOrderId(order.getId());
			dto.setSource(Optional.ofNullable(order.getSource()).orElse("H5"));
			amqpSendMsgUtil.convertAndSend(com.legendshop.data.constants.AmqpConst.LEGENDSHOP_DATA_EXCHANGE, com.legendshop.data.constants.AmqpConst.LEGENDSHOP_DATA_ORDER_LOG_ROUTING_KEY, dto);
		}
		return R.ok();
	}


	@Override
	public R<Void> shopMessageSendJobHandle() {

		//获得所有商家待处理订单信息数
		List<OrderShopMessageNoticeBO> orders = this.queryAllShopOrderNotice();
		List<Long> shopDetailIds = orders.stream().map(OrderShopMessageNoticeBO::getShopId).collect(Collectors.toList());
		//获得店铺信息
		R<List<ShopDetailDTO>> list = shopDetailApi.queryByIds(shopDetailIds);
		Map<Long, List<ShopDetailDTO>> listMap = list.getData().stream().collect(Collectors.groupingBy(ShopDetailDTO::getId));
		List<MessagePushDTO> messagePushDTOList = new ArrayList<MessagePushDTO>();
		//装载推送消息集合
		orders.forEach(e -> {
			List<MsgSendParamDTO> msgSendParamDtoList = new ArrayList<>();
			//站内信
			MsgSendParamDTO shopName = new MsgSendParamDTO(MsgSendParamEnum.SHOP_NAME, Optional.ofNullable(listMap.get(e.getShopId()).get(0).getShopName()).orElse(""), "black");
			msgSendParamDtoList.add(shopName);
			MsgSendParamDTO orderCount = new MsgSendParamDTO(MsgSendParamEnum.UNTREATED_ORDERS, e.getWaitDelivery().toString(), "black");
			msgSendParamDtoList.add(orderCount);
			MsgSendParamDTO orderRefundCount = new MsgSendParamDTO(MsgSendParamEnum.UNTREATED_ORDER_REFUNDS, e.getRefundCount().toString(), "black");
			msgSendParamDtoList.add(orderRefundCount);

			Integer count = e.getWaitDelivery() + e.getRefundCount();

			//微信
			MsgSendParamDTO first = new MsgSendParamDTO(MsgSendParamEnum.FIRST, " 商家待处理订单数量提醒 ", "black");    //first.DATA，可自定义
			MsgSendParamDTO KEYWORD1 = new MsgSendParamDTO(MsgSendParamEnum.KEYWORD1, Optional.ofNullable(listMap.get(e.getShopId()).get(0).getShopName()).orElse(""), "black");
			MsgSendParamDTO KEYWORD2 = new MsgSendParamDTO(MsgSendParamEnum.KEYWORD2, count.toString(), "black");
			MsgSendParamDTO remark = new MsgSendParamDTO(MsgSendParamEnum.REMARK, Optional.ofNullable(listMap.get(e.getShopId()).get(0).getShopName()).orElse("")
					+ "有 " + e.getWaitDelivery() + " 个订单未发货，" + e.getRefundCount() + " 个售后订单未处理，快去看看~", "black");
			msgSendParamDtoList.add(first);
			msgSendParamDtoList.add(KEYWORD1);
			msgSendParamDtoList.add(KEYWORD2);
			msgSendParamDtoList.add(remark);

			messagePushDTOList.add(new MessagePushDTO()
					.setMsgReceiverTypeEnum(MsgReceiverTypeEnum.SHOP_USER)
					.setReceiveIdArr(new Long[]{listMap.get(e.getShopId()).get(0).getId()}) //修改根据店铺Id推送信息给商家
					.setMsgSendType(MsgSendTypeEnum.ADMIN_NOTIFY)
					.setSysParamNameEnum(SysParamNameEnum.EVERY_DAY_NOTIFY_TO_SHOP)
					.setMsgSendParamDTOList(msgSendParamDtoList)
			);
		});
		messageApi.pushList(messagePushDTOList);
		log.info("发送 未发货订单通知 未处理售后通知 通知商家 成功");
		return R.ok();
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public R<Void> autoConfirmReceiptJobHandle() {

		//查询 用户未确认收货的订单  orderstatus 为 SUCCESS
		List<OrderDTO> orderDtoList = this.unReceiptOrder();
		//获取订单编号
		List<String> orderNumbers = orderDtoList.stream().map(OrderDTO::getOrderNumber).collect(Collectors.toList());

		//通过订单编号获取orderitem 拿到productid
		List<OrderItemDTO> orderItemDtoList = orderItemService.queryByOrderNumbers(orderNumbers);
		//根据订单id分组
		Map<String, List<OrderItemDTO>> orderItemMap = orderItemDtoList.stream().collect(Collectors.groupingBy(OrderItemDTO::getOrderNumber));
		//收集商品id
		List<Long> productIds = orderItemDtoList.stream().map(OrderItemDTO::getProductId).distinct().collect(Collectors.toList());
		//获取商品
		R<List<ProductDTO>> productDtoR = productApi.queryAllByIds(productIds);
		if (!productDtoR.getSuccess()) {
			throw new BusinessException("获取商品信息失败，请刷新重试");
		}
		List<ProductDTO> productDtoList = productDtoR.getData();
		if (CollUtil.isEmpty(productDtoList)) {
			throw new BusinessException("无法找到商品信息，计算售后截止时间失败");
		}

		//todo 获取订单完成时间 和 退换货时间
		//计算 结算时间
		//当前时间
		Date date = new Date();

		//记录下结算成功的订单编号(惠市宝支付单的主订单号 对应支付单号 唯一)
		List<String> mainOrderNumberList = new ArrayList<>();

		//获取订单完成时间
		orderDtoList.forEach(o -> {
			//发货时间
			Date deliveryTime = o.getDeliveryTime();

			//获取订单对应订单项
			List<OrderItemDTO> orderItemDTOList = orderItemMap.get(o.getOrderNumber());
			if (ObjectUtil.isEmpty(orderItemDTOList)) {
				return;
			}
//			//多个订单的话用退换货时间比较大的那个
			ObjectMapper mapper = new ObjectMapper();
			OrderSettingDTO orderSetting = mapper.convertValue(sysParamsApi.getConfigDtoByParamName(SysParamNameEnum.ORDER_SETTING.name(), OrderSettingDTO.class).getData(), OrderSettingDTO.class);
			Integer receivingDay = orderSetting.getAutoReceiveProductDay();
			//计算是否大于自动发货时间
			DateTime endDateTime = DateUtil.offsetDay(deliveryTime, receivingDay);
			log.info("endDateTime{}", endDateTime);
			int compare = DateUtil.compare(date, endDateTime);
			if (compare > 0) {
				log.info("订单配置", orderSetting);
				o.setCommentValidDay(orderSetting.getOrderCommentValidDay());
				o.setStatus(OrderStatusEnum.SUCCESS.getValue());
				o.setUpdateTime(new Date());
				o.setCompleteTime(new Date());
				mainOrderNumberList.add(o.getPaySettlementSn());
				this.confirmDeliver(o.getId(), o.getUserId());
				this.update(o);
			}

		});
		//更新订单结算状态
		if (ObjectUtil.isNotEmpty(orderDtoList)) {
			this.update(orderDtoList);
		}
		return R.ok();
	}

	/**
	 * 保存进订单历史
	 *
	 * @param orderId
	 * @param status
	 * @param reason
	 */
	private void saveOrderHistory(Long orderId, String status, String reason) {
		OrderHistory orderHistory = new OrderHistory();
		DateTime date = DateUtil.date();
		orderHistory.setCreateTime(date);
		orderHistory.setOrderId(orderId);
		orderHistory.setStatus(status);
		orderHistory.setReason(reason);
		orderHistoryDao.save(orderHistory);
	}


	/**
	 * 待评价订单转订单完成MQ
	 */
	@Transactional(rollbackFor = Exception.class)
	@RabbitListener(bindings = @QueueBinding(value = @Queue(value = AmqpConst.DELAY_TREAT_COMMENT_QUEUE, durable = "true"),
			exchange = @Exchange(value = AmqpConst.DELAY_EXCHANGE,
					arguments = @Argument(name = "x-delayed-type", value = "direct"), delayed = Exchange.TRUE),
			key = AmqpConst.DELAY_TREAT_COMMENT_ROUTING_KEY))
	public void autoTreatComment(Long orderId, Message message, Channel channel) throws IOException {

		log.info("收到待评价订单转订单完成消息:订单ID:" + orderId + ",当前时间：" + DateUtil.date() + "延迟时间：" + message.getMessageProperties().getReceivedDelay() + "秒");
		try {
			Order order = orderDao.getById(orderId);
			if (ObjectUtil.isEmpty(order)) {
				return;
			}
			if (!OrderStatusEnum.TREAT_COMMENT.getValue().equals(order.getStatus())) {
				log.error("订单状态不是待评价!");
				return;
			}
			order.setStatus(OrderStatusEnum.SUCCESS.getValue());
			orderDao.update(order);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("超时待评价订单转订单完成失败，原因,{}", e.toString());
		} finally {
			log.info("超时待评价订单转订单完成成功");
			channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
		}

	}
}
