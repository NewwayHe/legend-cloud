/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.region.exception;

/**
 * db文件配置异常
 *
 * @author legendshop
 */
public class DbMakerConfigException extends Exception {

	private static final long serialVersionUID = 2432085687211427593L;

	public DbMakerConfigException(String info) {
		super(info);
	}

	public DbMakerConfigException(Throwable res) {
		super(res);
	}

	public DbMakerConfigException(String info, Throwable res) {
		super(info, res);
	}
}
