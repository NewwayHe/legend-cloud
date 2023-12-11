/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.sankuai.inf.leaf.segment.dao;

import com.sankuai.inf.leaf.segment.model.LeafAlloc;

import java.util.List;

/**
 * @author legendshop
 */
public interface IDAllocDao {

	List<LeafAlloc> getAllLeafAllocs();

	LeafAlloc updateMaxIdAndGetLeafAlloc(String tag);

	LeafAlloc updateMaxIdByCustomStepAndGetLeafAlloc(LeafAlloc leafAlloc);

	List<String> getAllTags();

}
