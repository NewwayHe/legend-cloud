/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.log.event;

import org.springframework.context.ApplicationEvent;

/**
 * ID序列初始化 事件
 *
 * @author legendshop
 */
public class IdSeqInitEvent extends ApplicationEvent {

	public IdSeqInitEvent(Object source) {
		super(source);
	}

}
