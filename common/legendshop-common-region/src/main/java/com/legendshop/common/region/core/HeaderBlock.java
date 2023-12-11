/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.region.core;

import com.legendshop.common.region.util.RegionUtil;

/**
 * 头部块
 *
 * @author legendshop
 */
public class HeaderBlock {
	/**
	 * index block start ip address
	 */
	private long indexStartIp;

	/**
	 * ip address
	 */
	private int indexPtr;

	public HeaderBlock(long indexStartIp, int indexPtr) {
		this.indexStartIp = indexStartIp;
		this.indexPtr = indexPtr;
	}

	public long getIndexStartIp() {
		return indexStartIp;
	}

	public HeaderBlock setIndexStartIp(long indexStartIp) {
		this.indexStartIp = indexStartIp;
		return this;
	}

	public int getIndexPtr() {
		return indexPtr;
	}

	public HeaderBlock setIndexPtr(int indexPtr) {
		this.indexPtr = indexPtr;
		return this;
	}

	/**
	 * get the bytes for db storage
	 *
	 * @return byte[]
	 */
	public byte[] getBytes() {
		/*
		 * +------------+-----------+
		 * | 4bytes        | 4bytes    |
		 * +------------+-----------+
		 *  start ip      index ptr
		 */
		byte[] b = new byte[8];

		RegionUtil.writeIntLong(b, 0, indexStartIp);
		RegionUtil.writeIntLong(b, 4, indexPtr);

		return b;
	}
}
