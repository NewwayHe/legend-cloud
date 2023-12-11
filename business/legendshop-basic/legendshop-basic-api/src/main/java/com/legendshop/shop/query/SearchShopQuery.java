/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.query;

import cn.hutool.core.util.ObjectUtil;
import cn.legendshop.jpaplus.support.PageParams;
import com.legendshop.shop.enums.SearchShopSortByEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 店铺搜索参数
 *
 * @author legendshop
 */
@Data
public class SearchShopQuery extends PageParams {

	@Schema(description = "用户Id")
	private Long userId;

	@Schema(description = "店铺Id")
	private Long shopId;

	@Schema(description = "店铺名")
	private String key;


	/**
	 * 排序方式 {@link SearchShopSortByEnum}
	 */
	@Schema(description = "排序方式  默认：distance  评分：credit  销量：buys")
	private String sortBy;

	@Schema(description = "升序降序  降序true  升序 false")
	private Boolean descending;

	public String getSortBy() {
		return ObjectUtil.isNotNull(sortBy) ? sortBy : SearchShopSortByEnum.DISTANCE.getValue();
	}

	public Boolean getDescending() {
		return ObjectUtil.isNotNull(descending) ? descending : Boolean.FALSE;
	}
}
