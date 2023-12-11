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
public class DaTaoKeInfo {


	@HtmlQuery(value = ".tit-text", attr = "title")
	private String title;

	@HtmlQuery(value = ".tg-show p b", attr = "text")
	private Integer count;


}
