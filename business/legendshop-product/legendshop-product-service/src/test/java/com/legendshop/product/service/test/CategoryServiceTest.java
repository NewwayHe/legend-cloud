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
//import cn.hutool.json.JSONUtil;
//import com.legendshop.product.bo.CategoryBO;
//import com.legendshop.product.service.CategoryService;
//import lombok.extern.slf4j.Slf4j;
//import org.junit.Before;
//import org.junit.BeforeClass;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.runner.RunWith;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.context.annotation.ImportResource;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import java.util.ArrayList;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//@EnableAutoConfiguration
//@Slf4j
//class CategoryServiceTest {
//
//	@Autowired
//	private CategoryService categoryService;
//
//	@BeforeEach
//	public void setUp() {
//		// 数据打桩，设置该方法返回的 body一直 是空的
//		System.out.println("before calling");
////		CategoryBO categoryBO = new CategoryBO();
////		categoryBO.setAggName("setAggName");
////		Mockito.when(categoryService.getById(1L)).thenReturn(categoryBO);
//	}
//
//	@Test
//	public void getById() {
//		System.out.println("getById");
//		CategoryBO categoryBO = categoryService.getById(1L);
//		System.out.println("CategoryBO = " + JSONUtil.toJsonStr(categoryBO));
//	}
//
//	@Test
//	public void getCategoryNameById() {
//		String id = categoryService.getCategoryNameById(1L);
//		System.out.println("Id = " + id);
//	}
//
//	@Test
//	public void deleteById() {
//	}
//
//	@Test
//	public void save() {
//	}
//
//	@Test
//	public void update() {
//	}
//
//	@Test
//	void getCategoryByIds() {
//	}
//
//	@Test
//	void updateStatus() {
//	}
//
//	@Test
//	void queryByParentId() {
//	}
//
//	@Test
//	void queryAllOnline() {
//	}
//
//	@Test
//	void filterCategory() {
//	}
//
//	@Test
//	void getDecorateCategoryList() {
//	}
//
//	@Test
//	void getTreeById() {
//	}
//}