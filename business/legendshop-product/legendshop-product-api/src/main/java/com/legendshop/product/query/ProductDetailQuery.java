/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.query;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author legendshop
 */
@Schema(description = "商品详情页查看参数")
@Data
public class ProductDetailQuery {

	private static final long serialVersionUID = -4201363471834565078L;

	@Schema(description = "商品id")
	private Long productId;

	@Schema(description = "活动id 拼团/团购/秒杀活动需传")
	private Long activityId;

	/**
	 * 营销类型 {@link com.legendshop.product.enums.SkuActiveTypeEnum}
	 */
	@Schema(description = "营销类型 积分（INTEGRAL） ")
	private String skuType;

	@Schema(description = "秒杀skuId  秒杀活动需传")
	private Long skuId;

	private Long userId;

	@Schema(description = "token")
	private String token;

	@Schema(description = "查看草稿")
	private Boolean viewDraft;

	public ProductDetailQuery() {
		this.viewDraft = false;
	}
}
