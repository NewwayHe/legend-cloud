/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.sankuai.inf.leaf.common;

/**
 * @author legendshop
 */
public class CheckVO {
	private long timestamp;
	private int workID;

	public CheckVO(long timestamp, int workID) {
		this.timestamp = timestamp;
		this.workID = workID;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public int getWorkID() {
		return workID;
	}

	public void setWorkID(int workID) {
		this.workID = workID;
	}
}
