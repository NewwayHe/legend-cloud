/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.expetion.BusinessException;
import com.legendshop.order.bo.ConfirmOrderBO;
import com.legendshop.order.dto.InvalidOrderShopDTO;
import com.legendshop.order.dto.InvalidOrderSkuDTO;
import com.legendshop.order.dto.SubmitOrderShopDTO;
import com.legendshop.order.enums.OrderTypeEnum;
import com.legendshop.order.service.OrderUtil;
import com.legendshop.order.service.convert.OrderConverter;
import com.legendshop.product.api.TransFeeCalculateApi;
import com.legendshop.product.dto.TransFeeCalProductDTO;
import com.legendshop.product.dto.TransFeeCalculateDTO;
import com.legendshop.product.enums.ProductDeliveryTypeEnum;
import com.legendshop.product.enums.TransCalFeeResultEnum;
import com.legendshop.shop.api.ShopDetailApi;
import com.legendshop.shop.dto.ShopDetailDTO;
import com.legendshop.user.api.UserAddressApi;
import com.legendshop.user.api.UserContactApi;
import com.legendshop.user.api.UserInvoiceApi;
import com.legendshop.user.bo.UserAddressBO;
import com.legendshop.user.bo.UserContactBO;
import com.legendshop.user.bo.UserInvoiceBO;
import com.legendshop.user.dto.UserInvoiceForOrderDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 订单常用处理工具类，
 * 公共的订单处理逻辑。
 *
 * @author legendshop
 */
@Slf4j
@Service
@AllArgsConstructor
public class OrderUtilImpl implements OrderUtil {


	private final ShopDetailApi shopDetailApi;

	private final UserAddressApi userAddressApi;

	private final UserInvoiceApi userInvoiceApi;

	private final TransFeeCalculateApi transFeeCalculateApi;

	private final OrderConverter orderConverter;

	private final UserContactApi userContactApi;

	@Override
	public R<ConfirmOrderBO> handlerDelivery(Long addressId, ConfirmOrderBO confirmOrderBo) {

		log.info("###### 进入组装收货地址、提货信息以及运费计算逻辑 ##### ");
		// 获取用户收货地址
		R<UserAddressBO> userAddressBOR = this.userAddressApi.getUserAddressForOrder(confirmOrderBo.getUserId(), addressId);
		if (!userAddressBOR.getSuccess()) {
			return R.fail(userAddressBOR.getMsg());
		}
		// 如果商品包含自提，获取用户提货信息
		if (!ProductDeliveryTypeEnum.EXPRESS_DELIVERY.getCode().equals(confirmOrderBo.getDeliveryType())) {
			R<UserContactBO> userContactBOR = userContactApi.getUserContactForOrder(confirmOrderBo.getUserId(), null);
			if (!userContactBOR.getSuccess()) {
				return R.fail(userContactBOR.getMsg());
			}
			confirmOrderBo.setUserContactBO(userContactBOR.getData());
		}
		UserAddressBO userAddress = userAddressBOR.getData();
		// 用户没有维护的地址，则不用计算运费
		if (ObjectUtil.isNull(userAddress)) {
			return R.ok(confirmOrderBo);
		}
		confirmOrderBo.setAddressId(addressId);
		// 组装收货地址信息
		confirmOrderBo.setUserAddressBO(userAddress);
		log.info("###### 预订单收货地址信息 {} ##### ", JSONUtil.toJsonStr(userAddress));
		if (confirmOrderBo.getType().equals(OrderTypeEnum.INTEGRAL)) {
			return R.ok(confirmOrderBo);
		}
		log.info("###### 组装计算运费以及区域限售处理  ##### ");
		TransFeeCalculateDTO transFeeCalculateDTO = new TransFeeCalculateDTO();
		List<SubmitOrderShopDTO> shopOrderList = confirmOrderBo.getShopOrderList();

		// 区域限售失效商品筛选map集合
		Map<Long, InvalidOrderShopDTO> invalidOrderShopMap = new HashMap<>(shopOrderList.size());

		// 区域限售标识
		boolean regionalSalesFlag = false;

		for (SubmitOrderShopDTO shop : shopOrderList) {
			transFeeCalculateDTO.setShopId(shop.getShopId());
			transFeeCalculateDTO.setCityId(userAddress.getCityId());

			// 计算运费的商品
			List<TransFeeCalProductDTO> transFeeCalProductList = orderConverter.convert2TransFeeCalProductDtoList(shop.getSkuList());
			transFeeCalculateDTO.setCalProductDTOS(transFeeCalProductList);
			log.info("###### 计算运费的商品 {} ##### ", JSONUtil.toJsonStr(transFeeCalProductList));

			// 计算运费以及区域限售情况 todo 组装成所有店铺的商品数据，只调用一次
			R calTransFeeResult = transFeeCalculateApi.calTransFee(transFeeCalculateDTO);
			// 出现区域限售情况，预订单商品状态改为无货，激活整个预订单的区域限售标识，区域限售商品移到失效商品集合
			if (!calTransFeeResult.success() && calTransFeeResult.getCode() == TransCalFeeResultEnum.NOT_SUPPORT_AREA.getCode()) {

				// 处理拆分为微服务后，强转失败问题。TODO 待研究更好的解决方案 https://blog.csdn.net/keygod1/article/details/84070982
				ObjectMapper mapper = new ObjectMapper();
				List<TransFeeCalProductDTO> resultData = new ArrayList<>();
				List data = (List) calTransFeeResult.getData();
				for (Object o : data) {
					TransFeeCalProductDTO transFeeCalProductDTO = mapper.convertValue(o, TransFeeCalProductDTO.class);
					resultData.add(transFeeCalProductDTO);
				}
				shop.getSkuList().forEach(sku -> {
					resultData.forEach(resultSku -> {
						if (sku.getProductId().equals(resultSku.getProductId())) {
							sku.setStatusFlag(false);
							log.info("###### 区域限售商品 {} ##### ", JSONUtil.toJsonStr(sku));
						} else {
							sku.setStatusFlag(true);
						}
					});
					// 处理限售商品
					if (!sku.getStatusFlag()) {

						InvalidOrderSkuDTO invalidOrderSkuDTO = orderConverter.convert2InvalidOrderSkuDTO(sku);
						if (invalidOrderShopMap.containsKey(shop.getShopId())) {
							invalidOrderShopMap.get(shop.getShopId()).getInvalidOrderSkuList().add(invalidOrderSkuDTO);

						} else {
							InvalidOrderShopDTO invalidOrderShopDTO = new InvalidOrderShopDTO();
							invalidOrderShopDTO.setShopId(shop.getShopId());
							invalidOrderShopDTO.setShopName(shop.getShopName());
							ArrayList<InvalidOrderSkuDTO> invalidOrderSkuList = new ArrayList<>();
							invalidOrderSkuList.add(invalidOrderSkuDTO);
							invalidOrderShopDTO.setInvalidOrderSkuList(invalidOrderSkuList);
							invalidOrderShopMap.put(shop.getShopId(), invalidOrderShopDTO);
						}
					}
				});
				regionalSalesFlag = true;
			}
			if (!calTransFeeResult.success() && calTransFeeResult.getCode() != TransCalFeeResultEnum.NOT_SUPPORT_AREA.getCode()) {
				log.info("###### 运费计算失败 {} ##### ", calTransFeeResult.getMsg());
				return R.fail(calTransFeeResult.getMsg());
			}
			if (calTransFeeResult.success()) {
				// 无区域限售，则恢复商品状态
				shop.getSkuList().forEach(sku -> {
					sku.setStatusFlag(true);
				});
				//计算运费成功
				ObjectMapper mapper = new ObjectMapper();
				shop.setDeliveryAmount(mapper.convertValue(calTransFeeResult.getData(), BigDecimal.class));
				log.info("###### 运费计算完成，商家{} 总运费为{} ##### ", shop.getShopName(), calTransFeeResult.getData());
			}
		}
		confirmOrderBo.setRegionalSalesFlag(regionalSalesFlag);
		return R.ok(confirmOrderBo);
	}


