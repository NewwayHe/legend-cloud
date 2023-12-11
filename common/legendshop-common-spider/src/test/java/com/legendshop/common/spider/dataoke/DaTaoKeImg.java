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

/**
 * @author legendshop
 */
@Data
public class DaTaoKeImg {


	@HtmlQuery(value = "a", attr = "href")
	private String href;

	@HtmlQuery(value = "a", attr = "src")
	private String img;

}
