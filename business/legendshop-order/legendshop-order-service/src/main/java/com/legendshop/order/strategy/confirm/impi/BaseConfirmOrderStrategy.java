/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.strategy.confirm.impi;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.csp.sentinel.util.StringUtil;
import com.legendshop.activity.api.CouponApi;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.data.cache.util.CacheManagerUtil;
import com.legendshop.order.bo.ConfirmOrderBO;
import com.legendshop.order.dto.*;
import com.legendshop.order.service.*;
import com.legendshop.order.service.convert.OrderConverter;
import com.legendshop.order.service.impl.handler.CartHandler;
import com.legendshop.order.strategy.confirm.ConfirmOrderStrategy;
import com.legendshop.pay.api.ShopIncomingApi;
import com.legendshop.pay.api.YeepayApi;
import com.legendshop.pay.dto.ShopIncomingDTO;
import com.legendshop.pay.enums.ShopIncomingStatusEnum;
import com.legendshop.product.api.ProductApi;
import com.legendshop.product.api.SkuApi;
import com.legendshop.product.dto.ProductDTO;
import com.legendshop.product.dto.ProductQuotaDTO;
import com.legendshop.product.enums.ProductDeliveryTypeEnum;
import com.legendshop.product.enums.ProductQuotaTypeEnum;
import com.legendshop.shop.api.ShopDetailApi;
import com.legendshop.user.api.UserDetailApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 确认订单的空策略
 *
 * @author legendshop
 */
@Slf4j
@Service
public abstract class BaseConfirmOrderStrategy implements ConfirmOrderStrategy {

	@Autowired
	protected ShopDetailApi shopDetailApi;

	@Autowired
	protected OrderUtil orderUtil;

	@Autowired
	protected CouponUtil couponUtil;

	@Autowired
	protected OrderConverter orderConverter;

	@Autowired
	protected SkuApi skuApi;

	@Autowired
	protected ProductApi productApi;

	@Autowired
	protected CouponApi couponApi;

	@Autowired
	protected OrderCacheService orderCacheService;

	@Autowired
	protected OrderService orderService;

	@Autowired
	protected YeepayApi yeepayApi;

	@Autowired
	private ShopIncomingApi shopIncomingApi;

	@Autowired
	protected StringRedisTemplate redisTemplate;

	@Autowired
	protected Map<String, CartHandler> cartHandler;

	@Autowired
	protected CacheManagerUtil cacheManagerUtil;

	@Autowired
	private UserDetailApi userDetailApi;

	@Autowired
	private OrderItemService orderItemService;

	@Override
	public R<ConfirmOrderBO> check(ConfirmOrderDTO confirmOrderDTO) {
		log.info("进入下单检查以及组装商品信息模板策略, params: {}", JSONUtil.toJsonStr(confirmOrderDTO));

		if (ObjectUtil.isNull(confirmOrderDTO)) {
			return R.fail("下单数据为空，请确认后重新购买");
		}
		// 预订单数据
		ConfirmOrderBO confirmOrderBo = new ConfirmOrderBO();
		// 预订单数据生成UUID
		String s = UUID.randomUUID().toString();
		confirmOrderBo.setId(s);
		confirmOrderBo.setType(confirmOrderDTO.getOrderType());
		confirmOrderBo.setAddressId(confirmOrderDTO.getAddressId());
		confirmOrderBo.setUserId(confirmOrderDTO.getUserId());
		confirmOrderBo.setActivityId(confirmOrderDTO.getActivityId());
		confirmOrderBo.setGroupNumber(confirmOrderDTO.getGroupNumber());
		confirmOrderBo.setRegionalSalesFlag(false);
		confirmOrderBo.setDeductionFlag(confirmOrderDTO.getDeductionFlag());


		// 组装按店铺分组商品信息
		R<List<SubmitOrderShopDTO>> shopOrderListResult = checkAndAssemblyConfirmOrderProcess(confirmOrderDTO);
		if (!shopOrderListResult.success()) {
			return R.fail(shopOrderListResult.getMsg());
		}
		// 根据价格升序排序
		shopOrderListResult.getData().forEach(submitOrderShopDTO -> submitOrderShopDTO.getSkuList().sort(Comparator.comparing(SubmitOrderSkuDTO::getTotalPrice)));
		List<SubmitOrderShopDTO> shopOrderListResultData = shopOrderListResult.getData();

		for (SubmitOrderShopDTO shopOrderListResultDatum : shopOrderListResultData) {
			confirmOrderBo.setShopId(shopOrderListResultDatum.getShopId());
		}
		confirmOrderBo.setShopOrderList(shopOrderListResultData);
		log.info("###### 完成下单检查以及商品组装 ##### ");

		//检验用户的配送方式
		confirmOrderBo = checkAndDeliveryTypeConfirmOrderProcess(shopOrderListResultData, confirmOrderBo);

		// 把预订单信息放入缓存
		orderCacheService.putConfirmOrderInfoCache(confirmOrderDTO.getUserId(), confirmOrderBo);
		log.info("###### 用户{}， 生成预订单缓存 {} ##### ", confirmOrderDTO.getUserId(), JSONUtil.toJsonStr(confirmOrderBo));
		return R.ok(confirmOrderBo);
	}

