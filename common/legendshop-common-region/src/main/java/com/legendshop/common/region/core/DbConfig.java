/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.region.core;

import com.legendshop.common.region.exception.DbMakerConfigException;

/**
 * @author legendshop
 */
public class DbConfig {
	/**
	 * total header data block size
	 */
	private int totalHeaderSize;

	/**
	 * max index data block size
	 * u should always choice the fastest read block size
	 */
	private int indexBlockSize;

	/**
	 * construct method
	 *
	 * @param totalHeaderSize totalHeaderSize
	 * @throws DbMakerConfigException
	 */
	public DbConfig(int totalHeaderSize) throws DbMakerConfigException {
		if ((totalHeaderSize % 8) != 0) {
			throw new DbMakerConfigException("totalHeaderSize must be times of 8");
		}
		this.totalHeaderSize = totalHeaderSize;
		//4 * 1024
		this.indexBlockSize = 4096;
	}

	public DbConfig() throws DbMakerConfigException {
		this(8192);
	}

	public int getTotalHeaderSize() {
		return totalHeaderSize;
	}

	public DbConfig setTotalHeaderSize(int totalHeaderSize) {
		this.totalHeaderSize = totalHeaderSize;
		return this;
	}

	public int getIndexBlockSize() {
		return indexBlockSize;
	}

	public DbConfig setIndexBlockSize(int dataBlockSize) {
		this.indexBlockSize = dataBlockSize;
		return this;
	}
}
