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
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author legendshop
 */
public interface IDAllocMapper {

	@Select("SELECT biz_tag, max_id, step, update_time FROM leaf_alloc")
	@Results(value = {
			@Result(column = "biz_tag", property = "key"),
			@Result(column = "max_id", property = "maxId"),
			@Result(column = "step", property = "step"),
			@Result(column = "update_time", property = "updateTime")
	})
	List<LeafAlloc> getAllLeafAllocs();

	@Select("SELECT biz_tag, max_id, step FROM leaf_alloc WHERE biz_tag = #{tag}")
	@Results(value = {
			@Result(column = "biz_tag", property = "key"),
			@Result(column = "max_id", property = "maxId"),
			@Result(column = "step", property = "step")
	})
	LeafAlloc getLeafAlloc(@Param("tag") String tag);

	@Update("UPDATE leaf_alloc SET max_id = max_id + step WHERE biz_tag = #{tag}")
	void updateMaxId(@Param("tag") String tag);

	@Update("UPDATE leaf_alloc SET max_id = max_id + #{step} WHERE biz_tag = #{key}")
	void updateMaxIdByCustomStep(@Param("leafAlloc") LeafAlloc leafAlloc);

	@Select("SELECT biz_tag FROM leaf_alloc")
	List<String> getAllTags();
}
