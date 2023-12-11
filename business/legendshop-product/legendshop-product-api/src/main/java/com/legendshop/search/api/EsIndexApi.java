/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.search.api;

import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.constant.ServiceNameConstants;
import com.legendshop.search.dto.CouponDocumentDTO;
import com.legendshop.search.dto.ShopDocumentDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author legendshop
 */
@FeignClient(contextId = "EsIndexApi", value = ServiceNameConstants.PRODUCT_SERVICE)
public interface EsIndexApi {

	String PREFIX = ServiceNameConstants.PRODUCT_SERVICE_RPC_PREFIX;

	/**
	 * 根据SKUId和shop查找可领取的店铺发布的优惠券（注意：不包括平台优惠券）
	 *
	 * @param skuIds
	 * @param shopId
	 * @return
	 */
	@GetMapping(PREFIX + "/index/queryCouponBySkuIdsAndShopId")
	List<CouponDocumentDTO> queryCouponBySkuIdsAndShopId(@RequestParam("skuIds") List<Long> skuIds, @RequestParam("shopId") Long shopId);

	/**
	 * 根据店铺ID返回店铺索引
	 *
	 * @param shopId
	 * @return
	 */
	@GetMapping(PREFIX + "/index/getShopById")
	ShopDocumentDTO getShopById(@RequestParam("shopId") Long shopId);


	/**
	 * 按规则重建索引
	 *
	 * @param indexType    IndexTypeEnum.name()
	 * @param targetMethod IndexTargetMethodEnum.getValue()
	 * @param targetType   ProductTargetTypeEnum.getValue()、ActivityEsTypeEnum.getValue()
	 * @param targetId     id，多个时逗号分隔
	 * @return
	 */
	@PostMapping(PREFIX + "/index/reIndex")
	R<Boolean> reIndex(@RequestParam("indexType") String indexType,
					   @RequestParam("targetMethod") Integer targetMethod,
					   @RequestParam(value = "targetType", required = false) Integer targetType,
					   @RequestParam(value = "targetId", required = false) String targetId);


	/**
	 * 根据优惠券ID初始化索引
	 *
	 * @param couponId
	 * @return
	 */
	@PostMapping(PREFIX + "/index/createCouponIndexByCouponId")
	R<Void> createCouponIndexByCouponId(@RequestParam("couponId") Long couponId);

	/**
	 * 根据优惠券ID删除索引
	 *
	 * @param couponId
	 * @return
	 */
	@DeleteMapping(PREFIX + "/index/deleteCouponIndexByCouponId")
	R<Void> deleteCouponIndexByCouponId(@RequestParam("couponId") Long couponId);
}
