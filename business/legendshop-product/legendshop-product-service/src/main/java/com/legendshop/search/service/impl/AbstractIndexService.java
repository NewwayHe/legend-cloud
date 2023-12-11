/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.search.service.impl;

import com.legendshop.search.service.IndexService;

/**
 * @author legendshop
 */
public abstract class AbstractIndexService implements IndexService {

	public Boolean reIndexSuccess(Long reIndexId) {
		return true;
	}

}
