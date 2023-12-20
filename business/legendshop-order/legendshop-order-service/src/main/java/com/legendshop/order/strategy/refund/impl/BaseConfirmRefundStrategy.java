/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.strategy.refund.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.legendshop.activity.api.CouponApi;
import com.legendshop.activity.api.CouponUserApi;
import com.legendshop.activity.dto.CouponUserDTO;
import com.legendshop.activity.dto.OrderRefundCouponDTO;
import com.legendshop.activity.enums.CouponUserStatusEnum;
import com.legendshop.basic.api.MessageApi;
import com.legendshop.basic.dto.MessagePushDTO;
import com.legendshop.basic.dto.MsgSendParamDTO;
import com.legendshop.basic.enums.MsgReceiverTypeEnum;
import com.legendshop.basic.enums.MsgSendParamEnum;
import com.legendshop.basic.enums.MsgSendTypeEnum;
import com.legendshop.basic.enums.SysParamNameEnum;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.constant.StringConstant;
import com.legendshop.common.core.expetion.BusinessException;
import com.legendshop.order.dao.OrderDao;
import com.legendshop.order.dao.OrderItemDao;
import com.legendshop.order.dao.OrderRefundReturnDao;
import com.legendshop.order.dao.OrderRefundReturnDetailDao;
import com.legendshop.order.dto.ConfirmRefundDTO;
import com.legendshop.order.entity.Order;
import com.legendshop.order.entity.OrderItem;
import com.legendshop.order.entity.OrderRefundReturn;
import com.legendshop.order.entity.OrderRefundReturnDetail;
import com.legendshop.order.enums.*;
import com.legendshop.order.service.OrderItemService;
import com.legendshop.order.strategy.refund.ConfirmRefundStrategy;
import com.legendshop.pay.api.PaySettlementApi;
import com.legendshop.pay.api.PaySettlementItemApi;
import com.legendshop.pay.api.PaySettlementOrderApi;
import com.legendshop.pay.api.RefundApi;
import com.legendshop.pay.dto.*;
import com.legendshop.pay.enums.PaySettlementStateEnum;
import com.legendshop.user.api.CustomerBillApi;
import com.legendshop.user.api.UserDetailApi;
import com.legendshop.user.dto.CustomerBillCreateDTO;
import com.legendshop.user.enums.CustomerBillTypeEnum;
import com.legendshop.user.enums.IdentityTypeEnum;
import com.legendshop.user.enums.ModeTypeEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 平台确认退款的模板策略
 *
 * @author legendshop
 */
@Slf4j
@Service
public abstract class BaseConfirmRefundStrategy implements ConfirmRefundStrategy {

	@Autowired
	private OrderRefundReturnDao orderRefundReturnDao;
	@Autowired
	private UserDetailApi userDetailApi;
	@Autowired
	private RefundApi refundApi;

	@Autowired
	private PaySettlementOrderApi paySettlementOrderApi;

	@Autowired
	private PaySettlementApi paySettlementApi;

	@Autowired
	private PaySettlementItemApi paySettlementItemApi;

	@Autowired
	protected MessageApi messagePushClient;

	@Autowired
	private OrderRefundReturnDetailDao detailDao;

	@Autowired
	private OrderDao orderDao;

	@Autowired
	private OrderItemDao orderItemDao;

	@Autowired
	private CouponUserApi couponUserApi;

	@Autowired
	private CouponApi couponApi;

	@Getter
	@Setter
	private OrderTypeEnum orderTypeEnum;

	@Autowired
	private OrderItemService orderItemService;

	@Autowired
	protected CustomerBillApi customerBillApi;

	@Autowired
	private RedissonClient redissonClient;

	private final static String CONFIRM_REFUND = "redisson:confirmRefund:";


