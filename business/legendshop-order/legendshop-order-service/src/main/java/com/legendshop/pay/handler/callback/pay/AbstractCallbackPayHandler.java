/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.pay.handler.callback.pay;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.expetion.BusinessException;
import com.legendshop.common.data.cache.util.StringRedisUtil;
import com.legendshop.order.api.OrderApi;
import com.legendshop.order.dto.OrderDTO;
import com.legendshop.order.enums.OrderStatusEnum;
import com.legendshop.order.enums.PayTypeEnum;
import com.legendshop.pay.dto.*;
import com.legendshop.pay.enums.PaySettlementStateEnum;
import com.legendshop.pay.enums.WalletBusinessTypeEnum;
import com.legendshop.pay.enums.WalletOperationTypeEnum;
import com.legendshop.pay.service.*;
import io.seata.spring.annotation.GlobalTransactional;
import io.seata.tm.api.GlobalTransaction;
import io.seata.tm.api.GlobalTransactionContext;
import io.seata.tm.api.transaction.TransactionHookAdapter;
import io.seata.tm.api.transaction.TransactionHookManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author legendshop
 */
@Slf4j
public abstract class AbstractCallbackPayHandler implements CallbackPayHandler {

	@Autowired
	protected OrderApi orderApi;

	@Autowired
	protected StringRedisUtil stringRedisUtil;

	@Autowired
	protected UserWalletService userWalletService;

	@Autowired
	protected PaySystemConfigService systemConfigService;

	@Autowired
	protected PaySettlementService paySettlementService;

	@Autowired
	private PaymentBusinessService paymentBusinessService;

	@Autowired
	private PaySettlementItemService paySettlementItemService;

	@Autowired
	protected UserWalletDetailsService userWalletDetailsService;

	@Autowired
	protected PaySettlementOrderService paySettlementOrderService;

	@Autowired
	protected UserWalletBusinessService userWalletBusinessService;

	@Autowired
	private PaySettlementLogService paySettlementLogService;

	protected final ObjectMapper objectMapper = new ObjectMapper();

	public PayConfigDTO config(String payCode) {
		return this.systemConfigService.getConfig(payCode);
	}


