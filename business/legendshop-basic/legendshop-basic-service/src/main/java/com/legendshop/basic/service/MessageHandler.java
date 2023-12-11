/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.service;

import com.legendshop.basic.dto.MsgSendDTO;
import com.legendshop.common.core.config.sys.params.MsgSendConfig;

/**
 * 消息处理者
 *
 * @author legendshop
 */
public interface MessageHandler {

	/**
	 * 处理消息
	 *
	 * @param msgSendDTO
	 * @param msgSendConfig
	 */
	void handleMessage(MsgSendDTO msgSendDTO, MsgSendConfig msgSendConfig);

}
