/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.pay.strategy.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.legendshop.basic.api.MessageApi;
import com.legendshop.basic.api.SysParamsApi;
import com.legendshop.basic.dto.OrderSettingDTO;
import com.legendshop.basic.dto.PayTypeDTO;
import com.legendshop.basic.enums.SysParamNameEnum;
import com.legendshop.common.core.constant.R;
import com.legendshop.order.api.OrderApi;
import com.legendshop.order.api.PreSellOrderApi;
import com.legendshop.order.dto.OrderDTO;
import com.legendshop.order.enums.OrderStatusEnum;
import com.legendshop.order.enums.PayTypeEnum;
import com.legendshop.pay.bo.CreatePayBO;
import com.legendshop.pay.dao.ShopIncomingDao;
import com.legendshop.pay.dto.*;
import com.legendshop.pay.entity.ShopIncoming;
import com.legendshop.pay.enums.PaySettlementStateEnum;
import com.legendshop.pay.enums.ShopIncomingStatusEnum;
import com.legendshop.pay.enums.UserWalletAmountTypeEnum;
import com.legendshop.pay.enums.WalletBusinessTypeEnum;
import com.legendshop.pay.service.*;
import com.legendshop.pay.service.convert.PaymentConverter;
import com.legendshop.pay.strategy.PayBusinessStrategy;
import com.legendshop.pay.utils.RSAUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 订单支付业务策略
 *
 * @author legendshop
 */
@Slf4j
@Service
public abstract class AbstractPayBusinessStrategy implements PayBusinessStrategy {

	@Autowired
	protected OrderApi orderApi;

	@Autowired
	protected PaymentService paymentService;

	@Autowired
	protected ShopIncomingDao shopIncomingDao;

	@Autowired
	protected MessageApi messagePushClient;

	@Autowired
	protected SysParamsApi sysParamsApi;

	@Autowired
	protected PaymentConverter paymentConverter;

	@Autowired
	protected UserWalletService userWalletService;

	@Autowired
	protected PreSellOrderApi preSellOrderApi;

	@Autowired
	protected PaySettlementService paySettlementService;

	@Autowired
	protected PaySettlementItemService paySettlementItemService;

	@Autowired
	protected PaySettlementOrderService paySettlementOrderService;

	@Autowired
	protected UserWalletDetailsService userWalletDetailsService;

