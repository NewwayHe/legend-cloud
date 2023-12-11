/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.strategy.submit.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.expetion.BusinessException;
import com.legendshop.order.bo.AssemblyOrderBO;
import com.legendshop.order.bo.ConfirmOrderBO;
import com.legendshop.order.dto.*;
import com.legendshop.order.enums.OrderTypeEnum;
import com.legendshop.order.service.PreSellOrderService;
import com.legendshop.order.strategy.submit.SubmitOrderStrategy;
import com.legendshop.pay.enums.SettlementTypeEnum;
import com.legendshop.product.api.PreSellProductApi;
import com.legendshop.product.dto.PreSellProductDTO;
import com.legendshop.product.dto.ProductDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 预售订单提交策略
 *
 * @author legendshop
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PreSaleSubmitOrderStrategy extends BaseSubmitOrderStrategy implements SubmitOrderStrategy {

	final PreSellOrderService preSellOrderService;

	final PreSellProductApi preSellProductApi;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public R submit(ConfirmOrderBO confirmOrderBo) {
		log.info("进入预售订单提交策略, params: {}", JSONUtil.toJsonStr(confirmOrderBo));
		return super.submit(confirmOrderBo);
	}

	@Override
	protected void platformCouponsShard(ConfirmOrderBO confirmOrderBo) {
		super.handlerPlatformCouponsShard(confirmOrderBo);
	}

	@Override
	protected void couponsShard(SubmitOrderShopDTO submitOrderShopDTO) {
		super.handlerShopCouponsShard(submitOrderShopDTO);
	}


	@Override
	protected R<SubmitOrderSuccessDTO> assemblySubmitOrderSuccessProcess(SubmitOrderSuccessDTO submitOrderSuccessDTO) {

		// 设置支付类型
		submitOrderSuccessDTO.setSettlementType(SettlementTypeEnum.PRE_SALE_ORDER_DEPOSIT.getValue());

		return R.ok(submitOrderSuccessDTO);
	}

	@Override
	protected R<Void> activityCheckProcess(ConfirmOrderBO confirmOrderBo) {
		// 当前下单的所有skuId
		List<Long> productIds = confirmOrderBo.getShopOrderList().stream().map(SubmitOrderShopDTO::getSkuList).filter(item -> !CollectionUtils.isEmpty(item)).reduce(new ArrayList<>(), (all, item) -> {
			all.addAll(item);
			return all;
		}).stream().map(SubmitOrderSkuDTO::getProductId).collect(Collectors.toList());
		// 预售每个订单只允许有一个商家一个商品
		if (CollectionUtils.isEmpty(productIds)) {
			return R.fail("下单商品不能为空！");
		}
		Long product = productIds.get(0);
		if (null == product || product == 0L) {
			return R.fail("下单商品信息错误！");
		}
		// 查询预售信息
		R<PreSellProductDTO> preSellProductDTO = preSellProductApi.getByProductId(product);
		if (!preSellProductDTO.getSuccess()) {
			return R.fail(preSellProductDTO.getMsg());
		}
		PreSellProductDTO sellProductDTO = preSellProductDTO.getData();

		if (!sellProductDTO.getPreSaleStart().before(new Date())) {
			R.fail("部分商品未到定金支付时间");

		}
		return R.process(distributionUtil.checkCommission(confirmOrderBo).getSuccess(), "订单数据已发生变化，请重新提交！");
	}


	@Override
	protected R<Void> deductionStockProcess(SubmitOrderShopDTO submitOrderShopDTO) {
		log.info("########## 进入预售订单下单扣减库存判断逻辑   ##############");
		List<SubmitOrderSkuDTO> skuList = submitOrderShopDTO.getSkuList();
		for (SubmitOrderSkuDTO skuDTO : skuList) {

			// 获取商品是否为下单扣减库存
			ProductDTO productDTO = productApi.getDtoByProductId(skuDTO.getProductId()).getData();
			if (ObjectUtil.isNull(productDTO) || ObjectUtil.isNull(productDTO.getStockCounting())) {
				return R.fail("下单--库存扣减失败");
			}
			if (productDTO.getStockCounting()) {
				return R.ok();
			}
			log.info("########## 预售订单下单扣减库存 商品{}  ##############", skuDTO);
			// 扣减库存
			boolean result = stockApi.reduceHold(skuDTO.getProductId(), skuDTO.getSkuId(), skuDTO.getTotalCount()).getData();
			if (!result) {
				return R.fail("下单--库存扣减失败");
			}
			log.info("###### 预售订单下单扣减库存成功 ##### ");
		}
		return R.ok();
	}

	@Override
	protected AssemblyOrderBO uniqueLogicProcess(AssemblyOrderBO assemblyOrderBo, String groupNumber, Long activityId) {
		OrderDTO orderDTO = assemblyOrderBo.getOrderDTO();
		orderDTO.setOrderType(OrderTypeEnum.PRE_SALE.getValue());
		// Override
		return assemblyOrderBo;
	}


	@Override
	protected AssemblyOrderBO specificBusiness(AssemblyOrderBO assemblyOrderBo) {
		log.info(" [ 预售订单特殊处理 ] ");
		OrderDTO orderDTO = assemblyOrderBo.getOrderDTO();
		List<OrderItemDTO> orderItemList = assemblyOrderBo.getOrderItemDTO();
		if (null == orderDTO) {
			throw new BusinessException("预售下单错误！");
		}

		Long orderId = orderDTO.getId();

		// 预售订单下单商品有且只有一个
		Map<Long, List<OrderItemDTO>> listMap = orderItemList.stream().collect(Collectors.groupingBy(OrderItemDTO::getSkuId));
		if (listMap.size() != 1) {
			throw new BusinessException("预售下单错误！");
		}
		Long preSellProductId = orderItemList.get(0).getProductId();

		log.info(" [ 预售订单特殊处理 ] orderId : {} , preSellProductId : {}", orderId, preSellProductId);
		// 保存预售订单信息
		this.preSellOrderService.savePreSellOrder(orderDTO, preSellProductId);

		return super.handleIntegralDetail(assemblyOrderBo);
	}

	@Override
	protected AssemblyOrderBO specificAfterSaveOrder(AssemblyOrderBO assemblyOrderBo) {
		return assemblyOrderBo;
	}

}
