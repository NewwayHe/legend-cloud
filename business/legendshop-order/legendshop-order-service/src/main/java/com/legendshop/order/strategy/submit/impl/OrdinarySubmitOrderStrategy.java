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
import com.legendshop.order.bo.AssemblyOrderBO;
import com.legendshop.order.bo.ConfirmOrderBO;
import com.legendshop.order.dto.SubmitOrderShopDTO;
import com.legendshop.order.dto.SubmitOrderSkuDTO;
import com.legendshop.order.dto.SubmitOrderSuccessDTO;
import com.legendshop.order.strategy.submit.SubmitOrderStrategy;
import com.legendshop.pay.enums.SettlementTypeEnum;
import com.legendshop.product.dto.ProductDTO;
import com.legendshop.user.api.UserDetailApi;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 普通订单提交策略
 *
 * @author legendshop
 */
@Slf4j
@Service
public class OrdinarySubmitOrderStrategy extends BaseSubmitOrderStrategy implements SubmitOrderStrategy {

	@Resource
	private UserDetailApi userDetailApi;


	@Override
	@Transactional(rollbackFor = Exception.class)
	public R submit(ConfirmOrderBO confirmOrderBo) {

		log.info("进入普通订单提交策略, params: {}", JSONUtil.toJsonStr(confirmOrderBo));
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
		submitOrderSuccessDTO.setSettlementType(SettlementTypeEnum.ORDINARY_ORDER.getValue());
		return R.ok(submitOrderSuccessDTO);
	}


	@Override
	protected R activityCheckProcess(ConfirmOrderBO confirmOrderBo) {
		return R.process(distributionUtil.checkCommission(confirmOrderBo).getSuccess(), "订单数据已发生变化，请重新提交！");
	}

	@Override
	protected R deductionStockProcess(SubmitOrderShopDTO submitOrderShopDTO) {
		log.info("########## 进入普通订单下单扣减库存判断逻辑   ##############");

		List<SubmitOrderSkuDTO> skuList = submitOrderShopDTO.getSkuList();
		for (SubmitOrderSkuDTO skuDTO : skuList) {

			ProductDTO productDTO = productApi.getDtoByProductId(skuDTO.getProductId()).getData();
			if (ObjectUtil.isNull(productDTO) || ObjectUtil.isNull(productDTO.getStockCounting())) {
				return R.fail("下单--库存扣减失败");
			}
			// 获取商品是否为下单扣减库存 为付款减少库存则提交订单不进行库存扣减操作
			if (productDTO.getStockCounting()) {
				continue;
			}
			log.info("########## 普通订单下单扣减库存 商品{}  ##############", skuDTO);
			// 扣减库存
			boolean result = stockApi.reduceHold(skuDTO.getProductId(), skuDTO.getSkuId(), skuDTO.getTotalCount()).getData();
			if (!result) {
				return R.fail("下单--库存扣减失败");
			}
			log.info("###### 普通订单下单扣减库存成功 ##### ");
		}
		return R.ok();
	}


	@Override
	protected AssemblyOrderBO uniqueLogicProcess(AssemblyOrderBO assemblyOrderBo, String groupNumber, Long activityId) {
		// 普通订单使用模板方法，不需要重写逻辑
		return assemblyOrderBo;
	}

	@Override
	protected AssemblyOrderBO specificBusiness(AssemblyOrderBO assemblyOrderBo) {
		// 普通订单使用模板方法，不需要重写逻辑
		return assemblyOrderBo;
	}

	@Override
	protected R extensionMethod(SubmitOrderSkuDTO skuDTO) {
		// 实现普通订单特殊逻辑
		return super.extensionMethod(skuDTO);
	}

	@Override
	protected AssemblyOrderBO specificAfterSaveOrder(AssemblyOrderBO assemblyOrderBo) {
		return super.handleIntegralDetail(assemblyOrderBo);
	}
}
