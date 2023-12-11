/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.sankuai.inf.leaf.snowflake;

import cn.legendshop.jpaplus.model.Result;
import com.sankuai.inf.leaf.IDGen;
import com.sankuai.inf.leaf.common.PropertyFactory;
import org.junit.Test;

import java.util.Properties;

/**
 * @author legendshop
 */
public class SnowflakeIDGenImplTest {
	@Test
	public void testGetId() {
		Properties properties = PropertyFactory.getProperties();

		IDGen idGen = new SnowflakeIDGenImpl(properties.getProperty("leaf.zk.list"), 8080);
		for (int i = 1; i < 1000; ++i) {
			Result r = idGen.get("a");
			System.out.println(r);
		}
	}

}