	@Override
	public R confirmRefund(ConfirmRefundDTO confirmRefundDTO) {
		log.info("开始处理平台退款 参数：{}" + JSONUtil.toJsonStr(confirmRefundDTO));

		RLock lock = redissonClient.getLock(CONFIRM_REFUND + confirmRefundDTO.getRefundId());
		try {
			if (!lock.tryLock(0L, 60L, TimeUnit.SECONDS)) {
				return R.fail("请勿重复提交！");
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
			return R.fail("退款失败，抢锁失败!");
		}

		try {
			OrderRefundReturn orderRefundReturn = orderRefundReturnDao.getById(confirmRefundDTO.getRefundId());
			if (ObjectUtil.isEmpty(orderRefundReturn)) {
				log.info("###### 您要操作的订单不存在或已被删除 ####### ");
				throw new BusinessException("您要操作的订单不存在或已被删除");
			}
			if (!OrderRefundReturnStatusEnum.HANDLE_PROCESSS.value().equals(orderRefundReturn.getHandleSuccessStatus())) {
				log.info("###### 退款处理失败,该退款单已被处理过 ####### ");
				throw new BusinessException("退款处理失败,该退款单已被处理过");
			}
			if (!OrderRefundReturnStatusEnum.APPLY_WAIT_ADMIN.value().equals(orderRefundReturn.getApplyStatus())) {
				throw new BusinessException("对不起，该售后订单不处于待审核状态");
			}

			// 保存管理员备注信息
			orderRefundReturn.setAdminMessage(confirmRefundDTO.getAdminMessage());


			//进行退款处理
			RefundDTO refundDTO = new RefundDTO();
			refundDTO.setOrderNumber(orderRefundReturn.getOrderNumber());
			refundDTO.setRefundAmount(orderRefundReturn.getRefundAmount());
			refundDTO.setRefundSn(orderRefundReturn.getRefundSn());

			R<List<PaySettlementOrderDTO>> settlementOrderResult = this.paySettlementOrderApi.querySnByOrderNumber(orderRefundReturn.getOrderNumber());
			List<PaySettlementOrderDTO> settlementOrderList = settlementOrderResult.getData();
			if (CollectionUtils.isEmpty(settlementOrderList)) {
				throw new BusinessException("订单尚未支付，请勿退款！");
			}
			List<String> payNumberList = settlementOrderList.stream().map(PaySettlementOrderDTO::getPaySettlementSn).collect(Collectors.toList());
			// 获取支付单
			R<List<PaySettlementDTO>> settlementResult = this.paySettlementApi.queryPaidBySnList(payNumberList);
			List<PaySettlementDTO> settlementList = settlementResult.getData();
			if (CollectionUtils.isEmpty(settlementList)) {
				throw new BusinessException("退款失败，支付单不存在");
			}

			List<PaySettlementItemDTO> paySettlementItemDTOList = this.paySettlementItemApi.queryBySnList(settlementList.stream().map(PaySettlementDTO::getPaySettlementSn)
					.collect(Collectors.toList()))
					.getData()
					.stream()
					.filter(paySettlementItemDTO -> PaySettlementStateEnum.PAID.getCode().equals(paySettlementItemDTO.getState()))
					.collect(Collectors.toList());

			// 是易宝支付时，需要等易宝退款通知
			boolean isYeepay = false;
			for (PaySettlementItemDTO paySettlementItemDTO : paySettlementItemDTOList) {
				if (!isYeepay) {
					if (PayTypeEnum.YEEPAY_ALI_PAY.name().equals(paySettlementItemDTO.getPayTypeId())
							|| PayTypeEnum.YEEPAY_WX_PAY.name().equals(paySettlementItemDTO.getPayTypeId())) {
						isYeepay = true;
					}
				}
			}
			R<RefundResultDTO> resultDTOR = refundApi.refund(refundDTO);
			//退款失败操作
			if (!resultDTOR.getSuccess()) {
				log.error("退款失败，请重新提交， 错误信息: {}", resultDTOR.getMsg());
				throw new BusinessException("退款失败，请重新提交");
			}

			// 如果不是易宝支付，则走原先正常的流程
			if (!isYeepay) {
				this.refundHandler(orderRefundReturn);
			} else {
				// 如果是易宝支付，则保存管理员备注信息，然后等待回调通知做下一步处理 TODO 这里要是更改状态，则需要增加一个新的状态来标识是否已完成后置处理
				orderRefundReturn.setApplyStatus(OrderRefundReturnStatusEnum.APPLY_FINISH.value());
				orderRefundReturnDao.update(orderRefundReturn);
			}
		} finally {
			try {
				lock.unlock();
			} catch (Exception e1) {
				log.error("平台退款完成，释放锁失败!");
			}
		}
		log.info("###### 处理平台退款成功 ####### ");
		return R.ok();
	}

	@Override
	public R<Void> refundHandler(OrderRefundReturn orderRefundReturn) {
		if (null == orderRefundReturn) {
			return R.fail("退款处理失败, 退款单为空");
		}

		String refundSn = orderRefundReturn.getRefundSn();
		orderRefundReturn = this.orderRefundReturnDao.getReturnByRefundSn(refundSn);
		if (null == orderRefundReturn) {
			log.error("当前回调退款单查询失败，refundSn：{}", refundSn);
			return R.fail("对应退款记录不存在！");
		}

		if (OrderRefundReturnStatusEnum.APPLY_FINISH.value().equals(orderRefundReturn.getApplyStatus())) {
			log.info("当前售后单已完成，无需处理后置处理~");
			return R.ok();
		}

		//退款成功处理
		if (OrderRefundReturnTypeEnum.REFUND.value().equals(orderRefundReturn.getApplyType())) {
			refundSuccessHandle(orderRefundReturn);
		}

		//退款成功处理
		if (OrderRefundReturnTypeEnum.REFUND_CANEL.value().equals(orderRefundReturn.getApplyType())) {
			refundSuccessHandle(orderRefundReturn);
		}
		//退货成功处理
		if (OrderRefundReturnTypeEnum.REFUND_RETURN.value().equals(orderRefundReturn.getApplyType())) {
			refundReturnSuccessHandle(orderRefundReturn);
		}

		// 获取订单信息
		Order order = orderDao.getById(orderRefundReturn.getOrderId());

		// 记录用户账单
		this.customerBillApi.save(CustomerBillCreateDTO.builder()
				.mode(ModeTypeEnum.INCOME.value())
				.type(CustomerBillTypeEnum.REFUND.value())
				.ownerType(IdentityTypeEnum.USER.value())
				.ownerId(order.getUserId())
				.tradeExplain(order.getProductName())
				.amount(orderRefundReturn.getRefundAmount())
				.bizOrderNo(order.getOrderNumber())
				.innerPaymentNo(order.getPaySettlementSn())
				.relatedBizOrderNo(order.getOrderNumber())
				.payTypeId(order.getPayTypeId())
				.payTypeName(order.getPayTypeName())
				.delFlag(false)
				.status(1)
				.createTime(DateUtil.date()).build());


		//发送站内信
		sendMessage(orderRefundReturn);

		//特殊业务处理
		specialBusiness(orderRefundReturn);

		return R.ok();
	}

	@Override
	public R<Void> refundCallback(String refundSn) {
		OrderRefundReturn orderRefundReturn = this.orderRefundReturnDao.getReturnByRefundSn(refundSn);
		if (null == orderRefundReturn) {
			log.error("当前回调退款单查询失败，refundSn：{}", refundSn);
			return R.fail("对应退款记录不存在！");
		}
		if (OrderRefundReturnStatusEnum.APPLY_FINISH.value().equals(orderRefundReturn.getApplyStatus())) {
			return R.ok();
		}

		// 退款成功处理
		if (OrderRefundReturnTypeEnum.REFUND.value().equals(orderRefundReturn.getApplyType())) {
			refundSuccessHandle(orderRefundReturn);
		}

		// 退货成功处理
		if (OrderRefundReturnTypeEnum.REFUND_RETURN.value().equals(orderRefundReturn.getApplyType())) {
			refundReturnSuccessHandle(orderRefundReturn);
		}

		// 特殊业务处理
		specialBusiness(orderRefundReturn);

		// 发送站内信
		sendMessage(orderRefundReturn);
		log.info("###### 处理平台退款成功 ####### ");
		return R.ok();
	}

	public abstract void specialBusiness(OrderRefundReturn orderRefundReturn);

	protected void recordRefundDetail(RefundResultDTO refundResultDTO, Long refundId) {
		//存入退款明细表
		List<OrderRefundReturnDetail> detailList = new ArrayList<>();
		refundResultDTO.getItemList().forEach(refundResultItemDTO -> {
			OrderRefundReturnDetail detail = new OrderRefundReturnDetail();
			detail.setOutRefundNo(refundResultItemDTO.getExternalRefundSn());
			detail.setPayTypeId(refundResultItemDTO.getPayTypeId());
			detail.setRefundAmount(refundResultItemDTO.getAmount());
			detail.setRefundId(refundId);
			//不同订单存入不同的退款类型
			detail.setRefundType(getOrderTypeEnum().getDes() + "退款");
			detail.setRefundFlag(1);
			if (PayTypeEnum.YEEPAY_ALI_PAY.name().equals(refundResultItemDTO.getPayTypeId())
					|| PayTypeEnum.YEEPAY_WX_PAY.name().equals(refundResultItemDTO.getPayTypeId())) {
				detail.setRefundFlag(2);
			}
			detailList.add(detail);
		});
		detailDao.save(detailList);
	}


	/**
	 * 退款成功处理
	 *
	 * @param orderRefundReturn
	 */
	protected void refundSuccessHandle(OrderRefundReturn orderRefundReturn) {
		/*
		 * 分两种情况:
		 * 	1.如果是订单退款: 订单状态更新为已取消, 订单退款状态更新为已完成，回退销售库存
		 * 	2.如果是订单项退款: 订单项退款状态更新为已完成,检查是否最后一个在退款中的订单项,是则订单状态更新为已完成，不用回退库存
		 *
		 */
		log.info("###### 退款成功处理开始 ####### ");
		Long orderId = orderRefundReturn.getOrderId();
		Long orderItemId = orderRefundReturn.getOrderItemId();
		String orderNumber = orderRefundReturn.getOrderNumber();
		Order order = orderDao.getById(orderId);
		//如果是全单退款
		if (orderRefundReturn.getSkuId() == 0) {
			List<OrderItem> orderItems = orderItemDao.getByOrderNumber(orderNumber);

			//释放库存
			for (OrderItem item : orderItems) {
				Integer applyType = orderRefundReturn.getApplyType();
				//只有仅退款对销售库存进行操作，退货退款需商家手动确认是否能够再次进行销售，可以销售手动添加实际库存（会自动添加销售库存）
				if (ObjectUtil.isNotEmpty(orderRefundReturn.getApplyType()) && applyType.equals(OrderRefundTypeEnum.REFUND.getValue())) {
					releaseInventory(item, order.getActivityId());
				}

			}
			//订单退款状态为已完成
			order.setRefundStatus(OrderRefundReturnStatusEnum.ORDER_REFUND_FINISH.value());
			//订单状态为已取消
			order.setStatus(OrderStatusEnum.CLOSE.getValue());
			order.setUpdateTime(DateUtil.date());
			//更新订单
			orderDao.update(order);
			//更新订单下的所有订单项为退款完成
			orderItemDao.updateRefundStateByOrderNumber(orderRefundReturn.getOrderNumber(), OrderRefundReturnStatusEnum.ITEM_REFUND_FINISH.value());
			//如果使用了优惠券
			if (ObjectUtil.isNotNull(order.getCouponAmount()) && order.getCouponAmount().compareTo(BigDecimal.ZERO) > 0) {
				//返回优惠券
				orderRefundCoupon(orderNumber, 2);

			}

		} else {
			// 判断是否最后一个在处理的订单项
			Long count = orderItemDao.queryRefundItemCount(orderNumber, OrderRefundReturnStatusEnum.ITEM_REFUND_PROCESSING.value());
			//判断是否有未发起退款的订单项
			Long noRefundCount = orderItemDao.queryRefundItemCount(orderNumber, OrderRefundReturnStatusEnum.ITEM_NO_REFUND.value());
			if (count == 1 && noRefundCount == 0) {
				//订单退款状态为已完成
				order.setRefundStatus(OrderRefundReturnStatusEnum.ORDER_REFUND_FINISH.value());
				//订单状态为已完成
				order.setStatus(OrderStatusEnum.SUCCESS.getValue());
				order.setCompleteTime(DateUtil.date());
				//更新订单
				orderDao.update(order);
				//如果使用了优惠券
				if (ObjectUtil.isNotNull(order.getCouponAmount()) && order.getCouponAmount().compareTo(BigDecimal.ZERO) > 0) {
					//退还优惠券处理
					orderRefundCoupon(orderNumber, 3);
				}
			}
			//更新订单项为退款完成
			orderItemDao.updateRefundState(orderItemId, OrderRefundReturnStatusEnum.ITEM_REFUND_FINISH.value());
		}

		//更新退款单
		orderRefundReturn.setHandleSuccessStatus(OrderRefundReturnStatusEnum.HANDLE_SUCCESS.value());
		orderRefundReturn.setAdminTime(DateUtil.date());
		orderRefundReturn.setApplyStatus(OrderRefundReturnStatusEnum.APPLY_FINISH.value());
		orderRefundReturnDao.update(orderRefundReturn);

		//更新用户消费统计数据
		updateConsumptionStatistics(order.getUserId(), order.getRefundStatus(), orderRefundReturn.getRefundAmount());
		log.info("###### 退款成功处理结束 ####### ");
	}

	/**
	 * 退货成功处理
	 *
	 * @param orderRefundReturn
	 */
	protected void refundReturnSuccessHandle(OrderRefundReturn orderRefundReturn) {
		/**
		 * 分两种情况:
		 * 	1.如果是商家弃货: 订单项退款状态更新为已完成,检查是否最后一个在退款中的订单项,是则订单状态更新为已完成，不用回退库存
		 * 	2.如果商家不弃货: 订单项退款状态更新为已完成,检查是否最后一个在退款中的订单项,是则订单状态更新为已完成，回退库存
		 */
		log.info("###### 退货成功处理开始 ####### ");
		Long orderId = orderRefundReturn.getOrderId();
		Long orderItemId = orderRefundReturn.getOrderItemId();
		String orderNumber = orderRefundReturn.getOrderNumber();
		Order order = orderDao.getById(orderId);

		// 判断是否最后一个在处理的订单项
		Long count = orderItemDao.queryRefundItemCount(orderNumber, OrderRefundReturnStatusEnum.ITEM_REFUND_PROCESSING.value());
		log.info("判断是否最后一个在处理的订单项:{}", count);
		//判断是否有未发起退款的订单项
		Long noRefundCount = orderItemDao.queryRefundItemCount(orderNumber, OrderRefundReturnStatusEnum.ITEM_NO_REFUND.value());
		log.info("判断是否有未发起退款的订单项:{}", noRefundCount);
		if (count == 1 && noRefundCount == 0) {
			log.info("进入退还优惠劵-----------1------------");
			//订单退款状态为已完成
			order.setRefundStatus(OrderRefundReturnStatusEnum.ORDER_REFUND_FINISH.value());
			order.setStatus(OrderStatusEnum.SUCCESS.getValue());
			order.setCompleteTime(DateUtil.date());
			//更新订单
			orderDao.update(order);
			//如果使用了优惠券
			if (ObjectUtil.isNotNull(order.getCouponAmount()) && order.getCouponAmount().compareTo(BigDecimal.ZERO) > 0) {
				//退还优惠券处理
				log.info("进入退还优惠劵-----------2------------");
				orderRefundCoupon(orderNumber, 3);
			}
		}
		//更新订单项为退款完成
		orderItemDao.updateRefundState(orderItemId, OrderRefundReturnStatusEnum.ITEM_REFUND_FINISH.value());
		//更新退款单
		orderRefundReturn.setHandleSuccessStatus(OrderRefundReturnStatusEnum.HANDLE_SUCCESS.value());
		orderRefundReturn.setAdminTime(DateUtil.date());
		orderRefundReturn.setApplyStatus(OrderRefundReturnStatusEnum.APPLY_FINISH.value());
		orderRefundReturnDao.update(orderRefundReturn);

		//更新用户消费统计数据
		updateConsumptionStatistics(order.getUserId(), order.getRefundStatus(), orderRefundReturn.getRefundAmount());
		log.info("###### 退货成功处理结束 ####### ");
	}

	/**
	 * 返回优惠券处理
	 *
	 * @param orderNumber
	 * @param type        1.提交订单未付款可退还，2.生成订单申请退款可退还 3.生成订单全部商品申请售后可退还
	 */
	protected void orderRefundCoupon(String orderNumber, Integer type) {
		log.info("###### 返回优惠券处理开始 ####### ");
		//获取优惠劵列表
		R<List<CouponUserDTO>> listR = couponUserApi.getByOrderNumber(orderNumber);
		if (!listR.getSuccess()) {
			log.error("获取优惠券失败 {}" + listR.getMsg());
			return;
		}
		List<CouponUserDTO> couponUsers = listR.getData();
		List<Long> couponIds = new ArrayList<>();
		if (ObjectUtil.isNotNull(couponUsers)) {
			List<OrderRefundCouponDTO> refundCouponDTOS = new ArrayList<>();
			couponUsers.forEach(couponUser -> {
				boolean flag = false;
				if (type == 1 && couponUser.getNonPaymentRefundableFlag()) {
					flag = true;
				}
				if (type == 2 && couponUser.getPaymentRefundableFlag()) {
					flag = true;
				}
				if (type == 3 && couponUser.getPaymentAllAfterSalesRefundableFlag()) {
					flag = true;
				}
				OrderRefundCouponDTO orderRefundCouponDTO = new OrderRefundCouponDTO();
				if (flag) {

					// 2021010716451961923869|2021010716454193246465
					// 如果包含“|”，就代表是平台券，并且是多单使用的平台券，不能直接返还，应该删除对应订单号并保存
					if (couponUser.getOrderNumber().contains(StringConstant.VERTICAL_BAR)) {
						List<String> orderNumbers = new ArrayList<>(Arrays.asList(couponUser.getOrderNumber().split(StringConstant.VERTICAL_BAR)));
						orderNumbers.remove(orderNumber);
						orderRefundCouponDTO.setOrderNumber(CollUtil.join(orderNumbers, StringConstant.VERTICAL_BAR));
					} else {
						couponUser.setOrderNumber(null);
						couponUser.setStatus(CouponUserStatusEnum.UNUSED.getValue());
						orderRefundCouponDTO.setOrderNumber(null);
						orderRefundCouponDTO.setStatus(CouponUserStatusEnum.UNUSED.getValue());
					}

					orderRefundCouponDTO.setCouponId(couponUser.getCouponId());
					orderRefundCouponDTO.setCouponOrderNumber(couponUser.getOrderNumber());
					orderRefundCouponDTO.setCouponUserId(couponUser.getId());
					refundCouponDTOS.add(orderRefundCouponDTO);
				}
			});

			refundCouponDTOS.stream().map(OrderRefundCouponDTO::getCouponId).distinct().collect(Collectors.toList());

			couponUserApi.updateOrder(refundCouponDTOS);
			couponUserApi.update(couponUsers);
		}
		log.info("###### 返回优惠券处理结束 ####### ");
	}

	/**
	 * 释放库存
	 *
	 * @param orderItem
	 * @param activityId
	 */
	protected abstract void releaseInventory(OrderItem orderItem, Long activityId);

	/**
	 * 发送站内信
	 */
	protected void sendMessage(OrderRefundReturn orderRefundReturn) {
		//发送平台确认退款站内信给商家
		List<MsgSendParamDTO> msgSendParamDTOS = new ArrayList<>();
		//替换参数内容
		MsgSendParamDTO refundSnDTO = new MsgSendParamDTO(MsgSendParamEnum.REFUND_SN, orderRefundReturn.getRefundSn(), "black");
		msgSendParamDTOS.add(refundSnDTO);
		messagePushClient.push(new MessagePushDTO()
				.setMsgReceiverTypeEnum(MsgReceiverTypeEnum.SHOP_USER)
				.setReceiveIdArr(new Long[]{orderRefundReturn.getShopId()})
				.setMsgSendType(MsgSendTypeEnum.ORDER_REFUND)
				.setSysParamNameEnum(SysParamNameEnum.CONFIRM_REFUND_TO_SHOP)
				.setMsgSendParamDTOList(msgSendParamDTOS)
				.setDetailId(orderRefundReturn.getId())
		);
		/**
		 * 发送平台确认退款站内信给用户
		 * 	first.DATA，可自定义
		 */
		MsgSendParamDTO first = new MsgSendParamDTO(MsgSendParamEnum.FIRST, "平台审核订单完成", "black");
		MsgSendParamDTO KEYWORD1 = new MsgSendParamDTO(MsgSendParamEnum.KEYWORD1, orderRefundReturn.getProductName(), "black");
		MsgSendParamDTO KEYWORD2 = new MsgSendParamDTO(MsgSendParamEnum.KEYWORD2, orderRefundReturn.getRefundSn(), "black");
		//remark商家自定义
		MsgSendParamDTO REMARK = new MsgSendParamDTO(MsgSendParamEnum.REMARK, "您的售后申请已经通过，请注意查看哦", "black");
		msgSendParamDTOS.add(first);
		msgSendParamDTOS.add(KEYWORD1);
		msgSendParamDTOS.add(KEYWORD2);
		msgSendParamDTOS.add(REMARK);

		//未设置公众号跳转

		messagePushClient.push(new MessagePushDTO()
				.setMsgReceiverTypeEnum(MsgReceiverTypeEnum.ORDINARY_USER)
				.setReceiveIdArr(new Long[]{orderRefundReturn.getUserId()})
				.setMsgSendType(MsgSendTypeEnum.ORDER_REFUND)
				.setSysParamNameEnum(SysParamNameEnum.CONFIRM_REFUND_TO_USER)
				.setMsgSendParamDTOList(msgSendParamDTOS)
				.setDetailId(orderRefundReturn.getId())
		);
	}

	/**
	 * 更新用户消费统计数据 TODO 退款退货应该不需要再扣减。如果需要，则分销升级条件中的自购条件需要另外记录
	 *
	 * @param userId
	 * @param refundStatus
	 * @param refundAmount
	 */
	public void updateConsumptionStatistics(Long userId, Integer refundStatus, BigDecimal refundAmount) {
//		int orderCount = 0;
//		//如果订单退款退款为已完成，则用户的成交订单数需要-1
//		if (OrderRefundReturnStatusEnum.ORDER_REFUND_FINISH.value().equals(refundStatus)) {
//			orderCount = -1;
//		}
//		R resultR = userDetailClient.updateConsumptionStatistics(userId, refundAmount, orderCount);
//		if (!resultR.success()) {
//			log.error(resultR.getMsg());
//			throw new BusinessException(resultR.getMsg());
//		}
	}


}
