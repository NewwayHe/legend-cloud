/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.service;

import com.legendshop.common.core.service.BaseService;
import com.legendshop.product.dto.TransRuleDTO;

/**
 * 店铺运费规则(TransRule)表服务接口
 *
 * @author legendshop
 * @since 2020-09-08 17:00:51
 */
public interface TransRuleService extends BaseService<TransRuleDTO> {


	/**
	 * 根据店铺id获取
	 *
	 * @param shopId
	 * @return
	 */
	TransRuleDTO getByShopId(Long shopId);
}
