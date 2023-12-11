/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.spider.dataoke;

import com.legendshop.common.spider.annotation.HtmlQuery;
import lombok.Data;

import java.util.List;

/**
 * @author legendshop
 */
@Data
public class DaTaoKeTopSell {

	@HtmlQuery(value = "head > title", attr = "text")
	private String title;

	@HtmlQuery(value = ".goods-list div .goods-item-content .goods-info", inner = true) // 标记为嵌套模型
	List<DaTaoKeInfo> goodListList;

}
