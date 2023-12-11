/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.region.core;

import java.io.IOException;

/**
 * @author legendshop
 */
public interface DBReader {
	byte[] full() throws IOException;

	void readFully(long pos, byte[] buf, int offset, int length) throws IOException;

	void close() throws IOException;
}
