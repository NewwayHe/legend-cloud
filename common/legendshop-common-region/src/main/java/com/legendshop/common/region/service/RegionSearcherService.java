/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.region.service;

import com.legendshop.common.region.core.IpInfoDTO;
import org.springframework.lang.Nullable;

/**
 * 地区查找接口
 *
 * @author legendshop
 */
public interface RegionSearcherService {

	/**
	 * 内存搜索
	 *
	 * @param ip ip
	 * @return 位置
	 */
	@Nullable
	IpInfoDTO memorySearch(long ip);

	/**
	 * 内存搜索
	 *
	 * @param ip ip
	 * @return 位置
	 */
	@Nullable
	IpInfoDTO memorySearch(String ip);

	/**
	 * 获取地区索引
	 *
	 * @param ptr ptr
	 * @return 位置
	 */
	@Nullable
	IpInfoDTO getByIndexPtr(long ptr);

	/**
	 * 二叉树搜搜
	 *
	 * @param ip ip
	 * @return 位置
	 */
	@Nullable
	IpInfoDTO btreeSearch(long ip);

	/**
	 * 二叉树搜搜
	 *
	 * @param ip ip
	 * @return 位置
	 */
	@Nullable
	IpInfoDTO btreeSearch(String ip);

	/**
	 * 二叉树搜搜
	 *
	 * @param ip ip
	 * @return 位置
	 */
	@Nullable
	IpInfoDTO binarySearch(long ip);

	/**
	 * 二叉树搜搜
	 *
	 * @param ip ip
	 * @return 位置
	 */
	@Nullable
	IpInfoDTO binarySearch(String ip);
}
