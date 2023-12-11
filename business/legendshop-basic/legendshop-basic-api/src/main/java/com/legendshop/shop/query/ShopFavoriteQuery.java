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
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * 店铺收藏
 *
 * @author legendshop
 */
@Data
@Schema(description = "店铺收藏查询参数")
public class ShopFavoriteQuery extends PageParams {

	@Schema(description = "用户id")
	private Long userId;

	@Schema(description = "店铺名称")
	private String shopName;

	@Schema(description = "起始时间")
	private Date startDate;

	@Schema(description = "终止时间")
	private Date endDate;
}
