/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.search.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author legendshop
 */
@Data
@Schema(description = "商品详情BO")
public class ProductDocumentBO implements Serializable {

	@Schema(description = "商品索引信息")
	private ProductDocumentDTO productDocumentDTO;

	@Schema(description = "店铺索引信息")
	private ShopDocumentDTO shopDocumentDTO;

	@Schema(description = "优惠券索引信息")
	private List<CouponDocumentDTO> couponDocumentDTOList;
}
