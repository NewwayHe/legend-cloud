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

/**
 * @author legendshop
 */
@Data
public class IndexDocumentDTO implements Serializable {

	@Schema(description = "商品索引数量")
	private Long productDocumentCount;

	@Schema(description = "店铺索引数量")
	private Long shopDocumentCount;

	@Schema(description = "优惠券索引数量")
	private Long couponDocumentCount;
}