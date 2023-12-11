/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.region.service.impl;

import com.legendshop.common.region.core.DBReader;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * 文件读取类
 *
 * @author legendshop
 */
public class RandomAccessFileDBReader implements DBReader {
	protected RandomAccessFile raf;

	public RandomAccessFileDBReader(RandomAccessFile raf) {
		this.raf = raf;
	}

	@Override
	public byte[] full() throws IOException {
		byte[] buf = new byte[(int) raf.length()];
		raf.readFully(buf, 0, buf.length);
		return buf;
	}

	@Override
	public void readFully(long pos, byte[] buf, int offset, int length) throws IOException {
		raf.seek(pos);
		raf.readFully(buf, offset, length);
	}

	@Override
	public void close() throws IOException {
		raf.close();
	}
}
