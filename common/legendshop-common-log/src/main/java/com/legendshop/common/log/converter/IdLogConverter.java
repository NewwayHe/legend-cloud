/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.log.converter;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import com.github.yitter.idgen.YitIdHelper;

/**
 * 功能描述： 将ip打印到日志
 *
 * @author legendshop
 * @version 1.0.0
 */
public class IdLogConverter extends ClassicConverter {

	@Override
	public String convert(ILoggingEvent event) {
		return String.valueOf(YitIdHelper.nextId());
	}
}
