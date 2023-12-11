/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.monitor.model.dto;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author legendshop
 * @date 2020-09-21 9:26
 **/
@NoArgsConstructor
@Data
public class ConnectionResult {

	@JSONField(name = "ResultCode")
	private int ResultCode;
	@JSONField(name = "Content")
	private List<ContentBean> Content;

	@NoArgsConstructor
	@Data
	public static class ContentBean {

		@JSONField(name = "id")
		private int id;
		@JSONField(name = "connectionId")
		private int connectionId;
		@JSONField(name = "useCount")
		private int useCount;
		@JSONField(name = "lastActiveTime")
		private String lastActiveTime;
		@JSONField(name = "connectTime")
		private String connectTime;
		@JSONField(name = "holdability")
		private int holdability;
		@JSONField(name = "transactionIsolation")
		private int transactionIsolation;
		@JSONField(name = "autoCommit")
		private boolean autoCommit;
		@JSONField(name = "readoOnly")
		private boolean readoOnly;
		@JSONField(name = "keepAliveCheckCount")
		private int keepAliveCheckCount;
		@JSONField(name = "pscache")
		private List<?> pscache;
	}
}
