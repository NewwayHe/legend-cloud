/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.pay.api;

import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.constant.ServiceNameConstants;
import com.legendshop.pay.dto.ShopIncomingDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 进件接口
 *
 * @author legendshop
 * @create: 2021/3/26 10:46
 */
@FeignClient(contextId = "ShopIncomingApi", value = ServiceNameConstants.ORDER_SERVICE)
public interface ShopIncomingApi {

	String PREFIX = ServiceNameConstants.ORDER_SERVICE_RPC_PREFIX;

	/**
	 * 根据店铺ID获取进件信息
	 *
	 * @param shopId
	 * @return
	 */
	@PostMapping(PREFIX + "/shopIncoming/getByShopId")
	R<ShopIncomingDTO> getByShopId(@RequestParam("shopId") Long shopId);
}
