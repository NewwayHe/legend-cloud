/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.activity.dto;

import com.legendshop.product.dto.ProductItemDTO;
import lombok.Data;

/**
 * @author legendshop
 */
@Data
public class ProdCouponItemDTO extends ProductItemDTO {


	private static final long serialVersionUID = 1220940213513040242L;

	private CouponItemDTO shopCoupon;

	private CouponItemDTO prodCoupon;

}