	/**
	 * 检验用户的配送方式
	 *
	 * @param shopOrderListResultData
	 * @param confirmOrderBO
	 */
	private ConfirmOrderBO checkAndDeliveryTypeConfirmOrderProcess(List<SubmitOrderShopDTO> shopOrderListResultData, ConfirmOrderBO confirmOrderBO) {
		//无自提地址--快递
		confirmOrderBO.setDeliveryType(ProductDeliveryTypeEnum.EXPRESS_DELIVERY.getCode());
		return confirmOrderBO;

	}


	@Override
	public R<ConfirmOrderBO> confirm(ConfirmOrderBO confirmOrderBo) {

		log.info("进入确认订单模板策略, params: {}", JSONUtil.toJsonStr(confirmOrderBo));

		/*
		 * 1.组装收货地址，根据传入的收货地址ID,计算运费以及判断区域限售,如果部分存在限售，则商品信息状态显示无货。并归为失效商品。如果收货地址ID为空，则取用户默认收货地址。
		 * 如果用户没有维护收货地址，则不计算运费。
		 */
		R<ConfirmOrderBO> deliveryResult = orderUtil.handlerDelivery(confirmOrderBo.getAddressId(), confirmOrderBo);
		if (!deliveryResult.success()) {
			return deliveryResult;
		}

		/*
		 * 2.处理发票信息，商家当前是否支持开具发票以及支持开具的发票类型。若支持开具发票，默认选取用户默认发票，如果商家不支持用户当前默认发票类型，则选择商家支持的开具的其他类型
		 * 最新的发票信息，如果都没有，则前端显示新增发票。若不支持开具发票，则前端发票信息处，显示不开具发票，并且无法修改。
		 */
		R<ConfirmOrderBO> invoiceResult = orderUtil.handlerInvoice(deliveryResult.getData());


		/*
		 * 4.计算各个商家最优优惠券
		 */
		R<ConfirmOrderBO> bestCouponResult = getShopBestCoupons(invoiceResult.getData());

		/*
		 * 5.计算平台优惠券
		 */
		R<ConfirmOrderBO> platformCouponResult = getPlatformCoupons(bestCouponResult.getData());


		/*
		 * 7.处理特殊业务，如：预售逻辑：得出定金金额和尾款金额
		 */
		R<ConfirmOrderBO> specificBusiness = handleSpecificBusiness(platformCouponResult.getData());
		ConfirmOrderBO data = specificBusiness.getData();

		/*
		 * 8.初始化钱包对象
		 * */
		data.setUseWalletInfo(new UseWalletInfoDTO());
		log.info("###### 获取确认订单信息处理完成 {} ##### ", JSONUtil.toJsonStr(data));

		// 更新预订单缓存
		orderCacheService.putConfirmOrderInfoCache(confirmOrderBo.getUserId(), data);
		return specificBusiness;
	}


	/**
	 * 检查下单信息并组装商品数据
	 *
	 * @param confirmOrderDTO
	 * @return
	 */
	protected abstract R<List<SubmitOrderShopDTO>> checkAndAssemblyConfirmOrderProcess(ConfirmOrderDTO confirmOrderDTO);


