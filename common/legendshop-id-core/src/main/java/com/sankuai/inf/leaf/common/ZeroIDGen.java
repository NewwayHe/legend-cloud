/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.sankuai.inf.leaf.common;

import cn.legendshop.jpaplus.model.Result;
import cn.legendshop.jpaplus.model.Status;
import com.sankuai.inf.leaf.IDGen;

/**
 * @author legendshop
 */
public class ZeroIDGen implements IDGen {
	@Override
	public Result get(String key) {
		return new Result(0L, Status.SUCCESS);
	}

	@Override
	public boolean init() {
		return true;
	}
}
