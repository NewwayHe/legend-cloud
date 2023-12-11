/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 分类导航 推荐内容
 *
 * @author legendshop
 */
@Data
@Schema(description = "分类导航 推荐内容")
public class RecommConDTO implements Serializable {

	/**
	 * 推荐品牌列表
	 */
	@Schema(description = "推荐品牌列表")
	List<RecommBrandDTO> brandList = new ArrayList<>();

	/**
	 * 广告位列表
	 */
	@Schema(description = "广告位列表")
	List<RecommAdvDTO> advList = new ArrayList<>();

}
