/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.service.test;
//
//import cn.hutool.core.date.DateField;
//import cn.hutool.core.date.DateTime;
//import cn.hutool.core.date.DateUnit;
//import cn.hutool.core.date.DateUtil;
//import com.legendshop.product.dto.*;
//import com.legendshop.product.service.ProductCountService;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.collections.bag.SynchronizedSortedBag;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import java.util.Date;
//import java.util.List;
//
//@SpringBootTest
//@Slf4j
//@EnableAutoConfiguration
//@RunWith(SpringRunner.class)
//public class ProductCountTest {
//
////	@Autowired
////	private ProductCountService productCountService;
//
//
//	@Test
//	public void test1() throws Exception {
//
//
////		List<ProductDataCategoryDTO> list = productCountService.getCategoryTree();
////		list.forEach(a -> System.out.println(a));
//
////		ProductDataSpuDTO x = productCountService.getProductDataSpuCount();
////		ProductDataSkuDTO y = productCountService.getProductDataSkuCount();
////		ProductDataSkuOnSaleDTO z = productCountService.getProductDataSkuOnSaleCount();
////		ProductDataSpuClickDTO v = productCountService.getProductDataSpuClickCount();
////
////
////		System.out.println(x+"==="+y+"==="+z+"==="+v);
////
////		List<ProductDataSkuByCategoryDTO> list = productCountService.getProductSkuByCategory();
////		list.forEach(a -> System.out.println(a));
//
//
//		Integer num = 2;
//		String now = DateUtil.now();
//		Date date = DateUtil.parse(now);
//
//		Date dateTime1 = DateUtil.beginOfDay(date);
//		Date offset = DateUtil.offset(dateTime1, DateField.MONTH, -num);
//		long between = DateUtil.between(offset, dateTime1, DateUnit.DAY);
//		System.out.println(between);
////
////		System.out.println(offset);
//	}
//}
