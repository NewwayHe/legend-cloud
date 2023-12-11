/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.service;

import com.legendshop.common.core.constant.R;
import com.legendshop.product.dto.TransFeeCalProductDetailDTO;
import com.legendshop.product.dto.TransFeeCalculateDTO;
import com.legendshop.product.enums.TransCalFeeResultEnum;

/**
 * 运费计算
 *
 * @author legendshop
 */
public interface TransFeeCalculateService {


	/**
	 * 计算订单中店铺需要的运费 {@link TransCalFeeResultEnum}
	 *
	 * @param calculateDTO
	 * @return
	 */
	R calTransFee(TransFeeCalculateDTO calculateDTO);


	/**
	 * 用户端商品详情页计算运费
	 *
	 * @param calculateDTO
	 * @return
	 */
	R calTransFeeForUser(TransFeeCalProductDetailDTO calculateDTO);
}
