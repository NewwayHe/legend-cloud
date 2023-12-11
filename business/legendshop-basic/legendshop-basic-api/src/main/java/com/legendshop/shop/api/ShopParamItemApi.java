/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.api;

import com.legendshop.common.core.constant.ServiceNameConstants;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * 系统主配置服务
 *
 * @author legendshop
 */
@FeignClient(contextId = "shopParamItemApi", value = ServiceNameConstants.BASIC_SERVICE)
public interface ShopParamItemApi {


}
