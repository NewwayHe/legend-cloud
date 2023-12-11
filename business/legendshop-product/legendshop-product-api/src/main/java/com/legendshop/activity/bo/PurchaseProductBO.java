/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.activity.bo;

import lombok.Data;

import java.util.Date;

/**
 * @author legendshop
 */
@Data
public class PurchaseProductBO {

	private Long productId;

	private String productName;

	private String brief;

	private String pic;

	private Double cash;

	private Double discountPrice;

	private Date startTime;

	private Date endTime;
}