	/**
	 * 最优优惠券处理
	 *
	 * @param confirmOrderBO
	 * @return
	 */
	protected abstract R<ConfirmOrderBO> getShopBestCoupons(ConfirmOrderBO confirmOrderBO);


	/**
	 * 最优优惠券处理
	 *
	 * @param confirmOrderBO
	 * @return
	 */
	protected abstract R<ConfirmOrderBO> getPlatformCoupons(ConfirmOrderBO confirmOrderBO);


	/**
	 * 8.处理特殊业务，如：预售逻辑：得出定金金额和尾款金额
	 */
	@Override
	public R<ConfirmOrderBO> handleSpecificBusiness(ConfirmOrderBO confirmOrderBO) {
		return R.ok(confirmOrderBO);
	}


	protected R<String> checkShopIncoming(Long shopId) {
		R<Boolean> enabled = yeepayApi.enabled();
		if (!enabled.getSuccess() || !enabled.getData()) {
			return R.ok();
		}

		R<ShopIncomingDTO> result = shopIncomingApi.getByShopId(shopId);
		if (!result.getSuccess() || ObjectUtil.isEmpty(result.getData())) {
			return R.fail("该商家还未完成进件配置");
		}

		ShopIncomingDTO incomingDTO = result.getData();
		if (ObjectUtil.isEmpty(incomingDTO.getMerchantNo()) || !ShopIncomingStatusEnum.COMPLETED.getValue().equals(incomingDTO.getStatus())) {
			return R.fail("该商家还未完成进件配置");
		}
		return R.ok(incomingDTO.getMerchantNo());
	}

	protected R checkQuotaOrder(ProductDTO productDTO, ConfirmOrderDTO confirmOrderDTO) {
		log.info("开始处理限购校验");
		//获取限购商品
		ProductQuotaDTO productQuotaDTO = productDTO.getProductQuotaDTO();

		//判断是否限购
		if (StringUtil.isBlank(productQuotaDTO.getQuotaType())) {
			log.info("本商品不限购，校验完成");
			return R.ok();
		}
		//获取提交订单的商品数量
		QuotaOrderDTO quotaOrderDTO = new QuotaOrderDTO();
		quotaOrderDTO.setProductId(productDTO.getId());
		BeanUtil.copyProperties(productQuotaDTO, quotaOrderDTO);
		quotaOrderDTO.setUserId(confirmOrderDTO.getUserId());

		//判断购买数量是否超出限购数量
		log.info("本次限购校验类型: {}", productQuotaDTO.getQuotaType());
		if (StringUtil.isNotBlank(productQuotaDTO.getQuotaType())) {
			List<ConfirmOrderItemDTO> quotaOrderList = confirmOrderDTO.getConfirmOrderItemDTOList().stream()
					// 购买数量大于限购数据
					.filter(c -> c.getCount() > quotaOrderDTO.getQuotaCount())
					// 购买时间大于生效时间
					.filter(c -> quotaOrderDTO.getQuotaTime().before(new Date()))
					.collect(Collectors.toList());
			if (quotaOrderList.size() > 0) {
				String msg = "商品:" + productDTO.getName() + "为限购商品, 限购" + quotaOrderDTO.getQuotaCount() + "件, 您已超出购买件数";
				log.info("###### {} ##### ", msg);
				return R.fail(msg);
			}
			//时间限购, 限购事件段内购买数量是否超出限购数量
			if (!ProductQuotaTypeEnum.ORDER.getValue().equals(productQuotaDTO.getQuotaType())) {
				//获取指定时间内的限购订单数
				Integer quotaOrdersum = orderItemService.getQuotaorderSUM(quotaOrderDTO);
				if (ObjectUtil.isNull(quotaOrdersum)) {
					return R.ok();
				}
				//当前购买数量
				int buySum = confirmOrderDTO.getConfirmOrderItemDTOList().stream().mapToInt(ConfirmOrderItemDTO::getCount).sum();
				if (productQuotaDTO.getQuotaCount() - quotaOrdersum < buySum) {
					String msg = "商品:" + productDTO.getName() + "为限购商品, 限购" + quotaOrderDTO.getQuotaCount() + "件, 您已超出购买件数";
					log.info("###### {} ##### ", msg);
					return R.fail(msg);
				}
			}
		}
		log.info("商品限购校验完成");
		return R.ok();
	}

}
