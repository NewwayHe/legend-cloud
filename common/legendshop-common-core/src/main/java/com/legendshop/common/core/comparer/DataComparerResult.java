/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.core.comparer;


import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 对比的结果的封装类
 *
 * @author legendshop
 */
public class DataComparerResult<D> implements Serializable {

	final Map<Integer, List<D>> resultMap;

	public DataComparerResult(Map<Integer, List<D>> resultMap) {
		Assert.notNull(resultMap, "resultMap can not empty");
		this.resultMap = resultMap;
	}


	/**
	 * 获取删除的数据
	 *
	 * @return
	 */
	public List<D> getDelList() {
		return resultMap.get(-1);
	}

	/**
	 * 获取增加的对象
	 *
	 * @return
	 */
	public List<D> getAddList() {
		return resultMap.get(1);
	}

	/**
	 * 获取更新的对象列表
	 *
	 * @return
	 */
	public List<D> getUpdateList() {
		return resultMap.get(0);
	}

}