	/**
	 * 支付回调，处理结算单，优先结算单的处理
	 * 结算单处理包括 ls_pay_settlement【支付结算单据】，ls_pay_settlement_item【支付结算单据项次表】，在同一事务内完成。
	 * 验签后，结算单代表外部支付结果，记录外部业务编码，
	 * 结算单支付成功后，后续业务处理不成功，可根据结算单进行补尝。
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public R<List<PaySettlementItemDTO>> handler(HttpServletRequest request, HttpServletResponse response) {
		log.info(" [ CallbackPayHandler ]  支付异步回调处理开始 --------------> ");
		// 1. 验证报文参数 相同点 获取所有的请求参数封装成为map集合 并且进行参数验证
		Map<String, String> notifyParamMap = getNotifyParamMap(request, response);
		try {
			log.info(" [ CallbackPayHandler ]  异步回调 response : {}  ", this.objectMapper.writeValueAsString(notifyParamMap));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		// 2. 验签
		if (!verifyNotifyParamMap(notifyParamMap)) {
			log.error(" [ CallbackPayHandler ]  异步回调验签失败！ ");
			return R.fail(fail());
		}
		// 3. 转Bean
		PayNotifyDTO payNotifyDTO = notifyParamsToBean(notifyParamMap);
		Boolean paymentLock = this.stringRedisUtil.paymentLock(StringRedisUtil.PAY_CALLBACK_LOCK_KEY_PREFIX, payNotifyDTO.getTransactionSn());
		if (!paymentLock) {
			return R.fail(fail());
		}
		// 4. 处理结算单
		List<PaySettlementItemDTO> item = settlementHandler(payNotifyDTO);
		if (null == item) {
			return R.ok(null, success());
		}
		return R.ok(item);
	}

	@Override
	@GlobalTransactional(rollbackFor = Exception.class)
	public String callbackBusinessHandle(List<PaySettlementItemDTO> paySettlementItemDTOS) {

		long begin = System.currentTimeMillis();
		// 用户钱包余额抵扣
		this.walletSettle(paySettlementItemDTOS);
		log.info("\n [支付回调 ,callbackPayHandler.handlerWallet ], 耗时-> {} ms !", System.currentTimeMillis() - begin);

		log.info("结算单处理完成，调用订单回调处理！ settlement ：{}", JSONUtil.toJsonStr(paySettlementItemDTOS));

		begin = System.currentTimeMillis();
		this.paymentBusinessService.callbackBusinessHandle(paySettlementItemDTOS.get(0));
		log.info("\n [支付回调 ,paymentBusinessService.callbackBusinessHandle ], 耗时-> {} ms !", System.currentTimeMillis() - begin);
		return success();
	}

	/**
	 * 完结结算单
	 */
	public List<PaySettlementItemDTO> settlementHandler(PayNotifyDTO notifyDTO) {
		if (!notifyDTO.getPaySuccess()) {
			log.error(" [ CallbackPayHandler ]  异步回调  支付状态错误！");
			throw new BusinessException("notify callback pay status error");
		}
		String settlementSn = notifyDTO.getSettlementSn();

		// 用于支付单类型查询
		PaySettlementDTO settlement = this.paySettlementService.getBySn(settlementSn);
		if (settlement == null) {
			log.error(" [ CallbackPayHandler ]  异步回调  支付单不存在，回调数据：{}", JSONUtil.toJsonStr(notifyDTO));
			throw new BusinessException("支付单不存在！");
		}

		if (PaySettlementStateEnum.PAID.getCode().equals(settlement.getState())) {
			log.info(" [ CallbackPayHandler ]  异步回调  支付单已被处理，回调数据：{}", JSONUtil.toJsonStr(notifyDTO));

			//事务完成后，提交成功后，进行解锁
			TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
				@Override
				public void afterCompletion(int status) {
					PaySettlementLogDTO paySettlementLogDTO = new PaySettlementLogDTO();
					paySettlementLogDTO.setPaySettlementSn(settlement.getPaySettlementSn());
					paySettlementLogDTO.setUserId(settlement.getUserId());
					paySettlementLogDTO.setCreateTime(new Date());
					paySettlementLogDTO.setInfo("支付单已被处理,重复回调！");
					paySettlementLogService.saveLog(paySettlementLogDTO);
				}
			});
			return null;
		}

		List<PaySettlementItemDTO> settlementItemList = this.paySettlementItemService.queryBySn(settlementSn);

		Optional<PaySettlementItemDTO> itemOptional = settlementItemList.stream().filter(s -> s.getPayTypeId().equals(notifyDTO.getPayType())).findFirst();

		if (!itemOptional.isPresent()) {
			log.error(" [ CallbackPayHandler ]  没有对应的支付单项！");
			throw new BusinessException("notify callback pay status error");
		}

		PaySettlementItemDTO item = itemOptional.get();

		if (item.getAmount().compareTo(notifyDTO.getCashAmount()) != 0) {
			String msg = String.format(" [ CallbackPayHandler - pay_notice ] 支付失败,返回的订单金额与商户侧的订单金额不一致  结算单金额=%s, 第三方支付金额=%s", item.getAmount(), notifyDTO.getCashAmount());
			log.error(msg);

			//事务完成后，提交成功后，进行解锁
			TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
				@Override
				public void afterCompletion(int status) {
					PaySettlementLogDTO paySettlementLogDTO = new PaySettlementLogDTO();
					paySettlementLogDTO.setPaySettlementSn(settlement.getPaySettlementSn());
					paySettlementLogDTO.setPayTypeId(item.getPayTypeId());
					paySettlementLogDTO.setUserId(settlement.getUserId());
					paySettlementLogDTO.setCreateTime(new Date());
					paySettlementLogDTO.setInfo(msg);
					paySettlementLogDTO.setRemark("金额问题！");
					paySettlementLogService.saveLog(paySettlementLogDTO);
				}
			});

			throw new BusinessException("notify callback pay amount error");
		}

		//获取结算单项里的订单号是否有重复支付，如果有，则支付单需要改为支付异常。
		//查出当前支付单的所有订单
		List<PaySettlementOrderDTO> paySettlementOrderDTOS = paySettlementOrderService.queryOrderBySn(settlement.getPaySettlementSn());

		//查询这些订单的所有支付成功的支付单。
		List<String> orderNumberList = paySettlementOrderDTOS.stream().map(PaySettlementOrderDTO::getOrderNumber).collect(Collectors.toList());
		List<PaySettlementDTO> paidByOrderNumberList = paySettlementService.getPaidByOrderNumberList(orderNumberList);

		//如果订单有多次支付结果，【预售订单除外】，则标记为异常,查询到有订单已有已支付的支付单，则更新这个支付单为异常，重复支付。
		if (paidByOrderNumberList != null && paidByOrderNumberList.size() > 0) {
			settlement.setState(PaySettlementStateEnum.EXCEPTION.getCode());
			settlement.setUpdateTime(DateUtil.date());
			this.paySettlementService.updateSettlement(settlement);

			return null;
		} else {
			settlement.setState(PaySettlementStateEnum.PAID.getCode());
			settlement.setUpdateTime(DateUtil.date());
			this.paySettlementService.updateSettlement(settlement);

			item.setTransactionSn(notifyDTO.getTransactionSn());
			item.setUpdateTime(new Date());
			item.setState(PaySettlementStateEnum.PAID.getCode());
			// 更新结算单
			this.paySettlementItemService.update(item);
			return settlementItemList;
		}
	}


	/**
	 * 去除钱包余额冻结
	 */
	public void walletSettle(List<PaySettlementItemDTO> settlementItemList) {
		Optional<PaySettlementItemDTO> itemOptional = settlementItemList.stream().filter(s -> s.getPayTypeId().equals(PayTypeEnum.WALLET_PAY.name())).findFirst();
		if (!itemOptional.isPresent()) {
			return;
		}
		String paySettlementSn = itemOptional.get().getPaySettlementSn();
		log.info("[ ORDER CALLBACK ] 开始处理钱包，支付单号：{}", paySettlementSn);
		List<PaySettlementOrderDTO> paySettlementOrderList = paySettlementOrderService.queryOrderBySn(paySettlementSn);
		List<String> numberList = paySettlementOrderList.stream().map(PaySettlementOrderDTO::getOrderNumber).collect(Collectors.toList());

		R<List<OrderDTO>> orderListResult = this.orderApi.getOrderByOrderNumbersAndUserId(numberList, paySettlementOrderList.get(0).getUserId(), OrderStatusEnum.UNPAID.getValue());

		List<Long> orderIds = orderListResult.getData().stream().map(OrderDTO::getId).collect(Collectors.toList());

		PaySettlementItemDTO walletSettleItem = itemOptional.get();

		// 获取支付抵扣记录
		List<UserWalletDetailsDTO> walletDetailsDTOS = userWalletDetailsService.findDetailsByBusinessId(walletSettleItem.getId(), WalletBusinessTypeEnum.PAYMENT_DEDUCTION);
		if (CollUtil.isNotEmpty(walletDetailsDTOS)) {
			// 如果不为空，则代表已支付成功过，直接跳过
			return;
		}
		// 将钱包明细流水号当作支付单回调单据保存
		walletSettleItem.setTransactionSn("");
		walletSettleItem.setUpdateTime(new Date());
		walletSettleItem.setState(PaySettlementStateEnum.PAID.getCode());
		// 更新结算单
		this.paySettlementItemService.update(walletSettleItem);

		UserWalletOperationDTO operationDTO = new UserWalletOperationDTO();
		operationDTO.setUserId(orderListResult.getData().get(0).getUserId());
		operationDTO.setAmount(walletSettleItem.getAmount());
		operationDTO.setBusinessId(walletSettleItem.getId());
		operationDTO.setBusinessType(WalletBusinessTypeEnum.PAYMENT_DEDUCTION);
		operationDTO.setOperationType(WalletOperationTypeEnum.DEDUCTION);
		operationDTO.setRemarks("支付成功，冻结金额抵扣，相关订单号: " + JSONUtil.toJsonStr(orderListResult.getData().stream().map(OrderDTO::getOrderNumber).collect(Collectors.toList())));
		R<Long> frozenRecordUpdate = this.userWalletBusinessService.synchronizeFrozenRecordUpdate(operationDTO);
		GlobalTransaction currentTransaction = GlobalTransactionContext.getCurrent();
		//确认收货分销佣金处理
		if (null != currentTransaction) {
			TransactionHookManager.registerHook(new TransactionHookAdapter() {
				@Override
				public void afterCommit() {
					userWalletBusinessService.synchronizeNotify(frozenRecordUpdate.getData());
				}
			});
		} else {
			userWalletBusinessService.synchronizeNotify(frozenRecordUpdate.getData());
		}

	}

	/**
	 * 获取所有请求的参数，封装成Map集合 并且验证是否被篡改
	 *
	 * @param request  the request
	 * @param response the response
	 * @return Map
	 */
	public abstract Map<String, String> getNotifyParamMap(HttpServletRequest request, HttpServletResponse response);

	/**
	 *
	 */
	public abstract boolean verifyNotifyParamMap(Map<String, String> notifyParamMap);

	/**
	 * 通知参数Map转为Bean
	 */
	public abstract PayNotifyDTO notifyParamsToBean(Map<String, String> notifyMap);

	/**
	 * 成功响应
	 */
	public abstract String success();

	/**
	 * 失败响应
	 */
	public abstract String fail();
}
