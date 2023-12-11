/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.activity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * @author legendshop
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlatFormCouponDTO {

	private List<CouponItemDTO> platformCoupons;

	private Map<Long, ShopCouponDTO> shopCouponDTOMap;
}
