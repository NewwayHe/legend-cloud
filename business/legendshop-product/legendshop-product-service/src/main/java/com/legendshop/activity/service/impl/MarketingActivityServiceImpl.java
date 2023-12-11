/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.activity.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.activity.dto.CouponDTO;
import com.legendshop.activity.dto.MarketingActivityItemDTO;
import com.legendshop.activity.enums.CouponUseTypeEnum;
import com.legendshop.activity.query.CouponQuery;
import com.legendshop.activity.service.CouponService;
import com.legendshop.activity.service.MarketingActivityService;
import com.legendshop.common.core.expetion.BusinessException;
import com.legendshop.product.api.ProductApi;
import com.legendshop.product.api.SkuApi;
import com.legendshop.product.bo.SkuBO;
import com.legendshop.product.dto.ProductDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author legendshop
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MarketingActivityServiceImpl implements MarketingActivityService {

	final ProductApi productApi;
	final SkuApi skuApi;
	private final CouponService couponService;


	/**
	 * 根据商品id获取获取下面所有sku对应的促销信息和优惠劵
	 */
	@Override
	public Map<Long, List<MarketingActivityItemDTO>> getMarketingActivity(Long productId, Long userId) {
		ProductDTO product = productApi.getDtoByProductId(productId).getData();
		if (ObjectUtil.isEmpty(product)) {
			throw new BusinessException("该商品不存在或已被下线");
		}
		Long shopId = product.getShopId();
		List<SkuBO> skuBOS = skuApi.getSkuByProduct(productId).getData();
		if (CollUtil.isEmpty(skuBOS)) {
			throw new BusinessException("该商品不存在或已被下线");
		}
		List<MarketingActivityItemDTO> itemList = new ArrayList<>();

		// 获取所有sku优惠券
		//List<CouponDTO> couponDTOList = couponService.listReceivableES(CouponQuery.builder().skuIds(skuBOS.stream().map(SkuBO::getId).collect(Collectors.toList())).userId(userId).shopId(shopId).build());
		CouponQuery couponQuery = new CouponQuery();
		couponQuery.setShopProviderFlag(Boolean.FALSE);
		PageSupport<CouponDTO> couponAdmin = couponService.queryCouponsByStatus(couponQuery);
		couponQuery.setShopProviderFlag(Boolean.TRUE);
		couponQuery.setShopId(product.getShopId());
		PageSupport<CouponDTO> couponShop = couponService.queryCouponsByStatus(couponQuery);
		List<CouponDTO> couponDTOList = couponShop.getResultList();
		if (ObjectUtil.isNotEmpty(couponAdmin.getResultList())) {
			List<CouponDTO> couponAdminList = couponAdmin.getResultList();
			log.info("couponAdminList{}", couponAdminList);
			for (CouponDTO couponDTO : couponAdminList) {
				couponDTOList.add(couponDTO);
			}

		}

		for (SkuBO skuBO : skuBOS) {
			List<MarketingActivityItemDTO> marketingActivityItemDTOS = new ArrayList<>();
			MarketingActivityItemDTO marketingActivityItemDTO = new MarketingActivityItemDTO();
			//因为只是展示营销活动在商品详情页，并不需要根据数量展示对应折扣价，因此数量默认为1
			marketingActivityItemDTO.setTotalCount(1);
			marketingActivityItemDTO.setPrice(skuBO.getPrice());
			marketingActivityItemDTO.setTotalPrice(skuBO.getPrice());
			marketingActivityItemDTO.setSkuId(skuBO.getId());
			marketingActivityItemDTO.setShopId(shopId);
			marketingActivityItemDTO.setCouponDTOList(couponDTOList.stream()
					.filter(e -> {

						if (e.getShopProviderFlag()) {

							// 通用券直接返回
							if (CouponUseTypeEnum.GENERAL.getValue().equals(e.getUseType())) {
								return Boolean.TRUE;

								// 包含券，则存在才返回
							} else if (CouponUseTypeEnum.INCLUDE.getValue().equals(e.getUseType())) {
								return e.getSkuIdList().contains(skuBO.getSkuId());

								// 排除券，则不存在才返回
							} else if (CouponUseTypeEnum.EXCLUDE.getValue().equals(e.getUseType())) {
								return !e.getSkuIdList().contains(skuBO.getSkuId());
							}
							return Boolean.FALSE;
						} else {
							// 通用券直接返回
							//else
							if (CouponUseTypeEnum.GENERAL.getValue().equals(e.getUseType())) {
								return Boolean.TRUE;
							} else if (CouponUseTypeEnum.INCLUDE.getValue().equals(e.getUseType())) {
								return e.getSelectShopId().contains(product.getShopId());
								// 排除券，则不存在才返回
							} else if (CouponUseTypeEnum.EXCLUDE.getValue().equals(e.getUseType())) {
								return !e.getSelectShopId().contains(product.getShopId());
							}
						}
						return Boolean.FALSE;
					}).collect(Collectors.toList()));
			marketingActivityItemDTOS.add(marketingActivityItemDTO);
			itemList.addAll(marketingActivityItemDTOS);
		}
		//封装营销活动信息
		return itemList.stream().collect(Collectors.groupingBy(MarketingActivityItemDTO::getSkuId));
	}

}
