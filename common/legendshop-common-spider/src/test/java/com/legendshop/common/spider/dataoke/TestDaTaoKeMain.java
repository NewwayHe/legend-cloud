/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.spider.dataoke;


import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.legendshop.common.spider.core.DomMapper;

import java.util.List;

/**
 * 大淘客的爬虫test
 *
 * @author legendshop
 */
public class TestDaTaoKeMain {

	public static void main(String[] args) {
		// 同步，异常返回 null
		HttpResponse httpResponse = HttpUtil.createGet("https://www.dataoke.com/top_sell?type=1&is_new=0").execute();
		DaTaoKeTopSell daTaoKeTopSell = DomMapper.readValue(httpResponse, DaTaoKeTopSell.class);
		System.out.println("大淘客商品获取实时热销榜单");
		if (daTaoKeTopSell == null) {
			System.out.println("暂无商品");
			return;
		}

		List<DaTaoKeInfo> goodListList = daTaoKeTopSell.getGoodListList();
		String title = daTaoKeTopSell.getTitle();
		System.out.println("网页标题：" + title);
		System.out.println("热门商品");
		for (DaTaoKeInfo vBlog : goodListList) {
			System.out.println("商品标题:\t" + vBlog.getTitle());
			System.out.println("近2小时销售:" + vBlog.getCount() + "件");
		}
	}
}
