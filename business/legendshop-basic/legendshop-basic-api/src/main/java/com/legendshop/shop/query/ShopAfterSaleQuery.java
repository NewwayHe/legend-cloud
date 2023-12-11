/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.query;

import cn.legendshop.jpaplus.support.PageParams;
import lombok.Getter;
import lombok.Setter;

/**
 * @author legendshop
 */
@Getter
@Setter
public class ShopAfterSaleQuery extends PageParams {

	private static final long serialVersionUID = 2444712037552325484L;

	private Long userId;

	private String userName;

}
