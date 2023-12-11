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

/**
 * @author legendshop
 */
public class ByteArrayDBReader implements DBReader {
	protected byte[] buf;

	protected long pos;

	public ByteArrayDBReader(byte[] buf) {
		this.buf = buf;
	}

	@Override
	public byte[] full() throws IOException {
		return buf;
	}

	@Override
	public void readFully(long pos, byte[] buf, int offset, int length) throws IOException {
		System.arraycopy(this.buf, (int) pos, buf, offset, length);
	}

	@Override
	public void close() throws IOException {
	}
}
