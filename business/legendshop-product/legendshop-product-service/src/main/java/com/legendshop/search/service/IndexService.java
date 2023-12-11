/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.search.service;


import java.io.IOException;
import java.util.List;

/**
 * @author legendshop
 */
public interface IndexService {

	/**
	 * 支持的索引类型
	 *
	 * @param indexType
	 * @return
	 */
	boolean isSupport(String indexType);

	/**
	 * 获取支持的索引类class
	 *
	 * @return
	 */
	Class<?> getSupportClass();


	/**
	 * 初始化索引
	 * @param targetType
	 * @param targetId
	 * @return
	 * @throws IOException
	 */
	String initIndex(Integer targetType, List<Long> targetId) throws IOException;


	/**
	 * 删除索引
	 * @param targetType
	 * @param targetId
	 * @return
	 * @throws IOException
	 */
	String delIndex(Integer targetType, List<Long> targetId) throws IOException;


	/**
	 * 更新索引
	 * @param targetType
	 * @param targetId
	 * @return
	 */
	String updateIndex(Integer targetType, List<Long> targetId);

}