	@Override
	public R<CreatePayBO> createPrepay(CreatePayDTO createPayDTO) {
		// 构建收银台参数
		CreatePayBO createPayBO = new CreatePayBO();
		List<String> businessOrderNumberList = createPayDTO.getBusinessOrderNumberList();

		// 获取商品订单
		R<List<OrderDTO>> orderListResult = orderApi.getOrderByOrderNumbersAndUserId(businessOrderNumberList, createPayDTO.getUserId(), OrderStatusEnum.UNPAID.getValue());
		List<OrderDTO> orderList = orderListResult.getData();
		if (CollectionUtil.isEmpty(orderList)) {
			return R.fail("订单不存在，请重新提交购买");
		}

		List<Long> orderIds = orderList.stream().map(OrderDTO::getId).collect(Collectors.toList());
		// 获取钱包抵扣金额
		BigDecimal walletAmount = BigDecimal.ZERO;
		List<UserWalletDetailsDTO> walletDetailsList = this.userWalletDetailsService.findDetailsByBusinessIds(orderIds, WalletBusinessTypeEnum.ORDER_DEDUCTION, UserWalletAmountTypeEnum.FROZEN_AMOUNT);
		if (!CollectionUtils.isEmpty(walletDetailsList)) {
			walletAmount = walletDetailsList.stream().map(UserWalletDetailsDTO::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
			log.info("钱包余额抵扣金额 walletAmount：{}", walletAmount);
		}

		BigDecimal totalAmount = BigDecimal.ZERO;
		// 支付描述
		List<String> subjectList = new ArrayList<>();
		for (OrderDTO order : orderList) {
			totalAmount = totalAmount.add(order.getActualTotalPrice());
			subjectList.add(order.getProductName());
		}

		if (BigDecimal.ZERO.compareTo(walletAmount) < 0) {
			totalAmount = totalAmount.subtract(walletAmount);
			log.info("抵扣后需要支付的金额为 totalAmount：{}", totalAmount);
		}

		if (BigDecimal.ZERO.compareTo(walletAmount) > 0) {
			totalAmount = BigDecimal.ZERO;
		}

		createPayBO.setSubjectList(subjectList);
		createPayBO.setBusinessOrderNumberList(businessOrderNumberList);
		createPayBO.setAmount(totalAmount);

		// 默认60分钟
		Integer cancelUnPayOrderMinutes = 60;
		// 获取系统的超时自动取消订单时间配置
		ObjectMapper mapper = new ObjectMapper();
		OrderSettingDTO orderSetting = mapper.convertValue(sysParamsApi.getConfigDtoByParamName(SysParamNameEnum.ORDER_SETTING.name(), OrderSettingDTO.class).getData(), OrderSettingDTO.class);
		if (ObjectUtil.isNotNull(orderSetting) && ObjectUtil.isNotNull(orderSetting.getAutoCancelUnPayOrderMinutes())) {
			cancelUnPayOrderMinutes = orderSetting.getAutoCancelUnPayOrderMinutes();
		}
		// 计算倒数结束时间
		DateTime countdownEndTime = DateUtil.offset(orderList.get(0).getCreateTime(), DateField.MINUTE, cancelUnPayOrderMinutes);
		createPayBO.setOrderCancelCountdownEndTime(countdownEndTime.getTime());

		// 获取已启用的支付方式
		List<PayTypeDTO> enabledPayType = sysParamsApi.getUseEnabledPayType().getData();
		createPayBO.setPayTypeList(enabledPayType);
		return R.ok(createPayBO);
	}

	@Override
	public R<PaymentSuccessDTO> pay(PayParamsDTO payParamsDTO) {

		log.info(" [ Pay ]  -----------------------> ");
		log.info(" [ Pay ] param : {} ", JSONUtil.toJsonStr(payParamsDTO));

		//检查是否支持该支付方式
		List<PayTypeDTO> enabledPayType = sysParamsApi.getEnabledPayType().getData();
		Boolean isHavePayType = enabledPayType.stream().anyMatch(payType -> payType.getPayTypeId().equals(payParamsDTO.getPayTypeId()));
		if (!isHavePayType) {
			return R.fail("系统不支持该支付方式!");
		}

		// 检查是否重复支付
		R<Void> checkResult = this.paySettlementService.checkRepeatPay(payParamsDTO);
		if (!checkResult.success()) {
			return R.fail(checkResult.getMsg());
		}

		// 计算合并订单实际金额，支付信息
		R<PaymentFromDTO> paymentFromResult = initPaymentFrom(payParamsDTO);
		if (!paymentFromResult.success()) {
			return R.fail(paymentFromResult.getMsg());
		}

		PaymentFromDTO fromData = paymentFromResult.getData();


		// 组装支付单保存
		R<Void> saveResult = saveSubSettlement(fromData);
		if (!saveResult.success()) {
			return R.fail("支付异常，保存支付单据失败");
		}

		log.info(" 调用第三方支付 ----------------------------->");
		PaymentDTO paymentDTO = this.paymentConverter.convert2PaymentDTO(fromData);
		log.info(" 调用第三方支付参数 ------------------------------> param : {}", JSONUtil.toJsonStr(paymentDTO));
		R<String> paymentResult = this.paymentService.payment(paymentDTO);
		if (!paymentResult.success()) {
			return R.fail(paymentResult.getMsg());
		}
		PaymentSuccessDTO paymentSuccessDTO = new PaymentSuccessDTO();
		paymentSuccessDTO.setPaySettlementSn(paymentFromResult.getData().getNumber());
		//加密
		paymentSuccessDTO.setPaymentResult(RSAUtils.encryption(paymentResult.getData()));
		log.info("解密后:" + RSAUtils.decrypt(paymentResult.getData()));
		return R.ok(paymentSuccessDTO);
	}

	/**
	 * 保存支付单据
	 */
	private R<Void> saveSubSettlement(PaymentFromDTO paymentFromDTO) {
		// 保存时间
		Date now = new Date();
		// 用户ID
		Long userId = paymentFromDTO.getUserId();
		// 业务号
		String number = paymentFromDTO.getNumber();
		// 组装支付单据
		PaySettlementDTO settlement = new PaySettlementDTO();
		settlement.setUserId(userId);
		settlement.setPaySettlementSn(number);
		settlement.setUseType(paymentFromDTO.getSettlementType());
		settlement.setPaySource(paymentFromDTO.getVisitSource().name());
		settlement.setState(PaySettlementStateEnum.CREATE.getCode());
		settlement.setCreateTime(now);

		List<PaySettlementItemDTO> itemList = new ArrayList<>();
		// 是否使用钱包支付
		List<UserWalletDetailsDTO> useWalletList = paymentFromDTO.getUseWalletList();
		if (!CollectionUtils.isEmpty(useWalletList)) {
			useWalletList.forEach(e -> {
				PaySettlementItemDTO walletItem = new PaySettlementItemDTO();
				walletItem.setPaySettlementSn(number);
				walletItem.setAmount(e.getAmount());
				walletItem.setRefundAmount(BigDecimal.ZERO);
				walletItem.setPayTypeId(PayTypeEnum.WALLET_PAY.name());
				walletItem.setCreateTime(now);
				walletItem.setState(PaySettlementStateEnum.CREATE.getCode());
				itemList.add(walletItem);
			});
			paymentFromDTO.setAmount(paymentFromDTO.getAmount().subtract(useWalletList.stream().map(UserWalletDetailsDTO::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add)));
		}

		// 组装支付单据项
		PaySettlementItemDTO paySettlementItemDTO = new PaySettlementItemDTO();
		itemList.add(paySettlementItemDTO);
		paySettlementItemDTO.setPaySettlementSn(number);
		paySettlementItemDTO.setAmount(paymentFromDTO.getAmount());
		paySettlementItemDTO.setRefundAmount(BigDecimal.ZERO);
		paySettlementItemDTO.setPayTypeId(paymentFromDTO.getPayTypeId());
		paySettlementItemDTO.setCreateTime(now);
		paySettlementItemDTO.setState(PaySettlementStateEnum.CREATE.getCode());

		// 获取订单创建订单收据（可能有多个订单号）
		List<String> businessOrderNumberList = paymentFromDTO.getBusinessOrderNumberList();
		// 订单号关联支付单据处理
		List<PaySettlementOrderDTO> paySettlementOrderList = new ArrayList<>();
		for (String orderNumber : businessOrderNumberList) {
			PaySettlementOrderDTO paySettlementOrderDTO = new PaySettlementOrderDTO();
			paySettlementOrderDTO.setOrderNumber(orderNumber);
			paySettlementOrderDTO.setPaySettlementSn(number);
			paySettlementOrderDTO.setUserId(userId);
			paySettlementOrderList.add(paySettlementOrderDTO);
		}
		boolean settlementResult = paySettlementService.saveSettlement(settlement);
		if (!settlementResult) {
			return R.fail();
		}
		boolean itemResult = paySettlementItemService.savePaySettlementItem(itemList);
		if (!itemResult) {
			return R.fail();
		}
		boolean orderResult = paySettlementOrderService.batchSavePaySettlementOrder(paySettlementOrderList);
		if (!orderResult) {
			return R.fail();
		}
		return R.ok();
	}

	/**
	 * 订单钱包余额使用
	 */
	protected List<UserWalletDetailsDTO> orderWalletDeduction(List<Long> orderIds) {
		return this.userWalletDetailsService.findDetailsByBusinessIds(orderIds, WalletBusinessTypeEnum.ORDER_DEDUCTION, UserWalletAmountTypeEnum.FROZEN_AMOUNT);
	}

	/**
	 * 组装付款参数
	 */
	public abstract R<PaymentFromDTO> initPaymentFrom(PayParamsDTO payParamsDTO);

	/**
	 * 扩展subject
	 */
	protected abstract StringBuilder extendSubject(List<OrderDTO> orderList, StringBuilder subject);

	/**
	 * 检查进件
	 *
	 * @param shopIds
	 * @param payTypeList
	 * @return
	 */
	protected R<List<PayTypeDTO>> checkShopIncoming(List<Long> shopIds, List<PayTypeDTO> payTypeList) {
		List<ShopIncoming> incomingList = shopIncomingDao.getByShopId(shopIds);
		if (incomingList.size() != shopIds.size()
				|| incomingList.stream().anyMatch(incomingDTO -> !ShopIncomingStatusEnum.COMPLETED.getValue().equals(incomingDTO.getStatus()))) {
			log.info("存在未进件商户，不显示易宝支付方式！");
			payTypeList.removeIf(e -> SysParamNameEnum.YEEPAY_WX_PAY.name().equals(e.getPayTypeId()) || SysParamNameEnum.YEEPAY_ALI_PAY.name().equals(e.getPayTypeId()) || SysParamNameEnum.YEEPAY_UNION_PAY.name().equals(e.getPayTypeId()));
		}
		return R.ok(payTypeList);
	}
}
