/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.service.impl;

import cn.hutool.json.JSONUtil;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author legendshop
 */
public class SimpleTest {
	public static void main(String[] args) {
		List<Long> productIds = new ArrayList<>();

		productIds.add(5601L);
		productIds.add(4801L);
		productIds.add(5958L);
		productIds.add(5101L);
		productIds.add(5451L);
		System.out.println(productIds.indexOf(5451L));

		List<Long> productIds2 = new ArrayList<>();

		productIds2.add(5958L);
		productIds2.add(5451L);
		productIds2.add(5601L);
		productIds2.add(4801L);
		productIds2.add(5101L);
		System.out.println(productIds2.indexOf(5451L));

		List<Long> collect = productIds2.stream().sorted(Comparator.comparingInt(productIds::indexOf)).collect(Collectors.toList());
		System.out.println(JSONUtil.toJsonStr(productIds));
		System.out.println(JSONUtil.toJsonStr(productIds2));
		System.out.println(JSONUtil.toJsonStr(collect));
		System.out.println(collect.indexOf(5451L));
	}
}