	@Override
	public R<ConfirmOrderBO> handlerInvoice(ConfirmOrderBO confirmOrderBo) {

		log.info("###### 进入确认订单处理发票信息 ##### ");
		List<SubmitOrderShopDTO> shopOrderList = confirmOrderBo.getShopOrderList();
		// 获取商家信息
		shopOrderList.forEach(shop -> {

			R<ShopDetailDTO> shopDetailResult = shopDetailApi.getById(shop.getShopId());
			if (shopDetailResult.success()) {
				ShopDetailDTO shopDetail = shopDetailResult.getData();
				// 商家是否支持开具发票
				Boolean invoiceFlag = shopDetail.getInvoiceFlag();
				shop.setInvoiceFlag(invoiceFlag);
				if (invoiceFlag && ObjectUtil.isNotEmpty(shopDetail.getInvoiceType())) {
					// 获取商家支持开具的发票类型
					List<String> invoiceTypeList = JSONUtil.toList(JSONUtil.parseArray(shopDetail.getInvoiceType()), String.class);
					shop.setInvoiceTypeList(invoiceTypeList);

					// 根据商家支持开具的发票类型获取默认或最新一条发票信息
					UserInvoiceForOrderDTO invoiceForOrderDTO = new UserInvoiceForOrderDTO();
					invoiceForOrderDTO.setUserId(confirmOrderBo.getUserId());
					invoiceForOrderDTO.setInvoiceTypeList(invoiceTypeList);
					R<UserInvoiceBO> userInvoiceForOrder = this.userInvoiceApi.getUserInvoiceForOrder(invoiceForOrderDTO);
					if (!userInvoiceForOrder.getSuccess()) {
						throw new BusinessException(userInvoiceForOrder.getMsg());
					}
					shop.setUserInvoiceBo(userInvoiceForOrder.getData());
				}
			}
		});
		log.info("###### 发票信息处理完成 ##### ");
		return R.ok(confirmOrderBo);
	}
}
