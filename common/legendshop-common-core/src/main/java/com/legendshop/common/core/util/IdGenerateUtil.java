/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.core.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * 获取服务集群中全局唯一的序列号ID
 * 请在 {@link IdTypeEnum} 中设置使用类型，并赋值一个 5 位以内的不重复数字
 *
 * @author legendshop
 */
public class IdGenerateUtil {

	private IdGenerateUtil() {
	}

	public static Integer workerId = 1;
	public static Integer sequenceId = 1;

	static Map<IdTypeEnum, IdWorker> idWorkerMap = new HashMap<>();

	static {
		idWorkerMap.put(IdTypeEnum.REQUEST_TRACE_ID, new IdWorker(workerId, IdTypeEnum.REQUEST_TRACE_ID.getDatacenterId(), sequenceId));
	}

	public static long nextId(IdTypeEnum type) {
		return idWorkerMap.get(type).nextId();
	}

	public static String nextIdStr(IdTypeEnum type) {
		return idWorkerMap.get(type).nextId() + "";
	}

	public static void setInitSerial(int worker, int sequence) {
		workerId = worker;
		sequenceId = sequence;
		// 刷新map
		idWorkerMap.replaceAll((k, v) -> new IdWorker(workerId, k.getDatacenterId(), sequenceId));
	}

	@Getter
	@AllArgsConstructor
	public enum IdTypeEnum {
		// 请求链路ID
		REQUEST_TRACE_ID(1);

		private final Integer datacenterId;
	}
}
