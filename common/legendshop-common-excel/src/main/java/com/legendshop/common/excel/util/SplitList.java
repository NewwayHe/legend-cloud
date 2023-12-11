/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.excel.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author legendshop
 */
public class SplitList {
	/**
	 * 切割查询的数据
	 *
	 * @param list 需要切割的数据
	 * @param len  按照什么长度切割
	 * @param <T>
	 * @return
	 */
	public static <T> List<List<T>> splitList(List<T> list, int len) {
		if (list == null || list.size() == 0 || len < 1) {
			return null;
		}
		List<List<T>> result = new ArrayList<List<T>>();
		int size = list.size();
		int count = (size + len - 1) / len;
		for (int i = 0; i < count; i++) {
			List<T> subList = list.subList(i * len, (Math.min((i + 1) * len, size)));
			result.add(subList);
		}
		return result;
	}

	/**
	 * 集合平均分组
	 *
	 * @param source 源集合
	 * @param n      分成n个集合
	 * @param <T>    集合类型
	 * @return 平均分组后的集合
	 */
	public static <T> List<List<T>> groupList(List<T> source, int n) {
		if (source == null || source.size() == 0 || n < 1) {
			return null;
		}
		if (source.size() < n) {
			return Collections.singletonList(source);
		}
		List<List<T>> result = new ArrayList<List<T>>();
		int number = source.size() / n;
		int remaider = source.size() % n;
		// 偏移量，每有一个余数分配，就要往右偏移一位
		int offset = 0;
		for (int i = 0; i < n; i++) {
			List<T> list1 = null;
			if (remaider > 0) {
				list1 = source.subList(i * number + offset, (i + 1) * number + offset + 1);
				remaider--;
				offset++;
			} else {
				list1 = source.subList(i * number + offset, (i + 1) * number + offset);
			}
			result.add(list1);
		}
		return result;
	}
}

