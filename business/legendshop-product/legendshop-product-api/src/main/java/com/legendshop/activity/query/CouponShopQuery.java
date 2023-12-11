/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.activity.query;

import cn.legendshop.jpaplus.support.PageParams;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 优惠券可用店铺列表查询
 *
 * @author legendshop
 * @create: 2021-01-04 15:41
 */
@Data
@Schema(description = "优惠券可用店铺列表查询")
public class CouponShopQuery extends PageParams {

	private static final long serialVersionUID = -7403654972652043318L;

	@Schema(description = "优惠券ID")
	private Long couponId;

	/**
	 * 优惠券使用类型
	 * {@link com.legendshop.activity.enums.CouponUseTypeEnum}
	 */
	@Schema(description = "优惠券使用商品类型：0全场通用，1指定商品使用，-1排除商品使用")
	private Integer useType;
}
