/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.excel.util;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import lombok.experimental.UtilityClass;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

/**
 * 读取excel的工具类
 *
 * @author legendshop
 */
@UtilityClass
public class ExcelReadUtil {

	/**
	 * 默认大小
	 */
	private final Integer SIZE = 10;

	/**
	 * 制定阈值读取
	 *
	 * @param consumer
	 * @param threshold
	 * @param <T>
	 * @return
	 */
	public <T> AnalysisEventListener<T> getListener(Consumer<List<T>> consumer, Integer threshold) {

		return new AnalysisEventListener<T>() {
			private LinkedList<T> linkedList = new LinkedList<T>();

			@Override
			public void invoke(T t, AnalysisContext context) {
				linkedList.add(t);
				if (linkedList.size() == threshold) {
					consumer.accept(linkedList);
					linkedList.clear();
				}
			}

			@Override
			public void doAfterAllAnalysed(AnalysisContext context) {
				if (linkedList.size() > 0) {
					consumer.accept(linkedList);
				}
			}
		};
	}

	/**
	 * 不指定阈值，阈值为10
	 *
	 * @param consumer
	 * @param <T>
	 * @return
	 */
	public <T> AnalysisEventListener<T> getListener(Consumer<List<T>> consumer) {
		return getListener(consumer, SIZE);
	}
}
