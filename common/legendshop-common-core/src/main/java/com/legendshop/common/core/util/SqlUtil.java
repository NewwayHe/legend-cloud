/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.core.util;

import lombok.experimental.UtilityClass;

/**
 * sql工具类
 *
 * @author legendshop
 */
@UtilityClass
public class SqlUtil {


	/**
	 * 判断数据库操作是否成功
	 *
	 * @param result 数据库操作返回影响条数
	 * @return boolean
	 */
	public boolean retBool(int result) {
		return result >= 1;
	}


}
