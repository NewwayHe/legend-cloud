/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.core.service;


import cn.legendshop.jpaplus.support.PageSupport;

import java.util.List;

/**
 * mapstruct转换基类
 *
 * @param <SOURCE> 源
 * @param <TARGET> 目标
 * @author legendshop
 */
public interface BaseConverter<SOURCE, TARGET> {

	/**
	 * 将源对象转换为目标对象
	 *
	 * @param var1
	 * @return
	 */
	TARGET to(SOURCE var1);

	/**
	 * 将源对象集合转换为目标集合
	 *
	 * @param var1
	 * @return
	 */
	List<TARGET> to(List<SOURCE> var1);


	/**
	 * 将目标对象转换为源对象
	 *
	 * @param var1
	 * @return
	 */
	SOURCE from(TARGET var1);


	/**
	 * 将目标对象集合转换为源对象集合
	 *
	 * @param var1
	 * @return
	 */
	List<SOURCE> from(List<TARGET> var1);


	/**
	 * 将源对象分页转换为目标对象分页
	 *
	 * @param entityPage
	 * @return
	 */
	PageSupport<TARGET> page(PageSupport<SOURCE> entityPage);
}
