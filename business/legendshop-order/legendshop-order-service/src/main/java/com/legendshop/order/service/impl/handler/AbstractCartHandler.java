/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.service.impl.handler;


import com.legendshop.common.data.cache.util.CacheManagerUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 购物车抽象类
 * 抽象登录和未登录用户的方法
 *
 * @author legendshop
 */
public abstract class AbstractCartHandler implements CartHandler {

	@Autowired
	public CacheManagerUtil cacheManagerUtil;

	@Resource
	public HttpServletRequest request;

	@Resource
	public HttpServletResponse response;

}
