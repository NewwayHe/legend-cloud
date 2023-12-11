/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.service;

import com.legendshop.common.core.constant.R;

/**
 * 更新地址库服务
 *
 * @author legendshop
 */
public interface UpdateAddressService {

	/**
	 * 更新地址库
	 *
	 * @param path 地址库json文件路径
	 * @return
	 */
	R updateAddress(String path);
}
