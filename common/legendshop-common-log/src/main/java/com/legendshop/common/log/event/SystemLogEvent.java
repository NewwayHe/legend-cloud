/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.log.event;

import com.legendshop.common.core.dto.SystemLogDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author legendshop
 */
@Getter
@AllArgsConstructor
public class SystemLogEvent {

	private final SystemLogDTO systemLogDTO;
}
